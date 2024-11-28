package de.tbressler.waterrower.workout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.workout.WorkoutUnit.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for class WorkoutInterval.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWorkoutInterval {

    // Class under test.
    private WorkoutInterval interval;


    @BeforeEach
    public void setUp() {
        interval = new WorkoutInterval(1, 1, METERS);
    }


    // Constructor:

    @Test
    public void new1_withNullUnit_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new WorkoutInterval(1, null));
    }

    @Test
    public void new1_withDistance0_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0, METERS));
    }

    @Test
    public void new1_withDistance0xFA01andUnitMeters_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0xFA01, METERS));
    }

    @Test
    public void new1_withDistance0xFA00andUnitMeters_throwsNoException() {
        new WorkoutInterval(0xFA00, METERS);
    }

    @Test
    public void new1_withDistance0xFA01andUnitMiles_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0xFA01, MILES));
    }

    @Test
    public void new1_withDistance0xFA00andUnitMiles_throwsNoException() {
        new WorkoutInterval(0xFA00, MILES);
    }

    @Test
    public void new1_withDistance0xFA01andUnitKms_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0xFA01, KMS));
    }

    @Test
    public void new1_withDistance0xFA00andUnitKms_throwsNoException() {
        new WorkoutInterval(0xFA00, KMS);
    }

    @Test
    public void new1_withDistance0x1389andUnitStrokes_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0x1389, STROKES));
    }

    @Test
    public void new1_withDistance0x1388andUnitStrokes_throwsNoException() {
        new WorkoutInterval(0x1388, STROKES);
    }

    @Test
    public void new1_withDistance0x4651andUnitSeconds_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0x4651, SECONDS));
    }

    @Test
    public void new1_withDistance0x4650andUnitSeconds_throwsNoException() {
        new WorkoutInterval(0x4650, SECONDS);
    }

    @Test
    public void new2_withNullUnit_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new WorkoutInterval(1, 1, null));
    }

    @Test
    public void new2_withRestInterval0x0000_throwsNoException() {
        new WorkoutInterval(0x0000, 1, SECONDS);
    }

    @Test
    public void new2_withRestInterval0x0E10_throwsNoException() {
        new WorkoutInterval(0x0E10, 1234, METERS);
    }

    @Test
    public void new2_withRestInterval0x0E11_throwsNoException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutInterval(0x0E11, 4321, MILES));
    }


    // Check getter:

    @Test
    public void getRestInterval1_returnsValue() {
        interval = new WorkoutInterval(1, 4321, METERS);
        assertEquals(1, interval.getRestInterval());
    }

    @Test
    public void getRestInterval1234_returnsValue() {
        interval = new WorkoutInterval(1234, 4321, METERS);
        assertEquals(1234, interval.getRestInterval());
    }

    @Test
    public void getRestInterval_whenConstructedWithoutRestInterval_returnsValue0() {
        interval = new WorkoutInterval(4321, METERS);
        assertEquals(0, interval.getRestInterval());
    }

    @Test
    public void getDistance1_returnsValue() {
        interval = new WorkoutInterval(1234, 1, METERS);
        assertEquals(1, interval.getValue());
    }

    @Test
    public void getDistance4321_returnsValue() {
        interval = new WorkoutInterval(1234, 4321, METERS);
        assertEquals(4321, interval.getValue());
    }

    @Test
    public void getUnitStrokes_returnsValue() {
        interval = new WorkoutInterval(1234, 1, STROKES);
        assertEquals(STROKES, interval.getUnit());
    }

    @Test
    public void getUnitSeconds_returnsValue() {
        interval = new WorkoutInterval(1234, 4321, SECONDS);
        assertEquals(SECONDS, interval.getUnit());
    }

    // toString:

    @Test
    public void toString_returnsObjectInfo() {
        assertTrue(interval.toString().startsWith("WorkoutInterval"));
    }

}