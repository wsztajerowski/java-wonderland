package pl.symentis.commands;

import picocli.CommandLine;

import static pl.symentis.services.JCStressSubcommandServiceBuilder.getJCStressSubcommandService;

@CommandLine.Command(name = "jcstress", description = "Run JCStress performance tests")
public class JCStressSubcommand implements Runnable {

    @CommandLine.Mixin
    private ApiCommonSharedOptions apiCommonSharedOptions;

    @CommandLine.Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-stress-tests.jar}", description = "Path to JCStress benchmark jar (default: ${DEFAULT-VALUE})")
    String benchmarkPath;

    @Override
    public void run() {
        getJCStressSubcommandService()
            .withCommonOptions(apiCommonSharedOptions.getValues())
            .withBenchmarkPath(benchmarkPath)
            .build()
            .executeCommand();
    }
}
