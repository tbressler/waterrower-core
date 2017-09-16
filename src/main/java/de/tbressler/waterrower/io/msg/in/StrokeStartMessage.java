package de.tbressler.waterrower.io.msg.in;

import de.tbressler.waterrower.io.msg.AbstractMessage;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Start of stroke (S4/S5 -> PC).
 *
 * This packet is auto transmitted by the rowing computer.
 *
 * Start of stroke pull to show when the rowing computer determined acceleration occurring in the
 * paddle. This packet has the highest priority of transmission on the USB.
 *
 * [S][S] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class StrokeStartMessage extends AbstractMessage {

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }

}
