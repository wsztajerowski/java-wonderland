package pl.symentis.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import static pl.symentis.services.JmhSubcommandServiceBuilder.getJmhSubcommandService;

@Command(name = "jmh", description = "Run JHM benchmarks")
public class JmhSubcommand implements Runnable {

    @Mixin
    private JmhBenchmarksSharedOptions sharedJmhOptions;

    @Mixin
    private CommonSharedOptions commonSharedOptions;

    @Override
    public void run() {
        getJmhSubcommandService()
            .withCommitSha(commonSharedOptions.commitSha)
            .withRunAttempt(commonSharedOptions.runAttempt)
            .withTestNameRegex(commonSharedOptions.testNameRegex)
            .withBenchmarkPath(sharedJmhOptions.benchmarkPath)
            .withForks(sharedJmhOptions.forks)
            .withIterations(sharedJmhOptions.iterations)
            .withWarmupIterations(sharedJmhOptions.warmupIterations)
            .build()
            .executeCommand();
    }
}
