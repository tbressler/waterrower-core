package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.STROKE_AVERAGE;
import static org.mockito.Mockito.*;

/**
 * Tests for class AverageStrokeRateSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAverageStrokeRateSubscription {

    // Class under test.
    private AverageStrokeRateSubscription subscription;

    // Mocks:
    private AverageStrokeRateSubscription internalSubscription = mock(AverageStrokeRateSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x64_notifiesOnStrokeRateUpdated() {
        subscription = newAverageStrokeRateSubscription();
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x64));
        verify(internalSubscription, times(1)).onStrokeRateUpdated(24D);
    }

    @Test
    public void handle_withMessage0x00_notifiesOnStrokeRateUpdated() {
        subscription = newAverageStrokeRateSubscription();
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x00));
        verify(internalSubscription, times(1)).onStrokeRateUpdated(0D);
    }

    @Test
    public void handle_withMessage0x78_notifiesOnStrokeRateUpdated() {
        subscription = newAverageStrokeRateSubscription();
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x78));
        verify(internalSubscription, times(1)).onStrokeRateUpdated(20D);
    }

    @Test
    public void handle_withMessage0x64and0x64_notifiesOnStrokeRateUpdatedOnlyOnce() {
        subscription = newAverageStrokeRateSubscription();
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x64));
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x64));
        verify(internalSubscription, times(1)).onStrokeRateUpdated(24D);
    }

    @Test
    public void handle_withMessage0x64and0x78_notifiesOnStrokeRateUpdatedTwoTimes() {
        subscription = newAverageStrokeRateSubscription();
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x64));
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 0x78));
        verify(internalSubscription, times(1)).onStrokeRateUpdated(24D);
        verify(internalSubscription, times(1)).onStrokeRateUpdated(20D);
    }


    // Helper methods:

    private AverageStrokeRateSubscription newAverageStrokeRateSubscription() {
        return new AverageStrokeRateSubscription() {
            @Override
            protected void onStrokeRateUpdated(double strokeRate) {
                internalSubscription.onStrokeRateUpdated(strokeRate);
            }
        };
    }

}