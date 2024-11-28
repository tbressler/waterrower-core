package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class ExitCommunicationMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestExitCommunicationMessageInterpreter {

    /* Class under test. */
    private ExitCommunicationMessageInterpreter interpreter;


    @BeforeEach
    public void setUp() {
        interpreter = new ExitCommunicationMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsNull() {
        assertNull(interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        ExitCommunicationMessage msg = mock(ExitCommunicationMessage.class, "message");
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
    public void encode_withValidMessage_returnsEXIT() {
        String result = interpreter.encode(new ExitCommunicationMessage());
        assertNotNull(result);
        assertEquals("EXIT", result);
    }

}