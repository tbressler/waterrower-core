package de.tbressler.waterrower.io;

import de.tbressler.waterrower.logs.Log;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;

import java.io.IOException;

import static de.tbressler.waterrower.logs.Log.SERIAL;
import static io.netty.channel.rxtx.RxtxChannelConfig.Databits.DATABITS_8;
import static io.netty.channel.rxtx.RxtxChannelConfig.Paritybit.NONE;
import static io.netty.channel.rxtx.RxtxChannelConfig.Stopbits.STOPBITS_1;
import static java.util.Objects.requireNonNull;

/**
 *
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxTxChannelInitializer extends ChannelInitializer<RxtxChannel> {

    /* The handler of the serial channel. */
    private RxTxSerialHandler serialHandler;

    /* The message parser. */
    // private XBeeMessageParser parser = new XBeeMessageParser();


    /**
     *
     */
    public RxTxChannelInitializer() {}


    /**
     * Sets the serial handler.
     *
     * @param serialHandler The serial handler, must not be null.
     */
    public void setRxTxSerialHandler(RxTxSerialHandler serialHandler) {
        this.serialHandler = requireNonNull(serialHandler);
    }


    @Override
    protected void initChannel(RxtxChannel channel) throws Exception {
        Log.debug(SERIAL, "RxTx channel initialized. Configuring pipeline and channel...");

        checkIfRxTxSerialHandlerIsSet();

        configureChannel(channel);
        configurePipeline(channel);
    }

    /* Checks if the RxTx serial handler is not null. */
    private void checkIfRxTxSerialHandlerIsSet() throws IOException {
        if (serialHandler == null) {
            IOException exception = new IOException("You forgot to set the serial handler, before initializing the channel.");
            Log.error("RxTx channel couldn't be initialized!", exception);
            throw exception;
        }
    }


    /* Configures the channel. */
    private void configureChannel(RxtxChannel channel) {
        RxtxChannelConfig config = channel.config();
        config.setBaudrate(9600);
        config.setDatabits(DATABITS_8);
        config.setStopbits(STOPBITS_1);
        config.setParitybit(NONE);

        logSerialConfiguration(config);
    }

    /* Logs the serial configuration. */
    private void logSerialConfiguration(RxtxChannelConfig config) {
        Log.debug(SERIAL, "RXTX channel configured: " +
                "\n Baudrate: " + config.getBaudrate() +
                "\n Databits: " + config.getDatabits().name() +
                "\n Stopbits: " + config.getStopbits().name() +
                "\n Parity: " + config.getParitybit());
    }

    /* Configures the pipeline. */
    private void configurePipeline(RxtxChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        // pipeline.addLast("decoder", new RxtxMessageFrameDecoder(parser));
        // pipeline.addLast("encoder", new RxtxMessageFrameEncoder(parser));
        pipeline.addLast("handler", serialHandler);
        pipeline.addLast("exceptions", new RxTxExceptionHandler());

        Log.debug(SERIAL, "Pipeline configured and handler added.");
    }

}
