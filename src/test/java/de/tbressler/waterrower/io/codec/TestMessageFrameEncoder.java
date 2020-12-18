package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for class MessageFrameEncoder.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMessageFrameEncoder {

    /* Class under test. */
    private MessageFrameEncoder messageFrameEncoder;

    // Mocks:
    private MessageParser parser = mock(MessageParser.class, "parser");
    private ChannelHandlerContext ctx = mock(ChannelHandlerContext.class, "ctx");


    @Before
    public void setUp() {
        messageFrameEncoder = new MessageFrameEncoder(parser);
    }


    /**
     * Checks if a NPE is thrown, when null is given to the constructor.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullParser_throwsException() {
        new MessageFrameEncoder(null);
    }


    /**
     * Checks if an IllegalArgumentException is thrown, if an invalid message type is given.
     */
    @Test(expected = IllegalArgumentException.class)
    public void encode_withInvalidMessage_throwsException() throws Exception {
        ByteBuf out = Unpooled.directBuffer();
        messageFrameEncoder.encode(ctx, "invalid-message-type", out);
    }

    /**
     * Checks if a message will be decoded to correct bytes.
     */
    @Test
    public void encode_withValidStartCommunicationMessage_returnsCorrectBytes() throws Exception {
        when(parser.encode(any(StartCommunicationMessage.class))).thenReturn("USB".getBytes());

        ByteBuf out = Unpooled.buffer();
        messageFrameEncoder.encode(ctx, new StartCommunicationMessage(), out);

        assertTrue(new String(out.array()).startsWith("USB\r\n"));
    }

    /**
     * Checks if no message is written, if the message couldn't be parsed.
     */
    @Test
    public void encode_whenMessageCantBeParsed_dontWriteToBuffer() throws Exception {
        when(parser.encode(any(StartCommunicationMessage.class))).thenReturn(null);

        ByteBuf out = Unpooled.buffer();
        messageFrameEncoder.encode(ctx, new StartCommunicationMessage(), out);

        assertFalse(new String(out.array()).contains("\r\n"));
    }

}
