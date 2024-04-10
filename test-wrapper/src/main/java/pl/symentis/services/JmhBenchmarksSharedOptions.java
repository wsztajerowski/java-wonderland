package pl.symentis.services;
import java.nio.file.Path;

public record JmhBenchmarksSharedOptions(int warmupIterations, int forks, int iterations, String jvmArgs, Path benchmarkPath) {
    public static final String JMH_RESULT_FILENAME = "jmh-result.json";
}
