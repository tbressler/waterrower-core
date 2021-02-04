package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.MS_DISTANCE_DEC;
import static org.mockito.Mockito.*;

/**
 * Tests for class DistanceSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestDistanceSubscription {

    // Class under test.
    private DistanceSubscription subscription;

    // Mocks:
    private DistanceSubscription internalSubscription = mock(DistanceSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0DF000_notifiesOnDistanceUpdatedUpdate() {
        subscription = newDistanceSubscription();
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_DEC.getLocation(), 0x0D, 0xF0, 0x00)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x312D_notifiesOnDistanceUpdatedUpdate() {
        subscription = newDistanceSubscription();
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_DEC.getLocation(), 0x31, 0x2D, 0x00)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }

    @Test
    public void handle_withMessage0x0DF0and0x0DF0_notifiesOnDistanceUpdatedOnlyOnce() {
        subscription = newDistanceSubscription();
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_DEC.getLocation(), 0x0D, 0xF0, 0x00)); // TODO Test decimals as well.
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_DEC.getLocation(), 0x0D, 0xF0, 0x00)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x0DF0and0x312D_notifiesOnDistanceUpdatedUpdateTwoTimes() {
        subscription = newDistanceSubscription();
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_DEC.getLocation(), 0x0D, 0xF0, 0x00)); // TODO Test decimals as well.
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_DEC.getLocation(), 0x31, 0x2D, 0x00)); // TODO Test decimals as well.
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }


    // Helper methods:

    private DistanceSubscription newDistanceSubscription() {
        return new DistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                internalSubscription.onDistanceUpdated(distance);
            }
        };
    }

}