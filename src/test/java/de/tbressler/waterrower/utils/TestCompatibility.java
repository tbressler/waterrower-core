package de.tbressler.waterrower.utils;

import de.tbressler.waterrower.model.ModelInformation;
import org.junit.Test;

import static de.tbressler.waterrower.model.MonitorType.*;
import static de.tbressler.waterrower.utils.Compatibility.isSupportedWaterRower;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class Compatibility.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestCompatibility {

    @Test(expected = NullPointerException.class)
    public void isSupportedWaterRower_withNullModelInformation_throwsException() {
        isSupportedWaterRower(null);
    }

    @Test
    public void isSupportedWaterRower_withUnknownMonitorType_returnsFalse() {
        ModelInformation mi = new ModelInformation(UNKNOWN_MONITOR_TYPE, "02.00");
        assertFalse(isSupportedWaterRower(mi));
    }

    @Test
    public void isSupportedWaterRower_withS4AndFirmwareVersion0200_returnsTrue() {
        ModelInformation mi = new ModelInformation(WATER_ROWER_S4, "02.00");
        assertTrue(isSupportedWaterRower(mi));
    }

    @Test
    public void isSupportedWaterRower_withS5AndFirmwareVersion0200_returnsTrue() {
        ModelInformation mi = new ModelInformation(WATER_ROWER_S5, "02.00");
        assertTrue(isSupportedWaterRower(mi));
    }

    @Test
    public void isSupportedWaterRower_withS4AndFirmwareVersion0100_returnsFalse() {
        ModelInformation mi = new ModelInformation(WATER_ROWER_S4, "01.00");
        assertFalse(isSupportedWaterRower(mi));
    }

}
