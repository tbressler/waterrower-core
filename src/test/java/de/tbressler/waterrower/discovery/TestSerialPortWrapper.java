package de.tbressler.waterrower.discovery;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for class SerialPortWrapper.
 *
 * @author Tobias Breßler
 * @version 1.0
 */
public class TestSerialPortWrapper {

    @Test
    public void getCommPorts() {
        assertNotNull(new SerialPortWrapper().getAvailablePorts());
    }

}