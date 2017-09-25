package de.tbressler.waterrower;

import org.junit.Before;
import org.junit.Test;

import static java.time.Duration.ofSeconds;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for class WaterRowerInitializer.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWaterRowerInitializer {

    // Class under test.
    private WaterRowerInitializer initializer;


    @Before
    public void setUp() throws Exception {
        initializer = new WaterRowerInitializer(ofSeconds(1), ofSeconds(2), 5);
    }

    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new_withNullPollingInterval_throwsNPE() {
        new WaterRowerInitializer(null, ofSeconds(1), 5);
    }

    @Test(expected = NullPointerException.class)
    public void new_withNullTimeoutInterval_throwsNPE() {
        new WaterRowerInitializer(ofSeconds(1), null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withNullThreadPool0_throwsNPE() {
        new WaterRowerInitializer(ofSeconds(1), ofSeconds(1), 0);
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