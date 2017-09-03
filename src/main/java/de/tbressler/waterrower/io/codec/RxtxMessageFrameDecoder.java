package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.msg.SerialMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static de.tbressler.waterrower.io.utils.ByteUtils.bufferToString;
import static de.tbressler.waterrower.log.Log.SERIAL;
import static java.util.Objects.requireNonNull;

/**
 * Decodes messages (byte > msg).
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxMessageFrameDecoder extends ByteToMessageDecoder {

    /* The frame delimiter for serial messages from the Water Rower S4/S5 monitor. */
    private final static char DELIMITER = 0x0D0A;

    /* The message parser. */
    private final RxtxMessageParser parser;


    /**
     * Constructor.
     *
     * @param parser The message parser, must not be null.
     */
    public RxtxMessageFrameDecoder(RxtxMessageParser parser) {
        this.parser = requireNonNull(parser);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int startPosition = in.readerIndex();
        int numberOfBytes = in.readableBytes();

        Log.debug(SERIAL, "Decoder received new message buffer:\n" +
                " Buffer: " + bufferToString(in));

        // Check if bytes are available.
        if (numberOfBytes == 0) {
            Log.warn(SERIAL, "No bytes in message buffer!");
            return;
        }

        int length = 0;
        while (in.readableBytes() > 0) {

            length++;

            if (in.readChar() != DELIMITER)
                continue; // Delimiter not found.

            if (in.readerIndex() == startPosition)
                return; // Only the delimiter was found.

            byte [] byteArray = new byte[in.readerIndex() - 1 - startPosition];

            in.readerIndex(startPosition);

            in.readBytes(byteArray, 0, length - 1);
            in.readChar(); // Read delimiter from byte-stream.

            Log.debug(SERIAL, "Message buffer decoded to: >" + new String(byteArray) + "<");

            if (byteArray.length == 0) {
                Log.debug(SERIAL, "No payload! Maybe only the delimiter was received.");
                return;
            }

            // Decode the message.
            SerialMessage decodedMessage = parser.decode(byteArray);
            if (decodedMessage == null) {
                Log.debug(SERIAL, "Couldn't decode bytes to message! Skipping it.");
                return;
            }

            out.add(decodedMessage);

            return;
        }

        in.readerIndex(startPosition);

        Log.debug(SERIAL, "No delimiter found in message buffer. Waiting for next frame.\n" +
                " Buffer set to: " + bufferToString(in));
    }

}
