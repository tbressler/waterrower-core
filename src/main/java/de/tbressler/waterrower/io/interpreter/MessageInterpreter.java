package de.tbressler.waterrower.io.interpreter;

import de.tbressler.waterrower.msg.SerialMessage;

/**
 * Abstract message interpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class MessageInterpreter<T extends SerialMessage> implements IMessageInterpreter<T> {

}
