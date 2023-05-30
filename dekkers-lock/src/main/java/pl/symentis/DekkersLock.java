package pl.symentis;

public class DekkersLock {
  private Turn turn;
  final int lockNo;
  volatile boolean wantToEnter;
  private DekkersLock otherLock;

  void setOtherLock(DekkersLock otherLock) {
    this.otherLock = otherLock;
  }

  DekkersLock(int lockNo, Turn turn) {
    this.lockNo = lockNo;
    this.turn = turn;
  }

  static class Turn {
    public volatile int turn = 0;
  }

  public void lock(){
    wantToEnter = true;
    while (otherLock.wantToEnter){
      if (turn.turn != lockNo){
        wantToEnter = false;
        while (turn.turn != lockNo){
          // busy wait
        }
        wantToEnter = true;
      }
    }
  }

  public void unlock(){
    turn.turn = otherLock.lockNo;
    wantToEnter = false;
  }

}
