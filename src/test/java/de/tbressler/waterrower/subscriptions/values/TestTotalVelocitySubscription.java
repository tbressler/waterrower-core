package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_TOTAL;
import static org.mockito.Mockito.*;

/**
 * Tests for class TotalVelocitySubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalVelocitySubscription {

    // Class under test.
    private TotalVelocitySubscription subscription;

    // Mocks:
    private TotalVelocitySubscription internalSubscription = mock(TotalVelocitySubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0144_notifiesOnVelocityUpdate() {
        subscription = newTotalVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x44));
        verify(internalSubscription, times(1)).onVelocityUpdated(3.24D);
    }

    @Test
    public void handle_withMessage0x0012_notifiesOnVelocityUpdate() {
        subscription = newTotalVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x00, 0x12));
        verify(internalSubscription, times(1)).onVelocityUpdated(0.18D);
    }

    @Test
    public void handle_withMessage0x0144and0x0144_notifiesOnVelocityUpdateOnlyOnce() {
        subscription = newTotalVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x44));
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x44));
        verify(internalSubscription, times(1)).onVelocityUpdated(3.24D);
    }

    @Test
    public void handle_withMessage0x0144and0x0012_notifiesOnVelocityUpdateTwoTimes() {
        subscription = newTotalVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x44));
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x00, 0x12));
        verify(internalSubscription, times(1)).onVelocityUpdated(3.24D);
        verify(internalSubscription, times(1)).onVelocityUpdated(0.18D);
    }


    // Helper methods:

    private TotalVelocitySubscription newTotalVelocitySubscription() {
        return new TotalVelocitySubscription() {
            @Override
            protected void onVelocityUpdated(double velocity) {
                internalSubscription.onVelocityUpdated(velocity);
            }
        };
    }

}