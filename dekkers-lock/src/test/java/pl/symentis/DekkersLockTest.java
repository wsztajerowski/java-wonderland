package pl.symentis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DekkersLockTest {

  private DekkersLock sut;

  @BeforeEach
  void setUp() {
    this.sut = new DekkersLock();
  }

  @Test
  void dekker_algorithm_ends_without_any_exception() {
    // when
    Exception exception = Assertions.catchException(() -> sut.startTest());

    //then
    assertThat(exception)
        .doesNotThrowAnyException();
  }

  @Test
  void dekker_algorithm_returns_valid_result() throws InterruptedException {
    // when
    long testResult = sut.startTest();

    //then
    assertThat(testResult)
        .isEqualTo(2_000_000);
  }
}
