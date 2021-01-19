package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_STROKEL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class TotalWorkoutStrokesSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalWorkoutStrokesSubscription {

    // Class under test.
    private TotalWorkoutStrokesSubscription subscription;

    // Mocks:
    private TotalWorkoutStrokesSubscription internalSubscription = mock(TotalWorkoutStrokesSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithDoubleMemoryAndSTROKES_CNT_LOW() {
        subscription = newTotalWorkoutStrokesSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(WORKOUT_STROKEL.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withSTROKES_CNT_LOWAnd0x0102_notifiesOnStrokeCountUpdated() {
        subscription = newTotalWorkoutStrokesSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(WORKOUT_STROKEL.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onStrokesUpdated(eq(0x0102));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newTotalWorkoutStrokesSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_STROKEL.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_STROKEL.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onStrokesUpdated(eq(0x0102));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newTotalWorkoutStrokesSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_STROKEL.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_STROKEL.getLocation(), 0x02, 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onStrokesUpdated(eq(0x0102));
        verify(internalSubscription, times(1)).onStrokesUpdated(eq(0x0201));
    }



    // Helper methods:

    private TotalWorkoutStrokesSubscription newTotalWorkoutStrokesSubscription() {
        return new TotalWorkoutStrokesSubscription() {
            @Override
            protected void onStrokesUpdated(int strokes) {
                internalSubscription.onStrokesUpdated(strokes);
            }
        };
    }

}