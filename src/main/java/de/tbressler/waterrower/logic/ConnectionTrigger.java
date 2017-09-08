package de.tbressler.waterrower.logic;

/**
 * @author Tobias Bressler
 * @version 1.0
 */
public enum ConnectionTrigger {

    /* Application or user triggered a connect. */
    DO_CONNECT,

    /* RXTX has connected. */
    ON_CONNECTED,

    /* Device sent _WR_ message. */
    WATER_ROWER_CONFIRMED,

    /* Firmware of Water Rower is correct. */
    FIRMWARE_CONFIRMED,

    /* RXTX has disconnected. */
    ON_DISCONNECTED,

    /* Application or user triggered a disconnect. */
    DO_DISCONNECT,

    /* An error occurred. */
    ON_ERROR,

    /* */
    ON_WATCHDOG

}
