package pl.symentis.commands;

import dev.morphia.UpdateOptions;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
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

import static dev.morphia.query.filters.Filters.eq;
import static dev.morphia.query.updates.UpdateOperators.set;
import static java.nio.file.Files.list;
import static java.text.MessageFormat.format;
import static pl.symentis.FileUtils.getFilenameWithoutExtension;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.services.MorphiaService.getMorphiaService;
import static pl.symentis.services.ResultLoaderService.getResultLoaderService;
import static pl.symentis.services.S3Service.getS3Service;

@Command(name = "jmh-with-async", description = "Run JHM benchmarks with Async profiler")
public class JmhWithAsyncProfilerSubcommand implements Runnable {
    @CommandLine.Mixin
    private JmhBenchmarksSharedOptions sharedJmhOptions;

    @CommandLine.Mixin
    private CommonSharedOptions commonSharedOptions;

    @Option(names = "--async-path", defaultValue = "${ASYNC_PATH:-/home/ec2-user/async-profiler/build/libasyncProfiler.so}", description = "Path to Async profiler (default: ${DEFAULT-VALUE})")
    String asyncPath;

    @Option(names = {"-ai", "--async-interval"}, description = "Profiling interval (default: ${DEFAULT-VALUE})")
    int interval = 999;

    @Option(names = {"-ao", "--async-output"}, description = "Output format(s). Supported: [text, collapsed, flamegraph, tree, jfr] (default: ${DEFAULT-VALUE})")
    String output = "flamegraph";

    @Override
    public void run() {
        String s3Prefix = createS3PathPrefix();
        // Build process
        try {
            benchmarkProcessBuilder(sharedJmhOptions.benchmarkPath)
                .addArgumentWithValue("-f", sharedJmhOptions.forks)
                .addArgumentWithValue("-i", sharedJmhOptions.iterations)
                .addArgumentWithValue("-wi", sharedJmhOptions.warmupIterations)
                .addArgumentWithValue("-rf", "json")
                .addArgumentWithValue("-prof", createAsyncCommand())
                .addOptionalArgument(commonSharedOptions.testNameRegex)
                .buildAndStartProcess()
                .waitFor();
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

            JmhBenchmarkId benchmarkId = new JmhBenchmarkId(
                commonSharedOptions.commitSha,
                jmhResult.benchmark(),
                jmhResult.mode(),
                commonSharedOptions.runAttempt);
            BenchmarkMetadata benchmarkMetadata = new BenchmarkMetadata(flamegraphs);
            getMorphiaService()
                .getTestResultsDatastore()
                .find(JmhBenchmark.class)
                .filter(eq("benchmarkId", benchmarkId))
                .update(new UpdateOptions().upsert(true),
                    set("benchmarkMetadata", benchmarkMetadata));
        }
    }

    @NotNull
    private String createS3PathPrefix() {
        return format("gha-outputs/commit-{0}/attempt-{1}/jmh/", commonSharedOptions.commitSha, commonSharedOptions.runAttempt);
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
