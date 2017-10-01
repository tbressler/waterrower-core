package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.subscriptions.IntensitySubscription.IntensityType;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_AVERAGE;
import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_TOTAL;
import static de.tbressler.waterrower.subscriptions.IntensitySubscription.IntensityType.INSTANT_AVERAGE_DISTANCE;
import static de.tbressler.waterrower.subscriptions.IntensitySubscription.IntensityType.TOTAL_DISTANCE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Tests for class IntensitySubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestIntensitySubscription {

    // Class under test.
    private IntensitySubscription subscription;

    // Mocks:
    private IntensitySubscription internalSubscription = mock(IntensitySubscription.class, "internalSubscription");


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNull_throwsNPE() {
        new IntensitySubscription(null) {
            @Override
            void onIntensityUpdated(IntensityType intensityType, int intensity) {}
        };
    }


    // Handle:

    @Test
    public void handle_withMessageMS_DISTANCE_LOW_notifiesOnDistanceUpdated() {
        subscription = newIntensitySubscription(INSTANT_AVERAGE_DISTANCE);
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onIntensityUpdated(INSTANT_AVERAGE_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndSameMS_DISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateOnlyOnce() {
        subscription = newIntensitySubscription(INSTANT_AVERAGE_DISTANCE);
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onIntensityUpdated(INSTANT_AVERAGE_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndUnequalMS_DISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateTwice() {
        subscription = newIntensitySubscription(INSTANT_AVERAGE_DISTANCE);
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(M_S_LOW_AVERAGE.getLocation(), 0x01, 0x03));
        verify(internalSubscription, times(1)).onIntensityUpdated(INSTANT_AVERAGE_DISTANCE, 0x0102);
        verify(internalSubscription, times(1)).onIntensityUpdated(INSTANT_AVERAGE_DISTANCE, 0x0103);
    }

    // ...

    @Test
    public void handle_withMessageDISTANCE_LOW_notifiesOnDistanceUpdated() {
        subscription = newIntensitySubscription(TOTAL_DISTANCE);
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onIntensityUpdated(TOTAL_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndSameDISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateOnlyOnce() {
        subscription = newIntensitySubscription(TOTAL_DISTANCE);
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x02));
        verify(internalSubscription, times(1)).onIntensityUpdated(TOTAL_DISTANCE, 0x0102);
    }

    @Test
    public void handle_with2MessagesAndUnequalDISTANCE_LOW_notifiesOnAverageStrokeTimeUpdateTwice() {
        subscription = newIntensitySubscription(TOTAL_DISTANCE);
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x02));
        subscription.handle(new DataMemoryMessage(M_S_LOW_TOTAL.getLocation(), 0x01, 0x03));
        verify(internalSubscription, times(1)).onIntensityUpdated(TOTAL_DISTANCE, 0x0102);
        verify(internalSubscription, times(1)).onIntensityUpdated(TOTAL_DISTANCE, 0x0103);
    }

    // ...

    @Test
    public void handle_withOtherMessage_doesntNotifyOnAverageStrokeTimeUpdate() {
        subscription = newIntensitySubscription(TOTAL_DISTANCE);
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onIntensityUpdated(any(IntensityType.class), anyInt());
    }


    // Helper methods:

    private IntensitySubscription newIntensitySubscription(IntensityType type) {
        return new IntensitySubscription(type) {
            @Override
            void onIntensityUpdated(IntensityType intensityType, int intensity) {
                internalSubscription.onIntensityUpdated(intensityType, intensity);
            }
        };
    }


}