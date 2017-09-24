package de.tbressler.waterrower.io.msg.out;

import de.tbressler.waterrower.io.msg.out.ConfigureWorkoutMessage.MessageType;
import de.tbressler.waterrower.workout.WorkoutUnit;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.out.ConfigureWorkoutMessage.MessageType.*;
import static de.tbressler.waterrower.workout.WorkoutUnit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class ConfigureWorkoutMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestConfigureWorkoutMessage {

    @Test(expected = NullPointerException.class)
    public void new_withNullWorkoutUnit_throwsNPE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, null);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullMessageType_throwsNPE() {
        new ConfigureWorkoutMessage(null, 1, METERS);
    }

    // Single distance workouts (in Meters):

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitMeters_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 0, METERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance64001AndUnitMeters_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 64001, METERS);
    }

    @Test
    public void new_withDistance1AndUnitMeters() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, METERS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 1, METERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0xFFFF_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 0xFFFF, METERS);
    }

    @Test
    public void new_withDistance255AndUnitMeters() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 255, METERS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 255, METERS);
    }

    @Test
    public void new_withDistance64000AndUnitMeters() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 64000, METERS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 64000, METERS);
    }

    // Single distance workouts (in Miles):

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitMiles_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 0, MILES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance64001AndUnitMiles_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 64001, MILES);
    }

    @Test
    public void new_withDistance1AndUnitMiles() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, MILES);
        assertMessage(msg, SINGLE_WORKOUT, -1, 1, MILES);
    }

    @Test
    public void new_withDistance255AndUnitMiles() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 255, MILES);
        assertMessage(msg, SINGLE_WORKOUT, -1, 255, MILES);
    }

    @Test
    public void new_withDistance64000AndUnitMiles() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 64000, MILES);
        assertMessage(msg, SINGLE_WORKOUT, -1,  64000,  MILES);
    }

    // Single distance workouts (in Km's):

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitKms_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 0, KMS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance64001AndUnitKms_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 64001, KMS);
    }

    @Test
    public void new_withDistance1AndUnitKms() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, KMS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 1, KMS);
    }

    @Test
    public void new_withDistance255AndUnitKms() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 255, KMS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 255, KMS);
    }

    @Test
    public void new_withDistance64000AndUnitKms() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 64000, KMS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 64000, KMS);
    }

    // Single distance workouts (in Strokes):

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitStrokes_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 0, STROKES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance5001AndUnitStrokes_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 5001, STROKES);
    }

    @Test
    public void new_withDistance1AndUnitStrokes() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, STROKES);
        assertMessage(msg, SINGLE_WORKOUT, -1, 1, STROKES);
    }

    @Test
    public void new_withDistance255AndUnitStrokes() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 255, STROKES);
        assertMessage(msg, SINGLE_WORKOUT, -1, 255, STROKES);
    }

    @Test
    public void new_withDistance5000AndUnitStrokes() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 5000, STROKES);
        assertMessage(msg, SINGLE_WORKOUT, -1, 5000, STROKES);
    }

    // Single duration workouts (in Seconds):

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitSeconds_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 0, SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance18001AndUnitSeconds_throwsIAE() {
        new ConfigureWorkoutMessage(SINGLE_WORKOUT, 18001, SECONDS);
    }

    @Test
    public void new_withDistance1AndUnitSeconds() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, SECONDS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 1,SECONDS);
    }

    @Test
    public void new_withDistance255AndUnitSeconds() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 255, SECONDS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 255, SECONDS);
    }

    @Test
    public void new_withDistance18000AndUnitSeconds() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 18000, SECONDS);
        assertMessage(msg, SINGLE_WORKOUT, -1, 18000, SECONDS);
    }

    // Interval workouts:

    @Test
    public void new_startIntervalWorkout_withDistance123AndUnitMeters() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 123, METERS);
        assertMessage(msg, START_INTERVAL_WORKOUT, -1, 123, METERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_addIntervalWorkout_withRestInterval0x4651_throwsIAE() {
        new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 123, METERS, 0x4651);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_addIntervalWorkout_withRestInterval0x0000_throwsIAE() {
        new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 123, METERS, 0x0000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_addIntervalWorkout_withRestInterval0xFFFF_throwsIAE() {
        new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 123, METERS, 0xFFFF);
    }

    @Test
    public void new_addIntervalWorkout_withRestInterval255() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 123, METERS, 255);
        assertMessage(msg, ADD_INTERVAL_WORKOUT, 255, 123, METERS);
    }

    @Test
    public void new_endIntervalWorkout_withRestInterval0xFFFF() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(END_INTERVAL_WORKOUT, 123, METERS, 0xFFFF);
        assertMessage(msg, END_INTERVAL_WORKOUT, 0xFFFF, 123, METERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_endIntervalWorkout_withRestInterval0x0001_throwsIAE() {
        new ConfigureWorkoutMessage(END_INTERVAL_WORKOUT, 123, METERS, 0x0001);
    }

    @Test
    public void new_endIntervalWorkout_withRestInterval0xFFFFAndDistance0xFFFF() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(END_INTERVAL_WORKOUT, 0xFFFF, METERS, 0xFFFF);
        assertMessage(msg, END_INTERVAL_WORKOUT, 0xFFFF, 0xFFFF, METERS);
    }

    // Test of toString():

    @Test
    public void toString_returnsObjectInfo() {
        ConfigureWorkoutMessage msg = new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, METERS);
        assertTrue(msg.toString().startsWith("ConfigureWorkoutMessage"));
    }


    /* Assert the content of the message. */
    private void assertMessage(ConfigureWorkoutMessage msg, MessageType expectedMsgType, int expectedRestInterval, int expectedDistance, WorkoutUnit expectedUnit) {
        assertEquals(expectedMsgType, msg.getMessageType());
        assertEquals(expectedRestInterval, msg.getRestInterval());
        assertEquals(expectedDistance, msg.getDistance());
        assertEquals(expectedUnit, msg.getWorkoutUnit());
    }

}