package de.tbressler.waterrower.io.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for class RxtxMessageFrameEncoder.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRxtxMessageFrameEncoder {

    /* Class under test. */
    private RxtxMessageFrameEncoder messageFrameEncoder;

    // Mocks:
    private RxtxMessageParser parser = mock(RxtxMessageParser.class, "parser");
    private ChannelHandlerContext ctx = mock(ChannelHandlerContext.class, "ctx");


    @Before
    public void setUp() {
        messageFrameEncoder = new RxtxMessageFrameEncoder(parser);
    }


    /**
     * Checks if a NPE is thrown, when null is given to the constructor.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullParser_throwsException() {
        new RxtxMessageFrameEncoder(null);
    }


    /**
     * Checks if an IllegalArgumentException is thrown, if an invalid message type is given.
     */
    @Test(expected = IllegalArgumentException.class)
    public void encode_withInvalidMessage_throwsException() throws Exception {
        ByteBuf out = Unpooled.buffer();
        messageFrameEncoder.encode(ctx, new String("invalid-message-type"), out);
    }

}
