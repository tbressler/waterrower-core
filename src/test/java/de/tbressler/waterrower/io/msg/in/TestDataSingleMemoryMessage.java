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
public class TestDataSingleMemoryMessage {

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationLowerThan0_throwsIAE() {
        new DataSingleMemoryMessage(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withLocationGreaterThan4095_throwsIAE() {
        new DataSingleMemoryMessage(4096, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValueLowerThan0_throwsIAE() {
        new DataSingleMemoryMessage(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValueGreaterThan255_throwsIAE() {
        new DataSingleMemoryMessage(1, 256);
    }


    @Test
    public void getLocation_whenConstructedWith100_returns100() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(100, 1);
        assertEquals(100, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith0_returns0() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(0, 1);
        assertEquals(0, msg.getLocation());
    }

    @Test
    public void getLocation_whenConstructedWith4095_returns4095() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(4095, 1);
        assertEquals(4095, msg.getLocation());
    }


    @Test
    public void getValue_whenConstructedWith100_returns100() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(1, 100);
        assertEquals(100, msg.getValue());
    }

    @Test
    public void getValue_whenConstructedWith0_returns0() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(1, 0);
        assertEquals(0, msg.getValue());
    }

    @Test
    public void getValue_whenConstructedWith255_returns255() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(1, 255);
        assertEquals(255, msg.getValue());
    }


    @Test
    public void toString_returnsObjectInfo() {
        DataSingleMemoryMessage msg = new DataSingleMemoryMessage(1, 1);
        assertTrue(msg.toString().startsWith("DataSingleMemoryMessage"));
    }

}
