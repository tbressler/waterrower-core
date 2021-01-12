package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.TANK_VOLUME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class TankVolumeSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTankVolumeSubscription {

    // Class under test.
    private TankVolumeSubscription subscription;

    // Mocks:
    private TankVolumeSubscription internalSubscription = mock(TankVolumeSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithSingleMemoryAndTANK_VOLUME() {
        subscription = newTankVolumeSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(SINGLE_MEMORY, msg.getMemory());
        assertEquals(TANK_VOLUME.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withTANK_VOLUMEAnd0xA0_notifiesOnTankVolumeUpdated() {
        subscription = newTankVolumeSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0xA5);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(16.5D));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newTankVolumeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x96);
        DataMemoryMessage msg2 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x96);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(15D));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newTankVolumeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0xA0);
        DataMemoryMessage msg2 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x96);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(16D));
        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(15D));
    }



    // Helper methods:

    private TankVolumeSubscription newTankVolumeSubscription() {
        return new TankVolumeSubscription() {
            @Override
            protected void onTankVolumeUpdated(double tankVolume) {
                internalSubscription.onTankVolumeUpdated(tankVolume);
            }
        };
    }

}