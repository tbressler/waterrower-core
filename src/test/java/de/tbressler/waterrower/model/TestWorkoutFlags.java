package de.tbressler.waterrower.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for class WorkoutFlags.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkoutFlags {

    // Constructor:

    @Test(expected = IllegalArgumentException.class)
    public void new_withTooLowValue_throwsIAE() {
        new WorkoutFlags(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withTooHighValue_throwsIAE() {
        new WorkoutFlags(0x100);
    }


    // Methods:

    @Test
    public void isWorkingInHeartRateZone_with0x01_returnsTrue() {
        assertTrue(new WorkoutFlags(0x01).isWorkingInHeartRateZone());
    }

    @Test
    public void isWorkingInHeartRateZone_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkingInHeartRateZone());
    }


    @Test
    public void isWorkingInIntensityZone_with0x02_returnsTrue() {
        assertTrue(new WorkoutFlags(0x02).isWorkingInIntensityZone());
    }

    @Test
    public void isWorkingInIntensityZone_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkingInIntensityZone());
    }


    @Test
    public void isWorkingInStrokeRateZone_with0x04_returnsTrue() {
        assertTrue(new WorkoutFlags(0x04).isWorkingInStrokeRateZone());
    }

    @Test
    public void isWorkingInStrokeRateZone_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkingInStrokeRateZone());
    }


    @Test
    public void isPrognosticsActive_with0x08_returnsTrue() {
        assertTrue(new WorkoutFlags(0x08).isPrognosticsActive());
    }

    @Test
    public void isPrognosticsActive_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isPrognosticsActive());
    }


    @Test
    public void isWorkoutDistanceMode_with0x10_returnsTrue() {
        assertTrue(new WorkoutFlags(0x10).isWorkoutDistanceMode());
    }

    @Test
    public void isWorkoutDistanceMode_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkoutDistanceMode());
    }


    @Test
    public void isWorkoutDurationMode_with0x20_returnsTrue() {
        assertTrue(new WorkoutFlags(0x20).isWorkoutDurationMode());
    }

    @Test
    public void isWorkoutDurationMode_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkoutDurationMode());
    }


    @Test
    public void isWorkoutDistanceIntervalMode_with0x40_returnsTrue() {
        assertTrue(new WorkoutFlags(0x40).isWorkoutDistanceIntervalMode());
    }

    @Test
    public void isWorkoutDistanceIntervalMode_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkoutDistanceIntervalMode());
    }


    @Test
    public void isWorkoutDurationIntervalMode_with0x80_returnsTrue() {
        assertTrue(new WorkoutFlags(0x80).isWorkoutDurationIntervalMode());
    }

    @Test
    public void isWorkoutDurationIntervalMode_with0x00_returnsFalse() {
        assertFalse(new WorkoutFlags(0x00).isWorkoutDurationIntervalMode());
    }


    // Equals / hash code:

    @Test
    public void equals_testEqualsContract() {
        WorkoutFlags a = new WorkoutFlags(1);
        WorkoutFlags b = new WorkoutFlags(1);
        WorkoutFlags c = new WorkoutFlags(2);
        Integer d = Integer.valueOf(2);

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
        assertFalse(a.equals(null));
    }

    @Test
    public void equals_testHashCodeContract() {
        WorkoutFlags a = new WorkoutFlags(1);
        WorkoutFlags b = new WorkoutFlags(1);
        WorkoutFlags c = new WorkoutFlags(2);

        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }

}