package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;
@JCStressTest
// Outline the outcomes here. The default outcome is provided, you need to remove it:
@Outcome(id = "0, 0", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@State
public class ConcurrencyTest {

    @Actor
    public void actor1(II_Result r) {
        // Put the code for first thread here
    }

    @Actor
    public void actor2(II_Result r) {
        // Put the code for second thread here
    }

}
