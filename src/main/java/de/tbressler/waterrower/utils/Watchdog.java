package de.tbressler.waterrower.utils;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A watchdog.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class Watchdog {

    /* The wakeup interval for the watchdog. */
    private Duration interval;

    /* Repeat the watchdog periodically. */
    private final boolean doRepeat;

    /* The executor service. */
    private final ScheduledExecutorService executorService;

    /* True if watchdog is stopped. */
    private AtomicBoolean isStopped = new AtomicBoolean(true);


    /**
     * A watchdog.
     *
     * @param interval The interval, must not be null.
     * @param repeat True if the watchdog task should be repeated periodically.
     * @param executorService The scheduled executor service, must not be null.
     */
    public Watchdog(Duration interval, boolean repeat, ScheduledExecutorService executorService) {
        this.interval = requireNonNull(interval);
        this.doRepeat = repeat;
        this.executorService = requireNonNull(executorService);
    }


    /**
     * Starts the watchdog.
     */
    public void start() {
        isStopped.set(false);
        scheduleWatchdogTask();
    }

    private void scheduleWatchdogTask() {
        executorService.schedule(() -> executeWatchdogTask(), interval.toMillis(), MILLISECONDS);
    }

    private void executeWatchdogTask() {

        // Check if already stopped.
        if (isStopped.get())
            return;

        wakeUpAndCheck();

        // Start the next period if the task should
        // be executed periodically.
        if (doRepeat && !isStopped.get())
            scheduleWatchdogTask();
    }


    /**
     * The task that should be executed, when the watchdog wakes up.
     */
    protected abstract void wakeUpAndCheck();


    /**
     * Stops the watchdog.
     */
    public void stop() {
        isStopped.set(true);
    }

}
