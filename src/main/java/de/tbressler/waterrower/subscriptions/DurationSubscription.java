package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;

import java.time.Duration;

import static de.tbressler.waterrower.io.msg.Memory.TRIPLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.DISPLAY_SEC;
import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;

/**
 * Subscription for the duration values.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class DurationSubscription extends AbstractMemorySubscription {

    /**
     * The duration mode.
     */
    public enum DurationMode {

        /* Used to generate the display clock. */
        DISPLAYED_DURATION

    }


    /* The duration mode of this subscription. */
    private final DurationMode durationMode;

    /* The last duration received. */
    private Duration lastDuration = null;


    /**
     * Subscription to the distance value.
     *
     * @param durationMode The duration mode (e.g. displayed duration), must not be null.
     */
    public DurationSubscription(DurationMode durationMode) {
        super(TRIPLE_MEMORY, getMemoryLocation(durationMode));
        this.durationMode = durationMode;
    }

    private static MemoryLocation getMemoryLocation(DurationMode durationMode) {
        switch (requireNonNull(durationMode)) {
            case DISPLAYED_DURATION:
                return DISPLAY_SEC;

            // TODO More modes?

            default:
                throw new IllegalArgumentException("Unhandled duration mode!");
        }
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

        onDurationUpdated(durationMode, duration);
    }


    /**
     * Is called if the value for the duration was updated.
     *
     * @param mode The duration mode (e.g. displayed distance), never null.
     * @param duration The new duration, never null.
     */
    protected abstract void onDurationUpdated(DurationMode mode, Duration duration);

}
