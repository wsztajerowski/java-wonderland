package pl.symentis;

public class DekkersLockFactory {
    private final DekkersLock lock0;
    private final DekkersLock lock1;
    private DekkersLockFactory(boolean useOnSpinWait) {
        DekkersLock.Turn turn = new DekkersLock.Turn();
        lock0 = new DekkersLock(0, turn, useOnSpinWait);
        lock1 = new DekkersLock(1, turn, useOnSpinWait);
        lock0.setOtherLock(lock1);
        lock1.setOtherLock(lock0);
    }

    public static DekkersLockFactory get(boolean useOnSpinWait){
        return new DekkersLockFactory(useOnSpinWait);
    }

    public static DekkersLockFactory get(){
        return new DekkersLockFactory(false);
    }

    public DekkersLock getLock0() {
        return lock0;
    }

    public DekkersLock getLock1() {
        return lock1;
    }
}
