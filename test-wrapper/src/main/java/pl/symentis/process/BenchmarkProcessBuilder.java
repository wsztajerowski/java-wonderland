package pl.symentis.process;

import pl.symentis.JavaWonderlandException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkProcessBuilder {
    List<String> commands = new ArrayList<>();
    Path outputPath;
    Path errorOutputPath;

    private BenchmarkProcessBuilder(){
        outputPath = Paths.get("output.txt");
        errorOutputPath = Paths.get("error_output.txt");
    }

    public static BenchmarkProcessBuilder benchmarkProcessBuilder(String benchmarkPath) {
        return new BenchmarkProcessBuilder()
            .addArgument("java")
            .addArgument("-jar")
            .addArgument(benchmarkPath);
    }

    public BenchmarkProcessBuilder withOutputPath(Path path){
        this.outputPath = path;
        return this;
    }

    public BenchmarkProcessBuilder withErrorOutputPath(Path path){
        this.errorOutputPath = path;
        return this;
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

    public BenchmarkProcessBuilder addArgumentIfValueIsNotNull(String name, Object value) {
        if (value != null){
            commands.add(name);
            commands.add(value.toString());
        }
        return this;
    }

    public Process buildAndStartProcess() {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        try {
            Process process = processBuilder.start();
            Files.copy(
                process.getInputStream(),
                outputPath,
                StandardCopyOption.REPLACE_EXISTING);
            Files.copy(
                process.getErrorStream(),
                errorOutputPath,
                StandardCopyOption.REPLACE_EXISTING);
            return process;
        } catch (IOException e) {
            throw new JavaWonderlandException(e);
        }
    }
}
