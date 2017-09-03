package de.tbressler.waterrower.io;

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

    // Capture:
    private ArgumentCaptor<RxtxSerialHandler> callback = forClass(RxtxSerialHandler.class);


    @Before
    public void setUp() {
        rxTxCommunicationService = new RxtxCommunicationService(bootstrap, channelInitializer);
        rxTxCommunicationService.addRxtxConnectionListener(connectionListener);

        verify(channelInitializer, times(1)).setRxTxSerialHandler(callback.capture());
    }


    /**
     * Checks if a NPE is thrown, when bootstrap is null.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullBootstrap_throwsException() {
        new RxtxCommunicationService(null, channelInitializer);
    }

    /**
     * Checks if a NPE is thrown, when channelInitializer is null.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullChannelInitializer_throwsException() {
        new RxtxCommunicationService(bootstrap, null);
    }

    /**
     * Checks if the RxTxCommunicationService can be created with valid params.
     */
    @Test
    public void new_withValidBootstrapAndChannelInitializer() {
        rxTxCommunicationService = new RxtxCommunicationService(bootstrap, channelInitializer);
        assertNotNull(rxTxCommunicationService);
    }


    /**
     * Checks if getSerialPorts returns a non null list.
     */
    @Test
    public void getSerialPorts_returnsNonNull() {
        assertNotNull(rxTxCommunicationService.getSerialPorts());
    }


    /**
     * Checks if a NPE is thrown, when address is null.
     */
    @Test(expected = NullPointerException.class)
    public void open_withNullAddress_throwsException() throws IOException {
        rxTxCommunicationService.open(null);
    }


    private void mockUnsuccessfulConnect() throws IOException {
        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(false);
        when(bootstrap.connect(address)).thenReturn(channelFuture);

        rxTxCommunicationService.open(address);
    }

    /**
     * Checks if the listener is called and a IOException is thrown, when channel can not be open.
     */
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


    private void mockSuccessfulConnect() throws IOException {
        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(true);
        when(channelFuture.channel()).thenReturn(channel);
        when(bootstrap.connect(address)).thenReturn(channelFuture);
        rxTxCommunicationService.open(address);
        callback.getValue().onConnected();
    }

    /**
     * Checks if listener is called if connection was successful.
     */
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


    /**
     * Checks if listener is called and an exception is thrown when the connection can not be
     * closed because it was not opened before.
     */
    @Test(expected = IOException.class)
    public void close_whenNotConnected_throwsException() throws IOException {
        rxTxCommunicationService.close();
    }

    /**
     * Checks if previously connected channel gets closed and the listener is called.
     */
    @Test
    public void close_whenConnected_closesChannelAndNotifiesListener() throws IOException {

        mockSuccessfulConnect();

        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.close()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(true);

        rxTxCommunicationService.close();
        callback.getValue().onDisconnected();

        verify(connectionListener, times(1)).onDisconnected();
    }

    @Test(expected = IOException.class)
    public void close_whenConnectedAndClosingIsUnsuccessful_throwsException() throws IOException {

        mockSuccessfulConnect();

        when(channelFuture.syncUninterruptibly()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.close()).thenReturn(channelFuture);
        when(channelFuture.isSuccess()).thenReturn(false);

        rxTxCommunicationService.close();
    }


    @Test(expected = NullPointerException.class)
    public void addRxtxConnectionListener_withNull_throwsException() {
        rxTxCommunicationService.addRxtxConnectionListener(null);
    }


    @Test(expected = NullPointerException.class)
    public void removeRxtxConnectionListener_withNull_throwsException() {
        rxTxCommunicationService.removeRxtxConnectionListener(null);
    }

    @Test
    public void removeRxtxConnectionListener_withValidListener_notCalledAfterRemove() throws
            IOException {
        rxTxCommunicationService.removeRxtxConnectionListener(connectionListener);
        mockSuccessfulConnect();
        verify(connectionListener, never()).onConnected();
    }

}
