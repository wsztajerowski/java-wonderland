package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;
import pl.symentis.services.options.CommonSharedOptions;
import pl.symentis.services.options.JmhOptions;

import java.net.URI;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhSubcommandServiceBuilder {
    private S3Service s3Service;
    private CommonSharedOptions commonOptions;
    private JmhOptions jmhOptions;
    private URI mongoConnectionString;

    private JmhSubcommandServiceBuilder() {
    }

    public static JmhSubcommandServiceBuilder serviceBuilder() {
        return new JmhSubcommandServiceBuilder();
    }

    public static JmhSubcommandServiceBuilder serviceBuilderWithDefaultS3Service() {
        return new JmhSubcommandServiceBuilder()
            .withS3Service(S3ServiceBuilder.getDefaultS3ServiceBuilder().build());
    }

    public JmhSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JmhSubcommandServiceBuilder withJmhOptions(JmhOptions jmhOptions) {
        this.jmhOptions = jmhOptions;
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

    public JmhSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhSubcommandService(s3Service, morphiaService, commonOptions, jmhOptions);
    }
}
