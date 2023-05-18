package com.maddox.il2.game;

import com.maddox.util.HashMapInt;

public class GameState {

    public int id() {
        return id;
    }

    public void doQuitMission() {}

    public void doMenuMission() {}

    public void enterPush(GameState gamestate) {
        _enter();
    }

    public void leavePush(GameState gamestate) {
        _leave();
    }

    public void enterPop(GameState gamestate) {
        _enter();
    }

    public void leavePop(GameState gamestate) {
        _leave();
    }

    public void enter(GameState gamestate) {
        _enter();
    }

    public void leave(GameState gamestate) {
        _leave();
    }

    public void _enter() {}

    public void _leave() {}

    public String i18n(String s) {
        return I18N.gui(s);
    }

    public GameState(int i) {
        id = i;
        states.put(i, this);
    }

    public static GameState get(int i) {
        return (GameState) states.get(i);
    }

    public static final int     PLAYER_SELECT          = 1;
    public static final int     MAIN_MENU              = 2;
    public static final int     SINGLE_SELECT          = 3;
    public static final int     SINGLE_BRIEFING        = 4;
    public static final int     SINGLE_MISSION         = 5;
    public static final int     SINGLE_STAT            = 6;
    public static final int     RECORD_SELECT          = 7;
    public static final int     RECORD_PLAY            = 8;
    public static final int     RECORD_SAVE            = 9;
    public static final int     SETUP                  = 10;
    public static final int     SETUP_3D               = 11;
    public static final int     SETUP_VIDEO            = 12;
    public static final int     SETUP_SOUND            = 13;
    public static final int     QUICK                  = 14;
    public static final int     VIEW                   = 15;
    public static final int     CREDITS                = 16;
    public static final int     DIFFICULTY             = 17;
    public static final int     BUILDER                = 18;
    public static final int     SINGLE_COMPLETE        = 19;
    public static final int     CONTROLS               = 20;
    public static final int     PAD                    = 21;
    public static final int     OBJECT_INSPECTOR       = 22;
    public static final int     OBJECT_VIEW            = 23;
    public static final int     QUICK_SAVE             = 24;
    public static final int     QUICK_LOAD             = 25;
    public static final int     CAMPAIGN_NEW           = 26;
    public static final int     CAMPAIGNS              = 27;
    public static final int     CAMP_BRIEFING          = 28;
    public static final int     CAMP_MISSION           = 29;
    public static final int     CAMP_STAT              = 30;
    public static final int     CAMP_STAT_VIEW         = 31;
    public static final int     AWARDS                 = 32;
    public static final int     NET                    = 33;
    public static final int     NET_NEW_CLIENT         = 34;
    public static final int     NET_NEW_SERVER         = 35;
    public static final int     NET_CLIENT             = 36;
    public static final int     NET_SERVER             = 37;
    public static final int     NET_SERVER_MIS_SELECT  = 38;
    public static final int     NET_SERVER_D_BRIEF     = 39;
    public static final int     NET_CLIENT_D_BRIEF     = 40;
    public static final int     NET_DIFFICULTY         = 41;
    public static final int     NET_SERVER_DMIS        = 42;
    public static final int     NET_CLIENT_DMIS        = 43;
    public static final int     NET_AIRCRAFT           = 44;
    public static final int     NET_SERVER_C_BRIEF     = 45;
    public static final int     NET_CLIENT_C_BRIEF     = 46;
    public static final int     NET_SERVER_CSTART      = 47;
    public static final int     NET_CLIENT_CSTART      = 48;
    public static final int     NET_SERVER_CMIS        = 49;
    public static final int     NET_CLIENT_CMIS        = 50;
    public static final int     NET_CSCORE             = 51;
    public static final int     SETUP_NET              = 52;
    public static final int     SETUP_INPUT            = 53;
    public static final int     ARMING                 = 54;
    public static final int     AIRARMING              = 55;
    public static final int     TRAINING_SELECT        = 56;
    public static final int     TRAINING_PLAY          = 57;
    public static final int     BWDEMO_PLAY            = 58;
    public static final int     RECORD_NET_SAVE        = 59;
    public static final int     RECORD_NET_SAVEKEYS    = 60;
    public static final int     DGEN_NEW               = 61;
    public static final int     DGEN_BRIEF             = 62;
    public static final int     DGEN_MISSION           = 63;
    public static final int     DGEN_DEBRIEF           = 64;
    public static final int     DGEN_ROSTER            = 65;
    public static final int     DGEN_PILOT             = 66;
    public static final int     DGEN_PILOT_DETAIL      = 67;
    public static final int     NET_SERVER_NGEN_SELECT = 68;
    public static final int     NET_SERVER_NGEN_PROP   = 69;
    public static final int     DGEN_DOCS              = 70;
    public static final int     QUICK_STATS            = 71;
 // TODO: +++ Mods Settings GUI by SAS~Storebror +++
    public static final int     MOD_SETTINGS           = -1; // New GUI Window for Mod Settings
    // TODO: --- Mods Settings GUI by SAS~Storebror ---
    protected int               id;
    protected static HashMapInt states                 = new HashMapInt();

}
