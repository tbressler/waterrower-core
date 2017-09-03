package de.tbressler.waterrower.msg.in;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Ping (S4/S5 -> PC).
 *
 * Sent once a second while NO rowing is occurring to indicate to the PC the rowing monitor is
 * still operational but stopped.
 *
 * [P][ING] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class PingMessage extends AbstractMessage {
}
