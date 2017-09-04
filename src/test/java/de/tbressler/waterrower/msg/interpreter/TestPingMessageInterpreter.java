package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.in.PingMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Tests for class PingMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPingMessageInterpreter {

    /* Class under test. */
    private PingMessageInterpreter pingMessageInterpreter;

    // Mocks:
    private PingMessage pingMessage = mock(PingMessage.class, "pingMessage");


    @Before
    public void setUp() {
        pingMessageInterpreter = new PingMessageInterpreter();
    }


    /**
     * Checks if getMessageTypeChar always returns 'P'.
     */
    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("P", pingMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns PingMessage.class.
     */
    @Test
    public void getMessageType_returnsPingMessageClass() {
        assertEquals(PingMessage.class, pingMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessage_returnsPingMessage() {
        byte[] bytes = new String("PING").getBytes();
        assertNotNull(pingMessageInterpreter.decode(bytes));
    }


    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        pingMessageInterpreter.encode(pingMessage);
    }

}
