package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxConnectionListener;
import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import de.tbressler.waterrower.io.msg.out.ResetMessage;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.subscriptions.ISubscription;
import de.tbressler.waterrower.subscriptions.SubscriptionPollingService;
import de.tbressler.waterrower.workout.Workout;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

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

    private final WaterRowerConnector connector;

    private final SubscriptionPollingService subscriptionPollingService;

    private final ScheduledExecutorService executorService;

    /* All listeners. */
    private List<IWaterRowerConnectionListener> listeners = new ArrayList<>();


    /* The listener for the WaterRower connector. */
    private IRxtxConnectionListener connectionListener = new RxtxConnectionListener() {

    };


    public WaterRowerV2(WaterRowerConnector connector,
                        SubscriptionPollingService subscriptionPollingService,
                        ScheduledExecutorService executorService) {

        this.connector = requireNonNull(connector);
        this.connector.addConnectionListener(connectionListener);

        this.subscriptionPollingService = requireNonNull(subscriptionPollingService);

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
        connector.connect(requireNonNull(address));
    }


    /**
     * Returns true if connected to a supported WaterRower monitor.
     *
     * @return True if connected to WaterRower monitor.
     */
    public boolean isConnected() {

        return false;
    }


    /**
     * Disconnects from the rowing computer.
     *
     * @throws IOException If disconnect fails.
     */
    public void disconnect() throws IOException {
        // Be polite and send a goodbye.
        connector.send(new ExitCommunicationMessage());
        // Disconnect.
        connector.disconnect();
    }


    /**
     * Request the rowing computer to perform a reset; this will be identical to the user performing this with the
     * power button. Used prior to configuring the rowing computer from a PC. Interactive mode will be disabled on a
     * reset.
     *
     * @throws IOException If message couldn't be send.
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
