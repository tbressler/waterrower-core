package de.tbressler.waterrower.discovery;

import com.fazecast.jSerialComm.SerialPort;

/**
 * A wrapper for the serial port class of jSerialComm.
 * This wrapper is used in the unit tests.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public class SerialPortWrapper {

    public SerialPort[] getCommPorts() {
        return SerialPort.getCommPorts();
    }

}
