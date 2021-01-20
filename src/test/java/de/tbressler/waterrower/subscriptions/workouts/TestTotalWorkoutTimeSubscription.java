package de.tbressler.waterrower.subscriptions.workouts;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.time.Duration;

import static de.tbressler.waterrower.model.MemoryLocation.WORKOUT_TIMEL;
import static java.time.Duration.ofSeconds;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Tests for class TotalWorkoutTimeSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestTotalWorkoutTimeSubscription {

    // Class under test.
    private TotalWorkoutTimeSubscription subscription;

    // Mocks:
    private TotalWorkoutTimeSubscription internalSubscription = mock(TotalWorkoutTimeSubscription.class, "internalSubscription");


    // Handle:

    @Test
    public void handle_withMessage_notifiesOnAverageStrokeTimeUpdated() {
        subscription = newTotalWorkoutTimeSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x00, 0x64));
        verify(internalSubscription, times(1)).onTimeUpdated(argThat(matchesDuration(100)));
    }

    @Test
    public void handle_withTwoSameMessages_notifiesOnAverageStrokeTimeUpdatedOnce() {
        subscription = newTotalWorkoutTimeSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x02, 0x00));
        subscription.handle(new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x02, 0x00));
        verify(internalSubscription, times(1)).onTimeUpdated(argThat(matchesDuration(512)));
    }

    @Test
    public void handle_withTwoUnequalMessages_notifiesOnAverageStrokeTimeUpdatedTwice() {
        subscription = newTotalWorkoutTimeSubscription();
        subscription.handle(new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x02, 0x00));
        subscription.handle(new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x30, 0xC6));
        verify(internalSubscription, times(1)).onTimeUpdated(argThat(matchesDuration(512)));
        verify(internalSubscription, times(1)).onTimeUpdated(argThat(matchesDuration(12486)));
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnClockCountDownUpdated() {
        subscription = newTotalWorkoutTimeSubscription();
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onTimeUpdated(any(Duration.class));
    }


    // Helper methods:

    private TotalWorkoutTimeSubscription newTotalWorkoutTimeSubscription() {
        return new TotalWorkoutTimeSubscription() {
            @Override
            protected void onTimeUpdated(Duration time) {
                internalSubscription.onTimeUpdated(time);
            }
        };
    }

    private DurationMatcher matchesDuration(int sec) {
        return new DurationMatcher(ofSeconds(sec));
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