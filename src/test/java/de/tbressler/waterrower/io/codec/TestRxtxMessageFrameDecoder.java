package de.tbressler.waterrower.io.codec;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for class RxtxMessageFrameDecoder.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRxtxMessageFrameDecoder {

    /* Class under test. */
    private RxtxMessageFrameDecoder messageFrameDecoder;

    // Mocks:
    private RxtxMessageParser parser = mock(RxtxMessageParser.class, "parser");
    private ChannelHandlerContext ctx = mock(ChannelHandlerContext.class, "ctx");


    @Before
    public void setUp() {
        messageFrameDecoder = new RxtxMessageFrameDecoder(parser);
    }


    /**
     * Checks if a NPE is thrown, when null is given to the constructor.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullParser_throwsException() {
        new RxtxMessageFrameEncoder(null);
    }

}
