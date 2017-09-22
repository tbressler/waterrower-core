package de.tbressler.waterrower.model;

/**
 * Memory locations of the WaterRower S4, version 2.00.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public enum MemoryLocation {

    /*
     * Flags:
     *
     * These registers can be read to determine the current state of the display; they are formed of
     * 8 separate true or false flags. These flags are defined at the end of the memory map.
     */

    FEXTENDED(0x03e),           // working and workout control flags
                                // bits for extended zones and workout modes:
                                //   0 = fzone_hr fextended; working in heartrate zone
                                //   1 = fzone_int fextended; working in intensity zone
                                //   2 = fzone_sr fextended; working in strokerate zone
                                //   3 = fprognostics fextended; prognostics active.
                                //   4 = fworkout_dis fextended; workout distance mode
                                //   5 = fworkout_dur fextended; workout duration mode
                                //   6 = fworkout_dis_i fextended; workout distance interval mode
                                //   7 = fworkout_dur_i fextended; workout duration interval mode

    /*
     * Variables:
     *
     * The following memory locations are available to the user for reading. Other locations not specified are
     * unavailable for reading. A lot of the timers count 1bit per 25mS of actual time, remember this for ALL maths,
     * otherwise things can be confusing of how much time say a stroke was done IN.
     */

    /* Distance variables: */

    MS_DISTANCE_DEC(0x054),     // 0.1m count (only counts up from 0-9
    MS_DISTANCE_LOW(0x055),     // low byte of meters
    MS_DISTANCE_HI(0x056),      // hi byte of meters and km (65535meters max)

    /* This is the displayed distance: */

    DISTANCE_LOW(0x057),        // low byte of meters
    DISTANCE_HI(0x058),         // hi byte of meters and km (65535meters max)

    KCAL_WATTS_LOW(0x088),
    KCAL_WATTS_HI(0x089),
    TOTAL_KCAL_LOW(0x08a),
    TOTAL_KCAL_HI(0x08b),
    TOTAL_KCAL_UP(0x08c),

    /* Total distance meter counter - this is stored at switch off: */

    TOTAL_DIS_DEC(0x080),       // dec byte of meters
    TOTAL_DIS_LOW(0x081),       // low byte of meters
    TOTAL_DIS_HI(0x082),        // hi byte of meters and km (65535meters max)

    /* Tank volume in liters: */

    TANK_VOLUME(0x0a9),         // volume of water in tank

    /* Stroke counter: */

    STROKES_CNT_LOW(0x140),     // low byte count
    STROKES_CNT_HI(0x141),      // high byte count
    STROKE_AVERAGE(0x142),      // average time for a whole stroke
    STROKE_PULL(0x143),         // average time for a pull (acc to dec)

    // Stroke_pull is first subtracted from stroke_average then a modifier of 1.25 multiplied
    // by the result to generate the ratio value for display.

    /* Meters per second registers: */

    M_S_LOW_TOTAL(0x148),       // total distance per second in cm low byte
    M_S_HI_TOTAL(0x149),        // total distance per second in cm hi byte
    M_S_LOW_AVERAGE(0x14a),     // instant average distance in cm low byte
    M_S_HI_AVERAGE(0x14b),      // instant average distance in cm hi byte
    M_S_STORED(0x14c),          // no. of the stored values.
    M_S_PROJL_AVG(0x14d),       // all average for projected distance/duration maths
    M_S_PROJH_AVG(0x14e),       // all average for projected distance/duration maths

    /* Used to generate the display clock: */

    DISPLAY_SEC_DEC(0x1e0),     // seconds 0.0-0.9
    DISPLAY_SEC(0x1e1),         // seconds 0-59
    DISPLAY_MIN(0x1e2),         // minutes 0-59
    DISPLAY_HR(0x1e3),          // hours 0-9 only

    /* Workout total times/distances/limits: */

    WORKOUT_TIMEL(0x1e8),       // total workout time
    WORKOUT_TIMEH(0x1e9),
    WORKOUT_MS_L(0x1ea),        // total workout m/s
    WORKOUT_MS_H(0x1eb),
    WORKOUT_STROKEL(0x1ec),     // total workout strokes
    WORKOUT_STROKEH(0x1ed),
    WORKOUT_LIMIT_H(0x1ee),     // this is the limit value for workouts
    WORKOUT_LIMIT_L(0x1ef);



    /* The memory location as decimal. */
    private final int location;

    /**
     * Constructor of the enum.
     *
     * @param location The memory location as decimal (0x000 .. 0xFFF).
     */
    MemoryLocation(int location) {
        if ((location < 0x000) || (location > 0xFFF))
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
