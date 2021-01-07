package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.IDiscoveryStore;
import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.subscriptions.StrokeCountSubscription;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.concurrent.Executors;

import static java.time.Duration.ofSeconds;

/**
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWithRealDevice {

    private static final Marker TEST = MarkerManager.getMarker("Test");


    public static void main(String...args) throws IOException {

        WaterRowerInitializer initializer = new WaterRowerInitializer(ofSeconds(2), ofSeconds(2), 5);
        WaterRower waterRower = new WaterRower(initializer);
        waterRower.addConnectionListener(new IWaterRowerConnectionListener() {
            @Override
            public void onConnected(ModelInformation modelInformation) {
                Log.debug(TEST, "Connected to: " + modelInformation.getMonitorType().name() + ", " + modelInformation.getFirmwareVersion());
            }
            
            @Override
            public void onDisconnected() {
                Log.debug(TEST, "Disconnected.");

            }
            
            @Override
            public void onError(ErrorCode errorCode) {
                Log.error("An error occurred! Error code is "+errorCode.name()+".", null);
            }

        });

        WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower, new IDiscoveryStore() {

            @Override
            public void setLastSuccessfulSerialPort(String serialPort) {
                Log.debug(TEST, "The successful serial port is: " + serialPort);
            }

            @Override
            public String getLastSuccessfulSerialPort() {
                return null;
            }

        }, Executors.newSingleThreadScheduledExecutor());

        waterRower.subscribe(new StrokeCountSubscription() {
            @Override
            protected void onStrokeCountUpdated(int strokes) {
                Log.debug(TEST, "Value updated. Stroke count = " + strokes);
            }
        });

        discovery.start();
    }

}
