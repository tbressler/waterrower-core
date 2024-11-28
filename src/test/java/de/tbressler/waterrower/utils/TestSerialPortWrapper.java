package de.tbressler.waterrower.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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