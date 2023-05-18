/*Modified AircraftState class for the SAS Engine Mod*/
package com.maddox.il2.fm;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.*;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.*;
import com.maddox.il2.objects.weapons.MGunAircraftGeneric;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.*;
import com.maddox.sound.CmdMusic;
import java.io.*;

public class AircraftState
{
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
    public static final int _AS_COUNT_CODES = 41;
    public static final int _AS_BEACONS = 42;
    public static final int _AS_GYROANGLE = 44;
    public static final int _AS_SPREADANGLE = 45;
    public static final int _AS_PILOT_WOUNDED = 46;
    public static final int _AS_PILOT_BLEEDING = 47;
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
    protected long bleedingTime;
    public long astateBleedingTimes[];
    public long astateBleedingNext[];
    private boolean legsWounded;
    private boolean armsWounded;
    public int torpedoGyroAngle;
    public int torpedoSpreadAngle;
    public static final int spreadAngleLimit = 30;
    private int beacon;
    private boolean bWantBeaconsNet;
    public static final int MAX_NUMBER_OF_BEACONS = 32;
    public boolean listenLorenzBlindLanding;
    public boolean isAAFIAS;
    public boolean listenYGBeacon;
    public boolean listenNDBeacon;
    public boolean listenRadioStation;
    public Actor hayrakeCarrier;
    public String hayrakeCode;
    public static int hudLogBeaconId = HUD.makeIdLog();
    public boolean externalStoresDropped;
    private static final String astateOilStrings[] = {
        "3DO/Effects/Aircraft/BlackMediumSPD.eff", "3DO/Effects/Aircraft/BlackMediumTSPD.eff", null, null
    };
    private static final String astateTankStrings[] = {
        null, null, null, "3DO/Effects/Aircraft/RedLeakTSPD.eff", null, null, "3DO/Effects/Aircraft/RedLeakTSPD.eff", null, null, "3DO/Effects/Aircraft/BlackMediumSPD.eff", 
        "3DO/Effects/Aircraft/BlackMediumTSPD.eff", null, "3DO/Effects/Aircraft/BlackHeavySPD.eff", "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", null, "3DO/Effects/Aircraft/FireSPD.eff", "3DO/Effects/Aircraft/BlackHeavySPD.eff", "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", "3DO/Effects/Aircraft/FireSPD.eff", "3DO/Effects/Aircraft/BlackHeavySPD.eff", 
        "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", null, null, null, "3DO/Effects/Aircraft/RedLeakGND.eff", null, null, "3DO/Effects/Aircraft/RedLeakGND.eff", null, null, 
        "3DO/Effects/Aircraft/BlackMediumGND.eff", null, null, "3DO/Effects/Aircraft/BlackHeavyGND.eff", null, null, "3DO/Effects/Aircraft/FireGND.eff", "3DO/Effects/Aircraft/BlackHeavyGND.eff", null, "3DO/Effects/Aircraft/FireGND.eff", 
        "3DO/Effects/Aircraft/BlackHeavyGND.eff", null
    };
    private static final String astateEngineStrings[] = {
        null, null, null, "3DO/Effects/Aircraft/GraySmallSPD.eff", "3DO/Effects/Aircraft/GraySmallTSPD.eff", null, "3DO/Effects/Aircraft/BlackMediumSPD.eff", "3DO/Effects/Aircraft/BlackMediumTSPD.eff", null, "3DO/Effects/Aircraft/BlackHeavySPD.eff", 
        "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", null, "3DO/Effects/Aircraft/FireSPD.eff", "3DO/Effects/Aircraft/BlackHeavySPD.eff", "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", null, null, null, "3DO/Effects/Aircraft/GraySmallGND.eff", null, 
        null, "3DO/Effects/Aircraft/BlackMediumGND.eff", null, null, "3DO/Effects/Aircraft/BlackHeavyGND.eff", null, null, "3DO/Effects/Aircraft/FireGND.eff", "3DO/Effects/Aircraft/BlackHeavyGND.eff", null
    };
    private static final String astateCondensateStrings[] = {
        null, "3DO/Effects/Aircraft/CondensateTSPD.eff"
    };
    private static final String astateStallStrings[] = {
        null, "3DO/Effects/Aircraft/StallTSPD.eff"
    };
    public static final String astateHUDPilotHits[] = {
        "Player", "Pilot", "CPilot", "NGunner", "TGunner", "WGunner", "VGunner", "RGunner", "EngMas", "BombMas", 
        "RadMas", "ObsMas"
    };
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
    public byte astatePilotFunctions[] = {
        1, 7, 7, 7, 7, 7, 7, 7, 7
    };
    public int astatePlayerIndex;
    public boolean bIsAboutToBailout;
    public boolean bIsEnableToBailout;
    public byte astateBailoutStep;
    public int astateCockpitState;
    public byte astateOilStates[];
    private Eff3DActor astateOilEffects[][] = {
        new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2]
    };
    public byte astateTankStates[];
    private Eff3DActor astateTankEffects[][] = {
        new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3]
    };
    public byte astateEngineStates[];
    private Eff3DActor astateEngineEffects[][] = {
        new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3], new Eff3DActor[3]
    };
    public byte astateSootStates[];
    public Eff3DActor astateSootEffects[][] = {
        new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2]
    };
    private Eff3DActor astateCondensateEffects[];
    private Eff3DActor astateStallEffects[];
    private Eff3DActor astateDumpFuelEffects[];
    private Eff3DActor astateAirShowEffects[];
    private Eff3DActor astateNavLightsEffects[];
    private LightPointActor astateNavLightsLights[];
    public Eff3DActor astateLandingLightEffects[];
    private LightPointActor astateLandingLightLights[];
    public String astateEffectChunks[];
    public static final int astateEffectsDispTanks = 0;
    public static final int astateEffectsDispEngines = 4;
    public static final int astateEffectsDispLights = 12;
    public static final int astateEffectsDispLandingLights = 18;
    public static final int astateEffectsDispOilfilters = 22;
    public static boolean bCheckPlayerAircraft = true;
    private Item itemsToMaster[];
    private Item itemsToMirrors[];
	// -------------------------------------------------------------

	// TODO: New Parameters
	// -------------------------------------------------------------
	private int iAirShowSmoke;
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
	// TODO: -- Added Code for Net Replication --
	// -------------------------------------------------------------
    static class Item
    {

        void set(int paramInt1, int paramInt2, Actor paramActor)
        {
            msgDestination = paramInt1;
            msgContext = paramInt2;
            initiator = paramActor;
        }

        boolean equals(int paramInt1, int paramInt2, Actor paramActor)
        {
            return msgDestination == paramInt1 && msgContext == paramInt2 && initiator == paramActor;
        }

        int msgDestination;
        int msgContext;
        Actor initiator;

        Item()
        {
        }
    }


    public AircraftState()
    {
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
        listenYGBeacon = false;
        listenNDBeacon = false;
        listenRadioStation = false;
        hayrakeCarrier = null;
        hayrakeCode = null;
        externalStoresDropped = false;
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
        astateOilStates = new byte[8];
        astateTankStates = new byte[4];
        astateEngineStates = new byte[8];
        astateSootStates = new byte[8];
        astateCondensateEffects = new Eff3DActor[8];
        astateStallEffects = new Eff3DActor[2];
        astateDumpFuelEffects = new Eff3DActor[3];
        astateAirShowEffects = new Eff3DActor[3];
        astateNavLightsEffects = new Eff3DActor[6];
        astateNavLightsLights = new LightPointActor[6];
        astateLandingLightEffects = new Eff3DActor[4];
        astateLandingLightLights = new LightPointActor[4];
        astateEffectChunks = new String[30];
        itemsToMaster = null;
        itemsToMirrors = null;
		//TODO: New Parameters
        iAirShowSmoke = 0;
        bAirShowSmokeEnhanced = false;
        iAirShowSmoke = Config.cur.ini.get("Mods", "AirShowSmoke", 0);
        if(iAirShowSmoke < 1 || iAirShowSmoke > 3)
            iAirShowSmoke = 0;
        if(Config.cur.ini.get("Mods", "AirShowSmokeEnhanced", 0) > 0)
            bAirShowSmokeEnhanced = true;
        bDumpFuel = false;
        COCKPIT_DOOR = 1;
        SIDE_DOOR = 2;
    }

    public void set(Actor paramActor, boolean paramBoolean)
    {
        Loc localLoc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        Loc localLoc2 = new Loc();
        actor = paramActor;
        if(paramActor instanceof Aircraft)
            aircraft = (AircraftLH)paramActor;
        else
            throw new RuntimeException("Can not cast aircraft structure into a non-aircraft entity.");
        bIsMaster = paramBoolean;
        for(int i = 0; i < 4; i++)
            try
            {
                astateEffectChunks[i + 0] = actor.findHook("_Tank" + (i + 1) + "Burn").chunkName();
                astateEffectChunks[i + 0] = astateEffectChunks[i + 0].substring(0, astateEffectChunks[i + 0].length() - 1);
                Aircraft.debugprintln(aircraft, "AS: Tank " + i + " FX attached to '" + astateEffectChunks[i + 0] + "' substring..");
            }
            catch(Exception exception) { }

        for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); i++)
            try
            {
                astateEffectChunks[i + 4] = actor.findHook("_Engine" + (i + 1) + "Smoke").chunkName();
                astateEffectChunks[i + 4] = astateEffectChunks[i + 4].substring(0, astateEffectChunks[i + 4].length() - 1);
                Aircraft.debugprintln(aircraft, "AS: Engine " + i + " FX attached to '" + astateEffectChunks[i + 4] + "' substring..");
            }
            catch(Exception exception1) { }

        for(int i = 0; i < astateNavLightsEffects.length; i++)
            try
            {
                astateEffectChunks[i + 12] = actor.findHook("_NavLight" + i).chunkName();
                astateEffectChunks[i + 12] = astateEffectChunks[i + 12].substring(0, astateEffectChunks[i + 12].length() - 1);
                Aircraft.debugprintln(aircraft, "AS: Nav. Lamp #" + i + " attached to '" + astateEffectChunks[i + 12] + "' substring..");
                HookNamed localHookNamed1 = new HookNamed(aircraft, "_NavLight" + i);
                localLoc2.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                localHookNamed1.computePos(actor, localLoc1, localLoc2);
                com.maddox.JGP.Point3d localPoint3d = localLoc2.getPoint();
                astateNavLightsLights[i] = new LightPointActor(new LightPoint(), localPoint3d);
                if(i < 2)
                    astateNavLightsLights[i].light.setColor(1.0F, 0.1F, 0.1F);
                else
                if(i < 4)
                    astateNavLightsLights[i].light.setColor(0.0F, 1.0F, 0.0F);
                else
                    astateNavLightsLights[i].light.setColor(0.7F, 0.7F, 0.7F);
                astateNavLightsLights[i].light.setEmit(0.0F, 0.0F);
                actor.draw.lightMap().put("_NavLight" + i, astateNavLightsLights[i]);
            }
            catch(Exception exception2) { }

        for(int i = 0; i < 4; i++)
            try
            {
                astateEffectChunks[i + 18] = actor.findHook("_LandingLight0" + i).chunkName();
                astateEffectChunks[i + 18] = astateEffectChunks[i + 18].substring(0, astateEffectChunks[i + 18].length() - 1);
                Aircraft.debugprintln(aircraft, "AS: Landing Lamp #" + i + " attached to '" + astateEffectChunks[i + 18] + "' substring..");
                HookNamed localHookNamed2 = new HookNamed(aircraft, "_LandingLight0" + i);
                localLoc2.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                localHookNamed2.computePos(actor, localLoc1, localLoc2);
                com.maddox.JGP.Point3d localPoint3d = localLoc2.getPoint();
                astateLandingLightLights[i] = new LightPointActor(new LightPoint(), localPoint3d);
                astateLandingLightLights[i].light.setColor(0.4941177F, 0.9098039F, 0.9607843F);
                astateLandingLightLights[i].light.setEmit(0.0F, 0.0F);
                actor.draw.lightMap().put("_LandingLight0" + i, astateLandingLightLights[i]);
            }
            catch(Exception exception3) { }

        for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); i++)
            try
            {
                astateEffectChunks[i + 22] = actor.findHook("_Engine" + (i + 1) + "Oil").chunkName();
                astateEffectChunks[i + 22] = astateEffectChunks[i + 22].substring(0, astateEffectChunks[i + 22].length() - 1);
                Aircraft.debugprintln(aircraft, "AS: Oilfilter " + i + " FX attached to '" + astateEffectChunks[i + 22] + "' substring..");
            }
            catch(Exception exception4) { }

        for(int i = 0; i < astateEffectChunks.length; i++)
            if(astateEffectChunks[i] == null)
                astateEffectChunks[i] = "AChunkNameYouCanNeverFind";

    }

    public boolean isMaster()
    {
        return bIsMaster;
    }

    public void setOilState(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramInt2 < 0 || paramInt2 > 1 || astateOilStates[paramInt1] == paramInt2)
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetOilState(paramInt1, paramInt2);
            int i = 0;
            for(int j = 0; j < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); j++)
                if(astateOilStates[j] == 1)
                    i++;

            if(i == ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum())
                setCockpitState(actor, astateCockpitState | 0x80);
            netToMirrors(11, paramInt1, paramInt2);
        } else
        {
            netToMaster(11, paramInt1, paramInt2, paramActor);
        }
    }

    public void hitOil(Actor paramActor, int paramInt)
    {
        if(astateOilStates[paramInt] > 0)
            return;
        if(astateOilStates[paramInt] < 1)
            setOilState(paramActor, paramInt, astateOilStates[paramInt] + 1);
    }

    public void repairOil(int paramInt)
    {
        if(!bIsMaster)
            return;
        if(astateOilStates[paramInt] > 0)
            setOilState(actor, paramInt, astateOilStates[paramInt] - 1);
    }

    private void doSetOilState(int paramInt1, int paramInt2)
    {
        if(astateOilStates[paramInt1] == paramInt2)
            return;
        Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[paramInt1 + 22] + "' visibility..");
        boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt1 + 22]);
        Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[paramInt1 + 22] + "' is " + (bool ? "visible" : "invisible") + "..");
        Aircraft.debugprintln(aircraft, "Stating OilFilter " + paramInt1 + " to state " + paramInt2 + (bool ? ".." : " rejected (missing part).."));
        if(!bool)
            return;
        Aircraft.debugprintln(aircraft, "Stating OilFilter " + paramInt1 + " to state " + paramInt2 + "..");
        astateOilStates[paramInt1] = (byte)paramInt2;
        int i = 0;
        if(!bIsAboveCriticalSpeed)
            i = 2;
        if(astateOilEffects[paramInt1][0] != null)
            Eff3DActor.finish(astateOilEffects[paramInt1][0]);
        astateOilEffects[paramInt1][0] = null;
        if(astateOilEffects[paramInt1][1] != null)
            Eff3DActor.finish(astateOilEffects[paramInt1][1]);
        astateOilEffects[paramInt1][1] = null;
        switch(astateOilStates[paramInt1])
        {
        case 1: // '\001'
            String str = astateOilStrings[i];
            if(str != null)
                try
                {
                    astateOilEffects[paramInt1][0] = Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt1 + 1) + "Oil"), null, 1.0F, str, -1F);
                }
                catch(Exception exception) { }
            str = astateOilStrings[i + 1];
            if(str != null)
                try
                {
                    astateOilEffects[paramInt1][1] = Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt1 + 1) + "Oil"), null, 1.0F, str, -1F);
                }
                catch(Exception exception1) { }
            if(World.Rnd().nextFloat() < 0.25F)
                ((SndAircraft) (aircraft)).FM.setReadyToReturn(true);
            break;
        }
    }

    public void changeOilEffectBase(int paramInt, Actor paramActor)
    {
        for(int i = 0; i < 2; i++)
            if(astateOilEffects[paramInt][i] != null)
                ((Actor) (astateOilEffects[paramInt][i])).pos.changeBase(paramActor, null, true);

    }

    public void setTankState(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramInt2 < 0 || paramInt2 > 6 || astateTankStates[paramInt1] == paramInt2)
            return;
        if(bIsMaster)
        {
            int i = astateTankStates[paramInt1];
            if(!doSetTankState(paramActor, paramInt1, paramInt2))
                return;
            for(int j = i; j < paramInt2; j++)
            {
                if(j % 2 == 0)
                    aircraft.setDamager(paramActor);
                doHitTank(paramActor, paramInt1);
            }

            if(((SndAircraft) (aircraft)).FM.isPlayers() && paramActor != actor && (paramActor instanceof Aircraft) && ((Aircraft)paramActor).isNetPlayer() && paramInt2 > 5 && astateTankStates[0] < 5 && astateTankStates[1] < 5 && astateTankStates[2] < 5 && astateTankStates[3] < 5 && !((SndAircraft) (aircraft)).FM.isSentBuryNote())
                Chat.sendLogRnd(3, "gore_lightfuel", (Aircraft)paramActor, aircraft);
            netToMirrors(9, paramInt1, paramInt2);
        } else
        {
            netToMaster(9, paramInt1, paramInt2, paramActor);
        }
    }

    public void hitTank(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(astateTankStates[paramInt1] == 6)
            return;
        int i = astateTankStates[paramInt1] + paramInt2;
        if(i > 6)
            i = 6;
        setTankState(paramActor, paramInt1, i);
    }

    public void repairTank(int paramInt)
    {
        if(!bIsMaster)
            return;
        if(astateTankStates[paramInt] > 0)
            setTankState(actor, paramInt, astateTankStates[paramInt] - 1);
    }

    public boolean doSetTankState(Actor paramActor, int paramInt1, int paramInt2)
    {
        boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt1 + 0]);
        Aircraft.debugprintln(aircraft, "Stating Tank " + paramInt1 + " to state " + paramInt2 + (bool ? ".." : " rejected (missing part).."));
        if(!bool)
            return false;
        if(World.getPlayerAircraft() == actor)
        {
            if(astateTankStates[paramInt1] == 0 && (paramInt2 == 1 || paramInt2 == 2))
                HUD.log("FailedTank");
            if(astateTankStates[paramInt1] < 5 && paramInt2 >= 5)
                HUD.log("FailedTankOnFire");
        }
        astateTankStates[paramInt1] = (byte)paramInt2;
        if(astateTankStates[paramInt1] < 5 && paramInt2 >= 5)
        {
            ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, paramActor);
            ((SndAircraft) (aircraft)).FM.setCapableOfACM(false);
        }
        if(paramInt2 < 4 && ((SndAircraft) (aircraft)).FM.isCapableOfBMP())
            ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(false, paramActor);
        int j = 0;
        if(!bIsAboveCriticalSpeed)
            j = 21;
        for(int i = 0; i < 3; i++)
        {
            if(astateTankEffects[paramInt1][i] != null)
                Eff3DActor.finish(astateTankEffects[paramInt1][i]);
            astateTankEffects[paramInt1][i] = null;
            String str = astateTankStrings[j + i + paramInt2 * 3];
            if(str == null)
                continue;
            if(paramInt2 > 2)
                astateTankEffects[paramInt1][i] = Eff3DActor.New(actor, actor.findHook("_Tank" + (paramInt1 + 1) + "Burn"), null, 1.0F, str, -1F);
            else
                astateTankEffects[paramInt1][i] = Eff3DActor.New(actor, actor.findHook("_Tank" + (paramInt1 + 1) + "Leak"), null, 1.0F, str, -1F);
        }

        aircraft.sfxSmokeState(2, paramInt1, paramInt2 > 4);
        return true;
    }

    private void doHitTank(Actor paramActor, int paramInt)
    {
        if((World.Rnd().nextInt(0, 99) < 75 || (actor instanceof Scheme1)) && astateTankStates[paramInt] == 6)
        {
            ((SndAircraft) (aircraft)).FM.setReadyToDie(true);
            ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, paramActor);
            ((SndAircraft) (aircraft)).FM.setCapableOfACM(false);
            Voice.speakMayday(aircraft);
            Aircraft.debugprintln(aircraft, "I'm on fire, going down!.");
            Explosions.generateComicBulb(actor, "OnFire", 12F);
            if(World.Rnd().nextInt(0, 99) < 75 && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Skill > 1)
            {
                Aircraft.debugprintln(aircraft, "BAILING OUT - Tank " + paramInt + " is on fire!.");
                hitDaSilk();
            }
        } else
        if(World.Rnd().nextInt(0, 99) < 12)
        {
            ((SndAircraft) (aircraft)).FM.setReadyToReturn(true);
            Aircraft.debugprintln(aircraft, "Tank " + paramInt + " hit, RTB..");
            Explosions.generateComicBulb(actor, "RTB", 12F);
        }
    }

    public void changeTankEffectBase(int paramInt, Actor paramActor)
    {
        for(int i = 0; i < 3; i++)
            if(astateTankEffects[paramInt][i] != null)
                ((Actor) (astateTankEffects[paramInt][i])).pos.changeBase(paramActor, null, true);

        aircraft.sfxSmokeState(2, paramInt, false);
    }

    public void explodeTank(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            if(!aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt + 0]))
                return;
            netToMirrors(10, paramInt, 0);
            doExplodeTank(paramInt);
        } else
        {
            netToMaster(10, paramInt, 0, paramActor);
        }
    }

    private void doExplodeTank(int paramInt)
    {
        Aircraft.debugprintln(aircraft, "Tank " + paramInt + " explodes..");
        Eff3DActor.New(actor, actor.findHook("_Tank" + (paramInt + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
        Eff3DActor.New(actor, actor.findHook("_Tank" + (paramInt + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1F);
        Eff3DActor.New(actor, actor.findHook("_Tank" + (paramInt + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1F);
        Eff3DActor.New(actor, actor.findHook("_Tank" + (paramInt + 1) + "Burn"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1F);
        aircraft.msgCollision(actor, astateEffectChunks[paramInt + 0] + "0", astateEffectChunks[paramInt + 0] + "0");
        if((actor instanceof Scheme1) && aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt + 0]))
            aircraft.msgCollision(actor, "CF_D0", "CF_D0");
        HookNamed localHookNamed = new HookNamed((ActorMesh)actor, "_Tank" + (paramInt + 1) + "Burn");
        Loc localLoc1 = new Loc();
        Loc localLoc2 = new Loc();
        actor.pos.getCurrent(localLoc1);
        localHookNamed.computePos(actor, localLoc1, localLoc2);
        if(World.getPlayerAircraft() == actor)
            HUD.log("FailedTankExplodes");
    }

    public void setEngineState(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramInt2 < 0 || paramInt2 > 4 || astateEngineStates[paramInt1] == paramInt2)
            return;
        if(bIsMaster)
        {
            int i = astateEngineStates[paramInt1];
            if(!doSetEngineState(paramActor, paramInt1, paramInt2))
                return;
            int j;
            for(j = i; j < paramInt2; j++)
            {
                aircraft.setDamager(paramActor);
                doHitEngine(paramActor, paramInt1);
            }

            if(((SndAircraft) (aircraft)).FM.isPlayers() && paramActor != actor && (paramActor instanceof Aircraft) && ((Aircraft)paramActor).isNetPlayer() && paramInt2 > 3 && astateEngineStates[0] < 3 && astateEngineStates[1] < 3 && astateEngineStates[2] < 3 && astateEngineStates[3] < 3 && !((SndAircraft) (aircraft)).FM.isSentBuryNote())
                Chat.sendLogRnd(3, "gore_lighteng", (Aircraft)paramActor, aircraft);
            j = 0;
            for(int k = 0; k < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); k++)
                if(astateEngineStates[k] > 2)
                    j++;

            if(j == ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum())
                setCockpitState(actor, astateCockpitState | 0x80);
            netToMirrors(1, paramInt1, paramInt2);
        } else
        {
            netToMaster(1, paramInt1, paramInt2, paramActor);
        }
    }

    public void hitEngine(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(astateEngineStates[paramInt1] == 4)
            return;
        int i = astateEngineStates[paramInt1] + paramInt2;
        if(i > 4)
            i = 4;
        setEngineState(paramActor, paramInt1, i);
    }

    public void repairEngine(int paramInt)
    {
        if(!bIsMaster)
            return;
        if(astateEngineStates[paramInt] > 0)
            setEngineState(actor, paramInt, astateEngineStates[paramInt] - 1);
    }

    public boolean doSetEngineState(Actor paramActor, int paramInt1, int paramInt2)
    {
        Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[paramInt1 + 4] + "' visibility..");
        boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt1 + 4]);
        Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[paramInt1 + 4] + "' is " + (bool ? "visible" : "invisible") + "..");
        Aircraft.debugprintln(aircraft, "Stating Engine " + paramInt1 + " to state " + paramInt2 + (bool ? ".." : " rejected (missing part).."));
        if(!bool)
            return false;
        if(astateEngineStates[paramInt1] < 4 && paramInt2 >= 4)
        {
            if(World.getPlayerAircraft() == actor)
                HUD.log("FailedEngineOnFire");
            ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, paramActor);
            ((SndAircraft) (aircraft)).FM.setCapableOfACM(false);
            ((SndAircraft) (aircraft)).FM.setCapableOfTaxiing(false);
        }
        astateEngineStates[paramInt1] = (byte)paramInt2;
        if(paramInt2 < 2 && ((SndAircraft) (aircraft)).FM.isCapableOfBMP())
            ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(false, paramActor);
        int j = 0;
        if(!bIsAboveCriticalSpeed)
            j = 15;
        for(int i = 0; i < 3; i++)
        {
            if(astateEngineEffects[paramInt1][i] != null)
                Eff3DActor.finish(astateEngineEffects[paramInt1][i]);
            astateEngineEffects[paramInt1][i] = null;
            String str = astateEngineStrings[j + i + paramInt2 * 3];
            if(str != null)
                astateEngineEffects[paramInt1][i] = Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt1 + 1) + "Smoke"), null, 1.0F, str, -1F);
        }

        aircraft.sfxSmokeState(1, paramInt1, paramInt2 > 3);
        return true;
    }

    private void doHitEngine(Actor paramActor, int paramInt)
    {
        if(World.Rnd().nextInt(0, 99) < 12)
        {
            ((SndAircraft) (aircraft)).FM.setReadyToReturn(true);
            Aircraft.debugprintln(aircraft, "Engines out, RTB..");
            Explosions.generateComicBulb(actor, "RTB", 12F);
        }
        if(astateEngineStates[paramInt] >= 2 && !(actor instanceof Scheme1) && World.Rnd().nextInt(0, 99) < 25)
        {
            ((SndAircraft) (aircraft)).FM.setReadyToReturn(true);
            Aircraft.debugprintln(aircraft, "One of the engines out, RTB..");
        }
        if(astateEngineStates[paramInt] == 4)
            if(actor instanceof Scheme1)
            {
                if(World.Rnd().nextBoolean())
                {
                    Aircraft.debugprintln(aircraft, "BAILING OUT - Engine " + paramInt + " is on fire..");
                    aircraft.hitDaSilk();
                }
                ((SndAircraft) (aircraft)).FM.setReadyToDie(true);
                ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, paramActor);
            } else
            if(World.Rnd().nextInt(0, 99) < 50)
            {
                ((SndAircraft) (aircraft)).FM.setReadyToDie(true);
                if(World.Rnd().nextInt(0, 99) < 25)
                    ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, paramActor);
                ((SndAircraft) (aircraft)).FM.setCapableOfACM(false);
                Aircraft.debugprintln(aircraft, "Engines on fire, ditching..");
                Explosions.generateComicBulb(actor, "OnFire", 12F);
            }
    }

    public void changeEngineEffectBase(int paramInt, Actor paramActor)
    {
        for(int i = 0; i < 3; i++)
            if(astateEngineEffects[paramInt][i] != null)
                ((Actor) (astateEngineEffects[paramInt][i])).pos.changeBase(paramActor, null, true);

        aircraft.sfxSmokeState(1, paramInt, false);
    }

    public void explodeEngine(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            if(!aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt + 4]))
                return;
            netToMirrors(3, paramInt, 0);
            doExplodeEngine(paramInt);
        } else
        {
            netToMaster(3, paramInt, 0, paramActor);
        }
    }

    private void doExplodeEngine(int paramInt)
    {
        Aircraft.debugprintln(aircraft, "Engine " + paramInt + " explodes..");
        Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Burn.eff", -1F);
        Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SmokeBoiling.eff", -1F);
        Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_Sparks.eff", -1F);
        Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Fireworks/Tank_SparksP.eff", -1F);
        aircraft.msgCollision(aircraft, astateEffectChunks[paramInt + 4] + "0", astateEffectChunks[paramInt + 4] + "0");
        Actor localActor;
        if(aircraft.getDamager() != null)
            localActor = aircraft.getDamager();
        else
            localActor = actor;
        HookNamed localHookNamed = new HookNamed((ActorMesh)actor, "_Engine" + (paramInt + 1) + "Smoke");
        Loc localLoc1 = new Loc();
        Loc localLoc2 = new Loc();
        actor.pos.getCurrent(localLoc1);
        localHookNamed.computePos(actor, localLoc1, localLoc2);
        MsgExplosion.send(null, astateEffectChunks[4 + paramInt] + "0", localLoc2.getPoint(), localActor, 1.248F, 0.026F, 1, 75F);
    }

    public void setEngineStarts(int paramInt)
    {
        if(!bIsMaster)
        {
            return;
        } else
        {
            doSetEngineStarts(paramInt);
            netToMirrors(4, paramInt, 96);
            return;
        }
    }

    public void setEngineRunning(int paramInt)
    {
        if(!bIsMaster)
        {
            return;
        } else
        {
            doSetEngineRunning(paramInt);
            netToMirrors(5, paramInt, 81);
            return;
        }
    }

    public void setEngineStops(int paramInt)
    {
        if(!bIsMaster)
        {
            return;
        } else
        {
            doSetEngineStops(paramInt);
            netToMirrors(6, paramInt, 2);
            return;
        }
    }

    public void setEngineDies(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetEngineDies(paramInt);
            netToMirrors(7, paramInt, 77);
        } else
        {
            netToMaster(7, paramInt, 67, paramActor);
        }
    }

    public void setEngineStuck(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            doSetEngineStuck(paramInt);
            aircraft.setDamager(paramActor);
            netToMirrors(29, paramInt, 77);
        } else
        {
            netToMaster(29, paramInt, 67, paramActor);
        }
    }

    public void setEngineSpecificDamage(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetEngineSpecificDamage(paramInt1, paramInt2);
            netToMirrors(2, paramInt1, paramInt2);
        } else
        {
            netToMaster(2, paramInt1, paramInt2, paramActor);
        }
    }

    public void setEngineReadyness(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetEngineReadyness(paramInt1, paramInt2);
            netToMirrors(25, paramInt1, paramInt2);
        } else
        {
            netToMaster(25, paramInt1, paramInt2, paramActor);
        }
    }

    public void setEngineStage(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(bIsMaster)
        {
            doSetEngineStage(paramInt1, paramInt2);
            netToMirrors(26, paramInt1, paramInt2);
        } else
        {
            netToMaster(26, paramInt1, paramInt2, paramActor);
        }
    }

    public void setEngineCylinderKnockOut(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetEngineCylinderKnockOut(paramInt1, paramInt2);
            netToMirrors(27, paramInt1, paramInt2);
        } else
        {
            netToMaster(27, paramInt1, paramInt2, paramActor);
        }
    }

    public void setEngineMagnetoKnockOut(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetEngineMagnetoKnockOut(paramInt1, paramInt2);
            netToMirrors(28, paramInt1, paramInt2);
        } else
        {
            netToMaster(28, paramInt1, paramInt2, paramActor);
        }
    }

    private void doSetEngineStarts(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt].doSetEngineStarts();
    }

    private void doSetEngineRunning(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt].doSetEngineRunning();
    }

    private void doSetEngineStops(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt].doSetEngineStops();
    }

    private void doSetEngineDies(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt].doSetEngineDies();
    }

    private void doSetEngineStuck(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt].doSetEngineStuck();
    }

    private void doSetEngineSpecificDamage(int paramInt1, int paramInt2)
    {
        switch(paramInt2)
        {
        case 0: // '\0'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetKillCompressor();
            break;

        case 3: // '\003'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetKillPropAngleDevice();
            break;

        case 4: // '\004'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetKillPropAngleDeviceSpeeds();
            break;

        case 5: // '\005'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetExtinguisherFire();
            break;

        case 2: // '\002'
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented feature (E.S.D./H.)");

        case 1: // '\001'
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].isHasControlThrottle())
                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetKillControlThrottle();
            break;

        case 6: // '\006'
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].isHasControlProp())
                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetKillControlProp();
            break;

        case 7: // '\007'
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].isHasControlMix())
                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetKillControlMix();
            break;

        default:
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in (E.S.D./null)");
        }
    }

    private void doSetEngineReadyness(int paramInt1, int paramInt2)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetReadyness(0.01F * (float)paramInt2);
    }

    private void doSetEngineStage(int paramInt1, int paramInt2)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetStage(paramInt2);
    }

    private void doSetEngineCylinderKnockOut(int paramInt1, int paramInt2)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetCyliderKnockOut(paramInt2);
    }

    private void doSetEngineMagnetoKnockOut(int paramInt1, int paramInt2)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[paramInt1].doSetMagnetoKnockOut(paramInt2);
    }

    public void doSetEngineExtinguisherVisuals(int paramInt)
    {
        Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[paramInt + 4] + "' visibility..");
        boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt + 4]);
        Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[paramInt + 4] + "' is " + (bool ? "visible" : "invisible") + "..");
        Aircraft.debugprintln(aircraft, "Firing Extinguisher on Engine " + paramInt + (bool ? ".." : " rejected (missing part).."));
        if(!bool)
        {
            return;
        } else
        {
            Eff3DActor.New(actor, actor.findHook("_Engine" + (paramInt + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Aircraft/EngineExtinguisher1.eff", 3F);
            return;
        }
    }

    public void setSootState(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(astateSootStates[paramInt1] == paramInt2)
            return;
        if(bIsMaster)
        {
            if(!doSetSootState(paramInt1, paramInt2))
                return;
            netToMirrors(8, paramInt1, paramInt2);
        } else
        {
            netToMaster(8, paramInt1, paramInt2, paramActor);
        }
    }

    public boolean doSetSootState(int paramInt1, int paramInt2)
    {
        Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[paramInt1 + 4] + "' visibility..");
        boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[paramInt1 + 4]);
        Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[paramInt1 + 4] + "' is " + (bool ? "visible" : "invisible") + "..");
        Aircraft.debugprintln(aircraft, "Stating Engine " + paramInt1 + " to state " + paramInt2 + (bool ? ".." : " rejected (missing part).."));
        if(!bool)
        {
            return false;
        } else
        {
            astateSootStates[paramInt1] = (byte)paramInt2;
            aircraft.doSetSootState(paramInt1, paramInt2);
            return true;
        }
    }

    public void changeSootEffectBase(int paramInt, Actor paramActor)
    {
        for(int i = 0; i < 2; i++)
            if(astateSootEffects[paramInt][i] != null)
                ((Actor) (astateSootEffects[paramInt][i])).pos.changeBase(paramActor, null, true);

    }

    public void setCockpitState(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(astateCockpitState == paramInt)
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetCockpitState(paramInt);
            netToMirrors(23, 0, paramInt);
        } else
        {
            netToMaster(23, 0, paramInt, paramActor);
        }
    }

    public void doSetCockpitState(int paramInt)
    {
        if(astateCockpitState == paramInt)
        {
            return;
        } else
        {
            astateCockpitState = paramInt;
            aircraft.setCockpitState(paramInt);
            return;
        }
    }

    public void setControlsDamage(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            if(((SndAircraft) (aircraft)).FM.isPlayers() && paramActor != actor && (paramActor instanceof Aircraft) && ((Aircraft)paramActor).isNetPlayer() && !((SndAircraft) (aircraft)).FM.isSentControlsOutNote() && !((SndAircraft) (aircraft)).FM.isSentBuryNote())
            {
                Chat.sendLogRnd(3, "gore_hitctrls", (Aircraft)paramActor, aircraft);
                ((SndAircraft) (aircraft)).FM.setSentControlsOutNote(true);
            }
            doSetControlsDamage(paramInt, paramActor);
        } else
        {
            netToMaster(21, 0, paramInt, paramActor);
        }
    }

    public void setInternalDamage(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetInternalDamage(paramInt);
            netToMirrors(22, 0, paramInt);
        } else
        {
            netToMaster(22, 0, paramInt, paramActor);
        }
    }

    public void setGliderBoostOn()
    {
        if(!bIsMaster)
        {
            return;
        } else
        {
            netToMirrors(12, 5, 5);
            return;
        }
    }

    public void setGliderBoostOff()
    {
        if(!bIsMaster)
        {
            return;
        } else
        {
            netToMirrors(13, 7, 7);
            return;
        }
    }

    public void setGliderCutCart()
    {
        if(!bIsMaster)
        {
            return;
        } else
        {
            netToMirrors(14, 9, 9);
            return;
        }
    }

    private void doSetControlsDamage(int paramInt, Actor paramActor)
    {
        switch(paramInt)
        {
        case 0: // '\0'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.resetControl(0);
            if(((SndAircraft) (aircraft)).FM.isPlayers() && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasAileronControl)
                HUD.log("FailedAroneAU");
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasAileronControl = false;
            ((SndAircraft) (aircraft)).FM.setCapableOfACM(false);
            break;

        case 1: // '\001'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.resetControl(1);
            if(((SndAircraft) (aircraft)).FM.isPlayers() && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasElevatorControl)
                HUD.log("FailedVatorAU");
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasElevatorControl = false;
            ((SndAircraft) (aircraft)).FM.setCapableOfACM(false);
            if(Math.abs(((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z) > 7D)
                ((SndAircraft) (aircraft)).FM.setCapableOfBMP(false, paramActor);
            break;

        case 2: // '\002'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.resetControl(2);
            if(((SndAircraft) (aircraft)).FM.isPlayers() && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasRudderControl)
                HUD.log("FailedRudderAU");
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasRudderControl = false;
            break;

        default:
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in (C.A.D./null)");
        }
    }

    private void doSetInternalDamage(int paramInt)
    {
        switch(paramInt)
        {
        case 0: // '\0'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Gears.setHydroOperable(false);
            break;

        case 1: // '\001'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasFlapsControl = false;
            break;

        case 2: // '\002'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.nitro = 0.0F;
            break;

        case 3: // '\003'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Gears.setHydroOperable(false);
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Gears.setOperable(false);
            break;

        case 4: // '\004'
            if(aircraft instanceof DO_335A0)
                ((DO_335A0)aircraft).doKeelShutoff();
            else
            if(aircraft instanceof DO_335V13)
                ((DO_335V13)aircraft).doKeelShutoff();
            else
                throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (I.K.S.)");
            // fall through

        case 5: // '\005'
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[1].setPropReductorValue(0.007F);
            break;

        default:
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in (I.D./null)");
        }
    }

    private void doSetGliderBoostOn()
    {
        if(actor instanceof Scheme0)
            ((Scheme0)actor).doFireBoosters();
        else
        if(actor instanceof AR_234B2)
            ((AR_234B2)actor).doFireBoosters();
        else
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.B./On not in Nul)");
    }

    private void doSetGliderBoostOff()
    {
        if(actor instanceof Scheme0)
            ((Scheme0)actor).doCutBoosters();
        else
        if(actor instanceof AR_234B2)
            ((AR_234B2)actor).doCutBoosters();
        else
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.B./Off not in Nul)");
    }

    private void doSetGliderCutCart()
    {
        if(actor instanceof Scheme0)
            ((Scheme0)actor).doCutCart();
        else
            throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (G.C.C./Off not in Nul)");
    }

    private void doSetCondensateState(boolean paramBoolean)
    {
        for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); i++)
        {
            if(astateCondensateEffects[i] != null)
                Eff3DActor.finish(astateCondensateEffects[i]);
            astateCondensateEffects[i] = null;
            if(!paramBoolean)
                continue;
            String str = astateCondensateStrings[1];
            if(str == null)
                continue;
            try
            {
                astateCondensateEffects[i] = Eff3DActor.New(actor, actor.findHook("_Engine" + (i + 1) + "Smoke"), astateCondensateDispVector, 1.0F, str, -1F);
            }
            catch(Exception localException)
            {
                Aircraft.debugprintln(aircraft, "Above condensate failed - probably a glider..");
            }
        }

    }

    public void setStallState(boolean paramBoolean)
    {
        if(bIsMaster)
        {
            doSetStallState(paramBoolean);
            netToMirrors(16, paramBoolean ? 1 : 0, paramBoolean ? 1 : 0);
        }
    }

    private void doSetStallState(boolean paramBoolean)
    {
        for(int i = 0; i < 2; i++)
            if(astateStallEffects[i] != null)
                Eff3DActor.finish(astateStallEffects[i]);

        if(paramBoolean)
        {
            String str = astateStallStrings[1];
            if(str != null)
            {
                if(bWingTipLExists)
                    astateStallEffects[0] = Eff3DActor.New(actor, actor.findHook("_WingTipL"), null, 1.0F, str, -1F);
                if(bWingTipRExists)
                    astateStallEffects[1] = Eff3DActor.New(actor, actor.findHook("_WingTipR"), null, 1.0F, str, -1F);
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
		if (bShowSmokesOn == flag)
			return;
		if (bIsMaster) {
			doSetAirShowState(flag);
			netToMirrors(42, iAirShowSmoke, iAirShowSmoke);
			netToMirrors(43, bAirShowSmokeEnhanced ? 1 : 0,
					bAirShowSmokeEnhanced ? 1 : 0);
			netToMirrors(15, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetAirShowState(boolean flag) {
		bShowSmokesOn = flag;
		for (int i = 0; i < 3; i++)
			if (astateAirShowEffects[i] != null)
				Eff3DActor.finish(astateAirShowEffects[i]);

		if (flag) {
			Hook hook = null;
			try {
				hook = actor.findHook("_ClipCGear");
			} catch (Exception exception) {
				hook = null;
			}
			if (iAirShowSmoke < 1 || iAirShowSmoke > 3 || hook == null) {
				if (bWingTipLExists)
					astateAirShowEffects[0] = Eff3DActor.New(actor,
							actor.findHook("_WingTipL"), null, 1.0F,
							"3DO/Effects/Aircraft/AirShowRedTSPD.eff", -1F);
				if (bWingTipRExists)
					astateAirShowEffects[1] = Eff3DActor.New(actor,
							actor.findHook("_WingTipR"), null, 1.0F,
							"3DO/Effects/Aircraft/AirShowGreenTSPD.eff", -1F);
			} else if (iAirShowSmoke == 1) {
				astateAirShowEffects[0] = Eff3DActor.New(actor, hook, null,
						1.0F, "3DO/Effects/Aircraft/AirShowSmoke1.eff", -1F);
				if (bAirShowSmokeEnhanced) {
					astateAirShowEffects[1] = Eff3DActor.New(actor, hook, null,
							1.0F, "3DO/Effects/Aircraft/AirShowSmoke1_2.eff",
							-1F);
					astateAirShowEffects[2] = Eff3DActor.New(actor, hook, null,
							1.0F, "3DO/Effects/Aircraft/AirShowSmoke1_3.eff",
							-1F);
				}
			} else if (iAirShowSmoke == 2) {
				astateAirShowEffects[0] = Eff3DActor.New(actor, hook, null,
						1.0F, "3DO/Effects/Aircraft/AirShowSmoke2.eff", -1F);
				if (bAirShowSmokeEnhanced) {
					astateAirShowEffects[1] = Eff3DActor.New(actor, hook, null,
							1.0F, "3DO/Effects/Aircraft/AirShowSmoke2_2.eff",
							-1F);
					astateAirShowEffects[2] = Eff3DActor.New(actor, hook, null,
							1.0F, "3DO/Effects/Aircraft/AirShowSmoke2_3.eff",
							-1F);
				}
			} else {
				astateAirShowEffects[0] = Eff3DActor.New(actor, hook, null,
						1.0F, "3DO/Effects/Aircraft/AirShowSmoke3.eff", -1F);
				if (bAirShowSmokeEnhanced) {
					astateAirShowEffects[1] = Eff3DActor.New(actor, hook, null,
							1.0F, "3DO/Effects/Aircraft/AirShowSmoke3_2.eff",
							-1F);
					astateAirShowEffects[2] = Eff3DActor.New(actor, hook, null,
							1.0F, "3DO/Effects/Aircraft/AirShowSmoke3_3.eff",
							-1F);
				}
			}
		}
	}

	public void setDumpFuelState(boolean flag) {
		if (bDumpFuel == flag)
			return;
		if (bIsMaster) {
			doSetDumpFuelState(flag);
			netToMirrors(50, flag ? 1 : 0, flag ? 1 : 0);
		}
	}

	private void doSetDumpFuelState(boolean flag) {
		bDumpFuel = flag;
		for (int i = 0; i < 2; i++)
			if (astateDumpFuelEffects[i] != null)
				Eff3DActor.finish(astateDumpFuelEffects[i]);

		if (flag)
			// TODO: DBW version
			if (actor instanceof TypeFuelDump) {
				for (int j = 0; j < 2; j++) {
					switch (j) {
					case 0:
						for (int k = 0; k < 2; k++) {
							try {
								if (actor.findHook("_FuelDump0" + (k + 1)) != null) {
									astateDumpFuelEffects[k] = Eff3DActor
											.New(actor,
													actor.findHook("_FuelDump0"
															+ (k + 1)),
													null,
													1.0F,
													"3DO/Effects/Aircraft/DumpFuelTSPD.eff",
													-1F);
								}
							} catch (java.lang.Exception exception) {
								break;
							}
						}
						if (astateDumpFuelEffects[0] != null)
							break;
					case 1:
						if (astateDumpFuelEffects[0] == null) {
							if (bWingTipLExists)
								astateDumpFuelEffects[0] = Eff3DActor
										.New(actor,
												actor.findHook("_WingTipL"),
												null,
												1.0F,
												"3DO/Effects/Aircraft/DumpFuelTSPD.eff",
												-1F);
							if (bWingTipRExists)
								astateDumpFuelEffects[1] = Eff3DActor
										.New(actor,
												actor.findHook("_WingTipR"),
												null,
												1.0F,
												"3DO/Effects/Aircraft/DumpFuelTSPD.eff",
												-1F);
						}
						break;
					}
				}

			}
	}

    public void wantBeaconsNet(boolean paramBoolean)
    {
        if(paramBoolean == bWantBeaconsNet)
            return;
        bWantBeaconsNet = paramBoolean;
        if(World.getPlayerAircraft() != actor)
            return;
        if(bWantBeaconsNet)
            setBeacon(actor, beacon, 0, false);
    }

    public int getBeacon()
    {
        return beacon;
    }

    public void beaconPlus()
    {
        if(Main.cur().mission.hasBeacons(actor.getArmy()))
        {
            int i = Main.cur().mission.getBeacons(actor.getArmy()).size();
            if(beacon < i)
                setBeacon(beacon + 1);
            else
                setBeacon(0);
        }
    }

    public void beaconMinus()
    {
        if(Main.cur().mission.hasBeacons(actor.getArmy()))
        {
            int i = Main.cur().mission.getBeacons(actor.getArmy()).size();
            if(beacon >= 1)
                setBeacon(beacon - 1);
            else
                setBeacon(i);
        }
    }

    public void setBeacon(int paramInt)
    {
        for(; paramInt < 0; paramInt += 32);
        for(; paramInt > 32; paramInt -= 32);
        if(paramInt == beacon)
        {
            return;
        } else
        {
            setBeacon(actor, paramInt, 0, false);
            return;
        }
    }

    private void setBeacon(Actor paramActor, int paramInt1, int paramInt2, boolean paramBoolean)
    {
        doSetBeacon(paramActor, paramInt1, paramInt2);
        if(Mission.isSingle() || !bWantBeaconsNet)
            return;
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
            netToMirrors(42, paramInt1, paramInt2, paramActor);
        else
        if(!paramBoolean)
            netToMaster(42, paramInt1, paramInt2, paramActor);
    }

    private void doSetBeacon(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(actor != paramActor)
            return;
        if(World.getPlayerAircraft() != actor)
            return;
        if(paramInt1 > 0)
        {
            Actor localActor = (Actor)Main.cur().mission.getBeacons(actor.getArmy()).get(paramInt1 - 1);
            boolean bool = Aircraft.hasPlaneZBReceiver(aircraft);
            if(!bool && ((localActor instanceof TypeHasYGBeacon) || (localActor instanceof TypeHasHayRake)))
            {
                int i = paramInt1 - beacon;
                beacon = paramInt1;
                if(i > 0)
                    beaconPlus();
                else
                    beaconMinus();
                return;
            }
            String str1 = Beacon.getBeaconID(paramInt1 - 1);
            if(aircraft.getPilotsCount() == 1 || ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines.length == 1)
                Main3D.cur3D().ordersTree.setFrequency(null);
            if(localActor instanceof TypeHasYGBeacon)
            {
                Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
                HUD.log(hudLogBeaconId, "BeaconYG", new Object[] {
                    str1
                });
                startListeningYGBeacon();
            } else
            if(localActor instanceof TypeHasHayRake)
            {
                Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
                HUD.log(hudLogBeaconId, "BeaconYE", new Object[] {
                    str1
                });
                Object localObject = Main.cur().mission.getHayrakeCodeOfCarrier(localActor);
                startListeningHayrake(localActor, (String)localObject);
            } else
            if(localActor instanceof TypeHasLorenzBlindLanding)
            {
                Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
                HUD.log(hudLogBeaconId, "BeaconBA", new Object[] {
                    str1
                });
                startListeningLorenzBlindLanding();
                isAAFIAS = false;
                if(localActor instanceof TypeHasAAFIAS)
                    isAAFIAS = true;
            } else
            if(localActor instanceof TypeHasRadioStation)
            {
                Object localObject = (TypeHasRadioStation)localActor;
                str1 = ((TypeHasRadioStation)localObject).getStationID();
                String str2 = Property.stringValue(localActor.getClass(), "i18nName", str1);
                HUD.log(hudLogBeaconId, "BeaconRS", new Object[] {
                    str2
                });
                startListeningRadioStation(str1);
            } else
            {
                HUD.log(hudLogBeaconId, "BeaconND", new Object[] {
                    str1
                });
                startListeningNDBeacon();
            }
        } else
        {
            HUD.log(hudLogBeaconId, "BeaconNONE");
            stopListeningBeacons();
            Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
        }
        beacon = paramInt1;
    }

    public void startListeningYGBeacon()
    {
        stopListeningLorenzBlindLanding();
        stopListeningHayrake();
        listenYGBeacon = true;
        listenNDBeacon = false;
        listenRadioStation = false;
        aircraft.playBeaconCarrier(false, 0.0F);
        CmdMusic.setCurrentVolume(0.0F);
    }

    public void startListeningNDBeacon()
    {
        stopListeningLorenzBlindLanding();
        listenYGBeacon = false;
        listenNDBeacon = true;
        listenRadioStation = false;
        stopListeningHayrake();
        CmdMusic.setCurrentVolume(0.0F);
    }

    public void startListeningRadioStation(String paramString)
    {
        stopListeningLorenzBlindLanding();
        listenYGBeacon = false;
        listenNDBeacon = false;
        listenRadioStation = true;
        stopListeningHayrake();
        String str1 = paramString.replace(' ', '_');
        int i = Mission.curYear();
        int j = Mission.curMonth();
        int k = Mission.curDay();
        String str2 = "" + i;
        String str3 = "";
        String str4 = "";
        if(j < 10)
            str3 = "0" + j;
        else
            str3 = "" + j;
        if(k < 10)
            str4 = "0" + k;
        else
            str4 = "" + k;
        String arrayOfString[] = {
            str2 + str3 + str4, str2 + str3 + "XX", str2 + "XX" + str4, str2 + "XXXX"
        };
        for(int m = 0; m < arrayOfString.length; m++)
        {
            File localFile = new File("./samples/Music/Radio/" + str1 + "/" + arrayOfString[m]);
            if(localFile.exists())
            {
                CmdMusic.setPath("Music/Radio/" + str1 + "/" + arrayOfString[m], true);
                CmdMusic.play();
                return;
            }
        }

        CmdMusic.setPath("Music/Radio/" + str1, true);
        CmdMusic.play();
    }

    public void stopListeningBeacons()
    {
        stopListeningLorenzBlindLanding();
        listenYGBeacon = false;
        listenNDBeacon = false;
        listenRadioStation = false;
        stopListeningHayrake();
        aircraft.stopMorseSounds();
        CmdMusic.setCurrentVolume(0.0F);
    }

    public void startListeningHayrake(Actor paramActor, String paramString)
    {
        stopListeningLorenzBlindLanding();
        hayrakeCarrier = paramActor;
        hayrakeCode = paramString;
        listenYGBeacon = false;
        listenNDBeacon = false;
        listenRadioStation = false;
        aircraft.stopMorseSounds();
        CmdMusic.setCurrentVolume(0.0F);
    }

    public void stopListeningHayrake()
    {
        hayrakeCarrier = null;
        hayrakeCode = null;
    }

    public void startListeningLorenzBlindLanding()
    {
        listenLorenzBlindLanding = true;
        stopListeningHayrake();
        listenYGBeacon = false;
        listenNDBeacon = false;
        listenRadioStation = false;
        aircraft.stopMorseSounds();
        CmdMusic.setCurrentVolume(0.0F);
    }

    public void stopListeningLorenzBlindLanding()
    {
        listenLorenzBlindLanding = false;
        isAAFIAS = false;
        aircraft.stopMorseSounds();
    }

    public void preLoadRadioStation(Actor paramActor)
    {
        TypeHasRadioStation localTypeHasRadioStation = (TypeHasRadioStation)paramActor;
        String str = localTypeHasRadioStation.getStationID();
        startListeningRadioStation(str);
    }

    public void setNavLightsState(boolean paramBoolean)
    {
        if(bNavLightsOn == paramBoolean)
            return;
        if(bIsMaster)
        {
            doSetNavLightsState(paramBoolean);
            netToMirrors(30, paramBoolean ? 1 : 0, paramBoolean ? 1 : 0);
        }
    }

    private void doSetNavLightsState(boolean paramBoolean)
    {
        for(int i = 0; i < astateNavLightsEffects.length; i++)
        {
            if(astateNavLightsEffects[i] != null)
            {
                Eff3DActor.finish(astateNavLightsEffects[i]);
                astateNavLightsLights[i].light.setEmit(0.0F, 0.0F);
            }
            astateNavLightsEffects[i] = null;
        }

        if(paramBoolean)
        {
            for(int i = 0; i < astateNavLightsEffects.length; i++)
            {
                Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[i + 12] + "' visibility..");
                boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + 12]);
                Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[i + 12] + "' is " + (bool ? "visible" : "invisible") + "..");
                if(bool)
                {
                    bNavLightsOn = paramBoolean;
                  //TODO: Updated nav light code
                    String str = "3DO/Effects/Fireworks/Flare" + (i <= 1 ? "Red" : i <= 3 ? "Green" : "White") + ".eff";
                    astateNavLightsEffects[i] = Eff3DActor.New(actor, actor.findHook("_NavLight" + i), null, 1.0F, str, -1F, false);
                    astateNavLightsLights[i].light.setEmit(0.35F, 8F);
                }
            }

        } else
        {
            bNavLightsOn = paramBoolean;
        }
    }

    public void changeNavLightEffectBase(int paramInt, Actor paramActor)
    {
        if(astateNavLightsEffects[paramInt] != null)
        {
            Eff3DActor.finish(astateNavLightsEffects[paramInt]);
            astateNavLightsLights[paramInt].light.setEmit(0.0F, 0.0F);
            astateNavLightsEffects[paramInt] = null;
        }
    }

    public void setLandingLightState(boolean paramBoolean)
    {
        if(bLandingLightOn == paramBoolean)
            return;
        if(bIsMaster)
        {
            doSetLandingLightState(paramBoolean);
            int i = 0;
            for(int j = 0; j < astateLandingLightEffects.length; j++)
                if(astateLandingLightEffects[j] == null)
                    i++;

            if(i == astateLandingLightEffects.length)
            {
                bLandingLightOn = false;
                paramBoolean = false;
            }
            netToMirrors(31, paramBoolean ? 1 : 0, paramBoolean ? 1 : 0);
        }
    }

    private void doSetLandingLightState(boolean paramBoolean)
    {
        bLandingLightOn = paramBoolean;
        for(int i = 0; i < astateLandingLightEffects.length; i++)
        {
            if(astateLandingLightEffects[i] != null)
            {
                Eff3DActor.finish(astateLandingLightEffects[i]);
                astateLandingLightLights[i].light.setEmit(0.0F, 0.0F);
            }
            astateLandingLightEffects[i] = null;
        }

        if(paramBoolean)
        {
            for(int i = 0; i < astateLandingLightEffects.length; i++)
            {
                Aircraft.debugprintln(aircraft, "AS: Checking '" + astateEffectChunks[i + 18] + "' visibility..");
                boolean bool = aircraft.isChunkAnyDamageVisible(astateEffectChunks[i + 18]);
                Aircraft.debugprintln(aircraft, "AS: '" + astateEffectChunks[i + 18] + "' is " + (bool ? "visible" : "invisible") + "..");
                if(bool)
                {
                    String str = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
                    astateLandingLightEffects[i] = Eff3DActor.New(actor, actor.findHook("_LandingLight0" + i), null, 1.0F, str, -1F);
                    astateLandingLightLights[i].light.setEmit(1.2F, 8F);
                }
            }

        }
    }

    public void changeLandingLightEffectBase(int paramInt, Actor paramActor)
    {
        if(astateLandingLightEffects[paramInt] != null)
        {
            Eff3DActor.finish(astateLandingLightEffects[paramInt]);
            astateLandingLightLights[paramInt].light.setEmit(0.0F, 0.0F);
            astateLandingLightEffects[paramInt] = null;
            bLandingLightOn = false;
        }
    }

    public void setPilotState(Actor paramActor, int paramInt1, int paramInt2)
    {
        setPilotState(paramActor, paramInt1, paramInt2, true);
    }

    public void setPilotState(Actor paramActor, int paramInt1, int paramInt2, boolean paramBoolean)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramInt2 > 95)
            paramInt2 = 100;
        if(paramInt2 < 0)
            paramInt2 = 0;
        if(astatePilotStates[paramInt1] >= paramInt2)
            return;
        if(bIsMaster)
        {
            aircraft.setDamager(paramActor);
            doSetPilotState(paramInt1, paramInt2, paramActor);
            if(paramBoolean && ((SndAircraft) (aircraft)).FM.isPlayers() && paramInt1 == astatePlayerIndex && !World.isPlayerDead() && paramActor != actor && (paramActor instanceof Aircraft) && ((Aircraft)paramActor).isNetPlayer() && paramInt2 == 100)
                Chat.sendLogRnd(1, "gore_pk", (Aircraft)paramActor, aircraft);
            if(paramInt2 > 0 && paramBoolean)
                netToMirrors(17, paramInt1, paramInt2);
        } else
        if(paramBoolean)
            netToMaster(17, paramInt1, paramInt2, paramActor);
    }

    public void hitPilot(Actor paramActor, int paramInt1, int paramInt2)
    {
        setPilotState(paramActor, paramInt1, astatePilotStates[paramInt1] + paramInt2);
    }

    public void setBleedingPilot(Actor paramActor, int paramInt1, int paramInt2)
    {
        setBleedingPilot(paramActor, paramInt1, paramInt2, true);
    }

    public void setBleedingPilot(Actor paramActor, int paramInt1, int paramInt2, boolean paramBoolean)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            doSetBleedingPilot(paramInt1, paramInt2, paramActor);
            if(paramBoolean)
                netToMirrors(47, paramInt1, paramInt2);
        } else
        if(paramBoolean)
            netToMaster(47, paramInt1, paramInt2, paramActor);
    }

    private void doSetBleedingPilot(int paramInt1, int paramInt2, Actor paramActor)
    {
        if(Mission.isSingle() || ((SndAircraft) (aircraft)).FM.isPlayers() && !Mission.isServer() || Mission.isNet() && Mission.isServer() && !aircraft.isNetPlayer())
        {
            long l1 = 0x1d4c0 / paramInt2;
            if(astateBleedingNext[paramInt1] == 0L)
            {
                astateBleedingNext[paramInt1] = l1;
                if(World.getPlayerAircraft() == actor && Config.isUSE_RENDER() && (!bIsAboutToBailout || astateBailoutStep <= 11) && astatePilotStates[paramInt1] < 100)
                {
                    if(paramInt2 > 10)
                        HUD.log(astateHUDPilotHits[astatePilotFunctions[paramInt1]] + "BLEED0");
                    else
                        HUD.log(astateHUDPilotHits[astatePilotFunctions[paramInt1]] + "BLEED1");
                }
            } else
            {
                int i = 30000 / (int)astateBleedingNext[paramInt1];
                long l2 = 30000 / (i + paramInt2);
                if(l2 < 100L)
                    l2 = 100L;
                astateBleedingNext[paramInt1] = l2;
            }
            setBleedingTime(paramInt1);
        }
    }

    public boolean bleedingTest(int paramInt)
    {
        return Time.current() > astateBleedingTimes[paramInt];
    }

    public void setBleedingTime(int paramInt)
    {
        astateBleedingTimes[paramInt] = Time.current() + astateBleedingNext[paramInt];
    }

    public void doSetWoundPilot(int paramInt)
    {
        switch(paramInt)
        {
        default:
            break;

        case 1: // '\001'
            ((SndAircraft) (aircraft)).FM.SensRoll *= 0.6F;
            ((SndAircraft) (aircraft)).FM.SensPitch *= 0.6F;
            if(((SndAircraft) (aircraft)).FM.isPlayers() && Config.isUSE_RENDER())
                HUD.log("PlayerArmHit");
            break;

        case 2: // '\002'
            ((SndAircraft) (aircraft)).FM.SensYaw *= 0.2F;
            if(((SndAircraft) (aircraft)).FM.isPlayers() && Config.isUSE_RENDER())
                HUD.log("PlayerLegHit");
            break;
        }
    }

    public void setPilotWound(Actor paramActor, int paramInt1, int paramInt2)
    {
        setPilotWound(paramActor, paramInt2, true);
    }

    public void setPilotWound(Actor paramActor, int paramInt, boolean paramBoolean)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
        {
            doSetWoundPilot(paramInt);
            if(paramBoolean)
                netToMirrors(46, 0, paramInt);
        } else
        if(paramBoolean)
            netToMaster(46, 0, paramInt, paramActor);
    }

    public void woundedPilot(Actor paramActor, int paramInt1, int paramInt2)
    {
        switch(paramInt1)
        {
        default:
            break;

        case 1: // '\001'
            if(World.Rnd().nextFloat() < 0.18F && paramInt2 > 4 && !armsWounded)
            {
                setPilotWound(paramActor, paramInt1, true);
                armsWounded = true;
            }
            break;

        case 2: // '\002'
            if(paramInt2 > 4 && !legsWounded)
            {
                setPilotWound(paramActor, paramInt1, true);
                legsWounded = true;
            }
            break;
        }
    }

    private void doSetPilotState(int paramInt1, int paramInt2, Actor paramActor)
    {
        if(paramInt2 > 95)
            paramInt2 = 100;
        int i;
        if(World.getPlayerAircraft() == actor && Config.isUSE_RENDER() && (!bIsAboutToBailout || astateBailoutStep <= 11))
        {
            i = paramInt1 == astatePlayerIndex && !World.isPlayerDead() ? 1 : 0;
            if(astatePilotStates[paramInt1] < 100 && paramInt2 == 100)
            {
                HUD.log(astateHUDPilotHits[astatePilotFunctions[paramInt1]] + "HIT2");
                if(i != 0)
                {
                    World.setPlayerDead();
                    if(Mission.isNet())
                        Chat.sendLog(0, "gore_killed", (NetUser)NetEnv.host(), (NetUser)NetEnv.host());
                    if(Main3D.cur3D().cockpits != null && !World.isPlayerGunner())
                    {
                        int j = Main3D.cur3D().cockpits.length;
                        for(int k = 0; k < j; k++)
                        {
                            Cockpit localCockpit = Main3D.cur3D().cockpits[k];
                            if(Actor.isValid(localCockpit) && localCockpit.astatePilotIndx() != paramInt1 && !isPilotDead(localCockpit.astatePilotIndx()) && !Mission.isNet() && AircraftHotKeys.isCockpitRealMode(k))
                                AircraftHotKeys.setCockpitRealMode(k, false);
                        }

                    }
                }
            } else
            if(astatePilotStates[paramInt1] < 66 && paramInt2 > 66)
                HUD.log(astateHUDPilotHits[astatePilotFunctions[paramInt1]] + "HIT1");
            else
            if(astatePilotStates[paramInt1] < 25 && paramInt2 > 25)
                HUD.log(astateHUDPilotHits[astatePilotFunctions[paramInt1]] + "HIT0");
        }
        i = astatePilotStates[paramInt1];
        astatePilotStates[paramInt1] = (byte)paramInt2;
        if(bIsAboutToBailout && astateBailoutStep > paramInt1 + 11)
        {
            aircraft.doWoundPilot(paramInt1, 0.0F);
            return;
        }
        if(paramInt2 > 99)
        {
            aircraft.doWoundPilot(paramInt1, 0.0F);
            if(World.cur().isHighGore())
                aircraft.doMurderPilot(paramInt1);
            if(paramInt1 == 0)
            {
                if(!bIsAboutToBailout)
                    Explosions.generateComicBulb(actor, "PK", 9F);
                FlightModel localFlightModel = ((SndAircraft) (aircraft)).FM;
                if(localFlightModel instanceof Maneuver)
                {
                    ((Maneuver)localFlightModel).set_maneuver(44);
                    ((Maneuver)localFlightModel).set_task(2);
                    localFlightModel.setCapableOfTaxiing(false);
                }
            }
            if(paramInt1 > 0 && !bIsAboutToBailout)
                Explosions.generateComicBulb(actor, "GunnerDown", 9F);
            EventLog.onPilotKilled(aircraft, paramInt1, paramActor == aircraft ? null : paramActor);
        } else
        if(i < 66 && paramInt2 > 66)
            EventLog.onPilotHeavilyWounded(aircraft, paramInt1);
        else
        if(i < 25 && paramInt2 > 25)
            EventLog.onPilotWounded(aircraft, paramInt1);
        if(paramInt2 <= 99 && paramInt1 > 0 && World.cur().diffCur.RealisticPilotVulnerability)
            aircraft.doWoundPilot(paramInt1, getPilotHealth(paramInt1));
    }

    private void doRemoveBodyFromPlane(int paramInt)
    {
        aircraft.doRemoveBodyFromPlane(paramInt);
    }

    public float getPilotHealth(int paramInt)
    {
        if(paramInt < 0 || paramInt > ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).crew - 1)
            return 0.0F;
        else
            return 1.0F - (float)astatePilotStates[paramInt] * 0.01F;
    }

    public boolean isPilotDead(int paramInt)
    {
        if(paramInt < 0 || paramInt > ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).crew - 1)
            return true;
        else
            return astatePilotStates[paramInt] == 100;
    }

    public boolean isPilotParatrooper(int paramInt)
    {
        if(paramInt < 0 || paramInt > ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).crew - 1)
            return true;
        else
            return astatePilotStates[paramInt] == 100 && astateBailoutStep > 11 + paramInt;
    }

    public void setJamBullets(int paramInt1, int paramInt2)
    {
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1] == null || ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1].length <= paramInt2 || ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1][paramInt2] == null)
            return;
        if(bIsMaster)
        {
            doSetJamBullets(paramInt1, paramInt2);
            netToMirrors(24, paramInt1, paramInt2);
        } else
        {
            netToMaster(24, paramInt1, paramInt2);
        }
    }

    private void doSetJamBullets(int paramInt1, int paramInt2)
    {
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons != null && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1] != null && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1][paramInt2] != null && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1][paramInt2].haveBullets())
        {
            if(actor == World.getPlayerAircraft() && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1][paramInt2].haveBullets())
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1][paramInt2].bulletMassa() < 0.095F)
                    HUD.log(paramInt1 > 9 ? "FailedTMGun" : "FailedMGun");
                else
                    HUD.log("FailedCannon");
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt1][paramInt2].loadBullets(0);
        }
    }

    public boolean hitDaSilk()
    {
        if(!bIsMaster)
            return false;
        if(bIsAboutToBailout)
            return false;
        if(bCheckPlayerAircraft && actor == World.getPlayerAircraft())
            return false;
        if(!bIsEnableToBailout)
            return false;
        bIsAboutToBailout = true;
        FlightModel localFlightModel = ((SndAircraft) (aircraft)).FM;
        Aircraft.debugprintln(aircraft, "I've had it, bailing out..");
        Explosions.generateComicBulb(actor, "Bailing", 5F);
        if(localFlightModel instanceof Maneuver)
        {
            ((Maneuver)localFlightModel).set_maneuver(44);
            ((Maneuver)localFlightModel).set_task(2);
            localFlightModel.setTakenMortalDamage(true, null);
        }
        return true;
    }

    public void setFlatTopString(Actor paramActor, int paramInt)
    {
        if(bIsMaster)
            netToMirrors(36, paramInt, paramInt, paramActor);
    }

    private void doSetFlatTopString(Actor paramActor, int paramInt)
    {
        if(Actor.isValid(paramActor) && (paramActor instanceof BigshipGeneric) && ((BigshipGeneric)paramActor).getAirport() != null)
        {
            BigshipGeneric localBigshipGeneric = (BigshipGeneric)paramActor;
            if(paramInt >= 0 && paramInt < 255)
                localBigshipGeneric.forceTowAircraft(aircraft, paramInt);
            else
                localBigshipGeneric.requestDetowAircraft(aircraft);
        }
    }

    public void setFMSFX(Actor paramActor, int paramInt1, int paramInt2)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(bIsMaster)
            doSetFMSFX(paramInt1, paramInt2);
        else
            netToMaster(37, paramInt1, paramInt2, paramActor);
    }

    private void doSetFMSFX(int paramInt1, int paramInt2)
    {
        ((SndAircraft) (aircraft)).FM.doRequestFMSFX(paramInt1, paramInt2);
    }

    public void setWingFold(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramActor != aircraft)
            return;
        if(!((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasWingControl)
            return;
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.wingControl == (float)paramInt)
            return;
        if(!bIsMaster)
        {
            return;
        } else
        {
            doSetWingFold(paramInt);
            netToMirrors(38, paramInt, paramInt);
            return;
        }
    }

    private void doSetWingFold(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.wingControl = paramInt;
    }

    //TODO: New door parameters
    public void setCockpitDoor(Actor actor1, int i)
    {
        if(!Actor.isValid(actor1))
            return;
        if(actor1 != aircraft)
            return;
        if(!((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasCockpitDoorControl)
            return;
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl == (float)i)
            return;
        if(!bIsMaster)
            return;
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bMoveSideDoor)
        {
            doSetCockpitDoor(i, SIDE_DOOR);
            netToMirrors(44, SIDE_DOOR, i);
        } else
        {
            doSetCockpitDoor(i, COCKPIT_DOOR);
            netToMirrors(39, COCKPIT_DOOR, i);
        }
    }

    private void doSetCockpitDoor(int i, int j)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.setActiveDoor(j);
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl = i;
        if(i == 0 && (Main.cur() instanceof Main3D) && aircraft == World.getPlayerAircraft() && HookPilot.current != null)
            HookPilot.current.doUp(false);
    }

    public void setArrestor(Actor paramActor, int paramInt)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramActor != aircraft)
            return;
        if(!((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasArrestorControl)
            return;
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.arrestorControl == (float)paramInt)
            return;
        if(!bIsMaster)
        {
            return;
        } else
        {
            doSetArrestor(paramInt);
            netToMirrors(40, paramInt, paramInt);
            return;
        }
    }

    private void doSetArrestor(int paramInt)
    {
        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.arrestorControl = paramInt;
    }

    public void update(float paramFloat)
    {
        if(World.cur().diffCur.RealisticPilotVulnerability)
        {
            for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).crew; i++)
            {
                if(astateBleedingNext[i] > 0L && bleedingTest(i))
                {
                    hitPilot(actor, i, 1);
                    if(astatePilotStates[i] > 96)
                        astateBleedingNext[i] = 0L;
                    setBleedingTime(i);
                }
                if(astatePilotStates[0] > 60)
                    ((SndAircraft) (aircraft)).FM.setCapableOfBMP(false, actor);
            }

        }
        bCriticalStatePassed = bIsAboveCriticalSpeed ^ (aircraft.getSpeed(null) > 10D);
        if(bCriticalStatePassed)
        {
            bIsAboveCriticalSpeed = ((SndAircraft) (aircraft)).FM.getSpeed() > 10F;
            for(int i = 0; i < 4; i++)
                doSetTankState(null, i, astateTankStates[i]);

            for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); i++)
                doSetEngineState(null, i, astateEngineStates[i]);

            for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); i++)
                doSetOilState(i, astateOilStates[i]);

        }
        bCriticalStatePassed = bIsAboveCondensateAlt ^ (((SndAircraft) (aircraft)).FM.getAltitude() > 7000F);
        if(bCriticalStatePassed)
        {
            bIsAboveCondensateAlt = ((SndAircraft) (aircraft)).FM.getAltitude() > 7000F;
            doSetCondensateState(bIsAboveCondensateAlt);
        }
        bCriticalStatePassed = bIsOnInadequateAOA ^ (((SndAircraft) (aircraft)).FM.getSpeed() > 17F && ((SndAircraft) (aircraft)).FM.getAOA() > 15F - ((SndAircraft) (aircraft)).FM.getAltitude() * 0.001F);
        if(bCriticalStatePassed)
        {
            bIsOnInadequateAOA = ((SndAircraft) (aircraft)).FM.getSpeed() > 17F && ((SndAircraft) (aircraft)).FM.getAOA() > 15F - ((SndAircraft) (aircraft)).FM.getAltitude() * 0.001F;
            setStallState(bIsOnInadequateAOA);
        }
        if(bIsMaster)
        {
            float f = 0.0F;
            for(int i = 0; i < 4; i++)
                f += astateTankStates[i] * astateTankStates[i];
			// TODO: Disable for ModAct compatibility
			if (bDumpFuel && aircraft instanceof TypeFuelDump)
				if (aircraft.FM.M.fuel > ((TypeFuelDump) aircraft)
						.getFuelReserve())
					aircraft.FM.M.fuel -= ((TypeFuelDump) aircraft)
							.getFlowRate() * f;
				else
					setDumpFuelState(false);
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.requestFuel(f * 0.12F * paramFloat);
            for(int i = 0; i < 4; i++)
                switch(astateTankStates[i])
                {
                case 3: // '\003'
                default:
                    break;

                case 1: // '\001'
                    if(World.Rnd().nextFloat(0.0F, 1.0F) >= 0.0125F)
                        break;
                    repairTank(i);
                    Aircraft.debugprintln(aircraft, "Tank " + i + " protector clothes the hole - leak stops..");
                    // fall through

                case 2: // '\002'
                    if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.fuel <= 0.0F)
                    {
                        repairTank(i);
                        Aircraft.debugprintln(aircraft, "Tank " + i + " runs out of fuel - leak stops..");
                    }
                    break;

                case 4: // '\004'
                    if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.00333F)
                    {
                        hitTank(aircraft, i, 1);
                        Aircraft.debugprintln(aircraft, "Tank " + i + " catches fire..");
                    }
                    break;

                case 5: // '\005'
                    if(((SndAircraft) (aircraft)).FM.getSpeed() > 111F && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
                    {
                        repairTank(i);
                        Aircraft.debugprintln(aircraft, "Tank " + i + " cuts fire..");
                    }
                    if(World.Rnd().nextFloat() < 0.0048F)
                    {
                        Aircraft.debugprintln(aircraft, "Tank " + i + " fires up to the next stage..");
                        hitTank(aircraft, i, 1);
                    }
                    if(!(actor instanceof Scheme1) || astateTankEffects[i][0] == null || Math.abs(((Tuple3d) (((Actor) (astateTankEffects[i][0])).pos.getRelPoint())).y) >= 1.8999999761581421D || ((Tuple3d) (((Actor) (astateTankEffects[i][0])).pos.getRelPoint())).x <= -2.5999999046325679D || astatePilotStates[0] >= 96)
                        break;
                    hitPilot(actor, 0, 5);
                    if(astatePilotStates[0] < 96)
                        break;
                    hitPilot(actor, 0, 101 - astatePilotStates[0]);
                    if(((SndAircraft) (aircraft)).FM.isPlayers() && Mission.isNet() && !((SndAircraft) (aircraft)).FM.isSentBuryNote())
                        Chat.sendLogRnd(3, "gore_burnedcpt", aircraft, null);
                    break;

                case 6: // '\006'
                    if(((SndAircraft) (aircraft)).FM.getSpeed() > 140F && World.Rnd().nextFloat(0.0F, 1.0F) < 0.05F)
                    {
                        repairTank(i);
                        Aircraft.debugprintln(aircraft, "Tank " + i + " cuts fire..");
                    }
                    if(World.Rnd().nextFloat() < 0.02F)
                    {
                        Aircraft.debugprintln(aircraft, "Tank " + i + " EXPLODES!.");
                        explodeTank(aircraft, i);
                    }
                    if(!(actor instanceof Scheme1) || astateTankEffects[i][0] == null || Math.abs(((Tuple3d) (((Actor) (astateTankEffects[i][0])).pos.getRelPoint())).y) >= 1.8999999761581421D || ((Tuple3d) (((Actor) (astateTankEffects[i][0])).pos.getRelPoint())).x <= -2.5999999046325679D || astatePilotStates[0] >= 96)
                        break;
                    hitPilot(actor, 0, 7);
                    if(astatePilotStates[0] < 96)
                        break;
                    hitPilot(actor, 0, 101 - astatePilotStates[0]);
                    if(((SndAircraft) (aircraft)).FM.isPlayers() && Mission.isNet() && !((SndAircraft) (aircraft)).FM.isSentBuryNote())
                        Chat.sendLogRnd(3, "gore_burnedcpt", aircraft, null);
                    break;
                }

            for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum(); i++)
            {
                if(astateEngineStates[i] > 1)
                {
                    ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].setReadyness(actor, ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].getReadyness() - (float)astateEngineStates[i] * 0.00025F * paramFloat);
                    if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].getReadyness() < 0.2F && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].getReadyness() != 0.0F)
                        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].setEngineDies(actor);
                }
                switch(astateEngineStates[i])
                {
                case 3: // '\003'
                    if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.01F)
                    {
                        hitEngine(aircraft, i, 1);
                        Aircraft.debugprintln(aircraft, "Engine " + i + " catches fire..");
                    }
                    break;

                case 4: // '\004'
                    if(((SndAircraft) (aircraft)).FM.getSpeed() > 111F && World.Rnd().nextFloat(0.0F, 1.0F) < 0.15F)
                    {
                        repairEngine(i);
                        Aircraft.debugprintln(aircraft, "Engine " + i + " cuts fire..");
                    }
                    if((actor instanceof Scheme1) && World.Rnd().nextFloat() < 0.06F)
                    {
                        Aircraft.debugprintln(aircraft, "Engine 0 detonates and explodes, fatal damage level forced..");
                        aircraft.msgCollision(actor, "CF_D0", "CF_D0");
                    }
                    if((actor instanceof Scheme1) && astatePilotStates[0] < 96)
                    {
                        hitPilot(actor, 0, 4);
                        if(astatePilotStates[0] >= 96)
                        {
                            hitPilot(actor, 0, 101 - astatePilotStates[0]);
                            if(((SndAircraft) (aircraft)).FM.isPlayers() && Mission.isNet() && !((SndAircraft) (aircraft)).FM.isSentBuryNote())
                                Chat.sendLogRnd(3, "gore_burnedcpt", aircraft, null);
                        }
                    }
                    break;
                }
                if(astateOilStates[i] > 0)
                    ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].setReadyness(aircraft, ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].getReadyness() - 0.001875F * paramFloat);
            }
			// TODO: Edit allowing F-86D to keep rocket tray door open
			// indefinitely
			if (!((actor instanceof TypeFighter) && aircraft.FM.CT.bHasBayDoors)) {
				// TODO: Don't disable this statement and bailout. Else, you
				// won't be able to bailout!
				bailout();
	            if(World.Rnd().nextFloat() < 0.25F && !aircraft.FM.CT.saveWeaponControl[3])
	                if(aircraft.FM.isPlayers() && (aircraft.FM instanceof RealFlightModel) && ((RealFlightModel)aircraft.FM).isRealMode())
	                {
	                    if(!aircraft.FM.CT.bHasBayDoors)
	                        aircraft.FM.CT.BayDoorControl = 0.0F;
	                } else
	                if(!(actor instanceof TypeBomber) || aircraft.FM.isReadyToReturn())
	                    aircraft.FM.CT.BayDoorControl = 0.0F;
	            bailout();
	        } else
	        if(World.Rnd().nextFloat() < 0.125F && !aircraft.FM.CT.saveWeaponControl[3] && (!(actor instanceof TypeBomber) || aircraft.FM.AP.way.curr().Action != 3))
	            aircraft.FM.CT.BayDoorControl = 0.0F;
		}
	}

    private void bailout()
    {
        if(bIsAboutToBailout)
            if(astateBailoutStep >= 0 && astateBailoutStep < 2)
            {
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.getCockpitDoor() > 0.5F)
                {
                    astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    astateBailoutStep = 2;
                }
            } else
            if(astateBailoutStep >= 2 && astateBailoutStep <= 3)
            {
                switch(astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(bIsMaster)
                    netToMirrors(20, astateBailoutStep, 1);
                astateBailoutStep = (byte)(astateBailoutStep + 1);
                if(astateBailoutStep == 3 && (actor instanceof P_39))
                    astateBailoutStep = (byte)(astateBailoutStep + 1);
                if(astateBailoutStep == 4)
                    astateBailoutStep = 11;
            } else
            if(astateBailoutStep >= 11 && astateBailoutStep <= 19)
            {
                float f1 = ((SndAircraft) (aircraft)).FM.getSpeed();
                float f2 = (float)((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Loc)).z;
                float f3 = 140F;
                if((aircraft instanceof HE_162) || (aircraft instanceof GO_229) || (aircraft instanceof ME_262HGII) || (aircraft instanceof DO_335))
                    f3 = 9999.9F;
                if(Pitot.Indicator(f2, f1) < f3 && ((SndAircraft) (aircraft)).FM.getOverload() < 2.0F || !bIsMaster)
                {
                    int i = astateBailoutStep;
                    if(bIsMaster)
                        netToMirrors(20, astateBailoutStep, 1);
                    astateBailoutStep = (byte)(astateBailoutStep + 1);
                    if(i == 11)
                    {
                        ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, null);
                        if((((SndAircraft) (aircraft)).FM instanceof Maneuver) && ((Maneuver)((SndAircraft) (aircraft)).FM).get_maneuver() != 44)
                        {
                            World.cur();
                            if(actor != World.getPlayerAircraft())
                                ((Maneuver)((SndAircraft) (aircraft)).FM).set_maneuver(44);
                        }
                    }
                    if(astatePilotStates[i - 11] < 99)
                    {
                        doRemoveBodyFromPlane(i - 10);
                        if(i == 11)
                        {
                            if(aircraft instanceof HE_162)
                            {
                                ((HE_162)aircraft).doEjectCatapult();
                                astateBailoutStep = 51;
                                ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, null);
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[0] = false;
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[1] = false;
                                astateBailoutStep = -1;
                                return;
                            }
                            if(aircraft instanceof GO_229)
                            {
                                ((GO_229)aircraft).doEjectCatapult();
                                astateBailoutStep = 51;
                                ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, null);
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[0] = false;
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[1] = false;
                                astateBailoutStep = -1;
                                return;
                            }
                            if(aircraft instanceof DO_335)
                            {
                                ((DO_335)aircraft).doEjectCatapult();
                                astateBailoutStep = 51;
                                ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, null);
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[0] = false;
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[1] = false;
                                astateBailoutStep = -1;
                                return;
                            }
                            if(aircraft instanceof ME_262HGII)
                            {
                                ((ME_262HGII)aircraft).doEjectCatapult();
                                astateBailoutStep = 51;
                                ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, null);
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[0] = false;
                                ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[1] = false;
                                astateBailoutStep = -1;
                                return;
                            }
                        }
                        setPilotState(aircraft, i - 11, 100, false);
                        if(!actor.isNet() || actor.isNetMaster())
                        {
                            try
                            {
                                Hook localHook = actor.findHook("_ExternalBail0" + (i - 10));
                                if(localHook != null)
                                {
                                    Loc localLoc = new Loc(0.0D, 0.0D, 0.0D, World.Rnd().nextFloat(-45F, 45F), 0.0F, 0.0F);
                                    localHook.computePos(actor, actor.pos.getAbs(), localLoc);
                                    new Paratrooper(actor, actor.getArmy(), i - 11, localLoc, ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld);
                                    ((SndAircraft) (aircraft)).FM.setTakenMortalDamage(true, null);
                                    if(i == 11)
                                    {
                                        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[0] = false;
                                        ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[1] = false;
                                    }
                                    if(i > 10 && i <= 19)
                                        EventLog.onBailedOut(aircraft, i - 11);
                                }
                            }
                            catch(Exception exception) { }
                            if(astateBailoutStep == 19 && actor == World.getPlayerAircraft() && !World.isPlayerGunner() && ((SndAircraft) (aircraft)).FM.brakeShoe)
                                MsgDestroy.Post(Time.current() + 1000L, aircraft);
                        }
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1 && getPilotHealth(0) > 0.0F)
        {
            if(aircraft instanceof JU_88NEW)
            {
                float f = ((SndAircraft) (aircraft)).FM.getAltitude() - Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Loc)).y);
                if(f < 0.3F)
                {
                    aircraft.blisterRemoved(1);
                    return;
                }
            }
            aircraft.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage((ActorHMesh)actor, aircraft.hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld);
            localWreckage.setSpeed(localVector3d);
            aircraft.blisterRemoved(1);
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(aircraft.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && getPilotHealth(i - 1) > 0.0F)
            {
                aircraft.hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage localWreckage = new Wreckage((ActorHMesh)actor, aircraft.hierMesh().chunkFind("Blister" + i + "_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld);
                localWreckage.setSpeed(localVector3d);
                aircraft.blisterRemoved(i);
            }

    }

    public void netUpdate(boolean paramBoolean1, boolean paramBoolean2, NetMsgInput paramNetMsgInput)
        throws IOException
    {
        if(paramBoolean2)
        {
            if(paramBoolean1)
            {
                int i = paramNetMsgInput.readUnsignedByte();
                int j = paramNetMsgInput.readUnsignedByte();
                int k = paramNetMsgInput.readUnsignedByte();
                Actor localActor = null;
                if(paramNetMsgInput.available() > 0)
                {
                    NetObj localNetObj = paramNetMsgInput.readNetObj();
                    if(localNetObj != null)
                        localActor = (Actor)localNetObj.superObj();
                }
                switch(i)
                {
                case 1: // '\001'
                    setEngineState(localActor, j, k);
                    break;

                case 2: // '\002'
                    setEngineSpecificDamage(localActor, j, k);
                    break;

                case 3: // '\003'
                    explodeEngine(localActor, j);
                    break;

                case 4: // '\004'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (E.S.)");

                case 5: // '\005'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (E.R.)");

                case 6: // '\006'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (E.E.)");

                case 7: // '\007'
                    setEngineDies(localActor, j);
                    break;

                case 29: // '\035'
                    setEngineStuck(localActor, j);
                    break;

                case 27: // '\033'
                    setEngineCylinderKnockOut(localActor, j, k);
                    break;

                case 28: // '\034'
                    setEngineMagnetoKnockOut(localActor, j, k);
                    break;

                case 8: // '\b'
                    setSootState(localActor, j, k);
                    break;

                case 25: // '\031'
                    setEngineReadyness(localActor, j, k);
                    break;

                case 26: // '\032'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (EN.ST.)");

                case 9: // '\t'
                    setTankState(localActor, j, k);
                    break;

                case 10: // '\n'
                    explodeTank(localActor, j);
                    break;

                case 11: // '\013'
                    setOilState(localActor, j, k);
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
                    setPilotState(localActor, j, k);
                    break;

                case 46: // '.'
                    setPilotWound(localActor, j, k);
                    break;

                case 47: // '/'
                    setBleedingPilot(localActor, j, k);
                    break;

                case 18: // '\022'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented feature (K.P.)");

                case 19: // '\023'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented feature (H.S.)");

                case 20: // '\024'
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unexpected command (B.O.)");

                case 21: // '\025'
                    setControlsDamage(localActor, k);
                    break;

                case 22: // '\026'
                    setInternalDamage(localActor, k);
                    break;

                case 23: // '\027'
                    setCockpitState(localActor, k);
                    break;

                case 24: // '\030'
                    setJamBullets(j, k);
                    break;

                case 34: // '"'
                    ((TypeDockable)aircraft).typeDockableRequestAttach(localActor, j, true);
                    break;

                case 35: // '#'
                    ((TypeDockable)aircraft).typeDockableRequestDetach(localActor, j, true);
                    break;

                case 32: // ' '
                    ((TypeDockable)aircraft).typeDockableRequestAttach(localActor, j, k > 0);
                    break;

                case 33: // '!'
                    ((TypeDockable)aircraft).typeDockableRequestDetach(localActor, j, k > 0);
                    break;

                case 37: // '%'
                    setFMSFX(localActor, j, k);
                    break;

                case 42: // '*'
                    setBeacon(localActor, j, k, true);
                    break;

                case 44: // ','
                    setGyroAngle(localActor, j, k, true);
                    break;

                case 45: // '-'
                    setSpreadAngle(localActor, j, k, true);
                    break;
                    
					// TODO: ++ Added Code for Net Replication ++
				case _AS_SELECTED_ROCKET_HOOK:
					this.setRocketHook(localActor, j, true);
					break;

				case _AS_WEAPON_FIRE_MODE:
					this.setWeaponFireMode(localActor, j, true);
					break;

				case _AS_WEAPON_RELEASE_DELAY:
					int releaseDelayLow = j;
					int releaseDelayHigh = k << 8;
					this.setWeaponReleaseDelay(localActor, releaseDelayLow + releaseDelayHigh, true);
					break;
					// TODO: -- Added Code for Net Replication --
                }
            } else
            {
                ((Actor) (aircraft)).net.postTo(((Actor) (aircraft)).net.masterChannel(), new NetMsgGuaranted(paramNetMsgInput, paramNetMsgInput.available() > 3 ? 1 : 0));
            }
        } else
        {
            if(((Actor) (aircraft)).net.isMirrored())
                ((Actor) (aircraft)).net.post(new NetMsgGuaranted(paramNetMsgInput, paramNetMsgInput.available() > 3 ? 1 : 0));
            int i = paramNetMsgInput.readUnsignedByte();
            int j = paramNetMsgInput.readUnsignedByte();
            int k = paramNetMsgInput.readUnsignedByte();
            Actor localActor = null;
            if(paramNetMsgInput.available() > 0)
            {
                NetObj localNetObj = paramNetMsgInput.readNetObj();
                if(localNetObj != null)
                    localActor = (Actor)localNetObj.superObj();
            }
            switch(i)
            {
            case 32: // ' '
            case 33: // '!'
            case 41: // ')'
            case 43: // '+'
            default:
                break;

            case 1: // '\001'
                doSetEngineState(localActor, j, k);
                break;

            case 2: // '\002'
                doSetEngineSpecificDamage(j, k);
                break;

            case 3: // '\003'
                doExplodeEngine(j);
                break;

            case 4: // '\004'
                doSetEngineStarts(j);
                break;

            case 5: // '\005'
                doSetEngineRunning(j);
                break;

            case 6: // '\006'
                doSetEngineStops(j);
                break;

            case 7: // '\007'
                doSetEngineDies(j);
                break;

            case 29: // '\035'
                doSetEngineStuck(j);
                break;

            case 27: // '\033'
                doSetEngineCylinderKnockOut(j, k);
                break;

            case 28: // '\034'
                doSetEngineMagnetoKnockOut(j, k);
                break;

            case 8: // '\b'
                doSetSootState(j, k);
                break;

            case 25: // '\031'
                doSetEngineReadyness(j, k);
                break;

            case 26: // '\032'
                doSetEngineStage(j, k);
                break;

            case 9: // '\t'
                doSetTankState(localActor, j, k);
                break;

            case 10: // '\n'
                doExplodeTank(j);
                break;

            case 11: // '\013'
                doSetOilState(j, k);
                break;

            case 12: // '\f'
                if(k == 5)
                    doSetGliderBoostOn();
                else
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in signal (G.S.B./On)");
                // fall through

            case 13: // '\r'
                if(k == 7)
                    doSetGliderBoostOff();
                else
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in signal (G.S.B./Off)");
                // fall through

            case 14: // '\016'
                if(k == 9)
                    doSetGliderCutCart();
                else
                    throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Corrupt data in signal (G.C.C./Off)");
                // fall through

            case 15: // '\017'
                doSetAirShowState(k == 1);
                break;

            case 30: // '\036'
                doSetNavLightsState(k == 1);
                break;

            case 31: // '\037'
                doSetLandingLightState(k == 1);
                break;

            case 16: // '\020'
                doSetStallState(k == 1);
                break;

            case 17: // '\021'
                doSetPilotState(j, k, localActor);
                break;

            case 46: // '.'
                doSetWoundPilot(k);
                break;

            case 47: // '/'
                doSetBleedingPilot(j, k, localActor);
                break;

            case 18: // '\022'
                throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented signal (K.P.)");

            case 19: // '\023'
                throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Unimplemented signal (H.S.)");

            case 20: // '\024'
                bIsAboutToBailout = true;
                astateBailoutStep = (byte)j;
                bailout();
                break;

            case 21: // '\025'
                throw new RuntimeException("(" + aircraft.typedName() + ") A.S.: Uniexpected signal (C.A.D.)");

            case 22: // '\026'
                doSetInternalDamage(k);
                break;

            case 23: // '\027'
                doSetCockpitState(k);
                break;

            case 24: // '\030'
                doSetJamBullets(j, k);
                break;

            case 34: // '"'
                ((TypeDockable)aircraft).typeDockableDoAttachToDrone(localActor, j);
                break;

            case 35: // '#'
                ((TypeDockable)aircraft).typeDockableDoDetachFromDrone(j);
                break;

            case 36: // '$'
                doSetFlatTopString(localActor, k);
                break;

            case 37: // '%'
                doSetFMSFX(j, k);
                break;

            case 38: // '&'
                doSetWingFold(k);
                break;
		//TODO: Edited
            case 39: // '\''
                doSetCockpitDoor(j, k);
                break;

            case 40: // '('
                doSetArrestor(k);
                break;

            case 48: // '0'
                setAirShowSmokeType(j);
                break;

            case 49: // '1'
                setAirShowSmokeEnhanced(j == 1);
                break;

            case 50: // '2'
                doSetDumpFuelState(j == 1);
                break;

            case 42: // '*'
                doSetBeacon(localActor, j, k);
                break;

            case 44: // ','
                doSetGyroAngle(localActor, j, k);
                break;

            case 45: // '-'
                doSetSpreadAngle(localActor, j, k);
                break;
                
				// TODO: ++ Added Code for Net Replication ++
			case _AS_SELECTED_ROCKET_HOOK:
				this.doSetRocketHook(localActor, j);
				break;
			case _AS_WEAPON_FIRE_MODE:
				this.doSetWeaponFireMode(localActor, j);
				break;
			case _AS_WEAPON_RELEASE_DELAY:
				int releaseDelayLow = j;
				int releaseDelayHigh = k << 8;
				this.doSetWeaponReleaseDelay(localActor, releaseDelayLow + releaseDelayHigh);
				break;
				// TODO: -- Added Code for Net Replication --
            }
        }
    }

    public void netReplicate(NetMsgGuaranted paramNetMsgGuaranted)
        throws IOException
    {
        int j;
        if(aircraft instanceof FW_190A8MSTL)
            j = 1;
        else
            j = ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum();
        for(int i = 0; i < j; i++)
        {
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].replicateToNet(paramNetMsgGuaranted);
            paramNetMsgGuaranted.writeByte(astateEngineStates[i]);
        }

        for(int i = 0; i < 4; i++)
            paramNetMsgGuaranted.writeByte(astateTankStates[i]);

        for(int i = 0; i < j; i++)
            paramNetMsgGuaranted.writeByte(astateOilStates[i]);

        paramNetMsgGuaranted.writeByte((bShowSmokesOn ? 1 : 0) | (bNavLightsOn ? 2 : 0) | (bLandingLightOn ? 4 : 0));
        paramNetMsgGuaranted.writeByte(astateCockpitState);
        paramNetMsgGuaranted.writeByte(astateBailoutStep);
        if(aircraft instanceof TypeBomber)
            ((TypeBomber)aircraft).typeBomberReplicateToNet(paramNetMsgGuaranted);
        if(aircraft instanceof TypeDockable)
            ((TypeDockable)aircraft).typeDockableReplicateToNet(paramNetMsgGuaranted);
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasWingControl)
        {
            paramNetMsgGuaranted.writeByte((int)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.wingControl);
            paramNetMsgGuaranted.writeByte((int)(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.getWing() * 255F));
        }
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasCockpitDoorControl)
            paramNetMsgGuaranted.writeByte((int)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl);
        paramNetMsgGuaranted.writeByte(bIsEnableToBailout ? 1 : 0);
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasArrestorControl)
        {
            paramNetMsgGuaranted.writeByte((int)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.arrestorControl);
            paramNetMsgGuaranted.writeByte((int)(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.getArrestor() * 255F));
        }
        for(int i = 0; i < j; i++)
            paramNetMsgGuaranted.writeByte(astateSootStates[i]);

        if(bWantBeaconsNet)
            paramNetMsgGuaranted.writeByte(beacon);
        if(aircraft instanceof TypeHasToKG)
        {
            paramNetMsgGuaranted.writeByte(torpedoGyroAngle);
            paramNetMsgGuaranted.writeByte(torpedoSpreadAngle);
        }
        paramNetMsgGuaranted.writeShort(((Aircraft) (aircraft)).armingSeed);
        setArmingSeeds(((Aircraft) (aircraft)).armingSeed);
        if(aircraft.isNetPlayer())
        {
            int k = (int)Math.sqrt(World.cur().userCoverMashineGun - 100F) / 6;
            k += 6 * ((int)Math.sqrt(World.cur().userCoverCannon - 100F) / 6);
            k += 36 * ((int)Math.sqrt(World.cur().userCoverRocket - 100F) / 6);
            paramNetMsgGuaranted.writeByte(k);
        }
        if(externalStoresDropped)
            paramNetMsgGuaranted.writeByte(1);
        else
            paramNetMsgGuaranted.writeByte(0);
        //TODO
        paramNetMsgGuaranted.writeByte(iAirShowSmoke);
        paramNetMsgGuaranted.writeByte(bAirShowSmokeEnhanced ? 1 : 0);
        paramNetMsgGuaranted.writeByte(bDumpFuel ? 1 : 0);
		// TODO: ++ Added Code for Net Replication ++
		paramNetMsgGuaranted.writeByte(aircraft.FM.CT.rocketHookSelected);
		paramNetMsgGuaranted.writeByte(aircraft.FM.CT.weaponFireMode);
		int releaseDelayLow = (byte)aircraft.FM.CT.weaponReleaseDelay;
		int releaseDelayHigh = (byte)((int)aircraft.FM.CT.weaponReleaseDelay >> 8);
		paramNetMsgGuaranted.writeByte(releaseDelayLow);
		paramNetMsgGuaranted.writeByte(releaseDelayHigh);
		// TODO: -- Added Code for Net Replication --
    }

    public void netFirstUpdate(NetMsgInput paramNetMsgInput)
        throws IOException
    {
        int k;
        if(aircraft instanceof FW_190A8MSTL)
            k = 1;
        else
            k = ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.getNum();
        for(int i = 0; i < k; i++)
        {
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[i].replicateFromNet(paramNetMsgInput);
            int j = paramNetMsgInput.readUnsignedByte();
            doSetEngineState(null, i, j);
        }

        for(int i = 0; i < 4; i++)
        {
            int j = paramNetMsgInput.readUnsignedByte();
            doSetTankState(null, i, j);
        }

        for(int i = 0; i < k; i++)
        {
            int j = paramNetMsgInput.readUnsignedByte();
            doSetOilState(i, j);
        }

        int j = paramNetMsgInput.readUnsignedByte();
        doSetAirShowState((j & 1) != 0);
        doSetNavLightsState((j & 2) != 0);
        doSetLandingLightState((j & 4) != 0);
        j = paramNetMsgInput.readUnsignedByte();
        doSetCockpitState(j);
        j = paramNetMsgInput.readUnsignedByte();
        if(j != 0)
        {
            bIsAboutToBailout = true;
            astateBailoutStep = (byte)j;
            for(int i = 1; i <= Math.min(astateBailoutStep, 3); i++)
                if(aircraft.hierMesh().chunkFindCheck("Blister" + (i - 1) + "_D0") != -1)
                    aircraft.hierMesh().hideSubTrees("Blister" + (i - 1) + "_D0");

            if(astateBailoutStep >= 11 && astateBailoutStep <= 20)
            {
                int m = astateBailoutStep;
                if(astateBailoutStep == 20)
                    m = 19;
                m -= 11;
                for(int i = 0; i <= m; i++)
                    doRemoveBodyFromPlane(i + 1);

            }
        }
		//TODO /Storebro's Update
		if (paramNetMsgInput.available() == 0)
			return;
		paramNetMsgInput.fixed();
		try {
			if (aircraft instanceof TypeBomber)
				((TypeBomber) aircraft).typeBomberReplicateFromNet(paramNetMsgInput);
		} catch (Exception e) {
			paramNetMsgInput.reset();
		}
		if (paramNetMsgInput.available() == 0)
			return;
		paramNetMsgInput.fixed();
		try {
			if (aircraft instanceof TypeDockable)
				((TypeDockable) aircraft)
						.typeDockableReplicateFromNet(paramNetMsgInput);
		} catch (Exception e) {
			paramNetMsgInput.reset();
		}

		// /Storebro's Update
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasWingControl)
        {
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.wingControl = paramNetMsgInput.readUnsignedByte();
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.forceWing((float)paramNetMsgInput.readUnsignedByte() / 255F);
            aircraft.wingfold_ = ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.getWing();
        }
        if(paramNetMsgInput.available() == 0)
            return;
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasCockpitDoorControl)
        {
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl = paramNetMsgInput.readUnsignedByte();
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.forceCockpitDoor(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.cockpitDoorControl);
        }
        if(paramNetMsgInput.available() == 0)
            return;
        bIsEnableToBailout = paramNetMsgInput.readUnsignedByte() == 1;
        if(paramNetMsgInput.available() == 0)
            return;
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.bHasArrestorControl)
        {
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.arrestorControl = paramNetMsgInput.readUnsignedByte();
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.forceArrestor((float)paramNetMsgInput.readUnsignedByte() / 255F);
            aircraft.arrestor_ = ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.getArrestor();
        }
        if(paramNetMsgInput.available() == 0)
            return;
        for(int i = 0; i < k; i++)
        {
            j = paramNetMsgInput.readUnsignedByte();
            doSetSootState(i, j);
        }

        if(bWantBeaconsNet)
            beacon = paramNetMsgInput.readUnsignedByte();
        if(aircraft instanceof TypeHasToKG)
        {
            torpedoGyroAngle = paramNetMsgInput.readUnsignedByte();
            torpedoSpreadAngle = paramNetMsgInput.readUnsignedByte();
        }
        aircraft.armingSeed = paramNetMsgInput.readUnsignedShort();
        setArmingSeeds(((Aircraft) (aircraft)).armingSeed);
        if(aircraft.isNetPlayer())
        {
            int n = paramNetMsgInput.readUnsignedByte();
            int i1 = n % 6;
            float f = Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F);
            updConvDist(i1, 0, f);
            n = (n - i1) / 6;
            i1 = n % 6;
            updConvDist(i1, 1, f);
            n = (n - i1) / 6;
            updConvDist(n, 2, f);
        }
        j = paramNetMsgInput.readUnsignedByte();
        if(!externalStoresDropped && j > 0)
        {
            externalStoresDropped = true;
            aircraft.dropExternalStores(false);
        }
        //TODO
        setAirShowSmokeType(paramNetMsgInput.readUnsignedByte());
        setAirShowSmokeEnhanced(paramNetMsgInput.readUnsignedByte() == 1);
        doSetDumpFuelState(paramNetMsgInput.readUnsignedByte() == 1);
		// TODO: ++ Added Code for Net Replication ++
		if(paramNetMsgInput.available() == 0)
			return;
		this.doSetRocketHook(aircraft, paramNetMsgInput.readUnsignedByte());
		this.doSetWeaponFireMode(aircraft, paramNetMsgInput.readUnsignedByte());
		int releaseDelayLow = paramNetMsgInput.readUnsignedByte();
		int releaseDelayHigh = paramNetMsgInput.readUnsignedByte() << 8;
		this.doSetWeaponReleaseDelay(aircraft, releaseDelayLow + releaseDelayHigh);
		// TODO: -- Added Code for Net Replication --
		//TODO: Bay Doors
        if(aircraft.FM.CT.bHasBayDoors)
        	((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.BayDoorControl = paramNetMsgInput.readUnsignedByte();
    }

    void updConvDist(int paramInt1, int paramInt2, float paramFloat)
    {
        float f = (float)(paramInt1 * paramInt1) * 36F + 100F;
        try
        {
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt2] != null)
                if(paramInt2 == 2)
                {
                    for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt2].length; i++)
                        ((RocketGun)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt2][i]).setConvDistance(f, paramFloat);

                } else
                {
                    for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt2].length; i++)
                        ((MGunAircraftGeneric)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[paramInt2][i]).setConvDistance(f, paramFloat);

                }
        }
        catch(Exception localException)
        {
            System.out.println(localException.getMessage());
            localException.printStackTrace();
        }
    }

    void setArmingSeeds(int paramInt)
    {
        aircraft.armingRnd = new RangeRandom(((Aircraft) (aircraft)).armingSeed);
        try
        {
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[2] != null)
            {
                for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[2].length; i++)
                    ((RocketGun)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[2][i]).setSpreadRnd(((Aircraft) (aircraft)).armingRnd.nextInt());

            }
        }
        catch(Exception localException)
        {
            System.out.println(localException.getMessage());
            localException.printStackTrace();
        }
    }

    private void netToMaster(int paramInt1, int paramInt2, int paramInt3)
    {
        netToMaster(paramInt1, paramInt2, paramInt3, null);
    }

    public void netToMaster(int paramInt1, int paramInt2, int paramInt3, Actor paramActor)
    {
        if(!bIsMaster)
        {
            if(!aircraft.netNewAState_isEnable(true))
                return;
            if(itemsToMaster == null)
                itemsToMaster = new Item[41];
            if(sendedMsg(itemsToMaster, paramInt1, paramInt2, paramInt3, paramActor))
                return;
            try
            {
                NetMsgGuaranted localNetMsgGuaranted = aircraft.netNewAStateMsg(true);
                if(localNetMsgGuaranted != null)
                {
                    localNetMsgGuaranted.writeByte((byte)paramInt1);
                    localNetMsgGuaranted.writeByte((byte)paramInt2);
                    localNetMsgGuaranted.writeByte((byte)paramInt3);
                    com.maddox.il2.engine.ActorNet localActorNet = null;
                    if(Actor.isValid(paramActor))
                        localActorNet = paramActor.net;
                    if(localActorNet != null)
                        localNetMsgGuaranted.writeNetObj(localActorNet);
                    aircraft.netSendAStateMsg(true, localNetMsgGuaranted);
                    return;
                }
            }
            catch(Exception localException)
            {
                System.out.println(localException.getMessage());
                localException.printStackTrace();
            }
        }
    }

    private void netToMirrors(int paramInt1, int paramInt2, int paramInt3)
    {
        netToMirrors(paramInt1, paramInt2, paramInt3, null);
    }

    public void netToMirrors(int paramInt1, int paramInt2, int paramInt3, Actor paramActor)
    {
        if(!aircraft.netNewAState_isEnable(false))
            return;
        if(itemsToMirrors == null)
            itemsToMirrors = new Item[41];
        if(sendedMsg(itemsToMirrors, paramInt1, paramInt2, paramInt3, paramActor))
            return;
        try
        {
            NetMsgGuaranted localNetMsgGuaranted = aircraft.netNewAStateMsg(false);
            if(localNetMsgGuaranted != null)
            {
                localNetMsgGuaranted.writeByte((byte)paramInt1);
                localNetMsgGuaranted.writeByte((byte)paramInt2);
                localNetMsgGuaranted.writeByte((byte)paramInt3);
                com.maddox.il2.engine.ActorNet localActorNet = null;
                if(Actor.isValid(paramActor))
                    localActorNet = paramActor.net;
                if(localActorNet != null)
                    localNetMsgGuaranted.writeNetObj(localActorNet);
                aircraft.netSendAStateMsg(false, localNetMsgGuaranted);
                return;
            }
        }
        catch(Exception localException)
        {
            System.out.println(localException.getMessage());
            localException.printStackTrace();
        }
    }

    private boolean sendedMsg(Item paramArrayOfItem[], int paramInt1, int paramInt2, int paramInt3, Actor paramActor)
    {
        if(paramInt1 < 0 || paramInt1 >= paramArrayOfItem.length)
            return false;
        Item localItem = paramArrayOfItem[paramInt1];
        if(localItem == null)
        {
            localItem = new Item();
            localItem.set(paramInt2, paramInt3, paramActor);
            paramArrayOfItem[paramInt1] = localItem;
            return false;
        }
        if(localItem.equals(paramInt2, paramInt3, paramActor))
        {
            return true;
        } else
        {
            localItem.set(paramInt2, paramInt3, paramActor);
            return false;
        }
    }

    private int cvt(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2)
    {
        paramFloat1 = Math.min(Math.max(paramFloat1, paramFloat2), paramFloat3);
        return (int)((float)paramInt1 + ((float)(paramInt2 - paramInt1) * (paramFloat1 - paramFloat2)) / (paramFloat3 - paramFloat2));
    }

    private float cvt(int paramInt1, int paramInt2, int paramInt3, float paramFloat1, float paramFloat2)
    {
        paramInt1 = Math.min(Math.max(paramInt1, paramInt2), paramInt3);
        return paramFloat1 + ((paramFloat2 - paramFloat1) * (float)(paramInt1 - paramInt2)) / (float)(paramInt3 - paramInt2);
    }

    public float getGyroAngle()
    {
        if(torpedoGyroAngle == 0)
            return 0.0F;
        else
            return cvt(torpedoGyroAngle, 1, 65535, -50F, 50F);
    }

    public void setGyroAngle(float paramFloat)
    {
        int i = 0;
        if(paramFloat != 0.0F)
            i = cvt(paramFloat, -50F, 50F, 1, 65535);
        torpedoGyroAngle = i;
    }

    public void replicateGyroAngleToNet()
    {
        int i = cvt(getGyroAngle(), -50F, 50F, 1, 65535);
        int j = i & 0xff00;
        j >>= 8;
        int k = i & 0xff;
        setGyroAngle(actor, j, k, false);
    }

    private void setGyroAngle(Actor paramActor, int paramInt1, int paramInt2, boolean paramBoolean)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramBoolean)
            doSetGyroAngle(paramActor, paramInt1, paramInt2);
        if(bIsMaster)
            netToMirrors(44, paramInt1, paramInt2);
        else
            netToMaster(44, paramInt1, paramInt2, paramActor);
    }

    private void doSetGyroAngle(Actor paramActor, int paramInt1, int paramInt2)
    {
        torpedoGyroAngle = paramInt1 << 8 | paramInt2;
    }

    public int getSpreadAngle()
    {
        return torpedoSpreadAngle;
    }

    public void setSpreadAngle(int paramInt)
    {
        torpedoSpreadAngle = paramInt;
        if(torpedoSpreadAngle < 0)
            torpedoSpreadAngle = 0;
        if(torpedoSpreadAngle > 30)
            torpedoSpreadAngle = 30;
    }

    public void replicateSpreadAngleToNet()
    {
        setSpreadAngle(actor, getSpreadAngle(), 0, false);
    }

    private void setSpreadAngle(Actor paramActor, int paramInt1, int paramInt2, boolean paramBoolean)
    {
        if(!Actor.isValid(paramActor))
            return;
        if(paramBoolean)
            doSetSpreadAngle(paramActor, paramInt1, 0);
        if(bIsMaster)
            netToMirrors(45, paramInt1, 0);
        else
            netToMaster(45, paramInt1, 0, paramActor);
    }

    private void doSetSpreadAngle(Actor paramActor, int paramInt1, int paramInt2)
    {
        setSpreadAngle(paramInt1);
    }

	// TODO: ++ Added Code for Net Replication ++
	public void replicateRocketHookToNet(int theRocketHook)
	{
		setRocketHook(actor, theRocketHook, false);
	}

	private void setRocketHook(Actor theActor, int theRocketHook, boolean applySetting)
	{
		if(!Actor.isValid(theActor))
			return;
		if(applySetting)
			doSetRocketHook(theActor, theRocketHook);
		if(bIsMaster)
			netToMirrors(_AS_SELECTED_ROCKET_HOOK, theRocketHook, 0);
		else
			netToMaster(_AS_SELECTED_ROCKET_HOOK, theRocketHook, 0, theActor);
	}

	private void doSetRocketHook(Actor theActor, int theRocketHook)
	{
		aircraft.FM.CT.doSetRocketHook(theRocketHook);
	}

	public void replicateWeaponFireModeToNet(int theWeaponFireMode)
	{
		setWeaponFireMode(actor, theWeaponFireMode, false);
	}

	private void setWeaponFireMode(Actor theActor, int theWeaponFireMode, boolean applySetting)
	{
		if(!Actor.isValid(theActor))
			return;
		if(applySetting)
			doSetWeaponFireMode(theActor, theWeaponFireMode);
		if(bIsMaster)
			netToMirrors(_AS_WEAPON_FIRE_MODE, theWeaponFireMode, 0);
		else
			netToMaster(_AS_WEAPON_FIRE_MODE, theWeaponFireMode, 0, theActor);
	}

	private void doSetWeaponFireMode(Actor theActor, int theWeaponFireMode)
	{
		aircraft.FM.CT.doSetWeaponFireMode(theWeaponFireMode);
	}

	public void replicateWeaponReleaseDelay(long theWeaponReleaseDelay)
	{
		setWeaponReleaseDelay(actor, theWeaponReleaseDelay, false);
	}

	private void setWeaponReleaseDelay(Actor theActor, long theWeaponReleaseDelay, boolean applySetting)
	{
		if(!Actor.isValid(theActor))
			return;
		if(applySetting)
			doSetWeaponReleaseDelay(theActor, theWeaponReleaseDelay);
		int releaseDelayLow = (byte)aircraft.FM.CT.weaponReleaseDelay;
		int releaseDelayHigh = (byte)((int)aircraft.FM.CT.weaponReleaseDelay >> 8);
		if(bIsMaster)
			netToMirrors(_AS_WEAPON_RELEASE_DELAY, releaseDelayLow, releaseDelayHigh);
		else
			netToMaster(_AS_WEAPON_RELEASE_DELAY, releaseDelayLow, releaseDelayHigh, theActor);
	}

	private void doSetWeaponReleaseDelay(Actor theActor, long theWeaponReleaseDelay)
	{
		aircraft.FM.CT.doSetWeaponReleaseDelay(theWeaponReleaseDelay);
	}
	// TODO: -- Added Code for Net Replication --
}