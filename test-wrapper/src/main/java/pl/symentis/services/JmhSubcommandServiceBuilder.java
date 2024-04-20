package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhSubcommandServiceBuilder {
    private S3Service s3Service;
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;
    private URI mongoConnectionString;
    private Path outputPath;

    private JmhSubcommandServiceBuilder() {
        s3Service = S3ServiceBuilder.getDefaultS3ServiceBuilder().build();
        outputPath = Path.of("output.txt");
    }

    public static JmhSubcommandServiceBuilder getJmhSubcommandService() {
        return new JmhSubcommandServiceBuilder();
    }

    public JmhSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JmhSubcommandServiceBuilder withJmhOptions(JmhBenchmarksSharedOptions sharedOptions) {
        this.jmhBenchmarksOptions = sharedOptions;
        return this;
    }

    public JmhSubcommandServiceBuilder withMongoConnectionString(URI mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JmhSubcommandServiceBuilder withS3Service(S3Service s3Service){
        this.s3Service = s3Service;
        return this;
    }

    public JmhSubcommandServiceBuilder withOutputPath(Path outputPath) {
        Objects.requireNonNull(outputPath, "Provide non-null path as JMH output file!");
        this.outputPath = outputPath;
        return this;
    }

    public JmhSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhSubcommandService(s3Service, morphiaService, commonOptions, jmhBenchmarksOptions, outputPath);
    }
}
