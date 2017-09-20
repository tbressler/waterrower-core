package de.tbressler.waterrower.utils;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class DeviceVerificationWatchdog extends Watchdog {

    private AtomicBoolean deviceConfirmed = new AtomicBoolean(false);


    public DeviceVerificationWatchdog(Duration duration, ScheduledExecutorService executorService) {
        super(duration, false, executorService);
    }


    public void setDeviceConfirmed(boolean isConfirmed) {
        deviceConfirmed.set(isConfirmed);
    }

    public boolean isDeviceConfirmed() {
        return deviceConfirmed.get();
    }


    @Override
    protected final void wakeUpAndCheck() {
        if (!isDeviceConfirmed())
            onDeviceNotConfirmed();
    }

    abstract protected void onDeviceNotConfirmed();

    @Override
    public void start() {
        setDeviceConfirmed(false);
        super.start();
    }


    @Override
    public void stop() {
        super.stop();
    }

}
