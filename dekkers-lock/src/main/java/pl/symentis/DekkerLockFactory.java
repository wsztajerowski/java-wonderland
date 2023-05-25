package pl.symentis;

public class DekkerLockFactory {
    private final DekkersLock lock0;
    private final DekkersLock lock1;
    private DekkerLockFactory() {
        lock0 = new DekkersLock(0);
        lock1 = new DekkersLock(1);
        lock0.setOtherLock(lock1);
        lock1.setOtherLock(lock0);
    }

    public static DekkerLockFactory get(){
        return new DekkerLockFactory();
    }

    public DekkersLock getLock0() {
        return lock0;
    }

    public DekkersLock getLock1() {
        return lock1;
    }
}
