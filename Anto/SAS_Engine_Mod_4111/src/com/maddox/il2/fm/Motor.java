/*Modified Motor class for the SAS Engine Mod*/
package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.*;
import com.maddox.rts.*;

import java.io.IOException;

public class Motor extends FMMath
{
	//TODO: Default variables, modified by |ZUTI|: changed all private variables to protected
	// --------------------------------------------------------
    protected static final boolean ___debug___ = false;
    public static final int _E_TYPE_INLINE = 0;
    public static final int _E_TYPE_RADIAL = 1;
    public static final int _E_TYPE_JET = 2;
    public static final int _E_TYPE_ROCKET = 3;
    public static final int _E_TYPE_ROCKETBOOST = 4;
    public static final int _E_TYPE_TOW = 5;
    public static final int _E_TYPE_PVRD = 6;
    public static final int _E_TYPE_HELO_INLINE = 7;
    public static final int _E_TYPE_UNIDENTIFIED = 8;
    public static final int _E_PROP_DIR_LEFT = 0;
    public static final int _E_PROP_DIR_RIGHT = 1;
    public static final int _E_STAGE_NULL = 0;
    public static final int _E_STAGE_WAKE_UP = 1;
    public static final int _E_STAGE_STARTER_ROLL = 2;
    public static final int _E_STAGE_CATCH_UP = 3;
    public static final int _E_STAGE_CATCH_ROLL = 4;
    public static final int _E_STAGE_CATCH_FIRE = 5;
    public static final int _E_STAGE_NOMINAL = 6;
    public static final int _E_STAGE_DEAD = 7;
    public static final int _E_STAGE_STUCK = 8;
    public static final int _E_PROP_FIXED = 0;
    public static final int _E_PROP_RETAIN_RPM_1 = 1;
    public static final int _E_PROP_RETAIN_RPM_2 = 2;
    public static final int _E_PROP_RETAIN_AOA_1 = 3;
    public static final int _E_PROP_RETAIN_AOA_2 = 4;
    public static final int _E_PROP_FRICTION = 5;
    public static final int _E_PROP_MANUALDRIVEN = 6;
    public static final int _E_PROP_WM_KOMANDGERAT = 7;
    public static final int _E_PROP_FW_KOMANDGERAT = 8;
    public static final int _E_PROP_CSP_EL = 9;
    public static final int _E_CARB_SUCTION = 0;
    public static final int _E_CARB_CARBURETOR = 1;
    public static final int _E_CARB_INJECTOR = 2;
    public static final int _E_CARB_FLOAT = 3;
    public static final int _E_CARB_SHILLING = 4;
    public static final int _E_COMPRESSOR_NONE = 0;
    public static final int _E_COMPRESSOR_MANUALSTEP = 1;
    public static final int _E_COMPRESSOR_WM_KOMANDGERAT = 2;
    public static final int _E_COMPRESSOR_TURBO = 3;
    public static final int _E_MIXER_GENERIC = 0;
    public static final int _E_MIXER_BRIT_FULLAUTO = 1;
    public static final int _E_MIXER_LIMITED_PRESSURE = 2;
    public static final int _E_AFTERBURNER_GENERIC = 0;
    public static final int _E_AFTERBURNER_MW50 = 1;
    public static final int _E_AFTERBURNER_GM1 = 2;
    public static final int _E_AFTERBURNER_FIRECHAMBER = 3;
    public static final int _E_AFTERBURNER_WATER = 4;
    public static final int _E_AFTERBURNER_NO2 = 5;
    public static final int _E_AFTERBURNER_FUEL_INJECTION = 6;
    public static final int _E_AFTERBURNER_FUEL_ILA5 = 7;
    public static final int _E_AFTERBURNER_FUEL_ILA5AUTO = 8;
    public static final int _E_AFTERBURNER_WATERMETHANOL = 9;
    public static final int _E_AFTERBURNER_P51 = 10;
    public static final int _E_AFTERBURNER_SPIT = 11;
    protected static int heatStringID = -1;
    public FmSounds isnd;
    protected FlightModel reference;
    protected static boolean bTFirst;
    public String soundName;
    public String startStopName;
    public String propName;
    public String emdName;
    public String emdSubName;
    protected int number;
    protected int type;
    protected int cylinders;
    protected float engineMass;
    protected float wMin;
    protected float wNom;
    protected float wMax;
    protected float wWEP;
    protected float wMaxAllowed;
    public int wNetPrev;
    public float engineMoment;
    protected float engineMomentMax;
    protected float engineBoostFactor;
    protected float engineAfterburnerBoostFactor;
    protected float engineDistAM;
    protected float engineDistBM;
    protected float engineDistCM;
    protected float producedDistabilisation;
    protected boolean bRan;
    protected Point3f enginePos;
    protected Vector3f engineVector;
    protected Vector3f engineForce;
    protected Vector3f engineTorque;
    protected float engineDamageAccum;
    protected float _1_wMaxAllowed;
    protected float _1_wMax;
    protected float RPMMin;
    protected float RPMNom;
    protected float RPMMax;
    protected float Vopt;
    protected float pressureExtBar;
    protected double momForFuel;
    public double addVflow;
    public double addVside;
    protected Point3f propPos;
    protected float propReductor;
    protected int propAngleDeviceType;
    protected float propAngleDeviceMinParam;
    protected float propAngleDeviceMaxParam;
    protected float propAngleDeviceAfterburnerParam;
    protected int propDirection;
    protected float propDiameter;
    protected float propMass;
    protected float propI;
    public Vector3d propIW;
    protected float propSEquivalent;
    protected float propr;
    protected float propPhiMin;
    protected float propPhiMax;
    protected float propPhi;
    protected float propPhiW;
    protected float propAoA;
    protected float propAoA0;
    protected float propAoACrit;
    protected float propAngleChangeSpeed;
    protected float propForce;
    public float propMoment;
    protected float propTarget;
    protected int mixerType;
    protected float mixerLowPressureBar;
    protected float horsePowers;
    protected float thrustMax;
    protected int cylindersOperable;
    protected float engineI;
    protected float engineAcceleration;
    protected boolean bMagnetos[] = {
        true, true
    };
    protected boolean bIsAutonomous;
    protected boolean bIsMaster;
    protected boolean bIsStuck;
    protected boolean bIsInoperable;
    protected boolean bIsAngleDeviceOperational;
    protected boolean isPropAngleDeviceHydroOperable;
    protected int engineCarburetorType;
    protected float FuelConsumptionP0;
    protected float FuelConsumptionP05;
    protected float FuelConsumptionP1;
    protected float FuelConsumptionPMAX;
    protected float fuelConsumption0M;
    protected float fuelConsumption1M;
    protected int compressorType;
    public int compressorMaxStep;
    protected float compressorPMax;
    protected float compressorManifoldPressure;
    public float compressorAltitudes[];
    protected float compressorPressure[];
    protected float compressorAltMultipliers[];
    protected float compressorBaseMultipliers[];
    protected float compressorRPMtoP0;
    protected float compressorRPMtoCurvature;
    protected float compressorRPMtoPMax;
    protected float compressorRPMtoWMaxATA;
    protected float compressorSpeedManifold;
    protected float compressorRPM[];
    protected float compressorATA[];
    protected int nOfCompPoints;
    protected boolean compressorStepFound;
    protected float compressorManifoldThreshold;
    protected float afterburnerCompressorFactor;
    protected float _1_P0;
    protected float compressor1stThrottle;
    protected float compressor2ndThrottle;
    protected float compressorPAt0;
    protected int afterburnerType;
    protected boolean afterburnerChangeW;
    protected int stage;
    protected int oldStage;
    protected long timer;
    protected long given;
    protected float rpm;
    public float w;
    protected float aw;
    protected float oldW;
    protected float readyness;
    protected float oldReadyness;
    protected float radiatorReadyness;
    protected float rearRush;
    public float tOilIn;
    public float tOilOut;
    public float tWaterOut;
    public float tCylinders;
    protected float tWaterCritMin;
    public float tWaterCritMax;
    protected float tOilCritMin;
    public float tOilCritMax;
    protected float tWaterMaxRPM;
    public float tOilOutMaxRPM;
    protected float tOilInMaxRPM;
    protected float tChangeSpeed;
    protected float timeOverheat;
    protected float timeUnderheat;
    protected float timeCounter;
    protected float oilMass;
    protected float waterMass;
    protected float Ptermo;
    protected float R_air;
    protected float R_oil;
    protected float R_water;
    protected float R_cyl_oil;
    protected float R_cyl_water;
    protected float C_eng;
    protected float C_oil;
    protected float C_water;
    protected boolean bHasThrottleControl;
    protected boolean bHasAfterburnerControl;
    protected boolean bHasPropControl;
    protected boolean bHasRadiatorControl;
    protected boolean bHasMixControl;
    protected boolean bHasMagnetoControl;
    protected boolean bHasExtinguisherControl;
    protected boolean bHasCompressorControl;
    protected boolean bHasFeatherControl;
    protected int extinguishers;
    protected float controlThrottle;
    public float controlRadiator;
    protected boolean controlAfterburner;
    protected float controlProp;
    protected boolean bControlPropAuto;
    protected float controlMix;
    protected int controlMagneto;
    protected int controlCompressor;
    protected int controlFeather;
    public double zatizeni;
    public float coolMult;
    protected int controlPropDirection;
    protected float neg_G_Counter;
    protected boolean bFullT;
    protected boolean bFloodCarb;
    protected boolean bWepRpmInLowGear;
    public boolean fastATA;
    protected Vector3f old_engineForce;
    protected Vector3f old_engineTorque;
    protected float updateStep;
    protected float updateLast;
    public float relP;
    public float maxMoment;
    public float maxW;
    protected float fricCoeffT;
    protected static Vector3f tmpV3f = new Vector3f();
    protected static Vector3d tmpV3d1 = new Vector3d();
    protected static Vector3d tmpV3d2 = new Vector3d();
    protected static Point3f safeloc = new Point3f();
    protected static Point3d safeLoc = new Point3d();
    protected static Vector3f safeVwld = new Vector3f();
    protected static Vector3f safeVflow = new Vector3f();
    protected static boolean tmpB;
    protected static float tmpF;
    protected int engineNoFuelHUDLogId;
 // --------------------------------------------------------
	
 	//TODO: New Parameters
 	// --------------------------------------------------------
    public static final int _S_TYPE_INERTIA = 0;
	public static final int _S_TYPE_MANUAL = 1;
	public static final int _S_TYPE_ELECTRIC = 2;
	public static final int _S_TYPE_CARTRIDGE = 3;
	public static final int _S_TYPE_PNEUMATIC = 4;
	public static final int _S_TYPE_BOSCH = 5;
	public static final int _E_TYPE_ROTARY = 9;
	public static final int _E_TYPE_TURBOPROP = 10;
 	public static int starter = 0;
 	//New boolean to cause instant engine shutdown if prop is struck
 	public boolean bPropHit = false;
    
    public Motor()
    {
        isnd = null;
        reference = null;
        soundName = null;
        startStopName = null;
        propName = null;
        emdName = null;
        emdSubName = null;
        number = 0;
        type = 0;
        cylinders = 12;
        engineMass = 900F;
        wMin = 20F;
        wNom = 180F;
        wMax = 200F;
        wWEP = 220F;
        wMaxAllowed = 250F;
        wNetPrev = 0;
        engineMoment = 0.0F;
        engineMomentMax = 0.0F;
        engineBoostFactor = 1.0F;
        engineAfterburnerBoostFactor = 1.0F;
        engineDistAM = 0.0F;
        engineDistBM = 0.0F;
        engineDistCM = 0.0F;
        bRan = false;
        enginePos = new Point3f();
        engineVector = new Vector3f();
        engineForce = new Vector3f();
        engineTorque = new Vector3f();
        engineDamageAccum = 0.0F;
        _1_wMaxAllowed = 1.0F / wMaxAllowed;
        _1_wMax = 1.0F / wMax;
        RPMMin = 200F;
        RPMNom = 2000F;
        RPMMax = 2200F;
        Vopt = 90F;
        momForFuel = 0.0D;
        addVflow = 0.0D;
        addVside = 0.0D;
        propPos = new Point3f();
        propReductor = 1.0F;
        propAngleDeviceType = 0;
        propAngleDeviceMinParam = 0.0F;
        propAngleDeviceMaxParam = 0.0F;
        propAngleDeviceAfterburnerParam = -999.9F;
        propDirection = 0;
        propDiameter = 3F;
        propMass = 30F;
        propI = 1.0F;
        propIW = new Vector3d();
        propSEquivalent = 1.0F;
        propr = 1.125F;
        propPhiMin = (float)Math.toRadians(10D);
        propPhiMax = (float)Math.toRadians(29D);
        propPhi = (float)Math.toRadians(11D);
        propAoA0 = (float)Math.toRadians(11D);
        propAoACrit = (float)Math.toRadians(16D);
        propAngleChangeSpeed = 0.1F;
        propForce = 0.0F;
        propMoment = 0.0F;
        propTarget = 0.0F;
        mixerType = 0;
        mixerLowPressureBar = 0.0F;
        horsePowers = 1200F;
        thrustMax = 10.7F;
        cylindersOperable = 12;
        engineI = 1.0F;
        engineAcceleration = 1.0F;
        bIsAutonomous = true;
        bIsMaster = true;
        bIsStuck = false;
        bIsInoperable = false;
        bIsAngleDeviceOperational = true;
        isPropAngleDeviceHydroOperable = true;
        engineCarburetorType = 0;
        FuelConsumptionP0 = 0.4F;
        FuelConsumptionP05 = 0.24F;
        FuelConsumptionP1 = 0.28F;
        FuelConsumptionPMAX = 0.3F;
        compressorType = 0;
        compressorMaxStep = 0;
        compressorPMax = 1.0F;
        compressorManifoldPressure = 1.0F;
        compressorAltitudes = null;
        compressorPressure = null;
        compressorAltMultipliers = null;
        compressorBaseMultipliers = null;
        compressorRPMtoP0 = 1500F;
        compressorRPMtoCurvature = -30F;
        compressorRPMtoPMax = 2600F;
        compressorRPMtoWMaxATA = 1.45F;
        compressorSpeedManifold = 0.2F;
        compressorRPM = new float[16];
        compressorATA = new float[16];
        nOfCompPoints = 0;
        compressorStepFound = false;
        compressorManifoldThreshold = 1.0F;
        afterburnerCompressorFactor = 1.0F;
        _1_P0 = 1.0F / Atmosphere.P0();
        compressor1stThrottle = 1.0F;
        compressor2ndThrottle = 1.0F;
        compressorPAt0 = 0.3F;
        afterburnerType = 0;
        afterburnerChangeW = false;
        stage = 0;
        oldStage = 0;
        timer = 0L;
        given = 0x3fffffffffffffffL;
        rpm = 0.0F;
        w = 0.0F;
        aw = 0.0F;
        oldW = 0.0F;
        readyness = 1.0F;
        oldReadyness = 1.0F;
        radiatorReadyness = 1.0F;
        tOilIn = 0.0F;
        tOilOut = 0.0F;
        tWaterOut = 0.0F;
        tCylinders = 0.0F;
        oilMass = 90F;
        waterMass = 90F;
        bHasThrottleControl = true;
        bHasAfterburnerControl = true;
        bHasPropControl = true;
        bHasRadiatorControl = true;
        bHasMixControl = true;
        bHasMagnetoControl = true;
        bHasExtinguisherControl = false;
        bHasCompressorControl = false;
        bHasFeatherControl = false;
        extinguishers = 0;
        controlThrottle = 0.0F;
        controlRadiator = 0.0F;
        controlAfterburner = false;
        controlProp = 1.0F;
        bControlPropAuto = true;
        controlMix = 1.0F;
        controlMagneto = 0;
        controlCompressor = 0;
        controlFeather = 0;
        neg_G_Counter = 0.0F;
        bFullT = false;
        bFloodCarb = false;
        fastATA = false;
        old_engineForce = new Vector3f();
        old_engineTorque = new Vector3f();
        updateStep = 0.12F;
        updateLast = 0.0F;
        fricCoeffT = 1.0F;
        engineNoFuelHUDLogId = -1;
		//TODO: New starter variable
		starter = 0;
    }

    public void load(FlightModel flightmodel, String s, String s1, int i)
    {
        reference = flightmodel;
        String s2 = "FlightModels/" + s + ".emd";
        emdName = s;
        emdSubName = s1;
        number = i;
        SectFile sectfile = FlightModelMain.sectFile(s2);
        resolveFromFile(sectfile, "Generic");
        resolveFromFile(sectfile, s1);
        calcAfterburnerCompressorFactor();
      //TODO: Initializes new engine types
      		if(type == 0 || type == 1 || type == 7 || type == 9)
      			initializeInline(((FlightModelMain) (flightmodel)).Vmax);
      		if(type == 2)
      			initializeJet(((FlightModelMain) (flightmodel)).Vmax);
      		if(type == 10)
      			//initializeTurboprop(((FlightModelMain) (flightmodel)).Vmax);
      			initializeInline(((FlightModelMain) (flightmodel)).Vmax);
    }

