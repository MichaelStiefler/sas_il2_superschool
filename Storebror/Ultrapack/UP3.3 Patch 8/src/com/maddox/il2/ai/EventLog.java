/* 4.10.1 class, altered by _ITAF_Radar for his needs for IL2 war. */
package com.maddox.il2.ai;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.DServer;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.buildings.House;
import com.maddox.rts.HomePath;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;

public class EventLog {
    public static class Action {
        public int    event;
        public float  time;
        public String arg0;
        public int    scoreItem0;
        public int    army0;
        public String arg1;
        public int    scoreItem1;
        public int    argi;
        public float  x;
        public float  y;

        public Action() {
        }

        public Action(int i, String s, int j, String s1, int k, int l, float f, float f1) {
            this.event = i;
            this.time = World.getTimeofDay();
            this.arg0 = s;
            this.scoreItem0 = j;
            this.army0 = 0;
            if (s != null) {
                Actor actor = Actor.getByName(s);
                if (actor != null) this.army0 = actor.getArmy();
            }
            this.arg1 = s1;
            this.scoreItem1 = k;
            this.argi = l;
            this.x = f;
            this.y = f1;
            if (Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isDedicated()) return;
            else {
                EventLog.actions.add(this);
                return;
            }
        }
    }

    public static ArrayList            actions         = new ArrayList();
    public static EventLog.Action      lastFly         = null;
    public static final int            OCCUPIED        = 0;
    public static final int            TRYOCCUPIED     = 1;
    public static final int            CRASHED         = 2;
    public static final int            SHOTDOWN        = 3;
    public static final int            DESTROYED       = 4;
    public static final int            BAILEDOUT       = 5;
    public static final int            KILLED          = 6;
    public static final int            LANDED          = 7;
    public static final int            CAPTURED        = 8;
    public static final int            WOUNDED         = 9;
    public static final int            HEAVILYWOUNDED  = 10;
    public static final int            FLY             = 11;
    public static final int            PARAKILLED      = 12;
    public static final int            CHUTEKILLED     = 13;
    public static final int            SMOKEON         = 14;
    public static final int            SMOKEOFF        = 15;
    public static final int            LANDINGLIGHTON  = 16;
    public static final int            LANDINGLIGHTOFF = 17;
    public static final int            WEAPONSLOAD     = 18;
    public static final int            PARALANDED      = 19;
    public static final int            DAMAGEDGROUND   = 20;
    public static final int            DAMAGED         = 21;
    public static final int            DISCONNECTED    = 22;
    public static final int            CONNECTED       = 23;
    public static final int            INFLIGHT        = 24;
    public static final int            REFLY           = 25;
    public static final int            REMOVED         = 26;
    private static java.io.PrintStream file            = null;
    private static boolean             bBuffering      = true;
    private static String              fileName        = null;
    private static boolean             logKeep         = false;
    private static DateFormat          longDate        = null;
    private static DateFormat          shortDate       = null;
    private static boolean             bInited         = false;

    // +++ 4.14.1 Backport +++
    public static final int BASESELECT = 27;
    public static final int ANSVERFM = 28;
    public static final int NTRKSTART = 29;
    public static final int NTRKSTOP = 30;
    public static final int DROP_PARATROOPER = 31;
    public static final int DROP_CARGO = 32;
    public static final int LAND_PARATROOPER = 33;
    public static final int LAND_CARGO = 34;
    public static final int FAIL_CARGO = 35;
    public static final int WATER_CARGO = 36;
    public static final int AS_ENGINE_STATE_0 = 37;
    public static final int AS_ENGINE_STATE_1 = 38;
    public static final int AS_ENGINE_STATE_2 = 39;
    public static final int AS_ENGINE_STATE_3 = 40;
    public static final int AS_ENGINE_STATE_4 = 41;
    public static final int AS_ENGINE_SPECIFIC_BOOSTER = 42;
    public static final int AS_ENGINE_SPECIFIC_THROTTLECTRL = 43;
    public static final int AS_ENGINE_SPECIFIC_ANGLER = 44;
    public static final int AS_ENGINE_SPECIFIC_ANGLERSPEEDS = 45;
    public static final int AS_ENGINE_SPECIFIC_EXTINGUISHER = 46;
    public static final int AS_ENGINE_SPECIFIC_PROPCTRL = 47;
    public static final int AS_ENGINE_SPECIFIC_MIXCTRL = 48;
    public static final int AS_ENGINE_EXPLODES = 49;
    public static final int AS_ENGINE_DIES = 50;
    public static final int AS_ENGINE_STUCK = 51;
    public static final int AS_TANK_STATE_0 = 52;
    public static final int AS_TANK_STATE_1 = 53;
    public static final int AS_TANK_STATE_2 = 54;
    public static final int AS_TANK_STATE_3 = 55;
    public static final int AS_TANK_STATE_4 = 56;
    public static final int AS_TANK_STATE_5 = 57;
    public static final int AS_TANK_STATE_6 = 58;
    public static final int AS_TANK_EXPLODES = 59;
    public static final int AS_OIL_STATE_0 = 60;
    public static final int AS_OIL_STATE_1 = 61;
    public static final int AS_CONTROLS_AILERONS = 62;
    public static final int AS_CONTROLS_ELEVATORS = 63;
    public static final int AS_CONTROLS_RUDDERS = 64;
    public static final int AS_CONTROLS_AUTOPILOT = 65;
    public static final int AS_INTERNALS_HYDRO_OFFLINE = 66;
    public static final int AS_INTERNALS_PNEUMO_OFFLINE = 67;
    public static final int AS_INTERNALS_MW50_OFFLINE = 68;
    public static final int AS_INTERNALS_GEAR_STUCK = 69;
    public static final int AS_INTERNALS_KEEL_SHUTOFF = 70;
    public static final int AS_INTERNALS_SHAFT_SHUTOFF = 71;
    public static final int NA_DROP_EXTERNALSTORES = 72;
    public static final int NA_DROP_FUELTANKS = 73;
    // --- 4.14.1 Backport ----
    
    
    public EventLog() {
    }

    public synchronized static void flyPlayer(Point3d point3d) {
        if (Mission.isDogfight()) return;
        if (lastFly != null) {
            double d = (lastFly.x - point3d.x) * (lastFly.x - point3d.x) + (lastFly.y - point3d.y) * (lastFly.y - point3d.y);
            if (d < 250000D) return;
        }
        lastFly = new Action(11, null, -1, null, -1, 0, (float) point3d.x, (float) point3d.y);
    }

