package pl.symentis;

public class DekkersLockFactory {
    private final DekkersLock lock0;
    private final DekkersLock lock1;
    private DekkersLockFactory() {
        DekkersLock.Turn turn = new DekkersLock.Turn();
        lock0 = new DekkersLock(0, turn);
        lock1 = new DekkersLock(1, turn);
        lock0.setOtherLock(lock1);
        lock1.setOtherLock(lock0);
    }

    public static DekkersLockFactory get(){
        return new DekkersLockFactory();
    }

    public DekkersLock getLock0() {
        return lock0;
    }

    public DekkersLock getLock1() {
        return lock1;
    }
}
