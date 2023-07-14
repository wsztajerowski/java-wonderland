package pl.symentis.test_runner;

import picocli.CommandLine.*;

@Command
public class JmhBenchmarksSharedOptions {

    @Option(names = "-wi", description = "Number of warmup iterations to do (default: ${DEFAULT-VALUE})")
    int warmupIterations = 5;

    @Option(names = "-f", description = "How many times to fork a single benchmark. Use 0 to disable forking altogether (default: ${DEFAULT-VALUE})")
    int forks = 5;

    @Option(names = "-i", description = " Number of measurement iterations to do (default: ${DEFAULT-VALUE})")
    int iterations = 5;

    @Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-jmh-benchmarks.jar}", description = "Path to benchmark jar (default: ${DEFAULT-VALUE})")
    String benchmarkPath;

    @Option(names = "--commit-sha", defaultValue = "${GITHUB_SHA}", description = "Commit SHA - you could provide it as a option value or put in GITHUB_SHA env variable")
    String commitSha;

    @Option(names = "--run-attempt", defaultValue = "${GITHUB_RUN_ATTEMPT}", description = "Run attempt no - you could provide it as a option value or put in GITHUB_RUN_ATTEMPT env variable")
    int runAttempt;

    @Parameters(index = "0", description = "The file whose checksum to calculate.", arity = "0..1")
    String benchmarkNameRegex;

}
