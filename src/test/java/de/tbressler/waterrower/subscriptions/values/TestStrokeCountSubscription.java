package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.STROKES_CNT_LOW;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class StrokeCountSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStrokeCountSubscription {

    // Class under test.
    private StrokeCountSubscription subscription;

    // Mocks:
    private StrokeCountSubscription internalSubscription = mock(StrokeCountSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithDoubleMemoryAndSTROKES_CNT_LOW() {
        subscription = newStrokeCountSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(STROKES_CNT_LOW.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withSTROKES_CNT_LOWAnd0x0102_notifiesOnStrokeCountUpdated() {
        subscription = newStrokeCountSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(STROKES_CNT_LOW.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onStrokeCountUpdated(eq(0x0102));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newStrokeCountSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(STROKES_CNT_LOW.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(STROKES_CNT_LOW.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onStrokeCountUpdated(eq(0x0102));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newStrokeCountSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(STROKES_CNT_LOW.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(STROKES_CNT_LOW.getLocation(), 0x02, 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onStrokeCountUpdated(eq(0x0102));
        verify(internalSubscription, times(1)).onStrokeCountUpdated(eq(0x0201));
    }



    // Helper methods:

    private StrokeCountSubscription newStrokeCountSubscription() {
        return new StrokeCountSubscription() {
            @Override
            protected void onStrokeCountUpdated(int strokes) {
                internalSubscription.onStrokeCountUpdated(strokes);
            }
        };
    }

}