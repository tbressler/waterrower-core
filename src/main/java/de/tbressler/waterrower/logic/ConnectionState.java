package de.tbressler.waterrower.logic;

/**
 * The different connection states.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public enum ConnectionState {

    /* The device is not connected. */
    NOT_CONNECTED,

    /* The device is currently connecting. */
    CONNECTING,

    /* Connected to a device. The identity is currently unknown. */
    CONNECTED_WITH_UNKNOWN_DEVICE,

    /* Successfully connected to a Water Rower. */
    CONNECTED_WITH_WATER_ROWER,

    /* Successfully connected to a Water Rower with correct firmware. */
    CONNECTED_WITH_SUPPORTED_WATER_ROWER,

    /* The device is currently disconnecting. */
    DISCONNECTING

}