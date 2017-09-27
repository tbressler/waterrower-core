package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.TANK_VOLUME;

/**
 * Subscription for the tank volume value (in liters).
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class TankVolumeSubscription extends AbstractMemorySubscription {

    /**
     * Subscription for the tank volume value.
     */
    public TankVolumeSubscription() {
        super(SINGLE_MEMORY, TANK_VOLUME);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {
        int tankVolume = msg.getValue1();
        onTankVolumeUpdated(tankVolume);
    }


    /**
     * Is called if the value for the tank volume was updated.
     *
     * @param tankVolume The volume of water in the tank (in liters).
     */
    protected abstract void onTankVolumeUpdated(int tankVolume);

}
