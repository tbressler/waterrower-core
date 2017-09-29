package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.time.Duration;

import static de.tbressler.waterrower.model.MemoryLocation.CLOCK_DOWN_DEC;
import static java.time.Duration.ofSeconds;
import static org.mockito.Mockito.*;

/**
 * Tests for class ClockCountDownSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestClockCountDownSubscription {

    // Class under test.
    private ClockCountDownSubscription subscription;

    // Mocks:
    private ClockCountDownSubscription internalSubscription = mock(ClockCountDownSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage_notifiesOnAverageStrokeTimeUpdated() {
        subscription = newClockCountDownSubscription();
        subscription.handle(new DataMemoryMessage(CLOCK_DOWN_DEC.getLocation(), 0x01, 0x02, 0x03));
        verify(internalSubscription, times(1)).onClockCountDownUpdated(argThat(matchesDuration(0x0102, 300)));
    }

    @Test
    public void handle_withTwoSameMessages_notifiesOnAverageStrokeTimeUpdatedOnce() {
        subscription = newClockCountDownSubscription();
        subscription.handle(new DataMemoryMessage(CLOCK_DOWN_DEC.getLocation(), 0x03, 0x02, 0x01));
        subscription.handle(new DataMemoryMessage(CLOCK_DOWN_DEC.getLocation(), 0x03, 0x02, 0x01));
        verify(internalSubscription, times(1)).onClockCountDownUpdated(argThat(matchesDuration(0x0302, 100)));
    }

    @Test
    public void handle_withTwoUnequalMessages_notifiesOnAverageStrokeTimeUpdatedTwice() {
        subscription = newClockCountDownSubscription();
        subscription.handle(new DataMemoryMessage(CLOCK_DOWN_DEC.getLocation(), 0x01, 0x02, 0x09));
        subscription.handle(new DataMemoryMessage(CLOCK_DOWN_DEC.getLocation(), 0x03, 0x02, 0x01));
        verify(internalSubscription, times(1)).onClockCountDownUpdated(argThat(matchesDuration(0x0102, 900)));
        verify(internalSubscription, times(1)).onClockCountDownUpdated(argThat(matchesDuration(0x0302, 100)));
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnClockCountDownUpdated() {
        subscription = newClockCountDownSubscription();
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onClockCountDownUpdated(any(Duration.class));
    }


    // Helper methods:

    private ClockCountDownSubscription newClockCountDownSubscription() {
        return new ClockCountDownSubscription() {
            @Override
            protected void onClockCountDownUpdated(Duration duration) {
                internalSubscription.onClockCountDownUpdated(duration);
            }
        };
    }

    private DurationMatcher matchesDuration(int sec, int millis) {
        return new DurationMatcher(ofSeconds(sec).plusMillis(millis));
    }

    private class DurationMatcher extends ArgumentMatcher<Duration> {

        private Duration expectedDuration;

        public DurationMatcher(Duration expectedDuration) {
            this.expectedDuration = expectedDuration;
        }

        @Override
        public boolean matches(Object argument) {
            Duration duration = (Duration) argument;
            return (duration.equals(expectedDuration));
        }
    }

}