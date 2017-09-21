package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.IMessageInterpreter;
import de.tbressler.waterrower.io.msg.interpreter.*;
import de.tbressler.waterrower.log.Log;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static de.tbressler.waterrower.log.Log.SERIAL;

/**
 * Decodes and encodes messages received from or sent to the Water Rower S4/S5 monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxMessageParser {

    /* List of message interpreters. */
    private List<IMessageInterpreter> interpreters = new ArrayList<>();


    /**
     * Constructor.
     */
    public RxtxMessageParser() {
        createAndAddMessageInterpreters();
    }

    /* Adds message interpreters to this parser. */
    private void createAndAddMessageInterpreters() {

        // Information poll messages:
        interpreters.add(new InformationRequestMessageInterpreter());
        interpreters.add(new ConfigureWorkoutMessageInterpreter());
        interpreters.add(new PingMessageInterpreter());
        interpreters.add(new StrokeMessageInterpreter());
        interpreters.add(new AcknowledgeMessageInterpreter());
        interpreters.add(new ErrorMessageInterpreter());
        interpreters.add(new PulseCountMessageInterpreter());
        interpreters.add(new HardwareTypeMessageInterpreter());
        interpreters.add(new ExitCommunicationMessageInterpreter());
        interpreters.add(new ResetMessageInterpreter());
        interpreters.add(new StartCommunicationMessageInterpreter());
    }


    /**
     * Decodes the given byte array to a message object. Returns null if the message
     * couldn't be decoded.
     *
     * @param bytes The byte array.
     * @return The message object or null.
     */
    public AbstractMessage decode(byte[] bytes) {

        Log.debug(SERIAL, "Parsing message to object.");

        String msg = new String(bytes, UTF_8);

        String msgIdentifier;
        for (IMessageInterpreter interpreter : interpreters) {

            // Check message identifiers:
            msgIdentifier = interpreter.getMessageIdentifier();
            if (msgIdentifier == null)
                continue;
            if (!msg.startsWith(msgIdentifier))
                continue;

            // Decode message to an object:
            AbstractMessage decodedMsg = interpreter.decode(msg);

            if (decodedMsg != null)
                return decodedMsg;
        }

        Log.warn(SERIAL, "Message couldn't be decoded! Unknown message '"+ msg +"'.");

        return null;
    }


    /**
     * Encodes the given message to a byte array. Returns null if the message
     * couldn't be encoded.
     *
     * @param msg The message.
     * @return The byte array or null.
     */
    public byte[] encode(AbstractMessage msg) {

        Log.debug(SERIAL, "Parsing message '"+msg.toString()+"' to bytes.");

        for (IMessageInterpreter interpreter : interpreters) {

            // Check if message type matches:
            if (msg.getClass().equals(interpreter.getMessageType()))
                continue;

            // Encode object to message:
            String encodedMsg = interpreter.encode(msg);

            if (encodedMsg != null)
                return encodedMsg.getBytes(UTF_8);
        }

        Log.warn(SERIAL, "Message couldn't be encoded! Unknown message type '"+msg.getClass().getName()+"'.");

        return null;
    }

}
