package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.AcknowledgeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class AcknowledgeMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAcknowledgeMessageInterpreter {

    /* Class under test. */
    private AcknowledgeMessageInterpreter interpreter;

    // Mocks:
    private AcknowledgeMessage acknowledgeMessage = mock(AcknowledgeMessage.class, "acknowledgeMessage");


    @BeforeEach
    public void setUp() {
        interpreter = new AcknowledgeMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsOK() {
        assertEquals("OK", interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        AcknowledgeMessage msg = mock(AcknowledgeMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_withValidMessage_returnsAcknowledgeMessage() {
        assertNotNull(interpreter.decode("OK"));
    }


    @Test
    public void encode_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> interpreter.encode(acknowledgeMessage));
    }

}
