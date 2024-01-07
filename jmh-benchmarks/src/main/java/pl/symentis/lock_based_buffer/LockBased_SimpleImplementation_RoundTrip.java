package pl.symentis.lock_based_buffer;

import org.openjdk.jmh.annotations.*;
import pl.symentis.LockBasedCircularBuffer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static pl.symentis.LockBasedCircularBufferBuilder.integerCircularBufferBuilder;

@State(Scope.Group)
public class LockBased_SimpleImplementation_RoundTrip {

    private LockBasedCircularBuffer<Integer> outbox;
    private LockBasedCircularBuffer<Integer> inbox;
    private Integer element;

    @Setup(Level.Iteration)
    public void setup() {
        element = 1;
        inbox = integerCircularBufferBuilder()
            .withBufferSize(10_000)
            .build();
        outbox = integerCircularBufferBuilder()
            .withBufferSize(10_000)
            .build();
    }


    @Benchmark
    @GroupThreads(1)
    @Group("pingpong")
    public Integer ping() {
        try {
            outbox.push(element, 1, TimeUnit.MILLISECONDS);
            return inbox.pop(1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return -1;
        }
    }

    @Benchmark
    @GroupThreads(1)
    @Group("pingpong")
    public Integer pong() {
        try {
            Integer received = outbox.pop(1, TimeUnit.MILLISECONDS);
            inbox.push(received, 1, TimeUnit.MILLISECONDS);
            return received;
        } catch (TimeoutException e) {
            return -1;
        }
    }

}
