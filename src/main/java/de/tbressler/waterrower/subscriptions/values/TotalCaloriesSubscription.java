package de.tbressler.waterrower.subscriptions.values;

import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.subscriptions.AbstractMemorySubscription;

import static de.tbressler.waterrower.io.msg.Memory.TRIPLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.TOTAL_KCAL_LOW;
import static de.tbressler.waterrower.subscriptions.Priority.HIGH;
import static de.tbressler.waterrower.utils.MessageUtils.intFromUpHighAndLow;

/**
 * Subscription for the value of the total calories.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class TotalCaloriesSubscription extends AbstractMemorySubscription {

    /* The last stroke count received. */
    private int lastValue = -1;


    /**
     * Subscription for the value of the total calories.
     */
    public TotalCaloriesSubscription() {
        super(HIGH, TRIPLE_MEMORY, TOTAL_KCAL_LOW);
    }


    @Override
    protected final void handle(DataMemoryMessage msg) {

        int kcal = intFromUpHighAndLow(msg.getValue3(), msg.getValue2(), msg.getValue1());

        // If the received kcal is the same as before,
        // don't send an update.
        if (lastValue == kcal)
            return;
        lastValue = kcal;

        onCaloriesUpdated(kcal);
    }


    /**
     * Is called if the value for the total calories was updated.
     *
     * @param cal The new value (in cal).
     */
    abstract protected void onCaloriesUpdated(int cal);

}
