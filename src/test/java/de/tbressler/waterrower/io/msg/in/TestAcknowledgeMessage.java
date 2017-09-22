package de.tbressler.waterrower.io.msg.in;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

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