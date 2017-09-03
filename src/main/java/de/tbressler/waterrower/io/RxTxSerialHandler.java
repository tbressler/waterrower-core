package de.tbressler.waterrower.io;

import de.tbressler.waterrower.logs.Log;
import de.tbressler.waterrower.msg.AbstractSerialMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static de.tbressler.waterrower.logs.Log.SERIAL;

/**
 * Handler for serial messages.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
@Sharable
public abstract class RxTxSerialHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {

            if (!(msg instanceof AbstractSerialMessage)) {
                Log.warn(SERIAL, "Invalid message received! Message skipped.");
                return;
            }

            Log.debug(SERIAL, "Message received: " + msg);

            // Notify that a message was received.
            onMessageReceived((AbstractSerialMessage) msg);

        } finally {
           ReferenceCountUtil.release(msg);
        }
    }

    /**
     * Receives serial messages.
     *
     * @param message The serial message.
     */
    abstract protected void onMessageReceived(AbstractSerialMessage message);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        onConnected();
    }

    /**
     * Called if connection was established.
     */
    abstract protected void onConnected();


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        onDisconnected();
    }

    /**
     * Called if connection was closed.
     */
    protected abstract void onDisconnected();


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.error("Unexpected exception caught in "+this.getClass().getSimpleName() + "!", cause);
        ctx.close();
    }
}
