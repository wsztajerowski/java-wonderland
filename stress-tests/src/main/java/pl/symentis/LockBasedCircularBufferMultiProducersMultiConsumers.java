package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;
import static pl.symentis.LockBasedCircularBufferBuilder.integerCircularBufferBuilder;

@Outcome(id = "1, 1", expect = FORBIDDEN, desc = "Producer 1 has been executed twice, when Producer 2 hasn't been executed")
@Outcome(id = "2, 2", expect = FORBIDDEN, desc = "Producer 2 has been executed twice, when Producer 1 hasn't been executed")
@Outcome(id = "1, 2", expect = ACCEPTABLE, desc = "Correct behaviour")
@Outcome(id = "2, 1", expect = ACCEPTABLE, desc = "Correct behaviour")
public class LockBasedCircularBufferMultiProducersMultiConsumers {
    @JCStressTest
    @JCStressMeta(LockBasedCircularBufferMultiProducersMultiConsumers.class)
    @State
    public static class SimpleImplementationTest {
        LockBasedCircularBuffer<Integer> circularBuffer;

        SimpleImplementationTest() {
            circularBuffer = integerCircularBufferBuilder()
                .withBufferSize(2)
                .build();
        }

        @Actor
        public void producer1() {
            circularBuffer.push(1);
        }

        @Actor
        public void producer2() {
            circularBuffer.push(2);
        }

        @Actor
        public void consumer1(II_Result r) {
            r.r1 = circularBuffer.pop();
        }

        @Actor
        public void consumer2(II_Result r) {
            r.r2 = circularBuffer.pop();
        }

    }


}
