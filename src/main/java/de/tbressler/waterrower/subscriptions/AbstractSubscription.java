package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;

/**
 * An abstract class for subscriptions.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class AbstractSubscription {


    public AbstractSubscription() {}


    /**
     * Returns the message that must be send to the Water Rower S4/S5 monitor to send the current value
     * for the subscription.
     *
     * @return The request message or null, if not message must be send.
     */
    abstract AbstractMessage request();

    /**
     * Handles the received message from the Water Rower S4/S5 monitor.
     *
     * @param msg The message from the Water Rower S4/S5 monitor.
     */
    abstract void handle(AbstractMessage msg);


}
