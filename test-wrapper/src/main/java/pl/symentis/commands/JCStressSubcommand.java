package pl.symentis.commands;

import picocli.CommandLine;

import static pl.symentis.services.JCStressSubcommandServiceBuilder.getJCStressSubcommandService;

@CommandLine.Command(name = "jcstress", description = "Run JCStress performance tests")
public class JCStressSubcommand implements Runnable {

    @CommandLine.Mixin
    private CommonSharedOptions commonSharedOptions;

    @CommandLine.Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-stress-tests.jar}", description = "Path to JCStress benchmark jar (default: ${DEFAULT-VALUE})")
    String benchmarkPath;

    @Override
    public void run() {
        getJCStressSubcommandService()
            .withCommitSha(commonSharedOptions.commitSha)
            .withRunAttempt(commonSharedOptions.runAttempt)
            .withTestNameRegex(commonSharedOptions.testNameRegex)
            .withBenchmarkPath(benchmarkPath)
            .build()
            .executeCommand();
    }
}
