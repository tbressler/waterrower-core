package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.io.msg.InformationRequestMessage;
import de.tbressler.waterrower.io.msg.in.ModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.RequestModelInformationMessage;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.MonitorType;

import static de.tbressler.waterrower.model.MonitorType.*;

/**
 * Interpreter for:
 *
 * Request Model Information (PC -> S4/S5).
 *
 * Request details from the rowing computer on what it is and firmware version.
 *
 * [I][V?] + 0x0D0A
 *
 * Current Model Information (S4/S5 -> PC).
 *
 * Details of what unit is attached:
 * - Model - Sent as 4 or 5 to indicate if it is a Series 4 or series 5 rowing computer.
 * - Version high - 02 as an example for version 2.00 MSB of the firmware version.
 * - Version low - 00 as an example for version 2.00 LSB of the firmware version.
 *
 * [I][V] + [Model] + [Version High] + [Version Low] + 0x0D0A
 *
 * TODO More ...
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class InformationRequestMessageInterpreter extends AbstractMessageInterpreter<InformationRequestMessage> {

    @Override
    public String getMessageTypeChar() {
        return "I";
    }

    @Override
    public Class<InformationRequestMessage> getMessageType() {
        return InformationRequestMessage.class;
    }

    @Override
    public InformationRequestMessage decode(byte[] bytes) {

        String payload = new String(bytes);

        if (payload.startsWith("IV")) {

            MonitorType monitorType = parseMonitorType(payload);
            String firmwareVersion = payload.substring(3, 5) + "." + payload.substring(5, 7);

            return new ModelInformationMessage(new ModelInformation(monitorType, firmwareVersion));
        }

        // TODO More ...

        return null;
    }

    private MonitorType parseMonitorType(String payload) {
        switch (payload.charAt(2)) {
            case '4':
                return WATER_ROWER_S4;
            case '5':
                return WATER_ROWER_S5;
        }
        return UNKNOWN_MONITOR_TYPE;
    }

    @Override
    public byte[] encode(InformationRequestMessage msg) {

        if (msg instanceof RequestModelInformationMessage) {
            return new String("IV?").getBytes();
        }

        // TODO More ...

        return new byte[0];
    }

}
