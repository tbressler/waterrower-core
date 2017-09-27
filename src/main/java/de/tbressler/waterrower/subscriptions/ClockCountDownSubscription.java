package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import java.time.Duration;

import static de.tbressler.waterrower.io.msg.Memory.TRIPLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.CLOCK_DOWN_DEC;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;
import static java.time.Duration.ofMillis;

/**
 * Subscription for clock count down values.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class ClockCountDownSubscription extends AbstractMemorySubscription {

    /* The last clock count down received. */
    private Duration lastClockCountDown = null;


    /**
     * Subscription to the distance value.
     */
    public ClockCountDownSubscription() {
        super(TRIPLE_MEMORY, CLOCK_DOWN_DEC);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        Duration duration = ofMillis(msg.getValue1() * 100)
                .plusSeconds(intFromHighAndLow(msg.getValue3(), msg.getValue2()));

        // If the received duration is the same as before,
        // don't send an update.
        if (duration.equals(lastClockCountDown))
            return;

        lastClockCountDown = duration;

        onClockCountDownUpdated(duration);
    }


    /**
     * Is called if the value for the clock count-down was updated.
     *
     * @param duration The new clock count-down, never null.
     */
    protected abstract void onClockCountDownUpdated(Duration duration);

}
