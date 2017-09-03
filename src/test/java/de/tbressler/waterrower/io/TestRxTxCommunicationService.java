package de.tbressler.waterrower.io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for class RxTxCommunicationService.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRxTxCommunicationService {

    /* Class under test. */
    private RxTxCommunicationService rxTxCommunicationService;


    @Before
    public void setUp() {
        rxTxCommunicationService = new RxTxCommunicationService();
    }


    /**
     * Checks if getSerialPorts returns a non null list.
     */
    @Test
    public void getSerialPorts_returnsNonNull() {
        assertTrue(rxTxCommunicationService.getSerialPorts() != null);
    }

}
