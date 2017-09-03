package de.tbressler.waterrower.io;

import de.tbressler.waterrower.log.Log;
import io.netty.channel.*;

import java.net.SocketAddress;

/**
 * Handles exceptions in the communication thread.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxExceptionHandler extends ChannelDuplexHandler {

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess())
                    Log.error("Error while connecting to the channel!", future.cause());
            }
        }));
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess())
                    Log.error("Error while disconnecting from the channel!", future.cause());
            }
        }));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess())
                    Log.error("Error while writing to the channel!", future.cause());
            }
        }));
    }

}
