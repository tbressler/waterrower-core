package de.tbressler.waterrower;

import de.tbressler.waterrower.discovery.WaterRowerAutoDiscovery;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.ErrorCode;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.model.WorkoutFlags;
import de.tbressler.waterrower.subscriptions.values.*;
import de.tbressler.waterrower.subscriptions.workouts.TotalWorkoutDistanceSubscription;
import de.tbressler.waterrower.subscriptions.workouts.TotalWorkoutStrokesSubscription;
import de.tbressler.waterrower.subscriptions.workouts.TotalWorkoutTimeSubscription;
import de.tbressler.waterrower.subscriptions.workouts.WorkoutFlagsSubscription;
import de.tbressler.waterrower.workout.Workout;
import de.tbressler.waterrower.workout.WorkoutUnit;

import java.io.IOException;
import java.time.Duration;

import static java.time.Duration.ofMillis;
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

        // Custom setting for the connection to the WaterRower.
        WaterRowerInitializer initializer = new WaterRowerInitializer(ofMillis(100), ofSeconds(5), 5);

        WaterRower waterRower = new WaterRower(initializer);

        // Add a connection listener, listening for connect, disconnect or errors.
        waterRower.addConnectionListener(new IWaterRowerConnectionListener() {
            @Override
            public void onConnected(ModelInformation modelInformation) {

                Log.info("Connected to: " + modelInformation.getMonitorType().name() + " (" + modelInformation.getFirmwareVersion() + ")");

                Log.info("Sending workout to the WaterRower.");

                // When connected send an interval workout to the
                // Performance Monitor:

                try {

                    Workout workout = new Workout(2000, WorkoutUnit.METERS);
                    workout.addInterval(60, 2000);
                    workout.addInterval(60, 2000);
                    workout.addInterval(60, 2000);
                    workout.addInterval(60, 1000);

                    waterRower.startWorkout(workout);

                } catch (IOException e) {
                    Log.error("Couldn't send the workout!", e);
                }
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


        // Subscribe to the different values and output
        // the updates to the log:

        waterRower.subscribe(new WattsSubscription() {
            @Override
            protected void onWattsUpdated(int watt) {
                Log.info("Watts = " + watt + " W");
            }
        });

        waterRower.subscribe(new TotalCaloriesSubscription() {
            @Override
            protected void onCaloriesUpdated(int cal) {
                Log.info("Calories = " + (cal / 1000) + " kcal");
            }
        });

        waterRower.subscribe(new DisplayedDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                Log.info("Distance = " + distance + " m");
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
                Log.info("Stroke rate = " + strokeRate + " strokes/min");
            }
        });

        waterRower.subscribe(new AverageVelocitySubscription() {
            @Override
            protected void onVelocityUpdated(double velocity) {

                Log.info("Velocity = " + velocity + " m/sec");

                Duration split = Duration.ZERO;
                if (velocity > 0)
                    split = ofSeconds((long) (500D / velocity));
                Log.info("Split (500m) = " + split.toMinutesPart() + ":" + split.toSecondsPart());
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
            protected void onTimeUpdated(Duration time) {
                Log.info("Total workout time = "+time.toMinutesPart()+":"+time.toSecondsPart());
            }
        });
        waterRower.subscribe(new TotalWorkoutStrokesSubscription() {
            @Override
            protected void onStrokesUpdated(int strokes) {
                Log.info("Total workout strokes = "+strokes);
            }
        });

        waterRower.subscribe(new TotalWorkoutDistanceSubscription() {
            @Override
            protected void onDistanceUpdated(int distance) {
                Log.info("Total workout distance = " + distance + " m");
            }
        });


        // Initialize and start the auto-discovery:
        WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower);
        discovery.start();
    }

}
