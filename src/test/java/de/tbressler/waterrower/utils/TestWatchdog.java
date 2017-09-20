package de.tbressler.waterrower.utils;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for class Watchdog.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWatchdog {

    // Class under test.
    private Watchdog watchdog;

    // Mocks:
    private Duration interval = ofSeconds(2);
    private ScheduledExecutorService executor = mock(ScheduledExecutorService.class, "executor");

    // Capture:
    private ArgumentCaptor<Runnable> task = forClass(Runnable.class);


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullInterval_throwsNPE() {
        new Watchdog(null, false, executor) {
            @Override
            protected void wakeUpAndCheck() {}
        };
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullExecutor_throwsNPE() {
        new Watchdog(interval, false, null) {
            @Override
            protected void wakeUpAndCheck() {}
        };
    }

    // Start:

    @Test
    public void start_schedulesTaskWith2Seconds() {
        watchdog = newWatchdog(ofSeconds(2), false);

        watchdog.start();

        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)2000), eq(MILLISECONDS));
    }

    @Test
    public void start_schedulesTaskWith1Seconds() {
        watchdog = newWatchdog(ofSeconds(1), false);

        watchdog.start();

        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1000), eq(MILLISECONDS));
    }

    // Helper methods:

    private Watchdog newWatchdog(Duration duration, boolean repeat) {
        return new Watchdog(duration, repeat, executor) {
            @Override
            protected void wakeUpAndCheck() {}
        };
    }

}