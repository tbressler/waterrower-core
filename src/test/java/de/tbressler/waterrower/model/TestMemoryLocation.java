package de.tbressler.waterrower.model;

import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.utils.ASCIIUtils.intToAch;
import static org.junit.Assert.assertEquals;

/**
 * Tests for enum MemoryLocation.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMemoryLocation {

    /* Check all memory locations of the enums: */

    @Test public void ms_distance_dec() { assertMemoryLocation(MEMORY_MS_DISTANCE_DEC, "054"); }
    @Test public void ms_distance_low() { assertMemoryLocation(MEMORY_MS_DISTANCE_LOW, "055"); }
    @Test public void ms_distance_hi() { assertMemoryLocation(MEMORY_MS_DISTANCE_HI, "056"); }

    @Test public void distance_low() { assertMemoryLocation(MEMORY_DISTANCE_LOW, "057"); }
    @Test public void distance_hi() { assertMemoryLocation(MEMORY_DISTANCE_HI, "058"); }

    @Test public void tank_volume() { assertMemoryLocation(MEMORY_TANK_VOLUME, "0A9"); }

    @Test public void strokes_cnt_low() { assertMemoryLocation(MEMORY_STROKES_CNT_LOW, "140"); }
    @Test public void strokes_cnt_hi() { assertMemoryLocation(MEMORY_STROKES_CNT_HI, "141"); }
    @Test public void stroke_average() { assertMemoryLocation(MEMORY_STROKE_AVERAGE, "142"); }
    @Test public void stroke_pull() { assertMemoryLocation(MEMORY_STROKE_PULL, "143"); }


    /* Assert the memory locations. */
    private void assertMemoryLocation(MemoryLocation location, String expected) {
        assertEquals(expected, intToAch(location.getLocation(), 3));
    }

}
