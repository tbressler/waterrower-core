package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.msg.AbstractMessage;
import de.tbressler.waterrower.msg.out.ExitCommunicationMessage;
import de.tbressler.waterrower.msg.out.StartCommunicationMessage;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static java.util.Objects.requireNonNull;

/**
 *
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRower {

    /* The RXTX communication service. */
    private final RxtxCommunicationService communicationService;

    /* The executor service for asynchronous tasks. */
    private final ExecutorService executorService;

    /* Listeners. */
    private List<IWaterRowerListener> listeners = new ArrayList<>();

    /* The lock to synchronize connect and disconnect. */
    private ReentrantLock lock = new ReentrantLock(true);

    /* The listener for the RXTX connection. */
    private IRxtxConnectionListener connectionListener = new IRxtxConnectionListener() {

        @Override
        public void onConnected() {
            try {

                Log.debug(LIBRARY, "RXTX connected. Sending 'start communication' message.");

                sendAsync(new StartCommunicationMessage());

            } catch (IOException e) {
                Log.error("Couldn't send 'start communication' message!", e);
                fireOnError();
            }
        }

        @Override
        public void onDisconnected() {
            fireOnDisconnected();
        }

        @Override
        public void onError() {
            fireOnError();
        }

    };


    /**
     *
     * @param communicationService The RXTX communication service, must not be null.
     * @param executorService The executor service for asynchronous tasks, must not be null.
     */
    public WaterRower(RxtxCommunicationService communicationService, ExecutorService executorService) {
        this.communicationService = requireNonNull(communicationService);
        this.communicationService.addRxtxConnectionListener(connectionListener);

        this.executorService = requireNonNull(executorService);
    }



    /**
     * Connect to the rowing computer.
     *
     * @param address
     * @throws IOException
     */
    public void connect(RxtxDeviceAddress address) throws IOException {
        requireNonNull(address);

        lock.lock();

        try {

            if (isConnected())
                throw new IOException("Service is already connected! Can not connect.");

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        Log.debug(LIBRARY, "Opening RXTX channel at '" + address.value() + "' connection.");

                        communicationService.open(address);

                    } catch (IOException e) {
                        Log.warn(LIBRARY, "Couldn't connect to serial port! " + e.getMessage());
                        fireOnError();
                    }
                }
            });

        } finally {
            lock.unlock();
        }
    }

    /**
     * Disconnects from the rowing computer.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {

        lock.lock();

        try {

            if (!isConnected())
                throw new IOException("Service is not connected! Can not disconnect.");

            sendGoodbyeAndDisconnectAsync();

        } finally {
            lock.unlock();
        }
    }

    /* Sends "goodbye" message and disconnects asynchronous. */
    private void sendGoodbyeAndDisconnectAsync() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.debug(LIBRARY, "Sending 'exit communication' message.");

                    // Send "goodbye" message.
                    sendInternally(new ExitCommunicationMessage());

                    Log.debug(LIBRARY, "Closing RXTX channel.");

                    // Close channel.
                    communicationService.close();

                } catch (IOException e) {
                    Log.warn(LIBRARY, "Couldn't disconnect from serial port! " + e.getMessage());
                    fireOnError();
                }
            }
        });
    }

    /* Returns true if connected. */
    private boolean isConnected() {
        return communicationService.isConnected();
    }

    /* Sends given message asynchronous. */
    void sendAsync(AbstractMessage msg) throws IOException {
        requireNonNull(msg);

        lock.lock();

        try {

            if (!isConnected())
                throw new IOException("Service is not connected! Can not send message.");

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendInternally(msg);
                    } catch (IOException e) {
                        Log.error("Message couldn't be send!", e);
                        fireOnError();
                    }
                }
            });

        } finally {
            lock.unlock();
        }
    }

    /* Sends the message. */
    private void sendInternally(AbstractMessage msg) throws IOException {

        Log.debug(LIBRARY, "Sending message '" + msg.toString() + "'.");

        communicationService.send(msg);
    }


    /**
     * Returns the model information from the rowing computer.
     *
     * @return
     */
    public ModelInformation getModelInformation() {
        throw new IllegalStateException("Not implemented yet!");
    }


    /**
     * Request the rowing computer to perform a reset; this will be identical to the user performing this with the
     * power button. Used prior to configuring the rowing computer from a PC. Interactive mode will be disabled on a
     * reset.
     *
     * @throws IOException
     */
    public void performReset() throws IOException {
        throw new IllegalStateException("Not implemented yet!");
    }


    /**
     * Adds the listener.
     *
     * @param listener The listener, must not be null.
     */
    public void addWaterRowerListener(IWaterRowerListener listener) {
        listeners.add(requireNonNull(listener));
    }

    /* Notifies listeners when error occurred. */
    private void fireOnError() {
        for(IWaterRowerListener listener : listeners)
            listener.onError();
    }

    /* Notifies listeners when connected. */
    private void fireOnConnected() {
        for(IWaterRowerListener listener : listeners)
            listener.onConnected();
    }

    /* Notifies listeners when disconnected. */
    private void fireOnDisconnected() {
        for(IWaterRowerListener listener : listeners)
            listener.onDisconnected();
    }

    /**
     * Removes the listener.
     *
     * @param listener The listener that should be removed, must not be null.
     */
    public void removeWaterRowerListener(IWaterRowerListener listener) {
        listeners.remove(requireNonNull(listener));
    }

}
