package pl.symentis.lamport;

import org.openjdk.jmh.annotations.*;
import pl.symentis.CircularBuffer;

import java.util.Optional;

import static pl.symentis.CircularBufferBuilder.integerCircularBufferBuilder;

@State(Scope.Benchmark)
public class Lamport_SimpleImplementation {

    CircularBuffer<Integer> circularBuffer;

    @Setup
    public void setup(){
        circularBuffer = integerCircularBufferBuilder()
            .withBufferSize(1024*1024)
            .build();
    }


    @Benchmark
    @GroupThreads(1)
    @Group("g1")
    public boolean produceElement() {
        boolean pushed = circularBuffer
            .push(1);
        return pushed;
    }

    @Benchmark
    @GroupThreads(1)
    @Group("g1")
    public Optional<Integer> consumeElement() {
        Optional<Integer> pop = circularBuffer
            .pop();
        return pop;
    }

}
