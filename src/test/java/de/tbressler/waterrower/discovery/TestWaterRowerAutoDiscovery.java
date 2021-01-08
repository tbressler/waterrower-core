package de.tbressler.waterrower.discovery;

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

import static com.google.common.collect.Lists.newArrayList;
import static de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery.TRY_AGAIN_INTERVAL;
import static java.util.Collections.emptyList;
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
    private AvailablePort serialPort1 = mockAvailablePort("/serial/port1", false);
    private AvailablePort serialPort2 = mockAvailablePort("/serial/port2", false);

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
        when(serialPortWrapper.getAvailablePorts()).thenReturn(newArrayList(serialPort1));

        task.getValue().run();

        connectionListener.getValue().onConnected(new ModelInformation(MonitorType.WATER_ROWER_S4, "2.1"));

        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port1")));
        verify(discoveryStore, times(1)).setLastSuccessfulSerialPort("/serial/port1");
    }


    @Test
    public void start_withAvailableSerialPort1And2_connectToLastSuccessfulSerialPortFirst() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getAvailablePorts()).thenReturn(newArrayList(serialPort1, serialPort2));
        when(discoveryStore.getLastSuccessfulSerialPort()).thenReturn("/serial/port2");

        task.getValue().run();


        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port2")));
    }

    @Test
    public void start_withAvailableSerialPorts_connectOnlyClosed() throws IOException {
        AvailablePort serialPortOpened = mockAvailablePort("/serial/portOpened", true);
        AvailablePort serialPortClosed = mockAvailablePort("/serial/portClosed", false);

        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getAvailablePorts()).thenReturn(newArrayList(serialPortOpened, serialPortClosed));

        task.getValue().run();

        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/portClosed")));
    }


    @Test
    public void start_withInvalidSerialPorts_doNotConnect() throws IOException {
        AvailablePort serialPortDev = mockAvailablePort("/dev/cu.1", false);
        AvailablePort serialPortBT1 = mockAvailablePort("Bluetooth-1", false);
        AvailablePort serialPortBT2 = mockAvailablePort("BT-1", false);

        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getAvailablePorts()).thenReturn(newArrayList(serialPortDev, serialPortBT1, serialPortBT2));

        task.getValue().run();

        verify(waterRower, never()).connect(any());
    }

    @Test
    public void start_withNoSerialPort_scheduleAgain() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getAvailablePorts()).thenReturn(emptyList());

        task.getValue().run();

        verify(executor, times(1)).schedule(task.capture(), eq(TRY_AGAIN_INTERVAL.getSeconds()), eq(SECONDS));
        verify(waterRower, never()).connect(any());
    }

    @Test
    public void start_whenDisconnected_scheduleNewConnectionAttempts() throws IOException {
        discovery.start();
        verify(executor, times(1)).submit(task.capture());
        when(serialPortWrapper.getAvailablePorts()).thenReturn(newArrayList(serialPort1));

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
        when(serialPortWrapper.getAvailablePorts()).thenReturn(newArrayList(serialPort1));

        doThrow(new IOException("exception")).when(waterRower).connect(any());

        task.getValue().run();

        connectionListener.getValue().onConnected(new ModelInformation(MonitorType.WATER_ROWER_S4, "2.1"));
        connectionListener.getValue().onDisconnected();

        verify(executor, times(2)).submit(task.capture());
        verify(waterRower, times(1)).connect(argThat(eqAddress("/serial/port1")));
    }




    // Helper methods:

    private AvailablePort mockAvailablePort(String port, boolean isOpen) {
        AvailablePort availablePort = mock(AvailablePort.class, "port-"+port);
        when(availablePort.getSystemPortName()).thenReturn(port);
        when(availablePort.isOpen()).thenReturn(isOpen);
        return availablePort;
    }

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
