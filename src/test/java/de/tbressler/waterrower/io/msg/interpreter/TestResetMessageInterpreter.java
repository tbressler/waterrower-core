package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.out.ResetMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class ResetMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestResetMessageInterpreter {

    /* Class under test. */
    private ResetMessageInterpreter interpreter;


    @Before
    public void setUp() {
        interpreter = new ResetMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsNull() {
        assertNull(interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        ResetMessage msg = mock(ResetMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test(expected = IllegalStateException.class)
    public void decode_throwsIllegalStateException() {
        interpreter.decode("Something");
    }


    @Test
    public void encode_withValidMessage_returnsUSB() {
        String result = interpreter.encode(new ResetMessage());
        assertNotNull(result);
        assertEquals("RESET", result);
    }

}