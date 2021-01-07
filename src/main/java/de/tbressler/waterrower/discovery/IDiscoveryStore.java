package de.tbressler.waterrower.discovery;

/**
 * Interface for storing the last successful serial port.
 *
 * This interface can be used to improve the performance of the auto-discovery by
 * storing the last successful serial port. This port will be tested first the next time the
 * auto-discovery is started.
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
