//Modified AircraftState class for the SAS Engine Mod

//By western on 23rd/Jun./2018, expanded for 10x Engines aircrafts (from stock 6x), 8x fuel tank (from stock 4x)
//By western on 29th/Jun./2018, debug expanded codes, recover static int astateEffectsDispXXXX values in each used lines.

package com.maddox.il2.fm;

import java.io.File;
import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.air.AR_234B2;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.AircraftLH;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.DO_335;
import com.maddox.il2.objects.air.DO_335A0;
import com.maddox.il2.objects.air.DO_335V13;
import com.maddox.il2.objects.air.FW_190A8MSTL;
import com.maddox.il2.objects.air.GO_229;
import com.maddox.il2.objects.air.HE_162;
import com.maddox.il2.objects.air.JU_88NEW;
import com.maddox.il2.objects.air.ME_262HGII;
import com.maddox.il2.objects.air.P_39;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.Scheme0;
import com.maddox.il2.objects.air.Scheme1;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDockable;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeFuelDump;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.Beacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasAAFIAS;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.il2.objects.vehicles.radios.TypeHasILSBlindLanding;
import com.maddox.il2.objects.vehicles.radios.TypeHasLorenzBlindLanding;
import com.maddox.il2.objects.vehicles.radios.TypeHasRadioStation;
import com.maddox.il2.objects.vehicles.radios.TypeHasTACAN;
import com.maddox.il2.objects.vehicles.radios.TypeHasYGBeacon;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.Fuze_EL_AZ;
import com.maddox.il2.objects.weapons.MGunAircraftGeneric;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.CmdMusic;

import com.maddox.sas1946.il2.util.TrueRandom;


public class AircraftState {
	// TODO: Default Parameters
	// -------------------------------------------------------------
	public static final boolean __DEBUG_SPREAD__ = false;
	private static final Loc astateCondensateDispVector = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
	public static final int _AS_RESERVED = 0;
	public static final int _AS_ENGINE_STATE = 1;
	public static final int _AS_ENGINE_SPECIFIC_DMG = 2;
	public static final int _AS_ENGINE_EXPLODES = 3;
	public static final int _AS_ENGINE_STARTS = 4;
	public static final int _AS_ENGINE_RUNS = 5;
	public static final int _AS_ENGINE_STOPS = 6;
	public static final int _AS_ENGINE_DIES = 7;
	public static final int _AS_ENGINE_SOOT_POWERS = 8;
	public static final int _AS_ENGINE_READYNESS = 25;
	public static final int _AS_ENGINE_STAGE = 26;
	public static final int _AS_ENGINE_CYL_KNOCKOUT = 27;
	public static final int _AS_ENGINE_MAG_KNOCKOUT = 28;
	public static final int _AS_ENGINE_STUCK = 29;
	public static final int _AS_TANK_STATE = 9;
	public static final int _AS_TANK_EXPLODES = 10;
	public static final int _AS_OIL_STATE = 11;
	public static final int _AS_GLIDER_BOOSTER = 12;
	public static final int _AS_GLIDER_BOOSTOFF = 13;
	public static final int _AS_GLIDER_CUTCART = 14;
	public static final int _AS_AIRSHOW_SMOKES_STATE = 15;
	public static final int _AS_WINGTIP_SMOKES_STATE = 16;
	public static final int _AS_PILOT_STATE = 17;
	public static final int _AS_KILLPILOT = 18;
	public static final int _AS_HEADSHOT = 19;
	public static final int _AS_BAILOUT = 20;
	public static final int _AS_CONTROLS_HURT = 21;
	public static final int _AS_INTERNALS_HURT = 22;
	public static final int _AS_COCKPIT_STATE_BYTE = 23;
	public static final int _AS_JAM_BULLETS = 24;
	public static final int _AS_NAVIGATION_LIGHTS_STATE = 30;
	public static final int _AS_LANDING_LIGHT_STATE = 31;
	public static final int _AS_TYPEDOCKABLE_REQ_ATTACHTODRONE = 32;
	public static final int _AS_TYPEDOCKABLE_REQ_DETACHFROMDRONE = 33;
	public static final int _AS_TYPEDOCKABLE_FORCE_ATTACHTODRONE = 34;
	public static final int _AS_TYPEDOCKABLE_FORCE_DETACHFROMDRONE = 35;
	public static final int _AS_FLATTOP_FORCESTRING = 36;
	public static final int _AS_FMSFX = 37;
	public static final int _AS_WINGFOLD = 38;
	public static final int _AS_COCKPITDOOR = 39;
	public static final int _AS_ARRESTOR = 40;
	public static final int _AS_BEACONS = 41;
	public static final int _AS_GYROANGLE = 42;
	public static final int _AS_SPREADANGLE = 43;
	public static final int _AS_PILOT_WOUNDED = 44;
	public static final int _AS_PILOT_BLEEDING = 45;
	public static final int _AS_FUZE_STATES = 46;
	public static final int _AS_GEAR_STATE = 47;
	public static final int _AS_COUNT_CODES = 48;
	public static final int _AS_COCKPIT_GLASS = 1;
	public static final int _AS_COCKPIT_ARMORGLASS = 2;
	public static final int _AS_COCKPIT_LEFT1 = 4;
	public static final int _AS_COCKPIT_LEFT2 = 8;
	public static final int _AS_COCKPIT_RIGHT1 = 16;
	public static final int _AS_COCKPIT_RIGHT2 = 32;
	public static final int _AS_COCKPIT_INSTRUMENTS = 64;
	public static final int _AS_COCKPIT_OIL = 128;
	public static final int _ENGINE_SPECIFIC_BOOSTER = 0;
	public static final int _ENGINE_SPECIFIC_THROTTLECTRL = 1;
	public static final int _ENGINE_SPECIFIC_HEATER = 2;
	public static final int _ENGINE_SPECIFIC_ANGLER = 3;
	public static final int _ENGINE_SPECIFIC_ANGLERSPEEDS = 4;
	public static final int _ENGINE_SPECIFIC_EXTINGUISHER = 5;
	public static final int _ENGINE_SPECIFIC_PROPCTRL = 6;
	public static final int _ENGINE_SPECIFIC_MIXCTRL = 7;
	public static final int _CONTROLS_AILERONS = 0;
	public static final int _CONTROLS_ELEVATORS = 1;
	public static final int _CONTROLS_RUDDERS = 2;
	public static final int _INTERNALS_HYDRO_OFFLINE = 0;
	public static final int _INTERNALS_PNEUMO_OFFLINE = 1;
	public static final int _INTERNALS_MW50_OFFLINE = 2;
	public static final int _INTERNALS_GEAR_STUCK = 3;
	public static final int _INTERNALS_KEEL_SHUTOFF = 4;
	public static final int _INTERNALS_SHAFT_SHUTOFF = 5;
	public static final int LEFT_GEAR = 0;
	public static final int RIGHT_GEAR = 1;
	public static final int CENTER_GEAR = 2;
	public static final int GEAR_DAMAGE_NONE = 0;
	public static final int GEAR_DAMAGE_DOWN_BUT_NOT_LOCKED = 1;
	public static final int GEAR_DAMAGE_STUCK_AT_UP = 2;
	public static final int GEAR_DAMAGE_STUCK = 3;
	public static final int GEAR_DAMAGE_SLOW_EXTEND_FULLY_DOWN = 4;
	public static final int GEAR_DAMAGE_FAST_SHORT_EXTEND = 5;
	public static final int GEAR_DAMAGE_EXTEND = 6;
	public static final int GEAR_DAMAGE_STRUCTURAL_WEAKNESS = 7;
	public static final int GEAR_DAMAGE_NO_BRAKES = 8;
	public static final int GEAR_DAMAGE_ACTUATOR_BROKEN = 9;
	public static final int GEAR_DAMAGE_ACTUATOR_DAMAGED = 10;
	public static final int GEAR_DAMAGE_JAM_PERM = 11;
	public static final int GEAR_DAMAGE_RANDOM = 12;
	protected long bleedingTime;
	public long astateBleedingTimes[];
	public long astateBleedingNext[];
	private boolean legsWounded;
	private boolean armsWounded;
	public int torpedoGyroAngle;
	public int torpedoSpreadAngle;
	public static final int spreadAngleLimit = 30;
	public float gearStates[] = { 0.0F, 0.0F, 0.0F };
	public byte gearDamType[] = { 0, 0, 0 };
	public byte gearDamRecoveryStates[] = { 0, 0, 0 };
	private int beacon;
	private boolean bWantBeaconsNet;
	public static final int MAX_NUMBER_OF_BEACONS = 32;
	public boolean listenLorenzBlindLanding;
	public boolean isAAFIAS;
	public boolean isILSBL;
	public boolean listenYGBeacon;
	public boolean listenNDBeacon;
	public boolean listenRadioStation;
	public boolean listenTACAN;
	public Actor hayrakeCarrier;
	public String hayrakeCode;
	public static int hudLogBeaconId = HUD.makeIdLog();
	public boolean externalStoresDropped;
	public int armingSeed;
	public RangeRandom armingRnd;
	private static final String astateOilStrings[] = { "3DO/Effects/Aircraft/OilBlackMediumSPD.eff", "3DO/Effects/Aircraft/OilBlackMediumTSPD.eff", null, null };
	private static final String astateTankStrings[] = { null, null, null, "3DO/Effects/Aircraft/RedLeakTSPD.eff", null, null, "3DO/Effects/Aircraft/RedLeakTSPD.eff", null, null, "3DO/Effects/Aircraft/TankBlackMediumSPD.eff",
			"3DO/Effects/Aircraft/TankBlackMediumTSPD.eff", null, "3DO/Effects/Aircraft/BlackHeavySPD.eff", "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", null, "3DO/Effects/Aircraft/FireSPD.eff", "3DO/Effects/Aircraft/BlackHeavySPD.eff",
			"3DO/Effects/Aircraft/BlackHeavyTSPD.eff", "3DO/Effects/Aircraft/FireSPDLong.eff", "3DO/Effects/Aircraft/BlackHeavySPD.eff", "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", null, null, null, "3DO/Effects/Aircraft/RedLeakGND.eff", null, null,
			"3DO/Effects/Aircraft/RedLeakGND.eff", null, null, "3DO/Effects/Aircraft/BlackMediumGND.eff", null, null, "3DO/Effects/Aircraft/BlackHeavyGND.eff", null, null, "3DO/Effects/Aircraft/FireGND.eff", "3DO/Effects/Aircraft/BlackHeavyGND.eff",
			null, "3DO/Effects/Aircraft/FireGND.eff", "3DO/Effects/Aircraft/BlackHeavyGND.eff", null };
	private static final String astateEngineStrings[] = { null, null, null, "3DO/Effects/Aircraft/GraySmallSPD.eff", "3DO/Effects/Aircraft/GraySmallTSPD.eff", null, "3DO/Effects/Aircraft/EngineBlackMediumSPD.eff",
			"3DO/Effects/Aircraft/EngineBlackMediumTSPD.eff", null, "3DO/Effects/Aircraft/BlackHeavySPD.eff", "3DO/Effects/Aircraft/EngineBlackHeavyTSPD.eff", null, "3DO/Effects/Aircraft/FireSPD.eff", "3DO/Effects/Aircraft/BlackHeavySPD.eff",
			"3DO/Effects/Aircraft/EngineBlackHeavyTSPD.eff", null, null, null, "3DO/Effects/Aircraft/GraySmallGND.eff", null, null, "3DO/Effects/Aircraft/BlackMediumGND.eff", null, null, "3DO/Effects/Aircraft/BlackHeavyGND.eff", null, null,
			"3DO/Effects/Aircraft/FireGND.eff", "3DO/Effects/Aircraft/BlackHeavyGND.eff", null };
	private static final String astateCondensateStrings[] = { null, "3DO/Effects/Aircraft/CondensateTSPD.eff" };
	private static final String astateStallStrings[] = { null, "3DO/Effects/Aircraft/StallTSPD.eff" };
	public static final String astateHUDPilotHits[] = { "Player", "Pilot", "CPilot", "NGunner", "TGunner", "WGunner", "VGunner", "RGunner", "EngMas", "BombMas", "RadMas", "ObsMas" };
	private static boolean bCriticalStatePassed = false;
	private boolean bIsAboveCriticalSpeed;
	private boolean bIsAboveCondensateAlt;
	private boolean bIsOnInadequateAOA;
	public boolean bShowSmokesOn;
	public boolean bNavLightsOn;
	public boolean bLandingLightOn;
	public boolean bWingTipLExists;
	public boolean bWingTipRExists;
	private boolean bIsMaster;
	public Actor actor;
	public AircraftLH aircraft;
	public byte astatePilotStates[];
	public byte astatePilotFunctions[] = { 1, 7, 7, 7, 7, 7, 7, 7, 7 };
	public int astatePlayerIndex;
	public boolean bIsAboutToBailout;
	public boolean bIsEnableToBailout;
	public byte astateBailoutStep;
	public int astateCockpitState;
	// By western, expanded astateOilStates[], astateOilEffects[][], astateEngineStates[], astateEngineEffects[][], astateSootStates[], astateSootEffects[][], astateCondensateEffects[], astateEngineBurnLights[] from stock 6 to modded 10.
	// By western, expanded astateTankStates[], astateTankEffects[][], astateTankBurnLights[] from stock 4 to modded 8.
	// By western, expanded astateEffectChunks[] +6 to enable both above changes.
	public byte astateOilStates[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private Eff3DActor astateOilEffects[][] = { { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null } };
	public byte astateTankStates[] = { 0, 0, 0, 0, 0, 0, 0, 0 };
	private Eff3DActor astateTankEffects[][] = { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null } };
	public byte astateEngineStates[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private Eff3DActor astateEngineEffects[][] = { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null } };
	public byte astateSootStates[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	public Eff3DActor astateSootEffects[][] = { { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null } };
	public Eff3DActor astateCondensateEffects[] = { null, null, null, null, null, null, null, null, null, null };
	private Eff3DActor astateStallEffects[] = { null, null };
	private Eff3DActor astateAirShowEffects[] = { null, null };
	private Eff3DActor astateNavLightsEffects[] = { null, null, null, null, null, null };
	private LightPointActor astateNavLightsLights[] = { null, null, null, null, null, null };
	private LightPointActor astateTankBurnLights[] = { null, null, null, null, null, null, null, null };
	private LightPointActor astateEngineBurnLights[] = { null, null, null, null, null, null, null, null, null, null };
	public Eff3DActor astateLandingLightEffects[] = { null, null, null, null };
	private LightPointActor astateLandingLightLights[] = { null, null, null, null };
	public String astateEffectChunks[] = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null , null, null, null, null, null, null };
	public static final int astateEffectsDispTanks = 0;
	public static final int astateEffectsDispEngines = 8;
	public static final int astateEffectsDispLights = 18;
	public static final int astateEffectsDispLandingLights = 24;
	public static final int astateEffectsDispOilfilters = 28;
	public static boolean bCheckPlayerAircraft = true;
	private Item itemsToMaster[];
	private Item itemsToMirrors[];
	// -------------------------------------------------------------

	// TODO: New Parameters
	// -------------------------------------------------------------
	private Eff3DActor astateDumpFuelEffects[] = { null, null, null };
	public int iAirShowSmoke;
	private boolean bAirShowSmokeEnhanced;
	private boolean bDumpFuel;
	private int COCKPIT_DOOR;
	private int SIDE_DOOR;
	// TODO: ++ Added Code for Net Replication ++
	// The following 3 net codes were in use before but undocumented
	public static final int _AS_AIRSHOW_SMOKE = 48;
	public static final int _AS_AIRSHOW_SMOKE_ENHANCED = 49;
	public static final int _AS_DUMP_FUEL = 50;
	// The following 3 net codes are new
	public static final int _AS_SELECTED_ROCKET_HOOK = 51;
	public static final int _AS_WEAPON_FIRE_MODE = 52;
	public static final int _AS_WEAPON_RELEASE_DELAY = 53;
	// The following 1 net codes is importing 4.13.2m
	public static final int _AS_BOMB_MODE_STATES = 54;
	// The following 1 net codes is new by western0221
	public static final int _AS_CURRENT_BOMB_SELECTED = 55;

	// TODO: -- Added Code for Net Replication --
	// -------------------------------------------------------------
	static class Item {

		void set(int i, int j, Actor actor1) {
			msgDestination = i;
			msgContext = j;
			initiator = actor1;
		}

		boolean equals(int i, int j, Actor actor1) {
			return msgDestination == i && msgContext == j && initiator == actor1;
		}

		int msgDestination;
		int msgContext;
		Actor initiator;

		Item() {
		}
	}

	public AircraftState() {
		bleedingTime = 0L;
		astateBleedingTimes = new long[9];
		astateBleedingNext = new long[9];
		legsWounded = false;
		armsWounded = false;
		torpedoGyroAngle = 0;
		torpedoSpreadAngle = 0;
		beacon = 0;
		bWantBeaconsNet = false;
		listenLorenzBlindLanding = false;
		isAAFIAS = false;
		isILSBL = false;
		listenYGBeacon = false;
		listenNDBeacon = false;
		listenRadioStation = false;
		listenTACAN = false;
		hayrakeCarrier = null;
		hayrakeCode = null;
		externalStoresDropped = false;
		armingSeed = TrueRandom.nextInt(0, 65535);
		bIsAboveCriticalSpeed = false;
		bIsAboveCondensateAlt = false;
		bIsOnInadequateAOA = false;
		bShowSmokesOn = false;
		bNavLightsOn = false;
		bLandingLightOn = false;
		bWingTipLExists = true;
		bWingTipRExists = true;
		actor = null;
		aircraft = null;
		astatePilotStates = new byte[9];
		astatePlayerIndex = 0;
		bIsAboutToBailout = false;
		bIsEnableToBailout = true;
		astateBailoutStep = 0;
		astateCockpitState = 0;
		itemsToMaster = null;
		itemsToMirrors = null;
		// TODO: New Parameters
		iAirShowSmoke = 0;
		bAirShowSmokeEnhanced = false;
		iAirShowSmoke = Config.cur.ini.get("Mods", "AirShowSmoke", 0);
		if (iAirShowSmoke < 1 || iAirShowSmoke > 3) iAirShowSmoke = 0;
		if (Config.cur.ini.get("Mods", "AirShowSmokeEnhanced", 0) > 0) bAirShowSmokeEnhanced = true;
		bDumpFuel = false;
		COCKPIT_DOOR = 1;
		SIDE_DOOR = 2;
		// TODO: ++ Added Code for Net Replication ++
		// apparently due to a mistake this array had a size of "2" before which caused a "NetAircraft error, ID_03:" error on each aircraft spawn.
		astateAirShowEffects = new Eff3DActor[3];
		// TODO: ++ Added Code for Net Replication --
		astateDumpFuelEffects = new Eff3DActor[3];
	}

