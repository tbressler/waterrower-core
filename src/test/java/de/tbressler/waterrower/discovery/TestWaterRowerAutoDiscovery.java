package de.tbressler.waterrower.discovery;

import com.fazecast.jSerialComm.SerialPort;
import de.tbressler.waterrower.IWaterRowerConnectionListener;
import de.tbressler.waterrower.WaterRower;
import de.tbressler.waterrower.io.transport.JSerialCommDeviceAddress;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.MonitorType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import static de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery.TRY_AGAIN_INTERVAL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for class WaterRowerAutoDiscovery.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public class TestWaterRowerAutoDiscovery {

    // Class under test.
    private WaterRowerAutoDiscovery discovery;

    // Mocks:
    private WaterRower waterRower = mock(WaterRower.class, "waterRower");
    private IDiscoveryStore discoveryStore = mock(IDiscoveryStore.class, "discoveryStore");
    private ScheduledExecutorService executor = mock(ScheduledExecutorService.class, "executor");
    private SerialPortWrapper serialPortWrapper = mock(SerialPortWrapper.class, "serialPortWrapper");
    private SerialPort serialPort1 = SerialPort.getCommPort("/serial/port1");
    private SerialPort serialPort2 = SerialPort.getCommPort("/serial/port2");

    // Capture:
    private ArgumentCaptor<IWaterRowerConnectionListener> connectionListener = forClass(IWaterRowerConnectionListener.class);
    private ArgumentCaptor<Runnable> task = forClass(Runnable.class);


    @Before
    public void setUp() throws Exception {
        discovery = new WaterRowerAutoDiscovery(waterRower, discoveryStore, executor, serialPortWrapper);

        verify(waterRower, times(1)).addConnectionListener(connectionListener.capture());
    }


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullWaterRower_throwsNPE() {
        new WaterRowerAutoDiscovery(null, executor);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullExecutor_throwsNPE() {
        new WaterRowerAutoDiscovery(waterRower, null);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullDiscoveryStore_throwsNPE() {
        new WaterRowerAutoDiscovery(waterRower, null, executor);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullSerialPortWrapper_throwsNPE() {
        new WaterRowerAutoDiscovery(waterRower, discoveryStore, executor, null);
    }


    // isActive:

    @Test
    public void isActive_whenNotStartedYet_returnsFalse() {
        assertFalse(discovery.isActive());
    }

    @Test
    public void isActive_whenStarted_returnsTrue() {
        discovery.start();

        assertTrue(discovery.isActive());

        verify(executor, times(1)).submit(task.capture());
    }

    @Test
    public void isActive_whenStopped_returnsFalse() {
        discovery.start();
        discovery.stop();

        assertFalse(discovery.isActive());

        verify(executor, times(1)).submit(task.capture());
    }


    // Start:

    @Test
    public void start_whenStopped_dontExecuteConnectionAttempt() throws IOException {
        discovery.start();
        discovery.stop();
        verify(executor, times(1)).submit(task.capture());

        task.getValue().run();

        verify(waterRower, never()).connect(any());
    }

    @Test
    public void start_withAvailableSerialPort1_connectToSerialPort1() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getCommPorts()).thenReturn(new SerialPort[] { serialPort1 });

        task.getValue().run();

        connectionListener.getValue().onConnected(new ModelInformation(MonitorType.WATER_ROWER_S4, "2.1"));

        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port1")));
        verify(discoveryStore, times(1)).setLastSuccessfulSerialPort("/serial/port1");
    }


    @Test
    public void start_withAvailableSerialPort1And2_connectToLastSuccessfulSerialPortFirst() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getCommPorts()).thenReturn(new SerialPort[] { serialPort1, serialPort2 });
        when(discoveryStore.getLastSuccessfulSerialPort()).thenReturn("/serial/port2");

        task.getValue().run();


        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port2")));
    }

    @Test
    public void start_withInvalidSerialPorts_doNotConnect() throws IOException {
        SerialPort serialPortDev = SerialPort.getCommPort("/dev/cu.1");
        SerialPort serialPortBT1 = SerialPort.getCommPort("Bluetooth-1");
        SerialPort serialPortBT2 = SerialPort.getCommPort("BT-1");

        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getCommPorts()).thenReturn(new SerialPort[] { serialPortDev, serialPortBT1, serialPortBT2 });

        task.getValue().run();

        verify(waterRower, never()).connect(any());
    }

    @Test
    public void start_withNoSerialPort_scheduleAgain() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getCommPorts()).thenReturn(new SerialPort[] {});

        task.getValue().run();

        verify(executor, times(1)).schedule(task.capture(), eq(TRY_AGAIN_INTERVAL.getSeconds()), eq(SECONDS));
        verify(waterRower, never()).connect(any());
    }

    @Test
    public void start_whenDisconnected_scheduleNewConnectionAttempts() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getCommPorts()).thenReturn(new SerialPort[] { serialPort1 });

        task.getValue().run();

        connectionListener.getValue().onConnected(new ModelInformation(MonitorType.WATER_ROWER_S4, "2.1"));
        connectionListener.getValue().onDisconnected();

        verify(executor, times(2)).submit(task.capture());
        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port1")));
    }

    @Test
    public void start_onIOException_scheduleNewConnectionAttempts() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getCommPorts()).thenReturn(new SerialPort[] { serialPort1 });

        doThrow(new IOException("exception")).when(waterRower).connect(any());

        task.getValue().run();

        connectionListener.getValue().onConnected(new ModelInformation(MonitorType.WATER_ROWER_S4, "2.1"));
        connectionListener.getValue().onDisconnected();

        verify(executor, times(2)).submit(task.capture());
        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port1")));
    }




    // Helper methods:

    private ArgumentMatcher<JSerialCommDeviceAddress> eqAddress(String port) {
        return new ArgumentMatcher<>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof JSerialCommDeviceAddress))
                    return false;
                JSerialCommDeviceAddress address = (JSerialCommDeviceAddress) argument;
                return address.value().equals(port);
            }
        };
    }

}
