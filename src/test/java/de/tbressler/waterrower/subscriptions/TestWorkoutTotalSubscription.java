package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;
import de.tbressler.waterrower.subscriptions.WorkoutTotalSubscription.ValueType;
import org.junit.Test;
import org.mockito.Mockito;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.subscriptions.WorkoutTotalSubscription.ValueType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for class WorkoutTotalSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkoutTotalSubscription {

    // Class under test.
    private WorkoutTotalSubscription subscription;

    // Mocks:
    private WorkoutTotalSubscription internalSubscription = Mockito.mock(WorkoutTotalSubscription.class, "internalSubscription");


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNull_throwsNPE() {
        newWorkoutTotalSubscription(null);
    }

    // Poll:

    @Test
    public void poll_returnsValidMessage() {
        assertPoll(TIME, WORKOUT_TIMEL);
        assertPoll(INTENSITY, WORKOUT_MS_L);
        assertPoll(STROKES, WORKOUT_STROKEL);
        assertPoll(LIMIT, WORKOUT_LIMIT_H);
    }

    private void assertPoll(ValueType type, MemoryLocation expectedLocation) {
        subscription = newWorkoutTotalSubscription(type);
        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();

        assertNotNull(msg);
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(expectedLocation.getLocation(), msg.getLocation());
    }

    // Handle:

    @Test
    public void handle_notifiesOnTotalWorkoutValueUpdated() {
        assertHandle(TIME, WORKOUT_TIMEL, 0x01, 0x02, 0x0102);
        assertHandle(INTENSITY, WORKOUT_MS_L, 0x01, 0x02, 0x0102);
        assertHandle(STROKES, WORKOUT_STROKEL, 0x01, 0x02, 0x0102);
        assertHandle(LIMIT, WORKOUT_LIMIT_H, 0x01, 0x02, 0x0102);
    }

    private void assertHandle(ValueType type, MemoryLocation location, int value2, int value1, int expectedValue) {
        subscription = newWorkoutTotalSubscription(type);
        DataMemoryMessage msg = new DataMemoryMessage(location.getLocation(), value2, value1);

        subscription.handle(msg);

        verify(internalSubscription, times(1)).onTotalWorkoutValueUpdated(type, expectedValue);
    }

    @Test
    public void handle_for2EqualMessages_notifiesOnlyOnce() {
        subscription = newWorkoutTotalSubscription(TIME);
        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x02, 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x02, 0x01);

        subscription.handle(msg1);
        subscription.handle(msg2);

        verify(internalSubscription, times(1)).onTotalWorkoutValueUpdated(TIME, 0x0201);
    }

    @Test
    public void handle_for2DifferentMessages_notifiesTwice() {
        subscription = newWorkoutTotalSubscription(TIME);
        DataMemoryMessage msg1 = new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x01, 0x02);
        DataMemoryMessage msg2 = new DataMemoryMessage(WORKOUT_TIMEL.getLocation(), 0x02, 0x01);

        subscription.handle(msg1);
        subscription.handle(msg2);

        verify(internalSubscription, times(1)).onTotalWorkoutValueUpdated(TIME, 0x0102);
        verify(internalSubscription, times(1)).onTotalWorkoutValueUpdated(TIME, 0x0201);
    }


    // Helper methods:

    private WorkoutTotalSubscription newWorkoutTotalSubscription(ValueType valueType) {
        return new WorkoutTotalSubscription(valueType) {
            @Override
            void onTotalWorkoutValueUpdated(ValueType valueType, int value) {
                internalSubscription.onTotalWorkoutValueUpdated(valueType, value);
            }
        };
    }

}