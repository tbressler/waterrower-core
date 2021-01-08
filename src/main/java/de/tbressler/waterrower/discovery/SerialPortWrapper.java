package de.tbressler.waterrower.discovery;

import com.fazecast.jSerialComm.SerialPort;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * A wrapper for the serial port class of jSerialComm.
 * This wrapper is used in the unit tests.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public class SerialPortWrapper {

    public List<AvailablePort> getAvailablePorts() {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        return asList(serialPorts).stream().map((port) -> new AvailablePort(port)).collect(toList());
    }

}
