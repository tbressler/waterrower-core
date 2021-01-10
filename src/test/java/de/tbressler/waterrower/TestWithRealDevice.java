package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.subscriptions.DisplayedDistanceSubscription;
import de.tbressler.waterrower.subscriptions.DisplayedDurationSubscription;
import de.tbressler.waterrower.subscriptions.TankVolumeSubscription;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Executors;

import static java.time.Duration.ofSeconds;

/**
 * A small application which connects to the WaterRower Performance Monitor and
 * subscribes to some signals.
 *
 * The class can be used to test with a real device.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWithRealDevice {

    private static final String TEST = "Test";

    private static int STROKES = 0;


    public static void main(String...args) throws IOException {

        WaterRowerInitializer initializer = new WaterRowerInitializer(ofSeconds(5), ofSeconds(5), 5);
        WaterRower waterRower = new WaterRower(initializer);
        waterRower.addConnectionListener(new IWaterRowerConnectionListener() {
            @Override
            public void onConnected(ModelInformation modelInformation) {
                Log.debug(TEST, "Connected to: " + modelInformation.getMonitorType().name() + ", " + modelInformation.getFirmwareVersion());
            }
            
            @Override
            public void onDisconnected() {
                Log.debug(TEST, "Disconnected.");
                try {
                    waterRower.disconnect();
                } catch (IOException e) {
                    Log.error("Couldn't disconnect! " + e.getMessage(), null);
                }
            }
            
            @Override
            public void onError(ErrorCode errorCode) {
                Log.error("An error occurred! Error code is "+errorCode.name()+".", null);
            }

        });

        WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower, Executors.newSingleThreadScheduledExecutor());

        waterRower.subscribe(new TankVolumeSubscription() {
            @Override
            protected void onTankVolumeUpdated(int tankVolume) {
                Log.info(TEST, "Value updated. Tank volume = "+tankVolume + " ------------------------------------");
            }
        });

        waterRower.subscribe(new DisplayedDurationSubscription() {
            @Override
            protected void onDurationUpdated(Duration duration) {
                Log.info(TEST, "Value updated. Displayed duration = "+duration.toMinutes()+" minute(s)" + " ------------------------------------");
            }
        });

        waterRower.subscribe(new DisplayedDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                Log.info(TEST, "Value updated. Displayed distance = "+distance+" meter(s)" + " ------------------------------------");
            }
        });

        discovery.start();
    }

}
