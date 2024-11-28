package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.time.Duration;

import static de.tbressler.waterrower.model.MemoryLocation.DISPLAY_SEC;
import static java.time.Duration.ofHours;
import static org.mockito.Mockito.*;

/**
 * Tests for class DisplayedDurationSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestDisplayedDurationSubscription {

    // Class under test.
    private DisplayedDurationSubscription subscription;

    // Mocks:
    private DisplayedDurationSubscription internalSubscription = mock(DisplayedDurationSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage_notifiesOnAverageStrokeTimeUpdated() {
        subscription = newDisplayedDurationSubscription();
        subscription.handle(new DataMemoryMessage(DISPLAY_SEC.getLocation(), 0x01, 0x02, 0x03));
        verify(internalSubscription, times(1)).onDurationUpdated(argThat(matchesDuration(0x01, 0x02, 0x03)));
    }

    @Test
    public void handle_withTwoSameMessages_notifiesOnAverageStrokeTimeUpdatedOnce() {
        subscription = newDisplayedDurationSubscription();
        subscription.handle(new DataMemoryMessage(DISPLAY_SEC.getLocation(), 0x03, 0x02, 0x01));
        subscription.handle(new DataMemoryMessage(DISPLAY_SEC.getLocation(), 0x03, 0x02, 0x01));
        verify(internalSubscription, times(1)).onDurationUpdated(argThat(matchesDuration(0x03, 0x02, 0x01)));
    }

    @Test
    public void handle_withTwoUnequalMessages_notifiesOnAverageStrokeTimeUpdatedTwice() {
        subscription = newDisplayedDurationSubscription();
        subscription.handle(new DataMemoryMessage(DISPLAY_SEC.getLocation(), 0x01, 0x02, 0x09));
        subscription.handle(new DataMemoryMessage(DISPLAY_SEC.getLocation(), 0x03, 0x02, 0x01));
        verify(internalSubscription, times(1)).onDurationUpdated(argThat(matchesDuration(0x01, 0x02, 0x09)));
        verify(internalSubscription, times(1)).onDurationUpdated(argThat(matchesDuration(0x03, 0x02, 0x01)));
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnClockCountDownUpdated() {
        subscription = newDisplayedDurationSubscription();
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onDurationUpdated(any(Duration.class));
    }


    // Helper methods:

    private DisplayedDurationSubscription newDisplayedDurationSubscription() {
        return new DisplayedDurationSubscription() {
            @Override
            protected void onDurationUpdated(Duration duration) {
                internalSubscription.onDurationUpdated(duration);
            }
        };
    }

    private DurationMatcher matchesDuration(int hour, int min, int sec) {
        return new DurationMatcher(ofHours(hour).plusMinutes(min).plusSeconds(sec));
    }

    private class DurationMatcher implements ArgumentMatcher<Duration> {

        private Duration expectedDuration;

        public DurationMatcher(Duration expectedDuration) {
            this.expectedDuration = expectedDuration;
        }

        @Override
        public boolean matches(Duration duration) {
            return (duration.equals(expectedDuration));
        }
    }

}