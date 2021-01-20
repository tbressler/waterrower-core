package de.tbressler.waterrower.subscriptions;

/**
 * The priority of a subscription.
 *
 * The priority determines in which interval the subscription is polled by
 * the subscription polling serve.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public enum Priority {

    /** The subscription will be polled every polling cycle. */
    HIGH,

    /** The subscription will be polled every 2nd polling cycle. */
    MEDIUM,

    /** The subscription will be polled every 5th polling cycle. */
    LOW,

    /** This subscription must not be polled. */
    NO_POLLING

}
