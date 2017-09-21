package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.WaterRowerConnector;
import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxConnectionListener;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.log.Log;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The subscription polling manager.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class SubscriptionPollingService {

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
    private final IRxtxConnectionListener listener = new RxtxConnectionListener() {
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
    public void start() {

        Log.debug(LIBRARY, "Start subscription polling service.");

        isActive.set(true);
        scheduleTask();
    }

    /* Schedule the task for execution. */
    private void scheduleTask() {
        executorService.schedule(this::executeTask, interval.toMillis(), MILLISECONDS);
    }

    /* Execute the task. */
    private void executeTask() {

        Log.debug(LIBRARY, "Start polling for subscriptions...");

        for (ISubscription subscription : subscriptions) {

            // If not active skip execution.
            if (!isActive.get())
                return;

            try {
                AbstractMessage msg = subscription.poll();
                connector.send(msg);
            } catch (IOException e) {
                Log.error("Couldn't poll for subscriptions, due to errors!", e);
            }
        }

        Log.debug(LIBRARY, "Finished polling.");

        if (isActive.get())
            scheduleTask();
    }


    /**
     * Stop the subscription polling service.
     */
    public void stop() {

        Log.debug(LIBRARY, "Stop subscription polling service.");

        isActive.set(false);
    }


    /**
     * Subscribe to events. This will start the polling for the given data.
     *
     * @param subscription The subscription and callback, must not be null.
     */
    public void subscribe(ISubscription subscription) {
        subscriptions.add(requireNonNull(subscription));
    }

    /**
     * Unsubscribe from events. This will stop the polling for the given data.
     *
     * @param subscription The subscription, must not be null.
     */
    public void unsubscribe(ISubscription subscription) {
        subscriptions.remove(requireNonNull(subscription));
    }

}
