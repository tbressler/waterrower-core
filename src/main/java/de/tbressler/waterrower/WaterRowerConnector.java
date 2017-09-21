package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.log.Log;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static java.util.Objects.requireNonNull;

/**
 * Handles the connection to the WaterRower.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRowerConnector {

    /* The RXTX communication service. */
    private final RxtxCommunicationService communicationService;

    /* The executor service for connection and sending. */
    private final ExecutorService connectionExecutorService;

    /* The lock to synchronize connect and disconnect. */
    private ReentrantLock lock = new ReentrantLock(true);

    /* Listeners for the RXTX communication. */
    private List<IRxtxConnectionListener> listeners = new ArrayList<>();


    /**
     * Handles the connection to the WaterRower.
     *
     * @param communicationService The RXTX communication service, must not be null.
     * @param connectionExecutorService The executor service, must not be null.
     */
    public WaterRowerConnector(RxtxCommunicationService communicationService, ExecutorService connectionExecutorService) {
        this.communicationService = requireNonNull(communicationService);
        this.connectionExecutorService = requireNonNull(connectionExecutorService);
    }


    /**
     * Connect to the rowing computer.
     *
     * @param address The serial port, must not be null.
     *
     * @throws IOException If connect fails.
     */
    public void connect(RxtxDeviceAddress address) throws IOException {
        requireNonNull(address);

        lock.lock();

        try {

            if (isConnected())
                throw new IOException("Service is already connected! Can not connect.");

            connectionExecutorService.submit(() -> {
                try {

                    Log.debug(LIBRARY, "Opening RXTX channel at '" + address.value() + "' connection.");
                    communicationService.open(address);

                } catch (IOException e) {
                    Log.warn(LIBRARY, "Couldn't connect to serial port! " + e.getMessage());
                    fireOnError();
                }
            });

        } finally {
            lock.unlock();
        }
    }


    /* Returns true if connected. */
    public boolean isConnected() {
        return communicationService.isConnected();
    }


    /**
     * Disconnects from the rowing computer.
     *
     * @throws IOException If disconnect fails.
     */
    public void disconnect() throws IOException {

        lock.lock();

        try {

            if (!isConnected())
                throw new IOException("Service is not connected! Can not disconnect.");

            Log.debug(LIBRARY, "Closing RXTX channel.");

            connectionExecutorService.submit(() -> {
                try {

                    // Close channel.
                    communicationService.close();

                } catch (IOException e) {
                    Log.error("Couldn't disconnect from serial port!", e);
                    fireOnError();
                }
            });

        } finally {
            lock.unlock();
        }
    }


    /**
     * Sends given message asynchronous.
     *
     * @param msg The message to be sent, must not be null.
     */
    public void send(AbstractMessage msg) throws IOException {
        requireNonNull(msg);

        lock.lock();

        try {

            if (!isConnected())
                throw new IOException("Not connected! Can not send message to WaterRower.");

            connectionExecutorService.submit(() -> {
                try {

                    Log.debug(LIBRARY, "Sending message '" + msg.toString() + "'.");
                    communicationService.send(msg);

                } catch (IOException e) {
                    Log.error("Message couldn't be send to WaterRower!", e);
                    fireOnError();
                }
            });

        } finally {
            lock.unlock();
        }
    }


    /**
     * Adds the connection listener.
     *
     * @param listener The listener, must not be null.
     */
    public void addConnectionListener(IRxtxConnectionListener listener) {
        listeners.add(requireNonNull(listener));
        communicationService.addRxtxConnectionListener(listener);
    }

    /* Notifies the listeners about an error. */
    private void fireOnError() {
        listeners.forEach(IRxtxConnectionListener::onError);
    }

    /**
     * Removes the connection listener.
     *
     * @param listener The listener, must not be null.
     */
    public void removeConnectionListener(IRxtxConnectionListener listener) {
        listeners.remove(requireNonNull(listener));
        communicationService.removeRxtxConnectionListener(listener);
    }

}
