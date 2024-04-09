package pl.symentis.services;

import pl.symentis.infra.MorphiaService;
import pl.symentis.infra.S3Service;
import pl.symentis.infra.S3ServiceBuilder;

import java.net.URI;
import java.nio.file.Path;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JCStressSubcommandServiceBuilder {
    private static final String JCSTRESS_RESULTS_DIR = "jcstress-results";
    private CommonSharedOptions commonOptions;
    private S3Service s3Service;
    private URI mongoConnectionString;
    private Path benchmarkPath;
    private Path outputPath;
    private Path resultsPath;

    private JCStressSubcommandServiceBuilder() {
        this.s3Service = S3ServiceBuilder.getS3ServiceBuilder().build();
        outputPath = Path.of("output.txt");
        resultsPath = Path.of(JCSTRESS_RESULTS_DIR);
    }

    public static JCStressSubcommandServiceBuilder getJCStressSubcommandService() {
        return new JCStressSubcommandServiceBuilder();
    }

    public JCStressSubcommandServiceBuilder withBenchmarkPath(Path benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JCStressSubcommandServiceBuilder withOutputPath(Path outputPath) {
        this.outputPath = outputPath;
        return this;
    }

    public JCStressSubcommandServiceBuilder withResultsPath(Path resultsPath) {
        this.resultsPath = resultsPath;
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

    public JCStressSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JCStressSubcommandService(s3Service, morphiaService, commonOptions, benchmarkPath, outputPath, resultsPath);
    }
}
