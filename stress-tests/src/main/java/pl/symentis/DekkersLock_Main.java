package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

@Outcome(id = "1, 1", expect = ACCEPTABLE_INTERESTING, desc = "Both actors came up with the same value: atomicity failure.")
@Outcome(id = "1, 2", expect = ACCEPTABLE,             desc = "actor1 incremented, then actor2.")
@Outcome(id = "2, 1", expect = ACCEPTABLE,             desc = "actor2 incremented, then actor1.")
public class DekkersLock_Main {
    @JCStressTest
    @JCStressMeta(DekkersLock_Main.class)
    @State
    public static class PlainTest {
        int v;
        DekkersLock lock0;
        DekkersLock lock1;

        PlainTest(){
            DekkersLockFactory lockFactory = DekkersLockFactory.get();
            lock0 = lockFactory.getLock0();
            lock1 = lockFactory.getLock1();
        }

        @Actor
        public void actor1(II_Result r) {
            lock0.lock();
            r.r1 = ++v;
            lock0.unlock();
        }

        @Actor
        public void actor2(II_Result r) {
            lock1.lock();
            r.r2 = ++v;
            lock1.unlock();
        }
    }

    @JCStressTest
    @JCStressMeta(DekkersLock_Main.class)
    @State
    public static class VolatileTest {
        volatile int v;
        DekkersLock lock0;
        DekkersLock lock1;

        VolatileTest(){
            DekkersLockFactory lockFactory = DekkersLockFactory.get();
            lock0 = lockFactory.getLock0();
            lock1 = lockFactory.getLock1();
        }

        @Actor
        public void actor1(II_Result r) {
            lock0.lock();
            r.r1 = ++v;
            lock0.unlock();
        }

        @Actor
        public void actor2(II_Result r) {
            lock1.lock();
            r.r2 = ++v;
            lock1.unlock();
        }
    }

}
