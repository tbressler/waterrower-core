package de.tbressler.waterrower.utils;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Objects.requireNonNull;

/**
 * A watchdog.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class Watchdog {

    /* The wakeup interval for the watchdog. */
    private Duration interval;

    /* The timer. */
    private final Timer timer;


    /**
     * A watchdog.
     *
     * @param interval The interval, must not be null.
     * @param name The internal name for the timer.
     */
    public Watchdog(Duration interval, String name) {
        this.interval = requireNonNull(interval);
        this.timer = new Timer(name);
    }

    /**
     * A watchdog.
     *
     * @param interval The interval, must not be null.
     * @param isDaemon True if the associated thread should run as a daemon.
     * @param name The internal name for the timer.
     */
    public Watchdog(Duration interval, boolean isDaemon, String name) {
        this.interval = requireNonNull(interval);
        this.timer = new Timer(name, isDaemon);
    }


    /**
     * Starts the watchdog.
     */
    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wakeUpAndCheck();
            }
        }, interval.toMillis());
    }


    /**
     * The task that should be executed, when the watchdog wakes up.
     */
    protected abstract void wakeUpAndCheck();


    /**
     * Resets the watchdog.
     */
    public void reset() {
        stop();
        start();
    }


    /**
     * Stops the watchdog.
     */
    public void stop() {
        timer.cancel();
        timer.purge();
    }

}
