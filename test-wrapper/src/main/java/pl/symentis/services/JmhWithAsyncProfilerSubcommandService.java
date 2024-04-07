package pl.symentis.services;

import org.jetbrains.annotations.NotNull;
import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jmh.BenchmarkMetadata;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;
import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.Files.list;
import static java.text.MessageFormat.format;
import static pl.symentis.FileUtils.ensurePathExists;
import static pl.symentis.FileUtils.getFilenameWithoutExtension;
import static pl.symentis.infra.ResultLoaderService.getResultLoaderService;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.services.S3PrefixProvider.jmhWithAsyncS3Prefix;

public class JmhWithAsyncProfilerSubcommandService {

    private final CommonSharedOptions commonOptions;
    private final JmhBenchmarksSharedOptions jmhBenchmarksSharedOptions;
    private final S3Service s3Service;
    private final MorphiaService morphiaService;
    private final String asyncPath;
    private final int asyncInterval;
    private final String asyncOutputType;
    private final Path asyncOutputPath;
    private final Path s3Prefix;
    private final Path outputPath;
    private final Path jmhResultFilePath;

    JmhWithAsyncProfilerSubcommandService(S3Service s3Service, MorphiaService morphiaService, CommonSharedOptions commonOptions, JmhBenchmarksSharedOptions jmhBenchmarksSharedOptions, String asyncPath, int asyncInterval, String asyncOutputType, Path asyncOutputPath, Path outputPath, Path jmhResultFilePath) {
        this.s3Service = s3Service;
        this.morphiaService = morphiaService;
        this.commonOptions = commonOptions;
        this.jmhBenchmarksSharedOptions = jmhBenchmarksSharedOptions;
        this.asyncPath = asyncPath;
        this.asyncInterval = asyncInterval;
        this.asyncOutputType = asyncOutputType;
        this.asyncOutputPath = asyncOutputPath;
        this.outputPath = outputPath;
        this.jmhResultFilePath = jmhResultFilePath;
        this.s3Prefix = jmhWithAsyncS3Prefix(commonOptions.commitSha(), commonOptions.runAttempt());
    }

    public void executeCommand() {
        // Build process
        try {
            ensurePathExists(jmhResultFilePath);
            int exitCode = benchmarkProcessBuilder(jmhBenchmarksSharedOptions.benchmarkPath())
                .addArgumentWithValue("-f", jmhBenchmarksSharedOptions.forks())
                .addArgumentWithValue("-i", jmhBenchmarksSharedOptions.iterations())
                .addArgumentWithValue("-wi", jmhBenchmarksSharedOptions.warmupIterations())
                .addArgumentWithValue("-rf", "json")
                .addArgumentIfValueIsNotNull("-rff", jmhResultFilePath)
                .addArgumentWithValue("-prof", createAsyncCommand())
                .addArgumentIfValueIsNotNull("-jvmArgs", commonOptions.jvmArgs())
                .addOptionalArgument(commonOptions.testNameRegex())
                .withOutputPath(outputPath)
                .buildAndStartProcess()
                .waitFor();
            if (exitCode != 0) {
                throw new JavaWonderlandException(format("Benchmark process exit with non-zero code: {0}", exitCode));
            }
        } catch (InterruptedException e) {
            throw new JavaWonderlandException(e);
        }

        for (JmhResult jmhResult : getResultLoaderService().loadJmhResults(jmhResultFilePath)) {
            Map<String, String> flamegraphs = new HashMap<>();
            String benchmarkFullname = jmhResult.benchmark() + getFlamegraphsDirSuffix(jmhResult.mode());
            Path flamegraphsDir = asyncOutputPath.resolve(benchmarkFullname);
            try (Stream<Path> paths = list(flamegraphsDir)) {
                paths
                    .forEach(path -> {
                        String s3Key = s3Prefix.resolve(benchmarkFullname).resolve(path.getFileName()).toString();
                        s3Service
                            .saveFileOnS3(s3Key, path);
                        String flamegraphName = getFilenameWithoutExtension(path);
                        flamegraphs.put(flamegraphName, s3Key);
                    });
            } catch (IOException e) {
                throw new JavaWonderlandException(e);
            }

            morphiaService
                .upsert(JmhBenchmark.class)
                .byFieldValue("benchmarkId", new JmhBenchmarkId(
                    commonOptions.commitSha(),
                    jmhResult.benchmark(),
                    jmhResult.mode(),
                    commonOptions.runAttempt()))
                .setValue("benchmarkMetadata", new BenchmarkMetadata(flamegraphs))
                .setValue("jmhWithAsyncResult", jmhResult)
                .execute();
        }

        s3Service
            .saveFileOnS3(s3Prefix.resolve("output.txt").toString(), outputPath);

        try (Stream<Path> paths = list(asyncOutputPath)){
            paths
                .filter(f -> f.toString().endsWith("log"))
                .forEach(path -> {
                    String s3Key = s3Prefix.resolve("logs").resolve(path.getFileName()).toString();
                    s3Service
                        .saveFileOnS3(s3Key, path);
                });
        } catch (IOException e) {
            throw new JavaWonderlandException(e);
        }
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
        return "async:libPath=%s;output=%s;dir=%s;interval=%d".formatted(asyncPath, asyncOutputType, asyncOutputPath.toAbsolutePath(), asyncInterval);
    }
}
