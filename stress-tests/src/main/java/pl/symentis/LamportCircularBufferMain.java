package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.util.Optional;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;
import static pl.symentis.CircularBufferBuilder.*;

@Outcome(id = "1, 1", expect = ACCEPTABLE_INTERESTING, desc = "With single element added to buffer, two were read from it.")
@Outcome(id = "0, 0", expect = ACCEPTABLE_INTERESTING, desc = "With single element added to buffer, no elements were read from it.")
@Outcome(id = "1, 0", expect = ACCEPTABLE,             desc = "First Producer pushed data, and then consumer get it from buffer.")
@Outcome(id = "0, 1", expect = ACCEPTABLE,             desc = "First Consumer tried to get data from buffer, and then producer pushed element to it.")
public class LamportCircularBufferMain {
    @JCStressTest
    @JCStressMeta(LamportCircularBufferMain.class)
    @State
    public static class SimpleImplementationTest {
        CircularBuffer<Integer> circularBuffer;

        SimpleImplementationTest(){
            circularBuffer = integerCircularBufferBuilder()
                .withBufferSize(1024*1024)
                .build();
        }

        @Actor
        public void actor1() {
            circularBuffer.push(1);
        }

        @Actor
        public void actor2(II_Result r) {
            Optional<Integer> element = circularBuffer.pop();
            Integer result = element
                .orElse(0);
            r.r1 = result;
        }

        @Arbiter
        public void arbiter(II_Result r) {
            Optional<Integer> element = circularBuffer.pop();
            Integer result = element
                .orElse(0);
            r.r2 = result;
        }
    }


}
