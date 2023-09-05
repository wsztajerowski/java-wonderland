package pl.symentis.services;

public final class JmhSubcommandServiceBuilder {
    private String benchmarkPath;
    private int forks;
    private int iterations;
    private int warmupIterations;
    private String testNameRegex;
    private String commitSha;
    private int runAttempt;

    private JmhSubcommandServiceBuilder() {
    }

    public static JmhSubcommandServiceBuilder getJmhSubcommandService() {
        return new JmhSubcommandServiceBuilder();
    }

    public JmhSubcommandServiceBuilder withBenchmarkPath(String benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JmhSubcommandServiceBuilder withForks(int forks) {
        this.forks = forks;
        return this;
    }

    public JmhSubcommandServiceBuilder withIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    public JmhSubcommandServiceBuilder withWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public JmhSubcommandServiceBuilder withTestNameRegex(String testNameRegex) {
        this.testNameRegex = testNameRegex;
        return this;
    }

    public JmhSubcommandServiceBuilder withCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public JmhSubcommandServiceBuilder withRunAttempt(int runAttempt) {
        this.runAttempt = runAttempt;
        return this;
    }

    public JmhSubcommandService build() {
        return new JmhSubcommandService(benchmarkPath, forks, iterations, warmupIterations, testNameRegex, commitSha, runAttempt);
    }
}
