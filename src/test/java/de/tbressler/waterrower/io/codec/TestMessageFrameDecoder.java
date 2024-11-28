package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.*;

/**
 * Tests for class MessageFrameDecoder.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMessageFrameDecoder {

    /* Class under test. */
    private MessageFrameDecoder messageFrameDecoder;

    private List<Object> out = new ArrayList<>();

    // Mocks:
    private MessageParser parser = mock(MessageParser.class, "parser");
    private ChannelHandlerContext ctx = mock(ChannelHandlerContext.class, "ctx");

    private AbstractMessage message1 = mock(AbstractMessage.class, "message1");
    private AbstractMessage message2 = mock(AbstractMessage.class, "message2");


    @BeforeEach
    public void setUp() {
        messageFrameDecoder = new MessageFrameDecoder(parser);
    }


    // Constructor:

    @Test
    public void new_withNullParser_throwsException() {
        Assertions.assertThrows(NullPointerException.class, () -> new MessageFrameEncoder(null));
    }

    // Decode:

    @Test
    public void decode_withFrameAndReaderPosition0AndSuccessfulDecoding_outputsMessage() throws Exception {

        when(parser.decode(bytesEq("MESSAGE1"))).thenReturn(message1);

        ByteBuf in = newBuffer("MESSAGE1");

        messageFrameDecoder.decode(ctx, in, out);

        verify(parser, times(1)).decode(bytesEq("MESSAGE1"));
        assertTrue(out.contains(message1));
    }

    @Test
    public void decode_withFrameAndReaderPosition1AndSuccessfulDecoding_outputsMessage() throws Exception {

        when(parser.decode(bytesEq("MESSAGE1"))).thenReturn(message1);

        ByteBuf in = newBuffer("XMESSAGE1");
        in.readByte();

        messageFrameDecoder.decode(ctx, in, out);

        verify(parser, times(1)).decode(bytesEq("MESSAGE1"));
        assertTrue(out.contains(message1));
    }

    @Test
    public void decode_withFrameAndDecodeFails_doesntOutputMessage() throws Exception {

        when(parser.decode(bytesEq("MESSAGE1"))).thenReturn(null);

        ByteBuf in = newBuffer("MESSAGE1");

        messageFrameDecoder.decode(ctx, in, out);

        verify(parser, times(1)).decode(bytesEq("MESSAGE1"));
        assertTrue(out.isEmpty());
    }

    @Test
    public void decode_withEmptyFrameAndDecodeFails_doesntOutputMessage() throws Exception {

        ByteBuf in = newBuffer("");

        messageFrameDecoder.decode(ctx, in, out);

        verify(parser, never()).decode(any());
        assertTrue(out.isEmpty());
    }

    // Helper methods:

    private ByteBuf newBuffer(String content) {
        return wrappedBuffer(content.getBytes(UTF_8));
    }

    private byte[] bytesEq(String message) {
        return aryEq(message.getBytes(UTF_8));
    }

}
