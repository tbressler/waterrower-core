package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.subscriptions.AbstractMemorySubscription;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.KCAL_WATTS_LOW;
import static de.tbressler.waterrower.subscriptions.Priority.HIGH;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for the watt value.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class WattsSubscription extends AbstractMemorySubscription {

    /* The last stroke count received. */
    private int lastValue = -1;


    /**
     * Subscription for the watt value.
     */
    public WattsSubscription() {
        super(HIGH, DOUBLE_MEMORY, KCAL_WATTS_LOW);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        int watt = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received watt is the same as before,
        // don't send an update.
        if (lastValue == watt)
            return;
        lastValue = watt;

        onWattsUpdated(watt);
    }


    /**
     * Is called if the value for watts was updated.
     *
     * @param watt The new value (in watt).
     */
    abstract protected void onWattsUpdated(int watt);

}
