package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.in.HardwareTypeMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class HardwareTypeMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestHardwareTypeMessageInterpreter {

    /* Class under test. */
    private HardwareTypeMessageInterpreter hardwareTypeMessageInterpreter;

    // Mocks:
    private HardwareTypeMessage hardwareTypeMessage = mock(HardwareTypeMessage.class, "hardwareTypeMessage");


    @Before
    public void setUp() {
        hardwareTypeMessageInterpreter = new HardwareTypeMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returns_() {
        assertEquals("_", hardwareTypeMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsHardwareTypeMessageClass() {
        assertEquals(HardwareTypeMessage.class, hardwareTypeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidWaterRowerMessage_returnsHardwareTypeMessage() {
        HardwareTypeMessage msg = hardwareTypeMessageInterpreter.decode("_WR_");

        assertNotNull(msg);
        assertTrue(msg.isWaterRower());
    }

    @Test
    public void decode_withValidMessageButNotWaterRower_returnsHardwareTypeMessage() {
        HardwareTypeMessage msg = hardwareTypeMessageInterpreter.decode("_SOME_");

        assertNotNull(msg);
        assertFalse(msg.isWaterRower());
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        hardwareTypeMessageInterpreter.encode(hardwareTypeMessage);
    }

}
