package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.io.msg.in.PingMessage;

/**
 * Interpreter for:
 *
 * Ping (S4/S5 -> PC).
 *
 * Sent once a second while NO rowing is occurring to indicate to the PC the rowing monitor is
 * still operational but stopped.
 *
 * [P][ING] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class PingMessageInterpreter extends AbstractMessageInterpreter<PingMessage> {

    /* Single instance of an acknowledge message. */
    private PingMessage PING_MESSAGE = new PingMessage();


    @Override
    public String getMessageTypeChar() {
        return "P";
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
