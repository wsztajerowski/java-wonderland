package pl.symentis.test_runner;

import picocli.CommandLine;

@CommandLine.Command(name = "jcstress", description = "Run JCStress performance tests")
public class JCStressSubcommand {

    @CommandLine.Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-stress-tests.jar}", description = "Path to benchmark jar (default: ${DEFAULT-VALUE})")
    String benchmarkPath;
}
