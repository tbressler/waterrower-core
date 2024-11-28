package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_MS_L;
import static org.mockito.Mockito.*;

/**
 * Tests for class TotalWorkoutDistanceSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalWorkoutDistanceSubscription {

    // Class under test.
    private TotalWorkoutDistanceSubscription subscription;

    // Mocks:
    private TotalWorkoutDistanceSubscription internalSubscription = mock(TotalWorkoutDistanceSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0DF0_notifiesOnDistanceUpdatedUpdate() {
        subscription = newTotalWorkoutDistanceSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_MS_L.getLocation(), 0x0D, 0xF0));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x312D_notifiesOnDistanceUpdatedUpdate() {
        subscription = newTotalWorkoutDistanceSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_MS_L.getLocation(), 0x31, 0x2D));
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }

    @Test
    public void handle_withMessage0x0DF0and0x0DF0_notifiesOnDistanceUpdatedOnlyOnce() {
        subscription = newTotalWorkoutDistanceSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_MS_L.getLocation(), 0x0D, 0xF0));
        subscription.handle(new DataMemoryMessage(WORKOUT_MS_L.getLocation(), 0x0D, 0xF0));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x0DF0and0x312D_notifiesOnDistanceUpdatedUpdateTwoTimes() {
        subscription = newTotalWorkoutDistanceSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_MS_L.getLocation(), 0x0D, 0xF0));
        subscription.handle(new DataMemoryMessage(WORKOUT_MS_L.getLocation(), 0x31, 0x2D));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }


    // Helper methods:

    private TotalWorkoutDistanceSubscription newTotalWorkoutDistanceSubscription() {
        return new TotalWorkoutDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                internalSubscription.onDistanceUpdated(distance);
            }
        };
    }

}