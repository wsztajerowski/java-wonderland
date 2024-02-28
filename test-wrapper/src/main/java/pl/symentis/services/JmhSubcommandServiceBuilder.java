package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhSubcommandServiceBuilder {
    private S3Service s3Service;
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;
    private String mongoConnectionString;

    private JmhSubcommandServiceBuilder() {
        s3Service = S3ServiceBuilder.getS3ServiceBuilder().build();
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

    public JmhSubcommandServiceBuilder withMongoConnectionString(String mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JmhSubcommandServiceBuilder withS3Service(S3Service s3Service){
        this.s3Service = s3Service;
        return this;
    }

    public JmhSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhSubcommandService(s3Service, morphiaService, commonOptions, jmhBenchmarksOptions);
    }
}
