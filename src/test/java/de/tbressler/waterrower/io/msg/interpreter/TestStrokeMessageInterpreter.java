package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.StrokeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.StrokeType.END_OF_STROKE;
import static de.tbressler.waterrower.model.StrokeType.START_OF_STROKE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class StrokeMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStrokeMessageInterpreter {

    /* Class under test. */
    private StrokeMessageInterpreter interpreter;


    @BeforeEach
    public void setUp() {
        interpreter = new StrokeMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsS() {
        assertEquals("S", interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        StrokeMessage msg = mock(StrokeMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_withStrokeStart_returnsStrokeStartMessage() {
        StrokeMessage msg = interpreter.decode("SS");

        assertNotNull(msg);
        assertEquals(START_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void decode_withStrokeEnd_returnsStrokeEndMessage() {
        StrokeMessage msg = interpreter.decode("SE");

        assertNotNull(msg);
        assertEquals(END_OF_STROKE, msg.getStrokeType());
    }

    @Test
    public void decode_withInvalidMessage_returnsNull() {
        StrokeMessage msg = interpreter.decode("SB");

        assertNull(msg);
    }

    @Test
    public void decode_withTooShortMessage_returnsNull() {
        StrokeMessage msg = interpreter.decode("S");

        assertNull(msg);
    }


    @Test
    public void encode_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> interpreter.encode(new StrokeMessage(START_OF_STROKE)));
    }

}
