package pl.symentis.services;

import java.nio.file.Path;

public final class JmhBenchmarksSharedOptionsBuilder {
    private Integer iterations;
    private Integer batchSize;
    private String minimumIterationTime;
    private Integer warmupIterations;
    private Integer warmupBatchSize;
    private String minimumWarmupIterationTime;
    private String iterationTimeout;
    private Integer threads;
    private String benchmarkMode;
    private Boolean synchronizeIterations;
    private Boolean forceGCBetweenIterations;
    private Boolean failAfterUnrecoverableError;
    private String verbosityMode;
    private Integer forks;
    private Integer warmupForks;
    private Path humanReadableOutput;
    private Path machineReadableOutput;
    private String threadGroups;
    private String jvm;
    private String jvmArgs;
    private String jvmArgsAppend;
    private String jvmArgsPrepend;
    private String timeUnit;
    private Integer operationsPerInvocations;
    private String warmupMode;
    private String excludeBenchmarkRegex;
    private String warmupBenchmarks;
    private Path benchmarkPath;

    private JmhBenchmarksSharedOptionsBuilder() {
    }

    public static JmhBenchmarksSharedOptionsBuilder jmhBenchmarksSharedOptionsBuilder() {
        return new JmhBenchmarksSharedOptionsBuilder();
    }

    public JmhBenchmarksSharedOptionsBuilder withIterations(Integer iterations) {
        this.iterations = iterations;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withMinimumIterationTime(String minimumIterationTime) {
        this.minimumIterationTime = minimumIterationTime;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withWarmupIterations(Integer warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withWarmupBatchSize(Integer warmupBatchSize) {
        this.warmupBatchSize = warmupBatchSize;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withMinimumWarmupIterationTime(String minimumWarmupIterationTime) {
        this.minimumWarmupIterationTime = minimumWarmupIterationTime;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withIterationTimeout(String iterationTimeout) {
        this.iterationTimeout = iterationTimeout;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withThreads(Integer threads) {
        this.threads = threads;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withBenchmarkMode(String benchmarkMode) {
        this.benchmarkMode = benchmarkMode;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withSynchronizeIterations(Boolean synchronizeIterations) {
        this.synchronizeIterations = synchronizeIterations;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withForceGCBetweenIterations(Boolean forceGCBetweenIterations) {
        this.forceGCBetweenIterations = forceGCBetweenIterations;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withFailAfterUnrecoverableError(Boolean failAfterUnrecoverableError) {
        this.failAfterUnrecoverableError = failAfterUnrecoverableError;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withVerbosityMode(String verbosityMode) {
        this.verbosityMode = verbosityMode;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withForks(Integer forks) {
        this.forks = forks;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withWarmupForks(Integer warmupForks) {
        this.warmupForks = warmupForks;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withHumanReadableOutput(Path humanReadableOutput) {
        this.humanReadableOutput = humanReadableOutput;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withMachineReadableOutput(Path machineReadableOutput) {
        this.machineReadableOutput = machineReadableOutput;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withThreadGroups(String threadGroups) {
        this.threadGroups = threadGroups;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withJvm(String jvm) {
        this.jvm = jvm;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withJvmArgs(String jvmArgs) {
        this.jvmArgs = jvmArgs;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withJvmArgsAppend(String jvmArgsAppend) {
        this.jvmArgsAppend = jvmArgsAppend;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withJvmArgsPrepend(String jvmArgsPrepend) {
        this.jvmArgsPrepend = jvmArgsPrepend;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withOperationsPerInvocations(Integer operationsPerInvocations) {
        this.operationsPerInvocations = operationsPerInvocations;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withWarmupMode(String warmupMode) {
        this.warmupMode = warmupMode;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withExcludeBenchmarkRegex(String excludeBenchmarkRegex) {
        this.excludeBenchmarkRegex = excludeBenchmarkRegex;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withWarmupBenchmarks(String warmupBenchmarks) {
        this.warmupBenchmarks = warmupBenchmarks;
        return this;
    }

    public JmhBenchmarksSharedOptionsBuilder withBenchmarkPath(Path benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JmhBenchmarksSharedOptions build() {
        return new JmhBenchmarksSharedOptions(iterations, batchSize, minimumIterationTime, warmupIterations, warmupBatchSize, minimumWarmupIterationTime, iterationTimeout, threads, benchmarkMode, synchronizeIterations, forceGCBetweenIterations, failAfterUnrecoverableError, verbosityMode, forks, warmupForks, humanReadableOutput, machineReadableOutput, threadGroups, jvm, jvmArgs, jvmArgsAppend, jvmArgsPrepend, timeUnit, operationsPerInvocations, warmupMode, excludeBenchmarkRegex, warmupBenchmarks, benchmarkPath);
    }
}
