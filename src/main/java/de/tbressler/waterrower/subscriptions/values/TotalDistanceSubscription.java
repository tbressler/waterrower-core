package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.subscriptions.AbstractMemorySubscription;

import static de.tbressler.waterrower.io.msg.Memory.DOUBLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.TOTAL_DIS_LOW;
import static de.tbressler.waterrower.subscriptions.Priority.LOW;
import static de.tbressler.waterrower.utils.MessageUtils.intFromHighAndLow;

/**
 * Subscription for the total distance values of the Performance Monitor.
 *
 * The value represents the total distance meter counter - this value will be reset to zero when the Performance
 * Monitor is switched off.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class TotalDistanceSubscription extends AbstractMemorySubscription {

        /* The last distance received. */
        private int lastDistance = -1;

        /**
         * Subscription to the displayed distance values.
         */
        public TotalDistanceSubscription() {
            super(LOW, DOUBLE_MEMORY, TOTAL_DIS_LOW);
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
