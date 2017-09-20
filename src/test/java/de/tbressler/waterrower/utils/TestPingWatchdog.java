package de.tbressler.waterrower.utils;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class PingWatchdog.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPingWatchdog {

    // Class under test.
    private PingWatchdog pingWatchdog;

    // Mocks:
    private Duration interval = ofSeconds(2);
    private ScheduledExecutorService executor = mock(ScheduledExecutorService.class, "executor");
    private PingWatchdog internalWatchdog = mock(PingWatchdog.class, "internalWatchdog");

    // Capture:
    private ArgumentCaptor<Runnable> task = forClass(Runnable.class);


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullInterval_throwsNPE() {
        new PingWatchdog(null, executor) {
            @Override
            protected void onTimeout() {}
        };
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullExecutor_throwsNPE() {
        new PingWatchdog(interval, null) {
            @Override
            protected void onTimeout() {}
        };
    }

    // Start:

    @Test
    public void start_schedulesTask() {
        pingWatchdog = newPingWatchdog(ofMillis(1));

        pingWatchdog.start();

        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    // Task execution:

    @Test
    public void callRunnable_afterStartWithoutPingAfter50ms_executesOnTimeout() {
        pingWatchdog = newPingWatchdog(ofMillis(1));

        pingWatchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        sleepUninterruptedly(50);

        task.getValue().run();

        verify(internalWatchdog, times(1)).onTimeout();
        verify(executor, times(2)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStartWithPingReceivedInTime_executesOnTimeout() {
        pingWatchdog = newPingWatchdog(ofMillis(1000));

        pingWatchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1000), eq(MILLISECONDS));

        sleepUninterruptedly(50);

        pingWatchdog.pingReceived();
        task.getValue().run();

        verify(internalWatchdog, never()).onTimeout();
        verify(executor, times(2)).schedule(any(Runnable.class), eq((long)1000), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStop_doesntExecuteOnTimeout() {
        pingWatchdog = newPingWatchdog(ofMillis(1));

        pingWatchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        pingWatchdog.stop();

        sleepUninterruptedly(50);

        task.getValue().run();

        verify(internalWatchdog, never()).onTimeout();
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    // Helper methods:

    private PingWatchdog newPingWatchdog(Duration duration) {
        return new PingWatchdog(duration, executor) {
            @Override
            protected void onTimeout() {
                internalWatchdog.onTimeout();
            }
        };
    }

    private void sleepUninterruptedly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}