package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.codec.RxtxMessageFrameDecoder;
import de.tbressler.waterrower.io.codec.RxtxMessageFrameEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.junit.Before;
import org.junit.Test;

import static io.netty.channel.rxtx.RxtxChannelConfig.Databits.DATABITS_8;
import static io.netty.channel.rxtx.RxtxChannelConfig.Paritybit.NONE;
import static io.netty.channel.rxtx.RxtxChannelConfig.Stopbits.STOPBITS_1;
import static org.mockito.Mockito.*;

/**
 * Tests for class RxtxChannelInitializer.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestRxtxChannelInitializer {

    // Class under test.
    private RxtxChannelInitializer initializer;

    // Mocks:
    private RxtxChannel channel = mock(RxtxChannel.class, "channel");
    private RxtxSerialHandler handler = mock(RxtxSerialHandler.class, "handler");
    private RxtxChannelConfig config = mock(RxtxChannelConfig.class, "config");
    private ChannelPipeline pipeline = mock(ChannelPipeline.class, "pipeline");


    @Before
    public void setUp() throws Exception {
        initializer = new RxtxChannelInitializer();
    }


    @Test(expected = NullPointerException.class)
    public void setRxTxSerialHandler_withNull_throwsNPE() throws Exception {
        initializer.setRxTxSerialHandler(null);
    }

    @Test(expected = IllegalStateException.class)
    public void initChannel_whenNoRxtxSerialHandlerWasSet_throwsException() throws Exception {
        initializer.initChannel(channel);
    }

    @Test
    public void initChannel_configuresSerialChannel() throws Exception {
        when(channel.config()).thenReturn(config);
        when(config.getBaudrate()).thenReturn(19200);
        when(config.getDatabits()).thenReturn(DATABITS_8);
        when(config.getStopbits()).thenReturn(STOPBITS_1);
        when(config.getParitybit()).thenReturn(NONE);
        when(channel.pipeline()).thenReturn(pipeline);
        initializer.setRxTxSerialHandler(handler);

        initializer.initChannel(channel);

        // Check if serial config is correct:
        verify(config, times(1)).setBaudrate(19200);
        verify(config, times(1)).setDatabits(DATABITS_8);
        verify(config, times(1)).setStopbits(STOPBITS_1);
        verify(config, times(1)).setParitybit(NONE);

        // Check if encoders, decoders and handlers are set:
        verify(pipeline, times(1)).addLast(eq("framer"), any(DelimiterBasedFrameDecoder.class));
        verify(pipeline, times(1)).addLast(eq("decoder"), any(RxtxMessageFrameDecoder.class));
        verify(pipeline, times(1)).addLast(eq("encoder"), any(RxtxMessageFrameEncoder.class));
        verify(pipeline, times(1)).addLast(eq("handler"), eq(handler));
        verify(pipeline, never()).addLast(eq("exceptions"), any(RxtxExceptionHandler.class));
    }

}