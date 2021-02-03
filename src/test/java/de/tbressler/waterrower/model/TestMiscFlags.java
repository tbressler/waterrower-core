package de.tbressler.waterrower.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for class MiscFlags.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMiscFlags {

    // Constructor:

    @Test(expected = IllegalArgumentException.class)
    public void new_withTooLowValue_throwsIAE() {
        new MiscFlags(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void new_withTooHighValue_throwsIAE() {
        new MiscFlags(0x100);
    }


    // Methods:

    @Test
    public void isZoneWork_with0x01_returnsTrue() {
        assertTrue(new MiscFlags(0x01).isZoneWork());
    }

    @Test
    public void isZoneWork_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isZoneWork());
    }


    @Test
    public void isZoneRest_with0x02_returnsTrue() {
        assertTrue(new MiscFlags(0x02).isZoneRest());
    }

    @Test
    public void isZoneRest_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isZoneRest());
    }


    @Test
    public void isMiscLowBat_with0x04_returnsTrue() {
        assertTrue(new MiscFlags(0x04).isMiscLowBat());
    }

    @Test
    public void isMiscLowBat_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isMiscLowBat());
    }


    @Test
    public void isMiscPC_with0x08_returnsTrue() {
        assertTrue(new MiscFlags(0x08).isMiscPC());
    }

    @Test
    public void isMiscPC_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isMiscPC());
    }


    @Test
    public void isMiscLine_with0x10_returnsTrue() {
        assertTrue(new MiscFlags(0x10).isMiscLine());
    }

    @Test
    public void isMiscLine_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isMiscLine());
    }


    @Test
    public void isMiscMmcCd_with0x20_returnsTrue() {
        assertTrue(new MiscFlags(0x20).isMiscMmcCd());
    }

    @Test
    public void isMiscMmcCd_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isMiscMmcCd());
    }


    @Test
    public void isMiscMmcUp_with0x40_returnsTrue() {
        assertTrue(new MiscFlags(0x40).isMiscMmcUp());
    }

    @Test
    public void isMiscMmcUp_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isMiscMmcUp());
    }


    @Test
    public void isMiscMmcDn_with0x80_returnsTrue() {
        assertTrue(new MiscFlags(0x80).isMiscMmcDn());
    }

    @Test
    public void isMiscMmcDn_with0x00_returnsFalse() {
        assertFalse(new MiscFlags(0x00).isMiscMmcDn());
    }


    // Equals / hash code:

    @Test
    public void equals_testEqualsContract() {
        MiscFlags a = new MiscFlags(1);
        MiscFlags b = new MiscFlags(1);
        MiscFlags c = new MiscFlags(2);
        Integer d = Integer.valueOf(2);

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
        assertFalse(a.equals(null));
    }

    @Test
    public void equals_testHashCodeContract() {
        MiscFlags a = new MiscFlags(1);
        MiscFlags b = new MiscFlags(1);
        MiscFlags c = new MiscFlags(2);

        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }

}