    public static void resetGameClear() {
        actions.clear();
        lastFly = null;
    }

    public static void resetGameCreate() {
    }

    private static void checkInited() {
        if (!bInited) {
            logKeep = Config.cur.ini.get("game", "eventlogkeep", 1, 0, 1) == 1;
            fileName = Config.cur.ini.get("game", "eventlog", (String) null);
            bInited = true;
        }
        if (longDate == null) {
            longDate = DateFormat.getDateTimeInstance(2, 2);
            shortDate = DateFormat.getTimeInstance(2);
        }
    }

    public synchronized static void checkState() {
        EventLog.checkInited();
        if (fileName == null && Main.cur().campaign != null && Main.cur().campaign.isDGen()) {
            fileName = "eventlog.lst";
            Config.cur.ini.set("game", "eventlog", "eventlog.lst");
            Config.cur.ini.saveFile();
        }
        if (logKeep) EventLog.checkBuffering();
        else {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception exception) {}
                file = null;
            }
            if (fileName != null) try {
                java.io.File file1 = new File(HomePath.toFileSystemName(fileName, 0));
                file1.delete();
            } catch (Exception exception1) {}
        }
    }

    private static void checkBuffering() {
        if (file == null) return;
        boolean flag = true;
        if (Main.cur() instanceof DServer || Main.cur().netServerParams != null && !Main.cur().netServerParams.isSingle() && Main.cur().netServerParams.isMaster()) flag = false;
        if (flag != bBuffering) {
            EventLog.close();
            EventLog.open();
        }
    }

    public synchronized static boolean open() {
        if (file != null) return true;
        EventLog.checkInited();
        if (fileName == null) return false;
        try {
            bBuffering = true;
            if (Main.cur() instanceof DServer || Main.cur().netServerParams != null && !Main.cur().netServerParams.isSingle() && Main.cur().netServerParams.isMaster()) bBuffering = false;
            // +++ TODO: Piped Logging +++
            if (Config.cur != null && Config.cur.bPipedLog) {
                file = new PrintStream(new FileOutputStream(new File("\\\\.\\pipe\\SAS_PIPE_LOGGER")));
                file.print(HomePath.toFileSystemName(fileName, 0) + "\u0000" + (bBuffering ? "" + Config.cur.iEventLogFlushTimeout : "0")); // \u0000 = Separator, Flush Timeout = Instantly for Server, 1000ms for Client default
                file.flush();
            } else // --- TODO: Piped Logging ---
                if (bBuffering) file = new PrintStream(new BufferedOutputStream(new FileOutputStream(HomePath.toFileSystemName(fileName, 0), true), 2048));
                else file = new PrintStream(new FileOutputStream(HomePath.toFileSystemName(fileName, 0), true));
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public synchronized static void close() {
        if (file != null) {
            file.flush();
            file.close();
            file = null;
        }
    }

    public synchronized static StringBuffer logOnTime(float f) {
        if (Mission.isDogfight()) {
            Calendar calendar = Calendar.getInstance();
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("[");
            // TODO: +++ Instant Log by SAS~Storebror +++
            if (shortDate == null) shortDate = DateFormat.getTimeInstance(DateFormat.MEDIUM);
            // TODO: --- Instant Log by SAS~Storebror ---
            stringbuffer.append(shortDate.format(calendar.getTime()));
            stringbuffer.append("] ");
            return stringbuffer;
        }
        int i = (int) f;
        int j = (int) ((f - i) * 60F % 60F);
        int k = (int) ((f - i - j / 60) * 3600F % 60F);
        StringBuffer stringbuffer1 = new StringBuffer();
        if (i < 10) stringbuffer1.append('0');
        stringbuffer1.append(i);
        stringbuffer1.append(':');
        if (j < 10) stringbuffer1.append('0');
        stringbuffer1.append(j);
        stringbuffer1.append(':');
        if (k < 10) stringbuffer1.append('0');
        stringbuffer1.append(k);
        stringbuffer1.append(' ');
        return stringbuffer1;
    }

    public static String name(Actor actor) {
        if (!Actor.isValid(actor)) return "";
        if (Mission.isNet() && Mission.isDogfight() && actor instanceof Aircraft) {
            Aircraft aircraft = (Aircraft) actor;
            NetUser netuser = aircraft.netUser();
            if (netuser != null) return netuser.shortName() + ":" + Property.stringValue(aircraft.getClass(), "keyName", "");
            else return Property.stringValue(aircraft.getClass(), "keyName", "");
        } else return actor.name();
    }

    private static boolean isTyping(int i) {
        if (Main.cur().netServerParams == null) return true;
        if (Main.cur().netServerParams.isMaster()) return true;
        else return (Main.cur().netServerParams.eventlogClient() & 1 << i) != 0;
    }

    public static void type(int i, float f, String s, String s1, int j, float f1, float f2, boolean flag) {
        /*
         * Actor victim = Actor.getByName(s); Actor aggressor = Actor.getByName(s1);
         *
         * if( victim == null ) System.out.println("------------- can not be resolved as actor: " + s); if( aggressor == null ) System.out.println("------------- can not be resolved as actor1: " + s1);
         *
         * if( victim != null && aggressor != null ) System.out.println(" -------------------- " + victim.name() + " was fucked up by " + aggressor.name());
         */

        StringBuffer stringbuffer = EventLog.logOnTime(f);
        switch (i) {
            case OCCUPIED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") seat occupied by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");

                // TODO: Modified by |ZUTI|: make position at coordinates 0,0 so nobody can track you
                // ----------------------------------------
                f1 = f2 = 0.0F;
                // ----------------------------------------
                break;

            case TRYOCCUPIED:
                stringbuffer.append(s1);
                stringbuffer.append(" is trying to occupy seat ");
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(")");
                break;

            case CRASHED:
                stringbuffer.append(s);
                stringbuffer.append(" crashed at ");
                break;

            case SHOTDOWN:
                stringbuffer.append(s);
                stringbuffer.append(" shot down by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");
                break;

            case DESTROYED:
                stringbuffer.append(s);
                stringbuffer.append(" destroyed by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");
                break;

            case BAILEDOUT:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") bailed out at ");
                break;

            case KILLED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                if (s1 == null || "".equals(s1)) stringbuffer.append(") was killed at ");
                else {
                    stringbuffer.append(") was killed by ");
                    stringbuffer.append(s1);
                    stringbuffer.append(" at ");
                }
                break;

            case LANDED:
                stringbuffer.append(s);
                stringbuffer.append(" landed at ");
                break;

            case INFLIGHT:
                stringbuffer.append(s);
                stringbuffer.append(" in flight at ");
                break;

            case CAPTURED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") was captured at ");
                break;

            case WOUNDED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") was wounded at ");
                break;

            case HEAVILYWOUNDED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") was heavily wounded at ");
                break;

            case PARAKILLED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") was killed in his chute by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");
                break;

            case CHUTEKILLED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") has chute destroyed by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");
                break;

            case SMOKEON:
                stringbuffer.append(s);
                stringbuffer.append(" turned wingtip smokes on at ");
                break;

            case SMOKEOFF:
                stringbuffer.append(s);
                stringbuffer.append(" turned wingtip smokes off at ");
                break;

            case LANDINGLIGHTON:
                stringbuffer.append(s);
                stringbuffer.append(" turned landing lights on at ");
                break;

            case LANDINGLIGHTOFF:
                stringbuffer.append(s);
                stringbuffer.append(" turned landing lights off at ");
                break;

            case WEAPONSLOAD:
                stringbuffer.append(s);
                stringbuffer.append(" loaded weapons '");
                stringbuffer.append(s1);
                stringbuffer.append("' fuel ");
                stringbuffer.append(j);
                stringbuffer.append("%");
                break;

            case PARALANDED:
                stringbuffer.append(s);
                stringbuffer.append("(");
                stringbuffer.append(j);
                stringbuffer.append(") successfully bailed out at ");
                break;

            case DAMAGEDGROUND:
                stringbuffer.append(s);
                stringbuffer.append(" damaged on the ground at ");
                break;

            case DAMAGED:
                stringbuffer.append(s);
                stringbuffer.append(" damaged by ");
                stringbuffer.append(s1);
                stringbuffer.append(" at ");
                break;

            case DISCONNECTED:
                stringbuffer.append(s);
                stringbuffer.append(" has disconnected");
                break;

            case CONNECTED:
                stringbuffer.append(s);
                stringbuffer.append(" has connected");
                break;

            case REFLY:
                stringbuffer.append(s);
                stringbuffer.append(" entered refly menu");
                break;

            case REMOVED:
                stringbuffer.append(s);
                stringbuffer.append(" removed at ");
                break;

            case FLY:
            default:
                return;
        }

        if (i != TRYOCCUPIED && i != WEAPONSLOAD && i != DISCONNECTED && i != CONNECTED && i != REFLY) {
            stringbuffer.append(f1);
            stringbuffer.append(" ");
            stringbuffer.append(f2);
        }
        if (EventLog.open() && EventLog.isTyping(i)) {
            file.println(stringbuffer);
            // TODO: +++ Instant Log by SAS~Storebror +++
            if (Config.cur.bInstantLog) {
                file.flush();
                close();
                // TODO: --- Instant Log by SAS~Storebror ---
            }
        }
        if (flag) ((NetUser) NetEnv.host()).replicateEventLog(i, f, s, s1, j, f1, f2);
    }

    public synchronized static void type(boolean flag, String s) {
        if (EventLog.open()) {
            if (flag) {
                Calendar calendar = Calendar.getInstance();
                // TODO: +++ Instant Log by SAS~Storebror +++
                if (longDate == null) longDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
                // TODO: --- Instant Log by SAS~Storebror ---
                file.println("[" + longDate.format(calendar.getTime()) + "] " + s);
            } else file.println(s);
            file.flush();
            // TODO: +++ Instant Log by SAS~Storebror +++
            if (Config.cur.bInstantLog) close();
            // TODO: --- Instant Log by SAS~Storebror ---
        }
    }

    public static void type(String s) {
        StringBuffer stringbuffer = EventLog.logOnTime(World.getTimeofDay());
        stringbuffer.append(s);
        if (EventLog.open()) {
            file.println(stringbuffer);
            file.flush();
            // TODO: +++ Instant Log by SAS~Storebror +++
            if (Config.cur.bInstantLog) close();
            // TODO: --- Instant Log by SAS~Storebror ---
        }
    }

    public static void onOccupied(Aircraft aircraft, NetUser netuser, int i) {
        if (!Actor.isValid(aircraft)) return;
        String s = null;
        if (netuser != null) s = netuser.uniqueName();
        else if (Mission.isSingle()) s = "Player";
        if (s == null) return;
        EventLog.type(OCCUPIED, World.getTimeofDay(), EventLog.name(aircraft), s, i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);
        if (!Mission.isDogfight()) new Action(0, EventLog.name(aircraft), 0, s, -1, i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
    }

    public static void onTryOccupied(String s, NetUser netuser, int i) {
        String s1 = null;
        if (netuser != null) s1 = netuser.uniqueName();
        else if (Mission.isSingle()) s1 = "Player";
        if (s1 == null) return;
        else {
            EventLog.type(TRYOCCUPIED, World.getTimeofDay(), s, s1, i, 0.0F, 0.0F, true);
            return;
        }
    }

    public static void onActorDied(Actor victim, Actor aggressor) {
        if (!Mission.isPlaying()) return;
        if (!Actor.isValid(victim)) return;
        if (victim instanceof House && Main.cur().netServerParams != null && Main.cur().netServerParams.eventlogHouse()) {
            House house = (House) victim;
            EventLog.type(DESTROYED, World.getTimeofDay(), house.getMeshLiveName(), EventLog.name(aggressor), 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, false);
            return;
        }
        if (!victim.isNamed()) return;
        if (victim.isNetMirror()) return;
        if (World.cur().scoreCounter.getRegisteredType(victim) < 0) return;
        if (aggressor == World.remover) EventLog.type(26, World.getTimeofDay(), EventLog.name(victim), "", 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);
        else if (victim == aggressor || !Actor.isValid(aggressor) || !aggressor.isNamed()) {
            EventLog.type(CRASHED, World.getTimeofDay(), EventLog.name(victim), "", 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);
            if (!Mission.isDogfight()) new Action(2, EventLog.name(victim), World.cur().scoreCounter.getRegisteredType(victim), null, -1, 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y);
        } else if (victim instanceof Aircraft) {
            // TODO: Added by |ZUTI| fix aircraft naming problems
            // ---------------------------------------------------------------------
            String strVictim = ZutiSupportMethods.getAircraftCompleteName(victim);
            String strAggressor = ZutiSupportMethods.getAircraftCompleteName(aggressor);
            // ---------------------------------------------------------------------

            EventLog.type(SHOTDOWN, World.getTimeofDay(), strVictim, strAggressor, 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);

            if (!Mission.isDogfight()) new Action(3, EventLog.name(victim), 0, EventLog.name(aggressor), World.cur().scoreCounter.getRegisteredType(aggressor), 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y);
        } else {
            // TODO: Added by |ZUTI| fix aircraft naming problems
            // ---------------------------------------------------------------------
            String strVictim = ZutiSupportMethods.getAircraftCompleteName(victim);
            String strAggressor = ZutiSupportMethods.getAircraftCompleteName(aggressor);
            // ---------------------------------------------------------------------

            EventLog.type(DESTROYED, World.getTimeofDay(), strVictim, strAggressor, 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);

            if (!Mission.isDogfight()) new Action(4, EventLog.name(victim), World.cur().scoreCounter.getRegisteredType(victim), EventLog.name(aggressor), World.cur().scoreCounter.getRegisteredType(aggressor), 0, (float) victim.pos.getAbsPoint().x,
                    (float) victim.pos.getAbsPoint().y);
        }
    }

    public static void onBailedOut(Aircraft aircraft, int i) {
        if (!Mission.isPlaying()) return;
        if (aircraft.isNetMirror()) return;
        EventLog.type(BAILEDOUT, World.getTimeofDay(), EventLog.name(aircraft), "", i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);
        if (!Mission.isDogfight()) new Action(5, EventLog.name(aircraft), 0, null, -1, i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
    }

    public static void onPilotKilled(Aircraft victim, int i, Actor aggressor) {
        if (!Mission.isPlaying()) return;
        if (victim.isNetMirror()) return;
        String s = null;
        if (Actor.isValid(aggressor)) s = EventLog.name(aggressor);

        // TODO: Added by |ZUTI| fix aircraft naming problems
        // ---------------------------------------------------------------------
        String strVictim = ZutiSupportMethods.getAircraftCompleteName(victim);
        String strAggressor = ZutiSupportMethods.getAircraftCompleteName(aggressor);
        // ---------------------------------------------------------------------

        EventLog.type(KILLED, World.getTimeofDay(), strVictim, strAggressor, i, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);

        if (!Mission.isDogfight()) new Action(6, EventLog.name(victim), 0, s, -1, i, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y);
    }

    public static void onPilotKilled(Actor actor, String s, int i) {
        if (!Mission.isPlaying()) return;
        EventLog.type(KILLED, World.getTimeofDay(), s, "", i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
        if (!Mission.isDogfight()) new Action(6, s, 0, null, -1, i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y);
    }

    public static void onAirLanded(Aircraft aircraft) {
        if (!Mission.isPlaying()) return;
        if (aircraft.isNetMirror()) return;

        // TODO: Added by |ZUTI| fix aircraft naming problems
        // ---------------------------------------------------------------------
        String strAircraft = ZutiSupportMethods.getAircraftCompleteName(aircraft);
        // ---------------------------------------------------------------------

        EventLog.type(LANDED, World.getTimeofDay(), strAircraft, "", 0, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);

        if (!Mission.isDogfight()) new Action(7, EventLog.name(aircraft), 0, null, -1, 0, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
    }

    public static void onAirInflight(Aircraft aircraft) {
        if (!Mission.isPlaying()) return;
        if (aircraft.isNetMirror()) return;
        else {
            EventLog.type(INFLIGHT, World.getTimeofDay(), EventLog.name(aircraft), "", 0, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onCaptured(Actor actor, String s, int i) {
        if (!Mission.isPlaying()) return;
        if (actor.isNetMirror()) return;
        else {
            EventLog.type(CAPTURED, World.getTimeofDay(), s, "", i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onCaptured(Aircraft aircraft, int i) {
        if (!Mission.isPlaying()) return;
        if (aircraft.isNetMirror()) return;
        else {
            EventLog.type(CAPTURED, World.getTimeofDay(), EventLog.name(aircraft), "", i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onPilotWounded(Aircraft aircraft, int i) {
        if (!Mission.isPlaying()) return;
        if (aircraft.isNetMirror()) return;
        else {
            EventLog.type(WOUNDED, World.getTimeofDay(), EventLog.name(aircraft), "", i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onPilotHeavilyWounded(Aircraft aircraft, int i) {
        if (!Mission.isPlaying()) return;
        if (aircraft.isNetMirror()) return;
        else {
            EventLog.type(HEAVILYWOUNDED, World.getTimeofDay(), EventLog.name(aircraft), "", i, (float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onParaKilled(Actor actor, String s, int i, Actor actor1) {
        if (!Mission.isPlaying()) return;
        if (actor.isNetMirror()) return;
        EventLog.type(PARAKILLED, World.getTimeofDay(), s, EventLog.name(actor1), i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
        if (!Mission.isDogfight()) new Action(6, s, 0, null, -1, i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y);
    }

    public static void onChuteKilled(Actor actor, String s, int i, Actor actor1) {
        if (!Mission.isPlaying()) return;
        if (actor.isNetMirror()) return;
        else {
            EventLog.type(CHUTEKILLED, World.getTimeofDay(), s, EventLog.name(actor1), i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onToggleSmoke(Actor actor, boolean flag) {
        if (!Mission.isPlaying()) return;
        if (actor.isNetMirror()) return;
        else {
            EventLog.type(flag ? SMOKEON : SMOKEOFF, World.getTimeofDay(), EventLog.name(actor), "", 0, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onToggleLandingLight(Actor actor, boolean flag) {
        if (!Mission.isPlaying()) return;
        if (actor.isNetMirror()) return;
        else {
            EventLog.type(flag ? LANDINGLIGHTON : LANDINGLIGHTOFF, World.getTimeofDay(), EventLog.name(actor), "", 0, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onWeaponsLoad(Actor actor, String s, int i) {
        if (actor.isNetMirror()) return;
        else {
            EventLog.type(WEAPONSLOAD, World.getTimeofDay(), EventLog.name(actor), s, i, 0.0F, 0.0F, true);
            return;
        }
    }

    public static void onParaLanded(Actor actor, String s, int i) {
        if (!Mission.isPlaying()) return;
        if (actor.isNetMirror()) return;
        else {
            EventLog.type(PARALANDED, World.getTimeofDay(), s, "", i, (float) actor.pos.getAbsPoint().x, (float) actor.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onDamagedGround(Actor victim) {
        if (!Actor.isValid(victim)) return;
        if (!Mission.isPlaying()) return;
        if (victim.isNetMirror()) return;
        else {
            // TODO: Added by |ZUTI| fix aircraft naming problems
            // ---------------------------------------------------------------------
            String strVictim = ZutiSupportMethods.getAircraftCompleteName(victim);
            // ---------------------------------------------------------------------

            EventLog.type(DAMAGEDGROUND, World.getTimeofDay(), strVictim, "", 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onDamaged(Actor victim, Actor aggressor) {
        if (!Actor.isValid(victim)) return;
        if (!Mission.isPlaying()) return;
        if (victim.isNetMirror()) return;
        else {
            // TODO: Added by |ZUTI| fix aircraft naming problems
            // ---------------------------------------------------------------------
            String strVictim = ZutiSupportMethods.getAircraftCompleteName(victim);
            String strAggressor = ZutiSupportMethods.getAircraftCompleteName(aggressor);
            // ---------------------------------------------------------------------

            EventLog.type(DAMAGED, World.getTimeofDay(), strVictim, strAggressor, 0, (float) victim.pos.getAbsPoint().x, (float) victim.pos.getAbsPoint().y, true);
            return;
        }
    }

    public static void onDisconnected(String s) {
        if (!Mission.isPlaying()) return;
        else {
            EventLog.type(DISCONNECTED, World.getTimeofDay(), s, "", 0, 0.0F, 0.0F, false);
            return;
        }
    }

    public static void onConnected(String s) {
        if (!Mission.isPlaying()) return;
        else {
            EventLog.type(CONNECTED, World.getTimeofDay(), s, "", 0, 0.0F, 0.0F, false);
            return;
        }
    }

    public static void onRefly(String s) {
        if (!Mission.isPlaying()) return;
        else {
            EventLog.type(REFLY, World.getTimeofDay(), s, "", 0, 0.0F, 0.0F, true);
            return;
        }
    }
    
    
    // +++ 4.14.1 Backport +++
    public static void AnsverFM(Actor paramActor, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      if (!Mission.isPlaying()) {
        return;
      }
      if (paramActor.isNetMirror()) {
        return;
      }
      type(ANSVERFM, World.getTimeofDay(), name(paramActor), "", paramInt, paramFloat1, paramFloat2, paramFloat3, true);
    }
    
    public static void NTRK_Start()
    {
      if (!Mission.isPlaying()) {
        return;
      }
      Aircraft localAircraft = World.getPlayerAircraft();
      if (localAircraft.isNetMirror()) {
        return;
      }
      String str = World.cur().userCfg.name;
      if (str == null) {
        str = "Player";
      }
      type(NTRKSTART, World.getTimeofDay(), str, "", World.Rnd().nextInt(0, 255), World.Rnd().nextFloat(), World.Rnd().nextFloat(), World.Rnd().nextFloat(), true);
    }
    
    public static void NTRK_Stop()
    {
      if (!Mission.isPlaying()) {
        return;
      }
      Aircraft localAircraft = World.getPlayerAircraft();
      if (localAircraft.isNetMirror()) {
        return;
      }
      String str = World.cur().userCfg.name;
      if (str == null) {
        str = "Player";
      }
      type(NTRKSTOP, World.getTimeofDay(), str, "", World.Rnd().nextInt(0, 255), World.Rnd().nextFloat(), World.Rnd().nextFloat(), World.Rnd().nextFloat(), true);
    }
    
    public static void DropParatrooper(Actor paramActor)
    {
      if (!Actor.isValid(paramActor)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if (paramActor.isNetMirror()) {
        return;
      }
      type(DROP_PARATROOPER, World.getTimeofDay(), name(paramActor), "", World.Rnd().nextInt(0, 255), (float)paramActor.pos.getAbsPoint().x, (float)paramActor.pos.getAbsPoint().y, (float)paramActor.pos.getAbsPoint().z, true);
    }
    
    public static void DropCargo(Actor paramActor)
    {
      if (!Actor.isValid(paramActor)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if (paramActor.isNetMirror()) {
        return;
      }
      type(DROP_CARGO, World.getTimeofDay(), name(paramActor), "", World.Rnd().nextInt(0, 255), (float)paramActor.pos.getAbsPoint().x, (float)paramActor.pos.getAbsPoint().y, (float)paramActor.pos.getAbsPoint().z, true);
    }
    
    public static void LandParatrooper(Actor paramActor)
    {
      if (!Actor.isValid(paramActor)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if (paramActor.isNetMirror()) {
        return;
      }
      type(LAND_PARATROOPER, World.getTimeofDay(), name(paramActor), "", World.Rnd().nextInt(0, 255), (float)paramActor.pos.getAbsPoint().x, (float)paramActor.pos.getAbsPoint().y, (float)paramActor.pos.getAbsPoint().z, true);
    }
    
    public static void LandCargo(String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      if (!Mission.isPlaying()) {
        return;
      }
      if (!Mission.isNet()) {
        return;
      }
      if (!Mission.isServer()) {
        return;
      }
      type(LAND_CARGO, World.getTimeofDay(), paramString, "", World.Rnd().nextInt(0, 255), paramFloat1, paramFloat2, paramFloat3, true);
    }
    
    public static void FailCargo(String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      if (!Mission.isPlaying()) {
        return;
      }
      if (!Mission.isNet()) {
        return;
      }
      if (!Mission.isServer()) {
        return;
      }
      type(FAIL_CARGO, World.getTimeofDay(), paramString, "", World.Rnd().nextInt(0, 255), paramFloat1, paramFloat2, paramFloat3, true);
    }
    
    public static void WaterCargo(String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      if (!Mission.isPlaying()) {
        return;
      }
      if (!Mission.isNet()) {
        return;
      }
      if (!Mission.isServer()) {
        return;
      }
      type(WATER_CARGO, World.getTimeofDay(), paramString, "", World.Rnd().nextInt(0, 255), paramFloat1, paramFloat2, paramFloat3, true);
    }
    
    public static void typeEngineState(Actor paramActor1, Actor paramActor2, int paramInt1, int paramInt2)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        switch (paramInt2)
        {
        case 0: 
          type(AS_ENGINE_STATE_0, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 1: 
          type(AS_ENGINE_STATE_1, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 2: 
          type(AS_ENGINE_STATE_2, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 3: 
          type(AS_ENGINE_STATE_3, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 4: 
          type(AS_ENGINE_STATE_4, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        }
      }
    }
    
    public static void typeEngineSpecificDamage(Actor paramActor1, Actor paramActor2, int paramInt1, int paramInt2)
    {
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if (paramInt2 == 5)
      {
        if (paramActor1.isNetMirror()) {
          return;
        }
        type(AS_ENGINE_SPECIFIC_EXTINGUISHER, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
      }
      else
      {
        if (!logShowAircraftStateEvent) {
          return;
        }
        if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
          switch (paramInt2)
          {
          case 0: 
            type(AS_ENGINE_SPECIFIC_BOOSTER, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
            break;
          case 1: 
            type(AS_ENGINE_SPECIFIC_THROTTLECTRL, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
            break;
          case 3: 
            type(AS_ENGINE_SPECIFIC_ANGLER, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
            break;
          case 4: 
            type(AS_ENGINE_SPECIFIC_ANGLERSPEEDS, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
            break;
          case 6: 
            type(AS_ENGINE_SPECIFIC_PROPCTRL, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
            break;
          case 7: 
            type(AS_ENGINE_SPECIFIC_MIXCTRL, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
            break;
          }
        }
      }
    }
    
    public static void typeEngineExplodes(Actor paramActor1, Actor paramActor2, int paramInt)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        type(AS_ENGINE_EXPLODES, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
      }
    }
    
    public static void typeEngineDies(Actor paramActor1, Actor paramActor2, int paramInt)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        type(AS_ENGINE_DIES, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
      }
    }
    
    public static void typeEngineStuck(Actor paramActor1, Actor paramActor2, int paramInt)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        type(AS_ENGINE_STUCK, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
      }
    }
    
    public static void typeFuelTankState(Actor paramActor1, Actor paramActor2, int paramInt1, int paramInt2)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        switch (paramInt2)
        {
        case 0: 
          type(AS_TANK_STATE_0, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 1: 
          type(AS_TANK_STATE_1, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 2: 
          type(AS_TANK_STATE_2, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 3: 
          type(AS_TANK_STATE_3, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 4: 
          type(AS_TANK_STATE_4, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 5: 
          type(AS_TANK_STATE_5, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 6: 
          type(AS_TANK_STATE_6, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        }
      }
    }
    
    public static void typeFuelTankExplodes(Actor paramActor1, Actor paramActor2, int paramInt)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        type(AS_TANK_EXPLODES, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
      }
    }
    
    public static void typeOilState(Actor paramActor1, Actor paramActor2, int paramInt1, int paramInt2)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        switch (paramInt2)
        {
        case 0: 
          type(AS_OIL_STATE_0, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 1: 
          type(AS_OIL_STATE_1, World.getTimeofDay(), name(paramActor1), name(paramActor2), paramInt1, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        }
      }
    }
    
    public static void typeDamagedControls(Actor paramActor1, Actor paramActor2, int paramInt)
    {
      if (!logShowAircraftStateEvent) {
        return;
      }
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        switch (paramInt)
        {
        case 0: 
          type(AS_CONTROLS_AILERONS, World.getTimeofDay(), name(paramActor1), name(paramActor2), 0, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 1: 
          type(AS_CONTROLS_ELEVATORS, World.getTimeofDay(), name(paramActor1), name(paramActor2), 0, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 2: 
          type(AS_CONTROLS_RUDDERS, World.getTimeofDay(), name(paramActor1), name(paramActor2), 0, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        case 3: 
          type(AS_CONTROLS_AUTOPILOT, World.getTimeofDay(), name(paramActor1), name(paramActor2), 0, (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, false);
          break;
        }
      }
    }
    
    public static void typeInternalDamage(Actor paramActor1, Actor paramActor2, int paramInt)
    {
      if (!Actor.isValid(paramActor1)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if (paramActor1.isNetMirror()) {
        return;
      }
      switch (paramInt)
      {
      case 0: 
        type(AS_INTERNALS_HYDRO_OFFLINE, World.getTimeofDay(), name(paramActor1), name(paramActor2), World.Rnd().nextInt(0, 255), (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
        break;
      case 1: 
        type(AS_INTERNALS_PNEUMO_OFFLINE, World.getTimeofDay(), name(paramActor1), name(paramActor2), World.Rnd().nextInt(0, 255), (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
        break;
      case 2: 
        type(AS_INTERNALS_MW50_OFFLINE, World.getTimeofDay(), name(paramActor1), name(paramActor2), World.Rnd().nextInt(0, 255), (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
        break;
      case 3: 
        type(AS_INTERNALS_GEAR_STUCK, World.getTimeofDay(), name(paramActor1), name(paramActor2), World.Rnd().nextInt(0, 255), (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
        break;
      case 4: 
        type(AS_INTERNALS_KEEL_SHUTOFF, World.getTimeofDay(), name(paramActor1), name(paramActor2), World.Rnd().nextInt(0, 255), (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
        break;
      case 5: 
        type(AS_INTERNALS_SHAFT_SHUTOFF, World.getTimeofDay(), name(paramActor1), name(paramActor2), World.Rnd().nextInt(0, 255), (float)paramActor1.pos.getAbsPoint().x, (float)paramActor1.pos.getAbsPoint().y, (float)paramActor1.pos.getAbsPoint().z, true);
        break;
      }
    }
    
    public static void DropExternalStores(Actor paramActor)
    {
      if (!Actor.isValid(paramActor)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        type(NA_DROP_EXTERNALSTORES, World.getTimeofDay(), name(paramActor), "", 0, (float)paramActor.pos.getAbsPoint().x, (float)paramActor.pos.getAbsPoint().y, (float)paramActor.pos.getAbsPoint().z, false);
      }
    }
    
    public static void DropFuelTanks(Actor paramActor)
    {
      if (!Actor.isValid(paramActor)) {
        return;
      }
      if (!Mission.isPlaying()) {
        return;
      }
      if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDedicated()) && (Main.cur().netServerParams.isMaster())) {
        type(NA_DROP_FUELTANKS, World.getTimeofDay(), name(paramActor), "", 0, (float)paramActor.pos.getAbsPoint().x, (float)paramActor.pos.getAbsPoint().y, (float)paramActor.pos.getAbsPoint().z, false);
      }
    }
    
    public static void onBaseSelect(String paramString, BornPlace paramBornPlace)
    {
      if (!Mission.isPlaying()) {
        return;
      }
      type(BASESELECT, World.getTimeofDay(), paramString, "", null, paramBornPlace.army, (float)paramBornPlace.place.x, (float)paramBornPlace.place.y, 0.0F, false, paramString);
    }
    
    
    
    
    
    public static void type(int i, float f, String s, String s1, int j, float f1, float f2, float f3, 
            boolean flag)
    {
        type(i, f, s, s1, null, j, f1, f2, f3, flag, null);
    }

    public static void type(int i, float f, String s, String s1, String s2, int j, float f1, float f2, 
            float f3, boolean flag)
    {
        type(i, f, s, s1, s2, j, f1, f2, f3, flag, null);
    }

    public static void type(int i, float f, String s, String s1, String s2, int j, float f1, float f2, 
            float f3, boolean flag, String s3)
    {
        StringBuffer stringbuffer = logOnTime(f);
        if(logShowNameSender && Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isDedicated() && s3 != null && !s3.equals(""))
            stringbuffer.append("\253" + s3 + "\273");
        switch(i)
        {
        case OCCUPIED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") seat occupied by ");
            stringbuffer.append(s1);
            break;

        case TRYOCCUPIED:
            stringbuffer.append(s1);
            stringbuffer.append(" is trying to occupy seat ");
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(")");
            break;

        case CRASHED:
            stringbuffer.append(s);
            stringbuffer.append(" crashed");
            break;

        case SHOTDOWN:
            stringbuffer.append(s);
            stringbuffer.append(" shot down by ");
            stringbuffer.append(s1);
            if(s2 != null && !s2.equals(""))
            {
                stringbuffer.append(" and ");
                stringbuffer.append(s2);
            }
            break;

        case DESTROYED:
            stringbuffer.append(s);
            stringbuffer.append(" destroyed by ");
            stringbuffer.append(s1);
            break;

        case BAILEDOUT:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") bailed out");
            break;

        case KILLED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            if(s1 == null || "".equals(s1))
            {
                stringbuffer.append(") was killed");
            } else
            {
                stringbuffer.append(") was killed by ");
                stringbuffer.append(s1);
            }
            break;

        case LANDED:
            stringbuffer.append(s);
            stringbuffer.append(" landed");
            break;

        case INFLIGHT:
            stringbuffer.append(s);
            stringbuffer.append(" in flight");
            break;

        case CAPTURED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was captured");
            break;

        case WOUNDED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was wounded");
            break;

        case HEAVILYWOUNDED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was heavily wounded");
            break;

        case PARAKILLED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") was killed in his chute by ");
            stringbuffer.append(s1);
            break;

        case CHUTEKILLED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") has chute destroyed by ");
            stringbuffer.append(s1);
            break;

        case SMOKEON:
            stringbuffer.append(s);
            stringbuffer.append(" turned wingtip smokes on");
            break;

        case SMOKEOFF:
            stringbuffer.append(s);
            stringbuffer.append(" turned wingtip smokes off");
            break;

        case LANDINGLIGHTON:
            stringbuffer.append(s);
            stringbuffer.append(" turned landing lights on");
            break;

        case LANDINGLIGHTOFF:
            stringbuffer.append(s);
            stringbuffer.append(" turned landing lights off");
            break;

        case WEAPONSLOAD:
            stringbuffer.append(s);
            stringbuffer.append(" loaded weapons '");
            stringbuffer.append(s1);
            stringbuffer.append("' fuel ");
            stringbuffer.append(j);
            stringbuffer.append("%");
            break;

        case PARALANDED:
            stringbuffer.append(s);
            stringbuffer.append("(");
            stringbuffer.append(j);
            stringbuffer.append(") successfully bailed out");
            break;

        case DAMAGEDGROUND:
            stringbuffer.append(s);
            stringbuffer.append(" damaged on the ground");
            break;

        case DAMAGED:
            stringbuffer.append(s);
            stringbuffer.append(" damaged by ");
            stringbuffer.append(s1);
            break;

        case DISCONNECTED:
            stringbuffer.append(s);
            stringbuffer.append(" has disconnected");
            break;

        case CONNECTED:
            stringbuffer.append(s);
            stringbuffer.append(" has connected");
            break;

        case REFLY:
            stringbuffer.append(s);
            stringbuffer.append(" entered refly menu");
            break;

        case REMOVED:
            stringbuffer.append(s);
            stringbuffer.append(" removed");
            break;

        case BASESELECT:
            stringbuffer.append(s);
            stringbuffer.append(" selected army ");
            stringbuffer.append(Army.name(j));
            break;

        case ANSVERFM:
            if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && logShowAnswerFMClient)
            {
                int k = Float.floatToIntBits(f1);
                int l = Float.floatToIntBits(f2);
                int i1 = Float.floatToIntBits(f3);
                int j1 = k >>> 12 & 0xfffff;
                int k1 = k >>> 0 & 0xfff;
                int l1 = l >>> 12 & 0xfffff;
                int i2 = l >>> 0 & 0xfff;
                int j2 = i1 >>> 12 & 0xfffff;
                int k2 = i1 >>> 0 & 0xfff;
                stringbuffer.append(s);
                stringbuffer.append(" CRT2:");
                stringbuffer.append(" EOF=" + ((j & 0x80) == 0 ? 0 : 1));
                stringbuffer.append(" ESD=" + ((j & 0x20) == 0 ? 0 : 1));
                stringbuffer.append(" cTR=" + ((j & 0x40) == 0 ? 0 : 1));
                stringbuffer.append(" cFL=" + ((j & 0x10) == 0 ? 0 : 1));
                stringbuffer.append(" cAL=" + ((j & 8) == 0 ? 0 : 1));
                stringbuffer.append(" cEL=" + ((j & 4) == 0 ? 0 : 1));
                stringbuffer.append(" cRD=" + ((j & 2) == 0 ? 0 : 1));
                stringbuffer.append(" cAP=" + ((j & 1) == 0 ? 0 : 1));
                stringbuffer.append(" A=");
                stringbuffer.append(k2);
                stringbuffer.append(" M=");
                stringbuffer.append(j1);
                stringbuffer.append(" F=");
                stringbuffer.append(l1);
                stringbuffer.append(" hp=");
                stringbuffer.append(j2);
                stringbuffer.append(" tW=");
                stringbuffer.append(k1);
                stringbuffer.append(" tO=");
                stringbuffer.append(i2);
            }
            break;

        case NTRKSTART:
            stringbuffer.append(s);
            stringbuffer.append(" started NTRK record");
            break;

        case NTRKSTOP:
            stringbuffer.append(s);
            stringbuffer.append(" stopped NTRK record");
            break;

        case DROP_PARATROOPER:
            stringbuffer.append(s);
            stringbuffer.append(" dropped paratrooper");
            break;

        case DROP_CARGO:
            stringbuffer.append(s);
            stringbuffer.append(" dropped cargo container");
            break;

        case LAND_PARATROOPER:
            stringbuffer.append(s);
            stringbuffer.append(" paratrooper landed");
            break;

        case LAND_CARGO:
            stringbuffer.append(s);
            stringbuffer.append(" cargo container landed");
            break;

        case FAIL_CARGO:
            stringbuffer.append(s);
            stringbuffer.append(" cargo container crashed");
            break;

        case WATER_CARGO:
            stringbuffer.append(s);
            stringbuffer.append(" cargo container fell into the water");
            break;

        case AS_ENGINE_STATE_0:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") normal state by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_STATE_1:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") minor smoke by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_STATE_2:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") medium smoke by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_STATE_3:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") major smoke by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_STATE_4:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") on fire by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_SPECIFIC_BOOSTER:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") booster is out by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_SPECIFIC_THROTTLECTRL:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") throttle control is out by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_SPECIFIC_ANGLER:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") prop governor is out by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_SPECIFIC_ANGLERSPEEDS:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") prop governor hydro is out by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_SPECIFIC_EXTINGUISHER:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") used fire extinguisher");
            break;

        case AS_ENGINE_SPECIFIC_PROPCTRL:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") prop control is out by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_SPECIFIC_MIXCTRL:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") mixture control is out by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_EXPLODES:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") was exploded by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_DIES:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") was broken by ");
            stringbuffer.append(s1);
            break;

        case AS_ENGINE_STUCK:
            stringbuffer.append(s);
            stringbuffer.append(" engine(");
            stringbuffer.append(j);
            stringbuffer.append(") was stuck by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_STATE_0:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") normal state by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_STATE_1:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") minor leak by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_STATE_2:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") major leak by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_STATE_3:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") minor smoke by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_STATE_4:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") major smoke by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_STATE_5:
        case AS_TANK_STATE_6:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") on fire by ");
            stringbuffer.append(s1);
            break;

        case AS_TANK_EXPLODES:
            stringbuffer.append(s);
            stringbuffer.append(" fuel tank(");
            stringbuffer.append(j);
            stringbuffer.append(") was explodes by ");
            stringbuffer.append(s1);
            break;

        case AS_OIL_STATE_0:
            stringbuffer.append(s);
            stringbuffer.append(" oil radiator(");
            stringbuffer.append(j);
            stringbuffer.append(") normal state by ");
            stringbuffer.append(s1);
            break;

        case AS_OIL_STATE_1:
            stringbuffer.append(s);
            stringbuffer.append(" oil radiator(");
            stringbuffer.append(j);
            stringbuffer.append(") smoke by ");
            stringbuffer.append(s1);
            break;

        case AS_CONTROLS_AILERONS:
            stringbuffer.append(s);
            stringbuffer.append(" damaged controls aileron by ");
            stringbuffer.append(s1);
            break;

        case AS_CONTROLS_ELEVATORS:
            stringbuffer.append(s);
            stringbuffer.append(" damaged controls elevator by ");
            stringbuffer.append(s1);
            break;

        case AS_CONTROLS_RUDDERS:
            stringbuffer.append(s);
            stringbuffer.append(" damaged controls rudder by ");
            stringbuffer.append(s1);
            break;

        case AS_CONTROLS_AUTOPILOT:
            stringbuffer.append(s);
            stringbuffer.append(" damaged controls autopilot by ");
            stringbuffer.append(s1);
            break;

        case AS_INTERNALS_HYDRO_OFFLINE:
            stringbuffer.append(s);
            stringbuffer.append(" damaged hydro system by ");
            stringbuffer.append(s1);
            break;

        case AS_INTERNALS_PNEUMO_OFFLINE:
            stringbuffer.append(s);
            stringbuffer.append(" damaged controls flaps by ");
            stringbuffer.append(s1);
            break;

        case AS_INTERNALS_MW50_OFFLINE:
            stringbuffer.append(s);
            stringbuffer.append(" damaged MW50 by ");
            stringbuffer.append(s1);
            break;

        case AS_INTERNALS_GEAR_STUCK:
            stringbuffer.append(s);
            stringbuffer.append(" gear was stuck by ");
            stringbuffer.append(s1);
            break;

        case AS_INTERNALS_KEEL_SHUTOFF:
            stringbuffer.append(s);
            stringbuffer.append(" keel was shut off by ");
            stringbuffer.append(s1);
            break;

        case AS_INTERNALS_SHAFT_SHUTOFF:
            stringbuffer.append(s);
            stringbuffer.append(" shaft was shut off by ");
            stringbuffer.append(s1);
            break;

        case NA_DROP_EXTERNALSTORES:
            stringbuffer.append(s);
            stringbuffer.append(" dropped external stores");
            break;

        case NA_DROP_FUELTANKS:
            stringbuffer.append(s);
            stringbuffer.append(" dropped fuel tanks");
            break;

        case FLY:
        default:
            return;
        }
        if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && i != TRYOCCUPIED && i != WEAPONSLOAD && i != DISCONNECTED && i != CONNECTED && i != REFLY && i != ANSVERFM && i != NTRKSTART && i != NTRKSTOP)
        {
            stringbuffer.append(" at ");
            stringbuffer.append(f1);
            stringbuffer.append(" ");
            stringbuffer.append(f2);
            if(logShowZCoordinate)
            {
                stringbuffer.append(" ");
                stringbuffer.append(f3);
            }
        }
        if(open() && isTyping(i))
            if(i != ANSVERFM)
                file.println(stringbuffer);
            else
            if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && logShowAnswerFMClient)
                file.println(stringbuffer);
            else
                stringbuffer = null;
        if(flag)
            if(i != ANSVERFM)
            {
                if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isDedicated())
                {
                    f1 = World.Rnd().nextFloat(0.0F, 1000000F);
                    f2 = World.Rnd().nextFloat(0.0F, 1000000F);
//                    f3 = World.Rnd().nextFloat(0.0F, 10000F);
                }
                ((NetUser)NetEnv.host()).replicateEventLog(i, f, s, s1, j, f1, f2);
//                ((NetUser)NetEnv.host()).replicateEventLog(i, f, s, s1, j, f1, f2, f3);
            } else
            if(Main.cur().netServerParams != null && !Main.cur().netServerParams.isMaster())
                ((NetUser)NetEnv.host()).replicateEventLog(i, f, s, s1, j, f1, f2);
//                ((NetUser)NetEnv.host()).replicateEventLog(i, f, s, s1, j, f1, f2, f3);
        if(logDebug)
            close();
    }

    
    
    private static boolean logShowNameSender = false;
    private static boolean logShowZCoordinate = false;
    private static boolean logShowAnswerFMClient = false;
    private static boolean logShowAircraftStateEvent = false;
    private static boolean logDebug = false;
    
    
    // --- 4.14.1 Backport ---


}
