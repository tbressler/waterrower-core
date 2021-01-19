package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.TOTAL_KCAL_LOW;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class CaloriesSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalCaloriesSubscription {

    // Class under test.
    private TotalCaloriesSubscription subscription;

    // Mocks:
    private TotalCaloriesSubscription internalSubscription = mock(TotalCaloriesSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithDoubleMemoryAndTOTAL_KCAL_LOW() {
        subscription = newTotalCaloriesSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(TOTAL_KCAL_LOW.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_with0x0102_notifiesOnCaloriesUpdated() {
        subscription = newTotalCaloriesSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(TOTAL_KCAL_LOW.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onCaloriesUpdated(eq(258));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newTotalCaloriesSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(TOTAL_KCAL_LOW.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(TOTAL_KCAL_LOW.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onCaloriesUpdated(eq(258));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newTotalCaloriesSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(TOTAL_KCAL_LOW.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(TOTAL_KCAL_LOW.getLocation(), 0x02, 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onCaloriesUpdated(eq(258));
        verify(internalSubscription, times(1)).onCaloriesUpdated(eq(513));
    }



    // Helper methods:

    private TotalCaloriesSubscription newTotalCaloriesSubscription() {
        return new TotalCaloriesSubscription() {
            @Override
            protected void onCaloriesUpdated(int cal) {
                internalSubscription.onCaloriesUpdated(cal);
            }
        };
    }

}