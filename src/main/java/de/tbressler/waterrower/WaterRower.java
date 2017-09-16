package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.*;
import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import de.tbressler.waterrower.io.msg.out.RequestModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.ResetMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.StrokeType;
import de.tbressler.waterrower.utils.Watchdog;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static de.tbressler.waterrower.model.ErrorCode.*;
import static de.tbressler.waterrower.utils.WaterRowerCompatibility.isSupportedWaterRower;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;

/**
 * The entry point of the Water Rower library.
 * This class connects with the Water Rower and exchanges the information between PC and Water Rower monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRower {

    /* Maximum duration for the Water Rower device to send its device information. */
    private static Duration MAXIMUM_DEVICE_VERIFICATION_DURATION = ofSeconds(5);

    /* Maximum duration between ping messages.*/
    private static Duration MAXIMUM_PING_DURATION = ofSeconds(5);


    /* The RXTX communication service. */
    private final RxtxCommunicationService communicationService;

    /* The executor service for asynchronous tasks. */
    private final ExecutorService executorService;

    /* Listeners. */
    private List<IWaterRowerListener> listeners = new ArrayList<>();

    /* The lock to synchronize connect and disconnect. */
    private ReentrantLock lock = new ReentrantLock(true);

    /* Model information of connected Water Rower, maybe null if not connected. */
    private ModelInformation modelInformation;


    /* True if connected device is supported Water Rower. */
    private AtomicBoolean deviceConfirmed = new AtomicBoolean(false);
    /* Watchdog that checks if the device sends it's model information in order to verify compatibility with the library. */
    private Watchdog deviceVerificationWatchdog = new Watchdog(MAXIMUM_DEVICE_VERIFICATION_DURATION, "device-verification-watchdog") {
        @Override
        protected void wakeUpAndCheck() {
            if (deviceConfirmed.get() == false)
                fireOnError(DEVICE_NOT_SUPPORTED);
        }
    };


    /* Last time a ping was received. */
    private AtomicLong lastReceivedPing = new AtomicLong(0);
    /* Watchdog that checks if a ping is received periodically. */
    private Watchdog pingWatchdog = new Watchdog(MAXIMUM_PING_DURATION, true, "ping-watchdog") {
        @Override
        protected void wakeUpAndCheck() {
            if (currentTimeMillis() - lastReceivedPing.get() > MAXIMUM_PING_DURATION.toMillis())
                fireOnError(TIMEOUT);
        }
    };


    /* The listener for the RXTX connection. */
    private IRxtxConnectionListener connectionListener = new IRxtxConnectionListener() {

        @Override
        public void onConnected() {
            try {

                Log.debug(LIBRARY, "RXTX connected. Sending 'start communication' message.");

                deviceConfirmed.set(false);
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
            deviceConfirmed.set(false);
            pingWatchdog.stop();
            fireOnDisconnected();
        }

        @Override
        public void onError() {
            fireOnError(COMMUNICATION_FAILED);
        }

    };


    /**
     * The entry point of the Water Rower library.
     * This class connects with the Water Rower and exchanges the information between PC and Water Rower monitor.
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

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        Log.debug(LIBRARY, "Opening RXTX channel at '" + address.value() + "' connection.");

                        communicationService.open(address);

                    } catch (IOException e) {
                        Log.warn(LIBRARY, "Couldn't connect to serial port! " + e.getMessage());
                        fireOnError(COMMUNICATION_FAILED);
                    }
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

                Log.debug(LIBRARY, "Connected with Water Rower. Sending request for model information.");

                sendMessageAsync(new RequestModelInformationMessage());

            } else {

                Log.warn(LIBRARY, "The connected device is not a Water Rower!");

                fireOnError(DEVICE_NOT_SUPPORTED);
            }

        } else if (msg instanceof ModelInformationMessage) {

            modelInformation = ((ModelInformationMessage) msg).getModelInformation();

            Log.debug(LIBRARY, "Received model information from connected Water Rower:\n" +
                    " Model: " + modelInformation);

            if (isSupportedWaterRower(modelInformation)) {

                Log.debug(LIBRARY, "Monitor type and firmware are supported by this library. Successfully connected with Water Rower.");

                // Set device model confirmed and stop watchdog:
                deviceConfirmed.set(true);
                deviceVerificationWatchdog.stop();

                // Start ping watchdog.
                pingWatchdog.start();

                fireOnConnected(modelInformation);

            } else {

                Log.warn(LIBRARY, "The monitor type and/or firmware of the connected Water Rower are not supported by this library!");

                fireOnError(DEVICE_NOT_SUPPORTED);
            }

        } else if ((msg instanceof PingMessage)
                || (msg instanceof AcknowledgeMessage)) {

            Log.debug(LIBRARY, "'Ping' or 'Acknowledge' message received.");

            lastReceivedPing.set(currentTimeMillis());

        } else if (msg instanceof ErrorMessage) {

            Log.debug(LIBRARY, "Error message received from Water Rower monitor.");

            fireOnError(ERROR_MESSAGE_RECEIVED);
        }
    }

    /* Handles messages. */
    private void handleMessages(AbstractMessage msg) {
        if (msg instanceof StrokeMessage) {
            fireOnStroke(((StrokeMessage) msg).getStrokeType());
        } else if (msg instanceof PulseCountMessage) {
            fireOnPulseCount(((PulseCountMessage) msg).getPulsesCounted());
        }
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
        executorService.submit(new Runnable() {
            @Override
            public void run() {
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
            }
        });
    }

    /* Returns true if connected. */
    private boolean isConnected() {
        return communicationService.isConnected();
    }


    /**
     * Returns true if connected to a supported Water Rower monitor.
     *
     * @return True if connected to Water Rower monitor.
     */
    public boolean isConnectedWithSupportedMonitor() {
        if (isConnected())
            return deviceConfirmed.get();
        return false;
    }


    /* Sends given message asynchronous. */
    void sendMessageAsync(AbstractMessage msg) throws IOException {
        requireNonNull(msg);

        lock.lock();

        try {

            if (!isConnected())
                throw new IOException("Service is not connected! Can not send message.");

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMessageInternally(msg);
                    } catch (IOException e) {
                        Log.error("Message couldn't be send!", e);
                        fireOnError(COMMUNICATION_FAILED);
                    }
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

        Log.debug(LIBRARY, "Requesting Water Rower monitor to reset.");

        sendMessageAsync(new ResetMessage());
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
    private void fireOnError(ErrorCode errorCode) {
        for(IWaterRowerListener listener : listeners)
            listener.onError(errorCode);
    }

    /* Notifies listeners when connected. */
    private void fireOnConnected(ModelInformation modelInformation) {
        for(IWaterRowerListener listener : listeners)
            listener.onConnected(modelInformation);
    }

    /* Notifies listeners when disconnected. */
    private void fireOnDisconnected() {
        listeners.forEach(IWaterRowerListener::onDisconnected);
    }

    /* Notifies listeners about stroke update. */
    private void fireOnStroke(StrokeType strokeType) {
        for(IWaterRowerListener listener : listeners)
            listener.onStroke(strokeType);
    }

    /* Notifies listeners about pulse count update. */
    private void fireOnPulseCount(int pulsesCounted) {
        for(IWaterRowerListener listener : listeners)
            listener.onPulseCount(pulsesCounted);
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
