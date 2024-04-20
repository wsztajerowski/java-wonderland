package pl.symentis.process;

import pl.symentis.services.JmhBenchmarksSharedOptions;

import static pl.symentis.process.BenchmarkProcessBuilder.benchmarkProcessBuilder;

public class JmhBenchmarkProcessBuilderFactory {
    public static BenchmarkProcessBuilder prepopulatedJmhBenchmarkProcessBuilder(JmhBenchmarksSharedOptions jmhBenchmarksOptions){
        return benchmarkProcessBuilder(jmhBenchmarksOptions.benchmarkPath())
            .addArgumentWithValue("-rf", "json")
            .addArgumentIfValueIsNotNull("-i", jmhBenchmarksOptions.iterations())
            .addArgumentIfValueIsNotNull("-bs", jmhBenchmarksOptions.batchSize())
            .addArgumentIfValueIsNotNull("-r", jmhBenchmarksOptions.minimumIterationTime())
            .addArgumentIfValueIsNotNull("-wi", jmhBenchmarksOptions.warmupIterations())
            .addArgumentIfValueIsNotNull("-wbs", jmhBenchmarksOptions.warmupBatchSize())
            .addArgumentIfValueIsNotNull("-w", jmhBenchmarksOptions.minimumWarmupIterationTime())
            .addArgumentIfValueIsNotNull("-to", jmhBenchmarksOptions.iterationTimeout())
            .addArgumentIfValueIsNotNull("-t", jmhBenchmarksOptions.threads())
            .addArgumentIfValueIsNotNull("-bm", jmhBenchmarksOptions.benchmarkMode())
            .addArgumentIfValueIsNotNull("-si", jmhBenchmarksOptions.synchronizeIterations())
            .addArgumentIfValueIsNotNull("-gc", jmhBenchmarksOptions.forceGCBetweenIterations())
            .addArgumentIfValueIsNotNull("-foe", jmhBenchmarksOptions.failAfterUnrecoverableError())
            .addArgumentIfValueIsNotNull("-v", jmhBenchmarksOptions.verbosityMode())
            .addArgumentIfValueIsNotNull("-f", jmhBenchmarksOptions.forks())
            .addArgumentIfValueIsNotNull("-wf", jmhBenchmarksOptions.warmupForks())
            .addArgumentIfValueIsNotNull("-o", jmhBenchmarksOptions.humanReadableOutput())
            .addArgumentIfValueIsNotNull("-rff", jmhBenchmarksOptions.machineReadableOutput())
            .addArgumentIfValueIsNotNull("-tg", jmhBenchmarksOptions.threadGroups())
            .addArgumentIfValueIsNotNull("-jvm", jmhBenchmarksOptions.jvm())
            .addArgumentIfValueIsNotNull("-jvmArgs", jmhBenchmarksOptions.jvmArgs())
            .addArgumentIfValueIsNotNull("-jvmArgsAppend", jmhBenchmarksOptions.jvmArgsAppend())
            .addArgumentIfValueIsNotNull("-jvmArgsPrepend", jmhBenchmarksOptions.jvmArgsPrepend())
            .addArgumentIfValueIsNotNull("-tu", jmhBenchmarksOptions.timeUnit())
            .addArgumentIfValueIsNotNull("-opi", jmhBenchmarksOptions.operationsPerInvocations())
            .addArgumentIfValueIsNotNull("-wm", jmhBenchmarksOptions.warmupMode())
            .addArgumentIfValueIsNotNull("-e", jmhBenchmarksOptions.excludeBenchmarkRegex())
            .addArgumentIfValueIsNotNull("-wmb", jmhBenchmarksOptions.warmupBenchmarks());
    }
}
