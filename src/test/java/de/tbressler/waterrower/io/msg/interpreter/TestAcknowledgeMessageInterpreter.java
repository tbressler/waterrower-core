package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.in.AcknowledgeMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Tests for class AcknowledgeMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAcknowledgeMessageInterpreter {

    /* Class under test. */
    private AcknowledgeMessageInterpreter acknowledgeMessageInterpreter;

    // Mocks:
    private AcknowledgeMessage acknowledgeMessage = mock(AcknowledgeMessage.class, "acknowledgeMessage");


    @Before
    public void setUp() {
        acknowledgeMessageInterpreter = new AcknowledgeMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsOK() {
        assertEquals("OK", acknowledgeMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsAcknowledgeMessageClass() {
        assertEquals(AcknowledgeMessage.class, acknowledgeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessage_returnsAcknowledgeMessage() {
        assertNotNull(acknowledgeMessageInterpreter.decode("OK"));
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        acknowledgeMessageInterpreter.encode(acknowledgeMessage);
    }

}
