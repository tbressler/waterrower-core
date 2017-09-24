package de.tbressler.waterrower.workout;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * A workout configuration.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class Workout {

    /* True if this workout is a single workout. */
    private boolean isSingleWorkout = true;

    /* A list of workout intervals. */
    private final List<WorkoutInterval> intervals = new ArrayList<>();

    /* The unit for the distance/duration of the single or interval workout. */
    private final WorkoutUnit unit;


    // TODO The value ranges must be described here!
    /**
     * A workout configuration.
     *
     * @param distance The distance/duration of the single workout or the first interval of the
     *                 interval workout.
     * @param unit The unit for the distance/duration of the single or interval workout.
     */
    public Workout(int distance, WorkoutUnit unit) {
        this.unit = requireNonNull(unit);
        intervals.add(new WorkoutInterval(distance, unit));
    }


    /**
     * Adds an interval to the workout.
     *
     * @param restInterval The rest interval (in seconds).
     * @param distance The distance/duration of the interval, using the same workout unit from the
     *                 first interval (constructor).
     */
    public void addInterval(int restInterval, int distance) {
        isSingleWorkout = false;
        intervals.add(new WorkoutInterval(restInterval, distance, unit));
    }


    /**
     * Returns true if this is a single workout (only one interval).
     *
     * @return True if this is a single workout.
     */
    public boolean isSingleWorkout() {
        return isSingleWorkout;
    }


    /**
     * Returns true if this is an interval workout (more than one interval).
     *
     * @return True if this is an interval workout.
     */
    public boolean isIntervalWorkout() {
        return !isSingleWorkout;
    }


    /**
     * Returns the unit of the workout distance/duration.
     *
     * @return The unit of the workout distance/duration.
     */
    public WorkoutUnit getUnit() {
        return unit;
    }


    /**
     * Returns the intervals of the workout.
     *
     * @return The intervals of the workout.
     */
    public List<WorkoutInterval> getWorkoutIntervals() {
        return intervals;
    }


    @Override
    public String toString() {
        return toStringHelper(this)
                .add("unit", unit)
                .add("intervals(size)", intervals.size())
                .add("isSingleWorkout", isSingleWorkout)
                .toString();
    }

}