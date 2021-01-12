package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.TOTAL_DIS_LOW;
import static org.mockito.Mockito.*;

/**
 * Tests for class TotalDistanceSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalDistanceSubscription {

    // Class under test.
    private TotalDistanceSubscription subscription;

    // Mocks:
    private TotalDistanceSubscription internalSubscription = mock(TotalDistanceSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0DF0_notifiesOnDistanceUpdatedUpdate() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x0D, 0xF0));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x312D_notifiesOnDistanceUpdatedUpdate() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x31, 0x2D));
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }

    @Test
    public void handle_withMessage0x0DF0and0x0DF0_notifiesOnDistanceUpdatedOnlyOnce() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x0D, 0xF0));
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x0D, 0xF0));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x0DF0and0x312D_notifiesOnDistanceUpdatedUpdateTwoTimes() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x0D, 0xF0));
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x31, 0x2D));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }


    // Helper methods:

    private TotalDistanceSubscription newTotalDistanceSubscription() {
        return new TotalDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                internalSubscription.onDistanceUpdated(distance);
            }
        };
    }

}