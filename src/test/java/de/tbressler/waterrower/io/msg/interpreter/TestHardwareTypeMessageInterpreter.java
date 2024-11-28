package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.HardwareTypeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class HardwareTypeMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestHardwareTypeMessageInterpreter {

    /* Class under test. */
    private HardwareTypeMessageInterpreter interpreter;

    // Mocks:
    private HardwareTypeMessage hardwareTypeMessage = mock(HardwareTypeMessage.class, "hardwareTypeMessage");


    @BeforeEach
    public void setUp() {
        interpreter = new HardwareTypeMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returns_() {
        assertEquals("_", interpreter.getMessageIdentifier());
    }



    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        HardwareTypeMessage msg = mock(HardwareTypeMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_withValidWaterRowerMessage_returnsHardwareTypeMessage() {
        HardwareTypeMessage msg = interpreter.decode("_WR_");

        assertNotNull(msg);
        assertTrue(msg.isWaterRower());
    }

    @Test
    public void decode_withValidMessageButNotWaterRower_returnsHardwareTypeMessage() {
        HardwareTypeMessage msg = interpreter.decode("_SOME_");

        assertNotNull(msg);
        assertFalse(msg.isWaterRower());
    }


    @Test
    public void encode_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> interpreter.encode(hardwareTypeMessage));
    }

}
