/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.ai.air;

public class ManString {

    public ManString() {
    }

    public static String name(int i) {
        if (i >= MList.length) return "<-Special->";
        else return MList[i];
    }

    public static String tname(int i) {
        return TList[i];
    }

    public static String WaName(int i) {
        return WList[i];
    }

    public static String sMName(int i) {
        return sMList[i];
    }

    public static String wpname(int i) {
        return WPList[i];
    }

    private static String MList[]  = { "NONE         ", "HOLD         ", "PULL_UP      ", "LEVEL_PLANE  ", "ROLL         ", "ROLL_90      ", "ROLL_180     ", "SPIRAL_BRAKE ", "SPIRAL_UP    ", "SPIRAL_DOWN  ", "CLIMB        ", "DIVING_0_RPM ",
            "DIVING_30DEG ", "DIVING_45DEG ", "TURN         ", "MIL_TURN     ", "LOOP         ", "LOOP_DOWN    ", "HALF_LOOP_UP ", "HALF_LOOP_DN ", "STALL        ", "WAYPOINT     ", "SPEEDUP      ", "BELL         ", "FOLLOW       ", "LANDING      ",
            "TAKEOFF      ", "ATTACK       ", "WAVEOUT      ", "SINUS        ", "ZIGZAG_UP    ", "ZIGZAG_DOWN  ", "ZIGZAG_SPIT  ", "HLF_LP_DN135 ", "HARTMANN     ", "ROLL_360     ", "STALL_PKRSN  ", "BARREL_PKRSN ", "SLIDE_LEVEL  ", "SLIDE_DESCNT ",
            "RANVERSMAN   ", "CUBAN        ", "CUBAN_INVERT ", "GATTACK      ", "PLT_OFFLINE  ", "HANG_ON      ", "KAMIKAZE     ", "ATTACK_B_HAR ", "DELAY        ", "DITCH        ", "DIVE_BOMBING ", "TORPEDO_DROP ", "CASSETTE_BMB ", "FAR_FOLLOW   ",
            "SPIRAL_DN_SL ", "SPIRALUPSYNC ", "SINUS_SHALOW ", "GAIN         ", "SEPARATE     ", "BE_NEAR      ", "EVADE_UP     ", "EVADE_DOWN   ", "ENERGY ATTACK", "ATTACK BOMBER", "ENGINE RUNUP ", "COVER        ", "TAXI         ", "RUN_AWAY     ",
            "FAR_COVER    ", "VTOL_TAKEOFF ", "VTOL_TD      ", "GATTACK_HS293      ", "GA_FRITZX     ", "GA_TORP_TOKG     ", "COVER_DNB      ", "COVER_LD      ", "COVER_AGR      ", "TURN_HARD      ", "CLOUDS          ", "EVADE_ATTACK", "BRACKET_ATTACK",
            "DOUBLE_ATTACK", "BE_NEAR_LOW", "BARREL_ROLL", "PULL_UP_EMERGENCY", "DIVING_90_DEG", "SMOOTH_LVL", "IVAN", "FISHTAIL_LEFT", "FISHTAIL_RIGHT", "LOOKDOWN_LEFT", "LOOKDOWN_RIGHT", "LINE_ATTACK", "BOX_ATTACK", "BANG_BANG", "PANIC_MANIC",
            "PANIC_FREEZE", "COMBAT_CLIMB", "HIT_AND_RUN", "SEEK_AND_DESTROY", "BREAK_AWAY", "ATTACK_HARD", "TAXI_TO_TO", "MIL_TURN_LEFT", "MIL_TURN_RIGHT", "STRAIGHT_AND_LEVEL" };
    private static String TList[]  = { "NO_TASK       ", "WAIT          ", "STAY_FORMATION", "FLY_WAYPOINT  ", "DEFENCE       ", "DEFENDING     ", "ATTACK_AIR    ", "ATTACK_GROUND " };
    private static String sMList[] = { "NONE", "STAY_ON_THE_TAIL", "NOT_TOO_FAST", "FROM_WAYPOINT", "CONST_SPEED", "MIN_SPEED", "MAX_SPEED", "CONST_POWER", "ZERO_POWER", "BOOST_ON", "BOOST_FULL" };
    private static String WList[]  = { "BOOM_ZOOM", "FROM_AHEAD", "FROM_BELOW", "AS_IT_IS", "FROM_SIDE", "FROM_TAIL", "SHALLOW_DIVE", "FROM_BOTTOM", "FROM_AHEAD_LEFT", "FROM_AHEAD_RIGHT" };
    private static String WPList[] = { "NORMFLY", "TAKEOFF", "LANDING", "GATTACK" };

}
