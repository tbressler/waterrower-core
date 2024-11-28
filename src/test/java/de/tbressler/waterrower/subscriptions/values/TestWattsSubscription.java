package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.KCAL_WATTS_LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for class WattsSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWattsSubscription {

    // Class under test.
    private WattsSubscription subscription;

    // Mocks:
    private WattsSubscription internalSubscription = mock(WattsSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithDoubleMemoryAndKCAL_WATTS_LOW() {
        subscription = newWattsSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(KCAL_WATTS_LOW.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_with0x0102_notifiesOnWattsUpdated() {
        subscription = newWattsSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(KCAL_WATTS_LOW.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onWattsUpdated(eq(258));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newWattsSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(KCAL_WATTS_LOW.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(KCAL_WATTS_LOW.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onWattsUpdated(eq(258));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newWattsSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(KCAL_WATTS_LOW.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(KCAL_WATTS_LOW.getLocation(), 0x02, 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onWattsUpdated(eq(258));
        verify(internalSubscription, times(1)).onWattsUpdated(eq(513));
    }



    // Helper methods:

    private WattsSubscription newWattsSubscription() {
        return new WattsSubscription() {
            @Override
            protected void onWattsUpdated(int watt) {
                internalSubscription.onWattsUpdated(watt);
            }
        };
    }

}