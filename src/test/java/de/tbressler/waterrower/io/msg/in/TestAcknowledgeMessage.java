package de.tbressler.waterrower.io.msg.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class AcknowledgeMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAcknowledgeMessage {

    @Test
    public void toString_returnsObjectInfo() {
        AcknowledgeMessage msg = new AcknowledgeMessage();
        assertTrue(msg.toString().startsWith("AcknowledgeMessage"));
    }

}