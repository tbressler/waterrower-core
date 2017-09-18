package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for class StartCommunicationMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStartCommunicationMessageInterpreter {

    /* Class under test. */
    private StartCommunicationMessageInterpreter interpreter;


    @Before
    public void setUp() {
        interpreter = new StartCommunicationMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsNull() {
        assertNull(interpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsStartCommunicationMessageClass() {
        assertEquals(StartCommunicationMessage.class, interpreter.getMessageType());
    }


    @Test(expected = IllegalStateException.class)
    public void decode_throwsIllegalStateException() {
        interpreter.decode("Something");
    }


    @Test
    public void encode_withValidMessage_returnsUSB() {
        String result = interpreter.encode(new StartCommunicationMessage());
        assertNotNull(result);
        assertEquals("USB", result);
    }

}