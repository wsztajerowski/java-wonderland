package pl.symentis.services;

import org.jetbrains.annotations.NotNull;
import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jmh.BenchmarkMetadata;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.Files.list;
import static java.text.MessageFormat.format;
import static pl.symentis.FileUtils.getFilenameWithoutExtension;
import static pl.symentis.infra.MorphiaService.getMorphiaService;
import static pl.symentis.infra.ResultLoaderService.getResultLoaderService;
import static pl.symentis.infra.S3Service.getS3Service;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

public class JmhWithAsyncProfilerSubcommandService {

    private final String benchmarkPath;
    private final int forks;
    private final int iterations;
    private final int warmupIterations;
    private final String testNameRegex;
    private final String commitSha;
    private final int runAttempt;
    private final String asyncPath;
    private final int interval;
    private final String output;
    private final String s3Prefix;

    JmhWithAsyncProfilerSubcommandService(String benchmarkPath, int forks, int iterations, int warmupIterations, String testNameRegex, String commitSha, int runAttempt, String asyncPath, int interval, String output) {
        this.benchmarkPath = benchmarkPath;
        this.forks = forks;
        this.iterations = iterations;
        this.warmupIterations = warmupIterations;
        this.testNameRegex = testNameRegex;
        this.commitSha = commitSha;
        this.runAttempt = runAttempt;
        this.asyncPath = asyncPath;
        this.interval = interval;
        this.output = output;
        s3Prefix = createS3PathPrefix(commitSha, runAttempt);
    }

    public void executeCommand() {
        // Build process
        try {
            int exitCode = benchmarkProcessBuilder(benchmarkPath)
                .addArgumentWithValue("-f", forks)
                .addArgumentWithValue("-i", iterations)
                .addArgumentWithValue("-wi", warmupIterations)
                .addArgumentWithValue("-rf", "json")
                .addArgumentWithValue("-prof", createAsyncCommand())
                .addOptionalArgument(testNameRegex)
                .buildAndStartProcess()
                .waitFor();
            if (exitCode != 0) {
                throw new JavaWonderlandException(format("Benchmark process exit with non-zero code: {0}", exitCode));
            }
        } catch (InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        for (JmhResult jmhResult : getResultLoaderService().loadJmhResults()) {
            Map<String, String> flamegraphs = new HashMap<>();
            String flamegraphsDir = jmhResult.benchmark() + getFlamegraphsDirSuffix(jmhResult.mode());
            try (Stream<Path> paths = list(Path.of(flamegraphsDir))) {
                paths
                    .forEach(path -> {
                        String s3Key = s3Prefix + path.toString();
                        getS3Service()
                            .saveFileOnS3(s3Key, path);
                        String flamegraphName = getFilenameWithoutExtension(path);
                        flamegraphs.put(flamegraphName, s3Key);
                    });
            } catch (IOException e) {
                throw new JavaWonderlandException(e);
            }

            getMorphiaService()
                .upsert(JmhBenchmark.class)
                .byFieldValue("benchmarkId", new JmhBenchmarkId(
                    commitSha,
                    jmhResult.benchmark(),
                    jmhResult.mode(),
                    runAttempt))
                .setValue("benchmarkMetadata", new BenchmarkMetadata(flamegraphs));
        }
    }

    @NotNull
    private String createS3PathPrefix(String sha, int runNo) {
        return format("gha-outputs/commit-{0}/attempt-{1}/jmh/", sha, runNo);
    }


    @NotNull
    private static String getFlamegraphsDirSuffix(String mode) {
        return switch (mode) {
            case "thrpt":
                yield "-Throughput";
            default:
                throw new IllegalArgumentException("Unknown benchmark mode: " + mode);
        };
    }

    private String createAsyncCommand() {
        return "async:libPath=%s;output=%s;interval=%d".formatted(asyncPath, output, interval);
    }
}
