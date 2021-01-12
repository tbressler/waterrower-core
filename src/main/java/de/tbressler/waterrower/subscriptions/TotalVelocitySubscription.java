package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_AVERAGE;
import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_TOTAL;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for the total velocity (in meters per second).
 * TODO The interpretation of this value is unknown at the moment.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class TotalVelocitySubscription extends AbstractMemorySubscription {

    /* The last velocity received (in cm/s). */
    private int lastVelocity = -1;


    /**
     * Subscription for the total velocity.
     */
    public TotalVelocitySubscription() {
        super(DOUBLE_MEMORY, M_S_LOW_TOTAL);
    }


    @Override
    protected void handle(DataMemoryMessage msg) {

        int velocity = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received velocity is the same as before,
        // don't send an update.
        if (lastVelocity == velocity)
            return;
        lastVelocity = velocity;

        double value = ((double) velocity) / 100D;

        onVelocityUpdated(value);
    }


    /**
     * Is called if the value for the total velocity was updated.
     *
     * @param velocity The new value (in meters per second).
     */
    abstract protected void onVelocityUpdated(double velocity);

}
