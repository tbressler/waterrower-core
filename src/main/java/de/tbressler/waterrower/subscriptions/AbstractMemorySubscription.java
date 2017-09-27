package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.Memory;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.model.MemoryLocation;

import static de.tbressler.waterrower.log.Log.LIBRARY;
import static java.util.Objects.requireNonNull;

/**
 * An abstract subscription for memory locations.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
abstract class AbstractMemorySubscription implements ISubscription {

    /* Single, double or triple memory. */
    private final Memory memory;

    /* The memory location. */
    private final MemoryLocation location;


    /**
     * An abstract subscription for memory locations.
     *
     * @param memory Single, double or triple memory. Must not be null.
     * @param location The memory location, must not be null.
     */
    AbstractMemorySubscription(Memory memory, MemoryLocation location) {
        this.memory = requireNonNull(memory);
        this.location = requireNonNull(location);
    }

    @Override
    public final AbstractMessage poll() {
        return new ReadMemoryMessage(memory, location.getLocation());
    }

    @Override
    public final void handle(AbstractMessage msg) {
        if (!(msg instanceof DataMemoryMessage))
            return;

        DataMemoryMessage dataMemoryMessage = (DataMemoryMessage) msg;

        // Check if memory location and memory type matches:
        if (dataMemoryMessage.getLocation() != location.getLocation())
            return;
        if (dataMemoryMessage.getMemory() != memory) {
            Log.warn(LIBRARY, "Received message has memory type '"+dataMemoryMessage.getMemory()+"', but expected is '"+memory+"'!");
            return;
        }

        handle(dataMemoryMessage);
    }

    /**
     * Called if a memory message was received, which is for the location and memory type given.
     *
     * @param msg The message, never null.
     */
    abstract void handle(DataMemoryMessage msg);

}
