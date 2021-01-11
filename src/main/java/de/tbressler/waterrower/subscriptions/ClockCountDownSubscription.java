package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.log.Log;

import java.time.Duration;

import static de.tbressler.waterrower.io.msg.Memory.TRIPLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.CLOCK_DOWN_DEC;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;
import static java.lang.Integer.parseInt;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

/**
 * Subscription for clock count down values.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
@Deprecated
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

        int sec = parseInt(msg.getValue1AsACH());
        int min = parseInt(msg.getValue2AsACH());
        int hrs = parseInt(msg.getValue3AsACH());

        Duration duration = ofSeconds(sec)
                .plusMinutes(min)
                .plusHours(hrs);

        // If the received duration is the same as before,
        // don't send an update.
        if (duration.equals(lastClockCountDown))
            return;
        lastClockCountDown = duration;

        // TODO Remove debug message!
        Log.info("ClockCountDownSubscription ========= [ ACH3=" + msg.getValue3AsACH() + ", ACH2=" + msg.getValue2AsACH() + ", ACH1="+msg.getValue1AsACH() + " ] === " + duration.toMinutesPart() + ":" + duration.toSecondsPart());

        onClockCountDownUpdated(duration);
    }


    /**
     * Is called if the value for the clock count-down was updated.
     *
     * @param duration The new clock count-down, never null.
     */
    abstract protected void onClockCountDownUpdated(Duration duration);

}
