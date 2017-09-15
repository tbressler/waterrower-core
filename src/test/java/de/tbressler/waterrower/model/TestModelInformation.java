package de.tbressler.waterrower.model;

import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.model.MonitorType.WATER_ROWER_S4;
import static de.tbressler.waterrower.model.MonitorType.WATER_ROWER_S5;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class ModelInformation.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestModelInformation {

    /* Class under test. */
    private ModelInformation modelInformation;


    @Before
    public void setUp() {
        modelInformation = new ModelInformation(WATER_ROWER_S4, "02.00");
    }


    @Test(expected = NullPointerException.class)
    public void new_withNullMonitorType_throwsException() {
        new ModelInformation(null, "03.00");
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullFirmwareVersion_throwsException() {
        new ModelInformation(WATER_ROWER_S5, null);
    }

    @Test
    public void getMonitorType_returnsMonitorType() {
        assertEquals(WATER_ROWER_S4, modelInformation.getMonitorType());
    }

    @Test
    public void getFirmwareVersion_returnsFirmwareVersion() {
        assertEquals("02.00", modelInformation.getFirmwareVersion());
    }

    @Test
    public void toString_returnsObjectInfo() {
        assertTrue(modelInformation.toString().startsWith("ModelInformation"));
    }

}
