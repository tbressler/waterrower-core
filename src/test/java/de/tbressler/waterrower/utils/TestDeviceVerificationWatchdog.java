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
    private DeviceVerificationWatchdog internalWatchdog = mock(DeviceVerificationWatchdog.class, "internalWatchdog");

    // Capture:
    private ArgumentCaptor<Runnable> task = forClass(Runnable.class);


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullInterval_throwsNPE() {
        new DeviceVerificationWatchdog(null, executor) {
            @Override
            protected void onDeviceNotConfirmed() {}
        };
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullExecutor_throwsNPE() {
        new DeviceVerificationWatchdog(interval, null) {
            @Override
            protected void onDeviceNotConfirmed() {}
        };
    }

    // Start:

    @Test
    public void start_schedulesTask() {
        watchdog = newDeviceVerificationWatchdog(ofMillis(1));

        watchdog.start();

        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    // Task execution:

    @Test
    public void callRunnable_afterStartWithoutPingAfter50ms_executesOnTimeout() {
        watchdog = newDeviceVerificationWatchdog(ofMillis(1));

        watchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        task.getValue().run();

        verify(internalWatchdog, times(1)).onDeviceNotConfirmed();
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStartWithPingReceivedInTime_executesOnTimeout() {
        watchdog = newDeviceVerificationWatchdog(ofMillis(1000));

        watchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1000), eq(MILLISECONDS));

        watchdog.setDeviceConfirmed(true);
        task.getValue().run();

        verify(internalWatchdog, never()).onDeviceNotConfirmed();
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1000), eq(MILLISECONDS));
    }

    @Test
    public void callRunnable_afterStop_doesntExecuteOnTimeout() {
        watchdog = newDeviceVerificationWatchdog(ofMillis(1));

        watchdog.start();

        verify(executor, times(1)).schedule(task.capture(), eq((long)1), eq(MILLISECONDS));

        watchdog.stop();

        task.getValue().run();

        verify(internalWatchdog, never()).onDeviceNotConfirmed();
        verify(executor, times(1)).schedule(any(Runnable.class), eq((long)1), eq(MILLISECONDS));
    }

    // Helper methods:

    private DeviceVerificationWatchdog newDeviceVerificationWatchdog(Duration duration) {
        return new DeviceVerificationWatchdog(duration, executor) {
            @Override
            protected void onDeviceNotConfirmed() {
                internalWatchdog.onDeviceNotConfirmed();
            }
        };
    }

}