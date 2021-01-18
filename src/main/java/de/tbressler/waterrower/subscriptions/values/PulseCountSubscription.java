package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.PulseCountMessage;
import de.tbressler.waterrower.subscriptions.ISubscription;
import de.tbressler.waterrower.subscriptions.Priority;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * A subscription for pulse count events.
 *
 * Will be called, when pulse count was updated. The value is representing the number of
 * pulse’s counted during the last 25mS period; this value can range from 1 to 50
 * typically. (Zero values will not be transmitted).
 *
 * This packet is auto transmitted by the rowing computer.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class PulseCountSubscription implements ISubscription {

    @Override
    public Priority getPriority() {
        return Priority.NO_POLLING;
    }

    @Override
    public final AbstractMessage poll() {
        // No poll necessary! Pulse count will be send automatically by
        // the WaterRower Performance Monitor.
        return null;
    }

    @Override
    public final void handle(AbstractMessage msg) {
        if (!(msg instanceof PulseCountMessage))
            return;
        onPulseCount(((PulseCountMessage) msg).getPulsesCounted());
    }


    /**
     * Will be called, when pulse count was updated. The value is representing the number of
     * pulse’s counted during the last 25mS period; this value can range from 1 to 50
     * typically. (Zero values will not be transmitted).
     *
     * @param pulsesCount The number of pulse’s counted during the last 25mS period.
     */
    abstract protected void onPulseCount(int pulsesCount);


    @Override
    public String toString() {
        return toStringHelper(this)
                .toString();
    }

}