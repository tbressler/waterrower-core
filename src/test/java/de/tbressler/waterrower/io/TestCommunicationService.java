package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.transport.SerialDeviceAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * Tests for class CommunicationService.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestCommunicationService {

    /* Class under test. */
    private CommunicationService communicationService;

    // Mocks:
    private Bootstrap bootstrap = mock(Bootstrap.class, "bootstrap");
    private ChannelInitializer channelInitializer = mock(ChannelInitializer.class, "channelInitializer");
    private SerialDeviceAddress address = new SerialDeviceAddress("some-port");
    private ChannelFuture channelFuture = mock(ChannelFuture.class, "channelFuture");
    private Channel channel = mock(Channel.class, "channel");

    private IConnectionListener connectionListener = mock(IConnectionListener.class, "connectionListener");

    private AbstractMessage message = mock(AbstractMessage.class, "message");

    // Capture:
    private ArgumentCaptor<SerialHandler> callback = forClass(SerialHandler.class);


    @BeforeEach
    public void setUp() {
        communicationService = new CommunicationService(bootstrap, channelInitializer);
        communicationService.addConnectionListener(connectionListener);

        verify(channelInitializer, times(1)).setSerialHandler(callback.capture());
    }


    // Constructor:

    @Test
    public void new_withNullBootstrap_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CommunicationService(null, channelInitializer));
    }

    @Test
    public void new_withNullChannelInitializer_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CommunicationService(bootstrap, null));
    }

    @Test
    public void new_withValidBootstrapAndChannelInitializer() {
        communicationService = new CommunicationService(bootstrap, channelInitializer);
        assertNotNull(communicationService);
    }

    // Open / close:

    @Test
    public void open_withNullAddress_throwsException() {
        assertThrows(NullPointerException.class, () -> communicationService.open(null));
    }

    @Test
    public void open_withValidAddress_opensChannelWithoutSuccess_throwsExceptionAndNotifiesListener() {
        assertThrows(IOException.class, () -> mockUnsuccessfulConnect());
    }

    @Test
    public void isConnected_afterUnsuccessfulConnect_returnsFalse() {
        try {
            mockUnsuccessfulConnect();
        } catch (IOException e) {
            // Ignore IOException.
        }

        assertFalse(communicationService.isConnected());
    }

    @Test
    public void open_withValidAddress_opensChannelAndNotifiesListener() throws IOException {
        mockSuccessfulConnect();
        verify(connectionListener, times(1)).onConnected();
    }

    @Test
    public void isConnected_afterSuccessfulConnect_returnsTrue() throws IOException {
        mockSuccessfulConnect();
        assertTrue(communicationService.isConnected());
    }

    @Test
    public void close_whenNotConnected_throwsException() {
        assertThrows(IOException.class, () -> communicationService.close());
    }

    @Test
    public void close_whenConnected_closesChannelAndNotifiesListener() throws IOException {

        mockSuccessfulConnect();

        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.close()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(true);
        when(channel.isOpen()).thenReturn(true);

        communicationService.close();
        callback.getValue().onDisconnected();

        verify(connectionListener, times(1)).onDisconnected();
    }

    @Test
    public void close_whenConnectedAndClosingIsUnsuccessful_throwsException() throws IOException {

        mockSuccessfulConnect();

        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.disconnect()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(false);

        assertThrows(IOException.class, () -> communicationService.close());
    }

    // Send:

    @Test
    public void send_whenConnected_sendsMessage() throws IOException {

        mockSuccessfulConnect();
        when(channel.isOpen()).thenReturn(true);

        communicationService.send(message);

        verify(channel, times(1)).writeAndFlush(message);
    }

    @Test
    public void send_whenNotConnected_doesntSendMessage() {
        assertThrows(IOException.class, () -> communicationService.send(message));
    }

    // Message received:

    @Test
    public void callMessageReceived_whenConnected_notifiesListener() throws IOException {

        mockSuccessfulConnect();

        callback.getValue().onMessageReceived(message);

        verify(connectionListener, times(1)).onMessageReceived(message);
    }

    // Listeners:

    @Test
    public void addConnectionListener_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> communicationService.addConnectionListener(null));
    }

    @Test
    public void removeConnectionListener_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> communicationService.removeConnectionListener(null));
    }

    @Test
    public void removeConnectionListener_withValidListener_notCalledAfterRemove() throws IOException {
        communicationService.removeConnectionListener(connectionListener);
        mockSuccessfulConnect();
        verify(connectionListener, never()).onConnected();
    }


    // Helper methods:

    private void mockUnsuccessfulConnect() throws IOException {
        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(false);
        when(bootstrap.connect(address)).thenReturn(channelFuture);

        communicationService.open(address);
    }

    private void mockSuccessfulConnect() throws IOException {
        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(true);
        when(channelFuture.channel()).thenReturn(channel);
        when(bootstrap.connect(address)).thenReturn(channelFuture);
        communicationService.open(address);
        callback.getValue().onConnected();
    }

}
