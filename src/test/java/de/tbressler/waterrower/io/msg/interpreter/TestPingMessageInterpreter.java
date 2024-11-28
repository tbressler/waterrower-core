package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.PingMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class PingMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPingMessageInterpreter {

    /* Class under test. */
    private PingMessageInterpreter interpreter;

    // Mocks:
    private PingMessage pingMessage = mock(PingMessage.class, "pingMessage");


    @BeforeEach
    public void setUp() {
        interpreter = new PingMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("PING", interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        PingMessage msg = mock(PingMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_withValidMessage_returnsPingMessage() {
        assertNotNull(interpreter.decode("PING"));
    }


    @Test
    public void encode_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> interpreter.encode(pingMessage));
    }

}
