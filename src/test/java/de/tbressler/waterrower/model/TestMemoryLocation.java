package de.tbressler.waterrower.model;

import org.junit.Test;

import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.utils.MessageUtils.intToAch;
import static org.junit.Assert.assertEquals;

/**
 * Tests for enum MemoryLocation.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMemoryLocation {

    /* Check all memory locations of the enums: */

    @Test public void fextended() { assertMemoryLocation(FEXTENDED, "03E"); }

    @Test public void ms_distance_dec() { assertMemoryLocation(MS_DISTANCE_DEC, "054"); }
    @Test public void ms_distance_low() { assertMemoryLocation(MS_DISTANCE_LOW, "055"); }
    @Test public void ms_distance_hi() { assertMemoryLocation(MS_DISTANCE_HI, "056"); }

    @Test public void distance_low() { assertMemoryLocation(DISTANCE_LOW, "057"); }
    @Test public void distance_hi() { assertMemoryLocation(DISTANCE_HI, "058"); }

    @Test public void total_dis_dec() { assertMemoryLocation(TOTAL_DIS_DEC, "080"); }
    @Test public void total_dis_low() { assertMemoryLocation(TOTAL_DIS_LOW, "081"); }
    @Test public void total_dis_hi() { assertMemoryLocation(TOTAL_DIS_HI, "082"); }

    @Test public void kcal_watts_low() { assertMemoryLocation(KCAL_WATTS_LOW, "088"); }
    @Test public void kcal_watts_hi() { assertMemoryLocation(KCAL_WATTS_HI, "089"); }
    @Test public void total_kcal_low() { assertMemoryLocation(TOTAL_KCAL_LOW, "08A"); }
    @Test public void total_kcal_hi() { assertMemoryLocation(TOTAL_KCAL_HI, "08B"); }
    @Test public void total_kcal_up() { assertMemoryLocation(TOTAL_KCAL_UP, "08C"); }

    @Test public void tank_volume() { assertMemoryLocation(TANK_VOLUME, "0A9"); }

    @Test public void strokes_cnt_low() { assertMemoryLocation(STROKES_CNT_LOW, "140"); }
    @Test public void strokes_cnt_hi() { assertMemoryLocation(STROKES_CNT_HI, "141"); }
    @Test public void stroke_average() { assertMemoryLocation(STROKE_AVERAGE, "142"); }
    @Test public void stroke_pull() { assertMemoryLocation(STROKE_PULL, "143"); }

    @Test public void m_s_low_total() { assertMemoryLocation(M_S_LOW_TOTAL, "148"); }
    @Test public void m_s_hi_total() { assertMemoryLocation(M_S_HI_TOTAL, "149"); }
    @Test public void m_s_low_average() { assertMemoryLocation(M_S_LOW_AVERAGE, "14A"); }
    @Test public void m_s_hi_average() { assertMemoryLocation(M_S_HI_AVERAGE, "14B"); }
    @Test public void m_s_stored() { assertMemoryLocation(M_S_STORED, "14C"); }
    @Test public void m_s_projl_avg() { assertMemoryLocation(M_S_PROJL_AVG, "14D"); }
    @Test public void m_s_projh_avg() { assertMemoryLocation(M_S_PROJH_AVG, "14E"); }

    @Test public void display_sec_dec() { assertMemoryLocation(DISPLAY_SEC_DEC, "1E0"); }
    @Test public void display_sec() { assertMemoryLocation(DISPLAY_SEC, "1E1"); }
    @Test public void display_min() { assertMemoryLocation(DISPLAY_MIN, "1E2"); }
    @Test public void display_hr() { assertMemoryLocation(DISPLAY_HR, "1E3"); }

    @Test public void workout_timel() { assertMemoryLocation(WORKOUT_TIMEL, "1E8"); }
    @Test public void workout_timeh() { assertMemoryLocation(WORKOUT_TIMEH, "1E9"); }
    @Test public void workout_ms_l() { assertMemoryLocation(WORKOUT_MS_L, "1EA"); }
    @Test public void workout_ms_h() { assertMemoryLocation(WORKOUT_MS_H, "1EB"); }
    @Test public void workout_strokel() { assertMemoryLocation(WORKOUT_STROKEL, "1EC"); }
    @Test public void workout_strokeh() { assertMemoryLocation(WORKOUT_STROKEH, "1ED"); }
    @Test public void workout_limit_h() { assertMemoryLocation(WORKOUT_LIMIT_H, "1EE"); }
    @Test public void workout_limit_l() { assertMemoryLocation(WORKOUT_LIMIT_L, "1EF"); }


    /* Assert the memory locations. */
    private void assertMemoryLocation(MemoryLocation location, String expected) {
        assertEquals(expected, intToAch(location.getLocation(), 3));
    }

}
