package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class StartCommunicationMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStartCommunicationMessageInterpreter {

    /* Class under test. */
    private StartCommunicationMessageInterpreter interpreter;


    @BeforeEach
    public void setUp() {
        interpreter = new StartCommunicationMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsNull() {
        assertNull(interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        StartCommunicationMessage msg = mock(StartCommunicationMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> interpreter.decode("Something"));
    }


    @Test
    public void encode_withValidMessage_returnsUSB() {
        String result = interpreter.encode(new StartCommunicationMessage());
        assertNotNull(result);
        assertEquals("USB", result);
    }

}