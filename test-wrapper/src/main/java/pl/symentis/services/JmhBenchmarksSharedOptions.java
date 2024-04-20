package pl.symentis.services;
import java.nio.file.Path;

public record JmhBenchmarksSharedOptions(
    Integer iterations,
    Integer batchSize,
    String minimumIterationTime,
    Integer warmupIterations,
    Integer warmupBatchSize,
    String minimumWarmupIterationTime,
    String iterationTimeout,
    Integer threads,
    String benchmarkMode,
    Boolean synchronizeIterations,
    Boolean forceGCBetweenIterations,
    Boolean failAfterUnrecoverableError,
    String verbosityMode,
    Integer forks,
    Integer warmupForks,
    Path humanReadableOutput,
    Path machineReadableOutput,
    String threadGroups,
    String jvm,
    String jvmArgs,
    String jvmArgsAppend,
    String jvmArgsPrepend,
    String timeUnit,
    Integer operationsPerInvocations,
    String warmupMode,
    String excludeBenchmarkRegex,
    String warmupBenchmarks,
    Path benchmarkPath) {
}
