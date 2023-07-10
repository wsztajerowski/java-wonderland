package pl.symentis;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class WrapperTest {

    @Test
    void run_jmh_benchmark() throws IOException, InterruptedException {
//        java -jar jmh-benchmarks.jar  -f 1 -wi 1 -i 1 -rf json
        List<String> commands = new ArrayList<String>();
        commands.add("java"); // command
        commands.add("-jar"); // command
        commands.add("../jmh-benchmarks/target/jmh-benchmarks.jar"); // command
        commands.add("-f"); // command
        commands.add("1"); // command
        commands.add("-i"); // command
        commands.add("1"); // command
        commands.add("-wi"); // command
        commands.add("1"); // command
        commands.add("-rf"); // command
        commands.add("json"); // command

        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.inheritIO();
        // starting the process
        Process process = pb.start();
        process.waitFor();
    }

    @Test
    void run_jmh_benchmark_with_async_profiler() throws IOException, InterruptedException {
//        java -jar jmh-benchmarks.jar  -f 1 -wi 1 -i 1 -rf json
        List<String> commands = new ArrayList<String>();
        commands.add("java"); // command
        commands.add("-jar"); // command
        commands.add("../jmh-benchmarks/target/jmh-benchmarks.jar"); // command
        commands.add("-f"); // command
        commands.add("1"); // command
        commands.add("-i"); // command
        commands.add("1"); // command
        commands.add("-wi"); // command
        commands.add("1"); // command
        commands.add("-rf"); // command
        commands.add("json"); // command
        commands.add("-prof"); // command
        commands.add("async:libPath=/Users/Wiktor/workspace/way-to-neo/async-profiler/build/libasyncProfiler.so;output=flamegraph;interval=999"); // command

        // creating the process
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.inheritIO();
        pb.redirectErrorStream(true);
        // starting the process
        Process process = pb.start();
        process.waitFor();
    }
}