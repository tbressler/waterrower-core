package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.subscriptions.AverageStrokeTimeSubscription;
import de.tbressler.waterrower.subscriptions.DisplayedDurationSubscription;
import de.tbressler.waterrower.subscriptions.DistanceSubscription;
import de.tbressler.waterrower.subscriptions.StrokeCountSubscription;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

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

    private static final Marker TEST = MarkerManager.getMarker("Test");


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

            }
            
            @Override
            public void onError(ErrorCode errorCode) {
                Log.error("An error occurred! Error code is "+errorCode.name()+".", null);
            }

        });

        WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower, Executors.newSingleThreadScheduledExecutor());

        waterRower.subscribe(new StrokeCountSubscription() {
            @Override
            protected void onStrokeCountUpdated(int strokes) {
                Log.debug(TEST, "Value updated. Stroke count = " + strokes);
            }
        });

        waterRower.subscribe((new AverageStrokeTimeSubscription(AverageStrokeTimeSubscription.StrokeType.WHOLE_STROKE) {
            @Override
            protected void onAverageStrokeTimeUpdated(int averageStrokeTime) {
                Log.debug(TEST, "Value updated. Average stroke time (whole stroke) = " + averageStrokeTime + " ?");
            }
        }));

        waterRower.subscribe((new AverageStrokeTimeSubscription(AverageStrokeTimeSubscription.StrokeType.PULL_ONLY) {
            @Override
            protected void onAverageStrokeTimeUpdated(int averageStrokeTime) {
                Log.debug(TEST, "Value updated. Average stroke time (pull only) = " + averageStrokeTime + " ?");
            }
        }));

        waterRower.subscribe(new DisplayedDurationSubscription() {
            @Override
            protected void onDurationUpdated(Duration duration) {
                Log.debug(TEST, "Value updated. Displayed duration = "+duration.toMinutes()+" minute(s)");
            }
        });

        waterRower.subscribe(new DistanceSubscription(DistanceSubscription.DistanceMode.DISPLAYED_DISTANCE) {
            @Override
            protected void onDistanceUpdated(DistanceMode mode, int distance) {
                Log.debug(TEST, "Value updated. Displayed distance ("+mode+") = "+distance);
            }
        });

        waterRower.subscribe(new DistanceSubscription(DistanceSubscription.DistanceMode.DISTANCE) {
            @Override
            protected void onDistanceUpdated(DistanceMode mode, int distance) {
                Log.debug(TEST, "Value updated. Distance ("+mode+") = "+distance);
            }
        });

        waterRower.subscribe(new DistanceSubscription(DistanceSubscription.DistanceMode.TOTAL_DISTANCE) {
            @Override
            protected void onDistanceUpdated(DistanceMode mode, int distance) {
                Log.debug(TEST, "Value updated. Total distance ("+mode+") = "+distance);
            }
        });

        discovery.start();
    }

}
