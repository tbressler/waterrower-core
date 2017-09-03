package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.in.ErrorMessage;

/**
 * A parser for message type: ErrorMessage
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ErrorMessageInterpreter extends AbstractMessageInterpreter<ErrorMessage> {

    /* Single instance of an error message. */
    private ErrorMessage ERROR_MESSAGE = new ErrorMessage();


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
        if (bytes.length < 5)
            return null;
        return ERROR_MESSAGE;
    }

    @Override
    public byte[] encode(ErrorMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
