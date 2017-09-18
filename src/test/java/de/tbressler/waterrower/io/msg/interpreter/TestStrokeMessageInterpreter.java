package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.in.StrokeMessage;
import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.model.StrokeType.END_OF_STROKE;
import static de.tbressler.waterrower.model.StrokeType.START_OF_STROKE;
import static org.junit.Assert.*;

/**
 * Tests for class StrokeMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStrokeMessageInterpreter {

    /* Class under test. */
    private StrokeMessageInterpreter strokeMessageInterpreter;


    @Before
    public void setUp() {
        strokeMessageInterpreter = new StrokeMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsS() {
        assertEquals("S", strokeMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsStrokeMessageClass() {
        assertEquals(StrokeMessage.class, strokeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withStrokeStart_returnsStrokeStartMessage() {
        StrokeMessage msg = strokeMessageInterpreter.decode("SS");

        assertNotNull(msg);
        assertEquals(START_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void decode_withStrokeEnd_returnsStrokeEndMessage() {
        StrokeMessage msg = strokeMessageInterpreter.decode("SE");

        assertNotNull(msg);
        assertEquals(END_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void decode_withInvalidMessage_returnsNull() {
        StrokeMessage msg = strokeMessageInterpreter.decode("SB");

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage_returnsNull() {
        StrokeMessage msg = strokeMessageInterpreter.decode("S");

        assertNull(msg);
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        strokeMessageInterpreter.encode(new StrokeMessage(START_OF_STROKE));
    }

}
