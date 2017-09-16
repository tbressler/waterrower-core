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


    /**
     * Checks if getMessageTypeChar always returns 'S'.
     */
    @Test
    public void getMessageTypeChar_returnsS() {
        assertEquals("S", strokeMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns StrokeMessage.class.
     */
    @Test
    public void getMessageType_returnsStrokeMessageClass() {
        assertEquals(StrokeMessage.class, strokeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withStrokeStart_returnsStrokeStartMessage() {
        byte[] bytes = new String("SS").getBytes();

        StrokeMessage msg = strokeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(START_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void decode_withStrokeEnd_returnsStrokeEndMessage() {
        byte[] bytes = new String("SE").getBytes();

        StrokeMessage msg = strokeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(END_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void decode_withInvalidMessage_returnsNull() {
        byte[] bytes = new String("SB").getBytes();

        StrokeMessage msg = strokeMessageInterpreter.decode(bytes);

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage_returnsNull() {
        byte[] bytes = new String("S").getBytes();

        StrokeMessage msg = strokeMessageInterpreter.decode(bytes);

        assertNull(msg);
    }

    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        strokeMessageInterpreter.encode(new StrokeMessage(START_OF_STROKE));
    }

}
