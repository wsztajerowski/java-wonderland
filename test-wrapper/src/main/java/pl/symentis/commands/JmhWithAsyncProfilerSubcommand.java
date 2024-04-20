package pl.symentis.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import pl.symentis.services.options.AsyncProfilerOptions;

import java.nio.file.Path;

import static pl.symentis.services.JmhWithAsyncProfilerSubcommandServiceBuilder.serviceBuilderWithDefaultS3Service;

@Command(name = "jmh-with-async", description = "Run JHM benchmarks with Async profiler")
public class JmhWithAsyncProfilerSubcommand implements Runnable {
    @Mixin
    LoggingMixin loggingMixin;
    @Mixin
    private ApiJmhOptions apiJmhOptions;

    @Mixin
    private ApiCommonSharedOptions apiCommonSharedOptions;

    @Option(names = {"-ap", "--async-path"}, defaultValue = "${ASYNC_PATH:-/home/ec2-user/async-profiler/build/libasyncProfiler.so}", description = "Path to Async profiler (default: ${DEFAULT-VALUE})")
    String asyncPath;

    @Option(names = {"-ai", "--async-interval"}, description = "Profiling interval (default: ${DEFAULT-VALUE})")
    int asyncInterval = 9990;

    @Option(names = {"-aot", "--async-output-type"}, description = "Output format(s). Supported: [text, collapsed, flamegraph, tree, jfr] (default: ${DEFAULT-VALUE})")
    String asyncOutputType = "flamegraph";

    @Option(names = {"-aop", "--async-output-path"}, defaultValue = "./async-output", description = "Profiler output path")
    Path asyncOutputPath;

    @Override
    public void run() {
        serviceBuilderWithDefaultS3Service()
            .withCommonOptions(apiCommonSharedOptions.getValues())
            .withJmhOptions(apiJmhOptions.getJmhOptions())
            .withAsyncProfilerOptions(AsyncProfilerOptions.asyncProfilerOptionsBuilder()
                .withAsyncPath(asyncPath)
                .withAsyncInterval(asyncInterval)
                .withAsyncOutputType(asyncOutputType)
                .withAsyncOutputPath(asyncOutputPath)
                .build())
            .withMongoConnectionString(apiCommonSharedOptions.getMongoConnectionString())
            .build()
            .executeCommand();
    }
}
