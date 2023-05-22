package pl.symentis;

public class DekkersLock {
  private final SharedObject sharedObject;
  private final DekkerProcess process0;
  private final DekkerProcess process1;


  public DekkersLock() {
    sharedObject = new SharedObject();
    process0 = new DekkerProcess(sharedObject, 0);
    process1 = new DekkerProcess(sharedObject, 1);
  }

  public long startTest() throws InterruptedException {
    Thread firstThread = new Thread(process0);
    firstThread.start();
    Thread secondThread = new Thread(process1);
    secondThread.start();

    firstThread.join();
    secondThread.join();

    return sharedObject.counter;
  }
}
