package de.tbressler.waterrower.discovery;

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

    private SerialPort serialPort;

    public AvailablePort(SerialPort serialPort) {
        this.serialPort = requireNonNull(serialPort);
    }

    public String getSystemPortName() {
        return serialPort.getSystemPortName();
    }

    public boolean isOpen() {
        return serialPort.isOpen();
    }

}
