package pl.symentis.lamport;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Control;
import pl.symentis.CircularBuffer;

import java.util.Optional;

import static pl.symentis.CircularBufferBuilder.integerCircularBufferBuilder;

@State(Scope.Group)
public class Lamport_SimpleImplementation_RoundTrip {

    CircularBuffer<Integer> outbox;
    CircularBuffer<Integer> inbox;

    @Setup(Level.Iteration)
    public void setup(){
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
    public boolean ping(Control cnt) {
        boolean sended = outbox
            .push(1);
        while (!cnt.stopMeasurement && inbox.pop().isEmpty()) {
            // this body is intentionally left blank
        }
        return sended;
    }

    @Benchmark
    @GroupThreads(1)
    @Group("pingpong")
    public boolean pong(Control cnt) {
        while (!cnt.stopMeasurement && outbox.pop().isEmpty()) {
            // this body is intentionally left blank
        }
        boolean sended = inbox
            .push(1);
        return sended;
    }

}
