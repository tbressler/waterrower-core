package de.tbressler.waterrower.io;

import de.tbressler.waterrower.log.Log;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import static java.util.Objects.requireNonNull;

/**
 * Handles exceptions in the communication thread.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxExceptionHandler extends ChannelDuplexHandler {

    /* The serial channel handler. */
    private final RxtxSerialHandler serialHandler;


    /**
     * Handles exceptions in the communication thread.
     *
     * @param serialHandler The serial channel handler, must not be null.
     */
    public RxtxExceptionHandler(RxtxSerialHandler serialHandler) {
        this.serialHandler = requireNonNull(serialHandler);
    }


    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess())
                return;
            Log.error("Error while connecting to the channel!", future.cause());
            serialHandler.onError();
        }));
    }


    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess())
                return;
            Log.error("Error while disconnecting from the channel!", future.cause());
            serialHandler.onError();
        }));
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess())
                return;
            Log.error("Error while writing to the channel!", future.cause());
            serialHandler.onError();
        }));
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess())
                return;
            Log.error("Error while closing the channel!", future.cause());
            serialHandler.onError();
        }));
    }

}
