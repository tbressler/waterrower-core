package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;
import static java.util.Objects.requireNonNull;

/**
 * Subscription for values of the workout total times/distances/limits.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
@Deprecated
public abstract class WorkoutTotalSubscription extends AbstractMemorySubscription {

    /**
     * The value type.
     */
    public enum ValueType {

        /** Total workout time. */
        TIME,

        /** Total workout m/s. */
        INTENSITY,

        /** Total workout strokes. */
        STROKES,

        /** This is the limit value for workouts. */
        LIMIT
    }


    /* The value type of this subscription. */
    private final ValueType valueType;

    /* The last value received. */
    private int lastValue = -1;


    /**
     * Subscription for values of the workout total times/distances/limits.
     *
     * @param valueType The value type, must not be null.
     */
    public WorkoutTotalSubscription(ValueType valueType) {
        super(DOUBLE_MEMORY, getMemoryLocation(valueType));
        this.valueType = valueType;
    }

    /* Returns the memory location for the given value type. */
    private static MemoryLocation getMemoryLocation(ValueType valueType) {
        switch (requireNonNull(valueType)) {
            case TIME:
                return WORKOUT_TIMEL; // total workout duration in seconds
            case INTENSITY:
                return WORKOUT_MS_L; // total workout distance in meters
            case STROKES:
                return WORKOUT_STROKEL; // total workout strokes
            case LIMIT:
                return WORKOUT_LIMIT_H;
            default:
                throw new IllegalStateException("Unhandled value type!");
        }
    }


    @Override
    protected void handle(DataMemoryMessage msg) {
        int value = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received value is the same as before,
        // don't send an update.
        if (lastValue == value)
            return;

        lastValue = value;

        onTotalWorkoutValueUpdated(valueType, value);
    }


    /**
     * Is called if the value was updated.
     *
     * @param valueType The value type, never null.
     * @param value The new value.
     */
    abstract protected void onTotalWorkoutValueUpdated(ValueType valueType, int value);

}
