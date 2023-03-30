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
    // TODO: Modified by |ZUTI|: changed all private variables to protected
    public static final int      _E_TYPE_INLINE                  = 0;
    public static final int      _E_TYPE_RADIAL                  = 1;
    public static final int      _E_TYPE_JET                     = 2;
    public static final int      _E_TYPE_ROCKET                  = 3;
    public static final int      _E_TYPE_ROCKETBOOST             = 4;
    public static final int      _E_TYPE_TOW                     = 5;
    public static final int      _E_TYPE_PVRD                    = 6;
    public static final int      _E_TYPE_HELO_INLINE             = 7;
    public static final int      _E_TYPE_UNIDENTIFIED            = 8;
    public static final int      _E_PROP_DIR_LEFT                = 0;
    public static final int      _E_PROP_DIR_RIGHT               = 1;
    public static final int      _E_STAGE_NULL                   = 0;
    public static final int      _E_STAGE_WAKE_UP                = 1;
    public static final int      _E_STAGE_STARTER_ROLL           = 2;
    public static final int      _E_STAGE_CATCH_UP               = 3;
    public static final int      _E_STAGE_CATCH_ROLL             = 4;
    public static final int      _E_STAGE_CATCH_FIRE             = 5;
    public static final int      _E_STAGE_NOMINAL                = 6;
    public static final int      _E_STAGE_DEAD                   = 7;
    public static final int      _E_STAGE_STUCK                  = 8;
    public static final int      _E_PROP_FIXED                   = 0;
    public static final int      _E_PROP_RETAIN_RPM_1            = 1;
    public static final int      _E_PROP_RETAIN_RPM_2            = 2;
    public static final int      _E_PROP_RETAIN_AOA_1            = 3;
    public static final int      _E_PROP_RETAIN_AOA_2            = 4;
    public static final int      _E_PROP_FRICTION                = 5;
    public static final int      _E_PROP_MANUALDRIVEN            = 6;
    public static final int      _E_PROP_WM_KOMANDGERAT          = 7;
    public static final int      _E_PROP_FW_KOMANDGERAT          = 8;
    public static final int      _E_PROP_CSP_EL                  = 9;
    public static final int      _E_CARB_SUCTION                 = 0;
    public static final int      _E_CARB_CARBURETOR              = 1;
    public static final int      _E_CARB_INJECTOR                = 2;
    public static final int      _E_CARB_FLOAT                   = 3;
    public static final int      _E_CARB_SHILLING                = 4;
    public static final int      _E_COMPRESSOR_NONE              = 0;
    public static final int      _E_COMPRESSOR_MANUALSTEP        = 1;
    public static final int      _E_COMPRESSOR_WM_KOMANDGERAT    = 2;
    public static final int      _E_COMPRESSOR_TURBO             = 3;
    public static final int      _E_MIXER_GENERIC                = 0;
    public static final int      _E_MIXER_BRIT_FULLAUTO          = 1;
    public static final int      _E_MIXER_LIMITED_PRESSURE       = 2;
    public static final int      _E_AFTERBURNER_GENERIC          = 0;
    public static final int      _E_AFTERBURNER_MW50             = 1;
    public static final int      _E_AFTERBURNER_GM1              = 2;
    public static final int      _E_AFTERBURNER_FIRECHAMBER      = 3;
    public static final int      _E_AFTERBURNER_WATER            = 4;
    public static final int      _E_AFTERBURNER_NO2              = 5;
    public static final int      _E_AFTERBURNER_FUEL_INJECTION   = 6;
    public static final int      _E_AFTERBURNER_FUEL_ILA5        = 7;
    public static final int      _E_AFTERBURNER_FUEL_ILA5AUTO    = 8;
    public static final int      _E_AFTERBURNER_WATERMETHANOL    = 9;
    public static final int      _E_AFTERBURNER_P51              = 10;
    public static final int      _E_AFTERBURNER_SPIT             = 11;
    public static int            heatStringID                    = -1;
    public FmSounds              isnd                            = null;
    public FlightModel           reference                       = null;
    public static boolean        bTFirst;
    public String                soundName                       = null;
    public String                startStopName                   = null;
    public String                propName                        = null;
    public int                   number                          = 0;
    public int                   type                            = 0;
    public int                   cylinders                       = 12;
    public float                 engineMass                      = 900.0F;
    public float                 wMin                            = 20.0F;
    public float                 wNom                            = 180.0F;
    public float                 wMax                            = 200.0F;
    public float                 wWEP                            = 220.0F;
    public float                 wMaxAllowed                     = 250.0F;
    public int                   wNetPrev                        = 0;
    public float                 engineMoment                    = 0.0F;
    public float                 engineMomentMax                 = 0.0F;
    public float                 engineBoostFactor               = 1.0F;
    public float                 engineAfterburnerBoostFactor    = 1.0F;
    public float                 engineDistAM                    = 0.0F;
    public float                 engineDistBM                    = 0.0F;
    public float                 engineDistCM                    = 0.0F;
    public float                 producedDistabilisation;
    public boolean               bRan                            = false;
    public Point3f               enginePos                       = new Point3f();
    public Vector3f              engineVector                    = new Vector3f();
    public Vector3f              engineForce                     = new Vector3f();
    public Vector3f              engineTorque                    = new Vector3f();
    public float                 engineDamageAccum               = 0.0F;
    public float                 _1_wMaxAllowed                  = 1.0F / this.wMaxAllowed;
    public float                 _1_wMax                         = 1.0F / this.wMax;
    public float                 RPMMin                          = 200.0F;
    public float                 RPMNom                          = 2000.0F;
    public float                 RPMMax                          = 2200.0F;
    public float                 Vopt                            = 90.0F;
    public float                 pressureExtBar;
    public double                momForFuel                      = 0.0;
    public double                addVflow                        = 0.0;
    public double                addVside                        = 0.0;
    public Point3f               propPos                         = new Point3f();
    public float                 propReductor                    = 1.0F;
    public int                   propAngleDeviceType             = 0;
    public float                 propAngleDeviceMinParam         = 0.0F;
    public float                 propAngleDeviceMaxParam         = 0.0F;
    public float                 propAngleDeviceAfterburnerParam = -999.9F;
    public int                   propDirection                   = 0;
    public float                 propDiameter                    = 3.0F;
    public float                 propMass                        = 30.0F;
    public float                 propI                           = 1.0F;
    public Vector3d              propIW                          = new Vector3d();
    public float                 propSEquivalent                 = 1.0F;
    public float                 propr                           = 1.125F;
    public float                 propPhiMin                      = (float) Math.toRadians(10.0);
    public float                 propPhiMax                      = (float) Math.toRadians(29.0);
    public float                 propPhi                         = (float) Math.toRadians(11.0);
    public float                 propPhiW;
    public float                 propAoA;
    public float                 propAoA0                        = (float) Math.toRadians(11.0);
    public float                 propAoACrit                     = (float) Math.toRadians(16.0);
    public float                 propAngleChangeSpeed            = 0.1F;
    public float                 propForce                       = 0.0F;
    public float                 propMoment                      = 0.0F;
    public float                 propTarget                      = 0.0F;
    public int                   mixerType                       = 0;
    public float                 mixerLowPressureBar             = 0.0F;
    public float                 horsePowers                     = 1200.0F;
    public float                 thrustMax                       = 10.7F;
    public int                   cylindersOperable               = 12;
    public float                 engineI                         = 1.0F;
    public float                 engineAcceleration              = 1.0F;
    public boolean[]             bMagnetos                       = { true, true };
    public boolean               bIsAutonomous                   = true;
    public boolean               bIsMaster                       = true;
    public boolean               bIsStuck                        = false;
    public boolean               bIsInoperable                   = false;
    public boolean               bIsAngleDeviceOperational       = true;
    public boolean               isPropAngleDeviceHydroOperable  = true;
    public int                   engineCarburetorType            = 0;
    public float                 FuelConsumptionP0               = 0.4F;
    public float                 FuelConsumptionP05              = 0.24F;
    public float                 FuelConsumptionP1               = 0.28F;
    public float                 FuelConsumptionPMAX             = 0.3F;
    public int                   compressorType                  = 0;
    public int                   compressorMaxStep               = 0;
    public float                 compressorPMax                  = 1.0F;
    public float                 compressorManifoldPressure      = 1.0F;
    public float[]               compressorAltitudes             = null;
    public float[]               compressorPressure              = null;
    public float[]               compressorAltMultipliers        = null;
    public float                 compressorRPMtoP0               = 1500.0F;
    public float                 compressorRPMtoCurvature        = -30.0F;
    public float                 compressorRPMtoPMax             = 2600.0F;
    public float                 compressorRPMtoWMaxATA          = 1.45F;
    public float                 compressorSpeedManifold         = 0.2F;
    public float[]               compressorRPM                   = new float[16];
    public float[]               compressorATA                   = new float[16];
    public int                   nOfCompPoints                   = 0;
    public boolean               compressorStepFound             = false;
    public float                 compressorManifoldThreshold     = 1.0F;
    public float                 afterburnerCompressorFactor     = 1.0F;
    public float                 _1_P0                           = 1.0F / Atmosphere.P0();
    public float                 compressor1stThrottle           = 1.0F;
    public float                 compressor2ndThrottle           = 1.0F;
    public float                 compressorPAt0                  = 0.3F;
    public int                   afterburnerType                 = 0;
    public boolean               afterburnerChangeW              = false;
    public int                   stage                           = 0;
    public int                   oldStage                        = 0;
    public long                  timer                           = 0L;
    public long                  given                           = 4611686018427387903L;
    public float                 rpm                             = 0.0F;
    public float                 w                               = 0.0F;
    public float                 aw                              = 0.0F;
    public float                 oldW                            = 0.0F;
    public float                 readyness                       = 1.0F;
    public float                 oldReadyness                    = 1.0F;
    public float                 radiatorReadyness               = 1.0F;
    public float                 rearRush;
    public float                 tOilIn                          = 0.0F;
    public float                 tOilOut                         = 0.0F;
    public float                 tWaterOut                       = 0.0F;
    public float                 tCylinders                      = 0.0F;
    public float                 tWaterCritMin;
    public float                 tWaterCritMax;
    public float                 tOilCritMin;
    public float                 tOilCritMax;
    public float                 tWaterMaxRPM;
    public float                 tOilOutMaxRPM;
    public float                 tOilInMaxRPM;
    public float                 tChangeSpeed;
    public float                 timeOverheat;
    public float                 timeUnderheat;
    public float                 timeCounter;
    public float                 oilMass                         = 90.0F;
    public float                 waterMass                       = 90.0F;
    public float                 Ptermo;
    public float                 R_air;
    public float                 R_oil;
    public float                 R_water;
    public float                 R_cyl_oil;
    public float                 R_cyl_water;
    public float                 C_eng;
    public float                 C_oil;
    public float                 C_water;
    public boolean               bHasThrottleControl             = true;
    public boolean               bHasAfterburnerControl          = true;
    public boolean               bHasPropControl                 = true;
    public boolean               bHasRadiatorControl             = true;
    public boolean               bHasMixControl                  = true;
    public boolean               bHasMagnetoControl              = true;
    public boolean               bHasExtinguisherControl         = false;
    public boolean               bHasCompressorControl           = false;
    public boolean               bHasFeatherControl              = false;
    public int                   extinguishers                   = 0;
    public float                 controlThrottle                 = 0.0F;
    public float                 controlRadiator                 = 0.0F;
    public boolean               controlAfterburner              = false;
    public float                 controlProp                     = 1.0F;
    public boolean               bControlPropAuto                = true;
    public float                 controlMix                      = 1.0F;
    public int                   controlMagneto                  = 0;
    public int                   controlCompressor               = 0;
    public int                   controlFeather                  = 0;
    public double                zatizeni;
    public float                 coolMult;
    public int                   controlPropDirection;
    public boolean               bWepRpmInLowGear;
    public boolean               fastATA                         = false;
    public Vector3f              old_engineForce                 = new Vector3f();
    public Vector3f              old_engineTorque                = new Vector3f();
    public float                 updateStep                      = 0.12F;
    public float                 updateLast                      = 0.0F;
    public float                 fricCoeffT                      = 1.0F;
    public static Vector3f       tmpV3f                          = new Vector3f();
    public static Vector3d       tmpV3d1                         = new Vector3d();
    public static Vector3d       tmpV3d2                         = new Vector3d();
    public static Point3f        safeloc                         = new Point3f();
    public static Point3d        safeLoc                         = new Point3d();
    public static Vector3f       safeVwld                        = new Vector3f();
    public static Vector3f       safeVflow                       = new Vector3f();
    public static boolean        tmpB;
    public static float          tmpF;
    public int                   engineNoFuelHUDLogId            = -1;
    private static final boolean DEBUG                           = false;

    // TODO: new variables in 410
    // --------------------------------
    public float   neg_G_Counter = 0.0F;
    public boolean bFullT        = false;
    public boolean bFloodCarb    = false;
    // --------------------------------

    public void load(FlightModel flightmodel, String string, String string_0_, int i) {
        this.reference = flightmodel;
        this.number = i;
        SectFile sectfile = FlightModelMain.sectFile(string);
        this.resolveFromFile(sectfile, "Generic");
        this.resolveFromFile(sectfile, string_0_);
        this.calcAfterburnerCompressorFactor();
        if (this.type == 0 || this.type == 1 || this.type == 7) this.initializeInline(flightmodel.Vmax);
        if (this.type == 2) this.initializeJet(flightmodel.Vmax);
    }

    private void resolveFromFile(SectFile sectfile, String string) {
        this.soundName = sectfile.get(string, "SoundName", this.soundName);
        this.propName = sectfile.get(string, "PropName", this.propName);
        this.startStopName = sectfile.get(string, "StartStopName", this.startStopName);
        Aircraft.debugprintln(this.reference.actor, "Resolving submodel " + string + " from file '" + sectfile.toString() + "'....");
        String string_1_ = sectfile.get(string, "Type");
        if (string_1_ != null) if (string_1_.endsWith("Inline")) this.type = 0;
        else if (string_1_.endsWith("Radial")) this.type = 1;
        else if (string_1_.endsWith("Jet")) this.type = 2;
        else if (string_1_.endsWith("RocketBoost")) this.type = 4;
        else if (string_1_.endsWith("Rocket")) this.type = 3;
        else if (string_1_.endsWith("Tow")) this.type = 5;
        else if (string_1_.endsWith("PVRD")) this.type = 6;
        else if (string_1_.endsWith("Unknown")) this.type = 8;
        else if (string_1_.endsWith("Azure")) this.type = 8;
        else if (string_1_.endsWith("HeloI")) this.type = 7;
        if (this.type == 0 || this.type == 1 || this.type == 7) {
            int i = sectfile.get(string, "Cylinders", -99999);
            if (i != -99999) {
                this.cylinders = i;
                this.cylindersOperable = this.cylinders;
            }
        }
        string_1_ = sectfile.get(string, "Direction");
        if (string_1_ != null) if (string_1_.endsWith("Left")) this.propDirection = 0;
        else if (string_1_.endsWith("Right")) this.propDirection = 1;
        float f = sectfile.get(string, "RPMMin", -99999.0F);
        if (f != -99999.0F) {
            this.RPMMin = f;
            this.wMin = this.toRadianPerSecond(this.RPMMin);
        }
        f = sectfile.get(string, "RPMNom", -99999.0F);
        if (f != -99999.0F) {
            this.RPMNom = f;
            this.wNom = this.toRadianPerSecond(this.RPMNom);
        }
        f = sectfile.get(string, "RPMMax", -99999.0F);
        if (f != -99999.0F) {
            this.RPMMax = f;
            this.wMax = this.toRadianPerSecond(this.RPMMax);
            this._1_wMax = 1.0F / this.wMax;
        }
        f = sectfile.get(string, "RPMMaxAllowed", -99999.0F);
        if (f != -99999.0F) {
            this.wMaxAllowed = this.toRadianPerSecond(f);
            this._1_wMaxAllowed = 1.0F / this.wMaxAllowed;
        }
        f = sectfile.get(string, "Reductor", -99999.0F);
        if (f != -99999.0F) this.propReductor = f;
        if (this.type == 0 || this.type == 1 || this.type == 7) {
            f = sectfile.get(string, "HorsePowers", -99999.0F);
            if (f != -99999.0F) this.horsePowers = f;
            int i = sectfile.get(string, "Carburetor", -99999);
            if (i != -99999) this.engineCarburetorType = i;
            f = sectfile.get(string, "Mass", -99999.0F);
            if (f != -99999.0F) this.engineMass = f;
            else this.engineMass = this.horsePowers * 0.6F;
        } else {
            f = sectfile.get(string, "Thrust", -99999.0F);
            if (f != -99999.0F) this.thrustMax = f * 9.81F;
        }
        f = sectfile.get(string, "BoostFactor", -99999.0F);
        if (f != -99999.0F) this.engineBoostFactor = f;
        f = sectfile.get(string, "WEPBoostFactor", -99999.0F);
        if (f != -99999.0F) this.engineAfterburnerBoostFactor = f;
        if (this.type == 2) {
            this.FuelConsumptionP0 = 0.075F;
            this.FuelConsumptionP05 = 0.075F;
            this.FuelConsumptionP1 = 0.1F;
            this.FuelConsumptionPMAX = 0.11F;
        }
        if (this.type == 6) {
            this.FuelConsumptionP0 = 0.835F;
            this.FuelConsumptionP05 = 0.835F;
            this.FuelConsumptionP1 = 0.835F;
            this.FuelConsumptionPMAX = 0.835F;
        }
        f = sectfile.get(string, "FuelConsumptionP0", -99999.0F);
        if (f != -99999.0F) this.FuelConsumptionP0 = f;
        f = sectfile.get(string, "FuelConsumptionP05", -99999.0F);
        if (f != -99999.0F) this.FuelConsumptionP05 = f;
        f = sectfile.get(string, "FuelConsumptionP1", -99999.0F);
        if (f != -99999.0F) this.FuelConsumptionP1 = f;
        f = sectfile.get(string, "FuelConsumptionPMAX", -99999.0F);
        if (f != -99999.0F) this.FuelConsumptionPMAX = f;
        int i = sectfile.get(string, "Autonomous", -99999);
        if (i != -99999) if (i == 0) this.bIsAutonomous = false;
        else if (i == 1) this.bIsAutonomous = true;
        i = sectfile.get(string, "cThrottle", -99999);
        if (i != -99999) if (i == 0) this.bHasThrottleControl = false;
        else if (i == 1) this.bHasThrottleControl = true;
        i = sectfile.get(string, "cAfterburner", -99999);
        if (i != -99999) if (i == 0) this.bHasAfterburnerControl = false;
        else if (i == 1) this.bHasAfterburnerControl = true;
        i = sectfile.get(string, "cProp", -99999);
        if (i != -99999) if (i == 0) this.bHasPropControl = false;
        else if (i == 1) this.bHasPropControl = true;
        i = sectfile.get(string, "cMix", -99999);
        if (i != -99999) if (i == 0) this.bHasMixControl = false;
        else if (i == 1) this.bHasMixControl = true;
        i = sectfile.get(string, "cMagneto", -99999);
        if (i != -99999) if (i == 0) this.bHasMagnetoControl = false;
        else if (i == 1) this.bHasMagnetoControl = true;
        i = sectfile.get(string, "cCompressor", -99999);
        if (i != -99999) if (i == 0) this.bHasCompressorControl = false;
        else if (i == 1) this.bHasCompressorControl = true;
        i = sectfile.get(string, "cFeather", -99999);
        if (i != -99999) if (i == 0) this.bHasFeatherControl = false;
        else if (i == 1) this.bHasFeatherControl = true;
        i = sectfile.get(string, "cRadiator", -99999);
        if (i != -99999) if (i == 0) this.bHasRadiatorControl = false;
        else if (i == 1) this.bHasRadiatorControl = true;
        i = sectfile.get(string, "Extinguishers", -99999);
        if (i != -99999) {
            this.extinguishers = i;
            if (i != 0) this.bHasExtinguisherControl = true;
            else this.bHasExtinguisherControl = false;
        }
        f = sectfile.get(string, "PropDiameter", -99999.0F);
        if (f != -99999.0F) this.propDiameter = f;
        this.propr = 0.5F * this.propDiameter * 0.75F;
        f = sectfile.get(string, "PropMass", -99999.0F);
        if (f != -99999.0F) this.propMass = f;
        this.propI = this.propMass * this.propDiameter * this.propDiameter * 0.083F;
        this.bWepRpmInLowGear = false;
        i = sectfile.get(string, "PropAnglerType", -99999);
        if (i != -99999) {
            if (i > 255) {
                this.bWepRpmInLowGear = (i & 0x100) > 1;
                i -= 256;
            }
            this.propAngleDeviceType = i;
        }
        f = sectfile.get(string, "PropAnglerSpeed", -99999.0F);
        if (f != -99999.0F) this.propAngleChangeSpeed = f;
        f = sectfile.get(string, "PropAnglerMinParam", -99999.0F);
        if (f != -99999.0F) {
            this.propAngleDeviceMinParam = f;
            if (this.propAngleDeviceType == 6 || this.propAngleDeviceType == 5) this.propAngleDeviceMinParam = (float) Math.toRadians(this.propAngleDeviceMinParam);
            if (this.propAngleDeviceType == 1 || this.propAngleDeviceType == 2 || this.propAngleDeviceType == 7 || this.propAngleDeviceType == 8 || this.propAngleDeviceType == 9)
                this.propAngleDeviceMinParam = this.toRadianPerSecond(this.propAngleDeviceMinParam);
        }
        f = sectfile.get(string, "PropAnglerMaxParam", -99999.0F);
        if (f != -99999.0F) {
            this.propAngleDeviceMaxParam = f;
            if (this.propAngleDeviceType == 6 || this.propAngleDeviceType == 5) this.propAngleDeviceMaxParam = (float) Math.toRadians(this.propAngleDeviceMaxParam);
            if (this.propAngleDeviceType == 1 || this.propAngleDeviceType == 2 || this.propAngleDeviceType == 7 || this.propAngleDeviceType == 8 || this.propAngleDeviceType == 9)
                this.propAngleDeviceMaxParam = this.toRadianPerSecond(this.propAngleDeviceMaxParam);
            if (this.propAngleDeviceAfterburnerParam == -999.9F) this.propAngleDeviceAfterburnerParam = this.propAngleDeviceMaxParam;
        }
        f = sectfile.get(string, "PropAnglerAfterburnerParam", -99999.0F);
        if (f != -99999.0F) {
            this.propAngleDeviceAfterburnerParam = f;
            this.wWEP = this.toRadianPerSecond(this.propAngleDeviceAfterburnerParam);
            if (this.wWEP != this.wMax) this.afterburnerChangeW = true;
            if (this.propAngleDeviceType == 6 || this.propAngleDeviceType == 5) this.propAngleDeviceAfterburnerParam = (float) Math.toRadians(this.propAngleDeviceAfterburnerParam);
            if (this.propAngleDeviceType == 1 || this.propAngleDeviceType == 2 || this.propAngleDeviceType == 7 || this.propAngleDeviceType == 8 || this.propAngleDeviceType == 9)
                this.propAngleDeviceAfterburnerParam = this.toRadianPerSecond(this.propAngleDeviceAfterburnerParam);
        } else this.wWEP = this.wMax;
        f = sectfile.get(string, "PropPhiMin", -99999.0F);
        if (f != -99999.0F) {
            this.propPhiMin = (float) Math.toRadians(f);
            if (this.propPhi < this.propPhiMin) this.propPhi = this.propPhiMin;
            if (this.propTarget < this.propPhiMin) this.propTarget = this.propPhiMin;
        }
        f = sectfile.get(string, "PropPhiMax", -99999.0F);
        if (f != -99999.0F) {
            this.propPhiMax = (float) Math.toRadians(f);
            if (this.propPhi > this.propPhiMax) this.propPhi = this.propPhiMax;
            if (this.propTarget > this.propPhiMax) this.propTarget = this.propPhiMax;
        }
        f = sectfile.get(string, "PropAoA0", -99999.0F);
        if (f != -99999.0F) this.propAoA0 = (float) Math.toRadians(f);
        i = sectfile.get(string, "CompressorType", -99999);
        if (i != -99999) this.compressorType = i;
        f = sectfile.get(string, "CompressorPMax", -99999.0F);
        if (f != -99999.0F) this.compressorPMax = f;
        i = sectfile.get(string, "CompressorSteps", -99999);
        if (i != -99999) {
            this.compressorMaxStep = i - 1;
            if (this.compressorMaxStep < 0) this.compressorMaxStep = 0;
        }
        if (this.compressorAltitudes != null && this.compressorAltitudes.length == this.compressorMaxStep + 1) {
            /* empty */
        }
        this.compressorAltitudes = new float[this.compressorMaxStep + 1];
        this.compressorPressure = new float[this.compressorMaxStep + 1];
        this.compressorAltMultipliers = new float[this.compressorMaxStep + 1];
        if (this.compressorAltitudes.length > 0) for (int i_2_ = 0; i_2_ < this.compressorAltitudes.length; i_2_++) {
            f = sectfile.get(string, "CompressorAltitude" + i_2_, -99999.0F);
            if (f != -99999.0F) {
                this.compressorAltitudes[i_2_] = f;
                this.compressorPressure[i_2_] = Atmosphere.pressure(this.compressorAltitudes[i_2_]) * this._1_P0;
            }
            f = sectfile.get(string, "CompressorMultiplier" + i_2_, -99999.0F);
            if (f != -99999.0F) this.compressorAltMultipliers[i_2_] = f;
        }
        f = sectfile.get(string, "CompressorRPMP0", -99999.0F);
        if (f != -99999.0F) {
            this.compressorRPMtoP0 = f;
            this.insetrPoiInCompressorPoly(this.compressorRPMtoP0, 1.0F);
        }
        f = sectfile.get(string, "CompressorRPMCurvature", -99999.0F);
        if (f != -99999.0F) this.compressorRPMtoCurvature = f;
        f = sectfile.get(string, "CompressorMaxATARPM", -99999.0F);
        if (f != -99999.0F) {
            this.compressorRPMtoWMaxATA = f;
            this.insetrPoiInCompressorPoly(this.RPMMax, this.compressorRPMtoWMaxATA);
        }
        f = sectfile.get(string, "CompressorRPMPMax", -99999.0F);
        if (f != -99999.0F) {
            this.compressorRPMtoPMax = f;
            this.insetrPoiInCompressorPoly(this.compressorRPMtoPMax, this.compressorPMax);
        }
        f = sectfile.get(string, "CompressorSpeedManifold", -99999.0F);
        if (f != -99999.0F) this.compressorSpeedManifold = f;
        f = sectfile.get(string, "CompressorPAt0", -99999.0F);
        if (f != -99999.0F) this.compressorPAt0 = f;
        f = sectfile.get(string, "Voptimal", -99999.0F);
        if (f != -99999.0F) this.Vopt = f * 0.277778F;
        boolean bool = true;
        float f_3_ = 2000.0F;
        float f_4_ = 1.0F;
        int i_5_ = 0;
        while (bool) {
            f = sectfile.get(string, "CompressorRPM" + i_5_, -99999.0F);
            if (f != -99999.0F) f_3_ = f;
            else bool = false;
            f = sectfile.get(string, "CompressorATA" + i_5_, -99999.0F);
            if (f != -99999.0F) f_4_ = f;
            else bool = false;
            if (bool) this.insetrPoiInCompressorPoly(f_3_, f_4_);
            i_5_++;
            if (this.nOfCompPoints > 15 || i_5_ > 15) bool = false;
        }
        i = sectfile.get(string, "AfterburnerType", -99999);
        if (i != -99999) this.afterburnerType = i;
        i = sectfile.get(string, "MixerType", -99999);
        if (i != -99999) this.mixerType = i;
        f = sectfile.get(string, "MixerAltitude", -99999.0F);
        if (f != -99999.0F) this.mixerLowPressureBar = Atmosphere.pressure(f) / Atmosphere.P0();
        f = sectfile.get(string, "EngineI", -99999.0F);
        if (f != -99999.0F) this.engineI = f;
        f = sectfile.get(string, "EngineAcceleration", -99999.0F);
        if (f != -99999.0F) this.engineAcceleration = f;
        f = sectfile.get(string, "DisP0x", -99999.0F);
        if (f != -99999.0F) {
            float f_6_ = sectfile.get(string, "DisP0x", -99999.0F);
            f_6_ = this.toRadianPerSecond(f_6_);
            float f_7_ = sectfile.get(string, "DisP0y", -99999.0F);
            f_7_ *= 0.01F;
            float f_8_ = sectfile.get(string, "DisP1x", -99999.0F);
            f_8_ = this.toRadianPerSecond(f_8_);
            float f_9_ = sectfile.get(string, "DisP1y", -99999.0F);
            f_9_ *= 0.01F;
            float f_10_ = f_6_;
            float f_11_ = f_7_;
            float f_12_ = (f_8_ - f_6_) * (f_8_ - f_6_);
            float f_13_ = f_9_ - f_7_;
            this.engineDistAM = f_13_ / f_12_;
            this.engineDistBM = -2.0F * f_13_ * f_10_ / f_12_;
            this.engineDistCM = f_11_ + f_13_ * f_10_ * f_10_ / f_12_;
        }
        this.timeCounter = 0.0F;
        f = sectfile.get(string, "TESPEED", -99999.0F);
        if (f != -99999.0F) this.tChangeSpeed = f;
        f = sectfile.get(string, "TWATERMAXRPM", -99999.0F);
        if (f != -99999.0F) this.tWaterMaxRPM = f;
        f = sectfile.get(string, "TOILINMAXRPM", -99999.0F);
        if (f != -99999.0F) this.tOilInMaxRPM = f;
        f = sectfile.get(string, "TOILOUTMAXRPM", -99999.0F);
        if (f != -99999.0F) this.tOilOutMaxRPM = f;
        f = sectfile.get(string, "MAXRPMTIME", -99999.0F);
        if (f != -99999.0F) this.timeOverheat = f;
        f = sectfile.get(string, "MINRPMTIME", -99999.0F);
        if (f != -99999.0F) this.timeUnderheat = f;
        f = sectfile.get(string, "TWATERMAX", -99999.0F);
        if (f != -99999.0F) this.tWaterCritMax = f;
        f = sectfile.get(string, "TWATERMIN", -99999.0F);
        if (f != -99999.0F) this.tWaterCritMin = f;
        f = sectfile.get(string, "TOILMAX", -99999.0F);
        if (f != -99999.0F) this.tOilCritMax = f;
        f = sectfile.get(string, "TOILMIN", -99999.0F);
        if (f != -99999.0F) this.tOilCritMin = f;
        this.coolMult = 1.0F;
    }

    private void initializeInline(float f) {
        this.propSEquivalent = 0.26F * this.propr * this.propr;
        this.engineMomentMax = this.horsePowers * 746.0F * 1.2F / this.wMax;
    }

    private void initializeJet(float f) {
        this.propSEquivalent = this.cylinders * this.cylinders * (2.0F * this.thrustMax) / (this.getFanCy(this.propAoA0) * Atmosphere.ro0() * this.wMax * this.wMax * this.propr * this.propr);
        this.computePropForces(this.wMax, 0.0F, 0.0F, this.propAoA0, 0.0F);
        this.engineMomentMax = this.propMoment;
    }

    public void initializeTowString(float f) {
        this.propForce = f;
    }

    public void setMaster(boolean bool) {
        this.bIsMaster = bool;
    }

    private void insetrPoiInCompressorPoly(float f, float f_14_) {
        int i;
        for (i = 0; i < this.nOfCompPoints; i++)
            if (!(this.compressorRPM[i] < f)) {
                if (this.compressorRPM[i] != f) break;
                return;
            }
        for (int i_15_ = this.nOfCompPoints - 1; i_15_ >= i; i_15_--) {
            this.compressorRPM[i_15_ + 1] = this.compressorRPM[i_15_];
            this.compressorATA[i_15_ + 1] = this.compressorATA[i_15_];
        }
        this.nOfCompPoints++;
        this.compressorRPM[i] = f;
        this.compressorATA[i] = f_14_;
    }

    private void calcAfterburnerCompressorFactor() {
        if (this.afterburnerType == 1 || this.afterburnerType == 7 || this.afterburnerType == 8 || this.afterburnerType == 10 || this.afterburnerType == 11 || this.afterburnerType == 6 || this.afterburnerType == 5 || this.afterburnerType == 9
                || this.afterburnerType == 4) {
            float f = this.compressorRPM[this.nOfCompPoints - 1];
            float f_16_ = this.compressorATA[this.nOfCompPoints - 1];
            this.nOfCompPoints--;
            int i = 0;
            int i_17_ = 1;
            float f_18_ = 1.0F;
            float f_19_ = f;
            if (this.nOfCompPoints < 2) this.afterburnerCompressorFactor = 1.0F;
            else {
                if (f_19_ < 0.1) f_18_ = Atmosphere.pressure((float) this.reference.Loc.z) * this._1_P0;
                else if (f_19_ >= this.compressorRPM[this.nOfCompPoints - 1]) f_18_ = this.compressorATA[this.nOfCompPoints - 1];
                else {
                    if (f_19_ < this.compressorRPM[0]) {
                        i = 0;
                        i_17_ = 1;
                    } else for (int i_20_ = 0; i_20_ < this.nOfCompPoints - 1; i_20_++)
                        if (this.compressorRPM[i_20_] <= f_19_ && f_19_ < this.compressorRPM[i_20_ + 1]) {
                            i = i_20_;
                            i_17_ = i_20_ + 1;
                            break;
                        }
                    float f_21_ = this.compressorRPM[i_17_] - this.compressorRPM[i];
                    if (f_21_ < 0.0010F) f_21_ = 0.0010F;
                    f_18_ = this.compressorATA[i] + (f_19_ - this.compressorRPM[i]) * (this.compressorATA[i_17_] - this.compressorATA[i]) / f_21_;
                }
                this.afterburnerCompressorFactor = f_16_ / f_18_;
            }
        } else this.afterburnerCompressorFactor = 1.0F;
    }

    public float getATA(float f) {
        int compressorAtaIndex = 0;
        int compressorRpmIndex = 1;
        float ata = 1.0F;
        if (this.nOfCompPoints < 2) return 1.0F;
        if (f < 0.1) ata = Atmosphere.pressure((float) this.reference.Loc.z) * this._1_P0;
        else if (f >= this.compressorRPM[this.nOfCompPoints - 1]) ata = this.compressorATA[this.nOfCompPoints - 1];
        else {
            if (f < this.compressorRPM[0]) {
                compressorAtaIndex = 0;
                compressorRpmIndex = 1;
            } else for (int compressorRpmCheckIndex = 0; compressorRpmCheckIndex < this.nOfCompPoints - 1; compressorRpmCheckIndex++)
                if (this.compressorRPM[compressorRpmCheckIndex] <= f && f < this.compressorRPM[compressorRpmCheckIndex + 1]) {
                    compressorAtaIndex = compressorRpmCheckIndex;
                    compressorRpmIndex = compressorRpmCheckIndex + 1;
                    break;
                }
            float compressorDiff = this.compressorRPM[compressorRpmIndex] - this.compressorRPM[compressorAtaIndex];
            if (compressorDiff < 0.0010F) compressorDiff = 0.0010F;
            ata = this.compressorATA[compressorAtaIndex] + (f - this.compressorRPM[compressorAtaIndex]) * (this.compressorATA[compressorRpmIndex] - this.compressorATA[compressorAtaIndex]) / compressorDiff;
        }
        return ata;
    }

    public void update(float f) {
        if (!(this.reference instanceof RealFlightModel) && Time.tickCounter() > 200) {
            this.updateLast += f;
            if (this.updateLast >= this.updateStep) f = this.updateStep;
            else {
                this.engineForce.set(this.old_engineForce);
                this.engineTorque.set(this.old_engineTorque);
                return;
            }
        }
        this.producedDistabilisation = 0.0F;
        this.pressureExtBar = Atmosphere.pressure(this.reference.getAltitude()) + this.compressorSpeedManifold * 0.5F * Atmosphere.density(this.reference.getAltitude()) * this.reference.getSpeed() * this.reference.getSpeed();
        this.pressureExtBar /= Atmosphere.P0();
        if (this.controlThrottle > 1.0F && this.engineBoostFactor == 1.0F) {
            this.reference.CT.setPowerControl(1.0F);
            if (this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()) HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[] { new Integer(100) });
        }
        this.computeForces(f);
        this.computeStage(f);
        if (this.stage > 0 && this.stage < 6) this.engineForce.set(0.0F, 0.0F, 0.0F);
        else if (this.stage == 8) this.rpm = this.w = 0.0F;
        if (this.reference.isPlayers()) {
            if (this.bIsMaster && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()) {
                this.computeTemperature(f);
                if (World.cur().diffCur.Reliability) this.computeReliability(f);
            }
            if (World.cur().diffCur.Limited_Fuel) this.computeFuel(f);
        } else this.computeFuel(f);
        this.old_engineForce.set(this.engineForce);
        this.old_engineTorque.set(this.engineTorque);
        this.updateLast = 0.0F;
        float f_26_ = 0.5F / (Math.abs(this.aw) + 1.0F) - 0.1F;
        if (f_26_ < 0.025F) f_26_ = 0.025F;
        if (f_26_ > 0.4F) f_26_ = 0.4F;
        if (f_26_ < this.updateStep) this.updateStep = 0.9F * this.updateStep + 0.1F * f_26_;
        else this.updateStep = 0.99F * this.updateStep + 0.01F * f_26_;
    }

    public void netupdate(float f, boolean bool) {
        this.computeStage(f);
        if (Math.abs(this.w) < 1.0E-5) this.propPhiW = 1.5707964F;
        else this.propPhiW = (float) Math.atan(this.reference.Vflow.x / (this.w * this.propReductor * this.propr));
        this.propAoA = this.propPhi - this.propPhiW;
        this.computePropForces(this.w * this.propReductor, (float) this.reference.Vflow.x, this.propPhi, this.propAoA, this.reference.getAltitude());
        float f_27_ = this.w;
        float f_28_ = this.propPhi;
        float f_29_ = this.compressorManifoldPressure;
        this.computeForces(f);
        if (bool) this.compressorManifoldPressure = f_29_;
        this.w = f_27_;
        this.propPhi = f_28_;
        this.rpm = this.toRPM(this.w);
    }

    public void setReadyness(Actor actor, float f) {
        if (f > 1.0F) f = 1.0F;
        if (f < 0.0F) f = 0.0F;
        if (Actor.isAlive(actor)) {
            if (this.bIsMaster) {
                if (this.readyness > 0.0F && f == 0.0F) {
                    this.readyness = 0.0F;
                    this.setEngineDies(actor);
                    return;
                }
                this.doSetReadyness(f);
            }
            if (Math.abs(this.oldReadyness - this.readyness) > 0.1F) {
                this.reference.AS.setEngineReadyness(actor, this.number, (int) (f * 100.0F));
                this.oldReadyness = this.readyness;
            }
        }
    }

    private void setReadyness(float f) {
        this.setReadyness(this.reference.actor, f);
    }

    public void doSetReadyness(float f) {
        this.readyness = f;
    }

    public void setStage(Actor actor, int i) {
        if (Actor.isAlive(actor)) {
            if (this.bIsMaster) this.doSetStage(i);
            this.reference.AS.setEngineStage(actor, this.number, i);
        }
    }

    public void doSetStage(int i) {
        this.stage = i;
    }

    public void setEngineStarts(Actor actor) {
        if (this.bIsMaster && Actor.isAlive(actor) && (!this.isHasControlMagnetos() || !(this.getMagnetoMultiplier() < 0.1F))) this.reference.AS.setEngineStarts(this.number);
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
        } else if (this.stage == 0) {
            this.reference.CT.setMagnetoControl(3);
            this.setControlMagneto(3);
            this.stage = 1;
            this.timer = Time.current();
        }
    }

    public void setEngineStops(Actor actor) {
        if (Actor.isAlive(actor) && this.stage >= 1 && this.stage <= 6) this.reference.AS.setEngineStops(this.number);
    }

    public void doSetEngineStops() {
        if (this.stage != 0) {
            this.stage = 0;
            this.setControlMagneto(0);
            this.timer = Time.current();
        }
    }

    public void setEngineDies(Actor actor) {
        if (this.stage <= 6) this.reference.AS.setEngineDies(this.reference.actor, this.number);
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
                for (int i_30_ = 0; i_30_ < i; i_30_++)
                    f += this.reference.EI.engines[i_30_].getReadyness() / i;
                if (f < 0.7F) this.reference.setReadyToReturn(true);
                if (f < 0.3F) this.reference.setReadyToDie(true);
            }
            this.stage = 7;
            if (this.reference.isPlayers()) {
                printDebug("Motor doSetEngineDies()");
                HUD.log("FailedEngine");
            }
            this.timer = Time.current();
        }
    }

    public void setEngineRunning(Actor actor) {
        if (this.bIsMaster && Actor.isAlive(actor)) this.reference.AS.setEngineRunning(this.number);
    }

    public void doSetEngineRunning() {
        if (this.stage < 6) {
            this.stage = 6;
            this.reference.CT.setMagnetoControl(3);
            this.setControlMagneto(3);
            if (this.reference.isPlayers()) HUD.log("EngineI1");
            this.w = this.wMax * 0.75F;
            this.tWaterOut = 0.5F * (this.tWaterCritMin + this.tWaterMaxRPM);
            this.tOilOut = 0.5F * (this.tOilCritMin + this.tOilOutMaxRPM);
            this.tOilIn = 0.5F * (this.tOilCritMin + this.tOilInMaxRPM);
            this.propPhi = 0.5F * (this.propPhiMin + this.propPhiMax);
            this.propTarget = this.propPhi;
            if (this.isnd != null) this.isnd.onEngineState(this.stage);
        }
    }

    public void setKillCompressor(Actor actor) {
        this.reference.AS.setEngineSpecificDamage(actor, this.number, 0);
    }

    public void doSetKillCompressor() {
        switch (this.compressorType) {
            case 2:
                this.compressorAltitudes[0] = 50.0F;
                this.compressorAltMultipliers[0] = 1.0F;
                break;
            case 1:
                for (int i = 0; i < this.compressorMaxStep; i++) {
                    this.compressorAltitudes[i] = 50.0F;
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
        if (this.cylindersOperable < 0) this.cylindersOperable = 0;
        if (this.bIsMaster) if (this.getCylindersRatio() < 0.12F) this.setEngineDies(this.reference.actor);
        else if (this.getCylindersRatio() < this.getReadyness()) this.setReadyness(this.reference.actor, this.getCylindersRatio());
    }

    public void setMagnetoKnockOut(Actor actor, int i) {
        this.reference.AS.setEngineMagnetoKnockOut(this.reference.actor, this.number, i);
    }

    public void doSetMagnetoKnockOut(int i) {
        this.bMagnetos[i] = false;
        if (i == this.controlMagneto) this.setEngineStops(this.reference.actor);
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
            if (this.reference.isPlayers() && this.stage != 7) {
                printDebug("Motor doSetEngineStuck()");
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
                if (f > 1.0F && this.controlThrottle <= 1.0F && this.reference.M.requestNitro(1.0E-4F)) {
                    this.reference.CT.setAfterburnerControl(true);
                    this.setControlAfterburner(true);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP4");
                    }
                }
                if (f < 1.0F && this.controlThrottle >= 1.0F) {
                    this.reference.CT.setAfterburnerControl(false);
                    this.setControlAfterburner(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            } else if (this.afterburnerType == 8) {
                if (f > 1.0F && this.controlThrottle <= 1.0F) {
                    this.reference.CT.setAfterburnerControl(true);
                    this.setControlAfterburner(true);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP7");
                    }
                }
                if (f < 1.0F && this.controlThrottle >= 1.0F) {
                    this.reference.CT.setAfterburnerControl(false);
                    this.setControlAfterburner(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
            } else if (this.afterburnerType == 10) {
                if (f > 1.0F && this.controlThrottle <= 1.0F) {
                    this.reference.CT.setAfterburnerControl(true);
                    this.setControlAfterburner(true);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(true);
                        HUD.logRightBottom("BoostWepTP0");
                    }
                }
                if (f < 1.0F && this.controlThrottle >= 1.0F) {
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

    public void setControlAfterburner(boolean bool) {
        if (this.bHasAfterburnerControl) {
            if (this.afterburnerType == 1 && !this.controlAfterburner && bool && this.controlThrottle > 1.0F && World.Rnd().nextFloat() < 0.5F && this.reference.isPlayers() && this.reference instanceof RealFlightModel
                    && ((RealFlightModel) this.reference).isRealMode() && World.cur().diffCur.Vulnerability)
                this.setCyliderKnockOut(this.reference.actor, World.Rnd().nextInt(0, 3));
            this.controlAfterburner = bool;
        }
        if (this.afterburnerType == 4 || this.afterburnerType == 8 || this.afterburnerType == 10) this.controlAfterburner = bool;
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
        if (this.bHasPropControl) this.controlProp = f;
    }

    public void setControlPropAuto(boolean bool) {
        if (this.bHasPropControl) this.bControlPropAuto = bool && this.isAllowsAutoProp();
    }

    public void doSetKillControlProp() {
        this.bHasPropControl = false;
    }

    public void setControlMix(float f) {
        if (this.bHasMixControl) switch (this.mixerType) {
            case 0:
                this.controlMix = f;
                break;
            case 1:
                this.controlMix = f;
                if (this.controlMix < 1.0F) this.controlMix = 1.0F;
                break;
            default:
                this.controlMix = f;
        }
    }

    public void doSetKillControlMix() {
        this.bHasMixControl = false;
    }

    public void setControlMagneto(int i) {
        if (this.bHasMagnetoControl) {
            this.controlMagneto = i;
            if (i == 0) this.setEngineStops(this.reference.actor);
        }
    }

    public void setControlCompressor(int i) {
        if (this.bHasCompressorControl) this.controlCompressor = i;
    }

    public void setControlFeather(int i) {
        if (this.bHasFeatherControl) {
            this.controlFeather = i;
            if (this.reference.isPlayers()) HUD.log("EngineFeather" + this.controlFeather);
        }
    }

    public void setControlRadiator(float f) {
        if (this.bHasRadiatorControl) this.controlRadiator = f;
    }

    public void setExtinguisherFire() {
        if (this.bIsMaster) if (this.bHasExtinguisherControl) {
            this.reference.AS.setEngineSpecificDamage(this.reference.actor, this.number, 5);
            if (this.reference.AS.astateEngineStates[this.number] > 2) this.reference.AS.setEngineState(this.reference.actor, this.number, World.Rnd().nextInt(1, 2));
            else if (this.reference.AS.astateEngineStates[this.number] > 0) this.reference.AS.setEngineState(this.reference.actor, this.number, 0);
        }
    }

    public void doSetExtinguisherFire() {
        if (this.bHasExtinguisherControl) {
            this.extinguishers--;
            if (this.extinguishers == 0) this.bHasExtinguisherControl = false;
            this.reference.AS.doSetEngineExtinguisherVisuals(this.number);
            if (this.bIsMaster) {
                if (this.reference.AS.astateEngineStates[this.number] > 1 && World.Rnd().nextFloat() < 0.56F) this.reference.AS.repairEngine(this.number);
                if (this.reference.AS.astateEngineStates[this.number] > 3 && World.Rnd().nextFloat() < 0.21F) {
                    this.reference.AS.repairEngine(this.number);
                    this.reference.AS.repairEngine(this.number);
                }
                this.tWaterOut -= 4.0F;
                this.tOilIn -= 4.0F;
                this.tOilOut -= 4.0F;
            }
            if (this.reference.isPlayers()) HUD.log("ExtinguishersFired");
        }
    }

    private void computeStage(float f) {
        if (this.stage != 6) {
            bTFirst = false;
            float f_31_ = 20.0F;
            long l = Time.current() - this.timer;
            if (this.stage > 0 && this.stage < 6 && l > this.given) {
                this.stage++;
                this.timer = Time.current();
                l = 0L;
            }
            if (this.oldStage != this.stage) {
                bTFirst = true;
                this.oldStage = this.stage;
            }
            if (this.stage > 0 && this.stage < 6) this.setControlThrottle(0.2F);
            switch (this.stage) {
                case 0:
                    if (bTFirst) {
                        this.given = 4611686018427387903L;
                        this.timer = Time.current();
                    }
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    break;
                case 1:
                    if (bTFirst) {
                        if (this.bIsStuck) {
                            this.stage = 8;
                            break;
                        }
                        if (this.type == 3 || this.type == 4 || this.type == 6) {
                            this.stage = 5;
                            if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                            break;
                        }
                        if (this.type == 0 || this.type == 1 || this.type == 7) {
                            if (this.w > this.wMin) {
                                this.stage = 3;
                                if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                                break;
                            }
                            if (!this.bIsAutonomous) {
                                // TODO: Modified by |ZUTI|: disabled position check.
                                // if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
                                if (this.reference.isStationedOnGround()) {
                                    this.setControlMagneto(3);
                                    if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                                } else {
                                    this.doSetEngineStops();
                                    if (this.reference.isPlayers()) HUD.log("EngineI0");
                                    break;
                                }
                            } else if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                        } else if (!this.bIsAutonomous) {
                            // TODO: Modified by |ZUTI|: disabled position check.
                            // if ((Airport.distToNearestAirport(reference.Loc) < 1200.0) && reference.isStationedOnGround())
                            if (this.reference.isStationedOnGround()) {
                                this.setControlMagneto(3);
                                if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                            } else {
                                if (this.reference.getSpeedKMH() < 350.0F) {
                                    this.doSetEngineStops();
                                    if (this.reference.isPlayers()) HUD.log("EngineI0");
                                    break;
                                }
                                if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                            }
                        } else if (this.reference.isPlayers()) HUD.log("Starting_Engine");
                        this.given = (long) (500.0F * World.Rnd().nextFloat(1.0F, 2.0F));
                    }
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    this.reference.CT.setMagnetoControl(3);
                    this.setControlMagneto(3);
                    this.w = 0.1047F * (20.0F * l / this.given);
                    this.setControlThrottle(0.0F);
                    break;
                case 2:
                    if (bTFirst) {
                        this.given = (long) (4000.0F * World.Rnd().nextFloat(1.0F, 2.0F));
                        if (this.bRan) {
                            this.given = (long) (100.0F + (this.tOilOutMaxRPM - this.tOilOut) / (this.tOilOutMaxRPM - f_31_) * 7900.0F * World.Rnd().nextFloat(2.0F, 4.2F));
                            if (this.given > 9000L) this.given = World.Rnd().nextLong(7800L, 9600L);
                            if (this.bIsMaster && World.Rnd().nextFloat() < 0.5F) {
                                this.stage = 0;
                                this.reference.AS.setEngineStops(this.number);
                            }
                        }
                    }
                    this.w = 0.1047F * (20.0F + 15.0F * l / this.given);
                    this.setControlThrottle(0.0F);
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    break;
                case 3:
                    if (bTFirst) {
                        if (this.isnd != null) this.isnd.onEngineState(this.stage);
                        if (this.bIsInoperable) {
                            this.stage = 0;
                            this.doSetEngineDies();
                            break;
                        }
                        this.given = (long) (50.0F * World.Rnd().nextFloat(1.0F, 2.0F));
                        if (this.bIsMaster && World.Rnd().nextFloat() < 0.12F && (this.tOilOutMaxRPM - this.tOilOut) / (this.tOilOutMaxRPM - f_31_) < 0.75F) this.reference.AS.setEngineStops(this.number);
                    }
                    this.w = 0.1047F * (60.0F + 60.0F * l / this.given);
                    this.setControlThrottle(0.0F);
                    if (this.reference != null && this.type != 2 && this.type != 3 && this.type != 4 && this.type != 6 && this.type != 5) for (int i = 1; i < 32; i++)
                        try {
                            Hook hook = this.reference.actor.findHook("_Engine" + (this.number + 1) + "EF_" + (i < 10 ? "0" + i : "" + i));
                            if (hook != null) Eff3DActor.New(this.reference.actor, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineStart" + World.Rnd().nextInt(1, 3) + ".eff", -1.0F);
                        } catch (Exception exception) {
                            /* empty */
                        }
                    break;
                case 4:
                    if (bTFirst) this.given = (long) (500.0F * World.Rnd().nextFloat(1.0F, 2.0F));
                    this.w = 12.564F;
                    this.setControlThrottle(0.0F);
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    break;
                case 5:
                    if (bTFirst) {
                        this.given = (long) (500.0F * World.Rnd().nextFloat(1.0F, 2.0F));
                        if (this.bRan && (this.type == 0 || this.type == 1 || this.type == 7)) {
                            if ((this.tOilOutMaxRPM - this.tOilOut) / (this.tOilOutMaxRPM - f_31_) > 0.75F) if (this.type == 0 || this.type == 7) {
                                if (this.bIsMaster && this.getReadyness() > 0.75F && World.Rnd().nextFloat() < 0.25F) this.setReadyness(this.getReadyness() - 0.05F);
                            } else if (this.type == 1 && this.bIsMaster && World.Rnd().nextFloat() < 0.1F) this.reference.AS.setEngineDies(this.reference.actor, this.number);
                            if (this.bIsMaster && World.Rnd().nextFloat() < 0.1F) this.reference.AS.setEngineStops(this.number);
                        }
                        this.bRan = true;
                    }
                    this.w = 0.1047F * (120.0F + 120.0F * l / this.given);
                    this.setControlThrottle(0.2F);
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    break;
                case 6:
                    if (bTFirst) {
                        this.given = -1L;
                        this.reference.AS.setEngineRunning(this.number);
                    }
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    break;
                case 7:
                case 8:
                    if (bTFirst) this.given = -1L;
                    this.setReadyness(0.0F);
                    this.setControlMagneto(0);
                    if (this.isnd != null) this.isnd.onEngineState(this.stage);
                    break;
            }
        }
    }

    private void computeFuel(float f) {
        tmpF = 0.0F;
        if (this.stage == 6) {
            double d;
            float f_33_;
            switch (this.type) {
                case 0:
                case 1:
                case 7:
                    d = this.momForFuel * this.w * 0.00105;
                    f_33_ = (float) d / this.horsePowers;
                    if (d < this.horsePowers * 0.05) d = this.horsePowers * 0.05;
                    break;
                default:
                    d = this.thrustMax * (f_33_ = this.getPowerOutput());
                    if (d < this.thrustMax * 0.05) d = this.thrustMax * 0.05;
            }
            if (f_33_ < 0.0F) f_33_ = 0.0F;
            double d_34_;
            if (f_33_ <= 0.5F) d_34_ = this.FuelConsumptionP0 + (this.FuelConsumptionP05 - this.FuelConsumptionP0) * (2.0 * f_33_);
            else if (f_33_ <= 1.0) d_34_ = this.FuelConsumptionP05 + (this.FuelConsumptionP1 - this.FuelConsumptionP05) * (2.0 * (f_33_ - 0.5));
            else {
                float f_35_ = f_33_ - 1.0F;
                if (f_35_ > 0.1F) f_35_ = 0.1F;
                f_35_ *= 10.0F;
                d_34_ = this.FuelConsumptionP1 + (this.FuelConsumptionPMAX - this.FuelConsumptionP1) * f_35_;
            }
            d_34_ /= 3600.0;
            switch (this.type) {
                case 0:
                case 1:
                case 7: {
                    float f_36_ = (float) (d_34_ * d);
                    tmpF = f_36_ * f;
                    double d_37_ = f_36_ * 4.4E7F;
                    double d_38_ = f_36_ * 15.7F;
                    double d_39_ = 1010.0 * d_38_ * 700.0;
                    d *= 746.0;
                    this.Ptermo = (float) (d_37_ - d - d_39_);
                    break;
                }
                case 2:
                    tmpF = (float) (d_34_ * d * f);
                    break;
                case 3:
                    if (this.reference.actor instanceof BI_1 || this.reference.actor instanceof BI_6) tmpF = 1.8F * this.getPowerOutput() * f;
                    else if (this.reference.actor instanceof MXY_7) tmpF = 0.5F * this.getPowerOutput() * f;
                    else tmpF = 2.5777F * this.getPowerOutput() * f;
                    break;
                case 4:
                    tmpF = 1.4320556F * this.getPowerOutput() * f;
                    tmpB = this.reference.M.requestNitro(tmpF);
                    tmpF = 0.0F;
                    if (!tmpB && this.bIsMaster) {
                        this.setEngineStops(this.reference.actor);
                        if (this.reference.isPlayers() && this.engineNoFuelHUDLogId == -1) {
                            this.engineNoFuelHUDLogId = HUD.makeIdLog();
                            HUD.log(this.engineNoFuelHUDLogId, "EngineNoFuel");
                        }
                    } else break;
                    return;
                case 6:
                    tmpF = (float) (d_34_ * d * f);
                    tmpB = this.reference.M.requestNitro(tmpF);
                    tmpF = 0.0F;
                    if (!tmpB && this.bIsMaster) {
                        this.setEngineStops(this.reference.actor);
                        if (this.reference.isPlayers() && this.engineNoFuelHUDLogId == -1) {
                            this.engineNoFuelHUDLogId = HUD.makeIdLog();
                            HUD.log(this.engineNoFuelHUDLogId, "EngineNoFuel");
                        }
                    } else break;
                    return;
            }
        }
        tmpB = this.reference.M.requestFuel(tmpF);
        if (!tmpB && this.bIsMaster) {
            this.setEngineStops(this.reference.actor);
            this.reference.setCapableOfACM(false);
            this.reference.setCapableOfTaxiing(false);
            if (this.reference.isPlayers() && this.engineNoFuelHUDLogId == -1) {
                this.engineNoFuelHUDLogId = HUD.makeIdLog();
                HUD.log(this.engineNoFuelHUDLogId, "EngineNoFuel");
            }
        }
        if (this.controlAfterburner) switch (this.afterburnerType) {
            case 1:
                if (this.controlThrottle > 1.0F && !this.reference.M.requestNitro(0.044872F * f) && this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()
                        && World.cur().diffCur.Vulnerability)
                    this.setReadyness(this.reference.actor, this.getReadyness() - 0.01F * f);
                break;
            case 2:
                if (!this.reference.M.requestNitro(0.044872F * f)) {
                    /* empty */
                }
                break;
            case 5:
                if (!this.reference.M.requestNitro(0.044872F * f)) {
                    /* empty */
                }
                break;
            case 9:
                if (!this.reference.M.requestNitro(0.044872F * f)) {
                    /* empty */
                }
                break;
            case 4:
                if (!this.reference.M.requestNitro(0.044872F * f)) {
                    this.reference.CT.setAfterburnerControl(false);
                    if (this.reference.isPlayers()) {
                        Main3D.cur3D().aircraftHotKeys.setAfterburnerForAutoActivation(false);
                        HUD.logRightBottom(null);
                    }
                }
                break;
        }
    }

    private void computeReliability(float f) {
        if (this.stage == 6) {
            float f_40_ = this.controlThrottle;
            if (this.engineBoostFactor > 1.0F) f_40_ *= 0.9090909F;
            switch (this.type) {
                default:
                    this.zatizeni = f_40_;
                    this.zatizeni = this.zatizeni * this.zatizeni;
                    this.zatizeni = this.zatizeni * this.zatizeni;
                    this.zatizeni *= f * 6.19842621786999E-5;
                    if (this.zatizeni > World.Rnd().nextDouble(0.0, 1.0)) {
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
                case 0:
                case 1:
                case 7: {
                    this.zatizeni = this.coolMult * f_40_;
                    this.zatizeni *= this.w / this.wWEP;
                    this.zatizeni = this.zatizeni * this.zatizeni;
                    this.zatizeni = this.zatizeni * this.zatizeni;
                    double d = this.zatizeni * f * 1.4248134284734321E-5;
                    if (d > World.Rnd().nextDouble(0.0, 1.0)) {
                        int i = World.Rnd().nextInt(0, 19);
                        if (i < 10) {
                            this.reference.AS.setEngineCylinderKnockOut(this.reference.actor, this.number, 1);
                            Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - cylinder");
                        } else if (i < 12) {
                            if (i < 11) {
                                this.reference.AS.setEngineMagnetoKnockOut(this.reference.actor, this.number, 0);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - mag1");
                            } else {
                                this.reference.AS.setEngineMagnetoKnockOut(this.reference.actor, this.number, 1);
                                Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - mag2");
                            }
                        } else if (i < 14) {
                            this.reference.AS.setEngineDies(this.reference.actor, this.number);
                            Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - dead");
                        } else if (i < 15) {
                            this.reference.AS.setEngineStuck(this.reference.actor, this.number);
                            Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - stuck");
                        } else if (i < 17) {
                            this.setKillPropAngleDevice(this.reference.actor);
                            Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - propAngler");
                        } else {
                            this.reference.AS.hitOil(this.reference.actor, this.number);
                            Aircraft.debugprintln(this.reference.actor, "Malfunction #" + this.number + " - oil");
                        }
                    }
                }
            }
        }
    }

    private void computeTemperature(float f) {
        float f_41_ = Pitot.Indicator((float) this.reference.Loc.z, this.reference.getSpeedKMH());
        float f_42_ = Atmosphere.temperature((float) this.reference.Loc.z) - 273.15F;
        if (this.stage == 6) {
            float f_43_ = 1.05F * (float) Math.sqrt(Math.sqrt(this.getPowerOutput() > 0.2F ? this.getPowerOutput() + this.reference.AS.astateOilStates[this.number] * 0.33F : 0.2F))
                    * (float) Math.sqrt(this.w / this.wMax > 0.75F ? (double) (this.w / this.wMax) : 0.75) * this.tOilOutMaxRPM * (1.0F - 0.11F * this.controlRadiator) * (1.0F - f_41_ * 2.0E-4F) + 22.0F;
            if (this.getPowerOutput() > 1.0F) f_43_ *= this.getPowerOutput();
            this.tOilOut += (f_43_ - this.tOilOut) * f * this.tChangeSpeed;
        } else {
            float f_44_ = this.w / this.wMax * this.tOilOutMaxRPM * (1.0F - 0.2F * this.controlRadiator) + f_42_;
            Motor motor_45_ = this;
            motor_45_.tOilOut = motor_45_.tOilOut + (f_44_ - this.tOilOut) * f * this.tChangeSpeed * (this.type == 0 ? 0.42F : 1.07F);
        }
        float f_46_ = 0.8F - 0.05F * this.controlRadiator;
        float f_47_ = this.tOilOut * (f_46_ - f_41_ * 5.0E-4F) + f_42_ * (1.0F - f_46_ + f_41_ * 5.0E-4F);
        this.tOilIn += (f_47_ - this.tOilIn) * f * this.tChangeSpeed * 0.5F;
        f_47_ = 1.05F * (float) Math.sqrt(this.getPowerOutput()) * (1.0F - f_41_ * 2.0E-4F) * this.tWaterMaxRPM * (this.controlAfterburner ? 1.1F : 1.0F) + f_42_;
        Motor motor_48_ = this;
        motor_48_.tWaterOut = motor_48_.tWaterOut + (f_47_ - this.tWaterOut) * f * this.tChangeSpeed * (this.tWaterOut < 50.0F ? 0.4F : 1.0F) * (1.0F - f_41_ * 6.0E-4F);
        if (this.tOilOut < f_42_) this.tOilOut = f_42_;
        if (this.tOilIn < f_42_) this.tOilIn = f_42_;
        if (this.tWaterOut < f_42_) this.tWaterOut = f_42_;
        if (World.cur().diffCur.Engine_Overheat && (this.tWaterOut > this.tWaterCritMax || this.tOilOut > this.tOilCritMax)) {
            if (heatStringID == -1) heatStringID = HUD.makeIdLog();
            if (this.reference.isPlayers()) HUD.log(heatStringID, "EngineOverheat");
            this.timeCounter += f;
            if (this.timeCounter > this.timeOverheat) if (this.readyness > 0.32F) {
                this.setReadyness(this.readyness - 0.00666F * f);
                this.tOilCritMax -= 0.00666F * f * (this.tOilCritMax - this.tOilOutMaxRPM);
            } else this.setEngineDies(this.reference.actor);
        } else if (this.timeCounter > 0.0F) {
            this.timeCounter = 0.0F;
            if (heatStringID == -1) heatStringID = HUD.makeIdLog();
            if (this.reference.isPlayers()) HUD.log(heatStringID, "EngineRestored");
        }
    }

    public void updateRadiator(float f) {
        if (this.reference.actor instanceof GLADIATOR) this.controlRadiator = 0.0F;
        else if (this.reference.actor instanceof P_51 || this.reference.actor instanceof P_38 || this.reference.actor instanceof YAK_3 || this.reference.actor instanceof YAK_3P || this.reference.actor instanceof YAK_9M
                || this.reference.actor instanceof YAK_9U || this.reference.actor instanceof YAK_9UT || this.reference.actor instanceof P_63C) {
                    if (this.tOilOut > this.tOilOutMaxRPM) {
                        this.controlRadiator += 0.1F * f;
                        if (this.controlRadiator > 1.0F) this.controlRadiator = 1.0F;
                    } else {
                        this.controlRadiator = 1.0F - this.reference.getSpeed() / this.reference.VmaxH;
                        if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
                    }
                } else
            if (this.reference.actor instanceof SPITFIRE9 || this.reference.actor instanceof SPITFIRE8 || this.reference.actor instanceof SPITFIRE8CLP) {
                float f_49_ = 0.0F;
                if (this.tOilOut > this.tOilCritMin) {
                    float f_50_ = this.tOilCritMax - this.tOilCritMin;
                    f_49_ = 1.4F * (this.tOilOut - this.tOilCritMin) / f_50_;
                    if (f_49_ > 1.4F) f_49_ = 1.4F;
                }
                float f_51_ = 0.0F;
                if (this.tWaterOut > this.tWaterCritMin) {
                    float f_52_ = this.tWaterCritMax - this.tWaterCritMin;
                    f_51_ = 1.4F * (this.tWaterOut - this.tWaterCritMin) / f_52_;
                    if (f_51_ > 1.4F) f_51_ = 1.4F;
                }
                float f_53_ = Math.max(f_49_, f_51_);
                float f_54_ = 1.0F;
                float f_55_ = this.reference.getSpeed();
                if (f_55_ > this.reference.Vmin * 1.5F) {
                    float f_56_ = this.reference.Vmax - this.reference.Vmin * 1.5F;
                    f_54_ = 1.0F - 1.65F * (f_55_ - this.reference.Vmin * 1.5F) / f_56_;
                    if (f_54_ < -1.0F) f_54_ = -1.0F;
                }
                this.controlRadiator = 0.5F * (f_53_ + f_54_);
                if (this.tWaterOut > this.tWaterCritMax || this.tOilOut > this.tOilCritMax) this.controlRadiator += 0.05F * this.timeCounter;
                if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
                if (this.controlRadiator > 1.0F) this.controlRadiator = 1.0F;
            } else if (this.reference.actor instanceof GLADIATOR) this.controlRadiator = 0.0F;
            else switch (this.propAngleDeviceType) {
            default:
            this.controlRadiator = 1.0F - this.getPowerOutput();
            break;
            case 5:
            case 6:
            this.controlRadiator = 1.0F - this.reference.getSpeed() / this.reference.VmaxH;
            if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
            break;
            case 1:
            case 2:
            if (this.controlRadiator > 1.0F - this.getPowerOutput()) {
                this.controlRadiator -= 0.15F * f;
                if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
            } else this.controlRadiator += 0.15F * f;
            break;
            case 8:
            if (this.type == 0) {
                if (this.tOilOut > this.tOilOutMaxRPM) {
                    this.controlRadiator += 0.1F * f;
                    if (this.controlRadiator > 1.0F) this.controlRadiator = 1.0F;
                } else if (this.tOilOut < this.tOilOutMaxRPM - 10.0F) {
                    this.controlRadiator -= 0.1F * f;
                    if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
                }
            } else if (this.controlRadiator > 1.0F - this.getPowerOutput()) {
                this.controlRadiator -= 0.15F * f;
                if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
            } else this.controlRadiator += 0.15F * f;
            break;
            case 7:
            if (this.tOilOut > this.tOilOutMaxRPM) {
                this.controlRadiator += 0.1F * f;
                if (this.controlRadiator > 1.0F) this.controlRadiator = 1.0F;
            } else {
                this.controlRadiator = 1.0F - this.reference.getSpeed() / this.reference.VmaxH;
                if (this.controlRadiator < 0.0F) this.controlRadiator = 0.0F;
            }
            }
    }

    private void computeForces(float f) {
        switch (this.type) {
            case 0:
            case 1:
            case 7: {
                if (Math.abs(this.w) < 1.0E-5F) this.propPhiW = 1.5707964F;
                else if (this.type == 7) this.propPhiW = (float) Math.atan(Math.abs(this.reference.Vflow.x) / (this.w * this.propReductor * this.propr));
                else this.propPhiW = (float) Math.atan(this.reference.Vflow.x / (this.w * this.propReductor * this.propr));
                this.propAoA = this.propPhi - this.propPhiW;
                if (this.type == 7) this.computePropForces(this.w * this.propReductor, (float) Math.abs(this.reference.Vflow.x), this.propPhi, this.propAoA, this.reference.getAltitude());
                else this.computePropForces(this.w * this.propReductor, (float) this.reference.Vflow.x, this.propPhi, this.propAoA, this.reference.getAltitude());
                switch (this.propAngleDeviceType) {
                    case 0:
                        break;
                    case 3:
                    case 4: {
                        float f_57_ = this.controlThrottle;
                        if (f_57_ > 1.0F) f_57_ = 1.0F;
                        this.compressorManifoldThreshold = 0.5F + (this.compressorRPMtoWMaxATA - 0.5F) * f_57_;
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.bControlPropAuto) this.propTarget = this.propPhiW + this.propAoA0;
                            else this.propTarget = this.propPhiMax - this.controlProp * (this.propPhiMax - this.propPhiMin);
                        } else if (this.propAngleDeviceType == 3) this.propTarget = 0.0F;
                        else this.propTarget = 3.1415927F;
                        break;
                    }
                    case 9:
                        if (this.bControlPropAuto) {
                            float f_58_ = this.propAngleDeviceMaxParam;
                            if (this.controlAfterburner) f_58_ = this.propAngleDeviceAfterburnerParam;
                            this.controlProp += this.controlPropDirection * f / 5.0F;
                            if (this.controlProp > 1.0F) this.controlProp = 1.0F;
                            else if (this.controlProp < 0.0F) this.controlProp = 0.0F;
                            float f_59_ = this.propAngleDeviceMinParam + (f_58_ - this.propAngleDeviceMinParam) * this.controlProp;
                            float f_60_ = this.controlThrottle;
                            if (f_60_ > 1.0F) f_60_ = 1.0F;
                            this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam) * f_60_));
                            if (this.isPropAngleDeviceOperational()) {
                                if (this.w < f_59_) {
                                    f_59_ = Math.min(1.0F, 0.01F * (f_59_ - this.w) - 0.012F * this.aw);
                                    this.propTarget -= f_59_ * this.getPropAngleDeviceSpeed() * f;
                                } else {
                                    f_59_ = Math.min(1.0F, 0.01F * (this.w - f_59_) + 0.012F * this.aw);
                                    this.propTarget += f_59_ * this.getPropAngleDeviceSpeed() * f;
                                }
                                if (this.stage == 6 && this.propTarget < this.propPhiW - 0.12F) {
                                    this.propTarget = this.propPhiW - 0.12F;
                                    if (this.propPhi < this.propTarget) this.propPhi += 0.2F * f;
                                }
                            } else this.propTarget = this.propPhi;
                        } else {
                            this.compressorManifoldThreshold = 0.5F + (this.compressorRPMtoWMaxATA - 0.5F) * (this.controlThrottle > 1.0F ? 1.0F : this.controlThrottle);
                            this.propTarget = this.propPhi;
                            if (this.isPropAngleDeviceOperational()) if (this.controlPropDirection > 0) this.propTarget = this.propPhiMin;
                            else if (this.controlPropDirection < 0) this.propTarget = this.propPhiMax;
                        }
                        break;
                    case 1:
                    case 2: {
                        if (this.bControlPropAuto) if (this.engineBoostFactor > 1.0F) this.controlProp = 0.75F + 0.227272F * this.controlThrottle;
                        else this.controlProp = 0.75F + 0.25F * this.controlThrottle;
                        float f_61_ = this.propAngleDeviceMaxParam;
                        if (this.controlAfterburner && (!this.bWepRpmInLowGear || this.controlCompressor != this.compressorMaxStep)) f_61_ = this.propAngleDeviceAfterburnerParam;
                        float f_62_ = this.propAngleDeviceMinParam + (f_61_ - this.propAngleDeviceMinParam) * this.controlProp;
                        float f_63_ = this.controlThrottle;
                        if (f_63_ > 1.0F) f_63_ = 1.0F;
                        this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam) * f_63_));
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.w < f_62_) {
                                f_62_ = Math.min(1.0F, 0.01F * (f_62_ - this.w) - 0.012F * this.aw);
                                this.propTarget -= f_62_ * this.getPropAngleDeviceSpeed() * f;
                            } else {
                                f_62_ = Math.min(1.0F, 0.01F * (this.w - f_62_) + 0.012F * this.aw);
                                this.propTarget += f_62_ * this.getPropAngleDeviceSpeed() * f;
                            }
                            if (this.stage == 6 && this.propTarget < this.propPhiW - 0.12F) {
                                this.propTarget = this.propPhiW - 0.12F;
                                if (this.propPhi < this.propTarget) this.propPhi += 0.2F * f;
                            }
                        } else if (this.propAngleDeviceType == 1) this.propTarget = 0.0F;
                        else this.propTarget = 1.5708F;
                        break;
                    }
                    case 7: {
                        float f_64_ = this.controlThrottle;
                        if (this.engineBoostFactor > 1.0F) f_64_ = 0.90909094F * this.controlThrottle;
                        float f_65_ = this.propAngleDeviceMaxParam;
                        if (this.controlAfterburner) if (this.afterburnerType == 1) {
                            if (this.controlThrottle > 1.0F) f_65_ = this.propAngleDeviceMaxParam + 10.0F * (this.controlThrottle - 1.0F) * (this.propAngleDeviceAfterburnerParam - this.propAngleDeviceMaxParam);
                        } else f_65_ = this.propAngleDeviceAfterburnerParam;
                        float f_66_ = this.propAngleDeviceMinParam + (f_65_ - this.propAngleDeviceMinParam) * f_64_;
                        float f_67_ = this.controlThrottle;
                        if (f_67_ > 1.0F) f_67_ = 1.0F;
                        this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + (f_65_ - this.propAngleDeviceMinParam) * f_67_));
                        if (this.isPropAngleDeviceOperational()) if (this.bControlPropAuto) {
                            if (this.w < f_66_) {
                                f_66_ = Math.min(1.0F, 0.01F * (f_66_ - this.w) - 0.012F * this.aw);
                                this.propTarget -= f_66_ * this.getPropAngleDeviceSpeed() * f;
                            } else {
                                f_66_ = Math.min(1.0F, 0.01F * (this.w - f_66_) + 0.012F * this.aw);
                                this.propTarget += f_66_ * this.getPropAngleDeviceSpeed() * f;
                            }
                            if (this.stage == 6 && this.propTarget < this.propPhiW - 0.12F) {
                                this.propTarget = this.propPhiW - 0.12F;
                                if (this.propPhi < this.propTarget) this.propPhi += 0.2F * f;
                            }
                            if (this.propTarget < this.propPhiMin + (float) Math.toRadians(3.0)) this.propTarget = this.propPhiMin + (float) Math.toRadians(3.0);
                        } else {
                            this.propTarget = (1.0F - f * 0.1F) * this.propTarget + f * 0.1F * (this.propPhiMax - this.controlProp * (this.propPhiMax - this.propPhiMin));
                            if (this.w > 1.02F * this.wMax) this.wMaxAllowed = (1.0F - 4.0E-7F * (this.w - 1.02F * this.wMax)) * this.wMaxAllowed;
                            if (this.w > this.wMax) {
                                float f_68_ = this.w - this.wMax;
                                f_68_ *= f_68_;
                                float f_69_ = 1.0F - 0.0010F * f_68_;
                                if (f_69_ < 0.0F) f_69_ = 0.0F;
                                this.propForce *= f_69_;
                            }
                        }
                        break;
                    }
                    case 8: {
                        float f_70_ = this.controlThrottle;
                        if (this.engineBoostFactor > 1.0F) f_70_ = 0.90909094F * this.controlThrottle;
                        float f_71_ = this.propAngleDeviceMaxParam;
                        if (this.controlAfterburner) if (this.afterburnerType == 1) {
                            if (this.controlThrottle > 1.0F) f_71_ = this.propAngleDeviceMaxParam + 10.0F * (this.controlThrottle - 1.0F) * (this.propAngleDeviceAfterburnerParam - this.propAngleDeviceMaxParam);
                        } else f_71_ = this.propAngleDeviceAfterburnerParam;
                        float f_72_ = this.propAngleDeviceMinParam + (f_71_ - this.propAngleDeviceMinParam) * f_70_ + (this.bControlPropAuto ? 0.0F : -25.0F + 50.0F * this.controlProp);
                        float f_73_ = this.controlThrottle;
                        if (f_73_ > 1.0F) f_73_ = 1.0F;
                        this.compressorManifoldThreshold = this.getATA(this.toRPM(this.propAngleDeviceMinParam + (f_71_ - this.propAngleDeviceMinParam) * f_73_));
                        if (this.isPropAngleDeviceOperational()) {
                            if (this.w < f_72_) {
                                f_72_ = Math.min(1.0F, 0.01F * (f_72_ - this.w) - 0.012F * this.aw);
                                this.propTarget -= f_72_ * this.getPropAngleDeviceSpeed() * f;
                            } else {
                                f_72_ = Math.min(1.0F, 0.01F * (this.w - f_72_) + 0.012F * this.aw);
                                this.propTarget += f_72_ * this.getPropAngleDeviceSpeed() * f;
                            }
                            if (this.stage == 6 && this.propTarget < this.propPhiW - 0.12F) {
                                this.propTarget = this.propPhiW - 0.12F;
                                if (this.propPhi < this.propTarget) this.propPhi += 0.2F * f;
                            }
                            if (this.propTarget < this.propPhiMin + (float) Math.toRadians(3.0)) this.propTarget = this.propPhiMin + (float) Math.toRadians(3.0);
                        }
                        break;
                    }
                    case 6: {
                        float f_74_ = this.controlThrottle;
                        if (f_74_ > 1.0F) f_74_ = 1.0F;
                        this.compressorManifoldThreshold = 0.5F + (this.compressorRPMtoWMaxATA - 0.5F) * f_74_;
                        if (this.isPropAngleDeviceOperational()) if (this.bControlPropAuto) {
                            float f_75_ = 25.0F + (this.wMax - 25.0F) * (0.25F + 0.75F * this.controlThrottle);
                            if (this.w < f_75_) {
                                f_75_ = Math.min(1.0F, 0.01F * (f_75_ - this.w) - 0.012F * this.aw);
                                this.propTarget -= f_75_ * this.getPropAngleDeviceSpeed() * f;
                            } else {
                                f_75_ = Math.min(1.0F, 0.01F * (this.w - f_75_) + 0.012F * this.aw);
                                this.propTarget += f_75_ * this.getPropAngleDeviceSpeed() * f;
                            }
                            if (this.stage == 6 && this.propTarget < this.propPhiW - 0.12F) {
                                this.propTarget = this.propPhiW - 0.12F;
                                if (this.propPhi < this.propTarget) this.propPhi += 0.2F * f;
                            }
                            this.controlProp = (this.propAngleDeviceMaxParam - this.propTarget) / (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam);
                            if (this.controlProp < 0.0F) this.controlProp = 0.0F;
                            if (this.controlProp > 1.0F) this.controlProp = 1.0F;
                        } else this.propTarget = this.propAngleDeviceMaxParam - this.controlProp * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam);
                        break;
                    }
                    case 5: {
                        float f_76_ = this.controlThrottle;
                        if (f_76_ > 1.0F) f_76_ = 1.0F;
                        this.compressorManifoldThreshold = 0.5F + (this.compressorRPMtoWMaxATA - 0.5F) * f_76_;
                        if (this.bControlPropAuto) if (this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()) {
                            if (World.cur().diffCur.ComplexEManagement) this.controlProp = -this.controlThrottle;
                            else this.controlProp = -Aircraft.cvt(this.reference.getSpeed(), this.reference.Vmin, this.reference.Vmax, 0.0F, 1.0F);
                        } else this.controlProp = -Aircraft.cvt(this.reference.getSpeed(), this.reference.Vmin, this.reference.Vmax, 0.0F, 1.0F);
                        this.propTarget = this.propAngleDeviceMaxParam - this.controlProp * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam);
                        this.propPhi = this.propTarget;
                        break;
                    }
                }
                if (this.controlFeather == 1 && this.bHasFeatherControl && this.isPropAngleDeviceOperational()) this.propTarget = 1.55F;
                if (this.propPhi > this.propTarget) {
                    float f_77_ = Math.min(1.0F, 157.29578F * (this.propPhi - this.propTarget));
                    this.propPhi -= f_77_ * this.getPropAngleDeviceSpeed() * f;
                } else if (this.propPhi < this.propTarget) {
                    float f_78_ = Math.min(1.0F, 157.29578F * (this.propTarget - this.propPhi));
                    this.propPhi += f_78_ * this.getPropAngleDeviceSpeed() * f;
                }
                if (this.propTarget > this.propPhiMax) this.propTarget = this.propPhiMax;
                else if (this.propTarget < this.propPhiMin) this.propTarget = this.propPhiMin;
                if (this.propPhi > this.propPhiMax && this.controlFeather == 0) this.propPhi = this.propPhiMax;
                else if (this.propPhi < this.propPhiMin) this.propPhi = this.propPhiMin;
                this.engineMoment = this.getN();
                float f_79_ = this.getCompressorMultiplier(f);
                this.engineMoment *= f_79_;
                this.momForFuel = this.engineMoment;
                this.engineMoment *= this.getReadyness();
                this.engineMoment *= this.getMagnetoMultiplier();
                this.engineMoment *= this.getMixMultiplier();
                this.engineMoment *= this.getStageMultiplier();
                this.engineMoment *= this.getDistabilisationMultiplier();
                this.engineMoment += this.getFrictionMoment(f);
                float f_80_ = this.engineMoment - this.propMoment;
                this.aw = f_80_ / (this.propI + this.engineI);
                if (this.aw > 0.0F) this.aw *= this.engineAcceleration;
                this.oldW = this.w;
                this.w += this.aw * f;
                if (this.w < 0.0F) this.w = 0.0F;
                if (this.w > this.wMaxAllowed + this.wMaxAllowed) this.w = this.wMaxAllowed + this.wMaxAllowed;
                if (this.oldW == 0.0F) {
                    if (this.w < 10.0F * this.fricCoeffT) this.w = 0.0F;
                } else if (this.w < 2.0F * this.fricCoeffT) this.w = 0.0F;
                if (this.reference.isPlayers() && World.cur().diffCur.Torque_N_Gyro_Effects && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()) {
                    this.propIW.set(this.propI * this.w * this.propReductor, 0.0, 0.0);
                    if (this.propDirection == 1) this.propIW.x = -this.propIW.x;
                    this.engineTorque.set(0.0F, 0.0F, 0.0F);
                    float f_81_ = this.propI * this.aw * this.propReductor;
                    if (this.propDirection == 0) {
                        this.engineTorque.x += this.propMoment;
                        this.engineTorque.x += f_81_;
                    } else {
                        this.engineTorque.x -= this.propMoment;
                        this.engineTorque.x -= f_81_;
                    }
                } else this.engineTorque.set(0.0F, 0.0F, 0.0F);
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                tmpV3f.cross(this.propPos, this.engineForce);
                this.engineTorque.add(tmpV3f);
                this.rearRush = 0.0F;
                this.rpm = this.toRPM(this.w);
                double d = this.reference.Vflow.x + this.addVflow;
                if (d < 1.0) d = 1.0;
                double d_82_ = 1.0 / (Atmosphere.density(this.reference.getAltitude()) * 6.0F * d);
                this.addVflow = 0.95 * this.addVflow + 0.05 * this.propForce * d_82_;
                this.addVside = 0.95 * this.addVside + 0.05 * (this.propMoment / this.propr) * d_82_;
                if (this.addVside < 0.0) this.addVside = 0.0;
                break;
            }
            case 2: {
                this.engineMoment = this.propAngleDeviceMinParam + this.getControlThrottle() * (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam);
                this.engineMoment /= this.propAngleDeviceMaxParam;
                this.engineMoment *= this.engineMomentMax;
                this.engineMoment *= this.getReadyness();
                this.engineMoment *= this.getDistabilisationMultiplier();
                this.engineMoment *= this.getStageMultiplier();
                this.engineMoment += this.getJetFrictionMoment(f);
                this.computePropForces(this.w, 0.0F, 0.0F, this.propAoA0, 0.0F);
                float f_84_ = this.w * this._1_wMax;
                float f_85_ = f_84_ * this.pressureExtBar;
                float f_86_ = f_84_ * f_84_;
                float f_87_ = 1.0F - 0.0060F * (Atmosphere.temperature((float) this.reference.Loc.z) - 290.0F);
                float f_88_ = 1.0F - 0.0011F * this.reference.getSpeed();
                this.propForce = this.thrustMax * f_85_ * f_86_ * f_87_ * f_88_ * this.getStageMultiplier();
                float f_89_ = this.engineMoment - this.propMoment;
                this.aw = f_89_ / (this.propI + this.engineI) * 1.0F;
                if (this.aw > 0.0F) this.aw *= this.engineAcceleration;
                this.w += this.aw * f;
                if (this.w < -this.wMaxAllowed) this.w = -this.wMaxAllowed;
                if (this.w > this.wMaxAllowed + this.wMaxAllowed) this.w = this.wMaxAllowed + this.wMaxAllowed;
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                break;
            }
            case 3:
            case 4: {
                this.w = this.wMin + (this.wMax - this.wMin) * this.controlThrottle;
                if (this.w < this.wMin || this.w < 0.0F || this.reference.M.fuel == 0.0F || this.stage != 6) this.w = 0.0F;
                this.propForce = this.w / this.wMax * this.thrustMax;
                this.propForce *= this.getStageMultiplier();
                float f_90_ = (float) this.reference.Vwld.length();
                if (f_90_ > 208.333F) if (f_90_ > 291.666F) this.propForce = 0.0F;
                else this.propForce *= (float) Math.sqrt((291.666F - f_90_) / 83.33299F);
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                break;
            }
            case 6: {
                this.w = this.wMin + (this.wMax - this.wMin) * this.controlThrottle;
                if (this.w < this.wMin || this.w < 0.0F || this.stage != 6) this.w = 0.0F;
                float f_91_ = this.reference.getSpeed() / 94.0F;
                if (f_91_ < 1.0F) this.w = 0.0F;
                else f_91_ = (float) Math.sqrt(f_91_);
                this.propForce = this.w / this.wMax * this.thrustMax * f_91_;
                this.propForce *= this.getStageMultiplier();
                float f_92_ = (float) this.reference.Vwld.length();
                if (f_92_ > 208.333F) if (f_92_ > 291.666F) this.propForce = 0.0F;
                else this.propForce *= (float) Math.sqrt((291.666F - f_92_) / 83.33299F);
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                this.rpm = this.toRPM(this.w);
                if (this.reference instanceof RealFlightModel) {
                    RealFlightModel realflightmodel = (RealFlightModel) this.reference;
                    f_92_ = Aircraft.cvt(this.propForce, 0.0F, this.thrustMax, 0.0F, 0.21F);
                    if (realflightmodel.producedShakeLevel < f_92_) realflightmodel.producedShakeLevel = f_92_;
                }
                break;
            }
            case 5:
                this.engineForce.set(this.engineVector);
                this.engineForce.scale(this.propForce);
                this.engineTorque.cross(this.enginePos, this.engineForce);
                break;
        }
    }

    private void computePropForces(float f, float f_93_, float f_94_, float f_95_, float f_96_) {
        float f_97_ = f * this.propr;
        float f_98_ = f_93_ * f_93_ + f_97_ * f_97_;
        float f_99_ = (float) Math.sqrt(f_98_);
        float f_100_ = 0.5F * this.getFanCy((float) Math.toDegrees(f_95_)) * Atmosphere.density(f_96_) * f_98_ * this.propSEquivalent;
        float f_101_ = 0.5F * this.getFanCx((float) Math.toDegrees(f_95_)) * Atmosphere.density(f_96_) * f_98_ * this.propSEquivalent;
        if (f_99_ > 300.0F) {
            float f_102_ = 1.0F + 0.02F * (f_99_ - 300.0F);
            if (f_102_ > 2.0F) f_102_ = 2.0F;
            f_101_ *= f_102_;
        }
        if (f_99_ < 0.0010F) f_99_ = 0.0010F;
        float f_103_ = 1.0F / f_99_;
        float f_104_ = f_93_ * f_103_;
        float f_105_ = f_97_ * f_103_;
        float f_106_ = 1.0F;
        if (f_93_ < this.Vopt) {
            float f_107_ = this.Vopt - f_93_;
            f_106_ = 1.0F - 5.0E-5F * f_107_ * f_107_;
        }
        this.propForce = f_106_ * (f_100_ * f_105_ - f_101_ * f_104_);
        this.propMoment = (f_101_ * f_105_ + f_100_ * f_104_) * this.propr;
    }

    public void toggle() {
        if (this.stage == 0) this.setEngineStarts(this.reference.actor);
        else if (this.stage < 7) {
            this.setEngineStops(this.reference.actor);
            if (this.reference.isPlayers()) HUD.log("EngineI0");
        }
    }

    public float getPowerOutput() {
        if (this.stage == 0 || this.stage > 6) return 0.0F;
        return this.controlThrottle * this.readyness;
    }

    public float getThrustOutput() {
        if (this.stage == 0 || this.stage > 6) return 0.0F;
        float f = this.w * this._1_wMax * this.readyness;
        if (f > 1.1F) f = 1.1F;
        return f;
    }

    public float getReadyness() {
        return this.readyness;
    }

    public float getPropPhi() {
        return this.propPhi;
    }

    private float getPropAngleDeviceSpeed() {
        if (this.isPropAngleDeviceHydroOperable) return this.propAngleChangeSpeed;
        return this.propAngleChangeSpeed * 10.0F;
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
        if (this.bControlPropAuto) f = this.controlProp;
        else f = (this.propPhiMax - this.propPhi) / (this.propPhiMax - this.propPhiMin);
        if (f < 0.1F) return 0.0F;
        if (f > 0.9F) return 1.0F;
        return f;
    }

    public boolean getControlPropAuto() {
        return this.bControlPropAuto;
    }

    public boolean isHasControlProp() {
        return this.bHasPropControl;
    }

    public boolean isAllowsAutoProp() {
        if (this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()) if (World.cur().diffCur.ComplexEManagement) switch (this.propAngleDeviceType) {
            case 0:
                return false;
            case 5:
                return true;
            case 6:
                return false;
            case 3:
            case 4:
                return false;
            case 1:
            case 2:
                if (this.reference.actor instanceof SPITFIRE9 || this.reference.actor instanceof SPITFIRE8 || this.reference.actor instanceof SPITFIRE8CLP) return true;
                return false;
            case 7:
            case 8:
                return true;
            default:
                break;
        }
        else return this.bHasPropControl;
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
            if (this.reference.actor instanceof P_51 || this.reference.actor instanceof P_38 || this.reference.actor instanceof YAK_3 || this.reference.actor instanceof YAK_3P || this.reference.actor instanceof YAK_9M
                    || this.reference.actor instanceof YAK_9U || this.reference.actor instanceof YAK_9UT || this.reference.actor instanceof SPITFIRE8 || this.reference.actor instanceof SPITFIRE8CLP || this.reference.actor instanceof SPITFIRE9
                    || this.reference.actor instanceof P_63C)
                return true;
            switch (this.propAngleDeviceType) {
                case 7:
                    return true;
                case 8:
                    if (this.type == 0) return true;
                    return false;
                default:
                    return false;
            }
        }
        return true;
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
        if (f > 34.0F) f = 34.0F;
        if (f < -8.0F) f = -8.0F;
        if (f < 16.0F) return -0.004688F * f * f + 0.15F * f + 0.4F;
        float f_108_ = 0.0F;
        if (f > 22.0F) {
            f_108_ = 0.01F * (f - 22.0F);
            f = 22.0F;
        }
        return 9.7222E-4F * f * f - 0.070833F * f + 2.4844F + f_108_;
    }

    private float getFanCx(float f) {
        if (f < -4.0F) f = -8.0F - f;
        if (f > 34.0F) f = 34.0F;
        if (f < 16.0) return 3.5E-4F * f * f + 0.0028F * f + 0.0256F;
        float f_109_ = 0.0F;
        if (f > 22.0F) {
            f_109_ = 0.04F * (f - 22.0F);
            f = 22.0F;
        }
        return -0.00555F * f * f + 0.24444F * f - 2.32888F + f_109_;
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

    public float forcePropAOA(float f, float f_110_, float f_111_, boolean bool) {
        switch (this.type) {
            default:
                return -1.0F;
            case 0:
            case 1:
            case 7: {
                float f_112_ = this.controlThrottle;
                boolean bool_113_ = this.controlAfterburner;
                int i = this.stage;
                safeLoc.set(this.reference.Loc);
                safeVwld.set(this.reference.Vwld);
                safeVflow.set(this.reference.Vflow);
                if (bool) this.w = this.wWEP;
                else this.w = this.wMax;
                this.controlThrottle = f_111_;
                if (this.engineBoostFactor <= 1.0 && this.controlThrottle > 1.0F) this.controlThrottle = 1.0F;
                if (this.afterburnerType > 0 && bool) this.controlAfterburner = true;
                this.stage = 6;
                this.fastATA = true;
                this.reference.Loc.set(0.0, 0.0, f_110_);
                this.reference.Vwld.set(f, 0.0, 0.0);
                this.reference.Vflow.set(f, 0.0, 0.0);
                this.pressureExtBar = Atmosphere.pressure(this.reference.getAltitude()) + this.compressorSpeedManifold * 0.5F * Atmosphere.density(this.reference.getAltitude()) * f * f;
                this.pressureExtBar /= Atmosphere.P0();
                float f_114_ = this.getCompressorMultiplier(0.033F);
                f_114_ *= this.getN();
                if (bool && this.bWepRpmInLowGear && this.controlCompressor == this.compressorMaxStep) {
                    this.w = this.wMax;
                    float f_115_ = this.getCompressorMultiplier(0.033F);
                    f_115_ *= this.getN();
                    f_114_ = f_115_;
                }
                float f_117_ = this.propPhiMin;
                float f_118_ = -1.0E8F;
                boolean bool_119_ = false;
                if ((Aircraft) this.reference.actor instanceof SM79 || (Aircraft) this.reference.actor instanceof TypeTwoPitchProp) bool_119_ = true;
                while (this.propAngleDeviceType == 0 || bool_119_) {
                    float f_120_ = 2.0F;
                    int i_121_ = 0;
                    float f_122_ = 0.1F;
                    float f_123_ = 0.5F;
                    for (;;) {
                        if (bool) this.w = this.wWEP * f_123_;
                        else this.w = this.wMax * f_123_;
                        float f_124_ = (float) Math.sqrt(f * f + this.w * this.propr * this.propReductor * this.w * this.propr * this.propReductor);
                        float f_125_ = f_117_ - (float) Math.asin(f / f_124_);
                        this.computePropForces(this.w * this.propReductor, f, 0.0F, f_125_, f_110_);
                        f_114_ = this.getN() * this.getCompressorMultiplier(0.033F);
                        if (i_121_ > 32 || !(f_122_ > 1.0E-5)) break;
                        if (this.propMoment < f_114_) {
                            if (f_120_ == 1.0F) f_122_ /= 2.0F;
                            f_123_ *= 1.0F + f_122_;
                            f_120_ = 0.0F;
                        } else {
                            if (f_120_ == 0.0F) f_122_ /= 2.0F;
                            f_123_ /= 1.0F + f_122_;
                            f_120_ = 1.0F;
                        }
                        i_121_++;
                    }
                    if (!bool_119_) break;
                    if (f_117_ == this.propPhiMin) {
                        f_118_ = this.propForce;
                        f_117_ = this.propPhiMax;
                    } else {
                        if (f_118_ > this.propForce) this.propForce = f_118_;
                        break;
                    }
                }
                this.controlThrottle = f_112_;
                this.controlAfterburner = bool_113_;
                this.stage = i;
                this.reference.Loc.set(safeLoc);
                this.reference.Vwld.set(safeVwld);
                this.reference.Vflow.set(safeVflow);
                this.fastATA = false;
                this.w = 0.0F;
                if (bool_119_ || this.propAngleDeviceType == 0) return this.propForce;
                float f_126_ = 1.5F;
                float f_127_ = -0.06F;
                float f_128_ = 0.5F * (f_126_ + f_127_);
                int i_129_ = 0;
                for (;;) {
                    f_128_ = 0.5F * (f_126_ + f_127_);
                    if (bool && (!this.bWepRpmInLowGear || this.controlCompressor != this.compressorMaxStep)) this.computePropForces(this.wWEP * this.propReductor, f, 0.0F, f_128_, f_110_);
                    else this.computePropForces(this.wMax * this.propReductor, f, 0.0F, f_128_, f_110_);
                    if (this.propForce > 0.0F && Math.abs(this.propMoment - f_114_) < 1.0E-5F || i_129_ > 32) break;
                    if (this.propForce > 0.0F && this.propMoment > f_114_) f_126_ = f_128_;
                    else f_127_ = f_128_;
                    i_129_++;
                }
                return this.propForce;
            }
            case 2: {
                this.pressureExtBar = Atmosphere.pressure(f_110_) + this.compressorSpeedManifold * 0.5F * Atmosphere.density(f_110_) * f * f;
                this.pressureExtBar /= Atmosphere.P0();
                float f_130_ = this.pressureExtBar;
                float f_131_ = 1.0F - 0.0060F * (Atmosphere.temperature(f_110_) - 290.0F);
                float f_132_ = 1.0F - 0.0011F * f;
                this.propForce = this.thrustMax * f_130_ * f_131_ * f_132_;
                return this.propForce;
            }
            case 3:
            case 4:
            case 6:
                this.propForce = this.thrustMax;
                if (f > 208.333F) if (f > 291.666F) this.propForce = 0.0F;
                else this.propForce *= (float) Math.sqrt((291.666F - f) / 83.33299F);
                return this.propForce;
            case 5:
                return this.thrustMax;
            case 8:
                return -1.0F;
        }
    }

    public float getEngineLoad() {
        float f = 0.1F + this.getControlThrottle() * 0.81818175F;
        float f_133_ = this.getw() / this.wMax;
        return f_133_ / f;
    }

    private void overrevving() {
        if (this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode() && World.cur().diffCur.ComplexEManagement && World.cur().diffCur.Engine_Overheat && this.w > this.wMaxAllowed && this.bIsMaster) {
            this.wMaxAllowed = 0.999965F * this.wMaxAllowed;
            this._1_wMaxAllowed = 1.0F / this.wMaxAllowed;
            tmpF *= 1.0F - (this.wMaxAllowed - this.w) * 0.01F;
            this.engineDamageAccum += 0.01F + 0.05F * (this.w - this.wMaxAllowed) * this._1_wMaxAllowed;
            if (this.engineDamageAccum > 1.0F) {
                if (heatStringID == -1) heatStringID = HUD.makeIdLog();
                if (this.reference.isPlayers()) HUD.log(heatStringID, "EngineOverheat");
                this.setReadyness(this.getReadyness() - (this.engineDamageAccum - 1.0F) * 0.0050F);
            }
            if (this.getReadyness() < 0.2F) this.setEngineDies(this.reference.actor);
        }
    }

    public float getN() {
        if (this.stage == 6) {
            switch (this.engineCarburetorType) {
                case 0: {
                    float f = 0.05F + 0.95F * this.getControlThrottle();
                    float f_134_ = this.w / this.wMax;
                    tmpF = this.engineMomentMax * (-1.0F / f * f_134_ * f_134_ + 2.0F * f_134_);
                    if (this.getControlThrottle() > 1.0F) tmpF *= this.engineBoostFactor;
                    this.overrevving();
                    break;
                }
                case 3: {
                    float f = 0.1F + 0.9F * this.getControlThrottle();
                    float f_135_ = this.w / this.wNom;
                    tmpF = this.engineMomentMax * (-1.0F / f * f_135_ * f_135_ + 2.0F * f_135_);
                    if (this.getControlThrottle() > 1.0F) tmpF *= this.engineBoostFactor;
                    float f_136_ = this.getControlThrottle() - this.neg_G_Counter * 0.1F;
                    if (f_136_ <= 0.3F) f_136_ = 0.3F;
                    if (this.reference.getOverload() < 0.0F && this.neg_G_Counter >= 0.0F) {
                        this.neg_G_Counter += 0.03F;
                        this.producedDistabilisation += 10.0F + 5.0F * this.neg_G_Counter;
                        tmpF *= f_136_;
                        if (this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode() && this.bIsMaster && this.neg_G_Counter > World.Rnd().nextFloat(5.0F, 8.0F))
                            this.setEngineStops(this.reference.actor);
                    } else if (this.reference.getOverload() >= 0.0F && this.neg_G_Counter > 0.0F) {
                        this.neg_G_Counter -= 0.015F;
                        this.producedDistabilisation += 10.0F + 5.0F * this.neg_G_Counter;
                        tmpF *= f_136_;
                        this.bFloodCarb = true;
                    } else {
                        this.bFloodCarb = false;
                        this.neg_G_Counter = 0.0F;
                    }
                    this.overrevving();
                    break;
                }
                case 1:
                case 2: {
                    float f = 0.1F + 0.9F * this.getControlThrottle();
                    if (f > 1.0F) f = 1.0F;
                    float f_137_ = this.engineMomentMax * (-0.5F * f * f + 1.0F * f + 0.5F);
                    float f_138_;
                    if (this.controlAfterburner) f_138_ = this.w / (this.wWEP * f);
                    else f_138_ = this.w / (this.wNom * f);
                    tmpF = f_137_ * (2.0F * f_138_ - 1.0F * f_138_ * f_138_);
                    if (this.getControlThrottle() > 1.0F) tmpF *= 1.0F + (this.getControlThrottle() - 1.0F) * 10.0F * (this.engineBoostFactor - 1.0F);
                    this.overrevving();
                    break;
                }
                case 4: {
                    float f = 0.1F + 0.9F * this.getControlThrottle();
                    if (f > 1.0F) f = 1.0F;
                    float f_139_ = this.engineMomentMax * (-0.5F * f * f + 1.0F * f + 0.5F);
                    float f_140_;
                    if (this.controlAfterburner) {
                        f_140_ = this.w / (this.wWEP * f);
                        if (f >= 0.95F) this.bFullT = true;
                        else this.bFullT = false;
                    } else {
                        f_140_ = this.w / (this.wNom * f);
                        this.bFullT = false;
                        if (this.reference.actor instanceof SPITFIRE5B && f >= 0.95F) this.bFullT = true;
                    }
                    tmpF = f_139_ * (2.0F * f_140_ - 1.0F * f_140_ * f_140_);
                    if (this.getControlThrottle() > 1.0F) tmpF *= 1.0F + (this.getControlThrottle() - 1.0F) * 10.0F * (this.engineBoostFactor - 1.0F);
                    float f_141_ = this.getControlThrottle() - this.neg_G_Counter * 0.2F;
                    if (f_141_ <= 0.0F) f_141_ = 0.1F;
                    if (this.reference.getOverload() < 0.0F && this.neg_G_Counter >= 0.0F) {
                        this.neg_G_Counter += 0.03F;
                        if (this.bFullT && this.neg_G_Counter < 0.5F) {
                            this.producedDistabilisation += 15.0F + 5.0F * this.neg_G_Counter;
                            tmpF *= 0.52F - this.neg_G_Counter;
                        } else if (this.bFullT && this.neg_G_Counter >= 0.5F && this.neg_G_Counter <= 0.8F) {
                            this.neg_G_Counter = 0.51F;
                            this.bFloodCarb = false;
                        } else if (this.bFullT && this.neg_G_Counter > 0.8F) {
                            this.neg_G_Counter -= 0.045F;
                            this.producedDistabilisation += 10.0F + 5.0F * this.neg_G_Counter;
                            tmpF *= f_141_;
                            this.bFloodCarb = true;
                        } else {
                            this.producedDistabilisation += 10.0F + 5.0F * this.neg_G_Counter;
                            tmpF *= f_141_;
                            if (this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode() && this.bIsMaster && this.neg_G_Counter > World.Rnd().nextFloat(7.5F, 9.5F))
                                this.setEngineStops(this.reference.actor);
                        }
                    } else if (this.reference.getOverload() >= 0.0F && this.neg_G_Counter > 0.0F) {
                        this.neg_G_Counter -= 0.03F;
                        if (!this.bFullT) {
                            this.producedDistabilisation += 10.0F + 5.0F * this.neg_G_Counter;
                            tmpF *= f_141_;
                        }
                        this.bFloodCarb = true;
                    } else {
                        this.neg_G_Counter = 0.0F;
                        this.bFloodCarb = false;
                    }
                    this.overrevving();
                    break;
                }
            }
            if (this.controlAfterburner) if (this.afterburnerType == 1) {
                if (this.controlThrottle > 1.0F && this.reference.M.nitro > 0.0F) tmpF *= this.engineAfterburnerBoostFactor;
            } else if (this.afterburnerType == 8 || this.afterburnerType == 7) {
                if (this.controlCompressor < this.compressorMaxStep) tmpF *= this.engineAfterburnerBoostFactor;
            } else tmpF *= this.engineAfterburnerBoostFactor;
            if (this.engineDamageAccum > 0.0F) this.engineDamageAccum -= 0.01F;
            if (this.engineDamageAccum < 0.0F) this.engineDamageAccum = 0.0F;
            if (tmpF < 0.0F) tmpF = Math.max(tmpF, -0.8F * this.w * this._1_wMax * this.engineMomentMax);
            return tmpF;
        }
        tmpF = -1500.0F * this.w * this._1_wMax * this.engineMomentMax;
        if (this.stage == 8) this.w = 0.0F;
        return tmpF;
    }

    private float getDistabilisationMultiplier() {
        if (this.engineMoment < 0.0F) return 1.0F;
        float f = 1.0F + World.Rnd().nextFloat(-1.0F, 0.1F) * this.getDistabilisationAmplitude();
        if (f < 0.0F && this.w < 0.5F * (this.wMax + this.wMin)) return 0.0F;
        return f;
    }

    public float getDistabilisationAmplitude() {
        if (this.getCylindersOperable() > 2) {
            float f = 1.0F - this.getCylindersRatio();
            return this.engineDistAM * this.w * this.w + this.engineDistBM * this.w + this.engineDistCM + 9.25F * f * f + this.producedDistabilisation;
        }
        return 11.25F;
    }

    private float getCompressorMultiplier(float f) {
        float f_142_ = this.controlThrottle;
        if (f_142_ > 1.0F) f_142_ = 1.0F;
        float f_143_;
        switch (this.propAngleDeviceType) {
            case 1:
            case 2:
            case 7:
            case 8:
                f_143_ = this.getATA(this.toRPM(this.propAngleDeviceMinParam + (this.propAngleDeviceMaxParam - this.propAngleDeviceMinParam) * f_142_));
                break;
            default:
                f_143_ = this.compressorRPMtoWMaxATA * (0.55F + 0.45F * f_142_);
        }
        this.coolMult = 1.0F;
        this.compressorManifoldThreshold = f_143_;
        switch (this.compressorType) {
            case 0: {
                float f_144_ = Atmosphere.pressure(this.reference.getAltitude()) + 0.5F * Atmosphere.density(this.reference.getAltitude()) * this.reference.getSpeed() * this.reference.getSpeed();
                float f_145_ = f_144_ / Atmosphere.P0();
                this.coolMult = f_145_;
                return f_145_;
            }
            case 1: {
                float f_146_ = this.pressureExtBar;
                if ((!this.bHasCompressorControl || !this.reference.isPlayers() || !(this.reference instanceof RealFlightModel) || !((RealFlightModel) this.reference).isRealMode() || !World.cur().diffCur.ComplexEManagement || this.fastATA)
                        && (this.reference.isTick(128, 0) || this.fastATA)) {
                    this.compressorStepFound = false;
                    this.controlCompressor = 0;
                }
                float f_147_ = -1.0F;
                float f_148_ = -1.0F;
                int i = -1;
                float f_149_;
                if (this.fastATA) {
                    for (this.controlCompressor = 0; this.controlCompressor <= this.compressorMaxStep; this.controlCompressor++) {
                        this.compressorManifoldThreshold = f_143_;
                        float f_150_ = this.compressorPressure[this.controlCompressor];
                        float f_151_ = this.compressorRPMtoWMaxATA / f_150_;
                        float f_152_ = 1.0F;
                        float f_153_ = 1.0F;
                        if (f_146_ > f_150_) {
                            float f_154_ = 1.0F - f_150_;
                            if (f_154_ < 1.0E-4F) f_154_ = 1.0E-4F;
                            float f_155_ = 1.0F - f_146_;
                            if (f_155_ < 0.0F) f_155_ = 0.0F;
                            float f_156_ = 1.0F;
                            for (int i_157_ = 1; i_157_ <= this.controlCompressor; i_157_++)
                                if (this.compressorAltMultipliers[this.controlCompressor] >= 1.0F) f_156_ *= 0.8F;
                                else f_156_ *= 0.8F * this.compressorAltMultipliers[this.controlCompressor];
                            f_152_ = f_156_ + f_155_ / f_154_ * (this.compressorAltMultipliers[this.controlCompressor] - f_156_);
                        } else f_152_ = this.compressorAltMultipliers[this.controlCompressor];
                        this.compressorManifoldPressure = (this.compressorPAt0 + (1.0F - this.compressorPAt0) * this.w * this._1_wMax) * f_146_ * f_151_;
                        float f_158_ = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                        if (this.controlAfterburner && (this.afterburnerType != 8 && this.afterburnerType != 7 || this.controlCompressor != this.compressorMaxStep)
                                && (this.afterburnerType != 1 || !(this.controlThrottle > 1.0F) || !(this.reference.M.nitro <= 0.0F))) {
                            f_158_ *= this.afterburnerCompressorFactor;
                            this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                        }
                        this.compressor2ndThrottle = f_158_;
                        if (this.compressor2ndThrottle > 1.0F) this.compressor2ndThrottle = 1.0F;
                        this.compressorManifoldPressure *= this.compressor2ndThrottle;
                        this.compressor1stThrottle = f_143_ / this.compressorRPMtoWMaxATA;
                        if (this.compressor1stThrottle > 1.0F) this.compressor1stThrottle = 1.0F;
                        this.compressorManifoldPressure *= this.compressor1stThrottle;
                        f_153_ = f_152_ * this.compressorManifoldPressure / this.compressorManifoldThreshold;
                        if (this.controlAfterburner && (this.afterburnerType == 8 || this.afterburnerType == 7) && this.controlCompressor == this.compressorMaxStep) {
                            if (f_153_ / this.engineAfterburnerBoostFactor > f_147_) {
                                f_147_ = f_153_;
                                i = this.controlCompressor;
                            }
                        } else if (f_153_ > f_147_) {
                            f_147_ = f_153_;
                            i = this.controlCompressor;
                        }
                    }
                    f_149_ = f_147_;
                    this.controlCompressor = i;
                } else {
                    float f_159_ = f_143_;
                    if (this.controlAfterburner) f_159_ *= this.afterburnerCompressorFactor;
                    do {
                        if (this.controlCompressor < 0) this.controlCompressor = 0;
                        if (this.controlCompressor >= this.compressorPressure.length) this.controlCompressor = this.compressorPressure.length - 1;
                        float f_160_ = this.compressorPressure[this.controlCompressor];
                        float f_161_ = this.compressorRPMtoWMaxATA / f_160_;
                        float f_162_ = 1.0F;
                        float f_163_ = 1.0F;
                        if (f_146_ > f_160_) {
                            float f_164_ = 1.0F - f_160_;
                            if (f_164_ < 1.0E-4F) f_164_ = 1.0E-4F;
                            float f_165_ = 1.0F - f_146_;
                            if (f_165_ < 0.0F) f_165_ = 0.0F;
                            float f_166_ = 1.0F;
                            for (int i_167_ = 1; i_167_ <= this.controlCompressor; i_167_++)
                                if (this.compressorAltMultipliers[this.controlCompressor] >= 1.0F) f_166_ *= 0.8F;
                                else f_166_ *= 0.8F * this.compressorAltMultipliers[this.controlCompressor];
                            f_162_ = f_166_ + f_165_ / f_164_ * (this.compressorAltMultipliers[this.controlCompressor] - f_166_);
                            f_163_ = f_162_;
                        } else {
                            f_162_ = this.compressorAltMultipliers[this.controlCompressor];
                            f_163_ = f_162_ * f_146_ * f_161_ / f_159_;
                        }
                        if (f_163_ > f_147_) {
                            f_147_ = f_163_;
                            f_148_ = f_162_;
                            i = this.controlCompressor;
                        }
                        if (!this.compressorStepFound) {
                            this.controlCompressor++;
                            if (this.controlCompressor == this.compressorMaxStep + 1) this.compressorStepFound = true;
                        }
                    } while (!this.compressorStepFound);
                    if (i < 0) i = 0;
                    this.controlCompressor = i;
                    float f_168_ = this.compressorPressure[this.controlCompressor];
                    float f_169_ = this.compressorRPMtoWMaxATA / f_168_;
                    this.compressorManifoldPressure = (this.compressorPAt0 + (1.0F - this.compressorPAt0) * this.w * this._1_wMax) * f_146_ * f_169_;
                    float f_170_ = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                    if (this.controlAfterburner && (this.afterburnerType != 8 && this.afterburnerType != 7 || this.controlCompressor != this.compressorMaxStep)
                            && (this.afterburnerType != 1 || !(this.controlThrottle > 1.0F) || !(this.reference.M.nitro <= 0.0F))) {
                        f_170_ *= this.afterburnerCompressorFactor;
                        this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                    }
                    if (this.fastATA) this.compressor2ndThrottle = f_170_; // TODO: Reactivated by SAS~Storebror
                    else this.compressor2ndThrottle -= 3.0F * f * (this.compressor2ndThrottle - f_170_);
                    if (this.compressor2ndThrottle > 1.0F) this.compressor2ndThrottle = 1.0F;
                    this.compressorManifoldPressure *= this.compressor2ndThrottle;
                    this.compressor1stThrottle = f_143_ / this.compressorRPMtoWMaxATA;
                    if (this.compressor1stThrottle > 1.0F) this.compressor1stThrottle = 1.0F;
                    this.compressorManifoldPressure *= this.compressor1stThrottle;
                    f_149_ = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                    this.coolMult = f_149_;
                    f_149_ *= f_148_;
                }
                if (this.w <= 20.0F && this.w < 150.0F) this.compressorManifoldPressure = Math.min(this.compressorManifoldPressure, f_146_ * (0.4F + (this.w - 20.0F) * 0.04F));
                if (this.w < 20.0F) this.compressorManifoldPressure = f_146_ * (1.0F - this.w * 0.03F);
                if (this.mixerType == 1 && this.stage == 6) this.compressorManifoldPressure *= this.getMixMultiplier();
                return f_149_;
            }
            case 2: {
                float f_171_ = this.pressureExtBar;
                float f_172_ = this.compressorPressure[this.controlCompressor];
                float f_173_ = this.compressorRPMtoWMaxATA / f_172_;
                this.compressorManifoldPressure = (this.compressorPAt0 + (1.0F - this.compressorPAt0) * this.w * this._1_wMax) * f_171_ * f_173_;
                float f_174_ = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                if (this.controlAfterburner && (this.afterburnerType != 1 || !(this.controlThrottle > 1.0F) || !(this.reference.M.nitro <= 0.0F))) {
                    f_174_ *= this.afterburnerCompressorFactor;
                    this.compressorManifoldThreshold *= this.afterburnerCompressorFactor;
                }
                if (this.fastATA) this.compressor2ndThrottle = f_174_;
                else this.compressor2ndThrottle -= 0.1F * (this.compressor2ndThrottle - f_174_);
                if (this.compressor2ndThrottle > 1.0F) this.compressor2ndThrottle = 1.0F;
                this.compressorManifoldPressure *= this.compressor2ndThrottle;
                this.compressor1stThrottle = f_143_ / this.compressorRPMtoWMaxATA;
                if (this.compressor1stThrottle > 1.0F) this.compressor1stThrottle = 1.0F;
                this.compressorManifoldPressure *= this.compressor1stThrottle;
                float f_175_;
                if (this.controlAfterburner && this.afterburnerType == 2 && this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode() && this.reference.M.nitro > 0.0F) {
                    float f_176_ = this.compressorManifoldPressure + 0.2F;
//                    if (this.reference.actor == World.getPlayerAircraft()) HUD.training("" + this.compressorManifoldPressure + " / " + this.compressorRPMtoWMaxATA);
//                    if (f_176_ > this.compressorRPMtoWMaxATA + 0.199F && !this.fastATA && World.Rnd().nextFloat() < 0.0010F) {
                    if (f_176_ > this.compressorRPMtoWMaxATA + 0.249F && !this.fastATA && World.Rnd().nextFloat() < 0.0010F) {
                        this.readyness = 0.0F;
                        this.setEngineDies(this.reference.actor);
//                        System.out.println("######## f_176_=" + f_176_ + ", this.compressorRPMtoWMaxATA + 0.199F=" + (this.compressorRPMtoWMaxATA + 0.199F));
                    }
                    if (f_176_ > this.compressorManifoldThreshold) f_176_ = this.compressorManifoldThreshold;
                    f_175_ = f_176_ / this.compressorManifoldThreshold;
                } else f_175_ = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                if (this.w <= 20.0F && this.w < 150.0F) this.compressorManifoldPressure = Math.min(this.compressorManifoldPressure, f_171_ * (0.4F + (this.w - 20.0F) * 0.04F));
                if (this.w < 20.0F) this.compressorManifoldPressure = f_171_ * (1.0F - this.w * 0.03F);
                if (f_171_ > f_172_) {
                    float f_177_ = 1.0F - f_172_;
                    if (f_177_ < 1.0E-4F) f_177_ = 1.0E-4F;
                    float f_178_ = 1.0F - f_171_;
                    if (f_178_ < 0.0F) f_178_ = 0.0F;
                    float f_179_ = 1.0F;
                    float f_180_ = f_179_ + f_178_ / f_177_ * (this.compressorAltMultipliers[this.controlCompressor] - f_179_);
                    f_175_ *= f_180_;
                } else f_175_ *= this.compressorAltMultipliers[this.controlCompressor];
                this.coolMult = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                return f_175_;
            }
            case 3: {
                float f_181_ = this.pressureExtBar;
                this.controlCompressor = 0;
                float f_182_ = -1.0F;
                float f_183_ = this.compressorPressure[this.controlCompressor];
                float f_184_ = this.compressorRPMtoWMaxATA / f_183_;
                float f_185_ = 1.0F;
                if (f_181_ > f_183_) {
                    float f_187_ = 1.0F - f_183_;
                    if (f_187_ < 1.0E-4F) f_187_ = 1.0E-4F;
                    float f_188_ = 1.0F - f_181_;
                    if (f_188_ < 0.0F) f_188_ = 0.0F;
                    float f_189_ = 1.0F;
                    f_185_ = f_189_ + f_188_ / f_187_ * (this.compressorAltMultipliers[this.controlCompressor] - f_189_);
                } else f_185_ = this.compressorAltMultipliers[this.controlCompressor];
                f_182_ = f_185_;
                f_184_ = this.compressorRPMtoWMaxATA / f_183_;
                if (f_181_ < f_183_) f_181_ = 0.1F * f_181_ + 0.9F * f_183_;
                float f_190_ = f_181_ * f_184_;
                this.compressorManifoldPressure = (this.compressorPAt0 + (1.0F - this.compressorPAt0) * this.w * this._1_wMax) * f_190_;
                float f_191_ = this.compressorRPMtoWMaxATA / this.compressorManifoldPressure;
                if (this.fastATA) this.compressor2ndThrottle = f_191_;
                else this.compressor2ndThrottle -= 3.0F * f * (this.compressor2ndThrottle - f_191_);
                if (this.compressor2ndThrottle > 1.0F) this.compressor2ndThrottle = 1.0F;
                this.compressorManifoldPressure *= this.compressor2ndThrottle;
                this.compressor1stThrottle = f_143_ / this.compressorRPMtoWMaxATA;
                if (this.compressor1stThrottle > 1.0F) this.compressor1stThrottle = 1.0F;
                this.compressorManifoldPressure *= this.compressor1stThrottle;
                float f_192_ = this.compressorManifoldPressure / this.compressorManifoldThreshold;
                f_192_ *= f_182_;
                if (this.w <= 20.0F && this.w < 150.0F) this.compressorManifoldPressure = Math.min(this.compressorManifoldPressure, f_181_ * (0.4F + (this.w - 20.0F) * 0.04F));
                if (this.w < 20.0F) this.compressorManifoldPressure = f_181_ * (1.0F - this.w * 0.03F);
                return f_192_;
            }
            default:
                return 1.0F;
        }
    }

    private float getMagnetoMultiplier() {
        switch (this.controlMagneto) {
            case 0:
                return 0.0F;
            case 1:
                return this.bMagnetos[0] ? 0.87F : 0.0F;
            case 2:
                return this.bMagnetos[1] ? 0.87F : 0.0F;
            case 3: {
                float f = 0.0F;
                f = f + (this.bMagnetos[0] ? 0.87F : 0.0F);
                f = f + (this.bMagnetos[1] ? 0.87F : 0.0F);
                if (f > 1.0F) f = 1.0F;
                return f;
            }
            default:
                return 1.0F;
        }
    }

    private float getMixMultiplier() {
        float f = 0.0F;
        switch (this.mixerType) {
            case 0:
                return 1.0F;
            case 1:
                if (this.controlMix == 1.0F) {
                    if (this.bFloodCarb) this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                    else this.reference.AS.setSootState(this.reference.actor, this.number, 0);
                    return 1.0F;
                }
                /* fall through */
            case 2: {
                if (this.reference.isPlayers() && this.reference instanceof RealFlightModel && ((RealFlightModel) this.reference).isRealMode()) {
                    if (!World.cur().diffCur.ComplexEManagement) return 1.0F;
                    float f_193_ = this.mixerLowPressureBar * this.controlMix;
                    if (f_193_ < this.pressureExtBar - f) {
                        if (f_193_ < 0.03F) this.setEngineStops(this.reference.actor);
                        if (this.bFloodCarb) this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                        else this.reference.AS.setSootState(this.reference.actor, this.number, 0);
                        if (f_193_ > (this.pressureExtBar - f) * 0.25F) return 1.0F;
                        float f_194_ = f_193_ / ((this.pressureExtBar - f) * 0.25F);
                        return f_194_;
                    }
                    if (f_193_ > this.pressureExtBar) {
                        this.producedDistabilisation += 0.0F + 35.0F * (1.0F - (this.pressureExtBar + f) / (f_193_ + 1.0E-4F));
                        this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                        float f_195_ = (this.pressureExtBar + f) / (f_193_ + 1.0E-4F);
                        return f_195_;
                    }
                    if (this.bFloodCarb) this.reference.AS.setSootState(this.reference.actor, this.number, 1);
                    else this.reference.AS.setSootState(this.reference.actor, this.number, 0);
                    return 1.0F;
                }
                float f_196_ = this.mixerLowPressureBar * this.controlMix;
                if (f_196_ < this.pressureExtBar - f && f_196_ < 0.03F) this.setEngineStops(this.reference.actor);
                return 1.0F;
            }
            default:
                return 1.0F;
        }
    }

    private float getStageMultiplier() {
        if (this.stage == 6) return 1.0F;
        return 0.0F;
    }

    public void setFricCoeffT(float f) {
        this.fricCoeffT = f;
    }

    private float getFrictionMoment(float f) {
        float f_197_ = 0.0F;
        if (this.bIsInoperable || this.stage == 0 || this.controlMagneto == 0) {
            this.fricCoeffT += 0.1F * f;
            if (this.fricCoeffT > 1.0F) this.fricCoeffT = 1.0F;
            float f_198_ = this.w * this._1_wMax;
            f_197_ = -this.fricCoeffT * (6.0F + 3.8F * f_198_) * (this.propI + this.engineI) / f;
            float f_199_ = -0.99F * this.w * (this.propI + this.engineI) / f;
            if (f_197_ < f_199_) f_197_ = f_199_;
        } else this.fricCoeffT = 0.0F;
        return f_197_;
    }

    private float getJetFrictionMoment(float f) {
        float f_200_ = 0.0F;
        if (this.bIsInoperable || this.stage == 0) f_200_ = -0.0020F * this.w * (this.propI + this.engineI) / f;
        return f_200_;
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
        return f * 3.1415927F * 2.0F / 60.0F;
    }

    private float toRPM(float f) {
        return f * 60.0F / 2.0F / 3.1415927F;
    }

    // TODO: Altered by |ZUTI|: from private to public.
    public float getKforH(float f, float f_201_, float f_202_) {
        float f_203_ = Atmosphere.density(f_202_) * (f_201_ * f_201_) / (Atmosphere.density(0.0F) * (f * f));
        if (this.type != 2) f_203_ = f_203_ * this.kV(f) / this.kV(f_201_);
        return f_203_;
    }

    private float kV(float f) {
        return 1.0F - 0.0032F * f;
    }

    public final void setAfterburnerType(int i) {
        this.afterburnerType = i;
    }

    public final void setPropReductorValue(float f) {
        this.propReductor = f;
    }

    public void replicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.controlMagneto | this.stage << 4);
        netmsgguaranted.writeByte(this.cylinders);
        netmsgguaranted.writeByte(this.cylindersOperable);
        netmsgguaranted.writeByte((int) (255.0F * this.readyness));
        netmsgguaranted.writeByte((int) (255.0F * ((this.propPhi - this.propPhiMin) / (this.propPhiMax - this.propPhiMin))));
        netmsgguaranted.writeFloat(this.w);
    }

    public void replicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.stage = (i & 0xf0) >> 4;
        this.controlMagneto = i & 0xf;
        this.cylinders = netmsginput.readUnsignedByte();
        this.cylindersOperable = netmsginput.readUnsignedByte();
        this.readyness = netmsginput.readUnsignedByte() / 255.0F;
        this.propPhi = netmsginput.readUnsignedByte() / 255.0F * (this.propPhiMax - this.propPhiMin) + this.propPhiMin;
        this.w = netmsginput.readFloat();
    }

    public static void printDebug(String theMessage) {
        if (!DEBUG) return;
        System.out.println(theMessage);
        Exception e = new Exception("Motor Debugger Stacktrace");
        System.out.println("Stacktrace follows:");
        e.printStackTrace();
    }

    // TODO: +++ TD AI code backport from 4.13 +++
    public void setManualControlAfterburner(float f) {
        this.controlFAfterburner = f;
    }

    private float controlFAfterburner = 0.0F;

    public float getControlManualAfterburner() {
        return this.controlFAfterburner;
    }

    public boolean isHasControlBoost() {
        return this.afterburnerType == 12;
    }
// TODO: --- TD AI code backport from 4.13 ---

}