	public void set(Actor actor1, boolean flag) {
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		Loc loc1 = new Loc();
		actor = actor1;
		if (actor1 instanceof Aircraft) aircraft = (AircraftLH) actor1;
		else throw new RuntimeException("Can not cast aircraft structure into a non-aircraft entity.");
		bIsMaster = flag;
		for (int i = 0; i < 8; i++)
			try {
				astateEffectChunks[i + astateEffectsDispTanks] = actor.findHook("_Tank" + (i + 1) + "Burn").chunkName();
				astateEffectChunks[i + astateEffectsDispTanks] = astateEffectChunks[i + astateEffectsDispTanks].substring(0, astateEffectChunks[i + astateEffectsDispTanks].length() - 1);
				Aircraft.debugprintln(aircraft, "AS: Tank " + i + " FX attached to '" + astateEffectChunks[i + astateEffectsDispTanks] + "' substring..");
			} catch (Exception exception) {
			} finally {
			}

		for (int j = 0; j < aircraft.FM.EI.getNum(); j++)
			try {
				astateEffectChunks[j + astateEffectsDispEngines] = actor.findHook("_Engine" + (j + 1) + "Smoke").chunkName();
				astateEffectChunks[j + astateEffectsDispEngines] = astateEffectChunks[j + astateEffectsDispEngines].substring(0, astateEffectChunks[j + astateEffectsDispEngines].length() - 1);
				Aircraft.debugprintln(aircraft, "AS: Engine " + j + " FX attached to '" + astateEffectChunks[j + astateEffectsDispEngines] + "' substring..");
			} catch (Exception exception2) {
			} finally {
			}

		for (int k = 0; k < astateNavLightsEffects.length; k++)
			try {
				astateEffectChunks[k + astateEffectsDispLights] = actor.findHook("_NavLight" + k).chunkName();
				astateEffectChunks[k + astateEffectsDispLights] = astateEffectChunks[k + astateEffectsDispLights].substring(0, astateEffectChunks[k + astateEffectsDispLights].length() - 1);
				Aircraft.debugprintln(aircraft, "AS: Nav. Lamp #" + k + " attached to '" + astateEffectChunks[k + astateEffectsDispLights] + "' substring..");
				HookNamed hooknamed = new HookNamed(aircraft, "_NavLight" + k);
				loc1.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				hooknamed.computePos(actor, loc, loc1);
				Point3d point3d = loc1.getPoint();
				astateNavLightsLights[k] = new LightPointActor(new LightPoint(), point3d);
				if (k < 2) astateNavLightsLights[k].light.setColor(1.0F, 0.1F, 0.1F);
				else if (k < 4) astateNavLightsLights[k].light.setColor(0.0F, 1.0F, 0.0F);
				else astateNavLightsLights[k].light.setColor(0.7F, 0.7F, 0.7F);
				astateNavLightsLights[k].light.setEmit(0.0F, 0.0F);
				actor.draw.lightMap().put("_NavLight" + k, astateNavLightsLights[k]);
			} catch (Exception exception3) {
			} finally {
			}

		for (int l = 0; l < 4; l++)
			try {
				astateEffectChunks[l + astateEffectsDispLandingLights] = actor.findHook("_LandingLight0" + l).chunkName();
				astateEffectChunks[l + astateEffectsDispLandingLights] = astateEffectChunks[l + astateEffectsDispLandingLights].substring(0, astateEffectChunks[l + astateEffectsDispLandingLights].length() - 1);
				Aircraft.debugprintln(aircraft, "AS: Landing Lamp #" + l + " attached to '" + astateEffectChunks[l + astateEffectsDispLandingLights] + "' substring..");
				HookNamed hooknamed1 = new HookNamed(aircraft, "_LandingLight0" + l);
				loc1.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				hooknamed1.computePos(actor, loc, loc1);
				Point3d point3d1 = loc1.getPoint();
				astateLandingLightLights[l] = new LightPointActor(new LightPoint(), point3d1);
				astateLandingLightLights[l].light.setColor(0.4941176F, 0.9098039F, 0.9607843F);
				astateLandingLightLights[l].light.setEmit(0.0F, 0.0F);
				actor.draw.lightMap().put("_LandingLight0" + l, astateLandingLightLights[l]);
			} catch (Exception exception5) {
			} finally {
			}

		for (int i1 = 0; i1 < aircraft.FM.EI.getNum(); i1++)
			try {
				astateEffectChunks[i1 + astateEffectsDispOilfilters] = actor.findHook("_Engine" + (i1 + 1) + "Oil").chunkName();
				astateEffectChunks[i1 + astateEffectsDispOilfilters] = astateEffectChunks[i1 + astateEffectsDispOilfilters].substring(0, astateEffectChunks[i1 + astateEffectsDispOilfilters].length() - 1);
				Aircraft.debugprintln(aircraft, "AS: Oilfilter " + i1 + " FX attached to '" + astateEffectChunks[i1 + astateEffectsDispOilfilters] + "' substring..");
			} catch (Exception exception6) {
			} finally {
			}

		for (int j1 = 0; j1 < astateEffectChunks.length; j1++)
			if (astateEffectChunks[j1] == null) astateEffectChunks[j1] = "AChunkNameYouCanNeverFind";

	}

	public boolean isMaster() {
		return bIsMaster;
	}

