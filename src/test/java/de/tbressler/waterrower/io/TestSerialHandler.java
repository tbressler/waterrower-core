package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Tests for class SerialHandler.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestSerialHandler {

    // Class under test.
    private SerialHandler handler;

    // Mocks:
    private SerialHandler internalHandler = mock(SerialHandler.class, "internalHandler");
    private ChannelHandlerContext ctx = mock(ChannelHandlerContext.class, "ctx");
    private AbstractMessage message = mock(AbstractMessage.class, "message");


    @Before
    public void setUp() {
        handler = new SerialHandler() {
            @Override
            protected void onMessageReceived(AbstractMessage message) {
                internalHandler.onMessageReceived(message);
            }

            @Override
            protected void onConnected() {
                internalHandler.onConnected();
            }

            @Override
            protected void onDisconnected() {
                internalHandler.onDisconnected();
            }

            @Override
            protected void onError() {
                internalHandler.onError();
            }
        };
    }


    // Channel read:

    @Test
    public void channelRead_withValidMessage_notifiesListener() throws Exception {
        handler.channelRead(ctx, message);
        verify(internalHandler, times(1)).onMessageReceived(message);
    }

    @Test
    public void channelRead_withInvalidMessage_doesntNotifyListener() throws Exception {
        handler.channelRead(ctx, "invalid-message");
        verify(internalHandler, never()).onMessageReceived(any());
    }

    // Channel active / inactive:

    @Test
    public void channelActive_notifiesListener() throws Exception {
        handler.channelActive(ctx);
        verify(internalHandler, times(1)).onConnected();
    }

    @Test
    public void channelInactive_notifiesListener() throws Exception {
        handler.channelInactive(ctx);
        verify(internalHandler, times(1)).onDisconnected();
    }

    // Exceptions:

    @Test
    public void exceptionCaught_closesChannel() throws Exception {
        handler.exceptionCaught(ctx, new Throwable("mocked-exception"));
        verify(ctx, times(1)).close();
    }

}