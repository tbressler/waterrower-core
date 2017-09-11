package de.tbressler.waterrower;

import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;

/**
 * Interface for Water Rower listeners.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public interface IWaterRowerListener {

    /**
     * Will be called, if a supported Water Rower monitor was connected.
     *
     * @param modelInformation Model information (e.g. monitor type and firmware version).
     */
    void onConnected(ModelInformation modelInformation);

    /**
     * Will be called, if the Water Rower monitor was disconnected.
     */
    void onDisconnected();

    /**
     * Will be called, if an error occurred while communicating with Water Rower monitor.
     *
     * @param errorCode The error code.
     */
    void onError(ErrorCode errorCode);

}