package de.tbressler.waterrower.msg.in;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Current Model Information (S4/S5 -> PC).
 *
 * Details of what unit is attached:
 * - Model - Sent as 4 or 5 to indicate if it is a Series 4 or series 5 rowing computer.
 * - Version high - 02 as an example for version 2.00 MSB of the firmware version.
 * - Version low - 00 as an example for version 2.00 LSB of the firmware version.
 *
 * [I][V] + [Model] + [Version High] + [Version Low] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ModelInformationMessage extends AbstractMessage {
}
