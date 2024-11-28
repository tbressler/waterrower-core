package de.tbressler.waterrower.io.msg.out;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class ResetMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestResetMessage {

    @Test
    public void toString_returnsObjectInfo() {
        ResetMessage msg = new ResetMessage();
        assertTrue(msg.toString().startsWith("ResetMessage"));
    }

}