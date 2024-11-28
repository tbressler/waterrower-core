package de.tbressler.waterrower.io.msg.out;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class ExitCommunicationMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestExitCommunicationMessage {

    @Test
    public void toString_returnsObjectInfo() {
        ExitCommunicationMessage msg = new ExitCommunicationMessage();
        assertTrue(msg.toString().startsWith("ExitCommunicationMessage"));
    }

}