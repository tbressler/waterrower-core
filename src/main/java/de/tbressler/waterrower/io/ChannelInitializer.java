package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.codec.MessageFrameDecoder;
import de.tbressler.waterrower.io.codec.MessageFrameEncoder;
import de.tbressler.waterrower.io.codec.MessageParser;
import de.tbressler.waterrower.io.transport.JSerialCommChannel;
import de.tbressler.waterrower.io.transport.JSerialCommChannelConfig;
import de.tbressler.waterrower.log.Log;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import static de.tbressler.waterrower.io.transport.JSerialCommChannelConfig.Paritybit.NONE;
import static de.tbressler.waterrower.io.transport.JSerialCommChannelConfig.Stopbits.STOPBITS_1;
import static de.tbressler.waterrower.log.Log.SERIAL;
import static io.netty.handler.codec.Delimiters.lineDelimiter;
import static java.util.Objects.requireNonNull;

/**
 * Initializes the serial channel and sets up the pipeline for encoding and decoding the messages.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ChannelInitializer extends io.netty.channel.ChannelInitializer<JSerialCommChannel> {

    /* Maximum length of a single frame. */
    private static final int MAX_FRAME_LENGTH = 32;


    /* The handler of the serial channel. */
    private SerialHandler serialHandler;

    /* The message parser. */
    private MessageParser parser = new MessageParser();


    /**
     * Initializes the serial channel and sets up the pipeline for encoding and decoding the messages.
     */
    public ChannelInitializer() {}


    /**
     * Sets the serial handler.
     *
     * @param serialHandler The serial handler, must not be null.
     */
    public void setSerialHandler(SerialHandler serialHandler) {
        this.serialHandler = requireNonNull(serialHandler);
    }


    @Override
    protected void initChannel(JSerialCommChannel channel) throws Exception {
        Log.debug(SERIAL, "Serial channel initialized. Configuring pipeline and channel...");

        checkIfSerialHandlerIsSet();

        configureChannel(channel);
        configurePipeline(channel);
    }

    /* Checks if the serial handler is not null. */
    private void checkIfSerialHandlerIsSet() {
        if (serialHandler == null) {
            IllegalStateException exception = new IllegalStateException("You forgot to set the serial handler before initializing the channel.");
            Log.error("Serial channel couldn't be initialized!", exception);
            throw exception;
        }
    }


    /* Configures the channel. */
    private void configureChannel(JSerialCommChannel channel) {
        JSerialCommChannelConfig config = channel.config();
        config.setBaudrate(19200);
        config.setDatabits(8);
        config.setStopbits(STOPBITS_1);
        config.setParitybit(NONE);

        logSerialConfiguration(config);
    }

    /* Logs the serial configuration. */
    private void logSerialConfiguration(JSerialCommChannelConfig config) {
        Log.debug(SERIAL, "Serial channel configured to: " +
                "\n Baudrate: " + config.getBaudrate() +
                "\n Databits: " + config.getDatabits() +
                "\n Stopbits: " + config.getStopbits().name() +
                "\n Parity: " + config.getParitybit());
    }

    /* Configures the pipeline. */
    private void configurePipeline(JSerialCommChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        // Decode messages:
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, lineDelimiter()));
        pipeline.addLast("decoder", new MessageFrameDecoder(parser));

        // Encode messages:
        pipeline.addLast("encoder", new MessageFrameEncoder(parser));

        // Handle messages and exceptions:
        pipeline.addLast("handler", serialHandler);

        Log.debug(SERIAL, "Pipeline configured and handler added.");
    }

}