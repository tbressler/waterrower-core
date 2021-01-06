package de.tbressler.waterrower.log;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.apache.logging.log4j.MarkerManager.getMarker;

/**
 * Simple singleton for logging.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class Log {

    /** Marker for serial communication logs. */
    public static final Marker SERIAL = getMarker("Serial");
    /** Marker for messages that are send and received via serial communication. */
    public static final Marker MESSAGES = getMarker("Messages");
    /** Marker for library logs. */
    public static final Marker LIBRARY = getMarker("Library");
    /** Marker for auto discovery logs. */
    public static final Marker DISCOVERY = getMarker("Discovery");

    /* The logger. */
    private static final Logger logger = getLogger();


    /* Private constructor. */
    private Log() {}


    /** Logs debug messages. */
    public static void debug(Marker marker, String msg) {
        logger.debug(marker, msg);
    }

    /** Logs warning messages. */
    public static void warn(Marker marker, String msg) {
        logger.warn(marker, msg);
    }

    /** Logs error messages. */
    public static void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

}
