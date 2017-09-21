package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.ErrorMessage;
import de.tbressler.waterrower.io.msg.in.HardwareTypeMessage;
import de.tbressler.waterrower.io.msg.in.ModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import de.tbressler.waterrower.io.msg.out.RequestModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.ResetMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.subscriptions.ISubscription;
import de.tbressler.waterrower.utils.DeviceVerificationWatchdog;
import de.tbressler.waterrower.utils.PingWatchdog;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static de.tbressler.waterrower.model.ErrorCode.*;
import static de.tbressler.waterrower.utils.WaterRowerCompatibility.isSupportedWaterRower;
import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * The entry point of the WaterRower library.
 *
 * This class connects with the WaterRower and exchanges the information between PC and
 * WaterRower monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRower {

    /* Maximum duration for the WaterRower device to send its device information. */
    private static Duration DEVICE_VERIFICATION_INTERVAL = ofSeconds(5);

    /* Maximum duration between ping messages.*/
    private static Duration PING_INTERVAL = ofSeconds(5);


    /* The RXTX communication service. */
    private final RxtxCommunicationService communicationService;

    /* The executor service for connection and sending. */
    private final ExecutorService connectionExecutorService;


    private final ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();


    /* Listeners. */
    private List<IWaterRowerConnectionListener> listeners = new ArrayList<>();

    /* The lock to synchronize connect and disconnect. */
    private ReentrantLock lock = new ReentrantLock(true);

    /* Model information of connected WaterRower, maybe null if not connected. */
    private ModelInformation modelInformation;


    /* Watchdog that checks if the device sends it's model information in order to verify
     * compatibility with the library. */
    private DeviceVerificationWatchdog deviceVerificationWatchdog = new DeviceVerificationWatchdog(DEVICE_VERIFICATION_INTERVAL, scheduledExecutorService) {
        @Override
        public void onDeviceNotConfirmed() {
            fireOnError(DEVICE_NOT_SUPPORTED);
        }
    };


    /* Watchdog that checks if a ping is received periodically. */
    private PingWatchdog pingWatchdog = new PingWatchdog(PING_INTERVAL, scheduledExecutorService) {
        @Override
        protected void onTimeout() {
            fireOnError(TIMEOUT);
        }
    };


    /* The listener for the RXTX connection. */
    private IRxtxConnectionListener connectionListener = new IRxtxConnectionListener() {

        @Override
        public void onConnected() {
            try {

                Log.debug(LIBRARY, "RXTX connected. Sending 'start communication' message.");

                deviceVerificationWatchdog.start();

                sendMessageAsync(new StartCommunicationMessage());

            } catch (IOException e) {
                Log.error("Couldn't send 'start communication' message!", e);
                fireOnError(COMMUNICATION_FAILED);
            }
        }

        @Override
        public void onMessageReceived(AbstractMessage msg) {
            try {

                pingWatchdog.pingReceived();

                handleLowLevelMessages(msg);
                handleMessages(msg);

            } catch (IOException e) {
                Log.error("A communication error occurred!", e);
                fireOnError(COMMUNICATION_FAILED);
            }
        }

        @Override
        public void onDisconnected() {
            Log.debug(LIBRARY, "RXTX disconnected.");
            pingWatchdog.stop();
            fireOnDisconnected();
        }

        @Override
        public void onError() {
            fireOnError(COMMUNICATION_FAILED);
        }

    };


    /**
     * The entry point of the WaterRower library.
     *
     * This class connects with the WaterRower and exchanges the information between PC and
     * WaterRower monitor.
     *
     * @param communicationService The RXTX communication service, must not be null.
     * @param connectionExecutorService The executor service for connection and sending, must not be null.
     */
    public WaterRower(RxtxCommunicationService communicationService,
                      ExecutorService connectionExecutorService) {
        this.communicationService = requireNonNull(communicationService);
        this.communicationService.addRxtxConnectionListener(connectionListener);

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
                    fireOnError(COMMUNICATION_FAILED);
                }
            });

        } finally {
            lock.unlock();
        }
    }

    /* Handles background messages, like model information or ping messages. */
    private void handleLowLevelMessages(AbstractMessage msg) throws IOException {

        if (msg instanceof HardwareTypeMessage) {

            if (((HardwareTypeMessage) msg).isWaterRower()) {

                Log.debug(LIBRARY, "Connected with WaterRower. Sending poll for model information.");

                sendMessageAsync(new RequestModelInformationMessage());

            } else {

                Log.warn(LIBRARY, "The connected device is not a WaterRower!");

                fireOnError(DEVICE_NOT_SUPPORTED);
            }

        } else if (msg instanceof ModelInformationMessage) {

            modelInformation = ((ModelInformationMessage) msg).getModelInformation();

            Log.debug(LIBRARY, "Received model information from connected WaterRower:\n" +
                    " Model: " + modelInformation);

            if (isSupportedWaterRower(modelInformation)) {

                Log.debug(LIBRARY, "Monitor type and firmware are supported by this library. Successfully connected with WaterRower.");

                // Set device model confirmed and stop watchdog:
                deviceVerificationWatchdog.setDeviceConfirmed(true);
                deviceVerificationWatchdog.stop();

                // Start ping watchdog.
                pingWatchdog.start();

                fireOnConnected(modelInformation);

            } else {

                Log.warn(LIBRARY, "The monitor type and/or firmware of the connected WaterRower are not supported by this library!");

                fireOnError(DEVICE_NOT_SUPPORTED);
            }

        } else if (msg instanceof ErrorMessage) {

            Log.debug(LIBRARY, "Error message received from WaterRower monitor.");

            fireOnError(ERROR_MESSAGE_RECEIVED);
        }
    }

    /* Handles messages. */
    private void handleMessages(AbstractMessage msg) {
        // TODO Send received messages to subscriptions.
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

            // Stop watchdogs:
            deviceVerificationWatchdog.stop();
            pingWatchdog.stop();

            // Send goodbye and disconnect.
            sendGoodbyeAndDisconnectAsync();

        } finally {
            lock.unlock();
        }
    }

    /* Sends "goodbye" message and disconnects asynchronous. */
    private void sendGoodbyeAndDisconnectAsync() {
        connectionExecutorService.submit(() -> {
            try {

                Log.debug(LIBRARY, "Sending 'exit communication' message.");

                // Send "goodbye" message.
                sendMessageInternally(new ExitCommunicationMessage());

                Log.debug(LIBRARY, "Closing RXTX channel.");

                // Close channel.
                communicationService.close();

            } catch (IOException e) {
                Log.warn(LIBRARY, "Couldn't disconnect from serial port! " + e.getMessage());
                fireOnError(COMMUNICATION_FAILED);
            }
        });
    }

    /* Returns true if connected. */
    private boolean isConnected() {
        return communicationService.isConnected();
    }


    /**
     * Returns true if connected to a supported WaterRower monitor.
     *
     * @return True if connected to WaterRower monitor.
     */
    public boolean isConnectedWithSupportedMonitor() {
        if (isConnected())
            return deviceVerificationWatchdog.isDeviceConfirmed();
        return false;
    }


    /* Sends given message asynchronous. */
    void sendMessageAsync(AbstractMessage msg) throws IOException {
        requireNonNull(msg);

        lock.lock();

        try {

            if (!isConnected())
                throw new IOException("Not connected! Can not send message to WaterRower.");

            connectionExecutorService.submit(() -> {
                try {
                    sendMessageInternally(msg);
                } catch (IOException e) {
                    Log.error("Message couldn't be send to WaterRower!", e);
                    fireOnError(COMMUNICATION_FAILED);
                }
            });

        } finally {
            lock.unlock();
        }
    }

    /* Sends the message. */
    private void sendMessageInternally(AbstractMessage msg) throws IOException {

        Log.debug(LIBRARY, "Sending message '" + msg.toString() + "'.");

        communicationService.send(msg);
    }


    /**
     * Returns the model information from the rowing computer.
     *
     * @return Monitor type and firmware version, maybe null if not connected or not yet retrieved.
     */
    public ModelInformation getModelInformation() {
        return modelInformation;
    }


    /**
     * Request the rowing computer to perform a reset; this will be identical to the user performing this with the
     * power button. Used prior to configuring the rowing computer from a PC. Interactive mode will be disabled on a
     * reset.
     *
     * @throws IOException If message couldn't be send.
     */
    public void performReset() throws IOException {

        Log.debug(LIBRARY, "Requesting WaterRower monitor to reset.");

        sendMessageAsync(new ResetMessage());
    }


    /**
     * Subscribe to events. This will start the polling for the given data.
     *
     * @param subscription The subscription and callback, must not be null.
     */
    public void subscribe(ISubscription subscription) {
        // TODO Subscribe
    }

    /**
     * Unsubscribe from events. This will stop the polling for the given data.
     *
     * @param subscription The subscription, must not be null.
     */
    public void unsubscribe(ISubscription subscription) {
        // TODO Unsubscribe
    }


    /**
     * Adds the listener.
     *
     * @param listener The listener, must not be null.
     */
    public void addConnectionListener(IWaterRowerConnectionListener listener) {
        listeners.add(requireNonNull(listener));
    }


    /* Notifies listeners when error occurred. */
    private void fireOnError(ErrorCode errorCode) {
        for(IWaterRowerConnectionListener listener : listeners)
            listener.onError(errorCode);
    }

    /* Notifies listeners when connected. */
    private void fireOnConnected(ModelInformation modelInformation) {
        for(IWaterRowerConnectionListener listener : listeners)
            listener.onConnected(modelInformation);
    }

    /* Notifies listeners when disconnected. */
    private void fireOnDisconnected() {
        listeners.forEach(IWaterRowerConnectionListener::onDisconnected);
    }


    /**
     * Removes the listener.
     *
     * @param listener The listener that should be removed, must not be null.
     */
    public void removeConnectionListener(IWaterRowerConnectionListener listener) {
        listeners.remove(requireNonNull(listener));
    }

}
