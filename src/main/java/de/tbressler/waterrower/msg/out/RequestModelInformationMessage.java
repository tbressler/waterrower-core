package de.tbressler.waterrower.msg.out;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Request Model Information (PC -> S4/S5).
 *
 * Request details from the rowing computer on what it is and firmware version.
 *
 * [I][V?] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RequestModelInformationMessage extends AbstractMessage {}
