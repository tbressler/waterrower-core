package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.ErrorMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class ErrorMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestErrorMessageInterpreter {

    /* Class under test. */
    private ErrorMessageInterpreter interpreter;

    // Mocks:
    private ErrorMessage errorMessage = mock(ErrorMessage.class, "errorMessage");


    @Before
    public void setUp() {
        interpreter = new ErrorMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsERROR() {
        assertEquals("ERROR", interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        ErrorMessage msg = mock(ErrorMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsTrue() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test
    public void decode_withValidMessage_returnsErrorMessage() {
        assertNotNull(interpreter.decode("ERROR"));
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        interpreter.encode(errorMessage);
    }

}
