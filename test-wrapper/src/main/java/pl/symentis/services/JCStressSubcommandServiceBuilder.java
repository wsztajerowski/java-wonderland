package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

import static java.util.Objects.requireNonNull;
import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JCStressSubcommandServiceBuilder {
    private String benchmarkPath;
    private CommonSharedOptions commonOptions;
    private S3Service s3Service;
    private String mongoConnectionString;

    private JCStressSubcommandServiceBuilder() {
    }

    public static JCStressSubcommandServiceBuilder getJCStressSubcommandService() {
        return new JCStressSubcommandServiceBuilder();
    }

    public JCStressSubcommandServiceBuilder withBenchmarkPath(String benchmarkPath) {
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

    public JCStressSubcommandServiceBuilder withDefaultS3Service(){
        this.s3Service = S3ServiceBuilder.getS3ServiceBuilder().build();
        return this;
    }

    public JCStressSubcommandServiceBuilder withMongoConnectionString(String mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JCStressSubcommandService build() {
        requireNonNull(s3Service, "Please provide S3 service!");
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JCStressSubcommandService(s3Service, morphiaService, commonOptions, benchmarkPath);
    }
}
