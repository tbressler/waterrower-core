package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_INTER;
import static org.mockito.Mockito.*;

/**
 * Tests for class WorkoutIntervalsSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkoutIntervalsSubscription {

    // Class under test.
    private WorkoutIntervalsSubscription subscription;

    // Mocks:
    private WorkoutIntervalsSubscription internalSubscription = mock(WorkoutIntervalsSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0DF0_notifiesOnDistanceUpdatedUpdate() {
        subscription = newWorkoutIntervalsSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_INTER.getLocation(), 0x08));
        verify(internalSubscription, times(1)).onIntervalsUpdated(8);
    }

    @Test
    public void handle_withMessage0x312D_notifiesOnDistanceUpdatedUpdate() {
        subscription = newWorkoutIntervalsSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_INTER.getLocation(), 0x0A));
        verify(internalSubscription, times(1)).onIntervalsUpdated(10);
    }

    @Test
    public void handle_withMessage0x0DF0and0x0DF0_notifiesOnDistanceUpdatedOnlyOnce() {
        subscription = newWorkoutIntervalsSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_INTER.getLocation(), 0x08));
        subscription.handle(new DataMemoryMessage(WORKOUT_INTER.getLocation(), 0x08));
        verify(internalSubscription, times(1)).onIntervalsUpdated(8);
    }

    @Test
    public void handle_withMessage0x0DF0and0x312D_notifiesOnDistanceUpdatedUpdateTwoTimes() {
        subscription = newWorkoutIntervalsSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_INTER.getLocation(), 0x08));
        subscription.handle(new DataMemoryMessage(WORKOUT_INTER.getLocation(), 0x0A));
        verify(internalSubscription, times(1)).onIntervalsUpdated(8);
        verify(internalSubscription, times(1)).onIntervalsUpdated(10);
    }


    // Helper methods:

    private WorkoutIntervalsSubscription newWorkoutIntervalsSubscription() {
        return new WorkoutIntervalsSubscription() {
            @Override
            protected void onIntervalsUpdated(int intervals) {
                internalSubscription.onIntervalsUpdated(intervals);
            }
        };
    }

}