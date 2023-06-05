package pl.symentis;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class Incrementing_DekkersLock {

    int v;
    DekkersLock lock1;
    DekkersLock lock2;

    @Setup
    public void setup(){
        DekkersLockFactory factory = DekkersLockFactory.get();
        lock1 = factory.getLock0();
        lock2 = factory.getLock1();
    }


    @Benchmark
    @GroupThreads(1)
    @Group()
    public int incrementUsingLock1() {
        lock1.lock();
        v++;
        lock1.unlock();
        return v;
    }

    @Benchmark
    @GroupThreads(1)
    @Group()
    public int incrementUsingLock2() {
        lock2.lock();
        v++;
        lock2.unlock();
        return v;
    }

}
