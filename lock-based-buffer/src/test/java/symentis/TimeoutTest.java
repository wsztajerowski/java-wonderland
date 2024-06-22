package symentis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.symentis.LockBasedCircularBuffer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static pl.symentis.LockBasedCircularBufferBuilder.integerCircularBufferBuilder;

class TimeoutTest {
    private LockBasedCircularBuffer<Integer> circularBuffer;

    @BeforeEach
    void setUp() {
        this.circularBuffer = integerCircularBufferBuilder()
            .withBufferSize(1)
            .build();
    }

    @Test
    void read_value_from_buffer_timeouts_after_given_delay() {
        //when
        Exception caughtException = catchException(() -> circularBuffer
            .pop(3));

        // then
        assertThat(caughtException)
            .isExactlyInstanceOf(TimeoutException.class)
            .hasNoCause();
    }

    @Test
    void pop_method_canceled_operation_after_timeout_occurred(){
        // when
        Exception caughtException = catchException(() -> circularBuffer.pop(2));

        // and
        try (ScheduledExecutorService executorService = newScheduledThreadPool(1)) {
            executorService.schedule(() -> circularBuffer.push(1), 2, TimeUnit.MILLISECONDS);
        }

        // then
        assertDoesNotThrow(() -> circularBuffer.pop(2));
        // and
        assertThat(caughtException)
            .isExactlyInstanceOf(TimeoutException.class);
    }

    @Test
    void pop_method_returns_correct_value_without_timeout_if_there_is_value_to_read() {
        // given
        circularBuffer.push(1);

        // when
        Exception caughtException = catchException(() -> circularBuffer.pop(1));

        // then
        assertThat(caughtException)
            .doesNotThrowAnyException();
    }
}