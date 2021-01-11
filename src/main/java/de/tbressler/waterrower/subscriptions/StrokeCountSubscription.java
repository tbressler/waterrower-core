package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.STROKES_CNT_LOW;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for stroke count value.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
@Deprecated
public abstract class StrokeCountSubscription extends AbstractMemorySubscription {

    /* The last stroke count received. */
    private int lastStrokeCount = -1;


    /**
     * Subscription for stroke count value.
     */
    public StrokeCountSubscription() {
        super(DOUBLE_MEMORY, STROKES_CNT_LOW);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        int strokes = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received duration is the same as before,
        // don't send an update.
        if (lastStrokeCount == strokes)
            return;
        lastStrokeCount = strokes;

        onStrokeCountUpdated(strokes);
    }


    /**
     * Is called if the value for the stroke count was updated.
     *
     * @param strokes The new stroke count.
     */
    abstract protected void onStrokeCountUpdated(int strokes);

}
