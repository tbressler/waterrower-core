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


    // Constructors:

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

    @Test(expected = IllegalArgumentException.class)
    public void new_withValue2LowerThan0_throwsIAE() {
        new DataMemoryMessage(1, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValue2GreaterThan255_throwsIAE() {
        new DataMemoryMessage(1, 256, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValue3LowerThan0_throwsIAE() {
        new DataMemoryMessage(1, -1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withValue3GreaterThan255_throwsIAE() {
        new DataMemoryMessage(1, 256, 0, 0);
    }


    // getLocations:

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


    // getValue1:

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

    @Test
    public void getValue1AsACH_whenConstructedWith255_returns91() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 145);
        assertEquals("91", msg.getValue1AsACH());
    }


    // getValue2:

    @Test
    public void getValue2_whenConstructedWith100_returns100() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 200, 100);
        assertEquals(200, msg.getValue2());
    }

    @Test
    public void getValue2_whenConstructedWith0_returns0() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 50, 0);
        assertEquals(50, msg.getValue2());
    }

    @Test
    public void getValue2_whenConstructedWith255_returns255() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 255, 1);
        assertEquals(255, msg.getValue2());
    }

    @Test
    public void getValue2AsACH_whenConstructedWith255_returns91() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 145, 0);
        assertEquals("91", msg.getValue2AsACH());
    }


    // getValue3:

    @Test
    public void getValue3_whenConstructedWith50_returns50() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 50, 0, 0);
        assertEquals(50, msg.getValue3());
    }

    @Test
    public void getValue3_whenConstructedWith100_returns100() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 100, 0, 0);
        assertEquals(100, msg.getValue3());
    }

    @Test
    public void getValue3_whenConstructedWith255_returns255() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 255, 0, 0);
        assertEquals(255, msg.getValue3());
    }

    @Test
    public void getValue3AsACH_whenConstructedWith255_returns91() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 145, 0, 0);
        assertEquals("91", msg.getValue3AsACH());
    }


    // toString:

    @Test
    public void toString_returnsObjectInfo() {
        DataMemoryMessage msg = new DataMemoryMessage(1, 1);
        assertTrue(msg.toString().startsWith("DataMemoryMessage"));
    }

}
