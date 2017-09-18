package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.io.msg.in.StrokeMessage;

import static de.tbressler.waterrower.model.StrokeType.END_OF_STROKE;
import static de.tbressler.waterrower.model.StrokeType.START_OF_STROKE;

/**
 * Interpreter for:
 *
 * Stroke start/end (S4/S5 -> PC).
 *
 * This packet is auto transmitted by the rowing computer.
 *
 * Start of strokeType:
 *
 * Start of strokeType pull to show when the rowing computer determined acceleration occurring in the
 * paddle. This packet has the highest priority of transmission on the USB.
 *
 * [S][S] + 0x0D0A
 *
 * End of strokeType:
 *
 * End of strokeType pull to show when the rowing computer determined deceleration occurring in the
 * paddle. (Now entered the relax phase). This packet has the second highest priority of
 * transmission on the USB.
 *
 * [S][E] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class StrokeMessageInterpreter extends AbstractMessageInterpreter<StrokeMessage> {

    @Override
    public String getMessageIdentifier() {
        return "S";
    }

    @Override
    public Class<StrokeMessage> getMessageType() {
        return StrokeMessage.class;
    }

    @Override
    public StrokeMessage decode(String msg) {
        if (msg.startsWith("SS")) {
            return new StrokeMessage(START_OF_STROKE);
        } else if (msg.startsWith("SE")) {
            return new StrokeMessage(END_OF_STROKE);
        }
        return null;
    }

    @Override
    public String encode(StrokeMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
