package symentis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.symentis.LockBasedCircularBuffer;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.symentis.LockBasedCircularBufferBuilder.integerCircularBufferBuilder;

class LockBasedCircularBufferTest {
    private LockBasedCircularBuffer<Integer> circularBuffer;
    private AtomicLong counter;

    @BeforeEach
    void setUp() {
        counter = new AtomicLong(0L);
        this.circularBuffer = integerCircularBufferBuilder()
            .withBufferSize(10_000)
            .build();
    }

    @Test
    void single_producer_single_consumer_use_case() throws InterruptedException {
        // given
        var t0 = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                circularBuffer.push(i);
            }
        });
        var t1 = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                counter.addAndGet(circularBuffer.pop());
            }
        });

        // when
        t0.start();
        t1.start();

        t0.join();
        t1.join();

        //then
        assertThat(counter.get())
            .isEqualTo(49_999_995_000_000L);

    }

    @Test
    void multiple_producer_single_consumer_use_case() throws InterruptedException {
        // given
        var p1 = new Thread(() -> {
            for (int i = 0; i < 2_500_000; i++) {
                circularBuffer.push(i);
            }
        });
        var p2 = new Thread(() -> {
            for (int i = 2_500_000; i < 5_000_000; i++) {
                circularBuffer.push(i);
            }
        });
        var p3 = new Thread(() -> {
            for (int i = 5_000_000; i < 7_500_000; i++) {
                circularBuffer.push(i);
            }
        });
        var p4 = new Thread(() -> {
            for (int i = 7_500_000; i < 10_000_000; i++) {
                circularBuffer.push(i);
            }
        });
        var c1 = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                counter.addAndGet(circularBuffer.pop());
            }
        });

        // when
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        c1.start();

        p1.join();
        p2.join();
        p3.join();
        p4.join();
        c1.join();

        //then
        assertThat(counter.get())
            .isEqualTo(49_999_995_000_000L);

    }

    @Test
    void single_producer_multiple_consumer_use_case() throws InterruptedException {
        // given
        var p1 = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                circularBuffer.push(i);
            }
        });
        Runnable consumerLambda = () -> {
            for (int i = 0; i < 2_500_000; i++) {
                counter.addAndGet(circularBuffer.pop());
            }
        };
        var c1 = new Thread(consumerLambda);
        var c2 = new Thread(consumerLambda);
        var c3 = new Thread(consumerLambda);
        var c4 = new Thread(consumerLambda);

        // when
        p1.start();
        c1.start();
        c2.start();
        c3.start();
        c4.start();

        p1.join();
        c1.join();
        c2.join();
        c3.join();
        c4.join();

        //then
        assertThat(counter.get())
            .isEqualTo(49_999_995_000_000L);

    }

    @Test
    void multiple_producer_multiple_consumer_use_case() throws InterruptedException {
        // given
        var p1 = new Thread(() -> {
            for (int i = 0; i < 2_500_000; i++) {
                circularBuffer.push(i);
            }
        });
        var p2 = new Thread(() -> {
            for (int i = 2_500_000; i < 5_000_000; i++) {
                circularBuffer.push(i);
            }
        });
        var p3 = new Thread(() -> {
            for (int i = 5_000_000; i < 7_500_000; i++) {
                circularBuffer.push(i);
            }
        });
        var p4 = new Thread(() -> {
            for (int i = 7_500_000; i < 10_000_000; i++) {
                circularBuffer.push(i);
            }
        });
        Runnable consumerLambda = () -> {
            for (int i = 0; i < 2_500_000; i++) {
                counter.addAndGet(circularBuffer.pop());
            }
        };
        var c1 = new Thread(consumerLambda);
        var c2 = new Thread(consumerLambda);
        var c3 = new Thread(consumerLambda);
        var c4 = new Thread(consumerLambda);

        // when
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        c1.start();
        c2.start();
        c3.start();
        c4.start();

        p1.join();
        p2.join();
        p3.join();
        p4.join();
        c1.join();
        c2.join();
        c3.join();
        c4.join();

        //then
        assertThat(counter.get())
            .isEqualTo(49_999_995_000_000L);

    }

}