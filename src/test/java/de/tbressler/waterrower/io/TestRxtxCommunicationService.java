package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * Tests for class RxTxCommunicationService.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRxtxCommunicationService {

    /* Class under test. */
    private RxtxCommunicationService rxTxCommunicationService;

    // Mocks:
    private Bootstrap bootstrap = mock(Bootstrap.class, "bootstrap");
    private RxtxChannelInitializer channelInitializer = mock(RxtxChannelInitializer.class, "channelInitializer");
    private RxtxDeviceAddress address = new RxtxDeviceAddress("some-port");
    private ChannelFuture channelFuture = mock(ChannelFuture.class, "channelFuture");
    private Channel channel = mock(Channel.class, "channel");

    private IRxtxConnectionListener connectionListener = mock(IRxtxConnectionListener.class, "connectionListener");

    private AbstractMessage message = mock(AbstractMessage.class, "message");

    // Capture:
    private ArgumentCaptor<RxtxSerialHandler> callback = forClass(RxtxSerialHandler.class);


    @Before
    public void setUp() {
        rxTxCommunicationService = new RxtxCommunicationService(bootstrap, channelInitializer);
        rxTxCommunicationService.addRxtxConnectionListener(connectionListener);

        verify(channelInitializer, times(1)).setRxTxSerialHandler(callback.capture());
    }


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullBootstrap_throwsException() {
        new RxtxCommunicationService(null, channelInitializer);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullChannelInitializer_throwsException() {
        new RxtxCommunicationService(bootstrap, null);
    }

    @Test
    public void new_withValidBootstrapAndChannelInitializer() {
        rxTxCommunicationService = new RxtxCommunicationService(bootstrap, channelInitializer);
        assertNotNull(rxTxCommunicationService);
    }


    // Get serial ports:

    @Test
    public void getSerialPorts_returnsNonNull() {
        assertNotNull(rxTxCommunicationService.getSerialPorts());
    }


    // Open / close:

    @Test(expected = NullPointerException.class)
    public void open_withNullAddress_throwsException() throws IOException {
        rxTxCommunicationService.open(null);
    }

    @Test(expected = IOException.class)
    public void open_withValidAddress_opensChannelWithoutSuccess_throwsExceptionAndNotifiesListener() throws IOException {
        mockUnsuccessfulConnect();
    }

    @Test
    public void isConnected_afterUnsuccessfulConnect_returnsFalse() throws IOException {
        try {
            mockUnsuccessfulConnect();
        } catch (IOException e) {
            // Ignore IOException.
        }

        assertFalse(rxTxCommunicationService.isConnected());
    }

    @Test
    public void open_withValidAddress_opensChannelAndNotifiesListener() throws IOException {
        mockSuccessfulConnect();
        verify(connectionListener, times(1)).onConnected();
    }

    @Test
    public void isConnected_afterSuccessfulConnect_returnsTrue() throws IOException {
        mockSuccessfulConnect();
        assertTrue(rxTxCommunicationService.isConnected());
    }

    @Test(expected = IOException.class)
    public void close_whenNotConnected_throwsException() throws IOException {
        rxTxCommunicationService.close();
    }

    @Test
    public void close_whenConnected_closesChannelAndNotifiesListener() throws IOException {

        mockSuccessfulConnect();

        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.close()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(true);
        when(channel.isOpen()).thenReturn(true);

        rxTxCommunicationService.close();
        callback.getValue().onDisconnected();

        verify(connectionListener, times(1)).onDisconnected();
    }

    @Test(expected = IOException.class)
    public void close_whenConnectedAndClosingIsUnsuccessful_throwsException() throws IOException {

        mockSuccessfulConnect();

        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.disconnect()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(false);

        rxTxCommunicationService.close();
    }

    // Send:

    @Test
    public void send_whenConnected_sendsMessage() throws IOException {

        mockSuccessfulConnect();
        when(channel.isOpen()).thenReturn(true);

        rxTxCommunicationService.send(message);

        verify(channel, times(1)).write(message);
    }

    @Test(expected = IOException.class)
    public void send_whenNotConnected_doesntSendMessage() throws IOException {
        rxTxCommunicationService.send(message);
    }

    // Message received:

    @Test
    public void callMessageReceived_whenConnected_notifiesListener() throws IOException {

        mockSuccessfulConnect();

        callback.getValue().onMessageReceived(message);

        verify(connectionListener, times(1)).onMessageReceived(message);
    }

    // Listeners:

    @Test(expected = NullPointerException.class)
    public void addRxtxConnectionListener_withNull_throwsException() {
        rxTxCommunicationService.addRxtxConnectionListener(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeRxtxConnectionListener_withNull_throwsException() {
        rxTxCommunicationService.removeRxtxConnectionListener(null);
    }

    @Test
    public void removeRxtxConnectionListener_withValidListener_notCalledAfterRemove() throws IOException {
        rxTxCommunicationService.removeRxtxConnectionListener(connectionListener);
        mockSuccessfulConnect();
        verify(connectionListener, never()).onConnected();
    }


    // Helper methods:

    private void mockUnsuccessfulConnect() throws IOException {
        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(false);
        when(bootstrap.connect(address)).thenReturn(channelFuture);

        rxTxCommunicationService.open(address);
    }

    private void mockSuccessfulConnect() throws IOException {
        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(true);
        when(channelFuture.channel()).thenReturn(channel);
        when(bootstrap.connect(address)).thenReturn(channelFuture);
        rxTxCommunicationService.open(address);
        callback.getValue().onConnected();
    }

}
