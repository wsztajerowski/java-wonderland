package pl.symentis.services;

public final class JCStressSubcommandServiceBuilder {
    private String benchmarkPath;
    private CommonSharedOptions commonOptions;

    private JCStressSubcommandServiceBuilder() {
    }

    public static JCStressSubcommandServiceBuilder getJCStressSubcommandService() {
        return new JCStressSubcommandServiceBuilder();
    }

    public JCStressSubcommandServiceBuilder withBenchmarkPath(String benchmarkPath) {
        this.benchmarkPath = benchmarkPath;
        return this;
    }

    public JCStressSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JCStressSubcommandService build() {
        return new JCStressSubcommandService(commonOptions, benchmarkPath);
    }
}
