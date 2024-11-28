package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.model.WorkoutFlags;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.FEXTENDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        DataMemoryMessage msg = new DataMemoryMessage(FEXTENDED.getLocation(), 0xB6);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onWorkoutFlagsUpdated(argThat(matchesFlags(false, true, true, false, true, true, false, true)));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newWorkoutModeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onWorkoutFlagsUpdated(argThat(matchesFlags(true, false, false, false, false, false, false, false)));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newWorkoutModeSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(FEXTENDED.getLocation(), 0x80);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onWorkoutFlagsUpdated(argThat(matchesFlags(true, false, false, false, false, false, false, false)));
        verify(internalSubscription, times(1)).onWorkoutFlagsUpdated(argThat(matchesFlags(false, false, false, false, false, false, false, true)));
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnClockCountDownUpdated() {
        subscription = newWorkoutModeSubscription();
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onWorkoutFlagsUpdated(any(WorkoutFlags.class));
    }



    // Helper methods:

    private WorkoutFlagsSubscription newWorkoutModeSubscription() {
        return new WorkoutFlagsSubscription() {
            @Override
            protected void onWorkoutFlagsUpdated(WorkoutFlags flags) {
                internalSubscription.onWorkoutFlagsUpdated(flags);
            }
        };
    }


    private ArgumentMatcher<WorkoutFlags> matchesFlags(boolean heartRateZone, boolean intensityZone, boolean strokeRateZone, boolean prognosticsActive, boolean workoutDistanceMode, boolean workoutDurationMode, boolean workoutDistanceIntervalMode, boolean workoutDurationIntervalMode) {
        return flags -> (flags.isWorkingInHeartRateZone() == heartRateZone)
                && (flags.isWorkingInIntensityZone() == intensityZone)
                && (flags.isWorkingInStrokeRateZone() == strokeRateZone)
                && (flags.isPrognosticsActive() == prognosticsActive)
                && (flags.isWorkoutDistanceMode() == workoutDistanceMode)
                && (flags.isWorkoutDurationMode() == workoutDurationMode)
                && (flags.isWorkoutDistanceIntervalMode() == workoutDistanceIntervalMode)
                && (flags.isWorkoutDurationIntervalMode() == workoutDurationIntervalMode);
    }

}