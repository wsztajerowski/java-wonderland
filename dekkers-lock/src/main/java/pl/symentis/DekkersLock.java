package pl.symentis;

public class DekkersLock {
  private static volatile int turn = 0;
  final int lockNo;
  volatile boolean wantToEnter;
  private DekkersLock otherLock;

  void setOtherLock(DekkersLock otherLock) {
    this.otherLock = otherLock;
  }

  DekkersLock(int lockNo) {
    this.lockNo = lockNo;
  }

  public void lock(){
    wantToEnter = true;
    while (otherLock.wantToEnter){
      if (turn != lockNo){
        wantToEnter = false;
        while (turn != lockNo){
          // busy wait
        }
        wantToEnter = true;
      }
    }
  }

  public void unlock(){
    turn = otherLock.lockNo;
    wantToEnter = false;
  }

}
