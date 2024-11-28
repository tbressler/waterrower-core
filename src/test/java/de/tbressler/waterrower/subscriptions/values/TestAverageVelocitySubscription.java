package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_AVERAGE;
import static org.mockito.Mockito.*;

/**
 * Tests for class AverageVelocitySubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAverageVelocitySubscription {

    // Class under test.
    private AverageVelocitySubscription subscription;

    // Mocks:
    private AverageVelocitySubscription internalSubscription = mock(AverageVelocitySubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0144_notifiesOnVelocityUpdate() {
        subscription = newAverageVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x44));
        verify(internalSubscription, times(1)).onVelocityUpdated(3.24D);
    }

    @Test
    public void handle_withMessage0x0012_notifiesOnVelocityUpdate() {
        subscription = newAverageVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x00, 0x12));
        verify(internalSubscription, times(1)).onVelocityUpdated(0.18D);
    }

    @Test
    public void handle_withMessage0x0144and0x0144_notifiesOnVelocityUpdateOnlyOnce() {
        subscription = newAverageVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x44));
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x44));
        verify(internalSubscription, times(1)).onVelocityUpdated(3.24D);
    }

    @Test
    public void handle_withMessage0x0144and0x0012_notifiesOnVelocityUpdateTwoTimes() {
        subscription = newAverageVelocitySubscription();
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x44));
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x00, 0x12));
        verify(internalSubscription, times(1)).onVelocityUpdated(3.24D);
        verify(internalSubscription, times(1)).onVelocityUpdated(0.18D);
    }


    // Helper methods:

    private AverageVelocitySubscription newAverageVelocitySubscription() {
        return new AverageVelocitySubscription() {
            @Override
            protected void onVelocityUpdated(double velocity) {
                internalSubscription.onVelocityUpdated(velocity);
            }
        };
    }

}