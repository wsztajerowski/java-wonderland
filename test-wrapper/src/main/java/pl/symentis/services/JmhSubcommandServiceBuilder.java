package pl.symentis.services;

import pl.symentis.infra.MorphiaService;

import static pl.symentis.infra.MorphiaServiceBuilder.getMorphiaServiceBuilder;

public final class JmhSubcommandServiceBuilder {
    private CommonSharedOptions commonOptions;
    private JmhBenchmarksSharedOptions jmhBenchmarksOptions;
    private String mongoConnectionString;

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

    public JmhSubcommandServiceBuilder withMongoConnectionString(String mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
        return this;
    }

    public JmhSubcommandService build() {
        MorphiaService morphiaService = getMorphiaServiceBuilder()
            .withConnectionString(mongoConnectionString)
            .build();
        return new JmhSubcommandService(morphiaService, commonOptions, jmhBenchmarksOptions);
    }
}
