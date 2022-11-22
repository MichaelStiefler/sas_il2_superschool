/* 4.10.1 class */
package com.maddox.il2.game;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AirportGround;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.Target;
import com.maddox.il2.ai.Trigger;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BmpUtils;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.EffClouds;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Land2DText;
import com.maddox.il2.engine.Land2Dn;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.RenderContext;
import com.maddox.il2.engine.ZutiSupportMethods_Engine;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUIAirArming;
import com.maddox.il2.gui.GUIMenu;
import com.maddox.il2.gui.GUIPad;
import com.maddox.il2.gui.ZutiSupportMethods_GUI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetFilesTrack;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.buildings.HouseManager;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.il2.objects.effects.Zip;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ZutiSupportMethods_Ships;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.artillery.RocketryGeneric;
import com.maddox.il2.objects.vehicles.radios.TypeHasBeacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.il2.objects.vehicles.radios.TypeHasLorenzBlindLanding;
import com.maddox.il2.objects.vehicles.radios.TypeHasMeacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasRadioStation;
import com.maddox.il2.objects.vehicles.stationary.Smoke;
import com.maddox.il2.objects.vehicles.stationary.SmokeGeneric;
import com.maddox.rts.BackgroundTask;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Destroy;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.Joy;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgInvokeMethod_Object;
import com.maddox.rts.MsgNet;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelCallbackStream;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetChannelOutStream;
import com.maddox.rts.NetChannelStream;
import com.maddox.rts.NetControl;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgDestroy;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.rts.net.NetFileServerDef;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.AudioDevice;
import com.maddox.sound.CmdMusic;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.SharedTokenizer;

public class Mission implements Destroy {
    private int                  curYear;
    private int                  curMonth;
    private int                  curDay;
    private float                curWindDirection;
    private float                curWindVelocity;
    private float                curGust;
    private float                curTurbulence;
    private static ArrayList     beaconsRed            = new ArrayList();
    private static ArrayList     beaconsBlue           = new ArrayList();
    private static ArrayList     meaconsRed            = new ArrayList();
    private static ArrayList     meaconsBlue           = new ArrayList();
    private static Map           hayrakeMap            = new HashMap();
    public static boolean        hasRadioStations      = false;
    private static boolean       radioStationsLoaded   = false;
    private float                bigShipHpDiv;

    public static final String   DIR                   = "missions/";
    public static final String   DIRNET                = "missions/Net/Cache/";
    public static final float    CLOUD_HEIGHT          = 8000.0F;
    private String               name                  = null;
    private SectFile             sectFile;
    private long                 sectFinger            = 0L;
    public ArrayList             actors                = new ArrayList();
    private int                  curActor              = 0;
    private boolean              bPlaying              = false;
    private int                  curCloudsType         = 0;
    private float                curCloudsHeight       = 1000.0F;
    protected static int         viewSet               = 0;
    protected static int         iconTypes             = 0;
    private static HashMap       respawnMap            = new HashMap();
    private String               player;
    private boolean              _loadPlayer           = false;
    private int                  playerNum             = 0;
    private HashMap              mapWingTakeoff;
    private static SectFile      chiefsIni;
    private static ActorSpawnArg spawnArg              = new ActorSpawnArg();
    private static Point3d       p                     = new Point3d();
    private static Orient        o                     = new Orient();
    public static final int      NET_MSG_ID_NAME       = 0;
    public static final int      NET_MSG_ID_BODY       = 1;
    public static final int      NET_MSG_ID_BODY_END   = 2;
    public static final int      NET_MSG_ID_ACTORS     = 3;
    public static final int      NET_MSG_ID_ACTORS_END = 4;
    public static final int      NET_MSG_ID_LOADED     = 5;
    public static final int      NET_MSG_ID_BEGIN      = 10;
    public static final int      NET_MSG_ID_TOD        = 11;
    public static final int      NET_MSG_ID_START      = 12;
    public static final int      NET_MSG_ID_TIME       = 13;
    public static final int      NET_MSG_ID_END        = 20;
    protected NetObj             net;

    static class SPAWN implements NetSpawn {
        public void netSpawn(int i, NetMsgInput netmsginput) {
            try {
                if (Main.cur().mission != null) Main.cur().mission.destroy();
                Mission mission = new Mission();
                if (cur() != null) cur().destroy();
                mission.clear();
                Main.cur().mission = mission;
                mission.createNetObject(netmsginput.channel(), i);
                Main.cur().missionLoading = mission;
            } catch (Exception exception) {
                printDebug(exception);
            }
        }
    }

    class Mirror extends NetMissionObj {
        public Mirror(Mission mission_0_, NetChannel netchannel, int i) {
            super(mission_0_, netchannel, i);
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(0);
                this.postTo(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                if (mission_0_ != null) {
                    /* empty */
                }
                Mission.printDebug(exception);
            }
        }
    }

    class Master extends NetMissionObj {
        public Master(Mission mission_1_) {
            super(mission_1_);
            mission_1_.sectFinger = mission_1_.sectFile.fingerExcludeSectPrefix("$$$");
        }
    }

