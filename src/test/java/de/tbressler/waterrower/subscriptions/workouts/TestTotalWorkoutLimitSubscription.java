package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_LIMIT_L;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class TotalWorkoutLimitSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalWorkoutLimitSubscription {

    // Class under test.
    private TotalWorkoutLimitSubscription subscription;

    // Mocks:
    private TotalWorkoutLimitSubscription internalSubscription = mock(TotalWorkoutLimitSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithDoubleMemoryAndWORKOUT_LIMIT_L() {
        subscription = newTotalWorkoutLimitSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(WORKOUT_LIMIT_L.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withSTROKES_CNT_LOWAnd0x0102_notifiesOnTotalWorkoutLimitUpdated() {
        subscription = newTotalWorkoutLimitSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(WORKOUT_LIMIT_L.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onLimitUpdated(eq(0x0102));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newTotalWorkoutLimitSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_LIMIT_L.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_LIMIT_L.getLocation(), 0x01, 0x02);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onLimitUpdated(eq(0x0102));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newTotalWorkoutLimitSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_LIMIT_L.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_LIMIT_L.getLocation(), 0x02, 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onLimitUpdated(eq(0x0102));
        verify(internalSubscription, times(1)).onLimitUpdated(eq(0x0201));
    }



    // Helper methods:

    private TotalWorkoutLimitSubscription newTotalWorkoutLimitSubscription() {
        return new TotalWorkoutLimitSubscription() {
            @Override
            protected void onLimitUpdated(int limit) {

                internalSubscription.onLimitUpdated(limit);
            }
        };
    }

}