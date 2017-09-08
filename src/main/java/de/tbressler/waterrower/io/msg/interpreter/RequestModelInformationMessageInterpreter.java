package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.io.msg.out.RequestModelInformationMessage;

/**
 * Interpreter for:
 *
 * Request Model Information (PC -> S4/S5).
 *
 * Request details from the rowing computer on what it is and firmware version.
 *
 * [I][V?] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RequestModelInformationMessageInterpreter extends AbstractMessageInterpreter<RequestModelInformationMessage> {

    @Override
    public String getMessageTypeChar() {
        return null;
    }

    @Override
    public Class<RequestModelInformationMessage> getMessageType() {
        return RequestModelInformationMessage.class;
    }

    @Override
    public RequestModelInformationMessage decode(byte[] bytes) {
        throw new IllegalStateException("This type of message should not be send by Water Rower S4/S5 monitor to the PC.");
    }

    @Override
    public byte[] encode(RequestModelInformationMessage msg) {
        return new String("IV?").getBytes();
    }

}
