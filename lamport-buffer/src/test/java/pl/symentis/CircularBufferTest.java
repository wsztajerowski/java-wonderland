package pl.symentis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.symentis.CircularBufferBuilder.integerCircularBufferBuilder;

class CircularBufferTest {
    private CircularBuffer<Integer> circularBuffer;
    private Long counter = 0L;

    @BeforeEach
    void setUp() {
        this.circularBuffer = integerCircularBufferBuilder()
            .withBufferSize(10_000)
            .build();
    }

    @Test
    void single_producer_single_consumer_use_case() throws InterruptedException {
        // given
        var t0 = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                while(!circularBuffer.push(i)){

                }
            }
        });
        var t1 = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                Optional<Integer> element;
                do {
                    element = circularBuffer.pop();
                } while (element.isEmpty());
                counter += element.get();
            }
        });

        // when
        t0.start();
        t1.start();

        t0.join();
        t1.join();

        //then
        assertThat(counter)
            .isEqualTo(49_999_995_000_000L);

    }

}