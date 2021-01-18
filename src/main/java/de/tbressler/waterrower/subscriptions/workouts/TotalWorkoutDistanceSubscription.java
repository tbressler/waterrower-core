package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.subscriptions.AbstractMemorySubscription;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_MS_L;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for values of the total workout distance.
 * The distance is updated by the WaterRower after each workout interval.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class TotalWorkoutDistanceSubscription extends AbstractMemorySubscription {

    /* The last value received. */
    private int lastValue = -1;


    /**
     * Subscription for values of the total workout distance.
     * The distance is updated by the WaterRower after each workout interval.
     */
    public TotalWorkoutDistanceSubscription() {
        super(DOUBLE_MEMORY, WORKOUT_MS_L);
    }


    @Override
    protected void handle(DataMemoryMessage msg) {

        int value = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received value is the same as before,
        // don't send an update.
        if (lastValue == value)
            return;
        lastValue = value;

        onTotalWorkoutDistanceUpdated(value);
    }


    /**
     * Is called if the total workout distance value was updated.
     *
     * @param distance The new workout distance.
     */
    abstract protected void onTotalWorkoutDistanceUpdated(int distance);

}
