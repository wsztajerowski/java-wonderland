package pl.symentis.services;

import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

public final class JCStressSubcommandServiceBuilder {
    private String benchmarkPath;
    private CommonSharedOptions commonOptions;
    private S3Service s3Service;

    private JCStressSubcommandServiceBuilder() {
        s3Service = S3ServiceBuilder.getS3ServiceBuilder().build();
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

    public JCStressSubcommandService build() {
        return new JCStressSubcommandService(s3Service, commonOptions, benchmarkPath);
    }
}
