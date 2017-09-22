package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for class RxtxConnectionListener.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRxtxConnectionListener {

    // Class under test.
    RxtxConnectionListener listener;


    @Before
    public void setUp() {
        listener = new RxtxConnectionListener();
    }

    @Test
    public void onConnected() throws Exception {
        listener.onConnected();
    }

    @Test
    public void onMessageReceived() throws Exception {
        listener.onMessageReceived(mock(AbstractMessage.class, "message"));
    }

    @Test
    public void onDisconnected() throws Exception {
        listener.onDisconnected();
    }

    @Test
    public void onError() throws Exception {
        listener.onError();
    }

}