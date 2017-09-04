package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.in.HardwareTypeMessage;
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


    /**
     * Checks if getMessageTypeChar always returns '_'.
     */
    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("_", hardwareTypeMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns HardwareTypeMessage.class.
     */
    @Test
    public void getMessageType_returnsHardwareTypeMessageClass() {
        assertEquals(HardwareTypeMessage.class, hardwareTypeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidWaterRowerMessage_returnsHardwareTypeMessage() {
        byte[] bytes = new String("_WR_").getBytes();

        HardwareTypeMessage msg = hardwareTypeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertTrue(msg.isWaterRower());
    }

    @Test
    public void decode_withValidMessageButNotWaterRower_returnsHardwareTypeMessage() {
        byte[] bytes = new String("_SOME_").getBytes();

        HardwareTypeMessage msg = hardwareTypeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertFalse(msg.isWaterRower());
    }


    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        hardwareTypeMessageInterpreter.encode(hardwareTypeMessage);
    }

}
