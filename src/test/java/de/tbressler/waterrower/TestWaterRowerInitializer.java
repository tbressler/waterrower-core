package de.tbressler.waterrower;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for class WaterRowerInitializer.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWaterRowerInitializer {

    // Class under test.
    private WaterRowerInitializer initializer;


    @BeforeEach
    public void setUp() throws Exception {
        initializer = new WaterRowerInitializer(ofSeconds(2), 5);
    }

    // Constructor:

    @Test
    public void new_withNullTimeoutInterval_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new WaterRowerInitializer(null, 5));
    }

    @Test
    public void new_withNullThreadPool0_throwsNPE() {
        assertThrows(IllegalArgumentException.class, () -> new WaterRowerInitializer(ofSeconds(1), 0));
    }

    // Methods:

    @Test
    public void getWaterRowerConnector_returnsNotNull() throws Exception {
        assertNotNull(initializer.getWaterRowerConnector());
    }

    @Test
    public void getPingWatchdog_returnsNotNull() throws Exception {
        assertNotNull(initializer.getPingWatchdog());
    }

    @Test
    public void getDeviceVerificationWatchdog_returnsNotNull() throws Exception {
        assertNotNull(initializer.getDeviceVerificationWatchdog());
    }

    @Test
    public void getSubscriptionPollingService_returnsNotNull() throws Exception {
        assertNotNull(initializer.getSubscriptionPollingService());
    }

}