package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.model.MonitorType;
import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.in.ModelInformationMessage;

import static de.tbressler.waterrower.model.MonitorType.*;

/**
 * A parser for message type: ModelInformationMessage
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ModelInformationMessageInterpreter extends AbstractMessageInterpreter<ModelInformationMessage> {

    @Override
    public char getMessageTypeByte() {
        return 'I';
    }

    @Override
    public Class<ModelInformationMessage> getMessageType() {
        return ModelInformationMessage.class;
    }

    @Override
    public ModelInformationMessage decode(byte[] bytes) {
        if (bytes.length < 4)
            return null;

        String payload = new String(bytes);

        MonitorType monitorType = parseMonitorType(payload);
        String firmwareVersion = payload.charAt(4) + payload.charAt(5) + "." + payload.charAt(6) + payload.charAt(7);

        return new ModelInformationMessage(monitorType, firmwareVersion);
    }

    private MonitorType parseMonitorType(String payload) {
        switch (payload.charAt(3)) {
            case '4':
                return WATER_ROWER_S4;
            case '5':
                return WATER_ROWER_S5;
        }
        return UNKNOWN_MONITOR_TYPE;
    }

    @Override
    public byte[] encode(ModelInformationMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
