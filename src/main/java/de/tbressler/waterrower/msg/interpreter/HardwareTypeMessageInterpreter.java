package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.in.HardwareTypeMessage;

/**
 * A parser for message type: HardwareTypeMessage
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class HardwareTypeMessageInterpreter extends AbstractMessageInterpreter<HardwareTypeMessage> {

    @Override
    public char getMessageTypeByte() {
        return '_';
    }

    @Override
    public Class<HardwareTypeMessage> getMessageType() {
        return HardwareTypeMessage.class;
    }

    @Override
    public HardwareTypeMessage decode(byte[] bytes) {
        if (bytes.length < 4)
            return null;

        String payload = new String(bytes);
        boolean isWaterRower = payload.equals("_WR_");

        return new HardwareTypeMessage(isWaterRower);
    }

    @Override
    public byte[] encode(HardwareTypeMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
