package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.TOTAL_DIS_DEC;
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
    public void handle_withMessage0x0DF000_notifiesOnDistanceUpdatedUpdate() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_DEC.getLocation(), 0x0D, 0xF0, 0x00)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(3568.0);
    }

    @Test
    public void handle_withMessage0x312D12_notifiesOnDistanceUpdatedUpdate() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_DEC.getLocation(), 0x31, 0x2D, 0x12)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(12589.18);
    }

    @Test
    public void handle_withMessage0x0DF004and0x0DF004_notifiesOnDistanceUpdatedOnlyOnce() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_DEC.getLocation(), 0x0D, 0xF0, 0x04)); // TODO Test decimals as well.
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_DEC.getLocation(), 0x0D, 0xF0, 0x04)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(3568.04);
    }

    @Test
    public void handle_withMessage0x0DF0and0x312D_notifiesOnDistanceUpdatedUpdateTwoTimes() {
        subscription = newTotalDistanceSubscription();
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_DEC.getLocation(), 0x0D, 0xF0, 0x63)); // TODO Test decimals as well.
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_DEC.getLocation(), 0x31, 0x2D, 0x32)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(3568.99);
        verify(internalSubscription, times(1)).onDistanceUpdated(12589.5);
    }

    // Helper methods:

    private TotalDistanceSubscription newTotalDistanceSubscription() {
        return new TotalDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(double distance) {
                internalSubscription.onDistanceUpdated(distance);
            }
        };
    }

}