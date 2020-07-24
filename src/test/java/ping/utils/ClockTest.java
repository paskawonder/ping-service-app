package ping.utils;

import com.docler.ping.utils.Clock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ClockTest {

    private final Clock clock = new Clock();

    @Test
    void timestampTest() {
        final long before = System.currentTimeMillis();
        final long now = clock.timestamp();
        Assertions.assertTrue(now >= before);
        final long after = System.currentTimeMillis();
        Assertions.assertTrue(now <= after);
    }

}
