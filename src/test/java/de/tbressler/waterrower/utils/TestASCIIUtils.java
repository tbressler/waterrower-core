package de.tbressler.waterrower.utils;

import org.junit.Test;

import static de.tbressler.waterrower.utils.ASCIIUtils.acdToInt;
import static de.tbressler.waterrower.utils.ASCIIUtils.intToAcd;
import static org.junit.Assert.assertEquals;

/**
 * Tests for class ASCIIUtils.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestASCIIUtils {

    @Test(expected = NullPointerException.class)
    public void acdToInt_withNull_throwsException() {
        acdToInt(null);
    }

    @Test(expected = NumberFormatException.class)
    public void acdToInt_withText_throwsNumberFormatException() {
        acdToInt("Text");
    }

    @Test
    public void acdToInt_withDecimal1_returnsInteger1() {
        assertEquals(1, acdToInt("1"));
    }

    @Test
    public void acdToInt_withDecimal123_returnsInteger123() {
        assertEquals(123, acdToInt("123"));
    }

    @Test
    public void acdToInt_withDecimal101_returnsInteger101() {
        assertEquals(101, acdToInt("101"));
    }

    @Test
    public void acdToInt_withDecimal001_returnsInteger1() {
        assertEquals(1, acdToInt("001"));
    }


    @Test
    public void intToAcd_with0ValueAnd1Chars_returnsString0() {
        assertEquals("0", intToAcd(0, 1));
    }

    @Test
    public void intToAcd_with2ValueAnd1Chars_returnsString2() {
        assertEquals("2", intToAcd(2, 1));
    }

    @Test
    public void intToAcd_with123ValueAnd3Chars_returnsString123() {
        assertEquals("123", intToAcd(123, 3));
    }

    @Test(expected = NumberFormatException.class)
    public void intToAcd_with123ValueAnd2Chars_throwsNumberFormatException() {
        intToAcd(123, 2);
    }

    @Test(expected = NumberFormatException.class)
    public void intToAcd_withSomeValueAnd0Chars_throwsNumberFormatException() {
        intToAcd(123, 0);
    }

    @Test
    public void intToAcd_with1And3Chars_returnsString001() {
        assertEquals("001", intToAcd(1, 3));
    }

    @Test
    public void intToAcd_with1And2Chars_returnsString01() {
        assertEquals("01", intToAcd(1, 2));
    }

    @Test
    public void intToAcd_with12And3Chars_returnsString012() {
        assertEquals("012", intToAcd(12, 3));
    }

}
