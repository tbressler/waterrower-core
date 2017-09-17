package de.tbressler.waterrower.io.msg.out;

import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class ReadSingleMemoryMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestReadMemoryMessage {

    @Test(expected = NullPointerException.class)
    public void new_withNullMemory_throwsNPE() {
        new ReadMemoryMessage(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationLowerThan0_throwsIAE() {
        new ReadMemoryMessage(SINGLE_MEMORY, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationGreaterThan4095_throwsIAE() {
        new ReadMemoryMessage(SINGLE_MEMORY, 4096);
    }


    @Test
    public void getLocation_whenConstructedWith100_returns100() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 100);
        assertEquals(100, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith0_returns0() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 0);
        assertEquals(0, msg.getLocation());
    }


    @Test
    public void getMemory_whenConstructedWithSingleMemory_returnsSingleMemory() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 1);
        assertEquals(SINGLE_MEMORY, msg.getMemory());
    }

    @Test
    public void getMemory_whenConstructedWithDoubleMemory_returnsDoubleMemory() {
        ReadMemoryMessage msg = new ReadMemoryMessage(DOUBLE_MEMORY, 1);
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
    }

    @Test
    public void getMemory_whenConstructedWithTripleMemory_returnsTripleMemory() {
        ReadMemoryMessage msg = new ReadMemoryMessage(TRIPLE_MEMORY, 1);
        assertEquals(TRIPLE_MEMORY, msg.getMemory());
    }


    @Test
    public void getLocation_whenConstructedWith4095_returns4095() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 4095);
        assertEquals(4095, msg.getLocation());
    }


    @Test
    public void toString_returnsObjectInfo() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 1);
        assertTrue(msg.toString().startsWith("ReadMemoryMessage"));
    }

}
