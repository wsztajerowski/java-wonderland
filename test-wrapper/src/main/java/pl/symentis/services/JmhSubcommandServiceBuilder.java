package pl.symentis.services;

public final class JmhSubcommandServiceBuilder {
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;

    private JmhSubcommandServiceBuilder() {
    }

    public static JmhSubcommandServiceBuilder getJmhSubcommandService() {
        return new JmhSubcommandServiceBuilder();
    }

    public JmhSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JmhSubcommandServiceBuilder withJmhOptions(JmhBenchmarksSharedOptions sharedOptions) {
        this.jmhBenchmarksOptions = sharedOptions;
        return this;
    }

    public JmhSubcommandService build() {
        return new JmhSubcommandService(commonOptions, jmhBenchmarksOptions);
    }
}
