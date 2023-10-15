package pl.symentis.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static pl.symentis.services.JmhWithAsyncProfilerSubcommandServiceBuilder.getJmhWithAsyncProfilerSubcommandService;

@Command(name = "jmh-with-async", description = "Run JHM benchmarks with Async profiler")
public class JmhWithAsyncProfilerSubcommand implements Runnable {
    @CommandLine.Mixin
    private ApiJmhBenchmarksSharedOptions sharedJmhOptions;

    @CommandLine.Mixin
    private ApiCommonSharedOptions apiCommonSharedOptions;

    @Option(names = "--async-path", defaultValue = "${ASYNC_PATH:-/home/ec2-user/async-profiler/build/libasyncProfiler.so}", description = "Path to Async profiler (default: ${DEFAULT-VALUE})")
    String asyncPath;

    @Option(names = {"-ai", "--async-interval"}, description = "Profiling interval (default: ${DEFAULT-VALUE})")
    int interval = 999;

    @Option(names = {"-ao", "--async-output"}, description = "Output format(s). Supported: [text, collapsed, flamegraph, tree, jfr] (default: ${DEFAULT-VALUE})")
    String output = "flamegraph";

    @Override
    public void run() {
        getJmhWithAsyncProfilerSubcommandService()
            .withCommonOptions(apiCommonSharedOptions.getValues())
            .withJmhOptions(sharedJmhOptions.getValues())
            .withAsyncPath(asyncPath)
            .withInterval(interval)
            .withOutput(output)
            .build()
            .executeCommand();
    }


}
