package pl.symentis.services;

public final class JmhWithAsyncProfilerSubcommandServiceBuilder {
    private String benchmarkPath;
    private int forks;
    private int iterations;
    private int warmupIterations;
    private String testNameRegex;
    private String commitSha;
    private int runAttempt;
    private String asyncPath;
    private int interval;
    private String output;

    private JmhWithAsyncProfilerSubcommandServiceBuilder() {
    }

    public static JmhWithAsyncProfilerSubcommandServiceBuilder getJmhWithAsyncProfilerSubcommandService() {
        return new JmhWithAsyncProfilerSubcommandServiceBuilder();
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withBenchmarkPath(String benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withForks(int forks) {
        this.forks = forks;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withTestNameRegex(String testNameRegex) {
        this.testNameRegex = testNameRegex;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withRunAttempt(int runAttempt) {
        this.runAttempt = runAttempt;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withAsyncPath(String asyncPath) {
        this.asyncPath = asyncPath;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withOutput(String output) {
        this.output = output;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandService build() {
        return new JmhWithAsyncProfilerSubcommandService(benchmarkPath, forks, iterations, warmupIterations, testNameRegex, commitSha, runAttempt, asyncPath, interval, output);
    }
}
