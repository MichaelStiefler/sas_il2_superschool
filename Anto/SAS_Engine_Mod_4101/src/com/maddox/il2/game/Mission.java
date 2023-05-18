/* Mission - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.maddox.il2.game;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AirportGround;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.Target;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.ai.ground.ChiefGround;
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
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUIAirArming;
import com.maddox.il2.gui.GUIPad;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetFilesTrack;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.buildings.HouseManager;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.il2.objects.effects.Zip;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.ships.BigshipGeneric;
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
import com.maddox.rts.LDRres;
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
import com.maddox.sound.AudioDevice;
import com.maddox.sound.CmdMusic;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.SharedTokenizer;

public class Mission implements Destroy
{
    public static final String DIR = "missions/";
    public static final String DIRNET = "missions/Net/Cache/";
    public static final float CLOUD_HEIGHT = 8000.0F;
    private String name = null;
    private SectFile sectFile;
    private long sectFinger = 0L;
    ArrayList actors = new ArrayList();
    private int curActor = 0;
    private boolean bPlaying = false;
    private int curCloudsType = 0;
    private float curCloudsHeight = 1000.0F;
    protected static int viewSet = 0;
    protected static int iconTypes = 0;
    private static HashMap respawnMap = new HashMap();
    private int curYear = 0;
    private int curMonth = 0;
    private int curDay = 0;
    private float curWindDirection = 0.0F;
    private float curWindVelocity = 0.0F;
    private float curGust = 0.0F;
    private float curTurbulence = 0.0F;
    private static ArrayList beaconsRed = new ArrayList();
    private static ArrayList beaconsBlue = new ArrayList();
    private static ArrayList meaconsRed = new ArrayList();
    private static ArrayList meaconsBlue = new ArrayList();
    private static Map hayrakeMap = new HashMap();
    private final int HAYRAKE_CODE_LENGTH = 12;
    public static boolean hasRadioStations = false;
    private static boolean radioStationsLoaded = false;
    private float bigShipHpDiv = 1.0F;
    private String player;
    private boolean _loadPlayer = false;
    private int playerNum = 0;
    private HashMap mapWingTakeoff;
    private static SectFile chiefsIni;
    private static Point3d Loc = new Point3d();
    private static Orient Or = new Orient();
    private static Vector3f Spd = new Vector3f();
    private static Vector3d Spdd = new Vector3d();
    private static ActorSpawnArg spawnArg = new ActorSpawnArg();
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    public static final int NET_MSG_ID_NAME = 0;
    public static final int NET_MSG_ID_BODY = 1;
    public static final int NET_MSG_ID_BODY_END = 2;
    public static final int NET_MSG_ID_ACTORS = 3;
    public static final int NET_MSG_ID_ACTORS_END = 4;
    public static final int NET_MSG_ID_LOADED = 5;
    public static final int NET_MSG_ID_BEGIN = 10;
    public static final int NET_MSG_ID_TOD = 11;
    public static final int NET_MSG_ID_START = 12;
    public static final int NET_MSG_ID_TIME = 13;
    public static final int NET_MSG_ID_END = 20;
    protected NetObj net;
    public int zutiCarrierSpawnPoints_CV2;
    public int zutiCarrierSpawnPoints_CV9;
    public int zutiCarrierSpawnPoints_CVE;
    public int zutiCarrierSpawnPoints_CVL;
    public int zutiCarrierSpawnPoints_Akagi;
    public int zutiCarrierSpawnPoints_IJN;
    public int zutiCarrierSpawnPoints_HMS;
    public final boolean zutiTargets_MovingIcons = true;
    public final boolean zutiTargets_ShowTargets = true;
    public final boolean zutiIcons_ShowNeutralHB = true;
    public final boolean zutiRadar_ShowAircraft = true;
    public final boolean zutiRadar_ShowGroundUnits = true;
    public final boolean zutiRadar_StaticIconsIfNoRadar = true;
    public boolean zutiRadar_PlayerSideHasRadars;
    public int zutiRadar_RefreshInterval;
    public final boolean zutiRadar_AircraftIconsWhite = false;
    public boolean zutiRadar_HideUnpopulatedAirstripsFromMinimap;
    public boolean zutiRadar_EnableTowerCommunications;
    public boolean zutiRadar_ShipsAsRadar;
    public int zutiRadar_ShipRadar_MaxRange;
    public int zutiRadar_ShipRadar_MinHeight;
    public int zutiRadar_ShipRadar_MaxHeight;
    public int zutiRadar_ShipSmallRadar_MaxRange;
    public int zutiRadar_ShipSmallRadar_MinHeight;
    public int zutiRadar_ShipSmallRadar_MaxHeight;
    public boolean zutiRadar_ScoutsAsRadar;
    public int zutiRadar_ScoutRadar_MaxRange;
    public int zutiRadar_ScoutRadar_DeltaHeight;
    public ArrayList ScoutsRed;
    public ArrayList ScoutsBlue;
    public int zutiRadar_ScoutGroundObjects_Alpha;
    public final boolean zutiRadar_ShowRockets = true;
    public boolean zutiRadar_EnableBigShip_Radar;
    public boolean zutiRadar_EnableSmallShip_Radar;
    public boolean zutiRadar_ScoutCompleteRecon;
    public boolean zutiRadar_DisableVectoring;
    public static boolean ZUTI_RADAR_IN_ADV_MODE;
    private static int[] ZUTI_ICON_SIZES;
    public static int ZUTI_ICON_SIZE;
    public boolean zutiMisc_DisableAIRadioChatter;
    public boolean zutiMisc_DespawnAIPlanesAfterLanding;
    public boolean zutiMisc_HidePlayersCountOnHomeBase;
    public boolean zutiMisc_EnableReflyOnlyIfBailedOrDied;
    public boolean zutiMisc_DisableReflyForMissionDuration;
    public int zutiMisc_ReflyKIADelay;
    public int zutiMisc_MaxAllowedKIA;
    public float zutiMisc_ReflyKIADelayMultiplier;
    public float zutiMisc_BombsCat1_CratersVisibilityMultiplier;
    public float zutiMisc_BombsCat2_CratersVisibilityMultiplier;
    public float zutiMisc_BombsCat3_CratersVisibilityMultiplier;
    /*synthetic*/ static Class class$java$lang$String;
    /*synthetic*/ static Class class$com$maddox$rts$SectFile;
    /*synthetic*/ static Class class$com$maddox$il2$game$Mission;
    
    static class SPAWN implements NetSpawn
    {
	public void netSpawn(int i, NetMsgInput netmsginput) {
	    try {
		if (Main.cur().mission != null)
		    Main.cur().mission.destroy();
		Mission mission = new Mission();
		if (cur() != null)
		    cur().destroy();
		mission.clear();
		Main.cur().mission = mission;
		mission.createNetObject(netmsginput.channel(), i);
		Main.cur().missionLoading = mission;
	    } catch (Exception exception) {
		printDebug(exception);
	    }
	}
    }
    
    class Mirror extends NetMissionObj
    {
	public Mirror(Mission mission_0_, NetChannel netchannel, int i) {
	    super((Object) mission_0_, netchannel, i);
	    try {
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(0);
		postTo(netchannel, netmsgguaranted);
	    } catch (Exception exception) {
		if (mission_0_ != null) {
		    /* empty */
		}
		Mission.printDebug(exception);
	    }
	}
    }
    
    class Master extends NetMissionObj
    {
	public Master(Mission mission_1_) {
	    super((Object) mission_1_);
	    mission_1_.sectFinger
		= mission_1_.sectFile.fingerExcludeSectPrefix("$$$");
	}
    }
    
    class NetMissionObj extends NetObj implements NetChannelCallbackStream
    {
	private void msgCallback(NetChannel netchannel, int i) {
	    try {
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(1);
		netmsgguaranted.writeByte(i);
		NetMsgInput netmsginput = new NetMsgInput();
		netmsginput.setData(netchannel, true, netmsgguaranted.data(),
				    0, netmsgguaranted.size());
		MsgNet.postReal(Time.currentReal(), this, netmsginput);
	    } catch (Exception exception) {
		if (this != null) {
		    /* empty */
		}
		Mission.printDebug(exception);
	    }
	}
	
	public boolean netChannelCallback
	    (NetChannelOutStream netchanneloutstream,
	     NetMsgGuaranted netmsgguaranted) {
	    if (netmsgguaranted instanceof NetMsgSpawn)
		msgCallback(netchanneloutstream, 0);
	    else if (!(netmsgguaranted instanceof NetMsgDestroy)) {
	    while_2_:
		do {
		    boolean bool;
		    try {
			NetMsgInput netmsginput = new NetMsgInput();
			netmsginput.setData(netchanneloutstream, true,
					    netmsgguaranted.data(), 0,
					    netmsgguaranted.size());
			int i = netmsginput.readUnsignedByte();
			switch (i) {
			case 0:
			    msgCallback(netchanneloutstream, 1);
			    break while_2_;
			case 2:
			    msgCallback(netchanneloutstream, 3);
			    break while_2_;
			case 4:
			    msgCallback(netchanneloutstream, 4);
			    break while_2_;
			case 12:
			    Main3D.cur3D().gameTrackRecord()
				.startKeyRecord(netmsgguaranted);
			    bool = false;
			    break;
			default:
			    break while_2_;
			}
		    } catch (Exception exception) {
			if (this != null) {
			    /* empty */
			}
			Mission.printDebug(exception);
			break;
		    }
		    return bool;
		} while (false);
	    }
	    return true;
	}
	
	public boolean netChannelCallback
	    (NetChannelInStream netchannelinstream, NetMsgInput netmsginput) {
	    try {
		int i = netmsginput.readUnsignedByte();
		if (i == 4)
		    netchannelinstream.setPause(true);
		else if (i == 12) {
		    netchannelinstream.setGameTime();
		    if (isCoop() || isDogfight()) {
			Main.cur().netServerParams.prepareHidenAircraft();
			doMissionStarting();
			Time.setPause(false);
		    }
		    Main3D.cur3D().gameTrackPlay().startKeyPlay();
		}
		netmsginput.reset();
	    } catch (Exception exception) {
		if (this != null) {
		    /* empty */
		}
		Mission.printDebug(exception);
	    }
	    return true;
	}
	
	public void netChannelCallback(NetChannelInStream netchannelinstream,
				       NetMsgGuaranted netmsgguaranted) {
	    if (!(netmsgguaranted instanceof NetMsgSpawn)
		&& !(netmsgguaranted instanceof NetMsgDestroy)) {
		try {
		    NetMsgInput netmsginput = new NetMsgInput();
		    netmsginput.setData(netchannelinstream, true,
					netmsgguaranted.data(), 0,
					netmsgguaranted.size());
		    int i = netmsginput.readUnsignedByte();
		    if (i == 4)
			netchannelinstream.setPause(false);
		} catch (Exception exception) {
		    if (this != null) {
			/* empty */
		    }
		    Mission.printDebug(exception);
		}
	    }
	}
	
	public boolean netInput(NetMsgInput netmsginput) throws IOException {
	    Mission mission = (Mission) superObj();
	    mission.netInput(netmsginput);
	    return true;
	}
	
	public void msgNetNewChannel(NetChannel netchannel) {
	    if (Main.cur().missionLoading == null)
		tryReplicate(netchannel);
	}
	
	private void tryReplicate(NetChannel netchannel) {
	    if (netchannel.isReady() && netchannel.isPublic()
		&& netchannel != masterChannel && !netchannel.isMirrored(this)
		&& netchannel.userState == 1) {
		try {
		    postTo(netchannel, new NetMsgSpawn(this));
		} catch (Exception exception) {
		    if (this != null) {
			/* empty */
		    }
		    Mission.printDebug(exception);
		}
	    }
	}
	
	public NetMissionObj(Object object) {
	    super(object);
	}
	
	public NetMissionObj(Object object, NetChannel netchannel, int i) {
	    super(object, netchannel, i);
	}
    }
    
    static class WingTakeoffPos
    {
	public int x;
	public int y;
	
	public WingTakeoffPos(double d, double d_2_) {
	    x = (int) (d / 100.0) * 100;
	    y = (int) (d_2_ / 100.0) * 100;
	}
	
	public boolean equals(Object object) {
	    if (object == null)
		return false;
	    if (!(object instanceof WingTakeoffPos))
		return false;
	    WingTakeoffPos wingtakeoffpos_3_ = (WingTakeoffPos) object;
	    return x == wingtakeoffpos_3_.x && y == wingtakeoffpos_3_.y;
	}
	
	public int hashCode() {
	    return x + y;
	}
    }
    
    class TimeOutWing
    {
	String wingName;
	
	public void start() {
	    if (!isDestroyed()) {
		try {
		    NetAircraft.loadingCoopPlane = false;
		    Wing wing = new Wing();
		    wing.load(sectFile, wingName, null);
		    Mission.this.prepareSkinInWing(sectFile, wing);
		    wing.setOnAirport();
		} catch (Exception exception) {
		    printDebug(exception);
		}
	    }
	}
	
	public TimeOutWing(String string) {
	    wingName = string;
	}
    }
    
    public class BackgroundLoader extends BackgroundTask
    {
	private String _name;
	private SectFile _in;
	
	public void run() throws Exception {
	    Mission.this._load(_name, _in, true);
	}
	
	public BackgroundLoader(String string, SectFile sectfile) {
	    _name = string;
	    _in = sectfile;
	}
    }
    
    public static float respawnTime(String string) {
	Object object = respawnMap.get(string);
	if (object == null)
	    return 1800.0F;
	return ((Float) object).floatValue();
    }
    
    public static boolean isPlaying() {
	if (Main.cur() == null)
	    return false;
	if (Main.cur().mission == null)
	    return false;
	if (Main.cur().mission.isDestroyed())
	    return false;
	return Main.cur().mission.bPlaying;
    }
    
    public static boolean isSingle() {
	if (Main.cur().mission == null)
	    return false;
	if (Main.cur().mission.isDestroyed())
	    return false;
	if (Main.cur().mission.net == null)
	    return true;
	if (Main.cur().netServerParams == null)
	    return true;
	return Main.cur().netServerParams.isSingle();
    }
    
    public static boolean isNet() {
	if (Main.cur().mission == null)
	    return false;
	if (Main.cur().mission.isDestroyed())
	    return false;
	if (Main.cur().mission.net == null)
	    return false;
	if (Main.cur().netServerParams == null)
	    return false;
	return !Main.cur().netServerParams.isSingle();
    }
    
    public NetChannel getNetMasterChannel() {
	if (net == null)
	    return null;
	return net.masterChannel();
    }
    
    public static boolean isServer() {
	return NetEnv.isServer();
    }
    
    public static boolean isDeathmatch() {
	return isDogfight();
    }
    
    public static boolean isDogfight() {
	if (Main.cur().mission == null)
	    return false;
	if (Main.cur().mission.isDestroyed())
	    return false;
	if (Main.cur().mission.net == null)
	    return false;
	if (Main.cur().netServerParams == null)
	    return false;
	return Main.cur().netServerParams.isDogfight();
    }
    
    public static boolean isCoop() {
	if (Main.cur().mission == null)
	    return false;
	if (Main.cur().mission.isDestroyed())
	    return false;
	if (Main.cur().mission.net == null)
	    return false;
	if (Main.cur().netServerParams == null)
	    return false;
	return Main.cur().netServerParams.isCoop();
    }
    
    public static int curCloudsType() {
	if (Main.cur().mission == null)
	    return 0;
	return Main.cur().mission.curCloudsType;
    }
    
    public static float curCloudsHeight() {
	if (Main.cur().mission == null)
	    return 1000.0F;
	return Main.cur().mission.curCloudsHeight;
    }
    
    public static int curYear() {
	if (Main.cur().mission == null)
	    return 0;
	return Main.cur().mission.curYear;
    }
    
    public static int curMonth() {
	if (Main.cur().mission == null)
	    return 0;
	return Main.cur().mission.curMonth;
    }
    
    public static int curDay() {
	if (Main.cur().mission == null)
	    return 0;
	return Main.cur().mission.curDay;
    }
    
    public static float curWindDirection() {
	if (Main.cur().mission == null)
	    return 0.0F;
	return Main.cur().mission.curWindDirection;
    }
    
    public static float curWindVelocity() {
	if (Main.cur().mission == null)
	    return 0.0F;
	return Main.cur().mission.curWindVelocity;
    }
    
    public static float curGust() {
	if (Main.cur().mission == null)
	    return 0.0F;
	return Main.cur().mission.curGust;
    }
    
    public static float curTurbulence() {
	if (Main.cur().mission == null)
	    return 0.0F;
	return Main.cur().mission.curTurbulence;
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
    
    public static void load(String string, String string_5_, boolean bool)
	throws Exception {
	Mission mission = new Mission();
	if (cur() != null)
	    cur().destroy();
	else
	    mission.clear();
	mission.sectFile = new SectFile(string + string_5_);
	mission.load(string_5_, mission.sectFile, bool);
    }
    
    public static void loadFromSect(SectFile sectfile) throws Exception {
	loadFromSect(sectfile, false);
    }
    
    public static void loadFromSect(SectFile sectfile, boolean bool)
	throws Exception {
	Mission mission = new Mission();
	String string = sectfile.fileName();
	if (string != null && string.toLowerCase().startsWith("missions/"))
	    string = string.substring("missions/".length());
	if (cur() != null)
	    cur().destroy();
	else
	    mission.clear();
	mission.sectFile = sectfile;
	mission.load(string, mission.sectFile, bool);
    }
    
    public String name() {
	return name;
    }
    
    public SectFile sectFile() {
	return sectFile;
    }
    
    public long finger() {
	return sectFinger;
    }
    
    public boolean isDestroyed() {
	return name == null;
    }
    
    public void destroy() {
	if (!isDestroyed()) {
	    if (bPlaying)
		doEnd();
	    bPlaying = false;
	    clear();
	    name = null;
	    Main.cur().mission = null;
	    if (Main.cur().netMissionListener != null)
		Main.cur().netMissionListener.netMissionState(8, 0.0F, null);
	    if (net != null && !net.isDestroyed())
		net.destroy();
	    net = null;
	}
    }
    
    private void clear() {
	if (net != null) {
	    doReplicateNotMissionActors(false);
	    if (net.masterChannel() != null)
		doReplicateNotMissionActors(net.masterChannel(), false);
	}
	actors.clear();
	beaconsRed.clear();
	beaconsBlue.clear();
	hasRadioStations = false;
	radioStationsLoaded = false;
	meaconsRed.clear();
	meaconsBlue.clear();
	hayrakeMap.clear();
	curActor = 0;
	Main.cur().resetGame();
	ZutiSupportMethods.clear();
	ZutiRadarRefresh.reset();
	if (GUI.pad != null)
	    GUI.pad.zutiPadObjects.clear();
    }
    
    private void load(String string, SectFile sectfile, boolean bool)
	throws Exception {
	if (bool)
	    BackgroundTask.execute(new BackgroundLoader(string, sectfile));
	else
	    _load(string, sectfile, bool);
    }
    
    private void LOADING_STEP(float f, String string) {
	if (net != null && Main.cur().netMissionListener != null)
	    Main.cur().netMissionListener.netMissionState(3, f, string);
	if (!BackgroundTask.step(f, string))
	    throw new RuntimeException(BackgroundTask.executed()
					   .messageCancel());
    }
    
    private void _load(String string, SectFile sectfile, boolean bool)
	throws Exception {
	if (GUI.pad != null)
	    GUI.pad.zutiPadObjects.clear();
	zutiResetMissionVariables();
	AudioDevice.soundsOff();
	if (string != null)
	    System.out.println("Loading mission " + string + "...");
	else
	    System.out.println("Loading mission ...");
	EventLog.checkState();
	Main.cur().missionLoading = this;
	RTSConf.cur.time.setEnableChangePause1(false);
	Actor.setSpawnFromMission(true);
	try {
	    Main.cur().mission = this;
	    name = string;
	    if (net == null)
		createNetObject(null, 0);
	    loadMain(sectfile);
	    loadRespawnTime(sectfile);
	    Front.loadMission(sectfile);
	    List list = null;
	    if (Main.cur().netServerParams.isCoop()
		|| Main.cur().netServerParams.isDogfight()
		|| Main.cur().netServerParams.isSingle()) {
		try {
		    list = loadWings(sectfile);
		} catch (Exception exception) {
		    System.out.println("Mission error, ID_04: "
				       + exception.toString());
		    exception.printStackTrace();
		}
		try {
		    loadChiefs(sectfile);
		} catch (Exception exception) {
		    System.out.println("Mission error, ID_05: "
				       + exception.toString());
		}
	    }
	    try {
		loadHouses(sectfile);
	    } catch (Exception exception) {
		System.out.println("Mission error, ID_06.1: "
				   + exception.toString());
	    }
	    try {
		loadNStationary(sectfile);
	    } catch (Exception exception) {
		System.out.println("Mission error, ID_06.2: "
				   + exception.toString());
	    }
	    try {
		loadStationary(sectfile);
	    } catch (Exception exception) {
		System.out.println("Mission error, ID_06.3: "
				   + exception.toString());
	    }
	    try {
		loadRocketry(sectfile);
	    } catch (Exception exception) {
		System.out.println("Mission error, ID_06.4: "
				   + exception.toString());
	    }
	    try {
		loadViewPoint(sectfile);
	    } catch (Exception exception) {
		System.out.println("Mission error, ID_06.5: "
				   + exception.toString());
	    }
	    try {
		if (Main.cur().netServerParams.isDogfight())
		    loadBornPlaces(sectfile);
	    } catch (Exception exception) {
		System.out
		    .println("Mission error, ID_07: " + exception.toString());
	    }
	    if (Main.cur().netServerParams.isCoop()
		|| Main.cur().netServerParams.isSingle()
		|| Main.cur().netServerParams.isDogfight()) {
		try {
		    loadTargets(sectfile);
		} catch (Exception exception) {
		    System.out.println("Mission error, ID_08: "
				       + exception.toString());
		}
	    }
	    try {
		populateBeacons();
	    } catch (Exception exception) {
		System.out
		    .println("Mission error, ID_09: " + exception.toString());
	    }
	    try {
		populateRunwayLights();
	    } catch (Exception exception) {
		System.out
		    .println("Mission error, ID_10: " + exception.toString());
	    }
	    if (list != null) {
		int i = list.size();
		for (int i_6_ = 0; i_6_ < i; i_6_++) {
		    Wing wing = (Wing) list.get(i_6_);
		    try {
			if (Actor.isValid(wing))
			    wing.setOnAirport();
		    } catch (Exception exception) {
			/* empty */
		    }
		}
	    }
	} catch (Exception exception) {
	    if (net != null && Main.cur().netMissionListener != null)
		Main.cur().netMissionListener
		    .netMissionState(4, 0.0F, exception.getMessage());
	    printDebug(exception);
	    clear();
	    if (net != null && !net.isDestroyed())
		net.destroy();
	    net = null;
	    Main.cur().mission = null;
	    name = null;
	    Actor.setSpawnFromMission(false);
	    Main.cur().missionLoading = null;
	    setTime(false);
	    throw exception;
	}
	if (Config.isUSE_RENDER()) {
	    if (Actor.isValid(World.getPlayerAircraft()))
		World.land().cubeFullUpdate((float) World.getPlayerAircraft
							().pos
							.getAbsPoint().z);
	    else
		World.land().cubeFullUpdate(1000.0F);
	    GUI.pad.fillAirports();
	}
	Actor.setSpawnFromMission(false);
	Main.cur().missionLoading = null;
	Main.cur().missionCounter++;
	setTime(!Main.cur().netServerParams.isSingle());
	LOADING_STEP(90.0F, "task.Load_humans");
	Paratrooper.PRELOAD();
	LOADING_STEP(95.0F, "task.Load_humans");
	if (Main.cur().netServerParams.isSingle()
	    || Main.cur().netServerParams.isCoop()
	    || Main.cur().netServerParams.isDogfight())
	    Soldier.PRELOAD();
	LOADING_STEP(100.0F, "");
	if (Main.cur().netMissionListener != null)
	    Main.cur().netMissionListener.netMissionState(5, 0.0F, null);
	if (net.isMirror()) {
	    try {
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(4);
		net.masterChannel().userState = 4;
		net.postTo(net.masterChannel(), netmsgguaranted);
	    } catch (Exception exception) {
		printDebug(exception);
	    }
	    ((NetUser) NetEnv.host()).missionLoaded();
	} else if (Main.cur().netServerParams.isSingle()) {
	    if (Main.cur() instanceof Main3D)
		((Main3D) Main.cur()).ordersTree.missionLoaded();
	    Main.cur().dotRangeFriendly.setDefault();
	    Main.cur().dotRangeFoe.setDefault();
	    Main.cur().dotRangeFoe.set(-1.0, -1.0, -1.0, 5.0, -1.0, -1.0);
	} else
	    ((NetUser) NetEnv.host()).replicateDotRange();
	NetObj.tryReplicate(net, false);
	War.cur().missionLoaded();
	if (bool)
	    Main.cur().mission = this;
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
    
    private void loadZutis(SectFile sectfile) {
	try {
	    Main.cur().mission.zutiRadar_ShipsAsRadar = false;
	    if (sectfile.get("MDS", "MDS_Radar_ShipsAsRadar", 0, 0, 1) == 1)
		Main.cur().mission.zutiRadar_ShipsAsRadar = true;
	    Main.cur().mission.zutiRadar_ShipRadar_MaxRange
		= sectfile.get("MDS", "MDS_Radar_ShipRadar_MaxRange", 100, 1,
			       99999);
	    Main.cur().mission.zutiRadar_ShipRadar_MinHeight
		= sectfile.get("MDS", "MDS_Radar_ShipRadar_MinHeight", 100, 0,
			       99999);
	    Main.cur().mission.zutiRadar_ShipRadar_MaxHeight
		= sectfile.get("MDS", "MDS_Radar_ShipRadar_MaxHeight", 5000,
			       1000, 99999);
	    Main.cur().mission.zutiRadar_ShipSmallRadar_MaxRange
		= sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MaxRange", 25,
			       1, 99999);
	    Main.cur().mission.zutiRadar_ShipSmallRadar_MinHeight
		= sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MinHeight", 0,
			       0, 99999);
	    Main.cur().mission.zutiRadar_ShipSmallRadar_MaxHeight
		= sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MaxHeight",
			       2000, 1000, 99999);
	    Main.cur().mission.zutiRadar_ScoutsAsRadar = false;
	    if (sectfile.get("MDS", "MDS_Radar_ScoutsAsRadar", 0, 0, 1) == 1)
		Main.cur().mission.zutiRadar_ScoutsAsRadar = true;
	    Main.cur().mission.zutiRadar_ScoutRadar_MaxRange
		= sectfile.get("MDS", "MDS_Radar_ScoutRadar_MaxRange", 2, 1,
			       99999);
	    Main.cur().mission.zutiRadar_ScoutRadar_DeltaHeight
		= sectfile.get("MDS", "MDS_Radar_ScoutRadar_DeltaHeight", 1500,
			       100, 99999);
	    Main.cur().mission.zutiRadar_ScoutCompleteRecon = false;
	    if (sectfile.get("MDS", "MDS_Radar_ScoutCompleteRecon", 0, 0, 1)
		== 1)
		Main.cur().mission.zutiRadar_ScoutCompleteRecon = true;
	    zutiLoadScouts_Red(sectfile);
	    zutiLoadScouts_Blue(sectfile);
	    Main.cur().mission.zutiRadar_RefreshInterval
		= sectfile.get("MDS", "MDS_Radar_RefreshInterval", 0, 0,
			       99999) * 1000;
	    Main.cur().mission.zutiRadar_DisableVectoring = false;
	    if (sectfile.get("MDS", "MDS_Radar_DisableVectoring", 0, 0, 1)
		== 1)
		Main.cur().mission.zutiRadar_DisableVectoring = true;
	    Main.cur().mission.zutiRadar_EnableTowerCommunications = true;
	    if (sectfile.get("MDS", "MDS_Radar_EnableTowerCommunications", 1,
			     0, 1)
		== 0)
		Main.cur().mission.zutiRadar_EnableTowerCommunications = false;
	    ZUTI_RADAR_IN_ADV_MODE = false;
	    if (sectfile.get("MDS", "MDS_Radar_SetRadarToAdvanceMode", 0, 0, 1)
		== 1)
		ZUTI_RADAR_IN_ADV_MODE = true;
	    Main.cur().mission.zutiRadar_HideUnpopulatedAirstripsFromMinimap
		= false;
	    if (sectfile.get("MDS",
			     "MDS_Radar_HideUnpopulatedAirstripsFromMinimap",
			     0, 0, 1)
		== 1)
		Main.cur().mission
		    .zutiRadar_HideUnpopulatedAirstripsFromMinimap
		    = true;
	    Main.cur().mission.zutiRadar_ScoutGroundObjects_Alpha
		= sectfile.get("MDS", "MDS_Radar_ScoutGroundObjects_Alpha", 5,
			       1, 11);
	    Main.cur().mission.zutiMisc_DisableAIRadioChatter = false;
	    if (sectfile.get("MDS", "MDS_Misc_DisableAIRadioChatter", 0, 0, 1)
		== 1)
		Main.cur().mission.zutiMisc_DisableAIRadioChatter = true;
	    Main.cur().mission.zutiMisc_DespawnAIPlanesAfterLanding = true;
	    if (sectfile.get("MDS", "MDS_Misc_DespawnAIPlanesAfterLanding", 1,
			     0, 1)
		== 0)
		Main.cur().mission.zutiMisc_DespawnAIPlanesAfterLanding
		    = false;
	    Main.cur().mission.zutiMisc_HidePlayersCountOnHomeBase = false;
	    if (sectfile.get("MDS", "MDS_Misc_HidePlayersCountOnHomeBase", 0,
			     0, 1)
		== 1)
		Main.cur().mission.zutiMisc_HidePlayersCountOnHomeBase = true;
	    Main.cur().mission.zutiMisc_BombsCat1_CratersVisibilityMultiplier
		= (sectfile.get
		   ("MDS", "MDS_Misc_BombsCat1_CratersVisibilityMultiplier",
		    1.0F, 1.0F, 99999.0F));
	    Main.cur().mission.zutiMisc_BombsCat2_CratersVisibilityMultiplier
		= (sectfile.get
		   ("MDS", "MDS_Misc_BombsCat2_CratersVisibilityMultiplier",
		    1.0F, 1.0F, 99999.0F));
	    Main.cur().mission.zutiMisc_BombsCat3_CratersVisibilityMultiplier
		= (sectfile.get
		   ("MDS", "MDS_Misc_BombsCat3_CratersVisibilityMultiplier",
		    1.0F, 1.0F, 99999.0F));
	    zutiSetShipRadars();
	} catch (Exception exception) {
	    System.out
		.println("Mission error, ID_11: " + exception.toString());
	}
    }
    
    private void loadMain(SectFile sectfile) throws Exception {
	int i = sectfile.get("MAIN", "TIMECONSTANT", 0, 0, 1);
	World.cur().setTimeOfDayConstant(i == 1);
	World.setTimeofDay(sectfile.get("MAIN", "TIME", 12.0F, 0.0F, 23.99F));
	loadZutis(sectfile);
	int i_7_ = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
	World.cur().setWeaponsConstant(i_7_ == 1);
	bigShipHpDiv
	    = 1.0F / sectfile.get("MAIN", "ShipHP", 1.0F, 0.0010F, 100.0F);
	String string = sectfile.get("MAIN", "MAP");
	if (string == null)
	    throw new Exception("No MAP in mission file ");
	String string_8_ = null;
	int[] is = null;
	SectFile sectfile_9_ = new SectFile("maps/" + string);
	int i_10_ = sectfile_9_.sectionIndex("static");
	if (i_10_ >= 0 && sectfile_9_.vars(i_10_) > 0) {
	    string_8_ = sectfile_9_.var(i_10_, 0);
	    if (string_8_ == null || string_8_.length() <= 0)
		string_8_ = null;
	    else {
		string_8_ = HomePath.concatNames("maps/" + string, string_8_);
		is = Statics.readBridgesEndPoints(string_8_);
	    }
	}
	LOADING_STEP(0.0F, "task.Load_landscape");
	int i_11_ = sectfile.get("SEASON", "Year", 1940, 1930, 1960);
	int i_12_ = sectfile.get("SEASON", "Month",
				 World.land().config
				     .getDefaultMonth("maps/" + string),
				 1, 12);
	int i_13_ = sectfile.get("SEASON", "Day", 15, 1, 31);
	setDate(i_11_, i_12_, i_13_);
	World.land().LoadMap(string, is, i_12_, i_13_);
	World.cur().setCamouflage(World.land().config.camouflage);
	if (Config.isUSE_RENDER()) {
	    if (Main3D.cur3D().land2D != null) {
		if (!Main3D.cur3D().land2D.isDestroyed())
		    Main3D.cur3D().land2D.destroy();
		Main3D.cur3D().land2D = null;
	    }
	    int i_14_ = sectfile_9_.sectionIndex("MAP2D");
	    if (i_14_ >= 0) {
		int i_15_ = sectfile_9_.vars(i_14_);
		if (i_15_ > 0) {
		    LOADING_STEP(20.0F, "task.Load_map");
		    Main3D.cur3D().land2D
			= new Land2Dn(string, (double) World.land().getSizeX(),
				      (double) World.land().getSizeY());
		}
	    }
	    if (Main3D.cur3D().land2DText == null)
		Main3D.cur3D().land2DText = new Land2DText();
	    else
		Main3D.cur3D().land2DText.clear();
	    int i_16_ = sectfile_9_.sectionIndex("text");
	    if (i_16_ >= 0 && sectfile_9_.vars(i_16_) > 0) {
		LOADING_STEP(22.0F, "task.Load_landscape_texts");
		String string_17_ = sectfile_9_.var(i_16_, 0);
		Main3D.cur3D().land2DText
		    .load(HomePath.concatNames("maps/" + string, string_17_));
	    }
	}
	if (string_8_ != null) {
	    LOADING_STEP(23.0F, "task.Load_static_objects");
	    Statics.load(string_8_, World.cur().statics.bridges);
	    Engine.drawEnv().staticTrimToSize();
	}
	Statics.trim();
	if (Main.cur().netServerParams.isSingle()
	    || Main.cur().netServerParams.isCoop()
	    || Main.cur().netServerParams.isDogfight()) {
	    try {
		World.cur().statics.loadStateBridges(sectfile, false);
		World.cur().statics.loadStateHouses(sectfile, false);
		World.cur().statics.loadStateBridges(sectfile, true);
		World.cur().statics.loadStateHouses(sectfile, true);
		checkBridgesAndHouses(sectfile);
	    } catch (Exception exception) {
		System.out
		    .println("Mission error, ID_12: " + exception.toString());
	    }
	}
	if (Main.cur().netServerParams.isSingle()) {
	    player = sectfile.get("MAIN", "player");
	    playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
	} else
	    player = null;
	World.setMissionArmy(sectfile.get("MAIN", "army", 1, 1, 2));
	if (Config.isUSE_RENDER())
	    Main3D.cur3D().ordersTree.setFrequency(new Boolean(true));
	if (Config.isUSE_RENDER()) {
	    LOADING_STEP(29.0F, "task.Load_landscape_effects");
	    Main3D main3d = Main3D.cur3D();
	    int i_18_ = sectfile.get("MAIN", "CloudType", 0, 0, 6);
	    float f = sectfile.get("MAIN", "CloudHeight", 1000.0F, 300.0F,
				   5000.0F);
	    createClouds(i_18_, f);
	    if (!Config.cur.ini.get("game", "NoLensFlare", false)) {
		main3d.sunFlareCreate();
		main3d.sunFlareShow(true);
	    } else
		main3d.sunFlareShow(false);
	    float f_19_ = (float) (string.charAt(0) - '@') * 3.1415927F;
	    f_19_ = sectfile.get("WEATHER", "WindDirection", f_19_, 0.0F,
				 359.99F);
	    float f_20_ = 0.25F + (float) (i_18_ * i_18_) * 0.12F;
	    f_20_ = sectfile.get("WEATHER", "WindSpeed", f_20_, 0.0F, 15.0F);
	    float f_21_ = i_18_ <= 3 ? 0.0F : (float) i_18_ * 2.0F;
	    f_21_ = sectfile.get("WEATHER", "Gust", f_21_, 0.0F, 12.0F);
	    float f_22_ = i_18_ <= 2 ? 0.0F : (float) i_18_;
	    f_22_ = sectfile.get("WEATHER", "Turbulence", f_22_, 0.0F, 6.0F);
	    World.wind().set(f, f_19_, f_20_, f_21_, f_22_);
	    for (int i_23_ = 0; i_23_ < 3; i_23_++) {
		Main3D.cur3D()._lightsGlare[i_23_].setShow(true);
		Main3D.cur3D()._sunGlare[i_23_].setShow(true);
	    }
	}
    }
    
    public static void setDate(int i, int i_24_, int i_25_) {
	setYear(i);
	setMonth(i_24_);
	setDay(i_25_);
    }
    
    public static void setYear(int i) {
	if (i < 1930)
	    i = 1930;
	if (i > 1960)
	    i = 1960;
	if (cur() != null)
	    cur().curYear = i;
    }
    
    public static void setMonth(int i) {
	if (i < 1)
	    i = 1;
	if (i > 12)
	    i = 12;
	if (cur() != null)
	    cur().curMonth = i;
    }
    
    public static void setDay(int i) {
	if (i < 1)
	    i = 1;
	if (i > 31)
	    i = 31;
	if (cur() != null)
	    cur().curDay = i;
    }
    
    public static void setWindDirection(float f) {
	if (f < 0.0F)
	    f = 0.0F;
	if (f > 359.99F)
	    f = 0.0F;
	if (cur() != null)
	    cur().curWindDirection = f;
    }
    
    public static void setWindVelocity(float f) {
	if (f < 0.0F)
	    f = 0.0F;
	if (f > 15.0F)
	    f = 15.0F;
	if (cur() != null)
	    cur().curWindVelocity = f;
    }
    
    public static void setGust(float f) {
	if (f < 0.0F)
	    f = 0.0F;
	if (f > 12.0F)
	    f = 12.0F;
	if (cur() != null)
	    cur().curGust = f;
    }
    
    public static void setTurbulence(float f) {
	if (f < 0.0F)
	    f = 0.0F;
	if (f > 6.0F)
	    f = 6.0F;
	if (cur() != null)
	    cur().curTurbulence = f;
    }
    
    public static void createClouds(int i, float f) {
	if (i < 0)
	    i = 0;
	if (i > 6)
	    i = 6;
	if (cur() != null) {
	    cur().curCloudsType = i;
	    cur().curCloudsHeight = f;
	}
	if (Config.isUSE_RENDER()) {
	    Main3D main3d = Main3D.cur3D();
	    Camera3D camera3d = (Camera3D) Actor.getByName("camera");
	    if (main3d.clouds != null)
		main3d.clouds.destroy();
	    main3d.clouds
		= new EffClouds(World.cur().diffCur.NewCloudsRender, i, f);
	    if (i > 5) {
		try {
		    if (main3d.zip != null)
			main3d.zip.destroy();
		    main3d.zip = new Zip(f);
		} catch (Exception exception) {
		    System.out.println("Zip load error: " + exception);
		}
	    }
	    int i_26_ = 5 - i;
	    if (i == 6)
		i_26_ = 1;
	    if (i_26_ > 4)
		i_26_ = 4;
	    RenderContext.cfgLandFogHaze.set(i_26_);
	    RenderContext.cfgLandFogHaze.apply();
	    RenderContext.cfgLandFogHaze.reset();
	    RenderContext.cfgLandFogLow.set(0);
	    RenderContext.cfgLandFogLow.apply();
	    RenderContext.cfgLandFogLow.reset();
	    if (Actor.isValid(main3d.spritesFog))
		main3d.spritesFog.destroy();
	    main3d.spritesFog
		= new SpritesFog(camera3d, 0.7F, 7000.0F, 7500.0F);
	}
    }
    
    public static void setCloudsType(int i) {
	if (i < 0)
	    i = 0;
	if (i > 6)
	    i = 6;
	if (cur() != null)
	    cur().curCloudsType = i;
	if (Config.isUSE_RENDER()) {
	    Main3D main3d = Main3D.cur3D();
	    if (main3d.clouds != null)
		main3d.clouds.setType(i);
	    int i_27_ = 5 - i;
	    if (i == 6)
		i_27_ = 1;
	    if (i_27_ > 4)
		i_27_ = 4;
	    RenderContext.cfgLandFogHaze.set(i_27_);
	    RenderContext.cfgLandFogHaze.apply();
	    RenderContext.cfgLandFogHaze.reset();
	    RenderContext.cfgLandFogLow.set(0);
	    RenderContext.cfgLandFogLow.apply();
	    RenderContext.cfgLandFogLow.reset();
	}
    }
    
    public static void setCloudsHeight(float f) {
	if (cur() != null)
	    cur().curCloudsHeight = f;
	if (Config.isUSE_RENDER()) {
	    Main3D main3d = Main3D.cur3D();
	    if (main3d.clouds != null)
		main3d.clouds.setHeight(f);
	}
    }
    
    private void loadRespawnTime(SectFile sectfile) {
	respawnMap.clear();
	int i = sectfile.sectionIndex("RespawnTime");
	if (i >= 0) {
	    int i_28_ = sectfile.vars(i);
	    for (int i_29_ = 0; i_29_ < i_28_; i_29_++) {
		String string = sectfile.var(i, i_29_);
		float f = sectfile.get("RespawnTime", string, 1800.0F, 20.0F,
				       1000000.0F);
		respawnMap.put(string, new Float(f));
	    }
	}
    }
    
    private List loadWings(SectFile sectfile) throws Exception {
	int i = sectfile.sectionIndex("Wing");
	if (i < 0)
	    return null;
	if (!World.cur().diffCur.Takeoff_N_Landing)
	    prepareTakeoff(sectfile, !Main.cur().netServerParams.isSingle());
	NetChannel netchannel = null;
	if (!isServer())
	    netchannel = net.masterChannel();
	int i_30_ = sectfile.vars(i);
	ArrayList arraylist = null;
	if (i_30_ > 0)
	    arraylist = new ArrayList(i_30_);
	for (int i_31_ = 0; i_31_ < i_30_; i_31_++) {
	    LOADING_STEP((float) (30 + Math.round((float) i_31_ / (float) i_30_
						  * 30.0F)),
			 "task.Load_aircraft");
	    String string = sectfile.var(i, i_31_);
	    _loadPlayer = string.equals(player);
	    int i_32_ = sectfile.get(string, "StartTime", 0);
	    if (i_32_ > 0 && !_loadPlayer) {
		if (netchannel == null) {
		    double d = (double) ((long) i_32_ * 60L);
		    new MsgAction(0, d, new TimeOutWing(string)) {
			public void doAction(Object object) {
			    TimeOutWing timeoutwing = (TimeOutWing) object;
			    timeoutwing.start();
			}
		    };
		}
	    } else {
		NetAircraft.loadingCoopPlane
		    = (Main.cur().netServerParams != null
		       && Main.cur().netServerParams.isCoop());
		Wing wing = new Wing();
		wing.load(sectfile, string, netchannel);
		if (netchannel != null
		    && !Main.cur().netServerParams.isCoop()) {
		    Aircraft[] aircrafts = wing.airc;
		    for (int i_36_ = 0; i_36_ < aircrafts.length; i_36_++) {
			if (Actor.isValid(aircrafts[i_36_])
			    && aircrafts[i_36_].net == null) {
			    aircrafts[i_36_].destroy();
			    aircrafts[i_36_] = null;
			}
		    }
		}
		arraylist.add(wing);
		prepareSkinInWing(sectfile, wing);
	    }
	}
	LOADING_STEP(60.0F, "task.Load_aircraft");
	return arraylist;
    }
    
    private void prepareSkinInWing(SectFile sectfile, Wing wing) {
	if (Config.isUSE_RENDER()) {
	    Aircraft[] aircrafts = wing.airc;
	    for (int i = 0; i < aircrafts.length; i++) {
		if (Actor.isValid(aircrafts[i])) {
		    Aircraft aircraft = aircrafts[i];
		    prepareSkinInWing(sectfile, aircraft, wing.name(), i);
		}
	    }
	}
    }
    
    private void prepareSkinInWing(SectFile sectfile, Aircraft aircraft,
				   String string, int i) {
	if (Config.isUSE_RENDER()) {
	    if (World.getPlayerAircraft() == aircraft) {
		if (isSingle()) {
		    if (NetMissionTrack.isPlaying()) {
			((NetUser) Main.cur().netServerParams.host())
			    .tryPrepareSkin(aircraft);
			((NetUser) Main.cur().netServerParams.host())
			    .tryPrepareNoseart(aircraft);
			((NetUser) Main.cur().netServerParams.host())
			    .tryPreparePilot(aircraft);
		    } else {
			String string_37_
			    = Property.stringValue(aircraft.getClass(),
						   "keyName", null);
			String string_38_
			    = World.cur().userCfg.getSkin(string_37_);
			if (string_38_ != null) {
			    String string_39_
				= GUIAirArming.validateFileName(string_37_);
			    ((NetUser) NetEnv.host())
				.setSkin(string_39_ + "/" + string_38_);
			    ((NetUser) NetEnv.host()).tryPrepareSkin(aircraft);
			} else
			    ((NetUser) NetEnv.host()).setSkin(null);
			String string_40_
			    = World.cur().userCfg.getNoseart(string_37_);
			if (string_40_ != null) {
			    ((NetUser) NetEnv.host()).setNoseart(string_40_);
			    ((NetUser) NetEnv.host())
				.tryPrepareNoseart(aircraft);
			} else
			    ((NetUser) NetEnv.host()).setNoseart(null);
			String string_41_ = World.cur().userCfg.netPilot;
			((NetUser) NetEnv.host()).setPilot(string_41_);
			if (string_41_ != null)
			    ((NetUser) NetEnv.host())
				.tryPreparePilot(aircraft);
		    }
		}
	    } else {
		String string_42_
		    = sectfile.get(string, "skin" + i, (String) null);
		if (string_42_ != null) {
		    String string_43_
			= Aircraft.getPropertyMesh(aircraft.getClass(),
						   aircraft.getRegiment()
						       .country());
		    string_42_ = ((GUIAirArming.validateFileName
				   (Property.stringValue(aircraft.getClass(),
							 "keyName", null)))
				  + "/" + string_42_);
		    if (NetMissionTrack.isPlaying()) {
			string_42_ = (NetFilesTrack.getLocalFileName
				      (Main.cur().netFileServerSkin,
				       Main.cur().netServerParams.host(),
				       string_42_));
			if (string_42_ != null)
			    string_42_
				= Main.cur().netFileServerSkin
				      .alternativePath() + "/" + string_42_;
		    } else
			string_42_
			    = (Main.cur().netFileServerSkin.primaryPath() + "/"
			       + string_42_);
		    if (string_42_ != null) {
			String string_44_
			    = ("PaintSchemes/Cache/"
			       + Finger.file(0L, string_42_, -1));
			Aircraft.prepareMeshSkin(string_43_,
						 aircraft.hierMesh(),
						 string_42_, string_44_);
		    }
		}
		String string_45_
		    = sectfile.get(string, "noseart" + i, (String) null);
		if (string_45_ != null) {
		    String string_46_
			= (Main.cur().netFileServerNoseart.primaryPath() + "/"
			   + string_45_);
		    String string_47_
			= string_45_.substring(0, string_45_.length() - 4);
		    if (NetMissionTrack.isPlaying()) {
			string_46_ = (NetFilesTrack.getLocalFileName
				      (Main.cur().netFileServerNoseart,
				       Main.cur().netServerParams.host(),
				       string_45_));
			if (string_46_ != null) {
			    string_47_
				= string_46_
				      .substring(0, string_46_.length() - 4);
			    string_46_
				= Main.cur().netFileServerNoseart
				      .alternativePath() + "/" + string_46_;
			}
		    }
		    if (string_46_ != null) {
			String string_48_ = ("PaintSchemes/Cache/Noseart0"
					     + string_47_ + ".tga");
			String string_49_ = ("PaintSchemes/Cache/Noseart0"
					     + string_47_ + ".mat");
			String string_50_ = ("PaintSchemes/Cache/Noseart1"
					     + string_47_ + ".tga");
			String string_51_ = ("PaintSchemes/Cache/Noseart1"
					     + string_47_ + ".mat");
			if (BmpUtils.bmp8PalTo2TGA4(string_46_, string_48_,
						    string_50_))
			    Aircraft.prepareMeshNoseart(aircraft.hierMesh(),
							string_49_, string_51_,
							string_48_, string_50_,
							null);
		    }
		}
		String string_52_
		    = sectfile.get(string, "pilot" + i, (String) null);
		if (string_52_ != null) {
		    String string_53_
			= (Main.cur().netFileServerPilot.primaryPath() + "/"
			   + string_52_);
		    String string_54_
			= string_52_.substring(0, string_52_.length() - 4);
		    if (NetMissionTrack.isPlaying()) {
			string_53_ = (NetFilesTrack.getLocalFileName
				      (Main.cur().netFileServerPilot,
				       Main.cur().netServerParams.host(),
				       string_52_));
			if (string_53_ != null) {
			    string_54_
				= string_53_
				      .substring(0, string_53_.length() - 4);
			    string_53_
				= Main.cur().netFileServerPilot
				      .alternativePath() + "/" + string_53_;
			}
		    }
		    if (string_53_ != null) {
			String string_55_
			    = "PaintSchemes/Cache/Pilot" + string_54_ + ".tga";
			String string_56_
			    = "PaintSchemes/Cache/Pilot" + string_54_ + ".mat";
			if (BmpUtils.bmp8PalToTGA3(string_53_, string_55_))
			    Aircraft.prepareMeshPilot(aircraft.hierMesh(), 0,
						      string_56_, string_55_,
						      null);
		    }
		}
	    }
	}
    }
    
    public void prepareSkinAI(Aircraft aircraft) {
	String string = aircraft.name();
	if (string.length() >= 4) {
	    String string_57_ = string.substring(0, string.length() - 1);
	    int i;
	    try {
		i = Integer.parseInt(string.substring(string.length() - 1,
						      string.length()));
	    } catch (Exception exception) {
		return;
	    }
	    prepareSkinInWing(sectFile, aircraft, string_57_, i);
	}
    }
    
    public void recordNetFiles() {
	int i = sectFile.sectionIndex("Wing");
	if (i >= 0) {
	    int i_58_ = sectFile.vars(i);
	    for (int i_59_ = 0; i_59_ < i_58_; i_59_++) {
		try {
		    String string = sectFile.var(i, i_59_);
		    String string_60_
			= sectFile.get(string, "Class", (String) null);
		    Class var_class = ObjIO.classForName(string_60_);
		    String string_61_
			= (GUIAirArming.validateFileName
			   (Property.stringValue(var_class, "keyName", null)));
		    for (int i_62_ = 0; i_62_ < 4; i_62_++) {
			String string_63_
			    = sectFile.get(string, "skin" + i_62_,
					   (String) null);
			if (string_63_ != null)
			    recordNetFile(Main.cur().netFileServerSkin,
					  string_61_ + "/" + string_63_);
			recordNetFile(Main.cur().netFileServerNoseart,
				      sectFile.get(string, "noseart" + i_62_,
						   (String) null));
			recordNetFile(Main.cur().netFileServerPilot,
				      sectFile.get(string, "pilot" + i_62_,
						   (String) null));
		    }
		} catch (Exception exception) {
		    printDebug(exception);
		}
	    }
	}
    }
    
    private void recordNetFile(NetFileServerDef netfileserverdef,
			       String string) {
	if (string != null) {
	    String string_64_ = string;
	    if (NetMissionTrack.isPlaying()) {
		string_64_
		    = NetFilesTrack.getLocalFileName(netfileserverdef,
						     Main.cur()
							 .netServerParams
							 .host(),
						     string);
		if (string_64_ == null)
		    return;
	    }
	    NetFilesTrack.recordFile(netfileserverdef,
				     ((NetUser)
				      Main.cur().netServerParams.host()),
				     string, string_64_);
	}
    }
    
    public Aircraft loadAir(SectFile sectfile, String string,
			    String string_65_, String string_66_,
			    int i) throws Exception {
	boolean bool = !isServer();
	Class var_class = ObjIO.classForName(string);
	Aircraft aircraft = (Aircraft) var_class.newInstance();
	if (Main.cur().netServerParams.isSingle() && _loadPlayer) {
	    if (Property.value(var_class, "cockpitClass", null) == null)
		throw new Exception
			  ("One of selected aircraft has no cockpit.");
	    if (playerNum == 0) {
		World.setPlayerAircraft(aircraft);
		_loadPlayer = false;
	    } else
		playerNum--;
	}
	aircraft.setName(string_66_);
	int i_67_ = 0;
	if (bool) {
	    i_67_ = ((Integer) actors.get(curActor)).intValue();
	    if (i_67_ == 0)
		aircraft.load(sectfile, string_65_, i, null, 0);
	    else
		aircraft.load(sectfile, string_65_, i, net.masterChannel(),
			      i_67_);
	} else
	    aircraft.load(sectfile, string_65_, i, null, 0);
	if (aircraft.isSpawnFromMission()) {
	    if (net.isMirror()) {
		if (i_67_ == 0)
		    actors.set(curActor++, null);
		else
		    actors.set(curActor++, aircraft);
	    } else
		actors.add(aircraft);
	}
	aircraft.pos.reset();
	return aircraft;
    }
    
    public static void preparePlayerNumberOn(SectFile sectfile) {
	UserCfg usercfg = World.cur().userCfg;
	String string = sectfile.get("MAIN", "player", "");
	if (!"".equals(string)) {
	    String string_68_ = sectfile.get("MAIN", "playerNum", "");
	    sectfile.set(string, "numberOn" + string_68_,
			 usercfg.netNumberOn ? "1" : "0");
	}
    }
    
    private void prepareTakeoff(SectFile sectfile, boolean bool) {
	if (bool) {
	    int i = sectfile.sectionIndex("Wing");
	    if (i < 0)
		return;
	    int i_69_ = sectfile.vars(i);
	    for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
		prepareWingTakeoff(sectfile, sectfile.var(i, i_70_));
	} else {
	    String string = sectfile.get("MAIN", "player", (String) null);
	    if (string == null)
		return;
	    prepareWingTakeoff(sectfile, string);
	}
	sectFinger = sectfile.fingerExcludeSectPrefix("$$$");
    }
    
    private void prepareWingTakeoff(SectFile sectfile, String string) {
	String string_71_ = string + "_Way";
	int i = sectfile.sectionIndex(string_71_);
	if (i >= 0) {
	    int i_72_ = sectfile.vars(i);
	    if (i_72_ != 0) {
		ArrayList arraylist = new ArrayList(i_72_);
		for (int i_73_ = 0; i_73_ < i_72_; i_73_++)
		    arraylist.add(sectfile.line(i, i_73_));
		String string_74_ = (String) arraylist.get(0);
		if (string_74_.startsWith("TAKEOFF")) {
		    StringBuffer stringbuffer = new StringBuffer("NORMFLY");
		    NumberTokenizer numbertokenizer
			= new NumberTokenizer(string_74_);
		    numbertokenizer.next((String) null);
		    double d = numbertokenizer.next(1000.0);
		    double d_75_ = numbertokenizer.next(1000.0);
		    WingTakeoffPos wingtakeoffpos
			= new WingTakeoffPos(d, d_75_);
		    if (mapWingTakeoff == null) {
			mapWingTakeoff = new HashMap();
			mapWingTakeoff.put(wingtakeoffpos, wingtakeoffpos);
		    } else {
			for (;;) {
			    WingTakeoffPos wingtakeoffpos_76_
				= ((WingTakeoffPos)
				   mapWingTakeoff.get(wingtakeoffpos));
			    if (wingtakeoffpos_76_ == null) {
				mapWingTakeoff.put(wingtakeoffpos,
						   wingtakeoffpos);
				break;
			    }
			    wingtakeoffpos.x += 200;
			}
		    }
		    d = (double) wingtakeoffpos.x;
		    d_75_ = (double) wingtakeoffpos.y;
		    stringbuffer.append(" ");
		    stringbuffer.append(d);
		    stringbuffer.append(" ");
		    stringbuffer.append(d_75_);
		    do {
			if (i_72_ > 1) {
			    String string_77_ = (String) arraylist.get(1);
			    if (!string_77_.startsWith("TAKEOFF")
				&& !string_77_.startsWith("LANDING")) {
				numbertokenizer
				    = new NumberTokenizer(string_77_);
				numbertokenizer.next((String) null);
				numbertokenizer.next((String) null);
				numbertokenizer.next((String) null);
				stringbuffer.append(" ");
				stringbuffer
				    .append(numbertokenizer.next("1000.0"));
				stringbuffer.append(" ");
				stringbuffer
				    .append(numbertokenizer.next("300.0"));
				arraylist.set(0, stringbuffer.toString());
				break;
			    }
			}
			stringbuffer.append(" 1000 300");
			arraylist.set(0, stringbuffer.toString());
		    } while (false);
		    sectfile.sectionClear(i);
		    for (int i_78_ = 0; i_78_ < i_72_; i_78_++)
			sectfile.lineAdd(i, (String) arraylist.get(i_78_));
		}
	    }
	}
    }
    
    private void loadChiefs(SectFile sectfile) throws Exception {
	int i = sectfile.sectionIndex("Chiefs");
	if (i >= 0) {
	    if (chiefsIni == null)
		chiefsIni = new SectFile("com/maddox/il2/objects/chief.ini");
	    int i_79_ = sectfile.vars(i);
	    for (int i_80_ = 0; i_80_ < i_79_; i_80_++) {
		LOADING_STEP((float) (60
				      + Math.round((float) i_80_
						   / (float) i_79_ * 20.0F)),
			     "task.Load_tanks");
		NumberTokenizer numbertokenizer
		    = new NumberTokenizer(sectfile.line(i, i_80_));
		String string = numbertokenizer.next();
		String string_81_ = numbertokenizer.next();
		int i_82_ = numbertokenizer.next(-1);
		if (i_82_ < 0)
		    System.out.println("Mission: Wrong chief's army [" + i_82_
				       + "]");
		else {
		    Chief.new_DELAY_WAKEUP = numbertokenizer.next(0.0F);
		    Chief.new_SKILL_IDX = numbertokenizer.next(2);
		    if (Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3)
			System.out.println("Mission: Wrong chief's skill ["
					   + Chief.new_SKILL_IDX + "]");
		    else {
			Chief.new_SLOWFIRE_K = numbertokenizer.next(1.0F);
			if (Chief.new_SLOWFIRE_K < 0.5F
			    || Chief.new_SLOWFIRE_K > 100.0F)
			    System.out.println
				("Mission: Wrong chief's slowfire ["
				 + Chief.new_SLOWFIRE_K + "]");
			else if (chiefsIni.sectionIndex(string_81_) < 0)
			    System.out.println("Mission: Wrong chief's type ["
					       + string_81_ + "]");
			else {
			    int i_83_ = string_81_.indexOf('.');
			    if (i_83_ <= 0)
				System.out.println
				    ("Mission: Wrong chief's type ["
				     + string_81_ + "]");
			    else {
				String string_84_
				    = string_81_.substring(0, i_83_);
				String string_85_
				    = string_81_.substring(i_83_ + 1);
				String string_86_
				    = chiefsIni.get(string_84_, string_85_);
				if (string_86_ == null)
				    System.out.println
					("Mission: Wrong chief's type ["
					 + string_81_ + "]");
				else {
				    numbertokenizer
					= new NumberTokenizer(string_86_);
				    string_86_ = numbertokenizer.nextToken();
				    numbertokenizer.nextToken();
				    String string_87_ = null;
				    if (numbertokenizer.hasMoreTokens())
					string_87_
					    = numbertokenizer.nextToken();
				    Class var_class
					= ObjIO.classForName(string_86_);
				    if (var_class == null)
					System.out.println
					    ("Mission: Unknown chief's class ["
					     + string_86_ + "]");
				    else {
					Constructor constructor;
					try {
					    Class[] var_classes = new Class[6];
					    var_classes[0]
						= ((class$java$lang$String
						    == null)
						   ? (class$java$lang$String
						      = (class$
							 ("java.lang.String")))
						   : class$java$lang$String);
					    var_classes[1] = Integer.TYPE;
					    var_classes[2]
						= ((class$com$maddox$rts$SectFile
						    == null)
						   ? (class$com$maddox$rts$SectFile
						      = (class$
							 ("com.maddox.rts.SectFile")))
						   : class$com$maddox$rts$SectFile);
					    var_classes[3]
						= ((class$java$lang$String
						    == null)
						   ? (class$java$lang$String
						      = (class$
							 ("java.lang.String")))
						   : class$java$lang$String);
					    var_classes[4]
						= ((class$com$maddox$rts$SectFile
						    == null)
						   ? (class$com$maddox$rts$SectFile
						      = (class$
							 ("com.maddox.rts.SectFile")))
						   : class$com$maddox$rts$SectFile);
					    var_classes[5]
						= ((class$java$lang$String
						    == null)
						   ? (class$java$lang$String
						      = (class$
							 ("java.lang.String")))
						   : class$java$lang$String);
					    constructor
						= (var_class.getConstructor
						   (var_classes));
					} catch (Exception exception) {
					    System.out.println
						("Mission: No required constructor in chief's class ["
						 + string_86_ + "]");
					    continue;
					}
					int i_88_ = curActor;
					Object object;
					try {
					    Object[] objects = new Object[6];
					    objects[0] = string;
					    objects[1] = new Integer(i_82_);
					    objects[2] = chiefsIni;
					    objects[3] = string_81_;
					    objects[4] = sectfile;
					    objects[5] = string + "_Road";
					    object = constructor
							 .newInstance(objects);
					} catch (Exception exception) {
					    System.out.println
						("Mission: Can't create chief '"
						 + string + "' [class:"
						 + string_86_ + "]");
					    continue;
					}
					if (string_87_ != null)
					    ((Actor) object).icon
						= IconDraw.get(string_87_);
					if (i_88_ != curActor && net != null
					    && net.isMirror()) {
					    for (int i_89_ = i_88_;
						 i_89_ < curActor; i_89_++) {
						Actor actor
						    = ((Actor)
						       actors.get(i_89_));
						if (actor.net == null
						    || actor.net.isMaster()) {
						    if (Actor.isValid(actor)) {
							if (object
							    instanceof ChiefGround)
							    ((ChiefGround)
							     object)
								.Detach
								(actor, actor);
							actor.destroy();
						    }
						    actors.set(i_89_, null);
						}
					    }
					}
					if (object instanceof ChiefGround)
					    ((ChiefGround) object)
						.dreamFire(true);
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }
    
    public int getUnitNetIdRemote(Actor actor) {
	if (net.isMaster()) {
	    actors.add(actor);
	    return 0;
	}
	Integer integer = (Integer) actors.get(curActor);
	actors.set(curActor, actor);
	curActor++;
	return integer.intValue();
    }
    
    private Actor loadStationaryActor(String string, String string_90_, int i,
				      double d, double d_91_, float f,
				      float f_92_, String string_93_,
				      String string_94_, String string_95_) {
	Class var_class;
	try {
	    var_class = ObjIO.classForName(string_90_);
	} catch (Exception exception) {
	    System.out
		.println("Mission: class '" + string_90_ + "' not found");
	    return null;
	}
	ActorSpawn actorspawn
	    = (ActorSpawn) Spawn.get(var_class.getName(), false);
	if (actorspawn == null) {
	    System.out.println("Mission: ActorSpawn for '" + string_90_
			       + "' not found");
	    return null;
	}
	spawnArg.clear();
	if (string != null) {
	    if ("NONAME".equals(string)) {
		System.out.println("Mission: 'NONAME' - not valid actor name");
		return null;
	    }
	    if (Actor.getByName(string) != null) {
		System.out
		    .println("Mission: actor '" + string + "' alredy exist");
		return null;
	    }
	    spawnArg.name = string;
	}
	spawnArg.army = i;
	spawnArg.armyExist = true;
	spawnArg.country = string_93_;
	Chief.new_DELAY_WAKEUP = 0.0F;
	ArtilleryGeneric.new_RADIUS_HIDE = 0.0F;
	if (string_93_ != null) {
	    try {
		Chief.new_DELAY_WAKEUP = (float) Integer.parseInt(string_93_);
		ArtilleryGeneric.new_RADIUS_HIDE = Chief.new_DELAY_WAKEUP;
	    } catch (Exception exception) {
		/* empty */
	    }
	}
	Chief.new_SKILL_IDX = 2;
	if (string_94_ != null) {
	    try {
		Chief.new_SKILL_IDX = Integer.parseInt(string_94_);
	    } catch (Exception exception) {
		/* empty */
	    }
	    if (Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3) {
		System.out.println("Mission: Wrong actor skill '"
				   + Chief.new_SKILL_IDX + "'");
		return null;
	    }
	}
	Chief.new_SLOWFIRE_K = 1.0F;
	if (string_95_ != null) {
	    try {
		Chief.new_SLOWFIRE_K = Float.parseFloat(string_95_);
	    } catch (Exception exception) {
		/* empty */
	    }
	    if (Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100.0F) {
		System.out.println("Mission: Wrong actor slowfire '"
				   + Chief.new_SLOWFIRE_K + "'");
		return null;
	    }
	}
	p.set(d, d_91_, 0.0);
	spawnArg.point = p;
	o.set(f, 0.0F, 0.0F);
	spawnArg.orient = o;
	if (f_92_ > 0.0F) {
	    spawnArg.timeLenExist = true;
	    spawnArg.timeLen = f_92_;
	}
	spawnArg.netChannel = null;
	spawnArg.netIdRemote = 0;
	if (net.isMirror()) {
	    spawnArg.netChannel = net.masterChannel();
	    spawnArg.netIdRemote = ((Integer) actors.get(curActor)).intValue();
	    if (spawnArg.netIdRemote == 0) {
		actors.set(curActor++, null);
		return null;
	    }
	}
	Actor actor = null;
	try {
	    actor = actorspawn.actorSpawn(spawnArg);
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	}
	if (net.isMirror())
	    actors.set(curActor++, actor);
	else
	    actors.add(actor);
	return actor;
    }
    
    private void loadStationary(SectFile sectfile) {
	int i = sectfile.sectionIndex("Stationary");
	if (i >= 0) {
	    int i_96_ = sectfile.vars(i);
	    for (int i_97_ = 0; i_97_ < i_96_; i_97_++) {
		LOADING_STEP((float) (80 + Math.round((float) i_97_
						      / (float) i_96_ * 5.0F)),
			     "task.Load_stationary_objects");
		NumberTokenizer numbertokenizer
		    = new NumberTokenizer(sectfile.line(i, i_97_));
		loadStationaryActor(null, numbertokenizer.next(""),
				    numbertokenizer.next(0),
				    numbertokenizer.next(0.0),
				    numbertokenizer.next(0.0),
				    numbertokenizer.next(0.0F),
				    numbertokenizer.next(0.0F),
				    numbertokenizer.next((String) null),
				    numbertokenizer.next((String) null),
				    numbertokenizer.next((String) null));
	    }
	}
    }
    
    private void loadNStationary(SectFile sectfile) {
	int i = sectfile.sectionIndex("NStationary");
	if (i >= 0) {
	    int i_98_ = sectfile.vars(i);
	    for (int i_99_ = 0; i_99_ < i_98_; i_99_++) {
		LOADING_STEP((float) (85 + Math.round((float) i_99_
						      / (float) i_98_ * 5.0F)),
			     "task.Load_stationary_objects");
		NumberTokenizer numbertokenizer
		    = new NumberTokenizer(sectfile.line(i, i_99_));
		loadStationaryActor(numbertokenizer.next(""),
				    numbertokenizer.next(""),
				    numbertokenizer.next(0),
				    numbertokenizer.next(0.0),
				    numbertokenizer.next(0.0),
				    numbertokenizer.next(0.0F),
				    numbertokenizer.next(0.0F),
				    numbertokenizer.next((String) null),
				    numbertokenizer.next((String) null),
				    numbertokenizer.next((String) null));
	    }
	}
    }
    
    private void loadRocketry(SectFile sectfile) {
	int i = sectfile.sectionIndex("Rocket");
	if (i >= 0) {
	    int i_100_ = sectfile.vars(i);
	    for (int i_101_ = 0; i_101_ < i_100_; i_101_++) {
		NumberTokenizer numbertokenizer
		    = new NumberTokenizer(sectfile.line(i, i_101_));
		if (numbertokenizer.hasMoreTokens()) {
		    String string = numbertokenizer.next("");
		    if (numbertokenizer.hasMoreTokens()) {
			String string_102_ = numbertokenizer.next("");
			if (numbertokenizer.hasMoreTokens()) {
			    int i_103_ = numbertokenizer.next(1, 1, 2);
			    double d = numbertokenizer.next(0.0);
			    if (numbertokenizer.hasMoreTokens()) {
				double d_104_ = numbertokenizer.next(0.0);
				if (numbertokenizer.hasMoreTokens()) {
				    float f = numbertokenizer.next(0.0F);
				    if (numbertokenizer.hasMoreTokens()) {
					float f_105_
					    = numbertokenizer.next(0.0F);
					int i_106_ = numbertokenizer.next(1);
					float f_107_
					    = numbertokenizer.next(20.0F);
					Point2d point2d = null;
					if (numbertokenizer.hasMoreTokens())
					    point2d
						= new Point2d(numbertokenizer
								  .next(0.0),
							      numbertokenizer
								  .next(0.0));
					NetChannel netchannel = null;
					int i_108_ = 0;
					if (net.isMirror()) {
					    netchannel = net.masterChannel();
					    i_108_ = ((Integer)
						      actors.get(curActor))
							 .intValue();
					    if (i_108_ == 0) {
						actors.set(curActor++, null);
						continue;
					    }
					}
					RocketryGeneric rocketrygeneric = null;
					try {
					    rocketrygeneric
						= (RocketryGeneric.New
						   (string, string_102_,
						    netchannel, i_108_, i_103_,
						    d, d_104_, f, f_105_,
						    i_106_, f_107_, point2d));
					} catch (Exception exception) {
					    System.out.println
						(exception.getMessage());
					    exception.printStackTrace();
					}
					if (net.isMirror())
					    actors.set(curActor++,
						       rocketrygeneric);
					else
					    actors.add(rocketrygeneric);
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }
    
    private void loadHouses(SectFile sectfile) {
	int i = sectfile.sectionIndex("Buildings");
	if (i >= 0) {
	    int i_109_ = sectfile.vars(i);
	    if (i_109_ != 0) {
		if (net.isMirror()) {
		    spawnArg.netChannel = net.masterChannel();
		    spawnArg.netIdRemote
			= ((Integer) actors.get(curActor)).intValue();
		    HouseManager housemanager
			= new HouseManager(sectfile, "Buildings",
					   net.masterChannel(),
					   ((Integer) actors.get(curActor))
					       .intValue());
		    actors.set(curActor++, housemanager);
		} else {
		    HouseManager housemanager
			= new HouseManager(sectfile, "Buildings", null, 0);
		    actors.add(housemanager);
		}
	    }
	}
    }
    
    private void loadBornPlaces(SectFile sectfile) {
	int i = sectfile.sectionIndex("BornPlace");
	if (i >= 0) {
	    int i_110_ = sectfile.vars(i);
	    if (i_110_ != 0 && World.cur().airdrome != null
		&& World.cur().airdrome.stay != null) {
		World.cur().bornPlaces = new ArrayList(i_110_);
		for (int i_111_ = 0; i_111_ < i_110_; i_111_++) {
		    NumberTokenizer numbertokenizer
			= new NumberTokenizer(sectfile.line(i, i_111_));
		    int i_112_
			= numbertokenizer.next(0, 0, Army.amountNet() - 1);
		    float f = (float) numbertokenizer.next(1000, 500, 10000);
		    double d = (double) (f * f);
		    float f_113_ = (float) numbertokenizer.next(0);
		    float f_114_ = (float) numbertokenizer.next(0);
		    boolean bool = numbertokenizer.next(1) == 1;
		    int i_115_ = 1000;
		    int i_116_ = 200;
		    int i_117_ = 0;
		    int i_118_ = 0;
		    int i_119_ = 0;
		    int i_120_ = 5000;
		    int i_121_ = 50;
		    boolean bool_122_ = false;
		    boolean bool_123_ = false;
		    boolean bool_124_ = false;
		    boolean bool_125_ = false;
		    boolean bool_126_ = false;
		    boolean bool_127_ = false;
		    boolean bool_128_ = false;
		    double d_129_ = 3.8;
		    boolean bool_130_ = false;
		    int i_131_ = 0;
		    try {
			i_115_ = numbertokenizer.next(1000, 0, 10000);
			i_131_++;
			i_116_ = numbertokenizer.next(200, 0, 500);
			i_131_++;
			i_117_ = numbertokenizer.next(0, 0, 360);
			i_131_++;
			i_118_ = numbertokenizer.next(0, 0, 99999);
			i_131_++;
			i_119_ = numbertokenizer.next(0, 0, 99999);
			i_131_++;
			i_120_ = numbertokenizer.next(5000, 0, 99999);
			i_131_++;
			i_121_ = numbertokenizer.next(50, 1, 99999);
			i_131_++;
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_123_ = true;
			    i_131_++;
			}
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_124_ = true;
			    i_131_++;
			}
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_125_ = true;
			    i_131_++;
			}
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_127_ = true;
			    i_131_++;
			}
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_128_ = true;
			    i_131_++;
			}
			d_129_ = numbertokenizer.next(3.8, 0.0, 10.0);
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_126_ = true;
			    i_131_++;
			}
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_130_ = true;
			    i_131_++;
			}
			if (numbertokenizer.next(0, 0, 1) == 1) {
			    bool_122_ = true;
			    i_131_++;
			}
		    } catch (Exception exception) {
			System.out.println
			    ("Mission: no air spawn entries defined for HomeBase nr. "
			     + i_111_ + ". value index: " + i_131_);
		    }
		    boolean bool_132_ = false;
		    Point_Stay[][] point_stays = World.cur().airdrome.stay;
		    if (!isDogfight())
			bool_132_ = true;
		    else {
			for (int i_133_ = 0; i_133_ < point_stays.length;
			     i_133_++) {
			    if (point_stays[i_133_] != null) {
				Point_Stay point_stay
				    = (point_stays[i_133_]
				       [point_stays[i_133_].length - 1]);
				if ((double) (((point_stay.x - f_113_)
					       * (point_stay.x - f_113_))
					      + ((point_stay.y - f_114_)
						 * (point_stay.y - f_114_)))
				    <= d) {
				    bool_132_ = true;
				    break;
				}
			    }
			}
		    }
		    if (bool_132_) {
			BornPlace bornplace
			    = new BornPlace((double) f_113_, (double) f_114_,
					    i_112_, f);
			bornplace.zutiRadarHeight_MIN = i_119_;
			bornplace.zutiRadarHeight_MAX = i_120_;
			bornplace.zutiRadarRange = i_121_;
			bornplace.zutiSpawnHeight = i_115_;
			bornplace.zutiSpawnSpeed = i_116_;
			bornplace.zutiSpawnOrient = i_117_;
			bornplace.zutiMaxBasePilots = i_118_;
			bornplace.zutiAirspawnIfCarrierFull = bool_122_;
			bornplace.zutiAirspawnOnly = bool_123_;
			bornplace.zutiDisableSpawning = bool_127_;
			bornplace.zutiEnableFriction = bool_128_;
			bornplace.zutiFriction = d_129_;
			bornplace.zutiEnablePlaneLimits = bool_124_;
			bornplace.zutiDecreasingNumberOfPlanes = bool_125_;
			bornplace.zutiIncludeStaticPlanes = bool_126_;
			bornplace.zutiBpIndex = i_111_;
			bornplace.zutiStaticPositionOnly = bool_130_;
			bornplace.bParachute = bool;
			World.cur().bornPlaces.add(bornplace);
			bornplace.zutiCountBornPlaceStayPoints();
			if (actors != null) {
			    int i_134_ = actors.size();
			    for (int i_135_ = 0; i_135_ < i_134_; i_135_++) {
				Actor actor = (Actor) actors.get(i_135_);
				if (Actor.isValid(actor) && actor.pos != null
				    && ZutiSupportMethods
					   .isStaticActor(actor)) {
				    Point3d point3d = actor.pos.getAbsPoint();
				    double d_136_
					= (((point3d.x - (double) f_113_)
					    * (point3d.x - (double) f_113_))
					   + ((point3d.y - (double) f_114_)
					      * (point3d.y
						 - (double) f_114_)));
				    if (d_136_ <= d)
					actor.setArmy(bornplace.army);
				}
			    }
			}
			int i_137_
			    = sectfile.sectionIndex("BornPlace" + i_111_);
			if (i_137_ >= 0) {
			    int i_138_ = sectfile.vars(i_137_);
			    for (int i_139_ = 0; i_139_ < i_138_; i_139_++) {
				String string = sectfile.line(i_137_, i_139_);
				StringTokenizer stringtokenizer
				    = new StringTokenizer(string);
				ZutiAircraft zutiaircraft = new ZutiAircraft();
				String string_140_ = "";
				int i_141_ = 0;
				while (stringtokenizer.hasMoreTokens()) {
				    switch (i_141_) {
				    case 0:
					zutiaircraft.setAcName
					    (stringtokenizer.nextToken());
					break;
				    case 1:
					zutiaircraft.setMaxAllowed
					    (Integer.valueOf
						 (stringtokenizer.nextToken())
						 .intValue());
					break;
				    default:
					string_140_ += " " + stringtokenizer
								 .nextToken();
				    }
				    i_141_++;
				}
				zutiaircraft.setLoadedWeapons(string_140_,
							      false);
				String string_142_ = zutiaircraft.getAcName();
				if (string_142_ != null) {
				    string_142_ = string_142_.intern();
				    Class var_class
					= ((Class)
					   Property.value(string_142_,
							  "airClass", null));
				    if (var_class != null) {
					if (bornplace.zutiAircrafts == null)
					    bornplace.zutiAircrafts
						= new ArrayList();
					bornplace.zutiAircrafts
					    .add(zutiaircraft);
				    }
				}
			    }
			}
			bornplace.zutiFillAirNames();
			zutiLoadBornPlaceCountries(bornplace, sectfile,
						   i_111_);
		    }
		}
		try {
		    zutiAssignBpToMovingCarrier();
		} catch (Exception exception) {
		    System.out.println("Mission error, ID_15: "
				       + exception.toString());
		}
	    }
	}
    }
    
    private void loadTargets(SectFile sectfile) {
	if (Main.cur().netServerParams.isSingle()
	    || Main.cur().netServerParams.isCoop()
	    || Main.cur().netServerParams.isDogfight()) {
	    int i = sectfile.sectionIndex("Target");
	    if (i >= 0) {
		int i_143_ = sectfile.vars(i);
		for (int i_144_ = 0; i_144_ < i_143_; i_144_++)
		    Target.create(sectfile.line(i, i_144_));
	    }
	}
    }
    
	private void loadViewPoint(SectFile sectfile) {
		int i = sectfile.sectionIndex("StaticCamera");
		if(i < 0)
			return;
		int j = sectfile.vars(i);
		for(int k = 0; k < j; k++)
		{
			NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
			float f = numbertokenizer.next(0);
			float f1 = numbertokenizer.next(0);
			float f2 = numbertokenizer.next(100, 2, 10000);
			ActorViewPoint actorviewpoint = new ActorViewPoint();
			World.land();
			Point3d point3d = new Point3d(f, f1, f2 + Landscape.HQ_Air(f, f1));
			actorviewpoint.pos.setAbs(point3d);
			actorviewpoint.pos.reset();
			actorviewpoint.dreamFire(true);
			actorviewpoint.setName("StaticCamera_" + k);
			if(net.isMirror())
			{
				actorviewpoint.createNetObject(net.masterChannel(), ((Integer)(Integer)actors.get(curActor)).intValue());
				actors.set(curActor++, actorviewpoint);
			} else
			{
				actorviewpoint.createNetObject(null, 0);
				actors.add(actorviewpoint);
			}
		}
	}
    
    private void checkBridgesAndHouses(SectFile sectfile) {
	int i = sectfile.sections();
	for (int i_152_ = 0; i_152_ < i; i_152_++) {
	    String string = sectfile.sectionName(i_152_);
	    if (string.endsWith("_Way")) {
		int i_153_ = sectfile.vars(i_152_);
		for (int i_154_ = 0; i_154_ < i_153_; i_154_++) {
		    String string_155_ = sectfile.var(i_152_, i_154_);
		    if (string_155_.equals("GATTACK")) {
			SharedTokenizer.set(sectfile.value(i_152_, i_154_));
			SharedTokenizer.next((String) null);
			SharedTokenizer.next((String) null);
			SharedTokenizer.next((String) null);
			SharedTokenizer.next((String) null);
			String string_156_
			    = SharedTokenizer.next((String) null);
			if (string_156_ != null
			    && string_156_.startsWith("Bridge")) {
			    LongBridge longbridge
				= ((LongBridge)
				   Actor.getByName(" " + string_156_));
			    if (longbridge != null && !longbridge.isAlive())
				longbridge.BeLive();
			}
		    }
		}
	    } else if (string.endsWith("_Road")) {
		int i_157_ = sectfile.vars(i_152_);
		for (int i_158_ = 0; i_158_ < i_157_; i_158_++) {
		    SharedTokenizer.set(sectfile.value(i_152_, i_158_));
		    SharedTokenizer.next((String) null);
		    int i_159_ = (int) SharedTokenizer.next(1.0);
		    if (i_159_ < 0) {
			i_159_ = -i_159_ - 1;
			LongBridge longbridge = LongBridge.getByIdx(i_159_);
			if (longbridge != null && !longbridge.isAlive())
			    longbridge.BeLive();
		    }
		}
	    }
	}
	int i_160_ = sectfile.sectionIndex("Target");
	if (i_160_ >= 0) {
	    int i_161_ = sectfile.vars(i_160_);
	    for (int i_162_ = 0; i_162_ < i_161_; i_162_++) {
		SharedTokenizer.set(sectfile.line(i_160_, i_162_));
		int i_163_ = SharedTokenizer.next(0, 0, 7);
		if (i_163_ == 1 || i_163_ == 2 || i_163_ == 6 || i_163_ == 7) {
		    SharedTokenizer.next(0);
		    SharedTokenizer.next(0);
		    SharedTokenizer.next(0);
		    SharedTokenizer.next(0);
		    int i_164_ = SharedTokenizer.next(0);
		    int i_165_ = SharedTokenizer.next(0);
		    int i_166_ = SharedTokenizer.next(1000, 50, 3000);
		    String string = SharedTokenizer.next((String) null);
		    if (string != null && string.startsWith("Bridge"))
			string = " " + string;
		    switch (i_163_) {
		    case 1:
		    case 6:
			World.cur().statics.restoreAllHouses((float) i_164_,
							     (float) i_165_,
							     (float) i_166_);
			break;
		    case 2:
		    case 7:
			if (string != null) {
			    LongBridge longbridge
				= (LongBridge) Actor.getByName(string);
			    if (longbridge != null && !longbridge.isAlive())
				longbridge.BeLive();
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
	for (int i_168_ = 0; i_168_ < i; i_168_++) {
	    Actor actor = (Actor) arraylist.get(i_168_);
	    if (Actor.isValid(actor)) {
		try {
		    actor.missionStarting();
		} catch (Exception exception) {
		    System.out.println(exception.getMessage());
		    exception.printStackTrace();
		}
	    }
	}
    }
    
    public static void initRadioSounds() {
	if (!radioStationsLoaded) {
	    Aircraft aircraft = World.getPlayerAircraft();
	    if (aircraft != null) {
		ArrayList arraylist
		    = Main.cur().mission
			  .getBeacons(aircraft.FM.actor.getArmy());
		if (arraylist != null) {
		    for (int i = 0; i < arraylist.size(); i++) {
			Actor actor = (Actor) arraylist.get(i);
			if (actor instanceof TypeHasRadioStation) {
			    hasRadioStations = true;
			    aircraft.FM.AS.preLoadRadioStation(actor);
			    radioStationsLoaded = true;
			    CmdMusic.setCurrentVolume(0.0010F);
			    break;
			}
		    }
		}
	    }
	}
    }
    
    public void doBegin() {
	if (!bPlaying) {
	    if (Config.isUSE_RENDER()) {
		Main3D.cur3D().setDrawLand(true);
		if (World.cur().diffCur.Clouds) {
		    Main3D.cur3D().bDrawClouds = true;
		    if (RenderContext.cfgSky.get() == 0) {
			RenderContext.cfgSky.set(1);
			RenderContext.cfgSky.apply();
			RenderContext.cfgSky.reset();
		    }
		} else
		    Main3D.cur3D().bDrawClouds = false;
		CmdEnv.top().exec("fov 70");
		if (Main3D.cur3D().keyRecord != null) {
		    Main3D.cur3D().keyRecord.clearPrevStates();
		    Main3D.cur3D().keyRecord.clearRecorded();
		    Main3D.cur3D().keyRecord.stopRecording(false);
		    if (Main.cur().netServerParams.isSingle())
			Main3D.cur3D().keyRecord.startRecording();
		}
		NetMissionTrack.countRecorded = 0;
		if (Main3D.cur3D().guiManager != null) {
		    Main3D.cur3D().guiManager.setTimeGameActive(true);
		    GUIPad.bStartMission = true;
		}
		if (!Main.cur().netServerParams.isCoop())
		    doMissionStarting();
		if (Main.cur().netServerParams.isDogfight()) {
		    Time.setPause(false);
		    RTSConf.cur.time.setEnableChangePause1(false);
		}
		CmdEnv.top().exec("music PUSH");
		CmdEnv.top().exec("music STOP");
		if (!Main3D.cur3D().isDemoPlaying())
		    ForceFeedback.startMission();
		Joy.adapter().rePostMoves();
		viewSet = Main3D.cur3D().viewSet_Get();
		iconTypes = Main3D.cur3D().iconTypes();
	    } else {
		doMissionStarting();
		Time.setPause(false);
	    }
	    if (net.isMaster()) {
		sendCmd(10);
		doReplicateNotMissionActors(true);
	    }
	    if (Main.cur().netServerParams.isSingle()) {
		Main.cur().netServerParams.setExtraOcclusion(false);
		AudioDevice.soundsOn();
	    }
	    if (Main.cur().netServerParams.isMaster()
		&& (Main.cur().netServerParams.isCoop()
		    || Main.cur().netServerParams.isSingle()
		    || Main.cur().netServerParams.isDogfight()))
		World.cur().targetsGuard.activate();
	    EventLog.type(true, "Mission: " + name() + " is Playing");
	    EventLog.type("Mission BEGIN");
	    if (Main.cur().netServerParams != null)
		Main.cur().netServerParams.zutiResetServerTime();
	    bPlaying = true;
	    if (Main.cur().netServerParams != null)
		Main.cur().netServerParams.USGSupdate();
	}
    }
    
    public void doEnd()
    {
        if(!bPlaying)
            return;
        try
        {
            EventLog.type("Mission END");
            if(Config.isUSE_RENDER())
            {
                ForceFeedback.stopMission();
                if(Main3D.cur3D().guiManager != null)
                    Main3D.cur3D().guiManager.setTimeGameActive(false);
                NetMissionTrack.stopRecording();
                if(Main3D.cur3D().keyRecord != null)
                {
                    if(Main3D.cur3D().keyRecord.isPlaying())
                    {
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
            if(net.isMaster())
                sendCmd(20);
            AudioDevice.soundsOff();
            Voice.endSession();
            bPlaying = false;
            if(Main.cur().netServerParams != null)
                Main.cur().netServerParams.USGSupdate();
        }
        catch(Exception exception)
        {
            System.out.println("Mission error, ID_16: " + exception.toString());
        }
        return;
    }

    
    public NetObj netObj() {
	return net;
    }
    
    private void sendCmd(int i) {
	if (net.isMirrored()) {
	    try {
		List list = NetEnv.channels();
		int i_172_ = list.size();
		for (int i_173_ = 0; i_173_ < i_172_; i_173_++) {
		    NetChannel netchannel = (NetChannel) list.get(i_173_);
		    if (netchannel != net.masterChannel()
			&& netchannel.isReady() && netchannel.isMirrored(net)
			&& (netchannel.userState == 4
			    || netchannel.userState == 0)) {
			NetMsgGuaranted netmsgguaranted
			    = new NetMsgGuaranted();
			netmsgguaranted.writeByte(i);
			net.postTo(netchannel, netmsgguaranted);
		    }
		}
	    } catch (Exception exception) {
		printDebug(exception);
	    }
	}
    }
    
    private void doReplicateNotMissionActors(boolean bool) {
	if (net.isMirrored()) {
	    List list = NetEnv.channels();
	    int i = list.size();
	    for (int i_174_ = 0; i_174_ < i; i_174_++) {
		NetChannel netchannel = (NetChannel) list.get(i_174_);
		if (netchannel != net.masterChannel() && netchannel.isReady()
		    && netchannel.isMirrored(net)) {
		    if (bool) {
			if (netchannel.userState == 4)
			    doReplicateNotMissionActors(netchannel, true);
		    } else
			netchannel.userState = 1;
		}
	    }
	}
    }
    
    private void doReplicateNotMissionActors(NetChannel netchannel,
					     boolean bool) {
	if (bool) {
	    netchannel.userState = 0;
	    HashMapInt hashmapint = NetEnv.cur().objects;
	    for (HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null);
		 hashmapintentry != null;
		 hashmapintentry = hashmapint.nextEntry(hashmapintentry)) {
		NetObj netobj = (NetObj) hashmapintentry.getValue();
		if (netobj instanceof ActorNet
		    && !netchannel.isMirrored(netobj)) {
		    ActorNet actornet = (ActorNet) netobj;
		    if (Actor.isValid(actornet.actor())
			&& !actornet.actor().isSpawnFromMission())
			MsgNet.postRealNewChannel(netobj, netchannel);
		}
	    }
	} else
	    netchannel.userState = 1;
    }
    
    private void doResvMission(NetMsgInput netmsginput) {
	try {
	    while (netmsginput.available() > 0) {
		int i = netmsginput.readInt();
		if (i < 0) {
		    String string = netmsginput.read255();
		    sectFile.sectionAdd(string);
		} else
		    sectFile.lineAdd(i, netmsginput.read255(),
				     netmsginput.read255());
	    }
	} catch (Exception exception) {
	    printDebug(exception);
	    System.out.println("Bad format reseived missiion");
	}
    }
    
    private void doSendMission(NetChannel netchannel, int i) {
	try {
	    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
	    netmsgguaranted.writeByte(i);
	    int i_175_ = sectFile.sections();
	    for (int i_176_ = 0; i_176_ < i_175_; i_176_++) {
		String string = sectFile.sectionName(i_176_);
		if (!string.startsWith("$$$")) {
		    if (netmsgguaranted.size() >= 128) {
			net.postTo(netchannel, netmsgguaranted);
			netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(i);
		    }
		    netmsgguaranted.writeInt(-1);
		    netmsgguaranted.write255(string);
		    int i_177_ = sectFile.vars(i_176_);
		    for (int i_178_ = 0; i_178_ < i_177_; i_178_++) {
			if (netmsgguaranted.size() >= 128) {
			    net.postTo(netchannel, netmsgguaranted);
			    netmsgguaranted = new NetMsgGuaranted();
			    netmsgguaranted.writeByte(i);
			}
			netmsgguaranted.writeInt(i_176_);
			netmsgguaranted.write255(sectFile.var(i_176_, i_178_));
			netmsgguaranted.write255(sectFile.value(i_176_,
								i_178_));
		    }
		}
	    }
	    if (netmsgguaranted.size() > 1)
		net.postTo(netchannel, netmsgguaranted);
	    netmsgguaranted = new NetMsgGuaranted();
	    netmsgguaranted.writeByte(i + 1);
	    net.postTo(netchannel, netmsgguaranted);
	} catch (Exception exception) {
	    printDebug(exception);
	}
    }
    
    public void replicateTimeofDay() {
	if (isServer()) {
	    try {
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(11);
		netmsgguaranted.writeFloat(World.getTimeofDay());
		net.post(netmsgguaranted);
	    } catch (Exception exception) {
		/* empty */
	    }
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
	if (net instanceof Master
	    || netmsginput.channel() != net.masterChannel())
	    bool = true;
	boolean bool_179_ = netmsginput.channel() instanceof NetChannelStream;
	NetMsgGuaranted netmsgguaranted = null;
	int i = netmsginput.readUnsignedByte();
	switch (i) {
	case 0:
	    netmsginput.channel().userState = 2;
	    netmsgguaranted = new NetMsgGuaranted();
	    if (bool) {
		if (bool_179_) {
		    NetMsgGuaranted netmsgguaranted_180_
			= new NetMsgGuaranted();
		    netmsgguaranted_180_.writeByte(13);
		    netmsgguaranted_180_.writeLong(Time.current());
		    net.postTo(netmsginput.channel(), netmsgguaranted_180_);
		}
		netmsgguaranted.writeByte(0);
		netmsgguaranted.write255(name);
		netmsgguaranted.writeLong(sectFinger);
	    } else {
		name = netmsginput.read255();
		sectFinger = netmsginput.readLong();
		Main.cur().netMissionListener.netMissionState(0, 0.0F, name);
		if (!bool_179_)
		    ((NetUser) NetEnv.host()).setMissProp("missions/" + name);
		String string = "missions/" + name;
		if (!bool_179_ && isExistFile(string)) {
		    sectFile = new SectFile(string, 0, false);
		    if (sectFinger
			== sectFile.fingerExcludeSectPrefix("$$$")) {
			netmsgguaranted.writeByte(3);
			break;
		    }
		}
		string = "missions/Net/Cache/" + sectFinger + ".mis";
		int[] is = getSwTbl(string, sectFinger);
		sectFile = new SectFile(string, 0, false, is);
		if (!bool_179_
		    && sectFinger == sectFile.fingerExcludeSectPrefix("$$$"))
		    netmsgguaranted.writeByte(3);
		else {
		    sectFile = new SectFile(string, 1, false, is);
		    sectFile.clear();
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
	    if (bool)
		doSendMission(netmsginput.channel(), 1);
	    else {
		Main.cur().netMissionListener.netMissionState(1, 0.0F, null);
		doResvMission(netmsginput);
	    }
	    break;
	case 2:
	    if (!bool) {
		sectFile.saveFile();
		netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(3);
	    }
	    break;
	case 3:
	    if (bool) {
		int i_181_ = actors.size();
		int i_182_ = 0;
		while (i_182_ < i_181_) {
		    netmsgguaranted = new NetMsgGuaranted();
		    netmsgguaranted.writeByte(3);
		    int i_183_ = 64;
		    while (i_183_-- > 0 && i_182_ < i_181_) {
			Actor actor = (Actor) actors.get(i_182_++);
			if (Actor.isValid(actor))
			    netmsgguaranted.writeShort(actor.net.idLocal());
			else
			    netmsgguaranted.writeShort(0);
		    }
		    net.postTo(netmsginput.channel(), netmsgguaranted);
		}
		netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(4);
		netmsginput.channel().userState = 3;
	    } else {
		Main.cur().netMissionListener.netMissionState(2, 0.0F, null);
		while (netmsginput.available() > 0)
		    actors.add(new Integer(netmsginput.readUnsignedShort()));
	    }
	    break;
	case 4:
	    if (bool) {
		if (isDogfight()
		    || netmsginput.channel() instanceof NetChannelOutStream) {
		    World.cur().statics.netBridgeSync(netmsginput.channel());
		    World.cur().statics.netHouseSync(netmsginput.channel());
		}
		for (int i_184_ = 0; i_184_ < actors.size(); i_184_++) {
		    Actor actor = (Actor) actors.get(i_184_);
		    if (Actor.isValid(actor)) {
			try {
			    NetChannel netchannel = netmsginput.channel();
			    netchannel.setMirrored(actor.net);
			    actor.netFirstUpdate(netmsginput.channel());
			} catch (Exception exception) {
			    printDebug(exception);
			}
		    }
		}
		if (Actor.isValid(World.cur().houseManager))
		    World.cur().houseManager
			.fullUpdateChannel(netmsginput.channel());
		netmsgguaranted = new NetMsgGuaranted();
		if (isPlaying()) {
		    netmsgguaranted.writeByte(10);
		    net.postTo(netmsginput.channel(), netmsgguaranted);
		    netmsgguaranted = new NetMsgGuaranted();
		    netmsgguaranted.writeByte(11);
		    netmsgguaranted.writeFloat(World.getTimeofDay());
		    net.postTo(netmsginput.channel(), netmsgguaranted);
		    netmsgguaranted = null;
		    doReplicateNotMissionActors(netmsginput.channel(), true);
		    trySendMsgStart(netmsginput.channel());
		} else {
		    netmsgguaranted.writeByte(5);
		    netmsginput.channel().userState = 4;
		}
	    } else {
		netmsginput.channel().userState = 3;
		try {
		    load(name, sectFile, true);
		} catch (Exception exception) {
		    printDebug(exception);
		    Main.cur().netMissionListener
			.netMissionState(4, 0.0F, exception.getMessage());
		}
	    }
	    break;
	case 5:
	    if (bool) {
		/* empty */
	    }
	    break;
	case 10:
	    if (!(net instanceof Master)
		&& netmsginput.channel() == net.masterChannel()) {
		if (net.isMirrored()) {
		    netmsgguaranted = new NetMsgGuaranted();
		    netmsgguaranted.writeByte(10);
		    net.post(netmsgguaranted);
		    netmsgguaranted = null;
		}
		doReplicateNotMissionActors(true);
		doReplicateNotMissionActors(netmsginput.channel(), true);
		doBegin();
		Main.cur().netMissionListener.netMissionState(6, 0.0F, null);
	    }
	    break;
	case 11:
	    if (!(net instanceof Master)
		&& netmsginput.channel() == net.masterChannel()) {
		float f = netmsginput.readFloat();
		World.setTimeofDay(f);
		World.land().cubeFullUpdate();
	    }
	    break;
	case 20:
	    if (!(net instanceof Master)
		&& netmsginput.channel() == net.masterChannel()) {
		Main.cur().netMissionListener.netMissionState(7, 0.0F, null);
		doReplicateNotMissionActors(false);
		doReplicateNotMissionActors(netmsginput.channel(), false);
		doEnd();
		if (net.isMirrored()) {
		    netmsgguaranted = new NetMsgGuaranted();
		    netmsgguaranted.writeByte(20);
		    net.post(netmsgguaranted);
		    netmsgguaranted = null;
		}
	    }
	    break;
	case 12:
	    if (!(net instanceof Master)
		&& netmsginput.channel() == net.masterChannel())
		Main.cur().netMissionListener.netMissionState(9, 0.0F, null);
	    break;
	}
	if (netmsgguaranted != null && netmsgguaranted.size() > 0)
	    net.postTo(netmsginput.channel(), netmsgguaranted);
    }
    
    public void trySendMsgStart(Object object) {
	if (!isDestroyed()) {
	    NetChannel netchannel = (NetChannel) object;
	    if (!netchannel.isDestroyed()) {
		HashMapInt hashmapint = RTSConf.cur.netEnv.objects;
		HashMapIntEntry hashmapintentry = null;
		while ((hashmapintentry
			= hashmapint.nextEntry(hashmapintentry))
		       != null) {
		    NetObj netobj = (NetObj) hashmapintentry.getValue();
		    if (netobj != null && !netobj.isDestroyed()
			&& !netobj.isCommon() && !netchannel.isMirrored(netobj)
			&& netobj.masterChannel() != netchannel) {
			if ((!(netchannel instanceof NetChannelOutStream)
			     || (!(netobj instanceof NetControl)
				 && (!(netobj instanceof NetUser)
				     || !netobj.isMaster()
				     || !NetMissionTrack.isPlaying())))
			    && (!(netobj instanceof GameTrack)
				|| !netobj.isMirror())) {
			    Object object_185_ = netobj.superObj();
			    if (!(object_185_ instanceof Destroy)
				|| !((Destroy) object_185_).isDestroyed()) {
				new MsgInvokeMethod_Object
				    ("trySendMsgStart", netchannel)
				    .post(72, this, 0.0);
				return;
			    }
			}
		    }
		}
		try {
		    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		    netmsgguaranted.writeByte(12);
		    net.postTo(netchannel, netmsgguaranted);
		} catch (Exception exception) {
		    printDebug(exception);
		}
	    }
	}
    }
    
    private void createNetObject(NetChannel netchannel, int i) {
	setTime(true);
	if (netchannel == null) {
	    net = new Master(this);
	    doReplicateNotMissionActors(false);
	} else {
	    net = new Mirror(this, netchannel, i);
	    doReplicateNotMissionActors(netchannel, false);
	}
    }
    
    protected static void printDebug(Exception exception) {
	System.out.println(exception.getMessage());
	exception.printStackTrace();
    }
    
    private int[] getSwTbl(String string, long l) {
	int i = (int) l;
	int i_186_ = Finger.Int(string);
	if (i < 0)
	    i = -i;
	if (i_186_ < 0)
	    i_186_ = -i_186_;
	int i_187_ = (i_186_ + i / 7) % 16 + 15;
	int i_188_ = (i_186_ + i / 21) % Finger.kTable.length;
	if (i_187_ < 0)
	    i_187_ = -i_187_ % 16;
	if (i_187_ < 10)
	    i_187_ = 10;
	if (i_188_ < 0)
	    i_188_ = -i_188_ % Finger.kTable.length;
	int[] is = new int[i_187_];
	for (int i_189_ = 0; i_189_ < i_187_; i_189_++)
	    is[i_189_]
		= Finger.kTable[(i_188_ + i_189_) % Finger.kTable.length];
	return is;
    }
    
    public ArrayList getAllActors() {
	return actors;
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
		if (i_191_ != 74 && i_191_ != 81 && i_191_ != 89
		    && i_191_ <= 90) {
		    for (int i_192_ = 0;
			 i_192_ < is.length && i_191_ != is[i_192_];
			 i_192_++) {
			if (i_192_ == is.length - 1) {
			    bool = true;
			    is[i] = i_191_;
			}
		    }
		}
	    }
	}
	String string = new String(is);
	return string;
    }
    
    private void populateRunwayLights() {
	ArrayList arraylist = new ArrayList();
	World.getAirports(arraylist);
	for (int i = 0; i < arraylist.size(); i++) {
	    if (arraylist.get(i) instanceof AirportGround) {
		for (int i_193_ = 0; i_193_ < actors.size(); i_193_++) {
		    if (actors.get(i_193_) instanceof SmokeGeneric
			&& (actors.get(i_193_) instanceof Smoke.Smoke15
			    || actors.get(i_193_) instanceof Smoke.Smoke14
			    || actors.get(i_193_) instanceof Smoke.Smoke13
			    || actors.get(i_193_) instanceof Smoke.Smoke12)) {
			AirportGround airportground
			    = (AirportGround) arraylist.get(i);
			Actor actor = (Actor) actors.get(i_193_);
			double d = (airportground.pos.getAbsPoint().x
				    - actor.pos.getAbsPoint().x);
			double d_194_ = (airportground.pos.getAbsPoint().y
					 - actor.pos.getAbsPoint().y);
			if (Math.abs(d) < 2000.0 && Math.abs(d_194_) < 2000.0
			    && (actor.getArmy() == 1
				|| actor.getArmy() == 2)) {
			    SmokeGeneric smokegeneric = (SmokeGeneric) actor;
			    smokegeneric.setVisible(false);
			    airportground.addLights(smokegeneric);
			}
		    }
		}
	    }
	}
	for (int i = 0; i < actors.size(); i++) {
	    if (actors.get(i) instanceof SmokeGeneric)
		((SmokeGeneric) actors.get(i)).setArmy(0);
	}
    }
    
    private void populateBeacons() {
	ArrayList arraylist = new ArrayList();
	ArrayList arraylist_195_ = new ArrayList();
	for (int i = 0; i < actors.size(); i++) {
	    if (actors.get(i) instanceof TypeHasBeacon) {
		Point3d point3d = ((Actor) actors.get(i)).pos.getAbsPoint();
		arraylist.add(new Object[] { actors.get(i), point3d });
		if (actors.get(i) instanceof TypeHasLorenzBlindLanding)
		    ((Actor) actors.get(i)).missionStarting();
		if (actors.get(i) instanceof BigshipGeneric)
		    hayrakeMap.put((Actor) actors.get(i), "NDB");
	    } else if (actors.get(i) instanceof TypeHasMeacon) {
		Point3d point3d = ((Actor) actors.get(i)).pos.getAbsPoint();
		arraylist_195_.add(new Object[] { actors.get(i), point3d });
	    } else if (actors.get(i) instanceof TypeHasHayRake) {
		Point3d point3d = ((Actor) actors.get(i)).pos.getAbsPoint();
		String string = generateHayrakeCode(point3d);
		arraylist.add(new Object[] { actors.get(i), point3d });
		hayrakeMap.put((Actor) actors.get(i), string);
	    }
	}
	if (arraylist.size() != 0) {
	    sortBeaconsList(arraylist);
	    for (int i = 0; i < arraylist.size(); i++) {
		Object[] objects = (Object[]) arraylist.get(i);
		Actor actor = (Actor) objects[0];
		if ((actor instanceof TypeHasRadioStation
		     || actor.getArmy() == 1)
		    && beaconsRed.size() < 32)
		    beaconsRed.add(objects[0]);
		if ((actor instanceof TypeHasRadioStation
		     || actor.getArmy() == 2)
		    && beaconsBlue.size() < 32)
		    beaconsBlue.add(objects[0]);
	    }
	    for (int i = 0; i < arraylist_195_.size(); i++) {
		Object[] objects = (Object[]) arraylist_195_.get(i);
		Actor actor = (Actor) objects[0];
		if (actor.getArmy() == 1 && meaconsRed.size() < 32)
		    meaconsRed.add(objects[0]);
		else if (actor.getArmy() == 2 && meaconsBlue.size() < 32)
		    meaconsBlue.add(objects[0]);
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
		    String string_197_ = Property.stringValue(actor.getClass(),
							      "i18nName", "");
		    if (string_197_.equals("")) {
			try {
			    String string_198_
				= (actor.getClass().toString().substring
				   (actor.getClass().toString().indexOf("$")
				    + 1));
			    string_197_ = I18N.technic(string_198_);
			} catch (Exception exception) {
			    /* empty */
			}
		    }
		    int i_199_ = -1;
		    if (beaconsRed.contains(actor))
			i_199_ = beaconsRed.indexOf(actor);
		    else if (beaconsBlue.contains(actor))
			i_199_ = beaconsBlue.indexOf(actor);
		    if (string.equals("NDB"))
			Main3D.cur3D().ordersTree.addShipIDs(i, i_199_, actor,
							     string_197_, "");
		    else {
			boolean bool = (Aircraft.hasPlaneZBReceiver
					(World.getPlayerAircraft()));
			if (!bool)
			    continue;
			String string_200_ = string;
			if (string.length() == 12)
			    string_200_ = (string.substring(0, 3) + " / "
					   + string.substring(3, 6) + " / "
					   + string.substring(6, 9) + " / "
					   + string.substring(9, 12));
			else if (string.length() == 24)
			    string_200_ = (string.substring(0, 2) + "-"
					   + string.substring(2, 4) + "-"
					   + string.substring(4, 6) + " / "
					   + string.substring(6, 8) + "-"
					   + string.substring(8, 10) + "-"
					   + string.substring(10, 12) + " / "
					   + string.substring(12, 14) + "-"
					   + string.substring(14, 16) + "-"
					   + string.substring(16, 18) + " / "
					   + string.substring(18, 20) + "-"
					   + string.substring(20, 22) + "-"
					   + string.substring(22, 24));
			Main3D.cur3D().ordersTree.addShipIDs(i, i_199_, actor,
							     string_197_,
							     ("( "
							      + string_200_
							      + " )"));
		    }
		    i++;
		}
	    }
	}
    }
    
    private void sortBeaconsList(List list) {
	boolean bool = false;
	do {
	    for (int i = 0; i < list.size() - 1; i++) {
		bool = false;
		Object[] objects = (Object[]) list.get(i);
		Object[] objects_201_ = (Object[]) list.get(i + 1);
		if ((objects[0] instanceof TypeHasHayRake
		     && !(objects_201_[0] instanceof TypeHasHayRake))
		    || (objects[0] instanceof BigshipGeneric
			&& !(objects_201_[0] instanceof BigshipGeneric))) {
		    Object[] objects_202_ = objects;
		    list.set(i, objects_201_);
		    list.set(i + 1, objects_202_);
		    bool = true;
		}
	    }
	} while (bool);
    }
    
    public boolean hasBeacons(int i) {
	if (i == 1) {
	    if (beaconsRed.size() > 0)
		return true;
	    return false;
	}
	if (i == 2) {
	    if (beaconsBlue.size() > 0)
		return true;
	    return false;
	}
	return false;
    }
    
    public ArrayList getBeacons(int i) {
	if (i == 1)
	    return beaconsRed;
	if (i == 2)
	    return beaconsBlue;
	return null;
    }
    
    public ArrayList getMeacons(int i) {
	if (i == 1)
	    return meaconsBlue;
	if (i == 2)
	    return meaconsRed;
	return null;
    }
    
    public String getHayrakeCodeOfCarrier(Actor actor) {
	if (hayrakeMap.containsKey(actor))
	    return (String) hayrakeMap.get(actor);
	return null;
    }
    
    private void zutiAssignBpToMovingCarrier() {
	for (int i = 0; i < actors.size(); i++) {
	    Actor actor = (Actor) Main.cur().mission.actors.get(i);
	    if (actor instanceof BigshipGeneric
		&& actor.name().indexOf("_Chief") > -1
		&& (actor.toString()
			.indexOf(BigshipGeneric.ZUTI_CARRIER_STRING[0]) > -1
		    || (actor.toString()
			    .indexOf(BigshipGeneric.ZUTI_CARRIER_STRING[1])
			> -1))) {
		BigshipGeneric bigshipgeneric = (BigshipGeneric) actor;
		if (actor.icon != null
		    || Main.cur().netServerParams.isMaster())
		    bigshipgeneric.zutiAssignBornPlace();
	    }
	}
    }
    
    private void zutiResetMissionVariables() {
	if (ZutiSupportMethods.ZUTI_BANNED_PILOTS == null)
	    ZutiSupportMethods.ZUTI_BANNED_PILOTS = new ArrayList();
	ZutiSupportMethods.ZUTI_BANNED_PILOTS.clear();
	if (ZutiSupportMethods.ZUTI_DEAD_TARGETS == null)
	    ZutiSupportMethods.ZUTI_DEAD_TARGETS = new ArrayList();
	ZutiSupportMethods.ZUTI_DEAD_TARGETS.clear();
	if (GUI.pad != null)
	    GUI.pad.zutiColorAirfields = true;
	ZutiSupportMethods.ZUTI_KIA_COUNTER = 0;
	ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
	zutiCarrierSpawnPoints_CV2 = 6;
	zutiCarrierSpawnPoints_CV9 = 5;
	zutiCarrierSpawnPoints_CVE = 2;
	zutiCarrierSpawnPoints_CVL = 7;
	zutiCarrierSpawnPoints_Akagi = 8;
	zutiCarrierSpawnPoints_IJN = 6;
	zutiCarrierSpawnPoints_HMS = 5;
	zutiRadar_PlayerSideHasRadars = false;
	zutiRadar_ShipsAsRadar = false;
	zutiRadar_ShipRadar_MaxRange = 100;
	zutiRadar_ShipRadar_MinHeight = 100;
	zutiRadar_ShipRadar_MaxHeight = 5000;
	zutiRadar_ShipSmallRadar_MaxRange = 25;
	zutiRadar_ShipSmallRadar_MinHeight = 0;
	zutiRadar_ShipSmallRadar_MaxHeight = 2000;
	zutiRadar_ScoutsAsRadar = false;
	zutiRadar_ScoutRadar_MaxRange = 2;
	zutiRadar_ScoutRadar_DeltaHeight = 1500;
	zutiRadar_RefreshInterval = 0;
	zutiRadar_EnableTowerCommunications = true;
	zutiRadar_HideUnpopulatedAirstripsFromMinimap = false;
	ZUTI_RADAR_IN_ADV_MODE = false;
	zutiRadar_ScoutGroundObjects_Alpha = 5;
	ScoutsRed = new ArrayList();
	ScoutsBlue = new ArrayList();
	zutiRadar_ScoutCompleteRecon = false;
	zutiRadar_EnableBigShip_Radar = true;
	zutiRadar_EnableSmallShip_Radar = true;
	zutiMisc_DisableAIRadioChatter = false;
	zutiMisc_DespawnAIPlanesAfterLanding = true;
	zutiMisc_HidePlayersCountOnHomeBase = false;
	zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
	zutiMisc_DisableReflyForMissionDuration = false;
	zutiMisc_ReflyKIADelay = 0;
	zutiMisc_MaxAllowedKIA = 2147483647;
	zutiMisc_ReflyKIADelayMultiplier = 0.0F;
	if (Main.cur().netServerParams.reflyKIADelay > 0) {
	    zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
	    zutiMisc_ReflyKIADelay = Main.cur().netServerParams.reflyKIADelay;
	}
	if (Main.cur().netServerParams.reflyDisabled)
	    zutiMisc_DisableReflyForMissionDuration = true;
	else if (Main.cur().netServerParams.maxAllowedKIA >= 0) {
	    zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
	    zutiMisc_MaxAllowedKIA = Main.cur().netServerParams.maxAllowedKIA;
	}
	if (Main.cur().netServerParams.reflyKIADelayMultiplier > 0.0F
	    && zutiMisc_ReflyKIADelay != 0) {
	    zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
	    zutiMisc_ReflyKIADelayMultiplier
		= Main.cur().netServerParams.reflyKIADelayMultiplier;
	}
	zutiMisc_BombsCat1_CratersVisibilityMultiplier = 1.0F;
	zutiMisc_BombsCat2_CratersVisibilityMultiplier = 1.0F;
	zutiMisc_BombsCat3_CratersVisibilityMultiplier = 1.0F;
    }
    
    private void zutiLoadBornPlaceCountries(BornPlace bornplace,
					    SectFile sectfile, int i) {
	if (bornplace != null) {
	    if (bornplace != null && bornplace.zutiHomeBaseCountries == null)
		bornplace.zutiHomeBaseCountries = new ArrayList();
	    bornplace.zutiLoadAllCountries();
	    int i_203_ = sectfile.sectionIndex("BornPlaceCountries" + i);
	    if (i_203_ >= 0) {
		bornplace.zutiHomeBaseCountries.clear();
		ResourceBundle resourcebundle
		    = ResourceBundle.getBundle("i18n/country",
					       RTSConf.cur.locale,
					       LDRres.loader());
		int i_204_ = sectfile.vars(i_203_);
		for (int i_205_ = 0; i_205_ < i_204_; i_205_++) {
		    try {
			String string = sectfile.var(i_203_, i_205_);
			String string_206_ = resourcebundle.getString(string);
			if (!bornplace.zutiHomeBaseCountries
				 .contains(string_206_))
			    bornplace.zutiHomeBaseCountries.add(string_206_);
		    } catch (Exception exception) {
			/* empty */
		    }
		}
	    }
	}
    }
    
    private void zutiLoadScouts_Red(SectFile sectfile) {
	int i = sectfile.sectionIndex("MDS_Scouts_Red");
	if (i > -1) {
	    if (Main.cur().mission.ScoutsRed == null)
		Main.cur().mission.ScoutsRed = new ArrayList();
	    Main.cur().mission.ScoutsRed.clear();
	    int i_207_ = sectfile.vars(i);
	    for (int i_208_ = 0; i_208_ < i_207_; i_208_++) {
		String string = sectfile.line(i, i_208_);
		StringTokenizer stringtokenizer = new StringTokenizer(string);
		String string_209_ = null;
		if (stringtokenizer.hasMoreTokens())
		    string_209_ = stringtokenizer.nextToken();
		if (string_209_ != null) {
		    string_209_ = string_209_.intern();
		    Main.cur().mission.ScoutsRed.add(string_209_);
		}
	    }
	}
    }
    
    private void zutiLoadScouts_Blue(SectFile sectfile) {
	int i = sectfile.sectionIndex("MDS_Scouts_Blue");
	if (i > -1) {
	    if (Main.cur().mission.ScoutsBlue == null)
		Main.cur().mission.ScoutsBlue = new ArrayList();
	    Main.cur().mission.ScoutsBlue.clear();
	    int i_210_ = sectfile.vars(i);
	    for (int i_211_ = 0; i_211_ < i_210_; i_211_++) {
		String string = sectfile.line(i, i_211_);
		StringTokenizer stringtokenizer = new StringTokenizer(string);
		String string_212_ = null;
		if (stringtokenizer.hasMoreTokens())
		    string_212_ = stringtokenizer.nextToken();
		if (string_212_ != null) {
		    string_212_ = string_212_.intern();
		    Main.cur().mission.ScoutsBlue.add(string_212_);
		}
	    }
	}
    }
    
    private void zutiSetShipRadars() {
	if (zutiRadar_ShipRadar_MaxHeight == 0
	    && zutiRadar_ShipRadar_MaxRange == 0
	    && zutiRadar_ShipRadar_MinHeight == 0)
	    zutiRadar_EnableBigShip_Radar = false;
	if (zutiRadar_ShipSmallRadar_MaxHeight == 0
	    && zutiRadar_ShipSmallRadar_MaxRange == 0
	    && zutiRadar_ShipSmallRadar_MinHeight == 0)
	    zutiRadar_EnableSmallShip_Radar = false;
    }
    
    public static int getMissionDate(boolean bool) {
	int i;
	if (Main.cur().mission == null) {
	    SectFile sectfile = Main.cur().currentMissionFile;
	    if (sectfile == null)
		return 0;
	    String string = sectfile.get("MAIN", "MAP");
	    int i_214_ = World.land().config.getDefaultMonth("maps/" + string);
	    int i_215_ = sectfile.get("SEASON", "Year", 1940, 1930, 1960);
	    int i_216_ = sectfile.get("SEASON", "Month", i_214_, 1, 12);
	    int i_217_ = sectfile.get("SEASON", "Day", 15, 1, 31);
	    i = i_215_ * 10000 + i_216_ * 100 + i_217_;
	    int i_218_ = 19400000 + i_214_ * 100 + 15;
	    if (bool && i == i_218_)
		i = 0;
	} else {
	    int i_219_ = curYear();
	    int i_220_ = curMonth();
	    int i_221_ = curDay();
	    i = i_219_ * 10000 + i_220_ * 100 + i_221_;
	    if (bool) {
		SectFile sectfile = Main.cur().currentMissionFile;
		if (sectfile == null)
		    return 0;
		String string = sectfile.get("MAIN", "MAP");
		int i_222_
		    = World.land().config.getDefaultMonth("maps/" + string);
		int i_223_ = 19400000 + i_222_ * 100 + 15;
		if (i == i_223_)
		    i = 0;
	    }
	}
	return i;
    }
    
    public static float BigShipHpDiv() {
	if (Main.cur().mission == null)
	    return 1.0F;
	return Main.cur().mission.bigShipHpDiv;
    }
    
    /*synthetic*/ static Class class$(String string) {
	Class var_class;
	try {
	    var_class = Class.forName(string);
	} catch (ClassNotFoundException classnotfoundexception) {
	    throw new NoClassDefFoundError(classnotfoundexception
					       .getMessage());
	}
	return var_class;
    }
    
    static {
	Spawn.add((class$com$maddox$il2$game$Mission == null
		   ? (class$com$maddox$il2$game$Mission
		      = class$("com.maddox.il2.game.Mission"))
		   : class$com$maddox$il2$game$Mission),
		  new SPAWN());
	ZUTI_RADAR_IN_ADV_MODE = false;
	ZUTI_ICON_SIZES = new int[] { 8, 12, 16, 20, 24, 28, 32 };
	ZUTI_ICON_SIZE = ZUTI_ICON_SIZES[4];
    }
}
