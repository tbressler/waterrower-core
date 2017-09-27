package de.tbressler.waterrower.io.msg.in;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class DataSingleMemoryMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestDataMemoryMessage {

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationLowerThan0_throwsIAE() {
        new DataMemoryMessage(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationGreaterThan4095_throwsIAE() {
        new DataMemoryMessage(4096, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValue1LowerThan0_throwsIAE() {
        new DataMemoryMessage(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValue1GreaterThan255_throwsIAE() {
        new DataMemoryMessage(1, 256);
    }


    @Test
    public void getLocation_whenConstructedWith100_returns100() {
        DataMemoryMessage msg = new DataMemoryMessage(100, 1);
        assertEquals(100, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith0_returns0() {
        DataMemoryMessage msg = new DataMemoryMessage(0, 1);
        assertEquals(0, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith4095_returns4095() {
        DataMemoryMessage msg = new DataMemoryMessage(4095, 1);
        assertEquals(4095, msg.getLocation());
    }


    @Test
    public void getValue1_whenConstructedWith100_returns100() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 100);
        assertEquals(100, msg.getValue1());
    }

    @Test
    public void getValue1_whenConstructedWith0_returns0() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 0);
        assertEquals(0, msg.getValue1());
    }

    @Test
    public void getValue1_whenConstructedWith255_returns255() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 255);
        assertEquals(255, msg.getValue1());
    }


    // TODO Test other constructors!


    @Test
    public void toString_returnsObjectInfo() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 1);
        assertTrue(msg.toString().startsWith("DataMemoryMessage"));
    }

}
