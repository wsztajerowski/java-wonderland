package pl.symentis.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.nio.file.Path;

import static pl.symentis.services.JCStressSubcommandServiceBuilder.serviceBuilderWithDefaultS3Service;

@Command(name = "jcstress", description = "Run JCStress performance tests")
public class JCStressSubcommand implements Runnable {
    @Mixin
    LoggingMixin loggingMixin;

    @Mixin
    private ApiCommonSharedOptions apiCommonSharedOptions;

    @Mixin
    private ApiJCStressOptions apiJCStressOptions;

    @CommandLine.Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-stress-tests.jar}", description = "Path to JCStress benchmark jar (default: ${DEFAULT-VALUE})")
    Path benchmarkPath;

    @Override
    public void run() {
        serviceBuilderWithDefaultS3Service()
            .withCommonOptions(apiCommonSharedOptions.getValues())
            .withJCStressOptions(apiJCStressOptions.getValues())
            .withBenchmarkPath(benchmarkPath)
            .withMongoConnectionString(apiCommonSharedOptions.getMongoConnectionString())
            .build()
            .executeCommand();
    }
}
