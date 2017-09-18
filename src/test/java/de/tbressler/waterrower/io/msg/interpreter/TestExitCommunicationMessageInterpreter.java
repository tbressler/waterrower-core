package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for class ExitCommunicationMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestExitCommunicationMessageInterpreter {

    /* Class under test. */
    private ExitCommunicationMessageInterpreter interpreter;


    @Before
    public void setUp() {
        interpreter = new ExitCommunicationMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsNull() {
        assertNull(interpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsExitCommunicationMessageClass() {
        assertEquals(ExitCommunicationMessage.class, interpreter.getMessageType());
    }


    @Test(expected = IllegalStateException.class)
    public void decode_throwsIllegalStateException() {
        interpreter.decode("Something");
    }


    @Test
    public void encode_withValidMessage_returnsEXIT() {
        String result = interpreter.encode(new ExitCommunicationMessage());
        assertNotNull(result);
        assertEquals("EXIT", result);
    }

}