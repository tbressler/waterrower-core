package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.CLOCK_DOWN_DEC;
import static de.tbressler.waterrower.model.MemoryLocation.M_S_LOW_AVERAGE;
import static de.tbressler.waterrower.subscriptions.Priority.HIGH;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for class DebugSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestDebugSubscription {

    // Constructor:


    @Test
    public void new_withNullPriority_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new DebugSubscription(null, SINGLE_MEMORY, CLOCK_DOWN_DEC) {
            @Override
            protected void handle(DataMemoryMessage msg) {}
        });
    }

    @Test
    public void new_withNullMemory_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new DebugSubscription(HIGH, null, CLOCK_DOWN_DEC) {
            @Override
            protected void handle(DataMemoryMessage msg) {}
        });
    }

    @Test
    public void new_withNullMemoryLocation_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new DebugSubscription(HIGH, SINGLE_MEMORY, null) {
            @Override
            protected void handle(DataMemoryMessage msg) {}
        });
    }

    // Poll:

    @Test
    public void poll_returnsMessageForConstructorValues() {
        DebugSubscription subscription = new DebugSubscription(HIGH, DOUBLE_MEMORY, M_S_LOW_AVERAGE) {
            @Override
            protected void handle(DataMemoryMessage msg) {}
        };

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();

        assertNotNull(msg);
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(M_S_LOW_AVERAGE.getLocation(), msg.getLocation());
    }

}