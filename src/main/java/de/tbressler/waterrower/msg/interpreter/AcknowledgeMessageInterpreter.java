package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.in.AcknowledgeMessage;

/**
 * A parser for message type: AcknowledgeMessage
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class AcknowledgeMessageInterpreter extends AbstractMessageInterpreter<AcknowledgeMessage> {

    /* Single instance of an acknowledge message. */
    private AcknowledgeMessage ACKNOWLEDGE_MESSAGE = new AcknowledgeMessage();


    @Override
    public char getMessageTypeByte() {
        return 'O';
    }

    @Override
    public Class<AcknowledgeMessage> getMessageType() {
        return AcknowledgeMessage.class;
    }

    @Override
    public AcknowledgeMessage decode(byte[] bytes) {
        if (bytes.length < 2)
            return null;
        return ACKNOWLEDGE_MESSAGE;
    }

    @Override
    public byte[] encode(AcknowledgeMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
