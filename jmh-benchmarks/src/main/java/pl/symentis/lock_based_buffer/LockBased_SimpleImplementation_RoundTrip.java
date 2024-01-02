package pl.symentis.lock_based_buffer;

import org.openjdk.jmh.annotations.*;
import pl.symentis.LockBasedCircularBuffer;

import static pl.symentis.LockBasedCircularBufferBuilder.integerCircularBufferBuilder;

@State(Scope.Group)
public class LockBased_SimpleImplementation_RoundTrip {

    LockBasedCircularBuffer<Integer> outbox;
    LockBasedCircularBuffer<Integer> inbox;

    @Setup(Level.Iteration)
    public void setup() {
        inbox = integerCircularBufferBuilder()
            .withBufferSize(1)
            .build();
        outbox = integerCircularBufferBuilder()
            .withBufferSize(1)
            .build();
    }


    @Benchmark
    @GroupThreads(1)
    @Group("pingpong")
    public Integer ping() throws InterruptedException {
        outbox.push(1);
        return inbox.pop();
    }

    @Benchmark
    @GroupThreads(1)
    @Group("pingpong")
    public Integer pong() throws InterruptedException {
        Integer received = outbox.pop();
        inbox.push(received);
        return received;
    }

}
