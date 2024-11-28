package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.ZONE_HR_VAL;
import static org.mockito.Mockito.*;

/**
 * Tests for class HeartRateSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestHeartRateSubscription {

    // Class under test.
    private HeartRateSubscription subscription;

    // Mocks:
    private HeartRateSubscription internalSubscription = mock(HeartRateSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage_notifiesOnHeartRateUpdated() {
        subscription = newHeartRateSubscription();
        subscription.handle(new DataMemoryMessage(ZONE_HR_VAL.getLocation(), 0x92));
        verify(internalSubscription, times(1)).onHeartRateUpdated(eq(146));
    }

    @Test
    public void handle_withTwoSameMessages_notifiesOnHeartRateUpdatedOnce() {
        subscription = newHeartRateSubscription();
        subscription.handle(new DataMemoryMessage(ZONE_HR_VAL.getLocation(), 0x80));
        subscription.handle(new DataMemoryMessage(ZONE_HR_VAL.getLocation(), 0x80));
        verify(internalSubscription, times(1)).onHeartRateUpdated(eq(128));
    }

    @Test
    public void handle_withTwoUnequalMessages_notifiesOnHeartRateUpdatedTwice() {
        subscription = newHeartRateSubscription();
        subscription.handle(new DataMemoryMessage(ZONE_HR_VAL.getLocation(), 0x80));
        subscription.handle(new DataMemoryMessage(ZONE_HR_VAL.getLocation(), 0x59));
        verify(internalSubscription, times(1)).onHeartRateUpdated(eq(128));
        verify(internalSubscription, times(1)).onHeartRateUpdated(eq(89));
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnHeartRateUpdated() {
        subscription = newHeartRateSubscription();
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onHeartRateUpdated(anyInt());
    }


    // Helper methods:

    private HeartRateSubscription newHeartRateSubscription() {
        return new HeartRateSubscription() {
            @Override
            protected void onHeartRateUpdated(int heartRate) {
                internalSubscription.onHeartRateUpdated(heartRate);
            }
        };
    }

}