package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.WorkoutFlags;
import de.tbressler.waterrower.subscriptions.*;
import de.tbressler.waterrower.workout.Workout;
import de.tbressler.waterrower.workout.WorkoutUnit;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Executors;

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

//                Log.info("Try to start workout...");
//
//                Workout workout = new Workout(2000, WorkoutUnit.METERS);
//                workout.addInterval(60, 2000);
//                workout.addInterval(60, 2000);
//                workout.addInterval(60, 2000);
//                workout.addInterval(60, 2000);
//
//                try {
//                    waterRower.startWorkout(workout);
//                } catch (IOException e) {
//                    Log.error("Couldn't start workout!", e);
//                }
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

        waterRower.subscribe(new ClockCountDownSubscription() {

            @Override
            protected void onClockCountDownUpdated(Duration duration) {
                Log.info("Clock count down = "+duration.toMinutesPart()+":"+duration.toSecondsPart());
            }

        });

        waterRower.subscribe(new DisplayedDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                Log.info("Distance = " + distance);
            }
        });

        waterRower.subscribe(new DisplayedDurationSubscription() {
            @Override
            protected void onDurationUpdated(Duration duration) {
                Log.info("Duration = " + duration.toMinutesPart() + ":" + duration.toSecondsPart());
            }
        });

        waterRower.subscribe(new AverageStrokeRateSubscription() {
            @Override
            protected void onStrokeRateUpdated(double strokeRate) {
                Log.info("Stroke rate = " + strokeRate + "strokes/min");
            }
        });

        waterRower.subscribe(new AverageVelocitySubscription() {
            @Override
            protected void onVelocityUpdated(double velocity) {
                Log.info("Velocity = " + velocity + " m/sec");
            }
        });

        waterRower.subscribe(new WorkoutFlagsSubscription() {
            @Override
            protected void onWorkoutFlagsUpdated(WorkoutFlags flags) {
                Log.info("Workout distance mode = " + flags.isWorkoutDistanceMode());
                Log.info("Workout distance interval mode = " + flags.isWorkoutDistanceIntervalMode());
                Log.info("Workout duration mode = " + flags.isWorkoutDurationMode());
                Log.info("Workout duration interval mode = " + flags.isWorkoutDurationIntervalMode());
            }
        });

        waterRower.subscribe(new TotalWorkoutTimeSubscription() {
            @Override
            protected void onTotalWorkoutTimeUpdated(Duration time) {
                Log.info("Total workout time = "+time.toMinutesPart()+":"+time.toSecondsPart());
            }
        });
        waterRower.subscribe(new TotalWorkoutStrokesSubscription() {
            @Override
            protected void onTotalWorkoutStrokesUpdated(int strokes) {
                Log.info("Total workout strokes = "+strokes);
            }
        });

        waterRower.subscribe(new TotalWorkoutDistanceSubscription() {
            @Override
            protected void onTotalWorkoutDistanceUpdated(int distance) {
                Log.info("Total workout distance = " + distance);
            }
        });

        discovery.start();
    }

}
