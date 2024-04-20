package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;
import pl.symentis.services.options.AsyncProfilerOptions;
import pl.symentis.services.options.CommonSharedOptions;
import pl.symentis.services.options.JmhOptions;

import java.net.URI;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhWithAsyncProfilerSubcommandServiceBuilder {
    private AsyncProfilerOptions asyncProfilerOptions;
    private CommonSharedOptions commonOptions;
    private S3Service s3Service;
    private URI mongoConnectionString;
    private JmhOptions jmhOptions;

    private JmhWithAsyncProfilerSubcommandServiceBuilder() {
    }

    public static JmhWithAsyncProfilerSubcommandServiceBuilder serviceBuilder() {
        return new JmhWithAsyncProfilerSubcommandServiceBuilder();
    }

    public static JmhWithAsyncProfilerSubcommandServiceBuilder serviceBuilderWithDefaultS3Service() {
        return new JmhWithAsyncProfilerSubcommandServiceBuilder()
            .withS3Service(S3ServiceBuilder.getDefaultS3ServiceBuilder().build());
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withJmhOptions(JmhOptions jmhOptions) {
        this.jmhOptions = jmhOptions;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withAsyncProfilerOptions(AsyncProfilerOptions asyncProfilerOptions) {
        this.asyncProfilerOptions = asyncProfilerOptions;
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

    public JmhWithAsyncProfilerSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhWithAsyncProfilerSubcommandService(
            s3Service,
            morphiaService,
            commonOptions,
            jmhOptions,
            asyncProfilerOptions);
    }
}
