package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.STROKE_AVERAGE;
import static de.tbressler.waterrower.model.MemoryLocation.STROKE_PULL;
import static java.util.Objects.requireNonNull;

/**
 * Subscription for average stroke time value (e.g. whole stroke or pull-only).
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class AverageStrokeTimeSubscription extends AbstractMemorySubscription {

    // TODO Rename this enum. StrokeType already exists.
    public enum StrokeType {

        /* Average time for a whole stroke. */
        WHOLE_STROKE,

        /* Average time for a pull (acc to dec). */
        PULL_ONLY
    }


    /* The last average stroke time received. */
    private int lastAverageStrokeTime = -1;


    /**
     * Subscription for average stroke time value (e.g. whole stroke or pull).
     *
     * @param strokeType The stroke type (e.g. whole stroke or pull), must not be null.
     */
    public AverageStrokeTimeSubscription(StrokeType strokeType) {
        super(SINGLE_MEMORY, getMemoryLocation(strokeType));
    }

    /* Returns the memory location for the given stroke type. */
    private static MemoryLocation getMemoryLocation(StrokeType strokeType) {
        switch (requireNonNull(strokeType)) {
            case WHOLE_STROKE:
                return STROKE_AVERAGE;
            case PULL_ONLY:
                return STROKE_PULL;
            default:
                throw new IllegalStateException("Unhandled stroke type!");
        }
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        int averageStrokeTime = msg.getValue1();

        // If the received duration is the same as before,
        // don't send an update.
        if (lastAverageStrokeTime == averageStrokeTime)
            return;

        lastAverageStrokeTime = averageStrokeTime;

        onAverageStrokeTimeUpdated(averageStrokeTime);
    }


    /**
     * Is called if the value for the average stroke time was updated.
     *
     * @param averageStrokeTime The new average stroke time.
     */
    abstract protected void onAverageStrokeTimeUpdated(int averageStrokeTime);

}