	public void setOilState(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (j < 0 || j > 1 || astateOilStates[i] == j) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetOilState(i, j);
			int k = 0;
			for (int l = 0; l < aircraft.FM.EI.getNum(); l++)
				if (astateOilStates[l] == 1) k++;

			if (k == aircraft.FM.EI.getNum()) setCockpitState(actor, astateCockpitState | 0x80);
			netToMirrors(11, i, j);
		} else {
			netToMaster(11, i, j, actor1);
		}
	}

	public void hitOil(Actor actor1, int i) {
		if (astateOilStates[i] > 0) return;
		if (astateOilStates[i] < 1) setOilState(actor1, i, astateOilStates[i] + 1);
	}

	public void repairOil(int i) {
		if (!bIsMaster) return;
		if (astateOilStates[i] > 0) setOilState(actor, i, astateOilStates[i] - 1);
	}

	private void doSetOilState(int i, int j) {
		if (astateOilStates[i] == j) return;
		Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[i + astateEffectsDispOilfilters] + "' visibility..");
		boolean flag = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispOilfilters]);
		Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[i + astateEffectsDispOilfilters] + "' is " + (flag ? "visible" : "invisible") + "..");
		Aircraft.debugprintln(aircraft, "Stating OilFilter " + i + " to state " + j + (flag ? ".." : " rejected (missing part).."));
		if (!flag) return;
		Aircraft.debugprintln(aircraft, "Stating OilFilter " + i + " to state " + j + "..");
		astateOilStates[i] = (byte) j;
		byte byte0 = 0;
		if (!bIsAboveCriticalSpeed) byte0 = 2;
		if (astateOilEffects[i][0] != null) Eff3DActor.finish(astateOilEffects[i][0]);
		astateOilEffects[i][0] = null;
		if (astateOilEffects[i][1] != null) Eff3DActor.finish(astateOilEffects[i][1]);
		astateOilEffects[i][1] = null;
		switch (astateOilStates[i]) {
		case 1: // '\001'
			String s = astateOilStrings[byte0];
			if (s != null) try {
				astateOilEffects[i][0] = Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Oil"), null, 1.0F, s, -1F);
			} catch (Exception exception) {
			}
			s = astateOilStrings[byte0 + 1];
			if (s != null) try {
				astateOilEffects[i][1] = Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Oil"), null, 1.0F, s, -1F);
			} catch (Exception exception1) {
			}
			if (TrueRandom.nextFloat() < 0.25F) aircraft.FM.setReadyToReturn(true);
			break;
		}
	}

	public void changeOilEffectBase(int i, Actor actor1) {
		if (actor1 != null) {
			for (int j = 0; j < 2; j++)
				if (astateOilEffects[i][j] != null) astateOilEffects[i][j].pos.changeBase(actor1, null, true);

		}
	}

	public void setTankState(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (j < 0 || j > 6) return;
		if (bIsMaster) {
			byte byte0 = astateTankStates[i];
			if (!doSetTankState(actor1, i, j)) return;
			for (int k = byte0; k < j; k++) {
				if (k % 2 == 0) aircraft.setDamager(actor1);
				doHitTank(actor1, i);
			}

			if (aircraft.FM.isPlayers() && actor1 != actor && (actor1 instanceof Aircraft) && ((Aircraft) actor1).isNetPlayer() && j > 5 && astateTankStates[0] < 5 && astateTankStates[1] < 5 && astateTankStates[2] < 5 && astateTankStates[3] < 5
					&& !aircraft.FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_lightfuel", (Aircraft) actor1, aircraft);
			netToMirrors(9, i, j);
		} else {
			netToMaster(9, i, j, actor1);
		}
	}

	public void hitTank(Actor actor1, int i, int j) {
		if (astateTankStates[i] == 6) return;
		int k = astateTankStates[i] + j;
		if (k > 6) k = 6;
		setTankState(actor1, i, k);
	}

	public void repairTank(int i) {
		if (!bIsMaster) return;
		if (astateTankStates[i] > 0) setTankState(actor, i, astateTankStates[i] - 1);
	}

	public boolean doSetTankState(Actor actor1, int i, int j) {
		boolean flag = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispTanks]);
		Aircraft.debugprintln(aircraft, "Stating Tank " + i + " to state " + j + (flag ? ".." : " rejected (missing part).."));
		if (!flag) return false;
		if (World.getPlayerAircraft() == actor) {
			if (astateTankStates[i] == 0 && (j == 1 || j == 2)) HUD.log("FailedTank");
			if (astateTankStates[i] < 5 && j >= 5) HUD.log("FailedTankOnFire");
		}
		byte byte1 = astateTankStates[i];
		astateTankStates[i] = (byte) j;
		boolean flag1 = false;
		boolean flag2 = false;
		if (j == 5 && byte1 < 4) flag1 = true;
		if (j == 4 && byte1 == 4) flag2 = true;
		if (j == 4 && byte1 < 3) {
			flag2 = true;
			flag1 = true;
		}
		if (astateTankStates[i] < 5 && j >= 5) {
			aircraft.FM.setTakenMortalDamage(true, actor1);
			aircraft.FM.setCapableOfACM(false);
		}
		if (j < 4 && aircraft.FM.isCapableOfBMP()) aircraft.FM.setTakenMortalDamage(false, actor1);
		byte byte0 = 0;
		if (!bIsAboveCriticalSpeed) byte0 = 21;
		for (int k = 0; k < 3; k++) {
			if (astateTankEffects[i][k] != null) Eff3DActor.finish(astateTankEffects[i][k]);
			astateTankEffects[i][k] = null;
			String s = astateTankStrings[byte0 + k + j * 3];
			if (flag2 && s == null) s = "3DO/Effects/Aircraft/FireSPDShort.eff";
			if (s == null) continue;
			if (j > 2) {
				boolean flag3 = false;
				Hook hook1 = actor.findHook("_Tank" + (i + 1) + "Burn");
				String s1 = hook1.chunkName();
				if (s1.toLowerCase().startsWith("wing")) {
					flag3 = true;
					if (aircraft.hierMesh().isChunkVisible(s1) && j == 5) flag1 = true;
				}
				if (s.equals("3DO/Effects/Aircraft/FireSPD.eff")) {
					if (flag3) s = "3DO/Effects/Aircraft/FireSPDWing.eff";
				} else if (s.equals("3DO/Effects/Aircraft/FireSPDLong.eff")) {
					if (flag3) s = "3DO/Effects/Aircraft/FireSPDWingLong.eff";
				} else if (s.equals("3DO/Effects/Aircraft/BlackHeavySPD.eff")) {
					if (flag1) s = null;
					else if (flag3) s = "3DO/Effects/Aircraft/BlackHeavySPDWing.eff";
				} else if (s.equals("3DO/Effects/Aircraft/BlackHeavyTSPD.eff") && flag1 && !flag2) s = null;
				if (s != null) astateTankEffects[i][k] = Eff3DActor.New(actor, hook1, null, 1.0F, s, -1F);
			} else {
				astateTankEffects[i][k] = Eff3DActor.New(actor, actor.findHook("_Tank" + (i + 1) + "Leak"), null, 1.0F, s, -1F);
			}
			if (j > 4) {
				Hook hook = actor.findHook("_Tank" + (i + 1) + "Burn");
				Point3d point3d = aircraft.getTankBurnLightPoint(i, hook);
				boolean flag4 = true;
				for (int l = 0; l < astateTankBurnLights.length; l++) {
					if (astateTankBurnLights[l] == null) continue;
					Point3d point3d1 = astateTankBurnLights[l].relPos;
					double d = point3d.distance(point3d1);
					if (d >= 1.0D) continue;
					flag4 = false;
					break;
				}

				if (flag4) {
					for (int i1 = 0; i1 < astateEngineBurnLights.length; i1++) {
						if (astateEngineBurnLights[i1] == null) continue;
						Point3d point3d2 = astateEngineBurnLights[i1].relPos;
						double d1 = point3d.distance(point3d2);
						if (d1 >= 1.0D) continue;
						flag4 = false;
						break;
					}

				}
				if (flag4) {
					astateTankBurnLights[i] = new LightPointActor(new LightPoint(), point3d);
					astateTankBurnLights[i].light.setColor(1.0F, 0.9F, 0.5F);
					astateTankBurnLights[i].light.setEmit(5F, 5F);
					actor.draw.lightMap().put("_TankBurnLight" + i, astateTankBurnLights[i]);
				}
				continue;
			}
			if (astateTankBurnLights[i] != null) {
				actor.draw.lightMap().remove("_TankBurnLight" + i);
				astateTankBurnLights[i].destroy();
				astateTankBurnLights[i] = null;
			}
		}

		aircraft.sfxSmokeState(2, i, j > 4);
		return true;
	}

	private void doHitTank(Actor actor1, int i) {
		if ((TrueRandom.nextInt(0, 99) < 75 || (actor instanceof Scheme1)) && astateTankStates[i] == 6) {
			aircraft.FM.setReadyToDie(true);
			aircraft.FM.setTakenMortalDamage(true, actor1);
			aircraft.FM.setCapableOfACM(false);
			Voice.speakMayday(aircraft);
			Aircraft.debugprintln(aircraft, "I'm on fire, going down!.");
			Explosions.generateComicBulb(actor, "OnFire", 12F);
			if (TrueRandom.nextInt(0, 99) < 75 + aircraft.FM.subSkill) {
				Aircraft.debugprintln(aircraft, "BAILING OUT - Tank " + i + " is on fire!.");
				hitDaSilk();
			}
		} else if (TrueRandom.nextInt(0, 99) < 12) {
			aircraft.FM.setReadyToReturn(true);
			Aircraft.debugprintln(aircraft, "Tank " + i + " hit, RTB..");
			Explosions.generateComicBulb(actor, "RTB", 12F);
		}
	}

	public void changeTankEffectBase(int i, Actor actor1) {
		if (actor1 != null) {
			for (int j = 0; j < 3; j++)
				if (astateTankEffects[i][j] != null) astateTankEffects[i][j].pos.changeBase(actor1, null, true);

		}
		if (astateTankBurnLights[i] != null) {
			actor.draw.lightMap().remove("_TankBurnLight" + i);
			astateTankBurnLights[i].destroy();
			astateTankBurnLights[i] = null;
		}
		aircraft.sfxSmokeState(2, i, false);
	}

	public void explodeTank(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			if (!aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispTanks])) return;
			netToMirrors(10, i, 0);
			doExplodeTank(i);
		} else {
			netToMaster(10, i, 0, actor1);
		}
	}

	private void doExplodeTank(int i) {
		Aircraft.debugprintln(aircraft, "Tank " + i + " explodes..");
		Eff3DActor.New(actor, actor.findHook("_Tank" + (i + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
		Eff3DActor.New(actor, actor.findHook("_Tank" + (i + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1F);
		Eff3DActor.New(actor, actor.findHook("_Tank" + (i + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1F);
		Eff3DActor.New(actor, actor.findHook("_Tank" + (i + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1F);
		aircraft.msgCollision(actor, astateEffectChunks[i + astateEffectsDispTanks] + "0", astateEffectChunks[i + astateEffectsDispTanks] + "0");
		if ((actor instanceof Scheme1) && aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispTanks])) aircraft.msgCollision(actor, "CF_D0", "CF_D0");
		HookNamed hooknamed = new HookNamed((ActorMesh) actor, "_Tank" + (i + 1) + "Burn");
		Loc loc = new Loc();
		Loc loc1 = new Loc();
		actor.pos.getCurrent(loc);
		hooknamed.computePos(actor, loc, loc1);
		if (World.getPlayerAircraft() == actor) HUD.log("FailedTankExplodes");
	}

	public void setEngineState(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (j < 0 || j > 4 || astateEngineStates[i] == j) return;
		if (bIsMaster) {
			byte byte0 = astateEngineStates[i];
			if (!doSetEngineState(actor1, i, j)) return;
			for (int k = byte0; k < j; k++) {
				aircraft.setDamager(actor1);
				doHitEngine(actor1, i);
			}

			if (aircraft.FM.isPlayers() && actor1 != actor && (actor1 instanceof Aircraft) && ((Aircraft) actor1).isNetPlayer() && j > 3 && astateEngineStates[0] < 3 && astateEngineStates[1] < 3 && astateEngineStates[2] < 3 && astateEngineStates[3] < 3
					&& !aircraft.FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_lighteng", (Aircraft) actor1, aircraft);
			int l = 0;
			for (int i1 = 0; i1 < aircraft.FM.EI.getNum(); i1++)
				if (astateEngineStates[i1] > 2) l++;

			if (l == aircraft.FM.EI.getNum()) setCockpitState(actor, astateCockpitState | 0x80);
			netToMirrors(1, i, j);
		} else {
			netToMaster(1, i, j, actor1);
		}
	}

	public void hitEngine(Actor actor1, int i, int j) {
		if (astateEngineStates[i] == 4) return;
		int k = astateEngineStates[i] + j;
		if (k > 4) k = 4;
		setEngineState(actor1, i, k);
	}

	public void repairEngine(int i) {
		if (!bIsMaster) return;
		if (astateEngineStates[i] > 0) setEngineState(actor, i, astateEngineStates[i] - 1);
	}

	public boolean doSetEngineState(Actor actor1, int i, int j) {
		Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[i + astateEffectsDispEngines] + "' visibility..");
		boolean flag = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispEngines]);
		Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[i + astateEffectsDispEngines] + "' is " + (flag ? "visible" : "invisible") + "..");
		Aircraft.debugprintln(aircraft, "Stating Engine " + i + " to state " + j + (flag ? ".." : " rejected (missing part).."));
		if (!flag) return false;
		if (astateEngineStates[i] < 4 && j >= 4) {
			if (World.getPlayerAircraft() == actor) HUD.log("FailedEngineOnFire");
			if (aircraft.isDestroyed() || aircraft.FM.Gears.isUnderDeck() || aircraft.FM.Gears.getWheelsOnGround() || aircraft.FM.Gears.onGround()) {
				aircraft.FM.setTakenMortalDamage(true, actor1);
				aircraft.FM.setCapableOfACM(false);
			}
			if (aircraft.FM.EI.getNum() < 4) aircraft.FM.setCapableOfACM(false);
			aircraft.FM.setCapableOfTaxiing(false);
		}
		astateEngineStates[i] = (byte) j;
		if (j < 2 && aircraft.FM.isCapableOfBMP()) aircraft.FM.setTakenMortalDamage(false, actor1);
		byte byte0 = 0;
		if (!bIsAboveCriticalSpeed) byte0 = 15;
		for (int k = 0; k < 3; k++) {
			if (astateEngineEffects[i][k] != null) Eff3DActor.finish(astateEngineEffects[i][k]);
			astateEngineEffects[i][k] = null;
			String s = astateEngineStrings[byte0 + k + j * 3];
			if (s == null) continue;
			Hook hook = actor.findHook("_Engine" + (i + 1) + "Smoke");
			astateEngineEffects[i][k] = Eff3DActor.New(actor, hook, null, 1.0F, s, -1F);
			if (j > 3) {
				Loc loc = aircraft.getEngineBurnLightLoc(i);
				Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				hook.computePos(actor, loc, loc1);
				Point3d point3d = loc1.getPoint();
				boolean flag1 = true;
				for (int l = 0; l < astateTankBurnLights.length; l++) {
					if (astateTankBurnLights[l] == null) continue;
					Point3d point3d1 = astateTankBurnLights[l].relPos;
					double d = point3d.distance(point3d1);
					if (d >= 1.0D) continue;
					flag1 = false;
					break;
				}

				if (flag1) {
					for (int i1 = 0; i1 < astateEngineBurnLights.length; i1++) {
						if (astateEngineBurnLights[i1] == null) continue;
						Point3d point3d2 = astateEngineBurnLights[i1].relPos;
						double d1 = point3d.distance(point3d2);
						if (d1 >= 1.0D) continue;
						flag1 = false;
						break;
					}

				}
				if (flag1) {
					astateEngineBurnLights[i] = new LightPointActor(new LightPoint(), point3d);
					astateEngineBurnLights[i].light.setColor(1.0F, 0.9F, 0.5F);
					astateEngineBurnLights[i].light.setEmit(5F, 5F);
					actor.draw.lightMap().put("_EngineBurnLight" + i, astateEngineBurnLights[i]);
				}
				continue;
			}
			if (astateEngineBurnLights[i] != null) {
				actor.draw.lightMap().remove("_EngineBurnLight" + i);
				astateEngineBurnLights[i].destroy();
				astateEngineBurnLights[i] = null;
			}
		}

		aircraft.sfxSmokeState(1, i, j > 3);
		return true;
	}

	private void doHitEngine(Actor actor1, int i) {
		float f = 0.0F;
		int j = aircraft.FM.EI.getNum();
		for (int k = 0; k < j; k++)
			f += astateEngineStates[k];

		f = (f / (float) j) * 25F;
		if (astateEngineStates[i] == 3) {
			if (TrueRandom.nextFloat(0.0F, 100F - f) < 15F - (float) aircraft.FM.subSkill * 3F) {
				aircraft.FM.setReadyToReturn(true);
				Aircraft.debugprintln(aircraft, "Engines out, RTB..");
				Explosions.generateComicBulb(actor, "RTB", 12F);
			}
		} else if (astateEngineStates[i] == 4) {
			if (TrueRandom.nextFloat(0.0F, 100F - f) < 30F - (float) aircraft.FM.subSkill * 3F) {
				aircraft.FM.setReadyToReturn(true);
				Aircraft.debugprintln(aircraft, "Engines out, RTB..");
				Explosions.generateComicBulb(actor, "RTB", 12F);
			}
			if (actor instanceof Scheme1) {
				if (TrueRandom.nextInt(0, 99) < 50 - aircraft.FM.subSkill * 3) {
					Aircraft.debugprintln(aircraft, "BAILING OUT - Engine " + i + " is on fire..");
					aircraft.hitDaSilk();
				}
				aircraft.FM.setReadyToDie(true);
				aircraft.FM.setTakenMortalDamage(true, actor1);
				aircraft.FM.setCapableOfACM(false);
			} else if (TrueRandom.nextFloat(0.0F, 100F - f) < 50F - (float) aircraft.FM.subSkill * 3F) {
				aircraft.FM.setReadyToDie(true);
				aircraft.FM.setTakenMortalDamage(true, actor1);
				aircraft.FM.setCapableOfACM(false);
				Aircraft.debugprintln(aircraft, "Engines on fire, ditching..");
				Explosions.generateComicBulb(actor, "OnFire", 12F);
			}
		}
	}

	public void changeEngineEffectBase(int i, Actor actor1) {
		if (actor1 != null) {
			for (int j = 0; j < 3; j++)
				if (astateEngineEffects[i][j] != null) astateEngineEffects[i][j].pos.changeBase(actor1, null, true);

		}
		if (astateEngineBurnLights[i] != null) {
			actor.draw.lightMap().remove("_EngineBurnLight" + i);
			astateEngineBurnLights[i].destroy();
			astateEngineBurnLights[i] = null;
		}
		aircraft.sfxSmokeState(1, i, false);
		astateEngineStates[i] = 0;
	}

	public void explodeEngine(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			if (!aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispEngines])) return;
			netToMirrors(3, i, 0);
			doExplodeEngine(i);
		} else {
			netToMaster(3, i, 0, actor1);
		}
	}

	private void doExplodeEngine(int i) {
		Aircraft.debugprintln(aircraft, "Engine " + i + " explodes..");
		Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
		Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1F);
		Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1F);
		Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1F);
		aircraft.msgCollision(aircraft, astateEffectChunks[i + astateEffectsDispEngines] + "0", astateEffectChunks[i + astateEffectsDispEngines] + "0");
		Actor actor1;
		if (aircraft.getDamager() != null) actor1 = aircraft.getDamager();
		else actor1 = actor;
		HookNamed hooknamed = new HookNamed((ActorMesh) actor, "_Engine" + (i + 1) + "Smoke");
		Loc loc = new Loc();
		Loc loc1 = new Loc();
		actor.pos.getCurrent(loc);
		hooknamed.computePos(actor, loc, loc1);
		if (aircraft.FM.EI.engines[i].getType() != 1 && aircraft.FM.EI.engines[i].getType() != 0) MsgExplosion.send(null, astateEffectChunks[i + astateEffectsDispEngines] + "0", loc1.getPoint(), actor1, 1.248F, 0.026F, 1, 75F);
	}

	public void setEngineStarts(int i) {
		if (!bIsMaster) {
			return;
		} else {
			doSetEngineStarts(i);
			netToMirrors(4, i, 96);
			return;
		}
	}

	public void setEngineRunning(int i) {
		if (!bIsMaster) {
			return;
		} else {
			doSetEngineRunning(i);
			netToMirrors(5, i, 81);
			return;
		}
	}

	public void setEngineStops(int i) {
		if (!bIsMaster) {
			return;
		} else {
			doSetEngineStops(i);
			netToMirrors(6, i, 2);
			return;
		}
	}

	public void setEngineDies(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetEngineDies(i);
			netToMirrors(7, i, 77);
		} else {
			netToMaster(7, i, 67, actor1);
		}
	}

	public void setEngineStuck(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			doSetEngineStuck(i);
			aircraft.setDamager(actor1);
			netToMirrors(29, i, 77);
		} else {
			netToMaster(29, i, 67, actor1);
		}
	}

	public void setEngineSpecificDamage(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetEngineSpecificDamage(i, j);
			netToMirrors(2, i, j);
		} else {
			netToMaster(2, i, j, actor1);
		}
	}

	public void setEngineReadyness(Actor actor1, int i, int j) {
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetEngineReadyness(i, j);
			netToMirrors(25, i, j);
		} else {
			netToMaster(25, i, j, actor1);
		}
	}

	public void setEngineStage(Actor actor1, int i, int j) {
		if (bIsMaster) {
			doSetEngineStage(i, j);
			netToMirrors(26, i, j);
		} else {
			netToMaster(26, i, j, actor1);
		}
	}

	public void setEngineCylinderKnockOut(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetEngineCylinderKnockOut(i, j);
			netToMirrors(27, i, j);
		} else {
			netToMaster(27, i, j, actor1);
		}
	}

	public void setEngineMagnetoKnockOut(Actor actor1, int i, int j) {
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetEngineMagnetoKnockOut(i, j);
			netToMirrors(28, i, j);
		} else {
			netToMaster(28, i, j, actor1);
		}
	}

	private void doSetEngineStarts(int i) {
		aircraft.FM.EI.engines[i].doSetEngineStarts();
	}

	private void doSetEngineRunning(int i) {
		aircraft.FM.EI.engines[i].doSetEngineRunning();
	}

	private void doSetEngineStops(int i) {
		aircraft.FM.EI.engines[i].doSetEngineStops();
	}

	private void doSetEngineDies(int i) {
		aircraft.FM.EI.engines[i].doSetEngineDies();
	}

	private void doSetEngineStuck(int i) {
		aircraft.FM.EI.engines[i].doSetEngineStuck();
	}

	private void doSetEngineSpecificDamage(int i, int j) {
		switch (j) {
		case 0: // '\0'
			aircraft.FM.EI.engines[i].doSetKillCompressor();
			break;

		case 3: // '\003'
			aircraft.FM.EI.engines[i].doSetKillPropAngleDevice();
			break;

		case 4: // '\004'
			aircraft.FM.EI.engines[i].doSetKillPropAngleDeviceSpeeds();
			break;

		case 5: // '\005'
			aircraft.FM.EI.engines[i].doSetExtinguisherFire();
			break;

		case 2: // '\002'
			throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented feature (E.S.D./H.)");

		case 1: // '\001'
			if (aircraft.FM.EI.engines[i].isHasControlThrottle()) aircraft.FM.EI.engines[i].doSetKillControlThrottle();
			break;

		case 6: // '\006'
			if (aircraft.FM.EI.engines[i].isHasControlProp()) aircraft.FM.EI.engines[i].doSetKillControlProp();
			break;

		case 7: // '\007'
			if (aircraft.FM.EI.engines[i].isHasControlMix()) aircraft.FM.EI.engines[i].doSetKillControlMix();
			break;

		default:
			throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in (E.S.D./null)");
		}
	}

	private void doSetEngineReadyness(int i, int j) {
		aircraft.FM.EI.engines[i].doSetReadyness(0.01F * (float) j);
	}

	private void doSetEngineStage(int i, int j) {
		aircraft.FM.EI.engines[i].doSetStage(j);
	}

	private void doSetEngineCylinderKnockOut(int i, int j) {
		aircraft.FM.EI.engines[i].doSetCyliderKnockOut(j);
	}

	private void doSetEngineMagnetoKnockOut(int i, int j) {
		aircraft.FM.EI.engines[i].doSetMagnetoKnockOut(j);
	}

	public void doSetEngineExtinguisherVisuals(int i) {
		Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[i + astateEffectsDispEngines] + "' visibility..");
		boolean flag = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispEngines]);
		Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[i + astateEffectsDispEngines] + "' is " + (flag ? "visible" : "invisible") + "..");
		Aircraft.debugprintln(aircraft, "Firing Extinguisher on Engine " + i + (flag ? ".." : " rejected (missing part).."));
		if (!flag) {
			return;
		} else {
			Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Aircraft/EngineExtinguisher1.eff", 3F);
			return;
		}
	}

	public void setSootState(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (astateSootStates[i] == j) return;
		if (bIsMaster) {
			if (!doSetSootState(i, j)) return;
			netToMirrors(8, i, j);
		} else {
			netToMaster(8, i, j, actor1);
		}
	}

	public boolean doSetSootState(int i, int j) {
		Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[i + astateEffectsDispEngines] + "' visibility..");
		boolean flag = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + astateEffectsDispEngines]);
		Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[i + astateEffectsDispEngines] + "' is " + (flag ? "visible" : "invisible") + "..");
		Aircraft.debugprintln(aircraft, "Stating Engine " + i + " to state " + j + (flag ? ".." : " rejected (missing part).."));
		if (!flag) {
			return false;
		} else {
			astateSootStates[i] = (byte) j;
			aircraft.doSetSootState(i, j);
			return true;
		}
	}

	public void changeSootEffectBase(int i, Actor actor1) {
		if (actor1 != null) {
			for (int j = 0; j < 2; j++)
				if (astateSootEffects[i][j] != null) astateSootEffects[i][j].pos.changeBase(actor1, null, true);

		}
	}

	public void setCockpitState(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (astateCockpitState == i) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetCockpitState(i);
			netToMirrors(23, 0, i);
		} else {
			netToMaster(23, 0, i, actor1);
		}
	}

	public void doSetCockpitState(int i) {
		if (astateCockpitState == i) {
			return;
		} else {
			astateCockpitState = i;
			aircraft.setCockpitState(i);
			return;
		}
	}

	public void setControlsDamage(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			if (aircraft.FM.isPlayers() && actor1 != actor && (actor1 instanceof Aircraft) && ((Aircraft) actor1).isNetPlayer() && !aircraft.FM.isSentControlsOutNote() && !aircraft.FM.isSentBuryNote()) {
				Chat.sendLogRnd(3, "gore_hitctrls", (Aircraft) actor1, aircraft);
				aircraft.FM.setSentControlsOutNote(true);
			}
			doSetControlsDamage(i, actor1);
		} else {
			netToMaster(21, 0, i, actor1);
		}
	}

	public void setInternalDamage(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetInternalDamage(i);
			netToMirrors(22, 0, i);
		} else {
			netToMaster(22, 0, i, actor1);
		}
	}

	public void setGliderBoostOn() {
		if (!bIsMaster) {
			return;
		} else {
			netToMirrors(12, 5, 5);
			return;
		}
	}

	public void setGliderBoostOff() {
		if (!bIsMaster) {
			return;
		} else {
			netToMirrors(13, 7, 7);
			return;
		}
	}

	public void setGliderCutCart() {
		if (!bIsMaster) {
			return;
		} else {
			netToMirrors(14, 9, 9);
			return;
		}
	}

	private void doSetControlsDamage(int i, Actor actor1) {
		switch (i) {
		case 0: // '\0'
			aircraft.FM.CT.resetControl(0);
			if (aircraft.FM.isPlayers() && aircraft.FM.CT.bHasAileronControl) HUD.log("FailedAroneAU");
			aircraft.FM.CT.bHasAileronControl = false;
			aircraft.FM.setCapableOfACM(false);
			break;

		case 1: // '\001'
			aircraft.FM.CT.resetControl(1);
			if (aircraft.FM.isPlayers() && aircraft.FM.CT.bHasElevatorControl) HUD.log("FailedVatorAU");
			aircraft.FM.CT.bHasElevatorControl = false;
			aircraft.FM.setCapableOfACM(false);
			if (Math.abs(aircraft.FM.Vwld.z) > 7D) aircraft.FM.setCapableOfBMP(false, actor1);
			break;

		case 2: // '\002'
			aircraft.FM.CT.resetControl(2);
			if (aircraft.FM.isPlayers() && aircraft.FM.CT.bHasRudderControl) HUD.log("FailedRudderAU");
			aircraft.FM.CT.bHasRudderControl = false;
			break;

		default:
			throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in (C.A.D./null)");
		}
	}

	private void doSetInternalDamage(int i) {
		switch (i) {
		case 0: // '\0'
			aircraft.FM.Gears.setHydroOperable(false);
			break;

		case 1: // '\001'
			aircraft.FM.CT.bHasFlapsControl = false;
			break;

		case 2: // '\002'
			aircraft.FM.M.nitro = 0.0F;
			break;

		case 3: // '\003'
			aircraft.FM.Gears.setHydroOperable(false);
			aircraft.FM.Gears.setOperable(false);
			break;

		case 4: // '\004'
			if (aircraft instanceof DO_335A0) {
				((DO_335A0) aircraft).doKeelShutoff();
				break;
			}
			if (aircraft instanceof DO_335V13) ((DO_335V13) aircraft).doKeelShutoff();
			else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (I.K.S.)");
			break;

		case 5: // '\005'
			aircraft.FM.EI.engines[1].setPropReductorValue(0.007F);
			break;

		default:
			throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in (I.D./null)");
		}
	}

	private void doSetGliderBoostOn() {
		if (actor instanceof Scheme0) ((Scheme0) actor).doFireBoosters();
		else if (actor instanceof AR_234B2) ((AR_234B2) actor).doFireBoosters();
		else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.B./On not in Nul)");
	}

	private void doSetGliderBoostOff() {
		if (actor instanceof Scheme0) ((Scheme0) actor).doCutBoosters();
		else if (actor instanceof AR_234B2) ((AR_234B2) actor).doCutBoosters();
		else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.B./Off not in Nul)");
	}

	private void doSetGliderCutCart() {
		if (actor instanceof Scheme0) ((Scheme0) actor).doCutCart();
		else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.C.C./Off not in Nul)");
	}

	private void doSetCondensateState(boolean flag) {
		for (int i = 0; i < aircraft.FM.EI.getNum(); i++) {
			if (astateCondensateEffects[i] != null) Eff3DActor.finish(astateCondensateEffects[i]);
			astateCondensateEffects[i] = null;
			if (!flag) continue;
			String s = astateCondensateStrings[1];
			if (s == null) continue;
			try {
				astateCondensateEffects[i] = Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), astateCondensateDispVector, 1.0F, s, -1F);
			} catch (Exception exception) {
				Aircraft.debugprintln(aircraft, "Above condensate failed - probably a glider..");
			}
		}

	}

	public void setStallState(boolean flag) {
		if (bIsMaster) {
			doSetStallState(flag);
			netToMirrors(16, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetStallState(boolean flag) {
		for (int i = 0; i < 2; i++)
			if (astateStallEffects[i] != null) Eff3DActor.finish(astateStallEffects[i]);

		if (flag) {
			String s = astateStallStrings[1];
			if (s != null) {
				if (bWingTipLExists) astateStallEffects[0] = Eff3DActor.New(actor, actor.findHook("_WingTipL"), null, 1.0F, s, -1F);
				if (bWingTipRExists) astateStallEffects[1] = Eff3DActor.New(actor, actor.findHook("_WingTipR"), null, 1.0F, s, -1F);
			}
		}
	}

	// TODO: Following methods used for airshow smoke
	public void setAirShowSmokeType(int i) {
		iAirShowSmoke = i;
	}

	public void setAirShowSmokeEnhanced(boolean flag) {
		bAirShowSmokeEnhanced = flag;
	}

	public void setAirShowState(boolean flag) {
		if (bShowSmokesOn == flag) return;
		if (bIsMaster) {
			if (iAirShowSmoke != 0) iAirShowSmoke++;
			if (iAirShowSmoke > 3) iAirShowSmoke = 1;
			doSetAirShowState(flag);
			netToMirrors(42, iAirShowSmoke, iAirShowSmoke);
			netToMirrors(43, bAirShowSmokeEnhanced ? 1 : 0, bAirShowSmokeEnhanced ? 1 : 0);
			netToMirrors(15, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetAirShowState(boolean flag) {
		bShowSmokesOn = flag;
		for (int i = 0; i < 3; i++)
			if (astateAirShowEffects[i] != null) Eff3DActor.finish(astateAirShowEffects[i]);
		if (flag) {
			Hook hook = null;
			try {
				hook = actor.findHook("_Engine1ES_01");
			} catch (Exception exception) {
				hook = null;
			}
			if (iAirShowSmoke < 1 || iAirShowSmoke > 3 || hook == null) {
				if (bWingTipLExists) astateAirShowEffects[0] = Eff3DActor.New(actor, actor.findHook("_WingTipL"), null, 1.0F, "3DO/Effects/Aircraft/AirShowRedTSPD.eff", -1F);
				if (bWingTipRExists) astateAirShowEffects[1] = Eff3DActor.New(actor, actor.findHook("_WingTipR"), null, 1.0F, "3DO/Effects/Aircraft/AirShowGreenTSPD.eff", -1F);
			} else if (iAirShowSmoke == 1) {
				astateAirShowEffects[0] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke1.eff", -1F);
				if (bAirShowSmokeEnhanced) {
					astateAirShowEffects[1] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke1_2.eff", -1F);
					astateAirShowEffects[2] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke1_3.eff", -1F);
				}
			} else if (iAirShowSmoke == 2) {
				astateAirShowEffects[0] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke2.eff", -1F);
				if (bAirShowSmokeEnhanced) {
					astateAirShowEffects[1] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke2_2.eff", -1F);
					astateAirShowEffects[2] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke2_3.eff", -1F);
				}
			} else {
				astateAirShowEffects[0] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke3.eff", -1F);
				if (bAirShowSmokeEnhanced) {
					astateAirShowEffects[1] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke3_2.eff", -1F);
					astateAirShowEffects[2] = Eff3DActor.New(actor, hook, null, 1.0F, "3DO/Effects/Aircraft/AirShowSmoke3_3.eff", -1F);
				}
			}
		}
	}

	public void setDumpFuelState(boolean flag) {
		if (bDumpFuel == flag) return;
		if (bIsMaster) {
			doSetDumpFuelState(flag);
			netToMirrors(50, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetDumpFuelState(boolean flag) {
		bDumpFuel = flag;
		for (int i = 0; i < 3; i++)
			if (astateDumpFuelEffects[i] != null) Eff3DActor.finish(astateDumpFuelEffects[i]);

		if (flag)
		// TODO: DBW version
			if (actor instanceof TypeFuelDump) {
				for (int k = 0; k < 3; k++) {
					try {
						if (actor.findHook("_FuelDump0" + (k + 1)) != null) {
							astateDumpFuelEffects[k] = Eff3DActor.New(actor, actor.findHook("_FuelDump0" + (k + 1)), null, 1.0F, "3DO/Effects/Aircraft/DumpFuelTSPD.eff", -1F);
						}
					} catch (java.lang.Exception exception) {
						break;
					}
				}
				if (astateDumpFuelEffects[0] != null) return;

				if (astateDumpFuelEffects[0] == null) {	// aircraft's 3D model doesn't have _FuelDump01 Hook
					if (bWingTipLExists) astateDumpFuelEffects[0] = Eff3DActor.New(actor, actor.findHook("_WingTipL"), null, 1.0F, "3DO/Effects/Aircraft/DumpFuelTSPD.eff", -1F);
					if (bWingTipRExists) astateDumpFuelEffects[1] = Eff3DActor.New(actor, actor.findHook("_WingTipR"), null, 1.0F, "3DO/Effects/Aircraft/DumpFuelTSPD.eff", -1F);
				}

			}
	}

	public void setNavLightsState(boolean flag) {
		if (bNavLightsOn == flag) return;
		if (bIsMaster) {
			doSetNavLightsState(flag);
			netToMirrors(30, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetNavLightsState(boolean flag) {
		for (int i = 0; i < astateNavLightsEffects.length; i++) {
			if (astateNavLightsEffects[i] != null) {
				Eff3DActor.finish(astateNavLightsEffects[i]);
				astateNavLightsLights[i].light.setEmit(0.0F, 0.0F);
			}
			astateNavLightsEffects[i] = null;
		}

		if (flag) {
			new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			new Loc();
			for (int j = 0; j < astateNavLightsEffects.length; j++) {
				Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[j + astateEffectsDispLights] + "' visibility..");
				boolean flag1 = aircraft.isChunkAnyDamageVisible(astateEffectChunks[j + astateEffectsDispLights]);
				Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[j + astateEffectsDispLights] + "' is " + (flag1 ? "visible" : "invisible") + "..");
				if (flag1) {
					bNavLightsOn = flag;
					// TODO: Updated nav light code
					String s = "3DO/Effects/Fireworks/Flare" + (j <= 1 ? "Red" : j <= 3 ? "Green" : "White") + ".eff";
					astateNavLightsEffects[j] = Eff3DActor.New(actor, actor.findHook("_NavLight" + j), null, 1.0F, s, -1F, false);
					astateNavLightsLights[j].light.setEmit(0.35F, 8F);
				}
			}

		} else {
			bNavLightsOn = flag;
		}
	}

	public void changeNavLightEffectBase(int i, Actor actor1) {
		if (astateNavLightsEffects[i] != null) {
			Eff3DActor.finish(astateNavLightsEffects[i]);
			astateNavLightsLights[i].light.setEmit(0.0F, 0.0F);
			astateNavLightsEffects[i] = null;
		}
	}

	public void wantBeaconsNet(boolean flag) {
		if (flag == bWantBeaconsNet) return;
		bWantBeaconsNet = flag;
		if (World.getPlayerAircraft() != actor) return;
		if (bWantBeaconsNet) setBeacon(actor, beacon, 0, false);
	}

	public int getBeacon() {
		return beacon;
	}

	public void beaconPlus() {
		if (Main.cur().mission.hasBeacons(actor.getArmy())) {
			int i = Main.cur().mission.getBeacons(actor.getArmy()).size();
			if (beacon < i) setBeacon(beacon + 1);
			else setBeacon(0);
		}
	}

	public void beaconMinus() {
		if (Main.cur().mission.hasBeacons(actor.getArmy())) {
			int i = Main.cur().mission.getBeacons(actor.getArmy()).size();
			if (beacon >= 1) setBeacon(beacon - 1);
			else setBeacon(i);
		}
	}

	public void setBeacon(int i) {
		for (; i < 0; i += 32)
			;
		for (; i > 32; i -= 32)
			;
		if (i == beacon) {
			return;
		} else {
			setBeacon(actor, i, 0, false);
			return;
		}
	}

	private void setBeacon(Actor actor1, int i, int j, boolean flag) {
		doSetBeacon(actor1, i, j);
		if (Mission.isSingle() || !bWantBeaconsNet) return;
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) netToMirrors(41, i, j, actor1);
		else if (!flag) netToMaster(41, i, j, actor1);
	}

	private void doSetBeacon(Actor actor1, int i, int j) {
		if (actor != actor1) return;
		if (World.getPlayerAircraft() != actor) return;
		if (i > 0) {
			Actor actor2 = (Actor) Main.cur().mission.getBeacons(actor.getArmy()).get(i - 1);
			boolean flag = Aircraft.hasPlaneZBReceiver(aircraft);
			if (!flag && ((actor2 instanceof TypeHasYGBeacon) || (actor2 instanceof TypeHasHayRake))) {
				int k = i - beacon;
				beacon = i;
				if (k > 0) beaconPlus();
				else beaconMinus();
				return;
			}
			String s = Beacon.getBeaconID(i - 1);
			if (aircraft.getPilotsCount() == 1 || aircraft.FM.EI.engines.length == 1) Main3D.cur3D().ordersTree.setFrequency(null);
			if (actor2 instanceof TypeHasYGBeacon) {
				Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
				HUD.log(hudLogBeaconId, "BeaconYG", new Object[] { s });
				startListeningYGBeacon();
			} else if (actor2 instanceof TypeHasHayRake) {
				Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
				HUD.log(hudLogBeaconId, "BeaconYE", new Object[] { s });
				String s1 = Main.cur().mission.getHayrakeCodeOfCarrier(actor2);
				startListeningHayrake(actor2, s1);
			} else if (actor2 instanceof TypeHasLorenzBlindLanding && !(actor2 instanceof BigshipGeneric)) {
				Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
				HUD.log(hudLogBeaconId, "BeaconBA", new Object[] { s });
				startListeningLorenzBlindLanding();
				isAAFIAS = false;
				isILSBL = false;
				if (actor2 instanceof TypeHasAAFIAS) isAAFIAS = true;
				if (actor2 instanceof TypeHasILSBlindLanding) isILSBL = true;
			} else if (actor2 instanceof TypeHasILSBlindLanding && actor2 instanceof BigshipGeneric) {
				Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
				HUD.log(hudLogBeaconId, "BeaconBA", new Object[] { s });
				startListeningLorenzBlindLanding();
				isAAFIAS = false;
				isILSBL = true;
			} else if (actor2 instanceof TypeHasTACAN) {
				Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
				HUD.log(hudLogBeaconId, "BeaconTACAN", new Object[] { s });
				startListeningTACAN();
			} else if (actor2 instanceof TypeHasRadioStation) {
				TypeHasRadioStation typehasradiostation = (TypeHasRadioStation) actor2;
				s = typehasradiostation.getStationID();
				String s2 = Property.stringValue(actor2.getClass(), "i18nName", s);
				HUD.log(hudLogBeaconId, "BeaconRS", new Object[] { s2 });
				startListeningRadioStation(s);
			} else {
				HUD.log(hudLogBeaconId, "BeaconND", new Object[] { s });
				startListeningNDBeacon();
			}
		} else {
			HUD.log(hudLogBeaconId, "BeaconNONE");
			stopListeningBeacons();
			Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
		}
		beacon = i;
	}

	public void startListeningYGBeacon() {
		stopListeningLorenzBlindLanding();
		stopListeningHayrake();
		listenYGBeacon = true;
		listenNDBeacon = false;
		listenRadioStation = false;
		listenTACAN = false;
		aircraft.playBeaconCarrier(false, 0.0F);
		CmdMusic.setCurrentVolume(0.001F);
	}

	public void startListeningNDBeacon() {
		stopListeningLorenzBlindLanding();
		listenYGBeacon = false;
		listenNDBeacon = true;
		listenRadioStation = false;
		listenTACAN = false;
		stopListeningHayrake();
		CmdMusic.setCurrentVolume(0.001F);
	}

	public void startListeningTACAN() {
		stopListeningLorenzBlindLanding();
		listenYGBeacon = false;
		listenNDBeacon = false;
		listenRadioStation = false;
		listenTACAN = true;
		stopListeningHayrake();
	}

	public void startListeningRadioStation(String s) {
		stopListeningLorenzBlindLanding();
		listenYGBeacon = false;
		listenNDBeacon = false;
		listenRadioStation = true;
		listenTACAN = false;
		stopListeningHayrake();
		String s1 = s.replace(' ', '_');
		int i = Mission.curYear();
		int j = Mission.curMonth();
		int k = Mission.curDay();
		String s2 = "" + i;
		String s3 = "";
		String s4 = "";
		if (j < 10) s3 = "0" + j;
		else s3 = "" + j;
		if (k < 10) s4 = "0" + k;
		else s4 = "" + k;
		String as[] = { s2 + s3 + s4, s2 + s3 + "XX", s2 + "XX" + s4, s2 + "XXXX" };
		for (int l = 0; l < as.length; l++) {
			File file = new File("./samples/Music/Radio/" + s1 + "/" + as[l]);
			if (file.exists()) {
				CmdMusic.setPath("Music/Radio/" + s1 + "/" + as[l], true);
				CmdMusic.play();
				return;
			}
		}

		CmdMusic.setPath("Music/Radio/" + s1, true);
		CmdMusic.play();
	}

	public void stopListeningBeacons() {
		stopListeningLorenzBlindLanding();
		listenYGBeacon = false;
		listenNDBeacon = false;
		listenRadioStation = false;
		listenTACAN = false;
		stopListeningHayrake();
		aircraft.stopMorseSounds();
		CmdMusic.setCurrentVolume(0.001F);
	}

	public void startListeningHayrake(Actor actor1, String s) {
		stopListeningLorenzBlindLanding();
		hayrakeCarrier = actor1;
		hayrakeCode = s;
		listenYGBeacon = false;
		listenNDBeacon = false;
		listenRadioStation = false;
		listenTACAN = false;
		aircraft.stopMorseSounds();
		CmdMusic.setCurrentVolume(0.001F);
	}

	public void stopListeningHayrake() {
		hayrakeCarrier = null;
		hayrakeCode = null;
	}

	public void startListeningLorenzBlindLanding() {
		listenLorenzBlindLanding = true;
		stopListeningHayrake();
		listenYGBeacon = false;
		listenNDBeacon = false;
		listenRadioStation = false;
		listenTACAN = false;
		aircraft.stopMorseSounds();
		CmdMusic.setCurrentVolume(0.001F);
	}

	public void stopListeningLorenzBlindLanding() {
		listenLorenzBlindLanding = false;
		isAAFIAS = false;
		isILSBL = false;
		aircraft.stopMorseSounds();
	}

	public void preLoadRadioStation(Actor actor1) {
		TypeHasRadioStation typehasradiostation = (TypeHasRadioStation) actor1;
		String s = typehasradiostation.getStationID();
		startListeningRadioStation(s);
	}

	public void setLandingLightState(boolean flag) {
		if (bLandingLightOn == flag) return;
		if (bIsMaster) {
			doSetLandingLightState(flag);
			int i = 0;
			for (int j = 0; j < astateLandingLightEffects.length; j++)
				if (astateLandingLightEffects[j] == null) i++;

			if (i == astateLandingLightEffects.length) {
				bLandingLightOn = false;
				flag = false;
			}
			netToMirrors(31, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetLandingLightState(boolean flag) {
		bLandingLightOn = flag;
		for (int i = 0; i < astateLandingLightEffects.length; i++) {
			if (astateLandingLightEffects[i] != null) {
				Eff3DActor.finish(astateLandingLightEffects[i]);
				astateLandingLightLights[i].light.setEmit(0.0F, 0.0F);
			}
			astateLandingLightEffects[i] = null;
		}

		if (flag) {
			for (int j = 0; j < astateLandingLightEffects.length; j++) {
				Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[j + astateEffectsDispLandingLights] + "' visibility..");
				boolean flag1 = aircraft.isChunkAnyDamageVisible(astateEffectChunks[j + astateEffectsDispLandingLights]);
				Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[j + astateEffectsDispLandingLights] + "' is " + (flag1 ? "visible" : "invisible") + "..");
				if (flag1) {
					String s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
					astateLandingLightEffects[j] = Eff3DActor.New(actor, actor.findHook("_LandingLight0" + j), null, 1.0F, s, -1F);
					astateLandingLightLights[j].light.setEmit(1.2F, 8F);
				}
			}

		}
	}

	public void changeLandingLightEffectBase(int i, Actor actor1) {
		if (astateLandingLightEffects[i] != null) {
			Eff3DActor.finish(astateLandingLightEffects[i]);
			astateLandingLightLights[i].light.setEmit(0.0F, 0.0F);
			astateLandingLightEffects[i] = null;
			bLandingLightOn = false;
		}
	}

	public void disablePilot(int i) {
		astatePilotStates[i] = 100;
	}

	public void setPilotState(Actor actor1, int i, int j) {
		setPilotState(actor1, i, j, true);
	}

	public void setPilotState(Actor actor1, int i, int j, boolean flag) {
		if (!Actor.isValid(actor1)) return;
		if (j > 95) j = 100;
		if (j < 0) j = 0;
		if (astatePilotStates[i] >= j) return;
		if (bIsMaster) {
			aircraft.setDamager(actor1);
			doSetPilotState(i, j, actor1);
			if (flag && aircraft.FM.isPlayers() && i == astatePlayerIndex && !World.isPlayerDead() && actor1 != actor && (actor1 instanceof Aircraft) && ((Aircraft) actor1).isNetPlayer() && j == 100)
				Chat.sendLogRnd(1, "gore_pk", (Aircraft) actor1, aircraft);
			if (j > 0 && flag) netToMirrors(17, i, j);
		} else if (flag) netToMaster(17, i, j, actor1);
	}

	public void hitPilot(Actor actor1, int i, int j) {
		setPilotState(actor1, i, astatePilotStates[i] + j);
	}

	public void setBleedingPilot(Actor actor1, int i, int j) {
		setBleedingPilot(actor1, i, j, true);
	}

	public void setBleedingPilot(Actor actor1, int i, int j, boolean flag) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			doSetBleedingPilot(i, j, actor1);
			if (flag) netToMirrors(45, i, j);
		} else if (flag) netToMaster(45, i, j, actor1);
	}

	private void doSetBleedingPilot(int i, int j, Actor actor1) {
		if (Mission.isSingle() || aircraft.FM.isPlayers() && !Mission.isServer() || Mission.isNet() && Mission.isServer() && !aircraft.isNetPlayer()) {
			if (j == 0) return;
			long l = 0x1d4c0 / j;
			if (astateBleedingNext[i] == 0L) {
				astateBleedingNext[i] = l;
				if (World.getPlayerAircraft() == actor && Config.isUSE_RENDER() && (!bIsAboutToBailout || astateBailoutStep <= 11) && astatePilotStates[i] < 100) {
					boolean flag = i == astatePlayerIndex && !World.isPlayerDead();
					if (j < 10) HUD.log(flag ? "PlayerBLEED0" : astateHUDPilotHits[astatePilotFunctions[i]] + "BLEED0");
					else HUD.log(flag ? "PlayerBLEED1" : astateHUDPilotHits[astatePilotFunctions[i]] + "BLEED1");
				}
			} else {
				int k = 0x1d4c0 / (int) astateBleedingNext[i];
				long l1 = 0x1d4c0 / (k + j);
				if (l1 < 100L) l1 = 100L;
				astateBleedingNext[i] = l1;
			}
			setBleedingTime(i);
		}
	}

	public boolean bleedingTest(int i) {
		return Time.current() > astateBleedingTimes[i];
	}

	public void setBleedingTime(int i) {
		astateBleedingTimes[i] = Time.current() + astateBleedingNext[i];
	}

	public void doSetWoundPilot(int i) {
		switch (i) {
		default:
			break;

		case 1: // '\001'
			aircraft.FM.SensRoll *= 0.6F;
			aircraft.FM.SensPitch *= 0.6F;
			if (aircraft.FM.isPlayers() && Config.isUSE_RENDER()) HUD.log("PlayerArmHit");
			break;

		case 2: // '\002'
			aircraft.FM.SensYaw *= 0.2F;
			if (aircraft.FM.isPlayers() && Config.isUSE_RENDER()) HUD.log("PlayerLegHit");
			break;
		}
	}

	public void setPilotWound(Actor actor1, int i, int j) {
		setPilotWound(actor1, j, true);
	}

	public void setPilotWound(Actor actor1, int i, boolean flag) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			doSetWoundPilot(i);
			if (flag) netToMirrors(44, 0, i);
		} else if (flag) netToMaster(44, 0, i, actor1);
	}

	public void woundedPilot(Actor actor1, int i, int j) {
		switch (i) {
		default:
			break;

		case 1: // '\001'
			if (TrueRandom.nextFloat() < 0.18F && j > 4 && !armsWounded) {
				setPilotWound(actor1, i, true);
				armsWounded = true;
			}
			break;

		case 2: // '\002'
			if (j > 4 && !legsWounded) {
				setPilotWound(actor1, i, true);
				legsWounded = true;
			}
			break;
		}
	}

	private void doSetPilotState(int i, int j, Actor actor1) {
		if (j > 95) j = 100;
		if (World.getPlayerAircraft() == actor && Config.isUSE_RENDER() && (!bIsAboutToBailout || astateBailoutStep <= 11)) {
			boolean flag = i == astatePlayerIndex && !World.isPlayerDead();
			if (astatePilotStates[i] < 100 && j == 100) {
				HUD.log(flag ? "PlayerHIT2" : astateHUDPilotHits[astatePilotFunctions[i]] + "HIT2");
				if (flag) {
					World.setPlayerDead();
					if (Mission.isNet()) Chat.sendLog(0, "gore_killed", (NetUser) NetEnv.host(), (NetUser) NetEnv.host());
					if (Main3D.cur3D().cockpits != null && !World.isPlayerGunner()) {
						int k = Main3D.cur3D().cockpits.length;
						for (int l = 0; l < k; l++) {
							Cockpit cockpit = Main3D.cur3D().cockpits[l];
							if (Actor.isValid(cockpit) && cockpit.astatePilotIndx() != i && !isPilotDead(cockpit.astatePilotIndx()) && !Mission.isNet() && AircraftHotKeys.isCockpitRealMode(l)) AircraftHotKeys.setCockpitRealMode(l, false);
						}

					}
				}
			} else if (astatePilotStates[i] < 66 && j > 66) HUD.log(flag ? "PlayerHIT1" : astateHUDPilotHits[astatePilotFunctions[i]] + "HIT1");
			else if (astatePilotStates[i] < 25 && j > 25) HUD.log(flag ? "PlayerHIT0" : astateHUDPilotHits[astatePilotFunctions[i]] + "HIT0");
		}
		byte byte0 = astatePilotStates[i];
		astatePilotStates[i] = (byte) j;
		if (bIsAboutToBailout && astateBailoutStep > i + 11) {
			aircraft.doWoundPilot(i, 0.0F);
			return;
		}
		if (j > 99) {
			aircraft.doWoundPilot(i, 0.0F);
			aircraft.doMurderPilot(i);
			if (i == 0) {
				FlightModel flightmodel = aircraft.FM;
				if ((flightmodel instanceof Maneuver) && (astatePilotFunctions[1] != 2 || (double) getPilotHealth(1) <= 0.0D || World.getPlayerAircraft() == actor)) {
					if (!bIsAboutToBailout) Explosions.generateComicBulb(actor, "PK", 9F);
					((Maneuver) flightmodel).set_maneuver(44);
					((Maneuver) flightmodel).set_task(2);
					flightmodel.setCapableOfTaxiing(false);
					EventLog.onPilotKilled(aircraft, i, actor1 != aircraft ? actor1 : null);
				}
			} else if (i == 1 && astatePilotFunctions[1] == 2 && World.getPlayerAircraft() != actor) {
				FlightModel flightmodel1 = aircraft.FM;
				if ((flightmodel1 instanceof Maneuver) && (double) getPilotHealth(0) <= 0.0D) {
					if (!bIsAboutToBailout) Explosions.generateComicBulb(actor, "PK", 9F);
					((Maneuver) flightmodel1).set_maneuver(44);
					((Maneuver) flightmodel1).set_task(2);
					flightmodel1.setCapableOfTaxiing(false);
					EventLog.onPilotKilled(aircraft, i, actor1 != aircraft ? actor1 : null);
				}
			} else if (i > 0 && !bIsAboutToBailout) {
				Explosions.generateComicBulb(actor, "GunnerDown", 9F);
				EventLog.onPilotKilled(aircraft, i, actor1 != aircraft ? actor1 : null);
			}
		} else if (byte0 < 66 && j > 66) EventLog.onPilotHeavilyWounded(aircraft, i);
		else if (byte0 < 25 && j > 25) EventLog.onPilotWounded(aircraft, i);
		if (j <= 99 && i > 0 && World.cur().diffCur.RealisticPilotVulnerability) aircraft.doWoundPilot(i, getPilotHealth(i));
	}

	private void doRemoveBodyFromPlane(int i) {
		aircraft.doRemoveBodyFromPlane(i);
	}

	public float getPilotHealth(int i) {
		if (i < 0 || i > aircraft.FM.crew - 1) return 0.0F;
		else return 1.0F - (float) astatePilotStates[i] * 0.01F;
	}

	public boolean isAllPilotsDead() {
		if (aircraft.FM.crew <= 1) return isPilotDead(0);
		if (astatePilotFunctions[1] == 2) return isPilotDead(0) & isPilotDead(1);
		else return isPilotDead(0);
	}

	public boolean isPilotDead(int i) {
		if (i < 0 || i > aircraft.FM.crew - 1) return true;
		else return astatePilotStates[i] == 100;
	}

	public boolean isPilotParatrooper(int i) {
		if (i < 0 || i > aircraft.FM.crew - 1) return true;
		else return astatePilotStates[i] == 100 && astateBailoutStep > 11 + i;
	}

	public void setJamBullets(int i, int j) {
		if (aircraft.FM.CT.Weapons[i] == null || aircraft.FM.CT.Weapons[i].length <= j || aircraft.FM.CT.Weapons[i][j] == null) return;
		if (bIsMaster) {
			doSetJamBullets(i, j);
			netToMirrors(24, i, j);
		} else {
			netToMaster(24, i, j);
		}
	}

	private void doSetJamBullets(int i, int j) {
		if (aircraft.FM.CT.Weapons != null && aircraft.FM.CT.Weapons[i] != null && aircraft.FM.CT.Weapons[i][j] != null && aircraft.FM.CT.Weapons[i][j].haveBullets()) {
			if (actor == World.getPlayerAircraft() && aircraft.FM.CT.Weapons[i][j].haveBullets()) if (aircraft.FM.CT.Weapons[i][j].bulletMassa() < 0.095F) HUD.log(i <= 9 ? "FailedMGun" : "FailedTMGun");
			else HUD.log("FailedCannon");
			aircraft.FM.CT.Weapons[i][j].loadBullets(0);
		}
	}

	public boolean hitDaSilk() {
		if (!bIsMaster) return false;
		if (bIsAboutToBailout) return false;
		if (bCheckPlayerAircraft && actor == World.getPlayerAircraft()) return false;
		if (!bIsEnableToBailout) return false;
		bIsAboutToBailout = true;
		FlightModel flightmodel = aircraft.FM;
		Aircraft.debugprintln(aircraft, "I've had it, bailing out..");
		Explosions.generateComicBulb(actor, "Bailing", 5F);
		if (flightmodel instanceof Maneuver) {
			((Maneuver) flightmodel).set_maneuver(44);
			((Maneuver) flightmodel).set_task(2);
			flightmodel.setTakenMortalDamage(true, null);
		}
		return true;
	}

	public void setFlatTopString(Actor actor1, int i) {
		if (bIsMaster) netToMirrors(36, i, i, actor1);
	}

	private void doSetFlatTopString(Actor actor1, int i) {
		if (Actor.isValid(actor1) && (actor1 instanceof BigshipGeneric) && ((BigshipGeneric) actor1).getAirport() != null) {
			BigshipGeneric bigshipgeneric = (BigshipGeneric) actor1;
			if (i >= 0 && i < 255) bigshipgeneric.forceTowAircraft(aircraft, i);
			else bigshipgeneric.requestDetowAircraft(aircraft);
		}
	}

	public void setFMSFX(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) doSetFMSFX(i, j);
		else netToMaster(37, i, j, actor1);
	}

	private void doSetFMSFX(int i, int j) {
		aircraft.FM.doRequestFMSFX(i, j);
	}

	public void setWingFold(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (actor1 != aircraft) return;
		if (!aircraft.FM.CT.bHasWingControl) return;
		if (aircraft.FM.CT.wingControl == (float) i) return;
		if (!bIsMaster) {
			return;
		} else {
			doSetWingFold(i);
			netToMirrors(38, i, i);
			return;
		}
	}

	private void doSetWingFold(int i) {
		aircraft.FM.CT.wingControl = i;
	}

	// TODO: New door parameters
	public void setCockpitDoor(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (actor1 != aircraft) return;
		if (!((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasCockpitDoorControl) return;
		if (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl == (float) i) return;
		if (!bIsMaster) return;
		if (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bMoveSideDoor) {
			doSetCockpitDoor(i, SIDE_DOOR);
			netToMirrors(44, SIDE_DOOR, i);
		} else {
			doSetCockpitDoor(i, COCKPIT_DOOR);
			netToMirrors(39, COCKPIT_DOOR, i);
		}
	}

	private void doSetCockpitDoor(int i, int j) {
		((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.setActiveDoor(j);
		((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl = i;
		if (i == 0 && (Main.cur() instanceof Main3D) && aircraft == World.getPlayerAircraft() && HookPilot.current != null) HookPilot.current.doUp(false);
	}

	public void setArrestor(Actor actor1, int i) {
		if (!Actor.isValid(actor1)) return;
		if (actor1 != aircraft) return;
		if (!aircraft.FM.CT.bHasArrestorControl) return;
		if (aircraft.FM.CT.arrestorControl == (float) i) return;
		if (!bIsMaster) {
			return;
		} else {
			doSetArrestor(i);
			netToMirrors(40, i, i);
			return;
		}
	}

	private void doSetArrestor(int i) {
		aircraft.FM.CT.arrestorControl = i;
	}

	public void update(float f) {
		if (World.cur().diffCur.RealisticPilotVulnerability) {
			for (int i = 0; i < aircraft.FM.crew; i++) {
				if (astateBleedingNext[i] > 0L && bleedingTest(i)) {
					hitPilot(actor, i, 1);
					if (astatePilotStates[i] > 96) astateBleedingNext[i] = 0L;
					setBleedingTime(i);
				}
				if (aircraft.FM.crew <= 1 && astatePilotFunctions[1] != 2) {
					if (astatePilotStates[0] > 20 + 10 * aircraft.FM.courage) aircraft.FM.setCapableOfBMP(false, actor);
					continue;
				}
				if (astatePilotStates[0] > 20 + 10 * aircraft.FM.courage && astatePilotStates[1] > 20 + 10 * aircraft.FM.courage) aircraft.FM.setCapableOfBMP(false, actor);
			}

		}
		bCriticalStatePassed = bIsAboveCriticalSpeed != (aircraft.getSpeed(null) > 10D);
		if (bCriticalStatePassed) {
			bIsAboveCriticalSpeed = aircraft.FM.getSpeed() > 10F;
			for (int j = 0; j < 4; j++)
				doSetTankState(null, j, astateTankStates[j]);

			for (int k = 0; k < aircraft.FM.EI.getNum(); k++)
				doSetEngineState(null, k, astateEngineStates[k]);

			for (int l = 0; l < aircraft.FM.EI.getNum(); l++)
				doSetOilState(l, astateOilStates[l]);

		}
		bCriticalStatePassed = bIsAboveCondensateAlt != (aircraft.FM.getAltitude() > 7000F);
		if (bCriticalStatePassed) {
			bIsAboveCondensateAlt = aircraft.FM.getAltitude() > 7000F;
			doSetCondensateState(bIsAboveCondensateAlt);
		}
		bCriticalStatePassed = bIsOnInadequateAOA != (aircraft.FM.getSpeed() > 17F && aircraft.FM.getAOA() > 15F - aircraft.FM.getAltitude() * 0.001F);
		if (bCriticalStatePassed) {
			bIsOnInadequateAOA = aircraft.FM.getSpeed() > 17F && aircraft.FM.getAOA() > 15F - aircraft.FM.getAltitude() * 0.001F;
			setStallState(bIsOnInadequateAOA);
		}
		if (bIsMaster) {
			float f1 = 0.0F;
			for (int i1 = 0; i1 < 4; i1++)
				f1 += astateTankStates[i1] * astateTankStates[i1];

			aircraft.FM.M.requestFuel(f1 * 0.12F * f);
			// TODO: Disable for ModAct compatibility
			if (bDumpFuel && aircraft instanceof TypeFuelDump) if (aircraft.FM.M.fuel > ((TypeFuelDump) aircraft).getFuelReserve()) aircraft.FM.M.fuel -= ((TypeFuelDump) aircraft).getFlowRate() * f;
			else setDumpFuelState(false);
			for (int j1 = 0; j1 < 4; j1++)
				switch (astateTankStates[j1]) {
				case 3: // '\003'
				default:
					break;

				case 1: // '\001'
					if (TrueRandom.nextFloat(0.0F, 1.0F) < 0.0125F) {
						repairTank(j1);
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " protector clothes the hole - leak stops..");
					}
					// fall through

				case 2: // '\002'
					if (aircraft.FM.M.fuel <= 0.0F) {
						repairTank(j1);
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " runs out of fuel - leak stops..");
					}
					break;

				case 4: // '\004'
					if (TrueRandom.nextFloat(0.0F, 1.0F) < 0.06F) hitTank(aircraft, j1, 0);
					if (TrueRandom.nextFloat(0.0F, 1.0F) < 0.00333F) {
						hitTank(aircraft, j1, 1);
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " catches fire..");
					}
					break;

				case 5: // '\005'
					if (aircraft.FM.getSpeed() > 90F && TrueRandom.nextFloat() < Aircraft.cvt(aircraft.FM.getSpeed(), 90F, 200F, 0.0F, 0.2F)) {
						repairTank(j1);
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " cuts fire..");
					}
					if (TrueRandom.nextFloat() < 0.0048F) {
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " fires up to the next stage..");
						hitTank(aircraft, j1, 1);
					}
					if (TrueRandom.nextFloat() < 0.002F) {
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " EXPLODES!.");
						explodeTank(aircraft, j1);
					}
					if (!(actor instanceof Scheme1)) break;
					Hook hook = actor.findHook("_Tank" + (j1 + 1) + "Burn");
					if (!hook.chunkName().toLowerCase().startsWith("cf") || astatePilotStates[0] >= 96) break;
					hitPilot(actor, 0, 5);
					if (astatePilotStates[0] < 96) break;
					hitPilot(actor, 0, 101 - astatePilotStates[0]);
					if (aircraft.FM.isPlayers() && Mission.isNet() && !aircraft.FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_burnedcpt", aircraft, null);
					break;

				case 6: // '\006'
					if (aircraft.FM.getSpeed() > 90F && TrueRandom.nextFloat() < Aircraft.cvt(aircraft.FM.getSpeed(), 90F, 200F, 0.0F, 0.1F)) {
						repairTank(j1);
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " cuts fire..");
					}
					if (TrueRandom.nextFloat() < 0.0075F) {
						Aircraft.debugprintln(aircraft, "Tank " + j1 + " EXPLODES!.");
						explodeTank(aircraft, j1);
					}
					if (!(actor instanceof Scheme1)) break;
					Hook hook1 = actor.findHook("_Tank" + (j1 + 1) + "Burn");
					if (!hook1.chunkName().toLowerCase().startsWith("cf") || astatePilotStates[0] >= 96) break;
					hitPilot(actor, 0, 7);
					if (astatePilotStates[0] < 96) break;
					hitPilot(actor, 0, 101 - astatePilotStates[0]);
					if (aircraft.FM.isPlayers() && Mission.isNet() && !aircraft.FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_burnedcpt", aircraft, null);
					break;
				}

			for (int k1 = 0; k1 < aircraft.FM.EI.getNum(); k1++) {
				int l1 = aircraft.FM.EI.engines[k1].getStage();
				if (astateEngineStates[k1] > 1) {
					float f2 = 1.0F;
					if (astateEngineStates[k1] > 3) f2 = 15F;
					aircraft.FM.EI.engines[k1].setReadyness(actor, aircraft.FM.EI.engines[k1].getReadyness() - (float) astateEngineStates[k1] * f2 * 0.00025F * f);
					if (aircraft.FM.EI.engines[k1].getReadyness() < 0.2F && aircraft.FM.EI.engines[k1].getReadyness() != 0.0F) aircraft.FM.EI.engines[k1].setEngineDies(actor);
				}
				switch (astateEngineStates[k1]) {
				case 3: // '\003'
					if (l1 == 6 && TrueRandom.nextFloat() < Aircraft.cvt(aircraft.FM.EI.engines[k1].getEngineLoad(), 1.0F, 3F, 0.015F, 0.0F)) {
						hitEngine(aircraft, k1, 1);
						Aircraft.debugprintln(aircraft, "Engine " + k1 + " catches fire..");
					} else if (l1 > 0 && l1 < 6) {
						if (TrueRandom.nextFloat() < 0.2F) aircraft.FM.EI.engines[k1].setEngineDies(actor);
					} else if (l1 != 6 && (double) TrueRandom.nextFloat() < 0.0074999999999999997D) repairEngine(k1);
					break;

				case 4: // '\004'
					if (aircraft.FM.getSpeed() > 80F && TrueRandom.nextFloat() < Aircraft.cvt(aircraft.FM.getSpeed(), 80F, 200F, 0.0F, 0.3F)) {
						repairEngine(k1);
						Aircraft.debugprintln(aircraft, "Engine " + k1 + " cuts fire..");
					}
					if ((actor instanceof Scheme1) && astatePilotStates[0] < 96) {
						hitPilot(actor, 0, 4);
						if (astatePilotStates[0] >= 96) {
							hitPilot(actor, 0, 101 - astatePilotStates[0]);
							if (aircraft.FM.isPlayers() && Mission.isNet() && !aircraft.FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_burnedcpt", aircraft, null);
						}
					}
					if (aircraft.FM.EI.engines[k1].getType() == 1 || aircraft.FM.EI.engines[k1].getType() == 0) {
						if (l1 == 6) {
							float f3 = Aircraft.cvt(aircraft.FM.EI.engines[k1].getEngineLoad(), 1.0F, 5F, 0.01F, 0.0F);
							if (aircraft.FM.EI.engines[k1].getControlAfterburner()) f3 *= 8F;
							if (TrueRandom.nextFloat() < f3) if (actor instanceof Scheme1) {
								if ((double) TrueRandom.nextFloat() < 0.050000000000000003D) {
									Aircraft.debugprintln(aircraft, "Engine 0 detonates and explodes, fatal damage level forced..");
									aircraft.msgCollision(actor, "CF_D0", "CF_D0");
								} else {
									aircraft.FM.EI.engines[k1].setEngineDies(actor);
									explodeEngine(actor, k1);
								}
							} else {
								aircraft.FM.EI.engines[k1].setEngineDies(actor);
								explodeEngine(actor, k1);
							}
						} else if (l1 > 0 && l1 < 6) {
							if (TrueRandom.nextFloat() < 0.3F) aircraft.FM.EI.engines[k1].setEngineDies(actor);
						} else if (l1 != 6 && TrueRandom.nextFloat() < 0.15F) {
							repairEngine(k1);
							Aircraft.debugprintln(aircraft, "Engine " + k1 + " cuts fire..");
						}
					} else if ((actor instanceof Scheme1) && TrueRandom.nextFloat() < 0.05F) {
						Aircraft.debugprintln(aircraft, "Engine 0 detonates and explodes, fatal damage level forced..");
						aircraft.msgCollision(actor, "CF_D0", "CF_D0");
					}
					break;
				}
				if (astateOilStates[k1] > 0) aircraft.FM.EI.engines[k1].setReadyness(aircraft, aircraft.FM.EI.engines[k1].getReadyness() - 0.001875F * f);
			}

			// TODO: Edit allowing F-86D to keep rocket tray door open
			// indefinitely
			if (!((actor instanceof TypeFighter) && aircraft.FM.CT.bHasBayDoorControl)) {
				// TODO: Don't disable this statement and bailout. Else, you
				// won't be able to bailout!
				bailout();
				if (TrueRandom.nextFloat() < 0.25F && !aircraft.FM.CT.saveWeaponControl[3]) if (aircraft.FM.isPlayers() && (aircraft.FM instanceof RealFlightModel) && ((RealFlightModel) aircraft.FM).isRealMode()) {
					if (!aircraft.FM.CT.bHasBayDoorControl) aircraft.FM.CT.BayDoorControl = 0.0F;
				} else if (!(actor instanceof TypeBomber) || aircraft.FM.isReadyToReturn()) aircraft.FM.CT.BayDoorControl = 0.0F;
				bailout();
			} else if (!aircraft.FM.isPlayers() && TrueRandom.nextFloat() < 0.125F && !aircraft.FM.CT.saveWeaponControl[3] && (!(actor instanceof TypeBomber) || aircraft.FM.AP.way.curr().Action != 3)) aircraft.FM.CT.BayDoorControl = 0.0F;
		}
	}

	private void bailout() {
		if (this.bIsAboutToBailout) {
			if ((this.astateBailoutStep >= 0) && (this.astateBailoutStep < 2)) {
				if ((this.aircraft.FM.CT.cockpitDoorControl > 0.5F) && (this.aircraft.FM.CT.getCockpitDoor() > 0.5F)) {
					this.astateBailoutStep = 11;
					doRemoveBlisters();
				} else {
					this.astateBailoutStep = 2;
				}
			} else if ((this.astateBailoutStep >= 2) && (this.astateBailoutStep <= 3)) {
				switch (this.astateBailoutStep) {
				case 2:
					if (this.aircraft.FM.CT.cockpitDoorControl < 0.5F) {
						doRemoveBlister1();
					}
					break;
				case 3:
					doRemoveBlisters();
				}

				if (this.bIsMaster) netToMirrors(20, this.astateBailoutStep, 1);
				this.astateBailoutStep = ((byte) (this.astateBailoutStep + 1));
				if ((this.astateBailoutStep == 3) && ((this.actor instanceof P_39))) this.astateBailoutStep = ((byte) (this.astateBailoutStep + 1));
				if (this.astateBailoutStep == 4) {
					this.astateBailoutStep = 11;
				}
			} else if ((this.astateBailoutStep >= 11) && (this.astateBailoutStep <= 19)) {
				float f1 = this.aircraft.FM.getSpeed();
				float f2 = (float) this.aircraft.FM.Loc.z;
				float f3 = 140.0F;
				if (((this.aircraft instanceof HE_162)) || ((this.aircraft instanceof GO_229)) || ((this.aircraft instanceof ME_262HGII)) || ((this.aircraft instanceof DO_335))) {
					f3 = 9999.9004F;
				}
				if (((Pitot.Indicator(f2, f1) < f3) && (this.aircraft.FM.getOverload() < 2.0F)) || (!this.bIsMaster)) {
					int i = this.astateBailoutStep;
					if (this.bIsMaster) netToMirrors(20, this.astateBailoutStep, 1);
					this.astateBailoutStep = ((byte) (this.astateBailoutStep + 1));
					if (i == 11) {
						this.aircraft.FM.setTakenMortalDamage(true, null);
						if (((this.aircraft.FM instanceof Maneuver)) && (((Maneuver) this.aircraft.FM).get_maneuver() != 44)) {
							World.cur();
							if (this.actor != World.getPlayerAircraft()) {
								((Maneuver) this.aircraft.FM).set_maneuver(44);
							}
						}
					}
					if (this.astatePilotStates[(i - 11)] < 99) {
						doRemoveBodyFromPlane(i - 10);
						if ((i == 11) && (!this.aircraft.FM.isStationedOnGround())) {
							if ((this.aircraft instanceof HE_162)) {
								((HE_162) this.aircraft).doEjectCatapult();
								this.astateBailoutStep = 51;
								this.aircraft.FM.setTakenMortalDamage(true, null);
								this.aircraft.FM.CT.WeaponControl[0] = false;
								this.aircraft.FM.CT.WeaponControl[1] = false;
								this.astateBailoutStep = -1;
								return;
							}
							if ((this.aircraft instanceof GO_229)) {
								((GO_229) this.aircraft).doEjectCatapult();
								this.astateBailoutStep = 51;
								this.aircraft.FM.setTakenMortalDamage(true, null);
								this.aircraft.FM.CT.WeaponControl[0] = false;
								this.aircraft.FM.CT.WeaponControl[1] = false;
								this.astateBailoutStep = -1;
								return;
							}
							if ((this.aircraft instanceof DO_335)) {
								((DO_335) this.aircraft).doEjectCatapult();
								this.astateBailoutStep = 51;
								this.aircraft.FM.setTakenMortalDamage(true, null);
								this.aircraft.FM.CT.WeaponControl[0] = false;
								this.aircraft.FM.CT.WeaponControl[1] = false;
								this.astateBailoutStep = -1;
								return;
							}
							if ((this.aircraft instanceof ME_262HGII)) {
								((ME_262HGII) this.aircraft).doEjectCatapult();
								this.astateBailoutStep = 51;
								this.aircraft.FM.setTakenMortalDamage(true, null);
								this.aircraft.FM.CT.WeaponControl[0] = false;
								this.aircraft.FM.CT.WeaponControl[1] = false;
								this.astateBailoutStep = -1;
								return;
							}
						}
						setPilotState(this.aircraft, i - 11, 100, false);

						if ((!this.actor.isNet()) || (this.actor.isNetMaster())) {
							try {
								Hook localHook = this.actor.findHook("_ExternalBail0" + (i - 10));

								if (localHook != null) {
									Loc localLoc = new Loc(0.0D, 0.0D, 0.0D, TrueRandom.nextFloat(-45.0F, 45.0F), 0.0F, 0.0F);

									localHook.computePos(this.actor, this.actor.pos.getAbs(), localLoc);

									new Paratrooper(this.actor, this.actor.getArmy(), i - 11, localLoc, this.aircraft.FM.Vwld);

									this.aircraft.FM.setTakenMortalDamage(true, null);

									if (i == 11) {
										this.aircraft.FM.CT.WeaponControl[0] = false;
										this.aircraft.FM.CT.WeaponControl[1] = false;
									}
									if ((i > 10) && (i <= 19)) {
										EventLog.onBailedOut(this.aircraft, i - 11);
									}
								}
							} catch (Exception localException) {
							} finally {
							}
							if ((this.astateBailoutStep == 19) && (this.actor == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (this.aircraft.FM.brakeShoe)) {
								MsgDestroy.Post(Time.current() + 1000L, this.aircraft);
							}
						}
					}
				}
			}
		}
	}

	private final void doRemoveBlister1() {
		if (aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1 && getPilotHealth(0) > 0.0F) {
			if (aircraft instanceof JU_88NEW) {
				float f = aircraft.FM.getAltitude() - Landscape.HQ_Air((float) aircraft.FM.Loc.x, (float) aircraft.FM.Loc.y);
				if (f < 0.3F) {
					aircraft.blisterRemoved(1);
					return;
				}
			}
			aircraft.hierMesh().hideSubTrees("Blister1_D0");
			Wreckage wreckage = new Wreckage((ActorHMesh) actor, aircraft.hierMesh().chunkFind("Blister1_D0"));
			wreckage.collide(false);
			Vector3d vector3d = new Vector3d();
			vector3d.set(aircraft.FM.Vwld);
			wreckage.setSpeed(vector3d);
			aircraft.blisterRemoved(1);
		}
	}

	private final void doRemoveBlisters() {
		for (int i = 2; i < 10; i++)
			if (aircraft.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && getPilotHealth(i - 1) > 0.0F) {
				aircraft.hierMesh().hideSubTrees("Blister" + i + "_D0");
				Wreckage wreckage = new Wreckage((ActorHMesh) actor, aircraft.hierMesh().chunkFind("Blister" + i + "_D0"));
				wreckage.collide(false);
				Vector3d vector3d = new Vector3d();
				vector3d.set(aircraft.FM.Vwld);
				wreckage.setSpeed(vector3d);
				aircraft.blisterRemoved(i);
			}

	}

	public void netUpdate(boolean flag, boolean flag1, NetMsgInput netmsginput) throws IOException {
		if (flag1) {
			if (flag) {
				int i = netmsginput.readUnsignedByte();
				int k = netmsginput.readUnsignedByte();
				int i1 = netmsginput.readUnsignedByte();
				Actor actor1 = null;
				if (netmsginput.available() > 0) {
					NetObj netobj = netmsginput.readNetObj();
					if (netobj != null) actor1 = (Actor) netobj.superObj();
				}
				switch (i) {
				case 1: // '\001'
					setEngineState(actor1, k, i1);
					break;

				case 2: // '\002'
					setEngineSpecificDamage(actor1, k, i1);
					break;

				case 3: // '\003'
					explodeEngine(actor1, k);
					break;

				case 4: // '\004'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (E.S.)");

				case 5: // '\005'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (E.R.)");

				case 6: // '\006'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (E.E.)");

				case 7: // '\007'
					setEngineDies(actor1, k);
					break;

				case 29: // '\035'
					setEngineStuck(actor1, k);
					break;

				case 27: // '\033'
					setEngineCylinderKnockOut(actor1, k, i1);
					break;

				case 28: // '\034'
					setEngineMagnetoKnockOut(actor1, k, i1);
					break;

				case 8: // '\b'
					setSootState(actor1, k, i1);
					break;

				case 25: // '\031'
					setEngineReadyness(actor1, k, i1);
					break;

				case 26: // '\032'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (EN.ST.)");

				case 9: // '\t'
					setTankState(actor1, k, i1);
					break;

				case 10: // '\n'
					explodeTank(actor1, k);
					break;

				case 11: // '\013'
					setOilState(actor1, k, i1);
					break;

				case 12: // '\f'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.B./On)");

				case 13: // '\r'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.B./Off)");

				case 14: // '\016'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.C.C./Off)");

				case 15: // '\017'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (A.S.S.)");

				case 30: // '\036'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (NV.LT.ST.)");

				case 31: // '\037'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (LA.LT.ST.)");

				case 16: // '\020'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (W.T.S.)");

				case 17: // '\021'
					setPilotState(actor1, k, i1);
					break;

				case 44: // ','
					setPilotWound(actor1, k, i1);
					break;

				case 45: // '-'
					setBleedingPilot(actor1, k, i1);
					break;

				case 18: // '\022'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented feature (K.P.)");

				case 19: // '\023'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented feature (H.S.)");

				case 20: // '\024'
					throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (B.O.)");

				case 21: // '\025'
					setControlsDamage(actor1, i1);
					break;

				case 22: // '\026'
					setInternalDamage(actor1, i1);
					break;

				case 23: // '\027'
					setCockpitState(actor1, i1);
					break;

				case 24: // '\030'
					setJamBullets(k, i1);
					break;

				case 34: // '"'
					((TypeDockable) aircraft).typeDockableRequestAttach(actor1, k, true);
					break;

				case 35: // '#'
					((TypeDockable) aircraft).typeDockableRequestDetach(actor1, k, true);
					break;

				case 32: // ' '
					((TypeDockable) aircraft).typeDockableRequestAttach(actor1, k, i1 > 0);
					break;

				case 33: // '!'
					((TypeDockable) aircraft).typeDockableRequestDetach(actor1, k, i1 > 0);
					break;

				case 37: // '%'
					setFMSFX(actor1, k, i1);
					break;

				case 41: // ')'
					setBeacon(actor1, k, i1, true);
					break;

				case 42: // '*'
					setGyroAngle(actor1, k, i1, true);
					break;

				case 43: // '+'
					setSpreadAngle(actor1, k, i1, true);
					break;

				case 46: // '.'
					setFuzeStates(actor1, k, i1, true);
					break;

				case 47: // '/'
					setGearState(actor1, k, i1);
					break;

				// TODO: ++ Added Code for Net Replication ++
				case _AS_SELECTED_ROCKET_HOOK:
					this.setRocketHook(actor1, k, true);
					break;

				case _AS_WEAPON_FIRE_MODE:
					this.setWeaponFireMode(actor1, k, true);
					break;

				case _AS_WEAPON_RELEASE_DELAY:
					int releaseDelayLow = k;
					int releaseDelayHigh = i1 << 8;
					this.setWeaponReleaseDelay(actor1, releaseDelayLow + releaseDelayHigh, true);
					break;
				// TODO: -- Added Code for Net Replication --

				// TODO: ++ Added Code for importing 4.13.2m ++
				case _AS_BOMB_MODE_STATES:
					this.setBombModeStates(actor1, k, i1, true);
					break;
				// TODO: -- Added Code for importing 4.13.2m --

				// TODO: ++ Added Code for bomb select ++
				case _AS_CURRENT_BOMB_SELECTED:
					this.setCurrentBombSelected(actor1, k, true);
					break;
				// TODO: ++ Added Code for bomb select ++

				}
			} else {
				aircraft.net.postTo(aircraft.net.masterChannel(), new NetMsgGuaranted(netmsginput, netmsginput.available() <= 3 ? 0 : 1));
			}
		} else {
			if (aircraft.net.isMirrored()) aircraft.net.post(new NetMsgGuaranted(netmsginput, netmsginput.available() <= 3 ? 0 : 1));
			int j = netmsginput.readUnsignedByte();
			int l = netmsginput.readUnsignedByte();
			int j1 = netmsginput.readUnsignedByte();
			Actor actor2 = null;
			if (netmsginput.available() > 0) {
				NetObj netobj1 = netmsginput.readNetObj();
				if (netobj1 != null) actor2 = (Actor) netobj1.superObj();
			}
			switch (j) {
			case 32: // ' '
			case 33: // '!'
			default:
				break;

			case 1: // '\001'
				doSetEngineState(actor2, l, j1);
				break;

			case 2: // '\002'
				doSetEngineSpecificDamage(l, j1);
				break;

			case 3: // '\003'
				doExplodeEngine(l);
				break;

			case 4: // '\004'
				doSetEngineStarts(l);
				break;

			case 5: // '\005'
				doSetEngineRunning(l);
				break;

			case 6: // '\006'
				doSetEngineStops(l);
				break;

			case 7: // '\007'
				doSetEngineDies(l);
				break;

			case 29: // '\035'
				doSetEngineStuck(l);
				break;

			case 27: // '\033'
				doSetEngineCylinderKnockOut(l, j1);
				break;

			case 28: // '\034'
				doSetEngineMagnetoKnockOut(l, j1);
				break;

			case 8: // '\b'
				doSetSootState(l, j1);
				break;

			case 25: // '\031'
				doSetEngineReadyness(l, j1);
				break;

			case 26: // '\032'
				doSetEngineStage(l, j1);
				break;

			case 9: // '\t'
				doSetTankState(actor2, l, j1);
				break;

			case 10: // '\n'
				doExplodeTank(l);
				break;

			case 11: // '\013'
				doSetOilState(l, j1);
				break;

			case 12: // '\f'
				if (j1 == 5) doSetGliderBoostOn();
				else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in signal (G.S.B./On)");
				break;

			case 13: // '\r'
				if (j1 == 7) doSetGliderBoostOff();
				else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in signal (G.S.B./Off)");
				break;

			case 14: // '\016'
				if (j1 == 9) doSetGliderCutCart();
				else throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in signal (G.C.C./Off)");
				break;

			case 15: // '\017'
				doSetAirShowState(j1 == 1);
				break;

			case 30: // '\036'
				doSetNavLightsState(j1 == 1);
				break;

			case 31: // '\037'
				doSetLandingLightState(j1 == 1);
				break;

			case 16: // '\020'
				doSetStallState(j1 == 1);
				break;

			case 17: // '\021'
				doSetPilotState(l, j1, actor2);
				break;

			case 44: // ','
				doSetWoundPilot(j1);
				break;

			case 45: // '-'
				doSetBleedingPilot(l, j1, actor2);
				break;

			case 18: // '\022'
				throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented signal (K.P.)");

			case 19: // '\023'
				throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented signal (H.S.)");

			case 20: // '\024'
				bIsAboutToBailout = true;
				astateBailoutStep = (byte) l;
				bailout();
				break;

			case 21: // '\025'
				throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Uniexpected signal (C.A.D.)");

			case 22: // '\026'
				doSetInternalDamage(j1);
				break;

			case 23: // '\027'
				doSetCockpitState(j1);
				break;

			case 24: // '\030'
				doSetJamBullets(l, j1);
				break;

			case 34: // '"'
				((TypeDockable) aircraft).typeDockableDoAttachToDrone(actor2, l);
				break;

			case 35: // '#'
				((TypeDockable) aircraft).typeDockableDoDetachFromDrone(l);
				break;

			case 36: // '$'
				doSetFlatTopString(actor2, j1);
				break;

			case 37: // '%'
				doSetFMSFX(l, j1);
				break;

			case 38: // '&'
				doSetWingFold(j1);
				break;

			case 39: // '\''
				doSetCockpitDoor(l, j1);
				break;

			case 40: // '('
				doSetArrestor(j1);
				break;

			case 41: // ')'
				doSetBeacon(actor2, l, j1);
				break;

			case 42: // '*'
				doSetGyroAngle(actor2, l, j1);
				break;

			case 43: // '+'
				doSetSpreadAngle(actor2, l, j1);
				break;

			case 46: // '.'
				doSetFuzeStates(actor2, l, j1);
				break;

			case 47: // '/'
				doSetGearState(l, j1);
				break;

			// TODO: ++ Added Code for Net Replication ++
			case _AS_SELECTED_ROCKET_HOOK:
				this.doSetRocketHook(actor2, l);
				break;
			case _AS_WEAPON_FIRE_MODE:
				this.doSetWeaponFireMode(actor2, l);
				break;
			case _AS_WEAPON_RELEASE_DELAY:
				int releaseDelayLow = l;
				int releaseDelayHigh = j1 << 8;
				this.doSetWeaponReleaseDelay(actor2, releaseDelayLow + releaseDelayHigh);
				break;
			// TODO: -- Added Code for Net Replication --

			// TODO: ++ Added Code for importing 4.13.2m ++
			case _AS_BOMB_MODE_STATES:
				this.setBombModeStates(actor2, l, j1, true);
				break;
			// TODO: -- Added Code for importing 4.13.2m --

			// TODO: ++ Added Code for bomb select ++
			case _AS_CURRENT_BOMB_SELECTED:
				this.doSetCurrentBombSelected(actor2, l);
				break;
			// TODO: -- Added Code for bomb select --
			}
		}
	}

	public void netReplicate(NetMsgGuaranted netmsgguaranted) throws IOException {
		int i1;
		if (aircraft instanceof FW_190A8MSTL) i1 = 1;
		else i1 = aircraft.FM.EI.getNum();
		for (int i = 0; i < i1; i++) {
			aircraft.FM.EI.engines[i].replicateToNet(netmsgguaranted);
			netmsgguaranted.writeByte(astateEngineStates[i]);
		}

		for (int j = 0; j < 4; j++)
			netmsgguaranted.writeByte(astateTankStates[j]);

		for (int k = 0; k < i1; k++)
			netmsgguaranted.writeByte(astateOilStates[k]);

		netmsgguaranted.writeByte((bShowSmokesOn ? 1 : 0) | (bNavLightsOn ? 2 : 0) | (bLandingLightOn ? 4 : 0));
		netmsgguaranted.writeByte(astateCockpitState);
		netmsgguaranted.writeByte(astateBailoutStep);
		if (aircraft instanceof TypeBomber) ((TypeBomber) aircraft).typeBomberReplicateToNet(netmsgguaranted);
		if (aircraft instanceof TypeDockable) ((TypeDockable) aircraft).typeDockableReplicateToNet(netmsgguaranted);
		if (aircraft.FM.CT.bHasWingControl) {
			netmsgguaranted.writeByte((int) aircraft.FM.CT.wingControl);
			netmsgguaranted.writeByte((int) (aircraft.FM.CT.getWing() * 255F));
		}
		if (aircraft.FM.CT.bHasCockpitDoorControl) netmsgguaranted.writeByte((int) aircraft.FM.CT.cockpitDoorControl);
		netmsgguaranted.writeByte(bIsEnableToBailout ? 1 : 0);
		if (aircraft.FM.CT.bHasArrestorControl) {
			netmsgguaranted.writeByte((int) aircraft.FM.CT.arrestorControl);
			netmsgguaranted.writeByte((int) (aircraft.FM.CT.getArrestor() * 255F));
		}
		for (int l = 0; l < i1; l++)
			netmsgguaranted.writeByte(astateSootStates[l]);

		netmsgguaranted.writeByte(beacon);
		if (aircraft instanceof TypeHasToKG) {
			netmsgguaranted.writeByte(torpedoGyroAngle);
			netmsgguaranted.writeByte(torpedoSpreadAngle);
		}
		netmsgguaranted.writeShort(armingSeed);
		setArmingSeeds();
		if (aircraft.isNetPlayer()) {
			int j1 = (int) Math.sqrt(World.cur().userCoverMashineGun - 100F) / 6;
			j1 += 6 * ((int) Math.sqrt(World.cur().userCoverCannon - 100F) / 6);
			j1 += 36 * ((int) Math.sqrt(World.cur().userCoverRocket - 100F) / 6);
			netmsgguaranted.writeByte(j1);
		}
		if (externalStoresDropped) netmsgguaranted.writeByte(1);
		else netmsgguaranted.writeByte(0);
		if (World.getPlayerAircraft() == aircraft) replicateFuzeStatesToNet(World.cur().userCfg.fuzeType, Fuze_EL_AZ.getFuzeMode(), World.cur().userCfg.bombDelay);
		for (int k1 = 0; k1 < 3; k1++) {
			int l1 = k1 & 0xf;
			l1 <<= 4;
			l1 += gearDamType[k1] & 0xf;
			netmsgguaranted.writeByte(l1);
			float f = gearStates[k1];
			if (f > 0.0F) {
				netmsgguaranted.writeByte(cvt(f, 0.0F, 1.0F, 128, 255));
				continue;
			}
			if (f < 0.0F) {
				netmsgguaranted.writeByte(cvt(f, -1F, 0.0F, 0, 128));
				continue;
			}
			if (f == 0.0F) netmsgguaranted.writeByte(128);
		}

		// TODO
		netmsgguaranted.writeByte(iAirShowSmoke);
		netmsgguaranted.writeByte(bAirShowSmokeEnhanced ? 1 : 0);
		netmsgguaranted.writeByte(bDumpFuel ? 1 : 0);
		// TODO: ++ Added Code for Net Replication ++
		netmsgguaranted.writeByte(aircraft.FM.CT.rocketHookSelected);
		netmsgguaranted.writeByte(aircraft.FM.CT.weaponFireMode);
		int releaseDelayLow = (byte) aircraft.FM.CT.weaponReleaseDelay;
		int releaseDelayHigh = (byte) ((int) aircraft.FM.CT.weaponReleaseDelay >> 8);
		netmsgguaranted.writeByte(releaseDelayLow);
		netmsgguaranted.writeByte(releaseDelayHigh);
		// TODO: -- Added Code for Net Replication --
		// TODO: Bay Doors
		if (aircraft.FM.CT.bHasBayDoors) netmsgguaranted.writeByte((int) aircraft.FM.CT.BayDoorControl);
		// TODO: importing 4.13.2m , changing not limited to PlayerAircraft
		replicateBombModeStatesToNet();
		// TODO: current Bomb Selected for bHasBombSelect
		netmsgguaranted.writeByte(aircraft.FM.CT.curBombSelected);
	}

	public void netFirstUpdate(NetMsgInput netmsginput) throws IOException {
		int k2;
		if (aircraft instanceof FW_190A8MSTL) k2 = 1;
		else k2 = aircraft.FM.EI.getNum();
		for (int i = 0; i < k2; i++) {
			aircraft.FM.EI.engines[i].replicateFromNet(netmsginput);
			int k1 = netmsginput.readUnsignedByte();
			doSetEngineState(null, i, k1);
		}

		for (int j = 0; j < 4; j++) {
			int l1 = netmsginput.readUnsignedByte();
			doSetTankState(null, j, l1);
		}

		for (int k = 0; k < k2; k++) {
			int i2 = netmsginput.readUnsignedByte();
			doSetOilState(k, i2);
		}

		int j2 = netmsginput.readUnsignedByte();
		doSetAirShowState((j2 & 1) != 0);
		doSetNavLightsState((j2 & 2) != 0);
		doSetLandingLightState((j2 & 4) != 0);
		j2 = netmsginput.readUnsignedByte();
		doSetCockpitState(j2);
		j2 = netmsginput.readUnsignedByte();
		if (j2 != 0) {
			bIsAboutToBailout = true;
			astateBailoutStep = (byte) j2;
			for (int l = 1; l <= Math.min(astateBailoutStep, 3); l++)
				if (aircraft.hierMesh().chunkFindCheck("Blister" + (l - 1) + "_D0") != -1) aircraft.hierMesh().hideSubTrees("Blister" + (l - 1) + "_D0");

			if (astateBailoutStep >= 11 && astateBailoutStep <= 20) {
				int l2 = astateBailoutStep;
				if (astateBailoutStep == 20) l2 = 19;
				l2 -= 11;
				for (int i1 = 0; i1 <= l2; i1++)
					doRemoveBodyFromPlane(i1 + 1);

			}
		}
		// TODO /Storebro's Update
		if (netmsginput.available() == 0) return;
		netmsginput.fixed();
		try {
			if (aircraft instanceof TypeBomber) ((TypeBomber) aircraft).typeBomberReplicateFromNet(netmsginput);
		} catch (Exception e) {
			netmsginput.reset();
		}
		if (netmsginput.available() == 0) return;
		netmsginput.fixed();
		try {
			if (aircraft instanceof TypeDockable) ((TypeDockable) aircraft).typeDockableReplicateFromNet(netmsginput);
		} catch (Exception e) {
			netmsginput.reset();
		}

		// /Storebro's Update

		if (aircraft.FM.CT.bHasWingControl) {
			aircraft.FM.CT.wingControl = netmsginput.readUnsignedByte();
			aircraft.FM.CT.forceWing((float) netmsginput.readUnsignedByte() / 255F);
			aircraft.wingfold_ = aircraft.FM.CT.getWing();
		}
		if (netmsginput.available() == 0) return;
		if (aircraft.FM.CT.bHasCockpitDoorControl) {
			aircraft.FM.CT.cockpitDoorControl = netmsginput.readUnsignedByte();
			aircraft.FM.CT.forceCockpitDoor(aircraft.FM.CT.cockpitDoorControl);
		}
		if (netmsginput.available() == 0) return;
		bIsEnableToBailout = netmsginput.readUnsignedByte() == 1;
		if (netmsginput.available() == 0) return;
		if (aircraft.FM.CT.bHasArrestorControl) {
			aircraft.FM.CT.arrestorControl = netmsginput.readUnsignedByte();
			aircraft.FM.CT.forceArrestor((float) netmsginput.readUnsignedByte() / 255F);
			aircraft.arrestor_ = aircraft.FM.CT.getArrestor();
		}
		if (netmsginput.available() == 0) return;
		for (int j1 = 0; j1 < k2; j1++) {
			j2 = netmsginput.readUnsignedByte();
			doSetSootState(j1, j2);
		}

		if (netmsginput.available() == 0) return;
		if (!NetMissionTrack.isPlaying() || bWantBeaconsNet || NetMissionTrack.playingOriginalVersion() >= 105) {
			int i3 = netmsginput.readUnsignedByte();
			doSetBeacon(aircraft, i3, 0);
		}
		if (aircraft instanceof TypeHasToKG) {
			torpedoGyroAngle = netmsginput.readUnsignedByte();
			torpedoSpreadAngle = netmsginput.readUnsignedByte();
		}
		armingSeed = netmsginput.readUnsignedShort();
		setArmingSeeds();
		if (aircraft.isNetPlayer()) {
			int j3 = netmsginput.readUnsignedByte();
			int l3 = j3 % 6;
			float f = Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F);
			updConvDist(l3, 0, f);
			j3 = (j3 - l3) / 6;
			l3 = j3 % 6;
			updConvDist(l3, 1, f);
			j3 = (j3 - l3) / 6;
			updConvDist(j3, 2, f);
		}
		j2 = netmsginput.readUnsignedByte();
		if (!externalStoresDropped && j2 > 0) {
			externalStoresDropped = true;
			aircraft.dropExternalStores(false);
		}
		if (netmsginput.available() == 0) return;
		for (int k3 = 0; k3 < 3; k3++) {
			int i4 = netmsginput.readUnsignedByte();
			int j4 = netmsginput.readUnsignedByte();
			doSetGearState(i4, j4);
		}
		// TODO
		setAirShowSmokeType(netmsginput.readUnsignedByte());
		setAirShowSmokeEnhanced(netmsginput.readUnsignedByte() == 1);
		doSetDumpFuelState(netmsginput.readUnsignedByte() == 1);
		// TODO: ++ Added Code for Net Replication ++
		if (netmsginput.available() == 0) return;
		this.doSetRocketHook(aircraft, netmsginput.readUnsignedByte());
		this.doSetWeaponFireMode(aircraft, netmsginput.readUnsignedByte());
		int releaseDelayLow = netmsginput.readUnsignedByte();
		int releaseDelayHigh = netmsginput.readUnsignedByte() << 8;
		this.doSetWeaponReleaseDelay(aircraft, releaseDelayLow + releaseDelayHigh);
		// TODO: -- Added Code for Net Replication --
		if (aircraft.FM.CT.bHasBayDoorControl) aircraft.FM.CT.BayDoorControl = netmsginput.readUnsignedByte();
	}

	void updConvDist(int i, int j, float f) {
		float f1 = (float) i * (float) i * 36F + 100F;
		try {
			if (aircraft.FM.CT.Weapons[j] != null) if (j == 2) {
				for (int k = 0; k < aircraft.FM.CT.Weapons[j].length; k++)
					if (aircraft.FM.CT.Weapons[j][k] instanceof RocketGun) ((RocketGun) (RocketGun) aircraft.FM.CT.Weapons[j][k]).setConvDistance(f1, f);

			} else {
				for (int l = 0; l < aircraft.FM.CT.Weapons[j].length; l++)
					if (aircraft.FM.CT.Weapons[j][l] instanceof MGunAircraftGeneric) ((MGunAircraftGeneric) (MGunAircraftGeneric) aircraft.FM.CT.Weapons[j][l]).setConvDistance(f1, f);

			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	public void setArmingSeeds() {
		armingRnd = new RangeRandom(armingSeed);
		try {
			if (aircraft.FM.CT.Weapons[2] != null) {
				for (int k = 0; k < aircraft.FM.CT.Weapons[2].length; k++)
					if (aircraft.FM.CT.Weapons[2][k] instanceof RocketGun) {
						((RocketGun) aircraft.FM.CT.Weapons[2][k]).setSpreadRnd(armingRnd.nextInt());
					}

			}
			if (aircraft.FM.CT.Weapons[3] != null) {
				for (int l = 0; l < aircraft.FM.CT.Weapons[3].length; l++)
					if ((aircraft.FM.CT.Weapons[3][l] instanceof BombGun) && !(aircraft.FM.CT.Weapons[3][l] instanceof FuelTankGun)) {
						int j = armingRnd.nextInt();
						((BombGun) aircraft.FM.CT.Weapons[3][l]).setRnd(j);
					}

			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private void netToMaster(int i, int j, int k) {
		netToMaster(i, j, k, null);
	}

	public void netToMaster(int i, int j, int k, Actor actor1) {
		if (!bIsMaster) {
			if (!aircraft.netNewAState_isEnable(true)) return;
			if (itemsToMaster == null) itemsToMaster = new Item[48];
			if (sendedMsg(itemsToMaster, i, j, k, actor1)) return;
			try {
				NetMsgGuaranted netmsgguaranted = aircraft.netNewAStateMsg(true);
				if (netmsgguaranted != null) {
					netmsgguaranted.writeByte((byte) i);
					netmsgguaranted.writeByte((byte) j);
					netmsgguaranted.writeByte((byte) k);
					ActorNet actornet = null;
					if (Actor.isValid(actor1)) actornet = actor1.net;
					if (actornet != null) netmsgguaranted.writeNetObj(actornet);
					aircraft.netSendAStateMsg(true, netmsgguaranted);
					return;
				}
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void netToMirrors(int i, int j, int k) {
		netToMirrors(i, j, k, null);
	}

	public void netToMirrors(int i, int j, int k, Actor actor1) {
		if (!aircraft.netNewAState_isEnable(false)) return;
		if (itemsToMirrors == null) itemsToMirrors = new Item[48];
		if (sendedMsg(itemsToMirrors, i, j, k, actor1)) return;
		try {
			NetMsgGuaranted netmsgguaranted = aircraft.netNewAStateMsg(false);
			if (netmsgguaranted != null) {
				netmsgguaranted.writeByte((byte) i);
				netmsgguaranted.writeByte((byte) j);
				netmsgguaranted.writeByte((byte) k);
				ActorNet actornet = null;
				if (Actor.isValid(actor1)) actornet = actor1.net;
				if (actornet != null) netmsgguaranted.writeNetObj(actornet);
				aircraft.netSendAStateMsg(false, netmsgguaranted);
				return;
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private boolean sendedMsg(Item aitem[], int i, int j, int k, Actor actor1) {
		if (i < 0 || i >= aitem.length) return false;
		Item item = aitem[i];
		if (item == null) {
			item = new Item();
			item.set(j, k, actor1);
			aitem[i] = item;
			return false;
		}
		if (item.equals(j, k, actor1)) {
			return true;
		} else {
			item.set(j, k, actor1);
			return false;
		}
	}

	private int cvt(float f, float f1, float f2, int i, int j) {
		f = Math.min(Math.max(f, f1), f2);
		return (int) ((float) i + ((float) (j - i) * (f - f1)) / (f2 - f1));
	}

	private float cvt(int i, int j, int k, float f, float f1) {
		i = Math.min(Math.max(i, j), k);
		return f + ((f1 - f) * (float) (i - j)) / (float) (k - j);
	}

	public float getGyroAngle() {
		if (torpedoGyroAngle == 0) return 0.0F;
		else return cvt(torpedoGyroAngle, 1, 65535, -50F, 50F);
	}

	public void setGyroAngle(float f) {
		int i = 0;
		if (f != 0.0F) i = cvt(f, -50F, 50F, 1, 65535);
		torpedoGyroAngle = i;
	}

	public void replicateGyroAngleToNet() {
		int i = cvt(getGyroAngle(), -50F, 50F, 1, 65535);
		int j = i & 0xff00;
		j >>= 8;
		int k = i & 0xff;
		setGyroAngle(actor, j, k, false);
	}

	private void setGyroAngle(Actor actor1, int i, int j, boolean flag) {
		if (!Actor.isValid(actor1)) return;
		if (flag) doSetGyroAngle(actor1, i, j);
		if (bIsMaster) netToMirrors(42, i, j);
		else netToMaster(42, i, j, actor1);
	}

	private void doSetGyroAngle(Actor actor1, int i, int j) {
		torpedoGyroAngle = i << 8 | j;
	}

	public int getSpreadAngle() {
		return torpedoSpreadAngle;
	}

	public void setSpreadAngle(int i) {
		torpedoSpreadAngle = i;
		if (torpedoSpreadAngle < 0) torpedoSpreadAngle = 0;
		if (torpedoSpreadAngle > 30) torpedoSpreadAngle = 30;
	}

	public void replicateSpreadAngleToNet() {
		setSpreadAngle(actor, getSpreadAngle(), 0, false);
	}

	private void setSpreadAngle(Actor actor1, int i, int j, boolean flag) {
		if (!Actor.isValid(actor1)) return;
		if (flag) doSetSpreadAngle(actor1, i, 0);
		if (bIsMaster) netToMirrors(43, i, 0);
		else netToMaster(43, i, 0, actor1);
	}

	private void doSetSpreadAngle(Actor actor1, int i, int j) {
		setSpreadAngle(i);
	}

	public void replicateFuzeStatesToNet(int i, int j, float f) {
		int k = (int) (f * 2.0F);
		int l = i & 0xf;
		l <<= 4;
		l += j & 0xf;
		setFuzeStates(actor, l, k, false);
	}

	private void setFuzeStates(Actor actor1, int i, int j, boolean flag) {
		if (!Actor.isValid(actor1)) return;
		if (flag) doSetFuzeStates(actor1, i, j);
		if (bIsMaster) netToMirrors(46, i, j);
		else netToMaster(46, i, j, actor1);
	}

	private void doSetFuzeStates(Actor actor1, int i, int j) {
		float f = (float) j / 2.0F;
		int k = (i & 0xf0) >> 4;
		int l = i & 0xf;
		if (aircraft.FM.CT.Weapons[3] != null) {
			for (int i1 = 0; i1 < aircraft.FM.CT.Weapons[3].length; i1++)
				if (aircraft.FM.CT.Weapons[3][i1] != null && (aircraft.FM.CT.Weapons[3][i1] instanceof BombGun) && aircraft.FM.CT.Weapons[3][i1].countBullets() != 0) {
					BombGun bombgun = (BombGun) aircraft.FM.CT.Weapons[3][i1];
					bombgun.setFuzeForMirror(k, l, f);
				}

		}
	}

			// TODO: ++ Added Code for importing 4.13.2m ++
	public void replicateBombModeStatesToNet() {
		int i = aircraft.FM.CT.bombReleaseMode & 0xf;
		i <<= 4;
		i += aircraft.FM.CT.bombTrainDelay & 0xf;
		int j = aircraft.FM.CT.bombTrainAmount;
		setBombModeStates(actor, i, j, false);
	}

	private void setBombModeStates(Actor actor1, int i, int j, boolean flag) {
		if (!Actor.isValid(actor1))
			return;
		if (flag)
			doSetBombModeStates(actor1, i, j);
		if (bIsMaster)
			netToMirrors(_AS_BOMB_MODE_STATES, i, j);
		else
			netToMaster(_AS_BOMB_MODE_STATES, i, j, actor1);
	}

	private void doSetBombModeStates(Actor actor1, int i, int j) {
		int k = (i & 0xf0) >> 4;
		int l = i & 0xf;
		aircraft.FM.CT.bombReleaseMode = k;
		aircraft.FM.CT.bombTrainDelay = l;
		aircraft.FM.CT.bombTrainAmount = j;
	}
			// TODO: -- Added Code for importing 4.13.2m --

	private void doSetGearState(int i, int j) {
		int k = (i & 0xf0) >> 4;
		int l = i & 0xf;
		int i1 = j - 128;
		if (k > 2) {
			k -= 3;
			aircraft.hitGearPanels(k, l);
		}
		gearDamType[k] = (byte) l;
		if (k > 2) return;
		if (i1 > 0) gearStates[k] = cvt(i1, 0, 127, 0.0F, 1.0F);
		else if (i1 < 0) gearStates[k] = cvt(i1, -128, 0, -1F, 0.0F);
		else if (i1 == 0) gearStates[k] = 0.0F;
		if ((k == 0 || k == 1) && (l == 6 || l == 4)) aircraft.FM.setCapableOfACM(false);
		if (l == 9) aircraft.FM.Gears.setHydroOperable(false);
		if (gearDamType[0] == 8 && gearDamType[1] == 8) aircraft.FM.CT.bHasBrakeControl = false;
	}

	public void setGearState(Actor actor1, int i, int j) {
		if (!Actor.isValid(actor1)) return;
		if (bIsMaster) {
			doSetGearState(i, j);
			netToMirrors(47, i, j);
		} else {
			netToMaster(47, i, j, actor1);
		}
	}

	public void hitGear(Actor actor1, int i, int j) {
		int k = i;
		if (k > 2) k -= 3;
		if (gearStates[k] != 0.0F || k < 0 || k > 2) return;
		if (!aircraft.FM.Gears.lgear && k == 0 || !aircraft.FM.Gears.rgear && k == 1) return;
		byte byte0 = 0;
		if (j == 12) {
			hitGear(actor1, i, TrueRandom.nextInt(1, 10));
			return;
		}
		if (j == 1) {
			byte0 = -125;
			gearDamRecoveryStates[k] = (byte) TrueRandom.nextInt(0, 2);
		} else if (j == 2) {
			byte0 = -2;
			gearDamRecoveryStates[k] = (byte) TrueRandom.nextInt(0, 2);
		} else if (j == 3) {
			float f = TrueRandom.nextFloat(0.1F, 0.9F);
			byte0 = (byte) (int) (f * -128F);
			gearDamRecoveryStates[k] = (byte) TrueRandom.nextInt(0, 3);
		} else if (j == 4) byte0 = 124;
		else if (j == 5) byte0 = 6;
		else if (j == 6) {
			float f1 = TrueRandom.nextFloat(0.2F, 0.8F);
			byte0 = (byte) (int) (f1 * 127F);
		} else if (j == 7) byte0 = 0;
		else if (j == 8) byte0 = 0;
		else if (j == 9) {
			byte0 = 0;
			gearDamRecoveryStates[k] = 3;
		} else if (j == 10) byte0 = -128;
		else if (j == 11) byte0 = (byte) (int) (Math.abs(gearStates[k]) * 127F);
		int l = i & 0xf;
		l <<= 4;
		l += j & 0xf;
		int i1 = byte0 + 128;
		setGearState(actor1, l, i1);
	}

	public void fixGear(Actor actor1, int i) {
		int j = i & 0xf;
		j <<= 4;
		if (gearStates[i] == 0.0F) {
			return;
		} else {
			setGearState(actor1, j, 128);
			return;
		}
	}

	public boolean isEnginesOnFire() {
		for (int i = 0; i < aircraft.FM.EI.getNum(); i++)
			if (astateEngineStates[i] > 3) return true;

		return false;
	}

	// TODO: ++ Added Code for Net Replication ++
	public void replicateRocketHookToNet(int theRocketHook) {
		setRocketHook(actor, theRocketHook, false);
	}

	private void setRocketHook(Actor theActor, int theRocketHook, boolean applySetting) {
		if (!Actor.isValid(theActor)) return;
		if (applySetting) doSetRocketHook(theActor, theRocketHook);
		if (bIsMaster) netToMirrors(_AS_SELECTED_ROCKET_HOOK, theRocketHook, 0);
		else netToMaster(_AS_SELECTED_ROCKET_HOOK, theRocketHook, 0, theActor);
	}

	private void doSetRocketHook(Actor theActor, int theRocketHook) {
		aircraft.FM.CT.doSetRocketHook(theRocketHook);
	}

	public void replicateWeaponFireModeToNet(int theWeaponFireMode) {
		setWeaponFireMode(actor, theWeaponFireMode, false);
	}

	private void setWeaponFireMode(Actor theActor, int theWeaponFireMode, boolean applySetting) {
		if (!Actor.isValid(theActor)) return;
		if (applySetting) doSetWeaponFireMode(theActor, theWeaponFireMode);
		if (bIsMaster) netToMirrors(_AS_WEAPON_FIRE_MODE, theWeaponFireMode, 0);
		else netToMaster(_AS_WEAPON_FIRE_MODE, theWeaponFireMode, 0, theActor);
	}

	private void doSetWeaponFireMode(Actor theActor, int theWeaponFireMode) {
		aircraft.FM.CT.doSetWeaponFireMode(theWeaponFireMode);
	}

	public void replicateWeaponReleaseDelay(long theWeaponReleaseDelay) {
		setWeaponReleaseDelay(actor, theWeaponReleaseDelay, false);
	}

	private void setWeaponReleaseDelay(Actor theActor, long theWeaponReleaseDelay, boolean applySetting) {
		if (!Actor.isValid(theActor)) return;
		if (applySetting) doSetWeaponReleaseDelay(theActor, theWeaponReleaseDelay);
		int releaseDelayLow = (byte) aircraft.FM.CT.weaponReleaseDelay;
		int releaseDelayHigh = (byte) ((int) aircraft.FM.CT.weaponReleaseDelay >> 8);
		if (bIsMaster) netToMirrors(_AS_WEAPON_RELEASE_DELAY, releaseDelayLow, releaseDelayHigh);
		else netToMaster(_AS_WEAPON_RELEASE_DELAY, releaseDelayLow, releaseDelayHigh, theActor);
	}

	private void doSetWeaponReleaseDelay(Actor theActor, long theWeaponReleaseDelay) {
		aircraft.FM.CT.doSetWeaponReleaseDelay(theWeaponReleaseDelay);
	}

	public void replicateCurrentBombSelectedToNet(int theCurrentBombSelected) {
		setCurrentBombSelected(actor, theCurrentBombSelected, false);
	}

	private void setCurrentBombSelected(Actor theActor, int theCurrentBombSelected, boolean applySetting) {
		if (!Actor.isValid(theActor)) return;
		if (applySetting) doSetWeaponFireMode(theActor, theCurrentBombSelected);
		if (bIsMaster) netToMirrors(_AS_CURRENT_BOMB_SELECTED, theCurrentBombSelected, 0);
		else netToMaster(_AS_CURRENT_BOMB_SELECTED, theCurrentBombSelected, 0, theActor);
	}

	private void doSetCurrentBombSelected(Actor theActor, int theCurrentBombSelected) {
		aircraft.FM.CT.doSetCurrentBombSelected(theCurrentBombSelected);
	}
	// TODO: -- Added Code for Net Replication --
}