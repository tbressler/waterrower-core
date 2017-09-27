package de.tbressler.waterrower.utils;

import org.junit.Test;

import static de.tbressler.waterrower.utils.MessageUtils.*;
import static org.junit.Assert.*;

/**
 * Tests for class MessageUtils.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMessageUtils {

    // ACD to int:

    @Test(expected = NullPointerException.class)
    public void acdToInt_withNull_throwsNPE() {
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

    // Int to ACD:

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

    // ACH to int:

    @Test(expected = NullPointerException.class)
    public void achToInt_withNull_throwsNPE() {
        achToInt(null);
    }

    @Test(expected = NumberFormatException.class)
    public void achToInt_withInvalidHexValues_throwsNumberFormatException() {
        achToInt("YZ");
    }

    @Test
    public void achToInt_withF_returns15() {
        assertEquals(15, achToInt("F"));
    }

    @Test
    public void achToInt_with0F_returns15() {
        assertEquals(15, achToInt("0F"));
    }

    @Test
    public void achToInt_withFF_returns255() {
        assertEquals(255, achToInt("FF"));
    }

    @Test
    public void achToInt_with00_returns0() {
        assertEquals(0, achToInt("00"));
    }

    @Test
    public void achToInt_with0_returns0() {
        assertEquals(0, achToInt("0"));
    }

    @Test
    public void achToInt_withD00_returns0() {
        assertEquals(3328, achToInt("D00"));
    }

    // Int to ACH:

    @Test
    public void intToAch_with0ValueAnd1Chars_returns0() {
        assertEquals("0", intToAch(0, 1));
    }

    @Test
    public void intToAch_with8ValueAnd1Chars_returns8() {
        assertEquals("8", intToAch(8, 1));
    }

    @Test
    public void intToAch_with15ValueAnd1Chars_returnsF() {
        assertEquals("F", intToAch(15, 1));
    }

    @Test
    public void intToAch_with16ValueAnd2Chars_returns10() {
        assertEquals("10", intToAch(16, 2));
    }

    @Test
    public void intToAch_with3328ValueAnd3Chars_returnsD00() {
        assertEquals("D00", intToAch(3328, 3));
    }

    @Test(expected = NumberFormatException.class)
    public void intToAch_with3328ValueAnd2Chars_throwsNumberFormatException() {
        intToAch(3328, 2);
    }

    @Test
    public void intToAch_with15ValueAnd2Chars_returns0F() {
        assertEquals("0F", intToAch(15, 2));
    }

    @Test
    public void intToAch_with16ValueAnd3Chars_returns010() {
        assertEquals("010", intToAch(16, 3));
    }

    // Boolean from Byte:

    @Test(expected = IllegalArgumentException.class)
    public void getBooleanFromByte_withValueGreaterThan0xFF_throwsIAE() throws Exception {
        getBooleanFromByte(0x100, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooleanFromByte_withValueLowerThan0x00_throwsIAE() throws Exception {
        getBooleanFromByte(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooleanFromByte_withIndexLowerThan0_throwsIAE() throws Exception {
        getBooleanFromByte(0x00, -1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getBooleanFromByte_withIndexGreaterThan7_throwsIAE() throws Exception {
        getBooleanFromByte(0x00, 8);
    }

    @Test
    public void getBooleanFromByte_withValue0x00AndIndex0to7_returnsFalse() throws Exception {
        for (int i=0; i<=7; i++)
            assertFalse(getBooleanFromByte(0x00, i));
    }

    @Test
    public void getBooleanFromByte_withValue0xFFAndIndex0to7_returnsTrue() throws Exception {
        for (int i=0; i<=7; i++)
            assertTrue(getBooleanFromByte(0xFF, i));
    }

    @Test
    public void getBooleanFromByte_withValue0x01AndIndex0_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x01, 0));
    }

    @Test
    public void getBooleanFromByte_withValue0x02AndIndex0_returnsFalse() throws Exception {
        assertFalse(getBooleanFromByte(0x02, 0));
    }

    @Test
    public void getBooleanFromByte_withValue0x02AndIndex1_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x02, 1));
    }

    @Test
    public void getBooleanFromByte_withValue0x04AndIndex2_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x04, 2));
    }

    @Test
    public void getBooleanFromByte_withValue0x08AndIndex3_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x08, 3));
    }

    @Test
    public void getBooleanFromByte_withValue0x10AndIndex4_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x10, 4));
    }

    @Test
    public void getBooleanFromByte_withValue0x20AndIndex5_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x20, 5));
    }

    @Test
    public void getBooleanFromByte_withValue0x40AndIndex6_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x40, 6));
    }

    @Test
    public void getBooleanFromByte_withValue0x80AndIndex7_returnsTrue() throws Exception {
        assertTrue(getBooleanFromByte(0x80, 7));
    }

}
