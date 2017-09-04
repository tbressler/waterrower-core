package de.tbressler.waterrower.msg.out;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Application starting communication's (PC -> S4/S5).
 *
 * This is the very first packet sent by an application once the COM port is opened, this will
 * tell the rowing computer to reply with its hardware type packet.
 *
 * [U][SB] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class StartCommunicationMessage extends AbstractMessage {}
