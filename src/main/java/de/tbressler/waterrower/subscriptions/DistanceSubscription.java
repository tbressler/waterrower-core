package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;
import static java.util.Objects.requireNonNull;

/**
 * Subscription for the distance values.
 *
 * This subscription can be used for the different types of distances at the WaterRower:
 * - distance
 * - displayed distance
 * - total distance
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class DistanceSubscription extends AbstractMemorySubscription {

    /**
     * The distance mode.
     */
    public enum DistanceMode {

        /** The distance. */
        DISTANCE,

        /** The displayed distance. */
        DISPLAYED_DISTANCE,

        /** The total distance meter counter - this is stored at switch off. */
        TOTAL_DISTANCE
    }


    /* The distance mode of this subscription. */
    private final DistanceMode distanceMode;

    /* The last distance received. */
    private int lastDistance = -1;


    /**
     * Subscription to the displayed distance values.
     *
     * @param distanceMode The distance mode (e.g. total distance or displayed distance), must
     *                 not be null.
     */
    public DistanceSubscription(DistanceMode distanceMode) {
        super(DOUBLE_MEMORY, getMemoryLocation(distanceMode));
        this.distanceMode = distanceMode;
    }

    /* Returns the memory location for the given distance mode. */
    private static MemoryLocation getMemoryLocation(DistanceMode distanceMode) {
        switch (requireNonNull(distanceMode)) {
            case DISTANCE:
                return MS_DISTANCE_LOW;
            case DISPLAYED_DISTANCE:
                return DISTANCE_LOW;
            case TOTAL_DISTANCE:
                return TOTAL_DIS_LOW;
            default:
                throw new IllegalStateException("Unhandled distance mode!");
        }
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {
        int distance = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received distance is the same as before,
        // don't send an update.
        if (lastDistance == distance)
            return;

        lastDistance = distance;

        onDistanceUpdated(distanceMode, distance);
    }


    /**
     * Is called if the value for the displayed distance was updated.
     *
     * @param mode The distance mode (e.g. total distance or displayed distance), never null.
     * @param distance The new distance.
     */
    protected abstract void onDistanceUpdated(DistanceMode mode, int distance);

}
