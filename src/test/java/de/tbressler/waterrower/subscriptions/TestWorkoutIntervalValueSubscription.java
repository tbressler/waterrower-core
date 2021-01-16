package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;
import de.tbressler.waterrower.subscriptions.WorkoutIntervalValueSubscription.IntervalType;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.subscriptions.WorkoutIntervalValueSubscription.IntervalType.REST_INTERVAL;
import static de.tbressler.waterrower.subscriptions.WorkoutIntervalValueSubscription.IntervalType.ROW_INTERVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Tests for class WorkoutIntervalSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkoutIntervalValueSubscription {

    // Class under test.
    private WorkoutIntervalValueSubscription subscription;

    // Mocks:
    private WorkoutIntervalValueSubscription internalSubscription = mock(WorkoutIntervalValueSubscription.class, "internalSubscription");


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullIntervalType_throwsNPE() {
        newWorkoutIntervalSubscription(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withROW_INTERVALAndTooLowIntervalIndex_throwsNPE() {
        newWorkoutIntervalSubscription(ROW_INTERVAL, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withROW_INTERVALAndTooHighIntervalIndex_throwsNPE() {
        newWorkoutIntervalSubscription(ROW_INTERVAL, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withREST_INTERVALAndTooLowIntervalIndex_throwsNPE() {
        newWorkoutIntervalSubscription(REST_INTERVAL, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withREST_INTERVALAndTooHighIntervalIndex_throwsNPE() {
        newWorkoutIntervalSubscription(REST_INTERVAL, 9);
    }

    // Poll:

    @Test
    public void poll_forAllRowLocations_returnValidMessage() {
        assertPoll(ROW_INTERVAL, 1, WORKOUT_WORK1_L);
        assertPoll(ROW_INTERVAL, 2, WORKOUT_WORK2_L);
        assertPoll(ROW_INTERVAL, 3, WORKOUT_WORK3_L);
        assertPoll(ROW_INTERVAL, 4, WORKOUT_WORK4_L);
        assertPoll(ROW_INTERVAL, 5, WORKOUT_WORK5_L);
        assertPoll(ROW_INTERVAL, 6, WORKOUT_WORK6_L);
        assertPoll(ROW_INTERVAL, 7, WORKOUT_WORK7_L);
        assertPoll(ROW_INTERVAL, 8, WORKOUT_WORK8_L);
        assertPoll(ROW_INTERVAL, 9, WORKOUT_WORK9_L);
    }

    @Test
    public void poll_forAllRestLocations_returnValidMessage() {
        assertPoll(REST_INTERVAL, 1, WORKOUT_REST1_L);
        assertPoll(REST_INTERVAL, 2, WORKOUT_REST2_L);
        assertPoll(REST_INTERVAL, 3, WORKOUT_REST3_L);
        assertPoll(REST_INTERVAL, 4, WORKOUT_REST4_L);
        assertPoll(REST_INTERVAL, 5, WORKOUT_REST5_L);
        assertPoll(REST_INTERVAL, 6, WORKOUT_REST6_L);
        assertPoll(REST_INTERVAL, 7, WORKOUT_REST7_L);
        assertPoll(REST_INTERVAL, 8, WORKOUT_REST8_L);
    }

    private void assertPoll(IntervalType type, int index, MemoryLocation expectedLocation) {
        subscription = newWorkoutIntervalSubscription(type, index);
        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();

        assertNotNull(msg);
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(expectedLocation.getLocation(), msg.getLocation());
    }

    // Handle:

    @Test
    public void handle_forRowLocations_notifiesOnWorkoutIntervalUpdated() {
        assertHandle(ROW_INTERVAL, 1, WORKOUT_WORK1_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 2, WORKOUT_WORK2_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 3, WORKOUT_WORK3_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 4, WORKOUT_WORK4_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 5, WORKOUT_WORK5_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 6, WORKOUT_WORK6_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 7, WORKOUT_WORK7_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 8, WORKOUT_WORK8_L, 0x01, 0x02, 0x0102);
        assertHandle(ROW_INTERVAL, 9, WORKOUT_WORK9_L, 0x01, 0x02, 0x0102);
    }

    @Test
    public void handle_forRestLocations_notifiesOnWorkoutIntervalUpdated() {
        assertHandle(REST_INTERVAL, 1, WORKOUT_REST1_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 2, WORKOUT_REST2_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 3, WORKOUT_REST3_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 4, WORKOUT_REST4_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 5, WORKOUT_REST5_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 6, WORKOUT_REST6_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 7, WORKOUT_REST7_L, 0x01, 0x02, 0x0102);
        assertHandle(REST_INTERVAL, 8, WORKOUT_REST8_L, 0x01, 0x02, 0x0102);
    }

    private void assertHandle(IntervalType type, int index, MemoryLocation location, int value2, int value1, int expectedValue) {
        subscription = newWorkoutIntervalSubscription(type, index);
        DataMemoryMessage msg = new DataMemoryMessage(location.getLocation(), value2, value1);

        subscription.handle(msg);

        verify(internalSubscription, times(1)).onWorkoutIntervalUpdated(type, index, expectedValue);
    }

    @Test
    public void handle_for2EqualMessages_notifiesOnlyOnce() {
        subscription = newWorkoutIntervalSubscription(REST_INTERVAL, 2);
        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_REST2_L.getLocation(), 0x02, 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_REST2_L.getLocation(), 0x02, 0x01);

        subscription.handle(msg1);
        subscription.handle(msg2);

        verify(internalSubscription, times(1)).onWorkoutIntervalUpdated(REST_INTERVAL, 2, 0x0201);
    }

    @Test
    public void handle_for2DifferentMessages_notifiesTwice() {
        subscription = newWorkoutIntervalSubscription(REST_INTERVAL, 2);
        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_REST2_L.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_REST2_L.getLocation(), 0x02, 0x01);

        subscription.handle(msg1);
        subscription.handle(msg2);

        verify(internalSubscription, times(1)).onWorkoutIntervalUpdated(REST_INTERVAL, 2, 0x0102);
        verify(internalSubscription, times(1)).onWorkoutIntervalUpdated(REST_INTERVAL, 2, 0x0201);
    }


    // Helper methods:

    private WorkoutIntervalValueSubscription newWorkoutIntervalSubscription(IntervalType intervalType, int intervalIndex) {
        return new WorkoutIntervalValueSubscription(intervalType, intervalIndex) {
            @Override
            protected void onWorkoutIntervalUpdated(IntervalType intervalType, int intervalIndex, int value) {
                internalSubscription.onWorkoutIntervalUpdated(intervalType, intervalIndex, value);
            }
        };
    }

}