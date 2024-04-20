package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;
import pl.symentis.services.options.CommonSharedOptions;
import pl.symentis.services.options.JCStressOptions;

import java.net.URI;
import java.nio.file.Path;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JCStressSubcommandServiceBuilder {
    private CommonSharedOptions commonOptions;
    private S3Service s3Service;
    private URI mongoConnectionString;
    private Path benchmarkPath;
    private JCStressOptions jcStressOptions;

    private JCStressSubcommandServiceBuilder() {
    }

    public static JCStressSubcommandServiceBuilder serviceBuilder() {
        return new JCStressSubcommandServiceBuilder();
    }

    public static JCStressSubcommandServiceBuilder serviceBuilderWithDefaultS3Service() {
        return new JCStressSubcommandServiceBuilder()
            .withS3Service(S3ServiceBuilder.getDefaultS3ServiceBuilder().build());
    }

    public JCStressSubcommandServiceBuilder withBenchmarkPath(Path benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JCStressSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JCStressSubcommandServiceBuilder withS3Service(S3Service s3Service){
        this.s3Service = s3Service;
        return this;
    }

    public JCStressSubcommandServiceBuilder withMongoConnectionString(URI mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JCStressSubcommandServiceBuilder withJCStressOptions(JCStressOptions jcStressOptions) {
        this.jcStressOptions = jcStressOptions;
        return this;
    }

    public JCStressSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JCStressSubcommandService(s3Service, morphiaService, commonOptions, benchmarkPath, jcStressOptions);
    }
}
