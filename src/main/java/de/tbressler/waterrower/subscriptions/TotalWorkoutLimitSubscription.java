package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_LIMIT_L;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for values of the total workout limit.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class TotalWorkoutLimitSubscription extends AbstractMemorySubscription {

    /* The last value received. */
    private int lastValue = -1;


    /**
     * Subscription for values of the total workout limit.
     */
    public TotalWorkoutLimitSubscription() {
        super(DOUBLE_MEMORY, WORKOUT_LIMIT_L);
    }


    @Override
    protected void handle(DataMemoryMessage msg) {

        int value = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received value is the same as before,
        // don't send an update.
        if (lastValue == value)
            return;
        lastValue = value;

        onTotalWorkoutLimitUpdated(value);
    }


    /**
     * Is called if the total workout limit value was updated.
     *
     * @param limit The new workout limit.
     */
    abstract protected void onTotalWorkoutLimitUpdated(int limit);

}