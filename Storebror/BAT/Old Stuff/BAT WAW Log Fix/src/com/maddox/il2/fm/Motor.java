/* Modified Motor class for the SAS Engine Mod */

package com.maddox.il2.fm;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.BF_109;
import com.maddox.il2.objects.air.BF_110;
import com.maddox.il2.objects.air.BI_1;
import com.maddox.il2.objects.air.BI_6;
import com.maddox.il2.objects.air.DXXI_DK;
import com.maddox.il2.objects.air.DXXI_DU;
import com.maddox.il2.objects.air.DXXI_SARJA3_EARLY;
import com.maddox.il2.objects.air.DXXI_SARJA3_LATE;
import com.maddox.il2.objects.air.DXXI_SARJA3_SARVANTO;
import com.maddox.il2.objects.air.FW_190;
import com.maddox.il2.objects.air.GLADIATOR;
import com.maddox.il2.objects.air.Hurricane;
import com.maddox.il2.objects.air.JU_87;
import com.maddox.il2.objects.air.JU_88;
import com.maddox.il2.objects.air.JU_88NEW;
import com.maddox.il2.objects.air.MXY_7;
import com.maddox.il2.objects.air.P_38;
import com.maddox.il2.objects.air.P_39;
import com.maddox.il2.objects.air.P_40;
import com.maddox.il2.objects.air.P_40SUKAISVOLOCH;
import com.maddox.il2.objects.air.P_47;
import com.maddox.il2.objects.air.P_51;
import com.maddox.il2.objects.air.P_63C;
import com.maddox.il2.objects.air.SPITFIRE;
import com.maddox.il2.objects.air.SPITFIRE5B;
import com.maddox.il2.objects.air.SPITFIRE8;
import com.maddox.il2.objects.air.SPITFIRE8CLP;
import com.maddox.il2.objects.air.SPITFIRE9;
import com.maddox.il2.objects.air.SPITFIRE925LBSCW;
import com.maddox.il2.objects.air.SPITFIRE9E;
import com.maddox.il2.objects.air.SPITFIRE9ECLP;
import com.maddox.il2.objects.air.SPITFIRE9EHF;
import com.maddox.il2.objects.air.TypeTwoPitchProp;
import com.maddox.il2.objects.air.YAK_3;
import com.maddox.il2.objects.air.YAK_3P;
import com.maddox.il2.objects.air.YAK_9M;
import com.maddox.il2.objects.air.YAK_9U;
import com.maddox.il2.objects.air.YAK_9UT;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class Motor extends FMMath {
    // TODO: Default variables, modified by |ZUTI|: changed all private variables to protected
    // --------------------------------------------------------
    protected static final boolean ___debug___                   = false;
    public static final int        _E_TYPE_INLINE                = 0;
    public static final int        _E_TYPE_RADIAL                = 1;
    public static final int        _E_TYPE_JET                   = 2;
    public static final int        _E_TYPE_ROCKET                = 3;
    public static final int        _E_TYPE_ROCKETBOOST           = 4;
    public static final int        _E_TYPE_TOW                   = 5;
    public static final int        _E_TYPE_PVRD                  = 6;
    public static final int        _E_TYPE_HELO_INLINE           = 7;
    public static final int        _E_TYPE_UNKNOWN               = 8;
    public static final int        _E_TYPE_AZURE                 = 8;
    public static final int        _E_PROP_DIR_LEFT              = 0;
    public static final int        _E_PROP_DIR_RIGHT             = 1;
    public static final int        _E_STAGE_NULL                 = 0;
    public static final int        _E_STAGE_WAKE_UP              = 1;
    public static final int        _E_STAGE_STARTER_ROLL         = 2;
    public static final int        _E_STAGE_CATCH_UP             = 3;
    public static final int        _E_STAGE_CATCH_ROLL           = 4;
    public static final int        _E_STAGE_CATCH_FIRE           = 5;
    public static final int        _E_STAGE_NOMINAL              = 6;
    public static final int        _E_STAGE_DEAD                 = 7;
    public static final int        _E_STAGE_STUCK                = 8;
    public static final int        _E_PROP_FIXED                 = 0;
    public static final int        _E_PROP_RETAIN_RPM_1          = 1;
    public static final int        _E_PROP_RETAIN_RPM_2          = 2;
    public static final int        _E_PROP_RETAIN_AOA_1          = 3;
    public static final int        _E_PROP_RETAIN_AOA_2          = 4;
    public static final int        _E_PROP_FRICTION              = 5;
    public static final int        _E_PROP_MANUALDRIVEN          = 6;
    public static final int        _E_PROP_WM_KOMANDGERAT        = 7;
    public static final int        _E_PROP_FW_KOMANDGERAT        = 8;
    public static final int        _E_PROP_CSP_EL                = 9;
    public static final int        _E_CARB_SUCTION               = 0;
    public static final int        _E_CARB_CARBURETOR            = 1;
    public static final int        _E_CARB_INJECTOR              = 2;
    public static final int        _E_CARB_FLOAT                 = 3;
    public static final int        _E_CARB_SHILLING              = 4;
    public static final int        _E_COMPRESSOR_NONE            = 0;
    public static final int        _E_COMPRESSOR_MANUALSTEP      = 1;
    public static final int        _E_COMPRESSOR_WM_KOMANDGERAT  = 2;
    public static final int        _E_COMPRESSOR_TURBO           = 3;
    public static final int        _E_MIXER_GENERIC              = 0;
    public static final int        _E_MIXER_BRIT_FULLAUTO        = 1;
    public static final int        _E_MIXER_LIMITED_PRESSURE     = 2;
    public static final int        _E_AFTERBURNER_GENERIC        = 0;
    public static final int        _E_AFTERBURNER_MW50           = 1;
    public static final int        _E_AFTERBURNER_GM1            = 2;
    public static final int        _E_AFTERBURNER_FIRECHAMBER    = 3;
    public static final int        _E_AFTERBURNER_WATER          = 4;
    public static final int        _E_AFTERBURNER_NO2            = 5;
    public static final int        _E_AFTERBURNER_FUEL_INJECTION = 6;
    public static final int        _E_AFTERBURNER_FUEL_ILA5      = 7;
    public static final int        _E_AFTERBURNER_FUEL_ILA5AUTO  = 8;
    public static final int        _E_AFTERBURNER_WATERMETHANOL  = 9;
    public static final int        _E_AFTERBURNER_P51            = 10;
    public static final int        _E_AFTERBURNER_SPIT           = 11;
    protected static int           heatStringID                  = -1;
    public FmSounds                isnd;
    protected FlightModel          reference;
    protected static boolean       bTFirst;
    public String                  soundName;
    public String                  startStopName;
    public String                  propName;
    public String                  emdName;
    public String                  emdSubName;
    protected int                  number;
    protected int                  type;
    protected int                  cylinders;
    protected float                engineMass;
    protected float                wMin;
    protected float                wNom;
    public float                   wMax;
    protected float                wWEP;
    protected float                wMaxAllowed;
    public int                     wNetPrev;
    public float                   engineMoment;
    protected float                engineMomentMax;
    protected float                engineBoostFactor;
    protected float                engineAfterburnerBoostFactor;
    protected float                engineDistAM;
    protected float                engineDistBM;
    protected float                engineDistCM;
    protected float                producedDistabilisation;
    protected boolean              bRan;
    protected Point3f              enginePos;
    protected Vector3f             engineVector;
    protected Vector3f             engineForce;
    protected Vector3f             engineTorque;
    protected float                engineDamageAccum;
    protected float                _1_wMaxAllowed;
    protected float                _1_wMax;
    protected float                RPMMin;
    protected float                RPMNom;
    protected float                RPMMax;
    protected float                Vopt;
    protected float                pressureExtBar;
    protected double               momForFuel;
    public double                  addVflow;
    public double                  addVside;
    protected Point3f              propPos;
    protected float                propReductor;
    protected int                  propAngleDeviceType;
    protected float                propAngleDeviceMinParam;
    protected float                propAngleDeviceMaxParam;
    protected float                propAngleDeviceAfterburnerParam;
    protected int                  propDirection;
    protected float                propDiameter;
    protected float                propMass;
    protected float                propI;
    public Vector3d                propIW;
    protected float                propSEquivalent;
    protected float                propr;
    protected float                propPhiMin;
    protected float                propPhiMax;
    protected float                propPhi;
    protected float                propPhiW;
    protected float                propAoA;
    protected float                propAoA0;
    protected float                propAoACrit;
    protected float                propAngleChangeSpeed;
    protected float                propForce;
    public float                   propMoment;
    protected float                propTarget;
    protected int                  mixerType;
    protected float                mixerLowPressureBar;
    protected float                horsePowers;
    public float                   thrustMax;
    protected int                  cylindersOperable;
    protected float                engineI;
    protected float                engineAcceleration;
    protected boolean              bMagnetos[]                   = { true, true };
    protected boolean              bIsAutonomous;
    protected boolean              bIsMaster;
    protected boolean              bIsStuck;
    protected boolean              bIsInoperable;
    protected boolean              bIsAngleDeviceOperational;
    protected boolean              isPropAngleDeviceHydroOperable;
    protected int                  engineCarburetorType;
    protected float                FuelConsumptionP0;
    protected float                FuelConsumptionP05;
    protected float                FuelConsumptionP1;
    protected float                FuelConsumptionPMAX;
    protected float                fuelConsumption0M;
    protected float                fuelConsumption1M;
    protected int                  compressorType;
    public int                     compressorMaxStep;
    protected float                compressorPMax;
    protected float                compressorManifoldPressure;
    public float                   compressorAltitudes[];
    protected float                compressorPressure[];
    protected float                compressorAltMultipliers[];
    protected float                compressorBaseMultipliers[];
    protected float                compressorRPMtoP0;
    protected float                compressorRPMtoCurvature;
    protected float                compressorRPMtoPMax;
    protected float                compressorRPMtoWMaxATA;
    protected float                compressorSpeedManifold;
    protected float                compressorRPM[];
    protected float                compressorATA[];
    protected int                  nOfCompPoints;
    protected boolean              compressorStepFound;
    protected float                compressorManifoldThreshold;
    protected float                afterburnerCompressorFactor;
    protected float                _1_P0;
    protected float                compressor1stThrottle;
    protected float                compressor2ndThrottle;
    protected float                compressorPAt0;
    protected int                  afterburnerType;
    protected boolean              afterburnerChangeW;
    protected int                  stage;
    protected int                  oldStage;
    protected long                 timer;
    protected long                 given;
    protected float                rpm;
    public float                   w;
    protected float                aw;
    protected float                oldW;
    protected float                readyness;
    protected float                oldReadyness;
    protected float                radiatorReadyness;
    protected float                rearRush;
    public float                   tOilIn;
    public float                   tOilOut;
    public float                   tWaterOut;
    public float                   tCylinders;
    protected float                tWaterCritMin;
    public float                   tWaterCritMax;
    protected float                tOilCritMin;
    public float                   tOilCritMax;
    protected float                tWaterMaxRPM;
    public float                   tOilOutMaxRPM;
    protected float                tOilInMaxRPM;
    protected float                tChangeSpeed;
    protected float                timeOverheat;
    protected float                timeUnderheat;
    protected float                timeCounter;
    protected float                oilMass;
    protected float                waterMass;
    protected float                Ptermo;
    protected float                R_air;
    protected float                R_oil;
    protected float                R_water;
    protected float                R_cyl_oil;
    protected float                R_cyl_water;
    protected float                C_eng;
    protected float                C_oil;
    protected float                C_water;
    protected boolean              bHasThrottleControl;
    protected boolean              bHasAfterburnerControl;
    protected boolean              bHasPropControl;
    protected boolean              bHasRadiatorControl;
    protected boolean              bHasMixControl;
    protected boolean              bHasMagnetoControl;
    protected boolean              bHasExtinguisherControl;
    protected boolean              bHasCompressorControl;
    protected boolean              bHasFeatherControl;
    protected int                  extinguishers;
    protected float                controlThrottle;
    public float                   controlRadiator;
    protected boolean              controlAfterburner;
    protected float                controlProp;
    protected boolean              bControlPropAuto;
    protected float                controlMix;
    protected int                  controlMagneto;
    protected int                  controlCompressor;
    protected int                  controlFeather;
    public double                  zatizeni;
    public float                   coolMult;
    protected int                  controlPropDirection;
    protected float                neg_G_Counter;
    protected boolean              bFullT;
    protected boolean              bFloodCarb;
    protected boolean              bWepRpmInLowGear;
    public boolean                 fastATA;
    protected Vector3f             old_engineForce;
    protected Vector3f             old_engineTorque;
    protected float                updateStep;
    protected float                updateLast;
    public float                   relP;
    public float                   maxMoment;
    public float                   maxW;
    protected float                fricCoeffT;
    protected static Vector3f      tmpV3f                        = new Vector3f();
    protected static Vector3d      tmpV3d1                       = new Vector3d();
    protected static Vector3d      tmpV3d2                       = new Vector3d();
    protected static Point3f       safeloc                       = new Point3f();
    protected static Point3d       safeLoc                       = new Point3d();
    protected static Vector3f      safeVwld                      = new Vector3f();
    protected static Vector3f      safeVflow                     = new Vector3f();
    protected static boolean       tmpB;
    public static float            tmpF;
    protected int                  engineNoFuelHUDLogId;
    // --------------------------------------------------------

    // TODO: New Parameters
    // --------------------------------------------------------
    public static final int        _S_TYPE_INERTIA               = 0;
    public static final int        _S_TYPE_MANUAL                = 1;
    public static final int        _S_TYPE_ELECTRIC              = 2;
    public static final int        _S_TYPE_CARTRIDGE             = 3;
    public static final int        _S_TYPE_PNEUMATIC             = 4;
    public static final int        _S_TYPE_BOSCH                 = 5;
    public static final int        _E_TYPE_ROTARY                = 9;
    public static final int        _E_TYPE_TURBOPROP             = 10;

    // Storebror's starter code tuning
    private static String[]        starterTypes                  = { "Inertia", "Manual", "Electric", "Cartridge", "Pneumatic", "Bosch" };
    private int                    starter;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 // this property field must not be static, otherwise all engines share the same starter type (namely the latest one being loaded)
    // New boolean to cause instant engine shutdown if prop is struck
    public boolean                 bPropHit                      = false;

    // TODO: Edits here to add new engine types
    private static final String[]  ENGINE_TYPE_NAMES             = { "Inline", "Radial", "Jet", "Rocket", "RocketBoost", "Tow", "PVRD", "HeloI", "Unknown", "Azure", "Rotary", "Turboprop" };
    private static final int[]     ENGINE_TYPES                  = { _E_TYPE_INLINE, _E_TYPE_RADIAL, _E_TYPE_JET, _E_TYPE_ROCKET, _E_TYPE_ROCKETBOOST, _E_TYPE_TOW, _E_TYPE_PVRD, _E_TYPE_HELO_INLINE, _E_TYPE_UNKNOWN, _E_TYPE_AZURE, _E_TYPE_ROTARY, _E_TYPE_TURBOPROP };

    public Motor() {
        this.isnd = null;
        this.reference = null;
        this.soundName = null;
        this.startStopName = null;
        this.propName = null;
        this.emdName = null;
        this.emdSubName = null;
        this.number = 0;
        this.type = _E_TYPE_INLINE;
        this.cylinders = 12;
        this.engineMass = 900F;
        this.wMin = 20F;
        this.wNom = 180F;
        this.wMax = 200F;
        this.wWEP = 220F;
        this.wMaxAllowed = 250F;
        this.wNetPrev = 0;
        this.engineMoment = 0.0F;
        this.engineMomentMax = 0.0F;
        this.engineBoostFactor = 1.0F;
        this.engineAfterburnerBoostFactor = 1.0F;
        this.engineDistAM = 0.0F;
        this.engineDistBM = 0.0F;
        this.engineDistCM = 0.0F;
        this.bRan = false;
        this.enginePos = new Point3f();
        this.engineVector = new Vector3f();
        this.engineForce = new Vector3f();
        this.engineTorque = new Vector3f();
        this.engineDamageAccum = 0.0F;
        this._1_wMaxAllowed = 1.0F / this.wMaxAllowed;
        this._1_wMax = 1.0F / this.wMax;
        this.RPMMin = 200F;
        this.RPMNom = 2000F;
        this.RPMMax = 2200F;
        this.Vopt = 90F;
        this.momForFuel = 0.0D;
        this.addVflow = 0.0D;
        this.addVside = 0.0D;
        this.propPos = new Point3f();
        this.propReductor = 1.0F;
        this.propAngleDeviceType = 0;
        this.propAngleDeviceMinParam = 0.0F;
        this.propAngleDeviceMaxParam = 0.0F;
        this.propAngleDeviceAfterburnerParam = -999.9F;
        this.propDirection = 0;
        this.propDiameter = 3F;
        this.propMass = 30F;
        this.propI = 1.0F;
        this.propIW = new Vector3d();
        this.propSEquivalent = 1.0F;
        this.propr = 1.125F;
        this.propPhiMin = (float) Math.toRadians(10D);
        this.propPhiMax = (float) Math.toRadians(29D);
        this.propPhi = (float) Math.toRadians(11D);
        this.propAoA0 = (float) Math.toRadians(11D);
        this.propAoACrit = (float) Math.toRadians(16D);
        this.propAngleChangeSpeed = 0.1F;
        this.propForce = 0.0F;
        this.propMoment = 0.0F;
        this.propTarget = 0.0F;
        this.mixerType = 0;
        this.mixerLowPressureBar = 0.0F;
        this.horsePowers = 1200F;
        this.thrustMax = 10.7F;
        this.cylindersOperable = 12;
        this.engineI = 1.0F;
        this.engineAcceleration = 1.0F;
        this.bIsAutonomous = true;
        this.bIsMaster = true;
        this.bIsStuck = false;
        this.bIsInoperable = false;
        this.bIsAngleDeviceOperational = true;
        this.isPropAngleDeviceHydroOperable = true;
        this.engineCarburetorType = 0;
        this.FuelConsumptionP0 = 0.4F;
        this.FuelConsumptionP05 = 0.24F;
        this.FuelConsumptionP1 = 0.28F;
        this.FuelConsumptionPMAX = 0.3F;
        this.compressorType = 0;
        this.compressorMaxStep = 0;
        this.compressorPMax = 1.0F;
        this.compressorManifoldPressure = 1.0F;
        this.compressorAltitudes = null;
        this.compressorPressure = null;
        this.compressorAltMultipliers = null;
        this.compressorBaseMultipliers = null;
        this.compressorRPMtoP0 = 1500F;
        this.compressorRPMtoCurvature = -30F;
        this.compressorRPMtoPMax = 2600F;
        this.compressorRPMtoWMaxATA = 1.45F;
        this.compressorSpeedManifold = 0.2F;
        this.compressorRPM = new float[16];
        this.compressorATA = new float[16];
        this.nOfCompPoints = 0;
        this.compressorStepFound = false;
        this.compressorManifoldThreshold = 1.0F;
        this.afterburnerCompressorFactor = 1.0F;
        this._1_P0 = 1.0F / Atmosphere.P0();
        this.compressor1stThrottle = 1.0F;
        this.compressor2ndThrottle = 1.0F;
        this.compressorPAt0 = 0.3F;
        this.afterburnerType = 0;
        this.afterburnerChangeW = false;
        this.stage = 0;
        this.oldStage = 0;
        this.timer = 0L;
        this.given = 0x3fffffffffffffffL;
        this.rpm = 0.0F;
        this.w = 0.0F;
        this.aw = 0.0F;
        this.oldW = 0.0F;
        this.readyness = 1.0F;
        this.oldReadyness = 1.0F;
        this.radiatorReadyness = 1.0F;
        this.tOilIn = 0.0F;
        this.tOilOut = 0.0F;
        this.tWaterOut = 0.0F;
        this.tCylinders = 0.0F;
        this.oilMass = 90F;
        this.waterMass = 90F;
        this.bHasThrottleControl = true;
        this.bHasAfterburnerControl = true;
        this.bHasPropControl = true;
        this.bHasRadiatorControl = true;
        this.bHasMixControl = true;
        this.bHasMagnetoControl = true;
        this.bHasExtinguisherControl = false;
        this.bHasCompressorControl = false;
        this.bHasFeatherControl = false;
        this.extinguishers = 0;
        this.controlThrottle = 0.0F;
        this.controlRadiator = 0.0F;
        this.controlAfterburner = false;
        this.controlProp = 1.0F;
        this.bControlPropAuto = true;
        this.controlMix = 1.0F;
        this.controlMagneto = 0;
        this.controlCompressor = 0;
        this.controlFeather = 0;
        this.neg_G_Counter = 0.0F;
        this.bFullT = false;
        this.bFloodCarb = false;
        this.fastATA = false;
        this.old_engineForce = new Vector3f();
        this.old_engineTorque = new Vector3f();
        this.updateStep = 0.12F;
        this.updateLast = 0.0F;
        this.fricCoeffT = 1.0F;
        this.engineNoFuelHUDLogId = -1;
        // TODO: New starter variable
        this.starter = _S_TYPE_INERTIA;
    }

    public void load(FlightModel flightmodel, String s, String s1, int i) {
        this.reference = flightmodel;
        String s2 = "FlightModels/" + s + ".emd";
        this.emdName = s;
        this.emdSubName = s1;
        this.number = i;
        SectFile sectfile = FlightModelMain.sectFile(s2);
        this.resolveFromFile(sectfile, "Generic");
        this.resolveFromFile(sectfile, s1);
        this.calcAfterburnerCompressorFactor();
        // TODO: Initializes new engine types
        if ((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_RADIAL) || (this.type == _E_TYPE_HELO_INLINE) || (this.type == _E_TYPE_ROTARY)) {
            this.initializeInline(((FlightModelMain) (flightmodel)).Vmax);
        }
        if (this.type == _E_TYPE_JET) {
            this.initializeJet(((FlightModelMain) (flightmodel)).Vmax);
        }
        if (this.type == _E_TYPE_TURBOPROP) {
            // initializeTurboprop(((FlightModelMain) (flightmodel)).Vmax);
            this.initializeJet(((FlightModelMain) (flightmodel)).Vmax);
        }
    }

    private void resolveFromFile(SectFile sectfile, String s) {
        this.soundName = sectfile.get(s, "SoundName", this.soundName);
        this.propName = sectfile.get(s, "PropName", this.propName);
        this.startStopName = sectfile.get(s, "StartStopName", this.startStopName);
        Aircraft.debugprintln(this.reference.actor, "Resolving submodel " + s + " from file '" + sectfile.toString() + "'....");

        // TODO: Changed by SAS~Storebror, new simplified case-insensitive Engine Type identification
        String emdParameter = sectfile.get(s, "Type");
        if (emdParameter != null) {
            for (int engineTypeIndex = 0; engineTypeIndex < ENGINE_TYPE_NAMES.length; engineTypeIndex++) {
                if (emdParameter.equalsIgnoreCase(ENGINE_TYPE_NAMES[engineTypeIndex])) {
                    this.type = ENGINE_TYPES[engineTypeIndex];
                    break;
                }
            }
        }
        // --------------------------------------------------------
        emdParameter = sectfile.get(s, "Direction");
        if (emdParameter != null) {
            if (emdParameter.endsWith("Left")) {
                this.propDirection = 0;
            } else if (emdParameter.endsWith("Right")) {
                this.propDirection = 1;
            }
        }
        float f = sectfile.get(s, "RPMMin", -99999F);
        if (f != -99999F) {
            this.RPMMin = f;
            this.wMin = this.toRadianPerSecond(this.RPMMin);
        }
        f = sectfile.get(s, "RPMNom", -99999F);
        if (f != -99999F) {
            this.RPMNom = f;
            this.wNom = this.toRadianPerSecond(this.RPMNom);
        }
        f = sectfile.get(s, "RPMMax", -99999F);
        if (f != -99999F) {
            this.RPMMax = f;
            this.wMax = this.toRadianPerSecond(this.RPMMax);
            this._1_wMax = 1.0F / this.wMax;
        }
        f = sectfile.get(s, "RPMMaxAllowed", -99999F);
        if (f != -99999F) {
            this.wMaxAllowed = this.toRadianPerSecond(f);
            this._1_wMaxAllowed = 1.0F / this.wMaxAllowed;
        }
        f = sectfile.get(s, "Reductor", -99999F);
        if (f != -99999F) {
            this.propReductor = f;
        }
        // TODO: New engine types
        // --------------------------------------------------------
        if ((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_RADIAL) || (this.type == _E_TYPE_HELO_INLINE) || (this.type == _E_TYPE_ROTARY)) {
            int i = sectfile.get(s, "Cylinders", -99999);
            if (i != -99999) {
                this.cylinders = i;
                this.cylindersOperable = this.cylinders;
            }
            f = sectfile.get(s, "HorsePowers", -99999F);
            if (f != -99999F) {
                this.horsePowers = f;
            }
            // --------------------------------------------------------
            // TODO: New variable starter
            // --------------------------------------------------------
            emdParameter = sectfile.get(s, "Starter");
            // Storebror's starter code tuning
            do {
                if (emdParameter == null) {
                    break;
                }
                emdParameter = emdParameter.trim(); // make sure to remove trailing/leading spaces etc.
                this.starter = -1; // initialize starter type to invalid value, this is used as a type of flag to indicate whether the starter type has been found yet.
                for (int starterTypeIndex = 0; starterTypeIndex < starterTypes.length; starterTypeIndex++) { // parse starterTypes array for the given starter type
                    if (emdParameter.indexOf(starterTypes[starterTypeIndex]) != -1) { // check for occurance of the given string, don't use "endsWith" here as the string might contain trailing garbage.
                        this.starter = starterTypeIndex; // starter type found!
                        break;
                    }
                }
                if (this.starter != -1) {
                    break; // Starter Type has been found in starterTypes array
                }
                if (this.type == _E_TYPE_ROTARY) {
                    this.starter = 1;
                }
                if (this.starter < 0) {
                    this.starter = 0; // Default back to Inertia Starter default in case none of the above checks managed to resolve the starter type.
                }
            } while (false); // we don't really want to loop here. The do-while statement was used just to avoid excessive if-then-else nesting.
// System.out.println("Motor resolveFromFile starter = " + starter); // TODO: Changed by SAS~Storebror: Kick out that useless debug log line. It has been kicked out thousands of times before, but always made it back in because some "clever" guys worked on decompiled sources. DON'T. Listen and repeat: DON'T!
// --------------------------------------------------------
            int j = sectfile.get(s, "Carburetor", -99999);
            if (j != -99999) {
                this.engineCarburetorType = j;
            }
            f = sectfile.get(s, "Mass", -99999F);
            if (f != -99999F) {
                this.engineMass = f;
            } else {
                this.engineMass = this.horsePowers * 0.6F;
            }
        } else {
            f = sectfile.get(s, "Thrust", -99999F);
            if (f != -99999F) {
                this.thrustMax = f * 9.81F;
            }
        }
        f = sectfile.get(s, "BoostFactor", -99999F);
        if (f != -99999F) {
            this.engineBoostFactor = f;
        }
        f = sectfile.get(s, "WEPBoostFactor", -99999F);
        if (f != -99999F) {
            this.engineAfterburnerBoostFactor = f;
        }
        if (this.type == _E_TYPE_JET) {
            this.FuelConsumptionP0 = 0.075F;
            this.FuelConsumptionP05 = 0.075F;
            this.FuelConsumptionP1 = 0.1F;
            this.FuelConsumptionPMAX = 0.11F;
        }
        if (this.type == _E_TYPE_PVRD) {
            this.FuelConsumptionP0 = 0.835F;
            this.FuelConsumptionP05 = 0.835F;
            this.FuelConsumptionP1 = 0.835F;
            this.FuelConsumptionPMAX = 0.835F;
        }
        f = sectfile.get(s, "FuelConsumptionP0", -99999F);
        if (f != -99999F) {
            this.FuelConsumptionP0 = f;
        }
        f = sectfile.get(s, "FuelConsumptionP05", -99999F);
        if (f != -99999F) {
            this.FuelConsumptionP05 = f;
        }
        f = sectfile.get(s, "FuelConsumptionP1", -99999F);
        if (f != -99999F) {
            this.FuelConsumptionP1 = f;
        }
        f = sectfile.get(s, "FuelConsumptionPMAX", -99999F);
        if (f != -99999F) {
            this.FuelConsumptionPMAX = f;
        }
        this.fuelConsumption0M = (this.FuelConsumptionP0 - this.FuelConsumptionP05) * 4F;
        this.fuelConsumption1M = (this.FuelConsumptionP1 - this.FuelConsumptionP05) * 4F;
        int k = sectfile.get(s, "Autonomous", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bIsAutonomous = false;
            } else if (k == 1) {
                this.bIsAutonomous = true;
            }
        }
        k = sectfile.get(s, "cThrottle", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasThrottleControl = false;
            } else if (k == 1) {
                this.bHasThrottleControl = true;
            }
        }
        k = sectfile.get(s, "cAfterburner", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasAfterburnerControl = false;
            } else if (k == 1) {
                this.bHasAfterburnerControl = true;
            }
        }
        k = sectfile.get(s, "cProp", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasPropControl = false;
            } else if (k == 1) {
                this.bHasPropControl = true;
            }
        }
        k = sectfile.get(s, "cMix", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasMixControl = false;
            } else if (k == 1) {
                this.bHasMixControl = true;
            }
        }
        k = sectfile.get(s, "cMagneto", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasMagnetoControl = false;
            } else if (k == 1) {
                this.bHasMagnetoControl = true;
            }
        }
        k = sectfile.get(s, "cCompressor", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasCompressorControl = false;
            } else if (k == 1) {
                this.bHasCompressorControl = true;
            }
        }
        k = sectfile.get(s, "cFeather", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasFeatherControl = false;
            } else if (k == 1) {
                this.bHasFeatherControl = true;
            }
        }
        k = sectfile.get(s, "cRadiator", -99999);
        if (k != -99999) {
            if (k == 0) {
                this.bHasRadiatorControl = false;
            } else if (k == 1) {
                this.bHasRadiatorControl = true;
            }
        }
        k = sectfile.get(s, "Extinguishers", -99999);
        if (k != -99999) {
            this.extinguishers = k;
            if (k != 0) {
                this.bHasExtinguisherControl = true;
            } else {
                this.bHasExtinguisherControl = false;
            }
        }
        f = sectfile.get(s, "PropDiameter", -99999F);
        if (f != -99999F) {
            this.propDiameter = f;
        }
        this.propr = 0.5F * this.propDiameter * 0.75F;
        f = sectfile.get(s, "PropMass", -99999F);
        if (f != -99999F) {
            this.propMass = f;
        }
        this.propI = this.propMass * this.propDiameter * this.propDiameter * 0.083F;
        this.bWepRpmInLowGear = false;
        k = sectfile.get(s, "PropAnglerType", -99999);
        if (k != -99999) {
            if (k > 255) {
                this.bWepRpmInLowGear = (k & 0x100) > 1;
                k -= 256;
            }
            this.propAngleDeviceType = k;
        }
        f = sectfile.get(s, "PropAnglerSpeed", -99999F);
        if (f != -99999F) {
            this.propAngleChangeSpeed = f;
        }
        f = sectfile.get(s, "PropAnglerMinParam", -99999F);
        if (f != -99999F) {
            this.propAngleDeviceMinParam = f;
            if ((this.propAngleDeviceType == 6) || (this.propAngleDeviceType == 5)) {
                this.propAngleDeviceMinParam = (float) Math.toRadians(this.propAngleDeviceMinParam);
            }
            if ((this.propAngleDeviceType == 1) || (this.propAngleDeviceType == 2) || (this.propAngleDeviceType == 7) || (this.propAngleDeviceType == 8) || (this.propAngleDeviceType == 9)) {
                this.propAngleDeviceMinParam = this.toRadianPerSecond(this.propAngleDeviceMinParam);
            }
        }
        f = sectfile.get(s, "PropAnglerMaxParam", -99999F);
        if (f != -99999F) {
            this.propAngleDeviceMaxParam = f;
            if ((this.propAngleDeviceType == 6) || (this.propAngleDeviceType == 5)) {
                this.propAngleDeviceMaxParam = (float) Math.toRadians(this.propAngleDeviceMaxParam);
            }
            if ((this.propAngleDeviceType == 1) || (this.propAngleDeviceType == 2) || (this.propAngleDeviceType == 7) || (this.propAngleDeviceType == 8) || (this.propAngleDeviceType == 9)) {
                this.propAngleDeviceMaxParam = this.toRadianPerSecond(this.propAngleDeviceMaxParam);
            }
            if (this.propAngleDeviceAfterburnerParam == -999.9F) {
                this.propAngleDeviceAfterburnerParam = this.propAngleDeviceMaxParam;
            }
        }
        f = sectfile.get(s, "PropAnglerAfterburnerParam", -99999F);
        if (f != -99999F) {
            this.propAngleDeviceAfterburnerParam = f;
            this.wWEP = this.toRadianPerSecond(this.propAngleDeviceAfterburnerParam);
            if (this.wWEP != this.wMax) {
                this.afterburnerChangeW = true;
            }
            if ((this.propAngleDeviceType == 6) || (this.propAngleDeviceType == 5)) {
                this.propAngleDeviceAfterburnerParam = (float) Math.toRadians(this.propAngleDeviceAfterburnerParam);
            }
            if ((this.propAngleDeviceType == 1) || (this.propAngleDeviceType == 2) || (this.propAngleDeviceType == 7) || (this.propAngleDeviceType == 8) || (this.propAngleDeviceType == 9)) {
                this.propAngleDeviceAfterburnerParam = this.toRadianPerSecond(this.propAngleDeviceAfterburnerParam);
            }
        } else {
            this.wWEP = this.wMax;
        }
        f = sectfile.get(s, "PropPhiMin", -99999F);
        if (f != -99999F) {
            this.propPhiMin = (float) Math.toRadians(f);
            if (this.propPhi < this.propPhiMin) {
                this.propPhi = this.propPhiMin;
            }
            if (this.propTarget < this.propPhiMin) {
                this.propTarget = this.propPhiMin;
            }
        }
        f = sectfile.get(s, "PropPhiMax", -99999F);
        if (f != -99999F) {
            this.propPhiMax = (float) Math.toRadians(f);
            if (this.propPhi > this.propPhiMax) {
                this.propPhi = this.propPhiMax;
            }
            if (this.propTarget > this.propPhiMax) {
                this.propTarget = this.propPhiMax;
            }
        }
        f = sectfile.get(s, "PropAoA0", -99999F);
        if (f != -99999F) {
            this.propAoA0 = (float) Math.toRadians(f);
        }
        k = sectfile.get(s, "CompressorType", -99999);
        if (k != -99999) {
            this.compressorType = k;
        }
        f = sectfile.get(s, "CompressorPMax", -99999F);
        if (f != -99999F) {
            this.compressorPMax = f;
        }
        k = sectfile.get(s, "CompressorSteps", -99999);
        if (k != -99999) {
            this.compressorMaxStep = k - 1;
            if (this.compressorMaxStep < 0) {
                this.compressorMaxStep = 0;
            }
        }
        if (this.compressorAltitudes != null) {
            if (this.compressorAltitudes.length == (this.compressorMaxStep + 1)) {
                ;
            }
        }
        this.compressorAltitudes = new float[this.compressorMaxStep + 1];
        this.compressorPressure = new float[this.compressorMaxStep + 1];
        this.compressorAltMultipliers = new float[this.compressorMaxStep + 1];
        this.compressorBaseMultipliers = new float[this.compressorMaxStep + 1];
        if (this.compressorAltitudes.length > 0) {
            for (int l = 0; l < this.compressorAltitudes.length; l++) {
                f = sectfile.get(s, "CompressorAltitude" + l, -99999F);
                if (f != -99999F) {
                    this.compressorAltitudes[l] = f;
                    this.compressorPressure[l] = Atmosphere.pressure(this.compressorAltitudes[l]) * this._1_P0;
                }
                f = sectfile.get(s, "CompressorMultiplier" + l, -99999F);
                if (f != -99999F) {
                    this.compressorAltMultipliers[l] = f;
                }
                f = sectfile.get(s, "CompressorBaseMultiplier" + l, -99999F);
                if (f != -99999F) {
                    this.compressorBaseMultipliers[l] = f;
                } else {
                    this.compressorBaseMultipliers[l] = 0.8F;
                }
            }

        }
        f = sectfile.get(s, "CompressorRPMP0", -99999F);
        if (f != -99999F) {
            this.compressorRPMtoP0 = f;
            this.insetrPoiInCompressorPoly(this.compressorRPMtoP0, 1.0F);
        }
        f = sectfile.get(s, "CompressorRPMCurvature", -99999F);
        if (f != -99999F) {
            this.compressorRPMtoCurvature = f;
        }
        f = sectfile.get(s, "CompressorMaxATARPM", -99999F);
        if (f != -99999F) {
            this.compressorRPMtoWMaxATA = f;
            this.insetrPoiInCompressorPoly(this.RPMMax, this.compressorRPMtoWMaxATA);
        }
        f = sectfile.get(s, "CompressorRPMPMax", -99999F);
        if (f != -99999F) {
            this.compressorRPMtoPMax = f;
            this.insetrPoiInCompressorPoly(this.compressorRPMtoPMax, this.compressorPMax);
        }
        f = sectfile.get(s, "CompressorSpeedManifold", -99999F);
        if (f != -99999F) {
            this.compressorSpeedManifold = f;
        }
        f = sectfile.get(s, "CompressorPAt0", -99999F);
        if (f != -99999F) {
            this.compressorPAt0 = f;
        }
        f = sectfile.get(s, "Voptimal", -99999F);
        if (f != -99999F) {
            this.Vopt = f * 0.277778F;
        }
        boolean flag = true;
        float f1 = 2000F;
        float f2 = 1.0F;
        int i1 = 0;
        do {
            if (!flag) {
                break;
            }
            f = sectfile.get(s, "CompressorRPM" + i1, -99999F);
            if (f != -99999F) {
                f1 = f;
            } else {
                flag = false;
            }
            f = sectfile.get(s, "CompressorATA" + i1, -99999F);
            if (f != -99999F) {
                f2 = f;
            } else {
                flag = false;
            }
            if (flag) {
                this.insetrPoiInCompressorPoly(f1, f2);
            }
            i1++;
            if ((this.nOfCompPoints > 15) || (i1 > 15)) {
                flag = false;
            }
        } while (true);
        k = sectfile.get(s, "AfterburnerType", -99999);
        if (k != -99999) {
            this.afterburnerType = k;
        }
        k = sectfile.get(s, "MixerType", -99999);
        if (k != -99999) {
            this.mixerType = k;
        }
        f = sectfile.get(s, "MixerAltitude", -99999F);
        if (f != -99999F) {
            this.mixerLowPressureBar = Atmosphere.pressure(f) / Atmosphere.P0();
        }
        f = sectfile.get(s, "EngineI", -99999F);
        if (f != -99999F) {
            this.engineI = f;
        }
        f = sectfile.get(s, "EngineAcceleration", -99999F);
        if (f != -99999F) {
            this.engineAcceleration = f;
        }
        f = sectfile.get(s, "DisP0x", -99999F);
        if (f != -99999F) {
            float f3 = sectfile.get(s, "DisP0x", -99999F);
            f3 = this.toRadianPerSecond(f3);
            float f4 = sectfile.get(s, "DisP0y", -99999F);
            f4 *= 0.01F;
            float f5 = sectfile.get(s, "DisP1x", -99999F);
            f5 = this.toRadianPerSecond(f5);
            float f6 = sectfile.get(s, "DisP1y", -99999F);
            f6 *= 0.01F;
            float f7 = f3;
            float f8 = f4;
            float f9 = (f5 - f3) * (f5 - f3);
            float f10 = f6 - f4;
            this.engineDistAM = f10 / f9;
            this.engineDistBM = (-2F * f10 * f7) / f9;
            this.engineDistCM = f8 + ((f10 * f7 * f7) / f9);
        }
        this.timeCounter = 0.0F;
        f = sectfile.get(s, "TESPEED", -99999F);
        if (f != -99999F) {
            this.tChangeSpeed = f;
        }
        f = sectfile.get(s, "TWATERMAXRPM", -99999F);
        if (f != -99999F) {
            this.tWaterMaxRPM = f;
        }
        f = sectfile.get(s, "TOILINMAXRPM", -99999F);
        if (f != -99999F) {
            this.tOilInMaxRPM = f;
        }
        f = sectfile.get(s, "TOILOUTMAXRPM", -99999F);
        if (f != -99999F) {
            this.tOilOutMaxRPM = f;
        }
        f = sectfile.get(s, "MAXRPMTIME", -99999F);
        if (f != -99999F) {
            this.timeOverheat = f;
        }
        f = sectfile.get(s, "MINRPMTIME", -99999F);
        if (f != -99999F) {
            this.timeUnderheat = f;
        }
        f = sectfile.get(s, "TWATERMAX", -99999F);
        if (f != -99999F) {
            this.tWaterCritMax = f;
        }
        f = sectfile.get(s, "TWATERMIN", -99999F);
        if (f != -99999F) {
            this.tWaterCritMin = f;
        }
        f = sectfile.get(s, "TOILMAX", -99999F);
        if (f != -99999F) {
            this.tOilCritMax = f;
        }
        f = sectfile.get(s, "TOILMIN", -99999F);
        if (f != -99999F) {
            this.tOilCritMin = f;
        }
        this.coolMult = 1.0F;
    }

    private void initializeInline(float f) {
        this.propSEquivalent = 0.26F * this.propr * this.propr;
        this.engineMomentMax = (this.horsePowers * 746F * 1.2F) / this.wMax;
    }

    private void initializeJet(float f) {
        if (this.type == _E_TYPE_TURBOPROP) {
            this.propSEquivalent = 0.26F * this.propr * this.propr;
        } else {
            this.propSEquivalent = (this.cylinders * this.cylinders * (2.0F * this.thrustMax)) / (this.getFanCy(this.propAoA0) * Atmosphere.ro0() * this.wMax * this.wMax * this.propr * this.propr);
        }
        this.computePropForces(this.wMax, 0.0F, 0.0F, this.propAoA0, 0.0F);
        this.engineMomentMax = this.propMoment;
    }

    public void initializeTowString(float f) {
        this.propForce = f;
    }

    public void setMaster(boolean flag) {
        this.bIsMaster = flag;
    }

    private void insetrPoiInCompressorPoly(float f, float f1) {
        int i = 0;
        do {
            if (i >= this.nOfCompPoints) {
                break;
            }
            if (this.compressorRPM[i] >= f) {
                if (this.compressorRPM[i] == f) {
                    return;
                }
                break;
            }
            i++;
        } while (true);
        for (int j = this.nOfCompPoints - 1; j >= i; j--) {
            this.compressorRPM[j + 1] = this.compressorRPM[j];
            this.compressorATA[j + 1] = this.compressorATA[j];
        }

        this.nOfCompPoints++;
        this.compressorRPM[i] = f;
        this.compressorATA[i] = f1;
    }

    private void calcAfterburnerCompressorFactor() {
        if ((this.afterburnerType == 1) || (this.afterburnerType == 7) || (this.afterburnerType == 8) || (this.afterburnerType == 10) || (this.afterburnerType == 11) || (this.afterburnerType == 6) || (this.afterburnerType == 5) || (this.afterburnerType == 9) || (this.afterburnerType == 4)) {
            float f = this.compressorRPM[this.nOfCompPoints - 1];
            float f1 = this.compressorATA[this.nOfCompPoints - 1];
            this.nOfCompPoints--;
            int i = 0;
            int j = 1;
            float f2 = 1.0F;
            float f3 = f;
            if (this.nOfCompPoints < 2) {
                this.afterburnerCompressorFactor = 1.0F;
                return;
            }
            if (f3 < 0.10000000000000001D) {
                f2 = Atmosphere.pressure((float) this.reference.Loc.z) * this._1_P0;
            } else if (f3 >= this.compressorRPM[this.nOfCompPoints - 1]) {
                f2 = this.compressorATA[this.nOfCompPoints - 1];
            } else {
                if (f3 < this.compressorRPM[0]) {
                    i = 0;
                    j = 1;
                } else {
                    int k = 0;
                    do {
                        if (k >= (this.nOfCompPoints - 1)) {
                            break;
                        }
                        if ((this.compressorRPM[k] <= f3) && (f3 < this.compressorRPM[k + 1])) {
                            i = k;
                            j = k + 1;
                            break;
                        }
                        k++;
                    } while (true);
                }
                float f4 = this.compressorRPM[j] - this.compressorRPM[i];
                if (f4 < 0.001F) {
                    f4 = 0.001F;
                }
                f2 = this.compressorATA[i] + (((f3 - this.compressorRPM[i]) * (this.compressorATA[j] - this.compressorATA[i])) / f4);
            }
            this.afterburnerCompressorFactor = f1 / f2;
        } else {
            this.afterburnerCompressorFactor = 1.0F;
        }
    }

    public float getATA(float f) {
        int i = 0;
        int j = 1;
        float f1 = 1.0F;
        if (this.nOfCompPoints < 2) {
            return 1.0F;
        }
        if (f < 0.10000000000000001D) {
            f1 = Atmosphere.pressure((float) this.reference.Loc.z) * this._1_P0;
        } else if (f >= this.compressorRPM[this.nOfCompPoints - 1]) {
            f1 = this.compressorATA[this.nOfCompPoints - 1];
        } else {
            if (f < this.compressorRPM[0]) {
                i = 0;
                j = 1;
            } else {
                int k = 0;
                do {
                    if (k >= (this.nOfCompPoints - 1)) {
                        break;
                    }
                    if ((this.compressorRPM[k] <= f) && (f < this.compressorRPM[k + 1])) {
                        i = k;
                        j = k + 1;
                        break;
                    }
                    k++;
                } while (true);
            }
            float f2 = this.compressorRPM[j] - this.compressorRPM[i];
            if (f2 < 0.001F) {
                f2 = 0.001F;
            }
            f1 = this.compressorATA[i] + (((f - this.compressorRPM[i]) * (this.compressorATA[j] - this.compressorATA[i])) / f2);
        }
        return f1;
    }

    public void update(float f) {
        if (!(this.reference instanceof RealFlightModel) && (Time.tickCounter() > 200)) {
            this.updateLast += f;
            if (this.updateLast >= this.updateStep) {
                f = this.updateLast;
            } else {
                this.engineForce.set(this.old_engineForce);
                this.engineTorque.set(this.old_engineTorque);
                return;
            }
        }
        this.producedDistabilisation = 0.0F;
        this.pressureExtBar = Atmosphere.pressure(this.reference.getAltitude()) + (this.compressorSpeedManifold * 0.5F * Atmosphere.density(this.reference.getAltitude()) * this.reference.getSpeed() * this.reference.getSpeed());
        this.pressureExtBar *= 9.8716682999999996E-006D;
        if ((this.controlThrottle > 1.0F) && (this.engineBoostFactor == 1.0F)) {
            this.reference.CT.setPowerControl(1.0F);
            if (this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode()) {
                HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[] { new Integer(100) });
            }
        }
        this.computeForces(f);
        this.computeStage(f);
        if ((this.stage > 0) && (this.stage < 6)) {
            this.engineForce.set(0.0F, 0.0F, 0.0F);
        } else if (this.stage == 8) {
            this.rpm = this.w = 0.0F;
        }
        if (this.reference.isPlayers()) {
            if (this.bIsMaster && (this.reference instanceof RealFlightModel)) {
                this.computeTemperature(f);
                if (World.cur().diffCur.Reliability) {
                    this.computeReliability(f);
                }
            }
            if (World.cur().diffCur.Limited_Fuel) {
                this.computeFuel(f);
            }
        } else {
            this.computeFuel(f);
            if (this.bIsMaster && this.reference.isTick(32, 0)) {
                this.computeTemperature(f * 32F);
            }
        }
        this.old_engineForce.set(this.engineForce);
        this.old_engineTorque.set(this.engineTorque);
        this.updateLast = 0.0F;
        float f1 = (0.5F / (Math.abs(this.aw) + 1.0F)) - 0.1F;
        if (f1 < 0.025F) {
            f1 = 0.025F;
        }
        if (f1 > 0.4F) {
            f1 = 0.4F;
        }
        if (f1 < this.updateStep) {
            this.updateStep = (0.9F * this.updateStep) + (0.1F * f1);
        } else {
            this.updateStep = (0.99F * this.updateStep) + (0.01F * f1);
        }
    }

    public void netupdate(float f, boolean flag) {
        this.computeStage(f);
        if (Math.abs(this.w) < 1.0000000000000001E-005D) {
            this.propPhiW = 1.570796F;
        } else {
            this.propPhiW = (float) Math.atan(this.reference.Vflow.x / (this.w * this.propReductor * this.propr));
        }
        this.propAoA = this.propPhi - this.propPhiW;
        this.computePropForces(this.w * this.propReductor, (float) this.reference.Vflow.x, this.propPhi, this.propAoA, this.reference.getAltitude());
        float f1 = this.w;
        float f2 = this.propPhi;
        float f3 = this.compressorManifoldPressure;
        this.computeForces(f);
        if (flag) {
            this.compressorManifoldPressure = f3;
        }
        this.w = f1;
        this.propPhi = f2;
        this.rpm = this.toRPM(this.w);
    }

    public void setReadyness(Actor actor, float f) {
        if (f > 1.0F) {
            f = 1.0F;
        }
        if (f < 0.0F) {
            f = 0.0F;
        }
        if (!Actor.isAlive(actor)) {
            return;
        }
        if (this.bIsMaster) {
            if ((this.readyness > 0.0F) && (f == 0.0F)) {
                this.readyness = 0.0F;
                this.setEngineDies(actor);
                return;
            }
            this.doSetReadyness(f);
        }
        if (Math.abs(this.oldReadyness - this.readyness) > 0.1F) {
            this.reference.AS.setEngineReadyness(actor, this.number, (int) (f * 100F));
            this.oldReadyness = this.readyness;
        }
    }

    private void setReadyness(float f) {
        this.setReadyness(this.reference.actor, f);
    }

    public void doSetReadyness(float f) {
        this.readyness = f;
    }

    public void setStage(Actor actor, int i) {
        if (!Actor.isAlive(actor)) {
            return;
        }
        if (this.bIsMaster) {
            this.doSetStage(i);
        }
        this.reference.AS.setEngineStage(actor, this.number, i);
    }

    public void doSetStage(int i) {
        this.stage = i;
    }

    public void setEngineStarts(Actor actor) {
        if (!this.bIsMaster || !Actor.isAlive(actor)) {
            return;
        }
        if (this.isHasControlMagnetos() && (this.getMagnetoMultiplier() < 0.1F)) {
            return;
        } else {
            this.reference.AS.setEngineStarts(this.number);
            return;
        }
    }

    public void doSetEngineStarts() {
        // TODO: Modified by |ZUTI|: disabled position check.
        // if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
        if (this.reference.isStationedOnGround()) {
            this.reference.CT.setMagnetoControl(3);
            this.setControlMagneto(3);
            this.stage = 1;
            this.bRan = false;
            this.timer = Time.current();
            return;
        }
        /* TODO: here I've tried to solve that start after windmilling issue...w is used to calculate rpm's,so try to play with that */
        if (this.stage == 0) {
            if ((this.type == _E_TYPE_ROTARY) && this.bRan) {
                if (this.w > 10) {
                    this.stage = 5;
                } else if (this.w > 150) {
                    this.stage = 6;
                } else {
                    ((FlightModelMain) (this.reference)).CT.setMagnetoControl(3);
                    this.setControlMagneto(3);
                    this.stage = 1;
                    this.timer = Time.current();
                }
            } else if (((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_RADIAL) || (this.type == _E_TYPE_HELO_INLINE)) && this.bRan) {
                if (this.w > 20) {
                    this.stage = 5;
                } else if (this.w > 150) {
                    this.stage = 6;
                }
            } else {
                ((FlightModelMain) (this.reference)).CT.setMagnetoControl(3);
            }
            this.setControlMagneto(3);
            this.stage = 1;
            this.timer = Time.current();
        }
    }

    public void setEngineStops(Actor actor) {
        if (!Actor.isAlive(actor)) {
            return;
        }
        if ((this.stage < 1) || (this.stage > 6)) {
            return;
        } else {
            this.reference.AS.setEngineStops(this.number);
            return;
        }
    }

    public void doSetEngineStops() {
        if (this.stage != 0) {
            this.stage = 0;
            this.setControlMagneto(0);
            this.timer = Time.current();
        }
    }

    public void setEngineDies(Actor actor) {
        if (this.stage > 6) {
            return;
        } else {
            this.reference.AS.setEngineDies(this.reference.actor, this.number);
            return;
        }
    }

    public void doSetEngineDies() {
        if (this.stage < 7) {
            this.bIsInoperable = true;
            this.reference.setCapableOfTaxiing(false);
            this.reference.setCapableOfACM(false);
            this.doSetReadyness(0.0F);
            float f = 0.0F;
            int i = this.reference.EI.getNum();
            if (i != 0) {
                for (int j = 0; j < i; j++) {
                    f += this.reference.EI.engines[j].getReadyness() / i;
                }

                if (f < 0.7F) {
                    this.reference.setReadyToReturn(true);
                }
                if (f < 0.3F) {
                    this.reference.setReadyToDie(true);
                }
            }
            this.stage = 7;
            if (this.reference.isPlayers()) {
                HUD.log("FailedEngine");
            }
            this.timer = Time.current();
        }
    }

    public void setEngineRunning(Actor actor) {
        if (!this.bIsMaster || !Actor.isAlive(actor)) {
            return;
        } else {
            this.reference.AS.setEngineRunning(this.number);
            return;
        }
    }

    public void doSetEngineRunning() {
        if (this.stage >= 6) {
            return;
        }
        this.stage = 6;
        this.reference.CT.setMagnetoControl(3);
        this.setControlMagneto(3);
        if (this.reference.isPlayers()) {
            HUD.log("EngineI1");
        }
        this.w = this.wMax * 0.75F;
        this.tWaterOut = 0.5F * (this.tWaterCritMin + this.tWaterMaxRPM);
        this.tOilOut = 0.5F * (this.tOilCritMin + this.tOilOutMaxRPM);
        this.tOilIn = 0.5F * (this.tOilCritMin + this.tOilInMaxRPM);
        this.propPhi = 0.5F * (this.propPhiMin + this.propPhiMax);
        this.propTarget = this.propPhi;
        if (this.isnd != null) {
            this.isnd.onEngineState(this.stage);
        }
    }

    public void setKillCompressor(Actor actor) {
        this.reference.AS.setEngineSpecificDamage(actor, this.number, 0);
    }

    public void doSetKillCompressor() {
        switch (this.compressorType) {
            default:
                break;

            case 2: // '\002'
                this.compressorAltitudes[0] = 50F;
                this.compressorAltMultipliers[0] = 1.0F;
                break;

            case 1: // '\001'
                for (int i = 0; i < this.compressorMaxStep; i++) {
                    this.compressorAltitudes[i] = 50F;
                    this.compressorAltMultipliers[i] = 1.0F;
                }

                break;
        }
    }

    public void setKillPropAngleDevice(Actor actor) {
        this.reference.AS.setEngineSpecificDamage(actor, this.number, 3);
    }

    public void doSetKillPropAngleDevice() {
        this.bIsAngleDeviceOperational = false;
    }

    public void setKillPropAngleDeviceSpeeds(Actor actor) {
        this.reference.AS.setEngineSpecificDamage(actor, this.number, 4);
    }

    public void doSetKillPropAngleDeviceSpeeds() {
        this.isPropAngleDeviceHydroOperable = false;
    }

    public void setCyliderKnockOut(Actor actor, int i) {
        this.reference.AS.setEngineCylinderKnockOut(actor, this.number, i);
    }

    public void doSetCyliderKnockOut(int i) {
        this.cylindersOperable -= i;
        if (this.cylindersOperable < 0) {
            this.cylindersOperable = 0;
        }
        if (this.bIsMaster) {
            if (this.getCylindersRatio() < 0.12F) {
                this.setEngineDies(this.reference.actor);
            } else if (this.getCylindersRatio() < this.getReadyness()) {
                this.setReadyness(this.reference.actor, this.getCylindersRatio());
            }
        }
    }

    public void setMagnetoKnockOut(Actor actor, int i) {
        this.reference.AS.setEngineMagnetoKnockOut(this.reference.actor, this.number, i);
    }

    public void doSetMagnetoKnockOut(int i) {
        this.bMagnetos[i] = false;
        if (i == this.controlMagneto) {
            this.setEngineStops(this.reference.actor);
        }
    }

    public void setEngineStuck(Actor actor) {
        this.reference.AS.setEngineStuck(actor, this.number);
    }

    public void doSetEngineStuck() {
        this.bIsInoperable = true;
        this.reference.setCapableOfTaxiing(false);
        this.reference.setCapableOfACM(false);
        if (this.stage != 8) {
            this.setReadyness(0.0F);
            if (this.reference.isPlayers() && (this.stage != 7)) {
                HUD.log("FailedEngine");
            }
            this.stage = 8;
            this.timer = Time.current();
        }
    }

    public void setw(float f) {
        this.w = f;
        this.rpm = this.toRPM(this.w);
    }

    public void setPropPhi(float f) {
        this.propPhi = f;
    }

    public void setEngineMomentMax(float f) {
        this.engineMomentMax = f;
    }

    public void setPos(Point3d point3d) {
        this.enginePos.set(point3d);
    }

    public void setPropPos(Point3d point3d) {
        this.propPos.set(point3d);
    }

    public void setVector(Vector3f vector3f) {
        this.engineVector.set(vector3f);
        this.engineVector.normalize();
    }

    public void setControlThrottle(float f) {
        if (this.bHasThrottleControl) {
            if (this.afterburnerType == 4) {
                if ((f > 1.0F) && (this.controlThrottle <= 1.0F) && this.reference.M.requestNitro(0.0001F)) {
                    this.reference.CT.setAfterburnerControl(true);
                    this.setControlAfterburner(true);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP4");
                    }
                }
                if ((f < 1.0F) && (this.controlThrottle >= 1.0F)) {
                    this.reference.CT.setAfterburnerControl(false);
                    this.setControlAfterburner(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            } else if (this.afterburnerType == 8) {
                if ((f > 1.0F) && (this.controlThrottle <= 1.0F)) {
                    this.reference.CT.setAfterburnerControl(true);
                    this.setControlAfterburner(true);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP7");
                    }
                }
                if ((f < 1.0F) && (this.controlThrottle >= 1.0F)) {
                    this.reference.CT.setAfterburnerControl(false);
                    this.setControlAfterburner(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            } else if (this.afterburnerType == 10) {
                if ((f > 1.0F) && (this.controlThrottle <= 1.0F)) {
                    this.reference.CT.setAfterburnerControl(true);
                    this.setControlAfterburner(true);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP0");
                    }
                }
                if ((f < 1.0F) && (this.controlThrottle >= 1.0F)) {
                    this.reference.CT.setAfterburnerControl(false);
                    this.setControlAfterburner(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            }
            this.controlThrottle = f;
        }
    }

    public void setControlAfterburner(boolean flag) {
        if (this.bHasAfterburnerControl) {
            if ((this.afterburnerType == 1) && !this.controlAfterburner && flag && (this.controlThrottle > 1.0F) && (World.Rnd().nextFloat() < 0.5F) && this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode() && World.cur().diffCur.Vulnerability) {
                this.setCyliderKnockOut(this.reference.actor, World.Rnd().nextInt(0, 3));
            }
            this.controlAfterburner = flag;
        }
        if ((this.afterburnerType == 4) || (this.afterburnerType == 8) || (this.afterburnerType == 10)) {
            this.controlAfterburner = flag;
        }
    }

    public void doSetKillControlThrottle() {
        this.bHasThrottleControl = false;
    }

    public void setControlPropDelta(int i) {
        this.controlPropDirection = i;
    }

    public int getControlPropDelta() {
        return this.controlPropDirection;
    }

    public void doSetKillControlAfterburner() {
        this.bHasAfterburnerControl = false;
    }

    public void setControlProp(float f) {
        if (this.bHasPropControl) {
            this.controlProp = f;
        }
    }

    public void setControlPropAuto(boolean flag) {
        if (this.bHasPropControl) {
            this.bControlPropAuto = flag && this.isAllowsAutoProp();
        }
    }

    public void doSetKillControlProp() {
        this.bHasPropControl = false;
    }

    public void setControlMix(float f) {
        if (this.bHasMixControl) {
            switch (this.mixerType) {
                case 0: // '\0'
                    this.controlMix = f;
                    break;

                case 1: // '\001'
                    this.controlMix = f;
                    if (this.controlMix < 1.0F) {
                        this.controlMix = 1.0F;
                    }
                    break;

                default:
                    this.controlMix = f;
                    break;
            }
        }
    }

    public void doSetKillControlMix() {
        this.bHasMixControl = false;
    }

    public void setControlMagneto(int i) {
        if (this.bHasMagnetoControl) {
            this.controlMagneto = i;
            if (i == 0) {
                this.setEngineStops(this.reference.actor);
            }
        }
    }

    public void setControlCompressor(int i) {
        if (this.bHasCompressorControl) {
            this.controlCompressor = i;
        }
    }

    public void setControlFeather(int i) {
        if (this.bHasFeatherControl) {
            this.controlFeather = i;
            if (this.reference.isPlayers()) {
                HUD.log("EngineFeather" + this.controlFeather);
            }
        }
    }

    public void setControlRadiator(float f) {
        if (this.bHasRadiatorControl) {
            this.controlRadiator = f;
        }
    }

    public void setExtinguisherFire() {
        if (!this.bIsMaster) {
            return;
        }
        if (this.bHasExtinguisherControl) {
            this.reference.AS.setEngineSpecificDamage(this.reference.actor, this.number, 5);
            if (this.reference.AS.astateEngineStates[this.number] > 2) {
                this.reference.AS.setEngineState(this.reference.actor, this.number, World.Rnd().nextInt(1, 2));
            } else if (this.reference.AS.astateEngineStates[this.number] > 0) {
                this.reference.AS.setEngineState(this.reference.actor, this.number, 0);
            }
        }
    }

    public void doSetExtinguisherFire() {
        if (!this.bHasExtinguisherControl) {
            return;
        }
        if (this.reference.AS.bIsAboutToBailout) {
            return;
        }
        this.extinguishers--;
        if (this.extinguishers == 0) {
            this.bHasExtinguisherControl = false;
        }
        this.reference.AS.doSetEngineExtinguisherVisuals(this.number);
        if (this.bIsMaster) {
            if ((this.reference.AS.astateEngineStates[this.number] > 1) && (World.Rnd().nextFloat() < 0.56F)) {
                this.reference.AS.repairEngine(this.number);
            }
            if ((this.reference.AS.astateEngineStates[this.number] > 3) && (World.Rnd().nextFloat() < 0.21F)) {
                this.reference.AS.repairEngine(this.number);
                this.reference.AS.repairEngine(this.number);
            }
            this.tWaterOut -= 4F;
            this.tOilIn -= 4F;
            this.tOilOut -= 4F;
        }
        if (this.reference.isPlayers()) {
            HUD.log("ExtinguishersFired");
        }
    }

    private void computeStage(float f) {
        if (this.stage == _E_STAGE_NOMINAL) {
            return;
        }
        bTFirst = false;
        float f1 = 20F;
        long l = Time.current() - this.timer;
        if ((this.stage > _E_STAGE_NULL) && (this.stage < _E_STAGE_NOMINAL) && (l > this.given)) {
            this.stage++;
            /*
             * TODO: This part controls hiccups during startup.
             * -Rotary and radial engines more prone to hiccups due to greater mass
             * -Manual cranked engines (starter = 1) less likely to start first go
             * -Electric type (starter = 2) is less liking to fail as can be primed during wind-up.
             * -Meanwhile other types such as cartridge (starter = 3) and inertia (starter = 0) won't start if engine isn't properly primed
             * - To change how prone an engine type is to hiccups, increase the nextFloat values
             */
            // Inline engines
            if (((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_HELO_INLINE)) && (this.starter != _S_TYPE_MANUAL)) {
                // Electric type start is less liking to fail as can be primed during wind-up. Other types can fail to start
                if ((this.starter == _S_TYPE_ELECTRIC) && (this.stage > _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 2F)) {
                    this.stage = _E_STAGE_CATCH_UP;
                }
                if (((this.starter == _S_TYPE_INERTIA) || (this.starter == _S_TYPE_CARTRIDGE) || (this.starter == _S_TYPE_BOSCH)) && (this.stage > _E_STAGE_CATCH_UP)) {
                    if ((World.Rnd().nextFloat(0.0F, 10.0F) < 0.25F) && this.reference.isStationedOnGround()) {
                        this.stage = _E_STAGE_NULL;
                    } else if (World.Rnd().nextFloat(0.0F, 10.0F) < 3F) {
                        this.stage = _E_STAGE_CATCH_UP;
                    }
                }
            }
            // Radial engines
            if ((this.type == _E_TYPE_RADIAL) && (this.starter != _S_TYPE_MANUAL)) {
                if (this.starter == _S_TYPE_ELECTRIC) {
                    if ((this.stage == _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 2F)) {
                        this.stage = _E_STAGE_STARTER_ROLL;
                    } else if ((this.stage > _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 3F)) {
                        this.stage = _E_STAGE_CATCH_UP;
                    }
                }
                if ((this.starter == _S_TYPE_INERTIA) || (this.starter == _S_TYPE_CARTRIDGE) || (this.starter == _S_TYPE_BOSCH)) {
                    if ((this.stage > _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 0.25F) && this.reference.isStationedOnGround()) {
                        this.stage = _E_STAGE_NULL;
                    } else if ((this.stage == _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 1F)) {
                        this.stage = _E_STAGE_STARTER_ROLL;
                    } else if ((this.stage > _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 3F)) {
                        this.stage = _E_STAGE_CATCH_UP;
                    }
                }
            }
            // Rotary and hand-cranked engines
            if ((this.starter == _S_TYPE_MANUAL) || (this.type == _E_TYPE_ROTARY)) {
                if ((this.stage > _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 2F) && this.reference.isStationedOnGround()) {
                    this.stage = _E_STAGE_NULL;
                } else if ((this.stage == _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 2F)) {
                    this.stage = _E_STAGE_STARTER_ROLL;
                } else if ((this.stage > _E_STAGE_CATCH_UP) && (World.Rnd().nextFloat(0.0F, 10.0F) < 3F)) {
                    this.stage = _E_STAGE_CATCH_UP;
                }
            }

            this.timer = Time.current();
            l = 0L;
        }
        if (this.oldStage != this.stage) {
            bTFirst = true;
            this.oldStage = this.stage;
        }
        if ((this.stage > _E_STAGE_NULL) && (this.stage < _E_STAGE_NOMINAL)) {
            this.setControlThrottle(0.2F);
        }
        switch (this.stage) {
            case _E_STAGE_NULL:
                if (bTFirst) {
                    this.given = 0x3fffffffffffffffL;
                    this.timer = Time.current();
                }
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                break;

            case _E_STAGE_WAKE_UP:
                if (bTFirst) {
                    if (this.bIsStuck) {
                        this.stage = _E_STAGE_STUCK;
                        return;
                    }
                    if ((this.type == _E_TYPE_ROCKET) || (this.type == _E_TYPE_ROCKETBOOST) || (this.type == _E_TYPE_PVRD)) {
                        this.stage = _E_STAGE_CATCH_FIRE;
                        if (this.reference.isPlayers()) {
                            HUD.log("Starting_Engine");
                        }
                        return;
                    }
                    // TODO:
                    if ((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_RADIAL) || (this.type == _E_TYPE_HELO_INLINE) || (this.type == _E_TYPE_ROTARY)) {
                        if (this.w > this.wMin) {
                            this.stage = _E_STAGE_CATCH_UP;
                            if (this.reference.isPlayers()) {
                                HUD.log("Starting_Engine");
                            }
                            return;
                        }
                        if (!this.bIsAutonomous) {
                            // TODO: Modified by |ZUTI|: disabled position check.
                            // if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
                            if (this.reference.isStationedOnGround()) {
                                this.setControlMagneto(3);
                                if (this.reference.isPlayers()) {
                                    HUD.log("Starting_Engine");
                                }
                            } else {
                                this.doSetEngineStops();
                                if (this.reference.isPlayers()) {
                                    HUD.log("EngineI0");
                                }
                                return;
                            }
                        } else if (this.reference.isPlayers()) {
                            HUD.log("Starting_Engine");
                        }
                    } else if (!this.bIsAutonomous) {
                        // TODO: Modified by |ZUTI|: disabled position check.
                        // if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
                        if (this.reference.isStationedOnGround()) {
                            this.setControlMagneto(3);
                            if (this.reference.isPlayers()) {
                                HUD.log("Starting_Engine");
                            }
                        } else {
                            if (this.reference.getSpeedKMH() < 350F) {
                                this.doSetEngineStops();
                                if (this.reference.isPlayers()) {
                                    HUD.log("EngineI0");
                                }
                                return;
                            }
                            if (this.reference.isPlayers()) {
                                HUD.log("Starting_Engine");
                            }
                        }
                    } else if (this.reference.isPlayers()) {
                        HUD.log("Starting_Engine");
                    }
                    /* TODO: editing "given" in each stage will give you different starting times....test these planes and see the difference */
                    switch (this.type) {
                        case _E_TYPE_ROTARY:
                            this.given = (long) (50F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                        case _E_TYPE_INLINE:
                        case _E_TYPE_HELO_INLINE:
                            switch (this.starter) {
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                    this.given = (long) (1500F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_ELECTRIC:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (50F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case _E_TYPE_RADIAL:
                            switch (this.starter) {
                                case _S_TYPE_INERTIA:
                                    this.given = (long) (2000F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_BOSCH:
                                    this.given = (long) (5000F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_ELECTRIC:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (50F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            this.given = (long) (1000F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                    }
                }
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                this.reference.CT.setMagnetoControl(3);
                this.setControlMagneto(3);
                if (this.starter == _S_TYPE_BOSCH) {
                    this.w = 0;
                } else {
                    this.w = (0.1047F * ((20F * l) / this.given));
                }
                this.setControlThrottle(0.0F);
                break;

            case _E_STAGE_STARTER_ROLL:
                if (bTFirst) {
                    switch (this.type) {
                        case _E_TYPE_ROTARY:
                            this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                        case _E_TYPE_INLINE:
                        case _E_TYPE_HELO_INLINE:
                            switch (this.starter) {
                                case _S_TYPE_INERTIA:
                                    this.given = (long) (5000F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_ELECTRIC:
                                    this.given = (long) (1500F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_BOSCH:
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case _E_TYPE_RADIAL:
                            switch (this.starter) {
                                case _S_TYPE_INERTIA:
                                    this.given = (long) (8000F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_BOSCH:
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_ELECTRIC:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            this.given = (long) (8000F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                    }
                    if (this.bRan) {
                        this.given = (long) (100F + (((this.tOilOutMaxRPM - this.tOilOut) / (this.tOilOutMaxRPM - f1)) * 7900F * World.Rnd().nextFloat(2.0F, 4.2F)));
                        if (this.given > 9000L) {
                            this.given = World.Rnd().nextLong(7800L, 9600L);
                        }
                        if (this.bIsMaster && (World.Rnd().nextFloat() < 0.5F)) {
                            this.stage = 0;
                            this.reference.AS.setEngineStops(this.number);
                        }
                    }
                }
                float spin = 0.1047F * (20F + ((7F * l) / this.given));
                if (this.w > spin) {
                    /*
                     * float divider = 0.0F;
                     * divider += 0.5F;
                     */
                    this.w = 12.564F - (0.3547F * (20F + ((7F * l) / this.given)));
                } else {
                    this.w = spin;
                }
                this.setControlThrottle(0.0F);
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                break;

            case _E_STAGE_CATCH_UP:
                if (bTFirst) {
                    if (this.isnd != null) {
                        this.isnd.onEngineState(this.stage);
                    }
                    if (this.bIsInoperable) {
                        this.stage = 0;
                        this.doSetEngineDies();
                        return;
                    }
                    switch (this.type) {
                        case _E_TYPE_ROTARY:
                            this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                        case _E_TYPE_INLINE:
                        case _E_TYPE_HELO_INLINE:
                            switch (this.starter) {
                                case _S_TYPE_MANUAL:
                                    this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_ELECTRIC:
                                    this.given = (long) (200F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case _E_TYPE_RADIAL:
                            switch (this.starter) {
                                case _S_TYPE_MANUAL:
                                    this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                case _S_TYPE_ELECTRIC:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                    }
                    if (this.bIsMaster && (World.Rnd().nextFloat() < 0.12F) && (((this.tOilOutMaxRPM - this.tOilOut) / (this.tOilOutMaxRPM - f1)) < 0.75F)) {
                        this.reference.AS.setEngineStops(this.number);
                    }
                }
                this.w = 0.1047F * (60F + ((60F * l) / this.given));
                this.setControlThrottle(0.0F);
                if ((this.reference == null) || (this.type == _E_TYPE_JET) || (this.type == _E_TYPE_ROCKET) || (this.type == _E_TYPE_ROCKETBOOST) || (this.type == _E_TYPE_PVRD) || (this.type == _E_TYPE_TOW) || (this.type == _E_TYPE_TURBOPROP)) {
                    break;
                }
                for (int i = 1; i < 32; i++) {
                    try {
                        com.maddox.il2.engine.Hook hook = this.reference.actor.findHook("_Engine" + (this.number + 1) + "EF_" + (i >= 10 ? "" + i : "0" + i));
                        if (hook != null) {
                            Eff3DActor.New(this.reference.actor, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff", -1F);
                        }
                    } catch (Exception exception) {
                    }
                }

            case _E_STAGE_CATCH_ROLL:
                if (bTFirst) {
                    switch (this.type) {
                        case _E_TYPE_ROTARY:
                            this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                        case _E_TYPE_INLINE:
                        case _E_TYPE_HELO_INLINE:
                            switch (this.starter) {
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_ELECTRIC:
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                    this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case _E_TYPE_RADIAL:
                            switch (this.starter) {
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                case _S_TYPE_ELECTRIC:
                                    this.given = (long) (1200F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            this.given = (long) (1000F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                    }
                }
                this.w = 12.564F;
                this.setControlThrottle(0.0F);
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                break;

            case _E_STAGE_CATCH_FIRE:
                if (bTFirst) {
                    switch (this.type) {
                        case _E_TYPE_ROTARY:
                            this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                        case _E_TYPE_INLINE:
                        case _E_TYPE_HELO_INLINE:
                            switch (this.starter) {
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_ELECTRIC:
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                    this.given = (long) (100F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case _E_TYPE_RADIAL:
                            switch (this.starter) {
                                case _S_TYPE_MANUAL:
                                case _S_TYPE_CARTRIDGE:
                                    this.given = (long) (25F * World.Rnd().nextFloat(1.0F, 2.0F));
                                    break;
                                case _S_TYPE_INERTIA:
                                case _S_TYPE_BOSCH:
                                case _S_TYPE_ELECTRIC:
                                    this.given = (long) (800F * World.Rnd().nextFloat(1.0F, 2.0F));;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            this.given = (long) (500F * World.Rnd().nextFloat(1.0F, 2.0F));
                            break;
                    }
                    if (this.bRan && ((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_RADIAL) || (this.type == _E_TYPE_HELO_INLINE) || (this.type == _E_TYPE_ROTARY))) {
                        if (((this.tOilOutMaxRPM - this.tOilOut) / (this.tOilOutMaxRPM - f1)) > 0.75F) {
                            if ((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_HELO_INLINE)) {
                                if (this.bIsMaster && (this.getReadyness() > 0.75F) && (World.Rnd().nextFloat() < 0.25F)) {
                                    this.setReadyness(this.getReadyness() - 0.05F);
                                }
                            } else
                            // TODO: Stops random engine deaths on startup
                            // if(type == _E_TYPE_RADIAL && bIsMaster && World.Rnd().nextFloat() < 0.1F)
                            // reference.AS.setEngineDies(reference.actor, number);
                            if (this.bIsMaster && (World.Rnd().nextFloat() < 0.1F)) {
                                this.reference.AS.setEngineStops(this.number);
                            }
                        }
                    }
                    this.bRan = true;
                }
                this.w = 0.1047F * (120F + ((120F * l) / this.given));
                this.setControlThrottle(0.2F);
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                break;

            case _E_STAGE_NOMINAL:
                if (bTFirst) {
                    this.given = -1L;
                    this.reference.AS.setEngineRunning(this.number);
                }
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                break;

            case _E_STAGE_DEAD:
            case _E_STAGE_STUCK:
                if (bTFirst) {
                    this.given = -1L;
                }
                this.setReadyness(0.0F);
                this.setControlMagneto(0);
                if (this.isnd != null) {
                    this.isnd.onEngineState(this.stage);
                }
                break;

            default:
                return;
        }
    }

    private void computeFuel(float f) {
        tmpF = 0.0F;
        if (this.stage == 6) {
            float f1;
            double d;
            switch (this.type) {
                case 0: // '\0'
                case 1: // '\001'
                case 7: // '\007'
                    d = this.momForFuel * this.w * 0.0010499999999999999D;
                    f1 = (float) d / this.horsePowers;
                    if (d < (this.horsePowers * 0.050000000000000003D)) {
                        d = this.horsePowers * 0.050000000000000003D;
                    }
                    break;

                default:
                    d = this.thrustMax * (f1 = this.getPowerOutput());
                    if (d < (this.thrustMax * 0.050000000000000003D)) {
                        d = this.thrustMax * 0.050000000000000003D;
                    }
                    break;
            }
            if (f1 < 0.0F) {
                f1 = 0.0F;
            }
            double d1;
            if (f1 <= 0.5F) {
                d1 = f1 - 0.5F;
                d1 = (d1 * d1 * this.fuelConsumption0M) + this.FuelConsumptionP05;
            } else if (f1 <= 1.0D) {
                d1 = f1 - 0.5F;
                d1 = (d1 * d1 * this.fuelConsumption1M) + this.FuelConsumptionP05;
            } else {
                float f3 = f1 - 1.0F;
                if (f3 > 0.1F) {
                    f3 = 0.1F;
                }
                f3 *= 10F;
                d1 = this.FuelConsumptionP1 + ((this.FuelConsumptionPMAX - this.FuelConsumptionP1) * f3);
            }
            d1 /= 3600D;
            switch (this.type) {
                case 5: // '\005'
                default:
                    break;

                case 0: // '\0'
                case 1: // '\001'
                case 7: // '\007'
                    d1 *= 0.8F + ((0.2F * this.w) / this.wWEP);
                    float f4 = (float) (d1 * d);
                    tmpF = f4 * f;
                    double d2 = f4 * 4.4E+007F;
                    double d3 = f4 * 15.7F;
                    double d4 = 1010D * d3 * 700D;
                    d *= 746D;
                    this.Ptermo = (float) (d2 - d - d4);
                    break;

                case 2: // '\002'
                    tmpF = (float) (d1 * d * f);
                    break;

                case 10: // '\002'
                    tmpF = (float) (d1 * d * f);
                    break;

                case 3: // '\003'
                    if ((this.reference.actor instanceof BI_1) || (this.reference.actor instanceof BI_6)) {
                        tmpF = 1.8F * this.getPowerOutput() * f;
                        break;
                    }
                    if (this.reference.actor instanceof MXY_7) {
                        tmpF = 0.5F * this.getPowerOutput() * f;
                    } else {
                        tmpF = 2.5777F * this.getPowerOutput() * f;
                    }
                    break;

                case 4: // '\004'
                    tmpF = 1.432056F * this.getPowerOutput() * f;
                    tmpB = this.reference.M.requestNitro(tmpF);
                    tmpF = 0.0F;
                    if (tmpB || !this.bIsMaster) {
                        break;
                    }
                    this.setEngineStops(this.reference.actor);
                    if (this.reference.isPlayers() && (this.engineNoFuelHUDLogId == -1)) {
                        this.engineNoFuelHUDLogId = HUD.makeIdLog();
                        HUD.log(this.engineNoFuelHUDLogId, "EngineNoFuel");
                    }
                    return;

                case 6: // '\006'
                    tmpF = (float) (d1 * d * f);
                    tmpB = this.reference.M.requestNitro(tmpF);
                    tmpF = 0.0F;
                    if (tmpB || !this.bIsMaster) {
                        break;
                    }
                    this.setEngineStops(this.reference.actor);
                    if (this.reference.isPlayers() && (this.engineNoFuelHUDLogId == -1)) {
                        this.engineNoFuelHUDLogId = HUD.makeIdLog();
                        HUD.log(this.engineNoFuelHUDLogId, "EngineNoFuel");
                    }
                    return;
            }
        }
        tmpB = this.reference.M.requestFuel(tmpF);
        if (!tmpB && this.bIsMaster) {
            this.setEngineStops(this.reference.actor);
            this.reference.setCapableOfACM(false);
            this.reference.setCapableOfTaxiing(false);
            if (this.reference.isPlayers() && (this.engineNoFuelHUDLogId == -1)) {
                this.engineNoFuelHUDLogId = HUD.makeIdLog();
                HUD.log(this.engineNoFuelHUDLogId, "EngineNoFuel");
            }
        }
        if (this.controlAfterburner) {
            switch (this.afterburnerType) {
                case 3: // '\003'
                case 6: // '\006'
                case 7: // '\007'
                case 8: // '\b'
                default:
                    break;

                case 1: // '\001'
                    if ((this.controlThrottle > 1.0F) && !this.reference.M.requestNitro(0.044872F * f) && this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode() && World.cur().diffCur.Vulnerability) {
                        this.setReadyness(this.reference.actor, this.getReadyness() - (0.01F * f));
                    }
                    break;

                case 2: // '\002'
                    if (this.reference.M.requestNitro(0.044872F * f)) {
                        ;
                    }
                    break;

                case 5: // '\005'
                    if (this.reference.M.requestNitro(0.044872F * f)) {
                        ;
                    }
                    break;

                case 9: // '\t'
                    if (this.reference.M.requestNitro(0.044872F * f)) {
                        ;
                    }
                    break;

                case 4: // '\004'
                    if (this.reference.M.requestNitro(0.044872F * f)) {
                        break;
                    }
                    this.reference.CT.setAfterburnerControl(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                    break;
            }
        }
    }

    private void computeReliability(float f) {
        if (this.stage != 6) {
            return;
        }
        float f1 = this.controlThrottle;
        if (this.engineBoostFactor > 1.0F) {
            f1 *= 0.9090909F;
        }
        switch (this.type) {
            default:
                this.zatizeni = f1;
                this.zatizeni = this.zatizeni * this.zatizeni;
                this.zatizeni = this.zatizeni * this.zatizeni;
                this.zatizeni *= f * 6.1984262178699901E-005D;
                if (this.zatizeni > World.Rnd().nextDouble(0.0D, 1.0D)) {
                    int i = World.Rnd().nextInt(0, 9);
                    if (i < 2) {
                        this.reference.AS.hitEngine(this.reference.actor, this.number, 3);
                        Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - smoke");
                    } else {
                        this.setCyliderKnockOut(this.reference.actor, World.Rnd().nextInt(0, 3));
                        Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - power loss");
                    }
                }
                break;

            case 0: // '\0'
            case 1: // '\001'
            case 7: // '\007'
                this.zatizeni = this.coolMult * f1;
                this.zatizeni *= this.w / this.wWEP;
                this.zatizeni = this.zatizeni * this.zatizeni;
                this.zatizeni = this.zatizeni * this.zatizeni;
                double d = this.zatizeni * f * 1.4248134284734321E-005D;
                if (d <= World.Rnd().nextDouble(0.0D, 1.0D)) {
                    break;
                }
                int j = World.Rnd().nextInt(0, 19);
                if (j < 10) {
                    this.reference.AS.setEngineCylinderKnockOut(this.reference.actor, this.number, 1);
                    Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - cylinder");
                    break;
                }
                if (j < 12) {
                    if (j < 11) {
                        this.reference.AS.setEngineMagnetoKnockOut(this.reference.actor, this.number, 0);
                        Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - mag1");
                    } else {
                        this.reference.AS.setEngineMagnetoKnockOut(this.reference.actor, this.number, 1);
                        Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - mag2");
                    }
                    break;
                }
                if (j < 14) {
                    this.reference.AS.setEngineDies(this.reference.actor, this.number);
                    Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - dead");
                    break;
                }
                if (j < 15) {
                    this.reference.AS.setEngineStuck(this.reference.actor, this.number);
                    Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - stuck");
                    break;
                }
                if (j < 17) {
                    this.setKillPropAngleDevice(this.reference.actor);
                    Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - propAngler");
                } else {
                    this.reference.AS.hitOil(this.reference.actor, this.number);
                    Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - oil");
                }
                break;
        }
    }

    private void computeTemperature(float f) {
        float f1 = Math.max(0.0F, this.reference.getSpeedKMH());
        float f2 = Atmosphere.temperature((float) this.reference.Loc.z) - 273.15F;
        float f3 = this.controlThrottle;
        if (!this.reference.isPlayers()) {
            f3 *= 1.1F;
        }
        float f4 = f3;
        float f5 = 0.8F + (60F / (50F + f1));
        float f6 = 0.45F;
        if (this.engineBoostFactor > 1.0F) {
            f4 = (f3 + (Math.max(f3 - 1.0F, 0.0F) * (this.engineBoostFactor - 1.1F) * 10F)) / this.engineBoostFactor;
        }
        if (!this.controlAfterburner && (this.engineAfterburnerBoostFactor > 1.0F) && (this.afterburnerType != 1) && (this.afterburnerType != 9) && (this.afterburnerType != 4)) {
            f4 /= this.engineAfterburnerBoostFactor;
        }
        float f7 = f4;
        if ((this.type == _E_TYPE_INLINE) || (this.type == _E_TYPE_RADIAL) || (this.type == _E_TYPE_HELO_INLINE)) {
            f6 = 0.45F / Atmosphere.density((float) this.reference.Loc.z);
        }
        float f8 = this.coolMult * f4;
        if (World.cur().diffCur.ComplexEManagement && this.reference.isPlayers() && ((RealFlightModel) this.reference).isRealMode()) {
            f7 = this.w / this.wWEP;
        }
        if (!this.reference.isPlayers()) {
            f7 = (this.w * 1.02F) / this.wWEP;
        }
        if (this.stage == 6) {
            float f9 = f2 + (this.tOilOutMaxRPM * (1.0F - (0.15F * this.controlRadiator)) * (0.65F + (0.45F / Atmosphere.density((float) this.reference.Loc.z))) * (1.0F + (this.reference.AS.astateOilStates[this.number] * 0.35F)) * 1.2F * f7 * f7 * (0.7F + (0.3F * f8)) * f5);
            this.tOilOut += (f9 - this.tOilOut) * f * this.tChangeSpeed;
            float f13 = this.tOilOut * 0.8F;
            this.tOilIn += (f13 - this.tOilIn) * f * 0.5F;
            if (this.type == _E_TYPE_JET) {
                f6 = 0.35F + (0.1F / Atmosphere.density((float) this.reference.Loc.z));
            }
            f13 = f2 + (this.tWaterMaxRPM * (1.0F - (0.15F * this.controlRadiator)) * (0.65F + f6) * (0.6F + (0.4F * f7)) * (0.1F + (1.1F * f8)) * f5 * (1.1F - (0.1F * this.controlMix)));
            this.tWaterOut += (f13 - this.tWaterOut) * f * this.tChangeSpeed;
        } else {
            float f10 = f2;
            this.tOilOut += (f10 - this.tOilOut) * f * this.tChangeSpeed * (0.2F + (0.2F * this.controlRadiator));
            float f14 = this.tOilOut;
            this.tOilIn += (f14 - this.tOilIn) * f * 0.5F;
            f14 = f2;
            this.tWaterOut += (f14 - this.tWaterOut) * f * this.tChangeSpeed * (0.2F + (0.2F * this.controlRadiator));
        }
        if (!this.reference.isPlayers()) {
            return;
        }
        if (World.cur().diffCur.Engine_Overheat && ((this.tWaterOut > this.tWaterCritMax) || (this.tOilOut > this.tOilCritMax))) {
            if (heatStringID == -1) {
                heatStringID = HUD.makeIdLog();
            }
            if (this.reference.isPlayers()) {
                HUD.log(heatStringID, "EngineOverheat");
            }
            if (this.tWaterOut > this.tWaterCritMax) {
                float f11 = (this.tWaterOut / this.tWaterCritMax) - 1.0F;
                f11 *= f11 * f11 * f * 1000F;
                float f15 = World.Rnd().nextFloat() * this.timeOverheat;
                if (f11 > f15) {
                    switch (this.type) {
                        default:
                            int i = World.Rnd().nextInt(0, 9);
                            if (i < 2) {
                                this.reference.AS.hitEngine(this.reference.actor, this.number, 3);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - smoke");
                            } else {
                                this.setCyliderKnockOut(this.reference.actor, World.Rnd().nextInt(0, 3));
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - power loss");
                            }
                            break;

                        case 0: // '\0'
                        case 1: // '\001'
                        case 7: // '\007'
                            int k = World.Rnd().nextInt(0, 99);
                            if (k < 50) {
                                this.reference.AS.setEngineCylinderKnockOut(this.reference.actor, this.number, 1);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - cylinder");
                                break;
                            }
                            if (k < 65) {
                                this.reference.AS.hitOil(this.reference.actor, this.number);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - oil");
                                break;
                            }
                            if (k < 94) {
                                this.reference.AS.setEngineCylinderKnockOut(this.reference.actor, this.number, 1);
                                int i1 = 1;
                                do {
                                    if (i1 >= 11) {
                                        break;
                                    }
                                    if (World.Rnd().nextFloat() >= 0.8F) {
                                        try {
                                            com.maddox.il2.engine.Hook hook = this.reference.actor.findHook("_Engine" + (this.number + 1) + "EF_" + (i1 >= 10 ? "" + i1 : "0" + i1));
                                            if (hook != null) {
                                                Eff3DActor.New(this.reference.actor, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff", -1F);
                                            }
                                        } catch (Exception exception) {
                                        }
                                        break;
                                    }
                                    i1++;
                                } while (true);
                                this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - cylinder&smoke");
                                break;
                            }
                            if (k < 95) {
                                this.reference.AS.setEngineCylinderKnockOut(this.reference.actor, this.number, 1);
                                this.reference.AS.hitEngine(this.reference.actor, this.number, 10);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - fire");
                                break;
                            }
                            if (k < 97) {
                                this.setKillCompressor(this.reference.actor);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - Supercharger");
                                break;
                            }
                            if (k < 98) {
                                this.reference.AS.setEngineDies(this.reference.actor, this.number);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - dead");
                            } else {
                                this.reference.AS.setEngineStuck(this.reference.actor, this.number);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - stuck");
                            }
                            break;
                    }
                }
            }
            if (this.tOilOut > this.tOilCritMax) {
                float f12 = (this.tOilOut / this.tOilCritMax) - 1.0F;
                f12 *= f12 * f12 * f * 1000F;
                float f16 = World.Rnd().nextFloat() * this.timeOverheat;
                if (f12 > f16) {
                    switch (this.type) {
                        default:
                            int j = World.Rnd().nextInt(0, 9);
                            if (j < 2) {
                                this.reference.AS.hitEngine(this.reference.actor, this.number, 3);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - smoke");
                            } else {
                                this.setCyliderKnockOut(this.reference.actor, World.Rnd().nextInt(0, 3));
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - power loss");
                            }
                            break;

                        case 0: // '\0'
                        case 1: // '\001'
                        case 7: // '\007'
                            int l = World.Rnd().nextInt(0, 99);
                            if (l < 10) {
                                this.reference.AS.setEngineCylinderKnockOut(this.reference.actor, this.number, 1);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - cylinder");
                                break;
                            }
                            if (l < 60) {
                                this.reference.AS.hitOil(this.reference.actor, this.number);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - oil");
                                break;
                            }
                            if (l < 94) {
                                this.setReadyness(this.readyness * 0.95F);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - damaged bearing or something");
                                break;
                            }
                            if (l < 95) {
                                this.reference.AS.hitOil(this.reference.actor, this.number);
                                this.reference.AS.hitEngine(this.reference.actor, this.number, 3);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - fire");
                                break;
                            }
                            if (l < 96) {
                                this.setKillCompressor(this.reference.actor);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - Supercharger");
                                break;
                            }
                            if (l < 97) {
                                this.reference.AS.setEngineDies(this.reference.actor, this.number);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - dead");
                            } else {
                                this.reference.AS.setEngineStuck(this.reference.actor, this.number);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - stuck");
                            }
                            break;
                    }
                }
            }
        }
    }

    public void updateRadiator(float f) {
        if ((this.reference.actor instanceof GLADIATOR) || (this.reference.actor instanceof DXXI_DK) || (this.reference.actor instanceof DXXI_DU) || (this.reference.actor instanceof DXXI_SARJA3_EARLY) || (this.reference.actor instanceof DXXI_SARJA3_LATE) || (this.reference.actor instanceof DXXI_SARJA3_SARVANTO)) {
            this.controlRadiator = 0.0F;
            return;
        }
        if ((this.reference.actor instanceof P_51) || (this.reference.actor instanceof P_38) || (this.reference.actor instanceof YAK_3) || (this.reference.actor instanceof YAK_3P) || (this.reference.actor instanceof YAK_9M) || (this.reference.actor instanceof YAK_9U) || (this.reference.actor instanceof YAK_9UT) || (this.reference.actor instanceof P_63C)) {
            if (this.tOilOut > this.tOilOutMaxRPM) {
                this.controlRadiator += 0.1F * f;
                if (this.controlRadiator > 1.0F) {
                    this.controlRadiator = 1.0F;
                }
            } else {
                this.controlRadiator = 1.0F - (this.reference.getSpeed() / this.reference.VmaxH);
                if (this.controlRadiator < 0.0F) {
                    this.controlRadiator = 0.0F;
                }
            }
            return;
        }
        if ((this.reference.actor instanceof SPITFIRE9) || (this.reference.actor instanceof SPITFIRE8) || (this.reference.actor instanceof SPITFIRE8CLP)) {
            this.controlRadiator = ((this.tWaterOut - this.tWaterCritMax) + 20F) / 20F;
            if (this.controlRadiator < 0.0F) {
                this.controlRadiator = 0.0F;
            }
            if (this.controlRadiator > 1.0F) {
                this.controlRadiator = 1.0F;
            }
            return;
        }
        switch (this.propAngleDeviceType) {
            case 3: // '\003'
            case 4: // '\004'
            default:
                this.controlRadiator = 1.0F - this.getPowerOutput();
                break;

            case 5: // '\005'
            case 6: // '\006'
                this.controlRadiator = 1.0F - (this.reference.getSpeed() / this.reference.VmaxH);
                if (this.controlRadiator < 0.0F) {
                    this.controlRadiator = 0.0F;
                }
                break;

            case 1: // '\001'
            case 2: // '\002'
                if (this.controlRadiator > (1.0F - this.getPowerOutput())) {
                    this.controlRadiator -= 0.15F * f;
                    if (this.controlRadiator < 0.0F) {
                        this.controlRadiator = 0.0F;
                    }
                } else {
                    this.controlRadiator += 0.15F * f;
                }
                break;

            case 8: // '\b'
                if (this.type == _E_TYPE_INLINE) {
                    if (this.tOilOut > this.tOilOutMaxRPM) {
                        this.controlRadiator += 0.1F * f;
                        if (this.controlRadiator > 1.0F) {
                            this.controlRadiator = 1.0F;
                        }
                        break;
                    }
                    if (this.tOilOut >= (this.tOilOutMaxRPM - 10F)) {
                        break;
                    }
                    this.controlRadiator -= 0.1F * f;
                    if (this.controlRadiator < 0.0F) {
                        this.controlRadiator = 0.0F;
                    }
                    break;
                }
                if (this.controlRadiator > (1.0F - this.getPowerOutput())) {
                    this.controlRadiator -= 0.15F * f;
                    if (this.controlRadiator < 0.0F) {
                        this.controlRadiator = 0.0F;
                    }
                } else {
                    this.controlRadiator += 0.15F * f;
                }
                break;

            case 7: // '\007'
                if (this.tOilOut > this.tOilOutMaxRPM) {
                    this.controlRadiator += 0.1F * f;
                    if (this.controlRadiator > 1.0F) {
                        this.controlRadiator = 1.0F;
                    }
                    break;
                }
                this.controlRadiator = 1.0F - (this.reference.getSpeed() / this.reference.VmaxH);
                if (this.controlRadiator < 0.0F) {
                    this.controlRadiator = 0.0F;
                }
                break;
        }
    }

    private void computeForces(float f) {
        switch (this.type) {
            case 0: // '\0'
            case 1: // '\001'
                // TODO: Next case needed for rotary engines to work
            case 9: // '\009'
            case 7: // '\007'
                if (Math.abs(this.w) < 1E-005F) {
                    this.propPhiW = 1.570796F;
                } else if (this.type == _E_TYPE_HELO_INLINE) {
                    this.propPhiW = (float) Math.atan(Math.abs(this.reference.Vflow.x) / (this.w * this.propReductor * this.propr));
                } else {
                    this.propPhiW = (float) Math.atan(this.reference.Vflow.x / (this.w * this.propReductor * this.propr));
                }
                this.propAoA = this.propPhi - this.propPhiW;
                if (this.type == _E_TYPE_HELO_INLINE) {
                    this.computePropForces(this.w * this.propReductor, (float) Math.abs(this.reference.Vflow.x), this.propPhi, this.propAoA, this.reference.getAltitude());
                } else {
                    this.computePropForces(this.w * this.propReductor, (float) this.reference.Vflow.x, this.propPhi, this.propAoA, this.reference.getAltitude());
                }
                switch (this.propAngleDeviceType) {
                    case 3: // '\003'
                    case 4: // '\004'
                        float f8 = this.controlThrottle;
                        if (f8 > 1.0F) {
                            f8 = 1.0F;
                        }
                        this.compressorManifoldThreshold = 0.5F + ((this.compressorRPMtoWMaxATA - 0.5F) * f8);
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.bControlPropAuto) {
                                this.propTarget = this.propPhiW + this.propAoA0;
                            } else {
                                this.propTarget = this.propPhiMax - (this.controlProp * (this.propPhiMax - this.propPhiMin));
                            }
                        } else if (this.propAngleDeviceType == 3) {
                            this.propTarget = 0.0F;
                        } else {
                            this.propTarget = 3.141593F;
                        }
                        break;

                    case 9: // '\t'
                        if (this.bControlPropAuto) {
                            float f15 = this.propAngleDeviceMaxParam;
                            if (this.controlAfterburner) {
                                f15 = this.propAngleDeviceAfterburnerParam;
                            }
                            this.controlProp += (this.controlPropDirection * f) / 5F;
                            if (this.controlProp > 1.0F) {
                                this.controlProp = 1.0F;
                            } else if (this.controlProp < 0.0F) {
                                this.controlProp = 0.0F;
                            }
                            float f21 = this.propAngleDeviceMinParam + ((f15 - this.propAngleDeviceMinParam) * this.controlProp);
                            float f25 = this.controlThrottle;
                            if (f25 > 1.0F) {
                                f25 = 1.0F;
                            }
                            this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + ((this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam) * f25)));
                            if (this.isPropAngleDeviceOperational()) {
                                if (this.w < f21) {
                                    f21 = Math.min(1.0F, (0.01F * (f21 - this.w)) - (0.012F * this.aw));
                                    this.propTarget -= f21 * this.getPropAngleDeviceSpeed() * f;
                                } else {
                                    f21 = Math.min(1.0F, (0.01F * (this.w - f21)) + (0.012F * this.aw));
                                    this.propTarget += f21 * this.getPropAngleDeviceSpeed() * f;
                                }
                                if ((this.stage == 6) && (this.propTarget < (this.propPhiW - 0.12F))) {
                                    this.propTarget = this.propPhiW - 0.12F;
                                    if (this.propPhi < this.propTarget) {
                                        this.propPhi += 0.2F * f;
                                    }
                                }
                            } else {
                                this.propTarget = this.propPhi;
                            }
                        } else {
                            this.compressorManifoldThreshold = 0.5F + ((this.compressorRPMtoWMaxATA - 0.5F) * (this.controlThrottle <= 1.0F ? this.controlThrottle : 1.0F));
                            this.propTarget = this.propPhi;
                            if (this.isPropAngleDeviceOperational()) {
                                if (this.controlPropDirection > 0) {
                                    this.propTarget = this.propPhiMin;
                                } else if (this.controlPropDirection < 0) {
                                    this.propTarget = this.propPhiMax;
                                }
                            }
                        }
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                        if (this.bControlPropAuto) {
                            if (this.engineBoostFactor > 1.0F) {
                                this.controlProp = 0.75F + (0.227272F * this.controlThrottle);
                            } else {
                                this.controlProp = 0.75F + (0.25F * this.controlThrottle);
                            }
                        }
                        float f16 = this.propAngleDeviceMaxParam;
                        if (this.controlAfterburner && (!this.bWepRpmInLowGear || (this.controlCompressor != this.compressorMaxStep))) {
                            f16 = this.propAngleDeviceAfterburnerParam;
                        }
                        float f1 = this.propAngleDeviceMinParam + ((f16 - this.propAngleDeviceMinParam) * this.controlProp);
                        float f9 = this.controlThrottle;
                        if (f9 > 1.0F) {
                            f9 = 1.0F;
                        }
                        this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + ((this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam) * f9)));
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.w < f1) {
                                f1 = Math.min(1.0F, (0.01F * (f1 - this.w)) - (0.012F * this.aw));
                                this.propTarget -= f1 * this.getPropAngleDeviceSpeed() * f;
                            } else {
                                f1 = Math.min(1.0F, (0.01F * (this.w - f1)) + (0.012F * this.aw));
                                this.propTarget += f1 * this.getPropAngleDeviceSpeed() * f;
                            }
                            if ((this.stage == 6) && (this.propTarget < (this.propPhiW - 0.12F))) {
                                this.propTarget = this.propPhiW - 0.12F;
                                if (this.propPhi < this.propTarget) {
                                    this.propPhi += 0.2F * f;
                                }
                            }
                        } else if (this.propAngleDeviceType == 1) {
                            this.propTarget = 0.0F;
                        } else {
                            this.propTarget = 1.5708F;
                        }
                        break;

                    case 7: // '\007'
                        float f22 = this.controlThrottle;
                        if (this.engineBoostFactor > 1.0F) {
                            f22 = 0.9090909F * this.controlThrottle;
                        }
                        float f17 = this.propAngleDeviceMaxParam;
                        if (this.controlAfterburner) {
                            if (this.afterburnerType == 1) {
                                if (this.controlThrottle > 1.0F) {
                                    f17 = this.propAngleDeviceMaxParam + (10F * (this.controlThrottle - 1.0F) * (this.propAngleDeviceAfterburnerParam - this.propAngleDeviceMaxParam));
                                }
                            } else {
                                f17 = this.propAngleDeviceAfterburnerParam;
                            }
                        }
                        float f2 = this.propAngleDeviceMinParam + ((f17 - this.propAngleDeviceMinParam) * f22);
                        float f10 = this.controlThrottle;
                        if (f10 > 1.0F) {
                            f10 = 1.0F;
                        }
                        this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + ((f17 - this.propAngleDeviceMinParam) * f10)));
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.bControlPropAuto) {
                                if (this.w < f2) {
                                    f2 = Math.min(1.0F, (0.01F * (f2 - this.w)) - (0.012F * this.aw));
                                    this.propTarget -= f2 * this.getPropAngleDeviceSpeed() * f;
                                } else {
                                    f2 = Math.min(1.0F, (0.01F * (this.w - f2)) + (0.012F * this.aw));
                                    this.propTarget += f2 * this.getPropAngleDeviceSpeed() * f;
                                }
                                if ((this.stage == 6) && (this.propTarget < (this.propPhiW - 0.12F))) {
                                    this.propTarget = this.propPhiW - 0.12F;
                                    if (this.propPhi < this.propTarget) {
                                        this.propPhi += 0.2F * f;
                                    }
                                }
                                if (this.propTarget < (this.propPhiMin + (float) Math.toRadians(3D))) {
                                    this.propTarget = this.propPhiMin + (float) Math.toRadians(3D);
                                }
                            } else {
                                this.propTarget = ((1.0F - (f * 0.1F)) * this.propTarget) + (f * 0.1F * (this.propPhiMax - (this.controlProp * (this.propPhiMax - this.propPhiMin))));
                                if (this.w > (1.02F * this.wMax)) {
                                    this.wMaxAllowed = (1.0F - (4E-007F * (this.w - (1.02F * this.wMax)))) * this.wMaxAllowed;
                                }
                                if (this.w > this.wMax) {
                                    float f26 = this.w - this.wMax;
                                    f26 *= f26;
                                    float f27 = 1.0F - (0.001F * f26);
                                    if (f27 < 0.0F) {
                                        f27 = 0.0F;
                                    }
                                    this.propForce *= f27;
                                }
                            }
                        }
                        break;

                    case 8: // '\b'
                        float f23 = this.controlThrottle;
                        if (this.engineBoostFactor > 1.0F) {
                            f23 = 0.9090909F * this.controlThrottle;
                        }
                        float f18 = this.propAngleDeviceMaxParam;
                        if (this.controlAfterburner) {
                            if (this.afterburnerType == 1) {
                                if (this.controlThrottle > 1.0F) {
                                    f18 = this.propAngleDeviceMaxParam + (10F * (this.controlThrottle - 1.0F) * (this.propAngleDeviceAfterburnerParam - this.propAngleDeviceMaxParam));
                                }
                            } else {
                                f18 = this.propAngleDeviceAfterburnerParam;
                            }
                        }
                        float f3 = this.propAngleDeviceMinParam + ((f18 - this.propAngleDeviceMinParam) * f23) + (this.bControlPropAuto ? 0.0F : -25F + (50F * this.controlProp));
                        float f11 = this.controlThrottle;
                        if (f11 > 1.0F) {
                            f11 = 1.0F;
                        }
                        this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + ((f18 - this.propAngleDeviceMinParam) * f11)));
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.w < f3) {
                                f3 = Math.min(1.0F, (0.01F * (f3 - this.w)) - (0.012F * this.aw));
                                this.propTarget -= f3 * this.getPropAngleDeviceSpeed() * f;
                            } else {
                                f3 = Math.min(1.0F, (0.01F * (this.w - f3)) + (0.012F * this.aw));
                                this.propTarget += f3 * this.getPropAngleDeviceSpeed() * f;
                            }
                            if ((this.stage == 6) && (this.propTarget < (this.propPhiW - 0.12F))) {
                                this.propTarget = this.propPhiW - 0.12F;
                                if (this.propPhi < this.propTarget) {
                                    this.propPhi += 0.2F * f;
                                }
                            }
                            if (this.propTarget < (this.propPhiMin + (float) Math.toRadians(3D))) {
                                this.propTarget = this.propPhiMin + (float) Math.toRadians(3D);
                            }
                        }
                        break;

                    case 6: // '\006'
                        float f12 = this.controlThrottle;
                        if (f12 > 1.0F) {
                            f12 = 1.0F;
                        }
                        this.compressorManifoldThreshold = 0.5F + ((this.compressorRPMtoWMaxATA - 0.5F) * f12);
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.bControlPropAuto) {
                                float f4 = 25F + ((this.wMax - 25F) * (0.25F + (0.75F * this.controlThrottle)));
                                if (this.w < f4) {
                                    f4 = Math.min(1.0F, (0.01F * (f4 - this.w)) - (0.012F * this.aw));
                                    this.propTarget -= f4 * this.getPropAngleDeviceSpeed() * f;
                                } else {
                                    f4 = Math.min(1.0F, (0.01F * (this.w - f4)) + (0.012F * this.aw));
                                    this.propTarget += f4 * this.getPropAngleDeviceSpeed() * f;
                                }
                                if ((this.stage == 6) && (this.propTarget < (this.propPhiW - 0.12F))) {
                                    this.propTarget = this.propPhiW - 0.12F;
                                    if (this.propPhi < this.propTarget) {
                                        this.propPhi += 0.2F * f;
                                    }
                                }
                                this.controlProp = (this.propAngleDeviceMaxParam - this.propTarget) / (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam);
                                if (this.controlProp < 0.0F) {
                                    this.controlProp = 0.0F;
                                }
                                if (this.controlProp > 1.0F) {
                                    this.controlProp = 1.0F;
                                }
                            } else {
                                this.propTarget = this.propAngleDeviceMaxParam - (this.controlProp * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam));
                            }
                        }
                        break;

                    case 5: // '\005'
                        float f13 = this.controlThrottle;
                        if (f13 > 1.0F) {
                            f13 = 1.0F;
                        }
                        this.compressorManifoldThreshold = 0.5F + ((this.compressorRPMtoWMaxATA - 0.5F) * f13);
                        if (this.bControlPropAuto) {
                            if (this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode()) {
                                if (World.cur().diffCur.ComplexEManagement) {
                                    this.controlProp = -this.controlThrottle;
                                } else {
                                    this.controlProp = -Aircraft.cvt(this.reference.getSpeed(), this.reference.Vmin, this.reference.Vmax, 0.0F, 1.0F);
                                }
                            } else {
                                this.controlProp = -Aircraft.cvt(this.reference.getSpeed(), this.reference.Vmin, this.reference.Vmax, 0.0F, 1.0F);
                            }
                        }
                        this.propTarget = this.propAngleDeviceMaxParam - (this.controlProp * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam));
                        this.propPhi = this.propTarget;
                        break;
                }
                if ((this.controlFeather == 1) && this.bHasFeatherControl && this.isPropAngleDeviceOperational()) {
                    this.propTarget = 1.55F;
                }
                if (this.propPhi > this.propTarget) {
                    float f5 = Math.min(1.0F, 157.2958F * (this.propPhi - this.propTarget));
                    this.propPhi -= f5 * this.getPropAngleDeviceSpeed() * f;
                } else if (this.propPhi < this.propTarget) {
                    float f6 = Math.min(1.0F, 157.2958F * (this.propTarget - this.propPhi));
                    this.propPhi += f6 * this.getPropAngleDeviceSpeed() * f;
                }
                if (this.propTarget > this.propPhiMax) {
                    this.propTarget = this.propPhiMax;
                } else if (this.propTarget < this.propPhiMin) {
                    this.propTarget = this.propPhiMin;
                }
                if ((this.propPhi > this.propPhiMax) && (this.controlFeather == 0)) {
                    this.propPhi = this.propPhiMax;
                } else if (this.propPhi < this.propPhiMin) {
                    this.propPhi = this.propPhiMin;
                }
                this.engineMoment = this.getN();
                float f14 = this.getCompressorMultiplier(f);
                this.engineMoment *= f14;
                this.momForFuel = this.engineMoment;
                this.engineMoment *= this.getReadyness();
                this.engineMoment *= this.getMagnetoMultiplier();
                this.engineMoment *= this.getMixMultiplier();
                this.engineMoment *= this.getStageMultiplier();
                this.engineMoment *= this.getDistabilisationMultiplier();
                this.engineMoment += this.getFrictionMoment(f);
                float f19 = this.engineMoment - this.propMoment;
                this.aw = f19 / (this.propI + this.engineI);
                if (this.aw > 0.0F) {
                    this.aw *= this.engineAcceleration;
                }
                this.oldW = this.w;
                this.w += this.aw * f;
                if (this.w < 0.0F) {
                    this.w = 0.0F;
                }
                if (this.w > (this.wMaxAllowed + this.wMaxAllowed)) {
                    this.w = this.wMaxAllowed + this.wMaxAllowed;
                }
                if (this.oldW == 0.0F) {
                    if (this.w < (10F * this.fricCoeffT)) {
                        this.w = 0.0F;
                    }
                } else if (this.w < (2.0F * this.fricCoeffT)) {
                    this.w = 0.0F;
                }
                if (this.reference.isPlayers() && World.cur().diffCur.Torque_N_Gyro_Effects && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode()) {
                    this.propIW.set(this.propI * this.w * this.propReductor, 0.0D, 0.0D);
                    if (this.propDirection == 1) {
                        this.propIW.x = -this.propIW.x;
                    }
                    this.engineTorque.set(0.0F, 0.0F, 0.0F);
                    float f24 = this.propI * this.aw * this.propReductor;
                    if (this.propDirection == 0) {
                        this.engineTorque.x += this.propMoment;
                        this.engineTorque.x += f24;
                    } else {
                        this.engineTorque.x -= this.propMoment;
                        this.engineTorque.x -= f24;
                    }
                } else {
                    this.engineTorque.set(0.0F, 0.0F, 0.0F);
                }
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                tmpV3f.cross(this.propPos, this.engineForce);
                this.engineTorque.add(tmpV3f);
                this.rearRush = 0.0F;
                this.rpm = this.toRPM(this.w);
                double d = this.reference.Vflow.x + this.addVflow;
                if (d < 1.0D) {
                    d = 1.0D;
                }
                double d1 = 1.0D / (Atmosphere.density(this.reference.getAltitude()) * 6F * d);
                this.addVflow = (0.95D * this.addVflow) + (0.05D * this.propForce * d1);
                this.addVside = (0.95D * this.addVside) + (0.05D * (this.propMoment / this.propr) * d1);
                if (this.addVside < 0.0D) {
                    this.addVside = 0.0D;
                }
                break;

            case 2: // '\002'
                this.engineMoment = this.propAngleDeviceMinParam + (this.getControlThrottle() * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam));
                this.engineMoment /= this.propAngleDeviceMaxParam;
                this.engineMoment *= this.engineMomentMax;
                this.engineMoment *= this.getReadyness();
                this.engineMoment *= this.getDistabilisationMultiplier();
                this.engineMoment *= this.getStageMultiplier();
                this.engineMoment += this.getJetFrictionMoment(f);
                this.computePropForces(this.w, 0.0F, 0.0F, this.propAoA0, 0.0F);
                float f29 = this.w * this._1_wMax;
                float f30 = f29 * this.pressureExtBar;
                float f31 = f29 * f29;
                float f32 = 1.0F - (0.006F * (Atmosphere.temperature((float) this.reference.Loc.z) - 290F));
                float f33 = 1.0F - (0.0011F * this.reference.getSpeed());
                this.propForce = this.thrustMax * f30 * f31 * f32 * f33 * this.getStageMultiplier();
                float f20 = this.engineMoment - this.propMoment;
                this.aw = (f20 / (this.propI + this.engineI)) * 1.0F;
                if (this.aw > 0.0F) {
                    this.aw *= this.engineAcceleration;
                }
                this.w += this.aw * f;
                if (this.w < -this.wMaxAllowed) {
                    this.w = -this.wMaxAllowed;
                }
                if (this.w > (this.wMaxAllowed + this.wMaxAllowed)) {
                    this.w = this.wMaxAllowed + this.wMaxAllowed;
                }
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                break;

            // TODO
            case 10: // '\002'
                this.engineMoment = this.propAngleDeviceMinParam + (this.getControlThrottle() * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam));
                this.controlProp = 0.75F + (0.25F * this.controlThrottle);
                if (Math.abs(this.w) < 1E-005F) {
                    this.propPhiW = 1.570796F;
                } else {
                    this.propPhiW = (float) Math.atan(((Tuple3d) (((FlightModelMain) (this.reference)).Vflow)).x / (this.w * this.propReductor * this.propr));
                }
                this.propAoA = this.propPhi - this.propPhiW;
                this.computePropForces(this.w * this.propReductor, (float) ((Tuple3d) (((FlightModelMain) (this.reference)).Vflow)).x, this.propPhi, this.propAoA, this.reference.getAltitude());
                if (this.isPropAngleDeviceOperational()) {
                    if (this.w < this.engineMoment) {
                        this.engineMoment = Math.min(1.0F, (0.01F * (this.engineMoment - this.w)) - (0.012F * this.aw));
                        this.propTarget -= this.engineMoment * this.getPropAngleDeviceSpeed() * f;
                    } else {
                        this.engineMoment = Math.min(1.0F, (0.01F * (this.w - this.engineMoment)) + (0.012F * this.aw));
                        this.propTarget += this.engineMoment * this.getPropAngleDeviceSpeed() * f;
                    }
                    if ((this.stage == 6) && (this.propTarget < (this.propPhiW - 0.12F))) {
                        this.propTarget = this.propPhiW - 0.12F;
                        if (this.propPhi < this.propTarget) {
                            this.propPhi += 0.2F * f;
                        }
                    }
                } else {
                    this.propTarget = 0.0F;
                }
                this.engineMoment /= this.propAngleDeviceMaxParam;
                this.engineMoment *= this.engineMomentMax;
                this.engineMoment *= this.getReadyness();
                this.engineMoment *= this.getDistabilisationMultiplier();
                this.engineMoment *= this.getStageMultiplier();
                this.engineMoment += this.getJetFrictionMoment(f);
                this.computePropForces(this.w, 0.0F, 0.0F, this.propAoA0, 0.0F);
                float f291 = this.w * this._1_wMax;
                float f301 = f291 * this.pressureExtBar;
                float f311 = f291 * f291;
                float f321 = 1.0F - (0.006F * (Atmosphere.temperature((float) this.reference.Loc.z) - 290F));
                float f331 = 1.0F - (0.0011F * this.reference.getSpeed());
                this.propForce = this.thrustMax * f301 * f311 * f321 * f331 * this.getStageMultiplier();
                float f201 = this.engineMoment - this.propMoment;
                this.aw = (f201 / (this.propI + this.engineI)) * 1.0F;
                if (this.aw > 0.0F) {
                    this.aw *= this.engineAcceleration;
                }
                this.w += this.aw * f;
                if (this.w < -this.wMaxAllowed) {
                    this.w = -this.wMaxAllowed;
                }
                if (this.w > (this.wMaxAllowed + this.wMaxAllowed)) {
                    this.w = this.wMaxAllowed + this.wMaxAllowed;
                }
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                break;

            case 3: // '\003'
            case 4: // '\004'
                this.w = this.wMin + ((this.wMax - this.wMin) * this.controlThrottle);
                if ((this.w < this.wMin) || (this.w < 0.0F) || (this.reference.M.fuel == 0.0F) || (this.stage != 6)) {
                    this.w = 0.0F;
                }
                this.propForce = (this.w / this.wMax) * this.thrustMax;
                this.propForce *= this.getStageMultiplier();
                this.propForce *= this.compressorPMax / (this.compressorPMax + (1E-005F * Atmosphere.pressure((float) this.reference.Loc.z)));
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                break;

            case 6: // '\006'
                this.w = this.wMin + ((this.wMax - this.wMin) * this.controlThrottle);
                if ((this.w < this.wMin) || (this.w < 0.0F) || (this.stage != 6)) {
                    this.w = 0.0F;
                }
                float f34 = this.reference.getSpeed() / 94F;
                if (f34 < 1.0F) {
                    this.w = 0.0F;
                } else {
                    f34 = (float) Math.sqrt(f34);
                }
                this.propForce = (this.w / this.wMax) * this.thrustMax * f34;
                this.propForce *= this.getStageMultiplier();
                float f7 = (float) this.reference.Vwld.length();
                if (f7 > 208.333F) {
                    if (f7 > 291.666F) {
                        this.propForce = 0.0F;
                    } else {
                        this.propForce *= (float) Math.sqrt((291.666F - f7) / 83.33299F);
                    }
                }
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                if (!(this.reference instanceof RealFlightModel)) {
                    break;
                }
                RealFlightModel realflightmodel = (RealFlightModel) this.reference;
                f7 = Aircraft.cvt(this.propForce, 0.0F, this.thrustMax, 0.0F, 0.21F);
                if (realflightmodel.producedShakeLevel < f7) {
                    realflightmodel.producedShakeLevel = f7;
                }
                break;

            case 5: // '\005'
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                break;

            default:
                return;
        }
    }

    private void computePropForces(float f, float f1, float f2, float f3, float f4) {
        float f5 = f * this.propr;
        float f6 = (f1 * f1) + (f5 * f5);
        float f7 = (float) Math.sqrt(f6);
        float f8 = 0.5F * this.getFanCy((float) Math.toDegrees(f3)) * Atmosphere.density(f4) * f6 * this.propSEquivalent;
        float f9 = 0.5F * this.getFanCx((float) Math.toDegrees(f3)) * Atmosphere.density(f4) * f6 * this.propSEquivalent;
        if (f7 > 300F) {
            float f10 = 1.0F + (0.02F * (f7 - 300F));
            if (f10 > 2.0F) {
                f10 = 2.0F;
            }
            f9 *= f10;
        }
        if (f7 < 0.001F) {
            f7 = 0.001F;
        }
        float f11 = 1.0F / f7;
        float f12 = f1 * f11;
        float f13 = f5 * f11;
        float f14 = 1.0F;
        if (f1 < this.Vopt) {
            float f15 = this.Vopt - f1;
            f14 = 1.0F - (5E-005F * f15 * f15);
        }
        this.propForce = f14 * ((f8 * f13) - (f9 * f12));
        this.propMoment = ((f9 * f13) + (f8 * f12)) * this.propr;
    }

    public void toggle() {
        if (this.stage == 0) {
            this.setEngineStarts(this.reference.actor);
            return;
        }
        if (this.stage < 7) {
            this.setEngineStops(this.reference.actor);
            if (this.reference.isPlayers()) {
                HUD.log("EngineI0");
            }
            return;
        } else {
            return;
        }
    }

    public float getPowerOutput() {
        if ((this.stage == 0) || (this.stage > 6)) {
            return 0.0F;
        } else {
            return this.controlThrottle * this.readyness;
        }
    }

    public float getThrustOutput() {
        if ((this.stage == 0) || (this.stage > 6)) {
            return 0.0F;
        }
        float f = this.w * this._1_wMax * this.readyness;
        if (f > 1.1F) {
            f = 1.1F;
        }
        return f;
    }

    public float getReadyness() {
        return this.readyness;
    }

    public float getPropPhi() {
        return this.propPhi;
    }

    private float getPropAngleDeviceSpeed() {
        if (this.isPropAngleDeviceHydroOperable) {
            return this.propAngleChangeSpeed;
        } else {
            return this.propAngleChangeSpeed * 10F;
        }
    }

    public int getPropDir() {
        return this.propDirection;
    }

    public float getPropAoA() {
        return this.propAoA;
    }

    public Vector3f getForce() {
        return this.engineForce;
    }

    public float getRearRush() {
        return this.rearRush;
    }

    public float getw() {
        return this.w;
    }

    public float getRPM() {
        return this.rpm;
    }

    public float getPropw() {
        return this.w * this.propReductor;
    }

    public float getPropRPM() {
        return this.rpm * this.propReductor;
    }

    public int getType() {
        return this.type;
    }

    // TODO
    public int getStarter() // no static method but class method, following the non-static "starter" property!
    {
        return this.starter;
    }

    public float getControlThrottle() {
        return this.controlThrottle;
    }

    public boolean getControlAfterburner() {
        return this.controlAfterburner;
    }

    public boolean isHasControlThrottle() {
        return this.bHasThrottleControl;
    }

    public boolean isHasControlAfterburner() {
        return this.bHasAfterburnerControl;
    }

    public float getControlProp() {
        return this.controlProp;
    }

    public float getElPropPos() {
        float f;
        if (this.bControlPropAuto) {
            f = this.controlProp;
        } else {
            f = (this.propPhiMax - this.propPhi) / (this.propPhiMax - this.propPhiMin);
        }
        if (f < 0.1F) {
            return 0.0F;
        }
        if (f > 0.9F) {
            return 1.0F;
        } else {
            return f;
        }
    }

    public boolean getControlPropAuto() {
        return this.bControlPropAuto;
    }

    public boolean isHasControlProp() {
        return this.bHasPropControl;
    }

    public boolean isAllowsAutoProp() {
        if (this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode()) {
            if (World.cur().diffCur.ComplexEManagement) {
                switch (this.propAngleDeviceType) {
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
                        return (this.reference.actor instanceof SPITFIRE9E) || (this.reference.actor instanceof SPITFIRE9ECLP) || (this.reference.actor instanceof SPITFIRE9EHF) || (this.reference.actor instanceof SPITFIRE925LBSCW) || (this.reference.actor instanceof SPITFIRE8) || (this.reference.actor instanceof SPITFIRE8CLP);

                    case 7: // '\007'
                    case 8: // '\b'
                        return true;
                }
            } else {
                return this.bHasPropControl;
            }
        }
        return true;
    }

    public float getControlMix() {
        return this.controlMix;
    }

    public boolean isHasControlMix() {
        return this.bHasMixControl;
    }

    public int getControlMagnetos() {
        return this.controlMagneto;
    }

    public int getControlCompressor() {
        return this.controlCompressor;
    }

    public boolean isHasControlMagnetos() {
        return this.bHasMagnetoControl;
    }

    public boolean isHasControlCompressor() {
        return this.bHasCompressorControl;
    }

    public int getControlFeather() {
        return this.controlFeather;
    }

    public boolean isHasControlFeather() {
        return this.bHasFeatherControl;
    }

    public boolean isAllowsAutoRadiator() {
        if (World.cur().diffCur.ComplexEManagement) {
            if ((this.reference.actor instanceof P_51) || (this.reference.actor instanceof P_38) || (this.reference.actor instanceof YAK_3) || (this.reference.actor instanceof YAK_3P) || (this.reference.actor instanceof YAK_9M) || (this.reference.actor instanceof YAK_9U) || (this.reference.actor instanceof YAK_9UT) || (this.reference.actor instanceof SPITFIRE8) || (this.reference.actor instanceof SPITFIRE8CLP) || (this.reference.actor instanceof SPITFIRE9) || (this.reference.actor instanceof P_63C)) {
                return true;
            }
            switch (this.propAngleDeviceType) {
                case 7: // '\007'
                    return true;

                case 8: // '\b'
                    return this.type == _E_TYPE_INLINE;
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean isHasControlRadiator() {
        return this.bHasRadiatorControl;
    }

    public float getControlRadiator() {
        return this.controlRadiator;
    }

    public int getExtinguishers() {
        return this.extinguishers;
    }

    private float getFanCy(float f) {
        if (f > 34F) {
            f = 34F;
        }
        if (f < -8F) {
            f = -8F;
        }
        if (f < 16F) {
            return (-0.004688F * f * f) + (0.15F * f) + 0.4F;
        }
        float f1 = 0.0F;
        if (f > 22F) {
            f1 = 0.01F * (f - 22F);
            f = 22F;
        }
        return ((0.00097222F * f * f) - (0.070833F * f)) + 2.4844F + f1;
    }

    private float getFanCx(float f) {
        if (f < -4F) {
            f = -8F - f;
        }
        if (f > 34F) {
            f = 34F;
        }
        if (f < 16D) {
            return (0.00035F * f * f) + (0.0028F * f) + 0.0256F;
        }
        float f1 = 0.0F;
        if (f > 22F) {
            f1 = 0.04F * (f - 22F);
            f = 22F;
        }
        return (((-0.00555F * f * f) + (0.24444F * f)) - 2.32888F) + f1;
    }

    public int getCylinders() {
        return this.cylinders;
    }

    public int getCylindersOperable() {
        return this.cylindersOperable;
    }

    public float getCylindersRatio() {
        return (float) this.cylindersOperable / (float) this.cylinders;
    }

    public int getStage() {
        return this.stage;
    }

    public float getBoostFactor() {
        return this.engineBoostFactor;
    }

    public float getManifoldPressure() {
        return this.compressorManifoldPressure;
    }

    public void setManifoldPressure(float f) {
        this.compressorManifoldPressure = f;
    }

    public boolean getSootState() {
        return false;
    }

    public Point3f getEnginePos() {
        return this.enginePos;
    }

    public Point3f getPropPos() {
        return this.propPos;
    }

    public Vector3f getEngineVector() {
        return this.engineVector;
    }

    public float rangeAndFuel(float f, float f1, boolean flag, float f2) {
        tmpF = 0.0F;
        return 3600F * tmpF;
    }

    public float forcePropAOA(float f, float f1, float f2, boolean flag) {
        return this.forcePropAOA(f, f1, f2, flag, 1.0F);
    }

    public float forcePropAOA(float fSpeed, float fAltitude, float fThrottle, boolean isWEP, float fRPMfactor) {
        switch (this.type) {
            default:
                return -1F;

            case _E_TYPE_INLINE:
            case _E_TYPE_RADIAL:
            case _E_TYPE_HELO_INLINE:
                float fOldThrottle = this.controlThrottle;;
                boolean oldIsWEP = this.controlAfterburner;
                int oldStage = this.stage;;
// boolean flag3;
// {
                safeLoc.set(this.reference.Loc);
                safeVwld.set(this.reference.Vwld);
                safeVflow.set(this.reference.Vflow);
                if (isWEP) {
                    this.w = this.wWEP;
                } else {
                    this.w = this.wMax;
                }
                this.w *= fRPMfactor;
                this.controlThrottle = fThrottle;
                if ((this.engineBoostFactor <= 1.0D) && (this.controlThrottle > 1.0F)) {
                    this.controlThrottle = 1.0F;
                }
                if ((this.afterburnerType > 0) && isWEP) {
                    this.controlAfterburner = true;
                }
                this.stage = _E_STAGE_NOMINAL;
                this.fastATA = true;
                this.reference.Loc.set(0.0D, 0.0D, fAltitude);
                this.reference.Vwld.set(fSpeed, 0.0D, 0.0D);
                this.reference.Vflow.set(fSpeed, 0.0D, 0.0D);
                
                // Fix by SAS~Storebror: Wrong parentheses fixed.
//                this.pressureExtBar = (9.8716683E-006F * Atmosphere.pressure(this.reference.getAltitude())) + (this.compressorSpeedManifold * 0.5F * Atmosphere.density(this.reference.getAltitude()) * fSpeed * fSpeed); // Pa (N/m2) to atmosphere conversion
                this.pressureExtBar = 9.8716683E-006F * (Atmosphere.pressure(this.reference.getAltitude()) + (this.compressorSpeedManifold * 0.5F * Atmosphere.density(this.reference.getAltitude()) * fSpeed * fSpeed)); // Pa (N/m2) to atmosphere conversion
                
                this.maxMoment = this.getCompressorMultiplier(0.033F) * this.getN();
                if (isWEP && this.bWepRpmInLowGear && (this.controlCompressor == this.compressorMaxStep)) {
                    this.w = this.wMax * fRPMfactor;
                    float f5 = this.getCompressorMultiplier(0.033F);
                    f5 *= this.getN();
                    this.maxMoment = f5;
                }
                this.maxW = this.w;
                float propPhiTemp = this.propPhiMin;
                float propForceNew = -1E+008F;
                boolean isTypeTwoPitchProp = false;
                if ((Aircraft) this.reference.actor instanceof TypeTwoPitchProp) {
                    isTypeTwoPitchProp = true;
                }
                while ((this.propAngleDeviceType == _E_PROP_FIXED) || isTypeTwoPitchProp) {
                    int propMomentDivider = 0;
                    int j = 0;
                    float wFactorOffset = 0.1F;
                    float wFactor = 0.5F;
                    do {
                        if (isWEP) {
                            this.w = this.wWEP * wFactor;
                        } else {
                            this.w = this.wMax * wFactor;
                        }
                        float fanFlow = (float) Math.sqrt((fSpeed * fSpeed) + (this.w * this.propr * this.propReductor * this.w * this.propr * this.propReductor));
                        float fanPhiRadians = propPhiTemp - (float) Math.asin(fSpeed / fanFlow);
                        this.computePropForces(this.w * this.propReductor, fSpeed, 0.0F, fanPhiRadians, fAltitude);
                        this.maxMoment = this.getN() * this.getCompressorMultiplier(0.033F);
                        this.maxW = this.w;
                        if ((j > 32) || (wFactorOffset <= 1E-005F)) {
                            break;
                        }
                        if (this.propMoment < this.maxMoment) {
                            if (propMomentDivider == 1) {
                                wFactorOffset /= 2.0F;
                            }
                            wFactor *= 1.0F + wFactorOffset;
                            propMomentDivider = -1;
                        } else {
                            if (propMomentDivider == -1) {
                                wFactorOffset /= 2.0F;
                            }
                            wFactor /= 1.0F + wFactorOffset;
                            propMomentDivider = 1;
                        }
                        j++;
                    } while (true);
                    if (!isTypeTwoPitchProp) {
                        break;
                    }
                    if (propPhiTemp == this.propPhiMin) {
                        propForceNew = this.propForce;
                        propPhiTemp = this.propPhiMax;
                    } else {
                        if (propForceNew > this.propForce) {
                            this.propForce = propForceNew;
                        }
                        break;
                    }
                }
// }
                this.controlThrottle = fOldThrottle;
                this.controlAfterburner = oldIsWEP;
                this.stage = oldStage;
                this.reference.Loc.set(safeLoc);
                this.reference.Vwld.set(safeVwld);
                this.reference.Vflow.set(safeVflow);
                this.fastATA = false;
                this.w = 0.0F;
                if (isTypeTwoPitchProp || (this.propAngleDeviceType == _E_PROP_FIXED)) {
                    return this.propForce;
                }
                float fTempPropMax = 1.5F;
                float fTempPropMin = -0.06F;
                int k = 0;
                do {
                    float fTempProp = 0.5F * (fTempPropMax + fTempPropMin);
                    if (isWEP && (!this.bWepRpmInLowGear || (this.controlCompressor != this.compressorMaxStep))) {
                        this.computePropForces(this.wWEP * fRPMfactor * this.propReductor, fSpeed, 0.0F, fTempProp, fAltitude);
                    } else {
                        this.computePropForces(this.wMax * fRPMfactor * this.propReductor, fSpeed, 0.0F, fTempProp, fAltitude);
                    }
                    if (((this.propForce > 0.0F) && (Math.abs(this.propMoment - this.maxMoment) < 1E-005F)) || (k > 32)) {
                        break;
                    }
                    if ((this.propForce > 0.0F) && (this.propMoment > this.maxMoment)) {
                        fTempPropMax = fTempProp;
                    } else {
                        fTempPropMin = fTempProp;
                    }
                    k++;
                } while (true);
                return this.propForce;
            case _E_TYPE_TURBOPROP:
                float f5 = this.getCompressorMultiplier(0.033F);
                f5 *= this.getN();
                this.w = 0.0F;
                float f7 = 1.5F;
                float f9x = -0.06F;
                int kx = 0;
                do {
                    float f13 = 0.5F * (f7 + f9x);
                    if (isWEP) {
                        this.computePropForces(this.wWEP * this.propReductor, fSpeed, 0.0F, f13, fAltitude);
                    } else {
                        this.computePropForces(this.wMax * this.propReductor, fSpeed, 0.0F, f13, fAltitude);
                    }
                    if (((this.propForce <= 0.0F) || (Math.abs(this.propMoment - f5) >= 1E-005F)) && (kx <= 32)) {
                        if ((this.propForce > 0.0F) && (this.propMoment > f5)) {
                            f7 = f13;
                        } else {
                            f9x = f13;
                        }
                        kx++;
                    } else {
                        // TODO
                        this.propForce = (float) Math.toDegrees(Math.atan(fSpeed / (this.wMax * this.propReductor * this.propr)) + f13);
                        return this.propForce;
                    }
                } while (true);

            case _E_TYPE_JET:
                this.pressureExtBar = Atmosphere.pressure(fAltitude) + (this.compressorSpeedManifold * 0.5F * Atmosphere.density(fAltitude) * fSpeed * fSpeed);
                this.pressureExtBar *= 9.8716682999999996E-006D;
                float f17 = this.pressureExtBar;
                float f18 = 1.0F - (0.006F * (Atmosphere.temperature(fAltitude) - 290F));
                float f19 = 1.0F - (0.0011F * fSpeed);
                this.propForce = this.thrustMax * f17 * f18 * f19;
                if (fThrottle > 1.0F) {
                    fThrottle = 1.0F;
                }
                this.propForce *= fThrottle;
                return this.propForce;

            case _E_TYPE_ROCKET:
            case _E_TYPE_ROCKETBOOST:
            case _E_TYPE_PVRD:
                this.propForce = this.thrustMax;
                this.propForce *= this.compressorPMax / (this.compressorPMax + (1E-005F * Atmosphere.pressure(fAltitude)));
                return this.propForce;

            case _E_TYPE_TOW:
                return this.thrustMax;

            case _E_TYPE_AZURE:
                return -1F;
        }
    }

    public float getEngineLoad() {
        float f = 0.1F + (this.getControlThrottle() * 0.8181818F);
        float f1 = this.getw() / this.wMax;
        return f1 / f;
    }

    private void overrevving() {
        if ((this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode() && World.cur().diffCur.ComplexEManagement && World.cur().diffCur.Engine_Overheat && (this.w > this.wMaxAllowed) && this.bIsMaster) {
            this.wMaxAllowed = 0.999965F * this.wMaxAllowed;
            this._1_wMaxAllowed = 1.0F / this.wMaxAllowed;
            tmpF *= 1.0F - ((this.wMaxAllowed - this.w) * 0.01F);
            this.engineDamageAccum += 0.01F + (0.05F * (this.w - this.wMaxAllowed) * this._1_wMaxAllowed);
            if (this.engineDamageAccum > 1.0F) {
                if (heatStringID == -1) {
                    heatStringID = HUD.makeIdLog();
                }
                if (this.reference.isPlayers()) {
                    HUD.log(heatStringID, "EngineOverheat");
                }
                this.setReadyness(this.getReadyness() - ((this.engineDamageAccum - 1.0F) * 0.005F));
            }
            if (this.getReadyness() < 0.2F) {
                this.setEngineDies(this.reference.actor);
            }
        }
    }

    public float getN() {
        if (this.stage == 6) {
            switch (this.engineCarburetorType) {
                case 0: // '\0'
                    float f = 0.05F + (0.95F * this.getControlThrottle());
                    float f4 = this.w / this.wMax;
                    tmpF = this.engineMomentMax * (((-1F / f) * f4 * f4) + (2.0F * f4));
                    if (this.getControlThrottle() > 1.0F) {
                        tmpF *= this.engineBoostFactor;
                    }
                    this.overrevving();
                    break;

                case 3: // '\003'
                    float f1 = 0.1F + (0.9F * this.getControlThrottle());
                    float f5 = this.w / this.wNom;
                    tmpF = this.engineMomentMax * (((-1F / f1) * f5 * f5) + (2.0F * f5));
                    if (this.getControlThrottle() > 1.0F) {
                        tmpF *= this.engineBoostFactor;
                    }
                    float f10 = this.getControlThrottle() - (this.neg_G_Counter * 0.1F);
                    if (f10 <= 0.3F) {
                        f10 = 0.3F;
                    }
                    if ((this.reference.getOverload() < 0.0F) && (this.neg_G_Counter >= 0.0F)) {
                        this.neg_G_Counter += 0.03F;
                        this.producedDistabilisation += 10F + (5F * this.neg_G_Counter);
                        tmpF *= f10;
                        if (this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode() && this.bIsMaster && (this.neg_G_Counter > World.Rnd().nextFloat(5F, 8F))) {
                            this.setEngineStops(this.reference.actor);
                        }
                    } else if ((this.reference.getOverload() >= 0.0F) && (this.neg_G_Counter > 0.0F)) {
                        this.neg_G_Counter -= 0.015F;
                        this.producedDistabilisation += 10F + (5F * this.neg_G_Counter);
                        tmpF *= f10;
                        this.bFloodCarb = true;
                    } else {
                        this.bFloodCarb = false;
                        this.neg_G_Counter = 0.0F;
                    }
                    this.overrevving();
                    break;

                case 1: // '\001'
                case 2: // '\002'
                    float f2 = 0.1F + (0.9F * this.getControlThrottle());
                    if (f2 > 1.0F) {
                        f2 = 1.0F;
                    }
                    float f8 = this.engineMomentMax * ((-0.5F * f2 * f2) + (1.0F * f2) + 0.5F);
                    float f6;
                    if (this.controlAfterburner) {
                        f6 = this.w / (this.wWEP * f2);
                    } else {
                        f6 = this.w / (this.wNom * f2);
                    }
                    tmpF = f8 * ((2.0F * f6) - (1.0F * f6 * f6));
                    if (this.getControlThrottle() > 1.0F) {
                        tmpF *= 1.0F + ((this.getControlThrottle() - 1.0F) * 10F * (this.engineBoostFactor - 1.0F));
                    }
                    this.overrevving();
                    break;

                case 4: // '\004'
                    float f3 = 0.1F + (0.9F * this.getControlThrottle());
                    if (f3 > 1.0F) {
                        f3 = 1.0F;
                    }
                    float f9 = this.engineMomentMax * ((-0.5F * f3 * f3) + (1.0F * f3) + 0.5F);
                    float f7;
                    if (this.controlAfterburner) {
                        f7 = this.w / (this.wWEP * f3);
                        if (f3 >= 0.95F) {
                            this.bFullT = true;
                        } else {
                            this.bFullT = false;
                        }
                    } else {
                        f7 = this.w / (this.wNom * f3);
                        this.bFullT = false;
                        if ((this.reference.actor instanceof SPITFIRE5B) && (f3 >= 0.95F)) {
                            this.bFullT = true;
                        }
                    }
                    tmpF = f9 * ((2.0F * f7) - (1.0F * f7 * f7));
                    if (this.getControlThrottle() > 1.0F) {
                        tmpF *= 1.0F + ((this.getControlThrottle() - 1.0F) * 10F * (this.engineBoostFactor - 1.0F));
                    }
                    float f11 = this.getControlThrottle() - (this.neg_G_Counter * 0.2F);
                    if (f11 <= 0.0F) {
                        f11 = 0.1F;
                    }
                    if ((this.reference.getOverload() < 0.0F) && (this.neg_G_Counter >= 0.0F)) {
                        this.neg_G_Counter += 0.03F;
                        if (this.bFullT && (this.neg_G_Counter < 0.5F)) {
                            this.producedDistabilisation += 15F + (5F * this.neg_G_Counter);
                            tmpF *= 0.52F - this.neg_G_Counter;
                        } else if (this.bFullT && (this.neg_G_Counter >= 0.5F) && (this.neg_G_Counter <= 0.8F)) {
                            this.neg_G_Counter = 0.51F;
                            this.bFloodCarb = false;
                        } else if (this.bFullT && (this.neg_G_Counter > 0.8F)) {
                            this.neg_G_Counter -= 0.045F;
                            this.producedDistabilisation += 10F + (5F * this.neg_G_Counter);
                            tmpF *= f11;
                            this.bFloodCarb = true;
                        } else {
                            this.producedDistabilisation += 10F + (5F * this.neg_G_Counter);
                            tmpF *= f11;
                            if (this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode() && this.bIsMaster && (this.neg_G_Counter > World.Rnd().nextFloat(7.5F, 9.5F))) {
                                this.setEngineStops(this.reference.actor);
                            }
                        }
                    } else if ((this.reference.getOverload() >= 0.0F) && (this.neg_G_Counter > 0.0F)) {
                        this.neg_G_Counter -= 0.03F;
                        if (!this.bFullT) {
                            this.producedDistabilisation += 10F + (5F * this.neg_G_Counter);
                            tmpF *= f11;
                        }
                        this.bFloodCarb = true;
                    } else {
                        this.neg_G_Counter = 0.0F;
                        this.bFloodCarb = false;
                    }
                    this.overrevving();
                    break;
            }
            if (this.controlAfterburner) {
                if (this.afterburnerType == 1) {
                    if ((this.controlThrottle > 1.0F) && (this.reference.M.nitro > 0.0F)) {
                        tmpF *= this.engineAfterburnerBoostFactor;
                    }
                } else if ((this.afterburnerType == 8) || (this.afterburnerType == 7)) {
                    if (this.controlCompressor < this.compressorMaxStep) {
                        tmpF *= this.engineAfterburnerBoostFactor;
                    }
                } else {
                    tmpF *= this.engineAfterburnerBoostFactor;
                }
            }
            if (this.engineDamageAccum > 0.0F) {
                this.engineDamageAccum -= 0.01F;
            }
            if (this.engineDamageAccum < 0.0F) {
                this.engineDamageAccum = 0.0F;
            }
            if (tmpF < 0.0F) {
                tmpF = Math.max(tmpF, -0.8F * this.w * this._1_wMax * this.engineMomentMax);
            }
            return tmpF;
        }
        tmpF = -1500F * this.w * this._1_wMax * this.engineMomentMax;
        if (this.stage == 8) {
            this.w = 0.0F;
        }
        return tmpF;
    }

    private float getDistabilisationMultiplier() {
        if (this.engineMoment < 0.0F) {
            return 1.0F;
        }
        float f = 1.0F + (World.Rnd().nextFloat(-1F, 0.1F) * this.getDistabilisationAmplitude());
        if ((f < 0.0F) && (this.w < (0.5F * (this.wMax + this.wMin)))) {
            return 0.0F;
        } else {
            return f;
        }
    }

    public float getDistabilisationAmplitude() {
        if (this.getCylindersOperable() > 2) {
            float f = 1.0F - this.getCylindersRatio();
            return (this.engineDistAM * this.w * this.w) + (this.engineDistBM * this.w) + this.engineDistCM + (9.25F * f * f) + this.producedDistabilisation;
        } else {
            return 11.25F;
        }
    }

    private float getCompressorMultiplier(float f) {
        float f24 = this.controlThrottle;
        if (f24 > 1.0F) {
            f24 = 1.0F;
        }
        float f18;
        switch (this.propAngleDeviceType) {
            case 1:
            case 2:
            case 7:
            case 8:
                f18 = this.getATA(this.toRPM(this.propAngleDeviceMinParam + ((this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam) * f24)));
                break;

            case 3:
            case 4:
            case 5:
            case 6:
            default:
                f18 = this.compressorRPMtoWMaxATA * (0.55F + (0.45F * f24));
                break;
        }
        this.coolMult = 1.0F;
        this.compressorManifoldThreshold = f18;
        switch (this.compressorType) {
            case 0:
                float f1 = Atmosphere.pressure(this.reference.getAltitude()) + (0.5F * Atmosphere.density(this.reference.getAltitude()) * this.reference.getSpeed() * this.reference.getSpeed());
                float f25 = f1 / Atmosphere.P0();
                this.coolMult = f25;
                return f25;

            case 1:
                float f2 = this.pressureExtBar;
                if ((!this.bHasCompressorControl || !this.reference.isPlayers() || !(this.reference instanceof RealFlightModel) || !((RealFlightModel) this.reference).isRealMode() || !World.cur().diffCur.ComplexEManagement || this.fastATA) && (this.reference.isTick(128, 0) || this.fastATA)) {
                    this.compressorStepFound = false;
                    this.controlCompressor = 0;
                }
                float f29 = -1F;
                float f32 = -1F;
                int i = -1;
                float f26;
                if (this.fastATA) {
                    for (this.controlCompressor = 0; this.controlCompressor <= this.compressorMaxStep; this.controlCompressor++) {
                        this.compressorManifoldThreshold = f18;
                        float f5 = this.compressorPressure[this.controlCompressor];
                        float f12 = this.compressorRPMtoWMaxATA / f5;
                        float f36 = 1.0F;
                        float f41 = 1.0F;
                        if (f2 > f5) {
                            float f46 = 1.0F - f5;
                            if (f46 < 0.0001F) {
                                f46 = 0.0001F;
                            }
                            float f52 = 1.0F - f2;
                            float f57 = 1.0F;
                            for (int k = 1; k <= this.controlCompressor; k++) {
                                if (this.compressorAltMultipliers[this.controlCompressor] >= 1.0F) {
                                    f57 *= this.compressorBaseMultipliers[this.controlCompressor];
                                } else {
                                    f57 *= this.compressorBaseMultipliers[this.controlCompressor] * this.compressorAltMultipliers[this.controlCompressor];
                                }
                            }

                            f36 = f57 + ((f52 / f46) * (this.compressorAltMultipliers[this.controlCompressor] - f57));
                        } else {
                            f36 = this.compressorAltMultipliers[this.controlCompressor];
                        }
                        this.compressorManifoldPressure = (this.compressorPAt0 + ((1.0F - this.compressorPAt0) * this.w * this._1_wMax)) * f2 * f12;
                        float f19 = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                        if (this.controlAfterburner && (((this.afterburnerType != 8) && (this.afterburnerType != 7)) || (this.controlCompressor != this.compressorMaxStep)) && ((this.afterburnerType != 1) || (this.controlThrottle <= 1.0F) || (this.reference.M.nitro > 0.0F))) {
                            f19 *= this.afterburnerCompressorFactor;
                            this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                        }
                        this.compressor2ndThrottle = f19;
                        if (this.compressor2ndThrottle > 1.0F) {
                            this.compressor2ndThrottle = 1.0F;
                        }
                        this.compressorManifoldPressure *= this.compressor2ndThrottle;
                        this.compressor1stThrottle = f18 / this.compressorRPMtoWMaxATA;
                        if (this.compressor1stThrottle > 1.0F) {
                            this.compressor1stThrottle = 1.0F;
                        }
                        this.compressorManifoldPressure *= this.compressor1stThrottle;
                        f41 = (f36 * this.compressorManifoldPressure) / this.compressorManifoldThreshold;
                        if (this.controlAfterburner && ((this.afterburnerType == 8) || (this.afterburnerType == 7)) && (this.controlCompressor == this.compressorMaxStep)) {
                            if ((f41 / this.engineAfterburnerBoostFactor) > f29) {
                                f29 = f41;
                                i = this.controlCompressor;
                            }
                            continue;
                        }
                        if (f41 > f29) {
                            f29 = f41;
                            i = this.controlCompressor;
                        }
                    }

                    f26 = f29;
                    if (i < 0) {
                        i = 0;
                    }
                    this.controlCompressor = i;
                } else {
                    float f37 = f18;
                    if (this.controlAfterburner) {
                        f37 *= this.afterburnerCompressorFactor;
                    }
                    do {
                        if (this.controlCompressor < 0) {
                            this.controlCompressor = 0;
                        } else if (this.controlCompressor >= this.compressorPressure.length) {
                            this.controlCompressor = this.compressorPressure.length - 1;
                        }
                        float f6 = this.compressorPressure[this.controlCompressor];
                        float f42 = 1.0F;
                        float f47 = 1.0F;
                        if (f2 > f6) {
                            float f53 = 1.0F - f6;
                            if (f53 < 0.0001F) {
                                f53 = 0.0001F;
                            }
                            float f58 = 1.0F - f2;
                            float f62 = 1.0F;
                            for (int l = 1; l <= this.controlCompressor; l++) {
                                if (this.compressorAltMultipliers[this.controlCompressor] >= 1.0F) {
                                    f62 *= this.compressorBaseMultipliers[this.controlCompressor];
                                } else {
                                    f62 *= this.compressorBaseMultipliers[this.controlCompressor] * this.compressorAltMultipliers[this.controlCompressor];
                                }
                            }

                            f42 = f62 + ((f58 / f53) * (this.compressorAltMultipliers[this.controlCompressor] - f62));
                            f47 = f42;
                        } else {
                            f42 = this.compressorAltMultipliers[this.controlCompressor];
                            f47 = (f42 * f2 * f18 * (this.compressorPAt0 + ((1.0F - this.compressorPAt0) * this.w * this._1_wMax))) / (f6 * f37);
                        }
                        if (f47 > f29) {
                            f29 = f47;
                            f32 = f42;
                            i = this.controlCompressor;
                        }
                        if (!this.compressorStepFound) {
                            this.controlCompressor++;
                            if (this.controlCompressor == (this.compressorMaxStep + 1)) {
                                this.compressorStepFound = true;
                            }
                        }
                    } while (!this.compressorStepFound);
                    if (i < 0) {
                        i = 0;
                    }
                    this.controlCompressor = i;
                    float f7 = this.compressorPressure[this.controlCompressor];
                    float f14 = this.compressorRPMtoWMaxATA / f7;
                    this.compressorManifoldPressure = (this.compressorPAt0 + ((1.0F - this.compressorPAt0) * this.w * this._1_wMax)) * f2 * f14;
                    float f20 = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                    if (this.controlAfterburner && (((this.afterburnerType != 8) && (this.afterburnerType != 7)) || (this.controlCompressor != this.compressorMaxStep)) && ((this.afterburnerType != 1) || (this.controlThrottle <= 1.0F) || (this.reference.M.nitro > 0.0F))) {
                        f20 *= this.afterburnerCompressorFactor;
                        this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                    }
                    if (this.fastATA) {
                        this.compressor2ndThrottle = f20;
                    } else {
                        this.compressor2ndThrottle -= 3F * f * (this.compressor2ndThrottle - f20);
                    }
                    if (this.compressor2ndThrottle > 1.0F) {
                        this.compressor2ndThrottle = 1.0F;
                    }
                    this.compressorManifoldPressure *= this.compressor2ndThrottle;
                    this.compressor1stThrottle = f18 / this.compressorRPMtoWMaxATA;
                    if (this.compressor1stThrottle > 1.0F) {
                        this.compressor1stThrottle = 1.0F;
                    }
                    this.compressorManifoldPressure *= this.compressor1stThrottle;
                    f26 = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                    this.coolMult = f26;
                    f26 *= f32;
                }
                if ((this.w <= 20F) && (this.w < 150F)) {
                    this.compressorManifoldPressure = Math.min(this.compressorManifoldPressure, f2 * (0.4F + ((this.w - 20F) * 0.04F)));
                }
                if (this.w < 20F) {
                    this.compressorManifoldPressure = f2 * (1.0F - (this.w * 0.03F));
                }
                if ((this.mixerType == 1) && (this.stage == 6)) {
                    this.compressorManifoldPressure *= this.getMixMultiplier();
                }
                return f26;

            case 2:
                float f3 = this.pressureExtBar;
                if ((!this.bHasCompressorControl || !this.reference.isPlayers() || !(this.reference instanceof RealFlightModel) || !((RealFlightModel) this.reference).isRealMode() || !World.cur().diffCur.ComplexEManagement || this.fastATA) && (this.reference.isTick(128, 0) || this.fastATA)) {
                    this.compressorStepFound = false;
                    this.controlCompressor = 0;
                }
                float f30 = -1F;
                int j = -1;
                float f27;
                if (this.fastATA) {
                    float f38 = 0.0F;
                    float f43 = 0.0F;
                    for (this.controlCompressor = 0; this.controlCompressor <= this.compressorMaxStep; this.controlCompressor++) {
                        this.compressorManifoldThreshold = f18;
                        float f8 = this.compressorPressure[this.controlCompressor];
                        float f15 = this.compressorRPMtoWMaxATA / f8;
                        float f48 = 1.0F;
                        float f54 = 1.0F;
                        float f59 = 1.0F;
                        float f63 = 1.0F - f8;
                        float f65 = 1.0F - f3;
                        if (f3 > f8) {
                            if (f63 < 0.0001F) {
                                f63 = 0.0001F;
                            }
                            for (int i1 = 1; i1 <= this.controlCompressor; i1++) {
                                if (this.compressorAltMultipliers[this.controlCompressor] >= 1.0F) {
                                    f59 *= this.compressorBaseMultipliers[this.controlCompressor];
                                } else {
                                    f59 *= this.compressorBaseMultipliers[this.controlCompressor] * this.compressorAltMultipliers[this.controlCompressor];
                                }
                            }

                            f48 = f59 + ((f65 / f63) * (this.compressorAltMultipliers[this.controlCompressor] - f59));
                        } else {
                            f48 = this.compressorAltMultipliers[this.controlCompressor];
                        }
                        this.compressorManifoldPressure = (this.compressorPAt0 + ((1.0F - this.compressorPAt0) * this.w * this._1_wMax)) * f3 * f15;
                        float f21 = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                        if (this.controlAfterburner && ((this.afterburnerType != 1) || (this.controlThrottle <= 1.0F) || (this.reference.M.nitro > 0.0F))) {
                            f21 *= this.afterburnerCompressorFactor;
                            this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                        }
                        this.compressor2ndThrottle = f21;
                        if (this.compressor2ndThrottle > 1.0F) {
                            this.compressor2ndThrottle = 1.0F;
                        }
                        this.compressorManifoldPressure *= this.compressor2ndThrottle;
                        if (this.controlCompressor == 0) {
                            f43 = f48;
                            f38 = this.compressor2ndThrottle;
                        }
                        this.compressor1stThrottle = f18 / this.compressorRPMtoWMaxATA;
                        if (this.compressor1stThrottle > 1.0F) {
                            this.compressor1stThrottle = 1.0F;
                        }
                        this.compressorManifoldPressure *= this.compressor1stThrottle;
                        f54 = (f48 * this.compressorManifoldPressure) / this.compressorManifoldThreshold;
                        if ((this.controlCompressor == 1.0F) && (f38 == 1.0F) && (f21 < 1.0F)) {
                            float f67 = this.compressorPressure[1] / (this.compressorPressure[0] - this.compressorPressure[1]);
                            f54 *= 1.0F + (((f43 / f48) - 1.0F) * ((f38 / f21) - 1.0F) * f67);
                        }
                        if (f54 > f30) {
                            f30 = f54;
                            j = this.controlCompressor;
                        }
                    }

                    f27 = f30;
                    this.controlCompressor = j;
                } else {
                    float f39 = f18;
                    if (this.controlAfterburner) {
                        f39 *= this.afterburnerCompressorFactor;
                    }
                    float f44 = 1.0F;
                    float f49 = 1.0F - f3;
                    float f55 = this.compressorPressure[0];
                    float f60 = ((f3 * (this.compressorPAt0 + (1.0F - this.compressorPAt0)) * this.w * this._1_wMax * this.compressorRPMtoWMaxATA) / f55) + 0.001F;
                    float f64 = 1.0F;
                    if ((f60 < f39) && (this.compressorMaxStep == 1)) {
                        this.controlCompressor = 1;
                        float f9 = this.compressorPressure[1];
                        float f68 = 1.0F - f9;
                        if (f68 < 0.0001F) {
                            f68 = 0.0001F;
                        }
                        float f70 = ((f3 * (this.compressorPAt0 + (1.0F - this.compressorPAt0)) * this.w * this._1_wMax * this.compressorRPMtoWMaxATA) / f9) + 0.001F;
                        if (f70 > f39) {
                            if (this.compressorAltMultipliers[1] >= 1.0F) {
                                f44 *= this.compressorBaseMultipliers[1];
                            } else {
                                f44 *= this.compressorBaseMultipliers[1] * this.compressorAltMultipliers[1];
                            }
                            f64 = f44 + ((f49 / f68) * (this.compressorAltMultipliers[1] - f44));
                            f64 += ((this.compressorAltMultipliers[0] - f64) * (f70 - 1.0F)) / (((f55 * f39) / f9) - 1.0F);
                        } else {
                            f64 = this.compressorAltMultipliers[this.controlCompressor];
                        }
                    } else {
                        this.controlCompressor = 0;
                        float f69 = 1.0F - f55;
                        if (f69 < 0.0001F) {
                            f69 = 0.0001F;
                        }
                        f64 = 1.0F + ((f49 / f69) * (this.compressorAltMultipliers[0] - 1.0F));
                    }
                    float f34 = f64;
                    if (j < 0) {
                        j = 0;
                    }
                    float f10 = this.compressorPressure[this.controlCompressor];
                    float f16 = this.compressorRPMtoWMaxATA / f10;
                    this.compressorManifoldPressure = (this.compressorPAt0 + ((1.0F - this.compressorPAt0) * this.w * this._1_wMax)) * f3 * f16;
                    float f22 = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                    if (this.controlAfterburner && ((this.afterburnerType != 1) || (this.controlThrottle <= 1.0F) || (this.reference.M.nitro > 0.0F))) {
                        f22 *= this.afterburnerCompressorFactor;
                        this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                    }
                    this.compressor2ndThrottle = f22;
                    if (this.compressor2ndThrottle > 1.0F) {
                        this.compressor2ndThrottle = 1.0F;
                    }
                    this.compressorManifoldPressure *= this.compressor2ndThrottle;
                    this.compressor1stThrottle = f18 / this.compressorRPMtoWMaxATA;
                    if (this.compressor1stThrottle > 1.0F) {
                        this.compressor1stThrottle = 1.0F;
                    }
                    this.compressorManifoldPressure *= this.compressor1stThrottle;
                    f27 = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                    this.coolMult = f27;
                    f27 *= f34;
                }
                if ((this.w <= 20F) && (this.w < 150F)) {
                    this.compressorManifoldPressure = Math.min(this.compressorManifoldPressure, f3 * (0.4F + ((this.w - 20F) * 0.04F)));
                }
                if (this.w < 20F) {
                    this.compressorManifoldPressure = f3 * (1.0F - (this.w * 0.03F));
                }
                return f27;

            case 3:
                float f4 = this.pressureExtBar;
                this.controlCompressor = 0;
                float f35 = -1F;
                float f11 = this.compressorPressure[this.controlCompressor];
                float f17 = this.compressorRPMtoWMaxATA / f11;
                float f40 = 1.0F;
                if (f4 > f11) {
                    float f50 = 1.0F - f11;
                    if (f50 < 0.0001F) {
                        f50 = 0.0001F;
                    }
                    float f56 = 1.0F - f4;
                    if (f56 < 0.0F) {
                        f56 = 0.0F;
                    }
                    float f61 = 1.0F;
                    f40 = f61 + ((f56 / f50) * (this.compressorAltMultipliers[this.controlCompressor] - f61));
                } else {
                    f40 = this.compressorAltMultipliers[this.controlCompressor];
                }
                f35 = f40;
                f17 = this.compressorRPMtoWMaxATA / f11;
                if (f4 < f11) {
                    f4 = (0.1F * f4) + (0.9F * f11);
                }
                float f51 = f4 * f17;
                this.compressorManifoldPressure = (this.compressorPAt0 + ((1.0F - this.compressorPAt0) * this.w * this._1_wMax)) * f51;
                float f23 = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                if (this.fastATA) {
                    this.compressor2ndThrottle = f23;
                } else {
                    this.compressor2ndThrottle -= 3F * f * (this.compressor2ndThrottle - f23);
                }
                if (this.compressor2ndThrottle > 1.0F) {
                    this.compressor2ndThrottle = 1.0F;
                }
                this.compressorManifoldPressure *= this.compressor2ndThrottle;
                this.compressor1stThrottle = f18 / this.compressorRPMtoWMaxATA;
                if (this.compressor1stThrottle > 1.0F) {
                    this.compressor1stThrottle = 1.0F;
                }
                this.compressorManifoldPressure *= this.compressor1stThrottle;
                float f28 = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                f28 *= f35;
                if ((this.w <= 20F) && (this.w < 150F)) {
                    this.compressorManifoldPressure = Math.min(this.compressorManifoldPressure, f4 * (0.4F + ((this.w - 20F) * 0.04F)));
                }
                if (this.w < 20F) {
                    this.compressorManifoldPressure = f4 * (1.0F - (this.w * 0.03F));
                }
                return f28;
        }
        return 1.0F;
    }

    private float getMagnetoMultiplier() {
        switch (this.controlMagneto) {
            case 0: // '\0'
                return 0.0F;

            case 1: // '\001'
                return this.bMagnetos[0] ? 0.87F : 0.0F;

            case 2: // '\002'
                return this.bMagnetos[1] ? 0.87F : 0.0F;

            case 3: // '\003'
                float f = 0.0F;
                f += this.bMagnetos[0] ? 0.87F : 0.0F;
                f += this.bMagnetos[1] ? 0.87F : 0.0F;
                if (f > 1.0F) {
                    f = 1.0F;
                }
                return f;
        }
        return 1.0F;
    }

    private float getMixMultiplier() {
        float f4 = 0.0F;
        switch (this.mixerType) {
            case 0: // '\0'
                return 1.0F;

            case 1: // '\001'
                if (this.controlMix == 1.0F) {
                    if (this.bFloodCarb) {
                        this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                    } else {
                        this.reference.AS.setSootState(this.reference.actor, this.number, 0);
                    }
                    return 1.0F;
                }
                // fall through

            case 2: // '\002'
                if (this.reference.isPlayers() && (this.reference instanceof RealFlightModel) && ((RealFlightModel) this.reference).isRealMode()) {
                    if (!World.cur().diffCur.ComplexEManagement) {
                        return 1.0F;
                    }
                    float f = this.mixerLowPressureBar * this.controlMix;
                    if (f < (this.pressureExtBar - f4)) {
                        if (f < 0.03F) {
                            this.setEngineStops(this.reference.actor);
                        }
                        if (this.bFloodCarb) {
                            this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                        } else {
                            this.reference.AS.setSootState(this.reference.actor, this.number, 0);
                        }
                        if (f > ((this.pressureExtBar - f4) * 0.25F)) {
                            return 1.0F;
                        } else {
                            float f2 = f / ((this.pressureExtBar - f4) * 0.25F);
                            return f2;
                        }
                    }
                    if (f > this.pressureExtBar) {
                        this.producedDistabilisation += 0.0F + (35F * (1.0F - ((this.pressureExtBar + f4) / (f + 0.0001F))));
                        this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                        float f3 = (this.pressureExtBar + f4) / (f + 0.0001F);
                        return f3;
                    }
                    if (this.bFloodCarb) {
                        this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                    } else {
                        this.reference.AS.setSootState(this.reference.actor, this.number, 0);
                    }
                    return 1.0F;
                }
                float f1 = this.mixerLowPressureBar * this.controlMix;
                if ((f1 < (this.pressureExtBar - f4)) && (f1 < 0.03F)) {
                    this.setEngineStops(this.reference.actor);
                }
                return 1.0F;

            default:
                return 1.0F;
        }
    }

    private float getStageMultiplier() {
        return this.stage != 6 ? 0.0F : 1.0F;
    }

    public void setFricCoeffT(float f) {
        this.fricCoeffT = f;
    }

    private float getFrictionMoment(float f) {
        float f1 = 0.0F;
        if (!this.bPropHit && (this.bIsInoperable || (this.stage == 0) || (this.controlMagneto == 0))) {
            /* TODO: ok,this is spindown stuff :) */
            if ((((Interpolate) (this.reference)).actor instanceof BF_109) || (((Interpolate) (this.reference)).actor instanceof BF_110) || (((Interpolate) (this.reference)).actor instanceof JU_87) || (((Interpolate) (this.reference)).actor instanceof JU_88) || (((Interpolate) (this.reference)).actor instanceof JU_88NEW) || ((((Interpolate) (this.reference)).actor instanceof FW_190) && (this.type == _E_TYPE_INLINE))) {
                this.fricCoeffT += 0.09F * f; /* this value determines how fast will propeller loose speed when engine is turned off */
                if (this.fricCoeffT > 0.25F) {
                    this.fricCoeffT = 0.25F;
                }
            } else if ((((Interpolate) (this.reference)).actor instanceof P_51) || (((Interpolate) (this.reference)).actor instanceof P_47) || (((Interpolate) (this.reference)).actor instanceof P_38) || (((Interpolate) (this.reference)).actor instanceof P_39) || (((Interpolate) (this.reference)).actor instanceof P_40) || (((Interpolate) (this.reference)).actor instanceof P_40SUKAISVOLOCH) || (((Interpolate) (this.reference)).actor instanceof SPITFIRE) || (((Interpolate) (this.reference)).actor instanceof Hurricane) || ((((Interpolate) (this.reference)).actor instanceof FW_190) && (this.type == _E_TYPE_RADIAL)) || (this.type == _E_TYPE_RADIAL)) {
                this.fricCoeffT += 0.08F * f;
                if (this.fricCoeffT > 0.2F) {
                    this.fricCoeffT = 0.2F;
                }
            } else if (this.type == _E_TYPE_ROTARY) // TODO: Spin-down behaviour for rotaries. Great rotating inertial mass, hence longer spin-down
            {
                this.fricCoeffT += 0.07F * f;
                if (this.fricCoeffT > 0.05F) {
                    this.fricCoeffT = 0.05F;
                }
            } else {
                this.fricCoeffT += 0.09F * f;
                if (this.fricCoeffT > 0.3F) {
                    this.fricCoeffT = 0.3F;
                }
            }
            float f2 = this.w * this._1_wMax;
            f1 = (-this.fricCoeffT * (6F + (3.8F * f2)) * (this.propI + this.engineI)) / f;
            float f3 = (-0.99F * this.w * (this.propI + this.engineI)) / f;
            if (f1 < f3) {
                f1 = f3;
            }
        } else if (this.bPropHit && (this.bIsInoperable || (this.stage == 0) || (this.controlMagneto == 0))) {
            this.fricCoeffT += 0.2F * f;
            if (this.fricCoeffT > 2.0F) {
                this.fricCoeffT = 2.0F;
            }
            float f2 = this.w * this._1_wMax;
            f1 = (-this.fricCoeffT * (6F + (3.8F * f2)) * (this.propI + this.engineI)) / f;
            float f3 = (-0.99F * this.w * (this.propI + this.engineI)) / f;
            if (f1 < f3) {
                f1 = f3;
            }
        } else {
            this.fricCoeffT = 0.0F;
        }
        if ((this.stage == 0) && (this.w > this.wMaxAllowed)) {
            this.doSetEngineStuck(); /* I've put this in just so you can't overrev engine with windmilling */
        }
        return f1;
    }

    private float getJetFrictionMoment(float f) {
        float f1 = 0.0F;
        if (this.bIsInoperable || (this.stage == 0)) {
            f1 = (-0.002F * this.w * (this.propI + this.engineI)) / f;
        }
        return f1;
    }

    public Vector3f getEngineForce() {
        return this.engineForce;
    }

    public Vector3f getEngineTorque() {
        return this.engineTorque;
    }

    public Vector3d getEngineGyro() {
        tmpV3d1.cross(this.reference.getW(), this.propIW);
        return tmpV3d1;
    }

    public float getEngineMomentRatio() {
        return this.engineMoment / this.engineMomentMax;
    }

    public boolean isPropAngleDeviceOperational() {
        return this.bIsAngleDeviceOperational;
    }

    public float getCriticalW() {
        return this.wMaxAllowed;
    }

    public float getPropPhiMin() {
        return this.propPhiMin;
    }

    public int getAfterburnerType() {
        return this.afterburnerType;
    }

    private float toRadianPerSecond(float f) {
        return (f * 3.141593F * 2.0F) / 60F;
    }

    private float toRPM(float f) {
        return (f * 60F) / 2.0F / 3.141593F;
    }

    // TODO: Altered by |ZUTI|: from private to public.
    public float getKforH(float f, float f1, float f2) {
        float f3 = (Atmosphere.density(f2) * (f1 * f1)) / (Atmosphere.density(0.0F) * (f * f));
        if (this.type != 2) {
            f3 = (f3 * this.kV(f)) / this.kV(f1);
        }
        return f3;
    }

    private float kV(float f) {
        return 1.0F - (0.0032F * f);
    }

    public final void setAfterburnerType(int i) {
        this.afterburnerType = i;
    }

    public final void setPropReductorValue(float f) {
        this.propReductor = f;
    }

    public void replicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.controlMagneto | (this.stage << 4));
        netmsgguaranted.writeByte(this.cylinders);
        netmsgguaranted.writeByte(this.cylindersOperable);
        netmsgguaranted.writeByte((int) (255F * this.readyness));
        netmsgguaranted.writeByte((int) (255F * ((this.propPhi - this.propPhiMin) / (this.propPhiMax - this.propPhiMin))));
        netmsgguaranted.writeFloat(this.w);
    }

    public void replicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.stage = (i & 0xf0) >> 4;
        this.controlMagneto = i & 0xf;
        this.cylinders = netmsginput.readUnsignedByte();
        this.cylindersOperable = netmsginput.readUnsignedByte();
        this.readyness = netmsginput.readUnsignedByte() / 255F;
        this.propPhi = ((netmsginput.readUnsignedByte() / 255F) * (this.propPhiMax - this.propPhiMin)) + this.propPhiMin;
        this.w = netmsginput.readFloat();
    }
}
