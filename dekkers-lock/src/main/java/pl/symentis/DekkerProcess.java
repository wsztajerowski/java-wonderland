package pl.symentis;

public class DekkerProcess implements  Runnable{
    private final SharedObject sharedObject;
    private final int thisProcessNumber;
    private final int otherProcessNumber;

    public DekkerProcess(SharedObject sharedObject, int processNumber) {
        this.sharedObject = sharedObject;
        this.thisProcessNumber = processNumber;
        otherProcessNumber = (processNumber + 1) % 2;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1_000_000; i++) {
            incrementCounter();
        }
    }

    private void incrementCounter() {
        sharedObject.wants_to_enter[thisProcessNumber] = true;
        while (sharedObject.wants_to_enter[otherProcessNumber]){
            if (sharedObject.turn != thisProcessNumber){
                sharedObject.wants_to_enter[thisProcessNumber] = false;
                while (sharedObject.turn != thisProcessNumber){
                    // busy wait
                }
                sharedObject.wants_to_enter[thisProcessNumber] = true;
            }
        }

        // start of critical section
        sharedObject.counter++;
        // end of critical section

        sharedObject.turn = otherProcessNumber;
        sharedObject.wants_to_enter[thisProcessNumber] = false;
    }
}
