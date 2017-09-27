package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.DISTANCE_LOW;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription to the displayed distance value.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class DisplayedDistanceSubscription extends AbstractMemorySubscription {

    /* The last distance received. */
    private int lastDistance = -1;


    /**
     * Subscription to the displayed distance value.
     */
    public DisplayedDistanceSubscription() {
        super(DOUBLE_MEMORY, DISTANCE_LOW);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {
        int distance = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received workout flags are the same as before,
        // don't send an update.
        if (lastDistance == distance)
            return;

        lastDistance = distance;

        onDistanceUpdated(distance);
    }


    /**
     * Is called if the value for the displayed distance was updated.
     *
     * @param distance The new distance.
     */
    protected abstract void onDistanceUpdated(int distance);

}
