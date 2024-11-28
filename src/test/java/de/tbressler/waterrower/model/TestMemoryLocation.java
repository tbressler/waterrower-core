package de.tbressler.waterrower.model;

import org.junit.jupiter.api.Test;

import static de.tbressler.waterrower.model.MemoryLocation.*;
import static de.tbressler.waterrower.utils.MessageUtils.intToAch;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test public void clock_down_dec() { assertMemoryLocation(CLOCK_DOWN_DEC, "05A"); }
    @Test public void clock_down_low() { assertMemoryLocation(CLOCK_DOWN_LOW, "05B"); }
    @Test public void clock_down_hi() { assertMemoryLocation(CLOCK_DOWN_HI, "05C"); }

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
    @Test public void workout_limit_l() { assertMemoryLocation(WORKOUT_LIMIT_L, "1EE"); }
    @Test public void workout_limit_h() { assertMemoryLocation(WORKOUT_LIMIT_H, "1EF"); }

    @Test public void workout_work1_l() { assertMemoryLocation(WORKOUT_WORK1_L, "1B0"); };
    @Test public void workout_work1_h() { assertMemoryLocation(WORKOUT_WORK1_H, "1B1"); };
    @Test public void workout_rest1_l() { assertMemoryLocation(WORKOUT_REST1_L, "1B2"); };
    @Test public void workout_rest1_h() { assertMemoryLocation(WORKOUT_REST1_H, "1B3"); };
    @Test public void workout_work2_l() { assertMemoryLocation(WORKOUT_WORK2_L, "1B4"); };
    @Test public void workout_work2_h() { assertMemoryLocation(WORKOUT_WORK2_H, "1B5"); };
    @Test public void workout_rest2_l() { assertMemoryLocation(WORKOUT_REST2_L, "1B6"); };
    @Test public void workout_rest2_h() { assertMemoryLocation(WORKOUT_REST2_H, "1B7"); };
    @Test public void workout_work3_l() { assertMemoryLocation(WORKOUT_WORK3_L, "1B8"); };
    @Test public void workout_work3_h() { assertMemoryLocation(WORKOUT_WORK3_H, "1B9"); };
    @Test public void workout_rest3_l() { assertMemoryLocation(WORKOUT_REST3_L, "1BA"); };
    @Test public void workout_rest3_h() { assertMemoryLocation(WORKOUT_REST3_H, "1BB"); };
    @Test public void workout_work4_l() { assertMemoryLocation(WORKOUT_WORK4_L, "1BC"); };
    @Test public void workout_work4_h() { assertMemoryLocation(WORKOUT_WORK4_H, "1BD"); };
    @Test public void workout_rest4_l() { assertMemoryLocation(WORKOUT_REST4_L, "1BE"); };
    @Test public void workout_rest4_h() { assertMemoryLocation(WORKOUT_REST4_H, "1BF"); };
    @Test public void workout_work5_l() { assertMemoryLocation(WORKOUT_WORK5_L, "1C0"); };
    @Test public void workout_work5_h() { assertMemoryLocation(WORKOUT_WORK5_H, "1C1"); };
    @Test public void workout_rest5_l() { assertMemoryLocation(WORKOUT_REST5_L, "1C2"); };
    @Test public void workout_rest5_h() { assertMemoryLocation(WORKOUT_REST5_H, "1C3"); };
    @Test public void workout_work6_l() { assertMemoryLocation(WORKOUT_WORK6_L, "1C4"); };
    @Test public void workout_work6_h() { assertMemoryLocation(WORKOUT_WORK6_H, "1C5"); };
    @Test public void workout_rest6_l() { assertMemoryLocation(WORKOUT_REST6_L, "1C6"); };
    @Test public void workout_rest6_h() { assertMemoryLocation(WORKOUT_REST6_H, "1C7"); };
    @Test public void workout_work7_l() { assertMemoryLocation(WORKOUT_WORK7_L, "1C8"); };
    @Test public void workout_work7_h() { assertMemoryLocation(WORKOUT_WORK7_H, "1C9"); };
    @Test public void workout_rest7_l() { assertMemoryLocation(WORKOUT_REST7_L, "1CA"); };
    @Test public void workout_rest7_h() { assertMemoryLocation(WORKOUT_REST7_H, "1CB"); };
    @Test public void workout_work8_l() { assertMemoryLocation(WORKOUT_WORK8_L, "1CC"); };
    @Test public void workout_work8_h() { assertMemoryLocation(WORKOUT_WORK8_H, "1CD"); };
    @Test public void workout_rest8_l() { assertMemoryLocation(WORKOUT_REST8_L, "1CE"); };
    @Test public void workout_rest8_h() { assertMemoryLocation(WORKOUT_REST8_H, "1CF"); };
    @Test public void workout_work9_l() { assertMemoryLocation(WORKOUT_WORK9_L, "1D0"); };
    @Test public void workout_work9_h() { assertMemoryLocation(WORKOUT_WORK9_H, "1D1"); };

    @Test public void workout_inter() { assertMemoryLocation(WORKOUT_INTER, "1D9"); };


    /* Assert the memory locations. */
    private void assertMemoryLocation(MemoryLocation location, String expected) {
        assertEquals(expected, intToAch(location.getLocation(), 3));
    }

}
