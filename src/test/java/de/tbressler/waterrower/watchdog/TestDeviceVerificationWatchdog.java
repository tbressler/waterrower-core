package de.tbressler.waterrower.watchdog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

import static de.tbressler.waterrower.watchdog.TimeoutReason.DEVICE_NOT_CONFIRMED_TIMEOUT;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * Tests for class DeviceVerificationWatchdog.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestDeviceVerificationWatchdog {

    // Class under test.
    private DeviceVerificationWatchdog watchdog;

    // Mocks:
    private Duration interval = ofSeconds(2);
    private ScheduledExecutorService executor = mock(ScheduledExecutorService.class, "executor");
    private ITimeoutListener listener = mock(ITimeoutListener.class, "listener");

    // Capture:
    private ArgumentCaptor<Runnable> task = forClass(Runnable.class);


    @BeforeEach
    public void setUp() {
        watchdog = new DeviceVerificationWatchdog(ofMillis(1), executor);
        watchdog.setTimeoutListener(listener);
    }


    // Constructor:

    @Test
    public void new_withNullInterval_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new DeviceVerificationWatchdog(null, executor));
    }

    @Test
    public void new_withNullExecutor_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new DeviceVerificationWatchdog(interval, null));
    }

    // Start:

    @Test
    public void start_schedulesTask() {

        watchdog.start();

        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    // Task execution:

    @Test
    public void callRunnable_afterStartWithoutPingAfter50ms_executesOnTimeout() {
        watchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        task.getValue().run();

        verify(listener, times(1)).onTimeout(DEVICE_NOT_CONFIRMED_TIMEOUT);
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStartWithPingReceivedInTime_executesOnTimeout() {
        watchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        watchdog.setDeviceConfirmed(true);
        task.getValue().run();

        verify(listener, never()).onTimeout(DEVICE_NOT_CONFIRMED_TIMEOUT);
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStop_doesntExecuteOnTimeout() {
        watchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        watchdog.stop();

        task.getValue().run();

        verify(listener, never()).onTimeout(DEVICE_NOT_CONFIRMED_TIMEOUT);
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

}