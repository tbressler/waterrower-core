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
    public void handle_withTANK_VOLUMEAnd0x0F_notifiesOnTankVolumeUpdated() {
        subscription = newTankVolumeSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x0F);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(15));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newTankVolumeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x02);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(2));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newTankVolumeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x10);
        DataMemoryMessage msg2 = new DataMemoryMessage(TANK_VOLUME.getLocation(), 0x11);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(16));
        verify(internalSubscription, times(1)).onTankVolumeUpdated(eq(17));
    }



    // Helper methods:

    private TankVolumeSubscription newTankVolumeSubscription() {
        return new TankVolumeSubscription() {
            @Override
            protected void onTankVolumeUpdated(int tankVolume) {
                internalSubscription.onTankVolumeUpdated(tankVolume);
            }
        };
    }

}