package de.tbressler.waterrower.subscriptions.flags;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.model.MiscFlags;
import de.tbressler.waterrower.subscriptions.AbstractMemorySubscription;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.FMISC_FLAGS;
import static de.tbressler.waterrower.subscriptions.Priority.HIGH;

/**
 * Subscription for zone words and misc windows flags (FMISC_FLAGS).
 *
 * The received message contains the following flags:
 *  0 = fzone_fg_work
 *  1 = fzone_fg_rest
 *  2 = fmisc_fg_lowbat
 *  3 = fmisc_fg_pc
 *  4 = fmisc_fg_line
 *  5 = fmisc_fg_mmc_cd
 *  6 = fmisc_fg_mmc_up
 *  7 = fmisc_fg_mmc_dn
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class MiscFlagsSubscription extends AbstractMemorySubscription {

    /* The last received zone flags. */
    private MiscFlags lastZoneFlags;


    /**
     * Subscription for zone words and misc windows flags (FMISC_FLAGS).
     */
    public MiscFlagsSubscription() {
        super(HIGH, SINGLE_MEMORY, FMISC_FLAGS);
    }


    @Override
    public final void handle(DataMemoryMessage msg) {

        MiscFlags flags = new MiscFlags(msg.getValue1());

        // If the received zone flags are the same as before,
        // don't send an update.
        if (flags.equals(lastZoneFlags))
            return;
        lastZoneFlags = flags;

        onMiscFlagsUpdated(flags);
    }


    /**
     * Is called, when an update of the zone flags was received.
     *
     * @param flags The flags of the zones, never null.
     */
    abstract protected void onMiscFlagsUpdated(MiscFlags flags);

}
