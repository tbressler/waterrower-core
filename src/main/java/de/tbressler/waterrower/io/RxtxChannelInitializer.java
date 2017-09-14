package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.codec.RxtxMessageFrameDecoder;
import de.tbressler.waterrower.io.codec.RxtxMessageFrameEncoder;
import de.tbressler.waterrower.io.codec.RxtxMessageParser;
import de.tbressler.waterrower.log.Log;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;

import java.io.IOException;

import static de.tbressler.waterrower.log.Log.SERIAL;
import static io.netty.channel.rxtx.RxtxChannelConfig.Databits.DATABITS_8;
import static io.netty.channel.rxtx.RxtxChannelConfig.Paritybit.NONE;
import static io.netty.channel.rxtx.RxtxChannelConfig.Stopbits.STOPBITS_1;
import static java.util.Objects.requireNonNull;

/**
 * Initializes the RXTX channel and sets up the pipeline for encoding and decoding the messages.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxChannelInitializer extends ChannelInitializer<RxtxChannel> {

    /* The handler of the serial channel. */
    private RxtxSerialHandler serialHandler;

    /* The message parser. */
    private RxtxMessageParser parser = new RxtxMessageParser();


    /**
     * Initializes the RXTX channel and sets up the pipeline for encoding and decoding the messages.
     */
    public RxtxChannelInitializer() {}


    /**
     * Sets the serial handler.
     *
     * @param serialHandler The serial handler, must not be null.
     */
    public void setRxTxSerialHandler(RxtxSerialHandler serialHandler) {
        this.serialHandler = requireNonNull(serialHandler);
    }


    @Override
    protected void initChannel(RxtxChannel channel) throws Exception {
        Log.debug(SERIAL, "RXTX channel initialized. Configuring pipeline and channel...");

        checkIfRxTxSerialHandlerIsSet();

        configureChannel(channel);
        configurePipeline(channel);
    }

    /* Checks if the RXTX serial handler is not null. */
    private void checkIfRxTxSerialHandlerIsSet() throws IOException {
        if (serialHandler == null) {
            IOException exception = new IOException("You forgot to set the serial handler before initializing the channel.");
            Log.error("RXTX channel couldn't be initialized!", exception);
            throw exception;
        }
    }


    /* Configures the channel. */
    private void configureChannel(RxtxChannel channel) {
        RxtxChannelConfig config = channel.config();
        config.setBaudrate(19200);
        config.setDatabits(DATABITS_8);
        config.setStopbits(STOPBITS_1);
        config.setParitybit(NONE);

        logSerialConfiguration(config);
    }

    /* Logs the serial configuration. */
    private void logSerialConfiguration(RxtxChannelConfig config) {
        Log.debug(SERIAL, "RXTX channel configured to: " +
                "\n Baudrate: " + config.getBaudrate() +
                "\n Databits: " + config.getDatabits().name() +
                "\n Stopbits: " + config.getStopbits().name() +
                "\n Parity: " + config.getParitybit());
    }

    /* Configures the pipeline. */
    private void configurePipeline(RxtxChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("decoder", new RxtxMessageFrameDecoder(parser));
        pipeline.addLast("encoder", new RxtxMessageFrameEncoder(parser));
        pipeline.addLast("handler", serialHandler);
        pipeline.addLast("exceptions", new RxtxExceptionHandler());

        Log.debug(SERIAL, "Pipeline configured and handler added.");
    }

}
