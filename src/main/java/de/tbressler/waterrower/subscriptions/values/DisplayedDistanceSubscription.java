package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.subscriptions.AbstractMemorySubscription;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.DISTANCE_LOW;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for the displayed distance on the distance window of the Performance Monitor.
 *
 * The distance window displays the distance covered (or distance to be covered in a
 * distance workout).
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class DisplayedDistanceSubscription extends AbstractMemorySubscription {

        /* The last distance received. */
        private int lastDistance = -1;

        /**
         * Subscription to the displayed distance values.
         */
        public DisplayedDistanceSubscription() {
            super(DOUBLE_MEMORY, DISTANCE_LOW);
        }

        @Override
        protected final void handle(DataMemoryMessage msg) {

            int distance = intFromHighAndLow(msg.getValue2(), msg.getValue1());

            // If the received distance is the same as before,
            // don't send an update.
            if (lastDistance == distance)
                return;
            lastDistance = distance;

            // Notify update.
            onDistanceUpdated(distance);
        }

        /**
         * Is called if the value for the displayed distance was updated.
         *
         * @param distance The new distance (in meter).
         */
        abstract protected void onDistanceUpdated(int distance);

}
