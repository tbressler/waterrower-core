package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.PulseCountMessage;

/**
 * A subscription for pulse count events.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class PulseCountSubscription implements ISubscription {

    @Override
    public AbstractMessage poll() {
        // No poll necessary! Pulse count will be send automatically by WaterRower monitor.
        return null;
    }

    @Override
    public void handle(AbstractMessage msg) {
        if (msg instanceof PulseCountMessage) {
            onPulseCount(((PulseCountMessage) msg).getPulsesCounted());
        }
    }


    /**
     * Will be called, when pulse count was updated. The value is representing the number of
     * pulse’s counted during the last 25mS period; this value can range from 1 to 50
     * typically. (Zero values will not be transmitted).
     *
     * @param pulsesCount The number of pulse’s counted during the last 25mS period.
     */
    abstract void onPulseCount(int pulsesCount);

}