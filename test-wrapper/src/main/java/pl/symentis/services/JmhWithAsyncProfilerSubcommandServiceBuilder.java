package pl.symentis.services;

public final class JmhWithAsyncProfilerSubcommandServiceBuilder {
    private String asyncPath;
    private int interval;
    private String output;
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;

    private JmhWithAsyncProfilerSubcommandServiceBuilder() {
    }

    public static JmhWithAsyncProfilerSubcommandServiceBuilder getJmhWithAsyncProfilerSubcommandService() {
        return new JmhWithAsyncProfilerSubcommandServiceBuilder();
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withCommonOptions(CommonSharedOptions commonOptions) {
        this.commonOptions = commonOptions;
        return this;
    }

    public JmhWithAsyncProfilerSubcommandServiceBuilder withJmhOptions(JmhBenchmarksSharedOptions sharedOptions) {
        this.jmhBenchmarksOptions = sharedOptions;
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
        return new JmhWithAsyncProfilerSubcommandService(commonOptions, jmhBenchmarksOptions, asyncPath, interval, output);
    }
}
