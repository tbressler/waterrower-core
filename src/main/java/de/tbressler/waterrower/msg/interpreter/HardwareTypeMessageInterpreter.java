package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.in.HardwareTypeMessage;

/**
 * Interpreter for:
 *
 * Hardware Type (S4/S5 -> PC).
 *
 * The Water Rower will reply with this packet when it receives a "USB" packet and will then
 * proceed to send other packets accordingly until it switch’s off or the application issues an
 * exit packet.
 *
 * [_][WR_] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class HardwareTypeMessageInterpreter extends AbstractMessageInterpreter<HardwareTypeMessage> {

    @Override
    public String getMessageTypeChar() {
        return "_";
    }

    @Override
    public Class<HardwareTypeMessage> getMessageType() {
        return HardwareTypeMessage.class;
    }

    @Override
    public HardwareTypeMessage decode(byte[] bytes) {
        if (bytes.length < 3)
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
