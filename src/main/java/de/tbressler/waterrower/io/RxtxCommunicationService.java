package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.log.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static de.tbressler.waterrower.log.Log.SERIAL;
import static java.util.Objects.requireNonNull;

/**
 * A communication service that manages the serial connection.
 * It can receive and send serial messages via RXTX.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxCommunicationService {

    /* The bootstrap. */
    private final Bootstrap bootstrap;

    /* The current channel or null. */
    private Channel currentChannel;

    /* A lock for synchronized access to open/close/read/write on channel. */
    private ReentrantLock lock = new ReentrantLock(true);

    /* Listeners for RXTX connections. */
    private final List<IRxtxConnectionListener> connectionListeners = new ArrayList<>();


    /* Handler for the communication channel. */
    private RxtxSerialHandler serialHandler = new RxtxSerialHandler() {

        @Override
        protected void onConnected() {
            fireOnConnected();
        }

        @Override
        protected void onMessageReceived(AbstractMessage message) {
            fireOnMessageReceived(message);
        }

        @Override
        protected void onDisconnected() {
            fireOnDisconnected();
        }

        @Override
        protected void onError() {
            closeInternal();
            fireOnError();
        }

    };


    /**
     * A communication service that manages the serial connection.
     * It can receive and send serial messages via RXTX.
     *
     * @param bootstrap The bootstrap, not null.
     * @param channelInitializer The channel initializer, not null.
     */
    public RxtxCommunicationService(Bootstrap bootstrap, RxtxChannelInitializer channelInitializer) {
        requireNonNull(bootstrap);
        requireNonNull(channelInitializer);

        this.bootstrap = bootstrap;
        this.bootstrap.group(new OioEventLoopGroup());
        this.bootstrap.channel(RxtxChannel.class);

        channelInitializer.setRxTxSerialHandler(serialHandler);

        this.bootstrap.handler(channelInitializer);
    }


    /**
     * Opens the connection to the given serial port.
     *
     * @param address The serial port, must not be null.
     * @throws IOException if opening of the channel fails.
     */
    public void open(RxtxDeviceAddress address) throws IOException {
        requireNonNull(address);

        lock.lock();

        try {

            checkIfChannelIsClose();

            Log.debug(SERIAL, "Opening channel at serial port '" + address.value() + "'.");

            ChannelFuture future = bootstrap.connect(address).syncUninterruptibly();
            if (!future.isSuccess()) {
                fireOnError();
                throw new IOException("Serial channel couldn't be opened!");
            }

            Log.debug(SERIAL, "Serial channel was successfully opened.");

            currentChannel = future.channel();

        } catch (Exception e) {
            throw new IOException("Can not connect to '"+address.value()+"'!", e);
        } finally {
            lock.unlock();
        }
    }

    /* Throws IOException if channel is already open. */
    private void checkIfChannelIsClose() throws IOException {
        if (currentChannel != null)
            throw new IOException("Serial channel is already open!");
    }


    /**
     * Returns true if the communication service is connected.
     *
     * @return True if connected otherwise false.
     */
    public boolean isConnected() {

        lock.lock();

        try {

            return (currentChannel != null);

        } finally {
            lock.unlock();
        }
    }


    /**
     * Sends the given message.
     *
     * @param msg The message to be send, must not be null.
     */
    public void send(AbstractMessage msg) throws IOException {
        requireNonNull(msg);

        lock.lock();

        try {

            checkIfChannelIsOpen();

            Log.debug(SERIAL, "Sending message '" + msg.toString() + "'.");

            currentChannel.write(msg);

        } catch (Exception e) {
            throw new IOException("Can not send message '"+msg+"'!", e);
        } finally {
            lock.unlock();
        }
    }


    /**
     * Closes the current connection.
     *
     * @throws IOException if closing fails.
     */
    public void close() throws IOException {

        lock.lock();

        try {

            checkIfChannelIsOpen();

            Log.debug(SERIAL, "Closing serial channel.");

            ChannelFuture future = currentChannel.close().syncUninterruptibly();
            if (!future.isSuccess())
                throw new IOException("Serial channel couldn't be closed!");

            Log.debug(SERIAL, "Serial channel was successfully closed.");

            currentChannel = null;

        } catch (Exception e) {
            throw new IOException("Can not disconnect!", e);
        } finally {
            lock.unlock();
        }
    }

    /* Throws IOException if channel is already closed. */
    private void checkIfChannelIsOpen() throws IOException {
        if ((currentChannel == null) || (!currentChannel.isOpen()))
            throw new IOException("Serial channel is not open!");
    }


    private void closeInternal() {
        try {
            Log.debug(SERIAL, "Try to close channel.");
            close();
        } catch (IOException e) {
            Log.warn(SERIAL, "Channel can not be closed!");
        }
    }


    /**
     * Add a connection listener.
     *
     * @param listener The listener.
     */
    public void addRxtxConnectionListener(IRxtxConnectionListener listener) {
        requireNonNull(listener);
        connectionListeners.add(listener);
    }

    /* Notify all listeners about a successful connection. */
    private void fireOnConnected() {
        connectionListeners.forEach(IRxtxConnectionListener::onConnected);
    }

    /* Notify all listeners about an error. */
    private void fireOnError() {
        connectionListeners.forEach(IRxtxConnectionListener::onError);
    }

    /* Notify all listeners about a disconnect. */
    private void fireOnDisconnected() {
        connectionListeners.forEach(IRxtxConnectionListener::onDisconnected);
    }

    /* Notify all listeners about a received message. */
    private void fireOnMessageReceived(AbstractMessage msg) {
        for (IRxtxConnectionListener listener : connectionListeners)
            listener.onMessageReceived(msg);
    }

    /**
     * Remove a connection listener.
     *
     * @param listener The listener.
     */
    public void removeRxtxConnectionListener(IRxtxConnectionListener listener) {
        requireNonNull(listener);
        connectionListeners.remove(listener);
    }

}
