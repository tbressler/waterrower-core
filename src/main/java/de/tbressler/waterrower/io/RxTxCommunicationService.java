package de.tbressler.waterrower.io;

import de.tbressler.waterrower.logs.Log;
import gnu.io.CommPortIdentifier;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static de.tbressler.waterrower.logs.Log.SERIAL;
import static gnu.io.CommPortIdentifier.getPortIdentifiers;

/**
 * A communication service that manages the serial connection.
 * It can receive and send serial messages via RXTX.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxTxCommunicationService {


    /**
     * A communication service that manages the serial connection.
     * It can receive and send serial messages via RXTX.
     */
    public RxTxCommunicationService() {

    }

    /**
     * Returns a list of serial port addresses from the operating system.
     *
     * @return List of serial port addresses or an empty list.
     */
    public List<RxtxDeviceAddress> getSerialPorts() {

        List<RxtxDeviceAddress> result = new ArrayList<>();

        Log.debug(SERIAL, "Searching for serial ports...");

        Enumeration<CommPortIdentifier> ports = getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier portIdentifier = ports.nextElement();
            result.add(new RxtxDeviceAddress(portIdentifier.getName()));
            Log.debug(SERIAL, "Serial port found: " + portIdentifier.getName());
        }

        if (result.isEmpty())
            Log.warn(SERIAL, "No serial ports found!");

        return result;
    }

}
