package pl.symentis.test_runner;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import static picocli.CommandLine.HelpCommand;

@Command(name = "test-runner",
    description = "Wrapper for benchmarks run and processing it's results.",
    subcommands = {HelpCommand.class, JmhSubcommand.class, JmhWithAsyncProfilerSubcommand.class, JCStressSubcommand.class})
public class TestWrapper {

    public static void main(String... args) {
        int exitCode = new CommandLine(new TestWrapper())
            .execute(args);
        System.exit(exitCode);
    }

}
