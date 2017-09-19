package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;

/**
 * An interface for subscriptions.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public interface ISubscription {

    /**
     * Returns the message that must be send to the Water Rower S4/S5 monitor to send the current value
     * for the subscription.
     *
     * @return The poll message or null, if not message must be send.
     */
    AbstractMessage poll();

    /**
     * Handles the received message from the Water Rower S4/S5 monitor.
     *
     * @param msg The message from the Water Rower S4/S5 monitor.
     */
    void handle(AbstractMessage msg);

}