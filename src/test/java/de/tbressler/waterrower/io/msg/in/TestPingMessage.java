package de.tbressler.waterrower.io.msg.in;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for class PingMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPingMessage {

    @Test
    public void toString_returnsObjectInfo() {
        PingMessage msg = new PingMessage();
        assertTrue(msg.toString().startsWith("PingMessage"));
    }

}