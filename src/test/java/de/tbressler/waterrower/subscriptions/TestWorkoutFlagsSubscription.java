package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.model.WorkoutFlags;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.FEXTENDED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for class WorkoutFlagsSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkoutFlagsSubscription {

    // Class under test.
    private WorkoutFlagsSubscription subscription;

    // Mocks:
    private WorkoutFlagsSubscription internalSubscription = mock(WorkoutFlagsSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithSingleMemoryAndFEXTENDED() {
        subscription = newWorkoutModeSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(SINGLE_MEMORY, msg.getMemory());
        assertEquals(FEXTENDED.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withFEXTENDEDAnd0x01_notifiesOnWorkoutModeUpdated() {
        subscription = newWorkoutModeSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);

        subscription.handle((AbstractMessage) msg);

        // TODO Matcher erstellen!
        verify(internalSubscription, times(1)).onWorkoutModeUpdated(any(WorkoutFlags.class));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newWorkoutModeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onWorkoutModeUpdated(any(WorkoutFlags.class));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newWorkoutModeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x10);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(2)).onWorkoutModeUpdated(any(WorkoutFlags.class));
    }



    // Helper methods:

    private WorkoutFlagsSubscription newWorkoutModeSubscription() {
        return new WorkoutFlagsSubscription() {
            @Override
            protected void onWorkoutModeUpdated(WorkoutFlags flags) {
                internalSubscription.onWorkoutModeUpdated(flags);
            }
        };
    }

}