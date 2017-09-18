package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.in.PingMessage;
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


    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("PING", pingMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsPingMessageClass() {
        assertEquals(PingMessage.class, pingMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessage_returnsPingMessage() {
        assertNotNull(pingMessageInterpreter.decode("PING"));
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        pingMessageInterpreter.encode(pingMessage);
    }

}
