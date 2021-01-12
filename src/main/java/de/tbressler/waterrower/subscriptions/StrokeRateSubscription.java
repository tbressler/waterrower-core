package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.STROKE_AVERAGE;

/**
 * Subscription for the average stroke rate (strokes/min) of a whole stroke which is displayed in
 * the stroke rate window of the Performance Monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class StrokeRateSubscription extends AbstractMemorySubscription {

    /* The last stroke rate received. */
    private int lastStrokeRate = -1;


    /**
     * Subscription for the average stroke rate (strokes/min) of a whole stroke which is displayed in
     * the stroke rate window of the Performance Monitor.
     */
    public StrokeRateSubscription() {
        super(SINGLE_MEMORY, STROKE_AVERAGE);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        int strokeRate = msg.getValue1();

        // If the received duration is the same as before,
        // don't send an update.
        if (lastStrokeRate == strokeRate)
            return;
        lastStrokeRate = strokeRate;

        double value = (60000D / (((double) strokeRate) * 25D));
        onStrokeRateUpdated(value);
    }


    /**
     * Is called if the value for the average stroke rate was updated.
     *
     * @param strokeRate The new stroke rate.
     */
    abstract protected void onStrokeRateUpdated(double strokeRate);

}
