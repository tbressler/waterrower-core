package de.tbressler.waterrower.watchdog;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

import static de.tbressler.waterrower.watchdog.TimeoutReason.PING_TIMEOUT;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
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
    private ITimeoutListener listener = mock(ITimeoutListener.class, "listener");

    // Capture:
    private ArgumentCaptor<Runnable> task = forClass(Runnable.class);


    // Constructor:

    @Test
    public void new_withNullInterval_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new PingWatchdog(null, executor));
    }

    @Test
    public void new_withNullExecutor_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new PingWatchdog(interval, null));
    }

    // Start:

    @Test
    public void start_schedulesTask() {
        pingWatchdog = new PingWatchdog(ofMillis(1), executor);
        pingWatchdog.setTimeoutListener(listener);

        pingWatchdog.start();

        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    // Task execution:

    @Test
    public void callRunnable_afterStartWithoutPingAfter50ms_executesOnTimeout() {
        pingWatchdog = new PingWatchdog(ofMillis(1), executor);
        pingWatchdog.setTimeoutListener(listener);

        pingWatchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        sleepUninterruptedly(50);

        task.getValue().run();

        verify(listener, times(1)).onTimeout(PING_TIMEOUT);
        verify(executor, times(2)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStartWithPingReceivedInTime_executesOnTimeout() {
        pingWatchdog = new PingWatchdog(ofMillis(1000), executor);
        pingWatchdog.setTimeoutListener(listener);

        pingWatchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1000), eq(MILLISECONDS));

        sleepUninterruptedly(50);

        pingWatchdog.pingReceived();
        task.getValue().run();

        verify(listener, never()).onTimeout(PING_TIMEOUT);
        verify(executor, times(2)).schedule(any(Runnable.class), eq((long)1000), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStop_doesntExecuteOnTimeout() {
        pingWatchdog = new PingWatchdog(ofMillis(1), executor);
        pingWatchdog.setTimeoutListener(listener);

        pingWatchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        pingWatchdog.stop();

        sleepUninterruptedly(50);

        task.getValue().run();

        verify(listener, never()).onTimeout(PING_TIMEOUT);
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }


    // Helper methods:

    private void sleepUninterruptedly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}