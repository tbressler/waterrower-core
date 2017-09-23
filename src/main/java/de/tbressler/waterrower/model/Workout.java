package de.tbressler.waterrower.model;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * A workout configuration.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class Workout {

    /* True if this workout is a single workout. */
    private boolean isSingleWorkout = true;


    /**
     * A workout configuration.
     *
     * @param distance
     * @param unit
     */
    public Workout(int distance, WorkoutUnit unit) {

    }


    public void addInterval(int distance, int restInterval) {
        isSingleWorkout = false;

    }


    public boolean isSingleWorkout() {
        return isSingleWorkout;
    }


    public boolean isIntervalWorkout() {
        return !isSingleWorkout;
    }


    @Override
    public String toString() {
        return toStringHelper(this)
                .toString();
    }

}