package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.DISTANCE_LOW;
import static org.mockito.Mockito.*;

/**
 * Tests for class DisplayedDistanceSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestDisplayedDistanceSubscription {

    // Class under test.
    private DisplayedDistanceSubscription subscription;

    // Mocks:
    private DisplayedDistanceSubscription internalSubscription = mock(DisplayedDistanceSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage0x0DF0_notifiesOnDistanceUpdatedUpdate() {
        subscription = newDisplayedDistanceSubscription();
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x0D, 0xF0));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x312D_notifiesOnDistanceUpdatedUpdate() {
        subscription = newDisplayedDistanceSubscription();
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x31, 0x2D));
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }

    @Test
    public void handle_withMessage0x0DF0and0x0DF0_notifiesOnDistanceUpdatedOnlyOnce() {
        subscription = newDisplayedDistanceSubscription();
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x0D, 0xF0));
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x0D, 0xF0));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
    }

    @Test
    public void handle_withMessage0x0DF0and0x312D_notifiesOnDistanceUpdatedUpdateTwoTimes() {
        subscription = newDisplayedDistanceSubscription();
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x0D, 0xF0));
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x31, 0x2D));
        verify(internalSubscription, times(1)).onDistanceUpdated(3568);
        verify(internalSubscription, times(1)).onDistanceUpdated(12589);
    }


    // Helper methods:

    private DisplayedDistanceSubscription newDisplayedDistanceSubscription() {
        return new DisplayedDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                internalSubscription.onDistanceUpdated(distance);
            }
        };
    }

}