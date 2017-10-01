package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.subscriptions.DistanceSubscription.DistanceMode;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.subscriptions.DistanceSubscription.DistanceMode.*;
import static org.mockito.Matchers.anyInt;
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


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNull_throwsNPE() {
        new DistanceSubscription(null) {
            @Override
            protected void onDistanceUpdated(DistanceMode mode, int distance) {}
        };
    }


    // Handle:

    @Test
    public void handle_withMessageMS_DISTANCE_LOW_notifiesOnDistanceUpdated() {
        subscription = newDistanceSubscription(DISTANCE);
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_LOW.getLocation(), 0, 12));
        verify(internalSubscription, times(1)).onDistanceUpdated(DISTANCE, 12);
    }

    @Test
    public void handle_with2MessagesAndSameMS_DISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateOnlyOnce() {
        subscription = newDistanceSubscription(DISTANCE);
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_LOW.getLocation(), 0, 12));
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_LOW.getLocation(), 0, 12));
        verify(internalSubscription, times(1)).onDistanceUpdated(DISTANCE, 12);
    }

    @Test
    public void handle_with2MessagesAndUnequalMS_DISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateTwice() {
        subscription = newDistanceSubscription(DISTANCE);
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_LOW.getLocation(), 0, 12));
        subscription.handle(new DataMemoryMessage(MS_DISTANCE_LOW.getLocation(), 0, 13));
        verify(internalSubscription, times(1)).onDistanceUpdated(DISTANCE, 12);
        verify(internalSubscription, times(1)).onDistanceUpdated(DISTANCE, 13);
    }

    // ...

    @Test
    public void handle_withMessageDISTANCE_LOW_notifiesOnDistanceUpdated() {
        subscription = newDistanceSubscription(DISPLAYED_DISTANCE);
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onDistanceUpdated(DISPLAYED_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndSameDISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateOnlyOnce() {
        subscription = newDistanceSubscription(DISPLAYED_DISTANCE);
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onDistanceUpdated(DISPLAYED_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndUnequalDISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateTwice() {
        subscription = newDistanceSubscription(DISPLAYED_DISTANCE);
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(DISTANCE_LOW.getLocation(), 0x01, 0x03));
        verify(internalSubscription, times(1)).onDistanceUpdated(DISPLAYED_DISTANCE, 0x0102);
        verify(internalSubscription, times(1)).onDistanceUpdated(DISPLAYED_DISTANCE, 0x0103);
    }

    // ...

    @Test
    public void handle_withMessageTOTAL_DIS_LOW_notifiesOnDistanceUpdated() {
        subscription = newDistanceSubscription(TOTAL_DISTANCE);
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onDistanceUpdated(TOTAL_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndSameTOTAL_DIS_LOW_notifiesOnAverageStrokeTimeUpdateOnlyOnce() {
        subscription = newDistanceSubscription(TOTAL_DISTANCE);
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onDistanceUpdated(TOTAL_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndUnequalTOTAL_DIS_LOW_notifiesOnAverageStrokeTimeUpdateTwice() {
        subscription = newDistanceSubscription(TOTAL_DISTANCE);
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(TOTAL_DIS_LOW.getLocation(), 0x01, 0x03));
        verify(internalSubscription, times(1)).onDistanceUpdated(TOTAL_DISTANCE, 0x0102);
        verify(internalSubscription, times(1)).onDistanceUpdated(TOTAL_DISTANCE, 0x0103);
    }

    // ...

    @Test
    public void handle_withOtherMessage_doesntNotifyOnAverageStrokeTimeUpdate() {
        subscription = newDistanceSubscription(DISTANCE);
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onDistanceUpdated(any(DistanceMode.class), anyInt());
    }


    // Helper methods:

    private DistanceSubscription newDistanceSubscription(DistanceMode distanceMode) {
        return new DistanceSubscription(distanceMode) {
            @Override
            protected void onDistanceUpdated(DistanceMode mode, int distance) {
                internalSubscription.onDistanceUpdated(mode, distance);
            }
        };
    }

}