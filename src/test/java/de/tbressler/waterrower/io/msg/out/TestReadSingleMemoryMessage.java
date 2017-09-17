package de.tbressler.waterrower.io.msg.out;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class ReadSingleMemoryMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestReadSingleMemoryMessage {

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationLowerThan0_throwsIAE() {
        new ReadSingleMemoryMessage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationGreaterThan4095_throwsIAE() {
        new ReadSingleMemoryMessage(4096);
    }


    @Test
    public void getLocation_whenConstructedWith100_returns100() {
        ReadSingleMemoryMessage msg = new ReadSingleMemoryMessage(100);
        assertEquals(100, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith0_returns0() {
        ReadSingleMemoryMessage msg = new ReadSingleMemoryMessage(0);
        assertEquals(0, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith4095_returns4095() {
        ReadSingleMemoryMessage msg = new ReadSingleMemoryMessage(4095);
        assertEquals(4095, msg.getLocation());
    }


    @Test
    public void toString_returnsObjectInfo() {
        ReadSingleMemoryMessage msg = new ReadSingleMemoryMessage(1);
        assertTrue(msg.toString().startsWith("ReadSingleMemoryMessage"));
    }

}
