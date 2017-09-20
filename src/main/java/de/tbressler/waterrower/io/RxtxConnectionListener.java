package de.tbressler.waterrower.io;

import de.tbressler.waterrower.io.msg.AbstractMessage;

/**
 *
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxConnectionListener implements IRxtxConnectionListener {

    @Override
    public void onConnected() {}

    @Override
    public void onMessageReceived(AbstractMessage msg) {}

    @Override
    public void onDisconnected() {}

    @Override
    public void onError() {}

}
