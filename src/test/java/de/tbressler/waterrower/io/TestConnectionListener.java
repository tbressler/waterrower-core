package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for class ConnectionListener.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestConnectionListener {

    // Class under test.
    ConnectionListener listener;


    @BeforeEach
    public void setUp() {
        listener = new ConnectionListener();
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