package de.tbressler.waterrower.io.msg.in;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.model.MonitorType;

import static java.util.Objects.requireNonNull;

/**
 * Current Model Information (S4/S5 -> PC).
 *
 * Details of what unit is attached:
 * - Model - Sent as 4 or 5 to indicate if it is a Series 4 or series 5 rowing computer.
 * - Version high - 02 as an example for version 2.00 MSB of the firmware version.
 * - Version low - 00 as an example for version 2.00 LSB of the firmware version.
 *
 * [I][V] + [Model] + [Version High] + [Version Low] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ModelInformationMessage extends AbstractMessage {

    /* The type of monitor (e.g. S4 or S5). */
    private final MonitorType monitorType;

    /* The firmware version of the monitor */
    private final String firmwareVersion;


    /**
     * Current model information.
     *
     * @param monitorType The type of monitor (e.g. S4 or S5), must not be null.
     * @param firmwareVersion The firmware version of the monitor, must not be null.
     */
    public ModelInformationMessage(MonitorType monitorType, String firmwareVersion) {
        this.monitorType = requireNonNull(monitorType);
        this.firmwareVersion = requireNonNull(firmwareVersion);
    }


    /**
     * Returns the type of monitor (e.g. S4 or S5).
     *
     * @return The type of monitor, never null.
     */
    public MonitorType getMonitorType() {
        return monitorType;
    }


    /**
     * The firmware version of the monitor.
     *
     * @return The firmware version of the monitor, never null.
     */
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

}
