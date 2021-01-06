package de.tbressler.waterrower.discovery;

/**
 * Interface for storing the last successful serial port.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public interface IDiscoveryStore {

    /**
     * Store the latest successful serial port.
     *
     * @param serialPort The identifier of the serial port.
     */
    void setLastSuccessfulSerialPort(String serialPort);

    /**
     * Returns the latest successful serial port that was
     * stored before.
     *
     * @return The identifier of the serial port.
     */
    String getLastSuccessfulSerialPort();

}
