package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.in.PulseCountMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for class PulseCountMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPulseCountMessageInterpreter {

    /* Class under test. */
    private PulseCountMessageInterpreter pulseCountMessageInterpreter;


    @Before
    public void setUp() {
        pulseCountMessageInterpreter = new PulseCountMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsP() {
        assertEquals("P", pulseCountMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsStrokeMessageClass() {
        assertEquals(PulseCountMessage.class, pulseCountMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withPingMessage_returnsNull() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("PING");

        assertNull(msg);
    }

    @Test
    public void decode_withPulsesCount00_returnsMessageWith0() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("P00");

        assertNotNull(msg);
        assertEquals(0, msg.getPulsesCounted());
    }


    @Test
    public void decode_withPulsesCount01_returnsMessageWith1() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("P01");

        assertNotNull(msg);
        assertEquals(1, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCount10_returnsMessageWith16() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("P10");

        assertNotNull(msg);
        assertEquals(16, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCountFF_returnsMessageWith16() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("PFF");

        assertNotNull(msg);
        assertEquals(255, msg.getPulsesCounted());
    }

    @Test
    public void decode_withInvalidHexadecimal_returnsNull() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("PXY");

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage1_returnsNull() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("P");

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage2_returnsNull() {
        PulseCountMessage msg = pulseCountMessageInterpreter.decode("P0");

        assertNull(msg);
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        pulseCountMessageInterpreter.encode(new PulseCountMessage(12));
    }

}
