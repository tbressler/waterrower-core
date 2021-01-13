package de.tbressler.waterrower.workout;

import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.workout.WorkoutUnit.*;
import static org.junit.Assert.*;

/**
 * Tests for class Workout.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkout {

    // Class under test.
    private Workout workout;


    @Before
    public void setUp() {
        workout = new Workout(1, METERS);
    }


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullUnit_throwsNPE() {
        new Workout(1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitMeters_throwsIAE() {
        new Workout(0, METERS);
    }


    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0xFA01AndUnitMeters_throwsIAE() {
        new Workout(0xFA01, METERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitMiles_throwsIAE() {
        new Workout(0, MILES);
    }


    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0xFA01AndUnitMiles_throwsIAE() {
        new Workout(0xFA01, MILES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitKms_throwsIAE() {
        new Workout(0, KMS);
    }


    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0xFA01AndUnitKms_throwsIAE() {
        new Workout(0xFA01, KMS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitStrokes_throwsIAE() {
        new Workout(0, STROKES);
    }


    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0x1389AndUnitStrokes_throwsIAE() {
        new Workout(0x1389, STROKES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0AndUnitSeconds_throwsIAE() {
        new Workout(0, SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withDistance0x4651AndUnitSeconds_throwsIAE() {
        new Workout(0x4651, SECONDS);
    }

    // Units:

    @Test
    public void getUnit_forMeters_returnsMeters() {
        workout = new Workout(1234, METERS);
        assertEquals(METERS, workout.getUnit());
    }

    @Test
    public void getUnit_forMiles_returnsMiles() {
        workout = new Workout(4321, MILES);
        assertEquals(MILES, workout.getUnit());
    }

    @Test
    public void getUnit_forKms_returnsKms() {
        workout = new Workout(0xFA00, KMS);
        assertEquals(KMS, workout.getUnit());
    }

    @Test
    public void getUnit_forStrokes_returnsStrokes() {
        workout = new Workout(0x1388, STROKES);
        assertEquals(STROKES, workout.getUnit());
    }

    @Test
    public void getUnit_forSeconds_returnsSeconds() {
        workout = new Workout(0x4650, SECONDS);
        assertEquals(SECONDS, workout.getUnit());
    }

    // isSingleWorkout:

    @Test
    public void isSingleWorkout_withNoAdditionalInterval_returnsTrue() {
        assertTrue(workout.isSingleWorkout());
    }

    @Test
    public void isSingleWorkout_withAdditionalInterval_returnsFalse() {
        workout.addInterval(1, 2);
        assertFalse(workout.isSingleWorkout());
    }

    // isIntervalWorkout:

    @Test
    public void isIntervalWorkout_withNoAdditionalInterval_returnsFalse() {
        assertFalse(workout.isIntervalWorkout());
    }

    @Test
    public void isIntervalWorkout_withAdditionalInterval_returnsTrue() {
        workout.addInterval(1, 2);
        assertTrue(workout.isIntervalWorkout());
    }

    // getWorkoutIntervals:

    @Test
    public void getWorkoutIntervals_withNoAdditionalInterval_returnsOneInterval() {
        workout = new Workout(1000, METERS);

        assertEquals(1, workout.getWorkoutIntervals().size());
        assertWorkoutInterval(0, 0, 1000, METERS);
    }

    // Add intervals:

    @Test
    public void addInterval_withOneAdditionalInterval() {
        workout = new Workout(1000, METERS);
        workout.addInterval(1, 2000);

        assertEquals(2, workout.getWorkoutIntervals().size());
        assertWorkoutInterval(0, 0, 1000, METERS);
        assertWorkoutInterval(1, 1, 2000, METERS);
    }

    @Test
    public void addInterval_withTwoAdditionalInterval() {
        workout = new Workout(1000, MILES);
        workout.addInterval(1, 2000);
        workout.addInterval(60, 3000);

        assertEquals(3, workout.getWorkoutIntervals().size());
        assertWorkoutInterval(0, 0, 1000, MILES);
        assertWorkoutInterval(1, 1, 2000, MILES);
        assertWorkoutInterval(2, 60, 3000, MILES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addInterval_withRestInterval0_throwsIAE() {
        workout = new Workout(1000, MILES);
        workout.addInterval(0, 2000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addInterval_withRestInterval0x0E11_throwsIAE() {
        workout = new Workout(1000, MILES);
        workout.addInterval(0x0E11, 2000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addInterval_withDistance0_throwsIAE() {
        workout = new Workout(1000, METERS);
        workout.addInterval(60, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addInterval_withDistance0xFA00_throwsIAE() {
        workout = new Workout(1000, METERS);
        workout.addInterval(60, 0xFA01);
    }

    @Test(expected = IllegalStateException.class)
    public void addInterval_addTwoManyIntervals_throwsIllegalStateException() {
        workout = new Workout(1000, METERS);

        for(int i=0; i<9; i++)
            workout.addInterval(5, 2000);
    }


    // toString:

    @Test
    public void toString_returnsObjectInfo() {
        assertTrue(workout.toString().startsWith("Workout"));
    }


    // Helper methods:

    private void assertWorkoutInterval(int index, int restInterval, int distance, WorkoutUnit unit) {
        assertEquals(distance, workout.getWorkoutIntervals().get(index).getValue());
        assertEquals(unit, workout.getWorkoutIntervals().get(index).getUnit());
        assertEquals(restInterval, workout.getWorkoutIntervals().get(index).getRestInterval());
    }

}
