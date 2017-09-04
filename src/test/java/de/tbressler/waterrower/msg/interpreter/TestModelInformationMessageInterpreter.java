package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.in.ModelInformationMessage;
import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.model.MonitorType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class ModelInformationMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestModelInformationMessageInterpreter {

    /* Class under test. */
    private ModelInformationMessageInterpreter hardwareTypeMessageInterpreter;

    // Mocks:
    private ModelInformationMessage modelInformationMessage = mock(ModelInformationMessage.class, "modelInformationMessage");


    @Before
    public void setUp() {
        hardwareTypeMessageInterpreter = new ModelInformationMessageInterpreter();
    }


    /**
     * Checks if getMessageTypeChar always returns '_'.
     */
    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("I", hardwareTypeMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns ModelInformationMessage.class.
     */
    @Test
    public void getMessageType_returnsModelInformationMessageClass() {
        assertEquals(ModelInformationMessage.class, hardwareTypeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessageForS4_returnsMessage() {
        byte[] bytes = new String("IV40200").getBytes();

        ModelInformationMessage msg = hardwareTypeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(WATER_ROWER_S4, msg.getMonitorType());
        assertEquals("02.00", msg.getFirmwareVersion());
    }

    @Test
    public void decode_withValidMessageForS5_returnsMessage() {
        byte[] bytes = new String("IV51234").getBytes();

        ModelInformationMessage msg = hardwareTypeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(WATER_ROWER_S5, msg.getMonitorType());
        assertEquals("12.34", msg.getFirmwareVersion());
    }

    @Test
    public void decode_withValidMessageForUnknown_returnsMessage() {
        byte[] bytes = new String("IVX1234").getBytes();

        ModelInformationMessage msg = hardwareTypeMessageInterpreter.decode(bytes);

        assertNotNull(msg);
        assertEquals(UNKNOWN_MONITOR_TYPE, msg.getMonitorType());
        assertEquals("12.34", msg.getFirmwareVersion());
    }

    @Test
    public void decode_withTooShortMessageForUnknown_returnsNull() {
        byte[] bytes = new String("IV1").getBytes();
        assertNull(hardwareTypeMessageInterpreter.decode(bytes));
    }


    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        hardwareTypeMessageInterpreter.encode(modelInformationMessage);
    }

}
