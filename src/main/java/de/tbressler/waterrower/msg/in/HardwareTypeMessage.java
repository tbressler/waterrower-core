package de.tbressler.waterrower.msg.in;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Hardware Type (S4/S5 -> PC).
 *
 * The Water Rower will reply with this packet when it receives a “USB” packet and will then
 * proceed to send other packets accordingly until it switch’s off or the application issues an
 * exit packet.
 *
 * [_][WR_] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class HardwareTypeMessage extends AbstractMessage {
}
