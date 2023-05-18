/*4.10.1 class*/
package com.maddox.il2.fm;
import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.BI_1;
import com.maddox.il2.objects.air.BI_6;
import com.maddox.il2.objects.air.GLADIATOR;
import com.maddox.il2.objects.air.MXY_7;
import com.maddox.il2.objects.air.P_38;
import com.maddox.il2.objects.air.P_51;
import com.maddox.il2.objects.air.P_63C;
import com.maddox.il2.objects.air.SM79;
import com.maddox.il2.objects.air.SPITFIRE5B;
import com.maddox.il2.objects.air.SPITFIRE8;
import com.maddox.il2.objects.air.SPITFIRE8CLP;
import com.maddox.il2.objects.air.SPITFIRE9;
import com.maddox.il2.objects.air.YAK_3;
import com.maddox.il2.objects.air.YAK_3P;
import com.maddox.il2.objects.air.YAK_9M;
import com.maddox.il2.objects.air.YAK_9U;
import com.maddox.il2.objects.air.YAK_9UT;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class Motor extends FMMath
{
	//TODO: Modified by |ZUTI|: changed all private variables to protected
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
	public static int heatStringID = -1;
	public FmSounds isnd = null;
	public FlightModel reference = null;
	public static boolean bTFirst;
	public String soundName = null;
	public String startStopName = null;
	public String propName = null;
	public int number = 0;
	public int type = 0;
	public int cylinders = 12;
	public float engineMass = 900.0F;
	public float wMin = 20.0F;
	public float wNom = 180.0F;
	public float wMax = 200.0F;
	public float wWEP = 220.0F;
	public float wMaxAllowed = 250.0F;
	public int wNetPrev = 0;
	public float engineMoment = 0.0F;
	public float engineMomentMax = 0.0F;
	public float engineBoostFactor = 1.0F;
	public float engineAfterburnerBoostFactor = 1.0F;
	public float engineDistAM = 0.0F;
	public float engineDistBM = 0.0F;
	public float engineDistCM = 0.0F;
	public float producedDistabilisation;
	public boolean bRan = false;
	public Point3f enginePos = new Point3f();
	public Vector3f engineVector = new Vector3f();
	public Vector3f engineForce = new Vector3f();
	public Vector3f engineTorque = new Vector3f();
	public float engineDamageAccum = 0.0F;
	public float _1_wMaxAllowed = 1.0F / wMaxAllowed;
	public float _1_wMax = 1.0F / wMax;
	public float RPMMin = 200.0F;
	public float RPMNom = 2000.0F;
	public float RPMMax = 2200.0F;
	public float Vopt = 90.0F;
	public float pressureExtBar;
	public double momForFuel = 0.0;
	public double addVflow = 0.0;
	public double addVside = 0.0;
	public Point3f propPos = new Point3f();
	public float propReductor = 1.0F;
	public int propAngleDeviceType = 0;
	public float propAngleDeviceMinParam = 0.0F;
	public float propAngleDeviceMaxParam = 0.0F;
	public float propAngleDeviceAfterburnerParam = -999.9F;
	public int propDirection = 0;
	public float propDiameter = 3.0F;
	public float propMass = 30.0F;
	public float propI = 1.0F;
	public Vector3d propIW = new Vector3d();
	public float propSEquivalent = 1.0F;
	public float propr = 1.125F;
	public float propPhiMin = (float)Math.toRadians(10.0);
	public float propPhiMax = (float)Math.toRadians(29.0);
	public float propPhi = (float)Math.toRadians(11.0);
	public float propPhiW;
	public float propAoA;
	public float propAoA0 = (float)Math.toRadians(11.0);
	public float propAoACrit = (float)Math.toRadians(16.0);
	public float propAngleChangeSpeed = 0.1F;
	public float propForce = 0.0F;
	public float propMoment = 0.0F;
	public float propTarget = 0.0F;
	public int mixerType = 0;
	public float mixerLowPressureBar = 0.0F;
	public float horsePowers = 1200.0F;
	public float thrustMax = 10.7F;
	public int cylindersOperable = 12;
	public float engineI = 1.0F;
	public float engineAcceleration = 1.0F;
	public boolean[] bMagnetos = {true, true};
	public boolean bIsAutonomous = true;
	public boolean bIsMaster = true;
	public boolean bIsStuck = false;
	public boolean bIsInoperable = false;
	public boolean bIsAngleDeviceOperational = true;
	public boolean isPropAngleDeviceHydroOperable = true;
	public int engineCarburetorType = 0;
	public float FuelConsumptionP0 = 0.4F;
	public float FuelConsumptionP05 = 0.24F;
	public float FuelConsumptionP1 = 0.28F;
	public float FuelConsumptionPMAX = 0.3F;
	public int compressorType = 0;
	public int compressorMaxStep = 0;
	public float compressorPMax = 1.0F;
	public float compressorManifoldPressure = 1.0F;
	public float[] compressorAltitudes = null;
	public float[] compressorPressure = null;
	public float[] compressorAltMultipliers = null;
	public float compressorRPMtoP0 = 1500.0F;
	public float compressorRPMtoCurvature = -30.0F;
	public float compressorRPMtoPMax = 2600.0F;
	public float compressorRPMtoWMaxATA = 1.45F;
	public float compressorSpeedManifold = 0.2F;
	public float[] compressorRPM = new float[16];
	public float[] compressorATA = new float[16];
	public int nOfCompPoints = 0;
	public boolean compressorStepFound = false;
	public float compressorManifoldThreshold = 1.0F;
	public float afterburnerCompressorFactor = 1.0F;
	public float _1_P0 = 1.0F / Atmosphere.P0();
	public float compressor1stThrottle = 1.0F;
	public float compressor2ndThrottle = 1.0F;
	public float compressorPAt0 = 0.3F;
	public int afterburnerType = 0;
	public boolean afterburnerChangeW = false;
	public int stage = 0;
	public int oldStage = 0;
	public long timer = 0L;
	public long given = 4611686018427387903L;
	public float rpm = 0.0F;
	public float w = 0.0F;
	public float aw = 0.0F;
	public float oldW = 0.0F;
	public float readyness = 1.0F;
	public float oldReadyness = 1.0F;
	public float radiatorReadyness = 1.0F;
	public float rearRush;
	public float tOilIn = 0.0F;
	public float tOilOut = 0.0F;
	public float tWaterOut = 0.0F;
	public float tCylinders = 0.0F;
	public float tWaterCritMin;
	public float tWaterCritMax;
	public float tOilCritMin;
	public float tOilCritMax;
	public float tWaterMaxRPM;
	public float tOilOutMaxRPM;
	public float tOilInMaxRPM;
	public float tChangeSpeed;
	public float timeOverheat;
	public float timeUnderheat;
	public float timeCounter;
	public float oilMass = 90.0F;
	public float waterMass = 90.0F;
	public float Ptermo;
	public float R_air;
	public float R_oil;
	public float R_water;
	public float R_cyl_oil;
	public float R_cyl_water;
	public float C_eng;
	public float C_oil;
	public float C_water;
	public boolean bHasThrottleControl = true;
	public boolean bHasAfterburnerControl = true;
	public boolean bHasPropControl = true;
	public boolean bHasRadiatorControl = true;
	public boolean bHasMixControl = true;
	public boolean bHasMagnetoControl = true;
	public boolean bHasExtinguisherControl = false;
	public boolean bHasCompressorControl = false;
	public boolean bHasFeatherControl = false;
	public int extinguishers = 0;
	public float controlThrottle = 0.0F;
	public float controlRadiator = 0.0F;
	public boolean controlAfterburner = false;
	public float controlProp = 1.0F;
	public boolean bControlPropAuto = true;
	public float controlMix = 1.0F;
	public int controlMagneto = 0;
	public int controlCompressor = 0;
	public int controlFeather = 0;
	public double zatizeni;
	public float coolMult;
	public int controlPropDirection;
	public boolean bWepRpmInLowGear;
	public boolean fastATA = false;
	public Vector3f old_engineForce = new Vector3f();
	public Vector3f old_engineTorque = new Vector3f();
	public float updateStep = 0.12F;
	public float updateLast = 0.0F;
	public float fricCoeffT = 1.0F;
	public static Vector3f tmpV3f = new Vector3f();
	public static Vector3d tmpV3d1 = new Vector3d();
	public static Vector3d tmpV3d2 = new Vector3d();
	public static Point3f safeloc = new Point3f();
	public static Point3d safeLoc = new Point3d();
	public static Vector3f safeVwld = new Vector3f();
	public static Vector3f safeVflow = new Vector3f();
	public static boolean tmpB;
	public static float tmpF;
	public int engineNoFuelHUDLogId = -1;
	private static final boolean DEBUG = false;

	//TODO: new variables in 410
	//--------------------------------
	public float neg_G_Counter = 0.0F;
	public boolean bFullT = false;
	public boolean bFloodCarb = false;
	//--------------------------------
	
	public void load(FlightModel flightmodel, String string, String string_0_, int i)
	{
		reference = flightmodel;
		number = i;
		SectFile sectfile = FlightModelMain.sectFile(string);
		resolveFromFile(sectfile, "Generic");
		resolveFromFile(sectfile, string_0_);
		calcAfterburnerCompressorFactor();
		if (type == 0 || type == 1 || type == 7)
			initializeInline(flightmodel.Vmax);
		if (type == 2)
			initializeJet(flightmodel.Vmax);
	}
	
	private void resolveFromFile(SectFile sectfile, String string)
	{
		soundName = sectfile.get(string, "SoundName", soundName);
		propName = sectfile.get(string, "PropName", propName);
		startStopName = sectfile.get(string, "StartStopName", startStopName);
		Aircraft.debugprintln(reference.actor, ("Resolving submodel " + string + " from file '" + sectfile.toString() + "'...."));
		String string_1_ = sectfile.get(string, "Type");
		if (string_1_ != null)
		{
			if (string_1_.endsWith("Inline"))
				type = 0;
			else if (string_1_.endsWith("Radial"))
				type = 1;
			else if (string_1_.endsWith("Jet"))
				type = 2;
			else if (string_1_.endsWith("RocketBoost"))
				type = 4;
			else if (string_1_.endsWith("Rocket"))
				type = 3;
			else if (string_1_.endsWith("Tow"))
				type = 5;
			else if (string_1_.endsWith("PVRD"))
				type = 6;
			else if (string_1_.endsWith("Unknown"))
				type = 8;
			else if (string_1_.endsWith("Azure"))
				type = 8;
			else if (string_1_.endsWith("HeloI"))
				type = 7;
		}
		if (type == 0 || type == 1 || type == 7)
		{
			int i = sectfile.get(string, "Cylinders", -99999);
			if (i != -99999)
			{
				cylinders = i;
				cylindersOperable = cylinders;
			}
		}
		string_1_ = sectfile.get(string, "Direction");
		if (string_1_ != null)
		{
			if (string_1_.endsWith("Left"))
				propDirection = 0;
			else if (string_1_.endsWith("Right"))
				propDirection = 1;
		}
		float f = sectfile.get(string, "RPMMin", -99999.0F);
		if (f != -99999.0F)
		{
			RPMMin = f;
			wMin = toRadianPerSecond(RPMMin);
		}
		f = sectfile.get(string, "RPMNom", -99999.0F);
		if (f != -99999.0F)
		{
			RPMNom = f;
			wNom = toRadianPerSecond(RPMNom);
		}
		f = sectfile.get(string, "RPMMax", -99999.0F);
		if (f != -99999.0F)
		{
			RPMMax = f;
			wMax = toRadianPerSecond(RPMMax);
			_1_wMax = 1.0F / wMax;
		}
		f = sectfile.get(string, "RPMMaxAllowed", -99999.0F);
		if (f != -99999.0F)
		{
			wMaxAllowed = toRadianPerSecond(f);
			_1_wMaxAllowed = 1.0F / wMaxAllowed;
		}
		f = sectfile.get(string, "Reductor", -99999.0F);
		if (f != -99999.0F)
			propReductor = f;
		if (type == 0 || type == 1 || type == 7)
		{
			f = sectfile.get(string, "HorsePowers", -99999.0F);
			if (f != -99999.0F)
				horsePowers = f;
			int i = sectfile.get(string, "Carburetor", -99999);
			if (i != -99999)
				engineCarburetorType = i;
			f = sectfile.get(string, "Mass", -99999.0F);
			if (f != -99999.0F)
				engineMass = f;
			else
				engineMass = horsePowers * 0.6F;
		}
		else
		{
			f = sectfile.get(string, "Thrust", -99999.0F);
			if (f != -99999.0F)
				thrustMax = f * 9.81F;
		}
		f = sectfile.get(string, "BoostFactor", -99999.0F);
		if (f != -99999.0F)
			engineBoostFactor = f;
		f = sectfile.get(string, "WEPBoostFactor", -99999.0F);
		if (f != -99999.0F)
			engineAfterburnerBoostFactor = f;
		if (type == 2)
		{
			FuelConsumptionP0 = 0.075F;
			FuelConsumptionP05 = 0.075F;
			FuelConsumptionP1 = 0.1F;
			FuelConsumptionPMAX = 0.11F;
		}
		if (type == 6)
		{
			FuelConsumptionP0 = 0.835F;
			FuelConsumptionP05 = 0.835F;
			FuelConsumptionP1 = 0.835F;
			FuelConsumptionPMAX = 0.835F;
		}
		f = sectfile.get(string, "FuelConsumptionP0", -99999.0F);
		if (f != -99999.0F)
			FuelConsumptionP0 = f;
		f = sectfile.get(string, "FuelConsumptionP05", -99999.0F);
		if (f != -99999.0F)
			FuelConsumptionP05 = f;
		f = sectfile.get(string, "FuelConsumptionP1", -99999.0F);
		if (f != -99999.0F)
			FuelConsumptionP1 = f;
		f = sectfile.get(string, "FuelConsumptionPMAX", -99999.0F);
		if (f != -99999.0F)
			FuelConsumptionPMAX = f;
		int i = sectfile.get(string, "Autonomous", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bIsAutonomous = false;
			else if (i == 1)
				bIsAutonomous = true;
		}
		i = sectfile.get(string, "cThrottle", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasThrottleControl = false;
			else if (i == 1)
				bHasThrottleControl = true;
		}
		i = sectfile.get(string, "cAfterburner", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasAfterburnerControl = false;
			else if (i == 1)
				bHasAfterburnerControl = true;
		}
		i = sectfile.get(string, "cProp", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasPropControl = false;
			else if (i == 1)
				bHasPropControl = true;
		}
		i = sectfile.get(string, "cMix", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasMixControl = false;
			else if (i == 1)
				bHasMixControl = true;
		}
		i = sectfile.get(string, "cMagneto", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasMagnetoControl = false;
			else if (i == 1)
				bHasMagnetoControl = true;
		}
		i = sectfile.get(string, "cCompressor", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasCompressorControl = false;
			else if (i == 1)
				bHasCompressorControl = true;
		}
		i = sectfile.get(string, "cFeather", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasFeatherControl = false;
			else if (i == 1)
				bHasFeatherControl = true;
		}
		i = sectfile.get(string, "cRadiator", -99999);
		if (i != -99999)
		{
			if (i == 0)
				bHasRadiatorControl = false;
			else if (i == 1)
				bHasRadiatorControl = true;
		}
		i = sectfile.get(string, "Extinguishers", -99999);
		if (i != -99999)
		{
			extinguishers = i;
			if (i != 0)
				bHasExtinguisherControl = true;
			else
				bHasExtinguisherControl = false;
		}
		f = sectfile.get(string, "PropDiameter", -99999.0F);
		if (f != -99999.0F)
			propDiameter = f;
		propr = 0.5F * propDiameter * 0.75F;
		f = sectfile.get(string, "PropMass", -99999.0F);
		if (f != -99999.0F)
			propMass = f;
		propI = propMass * propDiameter * propDiameter * 0.083F;
		bWepRpmInLowGear = false;
		i = sectfile.get(string, "PropAnglerType", -99999);
		if (i != -99999)
		{
			if (i > 255)
			{
				bWepRpmInLowGear = (i & 0x100) > 1;
				i -= 256;
			}
			propAngleDeviceType = i;
		}
		f = sectfile.get(string, "PropAnglerSpeed", -99999.0F);
		if (f != -99999.0F)
			propAngleChangeSpeed = f;
		f = sectfile.get(string, "PropAnglerMinParam", -99999.0F);
		if (f != -99999.0F)
		{
			propAngleDeviceMinParam = f;
			if (propAngleDeviceType == 6 || propAngleDeviceType == 5)
				propAngleDeviceMinParam = (float)Math.toRadians((double)propAngleDeviceMinParam);
			if (propAngleDeviceType == 1 || propAngleDeviceType == 2 || propAngleDeviceType == 7 || propAngleDeviceType == 8 || propAngleDeviceType == 9)
				propAngleDeviceMinParam = toRadianPerSecond(propAngleDeviceMinParam);
		}
		f = sectfile.get(string, "PropAnglerMaxParam", -99999.0F);
		if (f != -99999.0F)
		{
			propAngleDeviceMaxParam = f;
			if (propAngleDeviceType == 6 || propAngleDeviceType == 5)
				propAngleDeviceMaxParam = (float)Math.toRadians((double)propAngleDeviceMaxParam);
			if (propAngleDeviceType == 1 || propAngleDeviceType == 2 || propAngleDeviceType == 7 || propAngleDeviceType == 8 || propAngleDeviceType == 9)
				propAngleDeviceMaxParam = toRadianPerSecond(propAngleDeviceMaxParam);
			if (propAngleDeviceAfterburnerParam == -999.9F)
				propAngleDeviceAfterburnerParam = propAngleDeviceMaxParam;
		}
		f = sectfile.get(string, "PropAnglerAfterburnerParam", -99999.0F);
		if (f != -99999.0F)
		{
			propAngleDeviceAfterburnerParam = f;
			wWEP = toRadianPerSecond(propAngleDeviceAfterburnerParam);
			if (wWEP != wMax)
				afterburnerChangeW = true;
			if (propAngleDeviceType == 6 || propAngleDeviceType == 5)
				propAngleDeviceAfterburnerParam = (float)(Math.toRadians((double)propAngleDeviceAfterburnerParam));
			if (propAngleDeviceType == 1 || propAngleDeviceType == 2 || propAngleDeviceType == 7 || propAngleDeviceType == 8 || propAngleDeviceType == 9)
				propAngleDeviceAfterburnerParam = toRadianPerSecond(propAngleDeviceAfterburnerParam);
		}
		else
			wWEP = wMax;
		f = sectfile.get(string, "PropPhiMin", -99999.0F);
		if (f != -99999.0F)
		{
			propPhiMin = (float)Math.toRadians((double)f);
			if (propPhi < propPhiMin)
				propPhi = propPhiMin;
			if (propTarget < propPhiMin)
				propTarget = propPhiMin;
		}
		f = sectfile.get(string, "PropPhiMax", -99999.0F);
		if (f != -99999.0F)
		{
			propPhiMax = (float)Math.toRadians((double)f);
			if (propPhi > propPhiMax)
				propPhi = propPhiMax;
			if (propTarget > propPhiMax)
				propTarget = propPhiMax;
		}
		f = sectfile.get(string, "PropAoA0", -99999.0F);
		if (f != -99999.0F)
			propAoA0 = (float)Math.toRadians((double)f);
		i = sectfile.get(string, "CompressorType", -99999);
		if (i != -99999)
			compressorType = i;
		f = sectfile.get(string, "CompressorPMax", -99999.0F);
		if (f != -99999.0F)
			compressorPMax = f;
		i = sectfile.get(string, "CompressorSteps", -99999);
		if (i != -99999)
		{
			compressorMaxStep = i - 1;
			if (compressorMaxStep < 0)
				compressorMaxStep = 0;
		}
		if (compressorAltitudes != null && compressorAltitudes.length == compressorMaxStep + 1)
		{
			/* empty */
		}
		compressorAltitudes = new float[compressorMaxStep + 1];
		compressorPressure = new float[compressorMaxStep + 1];
		compressorAltMultipliers = new float[compressorMaxStep + 1];
		if (compressorAltitudes.length > 0)
		{
			for (int i_2_ = 0; i_2_ < compressorAltitudes.length; i_2_++)
			{
				f = sectfile.get(string, "CompressorAltitude" + i_2_, -99999.0F);
				if (f != -99999.0F)
				{
					compressorAltitudes[i_2_] = f;
					compressorPressure[i_2_] = (Atmosphere.pressure(compressorAltitudes[i_2_]) * _1_P0);
				}
				f = sectfile.get(string, "CompressorMultiplier" + i_2_, -99999.0F);
				if (f != -99999.0F)
					compressorAltMultipliers[i_2_] = f;
			}
		}
		f = sectfile.get(string, "CompressorRPMP0", -99999.0F);
		if (f != -99999.0F)
		{
			compressorRPMtoP0 = f;
			insetrPoiInCompressorPoly(compressorRPMtoP0, 1.0F);
		}
		f = sectfile.get(string, "CompressorRPMCurvature", -99999.0F);
		if (f != -99999.0F)
			compressorRPMtoCurvature = f;
		f = sectfile.get(string, "CompressorMaxATARPM", -99999.0F);
		if (f != -99999.0F)
		{
			compressorRPMtoWMaxATA = f;
			insetrPoiInCompressorPoly(RPMMax, compressorRPMtoWMaxATA);
		}
		f = sectfile.get(string, "CompressorRPMPMax", -99999.0F);
		if (f != -99999.0F)
		{
			compressorRPMtoPMax = f;
			insetrPoiInCompressorPoly(compressorRPMtoPMax, compressorPMax);
		}
		f = sectfile.get(string, "CompressorSpeedManifold", -99999.0F);
		if (f != -99999.0F)
			compressorSpeedManifold = f;
		f = sectfile.get(string, "CompressorPAt0", -99999.0F);
		if (f != -99999.0F)
			compressorPAt0 = f;
		f = sectfile.get(string, "Voptimal", -99999.0F);
		if (f != -99999.0F)
			Vopt = f * 0.277778F;
		boolean bool = true;
		float f_3_ = 2000.0F;
		float f_4_ = 1.0F;
		int i_5_ = 0;
		while (bool)
		{
			f = sectfile.get(string, "CompressorRPM" + i_5_, -99999.0F);
			if (f != -99999.0F)
				f_3_ = f;
			else
				bool = false;
			f = sectfile.get(string, "CompressorATA" + i_5_, -99999.0F);
			if (f != -99999.0F)
				f_4_ = f;
			else
				bool = false;
			if (bool)
				insetrPoiInCompressorPoly(f_3_, f_4_);
			i_5_++;
			if (nOfCompPoints > 15 || i_5_ > 15)
				bool = false;
		}
		i = sectfile.get(string, "AfterburnerType", -99999);
		if (i != -99999)
			afterburnerType = i;
		i = sectfile.get(string, "MixerType", -99999);
		if (i != -99999)
			mixerType = i;
		f = sectfile.get(string, "MixerAltitude", -99999.0F);
		if (f != -99999.0F)
			mixerLowPressureBar = Atmosphere.pressure(f) / Atmosphere.P0();
		f = sectfile.get(string, "EngineI", -99999.0F);
		if (f != -99999.0F)
			engineI = f;
		f = sectfile.get(string, "EngineAcceleration", -99999.0F);
		if (f != -99999.0F)
			engineAcceleration = f;
		f = sectfile.get(string, "DisP0x", -99999.0F);
		if (f != -99999.0F)
		{
			float f_6_ = sectfile.get(string, "DisP0x", -99999.0F);
			f_6_ = toRadianPerSecond(f_6_);
			float f_7_ = sectfile.get(string, "DisP0y", -99999.0F);
			f_7_ *= 0.01F;
			float f_8_ = sectfile.get(string, "DisP1x", -99999.0F);
			f_8_ = toRadianPerSecond(f_8_);
			float f_9_ = sectfile.get(string, "DisP1y", -99999.0F);
			f_9_ *= 0.01F;
			float f_10_ = f_6_;
			float f_11_ = f_7_;
			float f_12_ = (f_8_ - f_6_) * (f_8_ - f_6_);
			float f_13_ = f_9_ - f_7_;
			engineDistAM = f_13_ / f_12_;
			engineDistBM = -2.0F * f_13_ * f_10_ / f_12_;
			engineDistCM = f_11_ + f_13_ * f_10_ * f_10_ / f_12_;
		}
		timeCounter = 0.0F;
		f = sectfile.get(string, "TESPEED", -99999.0F);
		if (f != -99999.0F)
			tChangeSpeed = f;
		f = sectfile.get(string, "TWATERMAXRPM", -99999.0F);
		if (f != -99999.0F)
			tWaterMaxRPM = f;
		f = sectfile.get(string, "TOILINMAXRPM", -99999.0F);
		if (f != -99999.0F)
			tOilInMaxRPM = f;
		f = sectfile.get(string, "TOILOUTMAXRPM", -99999.0F);
		if (f != -99999.0F)
			tOilOutMaxRPM = f;
		f = sectfile.get(string, "MAXRPMTIME", -99999.0F);
		if (f != -99999.0F)
			timeOverheat = f;
		f = sectfile.get(string, "MINRPMTIME", -99999.0F);
		if (f != -99999.0F)
			timeUnderheat = f;
		f = sectfile.get(string, "TWATERMAX", -99999.0F);
		if (f != -99999.0F)
			tWaterCritMax = f;
		f = sectfile.get(string, "TWATERMIN", -99999.0F);
		if (f != -99999.0F)
			tWaterCritMin = f;
		f = sectfile.get(string, "TOILMAX", -99999.0F);
		if (f != -99999.0F)
			tOilCritMax = f;
		f = sectfile.get(string, "TOILMIN", -99999.0F);
		if (f != -99999.0F)
			tOilCritMin = f;
		coolMult = 1.0F;
	}
	
	private void initializeInline(float f)
	{
		propSEquivalent = 0.26F * propr * propr;
		engineMomentMax = horsePowers * 746.0F * 1.2F / wMax;
	}
	
	private void initializeJet(float f)
	{
		propSEquivalent = ((float)(cylinders * cylinders) * (2.0F * thrustMax) / (getFanCy(propAoA0) * Atmosphere.ro0() * wMax * wMax * propr * propr));
		computePropForces(wMax, 0.0F, 0.0F, propAoA0, 0.0F);
		engineMomentMax = propMoment;
	}
	
	public void initializeTowString(float f)
	{
		propForce = f;
	}
	
	public void setMaster(boolean bool)
	{
		bIsMaster = bool;
	}
	
	private void insetrPoiInCompressorPoly(float f, float f_14_)
	{
		int i;
		for (i = 0; i < nOfCompPoints; i++)
		{
			if (!(compressorRPM[i] < f))
			{
				if (compressorRPM[i] != f)
					break;
				return;
			}
		}
		for (int i_15_ = nOfCompPoints - 1; i_15_ >= i; i_15_--)
		{
			compressorRPM[i_15_ + 1] = compressorRPM[i_15_];
			compressorATA[i_15_ + 1] = compressorATA[i_15_];
		}
		nOfCompPoints++;
		compressorRPM[i] = f;
		compressorATA[i] = f_14_;
	}
	
	private void calcAfterburnerCompressorFactor()
	{
		if (afterburnerType == 1 || afterburnerType == 7 || afterburnerType == 8 || afterburnerType == 10 || afterburnerType == 11 || afterburnerType == 6 || afterburnerType == 5 || afterburnerType == 9 || afterburnerType == 4)
		{
			float f = compressorRPM[nOfCompPoints - 1];
			float f_16_ = compressorATA[nOfCompPoints - 1];
			nOfCompPoints--;
			int i = 0;
			int i_17_ = 1;
			float f_18_ = 1.0F;
			float f_19_ = f;
			if (nOfCompPoints < 2)
				afterburnerCompressorFactor = 1.0F;
			else
			{
				if ((double)f_19_ < 0.1)
					f_18_ = Atmosphere.pressure((float)reference.Loc.z) * _1_P0;
				else if (f_19_ >= compressorRPM[nOfCompPoints - 1])
					f_18_ = compressorATA[nOfCompPoints - 1];
				else
				{
					if (f_19_ < compressorRPM[0])
					{
						i = 0;
						i_17_ = 1;
					}
					else
					{
						for (int i_20_ = 0; i_20_ < nOfCompPoints - 1; i_20_++)
						{
							if (compressorRPM[i_20_] <= f_19_ && f_19_ < compressorRPM[i_20_ + 1])
							{
								i = i_20_;
								i_17_ = i_20_ + 1;
								break;
							}
						}
					}
					float f_21_ = compressorRPM[i_17_] - compressorRPM[i];
					if (f_21_ < 0.0010F)
						f_21_ = 0.0010F;
					f_18_ = compressorATA[i] + ((f_19_ - compressorRPM[i]) * (compressorATA[i_17_] - compressorATA[i]) / f_21_);
				}
				afterburnerCompressorFactor = f_16_ / f_18_;
			}
		}
		else
			afterburnerCompressorFactor = 1.0F;
	}
	
	public float getATA(float f)
	{
		int i = 0;
		int i_22_ = 1;
		float f_23_ = 1.0F;
		if (nOfCompPoints < 2)
			return 1.0F;
		if ((double)f < 0.1)
			f_23_ = Atmosphere.pressure((float)reference.Loc.z) * _1_P0;
		else if (f >= compressorRPM[nOfCompPoints - 1])
			f_23_ = compressorATA[nOfCompPoints - 1];
		else
		{
			if (f < compressorRPM[0])
			{
				i = 0;
				i_22_ = 1;
			}
			else
			{
				for (int i_24_ = 0; i_24_ < nOfCompPoints - 1; i_24_++)
				{
					if (compressorRPM[i_24_] <= f && f < compressorRPM[i_24_ + 1])
					{
						i = i_24_;
						i_22_ = i_24_ + 1;
						break;
					}
				}
			}
			float f_25_ = compressorRPM[i_22_] - compressorRPM[i];
			if (f_25_ < 0.0010F)
				f_25_ = 0.0010F;
			f_23_ = (compressorATA[i] + ((f - compressorRPM[i]) * (compressorATA[i_22_] - compressorATA[i]) / f_25_));
		}
		return f_23_;
	}
	
	public void update(float f)
	{
		if (!(reference instanceof RealFlightModel) && Time.tickCounter() > 200)
		{
			updateLast += f;
			if (updateLast >= updateStep)
				f = updateStep;
			else
			{
				engineForce.set(old_engineForce);
				engineTorque.set(old_engineTorque);
				return;
			}
		}
		producedDistabilisation = 0.0F;
		pressureExtBar = (Atmosphere.pressure(reference.getAltitude()) + (compressorSpeedManifold * 0.5F * Atmosphere.density(reference.getAltitude()) * reference.getSpeed() * reference.getSpeed()));
		pressureExtBar /= Atmosphere.P0();
		if (controlThrottle > 1.0F && engineBoostFactor == 1.0F)
		{
			reference.CT.setPowerControl(1.0F);
			if (reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode())
				HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[]{new Integer(100)});
		}
		computeForces(f);
		computeStage(f);
		if (stage > 0 && stage < 6)
			engineForce.set(0.0F, 0.0F, 0.0F);
		else if (stage == 8)
			rpm = w = 0.0F;
		if (reference.isPlayers())
		{
			if (bIsMaster && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode())
			{
				computeTemperature(f);
				if (World.cur().diffCur.Reliability)
					computeReliability(f);
			}
			if (World.cur().diffCur.Limited_Fuel)
				computeFuel(f);
		}
		else
			computeFuel(f);
		old_engineForce.set(engineForce);
		old_engineTorque.set(engineTorque);
		updateLast = 0.0F;
		float f_26_ = 0.5F / (Math.abs(aw) + 1.0F) - 0.1F;
		if (f_26_ < 0.025F)
			f_26_ = 0.025F;
		if (f_26_ > 0.4F)
			f_26_ = 0.4F;
		if (f_26_ < updateStep)
			updateStep = 0.9F * updateStep + 0.1F * f_26_;
		else
			updateStep = 0.99F * updateStep + 0.01F * f_26_;
	}
	
	public void netupdate(float f, boolean bool)
	{
		computeStage(f);
		if ((double)Math.abs(w) < 1.0E-5)
			propPhiW = 1.5707964F;
		else
			propPhiW = (float)Math.atan(reference.Vflow.x / (double)(w * propReductor * propr));
		propAoA = propPhi - propPhiW;
		computePropForces(w * propReductor, (float)reference.Vflow.x, propPhi, propAoA, reference.getAltitude());
		float f_27_ = w;
		float f_28_ = propPhi;
		float f_29_ = compressorManifoldPressure;
		computeForces(f);
		if (bool)
			compressorManifoldPressure = f_29_;
		w = f_27_;
		propPhi = f_28_;
		rpm = toRPM(w);
	}
	
	public void setReadyness(Actor actor, float f)
	{
		if (f > 1.0F)
			f = 1.0F;
		if (f < 0.0F)
			f = 0.0F;
		if (Actor.isAlive(actor))
		{
			if (bIsMaster)
			{
				if (readyness > 0.0F && f == 0.0F)
				{
					readyness = 0.0F;
					setEngineDies(actor);
					return;
				}
				doSetReadyness(f);
			}
			if (Math.abs(oldReadyness - readyness) > 0.1F)
			{
				reference.AS.setEngineReadyness(actor, number, (int)(f * 100.0F));
				oldReadyness = readyness;
			}
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
		if (Actor.isAlive(actor))
		{
			if (bIsMaster)
				doSetStage(i);
			reference.AS.setEngineStage(actor, number, i);
		}
	}
	
	public void doSetStage(int i)
	{
		stage = i;
	}
	
	public void setEngineStarts(Actor actor)
	{
		if (bIsMaster && Actor.isAlive(actor) && (!isHasControlMagnetos() || !(getMagnetoMultiplier() < 0.1F)))
			reference.AS.setEngineStarts(number);
	}
	
	public void doSetEngineStarts()
	{
		//TODO: Modified by |ZUTI|: disabled position check. 
		//if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
		if( reference.isStationedOnGround() )
		{
			reference.CT.setMagnetoControl(3);
			setControlMagneto(3);
			stage = 1;
			bRan = false;
			timer = Time.current();
		}
		else if (stage == 0)
		{
			reference.CT.setMagnetoControl(3);
			setControlMagneto(3);
			stage = 1;
			timer = Time.current();
		}
	}
	
	public void setEngineStops(Actor actor)
	{
		if (Actor.isAlive(actor) && (stage >= 1 && stage <= 6))
			reference.AS.setEngineStops(number);
	}
	
	public void doSetEngineStops()
	{
		if (stage != 0)
		{
			stage = 0;
			setControlMagneto(0);
			timer = Time.current();
		}
	}
	
	public void setEngineDies(Actor actor)
	{
		if (stage <= 6)
			reference.AS.setEngineDies(reference.actor, number);
	}
	
	public void doSetEngineDies()
	{
		if (stage < 7)
		{
			bIsInoperable = true;
			reference.setCapableOfTaxiing(false);
			reference.setCapableOfACM(false);
			doSetReadyness(0.0F);
			float f = 0.0F;
			int i = reference.EI.getNum();
			if (i != 0)
			{
				for (int i_30_ = 0; i_30_ < i; i_30_++)
					f += (reference.EI.engines[i_30_].getReadyness() / (float)i);
				if (f < 0.7F)
					reference.setReadyToReturn(true);
				if (f < 0.3F)
					reference.setReadyToDie(true);
			}
			stage = 7;
			if (reference.isPlayers()) {
			    printDebug("Motor doSetEngineDies()");
				HUD.log("FailedEngine");
			}
			timer = Time.current();
		}
	}
	
	public void setEngineRunning(Actor actor)
	{
		if (bIsMaster && Actor.isAlive(actor))
			reference.AS.setEngineRunning(number);
	}
	
	public void doSetEngineRunning()
	{
		if (stage < 6)
		{
			stage = 6;
			reference.CT.setMagnetoControl(3);
			setControlMagneto(3);
			if (reference.isPlayers())
				HUD.log("EngineI1");
			w = wMax * 0.75F;
			tWaterOut = 0.5F * (tWaterCritMin + tWaterMaxRPM);
			tOilOut = 0.5F * (tOilCritMin + tOilOutMaxRPM);
			tOilIn = 0.5F * (tOilCritMin + tOilInMaxRPM);
			propPhi = 0.5F * (propPhiMin + propPhiMax);
			propTarget = propPhi;
			if (isnd != null)
				isnd.onEngineState(stage);
		}
	}
	
	public void setKillCompressor(Actor actor)
	{
		reference.AS.setEngineSpecificDamage(actor, number, 0);
	}
	
	public void doSetKillCompressor()
	{
		switch (compressorType)
		{
			case 2 :
				compressorAltitudes[0] = 50.0F;
				compressorAltMultipliers[0] = 1.0F;
				break;
			case 1 :
				for (int i = 0; i < compressorMaxStep; i++)
				{
					compressorAltitudes[i] = 50.0F;
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
		if (cylindersOperable < 0)
			cylindersOperable = 0;
		if (bIsMaster)
		{
			if (getCylindersRatio() < 0.12F)
				setEngineDies(reference.actor);
			else if (getCylindersRatio() < getReadyness())
				setReadyness(reference.actor, getCylindersRatio());
		}
	}
	
	public void setMagnetoKnockOut(Actor actor, int i)
	{
		reference.AS.setEngineMagnetoKnockOut(reference.actor, number, i);
	}
	
	public void doSetMagnetoKnockOut(int i)
	{
		bMagnetos[i] = false;
		if (i == controlMagneto)
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
		if (stage != 8)
		{
			setReadyness(0.0F);
			if (reference.isPlayers() && stage != 7) {
			    printDebug("Motor doSetEngineStuck()");
				HUD.log("FailedEngine");
			}
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
		if (bHasThrottleControl)
		{
			if (afterburnerType == 4)
			{
				if (f > 1.0F && controlThrottle <= 1.0F && reference.M.requestNitro(1.0E-4F))
				{
					reference.CT.setAfterburnerControl(true);
					setControlAfterburner(true);
					if (reference.isPlayers())
					{
						Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
						HUD.logRightBottom("BoostWepTP4");
					}
				}
				if (f < 1.0F && controlThrottle >= 1.0F)
				{
					reference.CT.setAfterburnerControl(false);
					setControlAfterburner(false);
					if (reference.isPlayers())
					{
						Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
						HUD.logRightBottom(null);
					}
				}
			}
			else if (afterburnerType == 8)
			{
				if (f > 1.0F && controlThrottle <= 1.0F)
				{
					reference.CT.setAfterburnerControl(true);
					setControlAfterburner(true);
					if (reference.isPlayers())
					{
						Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
						HUD.logRightBottom("BoostWepTP7");
					}
				}
				if (f < 1.0F && controlThrottle >= 1.0F)
				{
					reference.CT.setAfterburnerControl(false);
					setControlAfterburner(false);
					if (reference.isPlayers())
					{
						Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
						HUD.logRightBottom(null);
					}
				}
			}
			else if (afterburnerType == 10)
			{
				if (f > 1.0F && controlThrottle <= 1.0F)
				{
					reference.CT.setAfterburnerControl(true);
					setControlAfterburner(true);
					if (reference.isPlayers())
					{
						Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
						HUD.logRightBottom("BoostWepTP0");
					}
				}
				if (f < 1.0F && controlThrottle >= 1.0F)
				{
					reference.CT.setAfterburnerControl(false);
					setControlAfterburner(false);
					if (reference.isPlayers())
					{
						Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
						HUD.logRightBottom(null);
					}
				}
			}
			controlThrottle = f;
		}
	}
	
	public void setControlAfterburner(boolean bool)
	{
		if (bHasAfterburnerControl)
		{
			if (afterburnerType == 1 && !controlAfterburner && bool && controlThrottle > 1.0F && World.Rnd().nextFloat() < 0.5F && reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode()
					&& World.cur().diffCur.Vulnerability)
				setCyliderKnockOut(reference.actor, World.Rnd().nextInt(0, 3));
			controlAfterburner = bool;
		}
		if (afterburnerType == 4 || afterburnerType == 8 || afterburnerType == 10)
			controlAfterburner = bool;
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
		if (bHasPropControl)
			controlProp = f;
	}
	
	public void setControlPropAuto(boolean bool)
	{
		if (bHasPropControl)
			bControlPropAuto = bool && isAllowsAutoProp();
	}
	
	public void doSetKillControlProp()
	{
		bHasPropControl = false;
	}
	
	public void setControlMix(float f)
	{
		if (bHasMixControl)
		{
			switch (mixerType)
			{
				case 0 :
					controlMix = f;
					break;
				case 1 :
					controlMix = f;
					if (controlMix < 1.0F)
						controlMix = 1.0F;
					break;
				default :
					controlMix = f;
			}
		}
	}
	
	public void doSetKillControlMix()
	{
		bHasMixControl = false;
	}
	
	public void setControlMagneto(int i)
	{
		if (bHasMagnetoControl)
		{
			controlMagneto = i;
			if (i == 0)
				setEngineStops(reference.actor);
		}
	}
	
	public void setControlCompressor(int i)
	{
		if (bHasCompressorControl)
			controlCompressor = i;
	}
	
	public void setControlFeather(int i)
	{
		if (bHasFeatherControl)
		{
			controlFeather = i;
			if (reference.isPlayers())
				HUD.log("EngineFeather" + controlFeather);
		}
	}
	
	public void setControlRadiator(float f)
	{
		if (bHasRadiatorControl)
			controlRadiator = f;
	}
	
	public void setExtinguisherFire()
	{
		if (bIsMaster)
		{
			if (bHasExtinguisherControl)
			{
				reference.AS.setEngineSpecificDamage(reference.actor, number, 5);
				if (reference.AS.astateEngineStates[number] > 2)
					reference.AS.setEngineState(reference.actor, number, World.Rnd().nextInt(1, 2));
				else if (reference.AS.astateEngineStates[number] > 0)
					reference.AS.setEngineState(reference.actor, number, 0);
			}
		}
	}
	
	public void doSetExtinguisherFire()
	{
		if (bHasExtinguisherControl)
		{
			extinguishers--;
			if (extinguishers == 0)
				bHasExtinguisherControl = false;
			reference.AS.doSetEngineExtinguisherVisuals(number);
			if (bIsMaster)
			{
				if (reference.AS.astateEngineStates[number] > 1 && World.Rnd().nextFloat() < 0.56F)
					reference.AS.repairEngine(number);
				if (reference.AS.astateEngineStates[number] > 3 && World.Rnd().nextFloat() < 0.21F)
				{
					reference.AS.repairEngine(number);
					reference.AS.repairEngine(number);
				}
				tWaterOut -= 4.0F;
				tOilIn -= 4.0F;
				tOilOut -= 4.0F;
			}
			if (reference.isPlayers())
				HUD.log("ExtinguishersFired");
		}
	}
	
	private void computeStage(float f)
	{
		if (stage != 6)
		{
			bTFirst = false;
			float f_31_ = 20.0F;
			long l = Time.current() - timer;
			if (stage > 0 && stage < 6 && l > given)
			{
				stage++;
				timer = Time.current();
				l = 0L;
			}
			if (oldStage != stage)
			{
				bTFirst = true;
				oldStage = stage;
			}
			if (stage > 0 && stage < 6)
				setControlThrottle(0.2F);
			switch (stage)
			{
				case 0 :
					if (bTFirst)
					{
						given = 4611686018427387903L;
						timer = Time.current();
					}
					if (isnd != null)
						isnd.onEngineState(stage);
					break;
				case 1 :
					if (bTFirst)
					{
						if (bIsStuck)
						{
							stage = 8;
							break;
						}
						if (type == 3 || type == 4 || type == 6)
						{
							stage = 5;
							if (reference.isPlayers())
								HUD.log("Starting_Engine");
							break;
						}
						if (type == 0 || type == 1 || type == 7)
						{
							if (w > wMin)
							{
								stage = 3;
								if (reference.isPlayers())
									HUD.log("Starting_Engine");
								break;
							}
							if (!bIsAutonomous)
							{
								//TODO: Modified by |ZUTI|: disabled position check. 
								//if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
								if( reference.isStationedOnGround() )
								{
									setControlMagneto(3);
									if (reference.isPlayers())
										HUD.log("Starting_Engine");
								}
								else
								{
									doSetEngineStops();
									if (reference.isPlayers())
										HUD.log("EngineI0");
									break;
								}
							}
							else if (reference.isPlayers())
								HUD.log("Starting_Engine");
						}
						else if (!bIsAutonomous)
						{
							//TODO: Modified by |ZUTI|: disabled position check. 
							//if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
							if( reference.isStationedOnGround() )
							{
								setControlMagneto(3);
								if (reference.isPlayers())
									HUD.log("Starting_Engine");
							}
							else
							{
								if (reference.getSpeedKMH() < 350.0F)
								{
									doSetEngineStops();
									if (reference.isPlayers())
										HUD.log("EngineI0");
									break;
								}
								if (reference.isPlayers())
									HUD.log("Starting_Engine");
							}
						}
						else if (reference.isPlayers())
							HUD.log("Starting_Engine");
						given = (long)(500.0F * World.Rnd().nextFloat(1.0F, 2.0F));
					}
					if (isnd != null)
						isnd.onEngineState(stage);
					reference.CT.setMagnetoControl(3);
					setControlMagneto(3);
					w = 0.1047F * (20.0F * (float)l / (float)given);
					setControlThrottle(0.0F);
					break;
				case 2 :
					if (bTFirst)
					{
						given = (long)(4000.0F * World.Rnd().nextFloat(1.0F, 2.0F));
						if (bRan)
						{
							given = (long)(100.0F + ((tOilOutMaxRPM - tOilOut) / (tOilOutMaxRPM - f_31_) * 7900.0F * World.Rnd().nextFloat(2.0F, 4.2F)));
							if (given > 9000L)
								given = World.Rnd().nextLong(7800L, 9600L);
							if (bIsMaster && World.Rnd().nextFloat() < 0.5F)
							{
								stage = 0;
								reference.AS.setEngineStops(number);
							}
						}
					}
					w = 0.1047F * (20.0F + 15.0F * (float)l / (float)given);
					setControlThrottle(0.0F);
					if (isnd != null)
						isnd.onEngineState(stage);
					break;
				case 3 :
					if (bTFirst)
					{
						if (isnd != null)
							isnd.onEngineState(stage);
						if (bIsInoperable)
						{
							stage = 0;
							doSetEngineDies();
							break;
						}
						given = (long)(50.0F * World.Rnd().nextFloat(1.0F, 2.0F));
						if (bIsMaster && World.Rnd().nextFloat() < 0.12F && ((tOilOutMaxRPM - tOilOut) / (tOilOutMaxRPM - f_31_) < 0.75F))
							reference.AS.setEngineStops(number);
					}
					w = 0.1047F * (60.0F + 60.0F * (float)l / (float)given);
					setControlThrottle(0.0F);
					if (reference != null && type != 2 && type != 3 && type != 4 && type != 6 && type != 5)
					{
						for (int i = 1; i < 32; i++)
						{
							try
							{
								Hook hook = reference.actor.findHook("_Engine" + (number + 1) + "EF_" + (i < 10 ? "0" + i : "" + i));
								if (hook != null)
									Eff3DActor.New(reference.actor, hook, null, 1.0F, ("3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff"), -1.0F);
							}
							catch (Exception exception)
							{
								/* empty */
							}
						}
					}
					break;
				case 4 :
					if (bTFirst)
						given = (long)(500.0F * World.Rnd().nextFloat(1.0F, 2.0F));
					w = 12.564F;
					setControlThrottle(0.0F);
					if (isnd != null)
						isnd.onEngineState(stage);
					break;
				case 5 :
					if (bTFirst)
					{
						given = (long)(500.0F * World.Rnd().nextFloat(1.0F, 2.0F));
						if (bRan && (type == 0 || type == 1 || type == 7))
						{
							if ((tOilOutMaxRPM - tOilOut) / (tOilOutMaxRPM - f_31_) > 0.75F)
							{
								if (type == 0 || type == 7)
								{
									if (bIsMaster && getReadyness() > 0.75F && World.Rnd().nextFloat() < 0.25F)
										setReadyness(getReadyness() - 0.05F);
								}
								else if (type == 1 && bIsMaster && World.Rnd().nextFloat() < 0.1F)
									reference.AS.setEngineDies(reference.actor, number);
							}
							if (bIsMaster && World.Rnd().nextFloat() < 0.1F)
								reference.AS.setEngineStops(number);
						}
						bRan = true;
					}
					w = 0.1047F * (120.0F + 120.0F * (float)l / (float)given);
					setControlThrottle(0.2F);
					if (isnd != null)
						isnd.onEngineState(stage);
					break;
				case 6 :
					if (bTFirst)
					{
						given = -1L;
						reference.AS.setEngineRunning(number);
					}
					if (isnd != null)
						isnd.onEngineState(stage);
					break;
				case 7 :
				case 8 :
					if (bTFirst)
						given = -1L;
					setReadyness(0.0F);
					setControlMagneto(0);
					if (isnd != null)
						isnd.onEngineState(stage);
					break;
			}
		}
	}
	
	private void computeFuel(float f)
	{
		tmpF = 0.0F;
		if (stage == 6)
		{
			double d;
			float f_33_;
			switch (type)
			{
				case 0 :
				case 1 :
				case 7 :
					d = momForFuel * (double)w * 0.00105;
					f_33_ = (float)d / horsePowers;
					if (d < (double)horsePowers * 0.05)
						d = (double)horsePowers * 0.05;
					break;
				default :
					d = (double)(thrustMax * (f_33_ = getPowerOutput()));
					if (d < (double)thrustMax * 0.05)
						d = (double)thrustMax * 0.05;
			}
			if (f_33_ < 0.0F)
				f_33_ = 0.0F;
			double d_34_;
			if (f_33_ <= 0.5F)
				d_34_ = ((double)FuelConsumptionP0 + ((double)(FuelConsumptionP05 - FuelConsumptionP0) * (2.0 * (double)f_33_)));
			else if ((double)f_33_ <= 1.0)
				d_34_ = ((double)FuelConsumptionP05 + ((double)(FuelConsumptionP1 - FuelConsumptionP05) * (2.0 * ((double)f_33_ - 0.5))));
			else
			{
				float f_35_ = f_33_ - 1.0F;
				if (f_35_ > 0.1F)
					f_35_ = 0.1F;
				f_35_ *= 10.0F;
				d_34_ = (double)(FuelConsumptionP1 + ((FuelConsumptionPMAX - FuelConsumptionP1) * f_35_));
			}
			d_34_ /= 3600.0;
			switch (type)
			{
				case 0 :
				case 1 :
				case 7 :
				{
					float f_36_ = (float)(d_34_ * d);
					tmpF = f_36_ * f;
					double d_37_ = (double)(f_36_ * 4.4E7F);
					double d_38_ = (double)(f_36_ * 15.7F);
					double d_39_ = 1010.0 * d_38_ * 700.0;
					d *= 746.0;
					Ptermo = (float)(d_37_ - d - d_39_);
					break;
				}
				case 2 :
					tmpF = (float)(d_34_ * d * (double)f);
					break;
				case 3 :
					if (reference.actor instanceof BI_1 || reference.actor instanceof BI_6)
						tmpF = 1.8F * getPowerOutput() * f;
					else if (reference.actor instanceof MXY_7)
						tmpF = 0.5F * getPowerOutput() * f;
					else
						tmpF = 2.5777F * getPowerOutput() * f;
					break;
				case 4 :
					tmpF = 1.4320556F * getPowerOutput() * f;
					tmpB = reference.M.requestNitro(tmpF);
					tmpF = 0.0F;
					if (!tmpB && bIsMaster)
					{
						setEngineStops(reference.actor);
						if (reference.isPlayers() && engineNoFuelHUDLogId == -1)
						{
							engineNoFuelHUDLogId = HUD.makeIdLog();
							HUD.log(engineNoFuelHUDLogId, "EngineNoFuel");
						}
					}
					else
						break;
					return;
				case 6 :
					tmpF = (float)(d_34_ * d * (double)f);
					tmpB = reference.M.requestNitro(tmpF);
					tmpF = 0.0F;
					if (!tmpB && bIsMaster)
					{
						setEngineStops(reference.actor);
						if (reference.isPlayers() && engineNoFuelHUDLogId == -1)
						{
							engineNoFuelHUDLogId = HUD.makeIdLog();
							HUD.log(engineNoFuelHUDLogId, "EngineNoFuel");
						}
					}
					else
						break;
					return;
			}
		}
		tmpB = reference.M.requestFuel(tmpF);
		if (!tmpB && bIsMaster)
		{
			setEngineStops(reference.actor);
			reference.setCapableOfACM(false);
			reference.setCapableOfTaxiing(false);
			if (reference.isPlayers() && engineNoFuelHUDLogId == -1)
			{
				engineNoFuelHUDLogId = HUD.makeIdLog();
				HUD.log(engineNoFuelHUDLogId, "EngineNoFuel");
			}
		}
		if (controlAfterburner)
		{
			switch (afterburnerType)
			{
				case 1 :
					if (controlThrottle > 1.0F && !reference.M.requestNitro(0.044872F * f) && reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode() && World.cur().diffCur.Vulnerability)
						setReadyness(reference.actor, getReadyness() - 0.01F * f);
					break;
				case 2 :
					if (!reference.M.requestNitro(0.044872F * f))
					{
						/* empty */
					}
					break;
				case 5 :
					if (!reference.M.requestNitro(0.044872F * f))
					{
						/* empty */
					}
					break;
				case 9 :
					if (!reference.M.requestNitro(0.044872F * f))
					{
						/* empty */
					}
					break;
				case 4 :
					if (!reference.M.requestNitro(0.044872F * f))
					{
						reference.CT.setAfterburnerControl(false);
						if (reference.isPlayers())
						{
							Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
							HUD.logRightBottom(null);
						}
					}
					break;
			}
		}
	}
	
	private void computeReliability(float f)
	{
		if (stage == 6)
		{
			float f_40_ = controlThrottle;
			if (engineBoostFactor > 1.0F)
				f_40_ *= 0.9090909F;
			switch (type)
			{
				default :
					zatizeni = (double)f_40_;
					zatizeni = zatizeni * zatizeni;
					zatizeni = zatizeni * zatizeni;
					zatizeni *= (double)f * 6.19842621786999E-5;
					if (zatizeni > World.Rnd().nextDouble(0.0, 1.0))
					{
						int i = World.Rnd().nextInt(0, 9);
						if (i < 2)
						{
							reference.AS.hitEngine(reference.actor, number, 3);
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - smoke"));
						}
						else
						{
							setCyliderKnockOut(reference.actor, World.Rnd().nextInt(0, 3));
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - power loss"));
						}
					}
					break;
				case 0 :
				case 1 :
				case 7 :
				{
					zatizeni = (double)(coolMult * f_40_);
					zatizeni *= (double)(w / wWEP);
					zatizeni = zatizeni * zatizeni;
					zatizeni = zatizeni * zatizeni;
					double d = zatizeni * (double)f * 1.4248134284734321E-5;
					if (d > World.Rnd().nextDouble(0.0, 1.0))
					{
						int i = World.Rnd().nextInt(0, 19);
						if (i < 10)
						{
							reference.AS.setEngineCylinderKnockOut(reference.actor, number, 1);
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - cylinder"));
						}
						else if (i < 12)
						{
							if (i < 11)
							{
								reference.AS.setEngineMagnetoKnockOut((reference.actor), number, 0);
								Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - mag1"));
							}
							else
							{
								reference.AS.setEngineMagnetoKnockOut((reference.actor), number, 1);
								Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - mag2"));
							}
						}
						else if (i < 14)
						{
							reference.AS.setEngineDies(reference.actor, number);
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - dead"));
						}
						else if (i < 15)
						{
							reference.AS.setEngineStuck(reference.actor, number);
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - stuck"));
						}
						else if (i < 17)
						{
							setKillPropAngleDevice(reference.actor);
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - propAngler"));
						}
						else
						{
							reference.AS.hitOil(reference.actor, number);
							Aircraft.debugprintln(reference.actor, ("Malfunction #" + number + " - oil"));
						}
					}
				}
			}
		}
	}
	
	private void computeTemperature(float f)
	{
		float f_41_ = Pitot.Indicator((float)reference.Loc.z, reference.getSpeedKMH());
		float f_42_ = Atmosphere.temperature((float)reference.Loc.z) - 273.15F;
		if (stage == 6)
		{
			float f_43_ = ((1.05F * (float)(Math.sqrt(Math.sqrt((double)(getPowerOutput() > 0.2F ? (getPowerOutput() + (float)(reference.AS.astateOilStates[number]) * 0.33F) : 0.2F)))) * (float)Math.sqrt(w / wMax > 0.75F ? (double)(w / wMax) : 0.75)
					* tOilOutMaxRPM * (1.0F - 0.11F * controlRadiator) * (1.0F - f_41_ * 2.0E-4F)) + 22.0F);
			if (getPowerOutput() > 1.0F)
				f_43_ *= getPowerOutput();
			tOilOut += (f_43_ - tOilOut) * f * tChangeSpeed;
		}
		else
		{
			float f_44_ = (w / wMax * tOilOutMaxRPM * (1.0F - 0.2F * controlRadiator) + f_42_);
			Motor motor_45_ = this;
			motor_45_.tOilOut = motor_45_.tOilOut + ((f_44_ - tOilOut) * f * tChangeSpeed * (type == 0 ? 0.42F : 1.07F));
		}
		float f_46_ = 0.8F - 0.05F * controlRadiator;
		float f_47_ = (tOilOut * (f_46_ - f_41_ * 5.0E-4F) + f_42_ * (1.0F - f_46_ + f_41_ * 5.0E-4F));
		tOilIn += (f_47_ - tOilIn) * f * tChangeSpeed * 0.5F;
		f_47_ = (1.05F * (float)Math.sqrt((double)getPowerOutput()) * (1.0F - f_41_ * 2.0E-4F) * tWaterMaxRPM * (controlAfterburner ? 1.1F : 1.0F)) + f_42_;
		Motor motor_48_ = this;
		motor_48_.tWaterOut = motor_48_.tWaterOut + ((f_47_ - tWaterOut) * f * tChangeSpeed * (tWaterOut < 50.0F ? 0.4F : 1.0F) * (1.0F - f_41_ * 6.0E-4F));
		if (tOilOut < f_42_)
			tOilOut = f_42_;
		if (tOilIn < f_42_)
			tOilIn = f_42_;
		if (tWaterOut < f_42_)
			tWaterOut = f_42_;
		if (World.cur().diffCur.Engine_Overheat && (tWaterOut > tWaterCritMax || tOilOut > tOilCritMax))
		{
			if (heatStringID == -1)
				heatStringID = HUD.makeIdLog();
			if (reference.isPlayers())
				HUD.log(heatStringID, "EngineOverheat");
			timeCounter += f;
			if (timeCounter > timeOverheat)
			{
				if (readyness > 0.32F)
				{
					setReadyness(readyness - 0.00666F * f);
					tOilCritMax -= 0.00666F * f * (tOilCritMax - tOilOutMaxRPM);
				}
				else
					setEngineDies(reference.actor);
			}
		}
		else if (timeCounter > 0.0F)
		{
			timeCounter = 0.0F;
			if (heatStringID == -1)
				heatStringID = HUD.makeIdLog();
			if (reference.isPlayers())
				HUD.log(heatStringID, "EngineRestored");
		}
	}
	
	public void updateRadiator(float f)
	{
		if (reference.actor instanceof GLADIATOR)
			controlRadiator = 0.0F;
		else if (reference.actor instanceof P_51 || reference.actor instanceof P_38 || reference.actor instanceof YAK_3 || reference.actor instanceof YAK_3P || reference.actor instanceof YAK_9M || reference.actor instanceof YAK_9U
				|| reference.actor instanceof YAK_9UT || reference.actor instanceof P_63C)
		{
			if (tOilOut > tOilOutMaxRPM)
			{
				controlRadiator += 0.1F * f;
				if (controlRadiator > 1.0F)
					controlRadiator = 1.0F;
			}
			else
			{
				controlRadiator = 1.0F - reference.getSpeed() / reference.VmaxH;
				if (controlRadiator < 0.0F)
					controlRadiator = 0.0F;
			}
		}
		else if (reference.actor instanceof SPITFIRE9 || reference.actor instanceof SPITFIRE8 || reference.actor instanceof SPITFIRE8CLP)
		{
			float f_49_ = 0.0F;
			if (tOilOut > tOilCritMin)
			{
				float f_50_ = tOilCritMax - tOilCritMin;
				f_49_ = 1.4F * (tOilOut - tOilCritMin) / f_50_;
				if (f_49_ > 1.4F)
					f_49_ = 1.4F;
			}
			float f_51_ = 0.0F;
			if (tWaterOut > tWaterCritMin)
			{
				float f_52_ = tWaterCritMax - tWaterCritMin;
				f_51_ = 1.4F * (tWaterOut - tWaterCritMin) / f_52_;
				if (f_51_ > 1.4F)
					f_51_ = 1.4F;
			}
			float f_53_ = Math.max(f_49_, f_51_);
			float f_54_ = 1.0F;
			float f_55_ = reference.getSpeed();
			if (f_55_ > reference.Vmin * 1.5F)
			{
				float f_56_ = reference.Vmax - reference.Vmin * 1.5F;
				f_54_ = 1.0F - 1.65F * (f_55_ - reference.Vmin * 1.5F) / f_56_;
				if (f_54_ < -1.0F)
					f_54_ = -1.0F;
			}
			controlRadiator = 0.5F * (f_53_ + f_54_);
			if (tWaterOut > tWaterCritMax || tOilOut > tOilCritMax)
				controlRadiator += 0.05F * timeCounter;
			if (controlRadiator < 0.0F)
				controlRadiator = 0.0F;
			if (controlRadiator > 1.0F)
				controlRadiator = 1.0F;
		}
		else if (reference.actor instanceof GLADIATOR)
			controlRadiator = 0.0F;
		else
		{
			switch (propAngleDeviceType)
			{
				default :
					controlRadiator = 1.0F - getPowerOutput();
					break;
				case 5 :
				case 6 :
					controlRadiator = 1.0F - reference.getSpeed() / reference.VmaxH;
					if (controlRadiator < 0.0F)
						controlRadiator = 0.0F;
					break;
				case 1 :
				case 2 :
					if (controlRadiator > 1.0F - getPowerOutput())
					{
						controlRadiator -= 0.15F * f;
						if (controlRadiator < 0.0F)
							controlRadiator = 0.0F;
					}
					else
						controlRadiator += 0.15F * f;
					break;
				case 8 :
					if (type == 0)
					{
						if (tOilOut > tOilOutMaxRPM)
						{
							controlRadiator += 0.1F * f;
							if (controlRadiator > 1.0F)
								controlRadiator = 1.0F;
						}
						else if (tOilOut < tOilOutMaxRPM - 10.0F)
						{
							controlRadiator -= 0.1F * f;
							if (controlRadiator < 0.0F)
								controlRadiator = 0.0F;
						}
					}
					else if (controlRadiator > 1.0F - getPowerOutput())
					{
						controlRadiator -= 0.15F * f;
						if (controlRadiator < 0.0F)
							controlRadiator = 0.0F;
					}
					else
						controlRadiator += 0.15F * f;
					break;
				case 7 :
					if (tOilOut > tOilOutMaxRPM)
					{
						controlRadiator += 0.1F * f;
						if (controlRadiator > 1.0F)
							controlRadiator = 1.0F;
					}
					else
					{
						controlRadiator = 1.0F - reference.getSpeed() / reference.VmaxH;
						if (controlRadiator < 0.0F)
							controlRadiator = 0.0F;
					}
			}
		}
	}
	
	private void computeForces(float f)
	{
		switch (type)
		{
			case 0 :
			case 1 :
			case 7 :
			{
				if (Math.abs(w) < 1.0E-5F)
					propPhiW = 1.5707964F;
				else if (type == 7)
					propPhiW = (float)Math.atan(Math.abs(reference.Vflow.x) / (double)(w * propReductor * propr));
				else
					propPhiW = (float)Math.atan(reference.Vflow.x / (double)(w * propReductor * propr));
				propAoA = propPhi - propPhiW;
				if (type == 7)
					computePropForces(w * propReductor, (float)Math.abs(reference.Vflow.x), propPhi, propAoA, reference.getAltitude());
				else
					computePropForces(w * propReductor, (float)reference.Vflow.x, propPhi, propAoA, reference.getAltitude());
				switch (propAngleDeviceType)
				{
					case 0 :
						break;
					case 3 :
					case 4 :
					{
						float f_57_ = controlThrottle;
						if (f_57_ > 1.0F)
							f_57_ = 1.0F;
						compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * f_57_;
						if (isPropAngleDeviceOperational())
						{
							if (bControlPropAuto)
								propTarget = propPhiW + propAoA0;
							else
								propTarget = propPhiMax - controlProp * (propPhiMax - propPhiMin);
						}
						else if (propAngleDeviceType == 3)
							propTarget = 0.0F;
						else
							propTarget = 3.1415927F;
						break;
					}
					case 9 :
						if (bControlPropAuto)
						{
							float f_58_ = propAngleDeviceMaxParam;
							if (controlAfterburner)
								f_58_ = propAngleDeviceAfterburnerParam;
							controlProp += (float)controlPropDirection * f / 5.0F;
							if (controlProp > 1.0F)
								controlProp = 1.0F;
							else if (controlProp < 0.0F)
								controlProp = 0.0F;
							float f_59_ = (propAngleDeviceMinParam + (f_58_ - propAngleDeviceMinParam) * controlProp);
							float f_60_ = controlThrottle;
							if (f_60_ > 1.0F)
								f_60_ = 1.0F;
							compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + (propAngleDeviceMaxParam - propAngleDeviceMinParam) * f_60_));
							if (isPropAngleDeviceOperational())
							{
								if (w < f_59_)
								{
									f_59_ = Math.min(1.0F, 0.01F * (f_59_ - w) - 0.012F * aw);
									propTarget -= f_59_ * getPropAngleDeviceSpeed() * f;
								}
								else
								{
									f_59_ = Math.min(1.0F, 0.01F * (w - f_59_) + 0.012F * aw);
									propTarget += f_59_ * getPropAngleDeviceSpeed() * f;
								}
								if (stage == 6 && propTarget < propPhiW - 0.12F)
								{
									propTarget = propPhiW - 0.12F;
									if (propPhi < propTarget)
										propPhi += 0.2F * f;
								}
							}
							else
								propTarget = propPhi;
						}
						else
						{
							compressorManifoldThreshold = 0.5F + ((compressorRPMtoWMaxATA - 0.5F) * (controlThrottle > 1.0F ? 1.0F : controlThrottle));
							propTarget = propPhi;
							if (isPropAngleDeviceOperational())
							{
								if (controlPropDirection > 0)
									propTarget = propPhiMin;
								else if (controlPropDirection < 0)
									propTarget = propPhiMax;
							}
						}
						break;
					case 1 :
					case 2 :
					{
						if (bControlPropAuto)
						{
							if (engineBoostFactor > 1.0F)
								controlProp = 0.75F + 0.227272F * controlThrottle;
							else
								controlProp = 0.75F + 0.25F * controlThrottle;
						}
						float f_61_ = propAngleDeviceMaxParam;
						if (controlAfterburner && (!bWepRpmInLowGear || controlCompressor != compressorMaxStep))
							f_61_ = propAngleDeviceAfterburnerParam;
						float f_62_ = (propAngleDeviceMinParam + (f_61_ - propAngleDeviceMinParam) * controlProp);
						float f_63_ = controlThrottle;
						if (f_63_ > 1.0F)
							f_63_ = 1.0F;
						compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + (propAngleDeviceMaxParam - propAngleDeviceMinParam) * f_63_));
						if (isPropAngleDeviceOperational())
						{
							if (w < f_62_)
							{
								f_62_ = Math.min(1.0F, 0.01F * (f_62_ - w) - 0.012F * aw);
								propTarget -= f_62_ * getPropAngleDeviceSpeed() * f;
							}
							else
							{
								f_62_ = Math.min(1.0F, 0.01F * (w - f_62_) + 0.012F * aw);
								propTarget += f_62_ * getPropAngleDeviceSpeed() * f;
							}
							if (stage == 6 && propTarget < propPhiW - 0.12F)
							{
								propTarget = propPhiW - 0.12F;
								if (propPhi < propTarget)
									propPhi += 0.2F * f;
							}
						}
						else if (propAngleDeviceType == 1)
							propTarget = 0.0F;
						else
							propTarget = 1.5708F;
						break;
					}
					case 7 :
					{
						float f_64_ = controlThrottle;
						if (engineBoostFactor > 1.0F)
							f_64_ = 0.90909094F * controlThrottle;
						float f_65_ = propAngleDeviceMaxParam;
						if (controlAfterburner)
						{
							if (afterburnerType == 1)
							{
								if (controlThrottle > 1.0F)
									f_65_ = (propAngleDeviceMaxParam + (10.0F * (controlThrottle - 1.0F) * (propAngleDeviceAfterburnerParam - propAngleDeviceMaxParam)));
							}
							else
								f_65_ = propAngleDeviceAfterburnerParam;
						}
						float f_66_ = (propAngleDeviceMinParam + (f_65_ - propAngleDeviceMinParam) * f_64_);
						float f_67_ = controlThrottle;
						if (f_67_ > 1.0F)
							f_67_ = 1.0F;
						compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + ((f_65_ - propAngleDeviceMinParam) * f_67_)));
						if (isPropAngleDeviceOperational())
						{
							if (bControlPropAuto)
							{
								if (w < f_66_)
								{
									f_66_ = Math.min(1.0F, 0.01F * (f_66_ - w) - 0.012F * aw);
									propTarget -= f_66_ * getPropAngleDeviceSpeed() * f;
								}
								else
								{
									f_66_ = Math.min(1.0F, 0.01F * (w - f_66_) + 0.012F * aw);
									propTarget += f_66_ * getPropAngleDeviceSpeed() * f;
								}
								if (stage == 6 && propTarget < propPhiW - 0.12F)
								{
									propTarget = propPhiW - 0.12F;
									if (propPhi < propTarget)
										propPhi += 0.2F * f;
								}
								if (propTarget < propPhiMin + (float)Math.toRadians(3.0))
									propTarget = propPhiMin + (float)Math.toRadians(3.0);
							}
							else
							{
								propTarget = ((1.0F - f * 0.1F) * propTarget + f * 0.1F * (propPhiMax - controlProp * (propPhiMax - propPhiMin)));
								if (w > 1.02F * wMax)
									wMaxAllowed = ((1.0F - 4.0E-7F * (w - 1.02F * wMax)) * wMaxAllowed);
								if (w > wMax)
								{
									float f_68_ = w - wMax;
									f_68_ *= f_68_;
									float f_69_ = 1.0F - 0.0010F * f_68_;
									if (f_69_ < 0.0F)
										f_69_ = 0.0F;
									propForce *= f_69_;
								}
							}
						}
						break;
					}
					case 8 :
					{
						float f_70_ = controlThrottle;
						if (engineBoostFactor > 1.0F)
							f_70_ = 0.90909094F * controlThrottle;
						float f_71_ = propAngleDeviceMaxParam;
						if (controlAfterburner)
						{
							if (afterburnerType == 1)
							{
								if (controlThrottle > 1.0F)
									f_71_ = (propAngleDeviceMaxParam + (10.0F * (controlThrottle - 1.0F) * (propAngleDeviceAfterburnerParam - propAngleDeviceMaxParam)));
							}
							else
								f_71_ = propAngleDeviceAfterburnerParam;
						}
						float f_72_ = (propAngleDeviceMinParam + (f_71_ - propAngleDeviceMinParam) * f_70_ + (bControlPropAuto ? 0.0F : -25.0F + 50.0F * controlProp));
						float f_73_ = controlThrottle;
						if (f_73_ > 1.0F)
							f_73_ = 1.0F;
						compressorManifoldThreshold = getATA(toRPM(propAngleDeviceMinParam + ((f_71_ - propAngleDeviceMinParam) * f_73_)));
						if (isPropAngleDeviceOperational())
						{
							if (w < f_72_)
							{
								f_72_ = Math.min(1.0F, 0.01F * (f_72_ - w) - 0.012F * aw);
								propTarget -= f_72_ * getPropAngleDeviceSpeed() * f;
							}
							else
							{
								f_72_ = Math.min(1.0F, 0.01F * (w - f_72_) + 0.012F * aw);
								propTarget += f_72_ * getPropAngleDeviceSpeed() * f;
							}
							if (stage == 6 && propTarget < propPhiW - 0.12F)
							{
								propTarget = propPhiW - 0.12F;
								if (propPhi < propTarget)
									propPhi += 0.2F * f;
							}
							if (propTarget < propPhiMin + (float)Math.toRadians(3.0))
								propTarget = propPhiMin + (float)Math.toRadians(3.0);
						}
						break;
					}
					case 6 :
					{
						float f_74_ = controlThrottle;
						if (f_74_ > 1.0F)
							f_74_ = 1.0F;
						compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * f_74_;
						if (isPropAngleDeviceOperational())
						{
							if (bControlPropAuto)
							{
								float f_75_ = (25.0F + (wMax - 25.0F) * (0.25F + 0.75F * controlThrottle));
								if (w < f_75_)
								{
									f_75_ = Math.min(1.0F, 0.01F * (f_75_ - w) - 0.012F * aw);
									propTarget -= f_75_ * getPropAngleDeviceSpeed() * f;
								}
								else
								{
									f_75_ = Math.min(1.0F, 0.01F * (w - f_75_) + 0.012F * aw);
									propTarget += f_75_ * getPropAngleDeviceSpeed() * f;
								}
								if (stage == 6 && propTarget < propPhiW - 0.12F)
								{
									propTarget = propPhiW - 0.12F;
									if (propPhi < propTarget)
										propPhi += 0.2F * f;
								}
								controlProp = ((propAngleDeviceMaxParam - propTarget) / (propAngleDeviceMaxParam - propAngleDeviceMinParam));
								if (controlProp < 0.0F)
									controlProp = 0.0F;
								if (controlProp > 1.0F)
									controlProp = 1.0F;
							}
							else
								propTarget = (propAngleDeviceMaxParam - controlProp * (propAngleDeviceMaxParam - propAngleDeviceMinParam));
						}
						break;
					}
					case 5 :
					{
						float f_76_ = controlThrottle;
						if (f_76_ > 1.0F)
							f_76_ = 1.0F;
						compressorManifoldThreshold = 0.5F + (compressorRPMtoWMaxATA - 0.5F) * f_76_;
						if (bControlPropAuto)
						{
							if (reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode())
							{
								if (World.cur().diffCur.ComplexEManagement)
									controlProp = -controlThrottle;
								else
									controlProp = -Aircraft.cvt(reference.getSpeed(), reference.Vmin, reference.Vmax, 0.0F, 1.0F);
							}
							else
								controlProp = -Aircraft.cvt(reference.getSpeed(), reference.Vmin, reference.Vmax, 0.0F, 1.0F);
						}
						propTarget = (propAngleDeviceMaxParam - controlProp * (propAngleDeviceMaxParam - propAngleDeviceMinParam));
						propPhi = propTarget;
						break;
					}
				}
				if (controlFeather == 1 && bHasFeatherControl && isPropAngleDeviceOperational())
					propTarget = 1.55F;
				if (propPhi > propTarget)
				{
					float f_77_ = Math.min(1.0F, 157.29578F * (propPhi - propTarget));
					propPhi -= f_77_ * getPropAngleDeviceSpeed() * f;
				}
				else if (propPhi < propTarget)
				{
					float f_78_ = Math.min(1.0F, 157.29578F * (propTarget - propPhi));
					propPhi += f_78_ * getPropAngleDeviceSpeed() * f;
				}
				if (propTarget > propPhiMax)
					propTarget = propPhiMax;
				else if (propTarget < propPhiMin)
					propTarget = propPhiMin;
				if (propPhi > propPhiMax && controlFeather == 0)
					propPhi = propPhiMax;
				else if (propPhi < propPhiMin)
					propPhi = propPhiMin;
				engineMoment = getN();
				float f_79_ = getCompressorMultiplier(f);
				engineMoment *= f_79_;
				momForFuel = (double)engineMoment;
				engineMoment *= getReadyness();
				engineMoment *= getMagnetoMultiplier();
				engineMoment *= getMixMultiplier();
				engineMoment *= getStageMultiplier();
				engineMoment *= getDistabilisationMultiplier();
				engineMoment += getFrictionMoment(f);
				float f_80_ = engineMoment - propMoment;
				aw = f_80_ / (propI + engineI);
				if (aw > 0.0F)
					aw *= engineAcceleration;
				oldW = w;
				w += aw * f;
				if (w < 0.0F)
					w = 0.0F;
				if (w > wMaxAllowed + wMaxAllowed)
					w = wMaxAllowed + wMaxAllowed;
				if (oldW == 0.0F)
				{
					if (w < 10.0F * fricCoeffT)
						w = 0.0F;
				}
				else if (w < 2.0F * fricCoeffT)
					w = 0.0F;
				if (reference.isPlayers() && World.cur().diffCur.Torque_N_Gyro_Effects && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode())
				{
					propIW.set((double)(propI * w * propReductor), 0.0, 0.0);
					if (propDirection == 1)
						propIW.x = -propIW.x;
					engineTorque.set(0.0F, 0.0F, 0.0F);
					float f_81_ = propI * aw * propReductor;
					if (propDirection == 0)
					{
						engineTorque.x += propMoment;
						engineTorque.x += f_81_;
					}
					else
					{
						engineTorque.x -= propMoment;
						engineTorque.x -= f_81_;
					}
				}
				else
					engineTorque.set(0.0F, 0.0F, 0.0F);
				engineForce.set(engineVector);
				engineForce.scale(propForce);
				tmpV3f.cross(propPos, engineForce);
				engineTorque.add(tmpV3f);
				rearRush = 0.0F;
				rpm = toRPM(w);
				double d = reference.Vflow.x + addVflow;
				if (d < 1.0)
					d = 1.0;
				double d_82_ = 1.0 / ((double)(Atmosphere.density(reference.getAltitude()) * 6.0F) * d);
				addVflow = 0.95 * addVflow + 0.05 * (double)propForce * d_82_;
				addVside = (0.95 * addVside + 0.05 * (double)(propMoment / propr) * d_82_);
				if (addVside < 0.0)
					addVside = 0.0;
				break;
			}
			case 2 :
			{
				engineMoment = (propAngleDeviceMinParam + getControlThrottle() * (propAngleDeviceMaxParam - propAngleDeviceMinParam));
				engineMoment /= propAngleDeviceMaxParam;
				engineMoment *= engineMomentMax;
				engineMoment *= getReadyness();
				engineMoment *= getDistabilisationMultiplier();
				engineMoment *= getStageMultiplier();
				engineMoment += getJetFrictionMoment(f);
				computePropForces(w, 0.0F, 0.0F, propAoA0, 0.0F);
				float f_84_ = w * _1_wMax;
				float f_85_ = f_84_ * pressureExtBar;
				float f_86_ = f_84_ * f_84_;
				float f_87_ = (1.0F - 0.0060F * (Atmosphere.temperature((float)reference.Loc.z) - 290.0F));
				float f_88_ = 1.0F - 0.0011F * reference.getSpeed();
				propForce = (thrustMax * f_85_ * f_86_ * f_87_ * f_88_ * getStageMultiplier());
				float f_89_ = engineMoment - propMoment;
				aw = f_89_ / (propI + engineI) * 1.0F;
				if (aw > 0.0F)
					aw *= engineAcceleration;
				w += aw * f;
				if (w < -wMaxAllowed)
					w = -wMaxAllowed;
				if (w > wMaxAllowed + wMaxAllowed)
					w = wMaxAllowed + wMaxAllowed;
				engineForce.set(engineVector);
				engineForce.scale(propForce);
				engineTorque.cross(enginePos, engineForce);
				rpm = toRPM(w);
				break;
			}
			case 3 :
			case 4 :
			{
				w = wMin + (wMax - wMin) * controlThrottle;
				if (w < wMin || w < 0.0F || reference.M.fuel == 0.0F || stage != 6)
					w = 0.0F;
				propForce = w / wMax * thrustMax;
				propForce *= getStageMultiplier();
				float f_90_ = (float)reference.Vwld.length();
				if (f_90_ > 208.333F)
				{
					if (f_90_ > 291.666F)
						propForce = 0.0F;
					else
						propForce *= (float)Math.sqrt((double)((291.666F - f_90_) / 83.33299F));
				}
				engineForce.set(engineVector);
				engineForce.scale(propForce);
				engineTorque.cross(enginePos, engineForce);
				rpm = toRPM(w);
				break;
			}
			case 6 :
			{
				w = wMin + (wMax - wMin) * controlThrottle;
				if (w < wMin || w < 0.0F || stage != 6)
					w = 0.0F;
				float f_91_ = reference.getSpeed() / 94.0F;
				if (f_91_ < 1.0F)
					w = 0.0F;
				else
					f_91_ = (float)Math.sqrt((double)f_91_);
				propForce = w / wMax * thrustMax * f_91_;
				propForce *= getStageMultiplier();
				float f_92_ = (float)reference.Vwld.length();
				if (f_92_ > 208.333F)
				{
					if (f_92_ > 291.666F)
						propForce = 0.0F;
					else
						propForce *= (float)Math.sqrt((double)((291.666F - f_92_) / 83.33299F));
				}
				engineForce.set(engineVector);
				engineForce.scale(propForce);
				engineTorque.cross(enginePos, engineForce);
				rpm = toRPM(w);
				if (reference instanceof RealFlightModel)
				{
					RealFlightModel realflightmodel = (RealFlightModel)reference;
					f_92_ = Aircraft.cvt(propForce, 0.0F, thrustMax, 0.0F, 0.21F);
					if (realflightmodel.producedShakeLevel < f_92_)
						realflightmodel.producedShakeLevel = f_92_;
				}
				break;
			}
			case 5 :
				engineForce.set(engineVector);
				engineForce.scale(propForce);
				engineTorque.cross(enginePos, engineForce);
				break;
		}
	}
	
	private void computePropForces(float f, float f_93_, float f_94_, float f_95_, float f_96_)
	{
		float f_97_ = f * propr;
		float f_98_ = f_93_ * f_93_ + f_97_ * f_97_;
		float f_99_ = (float)Math.sqrt((double)f_98_);
		float f_100_ = (0.5F * getFanCy((float)Math.toDegrees((double)f_95_)) * Atmosphere.density(f_96_) * f_98_ * propSEquivalent);
		float f_101_ = (0.5F * getFanCx((float)Math.toDegrees((double)f_95_)) * Atmosphere.density(f_96_) * f_98_ * propSEquivalent);
		if (f_99_ > 300.0F)
		{
			float f_102_ = 1.0F + 0.02F * (f_99_ - 300.0F);
			if (f_102_ > 2.0F)
				f_102_ = 2.0F;
			f_101_ *= f_102_;
		}
		if (f_99_ < 0.0010F)
			f_99_ = 0.0010F;
		float f_103_ = 1.0F / f_99_;
		float f_104_ = f_93_ * f_103_;
		float f_105_ = f_97_ * f_103_;
		float f_106_ = 1.0F;
		if (f_93_ < Vopt)
		{
			float f_107_ = Vopt - f_93_;
			f_106_ = 1.0F - 5.0E-5F * f_107_ * f_107_;
		}
		propForce = f_106_ * (f_100_ * f_105_ - f_101_ * f_104_);
		propMoment = (f_101_ * f_105_ + f_100_ * f_104_) * propr;
	}
	
	public void toggle()
	{
		if (stage == 0)
			setEngineStarts(reference.actor);
		else if (stage < 7)
		{
			setEngineStops(reference.actor);
			if (reference.isPlayers())
				HUD.log("EngineI0");
		}
	}
	
	public float getPowerOutput()
	{
		if (stage == 0 || stage > 6)
			return 0.0F;
		return controlThrottle * readyness;
	}
	
	public float getThrustOutput()
	{
		if (stage == 0 || stage > 6)
			return 0.0F;
		float f = w * _1_wMax * readyness;
		if (f > 1.1F)
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
		if (isPropAngleDeviceHydroOperable)
			return propAngleChangeSpeed;
		return propAngleChangeSpeed * 10.0F;
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
		if (bControlPropAuto)
			f = controlProp;
		else
			f = (propPhiMax - propPhi) / (propPhiMax - propPhiMin);
		if (f < 0.1F)
			return 0.0F;
		if (f > 0.9F)
			return 1.0F;
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
		if (reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode())
		{
			if (World.cur().diffCur.ComplexEManagement)
			{
				switch (propAngleDeviceType)
				{
					case 0 :
						return false;
					case 5 :
						return true;
					case 6 :
						return false;
					case 3 :
					case 4 :
						return false;
					case 1 :
					case 2 :
						if (reference.actor instanceof SPITFIRE9 || reference.actor instanceof SPITFIRE8 || reference.actor instanceof SPITFIRE8CLP)
							return true;
						return false;
					case 7 :
					case 8 :
						return true;
					default :
						break;
				}
			}
			else
				return bHasPropControl;
		}
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
		if (World.cur().diffCur.ComplexEManagement)
		{
			if (reference.actor instanceof P_51 || reference.actor instanceof P_38 || reference.actor instanceof YAK_3 || reference.actor instanceof YAK_3P || reference.actor instanceof YAK_9M || reference.actor instanceof YAK_9U
					|| reference.actor instanceof YAK_9UT || reference.actor instanceof SPITFIRE8 || reference.actor instanceof SPITFIRE8CLP || reference.actor instanceof SPITFIRE9 || reference.actor instanceof P_63C)
				return true;
			switch (propAngleDeviceType)
			{
				case 7 :
					return true;
				case 8 :
					if (type == 0)
						return true;
					return false;
				default :
					return false;
			}
		}
		return true;
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
		if (f > 34.0F)
			f = 34.0F;
		if (f < -8.0F)
			f = -8.0F;
		if (f < 16.0F)
			return -0.004688F * f * f + 0.15F * f + 0.4F;
		float f_108_ = 0.0F;
		if (f > 22.0F)
		{
			f_108_ = 0.01F * (f - 22.0F);
			f = 22.0F;
		}
		return 9.7222E-4F * f * f - 0.070833F * f + 2.4844F + f_108_;
	}
	
	private float getFanCx(float f)
	{
		if (f < -4.0F)
			f = -8.0F - f;
		if (f > 34.0F)
			f = 34.0F;
		if ((double)f < 16.0)
			return 3.5E-4F * f * f + 0.0028F * f + 0.0256F;
		float f_109_ = 0.0F;
		if (f > 22.0F)
		{
			f_109_ = 0.04F * (f - 22.0F);
			f = 22.0F;
		}
		return -0.00555F * f * f + 0.24444F * f - 2.32888F + f_109_;
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
	
	public float forcePropAOA(float f, float f_110_, float f_111_, boolean bool)
	{
		switch (type)
		{
			default :
				return -1.0F;
			case 0 :
			case 1 :
			case 7 :
			{
				float f_112_ = controlThrottle;
				boolean bool_113_ = controlAfterburner;
				int i = stage;
				safeLoc.set(reference.Loc);
				safeVwld.set(reference.Vwld);
				safeVflow.set(reference.Vflow);
				if (bool)
					w = wWEP;
				else
					w = wMax;
				controlThrottle = f_111_;
				if ((double)engineBoostFactor <= 1.0 && controlThrottle > 1.0F)
					controlThrottle = 1.0F;
				if (afterburnerType > 0 && bool)
					controlAfterburner = true;
				stage = 6;
				fastATA = true;
				reference.Loc.set(0.0, 0.0, (double)f_110_);
				reference.Vwld.set((double)f, 0.0, 0.0);
				reference.Vflow.set((double)f, 0.0, 0.0);
				pressureExtBar = (Atmosphere.pressure(reference.getAltitude()) + (compressorSpeedManifold * 0.5F * Atmosphere.density(reference.getAltitude()) * f * f));
				pressureExtBar /= Atmosphere.P0();
				float f_114_ = getCompressorMultiplier(0.033F);
				f_114_ *= getN();
				if (bool && bWepRpmInLowGear && controlCompressor == compressorMaxStep)
				{
					w = wMax;
					float f_115_ = getCompressorMultiplier(0.033F);
					f_115_ *= getN();
					f_114_ = f_115_;
				}
				float f_117_ = propPhiMin;
				float f_118_ = -1.0E8F;
				boolean bool_119_ = false;
				if ((Aircraft)reference.actor instanceof SM79)
					bool_119_ = true;
				while (propAngleDeviceType == 0 || bool_119_)
				{
					float f_120_ = 2.0F;
					int i_121_ = 0;
					float f_122_ = 0.1F;
					float f_123_ = 0.5F;
					for (;;)
					{
						if (bool)
							w = wWEP * f_123_;
						else
							w = wMax * f_123_;
						float f_124_ = (float)Math.sqrt((double)(f * f + (w * propr * propReductor * w * propr * propReductor)));
						float f_125_ = f_117_ - (float)Math.asin((double)(f / f_124_));
						computePropForces(w * propReductor, f, 0.0F, f_125_, f_110_);
						f_114_ = getN() * getCompressorMultiplier(0.033F);
						if (i_121_ > 32 || !((double)f_122_ > 1.0E-5))
							break;
						if (propMoment < f_114_)
						{
							if (f_120_ == 1.0F)
								f_122_ /= 2.0F;
							f_123_ *= 1.0F + f_122_;
							f_120_ = 0.0F;
						}
						else
						{
							if (f_120_ == 0.0F)
								f_122_ /= 2.0F;
							f_123_ /= 1.0F + f_122_;
							f_120_ = 1.0F;
						}
						i_121_++;
					}
					if (!bool_119_)
						break;
					if (f_117_ == propPhiMin)
					{
						f_118_ = propForce;
						f_117_ = propPhiMax;
					}
					else
					{
						if (f_118_ > propForce)
							propForce = f_118_;
						break;
					}
				}
				controlThrottle = f_112_;
				controlAfterburner = bool_113_;
				stage = i;
				reference.Loc.set(safeLoc);
				reference.Vwld.set(safeVwld);
				reference.Vflow.set(safeVflow);
				fastATA = false;
				w = 0.0F;
				if (bool_119_ || propAngleDeviceType == 0)
					return propForce;
				float f_126_ = 1.5F;
				float f_127_ = -0.06F;
				float f_128_ = 0.5F * (f_126_ + f_127_);
				int i_129_ = 0;
				for (;;)
				{
					f_128_ = 0.5F * (f_126_ + f_127_);
					if (bool && (!bWepRpmInLowGear || controlCompressor != compressorMaxStep))
						computePropForces(wWEP * propReductor, f, 0.0F, f_128_, f_110_);
					else
						computePropForces(wMax * propReductor, f, 0.0F, f_128_, f_110_);
					if (propForce > 0.0F && Math.abs(propMoment - f_114_) < 1.0E-5F || i_129_ > 32)
						break;
					if (propForce > 0.0F && propMoment > f_114_)
						f_126_ = f_128_;
					else
						f_127_ = f_128_;
					i_129_++;
				}
				return propForce;
			}
			case 2 :
			{
				pressureExtBar = Atmosphere.pressure(f_110_) + (compressorSpeedManifold * 0.5F * Atmosphere.density(f_110_) * f * f);
				pressureExtBar /= Atmosphere.P0();
				float f_130_ = pressureExtBar;
				float f_131_ = 1.0F - 0.0060F * (Atmosphere.temperature(f_110_) - 290.0F);
				float f_132_ = 1.0F - 0.0011F * f;
				propForce = thrustMax * f_130_ * f_131_ * f_132_;
				return propForce;
			}
			case 3 :
			case 4 :
			case 6 :
				propForce = thrustMax;
				if (f > 208.333F)
				{
					if (f > 291.666F)
						propForce = 0.0F;
					else
						propForce *= (float)Math.sqrt((double)((291.666F - f) / 83.33299F));
				}
				return propForce;
			case 5 :
				return thrustMax;
			case 8 :
				return -1.0F;
		}
	}
	
	public float getEngineLoad()
	{
		float f = 0.1F + getControlThrottle() * 0.81818175F;
		float f_133_ = getw() / wMax;
		return f_133_ / f;
	}
	
	private void overrevving()
	{
		if (reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode() && World.cur().diffCur.ComplexEManagement && World.cur().diffCur.Engine_Overheat && w > wMaxAllowed && bIsMaster)
		{
			wMaxAllowed = 0.999965F * wMaxAllowed;
			_1_wMaxAllowed = 1.0F / wMaxAllowed;
			tmpF *= 1.0F - (wMaxAllowed - w) * 0.01F;
			engineDamageAccum += 0.01F + 0.05F * (w - wMaxAllowed) * _1_wMaxAllowed;
			if (engineDamageAccum > 1.0F)
			{
				if (heatStringID == -1)
					heatStringID = HUD.makeIdLog();
				if (reference.isPlayers())
					HUD.log(heatStringID, "EngineOverheat");
				setReadyness(getReadyness() - (engineDamageAccum - 1.0F) * 0.0050F);
			}
			if (getReadyness() < 0.2F)
				setEngineDies(reference.actor);
		}
	}
	
	public float getN()
	{
		if (stage == 6)
		{
			switch (engineCarburetorType)
			{
				case 0 :
				{
					float f = 0.05F + 0.95F * getControlThrottle();
					float f_134_ = w / wMax;
					tmpF = engineMomentMax * (-1.0F / f * f_134_ * f_134_ + 2.0F * f_134_);
					if (getControlThrottle() > 1.0F)
						tmpF *= engineBoostFactor;
					overrevving();
					break;
				}
				case 3 :
				{
					float f = 0.1F + 0.9F * getControlThrottle();
					float f_135_ = w / wNom;
					tmpF = engineMomentMax * (-1.0F / f * f_135_ * f_135_ + 2.0F * f_135_);
					if (getControlThrottle() > 1.0F)
						tmpF *= engineBoostFactor;
					float f_136_ = getControlThrottle() - neg_G_Counter * 0.1F;
					if (f_136_ <= 0.3F)
						f_136_ = 0.3F;
					if (reference.getOverload() < 0.0F && neg_G_Counter >= 0.0F)
					{
						neg_G_Counter += 0.03F;
						producedDistabilisation += 10.0F + 5.0F * neg_G_Counter;
						tmpF *= f_136_;
						if (reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode() && bIsMaster && neg_G_Counter > World.Rnd().nextFloat(5.0F, 8.0F))
							setEngineStops(reference.actor);
					}
					else if (reference.getOverload() >= 0.0F && neg_G_Counter > 0.0F)
					{
						neg_G_Counter -= 0.015F;
						producedDistabilisation += 10.0F + 5.0F * neg_G_Counter;
						tmpF *= f_136_;
						bFloodCarb = true;
					}
					else
					{
						bFloodCarb = false;
						neg_G_Counter = 0.0F;
					}
					overrevving();
					break;
				}
				case 1 :
				case 2 :
				{
					float f = 0.1F + 0.9F * getControlThrottle();
					if (f > 1.0F)
						f = 1.0F;
					float f_137_ = engineMomentMax * (-0.5F * f * f + 1.0F * f + 0.5F);
					float f_138_;
					if (controlAfterburner)
						f_138_ = w / (wWEP * f);
					else
						f_138_ = w / (wNom * f);
					tmpF = f_137_ * (2.0F * f_138_ - 1.0F * f_138_ * f_138_);
					if (getControlThrottle() > 1.0F)
						tmpF *= 1.0F + ((getControlThrottle() - 1.0F) * 10.0F * (engineBoostFactor - 1.0F));
					overrevving();
					break;
				}
				case 4 :
				{
					float f = 0.1F + 0.9F * getControlThrottle();
					if (f > 1.0F)
						f = 1.0F;
					float f_139_ = engineMomentMax * (-0.5F * f * f + 1.0F * f + 0.5F);
					float f_140_;
					if (controlAfterburner)
					{
						f_140_ = w / (wWEP * f);
						if (f >= 0.95F)
							bFullT = true;
						else
							bFullT = false;
					}
					else
					{
						f_140_ = w / (wNom * f);
						bFullT = false;
						if (reference.actor instanceof SPITFIRE5B && f >= 0.95F)
							bFullT = true;
					}
					tmpF = f_139_ * (2.0F * f_140_ - 1.0F * f_140_ * f_140_);
					if (getControlThrottle() > 1.0F)
						tmpF *= 1.0F + ((getControlThrottle() - 1.0F) * 10.0F * (engineBoostFactor - 1.0F));
					float f_141_ = getControlThrottle() - neg_G_Counter * 0.2F;
					if (f_141_ <= 0.0F)
						f_141_ = 0.1F;
					if (reference.getOverload() < 0.0F && neg_G_Counter >= 0.0F)
					{
						neg_G_Counter += 0.03F;
						if (bFullT && neg_G_Counter < 0.5F)
						{
							producedDistabilisation += 15.0F + 5.0F * neg_G_Counter;
							tmpF *= 0.52F - neg_G_Counter;
						}
						else if (bFullT && neg_G_Counter >= 0.5F && neg_G_Counter <= 0.8F)
						{
							neg_G_Counter = 0.51F;
							bFloodCarb = false;
						}
						else if (bFullT && neg_G_Counter > 0.8F)
						{
							neg_G_Counter -= 0.045F;
							producedDistabilisation += 10.0F + 5.0F * neg_G_Counter;
							tmpF *= f_141_;
							bFloodCarb = true;
						}
						else
						{
							producedDistabilisation += 10.0F + 5.0F * neg_G_Counter;
							tmpF *= f_141_;
							if (reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode() && bIsMaster && neg_G_Counter > World.Rnd().nextFloat(7.5F, 9.5F))
								setEngineStops(reference.actor);
						}
					}
					else if (reference.getOverload() >= 0.0F && neg_G_Counter > 0.0F)
					{
						neg_G_Counter -= 0.03F;
						if (!bFullT)
						{
							producedDistabilisation += 10.0F + 5.0F * neg_G_Counter;
							tmpF *= f_141_;
						}
						bFloodCarb = true;
					}
					else
					{
						neg_G_Counter = 0.0F;
						bFloodCarb = false;
					}
					overrevving();
					break;
				}
			}
			if (controlAfterburner)
			{
				if (afterburnerType == 1)
				{
					if (controlThrottle > 1.0F && reference.M.nitro > 0.0F)
						tmpF *= engineAfterburnerBoostFactor;
				}
				else if (afterburnerType == 8 || afterburnerType == 7)
				{
					if (controlCompressor < compressorMaxStep)
						tmpF *= engineAfterburnerBoostFactor;
				}
				else
					tmpF *= engineAfterburnerBoostFactor;
			}
			if (engineDamageAccum > 0.0F)
				engineDamageAccum -= 0.01F;
			if (engineDamageAccum < 0.0F)
				engineDamageAccum = 0.0F;
			if (tmpF < 0.0F)
				tmpF = Math.max(tmpF, -0.8F * w * _1_wMax * engineMomentMax);
			return tmpF;
		}
		tmpF = -1500.0F * w * _1_wMax * engineMomentMax;
		if (stage == 8)
			w = 0.0F;
		return tmpF;
	}
	
	private float getDistabilisationMultiplier()
	{
		if (engineMoment < 0.0F)
			return 1.0F;
		float f = 1.0F + (World.Rnd().nextFloat(-1.0F, 0.1F) * getDistabilisationAmplitude());
		if (f < 0.0F && w < 0.5F * (wMax + wMin))
			return 0.0F;
		return f;
	}
	
	public float getDistabilisationAmplitude()
	{
		if (getCylindersOperable() > 2)
		{
			float f = 1.0F - getCylindersRatio();
			return (engineDistAM * w * w + engineDistBM * w + engineDistCM + 9.25F * f * f + producedDistabilisation);
		}
		return 11.25F;
	}
	
	private float getCompressorMultiplier(float f)
	{
		float f_142_ = controlThrottle;
		if (f_142_ > 1.0F)
			f_142_ = 1.0F;
		float f_143_;
		switch (propAngleDeviceType)
		{
			case 1 :
			case 2 :
			case 7 :
			case 8 :
				f_143_ = getATA(toRPM(propAngleDeviceMinParam + (propAngleDeviceMaxParam - propAngleDeviceMinParam) * f_142_));
				break;
			default :
				f_143_ = compressorRPMtoWMaxATA * (0.55F + 0.45F * f_142_);
		}
		coolMult = 1.0F;
		compressorManifoldThreshold = f_143_;
		switch (compressorType)
		{
			case 0 :
			{
				float f_144_ = (Atmosphere.pressure(reference.getAltitude()) + (0.5F * Atmosphere.density(reference.getAltitude()) * reference.getSpeed() * reference.getSpeed()));
				float f_145_ = f_144_ / Atmosphere.P0();
				coolMult = f_145_;
				return f_145_;
			}
			case 1 :
			{
				float f_146_ = pressureExtBar;
				if ((!bHasCompressorControl || !reference.isPlayers() || !(reference instanceof RealFlightModel) || !((RealFlightModel)reference).isRealMode() || !World.cur().diffCur.ComplexEManagement || fastATA) && (reference.isTick(128, 0) || fastATA))
				{
					compressorStepFound = false;
					controlCompressor = 0;
				}
				float f_147_ = -1.0F;
				float f_148_ = -1.0F;
				int i = -1;
				float f_149_;
				if (fastATA)
				{
					for (controlCompressor = 0; controlCompressor <= compressorMaxStep; controlCompressor++)
					{
						compressorManifoldThreshold = f_143_;
						float f_150_ = compressorPressure[controlCompressor];
						float f_151_ = compressorRPMtoWMaxATA / f_150_;
						float f_152_ = 1.0F;
						float f_153_ = 1.0F;
						if (f_146_ > f_150_)
						{
							float f_154_ = 1.0F - f_150_;
							if (f_154_ < 1.0E-4F)
								f_154_ = 1.0E-4F;
							float f_155_ = 1.0F - f_146_;
							if (f_155_ < 0.0F)
								f_155_ = 0.0F;
							float f_156_ = 1.0F;
							for (int i_157_ = 1; i_157_ <= controlCompressor; i_157_++)
							{
								if (compressorAltMultipliers[controlCompressor] >= 1.0F)
									f_156_ *= 0.8F;
								else
									f_156_ *= 0.8F * (compressorAltMultipliers[controlCompressor]);
							}
							f_152_ = f_156_ + (f_155_ / f_154_ * ((compressorAltMultipliers[controlCompressor]) - f_156_));
						}
						else
							f_152_ = compressorAltMultipliers[controlCompressor];
						compressorManifoldPressure = ((compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f_146_ * f_151_);
						float f_158_ = compressorRPMtoWMaxATA / compressorManifoldPressure;
						if (controlAfterburner && (afterburnerType != 8 && afterburnerType != 7 || controlCompressor != compressorMaxStep) && (afterburnerType != 1 || !(controlThrottle > 1.0F) || !(reference.M.nitro <= 0.0F)))
						{
							f_158_ *= afterburnerCompressorFactor;
							compressorManifoldThreshold *= afterburnerCompressorFactor;
						}
						compressor2ndThrottle = f_158_;
						if (compressor2ndThrottle > 1.0F)
							compressor2ndThrottle = 1.0F;
						compressorManifoldPressure *= compressor2ndThrottle;
						compressor1stThrottle = f_143_ / compressorRPMtoWMaxATA;
						if (compressor1stThrottle > 1.0F)
							compressor1stThrottle = 1.0F;
						compressorManifoldPressure *= compressor1stThrottle;
						f_153_ = (f_152_ * compressorManifoldPressure / compressorManifoldThreshold);
						if (controlAfterburner && (afterburnerType == 8 || afterburnerType == 7) && controlCompressor == compressorMaxStep)
						{
							if (f_153_ / engineAfterburnerBoostFactor > f_147_)
							{
								f_147_ = f_153_;
								i = controlCompressor;
							}
						}
						else if (f_153_ > f_147_)
						{
							f_147_ = f_153_;
							i = controlCompressor;
						}
					}
					f_149_ = f_147_;
					controlCompressor = i;
				}
				else
				{
					float f_159_ = f_143_;
					if (controlAfterburner)
						f_159_ *= afterburnerCompressorFactor;
					do
					{
					    if (controlCompressor < 0) controlCompressor = 0;
                        if (controlCompressor >= compressorPressure.length) controlCompressor = compressorPressure.length - 1;
						float f_160_ = compressorPressure[controlCompressor];
						float f_161_ = compressorRPMtoWMaxATA / f_160_;
						float f_162_ = 1.0F;
						float f_163_ = 1.0F;
						if (f_146_ > f_160_)
						{
							float f_164_ = 1.0F - f_160_;
							if (f_164_ < 1.0E-4F)
								f_164_ = 1.0E-4F;
							float f_165_ = 1.0F - f_146_;
							if (f_165_ < 0.0F)
								f_165_ = 0.0F;
							float f_166_ = 1.0F;
							for (int i_167_ = 1; i_167_ <= controlCompressor; i_167_++)
							{
								if (compressorAltMultipliers[controlCompressor] >= 1.0F)
									f_166_ *= 0.8F;
								else
									f_166_ *= 0.8F * (compressorAltMultipliers[controlCompressor]);
							}
							f_162_ = f_166_ + (f_165_ / f_164_ * ((compressorAltMultipliers[controlCompressor]) - f_166_));
							f_163_ = f_162_;
						}
						else
						{
							f_162_ = compressorAltMultipliers[controlCompressor];
							f_163_ = f_162_ * f_146_ * f_161_ / f_159_;
						}
						if (f_163_ > f_147_)
						{
							f_147_ = f_163_;
							f_148_ = f_162_;
							i = controlCompressor;
						}
						if (!compressorStepFound)
						{
							controlCompressor++;
							if (controlCompressor == compressorMaxStep + 1)
								compressorStepFound = true;
						}
					}
					while (!compressorStepFound);
					if (i < 0)
						i = 0;
					controlCompressor = i;
					float f_168_ = compressorPressure[controlCompressor];
					float f_169_ = compressorRPMtoWMaxATA / f_168_;
					compressorManifoldPressure = ((compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f_146_ * f_169_);
					float f_170_ = compressorRPMtoWMaxATA / compressorManifoldPressure;
					if (controlAfterburner && (afterburnerType != 8 && afterburnerType != 7 || controlCompressor != compressorMaxStep) && (afterburnerType != 1 || !(controlThrottle > 1.0F) || !(reference.M.nitro <= 0.0F)))
					{
						f_170_ *= afterburnerCompressorFactor;
						compressorManifoldThreshold *= afterburnerCompressorFactor;
					}
					if (fastATA)
						compressor2ndThrottle = f_170_;
					else
						compressor2ndThrottle -= 3.0F * f * (compressor2ndThrottle - f_170_);
					if (compressor2ndThrottle > 1.0F)
						compressor2ndThrottle = 1.0F;
					compressorManifoldPressure *= compressor2ndThrottle;
					compressor1stThrottle = f_143_ / compressorRPMtoWMaxATA;
					if (compressor1stThrottle > 1.0F)
						compressor1stThrottle = 1.0F;
					compressorManifoldPressure *= compressor1stThrottle;
					f_149_ = compressorManifoldPressure / compressorManifoldThreshold;
					coolMult = f_149_;
					f_149_ *= f_148_;
				}
				if (w <= 20.0F && w < 150.0F)
					compressorManifoldPressure = Math.min(compressorManifoldPressure, f_146_ * (0.4F + (w - 20.0F) * 0.04F));
				if (w < 20.0F)
					compressorManifoldPressure = f_146_ * (1.0F - w * 0.03F);
				if (mixerType == 1 && stage == 6)
					compressorManifoldPressure *= getMixMultiplier();
				return f_149_;
			}
			case 2 :
			{
				float f_171_ = pressureExtBar;
				float f_172_ = compressorPressure[controlCompressor];
				float f_173_ = compressorRPMtoWMaxATA / f_172_;
				compressorManifoldPressure = ((compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f_171_ * f_173_);
				float f_174_ = compressorRPMtoWMaxATA / compressorManifoldPressure;
				if (controlAfterburner && (afterburnerType != 1 || !(controlThrottle > 1.0F) || !(reference.M.nitro <= 0.0F)))
				{
					f_174_ *= afterburnerCompressorFactor;
					compressorManifoldThreshold *= afterburnerCompressorFactor;
				}
				if (fastATA)
					compressor2ndThrottle = f_174_;
				else
					compressor2ndThrottle -= 0.1F * (compressor2ndThrottle - f_174_);
				if (compressor2ndThrottle > 1.0F)
					compressor2ndThrottle = 1.0F;
				compressorManifoldPressure *= compressor2ndThrottle;
				compressor1stThrottle = f_143_ / compressorRPMtoWMaxATA;
				if (compressor1stThrottle > 1.0F)
					compressor1stThrottle = 1.0F;
				compressorManifoldPressure *= compressor1stThrottle;
				float f_175_;
				if (controlAfterburner && afterburnerType == 2 && reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode() && reference.M.nitro > 0.0F)
				{
					float f_176_ = compressorManifoldPressure + 0.2F;
					if (f_176_ > compressorRPMtoWMaxATA + 0.199F && !fastATA && World.Rnd().nextFloat() < 0.0010F)
					{
						readyness = 0.0F;
						setEngineDies(reference.actor);
					}
					if (f_176_ > compressorManifoldThreshold)
						f_176_ = compressorManifoldThreshold;
					f_175_ = f_176_ / compressorManifoldThreshold;
				}
				else
					f_175_ = compressorManifoldPressure / compressorManifoldThreshold;
				if (w <= 20.0F && w < 150.0F)
					compressorManifoldPressure = Math.min(compressorManifoldPressure, f_171_ * (0.4F + (w - 20.0F) * 0.04F));
				if (w < 20.0F)
					compressorManifoldPressure = f_171_ * (1.0F - w * 0.03F);
				if (f_171_ > f_172_)
				{
					float f_177_ = 1.0F - f_172_;
					if (f_177_ < 1.0E-4F)
						f_177_ = 1.0E-4F;
					float f_178_ = 1.0F - f_171_;
					if (f_178_ < 0.0F)
						f_178_ = 0.0F;
					float f_179_ = 1.0F;
					float f_180_ = f_179_ + (f_178_ / f_177_ * (compressorAltMultipliers[controlCompressor] - f_179_));
					f_175_ *= f_180_;
				}
				else
					f_175_ *= compressorAltMultipliers[controlCompressor];
				coolMult = compressorManifoldPressure / compressorManifoldThreshold;
				return f_175_;
			}
			case 3 :
			{
				float f_181_ = pressureExtBar;
				controlCompressor = 0;
				float f_182_ = -1.0F;
				float f_183_ = compressorPressure[controlCompressor];
				float f_184_ = compressorRPMtoWMaxATA / f_183_;
				float f_185_ = 1.0F;
				if (f_181_ > f_183_)
				{
					float f_187_ = 1.0F - f_183_;
					if (f_187_ < 1.0E-4F)
						f_187_ = 1.0E-4F;
					float f_188_ = 1.0F - f_181_;
					if (f_188_ < 0.0F)
						f_188_ = 0.0F;
					float f_189_ = 1.0F;
					f_185_ = f_189_ + f_188_ / f_187_ * ((compressorAltMultipliers[controlCompressor]) - f_189_);
				}
				else
					f_185_ = compressorAltMultipliers[controlCompressor];
				f_182_ = f_185_;
				f_184_ = compressorRPMtoWMaxATA / f_183_;
				if (f_181_ < f_183_)
					f_181_ = 0.1F * f_181_ + 0.9F * f_183_;
				float f_190_ = f_181_ * f_184_;
				compressorManifoldPressure = ((compressorPAt0 + (1.0F - compressorPAt0) * w * _1_wMax) * f_190_);
				float f_191_ = compressorRPMtoWMaxATA / compressorManifoldPressure;
				if (fastATA)
					compressor2ndThrottle = f_191_;
				else
					compressor2ndThrottle -= 3.0F * f * (compressor2ndThrottle - f_191_);
				if (compressor2ndThrottle > 1.0F)
					compressor2ndThrottle = 1.0F;
				compressorManifoldPressure *= compressor2ndThrottle;
				compressor1stThrottle = f_143_ / compressorRPMtoWMaxATA;
				if (compressor1stThrottle > 1.0F)
					compressor1stThrottle = 1.0F;
				compressorManifoldPressure *= compressor1stThrottle;
				float f_192_ = compressorManifoldPressure / compressorManifoldThreshold;
				f_192_ *= f_182_;
				if (w <= 20.0F && w < 150.0F)
					compressorManifoldPressure = Math.min(compressorManifoldPressure, f_181_ * (0.4F + (w - 20.0F) * 0.04F));
				if (w < 20.0F)
					compressorManifoldPressure = f_181_ * (1.0F - w * 0.03F);
				return f_192_;
			}
			default :
				return 1.0F;
		}
	}
	
	private float getMagnetoMultiplier()
	{
		switch (controlMagneto)
		{
			case 0 :
				return 0.0F;
			case 1 :
				return bMagnetos[0] ? 0.87F : 0.0F;
			case 2 :
				return bMagnetos[1] ? 0.87F : 0.0F;
			case 3 :
			{
				float f = 0.0F;
				f = f + (bMagnetos[0] ? 0.87F : 0.0F);
				f = f + (bMagnetos[1] ? 0.87F : 0.0F);
				if (f > 1.0F)
					f = 1.0F;
				return f;
			}
			default :
				return 1.0F;
		}
	}
	
	private float getMixMultiplier()
	{
		float f = 0.0F;
		switch (mixerType)
		{
			case 0 :
				return 1.0F;
			case 1 :
				if (controlMix == 1.0F)
				{
					if (bFloodCarb)
						reference.AS.setSootState(reference.actor, number, 1);
					else
						reference.AS.setSootState(reference.actor, number, 0);
					return 1.0F;
				}
				/* fall through */
			case 2 :
			{
				if (reference.isPlayers() && reference instanceof RealFlightModel && ((RealFlightModel)reference).isRealMode())
				{
					if (!World.cur().diffCur.ComplexEManagement)
						return 1.0F;
					float f_193_ = mixerLowPressureBar * controlMix;
					if (f_193_ < pressureExtBar - f)
					{
						if (f_193_ < 0.03F)
							setEngineStops(reference.actor);
						if (bFloodCarb)
							reference.AS.setSootState(reference.actor, number, 1);
						else
							reference.AS.setSootState(reference.actor, number, 0);
						if (f_193_ > (pressureExtBar - f) * 0.25F)
							return 1.0F;
						float f_194_ = f_193_ / ((pressureExtBar - f) * 0.25F);
						return f_194_;
					}
					if (f_193_ > pressureExtBar)
					{
						producedDistabilisation += 0.0F + 35.0F * (1.0F - ((pressureExtBar + f) / (f_193_ + 1.0E-4F)));
						reference.AS.setSootState(reference.actor, number, 1);
						float f_195_ = (pressureExtBar + f) / (f_193_ + 1.0E-4F);
						return f_195_;
					}
					if (bFloodCarb)
						reference.AS.setSootState(reference.actor, number, 1);
					else
						reference.AS.setSootState(reference.actor, number, 0);
					return 1.0F;
				}
				float f_196_ = mixerLowPressureBar * controlMix;
				if (f_196_ < pressureExtBar - f && f_196_ < 0.03F)
					setEngineStops(reference.actor);
				return 1.0F;
			}
			default :
				return 1.0F;
		}
	}
	
	private float getStageMultiplier()
	{
		if (stage == 6)
			return 1.0F;
		return 0.0F;
	}
	
	public void setFricCoeffT(float f)
	{
		fricCoeffT = f;
	}
	
	private float getFrictionMoment(float f)
	{
		float f_197_ = 0.0F;
		if (bIsInoperable || stage == 0 || controlMagneto == 0)
		{
			fricCoeffT += 0.1F * f;
			if (fricCoeffT > 1.0F)
				fricCoeffT = 1.0F;
			float f_198_ = w * _1_wMax;
			f_197_ = -fricCoeffT * (6.0F + 3.8F * f_198_) * (propI + engineI) / f;
			float f_199_ = -0.99F * w * (propI + engineI) / f;
			if (f_197_ < f_199_)
				f_197_ = f_199_;
		}
		else
			fricCoeffT = 0.0F;
		return f_197_;
	}
	
	private float getJetFrictionMoment(float f)
	{
		float f_200_ = 0.0F;
		if (bIsInoperable || stage == 0)
			f_200_ = -0.0020F * w * (propI + engineI) / f;
		return f_200_;
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
		return f * 3.1415927F * 2.0F / 60.0F;
	}
	
	private float toRPM(float f)
	{
		return f * 60.0F / 2.0F / 3.1415927F;
	}
	
	//TODO: Altered by |ZUTI|: from private to public.
	public float getKforH(float f, float f_201_, float f_202_)
	{
		float f_203_ = (Atmosphere.density(f_202_) * (f_201_ * f_201_) / (Atmosphere.density(0.0F) * (f * f)));
		if (type != 2)
			f_203_ = f_203_ * kV(f) / kV(f_201_);
		return f_203_;
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
	
	public void replicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException
	{
		netmsgguaranted.writeByte(controlMagneto | stage << 4);
		netmsgguaranted.writeByte(cylinders);
		netmsgguaranted.writeByte(cylindersOperable);
		netmsgguaranted.writeByte((int)(255.0F * readyness));
		netmsgguaranted.writeByte((int)(255.0F * ((propPhi - propPhiMin) / (propPhiMax - propPhiMin))));
		netmsgguaranted.writeFloat(w);
	}
	
	public void replicateFromNet(NetMsgInput netmsginput) throws IOException
	{
		int i = netmsginput.readUnsignedByte();
		stage = (i & 0xf0) >> 4;
		controlMagneto = i & 0xf;
		cylinders = netmsginput.readUnsignedByte();
		cylindersOperable = netmsginput.readUnsignedByte();
		readyness = (float)netmsginput.readUnsignedByte() / 255.0F;
		propPhi = ((float)netmsginput.readUnsignedByte() / 255.0F * (propPhiMax - propPhiMin)) + propPhiMin;
		w = netmsginput.readFloat();
	}
	
    public static void printDebug(String theMessage) {
        if (!DEBUG) return;
        System.out.println(theMessage);
        Exception e = new Exception("Motor Debugger Stacktrace");
        System.out.println("Stacktrace follows:");
        e.printStackTrace();
    }
    
 // TODO: +++ TD AI code backport from 4.13 +++
    public void setManualControlAfterburner(float f)
    {
        controlFAfterburner = f;
    }
    private float controlFAfterburner = 0.0F;
    public float getControlManualAfterburner()
    {
        return controlFAfterburner;
    }
    public boolean isHasControlBoost()
    {
        return afterburnerType == 12;
    }
// TODO: --- TD AI code backport from 4.13 ---

}