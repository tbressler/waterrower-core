package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.in.ErrorMessage;

/**
 * @author Tobias Bressler
 * @version 1.0
 */
public class ErrorMessageInterpreter extends AbstractMessageInterpreter<ErrorMessage> {

    @Override
    public char getMessageTypeByte() {
        return 'E';
    }

    @Override
    public Class<ErrorMessage> getMessageType() {
        return ErrorMessage.class;
    }

    @Override
    public ErrorMessage decode(byte[] bytes) {
        return null;
    }

    @Override
    public byte[] encode(ErrorMessage msg) {
        return new byte[0];
    }

}
