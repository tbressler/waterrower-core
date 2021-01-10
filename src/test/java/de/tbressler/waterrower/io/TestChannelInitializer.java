package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.codec.MessageFrameDecoder;
import de.tbressler.waterrower.io.codec.MessageFrameEncoder;
import de.tbressler.waterrower.io.transport.SerialChannel;
import de.tbressler.waterrower.io.transport.SerialChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.io.transport.SerialChannelConfig.Paritybit.NONE;
import static de.tbressler.waterrower.io.transport.SerialChannelConfig.Stopbits.STOPBITS_1;
import static org.mockito.Mockito.*;

/**
 * Tests for class ChannelInitializer.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestChannelInitializer {

    // Class under test.
    private ChannelInitializer initializer;

    // Mocks:
    private SerialChannel channel = mock(SerialChannel.class, "channel");
    private SerialHandler handler = mock(SerialHandler.class, "handler");
    private SerialChannelConfig config = mock(SerialChannelConfig.class, "config");
    private ChannelPipeline pipeline = mock(ChannelPipeline.class, "pipeline");


    @Before
    public void setUp() throws Exception {
        initializer = new ChannelInitializer();
    }


    @Test(expected = NullPointerException.class)
    public void setSerialHandler_withNull_throwsNPE() throws Exception {
        initializer.setSerialHandler(null);
    }

    @Test(expected = IllegalStateException.class)
    public void initChannel_whenNoSerialHandlerWasSet_throwsException() throws Exception {
        initializer.initChannel(channel);
    }

    @Test
    public void initChannel_configuresSerialChannel() throws Exception {
        when(channel.config()).thenReturn(config);
        when(config.getBaudrate()).thenReturn(19200);
        when(config.getDatabits()).thenReturn(8);
        when(config.getStopbits()).thenReturn(STOPBITS_1);
        when(config.getParitybit()).thenReturn(NONE);
        when(channel.pipeline()).thenReturn(pipeline);
        initializer.setSerialHandler(handler);

        initializer.initChannel(channel);

        // Check if serial config is correct:
        verify(config, times(1)).setBaudrate(19200);
        verify(config, times(1)).setDatabits(8);
        verify(config, times(1)).setStopbits(STOPBITS_1);
        verify(config, times(1)).setParitybit(NONE);

        // Check if encoders, decoders and handlers are set:
        verify(pipeline, times(1)).addLast(eq("framer"), any(DelimiterBasedFrameDecoder.class));
        verify(pipeline, times(1)).addLast(eq("decoder"), any(MessageFrameDecoder.class));
        verify(pipeline, times(1)).addLast(eq("encoder"), any(MessageFrameEncoder.class));
        verify(pipeline, times(1)).addLast(eq("handler"), eq(handler));
    }

}