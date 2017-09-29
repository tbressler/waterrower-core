package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.subscriptions.AverageStrokeTimeSubscription.StrokeType;
import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.STROKE_AVERAGE;
import static de.tbressler.waterrower.model.MemoryLocation.STROKE_PULL;
import static de.tbressler.waterrower.subscriptions.AverageStrokeTimeSubscription.StrokeType.PULL_ONLY;
import static de.tbressler.waterrower.subscriptions.AverageStrokeTimeSubscription.StrokeType.WHOLE_STROKE;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Tests for class AverageStrokeTimeSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAverageStrokeTimeSubscription {

    // Class under test.
    private AverageStrokeTimeSubscription subscription;

    // Mocks:
    private AverageStrokeTimeSubscription internalSubscription = mock(AverageStrokeTimeSubscription.class, "internalSubscription");


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNull_throwsNPE() {
        new AverageStrokeTimeSubscription(null) {
            @Override
            protected void onAverageStrokeTimeUpdated(int averageStrokeTime) {}
        };
    }


    // Handle:

    @Test
    public void handle_withMessageSTROKE_AVERAGE_notifiesOnAverageStrokeTimeUpdate() {
        subscription = newAverageStrokeTimeSubscription(WHOLE_STROKE);
        subscription.handle(new DataMemoryMessage(STROKE_AVERAGE.getLocation(), 12));
        verify(internalSubscription, times(1)).onAverageStrokeTimeUpdated(12);
    }

    @Test
    public void handle_withMessageSTROKE_PULL_notifiesOnAverageStrokeTimeUpdate() {
        subscription = newAverageStrokeTimeSubscription(PULL_ONLY);
        subscription.handle(new DataMemoryMessage(STROKE_PULL.getLocation(), 1));
        verify(internalSubscription, times(1)).onAverageStrokeTimeUpdated(1);
    }

    @Test
    public void handle_with2MessagesAndSameSTROKE_PULL_notifiesOnAverageStrokeTimeUpdateOnlyOnce() {
        subscription = newAverageStrokeTimeSubscription(PULL_ONLY);
        subscription.handle(new DataMemoryMessage(STROKE_PULL.getLocation(), 12));
        subscription.handle(new DataMemoryMessage(STROKE_PULL.getLocation(), 12));
        verify(internalSubscription, times(1)).onAverageStrokeTimeUpdated(12);
    }

    @Test
    public void handle_with2MessagesAndUnequalSTROKE_PULL_notifiesOnAverageStrokeTimeUpdateTwice() {
        subscription = newAverageStrokeTimeSubscription(PULL_ONLY);
        subscription.handle(new DataMemoryMessage(STROKE_PULL.getLocation(), 12));
        subscription.handle(new DataMemoryMessage(STROKE_PULL.getLocation(), 13));
        verify(internalSubscription, times(1)).onAverageStrokeTimeUpdated(12);
        verify(internalSubscription, times(1)).onAverageStrokeTimeUpdated(13);
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnAverageStrokeTimeUpdate() {
        subscription = newAverageStrokeTimeSubscription(WHOLE_STROKE);
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onAverageStrokeTimeUpdated(anyInt());
    }


    // Helper methods:

    private AverageStrokeTimeSubscription newAverageStrokeTimeSubscription(StrokeType strokeType) {
        return new AverageStrokeTimeSubscription(strokeType) {
            @Override
            protected void onAverageStrokeTimeUpdated(int averageStrokeTime) {
                internalSubscription.onAverageStrokeTimeUpdated(averageStrokeTime);
            }
        };
    }

}