package pl.symentis.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import static pl.symentis.services.JmhSubcommandServiceBuilder.getJmhSubcommandService;

@Command(name = "jmh", description = "Run JHM benchmarks")
public class JmhSubcommand implements Runnable {

    @Mixin
    private ApiJmhBenchmarksSharedOptions sharedJmhOptions;

    @Mixin
    private ApiCommonSharedOptions apiCommonSharedOptions;

    @Override
    public void run() {
        getJmhSubcommandService()
            .withCommonOptions(apiCommonSharedOptions.getValues())
            .withJmhOptions(sharedJmhOptions.getValues())
            .build()
            .executeCommand();
    }
}
