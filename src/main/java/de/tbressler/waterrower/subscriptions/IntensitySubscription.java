package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.MemoryLocation;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_AVERAGE;
import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_TOTAL;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;
import static java.util.Objects.requireNonNull;

/**
 * Subscription for the intensity values, like:
 * - Total distance per second in cm
 * - Instant average distance in cm
 *
 * @author Tobias Bressler
 * @version 1.0
 */
@Deprecated
public abstract class IntensitySubscription extends AbstractMemorySubscription {

    /**
     * The intensity type.
     */
    public enum IntensityType {

        /** Total distance per second in cm. */
        TOTAL_DISTANCE,

        /** Instant average distance in cm. */
        INSTANT_AVERAGE_DISTANCE
    }


    /* The intensity type of this subscription. */
    private final IntensityType intensityType;

    /* The last intensity received. */
    private int lastIntensity = -1;


    /**
     * Subscription for the intensity values.
     *
     * @param intensityType The intensity type, must not be null.
     */
    public IntensitySubscription(IntensityType intensityType) {
        super(DOUBLE_MEMORY, getMemoryLocation(intensityType));
        this.intensityType = intensityType;
    }

    /* Returns the memory location for the given intensity type. */
    private static MemoryLocation getMemoryLocation(IntensityType intensityType) {
        switch (requireNonNull(intensityType)) {
            case TOTAL_DISTANCE:
                return M_S_LOW_TOTAL;
            case INSTANT_AVERAGE_DISTANCE:
                return M_S_LOW_AVERAGE;
            default:
                throw new IllegalStateException("Unhandled intensity type!");
        }
    }


    @Override
    protected void handle(DataMemoryMessage msg) {
        int intensity = intFromHighAndLow(msg.getValue2(), msg.getValue1());

        // If the received intensity is the same as before,
        // don't send an update.
        if (lastIntensity == intensity)
            return;
        lastIntensity = intensity;

        // TODO Remove debug message!
        Log.info("IntensitySubscription ========= [ ACH2=" + msg.getValue2AsACH() + ", ACH1="+msg.getValue1AsACH() + " ] === " + intensity);

        onIntensityUpdated(intensityType, intensity);
    }


    /**
     * Is called if the value for the intensity was updated.
     *
     * Depending on the intensity type the values are:
     * - Total distance per second in cm
     * - Instant average distance in cm
     *
     * @param intensityType The intensity type, never null.
     * @param intensity The new value.
     */
    abstract protected void onIntensityUpdated(IntensityType intensityType, int intensity);

}
