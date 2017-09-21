package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.google.common.base.Charsets.UTF_8;
import static de.tbressler.waterrower.io.utils.ByteUtils.bytesToHex;
import static de.tbressler.waterrower.log.Log.MESSAGES;
import static de.tbressler.waterrower.log.Log.SERIAL;
import static java.util.Objects.requireNonNull;

/**
 * Encodes messages (msg > byte).
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxMessageFrameEncoder extends MessageToByteEncoder {

    /* The frame delimiter for serial messages from the WaterRower S4/S5 monitor. */
    private final static char DELIMITER = 0x0D0A;

    /* The message parser. */
    private final RxtxMessageParser parser;


    /**
     * Constructor.
     *
     * @param parser The message parser, must not be null.
     */
    public RxtxMessageFrameEncoder(RxtxMessageParser parser) {
        this.parser = requireNonNull(parser);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (!(msg instanceof AbstractMessage)) {
            Exception e = new IllegalArgumentException("This type of message can not be send! " +
                    "Only messages of type >"+AbstractMessage.class.getSimpleName()+"< can be send.");
            Log.error("Message couldn't be send to serial device!", e);
            throw e;
        }

        // Parse the message:
        byte[] byteArray = parser.encode((AbstractMessage) msg);
        if (byteArray == null) {
            Log.warn(SERIAL, "Message couldn't been encoded! Skipped message.");
            return;
        }

        // Write bytes to channel.
        out.writeBytes(byteArray);
        out.writeChar(DELIMITER);

        Log.debug(MESSAGES, "Message buffer encoded and written:\n" +
                " As String: >" + new String(byteArray, UTF_8) + "<\n" +
                " As HEX: "+ bytesToHex(byteArray));

        ctx.flush();
    }

}
