package pl.symentis.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import static pl.symentis.services.JmhSubcommandServiceBuilder.serviceBuilderWithDefaultS3Service;

@Command(name = "jmh", description = "Run JHM benchmarks")
public class JmhSubcommand implements Runnable {
    @Mixin LoggingMixin loggingMixin;

    @Mixin
    private ApiJmhOptions apiJmhOptions;

    @Mixin
    private ApiCommonSharedOptions apiCommonSharedOptions;

    @Override
    public void run() {
        serviceBuilderWithDefaultS3Service()
            .withCommonOptions(apiCommonSharedOptions.getValues())
            .withJmhOptions(apiJmhOptions.getJmhOptions())
            .withMongoConnectionString(apiCommonSharedOptions.getMongoConnectionString())
            .build()
            .executeCommand();
    }
}
