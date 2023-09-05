package pl.symentis.services;

public final class JCStressSubcommandServiceBuilder {
    private String benchmarkPath;
    private String commitSha;
    private int runAttempt;
    private String testNameRegex;

    private JCStressSubcommandServiceBuilder() {
    }

    public static JCStressSubcommandServiceBuilder getJCStressSubcommandService() {
        return new JCStressSubcommandServiceBuilder();
    }

    public JCStressSubcommandServiceBuilder withBenchmarkPath(String benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JCStressSubcommandServiceBuilder withCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public JCStressSubcommandServiceBuilder withRunAttempt(int runAttempt) {
        this.runAttempt = runAttempt;
        return this;
    }

    public JCStressSubcommandServiceBuilder withTestNameRegex(String testNameRegex) {
        this.testNameRegex = testNameRegex;
        return this;
    }

    public JCStressSubcommandService build() {
        return new JCStressSubcommandService(benchmarkPath, commitSha, runAttempt, testNameRegex);
    }
}
