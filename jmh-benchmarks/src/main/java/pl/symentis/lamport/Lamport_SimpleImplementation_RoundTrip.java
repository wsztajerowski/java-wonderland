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
        boolean sent = outbox
            .push(1);
        while (!cnt.stopMeasurement && inbox.pop().isEmpty()) {
            // this body is intentionally left blank
        }
        return sent;
    }

    @Benchmark
    @GroupThreads(1)
    @Group("pingpong")
    public boolean pong(Control cnt) {
        Optional<Integer> received;
        do {
            received = outbox.pop();
        } while (!cnt.stopMeasurement && received.isEmpty());

        boolean sent = inbox
            .push(received.orElseThrow());
        return sent;
    }

}
