package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhWithAsyncProfilerSubcommandServiceBuilder {
    private String asyncPath;
    private int asyncInterval;
    private String asyncOutputType;
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;
    private S3Service s3Service;
    private URI mongoConnectionString;
    private Path outputPath;
    private Path resultsPath;
    private Path asyncOutputPath;

    private JmhWithAsyncProfilerSubcommandServiceBuilder() {
        s3Service = S3ServiceBuilder.getS3ServiceBuilder().build();
        asyncOutputPath = Path.of("async-results");
        outputPath = Path.of("output.txt");
        resultsPath = Path.of(JmhBenchmarksSharedOptions.JMH_RESULT_FILENAME);
    }

    public static JmhWithAsyncProfilerSubcommandServiceBuilder getJmhWithAsyncProfilerSubcommandService() {
        return new JmhWithAsyncProfilerSubcommandServiceBuilder();
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withJmhOptions(JmhBenchmarksSharedOptions sharedOptions) {
        this.jmhBenchmarksOptions = sharedOptions;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withAsyncPath(String asyncPath) {
        this.asyncPath = asyncPath;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withAsyncInterval(int interval) {
        this.asyncInterval = interval;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withAsyncOutputType(String outputType) {
        this.asyncOutputType = outputType;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withAsyncOutputPath(Path asyncOutputPath) {
        this.asyncOutputPath = asyncOutputPath;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withS3Service(S3Service s3Service){
        this.s3Service = s3Service;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withMongoConnectionString(URI mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withOutputPath(Path outputPath) {
        Objects.requireNonNull(outputPath, "Provide non-null path as JMH output file!");
        this.outputPath = outputPath;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withResultsPath(Path resultsPath) {
        Objects.requireNonNull(resultsPath, "Provide non-null path as JMH results file!");
        this.resultsPath = resultsPath.toAbsolutePath();
        return this;
    }

    public JmhWithAsyncProfilerSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhWithAsyncProfilerSubcommandService(
            s3Service,
            morphiaService,
            commonOptions,
            jmhBenchmarksOptions,
            asyncPath,
            asyncInterval,
            asyncOutputType,
            asyncOutputPath,
            outputPath,
            resultsPath);
    }
}
