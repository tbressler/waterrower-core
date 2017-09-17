package de.tbressler.waterrower.model;

/**
 * Memory locations.
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
    MEMORY_DISTANCE_HI(0x058);      // hi byte of meters and km (65535meters max)


    private final int location;

    MemoryLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }

}
