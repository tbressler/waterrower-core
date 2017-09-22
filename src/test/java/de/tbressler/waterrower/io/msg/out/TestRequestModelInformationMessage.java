package de.tbressler.waterrower.io.msg.out;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for class RequestModelInformationMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRequestModelInformationMessage {

    @Test
    public void toString_returnsObjectInfo() {
        RequestModelInformationMessage msg = new RequestModelInformationMessage();
        assertTrue(msg.toString().startsWith("RequestModelInformationMessage"));
    }

}