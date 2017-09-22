package de.tbressler.waterrower.io.msg.in;

import de.tbressler.waterrower.model.ModelInformation;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for class ModelInformationMessage.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestModelInformationMessage {

    // Mocks:
    private ModelInformation modelInformation = Mockito.mock(ModelInformation.class, "modelInformation");


    @Test
    public void getModelInformation() throws Exception {
        ModelInformationMessage msg = new ModelInformationMessage(modelInformation);
        assertEquals(modelInformation, msg.getModelInformation());
    }

    @Test
    public void toString_returnsObjectInfo() {
        ModelInformationMessage msg = new ModelInformationMessage(modelInformation);
        assertTrue(msg.toString().startsWith("ModelInformationMessage"));
    }

}