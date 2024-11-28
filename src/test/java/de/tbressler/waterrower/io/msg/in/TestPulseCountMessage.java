package de.tbressler.waterrower.io.msg.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class PulseCountMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPulseCountMessage {

    @Test
    public void getPulsesCounted() throws Exception {
        PulseCountMessage message = new PulseCountMessage(123);
        assertEquals(123, message.getPulsesCounted());
    }

    @Test
    public void toString_returnsObjectInfo() {
        PulseCountMessage msg = new PulseCountMessage(100);
        assertTrue(msg.toString().startsWith("PulseCountMessage"));
    }

}