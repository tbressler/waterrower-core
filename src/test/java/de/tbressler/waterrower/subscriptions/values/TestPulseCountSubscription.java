package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.PulseCountMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.subscriptions.Priority.NO_POLLING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Tests for class PulseCountSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestPulseCountSubscription {

    // Class under test.
    private PulseCountSubscription subscription;

    // Mocks:
    private PulseCountSubscription internalSubscription = mock(PulseCountSubscription.class, "internalSubscription");
    private PulseCountMessage pulseCountMessage = mock(PulseCountMessage.class, "pulseCountMessage");

    @BeforeEach
    public void setUp() {
        subscription = new PulseCountSubscription() {
            @Override
            protected void onPulseCount(int pulsesCount) {
                internalSubscription.onPulseCount(pulsesCount);
            }
        };
    }


    // Priority:

    @Test
    public void getPriority_returnsNO_POLLING() {
        assertEquals(NO_POLLING, subscription.getPriority());
    }


    // Poll:

    @Test
    public void poll_returnsNull() {
        assertNull(subscription.poll());
    }

    // Handle:

    @Test
    public void handle_withPulseCount10Message_callsOnStroke() {
        when(pulseCountMessage.getPulsesCounted()).thenReturn(10);

        subscription.handle(pulseCountMessage);

        verify(internalSubscription, times(1)).onPulseCount(10);
    }

    @Test
    public void handle_withPulseCount30Message_callsOnStroke() {
        when(pulseCountMessage.getPulsesCounted()).thenReturn(30);

        subscription.handle(pulseCountMessage);

        verify(internalSubscription, times(1)).onPulseCount(30);
    }

    @Test
    public void handle_withUnsupportedMessage_doesntCallOnPulseCount() {
        AbstractMessage someMessage = mock(AbstractMessage.class, "someMessage");

        subscription.handle(someMessage);

        verify(internalSubscription, never()).onPulseCount(any(Integer.class));
    }

}