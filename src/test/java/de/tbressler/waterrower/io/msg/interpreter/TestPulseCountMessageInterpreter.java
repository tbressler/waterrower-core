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


    /**
     * Checks if getMessageTypeChar always returns 'P'.
     */
    @Test
    public void getMessageTypeChar_returnsP() {
        assertEquals("P", pulseCountMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns PulseCountMessage.class.
     */
    @Test
    public void getMessageType_returnsStrokeMessageClass() {
        assertEquals(PulseCountMessage.class, pulseCountMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withPulsesCount00_returnsMessageWith0() {
        byte[] bytes = new String("P00").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(0, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCount01_returnsMessageWith1() {
        byte[] bytes = new String("P01").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(1, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCount10_returnsMessageWith16() {
        byte[] bytes = new String("P10").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(16, msg.getPulsesCounted());
    }

    @Test
    public void decode_withPulsesCountFF_returnsMessageWith16() {
        byte[] bytes = new String("PFF").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(255, msg.getPulsesCounted());
    }

    @Test
    public void decode_withInvalidHexadecimal_returnsNull() {
        byte[] bytes = new String("PXY").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage1_returnsNull() {
        byte[] bytes = new String("P").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage2_returnsNull() {
        byte[] bytes = new String("P0").getBytes();

        PulseCountMessage msg = pulseCountMessageInterpreter.decode(bytes);

        assertNull(msg);
    }

    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        pulseCountMessageInterpreter.encode(new PulseCountMessage(12));
    }

}
