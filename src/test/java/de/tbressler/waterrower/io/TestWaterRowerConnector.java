package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.transport.JSerialCommDeviceAddress;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Tests for class WaterRowerConnector.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWaterRowerConnector {

    /* Class under test. */
    private WaterRowerConnector connector;

    // Mocks:
    private CommunicationService communicationService = mock(CommunicationService.class, "communicationService");
    private JSerialCommDeviceAddress address = mock(JSerialCommDeviceAddress.class, "address");
    private IConnectionListener connectionListener = mock(IConnectionListener.class, "connectionListener");
    private AbstractMessage message = mock(AbstractMessage.class, "message");


    @Before
    public void setUp() {
        connector = new WaterRowerConnector(communicationService);
        connector.addConnectionListener(connectionListener);
    }

    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullCommunicationService_throwsNPE() {
        new WaterRowerConnector(null);
    }

    // Connect:

    @Test(expected = NullPointerException.class)
    public void connect_withNullAddress_throwsNPE() throws Exception {
        connector.connect(null);
    }

    @Test(expected = IOException.class)
    public void connect_whenConnected_throwsIOException() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);
        connector.connect(address);
    }

    @Test
    public void connect_whenNotConnectedYet_opensConnection() throws IOException {
        when(communicationService.isConnected()).thenReturn(false);

        connector.connect(address);

        verify(communicationService, times(1)).open(address);
    }


    // Send:

    @Test(expected = NullPointerException.class)
    public void send_withNullMessage_throwsException() throws Exception {
        connector.send(null);
    }

    @Test(expected = IOException.class)
    public void send_whenNotConnected_throwsException() throws Exception {
        when(communicationService.isConnected()).thenReturn(false);
        connector.send(message);
    }

    @Test
    public void send_withValidMessage_sendsMessage() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);

        connector.send(message);

        verify(communicationService, times(1)).send(message);
    }

    // Disconnect:

    @Test(expected = IOException.class)
    public void disconnect_whenNotConnected_throwsIOException() throws IOException {
        when(communicationService.isConnected()).thenReturn(false);
        connector.disconnect();
    }

    @Test
    public void disconnect_whenConnected_closesConnection() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);

        connector.disconnect();

        verify(communicationService, times(1)).close();
    }

    // Listener:

    @Test(expected = NullPointerException.class)
    public void addConnectionListener_withNull() throws IOException {
        connector.addConnectionListener(null);
    }

    @Test
    public void addConnectionListener_addsListenerToCommunicationService() throws IOException {
        connector.addConnectionListener(connectionListener);
        verify(communicationService, times(2)).addConnectionListener(connectionListener);
    }

    @Test(expected = NullPointerException.class)
    public void removeConnectionListener_withNull() throws IOException {
        connector.removeConnectionListener(null);
    }

    @Test
    public void removeConnectionListener_removesListenerToCommunicationService() throws IOException {
        connector.removeConnectionListener(connectionListener);
        verify(communicationService, times(1)).removeConnectionListener(connectionListener);
    }

}