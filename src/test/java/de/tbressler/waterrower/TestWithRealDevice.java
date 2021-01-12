package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.WorkoutFlags;
import de.tbressler.waterrower.subscriptions.AverageStrokeTimeSubscription;
import de.tbressler.waterrower.subscriptions.ClockCountDownSubscription;
import de.tbressler.waterrower.subscriptions.WorkoutFlagsSubscription;

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

    public static void main(String...args) throws IOException {

        WaterRowerInitializer initializer = new WaterRowerInitializer(ofSeconds(5), ofSeconds(5), 5);
        WaterRower waterRower = new WaterRower(initializer);
        waterRower.addConnectionListener(new IWaterRowerConnectionListener() {
            @Override
            public void onConnected(ModelInformation modelInformation) {
                Log.info("Connected to: " + modelInformation.getMonitorType().name() + ", " + modelInformation.getFirmwareVersion());
            }
            
            @Override
            public void onDisconnected() {
                Log.debug("Disconnected.");
                try {
                    // TODO Move this disconnect to the auto-discovery.
                    waterRower.disconnect();
                } catch (IOException e) {
                    Log.error("Couldn't disconnect! " + e.getMessage(), e);
                }
            }
            
            @Override
            public void onError(ErrorCode errorCode) {
                Log.error("An error occurred! Error code is "+errorCode.name()+".", null);
            }

        });

        WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower, Executors.newSingleThreadScheduledExecutor());

        waterRower.subscribe(new AverageStrokeTimeSubscription(AverageStrokeTimeSubscription.StrokeType.WHOLE_STROKE) {
            @Override
            protected void onAverageStrokeTimeUpdated(int averageStrokeTime) {
                Log.info("Average stroke time (WHOLE) - Original = "+averageStrokeTime);
                Log.info("Average stroke time (WHOLE) - V1 = "+ (60000 / (averageStrokeTime * 25)));
                Log.info("Average stroke time (WHOLE) - V2 = "+ (60000 / averageStrokeTime));
            }
        });

        waterRower.subscribe(new AverageStrokeTimeSubscription(AverageStrokeTimeSubscription.StrokeType.PULL_ONLY) {
            @Override
            protected void onAverageStrokeTimeUpdated(int averageStrokeTime) {
                Log.info("Average stroke time (PULL-ONLY) - Original = "+averageStrokeTime);
                Log.info("Average stroke time (PULL-ONLY) - V1 = "+ (60000 / (averageStrokeTime * 25)));
                Log.info("Average stroke time (PULL-ONLY) - V2 = "+ (60000 / averageStrokeTime));
            }
        });

        waterRower.subscribe(new ClockCountDownSubscription() {

            @Override
            protected void onClockCountDownUpdated(Duration duration) {
                Log.info("Clock count down = "+duration.toMinutesPart()+":"+duration.toSecondsPart());
            }

        });

        waterRower.subscribe(new WorkoutFlagsSubscription() {
            @Override
            protected void onWorkoutModeUpdated(WorkoutFlags flags) {
                Log.info("Workout distance mode = " + flags.isWorkoutDistanceMode());
                Log.info("Workout distance interval mode = " + flags.isWorkoutDistanceIntervalMode());
                Log.info("Workout duration mode = " + flags.isWorkoutDurationMode());
                Log.info("Workout duration interval mode = " + flags.isWorkoutDurationIntervalMode());
            }
        });

        discovery.start();
    }

}