    private void resolveFromFile(SectFile sectfile, String s)
    {
        soundName = sectfile.get(s, "SoundName", soundName);
        propName = sectfile.get(s, "PropName", propName);
        startStopName = sectfile.get(s, "StartStopName", startStopName);
        Aircraft.debugprintln(reference.actor, "Resolving submodel " + s + " from file '" + sectfile.toString() + "'....");
      //TODO: Edits here to add new engine types
      		// --------------------------------------------------------
              String s1 = sectfile.get(s, "Type");
      		if(s1 != null)
      			if(s1.endsWith("Inline"))
      				type = 0;
      			else
      				if(s1.endsWith("Radial"))
      					type = 1;
      				else
      					if(s1.endsWith("Jet"))
      						type = 2;
      					else
      						if(s1.endsWith("RocketBoost"))
      							type = 4;
      						else
      							if(s1.endsWith("Rocket"))
      								type = 3;
      							else
      								if(s1.endsWith("Tow"))
      									type = 5;
      								else
      									if(s1.endsWith("PVRD"))
      										type = 6;
      									else
      										if(s1.endsWith("Unknown"))
      											type = 8;
      										else
      											if(s1.endsWith("Azure"))
      												type = 8;
      											else
      												if(s1.endsWith("HeloI"))
      													type = 7;
      												else
      													if(s1.endsWith("Rotary"))
      														type = 9;
      													else
      														if(s1.endsWith("Turboprop"))
      															type = 10;
      		if(type == 0 || type == 1 || type == 7 || type == 9)
      		{
      			int i = sectfile.get(s, "Cylinders", 0xfffe7961);
      			if(i != 0xfffe7961)
      			{
      				cylinders = i;
      				cylindersOperable = cylinders;
      			}
      		}
      		// --------------------------------------------------------
        s1 = sectfile.get(s, "Direction");
        if(s1 != null)
            if(s1.endsWith("Left"))
                propDirection = 0;
            else
            if(s1.endsWith("Right"))
                propDirection = 1;
        float f = sectfile.get(s, "RPMMin", -99999F);
        if(f != -99999F)
        {
            RPMMin = f;
            wMin = toRadianPerSecond(RPMMin);
        }
        f = sectfile.get(s, "RPMNom", -99999F);
        if(f != -99999F)
        {
            RPMNom = f;
            wNom = toRadianPerSecond(RPMNom);
        }
        f = sectfile.get(s, "RPMMax", -99999F);
        if(f != -99999F)
        {
            RPMMax = f;
            wMax = toRadianPerSecond(RPMMax);
            _1_wMax = 1.0F / wMax;
        }
        f = sectfile.get(s, "RPMMaxAllowed", -99999F);
        if(f != -99999F)
        {
            wMaxAllowed = toRadianPerSecond(f);
            _1_wMaxAllowed = 1.0F / wMaxAllowed;
        }
        f = sectfile.get(s, "Reductor", -99999F);
        if(f != -99999F)
            propReductor = f;
      //TODO: New engine types
  		// --------------------------------------------------------
  		if(type == 0 || type == 1 || type == 7 || type == 9)
  		{
  			f = sectfile.get(s, "HorsePowers", -99999F);
  			if(f != -99999F)
  				horsePowers = f;
  		// --------------------------------------------------------
  			//TODO: New variable starter
  			// --------------------------------------------------------
		s1 = sectfile.get(s, "Starter");
		if(s1 != null)
			if(s1.endsWith("Inertia"))
				starter = 0;
			else
				if(s1.endsWith("Manual"))
					starter = 1;
				else
					if(s1.endsWith("Electric"))
						starter = 2;
					else
						if(s1.endsWith("Cartridge"))
							starter = 3;
						else
							if(s1.endsWith("Pneumatic"))
								starter = 4;
							else
								if(s1.endsWith("Bosch"))
									starter = 5;
								else
									if(type == 9)
										starter = 1;
		// --------------------------------------------------------
            int j = sectfile.get(s, "Carburetor", 0xfffe7961);
            if(j != 0xfffe7961)
                engineCarburetorType = j;
            f = sectfile.get(s, "Mass", -99999F);
            if(f != -99999F)
                engineMass = f;
            else
                engineMass = horsePowers * 0.6F;
        } else
        {
            f = sectfile.get(s, "Thrust", -99999F);
            if(f != -99999F)
                thrustMax = f * 9.81F;
        }
        f = sectfile.get(s, "BoostFactor", -99999F);
        if(f != -99999F)
            engineBoostFactor = f;
        f = sectfile.get(s, "WEPBoostFactor", -99999F);
        if(f != -99999F)
            engineAfterburnerBoostFactor = f;
        if(type == 2)
        {
            FuelConsumptionP0 = 0.075F;
            FuelConsumptionP05 = 0.075F;
            FuelConsumptionP1 = 0.1F;
            FuelConsumptionPMAX = 0.11F;
        }
        if(type == 6)
        {
            FuelConsumptionP0 = 0.835F;
            FuelConsumptionP05 = 0.835F;
            FuelConsumptionP1 = 0.835F;
            FuelConsumptionPMAX = 0.835F;
        }
        f = sectfile.get(s, "FuelConsumptionP0", -99999F);
        if(f != -99999F)
            FuelConsumptionP0 = f;
        f = sectfile.get(s, "FuelConsumptionP05", -99999F);
        if(f != -99999F)
            FuelConsumptionP05 = f;
        f = sectfile.get(s, "FuelConsumptionP1", -99999F);
        if(f != -99999F)
            FuelConsumptionP1 = f;
        f = sectfile.get(s, "FuelConsumptionPMAX", -99999F);
        if(f != -99999F)
            FuelConsumptionPMAX = f;
        fuelConsumption0M = (FuelConsumptionP0 - FuelConsumptionP05) * 4F;
        fuelConsumption1M = (FuelConsumptionP1 - FuelConsumptionP05) * 4F;
        int k = sectfile.get(s, "Autonomous", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bIsAutonomous = false;
            else
            if(k == 1)
                bIsAutonomous = true;
        k = sectfile.get(s, "cThrottle", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasThrottleControl = false;
            else
            if(k == 1)
                bHasThrottleControl = true;
        k = sectfile.get(s, "cAfterburner", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasAfterburnerControl = false;
            else
            if(k == 1)
                bHasAfterburnerControl = true;
        k = sectfile.get(s, "cProp", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasPropControl = false;
            else
            if(k == 1)
                bHasPropControl = true;
        k = sectfile.get(s, "cMix", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasMixControl = false;
            else
            if(k == 1)
                bHasMixControl = true;
        k = sectfile.get(s, "cMagneto", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasMagnetoControl = false;
            else
            if(k == 1)
                bHasMagnetoControl = true;
        k = sectfile.get(s, "cCompressor", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasCompressorControl = false;
            else
            if(k == 1)
                bHasCompressorControl = true;
        k = sectfile.get(s, "cFeather", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasFeatherControl = false;
            else
            if(k == 1)
                bHasFeatherControl = true;
        k = sectfile.get(s, "cRadiator", 0xfffe7961);
        if(k != 0xfffe7961)
            if(k == 0)
                bHasRadiatorControl = false;
            else
            if(k == 1)
                bHasRadiatorControl = true;
        k = sectfile.get(s, "Extinguishers", 0xfffe7961);
        if(k != 0xfffe7961)
        {
            extinguishers = k;
            if(k != 0)
                bHasExtinguisherControl = true;
            else
                bHasExtinguisherControl = false;
        }
        f = sectfile.get(s, "PropDiameter", -99999F);
        if(f != -99999F)
            propDiameter = f;
        propr = 0.5F * propDiameter * 0.75F;
        f = sectfile.get(s, "PropMass", -99999F);
        if(f != -99999F)
            propMass = f;
        propI = propMass * propDiameter * propDiameter * 0.083F;
        bWepRpmInLowGear = false;
        k = sectfile.get(s, "PropAnglerType", 0xfffe7961);
        if(k != 0xfffe7961)
        {
            if(k > 255)
            {
                bWepRpmInLowGear = (k & 0x100) > 1;
                k -= 256;
            }
            propAngleDeviceType = k;
        }
        f = sectfile.get(s, "PropAnglerSpeed", -99999F);
        if(f != -99999F)
            propAngleChangeSpeed = f;
        f = sectfile.get(s, "PropAnglerMinParam", -99999F);
        if(f != -99999F)
        {
            propAngleDeviceMinParam = f;
            if(propAngleDeviceType == 6 || propAngleDeviceType == 5)
                propAngleDeviceMinParam = (float)Math.toRadians(propAngleDeviceMinParam);
            if(propAngleDeviceType == 1 || propAngleDeviceType == 2 || propAngleDeviceType == 7 || propAngleDeviceType == 8 || propAngleDeviceType == 9)
                propAngleDeviceMinParam = toRadianPerSecond(propAngleDeviceMinParam);
        }
        f = sectfile.get(s, "PropAnglerMaxParam", -99999F);
        if(f != -99999F)
        {
            propAngleDeviceMaxParam = f;
            if(propAngleDeviceType == 6 || propAngleDeviceType == 5)
                propAngleDeviceMaxParam = (float)Math.toRadians(propAngleDeviceMaxParam);
            if(propAngleDeviceType == 1 || propAngleDeviceType == 2 || propAngleDeviceType == 7 || propAngleDeviceType == 8 || propAngleDeviceType == 9)
                propAngleDeviceMaxParam = toRadianPerSecond(propAngleDeviceMaxParam);
            if(propAngleDeviceAfterburnerParam == -999.9F)
                propAngleDeviceAfterburnerParam = propAngleDeviceMaxParam;
        }
        f = sectfile.get(s, "PropAnglerAfterburnerParam", -99999F);
        if(f != -99999F)
        {
            propAngleDeviceAfterburnerParam = f;
            wWEP = toRadianPerSecond(propAngleDeviceAfterburnerParam);
            if(wWEP != wMax)
                afterburnerChangeW = true;
            if(propAngleDeviceType == 6 || propAngleDeviceType == 5)
                propAngleDeviceAfterburnerParam = (float)Math.toRadians(propAngleDeviceAfterburnerParam);
            if(propAngleDeviceType == 1 || propAngleDeviceType == 2 || propAngleDeviceType == 7 || propAngleDeviceType == 8 || propAngleDeviceType == 9)
                propAngleDeviceAfterburnerParam = toRadianPerSecond(propAngleDeviceAfterburnerParam);
        } else
        {
            wWEP = wMax;
        }
        f = sectfile.get(s, "PropPhiMin", -99999F);
        if(f != -99999F)
        {
            propPhiMin = (float)Math.toRadians(f);
            if(propPhi < propPhiMin)
                propPhi = propPhiMin;
            if(propTarget < propPhiMin)
                propTarget = propPhiMin;
        }
        f = sectfile.get(s, "PropPhiMax", -99999F);
        if(f != -99999F)
        {
            propPhiMax = (float)Math.toRadians(f);
            if(propPhi > propPhiMax)
                propPhi = propPhiMax;
            if(propTarget > propPhiMax)
                propTarget = propPhiMax;
        }
        f = sectfile.get(s, "PropAoA0", -99999F);
        if(f != -99999F)
            propAoA0 = (float)Math.toRadians(f);
        k = sectfile.get(s, "CompressorType", 0xfffe7961);
        if(k != 0xfffe7961)
            compressorType = k;
        f = sectfile.get(s, "CompressorPMax", -99999F);
        if(f != -99999F)
            compressorPMax = f;
        k = sectfile.get(s, "CompressorSteps", 0xfffe7961);
        if(k != 0xfffe7961)
        {
            compressorMaxStep = k - 1;
            if(compressorMaxStep < 0)
                compressorMaxStep = 0;
        }
        if(compressorAltitudes != null)
            if(compressorAltitudes.length == compressorMaxStep + 1);
        compressorAltitudes = new float[compressorMaxStep + 1];
        compressorPressure = new float[compressorMaxStep + 1];
        compressorAltMultipliers = new float[compressorMaxStep + 1];
        compressorBaseMultipliers = new float[compressorMaxStep + 1];
        if(compressorAltitudes.length > 0)
        {
            for(int l = 0; l < compressorAltitudes.length; l++)
            {
                f = sectfile.get(s, "CompressorAltitude" + l, -99999F);
                if(f != -99999F)
                {
                    compressorAltitudes[l] = f;
                    compressorPressure[l] = Atmosphere.pressure(compressorAltitudes[l]) * _1_P0;
                }
                f = sectfile.get(s, "CompressorMultiplier" + l, -99999F);
                if(f != -99999F)
                    compressorAltMultipliers[l] = f;
                f = sectfile.get(s, "CompressorBaseMultiplier" + l, -99999F);
                if(f != -99999F)
                    compressorBaseMultipliers[l] = f;
                else
                    compressorBaseMultipliers[l] = 0.8F;
            }

        }
        f = sectfile.get(s, "CompressorRPMP0", -99999F);
        if(f != -99999F)
        {
            compressorRPMtoP0 = f;
            insetrPoiInCompressorPoly(compressorRPMtoP0, 1.0F);
        }
        f = sectfile.get(s, "CompressorRPMCurvature", -99999F);
        if(f != -99999F)
            compressorRPMtoCurvature = f;
        f = sectfile.get(s, "CompressorMaxATARPM", -99999F);
        if(f != -99999F)
        {
            compressorRPMtoWMaxATA = f;
            insetrPoiInCompressorPoly(RPMMax, compressorRPMtoWMaxATA);
        }
        f = sectfile.get(s, "CompressorRPMPMax", -99999F);
        if(f != -99999F)
        {
            compressorRPMtoPMax = f;
            insetrPoiInCompressorPoly(compressorRPMtoPMax, compressorPMax);
        }
        f = sectfile.get(s, "CompressorSpeedManifold", -99999F);
        if(f != -99999F)
            compressorSpeedManifold = f;
        f = sectfile.get(s, "CompressorPAt0", -99999F);
        if(f != -99999F)
            compressorPAt0 = f;
        f = sectfile.get(s, "Voptimal", -99999F);
        if(f != -99999F)
            Vopt = f * 0.277778F;
        boolean flag = true;
        float f1 = 2000F;
        float f2 = 1.0F;
        int i1 = 0;
        do
        {
            if(!flag)
                break;
            f = sectfile.get(s, "CompressorRPM" + i1, -99999F);
            if(f != -99999F)
                f1 = f;
            else
                flag = false;
            f = sectfile.get(s, "CompressorATA" + i1, -99999F);
            if(f != -99999F)
                f2 = f;
            else
                flag = false;
            if(flag)
                insetrPoiInCompressorPoly(f1, f2);
            i1++;
            if(nOfCompPoints > 15 || i1 > 15)
                flag = false;
        } while(true);
        k = sectfile.get(s, "AfterburnerType", 0xfffe7961);
        if(k != 0xfffe7961)
            afterburnerType = k;
        k = sectfile.get(s, "MixerType", 0xfffe7961);
        if(k != 0xfffe7961)
            mixerType = k;
        f = sectfile.get(s, "MixerAltitude", -99999F);
        if(f != -99999F)
            mixerLowPressureBar = Atmosphere.pressure(f) / Atmosphere.P0();
        f = sectfile.get(s, "EngineI", -99999F);
        if(f != -99999F)
            engineI = f;
        f = sectfile.get(s, "EngineAcceleration", -99999F);
        if(f != -99999F)
            engineAcceleration = f;
        f = sectfile.get(s, "DisP0x", -99999F);
        if(f != -99999F)
        {
            float f3 = sectfile.get(s, "DisP0x", -99999F);
            f3 = toRadianPerSecond(f3);
            float f4 = sectfile.get(s, "DisP0y", -99999F);
            f4 *= 0.01F;
            float f5 = sectfile.get(s, "DisP1x", -99999F);
            f5 = toRadianPerSecond(f5);
            float f6 = sectfile.get(s, "DisP1y", -99999F);
            f6 *= 0.01F;
            float f7 = f3;
            float f8 = f4;
            float f9 = (f5 - f3) * (f5 - f3);
            float f10 = f6 - f4;
            engineDistAM = f10 / f9;
            engineDistBM = (-2F * f10 * f7) / f9;
            engineDistCM = f8 + (f10 * f7 * f7) / f9;
        }
        timeCounter = 0.0F;
        f = sectfile.get(s, "TESPEED", -99999F);
        if(f != -99999F)
            tChangeSpeed = f;
        f = sectfile.get(s, "TWATERMAXRPM", -99999F);
        if(f != -99999F)
            tWaterMaxRPM = f;
        f = sectfile.get(s, "TOILINMAXRPM", -99999F);
        if(f != -99999F)
            tOilInMaxRPM = f;
        f = sectfile.get(s, "TOILOUTMAXRPM", -99999F);
        if(f != -99999F)
            tOilOutMaxRPM = f;
        f = sectfile.get(s, "MAXRPMTIME", -99999F);
        if(f != -99999F)
            timeOverheat = f;
        f = sectfile.get(s, "MINRPMTIME", -99999F);
        if(f != -99999F)
            timeUnderheat = f;
        f = sectfile.get(s, "TWATERMAX", -99999F);
        if(f != -99999F)
            tWaterCritMax = f;
        f = sectfile.get(s, "TWATERMIN", -99999F);
        if(f != -99999F)
            tWaterCritMin = f;
        f = sectfile.get(s, "TOILMAX", -99999F);
        if(f != -99999F)
            tOilCritMax = f;
        f = sectfile.get(s, "TOILMIN", -99999F);
        if(f != -99999F)
            tOilCritMin = f;
        coolMult = 1.0F;
    }

    private void initializeInline(float f)
    {
        propSEquivalent = 0.26F * propr * propr;
        engineMomentMax = (horsePowers * 746F * 1.2F) / wMax;
    }

    private void initializeJet(float f)
    {
        propSEquivalent = ((float)(cylinders * cylinders) * (2.0F * thrustMax)) / (getFanCy(propAoA0) * Atmosphere.ro0() * wMax * wMax * propr * propr);
        computePropForces(wMax, 0.0F, 0.0F, propAoA0, 0.0F);
        engineMomentMax = propMoment;
    }

    public void initializeTowString(float f)
    {
        propForce = f;
    }

    public void setMaster(boolean flag)
    {
        bIsMaster = flag;
    }

    private void insetrPoiInCompressorPoly(float f, float f1)
    {
        int i = 0;
        do
        {
            if(i >= nOfCompPoints)
                break;
            if(compressorRPM[i] >= f)
            {
                if(compressorRPM[i] == f)
                    return;
                break;
            }
            i++;
        } while(true);
        for(int j = nOfCompPoints - 1; j >= i; j--)
        {
            compressorRPM[j + 1] = compressorRPM[j];
            compressorATA[j + 1] = compressorATA[j];
        }

        nOfCompPoints++;
        compressorRPM[i] = f;
        compressorATA[i] = f1;
    }

    private void calcAfterburnerCompressorFactor()
    {
        if(afterburnerType == 1 || afterburnerType == 7 || afterburnerType == 8 || afterburnerType == 10 || afterburnerType == 11 || afterburnerType == 6 || afterburnerType == 5 || afterburnerType == 9 || afterburnerType == 4)
        {
            float f = compressorRPM[nOfCompPoints - 1];
            float f1 = compressorATA[nOfCompPoints - 1];
            nOfCompPoints--;
            int i = 0;
            int j = 1;
            float f2 = 1.0F;
            float f3 = f;
            if(nOfCompPoints < 2)
            {
                afterburnerCompressorFactor = 1.0F;
                return;
            }
            if((double)f3 < 0.10000000000000001D)
                f2 = Atmosphere.pressure((float)reference.Loc.z) * _1_P0;
            else
            if(f3 >= compressorRPM[nOfCompPoints - 1])
            {
                f2 = compressorATA[nOfCompPoints - 1];
            } else
            {
                if(f3 < compressorRPM[0])
                {
                    i = 0;
                    j = 1;
                } else
                {
                    int k = 0;
                    do
                    {
                        if(k >= nOfCompPoints - 1)
                            break;
                        if(compressorRPM[k] <= f3 && f3 < compressorRPM[k + 1])
                        {
                            i = k;
                            j = k + 1;
                            break;
                        }
                        k++;
                    } while(true);
                }
                float f4 = compressorRPM[j] - compressorRPM[i];
                if(f4 < 0.001F)
                    f4 = 0.001F;
                f2 = compressorATA[i] + ((f3 - compressorRPM[i]) * (compressorATA[j] - compressorATA[i])) / f4;
            }
            afterburnerCompressorFactor = f1 / f2;
        } else
        {
            afterburnerCompressorFactor = 1.0F;
        }
    }

    public float getATA(float f)
    {
        int i = 0;
        int j = 1;
        float f1 = 1.0F;
        if(nOfCompPoints < 2)
            return 1.0F;
        if((double)f < 0.10000000000000001D)
            f1 = Atmosphere.pressure((float)reference.Loc.z) * _1_P0;
        else
        if(f >= compressorRPM[nOfCompPoints - 1])
        {
            f1 = compressorATA[nOfCompPoints - 1];
        } else
        {
            if(f < compressorRPM[0])
            {
                i = 0;
                j = 1;
            } else
            {
                int k = 0;
                do
                {
                    if(k >= nOfCompPoints - 1)
                        break;
                    if(compressorRPM[k] <= f && f < compressorRPM[k + 1])
                    {
                        i = k;
                        j = k + 1;
                        break;
                    }
                    k++;
                } while(true);
            }
            float f2 = compressorRPM[j] - compressorRPM[i];
            if(f2 < 0.001F)
                f2 = 0.001F;
            f1 = compressorATA[i] + ((f - compressorRPM[i]) * (compressorATA[j] - compressorATA[i])) / f2;
        }
        return f1;
    }

    public void update(float f)
    {
        if(!(reference instanceof RealFlightModel) && Time.tickCounter() > 200)
        {
            updateLast += f;
            if(updateLast >= updateStep)
            {
                f = updateLast;
            } else
            {
                engineForce.set(old_engineForce);
                engineTorque.set(old_engineTorque);
                return;
            }
        }
        producedDistabilisation = 0.0F;
        pressureExtBar = Atmosphere.pressure(reference.getAltitude()) + compressorSpeedManifold * 0.5F * Atmosphere.density(reference.getAltitude()) * reference.getSpeed() * reference.getSpeed();
        pressureExtBar *= 9.8716682999999996E-006D;
        if(controlThrottle > 1.0F && engineBoostFactor == 1.0F)
        {
            reference.CT.setPowerControl(1.0F);
            if(reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[] {
                    new Integer(100)
                });
        }
        computeForces(f);
        computeStage(f);
        if(stage > 0 && stage < 6)
            engineForce.set(0.0F, 0.0F, 0.0F);
        else
        if(stage == 8)
            rpm = w = 0.0F;
        if(reference.isPlayers())
        {
            if(bIsMaster && (reference instanceof RealFlightModel))
            {
                computeTemperature(f);
                if(World.cur().diffCur.Reliability)
                    computeReliability(f);
            }
            if(World.cur().diffCur.Limited_Fuel)
                computeFuel(f);
        } else
        {
            computeFuel(f);
            if(bIsMaster && reference.isTick(32, 0))
                computeTemperature(f * 32F);
        }
        old_engineForce.set(engineForce);
        old_engineTorque.set(engineTorque);
        updateLast = 0.0F;
        float f1 = 0.5F / (Math.abs(aw) + 1.0F) - 0.1F;
        if(f1 < 0.025F)
            f1 = 0.025F;
        if(f1 > 0.4F)
            f1 = 0.4F;
        if(f1 < updateStep)
            updateStep = 0.9F * updateStep + 0.1F * f1;
        else
            updateStep = 0.99F * updateStep + 0.01F * f1;
    }

    public void netupdate(float f, boolean flag)
    {
        computeStage(f);
        if((double)Math.abs(w) < 1.0000000000000001E-005D)
            propPhiW = 1.570796F;
        else
            propPhiW = (float)Math.atan(reference.Vflow.x / (double)(w * propReductor * propr));
        propAoA = propPhi - propPhiW;
        computePropForces(w * propReductor, (float)reference.Vflow.x, propPhi, propAoA, reference.getAltitude());
        float f1 = w;
        float f2 = propPhi;
        float f3 = compressorManifoldPressure;
        computeForces(f);
        if(flag)
            compressorManifoldPressure = f3;
        w = f1;
        propPhi = f2;
        rpm = toRPM(w);
    }

    public void setReadyness(Actor actor, float f)
    {
        if(f > 1.0F)
            f = 1.0F;
        if(f < 0.0F)
            f = 0.0F;
        if(!Actor.isAlive(actor))
            return;
        if(bIsMaster)
        {
            if(readyness > 0.0F && f == 0.0F)
            {
                readyness = 0.0F;
                setEngineDies(actor);
                return;
            }
            doSetReadyness(f);
        }
        if(Math.abs(oldReadyness - readyness) > 0.1F)
        {
            reference.AS.setEngineReadyness(actor, number, (int)(f * 100F));
            oldReadyness = readyness;
        }
    }

    private void setReadyness(float f)
    {
        setReadyness(reference.actor, f);
    }

    public void doSetReadyness(float f)
    {
        readyness = f;
    }

    public void setStage(Actor actor, int i)
    {
        if(!Actor.isAlive(actor))
            return;
        if(bIsMaster)
            doSetStage(i);
        reference.AS.setEngineStage(actor, number, i);
    }

    public void doSetStage(int i)
    {
        stage = i;
    }

    public void setEngineStarts(Actor actor)
    {
        if(!bIsMaster || !Actor.isAlive(actor))
            return;
        if(isHasControlMagnetos() && getMagnetoMultiplier() < 0.1F)
        {
            return;
        } else
        {
            reference.AS.setEngineStarts(number);
            return;
        }
    }

	public void doSetEngineStarts()
	{
		//TODO: Modified by |ZUTI|: disabled position check. 
		//if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
		if( reference.isStationedOnGround() )
		{
			((FlightModelMain) (reference)).CT.setMagnetoControl(3);
			setControlMagneto(3);
			stage = 1;
			bRan = false;
			timer = Time.current();
			return;
		}
		/* TODO: here I've tried to solve that start after windmilling issue...w is used to calculate rpm's,so try to play with that */
		if(stage == 0) 
		{
			if(type == 9 && bRan){
				if(w > 10)
				{
					stage = 5;
				} else    
					if(w > 150)
					{
						stage = 6;
					} else
					{
						((FlightModelMain) (reference)).CT.setMagnetoControl(3);
						setControlMagneto(3);	
						stage = 1;
						timer = Time.current();
					}
			}
			else	
				if((type == 0 || type == 1 || type == 7) && bRan){
					if(w > 20)
					{
						stage = 5;
					} else    
						if(w > 150)
						{
							stage = 6;
						}
				}
				else
					((FlightModelMain) (reference)).CT.setMagnetoControl(3);
			setControlMagneto(3);
			stage = 1;
			timer = Time.current();
		}
	}

    public void setEngineStops(Actor actor)
    {
        if(!Actor.isAlive(actor))
            return;
        if(stage < 1 || stage > 6)
        {
            return;
        } else
        {
            reference.AS.setEngineStops(number);
            return;
        }
    }

    public void doSetEngineStops()
    {
        if(stage != 0)
        {
            stage = 0;
            setControlMagneto(0);
            timer = Time.current();
        }
    }

    public void setEngineDies(Actor actor)
    {
        if(stage > 6)
        {
            return;
        } else
        {
            reference.AS.setEngineDies(reference.actor, number);
            return;
        }
    }

    public void doSetEngineDies()
    {
        if(stage < 7)
        {
            bIsInoperable = true;
            reference.setCapableOfTaxiing(false);
            reference.setCapableOfACM(false);
            doSetReadyness(0.0F);
            float f = 0.0F;
            int i = reference.EI.getNum();
            if(i != 0)
            {
                for(int j = 0; j < i; j++)
                    f += reference.EI.engines[j].getReadyness() / (float)i;

                if(f < 0.7F)
                    reference.setReadyToReturn(true);
                if(f < 0.3F)
                    reference.setReadyToDie(true);
            }
            stage = 7;
            if(reference.isPlayers())
                HUD.log("FailedEngine");
            timer = Time.current();
        }
    }

    public void setEngineRunning(Actor actor)
    {
        if(!bIsMaster || !Actor.isAlive(actor))
        {
            return;
        } else
        {
            reference.AS.setEngineRunning(number);
            return;
        }
    }

    public void doSetEngineRunning()
    {
        if(stage >= 6)
            return;
        stage = 6;
        reference.CT.setMagnetoControl(3);
        setControlMagneto(3);
        if(reference.isPlayers())
            HUD.log("EngineI1");
        w = wMax * 0.75F;
        tWaterOut = 0.5F * (tWaterCritMin + tWaterMaxRPM);
        tOilOut = 0.5F * (tOilCritMin + tOilOutMaxRPM);
        tOilIn = 0.5F * (tOilCritMin + tOilInMaxRPM);
        propPhi = 0.5F * (propPhiMin + propPhiMax);
        propTarget = propPhi;
        if(isnd != null)
            isnd.onEngineState(stage);
    }

    public void setKillCompressor(Actor actor)
    {
        reference.AS.setEngineSpecificDamage(actor, number, 0);
    }

    public void doSetKillCompressor()
    {
        switch(compressorType)
        {
        default:
            break;

        case 2: // '\002'
            compressorAltitudes[0] = 50F;
            compressorAltMultipliers[0] = 1.0F;
            break;

        case 1: // '\001'
            for(int i = 0; i < compressorMaxStep; i++)
            {
                compressorAltitudes[i] = 50F;
                compressorAltMultipliers[i] = 1.0F;
            }

            break;
        }
    }

    public void setKillPropAngleDevice(Actor actor)
    {
        reference.AS.setEngineSpecificDamage(actor, number, 3);
    }

    public void doSetKillPropAngleDevice()
    {
        bIsAngleDeviceOperational = false;
    }

    public void setKillPropAngleDeviceSpeeds(Actor actor)
    {
        reference.AS.setEngineSpecificDamage(actor, number, 4);
    }

    public void doSetKillPropAngleDeviceSpeeds()
    {
        isPropAngleDeviceHydroOperable = false;
    }

    public void setCyliderKnockOut(Actor actor, int i)
    {
        reference.AS.setEngineCylinderKnockOut(actor, number, i);
    }

    public void doSetCyliderKnockOut(int i)
    {
        cylindersOperable -= i;
        if(cylindersOperable < 0)
            cylindersOperable = 0;
        if(bIsMaster)
            if(getCylindersRatio() < 0.12F)
                setEngineDies(reference.actor);
            else
            if(getCylindersRatio() < getReadyness())
                setReadyness(reference.actor, getCylindersRatio());
    }

    public void setMagnetoKnockOut(Actor actor, int i)
    {
        reference.AS.setEngineMagnetoKnockOut(reference.actor, number, i);
    }

    public void doSetMagnetoKnockOut(int i)
    {
        bMagnetos[i] = false;
        if(i == controlMagneto)
            setEngineStops(reference.actor);
    }

    public void setEngineStuck(Actor actor)
    {
        reference.AS.setEngineStuck(actor, number);
    }

    public void doSetEngineStuck()
    {
        bIsInoperable = true;
        reference.setCapableOfTaxiing(false);
        reference.setCapableOfACM(false);
        if(stage != 8)
        {
            setReadyness(0.0F);
            if(reference.isPlayers() && stage != 7)
                HUD.log("FailedEngine");
            stage = 8;
            timer = Time.current();
        }
    }

    public void setw(float f)
    {
        w = f;
        rpm = toRPM(w);
    }

    public void setPropPhi(float f)
    {
        propPhi = f;
    }

    public void setEngineMomentMax(float f)
    {
        engineMomentMax = f;
    }

    public void setPos(Point3d point3d)
    {
        enginePos.set(point3d);
    }

    public void setPropPos(Point3d point3d)
    {
        propPos.set(point3d);
    }

    public void setVector(Vector3f vector3f)
    {
        engineVector.set(vector3f);
        engineVector.normalize();
    }

    public void setControlThrottle(float f)
    {
        if(bHasThrottleControl)
        {
            if(afterburnerType == 4)
            {
                if(f > 1.0F && controlThrottle <= 1.0F && reference.M.requestNitro(0.0001F))
                {
                    reference.CT.setAfterburnerControl(true);
                    setControlAfterburner(true);
                    if(reference.isPlayers())
                    {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP4");
                    }
                }
                if(f < 1.0F && controlThrottle >= 1.0F)
                {
                    reference.CT.setAfterburnerControl(false);
                    setControlAfterburner(false);
                    if(reference.isPlayers())
                    {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            } else
            if(afterburnerType == 8)
            {
                if(f > 1.0F && controlThrottle <= 1.0F)
                {
                    reference.CT.setAfterburnerControl(true);
                    setControlAfterburner(true);
                    if(reference.isPlayers())
                    {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP7");
                    }
                }
                if(f < 1.0F && controlThrottle >= 1.0F)
                {
                    reference.CT.setAfterburnerControl(false);
                    setControlAfterburner(false);
                    if(reference.isPlayers())
                    {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            } else
            if(afterburnerType == 10)
            {
                if(f > 1.0F && controlThrottle <= 1.0F)
                {
                    reference.CT.setAfterburnerControl(true);
                    setControlAfterburner(true);
                    if(reference.isPlayers())
                    {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP0");
                    }
                }
                if(f < 1.0F && controlThrottle >= 1.0F)
                {
                    reference.CT.setAfterburnerControl(false);
                    setControlAfterburner(false);
                    if(reference.isPlayers())
                    {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            }
            controlThrottle = f;
        }
    }

    public void setControlAfterburner(boolean flag)
    {
        if(bHasAfterburnerControl)
        {
            if(afterburnerType == 1 && !controlAfterburner && flag && controlThrottle > 1.0F && World.Rnd().nextFloat() < 0.5F && reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode() && World.cur().diffCur.Vulnerability)
                setCyliderKnockOut(reference.actor, World.Rnd().nextInt(0, 3));
            controlAfterburner = flag;
        }
        if(afterburnerType == 4 || afterburnerType == 8 || afterburnerType == 10)
            controlAfterburner = flag;
    }

    public void doSetKillControlThrottle()
    {
        bHasThrottleControl = false;
    }

    public void setControlPropDelta(int i)
    {
        controlPropDirection = i;
    }

    public int getControlPropDelta()
    {
        return controlPropDirection;
    }

    public void doSetKillControlAfterburner()
    {
        bHasAfterburnerControl = false;
    }

    public void setControlProp(float f)
    {
        if(bHasPropControl)
            controlProp = f;
    }

    public void setControlPropAuto(boolean flag)
    {
        if(bHasPropControl)
            bControlPropAuto = flag && isAllowsAutoProp();
    }

    public void doSetKillControlProp()
    {
        bHasPropControl = false;
    }

    public void setControlMix(float f)
    {
        if(bHasMixControl)
            switch(mixerType)
            {
            case 0: // '\0'
                controlMix = f;
                break;

            case 1: // '\001'
                controlMix = f;
                if(controlMix < 1.0F)
                    controlMix = 1.0F;
                break;

            default:
                controlMix = f;
                break;
            }
    }

    public void doSetKillControlMix()
    {
        bHasMixControl = false;
    }

    public void setControlMagneto(int i)
    {
        if(bHasMagnetoControl)
        {
            controlMagneto = i;
            if(i == 0)
                setEngineStops(reference.actor);
        }
    }

    public void setControlCompressor(int i)
    {
        if(bHasCompressorControl)
            controlCompressor = i;
    }

    public void setControlFeather(int i)
    {
        if(bHasFeatherControl)
        {
            controlFeather = i;
            if(reference.isPlayers())
                HUD.log("EngineFeather" + controlFeather);
        }
    }

    public void setControlRadiator(float f)
    {
        if(bHasRadiatorControl)
            controlRadiator = f;
    }

    public void setExtinguisherFire()
    {
        if(!bIsMaster)
            return;
        if(bHasExtinguisherControl)
        {
            reference.AS.setEngineSpecificDamage(reference.actor, number, 5);
            if(reference.AS.astateEngineStates[number] > 2)
                reference.AS.setEngineState(reference.actor, number, World.Rnd().nextInt(1, 2));
            else
            if(reference.AS.astateEngineStates[number] > 0)
                reference.AS.setEngineState(reference.actor, number, 0);
        }
    }

    public void doSetExtinguisherFire()
    {
        if(!bHasExtinguisherControl)
            return;
        if(reference.AS.bIsAboutToBailout)
            return;
        extinguishers--;
        if(extinguishers == 0)
            bHasExtinguisherControl = false;
        reference.AS.doSetEngineExtinguisherVisuals(number);
        if(bIsMaster)
        {
            if(reference.AS.astateEngineStates[number] > 1 && World.Rnd().nextFloat() < 0.56F)
                reference.AS.repairEngine(number);
            if(reference.AS.astateEngineStates[number] > 3 && World.Rnd().nextFloat() < 0.21F)
            {
                reference.AS.repairEngine(number);
                reference.AS.repairEngine(number);
            }
            tWaterOut -= 4F;
            tOilIn -= 4F;
            tOilOut -= 4F;
        }
        if(reference.isPlayers())
            HUD.log("ExtinguishersFired");
    }

    private void computeStage(float f)
    {
        if(stage == 6)
            return;
        bTFirst = false;
        float f1 = 20F;
        long l = Time.current() - timer;
        if(stage > 0 && stage < 6 && l > given)
		{
			stage++;
			/*TODO:  This part controls hiccups during startup. 
			 * -Rotary and radial engines more prone to hiccups due to greater mass
			 * -Manual cranked engines (starter = 1) less likely to start first go
			 * -Electric type (starter = 2) is less liking to fail as can be primed during wind-up. 
			 * -Meanwhile other types such as cartridge (starter = 3) and inertia (starter = 0) won't start if engine isn't properly primed
			 * - To change how prone an engine type is to hiccups, increase the nextFloat values
			 */
			//Inline engines
			if((type == 0 || type == 7) && starter != 1){
				//Electric type start is less liking to fail as can be primed during wind-up. Other types can fail to start
				if(starter == 2 && stage > 3 && World.Rnd().nextFloat(0.0F,10.0F) < 2F){
						stage = 3;
				}
				if((starter == 0 || starter == 3 || starter == 5) && stage > 3){
					if(World.Rnd().nextFloat(0.0F,10.0F) < 0.25F && reference.isStationedOnGround())
						stage = 0;
					else
					if(World.Rnd().nextFloat(0.0F,10.0F) < 3F)
						stage = 3;
				}
			}
			//Radial engines
			if(type == 1 && starter != 1)
			{
				if(starter == 2 ){
				if(stage == 3 && World.Rnd().nextFloat(0.0F,10.0F) < 2F)
					stage = 2;
				else
					if(stage > 3 && World.Rnd().nextFloat(0.0F,10.0F) < 3F)
						stage = 3;
				}
				if(starter == 0 || starter == 3 || starter == 5){
					if(stage > 3 && World.Rnd().nextFloat(0.0F,10.0F) < 0.25F && reference.isStationedOnGround())
						stage = 0;
					else
					if(stage == 3 && World.Rnd().nextFloat(0.0F,10.0F) < 1F)
						stage = 2;
					else
					if(stage > 3 && World.Rnd().nextFloat(0.0F,10.0F) < 3F)
						stage = 3;
				}
			}
			//Rotary and hand-cranked engines
			if((starter == 1) || (type == 9))
			{
				if(stage > 3 && World.Rnd().nextFloat(0.0F,10.0F) < 2F && reference.isStationedOnGround())
					stage = 0;
				else
				if(stage == 3 && World.Rnd().nextFloat(0.0F,10.0F) < 2F)
					stage = 2;
				else
				if(stage > 3 && World.Rnd().nextFloat(0.0F,10.0F) < 3F)
					stage = 3;
			}

            timer = Time.current();
            l = 0L;
        }
        if(oldStage != stage)
        {
            bTFirst = true;
            oldStage = stage;
        }
        if(stage > 0 && stage < 6)
            setControlThrottle(0.2F);
label0:
        switch(stage)
        {
        case 0: // '\0'
            if(bTFirst)
            {
                given = 0x3fffffffffffffffL;
                timer = Time.current();
            }
            if(isnd != null)
                isnd.onEngineState(stage);
            break;

        case 1: // '\001'
            if(bTFirst)
            {
                if(bIsStuck)
                {
                    stage = 8;
                    return;
                }
                if(type == 3 || type == 4 || type == 6)
                {
                    stage = 5;
                    if(reference.isPlayers())
                        HUD.log("Starting_Engine");
                    return;
                }
                if(type == 0 || type == 1 || type == 7)
                {
                    if(w > wMin)
                    {
                        stage = 3;
                        if(reference.isPlayers())
                            HUD.log("Starting_Engine");
                        return;
                    }
                    if(!bIsAutonomous)
                    {
                    	//TODO: Modified by |ZUTI|: disabled position check. 
						//if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
						if( reference.isStationedOnGround() )
                        {
                            setControlMagneto(3);
                            if(reference.isPlayers())
                                HUD.log("Starting_Engine");
                        } else
                        {
                            doSetEngineStops();
                            if(reference.isPlayers())
                                HUD.log("EngineI0");
                            return;
                        }
                    } else
                    if(reference.isPlayers())
                        HUD.log("Starting_Engine");
                } else
                if(!bIsAutonomous)
                {
                	//TODO: Modified by |ZUTI|: disabled position check. 
					//if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
					if( reference.isStationedOnGround() )
                    {
                        setControlMagneto(3);
                        if(reference.isPlayers())
                            HUD.log("Starting_Engine");
                    } else
                    {
                        if(reference.getSpeedKMH() < 350F)
                        {
                            doSetEngineStops();
                            if(reference.isPlayers())
                                HUD.log("EngineI0");
                            return;
                        }
                        if(reference.isPlayers())
                            HUD.log("Starting_Engine");
                    }
                } else
                if(reference.isPlayers())
                    HUD.log("Starting_Engine");
                /*TODO:   editing "given" in each stage will give you different starting times....test these planes and see the difference  */ 
				if(type == 9)
				{
					given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
				}
				else
				if(type == 0 || type == 7){
					if(starter == 0)
					{
						given = (long)(1500F * World.Rnd().nextFloat(1.0F, 2.0F));
					} else
						if(starter == 1)
						{
							given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
						}
						else
						if(starter == 2)
						{
							given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
						} else	
							if(starter == 3)
							{
								given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
							}
							else
								if(starter == 5)
								{
									given = (long)(1500F * World.Rnd().nextFloat(1.0F, 2.0F));
								}
				}
				else
					if(type == 1){
						if(starter == 0)
						{
							given = (long)(2000F * World.Rnd().nextFloat(1.0F, 2.0F));
						} else
							if(starter == 1)
							{
								given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
							}
							else
							if(starter == 2)
							{
								given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else	
								if(starter == 3)
								{
									given = (long)(50F * World.Rnd().nextFloat(1.0F, 2.0F));
								}
								else
									if(starter == 5)
									{
										given = (long)(5000F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
					}
					else
						given = (long)(1000F * World.Rnd().nextFloat(1.0F, 2.0F));
			}
            if(isnd != null)
                isnd.onEngineState(stage);
            reference.CT.setMagnetoControl(3);
            setControlMagneto(3);
            w = 0.1047F * ((20F * (float)l) / (float)given);
            //TODO:
            if(starter == 5)
				w = 0;
			else
				w = (0.1047F * ((20F * (float)l) / (float)given));
            setControlThrottle(0.0F);
            break;

        case 2: // '\002'
            if(bTFirst)
            {
            	if(type == 9)
				{
					given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
				} else
					if(type == 0 || type == 7){
						if(starter == 0)
						{
							given = (long)(5000F * World.Rnd().nextFloat(1.0F, 2.0F));
						} else
							if(starter == 1)
							{
								given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
							if(starter == 2)
							{
								given = (long)(1500F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 3)
								{
									given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
								}
								else
									if(starter == 5)
									{
										given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
					}
					else
						if(type == 1){
							if(starter == 0)
							{
								given = (long)(8000F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 1)
								{
									given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else
								if(starter == 2)
								{
									given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else	
									if(starter == 3)
									{
										given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
									else
										if(starter == 5)
										{
											given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
										}
						}
						else
							given = (long)(8000F * World.Rnd().nextFloat(1.0F, 2.0F));
                if(bRan)
                {
                    given = (long)(100F + ((tOilOutMaxRPM - tOilOut) / (tOilOutMaxRPM - f1)) * 7900F * World.Rnd().nextFloat(2.0F, 4.2F));
                    if(given > 9000L)
                        given = World.Rnd().nextLong(7800L, 9600L);
                    if(bIsMaster && World.Rnd().nextFloat() < 0.5F)
                    {
                        stage = 0;
                        reference.AS.setEngineStops(number);
                    }
                }
            }
            w = 0.1047F * (20F + (15F * (float)l) / (float)given);
            setControlThrottle(0.0F);
            if(isnd != null)
                isnd.onEngineState(stage);
            break;

        case 3: // '\003'
            if(bTFirst)
            {
                if(isnd != null)
                    isnd.onEngineState(stage);
                if(bIsInoperable)
                {
                    stage = 0;
                    doSetEngineDies();
                    return;
                }
                if(type == 9)
				{
					given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
				} else
					if(type == 0 || type == 7){
						if(starter == 0)
						{
							given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
						} else
							if(starter == 1)
							{
								given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
							if(starter == 2)
							{
								given = (long)(200F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 3)
								{
									given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
								}
								else
									if(starter == 5)
									{
										given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
					}
					else
						if(type == 1){
							if(starter == 0)
							{
								given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 1)
								{
									given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else
								if(starter == 2)
								{
									given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else	
									if(starter == 3)
									{
										given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
									else
										if(starter == 5)
										{
											given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
										}
						}
						else
							given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
                if(bIsMaster && World.Rnd().nextFloat() < 0.12F && (tOilOutMaxRPM - tOilOut) / (tOilOutMaxRPM - f1) < 0.75F)
                    reference.AS.setEngineStops(number);
            }
            w = 0.1047F * (60F + (60F * (float)l) / (float)given);
            setControlThrottle(0.0F);
            if(reference == null || type == 2 || type == 3 || type == 4 || type == 6 || type == 5)
                break;
            int i = 1;
            do
            {
                if(i >= 32)
                    break label0;
                try
                {
                    com.maddox.il2.engine.Hook hook = reference.actor.findHook("_Engine" + (number + 1) + "EF_" + (i >= 10 ? "" + i : "0" + i));
                    if(hook != null)
                        Eff3DActor.New(reference.actor, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff", -1F);
                }
                catch(Exception exception) { }
                i++;
            } while(true);

        case 4: // '\004'
            if(bTFirst)
            	if(type == 9)
				{
					given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
				} else
					if(type == 0 || type == 7){
						if(starter == 0)
						{
							given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
						} else
							if(starter == 1)
							{
								given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
							if(starter == 2)
							{
								given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 3)
								{
									given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
								}
								else
									if(starter == 5)
									{
										given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
					}
					else
						if(type == 1){
							if(starter == 0)
							{
								given = (long)(1200F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 1)
								{
									given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else
								if(starter == 2)
								{
									given = (long)(1200F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else	
									if(starter == 3)
									{
										given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
									else
										if(starter == 5)
										{
											given = (long)(1200F * World.Rnd().nextFloat(1.0F, 2.0F));
										}
						}
						else
							given = (long)(1000F * World.Rnd().nextFloat(1.0F, 2.0F));
            w = 12.564F;
            setControlThrottle(0.0F);
            if(isnd != null)
                isnd.onEngineState(stage);
            break;

        case 5: // '\005'
            if(bTFirst)
            {
            	if(type == 9)
				{
					given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
				} else
					if(type == 0 || type == 7){
						if(starter == 0)
						{
							given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
						} else	
							if(starter == 1)
							{
								given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
							if(starter == 2)
							{
								given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 3)
								{
									given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
								}
								else
									if(starter == 5)
									{
										given = (long)(100F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
					}
					else
						if(type == 1){
							if(starter == 0)
							{
								given = (long)(800F * World.Rnd().nextFloat(1.0F, 2.0F));
							} else
								if(starter == 1)
								{
									given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else
								if(starter == 2)
								{
									given = (long)(800F * World.Rnd().nextFloat(1.0F, 2.0F));
								} else	
									if(starter == 3)
									{
										given = (long)(25F * World.Rnd().nextFloat(1.0F, 2.0F));
									}
									else
										if(starter == 5)
										{
											given = (long)(800F * World.Rnd().nextFloat(1.0F, 2.0F));
										}
						}
						else
							given = (long)(500F * World.Rnd().nextFloat(1.0F, 2.0F));
            	if(bRan && (type == 0 || type == 1 || type == 7 || type == 9))
                {
                    if((tOilOutMaxRPM - tOilOut) / (tOilOutMaxRPM - f1) > 0.75F)
                        if(type == 0 || type == 7)
                        {
                            if(bIsMaster && getReadyness() > 0.75F && World.Rnd().nextFloat() < 0.25F)
                                setReadyness(getReadyness() - 0.05F);
                        } else
                        if(type == 1 && bIsMaster && World.Rnd().nextFloat() < 0.1F)
                            reference.AS.setEngineDies(reference.actor, number);
                    if(bIsMaster && World.Rnd().nextFloat() < 0.1F)
                        reference.AS.setEngineStops(number);
                }
                bRan = true;
            }
            w = 0.1047F * (120F + (120F * (float)l) / (float)given);
            setControlThrottle(0.2F);
            if(isnd != null)
                isnd.onEngineState(stage);
            break;

        case 6: // '\006'
            if(bTFirst)
            {
                given = -1L;
                reference.AS.setEngineRunning(number);
            }
            if(isnd != null)
                isnd.onEngineState(stage);
            break;

        case 7: // '\007'
        case 8: // '\b'
            if(bTFirst)
                given = -1L;
            setReadyness(0.0F);
            setControlMagneto(0);
            if(isnd != null)
                isnd.onEngineState(stage);
            break;

        default:
            return;
        }
    }

    private void computeFuel(float f)
    {
        tmpF = 0.0F;
        if(stage == 6)
        {
            float f1;
            double d;
            switch(type)
            {
            case 0: // '\0'
            case 1: // '\001'
            case 7: // '\007'
                d = momForFuel * (double)w * 0.0010499999999999999D;
                f1 = (float)d / horsePowers;
                if(d < (double)horsePowers * 0.050000000000000003D)
                    d = (double)horsePowers * 0.050000000000000003D;
                break;

            default:
                d = thrustMax * (f1 = getPowerOutput());
                if(d < (double)thrustMax * 0.050000000000000003D)
                    d = (double)thrustMax * 0.050000000000000003D;
                break;
            }
            if(f1 < 0.0F)
                f1 = 0.0F;
            double d1;
            if(f1 <= 0.5F)
            {
                d1 = f1 - 0.5F;
                d1 = d1 * d1 * (double)fuelConsumption0M + (double)FuelConsumptionP05;
            } else
            if((double)f1 <= 1.0D)
            {
                d1 = f1 - 0.5F;
                d1 = d1 * d1 * (double)fuelConsumption1M + (double)FuelConsumptionP05;
            } else
            {
                float f3 = f1 - 1.0F;
                if(f3 > 0.1F)
                    f3 = 0.1F;
                f3 *= 10F;
                d1 = FuelConsumptionP1 + (FuelConsumptionPMAX - FuelConsumptionP1) * f3;
            }
            d1 /= 3600D;
            switch(type)
            {
            case 5: // '\005'
            default:
                break;

            case 0: // '\0'
            case 1: // '\001'
            case 7: // '\007'
                d1 *= 0.8F + (0.2F * w) / wWEP;
                float f4 = (float)(d1 * d);
                tmpF = f4 * f;
                double d2 = f4 * 4.4E+007F;
                double d3 = f4 * 15.7F;
                double d4 = 1010D * d3 * 700D;
                d *= 746D;
                Ptermo = (float)(d2 - d - d4);
                break;

            case 2: // '\002'
                tmpF = (float)(d1 * d * (double)f);
                break;

            case 3: // '\003'
                if((reference.actor instanceof BI_1) || (reference.actor instanceof BI_6))
                {
                    tmpF = 1.8F * getPowerOutput() * f;
                    break;
                }
                if(reference.actor instanceof MXY_7)
                    tmpF = 0.5F * getPowerOutput() * f;
                else
                    tmpF = 2.5777F * getPowerOutput() * f;
                break;

            case 4: // '\004'
                tmpF = 1.432056F * getPowerOutput() * f;
                tmpB = reference.M.requestNitro(tmpF);
                tmpF = 0.0F;
                if(tmpB || !bIsMaster)
                    break;
                setEngineStops(reference.actor);
                if(reference.isPlayers() && engineNoFuelHUDLogId == -1)
                {
                    engineNoFuelHUDLogId = HUD.makeIdLog();
                    HUD.log(engineNoFuelHUDLogId, "EngineNoFuel");
                }
                return;

            case 6: // '\006'
                tmpF = (float)(d1 * d * (double)f);
                tmpB = reference.M.requestNitro(tmpF);
                tmpF = 0.0F;
                if(tmpB || !bIsMaster)
                    break;
                setEngineStops(reference.actor);
                if(reference.isPlayers() && engineNoFuelHUDLogId == -1)
                {
                    engineNoFuelHUDLogId = HUD.makeIdLog();
                    HUD.log(engineNoFuelHUDLogId, "EngineNoFuel");
                }
                return;
            }
        }
        tmpB = reference.M.requestFuel(tmpF);
        if(!tmpB && bIsMaster)
        {
            setEngineStops(reference.actor);
            reference.setCapableOfACM(false);
            reference.setCapableOfTaxiing(false);
            if(reference.isPlayers() && engineNoFuelHUDLogId == -1)
            {
                engineNoFuelHUDLogId = HUD.makeIdLog();
                HUD.log(engineNoFuelHUDLogId, "EngineNoFuel");
            }
        }
        if(controlAfterburner)
            switch(afterburnerType)
            {
            case 3: // '\003'
            case 6: // '\006'
            case 7: // '\007'
            case 8: // '\b'
            default:
                break;

            case 1: // '\001'
                if(controlThrottle > 1.0F && !reference.M.requestNitro(0.044872F * f) && reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode() && World.cur().diffCur.Vulnerability)
                    setReadyness(reference.actor, getReadyness() - 0.01F * f);
                break;

            case 2: // '\002'
                if(reference.M.requestNitro(0.044872F * f));
                break;

            case 5: // '\005'
                if(reference.M.requestNitro(0.044872F * f));
                break;

            case 9: // '\t'
                if(reference.M.requestNitro(0.044872F * f));
                break;

            case 4: // '\004'
                if(reference.M.requestNitro(0.044872F * f))
                    break;
                reference.CT.setAfterburnerControl(false);
                if(reference.isPlayers())
                {
                    Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                    HUD.logRightBottom(null);
                }
                break;
            }
    }

    private void computeReliability(float f)
    {
        if(stage != 6)
            return;
        float f1 = controlThrottle;
        if(engineBoostFactor > 1.0F)
            f1 *= 0.9090909F;
        switch(type)
        {
        default:
            zatizeni = f1;
            zatizeni = zatizeni * zatizeni;
            zatizeni = zatizeni * zatizeni;
            zatizeni *= (double)f * 6.1984262178699901E-005D;
            if(zatizeni > World.Rnd().nextDouble(0.0D, 1.0D))
            {
                int i = World.Rnd().nextInt(0, 9);
                if(i < 2)
                {
                    reference.AS.hitEngine(reference.actor, number, 3);
                    Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - smoke");
                } else
                {
                    setCyliderKnockOut(reference.actor, World.Rnd().nextInt(0, 3));
                    Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - power loss");
                }
            }
            break;

        case 0: // '\0'
        case 1: // '\001'
        case 7: // '\007'
            zatizeni = coolMult * f1;
            zatizeni *= w / wWEP;
            zatizeni = zatizeni * zatizeni;
            zatizeni = zatizeni * zatizeni;
            double d = zatizeni * (double)f * 1.4248134284734321E-005D;
            if(d <= World.Rnd().nextDouble(0.0D, 1.0D))
                break;
            int j = World.Rnd().nextInt(0, 19);
            if(j < 10)
            {
                reference.AS.setEngineCylinderKnockOut(reference.actor, number, 1);
                Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - cylinder");
                break;
            }
            if(j < 12)
            {
                if(j < 11)
                {
                    reference.AS.setEngineMagnetoKnockOut(reference.actor, number, 0);
                    Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - mag1");
                } else
                {
                    reference.AS.setEngineMagnetoKnockOut(reference.actor, number, 1);
                    Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - mag2");
                }
                break;
            }
            if(j < 14)
            {
                reference.AS.setEngineDies(reference.actor, number);
                Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - dead");
                break;
            }
            if(j < 15)
            {
                reference.AS.setEngineStuck(reference.actor, number);
                Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - stuck");
                break;
            }
            if(j < 17)
            {
                setKillPropAngleDevice(reference.actor);
                Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - propAngler");
            } else
            {
                reference.AS.hitOil(reference.actor, number);
                Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - oil");
            }
            break;
        }
    }

    private void computeTemperature(float f)
    {
        float f1 = Math.max(0.0F, reference.getSpeedKMH());
        float f2 = Atmosphere.temperature((float)reference.Loc.z) - 273.15F;
        float f3 = controlThrottle;
        if(!reference.isPlayers())
            f3 *= 1.1F;
        float f4 = f3;
        float f5 = 0.8F + 60F / (50F + f1);
        float f6 = 0.45F;
        if(engineBoostFactor > 1.0F)
            f4 = (f3 + Math.max(f3 - 1.0F, 0.0F) * (engineBoostFactor - 1.1F) * 10F) / engineBoostFactor;
        if(!controlAfterburner && engineAfterburnerBoostFactor > 1.0F && afterburnerType != 1 && afterburnerType != 9 && afterburnerType != 4)
            f4 /= engineAfterburnerBoostFactor;
        float f7 = f4;
        if(type == 0 || type == 1 || type == 7)
            f6 = 0.45F / Atmosphere.density((float)reference.Loc.z);
        float f8 = coolMult * f4;
        if(World.cur().diffCur.ComplexEManagement && reference.isPlayers() && ((RealFlightModel)reference).isRealMode())
            f7 = w / wWEP;
        if(!reference.isPlayers())
            f7 = (w * 1.02F) / wWEP;
        if(stage == 6)
        {
            float f9 = f2 + tOilOutMaxRPM * (1.0F - 0.15F * controlRadiator) * (0.65F + 0.45F / Atmosphere.density((float)reference.Loc.z)) * (1.0F + (float)reference.AS.astateOilStates[number] * 0.35F) * 1.2F * f7 * f7 * (0.7F + 0.3F * f8) * f5;
            tOilOut += (f9 - tOilOut) * f * tChangeSpeed;
            float f13 = tOilOut * 0.8F;
            tOilIn += (f13 - tOilIn) * f * 0.5F;
            if(type == 2)
                f6 = 0.35F + 0.1F / Atmosphere.density((float)reference.Loc.z);
            f13 = f2 + tWaterMaxRPM * (1.0F - 0.15F * controlRadiator) * (0.65F + f6) * (0.6F + 0.4F * f7) * (0.1F + 1.1F * f8) * f5 * (1.1F - 0.1F * controlMix);
            tWaterOut += (f13 - tWaterOut) * f * tChangeSpeed;
        } else
        {
            float f10 = f2;
            tOilOut += (f10 - tOilOut) * f * tChangeSpeed * (0.2F + 0.2F * controlRadiator);
            float f14 = tOilOut;
            tOilIn += (f14 - tOilIn) * f * 0.5F;
            f14 = f2;
            tWaterOut += (f14 - tWaterOut) * f * tChangeSpeed * (0.2F + 0.2F * controlRadiator);
        }
        if(!reference.isPlayers())
            return;
        if(World.cur().diffCur.Engine_Overheat && (tWaterOut > tWaterCritMax || tOilOut > tOilCritMax))
        {
            if(heatStringID == -1)
                heatStringID = HUD.makeIdLog();
            if(reference.isPlayers())
                HUD.log(heatStringID, "EngineOverheat");
            if(tWaterOut > tWaterCritMax)
            {
                float f11 = tWaterOut / tWaterCritMax - 1.0F;
                f11 *= f11 * f11 * f * 1000F;
                float f15 = World.Rnd().nextFloat() * timeOverheat;
                if(f11 > f15)
                    switch(type)
                    {
                    default:
                        int i = World.Rnd().nextInt(0, 9);
                        if(i < 2)
                        {
                            reference.AS.hitEngine(reference.actor, number, 3);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - smoke");
                        } else
                        {
                            setCyliderKnockOut(reference.actor, World.Rnd().nextInt(0, 3));
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - power loss");
                        }
                        break;

                    case 0: // '\0'
                    case 1: // '\001'
                    case 7: // '\007'
                        int k = World.Rnd().nextInt(0, 99);
                        if(k < 50)
                        {
                            reference.AS.setEngineCylinderKnockOut(reference.actor, number, 1);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - cylinder");
                            break;
                        }
                        if(k < 65)
                        {
                            reference.AS.hitOil(reference.actor, number);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - oil");
                            break;
                        }
                        if(k < 94)
                        {
                            reference.AS.setEngineCylinderKnockOut(reference.actor, number, 1);
                            int i1 = 1;
                            do
                            {
                                if(i1 >= 11)
                                    break;
                                if(World.Rnd().nextFloat() >= 0.8F)
                                {
                                    try
                                    {
                                        com.maddox.il2.engine.Hook hook = reference.actor.findHook("_Engine" + (number + 1) + "EF_" + (i1 >= 10 ? "" + i1 : "0" + i1));
                                        if(hook != null)
                                            Eff3DActor.New(reference.actor, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff", -1F);
                                    }
                                    catch(Exception exception) { }
                                    break;
                                }
                                i1++;
                            } while(true);
                            reference.AS.setSootState(reference.actor, number, 1);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - cylinder&smoke");
                            break;
                        }
                        if(k < 95)
                        {
                            reference.AS.setEngineCylinderKnockOut(reference.actor, number, 1);
                            reference.AS.hitEngine(reference.actor, number, 10);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - fire");
                            break;
                        }
                        if(k < 97)
                        {
                            setKillCompressor(reference.actor);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - Supercharger");
                            break;
                        }
                        if(k < 98)
                        {
                            reference.AS.setEngineDies(reference.actor, number);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - dead");
                        } else
                        {
                            reference.AS.setEngineStuck(reference.actor, number);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - stuck");
                        }
                        break;
                    }
            }
            if(tOilOut > tOilCritMax)
            {
                float f12 = tOilOut / tOilCritMax - 1.0F;
                f12 *= f12 * f12 * f * 1000F;
                float f16 = World.Rnd().nextFloat() * timeOverheat;
                if(f12 > f16)
                    switch(type)
                    {
                    default:
                        int j = World.Rnd().nextInt(0, 9);
                        if(j < 2)
                        {
                            reference.AS.hitEngine(reference.actor, number, 3);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - smoke");
                        } else
                        {
                            setCyliderKnockOut(reference.actor, World.Rnd().nextInt(0, 3));
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - power loss");
                        }
                        break;

                    case 0: // '\0'
                    case 1: // '\001'
                    case 7: // '\007'
                        int l = World.Rnd().nextInt(0, 99);
                        if(l < 10)
                        {
                            reference.AS.setEngineCylinderKnockOut(reference.actor, number, 1);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - cylinder");
                            break;
                        }
                        if(l < 60)
                        {
                            reference.AS.hitOil(reference.actor, number);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - oil");
                            break;
                        }
                        if(l < 94)
                        {
                            setReadyness(readyness * 0.95F);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - damaged bearing or something");
                            break;
                        }
                        if(l < 95)
                        {
                            reference.AS.hitOil(reference.actor, number);
                            reference.AS.hitEngine(reference.actor, number, 3);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - fire");
                            break;
                        }
                        if(l < 96)
                        {
                            setKillCompressor(reference.actor);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - Supercharger");
                            break;
                        }
                        if(l < 97)
                        {
                            reference.AS.setEngineDies(reference.actor, number);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - dead");
                        } else
                        {
                            reference.AS.setEngineStuck(reference.actor, number);
                            Aircraft.debugprintln(reference.actor, "Malfunction #" + number + " - stuck");
                        }
                        break;
                    }
            }
        }
    }

    public void updateRadiator(float f)
    {
        if((reference.actor instanceof GLADIATOR) || (reference.actor instanceof DXXI_DK) || (reference.actor instanceof DXXI_DU) || (reference.actor instanceof DXXI_SARJA3_EARLY) || (reference.actor instanceof DXXI_SARJA3_LATE) || (reference.actor instanceof DXXI_SARJA3_SARVANTO))
        {
            controlRadiator = 0.0F;
            return;
        }
        if((reference.actor instanceof P_51) || (reference.actor instanceof P_38) || (reference.actor instanceof YAK_3) || (reference.actor instanceof YAK_3P) || (reference.actor instanceof YAK_9M) || (reference.actor instanceof YAK_9U) || (reference.actor instanceof YAK_9UT) || (reference.actor instanceof P_63C))
        {
            if(tOilOut > tOilOutMaxRPM)
            {
                controlRadiator += 0.1F * f;
                if(controlRadiator > 1.0F)
                    controlRadiator = 1.0F;
            } else
            {
                controlRadiator = 1.0F - reference.getSpeed() / reference.VmaxH;
                if(controlRadiator < 0.0F)
                    controlRadiator = 0.0F;
            }
            return;
        }
        if((reference.actor instanceof SPITFIRE9) || (reference.actor instanceof SPITFIRE8) || (reference.actor instanceof SPITFIRE8CLP))
        {
            controlRadiator = ((tWaterOut - tWaterCritMax) + 20F) / 20F;
            if(controlRadiator < 0.0F)
                controlRadiator = 0.0F;
            if(controlRadiator > 1.0F)
                controlRadiator = 1.0F;
            return;
        }
        switch(propAngleDeviceType)
        {
        case 3: // '\003'
        case 4: // '\004'
        default:
            controlRadiator = 1.0F - getPowerOutput();
            break;

        case 5: // '\005'
        case 6: // '\006'
            controlRadiator = 1.0F - reference.getSpeed() / reference.VmaxH;
            if(controlRadiator < 0.0F)
                controlRadiator = 0.0F;
            break;

        case 1: // '\001'
        case 2: // '\002'
            if(controlRadiator > 1.0F - getPowerOutput())
            {
                controlRadiator -= 0.15F * f;
                if(controlRadiator < 0.0F)
                    controlRadiator = 0.0F;
            } else
            {
                controlRadiator += 0.15F * f;
            }
            break;

        case 8: // '\b'
            if(type == 0)
            {
                if(tOilOut > tOilOutMaxRPM)
                {
                    controlRadiator += 0.1F * f;
                    if(controlRadiator > 1.0F)
                        controlRadiator = 1.0F;
                    break;
                }
                if(tOilOut >= tOilOutMaxRPM - 10F)
                    break;
                controlRadiator -= 0.1F * f;
                if(controlRadiator < 0.0F)
                    controlRadiator = 0.0F;
                break;
            }
            if(controlRadiator > 1.0F - getPowerOutput())
            {
                controlRadiator -= 0.15F * f;
                if(controlRadiator < 0.0F)
                    controlRadiator = 0.0F;
            } else
            {
                controlRadiator += 0.15F * f;
            }
            break;

        case 7: // '\007'
            if(tOilOut > tOilOutMaxRPM)
            {
                controlRadiator += 0.1F * f;
                if(controlRadiator > 1.0F)
                    controlRadiator = 1.0F;
                break;
            }
            controlRadiator = 1.0F - reference.getSpeed() / reference.VmaxH;
            if(controlRadiator < 0.0F)
                controlRadiator = 0.0F;
            break;
        }
    }

    private void computeForces(float f)
    {
        switch(type)
        {
        case 0: // '\0'
        case 1: // '\001'
        case 9: // '\009'
			//TODO: Stand-in code for turboprop
		case 10: // '\002'
			engineMoment = propAngleDeviceMinParam + getControlThrottle() * (propAngleDeviceMaxParam - propAngleDeviceMinParam);
			engineMoment /= propAngleDeviceMaxParam;
			engineMoment *= engineMomentMax;
			engineMoment *= getReadyness();
			engineMoment *= getDistabilisationMultiplier();
			engineMoment *= getStageMultiplier();
			engineMoment += getJetFrictionMoment(f);
			computePropForces(w, 0.0F, 0.0F, propAoA0, 0.0F);
			/*
            float fx27 = w * _1_wMax;
            float fx28 = fx27 * pressureExtBar;
            float fx29 = fx27 * fx27;
            float fx30 = 1.0F - 0.006F * (Atmosphere.temperature((float)((Tuple3d) (((FlightModelMain) (reference)).Loc)).z) - 290F);
            float fx31 = 1.0F - 0.0011F * reference.getSpeed();
            propForce = horsePowers * fx28 * fx29 * fx30 * fx31 * getStageMultiplier();
            float fx32 = engineMoment - propMoment;
            aw = (fx32 / (propI + engineI)) * 1.0F;
            if(aw > 0.0F)
                aw *= engineAcceleration;
            w += aw * f;
            if(w < -wMaxAllowed)
                w = -wMaxAllowed;
            if(w > wMaxAllowed + wMaxAllowed)
                w = wMaxAllowed + wMaxAllowed;
            engineForce.set(engineVector);
            engineForce.scale(propForce);
            engineTorque.cross(enginePos, engineForce);
            rpm = toRPM(w);
            break;
			 */
        case 7: // '\007'
            if(Math.abs(w) < 1E-005F)
                propPhiW = 1.570796F;
            else
            if(type == 7)
                propPhiW = (float)Math.atan(Math.abs(reference.Vflow.x) / (double)(w * propReductor * propr));
            else
                propPhiW = (float)Math.atan(reference.Vflow.x / (double)(w * propReductor * propr));
            propAoA = propPhi - propPhiW;
            if(type == 7)
                computePropForces(w * propReductor, (float)Math.abs(reference.Vflow.x), propPhi, propAoA, reference.getAltitude());
            else
                computePropForces(w * propReductor, (float)reference.Vflow.x, propPhi, propAoA, reference.getAltitude());
            switch(propAngleDeviceType)
            {
            case 3: // '\003'
            case 4: // '\004'
                float f8 = controlThrottle;
                if(f8 > 1.0F)
                    f8 = 1.0F;
                compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * f8;
                if(isPropAngleDeviceOperational())
                {
                    if(bControlPropAuto)
                        propTarget = propPhiW + propAoA0;
                    else
                        propTarget = propPhiMax - controlProp * (propPhiMax - propPhiMin);
                } else
                if(propAngleDeviceType == 3)
                    propTarget = 0.0F;
                else
                    propTarget = 3.141593F;
                break;

            case 9: // '\t'
                if(bControlPropAuto)
                {
                    float f15 = propAngleDeviceMaxParam;
                    if(controlAfterburner)
                        f15 = propAngleDeviceAfterburnerParam;
                    controlProp += ((float)controlPropDirection * f) / 5F;
                    if(controlProp > 1.0F)
                        controlProp = 1.0F;
                    else
                    if(controlProp < 0.0F)
                        controlProp = 0.0F;
                    float f21 = propAngleDeviceMinParam + (f15 - propAngleDeviceMinParam) * controlProp;
                    float f25 = controlThrottle;
                    if(f25 > 1.0F)
                        f25 = 1.0F;
                    compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + (propAngleDeviceMaxParam - propAngleDeviceMinParam) * f25));
                    if(isPropAngleDeviceOperational())
                    {
                        if(w < f21)
                        {
                            f21 = Math.min(1.0F, 0.01F * (f21 - w) - 0.012F * aw);
                            propTarget -= f21 * getPropAngleDeviceSpeed() * f;
                        } else
                        {
                            f21 = Math.min(1.0F, 0.01F * (w - f21) + 0.012F * aw);
                            propTarget += f21 * getPropAngleDeviceSpeed() * f;
                        }
                        if(stage == 6 && propTarget < propPhiW - 0.12F)
                        {
                            propTarget = propPhiW - 0.12F;
                            if(propPhi < propTarget)
                                propPhi += 0.2F * f;
                        }
                    } else
                    {
                        propTarget = propPhi;
                    }
                } else
                {
                    compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * (controlThrottle <= 1.0F ? controlThrottle : 1.0F);
                    propTarget = propPhi;
                    if(isPropAngleDeviceOperational())
                        if(controlPropDirection > 0)
                            propTarget = propPhiMin;
                        else
                        if(controlPropDirection < 0)
                            propTarget = propPhiMax;
                }
                break;

            case 1: // '\001'
            case 2: // '\002'
                if(bControlPropAuto)
                    if(engineBoostFactor > 1.0F)
                        controlProp = 0.75F + 0.227272F * controlThrottle;
                    else
                        controlProp = 0.75F + 0.25F * controlThrottle;
                float f16 = propAngleDeviceMaxParam;
                if(controlAfterburner && (!bWepRpmInLowGear || controlCompressor != compressorMaxStep))
                    f16 = propAngleDeviceAfterburnerParam;
                float f1 = propAngleDeviceMinParam + (f16 - propAngleDeviceMinParam) * controlProp;
                float f9 = controlThrottle;
                if(f9 > 1.0F)
                    f9 = 1.0F;
                compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + (propAngleDeviceMaxParam - propAngleDeviceMinParam) * f9));
                if(isPropAngleDeviceOperational())
                {
                    if(w < f1)
                    {
                        f1 = Math.min(1.0F, 0.01F * (f1 - w) - 0.012F * aw);
                        propTarget -= f1 * getPropAngleDeviceSpeed() * f;
                    } else
                    {
                        f1 = Math.min(1.0F, 0.01F * (w - f1) + 0.012F * aw);
                        propTarget += f1 * getPropAngleDeviceSpeed() * f;
                    }
                    if(stage == 6 && propTarget < propPhiW - 0.12F)
                    {
                        propTarget = propPhiW - 0.12F;
                        if(propPhi < propTarget)
                            propPhi += 0.2F * f;
                    }
                } else
                if(propAngleDeviceType == 1)
                    propTarget = 0.0F;
                else
                    propTarget = 1.5708F;
                break;

            case 7: // '\007'
                float f22 = controlThrottle;
                if(engineBoostFactor > 1.0F)
                    f22 = 0.9090909F * controlThrottle;
                float f17 = propAngleDeviceMaxParam;
                if(controlAfterburner)
                    if(afterburnerType == 1)
                    {
                        if(controlThrottle > 1.0F)
                            f17 = propAngleDeviceMaxParam + 10F * (controlThrottle - 1.0F) * (propAngleDeviceAfterburnerParam - propAngleDeviceMaxParam);
                    } else
                    {
                        f17 = propAngleDeviceAfterburnerParam;
                    }
                float f2 = propAngleDeviceMinParam + (f17 - propAngleDeviceMinParam) * f22;
                float f10 = controlThrottle;
                if(f10 > 1.0F)
                    f10 = 1.0F;
                compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + (f17 - propAngleDeviceMinParam) * f10));
                if(isPropAngleDeviceOperational())
                    if(bControlPropAuto)
                    {
                        if(w < f2)
                        {
                            f2 = Math.min(1.0F, 0.01F * (f2 - w) - 0.012F * aw);
                            propTarget -= f2 * getPropAngleDeviceSpeed() * f;
                        } else
                        {
                            f2 = Math.min(1.0F, 0.01F * (w - f2) + 0.012F * aw);
                            propTarget += f2 * getPropAngleDeviceSpeed() * f;
                        }
                        if(stage == 6 && propTarget < propPhiW - 0.12F)
                        {
                            propTarget = propPhiW - 0.12F;
                            if(propPhi < propTarget)
                                propPhi += 0.2F * f;
                        }
                        if(propTarget < propPhiMin + (float)Math.toRadians(3D))
                            propTarget = propPhiMin + (float)Math.toRadians(3D);
                    } else
                    {
                        propTarget = (1.0F - f * 0.1F) * propTarget + f * 0.1F * (propPhiMax - controlProp * (propPhiMax - propPhiMin));
                        if(w > 1.02F * wMax)
                            wMaxAllowed = (1.0F - 4E-007F * (w - 1.02F * wMax)) * wMaxAllowed;
                        if(w > wMax)
                        {
                            float f26 = w - wMax;
                            f26 *= f26;
                            float f27 = 1.0F - 0.001F * f26;
                            if(f27 < 0.0F)
                                f27 = 0.0F;
                            propForce *= f27;
                        }
                    }
                break;

            case 8: // '\b'
                float f23 = controlThrottle;
                if(engineBoostFactor > 1.0F)
                    f23 = 0.9090909F * controlThrottle;
                float f18 = propAngleDeviceMaxParam;
                if(controlAfterburner)
                    if(afterburnerType == 1)
                    {
                        if(controlThrottle > 1.0F)
                            f18 = propAngleDeviceMaxParam + 10F * (controlThrottle - 1.0F) * (propAngleDeviceAfterburnerParam - propAngleDeviceMaxParam);
                    } else
                    {
                        f18 = propAngleDeviceAfterburnerParam;
                    }
                float f3 = propAngleDeviceMinParam + (f18 - propAngleDeviceMinParam) * f23 + (bControlPropAuto ? 0.0F : -25F + 50F * controlProp);
                float f11 = controlThrottle;
                if(f11 > 1.0F)
                    f11 = 1.0F;
                compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + (f18 - propAngleDeviceMinParam) * f11));
                if(isPropAngleDeviceOperational())
                {
                    if(w < f3)
                    {
                        f3 = Math.min(1.0F, 0.01F * (f3 - w) - 0.012F * aw);
                        propTarget -= f3 * getPropAngleDeviceSpeed() * f;
                    } else
                    {
                        f3 = Math.min(1.0F, 0.01F * (w - f3) + 0.012F * aw);
                        propTarget += f3 * getPropAngleDeviceSpeed() * f;
                    }
                    if(stage == 6 && propTarget < propPhiW - 0.12F)
                    {
                        propTarget = propPhiW - 0.12F;
                        if(propPhi < propTarget)
                            propPhi += 0.2F * f;
                    }
                    if(propTarget < propPhiMin + (float)Math.toRadians(3D))
                        propTarget = propPhiMin + (float)Math.toRadians(3D);
                }
                break;

            case 6: // '\006'
                float f12 = controlThrottle;
                if(f12 > 1.0F)
                    f12 = 1.0F;
                compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * f12;
                if(isPropAngleDeviceOperational())
                    if(bControlPropAuto)
                    {
                        float f4 = 25F + (wMax - 25F) * (0.25F + 0.75F * controlThrottle);
                        if(w < f4)
                        {
                            f4 = Math.min(1.0F, 0.01F * (f4 - w) - 0.012F * aw);
                            propTarget -= f4 * getPropAngleDeviceSpeed() * f;
                        } else
                        {
                            f4 = Math.min(1.0F, 0.01F * (w - f4) + 0.012F * aw);
                            propTarget += f4 * getPropAngleDeviceSpeed() * f;
                        }
                        if(stage == 6 && propTarget < propPhiW - 0.12F)
                        {
                            propTarget = propPhiW - 0.12F;
                            if(propPhi < propTarget)
                                propPhi += 0.2F * f;
                        }
                        controlProp = (propAngleDeviceMaxParam - propTarget) / (propAngleDeviceMaxParam - propAngleDeviceMinParam);
                        if(controlProp < 0.0F)
                            controlProp = 0.0F;
                        if(controlProp > 1.0F)
                            controlProp = 1.0F;
                    } else
                    {
                        propTarget = propAngleDeviceMaxParam - controlProp * (propAngleDeviceMaxParam - propAngleDeviceMinParam);
                    }
                break;

            case 5: // '\005'
                float f13 = controlThrottle;
                if(f13 > 1.0F)
                    f13 = 1.0F;
                compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * f13;
                if(bControlPropAuto)
                    if(reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode())
                    {
                        if(World.cur().diffCur.ComplexEManagement)
                            controlProp = -controlThrottle;
                        else
                            controlProp = -Aircraft.cvt(reference.getSpeed(), reference.Vmin, reference.Vmax, 0.0F, 1.0F);
                    } else
                    {
                        controlProp = -Aircraft.cvt(reference.getSpeed(), reference.Vmin, reference.Vmax, 0.0F, 1.0F);
                    }
                propTarget = propAngleDeviceMaxParam - controlProp * (propAngleDeviceMaxParam - propAngleDeviceMinParam);
                propPhi = propTarget;
                break;
            }
            if(controlFeather == 1 && bHasFeatherControl && isPropAngleDeviceOperational())
                propTarget = 1.55F;
            if(propPhi > propTarget)
            {
                float f5 = Math.min(1.0F, 157.2958F * (propPhi - propTarget));
                propPhi -= f5 * getPropAngleDeviceSpeed() * f;
            } else
            if(propPhi < propTarget)
            {
                float f6 = Math.min(1.0F, 157.2958F * (propTarget - propPhi));
                propPhi += f6 * getPropAngleDeviceSpeed() * f;
            }
            if(propTarget > propPhiMax)
                propTarget = propPhiMax;
            else
            if(propTarget < propPhiMin)
                propTarget = propPhiMin;
            if(propPhi > propPhiMax && controlFeather == 0)
                propPhi = propPhiMax;
            else
            if(propPhi < propPhiMin)
                propPhi = propPhiMin;
            engineMoment = getN();
            float f14 = getCompressorMultiplier(f);
            engineMoment *= f14;
            momForFuel = engineMoment;
            engineMoment *= getReadyness();
            engineMoment *= getMagnetoMultiplier();
            engineMoment *= getMixMultiplier();
            engineMoment *= getStageMultiplier();
            engineMoment *= getDistabilisationMultiplier();
            engineMoment += getFrictionMoment(f);
            float f19 = engineMoment - propMoment;
            aw = f19 / (propI + engineI);
            if(aw > 0.0F)
                aw *= engineAcceleration;
            oldW = w;
            w += aw * f;
            if(w < 0.0F)
                w = 0.0F;
            if(w > wMaxAllowed + wMaxAllowed)
                w = wMaxAllowed + wMaxAllowed;
            if(oldW == 0.0F)
            {
                if(w < 10F * fricCoeffT)
                    w = 0.0F;
            } else
            if(w < 2.0F * fricCoeffT)
                w = 0.0F;
            if(reference.isPlayers() && World.cur().diffCur.Torque_N_Gyro_Effects && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode())
            {
                propIW.set(propI * w * propReductor, 0.0D, 0.0D);
                if(propDirection == 1)
                    propIW.x = -propIW.x;
                engineTorque.set(0.0F, 0.0F, 0.0F);
                float f24 = propI * aw * propReductor;
                if(propDirection == 0)
                {
                    engineTorque.x += propMoment;
                    engineTorque.x += f24;
                } else
                {
                    engineTorque.x -= propMoment;
                    engineTorque.x -= f24;
                }
            } else
            {
                engineTorque.set(0.0F, 0.0F, 0.0F);
            }
            engineForce.set(engineVector);
            engineForce.scale(propForce);
            tmpV3f.cross(propPos, engineForce);
            engineTorque.add(tmpV3f);
            rearRush = 0.0F;
            rpm = toRPM(w);
            double d = reference.Vflow.x + addVflow;
            if(d < 1.0D)
                d = 1.0D;
            double d1 = 1.0D / ((double)(Atmosphere.density(reference.getAltitude()) * 6F) * d);
            addVflow = 0.94999999999999996D * addVflow + 0.050000000000000003D * (double)propForce * d1;
            addVside = 0.94999999999999996D * addVside + 0.050000000000000003D * (double)(propMoment / propr) * d1;
            if(addVside < 0.0D)
                addVside = 0.0D;
            break;

        case 2: // '\002'
            engineMoment = propAngleDeviceMinParam + getControlThrottle() * (propAngleDeviceMaxParam - propAngleDeviceMinParam);
            engineMoment /= propAngleDeviceMaxParam;
            engineMoment *= engineMomentMax;
            engineMoment *= getReadyness();
            engineMoment *= getDistabilisationMultiplier();
            engineMoment *= getStageMultiplier();
            engineMoment += getJetFrictionMoment(f);
            computePropForces(w, 0.0F, 0.0F, propAoA0, 0.0F);
            float f29 = w * _1_wMax;
            float f30 = f29 * pressureExtBar;
            float f31 = f29 * f29;
            float f32 = 1.0F - 0.006F * (Atmosphere.temperature((float)reference.Loc.z) - 290F);
            float f33 = 1.0F - 0.0011F * reference.getSpeed();
            propForce = thrustMax * f30 * f31 * f32 * f33 * getStageMultiplier();
            float f20 = engineMoment - propMoment;
            aw = (f20 / (propI + engineI)) * 1.0F;
            if(aw > 0.0F)
                aw *= engineAcceleration;
            w += aw * f;
            if(w < -wMaxAllowed)
                w = -wMaxAllowed;
            if(w > wMaxAllowed + wMaxAllowed)
                w = wMaxAllowed + wMaxAllowed;
            engineForce.set(engineVector);
            engineForce.scale(propForce);
            engineTorque.cross(enginePos, engineForce);
            rpm = toRPM(w);
            break;

        case 3: // '\003'
        case 4: // '\004'
            w = wMin + (wMax - wMin) * controlThrottle;
            if(w < wMin || w < 0.0F || reference.M.fuel == 0.0F || stage != 6)
                w = 0.0F;
            propForce = (w / wMax) * thrustMax;
            propForce *= getStageMultiplier();
            propForce *= compressorPMax / (compressorPMax + 1E-005F * Atmosphere.pressure((float)reference.Loc.z));
            engineForce.set(engineVector);
            engineForce.scale(propForce);
            engineTorque.cross(enginePos, engineForce);
            rpm = toRPM(w);
            break;

        case 6: // '\006'
            w = wMin + (wMax - wMin) * controlThrottle;
            if(w < wMin || w < 0.0F || stage != 6)
                w = 0.0F;
            float f34 = reference.getSpeed() / 94F;
            if(f34 < 1.0F)
                w = 0.0F;
            else
                f34 = (float)Math.sqrt(f34);
            propForce = (w / wMax) * thrustMax * f34;
            propForce *= getStageMultiplier();
            float f7 = (float)reference.Vwld.length();
            if(f7 > 208.333F)
                if(f7 > 291.666F)
                    propForce = 0.0F;
                else
                    propForce *= (float)Math.sqrt((291.666F - f7) / 83.33299F);
            engineForce.set(engineVector);
            engineForce.scale(propForce);
            engineTorque.cross(enginePos, engineForce);
            rpm = toRPM(w);
            if(!(reference instanceof RealFlightModel))
                break;
            RealFlightModel realflightmodel = (RealFlightModel)reference;
            f7 = Aircraft.cvt(propForce, 0.0F, thrustMax, 0.0F, 0.21F);
            if(realflightmodel.producedShakeLevel < f7)
                realflightmodel.producedShakeLevel = f7;
            break;

        case 5: // '\005'
            engineForce.set(engineVector);
            engineForce.scale(propForce);
            engineTorque.cross(enginePos, engineForce);
            break;

        default:
            return;
        }
    }

    private void computePropForces(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = f * propr;
        float f6 = f1 * f1 + f5 * f5;
        float f7 = (float)Math.sqrt(f6);
        float f8 = 0.5F * getFanCy((float)Math.toDegrees(f3)) * Atmosphere.density(f4) * f6 * propSEquivalent;
        float f9 = 0.5F * getFanCx((float)Math.toDegrees(f3)) * Atmosphere.density(f4) * f6 * propSEquivalent;
        if(f7 > 300F)
        {
            float f10 = 1.0F + 0.02F * (f7 - 300F);
            if(f10 > 2.0F)
                f10 = 2.0F;
            f9 *= f10;
        }
        if(f7 < 0.001F)
            f7 = 0.001F;
        float f11 = 1.0F / f7;
        float f12 = f1 * f11;
        float f13 = f5 * f11;
        float f14 = 1.0F;
        if(f1 < Vopt)
        {
            float f15 = Vopt - f1;
            f14 = 1.0F - 5E-005F * f15 * f15;
        }
        propForce = f14 * (f8 * f13 - f9 * f12);
        propMoment = (f9 * f13 + f8 * f12) * propr;
    }

    public void toggle()
    {
        if(stage == 0)
        {
            setEngineStarts(reference.actor);
            return;
        }
        if(stage < 7)
        {
            setEngineStops(reference.actor);
            if(reference.isPlayers())
                HUD.log("EngineI0");
            return;
        } else
        {
            return;
        }
    }

    public float getPowerOutput()
    {
        if(stage == 0 || stage > 6)
            return 0.0F;
        else
            return controlThrottle * readyness;
    }

    public float getThrustOutput()
    {
        if(stage == 0 || stage > 6)
            return 0.0F;
        float f = w * _1_wMax * readyness;
        if(f > 1.1F)
            f = 1.1F;
        return f;
    }

    public float getReadyness()
    {
        return readyness;
    }

    public float getPropPhi()
    {
        return propPhi;
    }

    private float getPropAngleDeviceSpeed()
    {
        if(isPropAngleDeviceHydroOperable)
            return propAngleChangeSpeed;
        else
            return propAngleChangeSpeed * 10F;
    }

    public int getPropDir()
    {
        return propDirection;
    }

    public float getPropAoA()
    {
        return propAoA;
    }

    public Vector3f getForce()
    {
        return engineForce;
    }

    public float getRearRush()
    {
        return rearRush;
    }

    public float getw()
    {
        return w;
    }

    public float getRPM()
    {
        return rpm;
    }

    public float getPropw()
    {
        return w * propReductor;
    }

    public float getPropRPM()
    {
        return rpm * propReductor;
    }

    public int getType()
    {
        return type;
    }

    public float getControlThrottle()
    {
        return controlThrottle;
    }

    public boolean getControlAfterburner()
    {
        return controlAfterburner;
    }

    public boolean isHasControlThrottle()
    {
        return bHasThrottleControl;
    }

    public boolean isHasControlAfterburner()
    {
        return bHasAfterburnerControl;
    }

    public float getControlProp()
    {
        return controlProp;
    }

    public float getElPropPos()
    {
        float f;
        if(bControlPropAuto)
            f = controlProp;
        else
            f = (propPhiMax - propPhi) / (propPhiMax - propPhiMin);
        if(f < 0.1F)
            return 0.0F;
        if(f > 0.9F)
            return 1.0F;
        else
            return f;
    }

    public boolean getControlPropAuto()
    {
        return bControlPropAuto;
    }

    public boolean isHasControlProp()
    {
        return bHasPropControl;
    }

    public boolean isAllowsAutoProp()
    {
        if(reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode())
            if(World.cur().diffCur.ComplexEManagement)
                switch(propAngleDeviceType)
                {
                case 0: // '\0'
                    return false;

                case 5: // '\005'
                    return true;

                case 6: // '\006'
                    return false;

                case 3: // '\003'
                case 4: // '\004'
                    return false;

                case 1: // '\001'
                case 2: // '\002'
                    return (reference.actor instanceof SPITFIRE9E) || (reference.actor instanceof SPITFIRE9ECLP) || (reference.actor instanceof SPITFIRE9EHF) || (reference.actor instanceof SPITFIRE925LBSCW) || (reference.actor instanceof SPITFIRE8) || (reference.actor instanceof SPITFIRE8CLP);

                case 7: // '\007'
                case 8: // '\b'
                    return true;
                }
            else
                return bHasPropControl;
        return true;
    }

    public float getControlMix()
    {
        return controlMix;
    }

    public boolean isHasControlMix()
    {
        return bHasMixControl;
    }

    public int getControlMagnetos()
    {
        return controlMagneto;
    }

    public int getControlCompressor()
    {
        return controlCompressor;
    }

    public boolean isHasControlMagnetos()
    {
        return bHasMagnetoControl;
    }

    public boolean isHasControlCompressor()
    {
        return bHasCompressorControl;
    }

    public int getControlFeather()
    {
        return controlFeather;
    }

    public boolean isHasControlFeather()
    {
        return bHasFeatherControl;
    }

    public boolean isAllowsAutoRadiator()
    {
        if(World.cur().diffCur.ComplexEManagement)
        {
            if((reference.actor instanceof P_51) || (reference.actor instanceof P_38) || (reference.actor instanceof YAK_3) || (reference.actor instanceof YAK_3P) || (reference.actor instanceof YAK_9M) || (reference.actor instanceof YAK_9U) || (reference.actor instanceof YAK_9UT) || (reference.actor instanceof SPITFIRE8) || (reference.actor instanceof SPITFIRE8CLP) || (reference.actor instanceof SPITFIRE9) || (reference.actor instanceof P_63C))
                return true;
            switch(propAngleDeviceType)
            {
            case 7: // '\007'
                return true;

            case 8: // '\b'
                return type == 0;
            }
            return false;
        } else
        {
            return true;
        }
    }

    public boolean isHasControlRadiator()
    {
        return bHasRadiatorControl;
    }

    public float getControlRadiator()
    {
        return controlRadiator;
    }

    public int getExtinguishers()
    {
        return extinguishers;
    }

    private float getFanCy(float f)
    {
        if(f > 34F)
            f = 34F;
        if(f < -8F)
            f = -8F;
        if(f < 16F)
            return -0.004688F * f * f + 0.15F * f + 0.4F;
        float f1 = 0.0F;
        if(f > 22F)
        {
            f1 = 0.01F * (f - 22F);
            f = 22F;
        }
        return (0.00097222F * f * f - 0.070833F * f) + 2.4844F + f1;
    }

    private float getFanCx(float f)
    {
        if(f < -4F)
            f = -8F - f;
        if(f > 34F)
            f = 34F;
        if((double)f < 16D)
            return 0.00035F * f * f + 0.0028F * f + 0.0256F;
        float f1 = 0.0F;
        if(f > 22F)
        {
            f1 = 0.04F * (f - 22F);
            f = 22F;
        }
        return ((-0.00555F * f * f + 0.24444F * f) - 2.32888F) + f1;
    }

    public int getCylinders()
    {
        return cylinders;
    }

    public int getCylindersOperable()
    {
        return cylindersOperable;
    }

    public float getCylindersRatio()
    {
        return (float)cylindersOperable / (float)cylinders;
    }

    public int getStage()
    {
        return stage;
    }

    public float getBoostFactor()
    {
        return engineBoostFactor;
    }

    public float getManifoldPressure()
    {
        return compressorManifoldPressure;
    }

    public void setManifoldPressure(float f)
    {
        compressorManifoldPressure = f;
    }

    public boolean getSootState()
    {
        return false;
    }

    public Point3f getEnginePos()
    {
        return enginePos;
    }

    public Point3f getPropPos()
    {
        return propPos;
    }

    public Vector3f getEngineVector()
    {
        return engineVector;
    }

    public float rangeAndFuel(float f, float f1, boolean flag, float f2)
    {
        tmpF = 0.0F;
        return 3600F * tmpF;
    }

    public float forcePropAOA(float f, float f1, float f2, boolean flag)
    {
        return forcePropAOA(f, f1, f2, flag, 1.0F);
    }

    public float forcePropAOA(float f, float f1, float f2, boolean flag, float f3)
    {
        switch(type)
        {
        default:
            return -1F;

        case 0: // '\0'
        case 1: // '\001'
        case 9: // '\009'
		case 10: // '\010'
			//TODO: Stand-in code for turboprop
			/*
            pressureExtBar = Atmosphere.pressure(f1) + compressorSpeedManifold * 0.5F * Atmosphere.density(f1) * f * f;
            pressureExtBar /= Atmosphere.P0();
            float fx13 = pressureExtBar;
            float fx15 = 1.0F - 0.006F * (Atmosphere.temperature(f1) - 290F);
            float fx17 = 1.0F - 0.0011F * f;
            propForce = horsePowers * fx13 * fx15 * fx17;
            return propForce;
			 */
        case 7: // '\007'
            float f4;
            boolean flag1;
            int i;
            boolean flag3;
label0:
            {
                f4 = controlThrottle;
                flag1 = controlAfterburner;
                i = stage;
                safeLoc.set(reference.Loc);
                safeVwld.set(reference.Vwld);
                safeVflow.set(reference.Vflow);
                if(flag)
                    w = wWEP;
                else
                    w = wMax;
                w *= f3;
                controlThrottle = f2;
                if((double)engineBoostFactor <= 1.0D && controlThrottle > 1.0F)
                    controlThrottle = 1.0F;
                if(afterburnerType > 0 && flag)
                    controlAfterburner = true;
                stage = 6;
                fastATA = true;
                reference.Loc.set(0.0D, 0.0D, f1);
                reference.Vwld.set(f, 0.0D, 0.0D);
                reference.Vflow.set(f, 0.0D, 0.0D);
                pressureExtBar = Atmosphere.pressure(reference.getAltitude()) + compressorSpeedManifold * 0.5F * Atmosphere.density(reference.getAltitude()) * f * f;
                pressureExtBar *= 9.8716682999999996E-006D;
                maxMoment = getCompressorMultiplier(0.033F);
                maxMoment *= getN();
                if(flag && bWepRpmInLowGear && controlCompressor == compressorMaxStep)
                {
                    w = wMax * f3;
                    float f5 = getCompressorMultiplier(0.033F);
                    f5 *= getN();
                    maxMoment = f5;
                }
                maxW = w;
                float f6 = propPhiMin;
                float f7 = -1E+008F;
                flag3 = false;
                if((Aircraft)(Aircraft)reference.actor instanceof TypeTwoPitchProp)
                    flag3 = true;
                do
                {
                    if(propAngleDeviceType != 0 && !flag3)
                        break label0;
                    float f8 = 2.0F;
                    int j = 0;
                    float f10 = 0.1F;
                    float f12 = 0.5F;
                    do
                    {
                        if(flag)
                            w = wWEP * f12;
                        else
                            w = wMax * f12;
                        float f15 = (float)Math.sqrt(f * f + w * propr * propReductor * w * propr * propReductor);
                        float f16 = f6 - (float)Math.asin(f / f15);
                        computePropForces(w * propReductor, f, 0.0F, f16, f1);
                        maxMoment = getN() * getCompressorMultiplier(0.033F);
                        maxW = w;
                        if(j > 32 || (double)f10 <= 1.0000000000000001E-005D)
                            break;
                        if(propMoment < maxMoment)
                        {
                            if(f8 == 1.0F)
                                f10 /= 2.0F;
                            f12 *= 1.0F + f10;
                            f8 = 0.0F;
                        } else
                        {
                            if(f8 == 0.0F)
                                f10 /= 2.0F;
                            f12 /= 1.0F + f10;
                            f8 = 1.0F;
                        }
                        j++;
                    } while(true);
                    if(!flag3)
                        break label0;
                    if(f6 != propPhiMin)
                        break;
                    f7 = propForce;
                    f6 = propPhiMax;
                } while(true);
                if(f7 > propForce)
                    propForce = f7;
            }
            controlThrottle = f4;
            controlAfterburner = flag1;
            stage = i;
            reference.Loc.set(safeLoc);
            reference.Vwld.set(safeVwld);
            reference.Vflow.set(safeVflow);
            fastATA = false;
            w = 0.0F;
            if(flag3 || propAngleDeviceType == 0)
                return propForce;
            float f9 = 1.5F;
            float f11 = -0.06F;
            int k = 0;
            do
            {
                float f14 = 0.5F * (f9 + f11);
                if(flag && (!bWepRpmInLowGear || controlCompressor != compressorMaxStep))
                    computePropForces(wWEP * f3 * propReductor, f, 0.0F, f14, f1);
                else
                    computePropForces(wMax * f3 * propReductor, f, 0.0F, f14, f1);
                if((propForce <= 0.0F || Math.abs(propMoment - maxMoment) >= 1E-005F) && k <= 32)
                {
                    if(propForce > 0.0F && propMoment > maxMoment)
                        f9 = f14;
                    else
                        f11 = f14;
                    k++;
                } else
                {
                    return propForce;
                }
            } while(true);

        case 2: // '\002'
            pressureExtBar = Atmosphere.pressure(f1) + compressorSpeedManifold * 0.5F * Atmosphere.density(f1) * f * f;
            pressureExtBar *= 9.8716682999999996E-006D;
            float f17 = pressureExtBar;
            float f18 = 1.0F - 0.006F * (Atmosphere.temperature(f1) - 290F);
            float f19 = 1.0F - 0.0011F * f;
            propForce = thrustMax * f17 * f18 * f19;
            if(f2 > 1.0F)
                f2 = 1.0F;
            propForce *= f2;
            return propForce;

        case 3: // '\003'
        case 4: // '\004'
        case 6: // '\006'
            propForce = thrustMax;
            propForce *= compressorPMax / (compressorPMax + 1E-005F * Atmosphere.pressure(f1));
            return propForce;

        case 5: // '\005'
            return thrustMax;

        case 8: // '\b'
            return -1F;
        }
    }

    public float getEngineLoad()
    {
        float f = 0.1F + getControlThrottle() * 0.8181818F;
        float f1 = getw() / wMax;
        return f1 / f;
    }

    private void overrevving()
    {
        if((reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode() && World.cur().diffCur.ComplexEManagement && World.cur().diffCur.Engine_Overheat && w > wMaxAllowed && bIsMaster)
        {
            wMaxAllowed = 0.999965F * wMaxAllowed;
            _1_wMaxAllowed = 1.0F / wMaxAllowed;
            tmpF *= 1.0F - (wMaxAllowed - w) * 0.01F;
            engineDamageAccum += 0.01F + 0.05F * (w - wMaxAllowed) * _1_wMaxAllowed;
            if(engineDamageAccum > 1.0F)
            {
                if(heatStringID == -1)
                    heatStringID = HUD.makeIdLog();
                if(reference.isPlayers())
                    HUD.log(heatStringID, "EngineOverheat");
                setReadyness(getReadyness() - (engineDamageAccum - 1.0F) * 0.005F);
            }
            if(getReadyness() < 0.2F)
                setEngineDies(reference.actor);
        }
    }

    public float getN()
    {
        if(stage == 6)
        {
            switch(engineCarburetorType)
            {
            case 0: // '\0'
                float f = 0.05F + 0.95F * getControlThrottle();
                float f4 = w / wMax;
                tmpF = engineMomentMax * ((-1F / f) * f4 * f4 + 2.0F * f4);
                if(getControlThrottle() > 1.0F)
                    tmpF *= engineBoostFactor;
                overrevving();
                break;

            case 3: // '\003'
                float f1 = 0.1F + 0.9F * getControlThrottle();
                float f5 = w / wNom;
                tmpF = engineMomentMax * ((-1F / f1) * f5 * f5 + 2.0F * f5);
                if(getControlThrottle() > 1.0F)
                    tmpF *= engineBoostFactor;
                float f10 = getControlThrottle() - neg_G_Counter * 0.1F;
                if(f10 <= 0.3F)
                    f10 = 0.3F;
                if(reference.getOverload() < 0.0F && neg_G_Counter >= 0.0F)
                {
                    neg_G_Counter += 0.03F;
                    producedDistabilisation += 10F + 5F * neg_G_Counter;
                    tmpF *= f10;
                    if(reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode() && bIsMaster && neg_G_Counter > World.Rnd().nextFloat(5F, 8F))
                        setEngineStops(reference.actor);
                } else
                if(reference.getOverload() >= 0.0F && neg_G_Counter > 0.0F)
                {
                    neg_G_Counter -= 0.015F;
                    producedDistabilisation += 10F + 5F * neg_G_Counter;
                    tmpF *= f10;
                    bFloodCarb = true;
                } else
                {
                    bFloodCarb = false;
                    neg_G_Counter = 0.0F;
                }
                overrevving();
                break;

            case 1: // '\001'
            case 2: // '\002'
                float f2 = 0.1F + 0.9F * getControlThrottle();
                if(f2 > 1.0F)
                    f2 = 1.0F;
                float f8 = engineMomentMax * (-0.5F * f2 * f2 + 1.0F * f2 + 0.5F);
                float f6;
                if(controlAfterburner)
                    f6 = w / (wWEP * f2);
                else
                    f6 = w / (wNom * f2);
                tmpF = f8 * (2.0F * f6 - 1.0F * f6 * f6);
                if(getControlThrottle() > 1.0F)
                    tmpF *= 1.0F + (getControlThrottle() - 1.0F) * 10F * (engineBoostFactor - 1.0F);
                overrevving();
                break;

            case 4: // '\004'
                float f3 = 0.1F + 0.9F * getControlThrottle();
                if(f3 > 1.0F)
                    f3 = 1.0F;
                float f9 = engineMomentMax * (-0.5F * f3 * f3 + 1.0F * f3 + 0.5F);
                float f7;
                if(controlAfterburner)
                {
                    f7 = w / (wWEP * f3);
                    if(f3 >= 0.95F)
                        bFullT = true;
                    else
                        bFullT = false;
                } else
                {
                    f7 = w / (wNom * f3);
                    bFullT = false;
                    if((reference.actor instanceof SPITFIRE5B) && f3 >= 0.95F)
                        bFullT = true;
                }
                tmpF = f9 * (2.0F * f7 - 1.0F * f7 * f7);
                if(getControlThrottle() > 1.0F)
                    tmpF *= 1.0F + (getControlThrottle() - 1.0F) * 10F * (engineBoostFactor - 1.0F);
                float f11 = getControlThrottle() - neg_G_Counter * 0.2F;
                if(f11 <= 0.0F)
                    f11 = 0.1F;
                if(reference.getOverload() < 0.0F && neg_G_Counter >= 0.0F)
                {
                    neg_G_Counter += 0.03F;
                    if(bFullT && neg_G_Counter < 0.5F)
                    {
                        producedDistabilisation += 15F + 5F * neg_G_Counter;
                        tmpF *= 0.52F - neg_G_Counter;
                    } else
                    if(bFullT && neg_G_Counter >= 0.5F && neg_G_Counter <= 0.8F)
                    {
                        neg_G_Counter = 0.51F;
                        bFloodCarb = false;
                    } else
                    if(bFullT && neg_G_Counter > 0.8F)
                    {
                        neg_G_Counter -= 0.045F;
                        producedDistabilisation += 10F + 5F * neg_G_Counter;
                        tmpF *= f11;
                        bFloodCarb = true;
                    } else
                    {
                        producedDistabilisation += 10F + 5F * neg_G_Counter;
                        tmpF *= f11;
                        if(reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode() && bIsMaster && neg_G_Counter > World.Rnd().nextFloat(7.5F, 9.5F))
                            setEngineStops(reference.actor);
                    }
                } else
                if(reference.getOverload() >= 0.0F && neg_G_Counter > 0.0F)
                {
                    neg_G_Counter -= 0.03F;
                    if(!bFullT)
                    {
                        producedDistabilisation += 10F + 5F * neg_G_Counter;
                        tmpF *= f11;
                    }
                    bFloodCarb = true;
                } else
                {
                    neg_G_Counter = 0.0F;
                    bFloodCarb = false;
                }
                overrevving();
                break;
            }
            if(controlAfterburner)
                if(afterburnerType == 1)
                {
                    if(controlThrottle > 1.0F && reference.M.nitro > 0.0F)
                        tmpF *= engineAfterburnerBoostFactor;
                } else
                if(afterburnerType == 8 || afterburnerType == 7)
                {
                    if(controlCompressor < compressorMaxStep)
                        tmpF *= engineAfterburnerBoostFactor;
                } else
                {
                    tmpF *= engineAfterburnerBoostFactor;
                }
            if(engineDamageAccum > 0.0F)
                engineDamageAccum -= 0.01F;
            if(engineDamageAccum < 0.0F)
                engineDamageAccum = 0.0F;
            if(tmpF < 0.0F)
                tmpF = Math.max(tmpF, -0.8F * w * _1_wMax * engineMomentMax);
            return tmpF;
        }
        tmpF = -1500F * w * _1_wMax * engineMomentMax;
        if(stage == 8)
            w = 0.0F;
        return tmpF;
    }

    private float getDistabilisationMultiplier()
    {
        if(engineMoment < 0.0F)
            return 1.0F;
        float f = 1.0F + World.Rnd().nextFloat(-1F, 0.1F) * getDistabilisationAmplitude();
        if(f < 0.0F && w < 0.5F * (wMax + wMin))
            return 0.0F;
        else
            return f;
    }

    public float getDistabilisationAmplitude()
    {
        if(getCylindersOperable() > 2)
        {
            float f = 1.0F - getCylindersRatio();
            return engineDistAM * w * w + engineDistBM * w + engineDistCM + 9.25F * f * f + producedDistabilisation;
        } else
        {
            return 11.25F;
        }
    }

    private float getCompressorMultiplier(float f)
    {
        float f24 = controlThrottle;
        if(f24 > 1.0F)
            f24 = 1.0F;
        float f18;
        switch(propAngleDeviceType)
        {
        case 1: // '\001'
        case 2: // '\002'
        case 7: // '\007'
        case 8: // '\b'
            f18 = getATA(toRPM(propAngleDeviceMinParam + (propAngleDeviceMaxParam - propAngleDeviceMinParam) * f24));
            break;

        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        default:
            f18 = compressorRPMtoWMaxATA * (0.55F + 0.45F * f24);
            break;
        }
        coolMult = 1.0F;
        compressorManifoldThreshold = f18;
        switch(compressorType)
        {
        case 0: // '\0'
            float f1 = Atmosphere.pressure(reference.getAltitude()) + 0.5F * Atmosphere.density(reference.getAltitude()) * reference.getSpeed() * reference.getSpeed();
            float f25 = f1 / Atmosphere.P0();
            coolMult = f25;
            return f25;

        case 1: // '\001'
            float f2 = pressureExtBar;
            if((!bHasCompressorControl || !reference.isPlayers() || !(reference instanceof RealFlightModel) || !((RealFlightModel)reference).isRealMode() || !World.cur().diffCur.ComplexEManagement || fastATA) && (reference.isTick(128, 0) || fastATA))
            {
                compressorStepFound = false;
                controlCompressor = 0;
            }
            float f29 = -1F;
            float f32 = -1F;
            int i = -1;
            float f26;
            if(fastATA)
            {
                for(controlCompressor = 0; controlCompressor <= compressorMaxStep; controlCompressor++)
                {
                    compressorManifoldThreshold = f18;
                    float f5 = compressorPressure[controlCompressor];
                    float f12 = compressorRPMtoWMaxATA / f5;
                    float f36 = 1.0F;
                    float f41 = 1.0F;
                    if(f2 > f5)
                    {
                        float f46 = 1.0F - f5;
                        if(f46 < 0.0001F)
                            f46 = 0.0001F;
                        float f52 = 1.0F - f2;
                        float f57 = 1.0F;
                        for(int k = 1; k <= controlCompressor; k++)
                            if(compressorAltMultipliers[controlCompressor] >= 1.0F)
                                f57 *= compressorBaseMultipliers[controlCompressor];
                            else
                                f57 *= compressorBaseMultipliers[controlCompressor] * compressorAltMultipliers[controlCompressor];

                        f36 = f57 + (f52 / f46) * (compressorAltMultipliers[controlCompressor] - f57);
                    } else
                    {
                        f36 = compressorAltMultipliers[controlCompressor];
                    }
                    compressorManifoldPressure = (compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f2 * f12;
                    float f19 = compressorRPMtoWMaxATA / compressorManifoldPressure;
                    if(controlAfterburner && (afterburnerType != 8 && afterburnerType != 7 || controlCompressor != compressorMaxStep) && (afterburnerType != 1 || controlThrottle <= 1.0F || reference.M.nitro > 0.0F))
                    {
                        f19 *= afterburnerCompressorFactor;
                        compressorManifoldThreshold *= afterburnerCompressorFactor;
                    }
                    compressor2ndThrottle = f19;
                    if(compressor2ndThrottle > 1.0F)
                        compressor2ndThrottle = 1.0F;
                    compressorManifoldPressure *= compressor2ndThrottle;
                    compressor1stThrottle = f18 / compressorRPMtoWMaxATA;
                    if(compressor1stThrottle > 1.0F)
                        compressor1stThrottle = 1.0F;
                    compressorManifoldPressure *= compressor1stThrottle;
                    f41 = (f36 * compressorManifoldPressure) / compressorManifoldThreshold;
                    if(controlAfterburner && (afterburnerType == 8 || afterburnerType == 7) && controlCompressor == compressorMaxStep)
                    {
                        if(f41 / engineAfterburnerBoostFactor > f29)
                        {
                            f29 = f41;
                            i = controlCompressor;
                        }
                        continue;
                    }
                    if(f41 > f29)
                    {
                        f29 = f41;
                        i = controlCompressor;
                    }
                }

                f26 = f29;
                controlCompressor = i;
            } else
            {
                float f37 = f18;
                if(controlAfterburner)
                    f37 *= afterburnerCompressorFactor;
                do
                {
                    float f6 = compressorPressure[controlCompressor];
                    float f42 = 1.0F;
                    float f47 = 1.0F;
                    if(f2 > f6)
                    {
                        float f53 = 1.0F - f6;
                        if(f53 < 0.0001F)
                            f53 = 0.0001F;
                        float f58 = 1.0F - f2;
                        float f62 = 1.0F;
                        for(int l = 1; l <= controlCompressor; l++)
                            if(compressorAltMultipliers[controlCompressor] >= 1.0F)
                                f62 *= compressorBaseMultipliers[controlCompressor];
                            else
                                f62 *= compressorBaseMultipliers[controlCompressor] * compressorAltMultipliers[controlCompressor];

                        f42 = f62 + (f58 / f53) * (compressorAltMultipliers[controlCompressor] - f62);
                        f47 = f42;
                    } else
                    {
                        f42 = compressorAltMultipliers[controlCompressor];
                        f47 = (f42 * f2 * f18 * (compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax)) / (f6 * f37);
                    }
                    if(f47 > f29)
                    {
                        f29 = f47;
                        f32 = f42;
                        i = controlCompressor;
                    }
                    if(!compressorStepFound)
                    {
                        controlCompressor++;
                        if(controlCompressor == compressorMaxStep + 1)
                            compressorStepFound = true;
                    }
                } while(!compressorStepFound);
                if(i < 0)
                    i = 0;
                controlCompressor = i;
                float f7 = compressorPressure[controlCompressor];
                float f14 = compressorRPMtoWMaxATA / f7;
                compressorManifoldPressure = (compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f2 * f14;
                float f20 = compressorRPMtoWMaxATA / compressorManifoldPressure;
                if(controlAfterburner && (afterburnerType != 8 && afterburnerType != 7 || controlCompressor != compressorMaxStep) && (afterburnerType != 1 || controlThrottle <= 1.0F || reference.M.nitro > 0.0F))
                {
                    f20 *= afterburnerCompressorFactor;
                    compressorManifoldThreshold *= afterburnerCompressorFactor;
                }
                if(fastATA)
                    compressor2ndThrottle = f20;
                else
                    compressor2ndThrottle -= 3F * f * (compressor2ndThrottle - f20);
                if(compressor2ndThrottle > 1.0F)
                    compressor2ndThrottle = 1.0F;
                compressorManifoldPressure *= compressor2ndThrottle;
                compressor1stThrottle = f18 / compressorRPMtoWMaxATA;
                if(compressor1stThrottle > 1.0F)
                    compressor1stThrottle = 1.0F;
                compressorManifoldPressure *= compressor1stThrottle;
                f26 = compressorManifoldPressure / compressorManifoldThreshold;
                coolMult = f26;
                f26 *= f32;
            }
            if(w <= 20F && w < 150F)
                compressorManifoldPressure = Math.min(compressorManifoldPressure, f2 * (0.4F + (w - 20F) * 0.04F));
            if(w < 20F)
                compressorManifoldPressure = f2 * (1.0F - w * 0.03F);
            if(mixerType == 1 && stage == 6)
                compressorManifoldPressure *= getMixMultiplier();
            return f26;

        case 2: // '\002'
            float f3 = pressureExtBar;
            if((!bHasCompressorControl || !reference.isPlayers() || !(reference instanceof RealFlightModel) || !((RealFlightModel)reference).isRealMode() || !World.cur().diffCur.ComplexEManagement || fastATA) && (reference.isTick(128, 0) || fastATA))
            {
                compressorStepFound = false;
                controlCompressor = 0;
            }
            float f30 = -1F;
            int j = -1;
            float f27;
            if(fastATA)
            {
                float f38 = 0.0F;
                float f43 = 0.0F;
                for(controlCompressor = 0; controlCompressor <= compressorMaxStep; controlCompressor++)
                {
                    compressorManifoldThreshold = f18;
                    float f8 = compressorPressure[controlCompressor];
                    float f15 = compressorRPMtoWMaxATA / f8;
                    float f48 = 1.0F;
                    float f54 = 1.0F;
                    float f59 = 1.0F;
                    float f63 = 1.0F - f8;
                    float f65 = 1.0F - f3;
                    if(f3 > f8)
                    {
                        if(f63 < 0.0001F)
                            f63 = 0.0001F;
                        for(int i1 = 1; i1 <= controlCompressor; i1++)
                            if(compressorAltMultipliers[controlCompressor] >= 1.0F)
                                f59 *= compressorBaseMultipliers[controlCompressor];
                            else
                                f59 *= compressorBaseMultipliers[controlCompressor] * compressorAltMultipliers[controlCompressor];

                        f48 = f59 + (f65 / f63) * (compressorAltMultipliers[controlCompressor] - f59);
                    } else
                    {
                        f48 = compressorAltMultipliers[controlCompressor];
                    }
                    compressorManifoldPressure = (compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f3 * f15;
                    float f21 = compressorRPMtoWMaxATA / compressorManifoldPressure;
                    if(controlAfterburner && (afterburnerType != 1 || controlThrottle <= 1.0F || reference.M.nitro > 0.0F))
                    {
                        f21 *= afterburnerCompressorFactor;
                        compressorManifoldThreshold *= afterburnerCompressorFactor;
                    }
                    compressor2ndThrottle = f21;
                    if(compressor2ndThrottle > 1.0F)
                        compressor2ndThrottle = 1.0F;
                    compressorManifoldPressure *= compressor2ndThrottle;
                    if(controlCompressor == 0)
                    {
                        f43 = f48;
                        f38 = compressor2ndThrottle;
                    }
                    compressor1stThrottle = f18 / compressorRPMtoWMaxATA;
                    if(compressor1stThrottle > 1.0F)
                        compressor1stThrottle = 1.0F;
                    compressorManifoldPressure *= compressor1stThrottle;
                    f54 = (f48 * compressorManifoldPressure) / compressorManifoldThreshold;
                    if((float)controlCompressor == 1.0F && f38 == 1.0F && f21 < 1.0F)
                    {
                        float f67 = compressorPressure[1] / (compressorPressure[0] - compressorPressure[1]);
                        f54 *= 1.0F + (f43 / f48 - 1.0F) * (f38 / f21 - 1.0F) * f67;
                    }
                    if(f54 > f30)
                    {
                        f30 = f54;
                        j = controlCompressor;
                    }
                }

                f27 = f30;
                controlCompressor = j;
            } else
            {
                float f39 = f18;
                if(controlAfterburner)
                    f39 *= afterburnerCompressorFactor;
                float f44 = 1.0F;
                float f49 = 1.0F - f3;
                float f55 = compressorPressure[0];
                float f60 = (f3 * (compressorPAt0 + (1.0F - compressorPAt0)) * w * _1_wMax * compressorRPMtoWMaxATA) / f55 + 0.001F;
                float f64 = 1.0F;
                if(f60 < f39 && compressorMaxStep == 1)
                {
                    controlCompressor = 1;
                    float f9 = compressorPressure[1];
                    float f68 = 1.0F - f9;
                    if(f68 < 0.0001F)
                        f68 = 0.0001F;
                    float f70 = (f3 * (compressorPAt0 + (1.0F - compressorPAt0)) * w * _1_wMax * compressorRPMtoWMaxATA) / f9 + 0.001F;
                    if(f70 > f39)
                    {
                        if(compressorAltMultipliers[1] >= 1.0F)
                            f44 *= compressorBaseMultipliers[1];
                        else
                            f44 *= compressorBaseMultipliers[1] * compressorAltMultipliers[1];
                        f64 = f44 + (f49 / f68) * (compressorAltMultipliers[1] - f44);
                        f64 += ((compressorAltMultipliers[0] - f64) * (f70 - 1.0F)) / ((f55 * f39) / f9 - 1.0F);
                    } else
                    {
                        f64 = compressorAltMultipliers[controlCompressor];
                    }
                } else
                {
                    controlCompressor = 0;
                    float f69 = 1.0F - f55;
                    if(f69 < 0.0001F)
                        f69 = 0.0001F;
                    f64 = 1.0F + (f49 / f69) * (compressorAltMultipliers[0] - 1.0F);
                }
                float f34 = f64;
                if(j < 0)
                    j = 0;
                float f10 = compressorPressure[controlCompressor];
                float f16 = compressorRPMtoWMaxATA / f10;
                compressorManifoldPressure = (compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f3 * f16;
                float f22 = compressorRPMtoWMaxATA / compressorManifoldPressure;
                if(controlAfterburner && (afterburnerType != 1 || controlThrottle <= 1.0F || reference.M.nitro > 0.0F))
                {
                    f22 *= afterburnerCompressorFactor;
                    compressorManifoldThreshold *= afterburnerCompressorFactor;
                }
                compressor2ndThrottle = f22;
                if(compressor2ndThrottle > 1.0F)
                    compressor2ndThrottle = 1.0F;
                compressorManifoldPressure *= compressor2ndThrottle;
                compressor1stThrottle = f18 / compressorRPMtoWMaxATA;
                if(compressor1stThrottle > 1.0F)
                    compressor1stThrottle = 1.0F;
                compressorManifoldPressure *= compressor1stThrottle;
                f27 = compressorManifoldPressure / compressorManifoldThreshold;
                coolMult = f27;
                f27 *= f34;
            }
            if(w <= 20F && w < 150F)
                compressorManifoldPressure = Math.min(compressorManifoldPressure, f3 * (0.4F + (w - 20F) * 0.04F));
            if(w < 20F)
                compressorManifoldPressure = f3 * (1.0F - w * 0.03F);
            return f27;

        case 3: // '\003'
            float f4 = pressureExtBar;
            controlCompressor = 0;
            float f35 = -1F;
            float f11 = compressorPressure[controlCompressor];
            float f17 = compressorRPMtoWMaxATA / f11;
            float f40 = 1.0F;
            if(f4 > f11)
            {
                float f50 = 1.0F - f11;
                if(f50 < 0.0001F)
                    f50 = 0.0001F;
                float f56 = 1.0F - f4;
                if(f56 < 0.0F)
                    f56 = 0.0F;
                float f61 = 1.0F;
                f40 = f61 + (f56 / f50) * (compressorAltMultipliers[controlCompressor] - f61);
            } else
            {
                f40 = compressorAltMultipliers[controlCompressor];
            }
            f35 = f40;
            f17 = compressorRPMtoWMaxATA / f11;
            if(f4 < f11)
                f4 = 0.1F * f4 + 0.9F * f11;
            float f51 = f4 * f17;
            compressorManifoldPressure = (compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f51;
            float f23 = compressorRPMtoWMaxATA / compressorManifoldPressure;
            if(fastATA)
                compressor2ndThrottle = f23;
            else
                compressor2ndThrottle -= 3F * f * (compressor2ndThrottle - f23);
            if(compressor2ndThrottle > 1.0F)
                compressor2ndThrottle = 1.0F;
            compressorManifoldPressure *= compressor2ndThrottle;
            compressor1stThrottle = f18 / compressorRPMtoWMaxATA;
            if(compressor1stThrottle > 1.0F)
                compressor1stThrottle = 1.0F;
            compressorManifoldPressure *= compressor1stThrottle;
            float f28 = compressorManifoldPressure / compressorManifoldThreshold;
            f28 *= f35;
            if(w <= 20F && w < 150F)
                compressorManifoldPressure = Math.min(compressorManifoldPressure, f4 * (0.4F + (w - 20F) * 0.04F));
            if(w < 20F)
                compressorManifoldPressure = f4 * (1.0F - w * 0.03F);
            return f28;
        }
        return 1.0F;
    }

    private float getMagnetoMultiplier()
    {
        switch(controlMagneto)
        {
        case 0: // '\0'
            return 0.0F;

        case 1: // '\001'
            return bMagnetos[0] ? 0.87F : 0.0F;

        case 2: // '\002'
            return bMagnetos[1] ? 0.87F : 0.0F;

        case 3: // '\003'
            float f = 0.0F;
            f += bMagnetos[0] ? 0.87F : 0.0F;
            f += bMagnetos[1] ? 0.87F : 0.0F;
            if(f > 1.0F)
                f = 1.0F;
            return f;
        }
        return 1.0F;
    }

    private float getMixMultiplier()
    {
        float f4 = 0.0F;
        switch(mixerType)
        {
        case 0: // '\0'
            return 1.0F;

        case 1: // '\001'
            if(controlMix == 1.0F)
            {
                if(bFloodCarb)
                    reference.AS.setSootState(reference.actor, number, 1);
                else
                    reference.AS.setSootState(reference.actor, number, 0);
                return 1.0F;
            }
            // fall through

        case 2: // '\002'
            if(reference.isPlayers() && (reference instanceof RealFlightModel) && ((RealFlightModel)reference).isRealMode())
            {
                if(!World.cur().diffCur.ComplexEManagement)
                    return 1.0F;
                float f = mixerLowPressureBar * controlMix;
                if(f < pressureExtBar - f4)
                {
                    if(f < 0.03F)
                        setEngineStops(reference.actor);
                    if(bFloodCarb)
                        reference.AS.setSootState(reference.actor, number, 1);
                    else
                        reference.AS.setSootState(reference.actor, number, 0);
                    if(f > (pressureExtBar - f4) * 0.25F)
                    {
                        return 1.0F;
                    } else
                    {
                        float f2 = f / ((pressureExtBar - f4) * 0.25F);
                        return f2;
                    }
                }
                if(f > pressureExtBar)
                {
                    producedDistabilisation += 0.0F + 35F * (1.0F - (pressureExtBar + f4) / (f + 0.0001F));
                    reference.AS.setSootState(reference.actor, number, 1);
                    float f3 = (pressureExtBar + f4) / (f + 0.0001F);
                    return f3;
                }
                if(bFloodCarb)
                    reference.AS.setSootState(reference.actor, number, 1);
                else
                    reference.AS.setSootState(reference.actor, number, 0);
                return 1.0F;
            }
            float f1 = mixerLowPressureBar * controlMix;
            if(f1 < pressureExtBar - f4 && f1 < 0.03F)
                setEngineStops(reference.actor);
            return 1.0F;

        default:
            return 1.0F;
        }
    }

    private float getStageMultiplier()
    {
        return stage != 6 ? 0.0F : 1.0F;
    }

    public void setFricCoeffT(float f)
    {
        fricCoeffT = f;
    }

    private float getFrictionMoment(float f)
    {
		float f1 = 0.0F;
		if(type == 10) //TODO: Turboprops spin-down like jets
		{
			return getJetFrictionMoment(f);
		}
		else
			if(!bPropHit && (bIsInoperable || stage == 0 || controlMagneto == 0))
			{
				/*TODO:   ok,this is spindown stuff :)    */
				if((((Interpolate) (reference)).actor instanceof BF_109) || (((Interpolate) (reference)).actor instanceof BF_110) || (((Interpolate) (reference)).actor instanceof JU_87) || (((Interpolate) (reference)).actor instanceof JU_88) || (((Interpolate) (reference)).actor instanceof JU_88NEW) || ((((Interpolate) (reference)).actor instanceof FW_190) && type == 0))
				{
					fricCoeffT += 0.09F * f;     /*  this value determines how fast will propeller loose speed when engine is turned off   */
					if(fricCoeffT > 0.25F)    /*   this one affects windmilling...lower the valuse more windmilling...and oposite   */
						fricCoeffT = 0.25F;
				} else
					if((((Interpolate) (reference)).actor instanceof P_51) || (((Interpolate) (reference)).actor instanceof P_47) || (((Interpolate) (reference)).actor instanceof P_38) || (((Interpolate) (reference)).actor instanceof P_39) || (((Interpolate) (reference)).actor instanceof P_40) || (((Interpolate) (reference)).actor instanceof P_40SUKAISVOLOCH) || (((Interpolate) (reference)).actor instanceof SPITFIRE) || (((Interpolate) (reference)).actor instanceof Hurricane) || ((((Interpolate) (reference)).actor instanceof FW_190) && type == 1) || type == 1)
					{
						fricCoeffT += 0.08F * f;
						if(fricCoeffT > 0.2F)
							fricCoeffT = 0.2F;
					} else
						if(type == 9) //TODO: Spin-down behaviour for rotaries. Great rotating inertial mass, hence longer spin-down
						{
							fricCoeffT += 0.07F * f;
							if(fricCoeffT > 0.05F)
								fricCoeffT = 0.05F;
						} else
						{
							fricCoeffT += 0.09F * f;
							if(fricCoeffT > 0.3F)
								fricCoeffT = 0.3F;    
						}
				float f2 = w * _1_wMax;
				f1 = (-fricCoeffT * (6F + 3.8F * f2) * (propI + engineI)) / f;
				float f3 = (-0.99F * w * (propI + engineI)) / f;
				if(f1 < f3)
					f1 = f3;
			} else if(bPropHit && (bIsInoperable || stage == 0 || controlMagneto == 0))
			{
				fricCoeffT += 0.2F * f;
				if(fricCoeffT > 2.0F)
					fricCoeffT = 2.0F;
				float f2 = w * _1_wMax;
				f1 = (-fricCoeffT * (6F + 3.8F * f2) * (propI + engineI)) / f;
				float f3 = (-0.99F * w * (propI + engineI)) / f;
				if(f1 < f3)
					f1 = f3;
			} else
			{
				fricCoeffT = 0.0F;
			}
		if(stage == 0 && w > wMaxAllowed)
		{
			doSetEngineStuck();      /*   I've put this in just so you can't overrev engine with windmilling   */
		}
		return f1;
    }

    private float getJetFrictionMoment(float f)
    {
        float f1 = 0.0F;
        if(bIsInoperable || stage == 0)
            f1 = (-0.002F * w * (propI + engineI)) / f;
        return f1;
    }

    public Vector3f getEngineForce()
    {
        return engineForce;
    }

    public Vector3f getEngineTorque()
    {
        return engineTorque;
    }

    public Vector3d getEngineGyro()
    {
        tmpV3d1.cross(reference.getW(), propIW);
        return tmpV3d1;
    }

    public float getEngineMomentRatio()
    {
        return engineMoment / engineMomentMax;
    }

    public boolean isPropAngleDeviceOperational()
    {
        return bIsAngleDeviceOperational;
    }

    public float getCriticalW()
    {
        return wMaxAllowed;
    }

    public float getPropPhiMin()
    {
        return propPhiMin;
    }

    public int getAfterburnerType()
    {
        return afterburnerType;
    }

    private float toRadianPerSecond(float f)
    {
        return (f * 3.141593F * 2.0F) / 60F;
    }

    private float toRPM(float f)
    {
        return (f * 60F) / 2.0F / 3.141593F;
    }

  //TODO: Altered by |ZUTI|: from private to public.
  	public float getKforH(float f, float f1, float f2)
  	{
  		float f3 = (Atmosphere.density(f2) * (f1 * f1)) / (Atmosphere.density(0.0F) * (f * f));
  		if(type != 2)
  			f3 = (f3 * kV(f)) / kV(f1);
  		return f3;
  	}

    private float kV(float f)
    {
        return 1.0F - 0.0032F * f;
    }

    public final void setAfterburnerType(int i)
    {
        afterburnerType = i;
    }

    public final void setPropReductorValue(float f)
    {
        propReductor = f;
    }

    public void replicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(controlMagneto | stage << 4);
        netmsgguaranted.writeByte(cylinders);
        netmsgguaranted.writeByte(cylindersOperable);
        netmsgguaranted.writeByte((int)(255F * readyness));
        netmsgguaranted.writeByte((int)(255F * ((propPhi - propPhiMin) / (propPhiMax - propPhiMin))));
        netmsgguaranted.writeFloat(w);
    }

    public void replicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        stage = (i & 0xf0) >> 4;
        controlMagneto = i & 0xf;
        cylinders = netmsginput.readUnsignedByte();
        cylindersOperable = netmsginput.readUnsignedByte();
        readyness = (float)netmsginput.readUnsignedByte() / 255F;
        propPhi = ((float)netmsginput.readUnsignedByte() / 255F) * (propPhiMax - propPhiMin) + propPhiMin;
        w = netmsginput.readFloat();
    }



}
