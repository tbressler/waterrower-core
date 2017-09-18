package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.in.ErrorMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Tests for class ErrorMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestErrorMessageInterpreter {

    /* Class under test. */
    private ErrorMessageInterpreter errorMessageInterpreter;

    // Mocks:
    private ErrorMessage errorMessage = mock(ErrorMessage.class, "errorMessage");


    @Before
    public void setUp() {
        errorMessageInterpreter = new ErrorMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsERROR() {
        assertEquals("ERROR", errorMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsErrorMessageClass() {
        assertEquals(ErrorMessage.class, errorMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessage_returnsErrorMessage() {
        assertNotNull(errorMessageInterpreter.decode("ERROR"));
    }


    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        errorMessageInterpreter.encode(errorMessage);
    }

}
