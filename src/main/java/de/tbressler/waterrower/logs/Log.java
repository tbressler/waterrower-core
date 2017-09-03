package de.tbressler.waterrower.logs;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.apache.logging.log4j.MarkerManager.getMarker;

/**
 * @author Tobias Bressler
 * @version 1.0
 */
public class Log {

    public static final Marker SERIAL = getMarker("Serial");

    /* The logger. */
    private static final Logger logger = getLogger();


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
