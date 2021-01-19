package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.Memory;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.in.ErrorMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.model.MemoryLocation;
import org.junit.Test;
import org.mockito.Mockito;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.subscriptions.Priority.HIGH;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for class AbstractMemorySubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestAbstractMemorySubscription {

    // Class under test.
    private AbstractMemorySubscription subscription;

    // Mocks
    private AbstractMemorySubscription internalSubscription = Mockito.mock(AbstractMemorySubscription.class, "internalSubscription");
    private DataMemoryMessage dataMemoryMessage = Mockito.mock(DataMemoryMessage.class, "dataMemoryMessage");


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullPriority_throwsNPE() {
        newAbstractMemorySubscription(null, DOUBLE_MEMORY, M_S_PROJH_AVG);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullMemory_throwsNPE() {
        newAbstractMemorySubscription(HIGH, null, M_S_PROJH_AVG);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullMemoryLocation_throwsNPE() {
        newAbstractMemorySubscription(HIGH, SINGLE_MEMORY, null);
    }


    // getPriority:

    @Test
    public void getPriority_returnsValueFromConstructor() {
        subscription = newAbstractMemorySubscription(HIGH, SINGLE_MEMORY, FEXTENDED);
        assertEquals(HIGH, subscription.getPriority());
    }


    // Poll:

    @Test
    public void poll_withSingleMemoryAndFEXTENDED_returnsValidMessage() {
        subscription = newAbstractMemorySubscription(HIGH, SINGLE_MEMORY, FEXTENDED);

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(SINGLE_MEMORY, msg.getMemory());
        assertEquals(FEXTENDED.getLocation(), msg.getLocation());
    }

    @Test
    public void poll_withDoubleMemoryAndM_S_PROJH_AVG_returnsValidMessage() {
        subscription = newAbstractMemorySubscription(HIGH, DOUBLE_MEMORY, M_S_PROJH_AVG);

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(DOUBLE_MEMORY, msg.getMemory());
        assertEquals(M_S_PROJH_AVG.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withOtherMessageType_doesntNotifyInternalHandler() {
        subscription = newAbstractMemorySubscription(HIGH, SINGLE_MEMORY, DISPLAY_MIN);

        ErrorMessage msg = new ErrorMessage();
        subscription.handle(msg);

        verify(internalSubscription, never()).handle(any(DataMemoryMessage.class));
    }

    @Test
    public void handle_withValidMessage_notifiesInternalHandler() {
        subscription = newAbstractMemorySubscription(HIGH, SINGLE_MEMORY, DISPLAY_MIN);
        when(dataMemoryMessage.getMemory()).thenReturn(SINGLE_MEMORY);
        when(dataMemoryMessage.getLocation()).thenReturn(DISPLAY_MIN.getLocation());

        subscription.handle((AbstractMessage) dataMemoryMessage);

        verify(internalSubscription, times(1)).handle(dataMemoryMessage);
    }

    @Test
    public void handle_withNonSingledMessage_doesntNotifiesInternalHandler() {
        subscription = newAbstractMemorySubscription(HIGH, SINGLE_MEMORY, DISPLAY_MIN);
        when(dataMemoryMessage.getMemory()).thenReturn(DOUBLE_MEMORY);
        when(dataMemoryMessage.getLocation()).thenReturn(DISPLAY_MIN.getLocation());

        subscription.handle((AbstractMessage) dataMemoryMessage);

        verify(internalSubscription, never()).handle(dataMemoryMessage);
    }

    @Test
    public void handle_withOtherLocationMessage_doesntNotifiesInternalHandler() {
        subscription = newAbstractMemorySubscription(HIGH, DOUBLE_MEMORY, DISPLAY_MIN);
        when(dataMemoryMessage.getMemory()).thenReturn(DOUBLE_MEMORY);
        when(dataMemoryMessage.getLocation()).thenReturn(FEXTENDED.getLocation());

        subscription.handle((AbstractMessage) dataMemoryMessage);

        verify(internalSubscription, never()).handle(dataMemoryMessage);
    }


    // Helper methods:

    private AbstractMemorySubscription newAbstractMemorySubscription(Priority priority, Memory memory, MemoryLocation location) {
        return new AbstractMemorySubscription(priority, memory, location) {
            @Override
            protected void handle(DataMemoryMessage msg) {
                internalSubscription.handle(msg);
            }
        };
    }

}