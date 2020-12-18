package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.IMessageInterpreter;
import de.tbressler.waterrower.io.msg.in.AcknowledgeMessage;
import de.tbressler.waterrower.io.msg.in.DecodeErrorMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for class MessageParser.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMessageParser {

    // Class under test.
    private MessageParser parser;

    // Mocks:
    private IMessageInterpreter interpreter1 = mock(IMessageInterpreter.class, "interpreter1");
    private IMessageInterpreter interpreter2 = mock(IMessageInterpreter.class, "interpreter2");

    private List<IMessageInterpreter> interpreters = new ArrayList<>();

    private AbstractMessage message = mock(AcknowledgeMessage.class, "message");


    @Before
    public void setUp() {
        interpreters.add(interpreter1);
        interpreters.add(interpreter2);
        parser = new MessageParser(interpreters);
    }

    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNull_throwsNPE() {
        new MessageParser(null);
    }

    // Check interpreters:

    @Test
    public void checkInterpreters() {
        parser = new MessageParser();
        List<IMessageInterpreter> interpreters = parser.getInterpreters();
        assertEquals(11, interpreters.size());
    }

    // Decode:

    @Test
    public void decode_withSuccessfulInterpreter1_returnsMessage() {

        mockInterpreter(interpreter1, "T", "TEST", message);
        mockInterpreter(interpreter2, "X", "TEST", null);

        AbstractMessage result = parser.decode(newBytes("TEST"));

        assertEquals(message, result);
    }

    @Test
    public void decode_withSuccessfulInterpreter2_returnsMessage() {

        mockInterpreter(interpreter1, "T", "XYZ", null);
        mockInterpreter(interpreter2, "X", "XYZ", message);

        AbstractMessage result = parser.decode(newBytes("XYZ"));

        assertEquals(message, result);
    }

    @Test
    public void decode_withInterpreterThatReturnsNullAsIdentifier_returnsMessage() {

        mockInterpreter(interpreter1, null, "XYZ", null);
        mockInterpreter(interpreter2, "X", "XYZ", message);

        AbstractMessage result = parser.decode(newBytes("XYZ"));

        assertEquals(message, result);
    }

    @Test
    public void decode_withInterpretersReturningNull_returnsDecodeErrorMessage() {

        mockInterpreter(interpreter1, "Y", "TEST", null);
        mockInterpreter(interpreter2, "X", "TEST", null);

        AbstractMessage result = parser.decode(newBytes("XYZ"));

        assertNotNull(result);
        assertTrue(result instanceof DecodeErrorMessage);

        DecodeErrorMessage errorMessage = (DecodeErrorMessage) result;
        assertEquals("XYZ", errorMessage.getMessage());
    }

    // Encoder:

    @Test
    public void encode_withSupportedMessageType1_returnsMessage() {
        mockInterpreter(interpreter1, true, message, "TEST");
        mockInterpreter(interpreter2, false, message, null);

        byte[] result = parser.encode(message);

        assertArrayEquals("TEST".getBytes(UTF_8), result);
    }

    @Test
    public void encode_withSupportedMessageType2_returnsMessage() {
        mockInterpreter(interpreter1, false, message, null);
        mockInterpreter(interpreter2, true, message, "TEST");

        byte[] result = parser.encode(message);

        assertArrayEquals("TEST".getBytes(UTF_8), result);
    }

    @Test
    public void encode_withUnsupportedMessageType1_returnsMessage() {
        mockInterpreter(interpreter1, false, message, null);
        mockInterpreter(interpreter2, false, message, null);

        byte[] result = parser.encode(message);

        assertNull(result);
    }

    // Helper methods:

    private byte[] newBytes(String content) {
        return content.getBytes(UTF_8);
    }

    private void mockInterpreter(IMessageInterpreter interpreter, String identifier, String decodeMsg, AbstractMessage msg) {
        when(interpreter.getMessageIdentifier()).thenReturn(identifier);
        when(interpreter.decode(decodeMsg)).thenReturn(msg);
    }

    @SuppressWarnings("unchecked")
    private void mockInterpreter(IMessageInterpreter interpreter, boolean isSupported, AbstractMessage encodeMsg, String msg) {
        when(interpreter.isSupported(encodeMsg)).thenReturn(isSupported);
        when(interpreter.encode(encodeMsg)).thenReturn(msg);
    }

}