package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhWithAsyncProfilerSubcommandServiceBuilder {
    private String asyncPath;
    private int interval;
    private String output;
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;
    private S3Service s3Service;
    private String mongoConnectionString;

    private JmhWithAsyncProfilerSubcommandServiceBuilder() {
        s3Service = S3ServiceBuilder.getS3ServiceBuilder().build();
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

    public JmhWithAsyncProfilerSubcommandServiceBuilder withInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withOutput(String output) {
        this.output = output;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withS3Service(S3Service s3Service){
        this.s3Service = s3Service;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withMongoConnectionString(String mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhWithAsyncProfilerSubcommandService(s3Service, morphiaService, commonOptions, jmhBenchmarksOptions, asyncPath, interval, output);
    }
}
