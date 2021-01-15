package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_INTER;

/**
 * Subscription for the number of configured workout intervals at the
 * Performance Monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class WorkoutIntervalsSubscription extends AbstractMemorySubscription {

    /* The last value received. */
    private int lastValue = -1;


    /**
     * Subscription for the number of configured workout intervals at the
     * Performance Monitor.
     */
    public WorkoutIntervalsSubscription() {
        super(SINGLE_MEMORY, WORKOUT_INTER);
    }


    @Override
    protected void handle(DataMemoryMessage msg) {

        int value = msg.getValue1();

        // If the received value is the same as before,
        // don't send an update.
        if (lastValue == value)
            return;
        lastValue = value;

        onWorkoutIntervalsUpdated(value);
    }


    /**
     * Is called if the value for the number of workout intervals was updated.
     *
     * @param intervals The new value.
     */
    abstract protected void onWorkoutIntervalsUpdated(int intervals);

}
