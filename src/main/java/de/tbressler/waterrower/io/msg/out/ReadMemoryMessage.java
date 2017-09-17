package de.tbressler.waterrower.io.msg.out;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.Memory;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * Read a single memory location (PC -> S4/S5).
 *
 * Requests the contents of a single location XXX, this will return a single byte in hex format.
 *
 * XXX is in ACH format and has a maximum range of 0x000 to 0xFFF, however not all locations are
 * available (see Memory Map), errors will be replied for out of spec memory reads.
 *
 * [I][RS] + XXX + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ReadMemoryMessage extends AbstractMessage {


    /* The memory location (0 .. 4095). */
    protected final int location;

    /* Defines if you want to read single, double or triple memory locations. */
    protected final Memory memory;


    /**
     * This message requests the contents of a single location XXX, this will return a single
     * byte in hex format.
     *
     * @param memory Define if you want to read single, double or triple memory locations, must
     *               not be null.
     * @param location The memory location (0 .. 4095), please refer to memory map of the Water
 *                 Rower monitor.
     */
    public ReadMemoryMessage(Memory memory, int location) {
        if ((location < 0) || (location > 4095))
            throw new IllegalArgumentException("The value for the memory location must be between 0 and 4095!");
        this.memory = requireNonNull(memory);
        this.location = location;
    }


    /**
     * Returns the memory location (0 .. 4095), please refer to memory map of the Water Rower
     * monitor.
     *
     * @return The memory location (0 .. 4095).
     */
    public int getLocation() {
        return location;
    }


    /**
     *
     * @return
     */
    public Memory getMemory() {
        return memory;
    }


    @Override
    public String toString() {
        return toStringHelper(this)
                .add("location", location)
                .add("memory", memory)
                .toString();
    }

}
