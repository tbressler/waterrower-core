package de.tbressler.waterrower.utils;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.currentTimeMillis;

/**
 *
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class PingWatchdog extends Watchdog {

    private final Duration maxPingDuration;

    /* Last time a ping was received. */
    private AtomicLong lastReceivedPing = new AtomicLong(0);


    public PingWatchdog(Duration duration, ScheduledExecutorService executorService) {
        super(duration, true, executorService);
        this.maxPingDuration = duration;
    }


    public void pingReceived() {
        lastReceivedPing.set(currentTimeMillis());
    }


    @Override
    protected void wakeUpAndCheck() {
        if (currentTimeMillis() - lastReceivedPing.get() > maxPingDuration.toMillis())
            onTimeout();
    }


    abstract protected void onTimeout();


    @Override
    public void start() {
        pingReceived();
        super.start();
    }


    @Override
    public void stop() {
        super.stop();
    }

}
