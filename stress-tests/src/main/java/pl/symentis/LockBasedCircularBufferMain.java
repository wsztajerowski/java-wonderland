package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import static pl.symentis.LockBasedCircularBufferBuilder.integerCircularBufferBuilder;

@Outcome(id = "0", expect = ACCEPTABLE_INTERESTING, desc = "With single element added to buffer, no elements were read from it.")
@Outcome(id = "1", expect = ACCEPTABLE, desc = "Producer pushed data, and consumer get it from buffer.")
public class LockBasedCircularBufferMain {
    @JCStressTest
    @JCStressMeta(LockBasedCircularBufferMain.class)
    @State
    public static class SimpleImplementationTest {
        LockBasedCircularBuffer<Integer> circularBuffer;

        SimpleImplementationTest() {
            circularBuffer = integerCircularBufferBuilder()
                .withBufferSize(1)
                .build();
        }

        @Actor
        public void actor1() {
            circularBuffer.push(1);
        }

        @Actor
        public void actor2(I_Result r) {
            r.r1 = circularBuffer.pop();
        }

    }


}
