package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.PulseCountMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class PulseCountMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPulseCountMessageInterpreter {

    /* Class under test. */
    private PulseCountMessageInterpreter interpreter;


    @BeforeEach
    public void setUp() {
        interpreter = new PulseCountMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsP() {
        assertEquals("P", interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        PulseCountMessage msg = mock(PulseCountMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_withPingMessage_returnsNull() {
        PulseCountMessage msg = interpreter.decode("PING");

        assertNull(msg);
    }

    @Test
    public void decode_withPulsesCount00_returnsMessageWith0() {
        PulseCountMessage msg = interpreter.decode("P00");

        assertNotNull(msg);
        assertEquals(0, msg.getPulsesCounted());
    }


    @Test
    public void decode_withPulsesCount01_returnsMessageWith1() {
        PulseCountMessage msg = interpreter.decode("P01");

        assertNotNull(msg);
        assertEquals(1, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCount10_returnsMessageWith16() {
        PulseCountMessage msg = interpreter.decode("P10");

        assertNotNull(msg);
        assertEquals(16, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCountFF_returnsMessageWith16() {
        PulseCountMessage msg = interpreter.decode("PFF");

        assertNotNull(msg);
        assertEquals(255, msg.getPulsesCounted());
    }

    @Test
    public void decode_withInvalidHexadecimal_returnsNull() {
        PulseCountMessage msg = interpreter.decode("PXY");

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage1_returnsNull() {
        PulseCountMessage msg = interpreter.decode("P");

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage2_returnsNull() {
        PulseCountMessage msg = interpreter.decode("P0");

        assertNull(msg);
    }


    @Test
    public void encode_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> interpreter.encode(new PulseCountMessage(12)));
    }

}
