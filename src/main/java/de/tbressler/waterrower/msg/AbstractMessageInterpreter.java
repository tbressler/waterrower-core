package de.tbressler.waterrower.msg;

/**
 * Abstract message interpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class AbstractMessageInterpreter<T extends AbstractMessage> implements IMessageInterpreter<T> {}