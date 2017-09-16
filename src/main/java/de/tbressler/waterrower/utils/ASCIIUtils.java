package de.tbressler.waterrower.utils;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;
import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;

/**
 * Utils for conversion of ASCII data.
 * The class supports:
 * - ACD (ASCII coded decimal)
 * - ACH (ASCII coded hexadecimal)
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ASCIIUtils {

    /* Private constructor. */
    private ASCIIUtils() {}


    /**
     * Returns the ACD (ASCII coded decimal) as int value.
     *
     * @param ascii The ACD value, must not be null.
     * @return The integer value.
     *
     * @throws NumberFormatException If not a decimal value.
     */
    public static int acdToInt(String ascii) throws NumberFormatException {
        return parseInt(requireNonNull(ascii));
    }


    /**
     * Returns the int value as ACD (ASCII coded decimal).
     *
     * @param value The int value.
     * @param chars The number of chars.
     * @return The int value as ACD.
     *
     * @throws NumberFormatException If the int has more characters than the number of chars given.
     */
    public static String intToAcd(int value, int chars) throws NumberFormatException {
        String ascii = valueOf(value);
        return addLeadingZeros(chars, ascii);
    }


    /**
     * Returns the ACH (ASCII coded hexadecimal) as int value.
     *
     * @param ascii The ACH value, must not be null.
     * @return The integer value.
     *
     * @throws NumberFormatException If not a hexadecimal value.
     */
    public static int achToInt(String ascii) throws NumberFormatException {
        return parseInt(requireNonNull(ascii), 16);
    }


    /**
     * Returns the int value as ACH (ASCII coded hexadecimal).
     *
     * @param value The int value.
     * @param chars The number of chars.
     * @return The int value as ACH.
     *
     * @throws NumberFormatException If the int has more characters than the number of chars given.
     */
    public static String intToAch(int value, int chars) throws NumberFormatException {
        String ascii = toHexString(value).toUpperCase();
        return addLeadingZeros(chars, ascii);
    }


    /* Add leading zeros to string. */
    private static String addLeadingZeros(int chars, String ascii) {
        int numberOfLeadingZeros = chars - ascii.length();
        if (numberOfLeadingZeros < 0)
            throw new NumberFormatException("Number has more than "+chars+" characters!");
        for(int i=0; i<numberOfLeadingZeros; i++)
            ascii = "0"+ascii;
        return ascii;
    }

}
