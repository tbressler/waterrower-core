package de.tbressler.waterrower.utils;

import com.fazecast.jSerialComm.SerialPort;

import static java.util.Objects.requireNonNull;

/**
 * Wrapper for the SerialPort implementation.
 * This wrapper is used in the unit tests.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public class AvailablePort {

    /* The underlying jSerialComm port. */
    private SerialPort serialPort;


    /**
     * Wrapper for the SerialPort implementation.
     *
     * @param serialPort The underlying jSerialComm port, must not be null.
     */
    public AvailablePort(SerialPort serialPort) {
        this.serialPort = requireNonNull(serialPort);
    }

    /**
     * The system name of the port.
     *
     * @return The name of the port.
     */
    public String getSystemPortName() {
        return serialPort.getSystemPortName();
    }

    /**
     * The description of the port.
     *
     * @return The description of the port.
     */
    public String getDescription() {
        return serialPort.getPortDescription();
    }

    /**
     * Returns true if the port is open.
     *
     * @return True if the port is open.
     */
    public boolean isOpen() {
        return serialPort.isOpen();
    }

}
