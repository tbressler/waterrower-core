package de.tbressler.waterrower.io.msg.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class HardwareTypeMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestHardwareTypeMessage {

    @Test
    public void isWaterRower_whenConstructedWithTrue() throws Exception {
        HardwareTypeMessage msg = new HardwareTypeMessage(true);
        assertTrue(msg.isWaterRower());
    }

    @Test
    public void isWaterRower_whenConstructedWithFalse() throws Exception {
        HardwareTypeMessage msg = new HardwareTypeMessage(false);
        assertFalse(msg.isWaterRower());
    }

    @Test
    public void toString_returnsObjectInfo() {
        HardwareTypeMessage msg = new HardwareTypeMessage(false);
        assertTrue(msg.toString().startsWith("HardwareTypeMessage"));
    }

}