package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.StrokeMessage;
import de.tbressler.waterrower.model.StrokeType;
import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.model.StrokeType.END_OF_STROKE;
import static de.tbressler.waterrower.model.StrokeType.START_OF_STROKE;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Tests for class StrokeSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestStrokeSubscription {

    // Class under test.
    private StrokeSubscription subscription;

    // Mocks:
    private StrokeSubscription internalSubscription = mock(StrokeSubscription.class, "internalSubscription");
    private StrokeMessage strokeMessage = mock(StrokeMessage.class, "strokeMessage");

    @Before
    public void setUp() {
        subscription = new StrokeSubscription() {
            @Override
            protected void onStroke(StrokeType strokeType) {
                internalSubscription.onStroke(strokeType);
            }
        };
    }


    // Poll:

    @Test
    public void poll_returnsNull() {
        assertNull(subscription.poll());
    }

    // Handle:

    @Test
    public void handle_withStartStrokeMessage_callsOnStroke() {
        when(strokeMessage.getStrokeType()).thenReturn(START_OF_STROKE);

        subscription.handle(strokeMessage);

        verify(internalSubscription, times(1)).onStroke(START_OF_STROKE);
    }

    @Test
    public void handle_withEndStrokeMessage_callsOnStroke() {
        when(strokeMessage.getStrokeType()).thenReturn(END_OF_STROKE);

        subscription.handle(strokeMessage);

        verify(internalSubscription, times(1)).onStroke(END_OF_STROKE);
    }

    @Test
    public void handle_withUnsupportedMessage_doesntCallStroke() {
        AbstractMessage someMessage = mock(AbstractMessage.class, "someMessage");

        subscription.handle(someMessage);

        verify(internalSubscription, never()).onStroke(any(StrokeType.class));
    }

}