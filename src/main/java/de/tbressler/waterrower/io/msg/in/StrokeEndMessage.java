package de.tbressler.waterrower.io.msg.in;

import de.tbressler.waterrower.io.msg.AbstractMessage;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * End of stroke (S4/S5 -> PC).
 *
 * This packet is auto transmitted by the rowing computer.
 *
 * End of stroke pull to show when the rowing computer determined deceleration occurring in the
 * paddle. (Now entered the relax phase). This packet has the second highest priority of
 * transmission on the USB.
 *
 * [S][E] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class StrokeEndMessage extends AbstractMessage {

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }

}
