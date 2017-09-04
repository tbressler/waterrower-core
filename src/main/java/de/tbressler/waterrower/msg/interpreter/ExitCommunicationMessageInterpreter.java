package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.out.ExitCommunicationMessage;

/**
 * Interpreter for:
 *
 * Application is exiting (PC -> S4/S5).
 *
 * Any application wishing to normally terminate (close) is required to send this packet to stop
 * the automatic packets being sent to the PC.
 *
 * [E][XIT] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ExitCommunicationMessageInterpreter extends AbstractMessageInterpreter<ExitCommunicationMessage> {

    @Override
    public String getMessageTypeChar() {
        return null;
    }

    @Override
    public Class<ExitCommunicationMessage> getMessageType() {
        return ExitCommunicationMessage.class;
    }

    @Override
    public ExitCommunicationMessage decode(byte[] bytes) {
        throw new IllegalStateException("This type of message should not be send by Water Rower S4/S5 monitor to the PC.");
    }

    @Override
    public byte[] encode(ExitCommunicationMessage msg) {
        return new String("EXIT").getBytes();
    }

}
