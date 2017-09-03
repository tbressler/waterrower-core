package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.in.PingMessage;

/**
 * A parser for message type: PingMessage
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class PingMessageInterpreter extends AbstractMessageInterpreter<PingMessage> {

    /* Single instance of an acknowledge message. */
    private PingMessage PING_MESSAGE = new PingMessage();


    @Override
    public char getMessageTypeByte() {
        return 'P';
    }

    @Override
    public Class<PingMessage> getMessageType() {
        return PingMessage.class;
    }

    @Override
    public PingMessage decode(byte[] bytes) {
        if (bytes.length < 4)
            return null;
        return PING_MESSAGE;
    }

    @Override
    public byte[] encode(PingMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
