package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxConnectionListener;
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
import de.tbressler.waterrower.subscriptions.SubscriptionPollingService;
import de.tbressler.waterrower.watchdog.DeviceVerificationWatchdog;
import de.tbressler.waterrower.watchdog.ITimeoutListener;
import de.tbressler.waterrower.watchdog.PingWatchdog;
import de.tbressler.waterrower.workout.Workout;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static de.tbressler.waterrower.model.ErrorCode.*;
import static de.tbressler.waterrower.utils.WaterRowerCompatibility.isSupportedWaterRower;
import static de.tbressler.waterrower.watchdog.TimeoutReason.DEVICE_NOT_CONFIRMED_TIMEOUT;
import static java.util.Objects.requireNonNull;

/**
 * The entry point of the WaterRower library.
 *
 * This class connects with the WaterRower and exchanges the information between PC and
 * WaterRower monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRowerV2 {

    /* Handles the connection to the WaterRower. */
    private final WaterRowerConnector connector;

    /* Polls and handles subscriptions. */
    private final SubscriptionPollingService subscriptionPollingService;

    /* Watchdog that checks if a ping is received periodically. */
    private final PingWatchdog pingWatchdog;

    /* Watchdog that checks if the device sends it's model information in order to verify
     * compatibility with the library. */
    private final DeviceVerificationWatchdog deviceVerificationWatchdog;

    /* All listeners. */
    private List<IWaterRowerConnectionListener> listeners = new ArrayList<>();


    /* The listener for the WaterRower connector. */
    private IRxtxConnectionListener connectionListener = new RxtxConnectionListener() {

        @Override
        public void onConnected() {
            handleOnConnect();
        }

        @Override
        public void onMessageReceived(AbstractMessage msg) {
            handleOnMessageReceived(msg);
        }

        @Override
        public void onDisconnected() {
            handleOnDisconnected();
        }

        @Override
        public void onError() {
            fireOnError(COMMUNICATION_FAILED);
        }

    };


    /* Listener for watchdog timeouts. */
    private ITimeoutListener timeoutListener = reason -> {
        fireOnError((reason == DEVICE_NOT_CONFIRMED_TIMEOUT) ? DEVICE_NOT_SUPPORTED : TIMEOUT);
    };


    /**
     * The entry point of the WaterRower library.
     *
     * This class connects with the WaterRower and exchanges the information between PC and
     * WaterRower monitor.
     *
     * @param connector The connector to the WaterRower, must not be null.
     * @param pingWatchdog The Watchdog that checks if a ping is received periodically, must not
     *                     be null.
     * @param deviceVerificationWatchdog Watchdog that checks if the device sends it's model
     *                                   information in order to verify compatibility with the
     *                                   library. Must not be null.
     * @param subscriptionPollingService The subscription polling service, which polls and
     *                                   handles the subscriptions. Must not be null.
     */
    public WaterRowerV2(WaterRowerConnector connector,
                        PingWatchdog pingWatchdog,
                        DeviceVerificationWatchdog deviceVerificationWatchdog,
                        SubscriptionPollingService subscriptionPollingService) {

        this.connector = requireNonNull(connector);
        this.connector.addConnectionListener(connectionListener);

        this.pingWatchdog = pingWatchdog;
        this.pingWatchdog.setTimeoutListener(timeoutListener);

        this.deviceVerificationWatchdog = deviceVerificationWatchdog;
        this.deviceVerificationWatchdog.setTimeoutListener(timeoutListener);

        this.subscriptionPollingService = requireNonNull(subscriptionPollingService);
    }


    /**
     * Connect to the rowing computer.
     *
     * @param address The serial port, must not be null.
     *
     * @throws IOException If connect fails.
     */
    public void connect(RxtxDeviceAddress address) throws IOException {
        if (connector.isConnected())
            throw new IOException("Already connected! Can not connect again.");
        connector.connect(requireNonNull(address));
    }


    /* Handles the task after a successful connect. */
    private void handleOnConnect() {
        try {

            Log.debug(LIBRARY, "RXTX connected. Sending 'start communication' message.");

            deviceVerificationWatchdog.start();

            connector.send(new StartCommunicationMessage());

        } catch (IOException e) {
            Log.error("Couldn't send 'start communication' message!", e);
            fireOnError(COMMUNICATION_FAILED);
        }
    }


    /* Handles received messages. */
    private void handleOnMessageReceived(AbstractMessage msg) {
        try {

            pingWatchdog.pingReceived();

            handleLowLevelMessages(msg);

        } catch (IOException e) {
            Log.error("A communication error occurred!", e);
            fireOnError(COMMUNICATION_FAILED);
        }
    }

    /* Handles all the low level messages like ping, hardware type, ... */
    private void handleLowLevelMessages(AbstractMessage msg) throws IOException {

        if (msg instanceof HardwareTypeMessage) {

            if (((HardwareTypeMessage) msg).isWaterRower()) {

                Log.debug(LIBRARY, "Connected with WaterRower. Sending poll for model information.");

                connector.send(new RequestModelInformationMessage());

            } else {

                Log.warn(LIBRARY, "The connected device is not a WaterRower!");

                fireOnError(DEVICE_NOT_SUPPORTED);
            }

        } else if (msg instanceof ModelInformationMessage) {

            ModelInformation modelInformation = ((ModelInformationMessage) msg).getModelInformation();

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


    /**
     * Returns true if connected to a supported WaterRower monitor.
     *
     * @return True if connected to WaterRower monitor.
     */
    public boolean isConnected() {
        if (!connector.isConnected())
            return false;
        return deviceVerificationWatchdog.isDeviceConfirmed();
    }


    /**
     * Disconnects from the rowing computer.
     *
     * @throws IOException If disconnect fails.
     */
    public void disconnect() throws IOException {
        checkIfConnected();

        // Be polite and send a goodbye.
        connector.send(new ExitCommunicationMessage());
        // Disconnect.
        connector.disconnect();
    }


    /* Handle disconnect. */
    private void handleOnDisconnected() {

        Log.debug(LIBRARY, "RXTX disconnected.");

        deviceVerificationWatchdog.stop();
        pingWatchdog.stop();

        fireOnDisconnected();
    }


    /**
     * Request the rowing computer to perform a reset; this will be identical to the user performing this with the
     * power button. Used prior to configuring the rowing computer from a PC. Interactive mode will be disabled on a
     * reset.
     *
     * @throws IOException If the message couldn't be send.
     */
    public void performReset() throws IOException {
        checkIfConnected();
        connector.send(new ResetMessage());
    }


    public void startWorkout(Workout workout) throws IOException {
        checkIfConnected();
        // TODO Start workout
    }


    /* Throws IOException if not connected to a WaterRower. */
    private void checkIfConnected() throws IOException {
        if (!isConnected())
            throw new IOException("Not connected to a WaterRower!");
    }


    /**
     * Subscribe to events. This will start the polling for the given data.
     *
     * @param subscription The subscription and callback, must not be null.
     */
    public void subscribe(ISubscription subscription) {
        subscriptionPollingService.subscribe(requireNonNull(subscription));
    }

    /**
     * Unsubscribe from events. This will stop the polling for the given data.
     *
     * @param subscription The subscription, must not be null.
     */
    public void unsubscribe(ISubscription subscription) {
        subscriptionPollingService.unsubscribe(requireNonNull(subscription));
    }


    /**
     * Adds the listener.
     *
     * @param listener The listener, must not be null.
     */
    public void addConnectionListener(IWaterRowerConnectionListener listener) {
        listeners.add(requireNonNull(listener));
    }

    /* Notifies listeners when an error occurred. */
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
