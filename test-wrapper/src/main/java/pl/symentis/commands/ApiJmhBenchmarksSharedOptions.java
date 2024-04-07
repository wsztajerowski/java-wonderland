package pl.symentis.commands;

import picocli.CommandLine.*;
import pl.symentis.services.JmhBenchmarksSharedOptions;

import java.nio.file.Path;

@Command
public class ApiJmhBenchmarksSharedOptions {

    @Option(names = "-wi", description = "Number of warmup iterations to do (default: ${DEFAULT-VALUE})")
    int warmupIterations = 5;

    @Option(names = "-f", description = "How many times to fork a single benchmark. Use 0 to disable forking altogether (default: ${DEFAULT-VALUE})")
    int forks = 5;

    @Option(names = "-i", description = "Number of measurement iterations to do (default: ${DEFAULT-VALUE})")
    int iterations = 5;

    @Option(names = "--benchmark-path", defaultValue = "${BENCHMARK_PATH:-jmh-benchmarks.jar}", description = "Path to benchmark jar (default: ${DEFAULT-VALUE})")
    Path benchmarkPath;

    public JmhBenchmarksSharedOptions getValues(){
        return new JmhBenchmarksSharedOptions(warmupIterations, forks, iterations, benchmarkPath);
    }
}