    class NetMissionObj extends NetObj implements NetChannelCallbackStream {
        private void msgCallback(NetChannel netchannel, int i) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(1);
                netmsgguaranted.writeByte(i);
                NetMsgInput netmsginput = new NetMsgInput();
                netmsginput.setData(netchannel, true, netmsgguaranted.data(), 0, netmsgguaranted.size());
                MsgNet.postReal(Time.currentReal(), this, netmsginput);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        public boolean netChannelCallback(NetChannelOutStream netchanneloutstream, NetMsgGuaranted netmsgguaranted) {
            if (netmsgguaranted instanceof NetMsgSpawn) this.msgCallback(netchanneloutstream, 0);
            else if (!(netmsgguaranted instanceof NetMsgDestroy)) while_0_: do {
                boolean bool;
                try {
                    NetMsgInput netmsginput = new NetMsgInput();
                    netmsginput.setData(netchanneloutstream, true, netmsgguaranted.data(), 0, netmsgguaranted.size());
                    int i = netmsginput.readUnsignedByte();
                    switch (i) {
                        case 0:
                            this.msgCallback(netchanneloutstream, 1);
                            break while_0_;
                        case 2:
                            this.msgCallback(netchanneloutstream, 3);
                            break while_0_;
                        case 4:
                            this.msgCallback(netchanneloutstream, 4);
                            break while_0_;
                        case 12:
                            Main3D.cur3D().gameTrackRecord().startKeyRecord(netmsgguaranted);
                            bool = false;
                            break;
                        default:
                            break while_0_;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    break;
                }
                return bool;
            } while (false);
            return true;
        }

        public boolean netChannelCallback(NetChannelInStream netchannelinstream, NetMsgInput netmsginput) {
            try {
                int i = netmsginput.readUnsignedByte();
                if (i == 4) netchannelinstream.setPause(true);
                else if (i == 12) {
                    netchannelinstream.setGameTime();
                    // TODO: Edited by |ZUTI|
                    // if (isCoop())
                    if (isCoop() || isDogfight()) {
                        Main.cur().netServerParams.prepareHidenAircraft();
                        doMissionStarting();

                        Time.setPause(false);
                    }
                    Main3D.cur3D().gameTrackPlay().startKeyPlay();
                }
                netmsginput.reset();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return true;
        }

        public void netChannelCallback(NetChannelInStream netchannelinstream, NetMsgGuaranted netmsgguaranted) {
            if (!(netmsgguaranted instanceof NetMsgSpawn) && !(netmsgguaranted instanceof NetMsgDestroy)) try {
                NetMsgInput netmsginput = new NetMsgInput();
                netmsginput.setData(netchannelinstream, true, netmsgguaranted.data(), 0, netmsgguaranted.size());
                int i = netmsginput.readUnsignedByte();
                if (i == 4) netchannelinstream.setPause(false);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            Mission mission = (Mission) this.superObj();
            mission.netInput(netmsginput);
            return true;
        }

        public void msgNetNewChannel(NetChannel netchannel) {
            if (Main.cur().missionLoading == null) this.tryReplicate(netchannel);
        }

        private void tryReplicate(NetChannel netchannel) {
            if (netchannel.isReady() && netchannel.isPublic() && netchannel != this.masterChannel && !netchannel.isMirrored(this) && netchannel.userState == 1) try {
                this.postTo(netchannel, new NetMsgSpawn(this));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        public NetMissionObj(Object object) {
            super(object);
        }

        public NetMissionObj(Object object, NetChannel netchannel, int i) {
            super(object, netchannel, i);
        }
    }

    static class WingTakeoffPos {
        public int x;
        public int y;

        public WingTakeoffPos(double d, double d_2_) {
            this.x = (int) (d / 100.0) * 100;
            this.y = (int) (d_2_ / 100.0) * 100;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof WingTakeoffPos)) return false;
            WingTakeoffPos wingtakeoffpos_3_ = (WingTakeoffPos) object;
            return this.x == wingtakeoffpos_3_.x && this.y == wingtakeoffpos_3_.y;
        }

        public int hashCode() {
            return this.x + this.y;
        }
    }

    class TimeOutWing {
        String wingName;

        public void start() {
            if (!Mission.this.isDestroyed()) try {
                // TODO: Added by |ZUTI|: if wing is spawned on overrun home base, don't load it!
                // ----------------------------------------------------------------------------
                Point3d toPoint = ZutiSupportMethods_Air.getWingTakeoffLocation(Mission.this.sectFile, this.wingName);
                if (toPoint != null) {
                    Squadron squadron = Squadron.New(this.wingName.substring(0, this.wingName.length() - 1));
                    BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(toPoint.x, toPoint.y);

                    if (bp != null && squadron.getArmy() != bp.army) if (Math.sqrt(Math.pow(toPoint.x - bp.place.x, 2) + Math.pow(toPoint.y - bp.place.y, 2)) <= bp.r) {
                        System.out.println("Mission: wing " + this.wingName + " not loaded because its TO point is located on enemy home base!");
                        return;
                    }
                }
                // ----------------------------------------------------------------------------

                NetAircraft.loadingCoopPlane = false;
                Wing wing = new Wing();
                wing.load(Mission.this.sectFile, this.wingName, null);
                Mission.this.prepareSkinInWing(Mission.this.sectFile, wing);
                wing.setOnAirport();
            } catch (Exception exception) {
                printDebug(exception);
            }
        }

        public TimeOutWing(String string) {
            this.wingName = string;
        }
    }

    public class BackgroundLoader extends BackgroundTask {
        private String   _name;
        private SectFile _in;

        public void run() throws Exception {
            Mission.this._load(this._name, this._in, true);
        }

        public BackgroundLoader(String string, SectFile sectfile) {
            this._name = string;
            this._in = sectfile;
        }
    }

    public static float respawnTime(String string) {
        Object object = respawnMap.get(string);
        if (object == null) return 1800.0F;
        return ((Float) object).floatValue();
    }

    public static boolean isPlaying() {
        if (Main.cur() == null) return false;
        if (Main.cur().mission == null) return false;
        if (Main.cur().mission.isDestroyed()) return false;
        return Main.cur().mission.bPlaying;
    }

    public static boolean isSingle() {
        if (Main.cur().mission == null) return false;
        if (Main.cur().mission.isDestroyed()) return false;
        if (Main.cur().mission.net == null) return true;
        if (Main.cur().netServerParams == null) return true;
        return Main.cur().netServerParams.isSingle();
    }

    public static boolean isNet() {
        if (Main.cur().mission == null) return false;
        if (Main.cur().mission.isDestroyed()) return false;
        if (Main.cur().mission.net == null) return false;
        if (Main.cur().netServerParams == null) return false;
        return !Main.cur().netServerParams.isSingle();
    }

    public NetChannel getNetMasterChannel() {
        if (this.net == null) return null;
        return this.net.masterChannel();
    }

    public static boolean isServer() {
        return NetEnv.isServer();
    }

    public static boolean isDeathmatch() {
        return isDogfight();
    }

    public static boolean isDogfight() {
        if (Main.cur().mission == null) return false;
        if (Main.cur().mission.isDestroyed()) return false;
        if (Main.cur().mission.net == null) return false;
        if (Main.cur().netServerParams == null) return false;
        return Main.cur().netServerParams.isDogfight();
    }

    public static boolean isCoop() {
        if (Main.cur().mission == null) return false;
        if (Main.cur().mission.isDestroyed()) return false;
        if (Main.cur().mission.net == null) return false;
        if (Main.cur().netServerParams == null) return false;
        return Main.cur().netServerParams.isCoop();
    }

    public static int curCloudsType() {
        if (Main.cur().mission == null) return 0;
        return Main.cur().mission.curCloudsType;
    }

    public static float curCloudsHeight() {
        if (Main.cur().mission == null) return 1000.0F;
        return Main.cur().mission.curCloudsHeight;
    }

    public static Mission cur() {
        return Main.cur().mission;
    }

    public static void BreakP() {
        System.out.print("");
    }

    public static void load(String string) throws Exception {
        load(string, false);
    }

    public static void load(String string, boolean bool) throws Exception {
        load("missions/", string, bool);
    }

    public static void load(String string, String string_4_) throws Exception {
        load(string, string_4_, false);
    }

    public static void load(String string, String string_5_, boolean bool) throws Exception {
        Mission mission = new Mission();
        if (cur() != null) cur().destroy();
        else mission.clear();
        mission.sectFile = new SectFile(string + string_5_);
        mission.load(string_5_, mission.sectFile, bool);
    }

    public static void loadFromSect(SectFile sectfile) throws Exception {
        loadFromSect(sectfile, false);
    }

    public static void loadFromSect(SectFile sectfile, boolean bool) throws Exception {
        Mission mission = new Mission();
        String string = sectfile.fileName();
        if (string != null && string.toLowerCase().startsWith("missions/")) string = string.substring("missions/".length());
        if (cur() != null) cur().destroy();
        else mission.clear();
        mission.sectFile = sectfile;
        mission.load(string, mission.sectFile, bool);
    }

    public String name() {
        return this.name;
    }

    public SectFile sectFile() {
        return this.sectFile;
    }

    public long finger() {
        return this.sectFinger;
    }

    public boolean isDestroyed() {
        return this.name == null;
    }

    public void destroy() {
        if (!this.isDestroyed()) {
            // TODO: Added by |ZUTI|
            // -----------------------------------
            MDS_VARIABLES().resetVariables();
            // -----------------------------------

            if (this.bPlaying) this.doEnd();
            this.bPlaying = false;
            this.clear();
            this.name = null;
            Main.cur().mission = null;
            if (Main.cur().netMissionListener != null) Main.cur().netMissionListener.netMissionState(8, 0.0F, null);
            if (this.net != null && !this.net.isDestroyed()) this.net.destroy();
            this.net = null;

            // TODO: +++ New automatic Garbage Collection at mission end by SAS~Storebror +++
            doGarbageCollection();
            // TODO: --- New automatic Garbage Collection at mission end by SAS~Storebror ---
        }
    }

    private void clear() {
        if (this.net != null) {
            this.doReplicateNotMissionActors(false);
            if (this.net.masterChannel() != null) this.doReplicateNotMissionActors(this.net.masterChannel(), false);
        }
        this.actors.clear();
        beaconsRed.clear();
        beaconsBlue.clear();
        meaconsRed.clear();
        meaconsBlue.clear();
        hayrakeMap.clear();
        this.curActor = 0;
        Main.cur().resetGame();
        if (GUI.pad != null) GUI.pad.zutiPadObjects.clear();
    }

    private void load(String string, SectFile sectfile, boolean bool) throws Exception {
        if (bool) BackgroundTask.execute(new BackgroundLoader(string, sectfile));
        else this._load(string, sectfile, bool);
    }

    private void LOADING_STEP(float f, String string) {
        if (this.net != null && Main.cur().netMissionListener != null) Main.cur().netMissionListener.netMissionState(3, f, string);
        if (!BackgroundTask.step(f, string)) throw new RuntimeException(BackgroundTask.executed().messageCancel());
    }

    private void _load(String string, SectFile sectfile, boolean bool) throws Exception {
        AudioDevice.soundsOff();
        if (string != null) System.out.println("Loading mission " + string + "...");
        else System.out.println("Loading mission ...");

        // TODO: Added by |ZUTI|
        // -----------------------------------
        MDS_VARIABLES().resetVariables();
        // -----------------------------------

        EventLog.checkState();
        Main.cur().missionLoading = this;
        RTSConf.cur.time.setEnableChangePause1(false);
        Actor.setSpawnFromMission(true);
        try {
            Main.cur().mission = this;
            this.name = string;
            if (this.net == null) this.createNetObject(null, 0);

            this.loadMain(sectfile);

            try {
                this.loadRespawnTime(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                Front.loadMission(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            List list = null;
            // TODO: Edited by |ZUTI|: enables AI objects on the map
            // if (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isSingle())
            // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
//            if (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight() || Main.cur().netServerParams.isSingle()) {
            try
            {
                loadTriggers(sectfile);
            }
            catch(Exception ex)
            {
                if (this.isDestroyed()) return;
                System.out.println("Mission error, ID_30 (tiggers) : " + ex.toString());
                ex.printStackTrace();
            }
                // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
                // load AI planes
                try {
                    list = this.loadWings(sectfile);
                } catch (Exception ex) {
                    if (this.isDestroyed()) return;
                    // TODO: Edited by |ZUTI|: enables AI objects on the map
                    System.out.println("Mission error, ID_04: (wings)" + ex.toString());
                    // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
                    ex.printStackTrace();
                }

                // loads ships/cars/trains
                try {
                    this.loadChiefs(sectfile);
                } catch (Exception ex) {
                    if (this.isDestroyed()) return;
                    // TODO: Edited by |ZUTI|: enables AI objects on the map
                    System.out.println("Mission error, ID_05: (chiefs)" + ex.toString());
                    // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
                    ex.printStackTrace();
                }
                // TODO: Edited by |ZUTI|: enables AI objects on the map
//        }
                // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

            try {
                this.loadHouses(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                this.loadNStationary(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                this.loadStationary(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                this.loadRocketry(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                this.loadViewPoint(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }

            // Load born places for all game types, just in case users will be using radars... :) BPs are only rendered for DF missions anyhow.
            // if (Main.cur().netServerParams.isDogfight())
            // {
            try {
                this.loadBornPlaces(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                this.populateBeacons();
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            try {
                this.populateRunwayLights();
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            // }
            // if (Main.cur().netServerParams.isMaster() && (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isDogfight()))
            if (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isDogfight()) try {
                this.loadTargets(sectfile);
            } catch (Exception ex) {
                if (this.isDestroyed()) return;
                ex.printStackTrace();
            }
            if (list != null) {
                int i = list.size();
                for (int i_6_ = 0; i_6_ < i; i_6_++) {
                    Wing wing = (Wing) list.get(i_6_);
                    // Perhaps wings are no more, catch those exceptions!
                    try {
                        if (Actor.isValid(wing)) wing.setOnAirport();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            if (this.isDestroyed()) return;
            exception.printStackTrace();

            if (this.net != null && Main.cur().netMissionListener != null) Main.cur().netMissionListener.netMissionState(4, 0.0F, exception.getMessage());
            this.clear();
            if (this.net != null && !this.net.isDestroyed()) this.net.destroy();
            this.net = null;
            Main.cur().mission = null;
            this.name = null;
            Actor.setSpawnFromMission(false);
            Main.cur().missionLoading = null;
            this.setTime(false);
            throw exception;
        }
        if (Config.isUSE_RENDER()) {
            if (Actor.isValid(World.getPlayerAircraft())) World.land().cubeFullUpdate((float) World.getPlayerAircraft().pos.getAbsPoint().z);
            else World.land().cubeFullUpdate(1000.0F);
            GUI.pad.fillAirports();
        }
        Actor.setSpawnFromMission(false);
        Main.cur().missionLoading = null;
        Main.cur().missionCounter++;
        if (this.isDestroyed()) return;
        this.setTime(!Main.cur().netServerParams.isSingle());
        this.LOADING_STEP(90.0F, "task.Load_humans");
        Paratrooper.PRELOAD();
        this.LOADING_STEP(95.0F, "task.Load_humans");
        // TODO: Edited by |ZUTI|
        // if (Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop())
        if (Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight()) try {
            Soldier.PRELOAD();
        } catch (Exception ex) {
            if (this.isDestroyed()) return;
            System.out.println("Mission error, ID_09: " + ex.toString());
        }
        this.LOADING_STEP(100.0F, "");
        if (Main.cur().netMissionListener != null) Main.cur().netMissionListener.netMissionState(5, 0.0F, null);
        if (this.isDestroyed()) return;
        if (this.net.isMirror()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(4);
                this.net.masterChannel().userState = 4;
                this.net.postTo(this.net.masterChannel(), netmsgguaranted);
            } catch (Exception exception) {
                if (this.isDestroyed()) return;
                exception.printStackTrace();
            }
            ((NetUser) NetEnv.host()).missionLoaded();
        } else if (Main.cur().netServerParams.isSingle()) {
            if (Main.cur() instanceof Main3D) ((Main3D) Main.cur()).ordersTree.missionLoaded();
            Main.cur().dotRangeFriendly.setDefault();
            Main.cur().dotRangeFoe.setDefault();
            Main.cur().dotRangeFoe.set(-1.0, -1.0, -1.0, 5.0, -1.0, -1.0);
        } else((NetUser) NetEnv.host()).replicateDotRange();

        if (this.isDestroyed()) return;
        NetObj.tryReplicate(this.net, false);

        // TODO: Added by |ZUTI|
        // -----------------------------------------------------
        // Assign actors to markers
        try {
            ZutiSupportMethods.zutiAssignMarkersToActors();
        } catch (Exception ex) {
            if (this.isDestroyed()) return;
            ex.printStackTrace();
        }

        // Original line
        War.cur().missionLoaded();

        // Reset player army
        try {
            ((NetUser) NetEnv.host()).setArmy(0);
        } catch (Exception ex) {}

        if (this.isDestroyed()) return;
        // After both, mission and map files were read, load also born places and add them to friction plates, if they are set up like that
        ZutiSupportMethods.zutiAddHomeBasesAsFrictionDictators();

        // Load RRR objects to all defined airfields.
        if (World.cur().houseManager != null) ZutiSupportMethods.appendRRRObjectsToAirfields();

        // Save this mission as current mission even for SingleMission type. MDS needs it in order to work there too ;)
        if (bool) Main.cur().mission = this;

        // Set radars mission to this
        ZutiRadarRefresh.setCurrentMission(this);

        // Load resources
        ZutiMDSVariables.loadResources(sectfile, true);
        // -----------------------------------------------------
    }

    private void setTime(boolean bool) {
        if (RTSConf.cur.time != null) {
            /* empty */
        }
        Time.setSpeed(1.0F);
        if (RTSConf.cur.time != null) {
            /* empty */
        }
        Time.setSpeedReal(1.0F);
        if (bool) {
            RTSConf.cur.time.setEnableChangePause1(false);
            RTSConf.cur.time.setEnableChangeSpeed(false);
            RTSConf.cur.time.setEnableChangeTickLen(true);
        } else {
            RTSConf.cur.time.setEnableChangePause1(true);
            RTSConf.cur.time.setEnableChangeSpeed(true);
            RTSConf.cur.time.setEnableChangeTickLen(false);
        }
    }

    private void loadMain(SectFile sectfile) throws Exception {
        int i = sectfile.get("MAIN", "TIMECONSTANT", 0, 0, 1);
        World.cur().setTimeOfDayConstant(i == 1);
        World.setTimeofDay(sectfile.get("MAIN", "TIME", 12.0F, 0.0F, 23.99F));

        // TODO: Added by |ZUTI|
        // ----------------------------------------------------------------------------------------
        try {
            MDS_VARIABLES().loadVariables(sectfile);
            // Load additional airfields/rearm places that mission maker set up
            System.out.println("Loading mission.mis defined airfields...");
            if (ZutiSupportMethods_Engine.AIRFIELDS != null) ZutiSupportMethods_Engine.AIRFIELDS.clear();
            else ZutiSupportMethods_Engine.AIRFIELDS = new ArrayList();

            int s = sectfile.sectionIndex("AlternativeAirfield");
            if (s > 0) {
                int j = sectfile.vars(s);
                for (int k = 0; k < j; k++)
                    try {
                        String line = sectfile.line(s, k);

                        if (line.length() <= 0) continue;

                        ZutiSupportMethods_Engine.addAirfieldPoint_MisIni(line);
                    } catch (Exception ex) {}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // ----------------------------------------------------------------------------------------

        int i_7_ = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
        World.cur().setWeaponsConstant(i_7_ == 1);

        this.bigShipHpDiv = 1.0F / sectfile.get("MAIN", "ShipHP", 1.0F, 0.001F, 100F);

        String string = sectfile.get("MAIN", "MAP");
        if (string == null) throw new Exception("No MAP in mission file ");
        String string_8_ = null;
        int[] is = null;
        SectFile sectfile_9_ = new SectFile("maps/" + string);
        int i_10_ = sectfile_9_.sectionIndex("static");
        if (i_10_ >= 0 && sectfile_9_.vars(i_10_) > 0) {
            string_8_ = sectfile_9_.var(i_10_, 0);
            if (string_8_ == null || string_8_.length() <= 0) string_8_ = null;
            else {
                string_8_ = HomePath.concatNames("maps/" + string, string_8_);
                is = Statics.readBridgesEndPoints(string_8_);
            }
        }

        this.LOADING_STEP(0.0F, "task.Load_landscape");

        int l = sectfile.get("SEASON", "Year", 1940, 1930, 1960);
        int i1 = sectfile.get("SEASON", "Month", World.land().config.getDefaultMonth("maps/" + string), 1, 12);
        int j1 = sectfile.get("SEASON", "Day", 15, 1, 31);
        setDate(l, i1, j1);

        // TODO: +++ Mission Date Fix by SAS~Storebror +++
// World.land().LoadMap(string, is);
        World.land().LoadMap(string, is, i1, j1);
        // TODO: --- Mission Date Fix by SAS~Storebror ---

        // TODO: Disabled as this is loaded elsewhere!
        // ---------------------------------------------------------
        World.cur().setCamouflage(World.land().config.camouflage);
        // ---------------------------------------------------------

        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().land2D != null) {
                if (!Main3D.cur3D().land2D.isDestroyed()) Main3D.cur3D().land2D.destroy();
                Main3D.cur3D().land2D = null;
            }
            int i_11_ = sectfile_9_.sectionIndex("MAP2D");
            if (i_11_ >= 0) {
                int i_12_ = sectfile_9_.vars(i_11_);
                if (i_12_ > 0) {
                    this.LOADING_STEP(20.0F, "task.Load_map");
                    Main3D.cur3D().land2D = new Land2Dn(string, World.land().getSizeX(), World.land().getSizeY());
                }
            }
            if (Main3D.cur3D().land2DText == null) Main3D.cur3D().land2DText = new Land2DText();
            else Main3D.cur3D().land2DText.clear();
            int i_13_ = sectfile_9_.sectionIndex("text");
            if (i_13_ >= 0 && sectfile_9_.vars(i_13_) > 0) {
                this.LOADING_STEP(22.0F, "task.Load_landscape_texts");
                String string_14_ = sectfile_9_.var(i_13_, 0);
                Main3D.cur3D().land2DText.load(HomePath.concatNames("maps/" + string, string_14_));
            }
        }
        if (string_8_ != null) {
            this.LOADING_STEP(23.0F, "task.Load_static_objects");
            Statics.load(string_8_, World.cur().statics.bridges);
            Engine.drawEnv().staticTrimToSize();
        }
        Statics.trim();
        // TODO: Edited by |ZUTI|
        // if (Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop())
        if (Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight()) try {
            World.cur().statics.loadStateBridges(sectfile, false);
            World.cur().statics.loadStateHouses(sectfile, false);
            World.cur().statics.loadStateBridges(sectfile, true);
            World.cur().statics.loadStateHouses(sectfile, true);
            this.checkBridgesAndHouses(sectfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Main.cur().netServerParams.isSingle()) {
            this.player = sectfile.get("MAIN", "player");
            this.playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
        } else this.player = null;
        World.setMissionArmy(sectfile.get("MAIN", "army", 1, 1, 2));
        if (Config.isUSE_RENDER()) Main3D.cur3D().ordersTree.setFrequency(new Boolean(true));
        if (Config.isUSE_RENDER()) {
            this.LOADING_STEP(29.0F, "task.Load_landscape_effects");
            Main3D main3d = Main3D.cur3D();
            int i_15_ = sectfile.get("MAIN", "CloudType", 0, 0, 6);
            float f = sectfile.get("MAIN", "CloudHeight", 1000.0F, 300.0F, 5000.0F);
            createClouds(i_15_, f);
            if (!Config.cur.ini.get("game", "NoLensFlare", false)) {
                main3d.sunFlareCreate();
                main3d.sunFlareShow(true);
            } else main3d.sunFlareShow(false);

            float f1 = (string.charAt(0) - 64) * (float) Math.PI;
            f1 = sectfile.get("WEATHER", "WindDirection", f1, 0.0F, 359.99F);
            float f2 = 0.25F + i_15_ * i_15_ * 0.12F;
            f2 = sectfile.get("WEATHER", "WindSpeed", f2, 0.0F, 15F);
            float f3 = i_15_ > 3 ? i_15_ * 2.0F : 0.0F;
            f3 = sectfile.get("WEATHER", "Gust", f3, 0.0F, 12F);
            float f4 = i_15_ > 2 ? i_15_ : 0.0F;
            f4 = sectfile.get("WEATHER", "Turbulence", f4, 0.0F, 6F);

            World.wind().set(f, f1, f2, f3, f4);
            for (int i_16_ = 0; i_16_ < 3; i_16_++) {
                Main3D.cur3D()._lightsGlare[i_16_].setShow(true);
                Main3D.cur3D()._sunGlare[i_16_].setShow(true);
            }
        }
    }

    public static void setDate(int i, int j, int k) {
        setYear(i);
        setMonth(j);
        setDay(k);
    }

    public static void setYear(int i) {
        if (i < 1930) i = 1930;
        if (i > 1960) i = 1960;
        if (cur() != null) cur().curYear = i;
    }

    public static void setMonth(int i) {
        if (i < 1) i = 1;
        if (i > 12) i = 12;
        if (cur() != null) cur().curMonth = i;
    }

    public static void setDay(int i) {
        if (i < 1) i = 1;
        if (i > 31) i = 31;
        if (cur() != null) cur().curDay = i;
    }

    public static void setWindDirection(float f) {
        if (f < 0.0F) f = 0.0F;
        if (f > 359.99F) f = 0.0F;
        if (cur() != null) cur().curWindDirection = f;
    }

    public static void setWindVelocity(float f) {
        if (f < 0.0F) f = 0.0F;
        if (f > 15F) f = 15F;
        if (cur() != null) cur().curWindVelocity = f;
    }

    public static void setGust(float f) {
        if (f < 0.0F) f = 0.0F;
        if (f > 12F) f = 12F;
        if (cur() != null) cur().curGust = f;
    }

    public static void setTurbulence(float f) {
        if (f < 0.0F) f = 0.0F;
        if (f > 6F) f = 6F;
        if (cur() != null) cur().curTurbulence = f;
    }

    public static void initRadioSounds() {
        if (radioStationsLoaded) return;

        Aircraft aircraft = World.getPlayerAircraft();
        if (aircraft != null) {
            ArrayList arraylist = Main.cur().mission.getBeacons(aircraft.FM.actor.getArmy());
            if (arraylist != null) for (int i = 0; i < arraylist.size(); i++) {

                Actor actor = (Actor) arraylist.get(i);
                if (actor instanceof TypeHasRadioStation) {
                    hasRadioStations = true;
                    aircraft.FM.AS.preLoadRadioStation(actor);
                    CmdMusic.setCurrentVolume(0.001F);
                    radioStationsLoaded = true;
                }
            }
        }
    }

    public static void createClouds(int i, float f) {
        if (i < 0) i = 0;
        if (i > 6) i = 6;
        if (cur() != null) {
            cur().curCloudsType = i;
            cur().curCloudsHeight = f;
        }
        if (!Config.isUSE_RENDER()) {
            Main main = Main.cur();
            if (main.clouds != null) main.clouds.destroy();
            main.clouds = new EffClouds(World.cur().diffCur.NewCloudsRender, i, f);
            return;
        }
        if (Config.isUSE_RENDER()) {
            Main3D main3d = Main3D.cur3D();
            Camera3D camera3d = (Camera3D) Actor.getByName("camera");
            if (main3d.clouds != null) main3d.clouds.destroy();
            main3d.clouds = new EffClouds(World.cur().diffCur.NewCloudsRender, i, f);
            if (i > 5) try {
                if (main3d.zip != null) main3d.zip.destroy();
                main3d.zip = new Zip(f);
            } catch (Exception exception) {
                System.out.println("Zip load error: " + exception);
            }
            int i_17_ = 5 - i;
            if (i == 6) i_17_ = 1;
            if (i_17_ > 4) i_17_ = 4;
            RenderContext.cfgLandFogHaze.set(i_17_);
            RenderContext.cfgLandFogHaze.apply();
            RenderContext.cfgLandFogHaze.reset();
            RenderContext.cfgLandFogLow.set(0);
            RenderContext.cfgLandFogLow.apply();
            RenderContext.cfgLandFogLow.reset();
            if (Actor.isValid(main3d.spritesFog)) main3d.spritesFog.destroy();
            main3d.spritesFog = new SpritesFog(camera3d, 0.7F, 7000.0F, 7500.0F);
        }
    }

    public static void setCloudsType(int i) {
        if (i < 0) i = 0;
        if (i > 6) i = 6;
        if (cur() != null) cur().curCloudsType = i;
        if (Config.isUSE_RENDER()) {
            Main3D main3d = Main3D.cur3D();
            if (main3d.clouds != null) main3d.clouds.setType(i);
            int i_18_ = 5 - i;
            if (i == 6) i_18_ = 1;
            if (i_18_ > 4) i_18_ = 4;
            RenderContext.cfgLandFogHaze.set(i_18_);
            RenderContext.cfgLandFogHaze.apply();
            RenderContext.cfgLandFogHaze.reset();
            RenderContext.cfgLandFogLow.set(0);
            RenderContext.cfgLandFogLow.apply();
            RenderContext.cfgLandFogLow.reset();
        }
    }

    public static void setCloudsHeight(float f) {
        if (cur() != null) cur().curCloudsHeight = f;
        if (Config.isUSE_RENDER()) {
            Main3D main3d = Main3D.cur3D();
            if (main3d.clouds != null) main3d.clouds.setHeight(f);
        }
    }

    private void loadRespawnTime(SectFile sectfile) {
        respawnMap.clear();
        int i = sectfile.sectionIndex("RespawnTime");
        if (i >= 0) {
            int i_19_ = sectfile.vars(i);
            for (int i_20_ = 0; i_20_ < i_19_; i_20_++) {
                String string = sectfile.var(i, i_20_);
                float f = sectfile.get("RespawnTime", string, 1800.0F, 20.0F, 1200000.0F);
                respawnMap.put(string, new Float(f));
            }
        }
    }

    private List loadWings(SectFile sectfile) throws Exception {
        int i = sectfile.sectionIndex("Wing");
        if (i < 0) return null;

        if (!World.cur().diffCur.Takeoff_N_Landing) this.prepareTakeoff(sectfile, !Main.cur().netServerParams.isSingle());

        NetChannel netchannel = null;

        if (!isServer()) netchannel = this.net.masterChannel();

        int i_21_ = sectfile.vars(i);
        ArrayList arraylist = null;
        if (i_21_ > 0) arraylist = new ArrayList(i_21_);
        
//        System.out.println("Mission loadWings...");
        for (int t=0; t<World.cur().triggersGuard.getListTriggerAircraftSpawn().size(); t++)
            System.out.println("Trigger Aircraft Spawn no." + (t+1) + "=" + World.cur().triggersGuard.getListTriggerAircraftSpawn().get(t).toString());

        for (int i_22_ = 0; i_22_ < i_21_; i_22_++) {
            this.LOADING_STEP(30 + Math.round((float) i_22_ / (float) i_21_ * 30.0F), "task.Load_aircraft");
            String string = sectfile.var(i, i_22_);
//            System.out.println("Aircraft to Spawn = " + string);
            this._loadPlayer = string.equals(this.player);
            int i_23_ = sectfile.get(string, "StartTime", 0);
            if (i_23_ > 0 && !this._loadPlayer) {
                if (netchannel == null) {
                    double d = i_23_ * 60L;
//                  // TODO: +++ Trigger backport from HSFX 7.0.3 MODIFIED by SAS~Storebror +++
                    // TODO: Changed by SAS~Storebror: Don't delay-spawn aircraft when they have a Trigger spawning them!
                    if(World.cur().triggersGuard.getListTriggerAircraftSpawn().contains(string)) d= Double.MAX_VALUE;
                    // ---
 //                  // TODO: --- Trigger backport from HSFX 7.0.3 MODIFIED by SAS~Storebror ---
                    new MsgAction(0, d, new TimeOutWing(string)) {
                        public void doAction(Object object) {
                            TimeOutWing timeoutwing = (TimeOutWing) object;
                            timeoutwing.start();
                        }
                    };
                }
            } else
                // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
                if(!World.cur().triggersGuard.getListTriggerAircraftSpawn().contains(string)) 
                    // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
            {
                NetAircraft.loadingCoopPlane = Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop();

                Wing wing = new Wing();
                wing.load(sectfile, string, netchannel);

                if (netchannel != null && !Main.cur().netServerParams.isCoop()) {
                    Aircraft[] aircrafts = wing.airc;
                    for (int i_27_ = 0; i_27_ < aircrafts.length; i_27_++)
                        if (Actor.isValid(aircrafts[i_27_]) && aircrafts[i_27_].net == null) {
                            aircrafts[i_27_].destroy();
                            aircrafts[i_27_] = null;
                        }
                }
                arraylist.add(wing);
                this.prepareSkinInWing(sectfile, wing);
            }
        }
        this.LOADING_STEP(60.0F, "task.Load_aircraft");
        return arraylist;
    }

    // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
    public void loadWingOnTrigger(String s)
    {
        // System.out.println("loadWingOnTrigger(" + s + ")");
        if (!Mission.isSingle() && !Mission.isServer()) return;
        //if (this.net.isMirror()) return; // TODO: TEST Added by SAS~Storebror to fight dedicated Server / Client issues!
        //if (this.net.isMirror()) this.actors.add(null); // TODO: TEST Added by SAS~Storebror to fight dedicated Server / Client issues!
        try
        {
            NetAircraft.loadingCoopPlane = Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop(); // false;
            Wing localWing = new Wing();
            
            NetChannel netchannel = null;
            if (!isServer()) netchannel = this.net.masterChannel();
            
//            if (this.net.isMirror()) {
//                Actor actor = Actor.getByName(s);
//                this.actors.add(actor);
//                System.out.println("loadWingOnTrigger net.isMirror, actors size: " + this.actors.size() + ", curActor: " + this.curActor + ", actor=" + actor.name() + "(" + actor.getClass().getName() + ")");
//            }
            
            localWing.load(sectFile, s, netchannel, this.net.isMirror());
            
//            localWing.load(sectFile, s, null);
            prepareSkinInWing(sectFile, localWing);
            localWing.setOnAirport();
        }
        catch(Exception e)
        {
            System.out.println("Mission error, ID_31 : Wing not load : " + e.toString());
            e.printStackTrace();
        }
    }
    // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

     private void prepareSkinInWing(SectFile sectfile, Wing wing) {
        if (Config.isUSE_RENDER()) {
            Aircraft[] aircrafts = wing.airc;
            for (int i = 0; i < aircrafts.length; i++)
                if (Actor.isValid(aircrafts[i])) {
                    Aircraft aircraft = aircrafts[i];
                    this.prepareSkinInWing(sectfile, aircraft, wing.name(), i);
                }
        }
    }

    private void prepareSkinInWing(SectFile sectfile, Aircraft aircraft, String string, int i) {
        if (Config.isUSE_RENDER()) if (World.getPlayerAircraft() == aircraft) {
            if (isSingle()) if (NetMissionTrack.isPlaying()) {
                ((NetUser) Main.cur().netServerParams.host()).tryPrepareSkin(aircraft);
                ((NetUser) Main.cur().netServerParams.host()).tryPrepareNoseart(aircraft);
                ((NetUser) Main.cur().netServerParams.host()).tryPreparePilot(aircraft);
            } else {
                String string_28_ = Property.stringValue(aircraft.getClass(), "keyName", null);
                String string_29_ = World.cur().userCfg.getSkin(string_28_);
                if (string_29_ != null) {
                    String string_30_ = GUIAirArming.validateFileName(string_28_);
                    ((NetUser) NetEnv.host()).setSkin(string_30_ + "/" + string_29_);
                    ((NetUser) NetEnv.host()).tryPrepareSkin(aircraft);
                } else((NetUser) NetEnv.host()).setSkin(null);
                String string_31_ = World.cur().userCfg.getNoseart(string_28_);
                if (string_31_ != null) {
                    ((NetUser) NetEnv.host()).setNoseart(string_31_);
                    ((NetUser) NetEnv.host()).tryPrepareNoseart(aircraft);
                } else((NetUser) NetEnv.host()).setNoseart(null);
                String string_32_ = World.cur().userCfg.netPilot;
                ((NetUser) NetEnv.host()).setPilot(string_32_);
                if (string_32_ != null) ((NetUser) NetEnv.host()).tryPreparePilot(aircraft);
            }
        } else {
            String string_33_ = sectfile.get(string, "skin" + i, (String) null);
            if (string_33_ != null) {
                String string_34_ = Aircraft.getPropertyMesh(aircraft.getClass(), aircraft.getRegiment().country());
                string_33_ = GUIAirArming.validateFileName(Property.stringValue(aircraft.getClass(), "keyName", null)) + "/" + string_33_;
                if (NetMissionTrack.isPlaying()) {
                    string_33_ = NetFilesTrack.getLocalFileName(Main.cur().netFileServerSkin, Main.cur().netServerParams.host(), string_33_);
                    if (string_33_ != null) string_33_ = Main.cur().netFileServerSkin.alternativePath() + "/" + string_33_;
                } else string_33_ = Main.cur().netFileServerSkin.primaryPath() + "/" + string_33_;
                if (string_33_ != null) {
                    String string_35_ = "PaintSchemes/Cache/" + Finger.file(0L, string_33_, -1);
                    Aircraft.prepareMeshSkin(string_34_, aircraft.hierMesh(), string_33_, string_35_);
                }
            }
            String string_36_ = sectfile.get(string, "noseart" + i, (String) null);
            if (string_36_ != null) {
                String string_37_ = Main.cur().netFileServerNoseart.primaryPath() + "/" + string_36_;
                String string_38_ = string_36_.substring(0, string_36_.length() - 4);
                if (NetMissionTrack.isPlaying()) {
                    string_37_ = NetFilesTrack.getLocalFileName(Main.cur().netFileServerNoseart, Main.cur().netServerParams.host(), string_36_);
                    if (string_37_ != null) {
                        string_38_ = string_37_.substring(0, string_37_.length() - 4);
                        string_37_ = Main.cur().netFileServerNoseart.alternativePath() + "/" + string_37_;
                    }
                }
                if (string_37_ != null) {
                    String string_39_ = "PaintSchemes/Cache/Noseart0" + string_38_ + ".tga";
                    String string_40_ = "PaintSchemes/Cache/Noseart0" + string_38_ + ".mat";
                    String string_41_ = "PaintSchemes/Cache/Noseart1" + string_38_ + ".tga";
                    String string_42_ = "PaintSchemes/Cache/Noseart1" + string_38_ + ".mat";
                    if (BmpUtils.bmp8PalTo2TGA4(string_37_, string_39_, string_41_)) Aircraft.prepareMeshNoseart(aircraft.hierMesh(), string_40_, string_42_, string_39_, string_41_, null);
                }
            }
            String string_43_ = sectfile.get(string, "pilot" + i, (String) null);
            if (string_43_ != null) {
                String string_44_ = Main.cur().netFileServerPilot.primaryPath() + "/" + string_43_;
                String string_45_ = string_43_.substring(0, string_43_.length() - 4);
                if (NetMissionTrack.isPlaying()) {
                    string_44_ = NetFilesTrack.getLocalFileName(Main.cur().netFileServerPilot, Main.cur().netServerParams.host(), string_43_);
                    if (string_44_ != null) {
                        string_45_ = string_44_.substring(0, string_44_.length() - 4);
                        string_44_ = Main.cur().netFileServerPilot.alternativePath() + "/" + string_44_;
                    }
                }
                if (string_44_ != null) {
                    String string_46_ = "PaintSchemes/Cache/Pilot" + string_45_ + ".tga";
                    String string_47_ = "PaintSchemes/Cache/Pilot" + string_45_ + ".mat";
                    if (BmpUtils.bmp8PalToTGA3(string_44_, string_46_)) Aircraft.prepareMeshPilot(aircraft.hierMesh(), 0, string_47_, string_46_, null);
                }
            }
        }
    }

    public void prepareSkinAI(Aircraft aircraft) {
        String string = aircraft.name();
        if (string.length() >= 4) {
            String string_48_ = string.substring(0, string.length() - 1);
            int i;
            try {
                i = Integer.parseInt(string.substring(string.length() - 1, string.length()));
            } catch (Exception exception) {
                return;
            }
            this.prepareSkinInWing(this.sectFile, aircraft, string_48_, i);
        }
    }

    public void recordNetFiles() {
        // TODO: Implement 4.10.1 Codechanges +++
// if (isDogfight()) return;
// TODO: Implement 4.10.1 Codechanges ---

        int i = this.sectFile.sectionIndex("Wing");
        if (i >= 0) {
            int i1 = this.sectFile.vars(i);
            for (int i2 = 0; i2 < i1; i2++)
                try {
                    String string = this.sectFile.var(i, i2);
                    String string1 = this.sectFile.get(string, "Class", (String) null);
                    Class class1 = ObjIO.classForName(string1);
                    String string2 = GUIAirArming.validateFileName(Property.stringValue(class1, "keyName", null));
                    for (int i3 = 0; i3 < 4; i3++) {
                        String string4 = this.sectFile.get(string, "skin" + i3, (String) null);
                        if (string4 != null) this.recordNetFile(Main.cur().netFileServerSkin, string2 + "/" + string4);
                        this.recordNetFile(Main.cur().netFileServerNoseart, this.sectFile.get(string, "noseart" + i3, (String) null));
                        this.recordNetFile(Main.cur().netFileServerPilot, this.sectFile.get(string, "pilot" + i3, (String) null));
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
        }
    }

    private void recordNetFile(NetFileServerDef netfileserverdef, String string) {
        if (string != null) {
            String string_55_ = string;
            if (NetMissionTrack.isPlaying()) {
                string_55_ = NetFilesTrack.getLocalFileName(netfileserverdef, Main.cur().netServerParams.host(), string);
                if (string_55_ == null) return;
            }
            NetFilesTrack.recordFile(netfileserverdef, (NetUser) Main.cur().netServerParams.host(), string, string_55_);
        }
    }

    // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
    // TODO: Added by SAS~Storebror for Trigger online replication compatibility
    public Aircraft loadAir(SectFile sectfile, String aircraftClassName, String wingName, String aircraftName, int positionInWing) throws Exception {
        return this.loadAir(sectfile, aircraftClassName, wingName, aircraftName, positionInWing, false);
    }
    // ---
    
    public Aircraft loadAir(SectFile sectfile, String aircraftClassName, String wingName, String aircraftName, int positionInWing, boolean loadFromTrigger) throws Exception {
        // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
        boolean isNotServer = !isServer();
        Class aircraftClass = ObjIO.classForName(aircraftClassName);
        Aircraft aircraft = (Aircraft) aircraftClass.newInstance();
        if (Main.cur().netServerParams.isSingle() && this._loadPlayer) {
            if (Property.value(aircraftClass, "cockpitClass", null) == null) throw new Exception("One of selected aircraft has no cockpit.");
            if (this.playerNum == 0) {
                World.setPlayerAircraft(aircraft);
                this._loadPlayer = false;
            } else this.playerNum--;
        }
        aircraft.setName(aircraftName);
        // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
        // TODO: Added by SAS~Storebror for Trigger online replication compatibility
//      if (loadFromTrigger && this.curActor >= this.actors.size()) this.actors.add(new Integer(aircraft.net == null?0:aircraft.net.idLocal()));
      if (loadFromTrigger && this.curActor >= this.actors.size()) this.actors.add(new Integer(aircraft.net.idLocal()));
      // ---
      // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
        int actorIndex = 0;
        if (isNotServer) {
            actorIndex = ((Integer) this.actors.get(this.curActor)).intValue();
            if (actorIndex == 0) aircraft.load(sectfile, wingName, positionInWing, null, 0);
            else aircraft.load(sectfile, wingName, positionInWing, this.net.masterChannel(), actorIndex);
        } else aircraft.load(sectfile, wingName, positionInWing, null, 0);
        if (aircraft.isSpawnFromMission()) {
            if (this.net.isMirror()) {
                if (actorIndex == 0) {
                    this.actors.set(this.curActor++, null);
                } else {
                    this.actors.set(this.curActor++, aircraft);
                }
            } else {
                this.actors.add(aircraft);
            }
        }
        aircraft.pos.reset();
        return aircraft;
    }

    public static void preparePlayerNumberOn(SectFile sectfile) {
        UserCfg usercfg = World.cur().userCfg;
        String string = sectfile.get("MAIN", "player", "");
        if (!"".equals(string)) {
            String string_59_ = sectfile.get("MAIN", "playerNum", "");
            sectfile.set(string, "numberOn" + string_59_, usercfg.netNumberOn ? "1" : "0");
        }
    }

    private void prepareTakeoff(SectFile sectfile, boolean bool) {
        if (bool) {
            int i = sectfile.sectionIndex("Wing");
            if (i < 0) return;
            int i_60_ = sectfile.vars(i);
            for (int i_61_ = 0; i_61_ < i_60_; i_61_++)
                this.prepareWingTakeoff(sectfile, sectfile.var(i, i_61_));
        } else {
            String string = sectfile.get("MAIN", "player", (String) null);
            if (string == null) return;
            this.prepareWingTakeoff(sectfile, string);
        }
        this.sectFinger = sectfile.fingerExcludeSectPrefix("$$$");
    }

    private void prepareWingTakeoff(SectFile sectfile, String string) {
        String string_62_ = string + "_Way";
        int i = sectfile.sectionIndex(string_62_);
        if (i >= 0) {
            int i_63_ = sectfile.vars(i);
            if (i_63_ != 0) {
                ArrayList arraylist = new ArrayList(i_63_);
                for (int i_64_ = 0; i_64_ < i_63_; i_64_++)
                    arraylist.add(sectfile.line(i, i_64_));
                String string_65_ = (String) arraylist.get(0);
                if (string_65_.startsWith("TAKEOFF")) {
                    StringBuffer stringbuffer = new StringBuffer("NORMFLY");
                    NumberTokenizer numbertokenizer = new NumberTokenizer(string_65_);
                    numbertokenizer.next((String) null);
                    double d = numbertokenizer.next(1000.0);
                    double d_66_ = numbertokenizer.next(1000.0);
                    WingTakeoffPos wingtakeoffpos = new WingTakeoffPos(d, d_66_);
                    if (this.mapWingTakeoff == null) {
                        this.mapWingTakeoff = new HashMap();
                        this.mapWingTakeoff.put(wingtakeoffpos, wingtakeoffpos);
                    } else for (;;) {
                        WingTakeoffPos wingtakeoffpos_67_ = (WingTakeoffPos) this.mapWingTakeoff.get(wingtakeoffpos);
                        if (wingtakeoffpos_67_ == null) {
                            this.mapWingTakeoff.put(wingtakeoffpos, wingtakeoffpos);
                            break;
                        }
                        wingtakeoffpos.x += 200;
                    }
                    d = wingtakeoffpos.x;
                    d_66_ = wingtakeoffpos.y;
                    stringbuffer.append(" ");
                    stringbuffer.append(d);
                    stringbuffer.append(" ");
                    stringbuffer.append(d_66_);
                    do {
                        if (i_63_ > 1) {
                            String string_68_ = (String) arraylist.get(1);
                            if (!string_68_.startsWith("TAKEOFF") && !string_68_.startsWith("LANDING")) {
                                numbertokenizer = new NumberTokenizer(string_68_);
                                numbertokenizer.next((String) null);
                                numbertokenizer.next((String) null);
                                numbertokenizer.next((String) null);
                                stringbuffer.append(" ");
                                stringbuffer.append(numbertokenizer.next("1000.0"));
                                stringbuffer.append(" ");
                                stringbuffer.append(numbertokenizer.next("300.0"));
                                arraylist.set(0, stringbuffer.toString());
                                break;
                            }
                        }
                        stringbuffer.append(" 1000 300");
                        arraylist.set(0, stringbuffer.toString());
                    } while (false);
                    sectfile.sectionClear(i);
                    for (int i_69_ = 0; i_69_ < i_63_; i_69_++)
                        sectfile.lineAdd(i, (String) arraylist.get(i_69_));
                }
            }
        }
    }

//    private void loadChiefs(SectFile sectfile) throws Exception {
//        int i = sectfile.sectionIndex("Chiefs");
//        if (i >= 0) {
//            if (chiefsIni == null) chiefsIni = new SectFile("com/maddox/il2/objects/chief.ini");
//            int i_70_ = sectfile.vars(i);
//            for (int i_71_ = 0; i_71_ < i_70_; i_71_++) {
//                this.LOADING_STEP(60 + Math.round((float) i_71_ / (float) i_70_ * 20.0F), "task.Load_tanks");
//                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_71_));
//                String string = numbertokenizer.next();
//                String string_72_ = numbertokenizer.next();
//                int i_73_ = numbertokenizer.next(-1);
//                if (i_73_ < 0) System.out.println("Mission: Wrong chief's army [" + i_73_ + "]");
//                else {
//                    Chief.new_DELAY_WAKEUP = numbertokenizer.next(0.0F);
//                    Chief.new_SKILL_IDX = numbertokenizer.next(2);
//                    if (Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3) System.out.println("Mission: Wrong chief's skill [" + Chief.new_SKILL_IDX + "]");
//                    else {
//                        Chief.new_SLOWFIRE_K = numbertokenizer.next(1.0F);
//                        if (Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100.0F) System.out.println("Mission: Wrong chief's slowfire [" + Chief.new_SLOWFIRE_K + "]");
//                        else if (chiefsIni.sectionIndex(string_72_) < 0) System.out.println("Mission: Wrong chief's type [" + string_72_ + "]");
//                        else {
//                            int i_74_ = string_72_.indexOf('.');
//                            if (i_74_ <= 0) System.out.println("Mission: Wrong chief's type [" + string_72_ + "]");
//                            else {
//                                String string_75_ = string_72_.substring(0, i_74_);
//                                String string_76_ = string_72_.substring(i_74_ + 1);
//                                String string_77_ = chiefsIni.get(string_75_, string_76_);
//                                if (string_77_ == null) System.out.println("Mission: Wrong chief's type [" + string_72_ + "]");
//                                else {
//                                    numbertokenizer = new NumberTokenizer(string_77_);
//                                    string_77_ = numbertokenizer.nextToken();
//                                    numbertokenizer.nextToken();
//                                    String string_78_ = null;
//                                    if (numbertokenizer.hasMoreTokens()) string_78_ = numbertokenizer.nextToken();
//                                    Class var_class = ObjIO.classForName(string_77_);
//                                    if (var_class == null) System.out.println("Mission: Unknown chief's class [" + string_77_ + "]");
//                                    else {
//                                        Constructor constructor;
//                                        try {
//                                            Class[] var_classes = new Class[6];
//
//                                            var_classes[0] = String.class;
//                                            var_classes[1] = Integer.TYPE;
//                                            var_classes[2] = SectFile.class;
//                                            var_classes[3] = String.class;
//                                            var_classes[4] = SectFile.class;
//                                            var_classes[5] = String.class;
//                                            constructor = var_class.getConstructor(var_classes);
//                                        } catch (Exception exception) {
//                                            System.out.println("Mission: No required constructor in chief's class [" + string_77_ + "]");
//                                            continue;
//                                        }
//                                        int i_79_ = this.curActor;
//                                        Object object;
//                                        try {
//                                            Object[] objects = new Object[6];
//                                            objects[0] = string;
//                                            objects[1] = new Integer(i_73_);
//                                            objects[2] = chiefsIni;
//                                            objects[3] = string_72_;
//                                            objects[4] = sectfile;
//                                            objects[5] = string + "_Road";
//                                            object = constructor.newInstance(objects);
//                                        } catch (Exception exception) {
//                                            System.out.println("Mission: Can't create chief '" + string + "' [class:" + string_77_ + "]");
//                                            exception.printStackTrace();
//                                            continue;
//                                        }
//                                        if (string_78_ != null) ((Actor) object).icon = IconDraw.get(string_78_);
//                                        if (i_79_ != this.curActor && this.net != null && this.net.isMirror()) for (int i_80_ = i_79_; i_80_ < this.curActor; i_80_++) {
//                                            Actor actor = (Actor) this.actors.get(i_80_);
//                                            if (actor.net == null || actor.net.isMaster()) {
//                                                if (Actor.isValid(actor)) {
//                                                    if (object instanceof ChiefGround) ((ChiefGround) object).Detach(actor, actor);
//                                                    actor.destroy();
//                                                }
//                                                this.actors.set(i_80_, null);
//                                            }
//                                        }
//                                        if (object instanceof ChiefGround) ((ChiefGround) object).dreamFire(true);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//  private void dumpActors() {
//  for (int i=0; i<this.actors.size(); i++)
//      if (this.actors.get(i) instanceof Actor) System.out.println("this.actors[" + i + "]=" + ((Actor)this.actors.get(i)).name());
//  for (int i=0; i<Engine.targets().size(); i++)
//      if (Engine.targets().get(i) instanceof Actor) System.out.println("Engine.targets[" + i + "]=" + ((Actor)Engine.targets().get(i)).name());
//}

// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
private void loadChiefs(SectFile sectfile)
      throws Exception
  {
      loadChiefs(sectfile, false, "");
  }

  public void loadChiefsTrigger(String s)
  {
      // System.out.println("loadChiefsTrigger(" + s + ")");
//      if (!Mission.isSingle() && !Mission.isServer()) return;
      try
      {
//          if (this.net.isMirror()) return; // TODO: TEST Added by SAS~Storebror to fight dedicated Server / Client issues!
//          if (this.net.isMirror()) this.actors.add(null); // TODO: TEST Added by SAS~Storebror to fight dedicated Server / Client issues!
          
          boolean spawnFromMission = Reflection.getBoolean(Actor.class, "bSpawnFromMission");
          Actor.setSpawnFromMission(true);
//          this.dumpActors();
          loadChiefs(sectFile, true, s);
//          this.dumpActors();
          Actor.setSpawnFromMission(spawnFromMission);
      }
      catch(Exception e)
      {
          System.out.println("Mission error, ID_31 : Chiefs not load : " + e.toString());
          e.printStackTrace();
      }
  }

  private void loadChiefs(SectFile sectfile, boolean flagTrigger, String s)
//private void loadChiefs(SectFile sectfile)
  // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
      throws Exception {
      
      // System.out.println("loadChiefs(" + sectfile.fileName() + ", " + flagTrigger + ", " + s + ")");
  int sectIndex = sectfile.sectionIndex("Chiefs");
  if (sectIndex >= 0) {
      if (chiefsIni == null) chiefsIni = new SectFile("com/maddox/il2/objects/chief.ini");
      int sectVarsNum = sectfile.vars(sectIndex);
      for (int sectVarIndex = 0; sectVarIndex < sectVarsNum; sectVarIndex++) {
          // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
          if(!flagTrigger)
              // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
              this.LOADING_STEP(60 + Math.round((float) sectVarIndex / (float) sectVarsNum * 20.0F), "task.Load_tanks");
          NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(sectIndex, sectVarIndex));
          String chiefName = numbertokenizer.next();
          // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
          if((!flagTrigger && World.cur().triggersGuard.getListTriggerChiefSpawn().contains(chiefName)) || (flagTrigger && !chiefName.equals(s)))
              continue;
          // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
          String chiefType = numbertokenizer.next();
          int chiefArmy = numbertokenizer.next(-1);
          if (chiefArmy < 0) System.out.println("Mission: Wrong chief's army [" + chiefArmy + "]");
          else {
              Chief.new_DELAY_WAKEUP = numbertokenizer.next(0.0F);
              Chief.new_SKILL_IDX = numbertokenizer.next(2);
              if (Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3) System.out.println("Mission: Wrong chief's skill [" + Chief.new_SKILL_IDX + "]");
              else {
                  Chief.new_SLOWFIRE_K = numbertokenizer.next(1.0F);
                  if (Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100.0F) System.out.println("Mission: Wrong chief's slowfire [" + Chief.new_SLOWFIRE_K + "]");
                  else if (chiefsIni.sectionIndex(chiefType) < 0) System.out.println("Mission: Wrong chief's type [" + chiefType + "]");
                  else {
                      int typeSeparatorPos = chiefType.indexOf('.');
                      if (typeSeparatorPos <= 0) System.out.println("Mission: Wrong chief's type [" + chiefType + "]");
                      else {
                          String chiefTypePackage = chiefType.substring(0, typeSeparatorPos);
                          String chiefTypeName = chiefType.substring(typeSeparatorPos + 1);
                          String chiefFromIni = chiefsIni.get(chiefTypePackage, chiefTypeName);
                          if (chiefFromIni == null) System.out.println("Mission: Wrong chief's type [" + chiefType + "]");
                          else {
                              numbertokenizer = new NumberTokenizer(chiefFromIni);
                              chiefFromIni = numbertokenizer.nextToken();
                              numbertokenizer.nextToken();
                              String chiefIcon = null;
                              if (numbertokenizer.hasMoreTokens()) chiefIcon = numbertokenizer.nextToken();
                              Class chiefClass = ObjIO.classForName(chiefFromIni);
                              if (chiefClass == null) System.out.println("Mission: Unknown chief's class [" + chiefFromIni + "]");
                              else {
                                  Constructor constructor;
                                  try {
                                      Class[] chiefPropertyClasses = new Class[6];

                                      chiefPropertyClasses[0] = String.class;
                                      chiefPropertyClasses[1] = Integer.TYPE;
                                      chiefPropertyClasses[2] = SectFile.class;
                                      chiefPropertyClasses[3] = String.class;
                                      chiefPropertyClasses[4] = SectFile.class;
                                      chiefPropertyClasses[5] = String.class;
                                      constructor = chiefClass.getConstructor(chiefPropertyClasses);
                                  } catch (Exception exception) {
                                      System.out.println("Mission: No required constructor in chief's class [" + chiefFromIni + "]");
                                      continue;
                                  }
                                  int lastCurActor = this.curActor;
                                  Object object;
                                  try {
                                      Object[] chiefProperties = new Object[6];
                                      chiefProperties[0] = chiefName;
                                      chiefProperties[1] = new Integer(chiefArmy);
                                      chiefProperties[2] = chiefsIni;
                                      chiefProperties[3] = chiefType;
                                      chiefProperties[4] = sectfile;
                                      chiefProperties[5] = chiefName + "_Road";
                                      object = constructor.newInstance(chiefProperties);
                                  } catch (Exception exception) {
                                      System.out.println("Mission: Can't create chief '" + chiefName + "' [class:" + chiefFromIni + "]");
                                      exception.printStackTrace();
                                      continue;
                                  }
                                  if (chiefIcon != null) ((Actor) object).icon = IconDraw.get(chiefIcon);
                                  if (lastCurActor != this.curActor && this.net != null && this.net.isMirror() /*&& !flagTrigger*/) for (int actorIndex = lastCurActor; actorIndex < this.curActor; actorIndex++) {
//                                      System.out.print("loadChiefs TEST ");
//                                      if (this.actors.get(actorIndex) instanceof Actor) {
//                                          Actor actorTmp = (Actor) this.actors.get(actorIndex);
//                                          System.out.print("actor(" + actorIndex + ")=" + actorTmp.name() + " (" + actorTmp.getClass().getName() + ")");
//                                          System.out.println();
//                                      } else {
//                                          int actorIdx = ((Integer)this.actors.get(actorIndex)).intValue();
//                                          for (int tempIdx = 0; tempIdx < this.actors.size(); tempIdx++)
//                                              if (this.actors.get(actorIndex) instanceof Actor) {
//                                                  Actor actorTmp = (Actor) this.actors.get(actorIndex);
//                                                  if (actorTmp.net == null) continue;
//                                                  if (actorTmp.net.idLocal() != actorIdx && actorTmp.net.idRemote() != actorIdx) continue;
//                                                  System.out.print("actor(" + actorIndex + ")=" + actorTmp.name() + " (" + actorTmp.getClass().getName() + ")");
//                                                  System.out.println();
//                                              }                                            }
                                      if (!(this.actors.get(actorIndex) instanceof Actor)) continue;
                                      Actor actor = (Actor) this.actors.get(actorIndex);
                                      if (actor.net == null || actor.net.isMaster()) {
                                          if (Actor.isValid(actor)) {
                                              if (object instanceof ChiefGround) ((ChiefGround) object).Detach(actor, actor);
                                              actor.destroy();
                                          }
                                          this.actors.set(actorIndex, null);
                                      }
                                  }
                                  if (object instanceof ChiefGround) ((ChiefGround) object).dreamFire(true);
                              }
                          }
                      }
                  }
              }
          }
          // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
          if(flagTrigger && chiefName.equals(s))
              break;
          // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
      }
  }
}

  // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
//  public int getUnitNetIdRemote(Actor actor) {
//        if (this.net.isMaster()) {
//            this.actors.add(actor);
//            return 0;
//        }
//        Integer integer = (Integer) this.actors.get(this.curActor);
//        this.actors.set(this.curActor, actor);
//        this.curActor++;
//        return integer.intValue();
//    }
  
  public int getUnitNetIdRemote(Actor actor) {
      if (this.net.isMaster()) {
          this.actors.add(actor);
//          System.out.println("getUnitNetIdRemote net.isMaster, actors size: " + this.actors.size() + ", curActor: " + this.curActor + ", actor=" + actor.name() + "(" + actor.getClass().getName() + ")");
          return 0;
      } else if (this.net.isMirror() && this.curActor == this.actors.size()) { // TODO: Added by SAS~Storebror: Potentially adding an actor from Trigger
          this.actors.add(new Integer(actor.net == null?0:actor.net.idLocal()));
//          this.actors.add(new Integer(actor.net.idLocal()));
          this.curActor++;
//          System.out.println("getUnitNetIdRemote net.isMirror, actors size: " + this.actors.size() + ", curActor: " + this.curActor + ", actor=" + actor.name() + "(" + actor.getClass().getName() + ")");
          return actor.net == null?0:actor.net.idLocal();
//          return actor.net.idLocal();
      }
      Integer integer = (Integer) this.actors.get(this.curActor);
      this.actors.set(this.curActor, actor);
      this.curActor++;
//      System.out.println("getUnitNetIdRemote, actors size: " + this.actors.size() + ", curActor: " + this.curActor + ", actor=" + actor.name() + "(" + actor.getClass().getName() + ")");
      return integer.intValue();
  }
  // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

    private Actor loadStationaryActor(String string, String string_81_, int i, double d, double d_82_, float f, float f_83_, String string_84_, String string_85_, String string_86_) {
        // System.out.println("loadStationaryActor(" + string + ", " + string_81_+ ", " + i + ", " + d + ", " + d_82_ + ", " + f + ", " + f_83_ + ", " + string_84_ + ", " + string_85_ + ", " + string_86_ + ") net.isMirror()" + this.net.isMirror());
        Class var_class;
        try {
            var_class = ObjIO.classForName(string_81_);
        } catch (Exception exception) {
            System.out.println("Mission: class '" + string_81_ + "' not found");
            return null;
        }
        ActorSpawn actorspawn = (ActorSpawn) Spawn.get(var_class.getName(), false);
        if (actorspawn == null) {
            System.out.println("Mission: ActorSpawn for '" + string_81_ + "' not found");
            return null;
        }
        spawnArg.clear();
        if (string != null) {
            if ("NONAME".equals(string)) {
                System.out.println("Mission: 'NONAME' - not valid actor name");
                return null;
            }
            if (Actor.getByName(string) != null) {
                System.out.println("Mission: actor '" + string + "' alredy exist");
                return null;
            }
            spawnArg.name = string;
        }
        spawnArg.army = i;
        spawnArg.armyExist = true;
        spawnArg.country = string_84_;
        Chief.new_DELAY_WAKEUP = 0.0F;
        ArtilleryGeneric.new_RADIUS_HIDE = 0.0F;
        if (string_84_ != null) try {
            Chief.new_DELAY_WAKEUP = Integer.parseInt(string_84_);
            ArtilleryGeneric.new_RADIUS_HIDE = Chief.new_DELAY_WAKEUP;
        } catch (Exception exception) {
            /* empty */
        }
        Chief.new_SKILL_IDX = 2;
        if (string_85_ != null) {
            try {
                Chief.new_SKILL_IDX = Integer.parseInt(string_85_);
            } catch (Exception exception) {
                /* empty */
            }
            if (Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3) {
                System.out.println("Mission: Wrong actor skill '" + Chief.new_SKILL_IDX + "'");
                return null;
            }
        }
        Chief.new_SLOWFIRE_K = 1.0F;
        if (string_86_ != null) {
            try {
                Chief.new_SLOWFIRE_K = Float.parseFloat(string_86_);
            } catch (Exception exception) {
                /* empty */
            }
            if (Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100.0F) {
                System.out.println("Mission: Wrong actor slowfire '" + Chief.new_SLOWFIRE_K + "'");
                return null;
            }
        }
        p.set(d, d_82_, 0.0);
        spawnArg.point = p;
        o.set(f, 0.0F, 0.0F);
        spawnArg.orient = o;
        if (f_83_ > 0.0F) {
            spawnArg.timeLenExist = true;
            spawnArg.timeLen = f_83_;
        }
        spawnArg.netChannel = null;
        spawnArg.netIdRemote = 0;
        if (this.net.isMirror() && !NetMissionTrack.isPlaying()) {
            spawnArg.netChannel = this.net.masterChannel();
            // TODO: Added by SAS~Storebror: Check Array bounds!
            if (this.curActor < 0 || this.curActor >= this.actors.size()) return null;
            // ---
            spawnArg.netIdRemote = ((Integer) this.actors.get(this.curActor)).intValue();
            if (spawnArg.netIdRemote == 0) {
                // System.out.println("Mission.loadStationaryActor 00");
                this.actors.set(this.curActor++, null);
                return null;
            }
        }
        Actor actor = null;
        try {
            // System.out.println("Mission.loadStationaryActor 01");
            actor = actorspawn.actorSpawn(spawnArg);
            if (!(actorspawn instanceof NetSpawn)) { // FIXME: Nutjob to fight NetMsgSpawn Exceptions...
                // System.out.println("Mission.loadStationaryActor 01b");
                Reflection.setInt(actor, "flags", Reflection.getInt(actor, "flags") | 0x1000);
            }
            // System.out.println("Mission.loadStationaryActor 02, actor=" + (actor==null?"null":actor.getClass().getName()));
        } catch (Exception exception) {
            // System.out.println("Mission.loadStationaryActor Exception in actorspawn.actorSpawn(" + spawnArg.name + ")");
            // System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        // System.out.println("Mission.loadStationaryActor 03");
        if (this.net.isMirror() && !NetMissionTrack.isPlaying()) this.actors.set(this.curActor++, actor);
        else this.actors.add(actor);
        // System.out.println("Mission.loadStationaryActor 04");

        return actor;
    }

    private void loadStationary(SectFile sectfile) {
        int i = sectfile.sectionIndex("Stationary");
        if (i >= 0) {
            int i_87_ = sectfile.vars(i);
            for (int i_88_ = 0; i_88_ < i_87_; i_88_++)
                try {
                    this.LOADING_STEP(80 + Math.round((float) i_88_ / (float) i_87_ * 5.0F), "task.Load_stationary_objects");
                    NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_88_));
                    this.loadStationaryActor(null, numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String) null),
                            numbertokenizer.next((String) null), numbertokenizer.next((String) null));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error when reading .mis Stationary section line " + i_88_ + " (" + ex.toString() + " )");
                }
        }
    }

    private void loadNStationary(SectFile sectfile) {
        int i = sectfile.sectionIndex("NStationary");
        if (i >= 0) {
            int i_89_ = sectfile.vars(i);
            for (int i_90_ = 0; i_90_ < i_89_; i_90_++)
                try {
                    this.LOADING_STEP(85 + Math.round((float) i_90_ / (float) i_89_ * 5.0F), "task.Load_stationary_objects");
                    NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_90_));
                    // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
//                  this.loadStationaryActor(numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F),
//                          numbertokenizer.next((String) null), numbertokenizer.next((String) null), numbertokenizer.next((String) null));
                  String curName = numbertokenizer.next("");
                  if(!World.cur().triggersGuard.getListTriggerStaticSpawn().contains(curName))
                    this.loadStationaryActor(curName, numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F),
                                            numbertokenizer.next((String) null), numbertokenizer.next((String) null), numbertokenizer.next((String) null));
                  // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
                } catch (Exception ex) {
                    if (this.isDestroyed()) return;
                    System.out.println("Error when reading .mis NStationary section line " + i_90_ + " (" + ex.toString() + " )");
                }
        }
    }

    public void loadNStationaryTrigger(String s)
    {
        // System.out.println("loadNStationaryTrigger(" + s + ")");
        if (!Mission.isSingle() && !Mission.isServer()) return;
//        if (this.net.isMirror()) return; // TODO: TEST Added by SAS~Storebror to fight dedicated Server / Client issues!
//        if (this.net.isMirror()) this.actors.add(null); // TODO: TEST Added by SAS~Storebror to fight dedicated Server / Client issues!
        int sectionIndex = sectFile.sectionIndex("NStationary");
        if(sectionIndex >= 0)
        {
            int numVars = sectFile.vars(sectionIndex);
            for(int varIndex = 0; varIndex < numVars; varIndex++)
            {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectFile.line(sectionIndex, varIndex));
                String curName = numbertokenizer.next("");
                if(!curName.equals(s))
                    continue;
//                System.out.println("before loadStationaryActor");
                this.loadStationaryActor(curName, numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F),
                                        numbertokenizer.next((String) null), numbertokenizer.next((String) null), numbertokenizer.next((String) null));
//                System.out.println("after loadStationaryActor");
                break;
            }

        }
    }

    private void loadRocketry(SectFile sectfile) {
        int i = sectfile.sectionIndex("Rocket");
        if (i >= 0) {
            int i_91_ = sectfile.vars(i);
            for (int i_92_ = 0; i_92_ < i_91_; i_92_++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_92_));
                if (numbertokenizer.hasMoreTokens()) {
                    String string = numbertokenizer.next("");
                    if (numbertokenizer.hasMoreTokens()) {
                        String string_93_ = numbertokenizer.next("");
                        if (numbertokenizer.hasMoreTokens()) {
                            int i_94_ = numbertokenizer.next(1, 1, 2);
                            double d = numbertokenizer.next(0.0);
                            if (numbertokenizer.hasMoreTokens()) {
                                double d_95_ = numbertokenizer.next(0.0);
                                if (numbertokenizer.hasMoreTokens()) {
                                    float f = numbertokenizer.next(0.0F);
                                    if (numbertokenizer.hasMoreTokens()) {
                                        float f_96_ = numbertokenizer.next(0.0F);
                                        int i_97_ = numbertokenizer.next(1);
                                        float f_98_ = numbertokenizer.next(20.0F);
                                        Point2d point2d = null;
                                        if (numbertokenizer.hasMoreTokens()) point2d = new Point2d(numbertokenizer.next(0.0), numbertokenizer.next(0.0));
                                        NetChannel netchannel = null;
                                        int i_99_ = 0;
                                        if (this.net.isMirror()) {
                                            netchannel = this.net.masterChannel();
                                            i_99_ = ((Integer) this.actors.get(this.curActor)).intValue();
                                            if (i_99_ == 0) {
                                                this.actors.set(this.curActor++, null);
                                                continue;
                                            }
                                        }
                                        RocketryGeneric rocketrygeneric = null;
                                        try {
                                            rocketrygeneric = RocketryGeneric.New(string, string_93_, netchannel, i_99_, i_94_, d, d_95_, f, f_96_, i_97_, f_98_, point2d);
                                        } catch (Exception exception) {
                                            System.out.println(exception.getMessage());
                                            exception.printStackTrace();
                                        }
                                        if (this.net.isMirror()) this.actors.set(this.curActor++, rocketrygeneric);
                                        else this.actors.add(rocketrygeneric);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void loadRocketryTrigger(String s)
    {
        // System.out.println("loadRocketryTrigger(" + s + ")");
        if (!Mission.isSingle() && !Mission.isServer()) return;
        int i = sectFile.sectionIndex("Rocket");
        if(i < 0)
            return;
        int j = sectFile.vars(i);
        for(int k = 0; k < j; k++)
        {
            NumberTokenizer localNumberTokenizer = new NumberTokenizer(sectFile.line(i, k));
            if(!localNumberTokenizer.hasMoreTokens())
                continue;
            String str1 = localNumberTokenizer.next("");
            if(!str1.equals(s) || !localNumberTokenizer.hasMoreTokens())
                continue;
            String str2 = localNumberTokenizer.next("");
            if(!localNumberTokenizer.hasMoreTokens())
                continue;
            int m = localNumberTokenizer.next(1, 1, 2);
            double d1 = localNumberTokenizer.next(0.0D);
            if(!localNumberTokenizer.hasMoreTokens())
                continue;
            double d2 = localNumberTokenizer.next(0.0D);
            if(!localNumberTokenizer.hasMoreTokens())
                continue;
            float f1 = localNumberTokenizer.next(0.0F);
            if(!localNumberTokenizer.hasMoreTokens())
                continue;
            float f2 = localNumberTokenizer.next(0.0F);
            int n = localNumberTokenizer.next(1);
            float f3 = localNumberTokenizer.next(20F);
            Point2d localPoint2d = null;
            if(localNumberTokenizer.hasMoreTokens())
                localPoint2d = new Point2d(localNumberTokenizer.next(0.0D), localNumberTokenizer.next(0.0D));
            NetChannel localNetChannel = null;
            int i1 = 0;
            if(net.isMirror())
            {
                localNetChannel = net.masterChannel();
                i1 = ((Integer)actors.get(curActor)).intValue();
                if(i1 == 0)
                {
                    actors.set(curActor++, null);
                    // System.out.println("loadRocketryTrigger 1, actors size: " + this.actors.size() + ", curActor: " + this.curActor);
                    continue;
                }
            }
            RocketryGeneric localRocketryGeneric = null;
            try
            {
                localRocketryGeneric = RocketryGeneric.New(str1, str2, localNetChannel, i1, m, d1, d2, f1, f2, n, f3, localPoint2d);
            }
            catch(Exception localException)
            {
                System.out.println(localException.getMessage());
                localException.printStackTrace();
            }
            if(net.isMirror())
                actors.set(curActor++, localRocketryGeneric);
            else
                actors.add(localRocketryGeneric);
            // System.out.println("loadRocketryTrigger 2, actors size: " + this.actors.size() + ", curActor: " + this.curActor);
        }

    }

     private void loadHouses(SectFile sectfile) {
        int i = sectfile.sectionIndex("Buildings");
        if (i >= 0) {
            int i_100_ = sectfile.vars(i);
            if (i_100_ != 0) if (this.net.isMirror()) {
                spawnArg.netChannel = this.net.masterChannel();
                spawnArg.netIdRemote = ((Integer) this.actors.get(this.curActor)).intValue();
                HouseManager housemanager = new HouseManager(sectfile, "Buildings", this.net.masterChannel(), ((Integer) this.actors.get(this.curActor)).intValue());
                this.actors.set(this.curActor++, housemanager);
            } else {
                HouseManager housemanager = new HouseManager(sectfile, "Buildings", null, 0);
                this.actors.add(housemanager);
            }
        }
    }

    private void loadBornPlaces(SectFile sectfile) {
        // TODO: Added by |ZUTI|: precaution, check for missing branches for existing regiments because
        // Home bases at first load all countries, if no country limitation is enabled
        // -------------------------------------------------
        ZutiSupportMethods.checkForMissingBranches();
        boolean zutiMdsSectionIdExists = sectfile.sectionIndex("MDS") > -1;
        // System.out.println("number of stay places: " + World.cur().airdrome.stay.length);
        // -------------------------------------------------

        ArrayList myArray = new ArrayList();
        int i = sectfile.sectionIndex("BornPlace");
        if (i >= 0) {
            int i_101_ = sectfile.vars(i);
            if (i_101_ != 0 && World.cur().airdrome != null && World.cur().airdrome.stay != null) {
                World.cur().bornPlaces = new ArrayList(i_101_);

                // TODO: Added by |ZUTI|
                // ---------------------------------------
                int zutiSpawnPlaceReductionCounter = 0;
                // ---------------------------------------

                for (int i_102_ = 0; i_102_ < i_101_; i_102_++)
                    try {
                        NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_102_));
                        int army = numbertokenizer.next(0, 0, Army.amountNet() - 1);
                        float radius = numbertokenizer.next(1000, 500, 10000);
                        double d = radius * radius;
                        float xCoordinate = numbertokenizer.next(0);
                        float yCoordinate = numbertokenizer.next(0);
                        boolean parachute = numbertokenizer.next(1) == 1;

                        // TODO: Added by |ZUTI|
                        // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                        int zutiSpawnHeight = 1000;
                        int zutiSpawnSpeed = 200;
                        int zutiSpawnOrient = 0;
                        int zutiMaxPilots = 0;
                        boolean zutiCanThisHomeBaseBeCaptured = false;
                        int zutiRadarHeight_MIN = 0;
                        int zutiRadarHeight_MAX = 5000;
                        int zutiRadarRange = 50;
                        boolean zutiAirspawnOnly = false;
                        boolean zutiEnablePlaneLimits = false;
                        boolean zutiDecreasingNumberOfPlanes = false;
                        boolean zutiIncludeStaticPlanes = false;
                        boolean zutiDisableSpawning = false;
                        boolean zutiEnableFriction = false;
                        boolean zutiStaticPositionOnly = false;
                        double zutiFriction = 3.8D;
                        int zutiCapturingRequiredParatroopers = 100;
                        boolean zutiDisableRendering = false;
                        int zutiCarrierSpawnPlaces = 0;
                        boolean zutiIsStandAloneBornPlace = false;
                        boolean zutiEnableQueue = false;
                        int zutiDeckClearTimeout = 30;
                        boolean zutiAirspawnIfQueueFull = false;
                        boolean zutiPilotInVulnerableWhileOnTheDeck = false;
                        boolean zutiCaptureOnlyIfChiefPresent = false;

// int id = 0;
                        try {
                            zutiSpawnHeight = numbertokenizer.next(1000, 0, 10000);
// id++;
                            zutiSpawnSpeed = numbertokenizer.next(200, 0, 500);
// id++;
                            zutiSpawnOrient = numbertokenizer.next(0, 0, 360);
// id++;
                            zutiMaxPilots = numbertokenizer.next(0, 0, 99999);
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiCanThisHomeBaseBeCaptured = true;
// id++;
                            zutiRadarHeight_MIN = numbertokenizer.next(0, 0, 99999);
// id++;
                            zutiRadarHeight_MAX = numbertokenizer.next(5000, 0, 99999);
// id++;
                            zutiRadarRange = numbertokenizer.next(50, 1, 99999);
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiAirspawnOnly = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiEnablePlaneLimits = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiDecreasingNumberOfPlanes = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiDisableSpawning = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiEnableFriction = true;
// id++;
                            zutiFriction = numbertokenizer.next(3.8D, 0.0D, 10.0D);
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiIncludeStaticPlanes = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiStaticPositionOnly = true;
// id++;
                            zutiCapturingRequiredParatroopers = numbertokenizer.next(100, 0, 99999);
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiDisableRendering = true;
// id++;
                            zutiCarrierSpawnPlaces = numbertokenizer.next(0, 0, 99999);
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiIsStandAloneBornPlace = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiEnableQueue = true;
// id++;
                            zutiDeckClearTimeout = numbertokenizer.next(30, 0, 99999);
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiAirspawnIfQueueFull = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiPilotInVulnerableWhileOnTheDeck = true;
// id++;
                            if (numbertokenizer.next(0, 0, 1) == 1) zutiCaptureOnlyIfChiefPresent = true;
// id++;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                        boolean hasSpawnPointsDefined = false;
                        Point_Stay[][] point_stays = World.cur().airdrome.stay;

                        // TODO: Added by |ZUTI|. The goal is to show all born places for coops too, not only dogfights
                        if (!isDogfight()) hasSpawnPointsDefined = true;
                        else for (int spawnPointsCounter = 0; spawnPointsCounter < point_stays.length; spawnPointsCounter++)
                            if (point_stays[spawnPointsCounter] != null) {
                                Point_Stay point_stay = point_stays[spawnPointsCounter][point_stays[spawnPointsCounter].length - 1];
                                // System.out.println("Mission - sp coordinates: " + point_stay.x + ", " + point_stay.y);
                                if ((point_stay.x - xCoordinate) * (point_stay.x - xCoordinate) + (point_stay.y - yCoordinate) * (point_stay.y - yCoordinate) <= d) {
                                    hasSpawnPointsDefined = true;
                                    break;
                                }
                            }

                        if (hasSpawnPointsDefined || zutiIsStandAloneBornPlace) {
                            BornPlace bornplace = new BornPlace(xCoordinate, yCoordinate, army, radius);
                            bornplace.zutiCanThisHomeBaseBeCaptured = zutiCanThisHomeBaseBeCaptured;
                            bornplace.zutiRadarHeight_MIN = zutiRadarHeight_MIN;
                            bornplace.zutiRadarHeight_MAX = zutiRadarHeight_MAX;
                            bornplace.zutiRadarRange = zutiRadarRange;
                            bornplace.zutiSpawnHeight = zutiSpawnHeight;
                            bornplace.zutiSpawnSpeed = zutiSpawnSpeed;
                            bornplace.zutiSpawnOrient = zutiSpawnOrient;
                            bornplace.zutiMaxBasePilots = zutiMaxPilots;
                            bornplace.zutiAirspawnOnly = zutiAirspawnOnly;
                            bornplace.zutiDisableSpawning = zutiDisableSpawning;
                            bornplace.zutiEnableFriction = zutiEnableFriction;
                            bornplace.zutiFriction = zutiFriction;
                            bornplace.zutiEnablePlaneLimits = zutiEnablePlaneLimits;
                            bornplace.zutiDecreasingNumberOfPlanes = zutiDecreasingNumberOfPlanes;
                            bornplace.zutiIncludeStaticPlanes = zutiIncludeStaticPlanes;
                            bornplace.bParachute = parachute;
                            bornplace.zutiBpIndex = i_102_;
                            bornplace.zutiStaticPositionOnly = zutiStaticPositionOnly;
                            bornplace.zutiCapturingRequiredParatroopers = zutiCapturingRequiredParatroopers;
                            bornplace.zutiDisableRendering = zutiDisableRendering;
                            bornplace.zutiCarrierSpawnPlaces = zutiCarrierSpawnPlaces;
                            bornplace.zutiIsStandAloneBornPlace = zutiIsStandAloneBornPlace;
                            bornplace.zutiEnableQueue = zutiEnableQueue;
                            bornplace.zutiDeckClearTimeout = zutiDeckClearTimeout;
                            bornplace.zutiAirspawnIfQueueFull = zutiAirspawnIfQueueFull;
                            bornplace.zutiPilotInVulnerableWhileOnTheDeck = zutiPilotInVulnerableWhileOnTheDeck;
                            bornplace.zutiCaptureOnlyIfNoChiefPresent = zutiCaptureOnlyIfChiefPresent;
                            World.cur().bornPlaces.add(bornplace);

                            if (bornplace.zutiCanThisHomeBaseBeCaptured) ZutiMDSVariables.ZUTI_FRONT_ENABLE_HB_CAPTURING = true;

                            myArray.add(bornplace);

                            // Load born place captured planes list
                            ZutiSupportMethods.loadCapturedPlanesList(bornplace, sectfile);
                            // TODO: Comment by |ZUTI|: changed so that only static actors get converted. This should be fine, i hope
                            // --------------------------------------------------------------------------------------------------------------------------
                            if (this.actors != null) {
                                int i_108_ = this.actors.size();
                                for (int i_109_ = 0; i_109_ < i_108_; i_109_++) {
                                    Actor actor = (Actor) this.actors.get(i_109_);
                                    // Changed that this will convert only static actors to appropriate army
                                    // if (Actor.isValid(actor) && actor.pos != null)
                                    if (Actor.isValid(actor) && actor.pos != null && ZutiSupportMethods.isStaticActor(actor)) {
                                        Point3d point3d = actor.pos.getAbsPoint();
                                        double d_110_ = (point3d.x - xCoordinate) * (point3d.x - xCoordinate) + (point3d.y - yCoordinate) * (point3d.y - yCoordinate);
                                        if (d_110_ <= d) actor.setArmy(bornplace.army);
                                    }
                                }
                            }
                            // --------------------------------------------------------------------------------------------------------------------------
                            int i_111_ = sectfile.sectionIndex("BornPlace" + i_102_);
                            if (i_111_ >= 0) {
                                int i_112_ = sectfile.vars(i_111_);
                                for (int i_113_ = 0; i_113_ < i_112_; i_113_++) {
                                    // Rewritten with usage of StringTokenizer
                                    /*
                                     * String string = sectfile.var(i_111_, i_113_);
                                     */

                                    String readLine = sectfile.line(i_111_, i_113_);
                                    StringTokenizer stringtokenizer = new StringTokenizer(readLine);

                                    ZutiAircraft zac = new ZutiAircraft();
                                    ZutiSupportMethods.fillZutiAircraft(zac, stringtokenizer, zutiMdsSectionIdExists);
                                    String string = zac.getAcName();
                                    if (string != null) {
                                        string = string.intern();
                                        Class var_class = (Class) Property.value(string, "airClass", null);
                                        // if (var_class != null && (Property.containsValue(var_class, "cockpitClass")))
                                        if (var_class != null) {
                                            // Add this ac to modified table for this home base
                                            if (bornplace.zutiAircraft == null) bornplace.zutiAircraft = new ArrayList();

                                            bornplace.zutiAircraft.add(zac);
                                        }
                                    }
                                }
                            }
                            // TODO: Added by |ZUTI|: once we're done loading born place, fill it's airNames array (we keep that one)
                            ZutiSupportMethods_Net.fillBornPlaceAirNames(bornplace);

                            // Load planes that are loaded when home base is captured
                            ZutiSupportMethods.zutiLoadBornPlaceCapturedPlanes(bornplace, sectfile);

                            // TODO: Added by |ZUTI|: when we are done loading planes, let's load countries for this home base. Do it here because if BP has ALL planes (none selected), above entries are not created for it!
                            ZutiSupportMethods.zutiLoadBornPlaceCountries_oldMDS(bornplace, sectfile);
                            ZutiSupportMethods.zutiLoadBornPlaceCountries_newMDS(bornplace, sectfile);

                            // Load RRR settings for this born place, if it has any
                            ZutiSupportMethods.zutiLoadBornPlaceRRR(bornplace, sectfile);

                            // Delete existing MAP spawn places if born place is of stand alone type
                            if (bornplace.zutiIsStandAloneBornPlace) {
                                zutiSpawnPlaceReductionCounter += ZutiSupportMethods_AI.removeSpawnPlacesInsideArea(bornplace.place.x, bornplace.place.y, bornplace.r);
                                // System.out.println(".................................X=" + bornplace.place.x + ", Y=" + bornplace.place.y);

                                bornplace.zutiColor = Army.color(bornplace.army);
                                ZutiSupportMethods_GUI.STD_HOME_BASE_AIRFIELDS.add(bornplace);
                            }
                            // System.out.println("number of stay places: " + World.cur().airdrome.stay.length);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                // TODO: Added by |ZUTI|: create new amount of holding places
                // ----------------------------------------------------------
                World.cur().airdrome.stayHold = new boolean[World.cur().airdrome.stayHold.length - zutiSpawnPlaceReductionCounter];
                // ----------------------------------------------------------

                // TODO: Added by |ZUTI|: load dynamic airports - carriers
                // --------------------------------------------------------
                try {
                    // Load extra spawn places
                    ZutiSupportMethods_Builder.loadSpawnPointsForSTDBornPlace(sectfile, true);
                    ZutiSupportMethods_Ships.zutiAssignShipToBornPlaces();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // TODO: Now, trim world spawn places
                for (int ii = 0; ii < World.cur().bornPlaces.size(); ii++)
                    // Count born place spawn pints so we can disable user joining them if all slots are taken
                    ZutiSupportMethods_Net.setBornPlaceStayPoints((BornPlace) World.cur().bornPlaces.get(ii));
            }
        }
    }

    private void loadTargets(SectFile sectfile) {
        // TODO: Added by |ZUTI|
        // -----------------------------
        Target.zutiResetTargetsCount();
        // -----------------------------

        // TODO: Edited by |ZUTI|
        // if (Main.cur().netServerParams.isSingle() || (Main.cur().netServerParams.isCoop() && Main.cur().netServerParams.isMaster()))
        if (Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight()) {
            int i = sectfile.sectionIndex("Target");
            if (i >= 0) {
                int i_114_ = sectfile.vars(i);
                for (int i_115_ = 0; i_115_ < i_114_; i_115_++)
                    Target.create(sectfile.line(i, i_115_));
            }
        }
    }

    private void loadTriggers(SectFile sectfile)
    {
//        Exception test = new Exception("loadTriggers");
//        test.printStackTrace();
//        if(Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight())
//        {
            int triggerSectionIndex = sectfile.sectionIndex("Trigger");
            if(triggerSectionIndex >= 0)
            {
                int triggerSectionVars = sectfile.vars(triggerSectionIndex);
                int lineNumber = 0;
                if(sectfile.line(triggerSectionIndex, lineNumber).indexOf("Version ") >= 0)
                    lineNumber = 1;
                for(; lineNumber < triggerSectionVars; lineNumber++)
                    Trigger.create(sectfile.line(triggerSectionIndex, lineNumber));

            }
            Trigger.checkLinkedTriggerActivation();
            
//        }
    }

    private void loadViewPoint(SectFile sectfile) {
        int sectionIndex = sectfile.sectionIndex("StaticCamera");
        if (sectionIndex < 0) return;
        int numVars = sectfile.vars(sectionIndex);
        for (int varIndex = 0; varIndex < numVars; varIndex++) {
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(sectionIndex, varIndex));
            float f = numbertokenizer.next(0);
            float f_118_ = numbertokenizer.next(0);
            float f_119_ = numbertokenizer.next(100, 2, 10000);
            ActorViewPoint actorviewpoint = new ActorViewPoint();
            double d = f;
            double d_120_ = f_118_;
            float f_121_ = f_119_;
//            World.land();
            Point3d point3d = new Point3d(d, d_120_, f_121_ + Landscape.HQ_Air(f, f_118_));
            Point3d point3d_122_ = point3d;
            actorviewpoint.pos.setAbs(point3d_122_);
            actorviewpoint.pos.reset();
            actorviewpoint.dreamFire(true);
            actorviewpoint.setName("StaticCamera_" + varIndex);
            if (this.net.isMirror()) {
                if (this.curActor < this.actors.size()) { // TODO: Added by Storebror, check index vs. ArrayList size first
                    actorviewpoint.createNetObject(this.net.masterChannel(), ((Integer) this.actors.get(this.curActor)).intValue());
                    this.actors.set(this.curActor++, actorviewpoint);
                }
            } else {
                actorviewpoint.createNetObject(null, 0);
                this.actors.add(actorviewpoint);
            }
        }
    }

    private void checkBridgesAndHouses(SectFile sectfile) {
        int i = sectfile.sections();
        for (int i_123_ = 0; i_123_ < i; i_123_++) {
            String string = sectfile.sectionName(i_123_);
            if (string.endsWith("_Way")) {
                int i_124_ = sectfile.vars(i_123_);
                for (int i_125_ = 0; i_125_ < i_124_; i_125_++) {
                    String string_126_ = sectfile.var(i_123_, i_125_);
                    if (string_126_.equals("GATTACK")) {
                        SharedTokenizer.set(sectfile.value(i_123_, i_125_));
                        SharedTokenizer.next((String) null);
                        SharedTokenizer.next((String) null);
                        SharedTokenizer.next((String) null);
                        SharedTokenizer.next((String) null);
                        String string_127_ = SharedTokenizer.next((String) null);
                        if (string_127_ != null && string_127_.startsWith("Bridge")) {
                            LongBridge longbridge = (LongBridge) Actor.getByName(" " + string_127_);
                            if (longbridge != null && !longbridge.isAlive()) longbridge.BeLive();
                        }
                    }
                }
            } else if (string.endsWith("_Road")) {
                int i_128_ = sectfile.vars(i_123_);
                for (int i_129_ = 0; i_129_ < i_128_; i_129_++) {
                    SharedTokenizer.set(sectfile.value(i_123_, i_129_));
                    SharedTokenizer.next((String) null);
                    int i_130_ = (int) SharedTokenizer.next(1.0);
                    if (i_130_ < 0) {
                        i_130_ = -i_130_ - 1;
                        LongBridge longbridge = LongBridge.getByIdx(i_130_);
                        if (longbridge != null && !longbridge.isAlive()) longbridge.BeLive();
                    }
                }
            }
        }
        int i_131_ = sectfile.sectionIndex("Target");
        if (i_131_ >= 0) {
            int i_132_ = sectfile.vars(i_131_);
            for (int i_133_ = 0; i_133_ < i_132_; i_133_++) {
                SharedTokenizer.set(sectfile.line(i_131_, i_133_));
                int i_134_ = SharedTokenizer.next(0, 0, 7);
                if (i_134_ == 1 || i_134_ == 2 || i_134_ == 6 || i_134_ == 7) {
                    SharedTokenizer.next(0);
                    SharedTokenizer.next(0);
                    SharedTokenizer.next(0);
                    SharedTokenizer.next(0);
                    int i_135_ = SharedTokenizer.next(0);
                    int i_136_ = SharedTokenizer.next(0);
                    int i_137_ = SharedTokenizer.next(1000, 50, 3000);
                    SharedTokenizer.next(0);
                    String string = SharedTokenizer.next((String) null);
                    if (string != null && string.startsWith("Bridge")) string = " " + string;
                    switch (i_134_) {
                        case 1:
                        case 6:
                            World.cur().statics.restoreAllHouses(i_135_, i_136_, i_137_);
                            break;
                        case 2:
                        case 7:
                            if (string != null) {
                                LongBridge longbridge = (LongBridge) Actor.getByName(string);
                                if (longbridge != null && !longbridge.isAlive()) longbridge.BeLive();
                            }
                            break;
                    }
                }
            }
        }
    }

    public static void doMissionStarting() {
        ArrayList arraylist = new ArrayList(Engine.targets());
        int i = arraylist.size();
        for (int i_139_ = 0; i_139_ < i; i_139_++) {
            Actor actor = (Actor) arraylist.get(i_139_);
            if (Actor.isValid(actor)) try {
                actor.missionStarting();
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    public void doBegin() {
        if (this.bPlaying) return;
        if (Config.isUSE_RENDER()) {
            Main3D.cur3D().setDrawLand(true);
            if (World.cur().diffCur.Clouds) {
                Main3D.cur3D().bDrawClouds = true;
                if (RenderContext.cfgSky.get() == 0) {
                    RenderContext.cfgSky.set(1);
                    RenderContext.cfgSky.apply();
                    RenderContext.cfgSky.reset();
                }
            } else Main3D.cur3D().bDrawClouds = false;
            CmdEnv.top().exec("fov 70");
            if (Main3D.cur3D().keyRecord != null) {
                Main3D.cur3D().keyRecord.clearPrevStates();
                Main3D.cur3D().keyRecord.clearRecorded();
                Main3D.cur3D().keyRecord.stopRecording(false);
                if (Main.cur().netServerParams.isSingle()) Main3D.cur3D().keyRecord.startRecording();
            }
            NetMissionTrack.countRecorded = 0;
            if (Main3D.cur3D().guiManager != null) {
                Main3D.cur3D().guiManager.setTimeGameActive(true);
                GUIPad.bStartMission = true;
                GUIMenu.initHotKeyESC();
            }
            // TODO: Comment by |ZUTI|: this should be fine
            if (!Main.cur().netServerParams.isCoop()) doMissionStarting();
            if (Main.cur().netServerParams.isDogfight()) {
                Time.setPause(false);
                RTSConf.cur.time.setEnableChangePause1(false);
            }
            CmdEnv.top().exec("music PUSH");
            CmdEnv.top().exec("music STOP");
            if (!Main3D.cur3D().isDemoPlaying()) ForceFeedback.startMission();
            Joy.adapter().rePostMoves();
            viewSet = Main3D.cur3D().viewSet_Get();
            iconTypes = Main3D.cur3D().iconTypes();
        } else {
            doMissionStarting();
            Time.setPause(false);
        }
        if (this.net.isMaster()) {
            this.sendCmd(10);
            this.doReplicateNotMissionActors(true);
        }
        if (Main.cur().netServerParams.isSingle()) {
            Main.cur().netServerParams.setExtraOcclusion(false);
            AudioDevice.soundsOn();
        }
        // TODO: Edited by |ZUTI|
        // if (Main.cur().netServerParams.isMaster() && (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isSingle()))
        if (Main.cur().netServerParams.isMaster() && (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight() || Main.cur().netServerParams.isSingle())) {
            // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
            World.cur().triggersGuard.activate();
            // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
            World.cur().targetsGuard.activate();
        }
        EventLog.type(true, "Mission: " + this.name() + " is Playing");
        EventLog.type("Mission BEGIN");
        
        // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
        HUD.clearWaitingList();
        // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

        // TODO: Added by |ZUTI|: reset server time!
        if (Main.cur().netServerParams != null) ZutiSupportMethods_Net.resetServerTime(Main.cur().netServerParams);

        this.bPlaying = true;
        if (Main.cur().netServerParams != null) Main.cur().netServerParams.USGSupdate();
        // TODO: +++ Auto NTRK recording Mod by SAS~Storebror +++
        if (NetMissionTrack.isPlaying()) return;
        if (Config.cur.bAutoNtrkRecording) {
            Calendar calendar = Calendar.getInstance();
            String datetime = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-"
                    + calendar.get(Calendar.SECOND);
            String autoNtrkFolder = Config.cur.ini.get("Mods", "AutoNtrkFolder", "records");
            String filename = autoNtrkFolder + "/" + sanitizeFilename(datetime + "_" + this.name() + ".ntrk");
            float timeparam = Config.cur.netSpeed / 1000F + 5F;
            new File(HomePath.toFileSystemName(autoNtrkFolder, 0)).mkdirs();
            NetMissionTrack.startRecording(filename, timeparam);
        }
        // TODO: --- Auto NTRK recording Mod by SAS~Storebror ---
    }

    // TODO: +++ Auto NTRK recording Mod by SAS~Storebror +++
    public static String sanitizeFilename(String name) {
        String retVal = name;
        retVal = retVal.replace(':', '_');
        retVal = retVal.replace('\\', '_');
        retVal = retVal.replace('/', '_');
        retVal = retVal.replace('*', '_');
        retVal = retVal.replace('?', '_');
        retVal = retVal.replace('|', '_');
        retVal = retVal.replace('<', '_');
        retVal = retVal.replace('>', '_');
        return retVal;
    }
    // TODO: --- Auto NTRK recording Mod by SAS~Storebror ---

    public void doEnd() {
        // TODO: Added by |ZUTI|
        try {
            if (this.bPlaying) {
                EventLog.type("Mission END");
                
                // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
                HUD.clearWaitingList();
                // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

                // TODO: +++ Auto NTRK recording Mod by SAS~Storebror +++
                NetMissionTrack.stopRecording();
                // TODO: --- Auto NTRK recording Mod by SAS~Storebror ---
                if (Config.isUSE_RENDER()) {
                    ForceFeedback.stopMission();
                    // TODO: +++ Close Ingame Menu when Mission ends! +++
                    if(GUIMenu.isVisible()) GUIMenu.doCloseMenu();
                    // TODO: --- Close Ingame Menu when Mission ends! ---
                    // TODO: +++ Close Ingame Map/Pad when Mission ends! +++
//                    int i = Main.state().id();
//                    boolean bool = i == 5 || i == 29 || i == 63 || i == 49 || i == 50 || i == 42 || i == 43;
//                    if(GUI.pad.isActive()) {
//                        System.out.println("GUI.pad.leave(" + (!bool) + ") (Main.state().id() = " + i + ")");
//             //           GUI.pad.leave(true);
//                    }
                    GUIMenu.setHotKeyESC(false);
                    // TODO: --- Close Ingame Map/Pad when Mission ends! ---
                    if (Main3D.cur3D().guiManager != null) Main3D.cur3D().guiManager.setTimeGameActive(false);
                    // TODO: +++ Auto NTRK recording Mod by SAS~Storebror +++
// NetMissionTrack.stopRecording();
// TODO: --- Auto NTRK recording Mod by SAS~Storebror ---
                    if (Main3D.cur3D().keyRecord != null) {
                        if (Main3D.cur3D().keyRecord.isPlaying()) {
                            Main3D.cur3D().keyRecord.stopPlay();
                            Main3D.cur3D().guiManager.setKeyboardGameActive(true);
                            Main3D.cur3D().guiManager.setMouseGameActive(true);
                            Main3D.cur3D().guiManager.setJoyGameActive(true);
                        }
                        Main3D.cur3D().keyRecord.stopRecording(true);
                    }
                    CmdEnv.top().exec("music POP");
                    CmdEnv.top().exec("music PLAY");
                }
                RTSConf.cur.time.setEnableChangePause1(true);
                Time.setPause(true);
                if (this.net.isMaster()) this.sendCmd(20);
                AudioDevice.soundsOff();
                Voice.endSession();
                this.bPlaying = false;
                if (Main.cur().netServerParams != null) Main.cur().netServerParams.USGSupdate();
            }
        } catch (Exception ex) {
            System.out.println("Mission error, ID_16: " + ex.toString());
        }
    }

    public NetObj netObj() {
        return this.net;
    }

    private void sendCmd(int i) {
        if (this.net.isMirrored()) try {
            List list = NetEnv.channels();
            int i_140_ = list.size();
            for (int i_141_ = 0; i_141_ < i_140_; i_141_++) {
                NetChannel netchannel = (NetChannel) list.get(i_141_);
                if (netchannel != this.net.masterChannel() && netchannel.isReady() && netchannel.isMirrored(this.net) && (netchannel.userState == 4 || netchannel.userState == 0)) {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(i);
                    this.net.postTo(netchannel, netmsgguaranted);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void doReplicateNotMissionActors(boolean bool) {
        if (this.net.isMirrored()) {
            List list = NetEnv.channels();
            int i = list.size();
            for (int i_142_ = 0; i_142_ < i; i_142_++) {
                NetChannel netchannel = (NetChannel) list.get(i_142_);
                if (netchannel != this.net.masterChannel() && netchannel.isReady() && netchannel.isMirrored(this.net)) if (bool) {
                    if (netchannel.userState == 4) this.doReplicateNotMissionActors(netchannel, true);
                } else netchannel.userState = 1;
            }
        }
    }

    private void doReplicateNotMissionActors(NetChannel netchannel, boolean bool) {
        if (bool) {
            netchannel.userState = 0;
            HashMapInt hashmapint = NetEnv.cur().objects;
            for (HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint.nextEntry(hashmapintentry)) {
                NetObj netobj = (NetObj) hashmapintentry.getValue();
                if (netobj instanceof ActorNet && !netchannel.isMirrored(netobj)) {
                    ActorNet actornet = (ActorNet) netobj;
                    if (Actor.isValid(actornet.actor()) && !actornet.actor().isSpawnFromMission()) MsgNet.postRealNewChannel(netobj, netchannel);
                }
            }
        } else netchannel.userState = 1;
    }

    private void doResvMission(NetMsgInput netmsginput) {
        try {
            while (netmsginput.available() > 0) {
                int i = netmsginput.readInt();
                if (i < 0) {
                    String string = netmsginput.read255();
                    this.sectFile.sectionAdd(string);
                } else {
//                    this.sectFile.lineAdd(i, netmsginput.read255(), netmsginput.read255());
                    String var = netmsginput.read255();
                    
//                    String value = "";
//                    netmsginput.mark(2);
//                    byte test = netmsginput.readByte();
//                    if (test == 0) {
//                        int len = netmsginput.readByte();
//                        byte[] compressed = new byte[len];
//                        netmsginput.read(compressed, 0, len);
//                        value = new String(NetAircraft.decompress(compressed));
//                    } else {
//                        netmsginput.reset();
//                        value = netmsginput.read255();
//                    }
                    
                    String value = netmsginput.read255();
                    if (value.startsWith("!")) {
                        if (joinedValue == null) joinedValue = new StringBuffer();
                        joinedValue.append(value.substring(1));
                        if (value.endsWith("!")) {
                            joinedValue.deleteCharAt(joinedValue.length() - 1);
                            this.sectFile.lineAdd(i, var, joinedValue.toString());
//                            System.out.println("Mission full sectfile line = " + joinedValue.toString());
                            joinedValue.delete(0, joinedValue.length());
                            joinedValue = null;
                        }// else {
//                            System.out.println("Mission sectfile line part = " + value.substring(1));
//                            System.out.println("Mission sectfile line parts so far = " + joinedValue.toString());
//                        }
                    } else {
                        this.sectFile.lineAdd(i, var, value);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Bad format of received mission file");
        }
    }
    
    public static ArrayList splitEqually(String text, int size) {
        ArrayList ret = new ArrayList((text.length() + size - 1) / size);
        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }
    
    private static StringBuffer joinedValue = null;

    private void doSendMission(NetChannel netchannel, int i) {
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(i);
            int numSections = this.sectFile.sections();
            for (int sectionIndex = 0; sectionIndex < numSections; sectionIndex++) {
                String sectionName = this.sectFile.sectionName(sectionIndex);
                if (!sectionName.startsWith("$$$")) {
                    // TODO: +++ Fix exceeding netmessage length issue by SAS~Storebror +++
                    //if (netmsgguaranted.size() >= 128) {
                    if ((netmsgguaranted.size() + sectionName.length()) >= 240) {
                    // TODO: --- Fix exceeding netmessage length issue by SAS~Storebror ---
                        this.net.postTo(netchannel, netmsgguaranted);
                        netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(i);
                    }
                    netmsgguaranted.writeInt(-1);
                    netmsgguaranted.write255(sectionName);
                    int numVars = this.sectFile.vars(sectionIndex);
                    for (int varsIndex = 0; varsIndex < numVars; varsIndex++) {
                        // TODO: +++ Fix exceeding netmessage length issue by SAS~Storebror +++
//                        if (netmsgguaranted.size() >= 128) {
                        String outString = this.sectFile.value(sectionIndex, varsIndex);

                        if (outString.length() > 200) {
//                            System.out.println("Mission splitting sectfile line: " + outString);
                            ArrayList splitOutString = splitEqually(outString, 100);
                            for (int splitOutStringIndex = 0; splitOutStringIndex < splitOutString.size(); splitOutStringIndex++) {
                                this.net.postTo(netchannel, netmsgguaranted);
                                netmsgguaranted = new NetMsgGuaranted();
                                netmsgguaranted.writeByte(i);
                                netmsgguaranted.writeInt(sectionIndex); 
                                netmsgguaranted.write255(this.sectFile.var(sectionIndex, varsIndex));
                                StringBuffer sb = new StringBuffer("!").append((String)splitOutString.get(splitOutStringIndex));
                                if (splitOutStringIndex == splitOutString.size() - 1) sb.append("!");
                                netmsgguaranted.write255(sb.toString());
//                                System.out.println("Mission sectfile line part: " + sb.toString());
                            }
                            continue;
                        }
                        
                        if ((netmsgguaranted.size() + this.sectFile.var(sectionIndex, varsIndex).length() + outString.length()) >= 240 || outString.length() > 100) {
                        // TODO: --- Fix exceeding netmessage length issue by SAS~Storebror ---
                            this.net.postTo(netchannel, netmsgguaranted);
                            netmsgguaranted = new NetMsgGuaranted();
                            netmsgguaranted.writeByte(i);
                        }
                        netmsgguaranted.writeInt(sectionIndex);
                        netmsgguaranted.write255(this.sectFile.var(sectionIndex, varsIndex));
                        
//                        //TODO: Test!!!
//                        String tempValue = this.sectFile.value(sectionIndex, varsIndex);
//                        if (tempValue.indexOf("%GRID%") > -1 || tempValue.indexOf("%ALTM%") > -1 || tempValue.indexOf("%ALTF%") > -1) {
//                            System.out.println("tempValue = " + tempValue);
//                            tempValue = replaceAll(tempValue, "%GRID%", "%% GRID %%");
//                            tempValue = replaceAll(tempValue, "%ALTM%", "%% ALTM %%");
//                            tempValue = replaceAll(tempValue, "%ALTF%", "%% ALTF %%");
//                            System.out.println("tempValue new = " + tempValue);
//                        }
//                        netmsgguaranted.write255(tempValue);
                        
                        netmsgguaranted.write255(this.sectFile.value(sectionIndex, varsIndex));
//                        if (outString.length() > 100) {
//                            byte[] compressed = NetAircraft.compress(outString.getBytes());
//                            netmsgguaranted.writeByte(0);
//                            netmsgguaranted.writeByte(compressed.length);
//                            netmsgguaranted.write(compressed);
//                            System.out.println("Compressing String of " + outString.length() + " bytes to " + compressed.length + "bytes: " + outString);
//                        } else {
//                            netmsgguaranted.write255(outString);
//                        }
                    }
                }
            }
            if (netmsgguaranted.size() > 1) this.net.postTo(netchannel, netmsgguaranted);
            netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(i + 1);
            this.net.postTo(netchannel, netmsgguaranted);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void replicateTimeofDay() {
        if (isServer()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(11);
            netmsgguaranted.writeFloat(World.getTimeofDay());
            this.net.post(netmsgguaranted);
        } catch (Exception exception) {
            /* empty */
        }
    }

    private boolean isExistFile(String string) {
        boolean bool = false;
        try {
            SFSInputStream sfsinputstream = new SFSInputStream(string);
            sfsinputstream.close();
            bool = true;
        } catch (Exception exception) {
            /* empty */
        }
        return bool;
    }

    private void netInput(NetMsgInput netmsginput) throws IOException {
        boolean bool = false;
        if (this.net instanceof Master || netmsginput.channel() != this.net.masterChannel()) bool = true;
        boolean bool_147_ = netmsginput.channel() instanceof NetChannelStream;
        NetMsgGuaranted netmsgguaranted = null;
        int i = netmsginput.readUnsignedByte();
        switch (i) {
            case 0:
                netmsginput.channel().userState = 2;
                netmsgguaranted = new NetMsgGuaranted();
                if (bool) {
                    if (bool_147_) {
                        NetMsgGuaranted netmsgguaranted_148_ = new NetMsgGuaranted();
                        netmsgguaranted_148_.writeByte(13);
                        netmsgguaranted_148_.writeLong(Time.current());
                        this.net.postTo(netmsginput.channel(), netmsgguaranted_148_);
                    }
                    netmsgguaranted.writeByte(0);
                    netmsgguaranted.write255(this.name);
                    netmsgguaranted.writeLong(this.sectFinger);
                } else {
                    this.name = netmsginput.read255();
                    this.sectFinger = netmsginput.readLong();
                    Main.cur().netMissionListener.netMissionState(0, 0.0F, this.name);
                    if (!bool_147_) ((NetUser) NetEnv.host()).setMissProp("missions/" + this.name);
                    String string = "missions/" + this.name;
                    if (!bool_147_ && this.isExistFile(string)) {
                        this.sectFile = new SectFile(string, 0, false);
                        if (this.sectFinger == this.sectFile.fingerExcludeSectPrefix("$$$")) {
                            netmsgguaranted.writeByte(3);
                            break;
                        }
                    }
                    string = "missions/Net/Cache/" + this.sectFinger + ".mis";
                    int[] is = this.getSwTbl(string, this.sectFinger);
                    this.sectFile = new SectFile(string, 0, false, is);
                    if (!bool_147_ && this.sectFinger == this.sectFile.fingerExcludeSectPrefix("$$$")) netmsgguaranted.writeByte(3);
                    else {
                        this.sectFile = new SectFile(string, 1, false, is);
                        this.sectFile.clear();
                        netmsgguaranted.writeByte(1);
                    }
                }
                break;
            case 13:
                if (!bool) {
                    long l = netmsginput.readLong();
                    RTSConf.cur.time.setCurrent(l);
                    NetMissionTrack.playingStartTime = l;
                }
                break;
            case 1:
                if (bool) this.doSendMission(netmsginput.channel(), 1);
                else {
                    Main.cur().netMissionListener.netMissionState(1, 0.0F, null);
                    this.doResvMission(netmsginput);
                }
                break;
            case 2:
                if (!bool) {
                    this.sectFile.saveFile();
                    netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(3);
                }
                break;
            case 3:
                if (bool) {
                    int i_149_ = this.actors.size();
                    int i_150_ = 0;
                    while (i_150_ < i_149_) {
                        netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(3);
                        int i_151_ = 64;
                        while (i_151_-- > 0 && i_150_ < i_149_) {
                            Actor actor = (Actor) this.actors.get(i_150_++);
                            if (Actor.isValid(actor)) netmsgguaranted.writeShort(actor.net.idLocal());
                            else netmsgguaranted.writeShort(0);
                        }
                        this.net.postTo(netmsginput.channel(), netmsgguaranted);
                    }
                    netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(4);
                    netmsginput.channel().userState = 3;
                } else {
                    Main.cur().netMissionListener.netMissionState(2, 0.0F, null);
                    while (netmsginput.available() > 0)
                        this.actors.add(new Integer(netmsginput.readUnsignedShort()));
                }
                break;
            case 4:
                if (bool) {
                    // TODO: Comment by |ZUTI| - this is fine
                    if (isDogfight() || netmsginput.channel() instanceof NetChannelOutStream) {
                        World.cur().statics.netBridgeSync(netmsginput.channel());
                        World.cur().statics.netHouseSync(netmsginput.channel());
                    }
                    for (int i_152_ = 0; i_152_ < this.actors.size(); i_152_++) {
                        Actor actor = (Actor) this.actors.get(i_152_);
                        if (Actor.isValid(actor)) try {
                            NetChannel netchannel = netmsginput.channel();
                            netchannel.setMirrored(actor.net);
                            actor.netFirstUpdate(netmsginput.channel());
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    if (Actor.isValid(World.cur().houseManager)) World.cur().houseManager.fullUpdateChannel(netmsginput.channel());
                    netmsgguaranted = new NetMsgGuaranted();
                    if (isPlaying()) {
                        netmsgguaranted.writeByte(10);
                        this.net.postTo(netmsginput.channel(), netmsgguaranted);
                        netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(11);
                        netmsgguaranted.writeFloat(World.getTimeofDay());
                        this.net.postTo(netmsginput.channel(), netmsgguaranted);
                        netmsgguaranted = null;
                        this.doReplicateNotMissionActors(netmsginput.channel(), true);
                        this.trySendMsgStart(netmsginput.channel());
                    } else {
                        netmsgguaranted.writeByte(5);
                        netmsginput.channel().userState = 4;
                    }
                } else {
                    netmsginput.channel().userState = 3;
                    try {
                        this.load(this.name, this.sectFile, true);
                    } catch (Exception exception) {
                        printDebug(exception);
                        Main.cur().netMissionListener.netMissionState(4, 0.0F, exception.getMessage());
                    }
                }
                break;
            case 5:
                break;
            case 10:
                if (!(this.net instanceof Master) && netmsginput.channel() == this.net.masterChannel()) {
                    if (this.net.isMirrored()) {
                        netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(10);
                        this.net.post(netmsgguaranted);
                        netmsgguaranted = null;
                    }
                    this.doReplicateNotMissionActors(true);
                    this.doReplicateNotMissionActors(netmsginput.channel(), true);
                    this.doBegin();
                    Main.cur().netMissionListener.netMissionState(6, 0.0F, null);
                }
                break;
            case 11:
                // TODO: By SAS~Storebror, let Time of Day Changes appear in Net Playback as well!
                // if (!(this.net instanceof Master) && netmsginput.channel() == this.net.masterChannel()) {
                if (NetMissionTrack.isPlaying() || !(this.net instanceof Master) && netmsginput.channel() == this.net.masterChannel()) {
                    float f = netmsginput.readFloat();
                    World.setTimeofDay(f);
                    World.land().cubeFullUpdate();
                }
                break;
            case 20:
                if (!(this.net instanceof Master) && netmsginput.channel() == this.net.masterChannel()) {
                    Main.cur().netMissionListener.netMissionState(7, 0.0F, null);
                    this.doReplicateNotMissionActors(false);
                    this.doReplicateNotMissionActors(netmsginput.channel(), false);
                    this.doEnd();
                    if (this.net.isMirrored()) {
                        netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(20);
                        this.net.post(netmsgguaranted);
                        netmsgguaranted = null;
                    }
                }
                break;
            case 12:
                if (!(this.net instanceof Master) && netmsginput.channel() == this.net.masterChannel()) Main.cur().netMissionListener.netMissionState(9, 0.0F, null);
                break;
        }
        if (netmsgguaranted != null && netmsgguaranted.size() > 0) this.net.postTo(netmsginput.channel(), netmsgguaranted);
    }

    public void trySendMsgStart(Object object) {
        if (!this.isDestroyed()) {
            NetChannel netchannel = (NetChannel) object;
            if (!netchannel.isDestroyed()) {
                HashMapInt hashmapint = RTSConf.cur.netEnv.objects;
                HashMapIntEntry hashmapintentry = null;
                while ((hashmapintentry = hashmapint.nextEntry(hashmapintentry)) != null) {
                    NetObj netobj = (NetObj) hashmapintentry.getValue();
                    if (netobj != null && !netobj.isDestroyed() && !netobj.isCommon() && !netchannel.isMirrored(netobj) && netobj.masterChannel() != netchannel
                            && (!(netchannel instanceof NetChannelOutStream) || !(netobj instanceof NetControl) && (!(netobj instanceof NetUser) || !netobj.isMaster() || !NetMissionTrack.isPlaying()))
                            && (!(netobj instanceof GameTrack) || !netobj.isMirror())) {
                        Object object_153_ = netobj.superObj();
                        if (!(object_153_ instanceof Destroy) || !((Destroy) object_153_).isDestroyed()) {
                            new MsgInvokeMethod_Object("trySendMsgStart", netchannel).post(72, this, 0.0);
                            return;
                        }
                    }
                }
                try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(12);
                    this.net.postTo(netchannel, netmsgguaranted);
                } catch (Exception exception) {
                    printDebug(exception);
                }
            }
        }
    }

    private void createNetObject(NetChannel netchannel, int i) {
        this.setTime(true);
        if (netchannel == null) {
            this.net = new Master(this);
            this.doReplicateNotMissionActors(false);
        } else {
            this.net = new Mirror(this, netchannel, i);
            this.doReplicateNotMissionActors(netchannel, false);
        }
    }

    protected static void printDebug(Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    private int[] getSwTbl(String string, long l) {
        int i = (int) l;
        int i_154_ = Finger.Int(string);
        if (i < 0) i = -i;
        if (i_154_ < 0) i_154_ = -i_154_;
        int i_155_ = (i_154_ + i / 7) % 16 + 15;
        int i_156_ = (i_154_ + i / 21) % Finger.kTable.length;
        if (i_155_ < 0) i_155_ = -i_155_ % 16;
        if (i_155_ < 10) i_155_ = 10;
        if (i_156_ < 0) i_156_ = -i_156_ % Finger.kTable.length;
        int[] is = new int[i_155_];
        for (int i_157_ = 0; i_157_ < i_155_; i_157_++)
            is[i_157_] = Finger.kTable[(i_156_ + i_157_) % Finger.kTable.length];
        return is;
    }

    static {
        Spawn.add(Mission.class, new SPAWN());
    }

    public static int curYear() {
        if (Main.cur().mission == null) return 0;
        else return Main.cur().mission.curYear;
    }

    public static int curMonth() {
        if (Main.cur().mission == null) return 0;
        else return Main.cur().mission.curMonth;
    }

    public static int curDay() {
        if (Main.cur().mission == null) return 0;
        else return Main.cur().mission.curDay;
    }

    public static float curWindDirection() {
        if (Main.cur().mission == null) return 0.0F;
        else return Main.cur().mission.curWindDirection;
    }

    public static float curWindVelocity() {
        if (Main.cur().mission == null) return 0.0F;
        else return Main.cur().mission.curWindVelocity;
    }

    public static float curGust() {
        if (Main.cur().mission == null) return 0.0F;
        else return Main.cur().mission.curGust;
    }

    public static float curTurbulence() {
        if (Main.cur().mission == null) return 0.0F;
        else return Main.cur().mission.curTurbulence;
    }

    private String generateHayrakeCode(Point3d point3d) {
        double d = point3d.x;
        double d_190_ = point3d.y;
        long l = (long) (d + d_190_);
        Random random = new Random(l);
        byte[] is = new byte[12];
        for (int i = 0; i < is.length; i++) {
            boolean bool = false;
            while (!bool) {
                byte i_191_ = (byte) (random.nextInt(26) + 65);
                if (i_191_ != 74 && i_191_ != 81 && i_191_ != 89 && i_191_ <= 90) for (int i_192_ = 0; i_192_ < is.length && i_191_ != is[i_192_]; i_192_++)
                    if (i_192_ == is.length - 1) {
                        bool = true;
                        is[i] = i_191_;
                    }
            }
        }
        String string = new String(is);
        return string;
    }

    private void populateRunwayLights() {
        ArrayList arraylist = new ArrayList();
        World.getAirports(arraylist);
        for (int i = 0; i < arraylist.size(); i++)
            if (arraylist.get(i) instanceof AirportGround) for (int i_193_ = 0; i_193_ < this.actors.size(); i_193_++)
                if (this.actors.get(i_193_) instanceof SmokeGeneric
                        && (this.actors.get(i_193_) instanceof Smoke.Smoke15 || this.actors.get(i_193_) instanceof Smoke.Smoke14 || this.actors.get(i_193_) instanceof Smoke.Smoke13 || this.actors.get(i_193_) instanceof Smoke.Smoke12)) {
                            AirportGround airportground = (AirportGround) arraylist.get(i);
                            Actor actor = (Actor) this.actors.get(i_193_);
                            double d = airportground.pos.getAbsPoint().x - actor.pos.getAbsPoint().x;
                            double d_194_ = airportground.pos.getAbsPoint().y - actor.pos.getAbsPoint().y;
                            if (Math.abs(d) < 2000.0 && Math.abs(d_194_) < 2000.0 && (actor.getArmy() == 1 || actor.getArmy() == 2)) {
                                SmokeGeneric smokegeneric = (SmokeGeneric) actor;
                                smokegeneric.setVisible(false);
                                airportground.addLights(smokegeneric);
                            }
                        }
        for (int i = 0; i < this.actors.size(); i++)
            if (this.actors.get(i) instanceof SmokeGeneric) ((SmokeGeneric) this.actors.get(i)).setArmy(0);
    }

    private void populateBeacons() {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist_195_ = new ArrayList();
        for (int i = 0; i < this.actors.size(); i++)
            if (this.actors.get(i) instanceof TypeHasBeacon) {
                Point3d point3d = ((Actor) this.actors.get(i)).pos.getAbsPoint();
                arraylist.add(new Object[] { this.actors.get(i), point3d });
                if (this.actors.get(i) instanceof TypeHasLorenzBlindLanding) ((Actor) this.actors.get(i)).missionStarting();
                if (this.actors.get(i) instanceof BigshipGeneric) hayrakeMap.put(this.actors.get(i), "NDB");
            } else if (this.actors.get(i) instanceof TypeHasMeacon) {
                Point3d point3d = ((Actor) this.actors.get(i)).pos.getAbsPoint();
                arraylist_195_.add(new Object[] { this.actors.get(i), point3d });
            } else if (this.actors.get(i) instanceof TypeHasHayRake) {
                Point3d point3d = ((Actor) this.actors.get(i)).pos.getAbsPoint();
                String string = this.generateHayrakeCode(point3d);
                arraylist.add(new Object[] { this.actors.get(i), point3d });
                hayrakeMap.put(this.actors.get(i), string);
            }
        if (arraylist.size() != 0) {
            this.sortBeaconsList(arraylist);
            for (int i = 0; i < arraylist.size(); i++) {
                Object[] objects = (Object[]) arraylist.get(i);
                Actor actor = (Actor) objects[0];
                if ((actor instanceof TypeHasRadioStation || actor.getArmy() == 1) && beaconsRed.size() < 32) beaconsRed.add(objects[0]);
                if ((actor instanceof TypeHasRadioStation || actor.getArmy() == 2) && beaconsBlue.size() < 32) beaconsBlue.add(objects[0]);
            }
            for (int i = 0; i < arraylist_195_.size(); i++) {
                Object[] objects = (Object[]) arraylist_195_.get(i);
                Actor actor = (Actor) objects[0];
                if (actor.getArmy() == 1 && meaconsRed.size() < 32) meaconsRed.add(objects[0]);
                else if (actor.getArmy() == 2 && meaconsBlue.size() < 32) meaconsBlue.add(objects[0]);
            }
            arraylist.clear();
            arraylist_195_.clear();
        }
    }

    public static void addHayrakesToOrdersTree() {
        for (int i = 0; i < 10; i++)
            Main3D.cur3D().ordersTree.addShipIDs(i, -1, null, "", "");
        if (World.cur().diffCur.RealisticNavigationInstruments) {
            int i = 0;
            Iterator iterator = hayrakeMap.entrySet().iterator();
            while (iterator.hasNext() && i < 10) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Actor actor = (Actor) entry.getKey();
                if (actor.getArmy() == World.getPlayerArmy()) {
                    String string = (String) entry.getValue();
                    String string_197_ = Property.stringValue(actor.getClass(), "i18nName", "");

                    if (string_197_.equals("")) try {
                        String s2 = actor.getClass().toString().substring(actor.getClass().toString().indexOf("$") + 1);
                        string_197_ = I18N.technic(s2);
                    } catch (Exception exception) {}

                    int i_198_ = -1;
                    if (beaconsRed.contains(actor)) i_198_ = beaconsRed.indexOf(actor);
                    else if (beaconsBlue.contains(actor)) i_198_ = beaconsBlue.indexOf(actor);
                    if (string.equals("NDB")) Main3D.cur3D().ordersTree.addShipIDs(i, i_198_, actor, string_197_, "");
                    else {
                        boolean bool = Aircraft.hasPlaneZBReceiver(World.getPlayerAircraft());
                        if (!bool) continue;
                        String string_199_ = string;
                        if (string.length() == 12) string_199_ = string.substring(0, 3) + " / " + string.substring(3, 6) + " / " + string.substring(6, 9) + " / " + string.substring(9, 12);
                        else if (string.length() == 24) string_199_ = string.substring(0, 2) + "-" + string.substring(2, 4) + "-" + string.substring(4, 6) + " / " + string.substring(6, 8) + "-" + string.substring(8, 10) + "-" + string.substring(10, 12)
                                + " / " + string.substring(12, 14) + "-" + string.substring(14, 16) + "-" + string.substring(16, 18) + " / " + string.substring(18, 20) + "-" + string.substring(20, 22) + "-" + string.substring(22, 24);
                        Main3D.cur3D().ordersTree.addShipIDs(i, i_198_, actor, string_197_, "( " + string_199_ + " )");
                    }
                    i++;
                }
            }
        }
    }

    private void sortBeaconsList(List list) {
        boolean bool = false;
        do
            for (int i = 0; i < list.size() - 1; i++) {
                bool = false;
                Object[] objects = (Object[]) list.get(i);
                Object[] objects_200_ = (Object[]) list.get(i + 1);
                if (objects[0] instanceof TypeHasHayRake && !(objects_200_[0] instanceof TypeHasHayRake) || objects[0] instanceof BigshipGeneric && !(objects_200_[0] instanceof BigshipGeneric)) {
                    Object[] objects_201_ = objects;
                    list.set(i, objects_200_);
                    list.set(i + 1, objects_201_);
                    bool = true;
                }
            }
        while (bool);
    }

    public boolean hasBeacons(int i) {
        if (i == 1) {
            if (beaconsRed.size() > 0) return true;
            return false;
        }
        if (i == 2) {
            if (beaconsBlue.size() > 0) return true;
            return false;
        }
        return false;
    }

    public ArrayList getBeacons(int i) {
        if (i == 1) return beaconsRed;
        if (i == 2) return beaconsBlue;
        return null;
    }

    public ArrayList getMeacons(int i) {
        if (i == 1) return meaconsBlue;
        if (i == 2) return meaconsRed;
        return null;
    }

    public String getHayrakeCodeOfCarrier(Actor actor) {
        if (hayrakeMap.containsKey(actor)) return (String) hayrakeMap.get(actor);
        return null;
    }

    public Mission() {
        this.name = null;
        this.sectFinger = 0L;
        this.actors = new ArrayList();
        this.curActor = 0;
        this.bPlaying = false;
        this.curCloudsType = 0;
        this.curCloudsHeight = 1000F;
        this.curYear = 0;
        this.curMonth = 0;
        this.curDay = 0;
        this.curWindDirection = 0.0F;
        this.curWindVelocity = 0.0F;
        this.curGust = 0.0F;
        this.curTurbulence = 0.0F;
        this.bigShipHpDiv = 1.0F;
        this._loadPlayer = false;
        this.playerNum = 0;
    }

    public static int getMissionDate(boolean flag) {
        int i = 0;
        if (Main.cur().mission == null) {
            SectFile sectfile = Main.cur().currentMissionFile;
            if (sectfile == null) return 0;
            String s = sectfile.get("MAIN", "MAP");
            int l = World.land().config.getDefaultMonth("maps/" + s);
            int j1 = sectfile.get("SEASON", "Year", 1940, 1930, 1960);
            int k1 = sectfile.get("SEASON", "Month", l, 1, 12);
            int l1 = sectfile.get("SEASON", "Day", 15, 1, 31);
            i = j1 * 10000 + k1 * 100 + l1;
            int j2 = 0x1280540 + l * 100 + 15;
            if (flag && i == j2) i = 0;
        } else {
            int j = curYear();
            int k = curMonth();
            int i1 = curDay();
            i = j * 10000 + k * 100 + i1;
            if (flag) {
                SectFile sectfile1 = Main.cur().currentMissionFile;
                if (sectfile1 == null) return 0;
                String s1 = sectfile1.get("MAIN", "MAP");
                int i2 = World.land().config.getDefaultMonth("maps/" + s1);
                int k2 = 0x1280540 + i2 * 100 + 15;
                if (i == k2) i = 0;
            }
        }
        return i;
    }

    public static float BigShipHpDiv() {
        if (Main.cur().mission == null) return 1.0F;
        else return Main.cur().mission.bigShipHpDiv;
    }

    // TODO: Variables and Method created by |ZUTI|
    // -------------------------------------------------------------------------
    private static ZutiMDSVariables ZUTI_MDS_VARIABLES = null;

    public static ZutiMDSVariables MDS_VARIABLES() {
        if (ZUTI_MDS_VARIABLES == null) ZUTI_MDS_VARIABLES = new ZutiMDSVariables();

        return ZUTI_MDS_VARIABLES;
    }
    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // TODO: +++ New automatic Garbage Collection at mission end by SAS~Storebror +++
    public static void doGarbageCollection() {
        System.out.println("Before GC, Memory: total(" + Runtime.getRuntime().totalMemory() + ") free(" + Runtime.getRuntime().freeMemory() + ")");
        try {
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" After GC, Memory: total(" + Runtime.getRuntime().totalMemory() + ") free(" + Runtime.getRuntime().freeMemory() + ")");
    }
    // TODO: --- New automatic Garbage Collection at mission end by SAS~Storebror ---
}
