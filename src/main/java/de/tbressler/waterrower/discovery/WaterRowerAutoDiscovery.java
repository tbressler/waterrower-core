package de.tbressler.waterrower.discovery;

import de.tbressler.waterrower.IWaterRowerConnectionListener;
import de.tbressler.waterrower.WaterRower;
import de.tbressler.waterrower.io.transport.SerialDeviceAddress;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.utils.AvailablePort;
import de.tbressler.waterrower.utils.SerialPortWrapper;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Handles the auto-discovery of the WaterRower.
 *
 * This class automatically searches for the available serial ports and connects to
 * the WaterRower Performance Monitor. When the connection is lost, the class automatically
 * connects the WaterRower again as soon as the device is available again.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRowerAutoDiscovery {

    /* Try again interval, if no ports are available currently. */
    static final Duration TRY_AGAIN_INTERVAL = ofSeconds(5);


    /* The WaterRower. */
    private final WaterRower waterRower;

    /* The discovery store. */
    private final IDiscoveryStore discoveryStore;

    /* The executor service. */
    private final ScheduledExecutorService executorService;

    /* Wrapper for the serial port implementation. */
    private final SerialPortWrapper serialPortWrapper;


    /* The current stack of available ports. */
    private Stack<SerialDeviceAddress> availablePorts = new Stack<>();

    /* True if active. */
    private AtomicBoolean isActive = new AtomicBoolean(false);

    /* Lock, so that only one connection attempt can be done at the same time. */
    private ReentrantLock lock = new ReentrantLock(true);

    /* The current serial port. */
    private String currentSerialPort = null;


    /* Listener for WaterRower connections. */
    private IWaterRowerConnectionListener connectionListener = new IWaterRowerConnectionListener() {

        @Override
        public void onConnected(ModelInformation modelInformation) {
            // Remember the last successful serial port, in order
            // to speed up connection next time.
            discoveryStore.setLastSuccessfulSerialPort(currentSerialPort);
        }

        @Override
        public void onDisconnected() {
            Log.debug("WaterRower disconnected. Try to auto-connect again.");
            executorService.submit(() -> tryNextConnectionAttempt());
        }

        @Override
        public void onError(ErrorCode errorCode) {}

    };


    /**
     * Handles the auto-discovery of the WaterRower.
     *
     * @param waterRower The WaterRower, must not be null.
     * @param discoveryStore The store for successful serial ports.
     * @param executorService The executor service, must not be null.
     */
    public WaterRowerAutoDiscovery(WaterRower waterRower, IDiscoveryStore discoveryStore, ScheduledExecutorService executorService) {
        this(waterRower, discoveryStore, executorService, new SerialPortWrapper());
    }

    /**
     * Handles the auto-discovery of the WaterRower.
     *
     * @param waterRower The WaterRower, must not be null.
     * @param executorService The executor service, must not be null.
     */
    public WaterRowerAutoDiscovery(WaterRower waterRower, ScheduledExecutorService executorService) {
        this(waterRower, new IDiscoveryStore() {

            @Override
            public void setLastSuccessfulSerialPort(String serialPort) {}

            @Override
            public String getLastSuccessfulSerialPort() {
                return null;
            }

        }, executorService);
    }

    /**
     * Handles the auto-discovery of the WaterRower.
     *
     * @param waterRower The WaterRower, must not be null.
     * @param discoveryStore The store for successful serial ports.
     * @param executorService The executor service, must not be null.
     * @param serialPortWrapper The serial port wrapper, must not be null.
     */
    WaterRowerAutoDiscovery(WaterRower waterRower, IDiscoveryStore discoveryStore, ScheduledExecutorService executorService, SerialPortWrapper serialPortWrapper) {
        this.waterRower = requireNonNull(waterRower);
        this.waterRower.addConnectionListener(connectionListener);
        this.discoveryStore = requireNonNull(discoveryStore);
        this.executorService = requireNonNull(executorService);
        this.serialPortWrapper = requireNonNull(serialPortWrapper);
    }


    /**
     * Starts the auto-discovery.
     */
    public void start() {
        Log.debug("Starting discovery.");
        isActive.set(true);
        executorService.submit(() -> tryNextConnectionAttempt());
    }

    /* Try the next connection attempt. */
    private void tryNextConnectionAttempt() {
        lock.lock();

        try {

            if (!isActive.get())
                return;

            // If no ports are available anymore, update list of ports.
            if (availablePorts.empty())
                updateAvailablePorts();

            if (availablePorts.empty()) {
                // Still no serial ports available!
                Log.warn("Currently no serial ports available! Trying again in "+TRY_AGAIN_INTERVAL.getSeconds()+" second(s)...");
                executorService.schedule(this::tryNextConnectionAttempt, TRY_AGAIN_INTERVAL.getSeconds(), SECONDS);
                return;
            }

            SerialDeviceAddress address = availablePorts.pop();
            currentSerialPort = address.value();

            Log.debug("Auto-connecting serial port '"+address.value()+"'.");

            waterRower.connect(address);

        } catch (IOException e) {
            Log.warn("Couldn't connect to serial port, due to error ("+e.getMessage()+")! Trying next port.");
            executorService.schedule(this::tryNextConnectionAttempt, TRY_AGAIN_INTERVAL.getSeconds(), SECONDS);
        } finally {
            lock.unlock();
        }
    }

    /* Updates the available serial ports on the stack. */
    private void updateAvailablePorts() {

        Log.debug("Updating list of available serial ports.");

        // Get all available serial ports:
        List<AvailablePort> availablePorts = serialPortWrapper.getAvailablePorts();

        for(AvailablePort port : availablePorts) {

            String portName = port.getSystemPortName();
            // Ignore /dev/cu ports.
            if (portName.startsWith("/dev/cu."))
                continue;

            // Ignore bluetooth ports.
            if (portName.contains("Bluetooth") || portName.contains("BT"))
                continue;

            if (port.isOpen()) {
                Log.warn("Skipping serial port '"+portName+"', because it is currently owned by another thread or application.");
                continue;
            }

            this.availablePorts.push(new SerialDeviceAddress(portName));

            Log.debug("Serial port found: " + portName);
        }

        putLastSuccessfulPortFirst();
    }

    /* Add the last successful serial port, in order to speed up connection. */
    private void putLastSuccessfulPortFirst() {

        String lastSuccessfulPort = discoveryStore.getLastSuccessfulSerialPort();

        if (lastSuccessfulPort == null)
            return;

        SerialDeviceAddress lastAddress = new SerialDeviceAddress(lastSuccessfulPort);

        boolean wasRemoved = availablePorts.removeIf(address -> address.value().equals(lastSuccessfulPort));
        if (!wasRemoved)
            return;

        availablePorts.push(lastAddress);
    }


    /**
     * Returns true if auto-discovery is active.
     *
     * @return True if auto-discovery is active.
     */
    public boolean isActive() {
        return isActive.get();
    }


    /**
     * Stops the auto-discovery.
     */
    public void stop() {
        Log.debug("Stopping discovery.");
        isActive.set(false);
    }

}
