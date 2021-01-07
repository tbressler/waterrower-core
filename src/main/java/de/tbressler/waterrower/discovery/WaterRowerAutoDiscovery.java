package de.tbressler.waterrower.discovery;

import com.fazecast.jSerialComm.SerialPort;
import de.tbressler.waterrower.IWaterRowerConnectionListener;
import de.tbressler.waterrower.WaterRower;
import de.tbressler.waterrower.io.transport.JSerialCommDeviceAddress;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;

import java.io.IOException;
import java.time.Duration;
import java.util.Stack;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static de.tbressler.waterrower.log.Log.DISCOVERY;
import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Handles the auto-discovery of the WaterRower.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRowerAutoDiscovery {

    /* Try again interval, if no ports are available currently. */
    private static final Duration TRY_AGAIN_INTERVAL = ofSeconds(5);


    /* The WaterRower. */
    private final WaterRower waterRower;

    /* The discovery store. */
    private final IDiscoveryStore discoveryStore;

    /* The executor service. */
    private final ScheduledExecutorService executorService;

    /* The current stack of available ports. */
    private Stack<JSerialCommDeviceAddress> availablePorts = new Stack<>();

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
            Log.debug(DISCOVERY, "WaterRower disconnected. Try to auto-connect again.");
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
        this.waterRower = requireNonNull(waterRower);
        this.waterRower.addConnectionListener(connectionListener);
        this.discoveryStore = requireNonNull(discoveryStore);
        this.executorService = requireNonNull(executorService);
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
     * Starts the auto-discovery.
     */
    public void start() {
        Log.debug(DISCOVERY, "Starting discovery.");
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
                Log.warn(DISCOVERY, "Currently no serial ports available! Trying again in "+TRY_AGAIN_INTERVAL.getSeconds()+" second(s)...");
                executorService.schedule(this::tryNextConnectionAttempt, TRY_AGAIN_INTERVAL.getSeconds(), SECONDS);
                return;
            }

            JSerialCommDeviceAddress address = availablePorts.pop();
            currentSerialPort = address.value();

            Log.debug(DISCOVERY, "Auto-connecting serial port '"+address.value()+"'.");

            waterRower.connect(address);

        } catch (IOException e) {
            Log.error("Couldn't connect, due to errors! Trying next port.", e);
            executorService.schedule(this::tryNextConnectionAttempt, TRY_AGAIN_INTERVAL.getSeconds(), SECONDS);
        } finally {
            lock.unlock();
        }
    }

    /* Updates the available serial ports on the stack. */
    private void updateAvailablePorts() {

        Log.debug(DISCOVERY, "Updating list of available serial ports.");

        // Get all available serial ports:
        SerialPort[] commPorts = SerialPort.getCommPorts();

        for(SerialPort portIdentifier : commPorts) {

            String portName = portIdentifier.getSystemPortName();
            // Ignore /dev/cu ports.
            if (portName.startsWith("/dev/cu."))
                continue;

            // Ignore bluetooth ports.
            if (portName.contains("Bluetooth") || portName.contains("BT"))
                continue;

            if (portIdentifier.isOpen()) {
                Log.warn(DISCOVERY, "Skipping serial port '"+portName+"', because it is currently owned by another thread or application.");
                continue;
            }

            availablePorts.push(new JSerialCommDeviceAddress(portName));

            Log.debug(DISCOVERY, "Serial port found: " + portName);
        }

        putLastSuccessfulPortFirst();
    }

    /* Add the last successful serial port, in order to speed up connection. */
    private void putLastSuccessfulPortFirst() {

        String lastSuccessfulPort = discoveryStore.getLastSuccessfulSerialPort();

        if (lastSuccessfulPort == null)
            return;

        JSerialCommDeviceAddress lastAddress = new JSerialCommDeviceAddress(lastSuccessfulPort);

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
        Log.debug(DISCOVERY, "Stopping discovery.");
        isActive.set(false);
    }

}
