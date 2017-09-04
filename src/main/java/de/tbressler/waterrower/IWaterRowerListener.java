package de.tbressler.waterrower;

/**
 * Interface for Water Rower listeners.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public interface IWaterRowerListener {

    /**
     * Will be called, if the Water Rower monitor was connected.
     */
    void onConnected();

    /**
     * Will be called, if the Water Rower monitor was disconnected.
     */
    void onDisconnected();

    /**
     * Will be called, if an error occurred while communicating with Water Rower monitor.
     */
    void onError();

}
