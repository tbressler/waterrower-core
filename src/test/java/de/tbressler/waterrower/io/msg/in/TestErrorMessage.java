package de.tbressler.waterrower.io.msg.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class ErrorMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestErrorMessage {

    @Test
    public void toString_returnsObjectInfo() {
        ErrorMessage msg = new ErrorMessage();
        assertTrue(msg.toString().startsWith("ErrorMessage"));
    }

}