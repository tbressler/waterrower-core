package de.tbressler.waterrower.model;

/**
 * Memory locations of the Water Rower S4, version 2.00.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public enum MemoryLocation {

    /* Distance variables: */

    MEMORY_MS_DISTANCE_DEC(0x054),  // 0.1m count (only counts up from 0-9
    MEMORY_MS_DISTANCE_LOW(0x055),  // low byte of meters
    MEMORY_MS_DISTANCE_HI(0x056),   // hi byte of meters and km (65535meters max)

    /* This is the displayed distance: */

    MEMORY_DISTANCE_LOW(0x057),     // low byte of meters
    MEMORY_DISTANCE_HI(0x058),      // hi byte of meters and km (65535meters max)

    /* Tank volume in liters: */

    MEMORY_TANK_VOLUME(0x0A9),      // volume of water in tank

    /* Stroke counter: */

    MEMORY_STROKES_CNT_LOW(0x140),  // low byte count
    MEMORY_STROKES_CNT_HI(0x141),   // high byte count
    MEMORY_STROKE_AVERAGE(0x142),   // average time for a whole stroke
    MEMORY_STROKE_PULL(0x143);      // average time for a pull (acc to dec)

    // Stroke_pull is first subtracted from stroke_average then a modifier of 1.25 multiplied
    // by the result to generate the ratio value for display.


    /* The memory location as decimal. */
    private final int location;

    /**
     * Constructor of the enum.
     *
     * @param location The memory location as decimal (0x000 .. 0xFFF).
     */
    MemoryLocation(int location) {
        if ((location < 0) || (location > 4095))
            throw new IllegalArgumentException("Invalid memory location! Location must be between 0x000 and 0xFFF.");
        this.location = location;
    }

    /**
     * Returns the memory location as decimal.
     *
     * @return The memory location.
     */
    public int getLocation() {
        return location;
    }

}
