package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.ConnectionListener;
import de.tbressler.waterrower.io.IConnectionListener;
import de.tbressler.waterrower.io.WaterRowerConnector;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.log.Log;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The implementation of the subscription polling service.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class SubscriptionPollingService implements ISubscriptionPollingService {

    private static final int SEND_INTERVAL = 50; // in ms


    /* The polling interval. */
    private final Duration interval;

    /* List of subscriptions. */
    private final List<ISubscription> subscriptions = new ArrayList<>();

    /* The connector to the WaterRower. */
    private final WaterRowerConnector connector;

    /* The executor service for polling of subscriptions. */
    private final ScheduledExecutorService executorService;

    /* True if subscription polling is active. */
    private final AtomicBoolean isActive = new AtomicBoolean(false);


    /* Listener for the connection to the WaterRower, which handles the received messages*/
    private final IConnectionListener listener = new ConnectionListener() {
        @Override
        public void onMessageReceived(AbstractMessage msg) {

            // If not active skip execution.
            if (!isActive.get())
                return;

            for(ISubscription subscription : subscriptions) {
                subscription.handle(msg);
            }
        }
    };


    /**
     * The subscription polling manager.
     *
     * @param interval The polling interval (in milliseconds), must not be null.
     * @param connector The connector to the WaterRower, must not be null.
     * @param executorService The executor service for the subscription polling, must not be null.
     */
    public SubscriptionPollingService(Duration interval, WaterRowerConnector connector, ScheduledExecutorService executorService) {
        this.interval = requireNonNull(interval);

        this.connector = requireNonNull(connector);
        this.connector.addConnectionListener(listener);

        this.executorService = requireNonNull(executorService);
    }


    /**
     * Start the subscription polling service.
     */
    @Override
    public void start() {

        Log.debug("Start subscription polling service.");

        isActive.set(true);
        schedulePollingTask(0); // Execute the polling immediately.
    }

    /* Schedule the polling task for execution (with a delay in millis). */
    private void schedulePollingTask(long delay) {
        executorService.schedule(this::executePolling, delay, MILLISECONDS);
    }

    /* Execute the polling task. */
    private void executePolling() {

        // If not active skip execution.
        if (!isActive.get())
            return;

        Log.debug("Schedule polling for "+subscriptions.size()+" subscription(s)...");

        int delay = SEND_INTERVAL;
        for (ISubscription subscription : subscriptions) {

            AbstractMessage msg = subscription.poll();
            scheduleSendMessageTask(msg, delay);

            delay += SEND_INTERVAL;
        }

        if (isActive.get())
            schedulePollingTask(interval.toMillis()); // Execute the next polling with the delay.
    }

    /* Schedule the send task for execution. */
    private void scheduleSendMessageTask(AbstractMessage msg, int delay) {
        executorService.schedule(() -> sendMessage(msg), delay, MILLISECONDS);
    }

    /* Send the given message to the WaterRower. */
    private void sendMessage(AbstractMessage msg) {
        try {

            // If not active skip execution.
            if (!isActive.get())
                return;

            Log.debug("Send scheduled polling message >" + msg.toString() + "<");

            connector.send(msg);

        } catch (IOException e) {
            Log.error("Couldn't send polling message!", e);
        }
    }


    /**
     * Stop the subscription polling service.
     */
    @Override
    public void stop() {

        Log.debug("Stop subscription polling service.");

        isActive.set(false);
    }


    /**
     * Subscribe to data/events. This will start the polling for the given data.
     *
     * @param subscription The subscription and callback, must not be null.
     */
    @Override
    public void subscribe(ISubscription subscription) {
        subscriptions.add(requireNonNull(subscription));
        Log.debug("Added subscription: " + subscription);
    }

    /**
     * Unsubscribe from data/events. This will stop the polling for the given data.
     *
     * @param subscription The subscription, must not be null.
     */
    @Override
    public void unsubscribe(ISubscription subscription) {
        subscriptions.remove(requireNonNull(subscription));
        Log.debug("Removed subscription: " + subscription);
    }

}
