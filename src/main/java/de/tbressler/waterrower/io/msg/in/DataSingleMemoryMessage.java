package de.tbressler.waterrower.io.msg.in;

import de.tbressler.waterrower.io.msg.Memory;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;

import static com.google.common.base.MoreObjects.toStringHelper;
import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;

/**
 * Value from a single memory location (S4/S5 -> PC).
 *
 * Returns the single byte of data Y1 from location XXX for the users application.
 *
 * The read packets will retrieve values from the rowing-computer memory, these locations are
 * raw data which maybe a decimal, hexadecimal, binary or BCD format, each will be returned in
 * ACH format in the packet. Correct conversion and usage will be needed for the PC application
 * to use the values.
 *
 * XXX is in ACH format and has a maximum range of 0x000 to 0xFFF, however not all locations are
 * available (see Memory Map), errors will be replied for out of spec memory reads.
 *
 * [I][DS] + XXX + Y1 + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class DataSingleMemoryMessage extends ReadMemoryMessage {


    /* The single byte of data (0 .. 255) from memory location. */
    private final int value;


    /**
     * This message returns the single byte of data from memory location for the users
     * application.
     *
     * @param location The memory location (0 .. 4095), please refer to memory map of the Water
     *                 Rower monitor.
     * @param value The single byte of data (0 .. 255) from memory location.
     */
    public DataSingleMemoryMessage(int location, int value) {
        super(SINGLE_MEMORY, location);
        if ((value < 0) || (value > 255))
            throw new IllegalArgumentException("The value must be between 0 and 255!");
        this.value = value;
    }


    /**
     * Returns the single byte of data from the memory location.
     *
     * @return The single byte of data (0 .. 255) from memory location.
     */
    public int getValue() {
        return value;
    }


    @Override
    public String toString() {
        return toStringHelper(this)
                .add("location", location)
                .add("value", value)
                .toString();
    }

}
