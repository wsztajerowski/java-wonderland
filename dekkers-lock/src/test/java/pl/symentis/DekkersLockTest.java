package pl.symentis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DekkersLockTest {

  private DekkerLockFactory lockFactory;

  @BeforeEach
  void setUp() {
    this.lockFactory = DekkerLockFactory.get();
  }

  @Test
  void dekker_algorithm_returns_valid_result() throws InterruptedException {
    // given
    DekkersLock lock0 = lockFactory.getLock0();
    DekkersLock lock1 = lockFactory.getLock1();
    SharedObject sharedObject = new SharedObject();
    var t0 = new Thread(() -> {
      for (int i = 0; i < 10_000_000; i++) {
        lock0.lock();
        sharedObject.counter++;
        lock0.unlock();
      }
    });
    var t1 = new Thread(() -> {
      for (int i = 0; i < 10_000_000; i++) {
        lock1.lock();
        sharedObject.counter++;
        lock1.unlock();
      }
    });

    // when
    t0.start();
    t1.start();

    t0.join();
    t1.join();

    //then
    assertThat(sharedObject.counter)
        .isEqualTo(20_000_000);

  }


}
