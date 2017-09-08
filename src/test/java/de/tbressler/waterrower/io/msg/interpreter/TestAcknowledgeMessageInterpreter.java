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


    /**
     * Checks if getMessageTypeChar always returns 'O'.
     */
    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("O", acknowledgeMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns AcknowledgeMessage.class.
     */
    @Test
    public void getMessageType_returnsAcknowledgeMessageClass() {
        assertEquals(AcknowledgeMessage.class, acknowledgeMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessage_returnsAcknowledgeMessage() {
        byte[] bytes = new String("OK").getBytes();
        assertNotNull(acknowledgeMessageInterpreter.decode(bytes));
    }


    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        acknowledgeMessageInterpreter.encode(acknowledgeMessage);
    }

}
