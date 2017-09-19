package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The subscription manager.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class SubscriptionPollingService {

    /* The polling interval. */
    private final int interval;

    /* List of subscriptions. */
    private final List<ISubscription> subscriptions = new ArrayList<>();

    /* The executor service for polling of subscriptions. */
    private final ScheduledExecutorService executorService;

    /* True if subscription polling is active. */
    private final AtomicBoolean isActive = new AtomicBoolean(false);


    // TODO Receive messages from the WaterRowerConnector!


    /**
     *
     * @param interval The polling interval (in milliseconds).
     * @param executorService The executor service for the subscription polling, must not be null.
     */
    public SubscriptionPollingService(int interval, ScheduledExecutorService executorService) {
        this.interval = interval;
        this.executorService = requireNonNull(executorService);
    }


    /**
     *
     */
    public void start() {

        isActive.set(true);

        executorService.scheduleAtFixedRate(() -> {

            for (ISubscription subscription : subscriptions) {

                // If not active skip execution.
                if (!isActive.get())
                    return;

                AbstractMessage msg = subscription.poll();

                // TODO Send this message via WaterRowerConnector!

            }

        }, 0, interval, MILLISECONDS);

    }


    /**
     *
     */
    public void stop() {
        isActive.set(false);
        executorService.shutdown();
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
