package pl.symentis.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import static picocli.CommandLine.HelpCommand;

@Command(name = "test-runner",
    description = "Wrapper for benchmarks run and processing it's results.",
    subcommands = {HelpCommand.class, JmhSubcommand.class, JmhWithAsyncProfilerSubcommand.class, JCStressSubcommand.class})
public class TestWrapper {

    public static void main(String... args) {
        new CommandLine(new TestWrapper())
            .execute(args);
    }

}
