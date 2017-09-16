package de.tbressler.waterrower;

import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.StrokeType;

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


    /**
     * Will be called, when the rowing computer determined acceleration (start of stroke) or
     * deceleration (end of stroke) occurring in the paddle.
     *
     * @param strokeType The type of stroke (e.g. start or end), never null.
     */
    void onStroke(StrokeType strokeType);

    /**
     * Will be called, when pulse count was updated. The value is representing the number of
     * pulse’s counted during the last 25mS period; this value can range from 1 to 50
     * typically. (Zero values will not be transmitted).
     *
     * @param pulsesCount The number of pulse’s counted during the last 25mS period.
     */
    void onPulseCount(int pulsesCount);

}