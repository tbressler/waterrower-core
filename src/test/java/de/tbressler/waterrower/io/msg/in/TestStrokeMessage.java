package de.tbressler.waterrower.io.msg.in;

import org.junit.Test;

import static de.tbressler.waterrower.io.msg.in.StrokeMessage.StrokeType.END_OF_STROKE;
import static de.tbressler.waterrower.io.msg.in.StrokeMessage.StrokeType.START_OF_STROKE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class StrokeMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStrokeMessage {

    @Test(expected = NullPointerException.class)
    public void new_withNull_throwsNPE() {
        new StrokeMessage(null);
    }

    @Test
    public void getStrokeType_whenConstructedWithEndOfStroke_returnsEndOfStroke() {
        StrokeMessage msg = new StrokeMessage(END_OF_STROKE);
        assertEquals(END_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void getStrokeType_whenConstructedWithStartOfStroke_returnsStartOfStroke() {
        StrokeMessage msg = new StrokeMessage(START_OF_STROKE);
        assertEquals(START_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void toString_returnsObjectInfo() {
        StrokeMessage msg = new StrokeMessage(END_OF_STROKE);
        assertTrue(msg.toString().startsWith("StrokeMessage"));
    }

}
