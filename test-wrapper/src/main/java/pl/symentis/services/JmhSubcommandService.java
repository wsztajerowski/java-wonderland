package pl.symentis.services;

import pl.symentis.FileUtils;
import pl.symentis.JavaWonderlandException;
import pl.symentis.entities.jmh.JmhBenchmark;
import pl.symentis.entities.jmh.JmhBenchmarkId;
import pl.symentis.entities.jmh.JmhResult;
import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;

import java.nio.file.Path;

import static java.text.MessageFormat.format;
import static pl.symentis.infra.ResultLoaderService.getResultLoaderService;
import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;
import static pl.symentis.services.S3PrefixProvider.jmhS3Prefix;

public class JmhSubcommandService {

    private final CommonSharedOptions commonOptions;
    private final JmhBenchmarksSharedOptions jmhBenchmarksOptions;
    private final S3Service s3Service;
    private final MorphiaService morphiaService;
    private final Path outputPath;
    private final Path jmhResultFilePath;

    JmhSubcommandService(S3Service s3Service, MorphiaService morphiaService, CommonSharedOptions commonOptions, JmhBenchmarksSharedOptions jmhBenchmarksOptions, Path outputPath, Path jmhResultFilePath) {
        this.s3Service = s3Service;
        this.morphiaService = morphiaService;
        this.commonOptions = commonOptions;
        this.jmhBenchmarksOptions = jmhBenchmarksOptions;
        this.outputPath = outputPath;
        this.jmhResultFilePath = jmhResultFilePath;
    }

    public void executeCommand() {
        try {
            FileUtils.ensurePathExists(jmhResultFilePath);
            int exitCode = benchmarkProcessBuilder(jmhBenchmarksOptions.benchmarkPath())
                .addArgumentWithValue("-f", jmhBenchmarksOptions.forks())
                .addArgumentWithValue("-i", jmhBenchmarksOptions.iterations())
                .addArgumentWithValue("-wi", jmhBenchmarksOptions.warmupIterations())
                .addArgumentWithValue("-rf", "json")
                .addArgumentIfValueIsNotNull("-rff", jmhResultFilePath)
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
            JmhBenchmarkId benchmarkId = new JmhBenchmarkId(
                commonOptions.commitSha(),
                jmhResult.benchmark(),
                jmhResult.mode(),
                commonOptions.runAttempt());
            morphiaService
                .upsert(JmhBenchmark.class)
                .byFieldValue("benchmarkId", benchmarkId)
                .setValue("jmhResult", jmhResult)
                .execute();
        }

        Path s3Prefix = jmhS3Prefix(commonOptions.commitSha(), commonOptions.runAttempt());
        s3Service
            .saveFileOnS3(s3Prefix.resolve("output.txt").toString(), outputPath);
    }
}
