package de.tbressler.waterrower.io.msg.out;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for class StartCommunicationMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStartCommunicationMessage {

    @Test
    public void toString_returnsObjectInfo() {
        StartCommunicationMessage msg = new StartCommunicationMessage();
        assertTrue(msg.toString().startsWith("StartCommunicationMessage"));
    }

}