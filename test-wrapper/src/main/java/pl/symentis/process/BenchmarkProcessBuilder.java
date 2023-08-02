package pl.symentis.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkProcessBuilder {
    List<String> commands = new ArrayList<>();

    public static BenchmarkProcessBuilder benchmarkProcessBuilder(String benchmarkPath) {
        return new BenchmarkProcessBuilder()
            .addArgument("java")
            .addArgument("-jar")
            .addArgument(benchmarkPath);
    }

    public BenchmarkProcessBuilder addArgumentWithValue(String name, Object value) {
        commands.add(name);
        commands.add(value.toString());
        return this;
    }

    public BenchmarkProcessBuilder addArgument(String argument) {
        commands.add(argument);
        return this;
    }

    public BenchmarkProcessBuilder addOptionalArgument(String argument) {
        if (argument != null){
            commands.add(argument);
        }
        return this;
    }

    public Process buildAndStartProcess() throws IOException {
        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.inheritIO();
        // starting the process
        return pb.start();
    }
}
