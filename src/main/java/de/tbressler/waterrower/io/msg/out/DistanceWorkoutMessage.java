package de.tbressler.waterrower.io.msg.out;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.model.WorkoutUnit;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * @author Tobias Bressler
 * @version 1.0
 */
public class DistanceWorkoutMessage extends AbstractMessage {

    private final int distance;
    private final WorkoutUnit unit;


    public DistanceWorkoutMessage(int distance, WorkoutUnit unit) {
        this.distance = checkDistance(distance, unit);
        this.unit = requireNonNull(unit);
    }

    /* Check if distance is in range. */
    private int checkDistance(int distance, WorkoutUnit unit) {
        switch(unit) {
            case METER:
            case MILE:
            case KM:
                // When X = METER, MILE or KM: this value is in Meters, the display value for
                // miles is a conversion and valid values are 0001 to FA00.
                if ((distance < 0x0001) || (distance > 0xFA00))
                    throw new IllegalArgumentException("The distance must be between 0x0001 and 0xFA00!");
                break;
            case STROKE:
                // When X = STROKE this value is the number of strokes and valid values are
                // 0001 to 1388.
                if ((distance < 0x0001) || (distance > 0x1388))
                    throw new IllegalArgumentException("The distance must be between 0x0001 and 0x1388!");
                break;
        }
        return distance;
    }


    public int getDistance() {
        return distance;
    }


    public WorkoutUnit getUnit() {
        return unit;
    }


    @Override
    public String toString() {
        return toStringHelper(this)
                .add("distance", distance)
                .add("unit", unit)
                .toString();
    }

}
