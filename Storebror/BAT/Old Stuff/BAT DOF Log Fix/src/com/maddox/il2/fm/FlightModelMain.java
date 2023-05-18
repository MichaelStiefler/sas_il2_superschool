/* Modified FlightModelMain class for the SAS Engine Mod */
/* 4.12.2 Oct 2015 version, changes By PAL */
/* By PAL, from current Western Engine MOD */
/* By PAL, implemented methods to load plain debug FM */
/* By western, merge new function and clean-up on 19th/Oct./2016 */
/* By Storebror, FM log spam filter on 19th/Jan./2017 */
package com.maddox.il2.fm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Scheme1;
import com.maddox.il2.objects.air.Scheme2a;
import com.maddox.il2.objects.air.Scheme5;
import com.maddox.il2.objects.air.Scheme7;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.BackgroundTask;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.InOutStreams;
import com.maddox.rts.KryptoInputFilter;
import com.maddox.rts.Property;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class FlightModelMain extends FMMath {
    // TODO: Default Parameters
    // --------------------------------------------------------
    public static final int       __DEBUG__IL2C_DUMP_LEVEL__ = 0;
    public static final int       ROOKIE                     = 0;
    public static final int       NORMAL                     = 1;
    public static final int       VETERAN                    = 2;
    public static final int       ACE                        = 3;
    private static final long     FMFLAGS_READYTORETURN      = 2L;
    private static final long     FMFLAGS_READYTODIE         = 4L;
    private static final long     FMFLAGS_TAKENMORTALDAMAGE  = 8L;
    private static final long     FMFLAGS_CAPABLEAIRWORTHY   = 16L;
    private static final long     FMFLAGS_CAPABLEACM         = 32L;
    private static final long     FMFLAGS_CAPABLETAXI        = 64L;
    private static final long     FMFLAGS_STATIONEDONGROUND  = 128L;
    private static final long     FMFLAGS_CRASHEDONGROUND    = 256L;
    private static final long     FMFLAGS_NEARAIRDROME       = 512L;
    private static final long     FMFLAGS_ISCROSSCOUNTRY     = 1024L;
    private static final long     FMFLAGS_WASAIRBORNE        = 2048L;
    private static final long     FMFLAGS_NETSENTWINGNOTE    = 4096L;
    private static final long     FMFLAGS_NETSENTBURYNOTE    = 16384L;
    private static final long     FMFLAGS_NETSENTCTRLSDMG    = 32768L;
// private static final long FMFLAGS_NETSENT4 = 0x10000L;
// private static final long FMFLAGS_NETSENT5 = 0x20000L;
// private static final long FMFLAGS_NETSENT6 = 0x40000L;
// private static final long FMFLAGS_NETSENT7 = 0x80000L;
    public static final int       FMSFX_NOOP                 = 0;
    public static final int       FMSFX_DROP_WINGFOLDED      = 1;
    public static final int       FMSFX_DROP_LEFTWING        = 2;
    public static final int       FMSFX_DROP_RIGHTWING       = 3;
    public static float           outEngineAOA               = 0.0F;
    private long                  flags0;
    private boolean               bDamagedGround;
    private boolean               bDamaged;
    private Actor                 damagedInitiator;
    public int                    Skill;
    public int                    crew;
    public int                    turretSkill;
    public Autopilotage           AP;
    public Controls               CT;
    public float                  SensYaw;
    public float                  SensPitch;
    public float                  SensRoll;
    public float                  GearCX;
    public float                  radiatorCX;
    public Point3d                Loc;
    public Orientation            Or;
    public FlightModel            Leader;
    public FlightModel            Wingman;
    public Vector3d               Offset;
    public byte                   formationType;
    public float                  formationScale;
    public float                  minElevCoeff;
    protected float               AOA;
    protected float               AOS;
    protected float               V;
    protected float               V2;
    protected float               q_;
    protected float               Gravity;
    protected float               Mach;
    public float                  Energy;
    public float                  BarometerZ;
    public float                  WingDiff;
    public float                  WingLoss;
    public float                  turbCoeff;
    public float                  FuelConsumption;
    public Vector3d               producedAM;
    public Vector3d               producedAMM;
    public Vector3d               producedAF;
    protected int                 fmsfxCurrentType;
    protected float               fmsfxPrevValue;
    protected long                fmsfxTimeDisable;
    protected Vector3d            AF;
    protected Vector3d            AM;
    protected Vector3d            GF;
    protected Vector3d            GM;
    protected Vector3d            SummF;
    protected Vector3d            SummM;
    // BY PAL, static?
    private /* static */ Vector3d TmpA                       = new Vector3d();
    private /* static */ Vector3d TmpV                       = new Vector3d();
    protected Vector3d            ACmeter;
    protected Vector3d            Accel;
    protected Vector3d            LocalAccel;
    protected Vector3d            BallAccel;
    public Vector3d               Vwld;
    public Vector3d               Vrel;
    protected Vector3d            Vair;
    protected Vector3d            Vflow;
    protected Vector3d            Vwind;
    protected Vector3d            J0;
    protected Vector3d            J;
    protected Vector3d            W;
    protected Vector3d            AW;
    public EnginesInterface       EI;
    public Mass                   M;
    public AircraftState          AS;
    public Squares                Sq;
    protected Arm                 Arms;
    public Gear                   Gears;
    public boolean                AIRBRAKE;
    protected Polares             Wing;
    protected Polares             Tail;
    public Polares                Fusel;
    public int                    Scheme;
    public float                  Wingspan;
    public float                  Length;
    public float                  Vmax;
    public float                  VmaxH;
    public float                  Vmin;
    public float                  HofVmax;
    public float                  VmaxFLAPS;
    public float                  VminFLAPS;
    public float                  VmaxAllowed;
    public float                  indSpeed;
    public float                  AOA_Crit;
    public float                  Range;
    public float                  CruiseSpeed;
    public int                    damagedParts[];
    public int                    maxDamage;
    public int                    cutPart;
    private boolean               bReal;
    public float                  UltimateLoad;
    public float                  Negative_G_Limit;
    public float                  Negative_G_Ultimate;
    public float                  LimitLoad;
    public float                  G_ClassCoeff;
    public float                  refM;
    public float                  SafetyFactor;
    public float                  ReferenceForce;
// private String aircrName;
// private String engineFamily;
// private String engineModel;
    String                        turnFile;
    String                        speedFile;
    String                        craftFile;
    // By PAL, static???
    private /* static */ Vector3d actVwld                    = new Vector3d();
    public long                   Operate;
    private /* static */ Vector3d GPulse                     = new Vector3d();
    public /* static */ boolean   bCY_CRIT04                 = true;
    private static InOutStreams   fmDir                      = null;                                                                                                                                                                                // By PAL, start it as null to initialize later.
    // --------------------------------------------------------

    // TODO: New Parameters
    // --------------------------------------------------------
    public Supersonic             Ss;
    private float                 CyBlownFlapsOn;
    private float                 CyBlownFlapsOff;
    private double                ThrustBlownFlaps;
    public float                  WingspanFolded;
    // --------------------------------------------------------

    // By PAL, required for FMDiff
    private static String         fmLastDir;
    private static ArrayList      fmDirs                     = new ArrayList();
    private static ArrayList      fmDirNames                 = new ArrayList();

    // By PAL, Special Options for FMs
// private static boolean bPrintFM = false; // merged to Storebror's logLevel; no more needed
    private static boolean        bDebugFM                   = false;
    private static String         sDumpPath                  = "DumpedFMs" + "/";
    private static File           f                          = new File(sDumpPath);
    private static boolean        bDumpFM                    = f.exists() && f.isDirectory();

    public float getSpeedKMH() {
        return (float) (this.Vflow.x * 3.6000000000000001D);
    }

    public float getSpeed() {
        return (float) this.Vflow.x;
    }

    public void getSpeed(Vector3d vector3d) {
        vector3d.set(this.Vair);
    }

    public float getVertSpeed() {
        return (float) this.Vair.z;
    }

    public float getAltitude() {
        return (float) this.Loc.z;
    }

    public float getAOA() {
        return this.AOA;
    }

    public float getAOS() {
        return this.AOS;
    }

    public void getLoc(Point3f point3f) {
        point3f.set(this.Loc);
    }

    public void getLoc(Point3d point3d) {
        point3d.set(this.Loc);
    }

    public void getOrient(Orientation orientation) {
        orientation.set(this.Or);
    }

    public Vector3d getW() {
        return this.W;
    }

    public Vector3d getAW() {
        return this.AW;
    }

    public Vector3d getAccel() {
        return this.Accel;
    }

    public float getSideAccel() {
        return (float) this.ACmeter.y;
    }

    public Vector3d getLocalAccel() {
        return this.LocalAccel;
    }

    public Vector3d getBallAccel() {
        this.TmpV.set(this.LocalAccel);
        if (this.Vwld.lengthSquared() < 10D) {
            double d = this.Vwld.lengthSquared() - 5D;
            if (d < 0.0D) {
                d = 0.0D;
            }
            this.TmpV.scale(0.20000000000000001D * d);
        }
        this.TmpA.set(0.0D, 0.0D, -Atmosphere.g());
        this.Or.transformInv(this.TmpA);
        this.TmpA.sub(this.TmpV);
        return this.TmpA;
    }

    public Vector3d getAM() {
        return this.AM;
    }

    public Vector3d getVflow() {
        return this.Vflow;
    }

    public void load(String s) {
        String s1 = "Error loading params from " + s;
        SectFile sectfile = sectFile(s);
        String s2 = "Aircraft";
        this.Wingspan = sectfile.get(s2, "Wingspan", 0.0F);
        if (this.Wingspan == 0.0F) {
            throw new RuntimeException(s1);
        }
        // TODO: New folded wingspan parameter
        this.WingspanFolded = sectfile.get(s2, "WingspanFolded", 0.0F);
        this.Length = sectfile.get(s2, "Length", 0.0F);
        if (this.Length == 0.0F) {
            throw new RuntimeException(s1);
        }
        this.Scheme = sectfile.get(s2, "Type", -1);
        if (this.Scheme == -1) {
            throw new RuntimeException(s1);
        }
        this.crew = sectfile.get(s2, "Crew", 0, 0, 9);
        if (this.crew == 0) {
            throw new RuntimeException(s1);
        }
        for (int k = 0; k < this.AS.astatePilotFunctions.length; k++) {
            int i = sectfile.get(s2, "CrewFunction" + k, -1);
            if (i != -1) {
                this.AS.astatePilotFunctions[k] = (byte) i;
            }
        }

        int j = sectfile.get("Controls", "CDiveBrake", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.AIRBRAKE = j == 1;
        s2 = "Controls";
        // 2nd bit (number 2) of CAileron, CElevator, CRudder means powered by hydro
        j = sectfile.get(s2, "CAileron", 0);
        if ((j != 0) && (j != 1) && (j != 3)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasAileronControl = (j & 1) == 1;
        this.CT.bHasAileronControlPowered = (j & 2) == 2;
        j = sectfile.get(s2, "CAileronTrim", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasAileronTrim = j == 1;
        j = sectfile.get(s2, "CElevator", 0);
        if ((j != 0) && (j != 1) && (j != 3)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasElevatorControl = (j & 1) == 1;
        this.CT.bHasElevatorControlPowered = (j & 2) == 2;
        j = sectfile.get(s2, "CElevatorTrim", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasElevatorTrim = j == 1;
        j = sectfile.get(s2, "CRudder", 0);
        if ((j != 0) && (j != 1) && (j != 3)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasRudderControl = (j & 1) == 1;
        this.CT.bHasRudderControlPowered = (j & 2) == 2;
        j = sectfile.get(s2, "CRudderTrim", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasRudderTrim = j == 1;
        j = sectfile.get(s2, "CLimitModeOfAilerons", 0);
        // TODO: By western
        // "CLimitModeOfAilerons" has 3 bits 1 / 2 / 4 and the value has to be composite of them .... from 0 to 7
        // bit 1 : limit by higher flying speed
        // bit 2 : limit by flaps retracted
        // bit 4 : limit by gears retracted
        // composite value means AND , in example 6 makes decision by both flaps and gears are retracted
        if ((j < 0) && (j > 7)) {
            throw new RuntimeException(s1);
        }
        this.CT.limitModeOfAilerons = j;
        if ((this.CT.limitModeOfAilerons & 1) == 1) {
            float spd = sectfile.get(s2, "CLimitThresholdSpeedKMH", -1F);
            if (spd < 0) {
                throw new RuntimeException(s1);
            }
            this.CT.limitThresholdSpeedKMH = spd;
        }
        // TODO: By western
        // each "CLimitRatioXXXX" has float 0.0 ~ 1.0 to limit those movings
        // modern jets often have the always limit to minus elevator. CLimitRatioElevatorMinusALWAYS defines it.
        float f = sectfile.get(s2, "CLimitRatioAileron", -1F);
        if ((f > 0.0F) && (f <= 1.0F)) {
            this.CT.limitRatioAileron = f;
        }
        f = sectfile.get(s2, "CLimitRatioElevatorPlus", -1F);
        if ((f > 0.0F) && (f <= 1.0F)) {
            this.CT.limitRatioElevatorPlus = f;
        }
        f = sectfile.get(s2, "CLimitRatioElevatorMinus", -1F);
        if ((f > 0.0F) && (f <= 1.0F)) {
            this.CT.limitRatioElevatorMinus = f;
        }
        f = sectfile.get(s2, "CLimitRatioElevatorMinusALWAYS", -1F);
        if ((f > 0.0F) && (f <= 1.0F)) {
            this.CT.limitRatioElevatorMinusALWAYS = f;
        }
        f = sectfile.get(s2, "CLimitRatioAITakeoffElevatorPlus", -1F);
        if ((f > 0.0F) && (f <= 1.0F)) {
            this.CT.limitRatioAITakeoffElevatorPlus = f;
        }
        f = sectfile.get(s2, "CLimitRatioRudder", -1F);
        if ((f > 0.0F) && (f <= 1.0F)) {
            this.CT.limitRatioRudder = f;
        }
        j = sectfile.get(s2, "CFlap", 0);  // By western, looking at 1st bit (1) and 2nd bit (2), enabling both is 3
        if ((j != 0) && (j != 1) && (j != 3)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasFlapsControl = ((j & 1) != 0);
        this.CT.bHasFlapsControlSwitch = ((j & 2) != 0);
// j = sectfile.get(s2, "CFlapPos", -1); // ignore stock code to avoid error
// if(j < 0 || j > 3)
// throw new RuntimeException(s1);
// CT.bHasFlapsControlRed = j < 3;
// TODO: Modified flap parameters in flight model. Allows up to 5 stages of flaps.
// --------------------------------------------------------
        j = sectfile.get(s2, "CFlapPos", -1);
        if (j < 0) {
            throw new RuntimeException(s1);
        }
        // By PAL, stock is j < 3, EngineMOD said j < 2. Kind of: ONLY ONE POSITION.
        this.CT.bHasFlapsControlRed = j < 2;
        if (this.CT.nFlapStages == -1) {
            this.CT.nFlapStages = j;
        }
        f = sectfile.get(s2, "CFlapStageMax", -1F);
        if (f > 0F) {
            this.CT.FlapStageMax = f;
        }
        // By PAL, multiple Discrete Angle Specified Flap Stages
        if ((this.CT.nFlapStages != -1) && (this.CT.FlapStageMax != -1.0F)) {
            if ((this.CT.FlapStage == null) && (this.CT.nFlapStages != -1)) {
                this.CT.FlapStage = new float[this.CT.nFlapStages + 1];
                this.CT.FlapStage[0] = 0.0F;
                this.CT.FlapStage[this.CT.nFlapStages] = 1.0F;
            }
            if (this.CT.FlapStage.length > 2) {
                for (int k = 0; k < (this.CT.FlapStage.length - 2); k++) {
                    f = sectfile.get(s2, "CFlapStage" + k, -1F);
                    if (f != -1F) {
                        this.CT.FlapStage[k + 1] = f / this.CT.FlapStageMax;
                    }
                }
            }
        }
        // By western, Flaps Control switch's real implement is done in each aircraft classes like F_18.class or AV_8.class
        // Those functions or meanings are completely different in each aircrafts and no common behaviors.
        if (this.CT.bHasFlapsControlSwitch && (this.CT.FlapStageText == null) && (this.CT.nFlapStages != -1)) {
            this.CT.FlapStageText = new String[this.CT.nFlapStages];
            String sst = sectfile.get(s2, "CFlapStageText", (String) null);
            if (sst != null) {
                StringTokenizer stringtokenizer = new StringTokenizer(sst, ",");
                for (int ii = 0; ii < this.CT.nFlapStages; ii++) {
                    this.CT.FlapStageText[ii] = stringtokenizer.nextToken();
                }
            }
        }
        // --------------------------------------------------------
        j = sectfile.get(s2, "CFlapBlown", 0);
        if ((j != 0) && (j > 3)) {
            throw new RuntimeException(s1);
        } else if (j > 0) {
            this.CT.bHasBlownFlaps = true;
        }
        if (j == 3) {
            this.CT.BlownFlapsType = null;
        }
        if (j == 2) {
            this.CT.BlownFlapsType = "SPS ";
        } else {
            this.CT.BlownFlapsType = "Blown Flaps ";
        }

        // TODO: Variable geometry/incidence.
        // --------------------------------------------------------
        // By western, looking at 1st bit (1) and 2nd bit (2), 3rd bit (4). 2nd and 3rd bits are exclusion.
        // 9th bit (256) means using as Nozzle rotation for V/STOL.
        // Capable integer value is 1, 3, 5 or 257, 259, 261
        j = sectfile.get(s2, "CVarWing", 0);
        int jj = (j & 255);
        if ((jj != 0) && (jj != 1) && (jj != 3) && (jj != 5)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasVarWingControl = ((jj & 1) != 0);
        this.CT.bHasVarWingControlFree = jj == 3;     // By western, no step but free-stop var wing
        this.CT.bHasVarWingControlSwitch = jj == 5;   // By western, VG wing is fully controlled by FBW
        this.CT.bUseVarWingAsNozzleRot = ((j & 256) != 0);
        j = sectfile.get(s2, "CVarWingPos", -1);
        if (this.CT.bHasVarWingControl && (j < 0)) {
            throw new RuntimeException(s1);
        }
        if (this.CT.nVarWingStages == -1) {
            this.CT.nVarWingStages = j;
        }
        float fx = sectfile.get(s2, "CVarWingStageMax", -1F);
        if (fx > 0F) {
            this.CT.VarWingStageMax = fx;
        }
        if ((this.CT.nVarWingStages != -1) && (this.CT.VarWingStageMax != -1.0F)) {
            if ((this.CT.VarWingStage == null) && (this.CT.nVarWingStages != -1)) {
                this.CT.VarWingStage = new float[this.CT.nVarWingStages + 1];
                this.CT.VarWingStage[0] = 0.0F;
                this.CT.VarWingStage[this.CT.nVarWingStages] = 1.0F;
            }
            if (this.CT.VarWingStage.length > 2) {
                for (int k = 0; k < (this.CT.VarWingStage.length - 2); k++) {
                    f = sectfile.get(s2, "CVarWingStage" + k, -1F);
                    if (f != -1F) {
                        this.CT.VarWingStage[k + 1] = f / this.CT.VarWingStageMax;
                    }
                }
            }
        }
        // By western, VarWing Control switch's real implement is done in each aircraft classes like F_14.class
        // Those functions or meanings are completely different in each aircrafts and no common behaviors.
        if (this.CT.bHasVarWingControlSwitch && (this.CT.VarWingStageText == null) && (this.CT.nVarWingStages != -1)) {
            this.CT.VarWingStageText = new String[this.CT.nVarWingStages];
            String sst = sectfile.get(s2, "CVarWingStageText", (String) null);
            if (sst != null) {
                StringTokenizer stringtokenizer = new StringTokenizer(sst, ",");
                for (int ii = 0; ii < this.CT.nVarWingStages; ii++) {
                    this.CT.VarWingStageText[ii] = stringtokenizer.nextToken();
                }
            }
        }
        j = sectfile.get(s2, "CVarIncidence", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasVarIncidence = j == 1;
        // --------------------------------------------------------
        // TODO: New entries for animated refuelling gear, drag chutes and bay doors
        j = sectfile.get(s2, "CCatLaunchBar", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasCatLaunchBarControl = j == 1;
        j = sectfile.get(s2, "CRefuel", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasRefuelControl = j == 1;
        j = sectfile.get(s2, "CDragChute", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasDragChuteControl = j == 1;
        j = sectfile.get(s2, "CBayDoors", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasBayDoors = j == 1;
        // --------------------------------------------------------
        j = sectfile.get(s2, "CDiveBrake", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasAirBrakeControl = j == 1;
        j = sectfile.get(s2, "CUndercarriage", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasGearControl = j == 1;
        j = sectfile.get(s2, "CArrestorHook", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasArrestorControl = j == 1;
        j = sectfile.get(s2, "CWingFold", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasWingControl = j == 1;
        // TODO: Altered door control - allows setting to prevent canopy opening on carriers
        j = sectfile.get(s2, "CCockpitDoor", 0);
        if (j == 2) {
            this.CT.bNoCarrierCanopyOpen = true;
        }
        if ((j != 0) && (j != 1) && (j != 2)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasCockpitDoorControl = (j != 0);
        j = sectfile.get(s2, "CWheelBrakes", 1);
        this.CT.bHasBrakeControl = j == 1;
        j = sectfile.get(s2, "CLockTailwheel", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasLockGearControl = j == 1;
        // TODO: Differential braking/steering settings + Level Stabilizer
        // --------------------------------------------------------
        j = sectfile.get(s2, "CDiffBrake", 0);
        if ((j != 0) && (j > 4)) {
            throw new RuntimeException(s1);
        }
        this.CT.DiffBrakesType = j;
        j = sectfile.get(s2, "CStabilizer", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasStabilizerControl = j == 1;
        // --------------------------------------------------------
        this.CT.AilThr = 0.27778F * sectfile.get(s2, "CAileronThreshold", 360F);
        this.CT.RudThr = 0.27778F * sectfile.get(s2, "CRudderThreshold", 360F);
        this.CT.ElevThr = 0.27778F * sectfile.get(s2, "CElevatorThreshold", 403.2F);
        this.CT.CalcTresholds();
        j = sectfile.get(s2, "CBombBay", 0);
        if ((j != 0) && (j != 1)) {
            throw new RuntimeException(s1);
        }
        this.CT.bHasBayDoorControl = j == 1;
        this.CT.setTrimAileronControl(sectfile.get(s2, "DefaultAileronTrim", -999F));
        if (this.CT.getTrimAileronControl() == -999F) {
            throw new RuntimeException(s1);
        }
        if (!this.CT.bHasElevatorTrim) {
            this.CT.setTrimElevatorControl(sectfile.get(s2, "DefaultElevatorTrim", -999F));
            if (this.CT.getTrimElevatorControl() == -999F) {
                throw new RuntimeException(s1);
            }
        }
        this.CT.setTrimRudderControl(sectfile.get(s2, "DefaultRudderTrim", -999F));
        if (this.CT.getTrimRudderControl() == -999F) {
            throw new RuntimeException(s1);
        }
        if (!this.CT.bHasGearControl) {
            this.GearCX = 0.0F;
            this.CT.GearControl = 1.0F;
            this.CT.setFixedGear(true);
        }
        if (!this.CT.bHasFlapsControl) {
            this.CT.FlapsControl = 0.0F;
        }
        j = sectfile.get(s2, "cElectricProp", 0);
        this.CT.bUseElectricProp = j == 1;
        // TODO: Deleted float prefix
        // TODO: apply individual R/L/C periods
        f = sectfile.get(s2, "GearPeriod", -999F);
        if (f != -999F) {
            this.CT.dvGear = 1.0F / f;
            this.CT.dvGearR = this.CT.dvGear * 0.95F;
            this.CT.dvGearL = this.CT.dvGear * 1.05F;
            this.CT.dvGearC = this.CT.dvGear * 1.11F;
        }
        f = sectfile.get(s2, "WingPeriod", -999F);
        if (f != -999F) {
            this.CT.dvWing = 1.0F / f;
        }
        f = sectfile.get(s2, "CockpitDoorPeriod", -999F);
        if (f != -999F) {
            this.CT.dvCockpitDoor = 1.0F / f;
        }
        // TODO: New FM parameter - AirBrakePeriod
        // --------------------------------------------------------
        f = sectfile.get(s2, "AirBrakePeriod", -999F);
        if (f != -999F) {
            this.CT.dvAirbrake = 1.0F / f;
        }
        // --------------------------------------------------------
        // TODO: +++ Online Compatibility / Smoke Bug Fix +++
        if (Mission.isNet()) {
            boolean bOnlineArrestor = sectfile.get(s2, "OnlineArrestorHook", this.CT.bHasArrestorControl ? 1 : 0) == 1;
            boolean bOnlineWingFold = sectfile.get(s2, "OnlineWingFold", this.CT.bHasWingControl ? 1 : 0) == 1;
            boolean bOnlineCockpitDoor = sectfile.get(s2, "OnlineCockpitDoor", this.CT.bHasCockpitDoorControl ? 1 : 0) == 1;
            boolean bOnlineBombBay = sectfile.get(s2, "OnlineBombBay", this.CT.bHasBayDoorControl ? 1 : 0) == 1;

            if (Config.cur.ini.get("Mods", "ShowOnlineCompatibleFlightModelChanges", 0) == 1) {
                if ((this.CT.bHasArrestorControl != bOnlineArrestor) || (this.CT.bHasWingControl != bOnlineWingFold) || (this.CT.bHasCockpitDoorControl != bOnlineCockpitDoor) || (this.CT.bHasBayDoorControl != bOnlineBombBay)) {
                    System.out.println("******************************************");
                    System.out.println("*** Online Mission active, Ensuring Stock");
                    System.out.println("*** Compatibility for " + s);
                    System.out.println("*** Parameter             Old    New");
                    if (this.CT.bHasArrestorControl != bOnlineArrestor) {
                        System.out.println("*** HasArrestorControl    " + this.CT.bHasArrestorControl + " " + bOnlineArrestor);
                    }
                    if (this.CT.bHasWingControl != bOnlineWingFold) {
                        System.out.println("*** HasWingControl        " + this.CT.bHasWingControl + " " + bOnlineWingFold);
                    }
                    if (this.CT.bHasCockpitDoorControl != bOnlineCockpitDoor) {
                        System.out.println("*** HasCockpitDoorControl " + this.CT.bHasCockpitDoorControl + " " + bOnlineCockpitDoor);
                    }
                    if (this.CT.bHasBayDoorControl != bOnlineBombBay) {
                        System.out.println("*** HasBayDoorControl     " + this.CT.bHasBayDoorControl + " " + bOnlineBombBay);
                    }
                    System.out.println("******************************************");
                }
            }
            this.CT.bHasArrestorControl = bOnlineArrestor;
            this.CT.bHasWingControl = bOnlineWingFold;
            this.CT.bHasCockpitDoorControl = bOnlineCockpitDoor;
            this.CT.bHasBayDoorControl = bOnlineBombBay;
        }
        // TODO: --- Online Compatibility / Smoke Bug Fix ---
        switch (this.Scheme) {
            default:
                throw new RuntimeException("Invalid Plane Scheme (Can't Get There!)..");

            case 0:
            case 1:
                float f1 = this.Length * 0.35F;
                f1 *= f1;
                float f11 = this.Length * 0.125F;
                f11 *= f11;
                float f6 = this.Wingspan * 0.2F;
                f6 *= f6;
                float f16 = this.Length * 0.07F;
                f16 *= f16;
                this.J0.z = (f1 * 0.2F) + (f11 * 0.4F) + (f6 * 0.4F);
                this.J0.y = (f1 * 0.2F) + (f11 * 0.4F) + (f16 * 0.4F);
                this.J0.x = (f16 * 0.6F) + (f6 * 0.4F);
                break;

            case 2:
                float f2 = this.Length * 0.35F;
                f2 *= f2;
                float f12 = this.Length * 0.125F;
                f12 *= f12;
                float f7 = this.Wingspan * 0.2F;
                f7 *= f7;
                float f17 = this.Length * 0.07F;
                f17 *= f17;
                this.J0.z = (f2 * 0.2F) + (f12 * 0.1F) + (f7 * 0.7F);
                this.J0.y = (f2 * 0.2F) + (f12 * 0.1F) + (f17 * 0.7F);
                this.J0.x = (f17 * 0.3F) + (f7 * 0.7F);
                break;

            case 3:
                float f3 = this.Length * 0.35F;
                f3 *= f3;
                float f13 = this.Length * 0.125F;
                f13 *= f13;
                float f8 = this.Wingspan * 0.2F;
                f8 *= f8;
                float f18 = this.Length * 0.07F;
                f18 *= f18;
                this.J0.z = (f3 * 0.2F) + (f13 * 0.2F) + (f8 * 0.6F);
                this.J0.y = (f3 * 0.2F) + (f13 * 0.2F) + (f18 * 0.6F);
                this.J0.x = (f18 * 0.2F) + (f8 * 0.8F);
                break;

            case 4:
            case 5:
            case 7:
                float f4 = this.Length * 0.35F;
                f4 *= f4;
                float f14 = this.Length * 0.125F;
                f14 *= f14;
                float f9 = this.Wingspan * 0.2F;
                f9 *= f9;
                float f19 = this.Length * 0.07F;
                f19 *= f19;
                this.J0.z = (f4 * 0.25F) + (f14 * 0.15F) + (f9 * 0.6F);
                this.J0.y = (f4 * 0.25F) + (f14 * 0.15F) + (f19 * 0.6F);
                this.J0.x = (f19 * 0.4F) + (f9 * 0.6F);
                break;

            case 6:
                float f5 = this.Length * 0.35F;
                f5 *= f5;
                float f15 = this.Length * 0.125F;
                f15 *= f15;
                float f10 = this.Wingspan * 0.2F;
                f10 *= f10;
                float f20 = this.Length * 0.07F;
                f20 *= f20;
                this.J0.z = (f5 * 0.25F) + (f15 * 0.15F) + (f10 * 0.6F);
                this.J0.y = (f5 * 0.25F) + (f15 * 0.15F) + (f20 * 0.6F);
                this.J0.x = (f20 * 0.4F) + (f10 * 0.6F);
                break;
        }
        s2 = "Params";
        if (sectfile.exist(s2, "ReferenceWeight")) {
            this.refM = sectfile.get(s2, "ReferenceWeight", 0.0F, -2000F, 2000F);
        } else {
            this.refM = 0.0F;
        }
        // By PAL, sub-loads!!!
        this.M.load(sectfile, this);
        this.Sq.load(sectfile);
        this.Arms.load(sectfile);
        Aircraft.debugprintln(this.actor, "Calling engines interface to resolve file '" + sectfile.toString() + "'....");
        this.EI.load((FlightModel) this, sectfile);
        this.Gears.load(sectfile);
        this.Ss.load(sectfile);
        if (sectfile.exist(s2, "G_CLASS")) {
            this.LimitLoad = sectfile.get(s2, "G_CLASS", 12F, 0.0F, 15F);
            this.LimitLoad = this.LimitLoad / 1.5F;
        } else {
            this.LimitLoad = 12F;
        }
        if (sectfile.exist(s2, "G_CLASS_COEFF")) {
            this.G_ClassCoeff = sectfile.get(s2, "G_CLASS_COEFF", 20F, -30F, 50F);
        } else {
            this.G_ClassCoeff = 20F;
        }
        float f21 = this.M.maxWeight * Atmosphere.g();
        float f22 = this.Sq.squareWing;
        this.Vmax = sectfile.get(s2, "Vmax", 1.0F);
        this.VmaxH = sectfile.get(s2, "VmaxH", 1.0F);
        this.Vmin = sectfile.get(s2, "Vmin", 1.0F);
        this.HofVmax = sectfile.get(s2, "HofVmax", 1.0F);
        this.VmaxFLAPS = sectfile.get(s2, "VmaxFLAPS", 1.0F);
        this.VminFLAPS = sectfile.get(s2, "VminFLAPS", 1.0F);
        this.SensYaw = sectfile.get(s2, "SensYaw", 1.0F);
        this.SensPitch = sectfile.get(s2, "SensPitch", 1.0F);
        this.SensRoll = sectfile.get(s2, "SensRoll", 1.0F);
        this.VmaxAllowed = sectfile.get(s2, "VmaxAllowed", this.VmaxH * 1.3F);
        this.Range = sectfile.get(s2, "Range", 800F);
        this.CruiseSpeed = sectfile.get(s2, "CruiseSpeed", 0.7F * this.Vmax);
        this.FuelConsumption = this.M.maxFuel / (0.64F * ((this.Range / this.CruiseSpeed) * 3600F) * this.EI.getNum());
        this.Vmax *= 0.2777778F;
        this.VmaxH *= 0.2777778F;
        this.Vmin *= 0.2777778F;
        this.VmaxFLAPS *= 0.2777778F;
        this.VminFLAPS *= 0.2416667F;
        this.VmaxAllowed *= 0.2777778F;
// float f23 = Atmosphere.density(0.0F);
        s2 = "Polares";
        this.Fusel.lineCyCoeff = 0.02F;
        this.Fusel.AOAMinCx_Shift = 0.0F;
        this.Fusel.Cy0_0 = 0.0F;
        this.Fusel.AOACritH_0 = 17F;
        this.Fusel.AOACritL_0 = -17F;
        this.Fusel.CyCritH_0 = 0.2F;
        this.Fusel.CyCritL_0 = -0.2F;
        this.Fusel.parabCxCoeff_0 = 0.0006F;
        this.Fusel.CxMin_0 = 0.0F;
        this.Fusel.Cy0_1 = 0.0F;
        this.Fusel.AOACritH_1 = 17F;
        this.Fusel.AOACritL_1 = -17F;
        this.Fusel.CyCritH_1 = 0.2F;
        this.Fusel.CyCritL_1 = -0.2F;
        this.Fusel.CxMin_1 = 0.0F;
        this.Fusel.parabCxCoeff_1 = 0.0006F;
        this.Fusel.declineCoeff = 0.007F;
        this.Fusel.maxDistAng = 30F;
        this.Fusel.parabAngle = 5F;
        this.Fusel.setFlaps(0.0F);
        this.Tail.lineCyCoeff = 0.085F;
        this.Tail.AOAMinCx_Shift = 0.0F;
        this.Tail.Cy0_0 = 0.0F;
        this.Tail.AOACritH_0 = 17F;
        this.Tail.AOACritL_0 = -17F;
        this.Tail.CyCritH_0 = 1.1F;
        this.Tail.CyCritL_0 = -1.1F;
        this.Tail.parabCxCoeff_0 = 0.0006F;
        this.Tail.CxMin_0 = 0.02F;
        this.Tail.Cy0_1 = 0.0F;
        this.Tail.AOACritH_1 = 17F;
        this.Tail.AOACritL_1 = -17F;
        this.Tail.CyCritH_1 = 1.1F;
        this.Tail.CyCritL_1 = -1.1F;
        this.Tail.CxMin_1 = 0.02F;
        this.Tail.parabCxCoeff_1 = 0.0006F;
        this.Tail.declineCoeff = 0.007F;
        this.Tail.maxDistAng = 30F;
        this.Tail.parabAngle = 5F;
        this.Tail.setFlaps(0.0F);
        s2 = "Params";
        this.Wing.AOA_crit = sectfile.get(s2, "CriticalAOA", 16F);
        this.Wing.V_max = 0.27778F * sectfile.get(s2, "Vmax", 500F);
        this.Wing.V_min = 0.27778F * sectfile.get(s2, "Vmin", 160F);
        this.Wing.V_maxFlaps = 0.27778F * sectfile.get(s2, "VmaxFLAPS", 270F);
        this.Wing.V_land = 0.27778F * sectfile.get(s2, "VminFLAPS", 140F);
        this.Wing.T_turn = sectfile.get(s2, "T_turn", 20F);
        this.Wing.V_turn = 0.27778F * sectfile.get(s2, "V_turn", 300F);
        this.Wing.Vz_climb = sectfile.get(s2, "Vz_climb", 18F);
        this.Wing.V_climb = 0.27778F * sectfile.get(s2, "V_climb", 270F);
        this.Wing.K_max = sectfile.get(s2, "K_max", 14F);
        this.Wing.Cy0_max = sectfile.get(s2, "Cy0_max", 0.15F);
        this.Wing.FlapsMult = sectfile.get(s2, "FlapsMult", 0.16F);
        this.Wing.FlapsAngSh = sectfile.get(s2, "FlapsAngSh", 4F);
        this.Wing.P_Vmax = this.EI.forcePropAOA(this.Wing.V_max, 0.0F, 1.1F, true);
        this.Wing.S = f22;
        this.Wing.G = f21;
        s2 = "Polares";
        f = sectfile.get(s2, "lineCyCoeff", -999F);
        if (f != -999F) {
            this.Wing.lineCyCoeff = f;
            this.Wing.AOAMinCx_Shift = sectfile.get(s2, "AOAMinCx_Shift", 0.0F);
            this.Wing.Cy0_0 = sectfile.get(s2, "Cy0_0", 0.15F);
            this.Wing.AOACritH_0 = sectfile.get(s2, "AOACritH_0", 16F);
            this.Wing.AOACritL_0 = sectfile.get(s2, "AOACritL_0", -16F);
            this.Wing.CyCritH_0 = sectfile.get(s2, "CyCritH_0", 1.1F);
            this.Wing.CyCritL_0 = sectfile.get(s2, "CyCritL_0", -0.8F);
            this.Wing.parabCxCoeff_0 = sectfile.get(s2, "parabCxCoeff_0", 0.0008F);
            this.Wing.CxMin_0 = sectfile.get(s2, "CxMin_0", 0.026F);
            // TODO: Blown flaps
            // -----------------------------------------------------
            this.CyBlownFlapsOff = this.Wing.Cy0_1 = sectfile.get(s2, "Cy0_1", 0.65F);
            this.CyBlownFlapsOn = sectfile.get(s2, "CyBFlap", 0.5F);
            this.ThrustBlownFlaps = sectfile.get(s2, "ThrustBFlap", 0.0F);
            // -----------------------------------------------------
            this.Wing.Cy0_1 = sectfile.get(s2, "Cy0_1", 0.65F);
            this.Wing.AOACritH_1 = sectfile.get(s2, "AOACritH_1", 15F);
            this.Wing.AOACritL_1 = sectfile.get(s2, "AOACritL_1", -18F);
            this.Wing.CyCritH_1 = sectfile.get(s2, "CyCritH_1", 1.6F);
            this.Wing.CyCritL_1 = sectfile.get(s2, "CyCritL_1", -0.75F);
            this.Wing.CxMin_1 = sectfile.get(s2, "CxMin_1", 0.09F);
            this.Wing.parabCxCoeff_1 = sectfile.get(s2, "parabCxCoeff_1", 0.0025F);
            this.Wing.parabAngle = sectfile.get(s2, "parabAngle", 5F);
            this.Wing.declineCoeff = sectfile.get(s2, "Decline", 0.007F);
            this.Wing.maxDistAng = sectfile.get(s2, "maxDistAng", 30F);
            this.Wing.setFlaps(0.0F);
            // TODO: +++ Check Mach Drag availability prior to loading Mach Params +++
            String machCheck = sectfile.get(s2, "mc3", "");
            if (machCheck.length() > 8) {
                this.Wing.loadMachParams(sectfile);
            } else {
                // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// System.out.println("Flight Model File " + s + " contains no Mach Drag Parameters.");
                logFmLoad("Flight Model File \"{0}\" contains no Mach Drag Parameters.", s, null, FM_LOGLEVEL_EVERYFM);
                // TODO: --- log.lst Spam fighting by SAS~Storebror ---
                this.Wing.mcMin = 999.0F;
            }
// Wing.loadMachParams(sectfile);
// TODO: --- Check Mach Drag availability prior to loading Mach Params code ---
        } else {
            throw new RuntimeException(s1);
        }
        this.AOA_Crit = sectfile.get(s2, "AOACritH_0", -999F);
    }

    public void drawData() {
    }

    public void drawSpeed(String s, float f, float f1, float f2) {
        try {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            for (int i = 0; i <= 10000; i += 100) {
                printwriter.print(i + "\t");
                float f3 = -1000F;
                float f4 = -1000F;
                int j = 50;
                do {
                    if (j >= 1500) {
                        break;
                    }
                    float f5 = this.EI.forcePropAOA(j * 0.27778F, i, f1, true, f2);
                    float f7 = this.Wing.getClimb(j * 0.27778F, i, f5, (this.Sq.dragParasiteCx * 0.5F) / this.Wing.S);
                    if (f7 > f3) {
                        f3 = f7;
                    }
                    if ((f7 < 0.0F) && (f7 < f3)) {
                        f4 = j;
                        break;
                    }
                    j++;
                } while (true);
                if (f4 < 0.0F) {
                    printwriter.print("\t");
                } else {
                    printwriter.print(f4 + "\t");
                }
                printwriter.print((f3 * this.Wing.Vyfac) + "\t");
                f3 = -1000F;
                f4 = -1000F;
                j = 50;
                do {
                    if (j >= 1500) {
                        break;
                    }
                    float f6 = this.EI.forcePropAOA(j * 0.27778F, i, f, false, f2);
                    float f8 = this.Wing.getClimb(j * 0.27778F, i, f6, (this.Sq.dragParasiteCx * 0.5F) / this.Wing.S);
                    if (f8 > f3) {
                        f3 = f8;
                    }
                    if ((f8 < 0.0F) && (f8 < f3)) {
                        f4 = j;
                        break;
                    }
                    j++;
                } while (true);
                if (f4 < 0.0F) {
                    printwriter.print("\t");
                } else {
                    printwriter.print(f4 + "\t");
                }
                printwriter.print((f3 * this.Wing.Vyfac) + "\t");
                printwriter.println();
            }

            printwriter.close();
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public FlightModelMain(String s) {
        // By PAL, DiffFM specific Options, on top to be set before load!
// bPrintFM = Config.cur.ini.get("Mods", "PrintFMDinfo", 0) == 1; // merged to Storebror's logLevel; no more needed
        bDebugFM = Config.cur.ini.get("Mods", "PALDebugFM", 0) == 1;
        bDumpFM = bDumpFM && bDebugFM;

        // By PAL, why did the Engine MOD had these dupp NULL initializations?
        // By western, I'm not sure but maybe decompiler's meaningless work.
// flags0 = 0L;
// bDamagedGround = false;
// bDamaged = false;
// damagedInitiator = null;
// Skill = 0;
// crew = 0;
// turretSkill = 0;
// AP = null;
// CT = null;
// SensYaw = 0.0F;
// SensPitch = 0.0F;
// SensRoll = 0.0F;
// GearCX = 0.0F;
// radiatorCX = 0.0F;
// Loc = null;
// Or = null;
// Leader = null;
// Wingman = null;
// Offset = null;
// formationType = 0;
// formationScale = 0.0F;
// minElevCoeff = 0.0F;
// AOA = 0.0F;
// AOS = 0.0F;
// V = 0.0F;
// V2 = 0.0F;
// q_ = 0.0F;
// Gravity = 0.0F;
// Mach = 0.0F;
// Energy = 0.0F;
// BarometerZ = 0.0F;
// WingDiff = 0.0F;
// WingLoss = 0.0F;
// turbCoeff = 0.0F;
// FuelConsumption = 0.0F;
// producedAM = null;
// producedAMM = null;
// producedAF = null;
// fmsfxCurrentType = 0;
// fmsfxPrevValue = 0.0F;
// fmsfxTimeDisable = 0L;
// AF = null;
// AM = null;
// GF = null;
// GM = null;
// SummF = null;
// SummM = null;
// ACmeter = null;
// Accel = null;
// LocalAccel = null;
// BallAccel = null;
// Vwld = null;
// Vrel = null;
// Vair = null;
// Vflow = null;
// Vwind = null;
// J0 = null;
// J = null;
// W = null;
// AW = null;
// EI = null;
// M = null;
// AS = null;
// Sq = null;
// Ss = null;
// Arms = null;
// Gears = null;
// AIRBRAKE = false;
// Wing = null;
// Tail = null;
// Fusel = null;
// Scheme = 0;
// Wingspan = 0.0F;
// Length = 0.0F;
// Vmax = 0.0F;
// VmaxH = 0.0F;
// Vmin = 0.0F;
// HofVmax = 0.0F;
// VmaxFLAPS = 0.0F;
// VminFLAPS = 0.0F;
// VmaxAllowed = 0.0F;
// indSpeed = 0.0F;
// AOA_Crit = 0.0F;
// Range = 0.0F;
// CruiseSpeed = 0.0F;
// damagedParts = null;
// maxDamage = 0;
// cutPart = 0;
// bReal = false;
// UltimateLoad = 0.0F;
// Negative_G_Limit = 0.0F;
// Negative_G_Ultimate = 0.0F;
// LimitLoad = 0.0F;
// G_ClassCoeff = 0.0F;
// refM = 0.0F;
// SafetyFactor = 0.0F;
// ReferenceForce = 0.0F;
// turnFile = null;
// speedFile = null;
// craftFile = null;
// Operate = 0L;
        this.flags0 = 240L;
        this.bDamagedGround = false;
        this.bDamaged = false;
        this.damagedInitiator = null;
        this.CT = new Controls(this);
        this.SensYaw = 0.3F;
        this.SensPitch = 0.5F;
        this.SensRoll = 0.4F;
        this.GearCX = 0.035F;
        this.radiatorCX = 0.003F;
        this.Loc = new Point3d();
        this.Or = new Orientation();
        this.Offset = new Vector3d(25D, 25D, 0.0D);
        this.formationType = 0;
        this.formationScale = 1.0F;
        this.minElevCoeff = 4F;
        this.BarometerZ = 0.0F;
        this.WingDiff = 0.0F;
        this.WingLoss = 0.0F;
        this.turbCoeff = 1.0F;
        this.FuelConsumption = 0.06F;
        this.producedAM = new Vector3d(0.0D, 0.0D, 0.0D);
        this.producedAMM = new Vector3d(0.0D, 0.0D, 0.0D);
        this.producedAF = new Vector3d(0.0D, 0.0D, 0.0D);
        this.fmsfxCurrentType = -1;
        this.fmsfxPrevValue = 0.0F;
        this.fmsfxTimeDisable = -1L;
        this.AF = new Vector3d();
        this.AM = new Vector3d();
        this.GF = new Vector3d();
        this.GM = new Vector3d();
        this.SummF = new Vector3d();
        this.SummM = new Vector3d();
        this.ACmeter = new Vector3d();
        this.Accel = new Vector3d();
        this.LocalAccel = new Vector3d();
        this.BallAccel = new Vector3d();
        this.Vwld = new Vector3d();
        this.Vrel = new Vector3d();
        this.Vair = new Vector3d();
        this.Vflow = new Vector3d();
        this.Vwind = new Vector3d();
        this.J0 = new Vector3d();
        this.J = new Vector3d();
        this.W = new Vector3d();
        this.AW = new Vector3d();
        this.EI = new EnginesInterface();
        this.M = new Mass();
        this.AS = new AircraftState();
        this.Sq = new Squares();
        // TODO: New parameter
        // --------------------------------------------------------
        this.Ss = new Supersonic();
        this.CyBlownFlapsOn = 0.0F;
        this.CyBlownFlapsOff = 0.0F;
        this.ThrustBlownFlaps = 0.0F;
        // --------------------------------------------------------
        this.Arms = new Arm();
        this.Gears = new Gear();
        this.Wing = new Polares();
        this.Tail = new Polares();
        this.Fusel = new Polares();
        this.Vmax = 116.6667F;
        this.VmaxH = 122.2222F;
        this.Vmin = 43.33334F;
        this.HofVmax = 277.7778F;
        this.VmaxFLAPS = 72.22223F;
        this.VminFLAPS = 39.72223F;
        this.VmaxAllowed = 208.3333F;
        this.indSpeed = 0.0F;
        this.AOA_Crit = 15.5F;
        this.Range = 800F;
        this.CruiseSpeed = 370F;
        this.damagedParts = new int[7];
        this.maxDamage = 0;
        this.cutPart = -1;
        this.UltimateLoad = 12F;
        this.Negative_G_Limit = -4F;
        this.Negative_G_Ultimate = -6F;
        this.LimitLoad = 8F;
        this.SafetyFactor = 1.5F;
        this.turnFile = new String();
        this.speedFile = new String();
        this.craftFile = new String();
        this.Operate = 0xfffffffffffL;
        this.load(s);
        this.init_G_Limits();
    }

    public void set(Loc loc, Vector3f vector3f) {
        this.actor.pos.setAbs(loc);
        this.Vwld.set(vector3f);
        loc.get(this.Loc, this.Or);
    }

    public void set(Point3d point3d, Orient orient, Vector3f vector3f) {
        this.actor.pos.setAbs(point3d, orient);
        this.Vwld.set(vector3f);
        this.Loc.set(point3d);
        this.Or.set(orient);
    }

    public void update(float f) {
        ((Aircraft) this.actor).update(f);
        // TODO: Adds extra lift generated by blown flaps
        if (this.CT.bHasBlownFlaps) {
            if (this.CT.BlownFlapsType != null) {
                if ((this.CT.BlownFlapsControl > 0.0F) && (this.CT.FlapsControl > 0.0F) && (this.EI.getThrustOutput() > 0.2F)) {
                    this.Wing.Cy0_1 = this.CyBlownFlapsOff + (this.CyBlownFlapsOn * this.CT.BlownFlapsControl);
                    this.producedAF.x -= (this.ThrustBlownFlaps * this.EI.getThrustOutput());
                } else {
                    this.Wing.Cy0_1 = this.CyBlownFlapsOff;
                    if (this.CT.FlapsControl == 0F) {
                        this.CT.BlownFlapsControl = 0.0F;
                    }
                }
            } else if (this.CT.FlapsControl > 0.0F) {
                this.Wing.Cy0_1 = this.CyBlownFlapsOff + (this.CyBlownFlapsOn * this.EI.getThrustOutput());
            }
        }
    }

    public boolean tick() {
        float f = Time.tickLenFs();
        // By PAL, in stock 4.12.2 parasite line:
        // float f1 = (float)(Loc.z - Engine.land().HQ_Air(Loc.x, Loc.y));
        this.actor.pos.getAbs(this.Loc, this.Or);
        int i = (int) (f / 0.05F) + 1;
        float f2 = f / i;
        for (int j = 0; j < i; j++) {
            this.update(f2);
        }

        this.Gears.bFlatTopGearCheck = false;
        if (this.actor.pos.base() == null) {
            this.actor.pos.setAbs(this.Loc, this.Or);
        } else {
            if (this.actor.pos.base() instanceof Aircraft) {
                this.Vwld.set(((Aircraft) this.actor.pos.base()).FM.Vwld);
            } else {
                this.actor.pos.speed(this.actVwld);
                this.Vwld.x = (float) this.actVwld.x;
                this.Vwld.y = (float) this.actVwld.y;
                this.Vwld.z = (float) this.actVwld.z;
            }
            this.actor.pos.getAbs(this.Or);
            this.producedAF.z += 9.81F * this.M.mass;
        }
        this.Energy = (Atmosphere.g() * (float) this.Loc.z) + (this.V2 * 0.5F);
        return true;
    }

    public float getOverload() {
        return (float) this.ACmeter.z;
    }

    public float getForwAccel() {
        return (float) this.ACmeter.x;
    }

    public float getRollAcceleration() {
        return (float) this.AM.x / this.Gravity;
    }

    public void gunPulse(Vector3d vector3d) {
        this.GPulse.set(vector3d);
        this.GPulse.scale(1.0F / this.M.mass);
        this.Vwld.sub(this.GPulse);
    }

    private void cutOp(int i) {
        this.Operate &= ~(1L << i);
    }

    protected boolean getOp(int i) {
        return (this.Operate & (1L << i)) != 0L;
    }

    private float Op(int i) {
        return this.getOp(i) ? 1.0F : 0.0F;
    }

    public final boolean isPlayers() {
        return (this.actor != null) && (this.actor == World.getPlayerAircraft());
    }

    public final boolean isCapableOfACM() {
        return (this.flags0 & FMFLAGS_CAPABLEACM) != 0L;
    }

    public final boolean isCapableOfBMP() {
        return (this.flags0 & FMFLAGS_CAPABLEAIRWORTHY) != 0L;
    }

    public final boolean isCapableOfTaxiing() {
        return (this.flags0 & FMFLAGS_CAPABLETAXI) != 0L;
    }

    public final boolean isReadyToDie() {
        return (this.flags0 & FMFLAGS_READYTODIE) != 0L;
    }

    public final boolean isReadyToReturn() {
        return (this.flags0 & FMFLAGS_READYTORETURN) != 0L;
    }

    public final boolean isTakenMortalDamage() {
        return (this.flags0 & FMFLAGS_TAKENMORTALDAMAGE) != 0L;
    }

    public final boolean isStationedOnGround() {
        return (this.flags0 & FMFLAGS_STATIONEDONGROUND) != 0L;
    }

    public final boolean isCrashedOnGround() {
        return (this.flags0 & FMFLAGS_CRASHEDONGROUND) != 0L;
    }

    public final boolean isNearAirdrome() {
        return (this.flags0 & FMFLAGS_NEARAIRDROME) != 0L;
    }

    public final boolean isCrossCountry() {
        return (this.flags0 & FMFLAGS_ISCROSSCOUNTRY) != 0L;
    }

    public final boolean isWasAirborne() {
        return (this.flags0 & FMFLAGS_WASAIRBORNE) != 0L;
    }

    public final boolean isSentWingNote() {
        return (this.flags0 & FMFLAGS_NETSENTWINGNOTE) != 0L;
    }

    public final boolean isSentBuryNote() {
        return (this.flags0 & FMFLAGS_NETSENTBURYNOTE) != 0L;
    }

    public final boolean isSentControlsOutNote() {
        return (this.flags0 & FMFLAGS_NETSENTCTRLSDMG) != 0L;
    }

    public boolean isOk() {
        return this.isCapableOfBMP() && !this.isReadyToDie() && !this.isTakenMortalDamage();
    }

    private void checkDamaged() {
        if (!this.bDamaged && Actor.isValid(this.damagedInitiator) && (!this.isCapableOfBMP() || this.isTakenMortalDamage())) {
            this.bDamaged = true;
            if (this.actor != this.damagedInitiator) {
                EventLog.onDamaged(this.actor, this.damagedInitiator);
            }
            this.damagedInitiator = null;
        }
        if (!this.bDamagedGround && this.isStationedOnGround() && (!this.isCapableOfBMP() || !this.isCapableOfTaxiing() || this.isReadyToDie() || this.isTakenMortalDamage() || this.isSentControlsOutNote())) {
            this.bDamagedGround = true;
            EventLog.onDamagedGround(this.actor);
        }
    }

    public final void setCapableOfACM(boolean flag) {
        if (this.isCapableOfACM() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_CAPABLEACM;
        } else {
            this.flags0 &= -FMFLAGS_CAPABLEACM - 1L;
        }
    }

    public final void setCapableOfBMP(boolean flag, Actor actor) {
        if (this.isCapableOfBMP() == flag) {
            return;
        }
        if (this.isCapableOfBMP() && (World.Rnd().nextInt(0, 99) < 25)) {
            Voice.speakMayday((Aircraft) this.actor);
        }
        if (flag) {
            this.flags0 |= FMFLAGS_CAPABLEAIRWORTHY;
        } else {
            this.flags0 &= -FMFLAGS_CAPABLEAIRWORTHY - 1L;
            if (!this.bDamaged) {
                this.damagedInitiator = actor;
            }
            this.checkDamaged();
        }
    }

    public final void setCapableOfTaxiing(boolean flag) {
        if (this.isCapableOfTaxiing() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_CAPABLETAXI;
        } else {
            this.flags0 &= -FMFLAGS_CAPABLETAXI - 1L;
            this.checkDamaged();
        }
    }

    public final void setReadyToDie(boolean flag) {
        if (this.isReadyToDie() == flag) {
            return;
        }
        if (!this.isReadyToDie()) {
            if (World.Rnd().nextInt(0, 99) < 75) {
                Voice.speakMayday((Aircraft) this.actor);
            }
            Explosions.generateComicBulb(this.actor, "OnFire", 9F);
        }
        if (flag) {
            this.flags0 |= FMFLAGS_READYTODIE;
            this.checkDamaged();
        } else {
            this.flags0 &= -FMFLAGS_READYTODIE - 1L;
        }
    }

    public final void setReadyToDieSoftly(boolean flag) {
        if (this.isReadyToDie() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_READYTODIE;
            this.checkDamaged();
        } else {
            this.flags0 &= -FMFLAGS_READYTODIE - 1L;
        }
    }

    public final void setReadyToReturn(boolean flag) {
        if (this.isReadyToReturn() == flag) {
            return;
        }
        if (!this.isReadyToReturn()) {
            Explosions.generateComicBulb(this.actor, "RTB", 9F);
        }
        if (flag) {
            this.flags0 |= FMFLAGS_READYTORETURN;
        } else {
            this.flags0 &= -FMFLAGS_READYTORETURN - 1L;
        }
        Voice.speakToReturn((Aircraft) this.actor);
    }

    public final void setReadyToReturnSoftly(boolean flag) {
        if (this.isReadyToReturn() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_READYTORETURN;
        } else {
            this.flags0 &= -FMFLAGS_READYTORETURN - 1L;
        }
    }

    public final void setTakenMortalDamage(boolean flag, Actor actor) {
        if (this.isTakenMortalDamage() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_TAKENMORTALDAMAGE;
            if (!this.bDamaged && !Actor.isValid(this.damagedInitiator)) {
                this.damagedInitiator = actor;
            }
            this.checkDamaged();
        } else {
            this.flags0 &= -FMFLAGS_TAKENMORTALDAMAGE - 1L;
        }
        if (flag && (this.actor != World.getPlayerAircraft()) && (((Aircraft) this.actor).FM.turret.length > 0)) {
            for (int i = 0; i < ((Aircraft) this.actor).FM.turret.length; i++) {
                ((Aircraft) this.actor).FM.turret[i].bIsOperable = false;
            }

        }
    }

    public final void setStationedOnGround(boolean flag) {
        if (this.isStationedOnGround() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_STATIONEDONGROUND;
            EventLog.onAirLanded((Aircraft) this.actor);
            this.checkDamaged();
        } else {
            this.flags0 &= -FMFLAGS_STATIONEDONGROUND - 1L;
        }
    }

    public final void setCrashedOnGround(boolean flag) {
        if (this.isCrashedOnGround() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_CRASHEDONGROUND;
            this.checkDamaged();
        } else {
            this.flags0 &= -FMFLAGS_CRASHEDONGROUND - 1L;
        }
    }

    public final void setNearAirdrome(boolean flag) {
        if (this.isNearAirdrome() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_NEARAIRDROME;
        } else {
            this.flags0 &= -FMFLAGS_NEARAIRDROME - 1L;
        }
    }

    public final void setCrossCountry(boolean flag) {
        if (this.isCrossCountry() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_ISCROSSCOUNTRY;
        } else {
            this.flags0 &= -FMFLAGS_ISCROSSCOUNTRY - 1L;
        }
    }

    public final void setWasAirborne(boolean flag) {
        if (this.isWasAirborne() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_WASAIRBORNE;
        } else {
            this.flags0 &= -FMFLAGS_WASAIRBORNE - 1L;
        }
    }

    public final void setSentWingNote(boolean flag) {
        if (this.isSentWingNote() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_NETSENTWINGNOTE;
        } else {
            this.flags0 &= -FMFLAGS_NETSENTWINGNOTE - 1L;
        }
    }

    public final void setSentBuryNote(boolean flag) {
        if (this.isSentBuryNote() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_NETSENTBURYNOTE;
        } else {
            this.flags0 &= -FMFLAGS_NETSENTBURYNOTE - 1L;
        }
    }

    public final void setSentControlsOutNote(boolean flag) {
        if (this.isSentControlsOutNote() == flag) {
            return;
        }
        if (flag) {
            this.flags0 |= FMFLAGS_NETSENTCTRLSDMG;
            this.checkDamaged();
        } else {
            this.flags0 &= -FMFLAGS_NETSENTCTRLSDMG - 1L;
        }
    }

    public void hit(int i) {
        Aircraft.debugprintln(this.actor, "Detected NDL in " + Aircraft.partNames()[i] + "..");
        if ((i < 0) || (i >= 44)) {
            return;
        }
        if (this instanceof RealFlightModel) {
            this.bReal = true;
        } else {
            this.bReal = false;
        }
        switch (i) {
            case 2:
            case 7:
            case 8:
            case 9:
            case 10:
            case 14:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            default:
                break;

            case 13:
                this.Sq.dragFuselageCx = this.Sq.dragFuselageCx <= 0.08F ? 0.08F : this.Sq.dragFuselageCx * 2.0F;
                break;

            case 0:
                this.Sq.liftWingLOut *= 0.95F;
                this.SensRoll *= 0.68F;
                break;

            case 1:
                this.Sq.liftWingROut *= 0.95F;
                this.SensRoll *= 0.68F;
                break;

            case 17:
                this.Sq.liftStab *= 0.68F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                break;

            case 18:
                this.Sq.liftStab *= 0.68F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                break;

            case 31:
                this.Sq.squareElevators *= 0.68F;
                this.SensPitch *= 0.68F;
                break;

            case 32:
                this.Sq.squareElevators *= 0.68F;
                this.SensPitch *= 0.68F;
                break;

            case 11:
                this.Sq.liftKeel *= 0.68F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                break;

            case 12:
                this.Sq.liftKeel *= 0.68F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                break;

            case 15:
                this.Sq.squareRudders *= 0.5F;
                this.SensYaw *= 0.68F;
                break;

            case 16:
                this.Sq.squareRudders *= 0.5F;
                this.SensYaw *= 0.68F;
                break;

            case 33:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.6F, 2);
                }
                this.Sq.getClass();
                this.Sq.liftWingLIn -= 0.8F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setCapableOfACM(false);
                }
                break;

            case 36:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.6F, 3);
                }
                this.Sq.getClass();
                this.Sq.liftWingRIn -= 0.8F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setCapableOfACM(false);
                }
                break;

            case 34:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.7F, 1);
                }
                this.Sq.getClass();
                this.Sq.liftWingLMid -= 0.8F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setCapableOfACM(false);
                }
                break;

            case 37:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.7F, 4);
                }
                this.Sq.getClass();
                this.Sq.liftWingRMid -= 0.8F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextFloat() < 0.09F) {
                    this.setCapableOfACM(false);
                }
                break;

            case 35:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.8F, 0);
                }
                this.Sq.getClass();
                this.Sq.liftWingLOut -= 0.8F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setCapableOfACM(false);
                }
                break;

            case 38:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.8F, 5);
                }
                this.Sq.getClass();
                this.Sq.liftWingROut -= 0.8F;
                this.Sq.getClass();
                this.Sq.dragProducedCx += 0.12F;
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setCapableOfACM(false);
                }
                break;

            case 3:
                if (this.Sq.dragEngineCx[0] < 0.15F) {
                    this.Sq.dragEngineCx[0] += 0.05D;
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setReadyToReturn(true);
                }
                break;

            case 4:
                if (this.Sq.dragEngineCx[1] < 0.15F) {
                    this.Sq.dragEngineCx[1] += 0.05F;
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setReadyToReturn(true);
                }
                break;

            case 5:
                if (this.Sq.dragEngineCx[2] < 0.15F) {
                    this.Sq.dragEngineCx[2] += 0.05F;
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setReadyToReturn(true);
                }
                break;

            case 6:
                if (this.Sq.dragEngineCx[3] < 0.15F) {
                    this.Sq.dragEngineCx[3] += 0.05F;
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.setReadyToReturn(true);
                }
                break;

            case 19:
            case 20:
                if (this.bReal) {
                    this.setDmgLoadLimit(0.5F, 6);
                }
                break;
        }
    }

    public void cut(int i, int j, Actor actor) {
        if ((i < 0) || (i >= 44)) {
            return;
        }
        Aircraft.debugprintln(this.actor, "cutting part #" + i + " (" + Aircraft.partNames()[i] + ")");
        if (!this.getOp(i)) {
            Aircraft.debugprintln(this.actor, "part #" + i + " (" + Aircraft.partNames()[i] + ") already cut off");
            return;
        }
        this.cutOp(i);
        switch (i) {
            case 8:
            case 14:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            default:
                break;

            case 13:
                this.setCapableOfBMP(false, actor);
                this.setCapableOfACM(false);
                this.Sq.dragFuselageCx = this.Sq.dragFuselageCx <= 0.24F ? 0.24F : this.Sq.dragFuselageCx * 2.0F;
                break;

            case 9:
                this.setCapableOfTaxiing(false);
                this.Gears.hitLeftGear();
                break;

            case 10:
                this.setCapableOfTaxiing(false);
                this.Gears.hitRightGear();
                break;

            case 7:
                this.setCapableOfTaxiing(false);
                this.Gears.hitCentreGear();
                break;

            case 2:
                this.setCapableOfACM(false);
                this.setCapableOfBMP(false, actor);
                this.setCapableOfTaxiing(false);
                this.setTakenMortalDamage(true, actor);
                for (int k = 0; k < this.EI.getNum(); k++) {
                    this.EI.engines[k].setReadyness(this.actor, 0.0F);
                }

                this.cutOp(19);
                this.cutOp(20);
                // fall through

            case 19:
            case 20:
                this.setCapableOfACM(false);
                this.setCapableOfBMP(false, actor);
                this.setReadyToDie(true);
                this.setTakenMortalDamage(true, actor);
                this.Arms.GCENTER = 0.0F;
                this.W.y += World.Rnd().nextFloat(-0.1F, 0.1F);
                this.W.z += World.Rnd().nextFloat(-0.1F, 0.1F);
                this.J0.y *= 0.5D;
                this.cut(17, j, actor);
                this.cut(18, j, actor);
                this.cut(11, j, actor);
                this.cut(12, j, actor);
                break;

            case 17:
                if (World.Rnd().nextInt(-1, 8) < this.Skill) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextInt(-1, 16) < this.Skill) {
                    this.setReadyToDie(true);
                }
                this.Sq.liftStab *= (0.5F * this.Op(18)) + 0.1F;
                if (this.isPlayers()) {
                    this.Sq.getClass();
                    this.Sq.dragProducedCx += 0.12F;
                } else {
                    this.Sq.getClass();
                    this.Sq.dragProducedCx += 0.06F;
                }
                this.Sq.liftWingRMid *= 0.9F;
                this.Sq.liftWingLMid *= 1.1F;
                this.Sq.liftWingROut *= 0.9F;
                this.Sq.liftWingLOut *= 1.1F;
                if (this.Op(18) == 0.0F) {
                    this.CT.setTrimAileronControl(this.CT.getTrimAileronControl() - 0.25F);
                    if (World.Rnd().nextBoolean()) {
                        this.Sq.liftWingLOut *= 0.95F;
                        this.Sq.liftWingLMid *= 0.95F;
                        this.Sq.liftWingLIn *= 0.95F;
                        this.Sq.liftWingRIn *= 0.75F;
                        this.Sq.liftWingRMid *= 0.75F;
                        this.Sq.liftWingROut *= 0.75F;
                    } else {
                        this.Sq.liftWingROut *= 0.75F;
                        this.Sq.liftWingRMid *= 0.75F;
                        this.Sq.liftWingRIn *= 0.75F;
                        this.Sq.liftWingLIn *= 0.95F;
                        this.Sq.liftWingLMid *= 0.95F;
                        this.Sq.liftWingLOut *= 0.95F;
                    }
                }
                this.cutOp(31);
                // fall through

            case 31:
                this.setCapableOfACM(false);
                if (this.Op(32) == 0.0F) {
                    this.setReadyToDie(true);
                }
                this.Sq.squareElevators *= 0.5F * this.Op(32);
                this.SensPitch *= 0.5F * this.Op(32);
                break;

            case 18:
                if (World.Rnd().nextInt(-1, 8) < this.Skill) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextInt(-1, 16) < this.Skill) {
                    this.setReadyToDie(true);
                }
                this.Sq.liftStab *= (0.5F * this.Op(17)) + 0.1F;
                if (this.isPlayers()) {
                    this.Sq.getClass();
                    this.Sq.dragProducedCx += 0.12F;
                } else {
                    this.Sq.getClass();
                    this.Sq.dragProducedCx += 0.06F;
                }
                this.Sq.liftWingLMid *= 0.9F;
                this.Sq.liftWingRMid *= 1.1F;
                this.Sq.liftWingLOut *= 0.9F;
                this.Sq.liftWingROut *= 1.1F;
                if (this.Op(17) == 0.0F) {
                    this.CT.setTrimAileronControl(this.CT.getTrimAileronControl() + 0.25F);
                    if (World.Rnd().nextBoolean()) {
                        this.Sq.liftWingLOut *= 0.95F;
                        this.Sq.liftWingLMid *= 0.95F;
                        this.Sq.liftWingLIn *= 0.95F;
                        this.Sq.liftWingRIn *= 0.75F;
                        this.Sq.liftWingRMid *= 0.75F;
                        this.Sq.liftWingROut *= 0.75F;
                    } else {
                        this.Sq.liftWingROut *= 0.75F;
                        this.Sq.liftWingRMid *= 0.75F;
                        this.Sq.liftWingRIn *= 0.75F;
                        this.Sq.liftWingLIn *= 0.95F;
                        this.Sq.liftWingLMid *= 0.95F;
                        this.Sq.liftWingLOut *= 0.95F;
                    }
                }
                this.cutOp(32);
                // fall through

            case 32:
                this.setCapableOfACM(false);
                if (this.Op(31) == 0.0F) {
                    this.setReadyToDie(true);
                }
                this.Sq.squareElevators *= 0.5F * this.Op(31);
                this.SensPitch *= 0.5F * this.Op(31);
                break;

            case 11:
                if (World.Rnd().nextInt(-1, 8) < this.Skill) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextInt(-1, 16) < this.Skill) {
                    this.setReadyToDie(true);
                }
                if ((this.actor instanceof Scheme2a) || (this.actor instanceof Scheme5) || (this.actor instanceof Scheme7)) {
                    this.Sq.liftKeel *= 0.25F * this.Op(12);
                } else {
                    this.Sq.liftKeel *= 0.0F;
                }
                this.cutOp(15);
                // fall through

            case 15:
                this.setCapableOfACM(false);
                if (World.Rnd().nextInt(-1, 8) < this.Skill) {
                    this.setReadyToReturn(true);
                }
                if ((this.actor instanceof Scheme2a) || (this.actor instanceof Scheme5) || (this.actor instanceof Scheme7)) {
                    this.Sq.squareRudders *= 0.5F;
                    this.SensYaw *= 0.25F;
                } else {
                    this.Sq.squareRudders *= 0.0F;
                    this.SensYaw *= 0.0F;
                }
                break;

            case 12:
                if (World.Rnd().nextInt(-1, 8) < this.Skill) {
                    this.setReadyToReturn(true);
                }
                if (World.Rnd().nextInt(-1, 16) < this.Skill) {
                    this.setReadyToDie(true);
                }
                if ((this.actor instanceof Scheme2a) || (this.actor instanceof Scheme5) || (this.actor instanceof Scheme7)) {
                    this.Sq.liftKeel *= 0.25F * this.Op(12);
                } else {
                    this.Sq.liftKeel *= 0.0F;
                }
                this.cutOp(16);
                // fall through

            case 16:
                this.setCapableOfACM(false);
                if (World.Rnd().nextInt(-1, 8) < this.Skill) {
                    this.setReadyToReturn(true);
                }
                if ((this.actor instanceof Scheme2a) || (this.actor instanceof Scheme5) || (this.actor instanceof Scheme7)) {
                    this.Sq.squareRudders *= 0.5F;
                    this.SensYaw *= 0.25F;
                } else {
                    this.Sq.squareRudders *= 0.0F;
                    this.SensYaw *= 0.0F;
                }
                break;

            case 33:
                this.Sq.liftWingLIn *= 0.25F;
                ((ActorHMesh) this.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.BombGun.class);
                ((ActorHMesh) this.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketBombGun.class);
                this.cut(9, j, actor);
                this.cutOp(34);
                // fall through

            case 34:
                this.setTakenMortalDamage(true, actor);
                this.setReadyToDie(true);
                this.Sq.liftWingLMid *= 0.0F;
                this.Sq.liftWingLIn *= 0.9F;
                ((ActorHMesh) this.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketGun.class);
                this.cutOp(35);
                // fall through

            case 35:
                this.setCapableOfBMP(false, actor);
                this.setCapableOfACM(false);
                this.AS.bWingTipLExists = false;
                this.AS.setStallState(false);
                this.AS.setAirShowState(false);
                this.Sq.liftWingLOut *= 0.0F;
                this.Sq.liftWingLMid *= 0.5F;
                this.Sq.liftWingLOut = 0.0F;
                this.Sq.liftWingLMid = 0.0F;
                this.Sq.liftWingLIn = 0.0F;
                this.CT.bHasAileronControl = false;
                this.cutOp(0);
                // fall through

            case 0:
                if (this.Op(1) == 0.0F) {
                    this.setCapableOfACM(false);
                }
                this.Sq.squareAilerons *= 0.5F;
                this.SensRoll *= 0.5F * this.Op(1);
                break;

            case 36:
                this.Sq.liftWingRIn *= 0.25F;
                ((ActorHMesh) this.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.BombGun.class);
                ((ActorHMesh) this.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketBombGun.class);
                this.cut(10, j, actor);
                this.cutOp(37);
                // fall through

            case 37:
                this.setTakenMortalDamage(true, actor);
                this.setReadyToDie(true);
                this.Sq.liftWingRMid *= 0.0F;
                this.Sq.liftWingRIn *= 0.9F;
                ((ActorHMesh) this.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketGun.class);
                this.cutOp(38);
                // fall through

            case 38:
                this.setCapableOfBMP(false, actor);
                this.setCapableOfACM(false);
                this.AS.bWingTipRExists = false;
                this.AS.setStallState(false);
                this.AS.setAirShowState(false);
                this.Sq.liftWingROut *= 0.0F;
                this.Sq.liftWingRMid *= 0.5F;
                this.Sq.liftWingROut = 0.0F;
                this.Sq.liftWingRMid = 0.0F;
                this.Sq.liftWingRIn = 0.0F;
                this.CT.bHasAileronControl = false;
                this.cutOp(1);
                // fall through

            case 1:
                if (this.Op(0) == 0.0F) {
                    this.setCapableOfACM(false);
                }
                this.Sq.squareAilerons *= 0.5F;
                this.SensRoll *= 0.5F * this.Op(0);
                break;

            case 3:
                if (this.EI.engines.length <= 0) {
                    break;
                }
                this.setCapableOfTaxiing(false);
                if (this.actor instanceof Scheme1) {
                    this.setReadyToDie(true);
                }
                this.setCapableOfACM(false);
                this.EI.engines[0].setEngineDies(this.actor);
                this.Sq.dragEngineCx[0] = 0.15F;
                break;

            case 4:
                if (this.EI.engines.length > 1) {
                    this.setCapableOfTaxiing(false);
                    this.setCapableOfACM(false);
                    this.EI.engines[1].setEngineDies(this.actor);
                    this.Sq.dragEngineCx[1] = 0.15F;
                }
                break;

            case 5:
                if (this.EI.engines.length > 2) {
                    this.setCapableOfTaxiing(false);
                    this.setCapableOfACM(false);
                    this.EI.engines[2].setEngineDies(this.actor);
                    this.Sq.dragEngineCx[2] = 0.15F;
                }
                break;

            case 6:
                if (this.EI.engines.length > 3) {
                    this.setCapableOfTaxiing(false);
                    this.setCapableOfACM(false);
                    this.EI.engines[3].setEngineDies(this.actor);
                    this.Sq.dragEngineCx[3] = 0.15F;
                }
                break;
        }
    }

    private void init_G_Limits() {
        this.UltimateLoad = this.LimitLoad * 1.5F;
        this.ReferenceForce = this.LimitLoad * (this.M.referenceWeight + this.refM);
        this.Negative_G_Limit = -0.5F * this.LimitLoad;
        this.Negative_G_Ultimate = this.Negative_G_Limit * 1.5F;
    }

    public void setDmgLoadLimit(float f, int i) {
        this.setLimitLoad(this.LimitLoad -= f);
        this.damagedParts[i]++;
        for (i = 0; i < this.damagedParts.length; i++) {
            if (this.damagedParts[i] > this.maxDamage) {
                this.cutPart = i;
            }
        }

    }

    public void setUltimateLoad(float f) {
        this.UltimateLoad = f * this.SafetyFactor;
        this.Negative_G_Ultimate = this.UltimateLoad * -0.5F;
    }

    public float getUltimateLoad() {
        return this.UltimateLoad;
    }

    public void setLimitLoad(float f) {
        this.LimitLoad = f;
        this.Negative_G_Limit = -f * 0.5F;
        this.setUltimateLoad(f);
    }

    public float getLimitLoad() {
        return this.LimitLoad;
    }

    public void setSafetyFactor(float f) {
        this.SafetyFactor -= f;
    }

    public float getSafetyFactor() {
        return this.SafetyFactor;
    }

    public float getLoadDiff() {
        return this.getLimitLoad() - this.getOverload();
    }

    public void doRequestFMSFX(int i, int j) {
        if ((this.fmsfxCurrentType == 1) && (i != 1)) {
            return;
        }
        switch (i) {
            default:
                break;

            case 0:
                this.fmsfxCurrentType = i;
                break;

            case 1:
                this.fmsfxCurrentType = i;
                this.fmsfxPrevValue = j * 0.01F;
                if (this.fmsfxPrevValue < 0.05F) {
                    this.fmsfxCurrentType = 0;
                }
                break;

            case 2:
            case 3:
                this.fmsfxCurrentType = i;
                this.fmsfxTimeDisable = Time.current() + (long) ((100 * j) / (this.Wingspan * this.Wingspan));
                break;
        }
    }

    public void setGCenter(float f) {
        this.Arms.GCENTER = f;
    }

    public void setGC_Gear_Shift(float f) {
        this.Arms.GC_GEAR_SHIFT = f;
    }

    public void setFlapsShift(float f) {
        this.Wing.setFlaps(f);
    }

    public static long finger(long l, String s) {
        SectFile sectfile = sectFile(s);
        l = sectfile.finger(l);
        int i = 0;
        do {
            if (i >= 10) {
                break;
            }
            String s1 = "Engine" + i + "Family";
            String s2 = sectfile.get("Engine", s1);
            if (s2 == null) {
                break;
            }
            SectFile sectfile1 = sectFile("FlightModels/" + s2 + ".emd");
            l = sectfile1.finger(l);
            i++;
        } while (true);
        return l;
    }

    // TODO: Following three methods are part of Auto DiffFM mod by Benitomuso
    private static boolean fileExists(String s) {
        try {
            SFSInputStream is = new SFSInputStream(s);
            is.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

// By PAL, different additions for auto DiffFM, DM Debugging, etc.
    public static SectFile sectFile(String s) {

        // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
        if (logLevel == FM_LOGLEVEL_NOT_INITIALIZED) {
            logLevel = Config.cur.ini.get("Mods", "fmloglevel", FM_LOGLEVEL_ERROR);
            flightModelsLogged.clear();
        }
        // TODO: --- log.lst Spam fighting by SAS~Storebror ---

        SectFile sectfile = null;
        String sName = s.toLowerCase();
        // By PAL, from DiffFM, begin
        String sDir = "gui/game/buttons";
        int diffFMSeparatorPos = sName.indexOf(":"); // By PAL, specific FMD (DiffFM)
        if (diffFMSeparatorPos > -1) // It is not valid in first position either
        {
            sDir = sName.substring(diffFMSeparatorPos + 1);
            sName = sName.substring(0, diffFMSeparatorPos);
            // Example: 'FlightModels/F-105D.fmd:F105' for plane FM
            // Example: 'FlightModels/PWJ75:F105.emd' for Engine EMD, appears as
            // Engine0Family PWJ75:F105
            if (sDir.endsWith(".emd")) {
                sName = sName + ".emd";   // FlightModels/PWJ57 + .emd
                sDir = sDir.substring(0, sDir.length() - 4);
            }

            // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
            // System.out.println("*DiffFM '" + sName + "' being loaded from: '" + sDir + "'...");
            logFmLoad("*DiffFM \"{0}\" being loaded from: \"{1}\"...", sName, sDir, FM_LOGLEVEL_DIFFFM | FM_LOGLEVEL_EVERYFM);
            // TODO: --- log.lst Spam fighting by SAS~Storebror ---

        }

        // By PAL, Option A: for Debug, not Encrypted, direct File Names. Only in Single Mission!!!
        if (bDebugFM) {
            // By PAL, if not loading Mission or possitive it is a Single Mission
            // TODO: Fixed by Storebror, original line was "if(Main.cur().mission.isSingle())"
            if (Mission.isSingle()) {
                if (fileExists(sName)) {
                    sectfile = new SectFile(sName, 0);

                    // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
                    // System.out.println("**Debug FM loaded directly from '" + sName + "' File!!!'");
                    logFmLoad("**Debug FM loaded directly from \"{1}\" File!!!", sName, null, FM_LOGLEVEL_DEBUGFM | FM_LOGLEVEL_EVERYFM);
                    // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                    if (bDumpFM) {
                        sectfile.saveFile(sDumpPath + sName);
                    }
                    return sectfile;
                }
            }
        }

        // By PAL, Option C: Check DiffFM or Normal Loading
        try {
            // By PAL, new: Object obj = Property.value(s, "stream", null);
// Object obj = Property.value(sName, "stream", null);

            // TODO: Changed by SAS~Storebror, DiffFM file name added to Property Name if applicable to avoid overlapping buttons/DiffFM
            String propertyName = sName;
            if (diffFMSeparatorPos > -1) {
                propertyName += ":" + sDir;
            }
            Object obj = Property.value(propertyName, "stream", null);

            InputStream inputstream = null;
            if (obj != null) {
                inputstream = (InputStream) obj;
            } else {
                // By PAL, DiffFM begin

                // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// if(bPrintFM)
// {
// System.out.println("FM Requested = " + s);
// System.out.println("*FM sDir = " + sDir);
// System.out.println("*FM sName = " + sName);
// System.out.println("**fmLastDir = " + fmLastDir);
// }
                logFmLoad("FM Requested=" + s + ", FM sDir={1}, FM sName={0}, fmLastDir=" + fmLastDir, sName, sDir, FM_LOGLEVEL_EVERYFM);
                // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                // By PAL, first check if it is the last one or not. If so fmDir should be the same and not null.
                if (!sDir.equalsIgnoreCase(fmLastDir)) // By PAL, if different one, reopen it.
                {
                    fmDir = null;
                    fmLastDir = "EmptyFM"; // To be possitive that I'm not inheriting a wrong name.
                    int t = fmDirNames.size();
                    // By PAL, now check all the previous files I have opened.
                    for (int j = 0; j < t; j++) {
                        String sAux = (String) fmDirNames.get(j);
                        if (sDir.equalsIgnoreCase(sAux)) {
                            fmDir = (InOutStreams) fmDirs.get(j);

                            // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// if(bPrintFM)
// System.out.println("Getting FM from Previous File: '" + sDir + "'");
                            logFmLoad("Getting FM from Previous File: \"{1}\"", sName, sDir, FM_LOGLEVEL_EVERYFM);
                            // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                            fmLastDir = sDir;
                            break;
                        }
                    }
                }

                if (fmDir == null) // By PAL, it wasn't loaded before, load new one and add it to the list.
                {
                    if (fileExists(sDir)) {
                        fmDir = new InOutStreams();
                        fmDir.open(Finger.LongFN(0L, sDir));
                        if (fmDir != null) {
                            fmDirs.add(fmDir); // By PAL, from DiffFM
                            fmDirNames.add(sDir); // By PAL, from DiffFM

                            // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// if(bPrintFM)
// System.out.println("Opening FM from New File: '" + sDir + "'");
                            logFmLoad("Opening FM from New File: \"{1}\"", sName, sDir, FM_LOGLEVEL_EVERYFM);
                            // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                            fmLastDir = sDir;
                        }
                    } else {

                        // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// System.out.println("*Warning: the '" + sDir + "' File Doesn't Exist! Check your Configuration.");
                        logFmLoad("*Warning: the \"{1}\" File Doesn't Exist! Check your Configuration.", null, sDir, FM_LOGLEVEL_ERROR);
                        // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                    }
                }

                // By PAL, if it doesn't exist, load generic FM.
                if (fmDir == null) {
                    // By PAL, if Mission Loading, notify the user about the problem!!!
                    if (Main.cur().missionLoading != null) {
                        BackgroundTask.cancel("Mission Cancelled, Error in FM!!!" + "\n\nThere was a problem loading FM called:\n'" + sName + "'\n\nIt is not present in File:\n'" + sDir + "'");// i18n("miss.ErrorFM"));
                        // errorMessage(s, sDir);
                        return null;
                    }
                    // By PAL, if just Game Loading, load generic FM to continue process.
                    fmDir = new InOutStreams();
                    fmDir.open(Finger.LongFN(0L, "gui/game/buttons"));
                    // By PAL,
                    if (sName.endsWith(".emd")) {
                        sName = "FlightModels/DB-600_Series.emd"; // By PAL, open PlaceHolder FM to avoid 60% CTD!!!

                        // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// System.out.println("**Engine FM will be loaded from placeholder: '" + sName + "'! Not from the expected one.");
                        logFmLoad("**Engine FM will be loaded from placeholder: \"{0}\"! Not from the expected one.", sName, null, FM_LOGLEVEL_ERROR);
                        // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                    } else {
                        sName = "FlightModels/Bf-109F-2.fmd"; // By PAL, open PlaceHolder FM to avoid 60% CTD!!!

                        // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// System.out.println("**Plane FM will be loaded from placeholder: '" + sName + "'! Not from the expected one.");
                        logFmLoad("**Plane FM will be loaded from placeholder: \"{0}\"! Not from the expected one.", sName, null, FM_LOGLEVEL_ERROR);
                        // TODO: --- log.lst Spam fighting by SAS~Storebror ---

                    }
                    sName = sName.toLowerCase();
                }

                // By PAL, now Read it with all the eventual encodings known.
                if (fmDir != null) {
                    // By PAL, if it didn't work with 4.09 / 4.11 / 4.12 Encoding:
                    if (inputstream == null) {
                        inputstream = fmDir.openStream("" + Finger.Int(sName + "d2w0"));
                    }
                    // By PAL, check with 4.10 Encoding:
                    if (inputstream == null) {
                        inputstream = fmDir.openStream("" + Finger.Int(sName + "d2wO"));
                    }
                    // By PAL, check with HSFX Expert Encoding:
                    if (inputstream == null) {
                        inputstream = fmDir.openStream("" + Finger.Int(sName + "d2w5"));
                    }
                }

                if (inputstream == null) // By PAL, Error: no FM, Notify It!
                {
                    System.out.println("Error loading FM called '" + sName + "'!");
                    System.out.println("It Cannot be Loaded from '" + sDir + "' File");
                    if (sName.endsWith(".emd")) {
                        System.out.println("*** This should have been the reason of the 'Explosion in the Air' when mission started ***");
                    } else {
                        System.out.println("*** This was the Reason of your 60% or 70% CTD ***");
                    }
                }
            }
            // By PAL, DiffFM end, now read contents from the stream.
            inputstream.mark(0);
            sectfile = new SectFile(new InputStreamReader(new KryptoInputFilter(inputstream, getSwTbl(Finger.Int(sName + "ogh9"), inputstream.available())), "Cp1252"));
            inputstream.reset();
            if (obj == null) {
                // By PAL, new: Property.set(s, "stream", inputstream);
                // Property.set(sName, "stream", inputstream);
                Property.set(propertyName, "stream", inputstream); // TODO: DiffFM file name added to Property Name if applicable to avoid overlapping buttons/DiffFM names
            }
        } catch (Exception exception) {
            // By PAL, tell the Reason of the Crash

            // TODO: +++ log.lst Spam fighting by SAS~Storebror +++
// System.out.println("FM Loading General Exception!\nWarning, the '" + s + "' cannot be loaded from '" + sDir + "' File");
            logFmLoad("FM Loading General Exception!\nWarning, the \"{0}\" cannot be loaded from \"{1}\" File", s, sDir, FM_LOGLEVEL_ERROR);
            // TODO: --- log.lst Spam fighting by SAS~Storebror ---

        }
        if (bDumpFM) {
            sectfile.saveFile(sDumpPath + sName);
        }
        return sectfile;
    }

    private static int[] getSwTbl(int i, int j) {
        if (i < 0) {
            i = -i;
        }
        if (j < 0) {
            j = -j;
        }
        int k = ((j + (i / 5)) % 16) + 14;
        int l = (j + (i / 19)) % Finger.kTable.length;
        if (k < 0) {
            k = -k % 16;
        }
        if (k < 10) {
            k = 10;
        }
        if (l < 0) {
            l = -l % Finger.kTable.length;
        }
        int ai[] = new int[k];
        for (int i1 = 0; i1 < k; i1++) {
            ai[i1] = Finger.kTable[(l + i1) % Finger.kTable.length];
        }

        return ai;
    }

    // TODO: +++ log.lst Spam fighting by SAS~Storebror +++

    /*
     * New conf.ini parameters for [Mods] Section:
     * fmloglevel = <n>
     *
     * n is an integer value from a binary array:
     *
     * Index 0: Log Errors
     * Index 1: Log "Debug FM" loads
     * Index 2: Log "Diff FM" loads
     * Index 3: Log every flight model
     * Index 7: Log every occurance (unset: Log only once per flight model)
     *
     * Example (binary index)
     * 1 = Log Errors enabled
     * 1 = Log "Debug FM" loads enabled
     * 0 = Log "Diff FM" loads disabled
     * 0 = Log every flight model disabled
     * 0 <don't care>
     * 0 <don't care>
     * 0 <don't care>
     * 0 = Log only once per flight model
     *
     * Hexadecimal representation of the values above = 0x03
     * Decimal: 3
     *
     * conf-ini entry:
     * fmloglevel=3
     *
     * Default value is "fmloglevel=1", this applies when no value is set in conf.ini
     *
     */

    private static void logFmLoad(String logLine, String name, String dir, int mask) {
        if (name == null) {
            name = "";
        }
        if (dir == null) {
            dir = "";
        }
        if ((logLevel & mask) != 0) {
            boolean logMe = false;
            if ((logLevel & FM_LOGLEVEL_EVERYTIME) != 0) {
                logMe = true;
            } else {
                String theFM = name + dir;
                if (!flightModelsLogged.contains(theFM)) {
                    logMe = true;
                    flightModelsLogged.add(theFM);
                }
            }
            if (logMe) {
                System.out.println(MessageFormat.format(logLine, new Object[] { name, dir }));
            }
        }

    }

    private static final int FM_LOGLEVEL_ERROR           = 0x1;
    private static final int FM_LOGLEVEL_DEBUGFM         = 0x2;
    private static final int FM_LOGLEVEL_DIFFFM          = 0x4;
    private static final int FM_LOGLEVEL_EVERYFM         = 0x8;
    private static final int FM_LOGLEVEL_EVERYTIME       = 0x80;                                                                                                                                                                  // Decimal 128
    private static final int FM_LOGLEVEL_NOT_INITIALIZED = -1;

    private static ArrayList flightModelsLogged          = new ArrayList();
    private static int       logLevel                    = FM_LOGLEVEL_NOT_INITIALIZED;
    // TODO: --- log.lst Spam fighting by SAS~Storebror ---

}
