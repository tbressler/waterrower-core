package de.tbressler.waterrower.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple singleton for logging.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class Log {

    /** Marker for serial communication logs. */
    public static final String SERIAL = "Serial";
    /** Marker for messages that are send and received via serial communication. */
    public static final String MESSAGES = "Messages";
    /** Marker for library logs. */
    public static final String LIBRARY = "Library";
    /** Marker for auto discovery logs. */
    public static final String DISCOVERY = "Discovery";

    /* The logger. */
    private static final Logger logger = LoggerFactory.getLogger("WaterRowerLibrary");


    /* Private constructor. */
    private Log() {}


    /** Logs debug messages. */
    public static void debug(String marker, String msg) {
        logger.debug(msg);
    }

    /** Logs debug messages. */
    public static void info(String marker, String msg) {
        logger.info(msg);
    }

    /** Logs warning messages. */
    public static void warn(String marker, String msg) {
        logger.warn(msg);
    }

    /** Logs error messages. */
    public static void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

}
