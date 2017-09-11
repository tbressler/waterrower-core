package de.tbressler.waterrower.utils;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Objects.requireNonNull;

/**
 * A watchdog that checks
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class Watchdog {

    private Duration interval;

    private final Timer timer;

    /**
     *
     * @param interval The interval, must not be null.
     * @param name The internal name for the timer.
     */
    public Watchdog(Duration interval, String name) {
        this.interval = requireNonNull(interval);
        this.timer = new Timer(name);
    }

    public Watchdog(Duration interval, boolean isDaemon, String name) {
        this.interval = requireNonNull(interval);
        this.timer = new Timer(name, isDaemon);
    }


    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wakeUpAndCheck();
            }
        }, interval.toMillis());
    }


    protected abstract void wakeUpAndCheck();


    public void reset() {
        stop();
        start();
    }


    public void stop() {
        timer.cancel();
        timer.purge();
    }

}
