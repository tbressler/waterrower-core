package de.tbressler.waterrower.model;

import static de.tbressler.waterrower.utils.MessageUtils.getBooleanFromByte;

/**
 * Zone words and misc windows flags.
 *  0 = fzone_fg_work
 *  1 = fzone_fg_rest
 *  2 = fmisc_fg_lowbat
 *  3 = fmisc_fg_pc
 *  4 = fmisc_fg_line
 *  5 = fmisc_fg_mmc_cd
 *  6 = fmisc_fg_mmc_up
 *  7 = fmisc_fg_mmc_dn
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class MiscFlags {

    /* The flag values as byte. */
    private final int value;


    /**
     * Zone words and misc windows flags.
     *
     * @param value The flags as byte.
     */
    public MiscFlags(int value) {
        if ((value < 0x00) || (value > 0xFF))
            throw new IllegalArgumentException("Value must be in range 0x00 to 0xFF!");
        this.value = value;
    }


    /**
     *
     *
     * @return
     */
    public boolean isZoneWork() {
        return getBooleanFromByte(value, 0);
    }

    /**
     *
     *
     * @return
     */
    public boolean isZoneRest() {
        return getBooleanFromByte(value, 1);
    }


    public boolean isMiscLowBat() {
        return getBooleanFromByte(value, 2);
    }

    public boolean isMiscPC() {
        return getBooleanFromByte(value, 3);
    }

    public boolean isMiscLine() {
        return getBooleanFromByte(value, 4);
    }

    public boolean isMiscMmcCd() {
        return getBooleanFromByte(value, 5);
    }

    public boolean isMiscMmcUp() {
        return getBooleanFromByte(value, 6);
    }

    public boolean isMiscMmcDn() {
        return getBooleanFromByte(value, 7);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MiscFlags that = (MiscFlags) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

}
