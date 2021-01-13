package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.WorkoutFlags;
import de.tbressler.waterrower.subscriptions.*;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Executors;

import static de.tbressler.waterrower.subscriptions.WorkoutIntervalSubscription.IntervalType.REST_INTERVAL;
import static de.tbressler.waterrower.subscriptions.WorkoutIntervalSubscription.IntervalType.ROW_INTERVAL;
import static de.tbressler.waterrower.subscriptions.WorkoutTotalSubscription.ValueType.*;
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

        WaterRower waterRower = new WaterRower();
        waterRower.addConnectionListener(new IWaterRowerConnectionListener() {
            @Override
            public void onConnected(ModelInformation modelInformation) {
                Log.info("Connected to: " + modelInformation.getMonitorType().name() + ", " + modelInformation.getFirmwareVersion());
            }
            
            @Override
            public void onDisconnected() {
                Log.debug("Disconnected.");
            }
            
            @Override
            public void onError(ErrorCode errorCode) {
                Log.error("An error occurred! Error code is "+errorCode.name()+".", null);
            }

        });

        WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower, Executors.newSingleThreadScheduledExecutor());

        waterRower.subscribe(new AverageVelocitySubscription() {
            @Override
            protected void onVelocityUpdated(double velocity) {
                Log.info("Velocity = " + velocity);
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

        waterRower.subscribe(new WorkoutIntervalSubscription(ROW_INTERVAL, 1) {
            @Override
            protected void onWorkoutIntervalUpdated(IntervalType intervalType, int intervalIndex, int value) {
                Log.info("Workout interval = " + intervalType.name() + "-" + intervalIndex + " = " + value);
            }
        });
        waterRower.subscribe(new WorkoutIntervalSubscription(REST_INTERVAL, 1) {
            @Override
            protected void onWorkoutIntervalUpdated(IntervalType intervalType, int intervalIndex, int value) {
                Log.info("Workout interval = " + intervalType.name() + "-" + intervalIndex + " = " + value);
            }
        });
        waterRower.subscribe(new WorkoutIntervalSubscription(ROW_INTERVAL, 2) {
            @Override
            protected void onWorkoutIntervalUpdated(IntervalType intervalType, int intervalIndex, int value) {
                Log.info("Workout interval = " + intervalType.name() + "-" + intervalIndex + " = " + value);
            }
        });
        waterRower.subscribe(new WorkoutIntervalSubscription(REST_INTERVAL, 2) {
            @Override
            protected void onWorkoutIntervalUpdated(IntervalType intervalType, int intervalIndex, int value) {
                Log.info("Workout interval = " + intervalType.name() + "-" + intervalIndex + " = " + value);
            }
        });
        waterRower.subscribe(new WorkoutTotalSubscription(INTENSITY) {
            @Override
            protected void onTotalWorkoutValueUpdated(ValueType valueType, int value) {
                Log.info("Workout interval [" + valueType.name() + "] = " + value);
            }
        });
        waterRower.subscribe(new WorkoutTotalSubscription(STROKES) {
            @Override
            protected void onTotalWorkoutValueUpdated(ValueType valueType, int value) {
                Log.info("Workout interval [" + valueType.name() + "] = " + value);
            }
        });
        waterRower.subscribe(new WorkoutTotalSubscription(LIMIT) {
            @Override
            protected void onTotalWorkoutValueUpdated(ValueType valueType, int value) {
                Log.info("Workout interval [" + valueType.name() + "] = " + value);
            }
        });
        waterRower.subscribe(new WorkoutTotalSubscription(TIME) {
            @Override
            protected void onTotalWorkoutValueUpdated(ValueType valueType, int value) {
                Log.info("Workout interval [" + valueType.name() + "] = " + value);
            }
        });


        discovery.start();
    }

}
