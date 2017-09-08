package de.tbressler.waterrower.io.msg;


/**
 * Interface for message interpreters, which decode or encode incoming and outgoing messages.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public interface IMessageInterpreter<T extends AbstractMessage> {

    /**
     * Returns the identifier of the message type which this interpreter can decode. Can be null if no incoming
     * messages of this type are expected.
     *
     * @return The identifier char or null.
     */
    String getMessageTypeChar();

    /**
     * Returns the type of message which this interpreter can encode.
     *
     * @return The message type.
     */
    Class<T> getMessageType();

    /**
     * Decodes the given byte array to a message object. If the message can not be decoded the method returns null.
     *
     * @param bytes The message byte array.
     * @return The message object or null.
     */
    T decode(byte[] bytes);

    /**
     * Encodes the given message object to a byte array. If the message can not be encoded the method returns null.
     *
     * @param msg The message object.
     * @return The byte array or null.
     */
    byte[] encode(T msg);

}
