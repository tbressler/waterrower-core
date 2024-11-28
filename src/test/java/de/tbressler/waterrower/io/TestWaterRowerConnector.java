package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.transport.SerialDeviceAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private SerialDeviceAddress address = mock(SerialDeviceAddress.class, "address");
    private IConnectionListener connectionListener = mock(IConnectionListener.class, "connectionListener");
    private AbstractMessage message1 = mock(AbstractMessage.class, "message-1");
    private AbstractMessage message2 = mock(AbstractMessage.class, "message-2");


    @BeforeEach
    public void setUp() {
        connector = new WaterRowerConnector(communicationService);
        connector.addConnectionListener(connectionListener);
    }

    // Constructor:

    @Test
    public void new_withNullCommunicationService_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new WaterRowerConnector(null));
    }

    // Connect:

    @Test
    public void connect_withNullAddress_throwsNPE() {
        assertThrows(NullPointerException.class, () -> connector.connect(null));
    }

    @Test
    public void connect_whenConnected_throwsIOException() {
        when(communicationService.isConnected()).thenReturn(true);
        assertThrows(IOException.class, () -> connector.connect(address));
    }

    @Test
    public void connect_whenNotConnectedYet_opensConnection() throws IOException {
        when(communicationService.isConnected()).thenReturn(false);

        connector.connect(address);

        verify(communicationService, times(1)).open(address);
    }


    // Send:

    @Test
    public void send_withNullMessage_throwsException() {
        assertThrows(NullPointerException.class, () -> connector.send((AbstractMessage) null));
    }

    @Test
    public void send_whenNotConnected_throwsException() {
        when(communicationService.isConnected()).thenReturn(false);
        assertThrows(IOException.class, () -> connector.send(message1));
    }

    @Test
    public void send_withValidMessage_sendsMessage() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);

        connector.send(message1);

        verify(communicationService, times(1)).send(message1);
    }

    @Test
    public void send2_withNullMessage_throwsException() {
        assertThrows(NullPointerException.class, () -> connector.send((List<AbstractMessage>) null));
    }

    @Test
    public void send2_whenNotConnected_throwsException() {
        when(communicationService.isConnected()).thenReturn(false);
        assertThrows(IOException.class, () -> connector.send(Arrays.asList(message1)));
    }

    @Test
    public void send2_withMultipleMessage_sendsMessages() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);

        connector.send(Arrays.asList(message1, message2));

        verify(communicationService, times(1)).send(message1);
        verify(communicationService, times(1)).send(message2);
    }

    // Disconnect:

    @Test
    public void disconnect_whenNotConnected_throwsIOException() {
        when(communicationService.isConnected()).thenReturn(false);
        assertThrows(IOException.class, () -> connector.disconnect());
    }

    @Test
    public void disconnect_whenConnected_closesConnection() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);

        connector.disconnect();

        verify(communicationService, times(1)).close();
    }

    // Listener:

    @Test
    public void addConnectionListener_withNull() {
        assertThrows(NullPointerException.class, () -> connector.addConnectionListener(null));
    }

    @Test
    public void addConnectionListener_addsListenerToCommunicationService() {
        connector.addConnectionListener(connectionListener);
        verify(communicationService, times(2)).addConnectionListener(connectionListener);
    }

    @Test
    public void removeConnectionListener_withNull() {
        assertThrows(NullPointerException.class, () -> connector.removeConnectionListener(null));
    }

    @Test
    public void removeConnectionListener_removesListenerToCommunicationService() throws IOException {
        connector.removeConnectionListener(connectionListener);
        verify(communicationService, times(1)).removeConnectionListener(connectionListener);
    }

}