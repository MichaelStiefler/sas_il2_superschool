/*Modified Aircraft class for the SAS Engine Mod*/

package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.ships.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import java.util.AbstractCollection;
import java.util.List;

public class Gear
{
	//TODO: Default Parameters
	// --------------------------------------------------------
    public int pnti[];
    boolean onGround;
    boolean nearGround;
    public float H;
    public float Pitch;
    public float sinkFactor;
    public float springsStiffness;
    public float tailStiffness;
    public boolean bIsSail;
    public int nOfGearsOnGr;
    public int nOfPoiOnGr;
    private int oldNOfGearsOnGr;
    private int oldNOfPoiOnGr;
    private int nP;
    public boolean gearsChanged;
    HierMesh HM;
    public boolean bFlatTopGearCheck;
    public static final int MP = 64;
    public static final double maxVf_gr = 65D;
    public static final double maxVn_gr = 7D;
    public static final double maxVf_wt = 50D;
    public static final double maxVn_wt = 30D;
    public static final double maxVf_wl = 110D;
    public static final double maxVn_wl = 7D;
    public static final double _1_maxVf_gr2 = 0.00023668639053254438D;
    public static final double _1_maxVn_gr2 = 0.020408163265306121D;
    public static final double _1_maxVf_wt2 = 0.00040000000000000002D;
    public static final double _1_maxVn_wt2 = 0.0011111111111111111D;
    public static final double _1_maxVf_wl2 = 8.264462809917356E-005D;
    public static final double _1_maxVn_wl2 = 0.020408163265306121D;
    private static Point3f Pnt[];
    private static boolean clp[] = new boolean[64];
    private Eff3DActor catEff;
    private Eff3DActor clpEff[];
    public Eff3DActor clpGearEff[][] = {
        new Eff3DActor[2], new Eff3DActor[2], new Eff3DActor[2]
    };
    public Eff3DActor clpEngineEff[][];
    private String effectName;
    private boolean bTheBabysGonnaBlow;
    public boolean lgear;
    public boolean rgear;
    public boolean cgear;
    public boolean bIsHydroOperable;
    private boolean bIsOperable;
    public boolean bTailwheelLocked;
    public double gVelocity[] = {
        0.0D, 0.0D, 0.0D
    };
    public float gWheelAngles[] = {
        0.0F, 0.0F, 0.0F
    };
    public float gWheelSinking[] = {
        0.0F, 0.0F, 0.0F
    };
    public float steerAngle;
    public double roughness;
    public float arrestorVAngle;
    public float arrestorVSink;
    public HookNamed arrestorHook;
    public int waterList[];
    private boolean isGearColl;
    private double MassCoeff;
    public boolean bFrontWheel;
    private static AnglesFork steerAngleFork = new AnglesFork();
    private double d;
    private double D;
    private double Vnorm;
    private boolean isWater;
    private boolean bUnderDeck;
    private boolean bIsGear;
    private FlightModel FM;
    private boolean bIsMaster;
    private int fatigue[];
    private Point3d p0;
    private Point3d p1;
    private Loc l0;
    private Vector3d v0;
    private Vector3d tmpV;
    private double fric;
    private double fricF;
    private double fricR;
    private double maxFric;
    public double screenHQ;
    static ClipFilter clipFilter = new ClipFilter();
    private boolean canDoEffect;
    private static Vector3d Normal = new Vector3d();
    private static Vector3d Forward = new Vector3d();
    private static Vector3d Right = new Vector3d();
    private static Vector3d nwForward = new Vector3d();
    private static Vector3d nwRight = new Vector3d();
    private static double NormalVPrj;
    private static double ForwardVPrj;
    private static double RightVPrj;
    private static Vector3d Vnf = new Vector3d();
    private static Vector3d Fd = new Vector3d();
    private static Vector3d Fx = new Vector3d();
    private static Vector3d Vship = new Vector3d();
    private static Vector3d Fv = new Vector3d();
    private static Vector3d Tn = new Vector3d();
    private static Point3d Pn = new Point3d();
    private static Point3d PnT = new Point3d();
    private static Point3d Pship = new Point3d();
    private static Vector3d Vs = new Vector3d();
    private static Matrix4d M4 = new Matrix4d();
    private static PlateFilter plateFilter = new PlateFilter(null);
    private static Point3d corn = new Point3d();
    private static Point3d corn1 = new Point3d();
    private static Loc L = new Loc();
    private static float plateBox[] = new float[6];
    private static boolean bPlateExist = false;
    private static boolean bPlateGround = false;
    private static String ZUTI_SKIS_AC_CLASSES[] = {
        "DXXI_SARJA3_EARLY", "DXXI_SARJA3_LATE", "DXXI_SARJA3_SARVANTO", "DXXI_SARJA4", "R_5_SKIS", "BLENHEIM1", "BLENHEIM4", "GLADIATOR1", "GLADIATOR1J8A", "GLADIATOR2", 
        "I_15BIS_SKIS", "I_16TYPE5_SKIS", "I_16TYPE6_SKIS"
    };
	// --------------------------------------------------------

