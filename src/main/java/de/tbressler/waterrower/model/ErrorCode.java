package de.tbressler.waterrower.model;

/**
 * Simple error codes which identifies the type of error.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public enum ErrorCode {

    /* The device is not supported. */
    ERROR_DEVICE_NOT_SUPPORTED,

    /* IO or connection error. */
    ERROR_COMMUNICATION_FAILED,

    /* Communication timed out (no ping received). */
    ERROR_TIMEOUT

}
