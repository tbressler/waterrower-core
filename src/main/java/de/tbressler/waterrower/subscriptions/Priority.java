package de.tbressler.waterrower.subscriptions;

/**
 * The priority of the subscription.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public enum Priority {

    /** The subscription will be polled every cycle. */
    HIGH,

    /** The subscription will be polled every 2nd polling cycle. */
    MEDIUM,

    /** The subscription will be polled every 5th polling cycle. */
    LOW,

    /** This subscription must not be polled. */
    NO_POLLING

}