	// TODO: New parameters
	// --------------------------------------------------------
    private double dCatapultOffsetX;
    private double dCatapultOffsetY;
    private double dCatapultYaw;
    private double dCatapultOffsetX2;
    private double dCatapultOffsetY2;
    private double dCatapultYaw2;
    private boolean bCatapultAllow;
    private boolean bCatapultBoost;
    private float catapultPower;
    private float catapultPowerJets;
    private boolean bCatapultAI;
    private boolean bCatapultAllowAI;
    private boolean bCatapultSet;
    private boolean bAlreadySetCatapult;
    private boolean bCatapultAI_CVE;
    private boolean bCatapultAI_EssexClass;
    private boolean bCatapultAI_Illustrious;
    private boolean bCatapultAI_GrafZep;
    private boolean bCatapultAI_CVL;
    private boolean bCatapultAI_ClemenceauClass;
    private boolean bStandardDeckCVL;
    private boolean bSteamCatapult;
    private boolean zutiHasPlaneSkisOnWinterCamo;
    public ZutiAirfieldPoint zutiCurrentZAP;
	// --------------------------------------------------------
    static class ClipFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d)
        {
            return actor instanceof BigshipGeneric;
        }

        ClipFilter()
        {
        }
    }

    private static class PlateFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d)
        {
            if(!(actor instanceof Plate))
                return true;
            Mesh mesh = ((ActorMesh)actor).mesh();
            mesh.getBoundBox(Gear.plateBox);
            Gear.corn1.set(Gear.corn);
            Loc loc = actor.pos.getAbs();
            loc.transformInv(Gear.corn1);
            if((double)(Gear.plateBox[0] - 2.5F) < ((Tuple3d) (Gear.corn1)).x && ((Tuple3d) (Gear.corn1)).x < (double)(Gear.plateBox[3] + 2.5F) && (double)(Gear.plateBox[1] - 2.5F) < ((Tuple3d) (Gear.corn1)).y && ((Tuple3d) (Gear.corn1)).y < (double)(Gear.plateBox[4] + 2.5F))
            {
                Gear.bPlateExist = true;
                Gear.bPlateGround = ((Plate)actor).isGround();
            }
            return true;
        }

        private PlateFilter()
        {
        }

        PlateFilter(PlateFilter platefilter)
        {
            this();
        }
    }


    public Gear()
    {
        onGround = false;
        nearGround = false;
        nOfGearsOnGr = 0;
        nOfPoiOnGr = 0;
        oldNOfGearsOnGr = 0;
        oldNOfPoiOnGr = 0;
        nP = 0;
        gearsChanged = false;
        clpEff = new Eff3DActor[64];
        clpEngineEff = new Eff3DActor[8][2];
        effectName = new String();
        bTheBabysGonnaBlow = false;
        lgear = true;
        rgear = true;
        cgear = true;
        bIsHydroOperable = true;
        bIsOperable = true;
        bTailwheelLocked = false;
        steerAngle = 0.0F;
        roughness = 0.5D;
        arrestorVAngle = 0.0F;
        arrestorVSink = 0.0F;
        arrestorHook = null;
        waterList = null;
        isGearColl = false;
        MassCoeff = 1.0D;
        bFrontWheel = false;
        bUnderDeck = false;
        bIsMaster = true;
        fatigue = new int[2];
        p0 = new Point3d();
        p1 = new Point3d();
        l0 = new Loc();
        v0 = new Vector3d();
        tmpV = new Vector3d();
        fric = 0.0D;
        fricF = 0.0D;
        fricR = 0.0D;
        maxFric = 0.0D;
        screenHQ = 0.0D;
        canDoEffect = true;
        zutiHasPlaneSkisOnWinterCamo = false;
        zutiCurrentZAP = null;
        dCatapultOffsetX = 0.0D;
        dCatapultOffsetY = 0.0D;
        dCatapultYaw = 0.0D;
        dCatapultOffsetX2 = 0.0D;
        dCatapultOffsetY2 = 0.0D;
        dCatapultYaw2 = 0.0D;
        bCatapultAllow = true;
        bCatapultBoost = false;
        bCatapultAI = true;
        bCatapultAllowAI = true;
        bCatapultSet = false;
        bAlreadySetCatapult = false;
        bCatapultAI_CVE = true;
        bCatapultAI_EssexClass = true;
        bCatapultAI_Illustrious = true;
        bCatapultAI_GrafZep = true;
        bCatapultAI_CVL = true;
        bCatapultAI_ClemenceauClass = true;
        bStandardDeckCVL = false;
        bSteamCatapult = false;
        onGround = false;
        nearGround = false;
        nOfGearsOnGr = 0;
        nOfPoiOnGr = 0;
        oldNOfGearsOnGr = 0;
        oldNOfPoiOnGr = 0;
        nP = 0;
        gearsChanged = false;
        catEff = null;
        clpEff = new Eff3DActor[64];
        clpEngineEff = new Eff3DActor[8][2];
        effectName = new String();
        bTheBabysGonnaBlow = false;
        lgear = true;
        rgear = true;
        cgear = true;
        bIsHydroOperable = true;
        bIsOperable = true;
        bTailwheelLocked = false;
        steerAngle = 0.0F;
        roughness = 0.5D;
        arrestorVAngle = 0.0F;
        arrestorVSink = 0.0F;
        arrestorHook = null;
        waterList = null;
        isGearColl = false;
        MassCoeff = 1.0D;
        bFrontWheel = false;
        bUnderDeck = false;
        bIsMaster = true;
        fatigue = new int[2];
        p0 = new Point3d();
        p1 = new Point3d();
        l0 = new Loc();
        v0 = new Vector3d();
        tmpV = new Vector3d();
        fric = 0.0D;
        fricF = 0.0D;
        fricR = 0.0D;
        maxFric = 0.0D;
        screenHQ = 0.0D;
        canDoEffect = true;
        if(Mission.cur().sectFile().get("Mods", "CatapultAllow", 1) == 0)
        {
            bCatapultAllow = false;
            bCatapultAllowAI = false;
        }
        if(Mission.cur().sectFile().get("Mods", "CatapultBoost", 1) == 1 && !Mission.isCoop())
            bCatapultBoost = true;
        if(Config.cur.ini.get("Mods", "CatapultAllowAI", 1) == 0)
            bCatapultAllowAI = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAllowAI", 1) == 0)
            bCatapultAllowAI = false;
        if(Config.cur.ini.get("Mods", "CatapultAI_CVE", 1) == 0)
            bCatapultAI_CVE = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAI_CVE", 1) == 0)
            bCatapultAI_CVE = false;
        if(Config.cur.ini.get("Mods", "CatapultAI_EssexClass", 1) == 0)
            bCatapultAI_EssexClass = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAI_EssexClass", 1) == 0)
            bCatapultAI_EssexClass = false;
        if(Config.cur.ini.get("Mods", "CatapultAI_Illustrious", 1) == 0)
            bCatapultAI_Illustrious = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAI_Illustrious", 1) == 0)
            bCatapultAI_Illustrious = false;
        if(Config.cur.ini.get("Mods", "CatapultAI_GrafZep", 1) == 0)
            bCatapultAI_GrafZep = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAI_GrafZep", 1) == 0)
            bCatapultAI_GrafZep = false;
        if(Config.cur.ini.get("Mods", "CatapultAI_CVL", 1) == 0)
            bCatapultAI_CVL = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAI_CVL", 1) == 0)
            bCatapultAI_CVL = false;
        if(Config.cur.ini.get("Mods", "CatapultAI_ClemenceauClass", 1) == 0)
            bCatapultAI_ClemenceauClass = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAI_ClemenceauClass", 1) == 0)
            bCatapultAI_ClemenceauClass = false;
        if(Config.cur.ini.get("Mods", "StandardDeckCVL", 0) == 1)
            bStandardDeckCVL = true;
        if(Mission.cur().sectFile().get("Mods", "StandardDeckCVL", 0) == 1)
            bStandardDeckCVL = true;
    }

    public boolean onGround()
    {
        return onGround;
    }

    public boolean nearGround()
    {
        return nearGround;
    }

    public boolean isHydroOperable()
    {
        return bIsHydroOperable;
    }

    public void setHydroOperable(boolean bool)
    {
        bIsHydroOperable = bool;
    }

    public boolean isOperable()
    {
        return bIsOperable;
    }

    public void setOperable(boolean bool)
    {
        bIsOperable = bool;
    }

    public float getSteeringAngle()
    {
        return steerAngle;
    }

    public boolean isUnderDeck()
    {
        return bUnderDeck;
    }

    public boolean getWheelsOnGround()
    {
        boolean bool = isGearColl;
        isGearColl = false;
        return bool;
    }

    public void set(HierMesh hiermesh)
    {
        HM = hiermesh;
        int i;
        if(pnti == null)
        {
            for(i = 0; i < 61 && HM.hookFind("_Clip" + s(i)) >= 0; i++);
            pnti = new int[i + 3];
            pnti[0] = HM.hookFind("_ClipLGear");
            pnti[1] = HM.hookFind("_ClipRGear");
            pnti[2] = HM.hookFind("_ClipCGear");
            for(i = 3; i < pnti.length; i++)
                pnti[i] = HM.hookFind("_Clip" + s(i - 3));

        }
        if(arrestorHook == null && hiermesh.hookFind("_ClipAGear") != -1)
            arrestorHook = new HookNamed(hiermesh, "_ClipAGear");
        i = pnti[2];
        if(i > 0)
        {
            HM.hookMatrix(i, M4);
            if(M4.m03 > -1D)
                bFrontWheel = true;
        }
    }

    public void computePlaneLandPose(FlightModel flightmodel)
    {
        FM = flightmodel;
        if(H == 0.0F || Pitch == 0.0F)
        {
            for(int i = 0; i < 3; i++)
                if(pnti[i] < 0)
                    return;

            HM.hookMatrix(pnti[2], M4);
            double d = M4.m03;
            double d_0_ = M4.m23;
            HM.hookMatrix(pnti[0], M4);
            double d_1_ = M4.m03;
            double d_2_ = M4.m23;
            HM.hookMatrix(pnti[1], M4);
            d_1_ = (d_1_ + M4.m03) * 0.5D;
            d_2_ = (d_2_ + M4.m23) * 0.5D;
            double d_3_ = d_1_ - d;
            double d_4_ = d_2_ - d_0_;
            Pitch = -Geom.RAD2DEG((float)Math.atan2(d_4_, d_3_));
            if(d_3_ < 0.0D)
                Pitch += 180F;
            Line2f line2f = new Line2f();
            line2f.set(new Point2f((float)d_1_, (float)d_2_), new Point2f((float)d, (float)d_0_));
            H = line2f.distance(new Point2f(0.0F, 0.0F));
            H -= (double)((((FlightModelMain) (FM)).M.massEmpty + ((FlightModelMain) (FM)).M.maxFuel + ((FlightModelMain) (FM)).M.maxNitro) * Atmosphere.g()) / 2700000D;
        }
    }

    public void set(Gear gear_5_)
    {
        if(gear_5_.pnti != null)
        {
            pnti = new int[gear_5_.pnti.length];
            if(gear_5_.waterList != null)
            {
                waterList = new int[gear_5_.waterList.length];
                for(int i = 0; i < waterList.length; i++)
                    waterList[i] = gear_5_.waterList[i];

            }
            for(int i = 0; i < pnti.length; i++)
                pnti[i] = gear_5_.pnti[i];

            bIsSail = gear_5_.bIsSail;
            sinkFactor = gear_5_.sinkFactor;
            springsStiffness = gear_5_.springsStiffness;
            H = gear_5_.H;
            Pitch = gear_5_.Pitch;
            bFrontWheel = gear_5_.bFrontWheel;
        }
    }

    public void ground(FlightModel flightmodel, boolean bool)
    {
        ground(flightmodel, bool, false);
    }

    public void ground(FlightModel flightmodel, boolean flag, boolean flag1)
    {
        int catapultAdd = 0;
        FM = flightmodel;
        bIsMaster = flag;
        onGround = flag1;
        ((FlightModelMain) (FM)).Vrel.x = -((Tuple3d) (((FlightModelMain) (FM)).Vwld)).x;
        ((FlightModelMain) (FM)).Vrel.y = -((Tuple3d) (((FlightModelMain) (FM)).Vwld)).y;
        ((FlightModelMain) (FM)).Vrel.z = -((Tuple3d) (((FlightModelMain) (FM)).Vwld)).z;
        for(int i = 0; i < 2; i++)
            if(fatigue[i] > 0)
                fatigue[i]--;

        Pn.set(((FlightModelMain) (FM)).Loc);
        Pn.z = Engine.cur.land.HQ(((Tuple3d) (Pn)).x, ((Tuple3d) (Pn)).y);
        double d1 = ((Tuple3d) (Pn)).z;
        screenHQ = d1;
        if(((Tuple3d) (((FlightModelMain) (FM)).Loc)).z - d1 > 50D && !bFlatTopGearCheck)
        {
            turnOffEffects();
            arrestorVSink = -50F;
            return;
        }
        isWater = Engine.cur.land.isWater(((Tuple3d) (Pn)).x, ((Tuple3d) (Pn)).y);
        if(isWater)
            roughness = 0.5D;
        Engine.cur.land.EQN(((Tuple3d) (Pn)).x, ((Tuple3d) (Pn)).y, Normal);
        bUnderDeck = false;
        BigshipGeneric bigshipgeneric = null;
        if(bFlatTopGearCheck)
        {
            corn.set(((FlightModelMain) (FM)).Loc);
            corn1.set(((FlightModelMain) (FM)).Loc);
            corn1.z -= 20D;
            Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
            if(actor instanceof BigshipGeneric)
            {
                Pn.z = ((Tuple3d) (Pship)).z + 0.5D;
                d1 = ((Tuple3d) (Pn)).z;
                isWater = false;
                bUnderDeck = true;
                actor.getSpeed(Vship);
                ((FlightModelMain) (FM)).Vrel.add(Vship);
                bigshipgeneric = (BigshipGeneric)actor;
                bigshipgeneric.addRockingSpeed(((FlightModelMain) (FM)).Vrel, Normal, ((FlightModelMain) (FM)).Loc);
                if(((FlightModelMain) (flightmodel)).AS.isMaster() && bigshipgeneric.getAirport() != null && ((FlightModelMain) (flightmodel)).CT.bHasArrestorControl)
                {
                    if(!bigshipgeneric.isTowAircraft((Aircraft)((Interpolate) (flightmodel)).actor) && ((FlightModelMain) (FM)).Vrel.lengthSquared() > 10D && ((FlightModelMain) (flightmodel)).CT.getArrestor() > 0.1F)
                    {
                        bigshipgeneric.requestTowAircraft((Aircraft)((Interpolate) (flightmodel)).actor);
                        if(bigshipgeneric.isTowAircraft((Aircraft)((Interpolate) (flightmodel)).actor))
                        {
                            ((FlightModelMain) (flightmodel)).AS.setFlatTopString(bigshipgeneric, bigshipgeneric.towPortNum);
                            if((FM instanceof RealFlightModel) && bIsMaster && ((RealFlightModel)FM).isRealMode())
                                ((RealFlightModel)FM).producedShakeLevel = 5F;
                            ((Aircraft)((Interpolate) (flightmodel)).actor).sfxTow();
                        }
                    }
                    if(bigshipgeneric.isTowAircraft((Aircraft)((Interpolate) (flightmodel)).actor) && ((FlightModelMain) (FM)).Vrel.lengthSquared() < 1.0D && World.Rnd().nextFloat() < 0.008F)
                    {
                        bigshipgeneric.requestDetowAircraft((Aircraft)((Interpolate) (flightmodel)).actor);
                        ((FlightModelMain) (flightmodel)).AS.setFlatTopString(bigshipgeneric, -1);
                    }
                }
                if(bigshipgeneric.isTowAircraft((Aircraft)((Interpolate) (flightmodel)).actor))
                {
                    int k = bigshipgeneric.towPortNum;
                    Point3d apoint3d[] = bigshipgeneric.getShipProp().propAirport.towPRel;
                    ((Actor) (bigshipgeneric)).pos.getAbs(l0);
                    l0.transform(apoint3d[k * 2], p0);
                    l0.transform(apoint3d[k * 2 + 1], p1);
                    p0.x = 0.5D * (((Tuple3d) (p0)).x + ((Tuple3d) (p1)).x);
                    p0.y = 0.5D * (((Tuple3d) (p0)).y + ((Tuple3d) (p1)).y);
                    p0.z = 0.5D * (((Tuple3d) (p0)).z + ((Tuple3d) (p1)).z);
                    ((Interpolate) (flightmodel)).actor.pos.getAbs(l0);
                    l0.transformInv(p0);
                    l0.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    bigshipgeneric.towHook.computePos(((Interpolate) (flightmodel)).actor, new Loc(l0), l0);
                    v0.sub(p0, l0.getPoint());
                    if(((Tuple3d) (v0)).x > 0.0D)
                    {
                        if(bigshipgeneric.isTowAircraft((Aircraft)((Interpolate) (flightmodel)).actor))
                        {
                            bigshipgeneric.requestDetowAircraft((Aircraft)((Interpolate) (flightmodel)).actor);
                            ((FlightModelMain) (flightmodel)).AS.setFlatTopString(bigshipgeneric, -1);
                        }
                    } else
                    {
                        tmpV.set(((FlightModelMain) (FM)).Vrel);
                        ((Interpolate) (flightmodel)).actor.pos.getAbsOrient().transformInv(tmpV);
                        if(((Tuple3d) (tmpV)).x < 0.0D)
                        {
                            double d3 = v0.length();
                            v0.normalize();
                            arrestorVAngle = (float)Math.toDegrees(Math.asin(((Tuple3d) (v0)).z));
                            double Fr = 1000D;
                            if(Mission.curYear() > 1948 || (((Interpolate) (FM)).actor instanceof TypeSupersonic) || (((Interpolate) (FM)).actor instanceof TypeFastJet))
                                Fr = 3000D;
                            if(((FlightModelMain) (FM)).Vmin > 190F)
                                Fr += (((FlightModelMain) (FM)).Vmin - 190F) * 50F;
                            if(((FlightModelMain) (FM)).M.getFullMass() > 4500F)
                                Fr += ((FlightModelMain) (FM)).M.getFullMass() - 4500F;
                            v0.scale(Fr * d3);
                            ((FlightModelMain) (flightmodel)).GF.add(v0);
                            v0.scale(0.29999999999999999D);
                            v0.cross(l0.getPoint(), v0);
                            ((FlightModelMain) (flightmodel)).GM.add(v0);
                        }
                    }
                } else
                {
                    arrestorVAngle = 0.0F;
                }
            }
        }
        if(((FlightModelMain) (flightmodel)).CT.bHasArrestorControl)
        {
            ((Interpolate) (flightmodel)).actor.pos.getAbs(Aircraft.tmpLoc1);
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            arrestorHook.computePos(((Interpolate) (flightmodel)).actor, Aircraft.tmpLoc1, loc);
            arrestorVSink = (float)(((Tuple3d) (Pn)).z - ((Tuple3d) (loc.getPoint())).z);
        }
        Fd.set(((FlightModelMain) (FM)).Vrel);
        Vnf.set(Normal);
        ((FlightModelMain) (FM)).Or.transformInv(Normal);
        ((FlightModelMain) (FM)).Or.transformInv(Fd);
        Fd.normalize();
        Pn.x = 0.0D;
        Pn.y = 0.0D;
        Pn.z -= ((Tuple3d) (((FlightModelMain) (FM)).Loc)).z;
        ((FlightModelMain) (FM)).Or.transformInv(Pn);
        D = -Normal.dot(Pn);
        if(!bIsMaster)
            D -= 0.014999999999999999D;
        if(D > 50D)
        {
            nearGround = false;
            return;
        }
        nearGround = true;
        gWheelSinking[0] = gWheelSinking[1] = gWheelSinking[2] = 0.0F;
        for(int j = 0; j < pnti.length; j++)
        {
            int l = pnti[j];
            if(l <= 0)
            {
                Pnt[j].set(0.0F, 0.0F, 0.0F);
            } else
            {
                HM.hookMatrix(l, M4);
                Pnt[j].set((float)M4.m03, (float)M4.m13, (float)M4.m23);
            }
        }

        nP = 0;
        nOfGearsOnGr = 0;
        nOfPoiOnGr = 0;
        tmpV.set(0.0D, 1.0D, 0.0D);
        Forward.cross(tmpV, Normal);
        Forward.normalize();
        Right.cross(Normal, Forward);
        for(int i1 = 0; i1 < pnti.length; i1++)
        {
            clp[i1] = false;
            if(i1 <= 2)
            {
                bIsGear = true;
                if(i1 == 0 && (!lgear || ((FlightModelMain) (FM)).CT.getGear() < 0.01F) || i1 == 1 && (!rgear || ((FlightModelMain) (FM)).CT.getGear() < 0.01F) || i1 == 2 && !cgear)
                    continue;
            } else
            {
                bIsGear = false;
            }
            PnT.set(Pnt[i1]);
            d = Normal.dot(PnT) + D;
            Fx.set(Fd);
            MassCoeff = 0.00040000000000000002D * (double)((FlightModelMain) (FM)).M.getFullMass();
            if(MassCoeff > 1.0D)
                MassCoeff = 1.0D;
            if(d < 0.0D)
            {
                if(!testPropellorCollision(i1) || (isWater ? !testWaterCollision(i1) : bIsGear ? !testGearCollision(i1) : !testNonGearCollision(i1)))
                    continue;
                clp[i1] = true;
                nP++;
            } else
            {
                if(d >= 0.10000000000000001D || isWater || bIsGear || !testNonGearCollision(i1))
                    continue;
                clp[i1] = true;
                nP++;
            }
            PnT.x += ((FlightModelMain) (FM)).Arms.GC_GEAR_SHIFT;
            Fx.cross(PnT, Tn);
            Fv.set(Fx);
            if(bIsSail && bInWaterList(i1))
            {
                tmpV.scale(fricF * 0.5D, Forward);
                Tn.add(tmpV);
                tmpV.scale(fricR * 0.5D, Right);
                Tn.add(tmpV);
            }
            if(bIsMaster)
            {
                ((FlightModelMain) (FM)).GF.add(Tn);
                ((FlightModelMain) (FM)).GM.add(Fx);
            }
        }

        if(oldNOfGearsOnGr != nOfGearsOnGr || oldNOfPoiOnGr != nOfPoiOnGr)
            gearsChanged = true;
        else
            gearsChanged = false;
        oldNOfGearsOnGr = nOfGearsOnGr;
        oldNOfPoiOnGr = nOfPoiOnGr;
        onGround = nP > 0;
        if(Config.isUSE_RENDER())
            drawEffects();
        if(bIsMaster)
        {
            FM.canChangeBrakeShoe = false;
            BigshipGeneric bigshipgeneric1 = bigshipgeneric;
            if(bigshipgeneric1 != null)
                FM.brakeShoeLastCarrier = bigshipgeneric1;
            else
            if(Mission.isCoop() && !Mission.isServer() && Actor.isAlive(FM.brakeShoeLastCarrier) && Time.current() < 60000L)
                bigshipgeneric1 = (BigshipGeneric)FM.brakeShoeLastCarrier;
            if(bigshipgeneric1 != null)
            {
                if(FM.brakeShoe)
                {
                    if(!isAnyDamaged())
                    {
                        L.set(FM.brakeShoeLoc);
                        L.add(FM.brakeShoeLastCarrier.pos.getAbs());
                        ((FlightModelMain) (FM)).Loc.set(L.getPoint());
                        ((FlightModelMain) (FM)).Or.set(L.getOrient());
                        //TODO:
                        if(!bAlreadySetCatapult)
                        {
                            bCatapultSet = setCatapultOffset(bigshipgeneric1, new SectFile("com/maddox/il2/objects/Catapults.ini"));
                            bAlreadySetCatapult = true;
                        }
                        if(bCatapultAllow && !((FlightModelMain) (FM)).EI.getCatapult() && bCatapultSet)
                        {
                            ((Actor) (bigshipgeneric1.getAirport())).pos.getCurrent();
                            Point3d point3d = L.getPoint();
                            CellAirField cellairfield = bigshipgeneric1.getCellTO();
                            double d4 = -((Tuple3d) (cellairfield.leftUpperCorner())).x - dCatapultOffsetX;
                            double d6 = ((Tuple3d) (cellairfield.leftUpperCorner())).y - dCatapultOffsetY;
                            Loc loc2 = new Loc(d6, d4, 0.0D, 0.0F, 0.0F, 0.0F);
                            loc2.add(FM.brakeShoeLastCarrier.pos.getAbs());
                            Point3d point3d1 = loc2.getPoint();
                            double d8 = Math.abs((((Tuple3d) (point3d)).x - ((Tuple3d) (point3d1)).x) * (((Tuple3d) (point3d)).x - ((Tuple3d) (point3d1)).x) + (((Tuple3d) (point3d)).y - ((Tuple3d) (point3d1)).y) * (((Tuple3d) (point3d)).y - ((Tuple3d) (point3d1)).y));
                            if(d8 <= 100D)
                            {
                                L.set(d6, d4, FM.brakeShoeLoc.getZ(), FM.brakeShoeLoc.getAzimut(), FM.brakeShoeLoc.getTangage(), FM.brakeShoeLoc.getKren());
                                L.add(FM.brakeShoeLastCarrier.pos.getAbs());
                                ((FlightModelMain) (FM)).Loc.set(L.getPoint());
                                ((FlightModelMain) (FM)).Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw, Pitch, FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
                                ((Interpolate) (FM)).actor.pos.setAbs(((FlightModelMain) (FM)).Loc, ((FlightModelMain) (FM)).Or);
                                if(bCatapultBoost)
                                {
                                	//TODO:
                                    if(!bSteamCatapult && ((((Interpolate) (FM)).actor instanceof TypeSupersonic) || (((Interpolate) (FM)).actor instanceof TypeFastJet)))
                                        ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPowerJets, 0);
                                    else
                                    if(((((Interpolate) (FM)).actor instanceof TypeSupersonic) || (((Interpolate) (FM)).actor instanceof TypeFastJet)))
                                    {
                                        catapultAdd = (int)Math.ceil(((((FlightModelMain) (FM)).M.getFullMass() - ((FlightModelMain) (FM)).M.massEmpty) / 1000F) * 2.0F);
                                        ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPowerJets + (float)catapultAdd, 0);
                                    } else
                                    if(!bSteamCatapult)
                                    {
                                        ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPower, 0);
                                    } else
                                    {
                                        catapultAdd = (int)Math.ceil(((((FlightModelMain) (FM)).M.getFullMass() - ((FlightModelMain) (FM)).M.massEmpty) / 1000F) * 2.0F);
                                        ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPower + (float)catapultAdd, 0);
                                    }
                                } else
                                {
                                    ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), 10F, 0);
                                }
                                FM.brakeShoeLoc.set(((Interpolate) (FM)).actor.pos.getAbs());
                                FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getAbs());
                            } else
                            if(dCatapultOffsetX2 != 0.0D)
                            {
                                double d5 = -((Tuple3d) (cellairfield.leftUpperCorner())).x - dCatapultOffsetX2;
                                double d7 = ((Tuple3d) (cellairfield.leftUpperCorner())).y - dCatapultOffsetY2;
                                loc2.set(d7, d5, 0.0D, 0.0F, 0.0F, 0.0F);
                                loc2.add(FM.brakeShoeLastCarrier.pos.getAbs());
                                Point3d point3d2 = loc2.getPoint();
                                double d9 = Math.abs((((Tuple3d) (point3d)).x - ((Tuple3d) (point3d2)).x) * (((Tuple3d) (point3d)).x - ((Tuple3d) (point3d2)).x) + (((Tuple3d) (point3d)).y - ((Tuple3d) (point3d2)).y) * (((Tuple3d) (point3d)).y - ((Tuple3d) (point3d2)).y));
                                if(d9 <= 100D)
                                {
                                    L.set(d7, d5, FM.brakeShoeLoc.getZ(), FM.brakeShoeLoc.getAzimut(), FM.brakeShoeLoc.getTangage(), FM.brakeShoeLoc.getKren());
                                    L.add(FM.brakeShoeLastCarrier.pos.getAbs());
                                    ((FlightModelMain) (FM)).Loc.set(L.getPoint());
                                    ((FlightModelMain) (FM)).Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw2, Pitch, FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
                                    ((Interpolate) (FM)).actor.pos.setAbs(((FlightModelMain) (FM)).Loc, ((FlightModelMain) (FM)).Or);
                                    if(bCatapultBoost)
                                    {
                                        if(!bSteamCatapult && ((((Interpolate) (FM)).actor instanceof TypeSupersonic) || (((Interpolate) (FM)).actor instanceof TypeFastJet)) && Mission.curYear() > 1945 && Mission.curYear() < 1953)
                                            ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPowerJets, 1);
                                        else
                                        if(((((Interpolate) (FM)).actor instanceof TypeSupersonic) || (((Interpolate) (FM)).actor instanceof TypeFastJet)) && Mission.curYear() > 1952)
                                        {
                                            catapultAdd = (int)Math.ceil(((((FlightModelMain) (FM)).M.getFullMass() - ((FlightModelMain) (FM)).M.massEmpty) / 1000F) * 2.0F);
                                            ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPowerJets + (float)catapultAdd, 1);
                                        } else
                                        if(!bSteamCatapult)
                                        {
                                            ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPower, 1);
                                        } else
                                        {
                                            catapultAdd = (int)Math.ceil(((((FlightModelMain) (FM)).M.getFullMass() - ((FlightModelMain) (FM)).M.massEmpty) / 1000F) * 2.0F);
                                            ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), catapultPower + (float)catapultAdd, 1);
                                        }
                                    } else
                                    {
                                        ((FlightModelMain) (FM)).EI.setCatapult(((FlightModelMain) (FM)).M.getFullMass(), 10F, 1);
                                    }
                                    FM.brakeShoeLoc.set(((Interpolate) (FM)).actor.pos.getAbs());
                                    FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getAbs());
                                }
                            }
                        } else
                        if(((FlightModelMain) (FM)).EI.getCatapult())
                            ((FlightModelMain) (FM)).EI.resetCatapultTime();
                        FM.brakeShoeLastCarrier.getSpeed(((FlightModelMain) (FM)).Vwld);
                        ((FlightModelMain) (FM)).Vrel.set(0.0D, 0.0D, 0.0D);
                        for(int j1 = 0; j1 < 3; j1++)
                            gVelocity[j1] = 0.0D;

                        onGround = true;
                        FM.canChangeBrakeShoe = true;
                    } else
                    {
                        FM.brakeShoe = false;
                    }
                } else
                {
                    if(((FlightModelMain) (FM)).EI.getCatapult())
                        if(((FlightModelMain) (FM)).EI.getCatapultNumber() == 0)
                            ((FlightModelMain) (FM)).Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw, ((FlightModelMain) (FM)).Or.getPitch(), FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
                        else
                        if(((FlightModelMain) (FM)).EI.getCatapultNumber() == 1)
                            ((FlightModelMain) (FM)).Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw2, ((FlightModelMain) (FM)).Or.getPitch(), FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
                    if(nOfGearsOnGr == 3 && nP == 3 && ((FlightModelMain) (FM)).Vrel.lengthSquared() < 1.0D)
                    {
                        FM.brakeShoeLoc.set(((Interpolate) (FM)).actor.pos.getCurrent());
                        FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getCurrent());
                        FM.canChangeBrakeShoe = true;
                    }
                    bAlreadySetCatapult = false;
                }
            } else
            if(nOfGearsOnGr == 3 && nP == 3 && ((FlightModelMain) (FM)).Vrel.lengthSquared() < 1.5D)
            {
                FM.brakeShoeLoc.set(((Interpolate) (FM)).actor.pos.getCurrent());
                ((FlightModelMain) (FM)).Vrel.set(0.0D, 0.0D, 0.0D);
                for(int k1 = 0; k1 < 3; k1++)
                    gVelocity[k1] = 0.0D;

                FM.canChangeBrakeShoe = true;
                onGround = true;
                if(FM.brakeShoe)
                {
                    ((FlightModelMain) (FM)).GF.set(0.0D, 0.0D, 0.0D);
                    ((FlightModelMain) (FM)).GM.set(0.0D, 0.0D, 0.0D);
                    ((FlightModelMain) (FM)).Vwld.set(0.0D, 0.0D, 0.0D);
                }
            }
        }
        if(!bIsMaster)
            return;
        if(onGround && !isWater)
            processingCollisionEffect();
        double d2 = Engine.cur.land.HQ_ForestHeightHere(((Tuple3d) (((FlightModelMain) (FM)).Loc)).x, ((Tuple3d) (((FlightModelMain) (FM)).Loc)).y);
        if(d2 > 0.0D && ((Tuple3d) (((FlightModelMain) (FM)).Loc)).z <= d1 + d2 && ((Aircraft)((Interpolate) (FM)).actor).isEnablePostEndAction(0.0D))
            ((Aircraft)((Interpolate) (FM)).actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
    }

    private boolean testNonGearCollision(int i)
    {
        nOfPoiOnGr++;
        Vs.set(((FlightModelMain) (FM)).Vrel);
        Vs.scale(-1D);
        ((FlightModelMain) (FM)).Or.transformInv(Vs);
        tmpV.set(Pnt[i]);
        tmpV.cross(FM.getW(), tmpV);
        Vs.add(tmpV);
        ForwardVPrj = Forward.dot(Vs);
        NormalVPrj = Normal.dot(Vs);
        RightVPrj = Right.dot(Vs);
        if(NormalVPrj > 0.0D)
        {
            NormalVPrj -= 3D;
            if(NormalVPrj < 0.0D)
                NormalVPrj = 0.0D;
        }
        double d = 1.0D;
        double d_11_ = this.d - 0.059999999999999998D;
        double d_12_ = this.d + 0.040000000000000008D;
        if(d_11_ > 0.0D)
            d_11_ = 0.0D;
        if(d_11_ < -2D)
            d_11_ = -2D;
        if(d_12_ > 0.0D)
            d_12_ = 0.0D;
        if(d_12_ < -0.25D)
            d_12_ = -0.25D;
        d = Math.max(-120000D * d_11_, -360000D * d_12_);
        d *= MassCoeff;
        Tn.scale(d, Normal);
        fric = -40000D * NormalVPrj;
        if(fric > 100000D)
            fric = 100000D;
        if(fric < -100000D)
            fric = -100000D;
        tmpV.scale(fric, Normal);
        Tn.add(tmpV);
        double d_13_ = 1.0D - (0.5D * (double)Math.abs(((Tuple3f) (Pnt[i])).y)) / (double)((FlightModelMain) (FM)).Arms.WING_END;
        fricF = -8000D * ForwardVPrj;
        fricR = -50000D * RightVPrj;
        fric = Math.sqrt(fricF * fricF + fricR * fricR);
        if(fric > 20000D * d_13_)
        {
            fric = (20000D * d_13_) / fric;
            fricF *= fric;
            fricR *= fric;
        }
        tmpV.scale(fricF, Forward);
        Tn.add(tmpV);
        tmpV.scale(fricR, Right);
        Tn.add(tmpV);
        if(i > 6 && bIsMaster)
        {
            Actor actor = ((Interpolate) (FM)).actor;
            World.cur();
            if(actor == World.getPlayerAircraft() && ((Tuple3d) (((FlightModelMain) (FM)).Loc)).z - Engine.land().HQ_Air(((Tuple3d) (((FlightModelMain) (FM)).Loc)).x, ((Tuple3d) (((FlightModelMain) (FM)).Loc)).y) < 2D && !bTheBabysGonnaBlow)
            {
                for(int i_14_ = 0; i_14_ < ((FlightModelMain) (FM)).CT.Weapons.length; i_14_++)
                    if(((FlightModelMain) (FM)).CT.Weapons[i_14_] != null && ((FlightModelMain) (FM)).CT.Weapons[i_14_].length > 0)
                    {
                        for(int i_15_ = 0; i_15_ < ((FlightModelMain) (FM)).CT.Weapons[i_14_].length; i_15_++)
                            if(((((FlightModelMain) (FM)).CT.Weapons[i_14_][i_15_] instanceof BombGun) || (((FlightModelMain) (FM)).CT.Weapons[i_14_][i_15_] instanceof RocketGun) || (((FlightModelMain) (FM)).CT.Weapons[i_14_][i_15_] instanceof RocketBombGun)) && ((FlightModelMain) (FM)).CT.Weapons[i_14_][i_15_].haveBullets() && FM.getSpeed() > 38F && ((FlightModelMain) (FM)).CT.Weapons[i_14_][i_15_].getHookName().startsWith("_External"))
                                bTheBabysGonnaBlow = true;

                    }

                if(bTheBabysGonnaBlow && (!FM.isPlayers() || World.cur().diffCur.Vulnerability) && ((Aircraft)((Interpolate) (FM)).actor).isEnablePostEndAction(0.0D))
                {
                    ((Aircraft)((Interpolate) (FM)).actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
                    if(FM.isPlayers())
                        HUD.log("FailedBombsDetonate");
                }
            }
        }
        if(bIsMaster && NormalVPrj < 0.0D)
        {
            double d_16_ = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.00023668639053254438D + NormalVPrj * NormalVPrj * 0.020408163265306121D;
            if(d_16_ > 1.0D)
                landHit(i, (float)d_16_);
        }
        return true;
    }

    //TODO: This method has been extensively edited for the differential braking and steering mod. Please follow comments for changes
	private boolean testGearCollision(int i) {
		if ((double) ((FlightModelMain) (FM)).CT.getGear() < 0.01D)
			return false;
		double d1 = 1.0D;
		gWheelSinking[i] = (float) (-d);
		Vs.set(((FlightModelMain) (FM)).Vrel);
		Vs.scale(-1D);
		((FlightModelMain) (FM)).Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		if (NormalVPrj > 0.0D)
			NormalVPrj = 0.0D;
		double d2 = (((Tuple3d) (((FlightModelMain) (FM)).Vrel)).x
				* ((Tuple3d) (((FlightModelMain) (FM)).Vrel)).x + ((Tuple3d) (((FlightModelMain) (FM)).Vrel)).y
				* ((Tuple3d) (((FlightModelMain) (FM)).Vrel)).y) - 2D;
		if (d2 < 0.0D)
			d2 = 0.0D;
		double d3 = 0.01D * d2;
		if (d3 < 0.0D)
			d3 = 0.0D;
		if (d3 > 4.5D)
			d3 = 4.5D;
		double d4 = 0.40000000596046448D * Math.max(roughness * roughness,
				roughness);
		if (d3 > d4)
			d3 = d4;
		if (roughness > d3)
			roughness = d3;
		if (roughness < 0.20000000298023224D)
			roughness = 0.20000000298023224D;
		if (i < 2) {
			d += (double) World.Rnd().nextFloat(-2F, 1.0F)
			* 0.040000000000000001D * d3 * MassCoeff;
			d1 = Math.max(-9500D * (d - 0.10000000000000001D), -950000D * d);
			d1 *= springsStiffness;
		} else {
			d += (double) (World.Rnd().nextFloat(-2F, 1.0F) * 0.04F) * d3
			* MassCoeff;
			d1 = Math.max(-9500D * (d - 0.10000000000000001D), -950000D * d);
			if (((Tuple3f) (Pnt[i])).x > 0.0F && Fd.dot(Normal) >= 0.0D)
				d1 *= 0.44999998807907104D;
			else
				d1 *= tailStiffness;
		}
		d1 -= 40000D * NormalVPrj;
		Tn.scale(d1, Normal);
		double d5 = 0.0001D * d1;
		double d6 = ((FlightModelMain) (FM)).CT.getBrake();
		double d7 = ((FlightModelMain) (FM)).CT.getRudder();
		// TODO: PAS++
		double db1 = ((FlightModelMain) (FM)).CT.getBrakeRight();
		double db2 = ((FlightModelMain) (FM)).CT.getBrakeLeft();
		double da = db1 - db2; // diff alone
		double db = Math.max(d6, Math.max(db1, db2)); // applied pedal brakes
		// combined with common
		// PAS--

		switch (i) {
		case 0: // '\0' // MAIN WHEELS ( LEFT & RIGHT )
		case 1: // '\001'
			double d8 = 1.2D;
			if (i == 0)
				d8 = -1.2D;
			nOfGearsOnGr++;
			isGearColl = true;
			gVelocity[i] = ForwardVPrj;
			// TODO: PAS++ after TAK
			// this sets the action of braking system: independent differential
			// vs. rudder controlled
			if (d6 > 0.1D || db > 0.1D) // total brake action as db or d6
			{
				switch (FM.CT.DiffBrakesType) {
				case 0: // std
				case 1: // std for passive front wheel steering
				default:
					if (d7 > 0.1D)
						db += d8 * db * (d7 - 0.1D); // common * rudder - brake
					// & pedal action
					// combined
					if (d7 < -0.1D)
						db += d8 * db * (d7 + 0.1D);
					break;
				case 2: // diff combined - common * rudder plus left/right
					// brakes difference
					if (d7 > 0.1D)
						db += d8 * db * (d7 - 0.1D);
					if (d7 < -0.1D)
						db += d8 * db * (d7 + 0.1D);
					if (da > 0.1D)
						db += d8 * 4D * (da - 0.1D);
					if (da < -0.1D)
						db += d8 * 4D * (da + 0.1D);
					break;
				case 3: // diff - toe brakes
					if (da > 0.1D)
						db += d8 * 2D * (da - 0.1D); // left/right brakes
					// difference alone
					if (da < -0.1D)
						db += d8 * 2D * (da + 0.1D);
					// if(d6 > 0.1D)
					// db += d8 * 2D * (d6 - 0.1D); // common added (overriding)
					// - in reality there should be no common!
					break;
				case 4: // common * rudder - no pedals - Spitfire & Yak style
					if (d7 > 0.1D)
						db += d8 * d6 * (d7 - 0.1D);
					if (d7 < -0.1D)
						db += d8 * d6 * (d7 + 0.1D);
					break;
				}
				if (db > 1.0D)
					db = 1.0D;
				if (db < 0.0D)
					db = 0.0D;
				// PAS--
			}
			double d9 = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj
					* RightVPrj);
			if (d9 < 0.01D)
				d9 = 0.01D;
			double d10 = 1.0D / d9;
			double d11 = ForwardVPrj * d10;
			if (d11 < 0.0D)
				d11 *= -1D;
			double d12 = RightVPrj * d10;
			if (d12 < 0.0D)
				d12 *= -1D;
			double d13 = 5D;
			if (((Tuple3d) (PnT)).y * RightVPrj > 0.0D) {
				if (((Tuple3d) (PnT)).y > 0.0D)
					d13 += 7D * RightVPrj;
				else
					d13 -= 7D * RightVPrj;
				if (d13 > 20D)
					d13 = 20D;
			}
			double d14 = 15000D;
			if (d9 < 3D) {
				double d15 = -0.33333299999999999D * (d9 - 3D);
				d14 += 3000D * d15 * d15;
			}
			fricR = -d13 * 100000D * RightVPrj * d5;
			maxFric = d14 * d5 * d12;
			if (fricR > maxFric)
				fricR = maxFric;
			if (fricR < -maxFric)
				fricR = -maxFric;
			fricF = -d13 * 600D * ForwardVPrj * d5;
			maxFric = d13
			* Math.max(200D * (1.0D - 0.040000000000000001D * d9), 5D)
			* d5 * d11;
			if (fricF > maxFric)
				fricF = maxFric;
			if (fricF < -maxFric)
				fricF = -maxFric;
			double d16 = 0.029999999999999999D;
			if (((Tuple3f) (Pnt[2])).x > 0.0F)
				d16 = 0.059999999999999998D;
			double d17 = Math.abs(ForwardVPrj);
			if (d17 < 1.0D)
				d16 += 3D * (1.0D - d17);
			// TODO: PAS++
			d16 *= 0.029999999999999999D * db;
			// PAS--
			fricF += -300000D * d16 * ForwardVPrj * d5;
			maxFric = d14 * d5 * d11;
			if (fricF > maxFric)
				fricF = maxFric;
			if (fricF < -maxFric)
				fricF = -maxFric;
			fric = Math.sqrt(fricF * fricF + fricR * fricR);
			if (fric > maxFric) {
				fric = maxFric / fric;
				fricF *= fric;
				fricR *= fric;
			}
			tmpV.scale(fricF, Forward);
			Tn.add(tmpV);
			tmpV.scale(fricR, Right);
			Tn.add(tmpV);
			if (bIsMaster && NormalVPrj < 0.0D) {
				double d18 = ForwardVPrj * ForwardVPrj
				* 8.0000000000000007E-005D + RightVPrj * RightVPrj
				* 0.0067999999999999996D;
				if (((FlightModelMain) (FM)).CT.bHasArrestorControl)
					d18 += NormalVPrj * NormalVPrj * 0.025000000000000001D;
				else
					d18 += NormalVPrj * NormalVPrj * 0.070000000000000007D;
				if (d18 > 1.0D) {
					fatigue[i] += 10;
					double d21 = 38000D + (double) ((FlightModelMain) (FM)).M.massEmpty * 6D;
					double d23 = (((Tuple3d) (Tn)).x * ((Tuple3d) (Tn)).x
							* 0.14999999999999999D + ((Tuple3d) (Tn)).y
							* ((Tuple3d) (Tn)).y * 0.14999999999999999D + ((Tuple3d) (Tn)).z
							* ((Tuple3d) (Tn)).z * 0.080000000000000002D)
							/ (d21 * d21);
					if (fatigue[i] > 100 || d23 > 1.0D) {
						landHit(i, (float) d18);
						Aircraft aircraft1 = (Aircraft) ((Interpolate) (FM)).actor;
						if (i == 0)
							aircraft1.msgCollision(aircraft1, "GearL2_D0",
							"GearL2_D0");
						if (i == 1)
							aircraft1.msgCollision(aircraft1, "GearR2_D0",
							"GearR2_D0");
					}
				}
			}
			break;

		case 2: // '\002' // FRONT or REAR WHEEL
			nOfGearsOnGr++;
			if (bTailwheelLocked && steerAngle > -5F && steerAngle < 5F) {
				gVelocity[i] = ForwardVPrj;
				steerAngle = 0.0F;
				fric = -400D * ForwardVPrj;
				maxFric = 400D;
				if (fric > maxFric)
					fric = maxFric;
				if (fric < -maxFric)
					fric = -maxFric;
				tmpV.scale(fric, Forward);
				Tn.add(tmpV);
				fric = -10000D * RightVPrj;
				maxFric = 40000D;
				if (fric > maxFric)
					fric = maxFric;
				if (fric < -maxFric)
					fric = -maxFric;
				tmpV.scale(fric, Right);
				Tn.add(tmpV);
			} else if (bFrontWheel) // FRONT WHEEL STEERING
			{
				// TODO: PAS++
				// passive or active steering dependent on diff brakes type
				switch (FM.CT.DiffBrakesType) {
				case 0:
				default:
					// if(msg1)
					// {System.out.println(">>> Front wheel active steering");
					// msg1 = false;}
					gVelocity[i] = ForwardVPrj;
					tmpV.set(1.0D, -0.5D * (double) FM.CT.getRudder(), 0.0D); // std
					// -
					// rudder
					// control
					steerAngleFork.setDeg(steerAngle, (float) Math
							.toDegrees(Math.atan2(tmpV.y, tmpV.x)));
					steerAngle = steerAngleFork.getDeg(0.115F);
					nwRight.cross(Normal, tmpV);
					nwRight.normalize();
					nwForward.cross(nwRight, Normal);
					ForwardVPrj = nwForward.dot(Vs);
					RightVPrj = nwRight.dot(Vs);
					double d19 = Math.sqrt(ForwardVPrj * ForwardVPrj
							+ RightVPrj * RightVPrj);
					if (d19 < 0.01D)
						d19 = 0.01D;
					fricF = -100D * ForwardVPrj;
					maxFric = 4000D;
					if (fricF > maxFric)
						fricF = maxFric;
					if (fricF < -maxFric)
						fricF = -maxFric;
					fricR = -500D * RightVPrj;
					maxFric = 4000D;
					if (fricR > maxFric)
						fricR = maxFric;
					if (fricR < -maxFric)
						fricR = -maxFric;
					maxFric = 1.0D - 0.02D * d19;
					if (maxFric < 0.10000000000000001D)
						maxFric = 0.10000000000000001D;
					maxFric = 5000D * maxFric;
					fric = Math.sqrt(fricF * fricF + fricR * fricR);
					if (fric > maxFric) {
						fric = maxFric / fric;
						fricF *= fric;
						fricR *= fric;
					}
					tmpV.scale(fricF, Forward);
					Tn.add(tmpV);
					tmpV.scale(fricR, Right);
					Tn.add(tmpV);
					break;

				case 1: // passive steering
				case 2:
				case 3:
				case 4:
					// if(msg2)
					// {System.out.println(">>> Front wheel passive steering");
					// msg2 = false;}
					gVelocity[i] = Vs.length();
					if (Vs.lengthSquared() > 0.040000000000000001D) {
						steerAngleFork.setDeg(steerAngle, (float) Math
								.toDegrees(Math.atan2(Vs.y, Vs.x)));
						//TODO: This edit sets limits on degree of rotation for passively steering front wheels
						if(steerAngleFork.getDeg(0.001F) > -1F && steerAngleFork.getDeg(0.001F) <1F)
							steerAngle = steerAngleFork.getDeg(0.001F);
						else if(steerAngleFork.getDeg(0.001F) < -1F)
							steerAngle = -1F;
						else if(steerAngleFork.getDeg(0.001F) > 1F)
							steerAngle = 1F;
					}
					fricF = -1000D * ForwardVPrj;
					fricR = -1000D * RightVPrj;
					fric = Math.sqrt(fricF * fricF + fricR * fricR);
					maxFric = 1500D;
					if (fric > maxFric) {
						fric = maxFric / fric;
						fricF *= fric;
						fricR *= fric;
					}
					tmpV.scale(fricF, Forward);
					Tn.add(tmpV);
					tmpV.scale(fricR, Right);
					Tn.add(tmpV);
					break;
				}
				// PAS--
			} else // REAR WHEEL PASSIVE STEERING
			{
				gVelocity[i] = Vs.length();
				if (Vs.lengthSquared() > 0.040000000000000001D) {
					steerAngleFork.setDeg(steerAngle, (float) Math
							.toDegrees(Math.atan2(((Tuple3d) (Vs)).y,
									((Tuple3d) (Vs)).x)));
					steerAngle = steerAngleFork.getDeg(0.115F);
				}
				fricF = -1000D * ForwardVPrj;
				fricR = -1000D * RightVPrj;
				fric = Math.sqrt(fricF * fricF + fricR * fricR);
				maxFric = 1500D;
				if (fric > maxFric) {
					fric = maxFric / fric;
					fricF *= fric;
					fricR *= fric;
				}
				tmpV.scale(fricF, Forward);
				Tn.add(tmpV);
				tmpV.scale(fricR, Right);
				Tn.add(tmpV);
			}
			if (!bIsMaster || NormalVPrj >= 0.0D)
				break;
			double d20 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.0001D;
			if (((FlightModelMain) (FM)).CT.bHasArrestorControl)
				d20 += NormalVPrj * NormalVPrj * 0.0040000000000000001D;
			else
				d20 += NormalVPrj * NormalVPrj * 0.02D;
			if (d20 > 1.0D) {
				landHit(i, (float) d20);
				Aircraft aircraft = (Aircraft) ((Interpolate) (FM)).actor;
				aircraft.msgCollision(aircraft, "GearC2_D0", "GearC2_D0");
			}
			break;

		default:
			fricF = -4000D * ForwardVPrj;
			fricR = -4000D * RightVPrj;
			fric = Math.sqrt(fricF * fricF + fricR * fricR);
			if (fric > 10000D) {
				fric = 10000D / fric;
				fricF *= fric;
				fricR *= fric;
			}
			tmpV.scale(fricF, Forward);
			Tn.add(tmpV);
			tmpV.scale(fricR, Right);
			Tn.add(tmpV);
			break;
		}
		Tn.scale(MassCoeff);
		return true;
	}

    private boolean testWaterCollision(int i)
    {
        Vs.set(((FlightModelMain) (FM)).Vrel);
        Vs.scale(-1D);
        ((FlightModelMain) (FM)).Or.transformInv(Vs);
        tmpV.set(Pnt[i]);
        tmpV.cross(FM.getW(), tmpV);
        Vs.add(tmpV);
        ForwardVPrj = Forward.dot(Vs);
        NormalVPrj = Normal.dot(Vs);
        RightVPrj = Right.dot(Vs);
        double d = ForwardVPrj;
        if(d < 0.0D)
            d = 0.0D;
        if((!bIsSail || !bInWaterList(i)) && this.d < -2D)
            this.d = -2D;
        double d_39_ = -(1.0D + 0.29999999999999999D * d) * (double)sinkFactor * this.d * Math.abs(this.d) * (1.0D + 0.29999999999999999D * Math.sin((double)((long)(1 + i % 3) * Time.current()) * 0.001D));
        if(bIsSail && bInWaterList(i))
        {
            if(NormalVPrj > 0.0D)
                NormalVPrj = 0.0D;
            Tn.scale(d_39_, Normal);
            fric = -1000D * NormalVPrj;
            if(fric > 4000D)
                fric = 4000D;
            if(fric < -4000D)
                fric = -4000D;
            tmpV.scale(fric, Normal);
            Tn.add(tmpV);
            fricF = -40D * ForwardVPrj;
            fricR = -300D * RightVPrj;
            fric = Math.sqrt(fricF * fricF + fricR * fricR);
            if(fric > 50000D)
            {
                fric = 50000D / fric;
                fricF *= fric;
                fricR *= fric;
            }
            tmpV.scale(fricF * 0.5D, Forward);
            Tn.add(tmpV);
            tmpV.scale(fricR * 0.5D, Right);
            Tn.add(tmpV);
        } else
        {
            Tn.scale(d_39_, Normal);
            fric = -1000D * NormalVPrj;
            if(fric > 4000D)
                fric = 4000D;
            if(fric < -4000D)
                fric = -4000D;
            tmpV.scale(fric, Normal);
            Tn.add(tmpV);
            fricF = -500D * ForwardVPrj;
            fricR = -800D * RightVPrj;
            fric = Math.sqrt(fricF * fricF + fricR * fricR);
            if(fric > 50000D)
            {
                fric = 50000D / fric;
                fricF *= fric;
                fricR *= fric;
            }
            tmpV.scale(fricF, Forward);
            Tn.add(tmpV);
            tmpV.scale(fricR, Right);
            Tn.add(tmpV);
            if(sinkFactor > 1.0F && !bIsSail)
            {
                sinkFactor -= 0.4F * Time.tickLenFs();
                if(sinkFactor < 1.0F)
                    sinkFactor = 1.0F;
            }
        }
        if(bIsMaster && NormalVPrj < 0.0D)
        {
            double d_41_ = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.00040000000000000002D + NormalVPrj * NormalVPrj * 0.0011111111111111111D;
            if(d_41_ > 1.0D)
                landHit(i, (float)d_41_);
        }
        return true;
    }

    private boolean testPropellorCollision(int i)
    {
        if(bIsMaster && i >= 3 && i <= 6)
        {
            if(((Interpolate) (FM)).actor == World.getPlayerAircraft() && !World.cur().diffCur.Realistic_Landings)
                return false;
            FM.setCapableOfTaxiing(false);
            switch(((FlightModelMain) (FM)).Scheme)
            {
            default:
                break;

            case 1: // '\001'
                ((Aircraft)((Interpolate) (FM)).actor).hitProp(0, 0, Engine.actorLand());
                break;

            case 2: // '\002'
            case 3: // '\003'
                if(i < 5)
                    ((Aircraft)((Interpolate) (FM)).actor).hitProp(0, 0, Engine.actorLand());
                else
                    ((Aircraft)((Interpolate) (FM)).actor).hitProp(1, 0, Engine.actorLand());
                break;

            case 4: // '\004'
            case 5: // '\005'
                ((Aircraft)((Interpolate) (FM)).actor).hitProp(i - 3, 0, Engine.actorLand());
                break;

            case 6: // '\006'
                switch(i)
                {
                case 3: // '\003'
                    ((Aircraft)((Interpolate) (FM)).actor).hitProp(0, 0, Engine.actorLand());
                    break;

                case 4: // '\004'
                case 5: // '\005'
                    ((Aircraft)((Interpolate) (FM)).actor).hitProp(1, 0, Engine.actorLand());
                    break;

                case 6: // '\006'
                    ((Aircraft)((Interpolate) (FM)).actor).hitProp(2, 0, Engine.actorLand());
                    break;
                }
                break;
            }
            return false;
        } else
        {
            return true;
        }
    }

    private void landHit(int i, double d)
    {
        if(((FlightModelMain) (FM)).Vrel.length() >= 13D && pnti[i] >= 0 && (((Interpolate) (FM)).actor != World.getPlayerAircraft() || World.cur().diffCur.Realistic_Landings))
        {
            ActorHMesh actorhmesh = (ActorHMesh)((Interpolate) (FM)).actor;
            if(Actor.isValid(actorhmesh))
            {
                Mesh mesh = actorhmesh.mesh();
                long l = Time.tick();
                String string = actorhmesh.findHook(mesh.hookName(pnti[i])).chunkName();
                if(string.compareTo("CF_D0") == 0)
                {
                    if(d > 2D)
                        MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
                } else
                if(string.compareTo("Tail1_D0") == 0)
                {
                    if(d > 1.3D)
                        MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
                } else
                if((((Interpolate) (FM)).actor instanceof Scheme1) && string.compareTo("Engine1_D0") == 0)
                {
                    MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
                    if(d > 5D)
                        MsgCollision.post(l, actorhmesh, Engine.actorLand(), "CF_D0", "Body");
                } else
                {
                    MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
                }
            }
        }
    }

    public void hitLeftGear()
    {
        lgear = false;
        FM.brakeShoe = false;
    }

    public void hitRightGear()
    {
        rgear = false;
        FM.brakeShoe = false;
    }

    public void hitCentreGear()
    {
        cgear = false;
        FM.brakeShoe = false;
    }

    public boolean isAnyDamaged()
    {
        return !lgear || !rgear || !cgear;
    }

    private void drawEffects()
    {
        boolean bool = FM.isTick(16, 0);
        for(int i = 0; i < 3; i++)
            if(bIsSail && bool && isWater && clp[i] && FM.getSpeedKMH() > 10F)
            {
                if(clpGearEff[i][0] == null)
                {
                    clpGearEff[i][0] = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WakeSmaller.eff", -1F);
                    clpGearEff[i][1] = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WaveSmaller.eff", -1F);
                }
            } else
            if(bool && clpGearEff[i][0] != null)
            {
                Eff3DActor.finish(clpGearEff[i][0]);
                Eff3DActor.finish(clpGearEff[i][1]);
                clpGearEff[i][0] = null;
                clpGearEff[i][1] = null;
            }

        for(int i = 0; i < pnti.length; i++)
            if(clp[i] && ((FlightModelMain) (FM)).Vrel.length() > 16.666666670000001D && !isUnderDeck())
            {
                if(clpEff[i] == null)
                {
                    if(isWater)
                        effectName = "EFFECTS/Smokes/SmokeAirSplat.eff";
                    else
                    if(World.cur().camouflage == 1)
                        effectName = "EFFECTS/Smokes/SmokeAirTouchW.eff";
                    else
                        effectName = "EFFECTS/Smokes/SmokeAirTouch.eff";
                    clpEff[i] = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 90F, 0.0F)), 1.0F, effectName, -1F);
                }
            } else
            if(bool && clpEff[i] != null)
            {
                Eff3DActor.finish(clpEff[i]);
                clpEff[i] = null;
            }

        if(((FlightModelMain) (FM)).EI.getCatapult() && isUnderDeck() && !FM.brakeShoe)
        {
            if(FM.brakeShoeLastCarrier != null && catEff == null && bSteamCatapult)
            {
                ((Interpolate) (FM)).actor.pos.getAbs(Aircraft.tmpLoc1);
                FM.brakeShoeLoc.get(Pn);
                Aircraft.tmpLoc1.transform(Pn, PnT);
                float airtemperC = Atmosphere.temperature((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z) - 273.16F;
                float cloudstemperC = airtemperC - World.wind().curGust() - (float)(Mission.curCloudsType() * 3);
                if(cloudstemperC > 20F)
                    effectName = "3DO/Effects/Ships/CatapultSteam.eff";
                else
                if(cloudstemperC > 10F)
                    effectName = "3DO/Effects/Ships/CatapultSteam2.eff";
                else
                if(cloudstemperC > 0.0F)
                    effectName = "3DO/Effects/Ships/CatapultSteam3.eff";
                else
                if(cloudstemperC > -10F)
                    effectName = "3DO/Effects/Ships/CatapultSteam4.eff";
                else
                    effectName = "3DO/Effects/Ships/CatapultSteam5.eff";
                catEff = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(new Point3d(-2D, 0.0D, -1.5D), new Orient(0.0F, 0.0F, -70F)), 1.0F, effectName, -1F);
            }
        } else
        if(bool && catEff != null)
        {
            Eff3DActor.finish(catEff);
            catEff = null;
        }
        if(((FlightModelMain) (FM)).EI.getNum() > 0)
        {
            for(int i = 0; i < ((FlightModelMain) (FM)).EI.getNum(); i++)
            {
                ((Interpolate) (FM)).actor.pos.getAbs(Aircraft.tmpLoc1);
                Pn.set(((FlightModelMain) (FM)).EI.engines[i].getPropPos());
                Aircraft.tmpLoc1.transform(Pn, PnT);
                float f = (float)(((Tuple3d) (PnT)).z - Engine.cur.land.HQ(((Tuple3d) (PnT)).x, ((Tuple3d) (PnT)).y));
                if(f < 16.2F && ((FlightModelMain) (FM)).EI.engines[i].getThrustOutput() > 0.5F)
                {
                    Pn.x -= f * Aircraft.cvt(((FlightModelMain) (FM)).Or.getTangage(), -30F, 30F, 8F, 2.0F);
                    Aircraft.tmpLoc1.transform(Pn, PnT);
                    PnT.z = Engine.cur.land.HQ(((Tuple3d) (PnT)).x, ((Tuple3d) (PnT)).y);
                    if(clpEngineEff[i][0] == null)
                    {
                        Aircraft.tmpLoc1.transformInv(PnT);
                        if(isWater)
                        {
                            clpEngineEff[i][0] = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust2.eff", -1F);
                            clpEngineEff[i][1] = Eff3DActor.New(new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/WhiteEngineWaveTSPD.eff", -1F);
                        } else
                        {
                            clpEngineEff[i][0] = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", -1F);
                        }
                        continue;
                    }
                    if(isWater)
                    {
                        if(clpEngineEff[i][1] == null)
                        {
                            Eff3DActor.finish(clpEngineEff[i][0]);
                            clpEngineEff[i][0] = null;
                            continue;
                        }
                    } else
                    if(clpEngineEff[i][1] != null)
                    {
                        Eff3DActor.finish(clpEngineEff[i][0]);
                        clpEngineEff[i][0] = null;
                        Eff3DActor.finish(clpEngineEff[i][1]);
                        clpEngineEff[i][1] = null;
                        continue;
                    }
                    Aircraft.tmpOr.set(((FlightModelMain) (FM)).Or.getAzimut() + 180F, 0.0F, 0.0F);
                    ((Actor) (clpEngineEff[i][0])).pos.setAbs(PnT);
                    ((Actor) (clpEngineEff[i][0])).pos.setAbs(Aircraft.tmpOr);
                    ((Actor) (clpEngineEff[i][0])).pos.resetAsBase();
                    if(clpEngineEff[i][1] != null)
                    {
                        PnT.z = 0.0D;
                        ((Actor) (clpEngineEff[i][1])).pos.setAbs(PnT);
                    }
                } else
                {
                    if(clpEngineEff[i][0] != null)
                    {
                        Eff3DActor.finish(clpEngineEff[i][0]);
                        clpEngineEff[i][0] = null;
                    }
                    if(clpEngineEff[i][1] != null)
                    {
                        Eff3DActor.finish(clpEngineEff[i][1]);
                        clpEngineEff[i][1] = null;
                    }
                }
            }

        }
    }

    private void turnOffEffects()
    {
        if(FM.isTick(69, 0))
        {
            for(int i = 0; i < pnti.length; i++)
                if(clpEff[i] != null)
                {
                    Eff3DActor.finish(clpEff[i]);
                    clpEff[i] = null;
                }

            for(int i = 0; i < ((FlightModelMain) (FM)).EI.getNum(); i++)
            {
                if(clpEngineEff[i][0] != null)
                {
                    Eff3DActor.finish(clpEngineEff[i][0]);
                    clpEngineEff[i][0] = null;
                }
                if(clpEngineEff[i][1] != null)
                {
                    Eff3DActor.finish(clpEngineEff[i][1]);
                    clpEngineEff[i][1] = null;
                }
            }

            if(catEff != null)
            {
                Eff3DActor.finish(catEff);
                catEff = null;
            }
        }
    }

    private void processingCollisionEffect()
    {
        if(canDoEffect)
        {
            Vnorm = ((FlightModelMain) (FM)).Vwld.dot(Normal);
            if(((Interpolate) (FM)).actor == World.getPlayerAircraft() && World.cur().diffCur.Realistic_Landings && Vnorm < -20D && (double)World.Rnd().nextFloat() < 0.02D)
            {
                canDoEffect = false;
                int i = 20 + (int)(30F * World.Rnd().nextFloat());
                if(((FlightModelMain) (FM)).CT.Weapons[3] != null && ((FlightModelMain) (FM)).CT.Weapons[3][0] != null && ((FlightModelMain) (FM)).CT.Weapons[3][0].countBullets() != 0)
                    i = 0;
                if(((Aircraft)((Interpolate) (FM)).actor).isEnablePostEndAction(i))
                {
                    Eff3DActor eff3dactor = null;
                    if(i > 0)
                    {
                        Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(new Point3d(0.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/FireGND.eff", i);
                        eff3dactor = Eff3DActor.New(((Interpolate) (FM)).actor, null, new Loc(new Point3d(0.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/BlackHeavyGND.eff", i + 10);
                        ((NetAircraft)((Interpolate) (FM)).actor).sfxSmokeState(0, 0, true);
                    }
                    ((Aircraft)((Interpolate) (FM)).actor).postEndAction(i, Engine.actorLand(), 2, eff3dactor);
                }
            }
        }
    }

    public void load(SectFile sectfile)
    {
        bIsSail = sectfile.get("Aircraft", "Seaplane", 0) != 0;
        sinkFactor = sectfile.get("Gear", "SinkFactor", 1.0F);
        springsStiffness = sectfile.get("Gear", "SpringsStiffness", 1.0F);
        tailStiffness = sectfile.get("Gear", "TailStiffness", 0.6F);
        if(sectfile.get("Gear", "FromIni", 0) == 1)
        {
            H = sectfile.get("Gear", "H", 2.0F);
            Pitch = sectfile.get("Gear", "Pitch", 10F);
        } else
        {
            H = Pitch = 0.0F;
        }
        String string = sectfile.get("Gear", "WaterClipList", "-");
        if(!string.startsWith("-"))
        {
            waterList = new int[3 + string.length() / 2];
            waterList[0] = 0;
            waterList[1] = 1;
            waterList[2] = 2;
            for(int i = 0; i < waterList.length - 3; i++)
            {
                waterList[3 + i] = 10 * (string.charAt(i + i) - 48) + 1 * (string.charAt(i + i + 1) - 48);
                waterList[3 + i] += 3;
            }

        }
    }

    public float getLandingState()
    {
        if(!onGround)
            return 0.0F;
        float f = 0.4F + ((float)roughness - 0.2F) * 0.5F;
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    public double plateFriction(FlightModel flightmodel)
    {
        Actor actor = ((Interpolate) (flightmodel)).actor;
        if(bUnderDeck)
            return 0.0D;
        if(!Actor.isValid(actor))
            return 0.20000000000000001D;
        if(!World.cur().diffCur.Realistic_Landings)
            return 0.20000000000000001D;
        float f = 200F;
        actor.pos.getAbs(corn);
        bPlateExist = false;
        bPlateGround = false;
        Engine.drawEnv().getFiltered((AbstractCollection)null, ((Tuple3d) (corn)).x - (double)f, ((Tuple3d) (corn)).y - (double)f, ((Tuple3d) (corn)).x + (double)f, ((Tuple3d) (corn)).y + (double)f, 1, plateFilter);
        if(bPlateExist)
            return bPlateGround ? 0.80000000000000004D : 0.0D;
        int i = Engine.cur.land.HQ_RoadTypeHere(((Tuple3d) (((FlightModelMain) (flightmodel)).Loc)).x, ((Tuple3d) (((FlightModelMain) (flightmodel)).Loc)).y);
        switch(i)
        {
        case 1: // '\001'
            return 0.80000000000000004D;

        case 2: // '\002'
            return 0.0D;

        case 3: // '\003'
            return 5D;
        }
        if(zutiCurrentZAP != null)
        {
            double result = zutiCurrentZAP.isInZAPArea(((Tuple3d) (((FlightModelMain) (flightmodel)).Loc)).x, ((Tuple3d) (((FlightModelMain) (flightmodel)).Loc)).y);
            if(result > -1D)
                if(zutiHasPlaneSkisOnWinterCamo && result > 2.3999999999999999D)
                    return 2.4000000953674316D;
                else
                    return result;
        }
        List airports = ZutiSupportMethods_Engine.AIRFIELDS;
        int size = airports.size();
        for(int z = 0; z < size; z++)
        {
            ZutiAirfieldPoint point = (ZutiAirfieldPoint)airports.get(z);
            double result = point.isInZAPArea(((Tuple3d) (((FlightModelMain) (flightmodel)).Loc)).x, ((Tuple3d) (((FlightModelMain) (flightmodel)).Loc)).y);
            if(result > -1D)
            {
                zutiCurrentZAP = point;
                if(zutiHasPlaneSkisOnWinterCamo)
                    return 2.4000000953674316D;
                else
                    return result;
            }
        }

        return 3.7999999999999998D;
    }

    private String s(int i)
    {
        return (i < 10 ? "0" + Integer.toString(i) : "" + Integer.toString(i));
    }

    private boolean bInWaterList(int i)
    {
        if(waterList != null)
        {
            for(int i_42_ = 0; i_42_ < waterList.length; i_42_++)
                if(waterList[i_42_] == i)
                    return true;

        }
        return false;
    }

    public void zutiCheckPlaneForSkisAndWinterCamo(String string)
    {
        for(int i = 0; i < ZUTI_SKIS_AC_CLASSES.length; i++)
            if(string.endsWith(ZUTI_SKIS_AC_CLASSES[i]))
            {
                if("WINTER".equals(Engine.land().config.camouflage))
                    zutiHasPlaneSkisOnWinterCamo = true;
                else
                    zutiHasPlaneSkisOnWinterCamo = false;
                return;
            }

        zutiHasPlaneSkisOnWinterCamo = false;
    }
      
    //TODO: This method controls carrier catapult offest and positioning. To be edited in future versions so this info is read from an external .ini file
    public boolean setCatapultOffset(BigshipGeneric bigshipgeneric, SectFile theSectFile)
    {
    	boolean flag2 = false;
    	bCatapultAI = false;
    	
        String s = bigshipgeneric.getClass().getName();
        int i = s.lastIndexOf('.');
        int j = s.lastIndexOf('$');
        if(i < j)
        i = j;
        String strSection = s.substring(i + 1);
    	if(!flag2){
    		if (!theSectFile.sectionExist(strSection)) strSection = "Default";
    		if(!bStandardDeckCVL && theSectFile.exist(strSection, "dCatapultOffsetXAlt"))
    		{
    			dCatapultOffsetX = theSectFile.get(strSection, "dCatapultOffsetXAlt", 0.0F);
    			dCatapultOffsetY = theSectFile.get(strSection, "dCatapultOffsetYAlt", 0.0F);
    			dCatapultYaw = theSectFile.get(strSection, "dCatapultYawAlt", 0.0F);
    		}
    		else
    		{
    			dCatapultOffsetX = theSectFile.get(strSection, "dCatapultOffsetX", 0.0F);
    			dCatapultOffsetY = theSectFile.get(strSection, "dCatapultOffsetY", 0.0F);
    			dCatapultYaw = theSectFile.get(strSection, "dCatapultYaw", 0.0F);
    		}
	
    		dCatapultOffsetX2 = theSectFile.get(strSection, "dCatapultOffsetX2", 0.0F);
    		dCatapultOffsetY2 = theSectFile.get(strSection, "dCatapultOffsetY2", 0.0F);
    		dCatapultYaw2 = theSectFile.get(strSection, "dCatapultYaw2", 0.0F);
    		catapultPower = theSectFile.get(strSection, "catapultPower", 0.0F);
    		catapultPowerJets = theSectFile.get(strSection, "catapultPowerJets", 0.0F); 
    		if(theSectFile.get(strSection, "bSteamCatapult", 0) == 1)
    			bSteamCatapult = true;
        	if (dCatapultOffsetX != 0.0D || dCatapultOffsetY != 0.0D || dCatapultOffsetX2 != 0.0D || dCatapultOffsetY2 != 0.0D) {
        		flag2 = true;
        		bCatapultAI = bCatapultAllowAI;
        	}
        }
        return flag2;
    }

	public double getCatapultOffsetX()
    {
        return dCatapultOffsetX;
    }

    public double getCatapultOffsetY()
    {
        return dCatapultOffsetY;
    }

    public double getCatapultOffsetX2()
    {
        return dCatapultOffsetX2;
    }

    public double getCatapultOffsetY2()
    {
        return dCatapultOffsetY2;
    }

    public boolean getCatapultAI()
    {
        return bCatapultAI;
    }
    
    static 
    {
        Pnt = new Point3f[64];
        for(int i = 0; i < Pnt.length; i++)
            Pnt[i] = new Point3f();

    }


}