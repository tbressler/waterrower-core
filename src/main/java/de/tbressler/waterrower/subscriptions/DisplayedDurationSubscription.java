package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import java.time.Duration;

import static de.tbressler.waterrower.io.msg.Memory.TRIPLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.DISPLAY_SEC;
import static java.time.Duration.ofSeconds;

/**
 * Subscription for the displayed duration values.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class DisplayedDurationSubscription extends AbstractMemorySubscription {

    /* The last duration received. */
    private Duration lastDuration = null;


    /**
     * Subscription to the displayed duration value.
     */
    public DisplayedDurationSubscription() {
        super(TRIPLE_MEMORY, DISPLAY_SEC);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        Duration duration = ofSeconds(msg.getValue1())
                .plusMinutes(msg.getValue2())
                .plusHours(msg.getValue3());

        // If the received duration is the same as before,
        // don't send an update.
        if (duration.equals(lastDuration))
            return;

        lastDuration = duration;

        onDurationUpdated(duration);
    }


    /**
     * Is called if the value for the displayed duration was updated.
     *
     * @param duration The new duration, never null.
     */
    protected abstract void onDurationUpdated(Duration duration);

}
