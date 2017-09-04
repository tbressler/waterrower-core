package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.in.ErrorMessage;
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


    /**
     * Checks if getMessageTypeChar always returns 'E'.
     */
    @Test
    public void getMessageTypeChar_returnsO() {
        assertEquals("E", errorMessageInterpreter.getMessageTypeChar());
    }


    /**
     * Checks if getMessageType always returns ErrorMessage.class.
     */
    @Test
    public void getMessageType_returnsErrorMessageClass() {
        assertEquals(ErrorMessage.class, errorMessageInterpreter.getMessageType());
    }


    @Test
    public void decode_withValidMessage_returnsErrorMessage() {
        byte[] bytes = new String("ERROR").getBytes();
        assertNotNull(errorMessageInterpreter.decode(bytes));
    }


    /**
     * Checks if an IllegalStateException is thrown, when encode is called.
     */
    @Test(expected = IllegalStateException.class)
    public void encode_throwsIllegalStateException() {
        errorMessageInterpreter.encode(errorMessage);
    }

}
