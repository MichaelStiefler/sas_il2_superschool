/*4.10.1 class + CTO Mod*/

// Code Cleaned by SAS~Storebror
package com.maddox.il2.fm;
import java.util.AbstractCollection;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Line2f;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollision;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.ZutiSupportMethods_Engine;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiAirfieldPoint;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.Scheme1;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.Ship;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class Gear
{
	// TODO: CTO Mod
	// ----------------------------------------
	private double dCatapultOffsetX;
	private double dCatapultOffsetY;
	private double dCatapultOffsetX2;
	private double dCatapultOffsetY2;
	private boolean bCatapultAllow;
	private boolean bCatapultBoost;
	private boolean bCatapultAI;
	private boolean bCatapultAllowAI;
	private boolean bCatapultSet;
	private boolean bAlreadySetCatapult;
	private boolean bCatapultAI_CVE;
	private boolean bCatapultAI_EssexClass;
	private boolean bCatapultAI_Illustrious;
	private boolean bCatapultAI_GrafZep;
	private boolean bCatapultAI_CVL;
	private boolean bStandardDeckCVL;
	// ----------------------------------------

	public int[] pnti;
	boolean onGround = false;
	boolean nearGround = false;
	public float H;
	public float Pitch;
	public float sinkFactor;
	public float springsStiffness;
	public float tailStiffness;
	public boolean bIsSail;
	public int nOfGearsOnGr = 0;
	public int nOfPoiOnGr = 0;
	private int oldNOfGearsOnGr = 0;
	private int oldNOfPoiOnGr = 0;
	private int nP = 0;
	public boolean gearsChanged = false;
	HierMesh HM;
	public boolean bFlatTopGearCheck;
	public static final int MP = 64;
	public static final double maxVf_gr = 65.0;
	public static final double maxVn_gr = 7.0;
	public static final double maxVf_wt = 50.0;
	public static final double maxVn_wt = 30.0;
	public static final double maxVf_wl = 110.0;
	public static final double maxVn_wl = 7.0;
	public static final double _1_maxVf_gr2 = 2.3668639053254438E-4;
	public static final double _1_maxVn_gr2 = 0.02040816326530612;
	public static final double _1_maxVf_wt2 = 4.0E-4;
	public static final double _1_maxVn_wt2 = 0.0011111111111111111;
	public static final double _1_maxVf_wl2 = 8.264462809917356E-5;
	public static final double _1_maxVn_wl2 = 0.02040816326530612;
	private static Point3f[] Pnt = new Point3f[64];
	private static boolean[] clp = new boolean[64];
	private Eff3DActor[] clpEff = new Eff3DActor[64];
	public Eff3DActor[][] clpGearEff = {{null, null}, {null, null}, {null, null}};
	public Eff3DActor[][] clpEngineEff = new Eff3DActor[8][2];
	private String effectName = new String();
	private boolean bTheBabysGonnaBlow = false;
	public boolean lgear = true;
	public boolean rgear = true;
	public boolean cgear = true;
	public boolean bIsHydroOperable = true;
	private boolean bIsOperable = true;
	public boolean bTailwheelLocked = false;
	public double[] gVelocity = {0.0, 0.0, 0.0};
	public float[] gWheelAngles = {0.0F, 0.0F, 0.0F};
	public float[] gWheelSinking = {0.0F, 0.0F, 0.0F};
	public float steerAngle = 0.0F;
	public double roughness = 0.5;
	public float arrestorVAngle = 0.0F;
	public float arrestorVSink = 0.0F;
	public HookNamed arrestorHook = null;
	public int[] waterList = null;
	private boolean isGearColl = false;
	private double MassCoeff = 1.0;
	public boolean bFrontWheel = false;
	private static AnglesFork steerAngleFork = new AnglesFork();
	private double d;
	private double D;
	private double Vnorm;
	private boolean isWater;
	private boolean bUnderDeck = false;
	private boolean bIsGear;
	private FlightModel FM;
	private boolean bIsMaster = true;
	private int[] fatigue = new int[2];
	private Point3d p0 = new Point3d();
	private Point3d p1 = new Point3d();
	private Loc l0 = new Loc();
	private Vector3d v0 = new Vector3d();
	private Vector3d tmpV = new Vector3d();
	private double fric = 0.0;
	private double fricF = 0.0;
	private double fricR = 0.0;
	private double maxFric = 0.0;
	public double screenHQ = 0.0;
	static ClipFilter clipFilter = new ClipFilter();
	private boolean canDoEffect = true;
	private static Vector3d Normal;
	private static Vector3d Forward;
	private static Vector3d Right;
	private static Vector3d nwForward;
	private static Vector3d nwRight;
	private static double NormalVPrj;
	private static double ForwardVPrj;
	private static double RightVPrj;
	private static Vector3d Vnf;
	private static Vector3d Fd;
	private static Vector3d Fx;
	private static Vector3d Vship;
	private static Vector3d Fv;
	private static Vector3d Tn;
	private static Point3d Pn;
	private static Point3d PnT;
	private static Point3d Pship;
	private static Vector3d Vs;
	private static Matrix4d M4;
	private static PlateFilter plateFilter;
	private static Point3d corn;
	private static Point3d corn1;
	private static Loc L;
	private static float[] plateBox;
	private static boolean bPlateExist;
	private static boolean bPlateGround;
	private static String[] ZUTI_SKIS_AC_CLASSES;
	private boolean zutiHasPlaneSkisOnWinterCamo = false;
	
	// TODO: +++ TD AI code backport from 4.13 +++
    public float mgx;
    public float mgy;
    public float mgh;
    public float mgalpha;
    public float sgx;
	// TODO: --- TD AI code backport from 4.13 ---


	private static class PlateFilter implements ActorFilter
	{
		private PlateFilter()
		{
		}

		public boolean isUse(Actor actor, double d)
		{
			if (!(actor instanceof Plate))
				return true;
			Mesh mesh = ((ActorMesh)actor).mesh();
			mesh.getBoundBox(Gear.plateBox);
			Gear.corn1.set(Gear.corn);
			Loc loc = actor.pos.getAbs();
			loc.transformInv(Gear.corn1);
			if ((double)(Gear.plateBox[0] - 2.5F) < Gear.corn1.x && Gear.corn1.x < (double)(Gear.plateBox[3] + 2.5F) && (double)(Gear.plateBox[1] - 2.5F) < Gear.corn1.y && Gear.corn1.y < (double)(Gear.plateBox[4] + 2.5F))
			{
				Gear.bPlateExist = true;
				Gear.bPlateGround = ((Plate)actor).isGround();
			}
			return true;
		}
	}

	static class ClipFilter implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			return actor instanceof BigshipGeneric;
		}
	}

	public Gear()
	{
		// TODO: CTO Mod
		// ----------------------------------------
		dCatapultOffsetX = 0.0D;
		dCatapultOffsetY = 0.0D;
		dCatapultOffsetX2 = 0.0D;
		dCatapultOffsetY2 = 0.0D;
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
		bStandardDeckCVL = false;
		// ----------------------------------------

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

		// TODO: CTO Mod
		// ----------------------------------------
		if (Mission.cur().sectFile().get("Mods", "CatapultAllow", 1) == 0)
		{
			bCatapultAllow = false;
			bCatapultAllowAI = false;
		}
		if (Mission.cur().sectFile().get("Mods", "CatapultBoost", 0) == 1 && !Mission.isCoop())
			bCatapultBoost = true;
		if (Config.cur.ini.get("Mods", "CatapultAllowAI", 1) == 0)
			bCatapultAllowAI = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAllowAI", 1) == 0)
			bCatapultAllowAI = false;
		if (Config.cur.ini.get("Mods", "CatapultAI_CVE", 1) == 0)
			bCatapultAI_CVE = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAI_CVE", 1) == 0)
			bCatapultAI_CVE = false;
		if (Config.cur.ini.get("Mods", "CatapultAI_EssexClass", 1) == 0)
			bCatapultAI_EssexClass = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAI_EssexClass", 1) == 0)
			bCatapultAI_EssexClass = false;
		if (Config.cur.ini.get("Mods", "CatapultAI_Illustrious", 1) == 0)
			bCatapultAI_Illustrious = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAI_Illustrious", 1) == 0)
			bCatapultAI_Illustrious = false;
		if (Config.cur.ini.get("Mods", "CatapultAI_GrafZep", 1) == 0)
			bCatapultAI_GrafZep = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAI_GrafZep", 1) == 0)
			bCatapultAI_GrafZep = false;
		if (Config.cur.ini.get("Mods", "CatapultAI_CVL", 1) == 0)
			bCatapultAI_CVL = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAI_CVL", 1) == 0)
			bCatapultAI_CVL = false;
		if (Config.cur.ini.get("Mods", "StandardDeckCVL", 0) == 1)
			bStandardDeckCVL = true;
		if (Mission.cur().sectFile().get("Mods", "StandardDeckCVL", 0) == 1)
			bStandardDeckCVL = true;
		// ----------------------------------------
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
		if (pnti == null)
		{
			int i;
			for (i = 0; i < 61 && HM.hookFind("_Clip" + s(i)) >= 0; i++)
			{
				/* empty */
			}
			pnti = new int[i + 3];
			pnti[0] = HM.hookFind("_ClipLGear");
			pnti[1] = HM.hookFind("_ClipRGear");
			pnti[2] = HM.hookFind("_ClipCGear");
			for (i = 3; i < pnti.length; i++)
				pnti[i] = HM.hookFind("_Clip" + s(i - 3));
		}
		if (arrestorHook == null && hiermesh.hookFind("_ClipAGear") != -1)
			arrestorHook = new HookNamed(hiermesh, "_ClipAGear");
		int i = pnti[2];
		if (i > 0)
		{
			HM.hookMatrix(i, M4);
			if (M4.m03 > -1.0)
				bFrontWheel = true;
		}
	}

	// TODO: +++ TD AI code backport from 4.13 +++
	public void computePlaneLandPoseOld(FlightModel flightmodel)
	{
		FM = flightmodel;
		if (H == 0.0F || Pitch == 0.0F)
		{
			for (int i = 0; i < 3; i++)
			{
				if (pnti[i] < 0)
					return;
			}
			HM.hookMatrix(pnti[2], M4);
			double d = M4.m03;
			double d_0_ = M4.m23;
			HM.hookMatrix(pnti[0], M4);
			double d_1_ = M4.m03;
			double d_2_ = M4.m23;
			HM.hookMatrix(pnti[1], M4);
			d_1_ = (d_1_ + M4.m03) * 0.5;
			d_2_ = (d_2_ + M4.m23) * 0.5;
			double d_3_ = d_1_ - d;
			double d_4_ = d_2_ - d_0_;
			Pitch = -Geom.RAD2DEG((float)Math.atan2(d_4_, d_3_));
			if (d_3_ < 0.0)
				Pitch += 180.0F;
			Line2f line2f = new Line2f();
			line2f.set(new Point2f((float)d_1_, (float)d_2_), new Point2f((float)d, (float)d_0_));
			H = line2f.distance(new Point2f(0.0F, 0.0F));
			H -= (FM.M.massEmpty + FM.M.maxFuel + FM.M.maxNitro) * Atmosphere.g() / 2700000.0F;
		}
	}
	
    public void computePlaneLandPose(FlightModel flightmodel)
    {
        FM = flightmodel;
        for(int i = 0; i < 3; i++)
            if(pnti[i] < 0)
                return;

        HM.hookMatrix(pnti[0], M4);
        double d1 = M4.m03;
        double d2 = M4.m23;
        double d3 = M4.m13;
        HM.hookMatrix(pnti[2], M4);
        double d4 = M4.m03;
        double d5 = M4.m23;
        HM.hookMatrix(pnti[1], M4);
        d1 = (d1 + M4.m03) * 0.5D;
        d2 = (d2 + M4.m23) * 0.5D;
        double d6 = d1 - d4;
        double d7 = d2 - d5;
        if(H == 0.0F || Pitch == 0.0F)
        {
            Pitch = -Geom.RAD2DEG((float)Math.atan2(d7, d6));
            if(d6 < 0.0D)
                Pitch += 180F;
            Line2f line2f = new Line2f();
            line2f.set(new Point2f((float)d1, (float)d2), new Point2f((float)d4, (float)d5));
            H = line2f.distance(new Point2f(0.0F, 0.0F));
            H -= (double)((FM.M.massEmpty + FM.M.maxFuel + FM.M.maxNitro) * Atmosphere.g()) / 2700000D;
        }
        double d8 = Math.sqrt(d6 * d6 + d7 * d7);
        d1 = (d6 / d8) * d1 + (d7 / d8) * d2;
        if(d6 < 0.0D)
            d1 = -d1;
        mgx = (float)d1;
        mgy = (float)d3;
        sgx = (float)d4;
        mgh = (float)Math.sqrt(mgx * mgx + mgy * mgy);
        mgalpha = (float)Math.atan2(mgx, mgy);
    }
    // TODO: --- TD AI code backport from 4.13 ---

	public void set(Gear gear_5_)
	{
		if (gear_5_.pnti != null)
		{
			pnti = new int[gear_5_.pnti.length];
			if (gear_5_.waterList != null)
			{
				waterList = new int[gear_5_.waterList.length];
				for (int i = 0; i < waterList.length; i++)
					waterList[i] = gear_5_.waterList[i];
			}
			for (int i = 0; i < pnti.length; i++)
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
		FM = flightmodel;
		bIsMaster = flag;
		onGround = flag1;
		FM.Vrel.x = -FM.Vwld.x;
		FM.Vrel.y = -FM.Vwld.y;
		FM.Vrel.z = -FM.Vwld.z;
		for (int i = 0; i < 2; i++)
			if (fatigue[i] > 0)
				fatigue[i]--;

		Pn.set(FM.Loc);
		Pn.z = Engine.cur.land.HQ(Pn.x, Pn.y);
		double d1 = Pn.z;
		screenHQ = d1;
		if (FM.Loc.z - d1 > 50D && !bFlatTopGearCheck)
		{
			turnOffEffects();
			arrestorVSink = -50F;
			return;
		}
		isWater = Engine.cur.land.isWater(Pn.x, Pn.y);
		if (isWater)
			roughness = 0.5D;
		Engine.cur.land.EQN(Pn.x, Pn.y, Normal);
		bUnderDeck = false;
		BigshipGeneric bigshipgeneric = null;
		if (bFlatTopGearCheck)
		{
			corn.set(FM.Loc);
			corn1.set(FM.Loc);
			corn1.z -= 20D;
			Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
			if (actor instanceof BigshipGeneric)
			{
				Pn.z = Pship.z + 0.5D;
				d1 = Pn.z;
				isWater = false;
				bUnderDeck = true;
				actor.getSpeed(Vship);
				FM.Vrel.add(Vship);
				bigshipgeneric = (BigshipGeneric)actor;
				bigshipgeneric.addRockingSpeed(FM.Vrel, Normal, FM.Loc);
				if (flightmodel.AS.isMaster() && bigshipgeneric.getAirport() != null && flightmodel.CT.bHasArrestorControl)
				{
					if (!bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor) && FM.Vrel.lengthSquared() > 10D
							&& flightmodel.CT.getArrestor() > 0.1F)
					{
						bigshipgeneric.requestTowAircraft((Aircraft)flightmodel.actor);
						if (bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor))
						{
							flightmodel.AS.setFlatTopString(bigshipgeneric, bigshipgeneric.towPortNum);
							if ((FM instanceof RealFlightModel) && bIsMaster && ((RealFlightModel)FM).isRealMode())
								((RealFlightModel)FM).producedShakeLevel = 5F;
							((Aircraft)flightmodel.actor).sfxTow();
						}
					}
					if (bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor) && FM.Vrel.lengthSquared() < 1.0D
							&& World.Rnd().nextFloat() < 0.008F)
					{
						bigshipgeneric.requestDetowAircraft((Aircraft)flightmodel.actor);
						flightmodel.AS.setFlatTopString(bigshipgeneric, -1);
					}
				}
				if (bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor))
				{
					int k = bigshipgeneric.towPortNum;
					Point3d apoint3d[] = bigshipgeneric.getShipProp().propAirport.towPRel;
					bigshipgeneric.pos.getAbs(l0);
					l0.transform(apoint3d[k * 2], p0);
					l0.transform(apoint3d[k * 2 + 1], p1);
					p0.x = 0.5D * (p0.x + p1.x);
					p0.y = 0.5D * (p0.y + p1.y);
					p0.z = 0.5D * (p0.z + p1.z);
					flightmodel.actor.pos.getAbs(l0);
					l0.transformInv(p0);
					l0.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
					bigshipgeneric.towHook.computePos(flightmodel.actor, new Loc(l0), l0);
					v0.sub(p0, l0.getPoint());
					if (v0.x > 0.0D)
					{
						if (bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor))
						{
							bigshipgeneric.requestDetowAircraft((Aircraft)flightmodel.actor);
							flightmodel.AS.setFlatTopString(bigshipgeneric, -1);
						}
					}
					else
					{
						tmpV.set(FM.Vrel);
						flightmodel.actor.pos.getAbsOrient().transformInv(tmpV);
						if (tmpV.x < 0.0D)
						{
							double d3 = v0.length();
							v0.normalize();
							arrestorVAngle = (float)Math.toDegrees(Math.asin(v0.z));
							v0.scale(1000D * d3);
							flightmodel.GF.add(v0);
							v0.scale(0.3D);
							v0.cross(l0.getPoint(), v0);
							flightmodel.GM.add(v0);
						}
					}
				}
				else
				{
					arrestorVAngle = 0.0F;
				}
			}
		}
		if (flightmodel.CT.bHasArrestorControl)
		{
			flightmodel.actor.pos.getAbs(Aircraft.tmpLoc1);
			Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			arrestorHook.computePos(flightmodel.actor, Aircraft.tmpLoc1, loc);
			arrestorVSink = (float)(Pn.z - loc.getPoint().z);
		}
		Fd.set(FM.Vrel);
		Vnf.set(Normal);
		FM.Or.transformInv(Normal);
		FM.Or.transformInv(Fd);
		Fd.normalize();
		Pn.x = 0.0D;
		Pn.y = 0.0D;
		Pn.z -= FM.Loc.z;
		FM.Or.transformInv(Pn);
		D = -Normal.dot(Pn);
		if (!bIsMaster)
			D -= 0.015D;
		if (D > 50D)
		{
			nearGround = false;
			return;
		}
		nearGround = true;
		gWheelSinking[0] = gWheelSinking[1] = gWheelSinking[2] = 0.0F;
		for (int j = 0; j < pnti.length; j++)
		{
			int l = pnti[j];
			if (l <= 0)
			{
				Pnt[j].set(0.0F, 0.0F, 0.0F);
			}
			else
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
		for (int i1 = 0; i1 < pnti.length; i1++)
		{
			clp[i1] = false;
			if (i1 <= 2)
			{
				bIsGear = true;
				if (i1 == 0 && (!lgear || FM.CT.getGear() < 0.01F) || i1 == 1 && (!rgear || FM.CT.getGear() < 0.01F) || i1 == 2 && !cgear) {
			        continue;
				}
			}
			else
			{
				bIsGear = false;
			}
			PnT.set(Pnt[i1]);
			d = Normal.dot(PnT) + D;
			Fx.set(Fd);
			MassCoeff = 0.0004D * (double)FM.M.getFullMass();
			if (MassCoeff > 1.0D)
				MassCoeff = 1.0D;
//			if (i1 <= 2) System.out.println("ground d=" + d);
			if (d < 0.0D)
			{
				if (!testPropellorCollision(i1) || (isWater ? !testWaterCollision(i1) : bIsGear ? !testGearCollision(i1) : !testNonGearCollision(i1)))
					continue;
				clp[i1] = true;
				nP++;
			}
			else
			{
				if (d >= 0.1D || isWater || bIsGear || !testNonGearCollision(i1))
					continue;
				clp[i1] = true;
				nP++;
			}
			PnT.x += FM.Arms.GC_GEAR_SHIFT;
			Fx.cross(PnT, Tn);
			Fv.set(Fx);
			if (bIsSail && bInWaterList(i1))
			{
				tmpV.scale(fricF * 0.5D, Forward);
				Tn.add(tmpV);
				tmpV.scale(fricR * 0.5D, Right);
				Tn.add(tmpV);
			}
			if (bIsMaster)
			{
				FM.GF.add(Tn);
				FM.GM.add(Fx);
			}
		}

		if (oldNOfGearsOnGr != nOfGearsOnGr || oldNOfPoiOnGr != nOfPoiOnGr)
			gearsChanged = true;
		else
			gearsChanged = false;
		oldNOfGearsOnGr = nOfGearsOnGr;
		oldNOfPoiOnGr = nOfPoiOnGr;
		onGround = nP > 0;
		if (Config.isUSE_RENDER())
			drawEffects();
		if (bIsMaster)
		{
			FM.canChangeBrakeShoe = false;
			BigshipGeneric bigshipgeneric1 = bigshipgeneric;
			if (bigshipgeneric1 != null)
				FM.brakeShoeLastCarrier = bigshipgeneric1;
			else if (Mission.isCoop() && !Mission.isServer() && Actor.isAlive(FM.brakeShoeLastCarrier) && Time.current() < 60000L)
				bigshipgeneric1 = (BigshipGeneric)FM.brakeShoeLastCarrier;
//			System.out.println("nOfGearsOnGr == " + nOfGearsOnGr + ", nP == " + nP + ", FM.Vrel.lengthSquared() = " + FM.Vrel.lengthSquared());
			if (bigshipgeneric1 != null)
			{
				if (FM.brakeShoe)
				{
					if (!isAnyDamaged())
					{
						L.set(FM.brakeShoeLoc);
						L.add(FM.brakeShoeLastCarrier.pos.getAbs());

						// TODO: CTO Mod
						// ----------------------------------------
						FM.Loc.set(L.getPoint());
						FM.Or.set(L.getOrient());
						if (!bAlreadySetCatapult)
						{
							bCatapultSet = setCatapultOffset(bigshipgeneric1);
							bAlreadySetCatapult = true;
						}
						if (bCatapultAllow && !FM.EI.getCatapult() && bCatapultSet)
						{
							((Actor)(bigshipgeneric1.getAirport())).pos.getCurrent();
							Point3d point3d = L.getPoint();
							CellAirField cellairfield = bigshipgeneric1.getCellTO();
							double d4 = -cellairfield.leftUpperCorner().x - dCatapultOffsetX;
							double d6 = cellairfield.leftUpperCorner().y - dCatapultOffsetY;
							Loc loc2 = new Loc(d6, d4, 0.0D, 0.0F, 0.0F, 0.0F);
							loc2.add(FM.brakeShoeLastCarrier.pos.getAbs());
							Point3d point3d1 = loc2.getPoint();
							double d8 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x)
									+ (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
							if (d8 <= 100D)
							{
								L.set(d6, d4, FM.brakeShoeLoc.getZ(), FM.brakeShoeLoc.getAzimut(), FM.brakeShoeLoc.getTangage(), FM.brakeShoeLoc.getKren());
								L.add(FM.brakeShoeLastCarrier.pos.getAbs());
								FM.Loc.set(L.getPoint());
								FM.Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw(), Pitch, FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
								FM.actor.pos.setAbs(FM.Loc, FM.Or);
								FM.EI.setCatapult(FM.M.mass, bCatapultBoost);
								FM.brakeShoeLoc.set(FM.actor.pos.getAbs());
								FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getAbs());
							}
							else if (dCatapultOffsetX2 > 0.0D)
							{
								double d5 = -cellairfield.leftUpperCorner().x - dCatapultOffsetX2;
								double d7 = cellairfield.leftUpperCorner().y - dCatapultOffsetY2;
								loc2.set(d7, d5, 0.0D, 0.0F, 0.0F, 0.0F);
								loc2.add(FM.brakeShoeLastCarrier.pos.getAbs());
								Point3d point3d2 = loc2.getPoint();
								double d9 = (point3d.x - point3d2.x) * (point3d.x - point3d2.x)
										+ (point3d.y - point3d2.y) * (point3d.y - point3d2.y);
								if (d9 <= 100D)
								{
									L.set(d7, d5, FM.brakeShoeLoc.getZ(), FM.brakeShoeLoc.getAzimut(), FM.brakeShoeLoc.getTangage(), FM.brakeShoeLoc.getKren());
									L.add(FM.brakeShoeLastCarrier.pos.getAbs());
									FM.Loc.set(L.getPoint());
									FM.Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw(), Pitch, FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
									FM.actor.pos.setAbs(FM.Loc, FM.Or);
									FM.EI.setCatapult(FM.M.mass, bCatapultBoost);
									FM.brakeShoeLoc.set(FM.actor.pos.getAbs());
									FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getAbs());
								}
							}
						}
						else if (FM.EI.getCatapult())
							FM.EI.resetCatapultTime();
						FM.brakeShoeLastCarrier.getSpeed(FM.Vwld);
						// ----------------------------------------

						FM.Vrel.set(0.0D, 0.0D, 0.0D);
						for (int j1 = 0; j1 < 3; j1++)
							gVelocity[j1] = 0.0D;

						onGround = true;
						FM.canChangeBrakeShoe = true;
					}
					else
					{
						FM.brakeShoe = false;
					}
				}
				else
				{
					// TODO: CTO Mod
					// ----------------------------------------
					if (FM.EI.getCatapult())
						FM.Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw(), FM.Or.getPitch(), FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
					if (nOfGearsOnGr == 3 && nP == 3 && FM.Vrel.lengthSquared() < 1.0D)
					{
						FM.brakeShoeLoc.set(FM.actor.pos.getCurrent());
						FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getCurrent());
						FM.canChangeBrakeShoe = true;
					}
					bAlreadySetCatapult = false;
					// ----------------------------------------
				}
			}
			//TODO: Added by |ZUTI|: enables usage of chocks on ground airfields too. And compensate if player fires during chocks-in period - no back thrust.
			//-----------------------------------------------------------------------
			else if (nOfGearsOnGr == 3 && nP == 3 && FM.Vrel.lengthSquared() < 1.5)
			{
	            // TODO: +++ Slowly rolling wheels Mod by SAS~Storebror +++
	            if (FM.Vrel.lengthSquared() < 0.0001)
	                for(int k1 = 0; k1 < 3; k1++)
	                    gVelocity[k1] = 0.0D;
	            // TODO: --- Slowly rolling wheels Mod by SAS~Storebror ---
				FM.brakeShoeLoc.set(FM.actor.pos.getCurrent());
                FM.Vrel.set(0.0D, 0.0D, 0.0D);
                // TODO: +++ Slowly rolling wheels Mod by SAS~Storebror +++
//                for(int k1 = 0; k1 < 3; k1++)
//                    gVelocity[k1] = 0.0D;
                // TODO: --- Slowly rolling wheels Mod by SAS~Storebror ---

                FM.canChangeBrakeShoe = true;
                onGround = true;

                if(FM.brakeShoe)
                {
                    FM.GF.set(0.0D, 0.0D, 0.0D);
                    FM.GM.set(0.0D, 0.0D, 0.0D);
                    FM.Vwld.set(0.0D, 0.0D, 0.0D);
                }
			}
			//-----------------------------------------------------------------------
		}
		if (!bIsMaster)
			return;
		if (onGround && !isWater)
			processingCollisionEffect();
		double d2 = Engine.cur.land.HQ_ForestHeightHere(FM.Loc.x, FM.Loc.y);
		if (d2 > 0.0D && FM.Loc.z <= d1 + d2 && ((Aircraft)FM.actor).isEnablePostEndAction(0.0D))
			((Aircraft)FM.actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
	}

	private boolean testNonGearCollision(int i)
	{
		nOfPoiOnGr++;
		Vs.set(FM.Vrel);
		Vs.scale(-1.0);
		FM.Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		if (NormalVPrj > 0.0)
		{
			NormalVPrj -= 3.0;
			if (NormalVPrj < 0.0)
				NormalVPrj = 0.0;
		}
		double d = 1.0;
		double d_11_ = this.d - 0.06;
		double d_12_ = this.d + 0.04;
		if (d_11_ > 0.0)
			d_11_ = 0.0;
		if (d_11_ < -2.0)
			d_11_ = -2.0;
		if (d_12_ > 0.0)
			d_12_ = 0.0;
		if (d_12_ < -0.25)
			d_12_ = -0.25;
		d = Math.max(-120000.0 * d_11_, -360000.0 * d_12_);
		d *= MassCoeff;
		Tn.scale(d, Normal);
		fric = -40000.0 * NormalVPrj;
		if (fric > 100000.0)
			fric = 100000.0;
		if (fric < -100000.0)
			fric = -100000.0;
		tmpV.scale(fric, Normal);
		Tn.add(tmpV);
		double d_13_ = 1.0 - (0.5 * (double)Math.abs(Pnt[i].y) / (double)FM.Arms.WING_END);
		fricF = -8000.0 * ForwardVPrj;
		fricR = -50000.0 * RightVPrj;
		fric = Math.sqrt(fricF * fricF + fricR * fricR);
		if (fric > 20000.0 * d_13_)
		{
			fric = 20000.0 * d_13_ / fric;
			fricF *= fric;
			fricR *= fric;
		}
		tmpV.scale(fricF, Forward);
		Tn.add(tmpV);
		tmpV.scale(fricR, Right);
		Tn.add(tmpV);
		if (i > 6 && bIsMaster)
		{
			Actor actor = FM.actor;
			World.cur();
			if (actor == World.getPlayerAircraft() && FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) < 2.0 && !bTheBabysGonnaBlow)
			{
				for (int i_14_ = 0; i_14_ < FM.CT.Weapons.length; i_14_++)
				{
					if (FM.CT.Weapons[i_14_] != null && FM.CT.Weapons[i_14_].length > 0)
					{
						for (int i_15_ = 0; i_15_ < FM.CT.Weapons[i_14_].length; i_15_++)
						{
							if ((FM.CT.Weapons[i_14_][i_15_] instanceof BombGun || (FM.CT.Weapons[i_14_][i_15_] instanceof RocketGun) || (FM.CT.Weapons[i_14_][i_15_] instanceof RocketBombGun)) && FM.CT.Weapons[i_14_][i_15_].haveBullets()
									&& FM.getSpeed() > 38.0F && FM.CT.Weapons[i_14_][i_15_].getHookName().startsWith("_External"))
								bTheBabysGonnaBlow = true;
						}
					}
				}
				if (bTheBabysGonnaBlow && (!FM.isPlayers() || World.cur().diffCur.Vulnerability) && ((Aircraft)FM.actor).isEnablePostEndAction(0.0))
				{
					((Aircraft)FM.actor).postEndAction(0.0, Engine.actorLand(), 2, null);
					if (FM.isPlayers())
						HUD.log("FailedBombsDetonate");
				}
			}
		}
		if (bIsMaster && NormalVPrj < 0.0)
		{
			double d_16_ = ((ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 2.3668639053254438E-4 + NormalVPrj * NormalVPrj * 0.02040816326530612);
			if (d_16_ > 1.0)
				landHit(i, (double)(float)d_16_);
		}
		return true;
	}

	private boolean testGearCollision(int i)
	{
		if ((double)FM.CT.getGear() < 0.01)
			return false;
		double d = 1.0;
		gWheelSinking[i] = (float)-this.d;
		Vs.set(FM.Vrel);
		Vs.scale(-1.0);
		FM.Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		if (NormalVPrj > 0.0)
			NormalVPrj = 0.0;
		double d_17_ = FM.Vrel.x * FM.Vrel.x + FM.Vrel.y * FM.Vrel.y - 2.0;
		if (d_17_ < 0.0)
			d_17_ = 0.0;
		double d_18_ = 0.01 * d_17_;
		if (d_18_ < 0.0)
			d_18_ = 0.0;
		if (d_18_ > 4.5)
			d_18_ = 4.5;
		double d_19_ = 0.4 * Math.max(roughness * roughness, roughness);
		if (d_18_ > d_19_)
			d_18_ = d_19_;
		if (roughness > d_18_)
			roughness = d_18_;
		if (roughness < 0.2)
			roughness = 0.2;
		if (i < 2)
		{
			this.d += ((double)World.Rnd().nextFloat(-2.0F, 1.0F) * 0.04 * d_18_ * MassCoeff);
			d = Math.max(-9500.0 * (this.d - 0.1), -950000.0 * this.d);
			d *= (double)springsStiffness;
		}
		else
		{
			this.d += ((double)(World.Rnd().nextFloat(-2.0F, 1.0F) * 0.04F) * d_18_ * MassCoeff);
			d = Math.max(-9500.0 * (this.d - 0.1), -950000.0 * this.d);
			if (Pnt[i].x > 0.0F && Fd.dot(Normal) >= 0.0)
				d *= 0.45;
			else
				d *= (double)tailStiffness;
		}
		d -= 40000.0 * NormalVPrj;
		Tn.scale(d, Normal);
		double d_20_ = 1.0E-4 * d;
		double d_21_ = (double)FM.CT.getBrake();
		double d_22_ = (double)FM.CT.getRudder();
		// TAK++
		double d29_Right = (double) FM.CT.getBrakeRight();
		double d29_Left = (double) FM.CT.getBrakeLeft();
		double d30_2 = (double) d29_Right - d29_Left;
		// TAK--
//		System.out.println("testGearCollision(" + i+ ")");
		switch (i)
		{
			case 0 :
			case 1 :
			{
				double d_23_ = 1.2;
				if (i == 0)
					d_23_ = -1.2;
				nOfGearsOnGr++;
				isGearColl = true;
				gVelocity[i] = ForwardVPrj;
				if (d_21_ > 0.1)
				{
					if (d_22_ > 0.1)
						d_21_ += d_23_ * d_21_ * (d_22_ - 0.1);
					if (d_22_ < -0.1)
						d_21_ += d_23_ * d_21_ * (d_22_ + 0.1);
					// TAK
					if (d30_2 > 0.1)
						d_21_ += d_23_ * 4.0F * (d30_2 - 0.1);
					if (d30_2 < -0.1)
						d_21_ += d_23_ * 4.0F * (d30_2 + 0.1);
					// TAK--
					if (d_21_ > 1.0)
						d_21_ = 1.0;
					if (d_21_ < 0.0)
						d_21_ = 0.0;
				}
				double d_24_ = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj);
				if (d_24_ < 0.01)
					d_24_ = 0.01;
				double d_25_ = 1.0 / d_24_;
				double d_26_ = ForwardVPrj * d_25_;
				if (d_26_ < 0.0)
					d_26_ *= -1.0;
				double d_27_ = RightVPrj * d_25_;
				if (d_27_ < 0.0)
					d_27_ *= -1.0;
				double d_28_ = 5.0;
				if (PnT.y * RightVPrj > 0.0)
				{
					if (PnT.y > 0.0)
						d_28_ += 7.0 * RightVPrj;
					else
						d_28_ -= 7.0 * RightVPrj;
					if (d_28_ > 20.0)
						d_28_ = 20.0;
				}
				double d_29_ = 15000.0;
				if (d_24_ < 3.0)
				{
					double d_30_ = -0.333333 * (d_24_ - 3.0);
					d_29_ += 3000.0 * d_30_ * d_30_;
				}
				fricR = -d_28_ * 100000.0 * RightVPrj * d_20_;
				maxFric = d_29_ * d_20_ * d_27_;
				if (fricR > maxFric)
					fricR = maxFric;
				if (fricR < -maxFric)
					fricR = -maxFric;
				fricF = -d_28_ * 600.0 * ForwardVPrj * d_20_;
				maxFric = (d_28_ * Math.max(200.0 * (1.0 - 0.04 * d_24_), 5.0) * d_20_ * d_26_);
				if (fricF > maxFric)
					fricF = maxFric;
				if (fricF < -maxFric)
					fricF = -maxFric;
				double d_31_ = 0.03;
				if (Pnt[2].x > 0.0F)
					d_31_ = 0.06;
				double d_32_ = Math.abs(ForwardVPrj);
				if (d_32_ < 1.0)
					d_31_ += 3.0 * (1.0 - d_32_);
				d_31_ *= 0.03 * d_21_;
				fricF += -300000.0 * d_31_ * ForwardVPrj * d_20_;
				maxFric = d_29_ * d_20_ * d_26_;
				if (fricF > maxFric)
					fricF = maxFric;
				if (fricF < -maxFric)
					fricF = -maxFric;
				fric = Math.sqrt(fricF * fricF + fricR * fricR);
				if (fric > maxFric)
				{
					fric = maxFric / fric;
					fricF *= fric;
					fricR *= fric;
				}
				tmpV.scale(fricF, Forward);
				Tn.add(tmpV);
				tmpV.scale(fricR, Right);
				Tn.add(tmpV);
				if (bIsMaster && NormalVPrj < 0.0)
				{
					double d_33_ = (ForwardVPrj * ForwardVPrj * 8.0E-5 + RightVPrj * RightVPrj * 0.0068);
					if (FM.CT.bHasArrestorControl)
						d_33_ += NormalVPrj * NormalVPrj * 0.025;
					else
						d_33_ += NormalVPrj * NormalVPrj * 0.07;
					if (d_33_ > 1.0)
					{
						fatigue[i] += 10;
						double d_34_ = 38000.0 + (double)FM.M.massEmpty * 6.0;
						double d_35_ = ((Tn.x * Tn.x * 0.15 + Tn.y * Tn.y * 0.15 + Tn.z * Tn.z * 0.08) / (d_34_ * d_34_));
						if (fatigue[i] > 100 || d_35_ > 1.0)
						{
							landHit(i, (double)(float)d_33_);
							Aircraft aircraft = (Aircraft)FM.actor;
							if (i == 0)
								aircraft.msgCollision(aircraft, "GearL2_D0", "GearL2_D0");
							if (i == 1)
								aircraft.msgCollision(aircraft, "GearR2_D0", "GearR2_D0");
						}
					}
				}
				break;
			}
			case 2 :
				nOfGearsOnGr++;
				if (bTailwheelLocked && steerAngle > -5.0F && steerAngle < 5.0F)
				{
					gVelocity[i] = ForwardVPrj;
					steerAngle = 0.0F;
					fric = -400.0 * ForwardVPrj;
					maxFric = 400.0;
					if (fric > maxFric)
						fric = maxFric;
					if (fric < -maxFric)
						fric = -maxFric;
					tmpV.scale(fric, Forward);
					Tn.add(tmpV);
					fric = -10000.0 * RightVPrj;
					maxFric = 40000.0;
					if (fric > maxFric)
						fric = maxFric;
					if (fric < -maxFric)
						fric = -maxFric;
					tmpV.scale(fric, Right);
					Tn.add(tmpV);
				}
				else if (bFrontWheel)
				{
					gVelocity[i] = ForwardVPrj;
					//tmpV.set(1.0, -0.5 * (double)FM.CT.getRudder(), 0.0);
					// TAK++
					tmpV.set(1.0, -0.5 * (double) (d29_Right - d29_Left + FM.CT.getRudder()), 0.0);
					// TAK--
					steerAngleFork.setDeg(steerAngle, (float)Math.toDegrees(Math.atan2(tmpV.y, tmpV.x)));
					steerAngle = steerAngleFork.getDeg(0.115F);
					nwRight.cross(Normal, tmpV);
					nwRight.normalize();
					nwForward.cross(nwRight, Normal);
					ForwardVPrj = nwForward.dot(Vs);
					RightVPrj = nwRight.dot(Vs);
					double d_36_ = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj);
					if (d_36_ < 0.01)
						d_36_ = 0.01;
					fricF = -100.0 * ForwardVPrj;
					maxFric = 4000.0;
					if (fricF > maxFric)
						fricF = maxFric;
					if (fricF < -maxFric)
						fricF = -maxFric;
					fricR = -500.0 * RightVPrj;
					maxFric = 4000.0;
					if (fricR > maxFric)
						fricR = maxFric;
					if (fricR < -maxFric)
						fricR = -maxFric;
					maxFric = 1.0 - 0.02 * d_36_;
					if (maxFric < 0.1)
						maxFric = 0.1;
					maxFric = 5000.0 * maxFric;
					fric = Math.sqrt(fricF * fricF + fricR * fricR);
					if (fric > maxFric)
					{
						fric = maxFric / fric;
						fricF *= fric;
						fricR *= fric;
					}
					tmpV.scale(fricF, Forward);
					Tn.add(tmpV);
					tmpV.scale(fricR, Right);
					Tn.add(tmpV);
				}
				else
				{
					gVelocity[i] = Vs.length();
					if (Vs.lengthSquared() > 0.04)
					{
						steerAngleFork.setDeg(steerAngle, (float)Math.toDegrees(Math.atan2(Vs.y, Vs.x)));
						steerAngle = steerAngleFork.getDeg(0.115F);
					}
					fricF = -1000.0 * ForwardVPrj;
					fricR = -1000.0 * RightVPrj;
					fric = Math.sqrt(fricF * fricF + fricR * fricR);
					maxFric = 1500.0;
					if (fric > maxFric)
					{
						fric = maxFric / fric;
						fricF *= fric;
						fricR *= fric;
					}
					tmpV.scale(fricF, Forward);
					Tn.add(tmpV);
					tmpV.scale(fricR, Right);
					Tn.add(tmpV);
				}
				if (bIsMaster && NormalVPrj < 0.0)
				{
					double d_38_ = ((ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 1.0E-4);
					if (FM.CT.bHasArrestorControl)
						d_38_ += NormalVPrj * NormalVPrj * 0.0040;
					else
						d_38_ += NormalVPrj * NormalVPrj * 0.02;
					if (d_38_ > 1.0)
					{
						landHit(i, d_38_);
						Aircraft aircraft = (Aircraft)FM.actor;
						aircraft.msgCollision(aircraft, "GearC2_D0", "GearC2_D0");
					}
				}
				break;
			default :
				fricF = -4000.0 * ForwardVPrj;
				fricR = -4000.0 * RightVPrj;
				fric = Math.sqrt(fricF * fricF + fricR * fricR);
				if (fric > 10000.0)
				{
					fric = 10000.0 / fric;
					fricF *= fric;
					fricR *= fric;
				}
				tmpV.scale(fricF, Forward);
				Tn.add(tmpV);
				tmpV.scale(fricR, Right);
				Tn.add(tmpV);
		}
		Tn.scale(MassCoeff);
		return true;
	}

	private boolean testWaterCollision(int i)
	{
		Vs.set(FM.Vrel);
		Vs.scale(-1.0);
		FM.Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		double d = ForwardVPrj;
		if (d < 0.0)
			d = 0.0;
		if ((!bIsSail || !bInWaterList(i)) && this.d < -2.0)
			this.d = -2.0;
		double d_39_ = (-(1.0 + 0.3 * d) * (double)sinkFactor * this.d * Math.abs(this.d) * (1.0 + 0.3 * Math.sin((double)((long)(1 + i % 3) * Time.current()) * 0.0010)));
		if (bIsSail && bInWaterList(i))
		{
			if (NormalVPrj > 0.0)
				NormalVPrj = 0.0;
			Tn.scale(d_39_, Normal);
			fric = -1000.0 * NormalVPrj;
			if (fric > 4000.0)
				fric = 4000.0;
			if (fric < -4000.0)
				fric = -4000.0;
			tmpV.scale(fric, Normal);
			Tn.add(tmpV);
			fricF = -40.0 * ForwardVPrj;
			fricR = -300.0 * RightVPrj;
			fric = Math.sqrt(fricF * fricF + fricR * fricR);
			if (fric > 50000.0)
			{
				fric = 50000.0 / fric;
				fricF *= fric;
				fricR *= fric;
			}
			tmpV.scale(fricF * 0.5, Forward);
			Tn.add(tmpV);
			tmpV.scale(fricR * 0.5, Right);
			Tn.add(tmpV);
		}
		else
		{
			Tn.scale(d_39_, Normal);
			fric = -1000.0 * NormalVPrj;
			if (fric > 4000.0)
				fric = 4000.0;
			if (fric < -4000.0)
				fric = -4000.0;
			tmpV.scale(fric, Normal);
			Tn.add(tmpV);
			fricF = -500.0 * ForwardVPrj;
			fricR = -800.0 * RightVPrj;
			fric = Math.sqrt(fricF * fricF + fricR * fricR);
			if (fric > 50000.0)
			{
				fric = 50000.0 / fric;
				fricF *= fric;
				fricR *= fric;
			}
			tmpV.scale(fricF, Forward);
			Tn.add(tmpV);
			tmpV.scale(fricR, Right);
			Tn.add(tmpV);
			if (sinkFactor > 1.0F && !bIsSail)
			{
				sinkFactor -= 0.4F * Time.tickLenFs();
				if (sinkFactor < 1.0F)
					sinkFactor = 1.0F;
			}
		}
		if (bIsMaster && NormalVPrj < 0.0)
		{
			double d_41_ = ((ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 4.0E-4 + NormalVPrj * NormalVPrj * 0.0011111111111111111);
			if (d_41_ > 1.0)
				landHit(i, (double)(float)d_41_);
		}
		return true;
	}

	private boolean testPropellorCollision(int i)
	{
		if (bIsMaster && i >= 3 && i <= 6)
		{
			if (FM.actor == World.getPlayerAircraft() && !World.cur().diffCur.Realistic_Landings)
				return false;
			FM.setCapableOfTaxiing(false);
			switch (FM.Scheme)
			{
				default :
					break;
				case 1 :
					((Aircraft)FM.actor).hitProp(0, 0, Engine.actorLand());
					break;
				case 2 :
				case 3 :
					if (i < 5)
						((Aircraft)FM.actor).hitProp(0, 0, Engine.actorLand());
					else
						((Aircraft)FM.actor).hitProp(1, 0, Engine.actorLand());
					break;
				case 4 :
				case 5 :
					((Aircraft)FM.actor).hitProp(i - 3, 0, Engine.actorLand());
					break;
				case 6 :
					switch (i)
					{
						case 3 :
							((Aircraft)FM.actor).hitProp(0, 0, Engine.actorLand());
							break;
						case 4 :
						case 5 :
							((Aircraft)FM.actor).hitProp(1, 0, Engine.actorLand());
							break;
						case 6 :
							((Aircraft)FM.actor).hitProp(2, 0, Engine.actorLand());
							break;
					}
			}
			return false;
		}
		return true;
	}

	private void landHit(int i, double d)
	{
		if (!(FM.Vrel.length() < 13.0) && pnti[i] >= 0 && (FM.actor != World.getPlayerAircraft() || World.cur().diffCur.Realistic_Landings))
		{
			ActorHMesh actorhmesh = (ActorHMesh)FM.actor;
			if (Actor.isValid(actorhmesh))
			{
				Mesh mesh = actorhmesh.mesh();
				long l = Time.tick();
				String string = actorhmesh.findHook(mesh.hookName(pnti[i])).chunkName();
				if (string.compareTo("CF_D0") == 0)
				{
					if (d > 2.0)
						MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
				}
				else if (string.compareTo("Tail1_D0") == 0)
				{
					if (d > 1.3)
						MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
				}
				else if (FM.actor instanceof Scheme1 && string.compareTo("Engine1_D0") == 0)
				{
					MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
					if (d > 5.0)
						MsgCollision.post(l, actorhmesh, Engine.actorLand(), "CF_D0", "Body");
				}
				else
					MsgCollision.post(l, actorhmesh, Engine.actorLand(), string, "Body");
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
		for (int i = 0; i < 3; i++)
		{
			if (bIsSail && bool && isWater && clp[i] && FM.getSpeedKMH() > 10.0F)
			{
				if (clpGearEff[i][0] == null)
				{
					clpGearEff[i][0] = (Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WakeSmaller.eff", -1.0F));
					clpGearEff[i][1] = (Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WaveSmaller.eff", -1.0F));
				}
			}
			else if (bool && clpGearEff[i][0] != null)
			{
				Eff3DActor.finish(clpGearEff[i][0]);
				Eff3DActor.finish(clpGearEff[i][1]);
				clpGearEff[i][0] = null;
				clpGearEff[i][1] = null;
			}
		}
		for (int i = 0; i < pnti.length; i++)
		{
			if (clp[i] && FM.Vrel.length() > 16.66666667 && !isUnderDeck())
			{
				if (clpEff[i] == null)
				{
					if (isWater)
						effectName = "EFFECTS/Smokes/SmokeAirSplat.eff";
					else if (World.cur().camouflage == 1)
						effectName = "EFFECTS/Smokes/SmokeAirTouchW.eff";
					else
						effectName = "EFFECTS/Smokes/SmokeAirTouch.eff";
					clpEff[i] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 90.0F, 0.0F)), 1.0F, effectName, -1.0F);
				}
			}
			else if (bool && clpEff[i] != null)
			{
				Eff3DActor.finish(clpEff[i]);
				clpEff[i] = null;
			}
		}
		if (FM.EI.getNum() > 0)
		{
			int i = 0;
			for (/**/; i < FM.EI.getNum(); i++)
			{
				FM.actor.pos.getAbs(Aircraft.tmpLoc1);
				Pn.set(FM.EI.engines[i].getPropPos());
				Aircraft.tmpLoc1.transform(Pn, PnT);
				float f = (float)(PnT.z - Engine.cur.land.HQ(PnT.x, PnT.y));
				if (f < 16.2F && FM.EI.engines[i].getThrustOutput() > 0.5F)
				{
					Pn.x -= (double)(f * Aircraft.cvt(FM.Or.getTangage(), -30.0F, 30.0F, 8.0F, 2.0F));
					Aircraft.tmpLoc1.transform(Pn, PnT);
					PnT.z = Engine.cur.land.HQ(PnT.x, PnT.y);
					if (clpEngineEff[i][0] == null)
					{
						Aircraft.tmpLoc1.transformInv(PnT);
						if (isWater)
						{
							clpEngineEff[i][0] = (Eff3DActor.New(FM.actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust2.eff", -1.0F));
							clpEngineEff[i][1] = (Eff3DActor.New(new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/WhiteEngineWaveTSPD.eff", -1.0F));
						}
						else
							clpEngineEff[i][0] = (Eff3DActor.New(FM.actor, null, new Loc(PnT), 1.0F, ("3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage == 1 ? "2" : "1") + ".eff"), -1.0F));
					}
					else
					{
						if (isWater)
						{
							if (clpEngineEff[i][1] == null)
							{
								Eff3DActor.finish(clpEngineEff[i][0]);
								clpEngineEff[i][0] = null;
								continue;
							}
						}
						else if (clpEngineEff[i][1] != null)
						{
							Eff3DActor.finish(clpEngineEff[i][0]);
							clpEngineEff[i][0] = null;
							Eff3DActor.finish(clpEngineEff[i][1]);
							clpEngineEff[i][1] = null;
							continue;
						}
						Aircraft.tmpOr.set(FM.Or.getAzimut() + 180.0F, 0.0F, 0.0F);
						clpEngineEff[i][0].pos.setAbs(PnT);
						clpEngineEff[i][0].pos.setAbs(Aircraft.tmpOr);
						clpEngineEff[i][0].pos.resetAsBase();
						if (clpEngineEff[i][1] != null)
						{
							PnT.z = 0.0;
							clpEngineEff[i][1].pos.setAbs(PnT);
						}
					}
				}
				else
				{
					if (clpEngineEff[i][0] != null)
					{
						Eff3DActor.finish(clpEngineEff[i][0]);
						clpEngineEff[i][0] = null;
					}
					if (clpEngineEff[i][1] != null)
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
		if (FM.isTick(69, 0))
		{
			for (int i = 0; i < pnti.length; i++)
			{
				if (clpEff[i] != null)
				{
					Eff3DActor.finish(clpEff[i]);
					clpEff[i] = null;
				}
			}
			for (int i = 0; i < FM.EI.getNum(); i++)
			{
				if (clpEngineEff[i][0] != null)
				{
					Eff3DActor.finish(clpEngineEff[i][0]);
					clpEngineEff[i][0] = null;
				}
				if (clpEngineEff[i][1] != null)
				{
					Eff3DActor.finish(clpEngineEff[i][1]);
					clpEngineEff[i][1] = null;
				}
			}
		}
	}

	private void processingCollisionEffect()
	{
		if (canDoEffect)
		{
			Vnorm = FM.Vwld.dot(Normal);
			if (FM.actor == World.getPlayerAircraft() && World.cur().diffCur.Realistic_Landings && Vnorm < -20.0 && (double)World.Rnd().nextFloat() < 0.02)
			{
				canDoEffect = false;
				int i = 20 + (int)(30.0F * World.Rnd().nextFloat());
				if (FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][0].countBullets() != 0)
					i = 0;
				if (((Aircraft)FM.actor).isEnablePostEndAction((double)i))
				{
					Eff3DActor eff3dactor = null;
					if (i > 0)
					{
						Eff3DActor.New(FM.actor, null, new Loc(new Point3d(0.0, 0.0, 0.0), new Orient(0.0F, 90.0F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/FireGND.eff", (float)i);
						eff3dactor = (Eff3DActor.New(FM.actor, null, new Loc(new Point3d(0.0, 0.0, 0.0), new Orient(0.0F, 90.0F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/BlackHeavyGND.eff", (float)(i + 10)));
						((NetAircraft)FM.actor).sfxSmokeState(0, 0, true);
					}
					((Aircraft)FM.actor).postEndAction((double)i, Engine.actorLand(), 2, eff3dactor);
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
		if (sectfile.get("Gear", "FromIni", 0) == 1)
		{
			H = sectfile.get("Gear", "H", 2.0F);
			Pitch = sectfile.get("Gear", "Pitch", 10.0F);
		}
		else
			H = Pitch = 0.0F;
		String string = sectfile.get("Gear", "WaterClipList", "-");
		if (!string.startsWith("-"))
		{
			waterList = new int[3 + string.length() / 2];
			waterList[0] = 0;
			waterList[1] = 1;
			waterList[2] = 2;
			for (int i = 0; i < waterList.length - 3; i++)
			{
				waterList[3 + i] = (10 * (string.charAt(i + i) - 48) + 1 * (string.charAt(i + i + 1) - 48));
				waterList[3 + i] += 3;
			}
		}
	}

	public float getLandingState()
	{
		if (!onGround)
			return 0.0F;
		float f = 0.4F + ((float)roughness - 0.2F) * 0.5F;
		if (f > 1.0F)
			f = 1.0F;
		return f;
	}

	public double plateFriction(FlightModel flightmodel)
	{
		// TODO: Added by |ZUTI|: has to be called here because this is the point
		// that actually refreshes RRR timers. As as long as we are on the ground, this
		// is refreshing.
		// -------------------------------------
		ZutiSupportMethods_FM.resetRRRTimers();
		ZutiSupportMethods_FM.updateDeckTimer();
		ZutiSupportMethods_FM.updateVulnerabilityTimer();
		// -------------------------------------

		Actor actor = flightmodel.actor;
		if (bUnderDeck)
			return 0.0;
		if (!Actor.isValid(actor))
			return 0.2;
		if (!World.cur().diffCur.Realistic_Landings)
			return 0.2;
		float f = 200.0F;
		actor.pos.getAbs(corn);
		bPlateExist = false;
		bPlateGround = false;
		Engine.drawEnv().getFiltered((AbstractCollection)null, corn.x - (double)f, corn.y - (double)f, corn.x + (double)f, corn.y + (double)f, 1, plateFilter);
		if (bPlateExist)
		{
			if (bPlateGround)
				return 0.8;
			return 0.0;
		}
		int i = Engine.cur.land.HQ_RoadTypeHere(flightmodel.Loc.x, flightmodel.Loc.y);
		switch (i)
		{
			case 1 :
				return 0.8;
			case 2 :
				return 0.0;
			case 3 :
				return 5.0;
			default :
				// TODO: Edited by |ZUTI|: default value, means nothing is on this point. Check if the point is in defined airfields areas on the map...
				// World.land().config
				// ------------------------------------------------------------
				if (zutiCurrentZAP != null)
				{
					double result = zutiCurrentZAP.isInZAPArea(flightmodel.Loc.x, flightmodel.Loc.y);
					if (result > -1)
					{
						if (zutiHasPlaneSkisOnWinterCamo && result > 2.4)
							return 2.4;

						return result;
					}
				}

				List airports = ZutiSupportMethods_Engine.AIRFIELDS;
				int size = airports.size();
				for (int z = 0; z < size; z++)
				{
					ZutiAirfieldPoint point = (ZutiAirfieldPoint)airports.get(z);
					{
						double result = point.isInZAPArea(flightmodel.Loc.x, flightmodel.Loc.y);
						if (result > -1)
						{
							zutiCurrentZAP = point;

							if (zutiHasPlaneSkisOnWinterCamo)
								return 2.4;

							return result;
						}
					}
				}
				// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				return 3.8;
		}
	}

	private String s(int i)
	{
		return i < 10 ? "0" + i : "" + i;
	}

	private boolean bInWaterList(int i)
	{
		if (waterList != null)
		{
			for (int i_42_ = 0; i_42_ < waterList.length; i_42_++)
			{
				if (waterList[i_42_] == i)
					return true;
			}
		}
		return false;
	}

	public void zutiCheckPlaneForSkisAndWinterCamo(String string)
	{
		for (int i = 0; i < ZUTI_SKIS_AC_CLASSES.length; i++)
		{
			if (string.endsWith(ZUTI_SKIS_AC_CLASSES[i]))
			{
				if ("WINTER".equals(Engine.land().config.camouflage))
					zutiHasPlaneSkisOnWinterCamo = true;
				else
					zutiHasPlaneSkisOnWinterCamo = false;
				return;
			}
		}
		zutiHasPlaneSkisOnWinterCamo = false;
	}

	static
	{
		for (int i = 0; i < Pnt.length; i++)
			Pnt[i] = new Point3f();
		Normal = new Vector3d();
		Forward = new Vector3d();
		Right = new Vector3d();
		nwForward = new Vector3d();
		nwRight = new Vector3d();
		Vnf = new Vector3d();
		Fd = new Vector3d();
		Fx = new Vector3d();
		Vship = new Vector3d();
		Fv = new Vector3d();
		Tn = new Vector3d();
		Pn = new Point3d();
		PnT = new Point3d();
		Pship = new Point3d();
		Vs = new Vector3d();
		M4 = new Matrix4d();
		plateFilter = new PlateFilter();
		corn = new Point3d();
		corn1 = new Point3d();
		L = new Loc();
		plateBox = new float[6];
		bPlateExist = false;
		bPlateGround = false;
		ZUTI_SKIS_AC_CLASSES = new String[]{"DXXI_SARJA3_EARLY", "DXXI_SARJA3_LATE", "DXXI_SARJA3_SARVANTO", "DXXI_SARJA4", "R_5_SKIS", "BLENHEIM1", "BLENHEIM4", "GLADIATOR1", "GLADIATOR1J8A", "GLADIATOR2", "I_15BIS_SKIS", "I_16TYPE5_SKIS",
				"I_16TYPE6_SKIS"};
	}

	// TODO: CTO Mod
	// ----------------------------------------
	public boolean setCatapultOffset(BigshipGeneric bigshipgeneric)
	{
        printDebugMessage(DEBUG_DETAILED, "setCatapultOffset(" + bigshipgeneric.getClass().getName() + ")"); // TODO: Graf Zeppelin Debug
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		dCatapultOffsetX = 0.0D;
		dCatapultOffsetY = 0.0D;
		dCatapultOffsetX2 = 0.0D;
		dCatapultOffsetY2 = 0.0D;
		bCatapultAI = false;
		if ((bigshipgeneric instanceof Ship.USSKitkunBayCVE71) || (bigshipgeneric instanceof Ship.USSCasablancaCVE55)
				|| (bigshipgeneric instanceof Ship.USSShamrockBayCVE84))
		{
			dCatapultOffsetX = 4.2D;
			dCatapultOffsetY = -64D;
			flag2 = true;
			if (!bCatapultAllowAI || !bCatapultAI_CVE)
				bCatapultAI = false;
			else
				bCatapultAI = true;
		}
		else if ((bigshipgeneric instanceof Ship.USSEssexCV9) || (bigshipgeneric instanceof Ship.USSIntrepidCV11))
		{
			dCatapultOffsetX = 9D;
			dCatapultOffsetY = -130D;
			dCatapultOffsetX2 = 27.6D;
			dCatapultOffsetY2 = -137.5D;
			flag2 = true;
			if (!bCatapultAllowAI || !bCatapultAI_EssexClass)
				bCatapultAI = false;
			else
				bCatapultAI = true;
		}
		else if (bigshipgeneric instanceof Ship.HMSIllustriousCV)
		{
			dCatapultOffsetX = 7D;
			dCatapultOffsetY = -68D;
			flag2 = true;
			if (!bCatapultAllowAI || !bCatapultAI_Illustrious)
				bCatapultAI = false;
			else
				bCatapultAI = true;
		}
		try
		{
			Class.forName("com.maddox.il2.objects.ships.Ship$Carrier1"); // TODO: Patch Pack 107, fix missing catapult on german carriers
//            Class.forName("com.maddox.il2.objects.ships.Ship$Carrier0");
			flag = true;
		}
		catch (ClassNotFoundException classnotfoundexception)
		{}
		try
		{
			Class.forName("com.maddox.il2.objects.ships.Ship$USSBelleauWoodCVL24");
			flag1 = true;
		}
		catch (ClassNotFoundException classnotfoundexception1)
		{}
        printDebugMessage(DEBUG_DETAILED, "setCatapultOffset(" + bigshipgeneric.getClass().getName() + ") flag2 = " + flag2 + ", flag = " + flag); // TODO: Graf Zeppelin Debug
		if (!flag2 && flag) {
			if ((bigshipgeneric instanceof Ship.GrafZeppelin) || (bigshipgeneric instanceof Ship.PeterStrasser))
			{
			    printDebugMessage(DEBUG_NORMAL, "### GRAF ZEPPELIN / PETER STRASSER CATAPULT ENABLED!!! ###"); // TODO: Graf Zeppelin Debug
				dCatapultOffsetX = 4.8D;
				dCatapultOffsetY = -113.5D;
				dCatapultOffsetX2 = 19.1D;
				dCatapultOffsetY2 = -113.5D;
				flag2 = true;
				if (!bCatapultAllowAI || !bCatapultAI_GrafZep)
					bCatapultAI = false;
				else
					bCatapultAI = true;
			}
			else if (bigshipgeneric instanceof Ship.Carrier1)
			{
				dCatapultOffsetX = 4.2D;
				dCatapultOffsetY = -64D;
				flag2 = true;
				if (!bCatapultAllowAI || !bCatapultAI_CVE)
					bCatapultAI = false;
				else
					bCatapultAI = true;
			}
		}
		if (!flag2
				&& flag1
				&& ((bigshipgeneric instanceof Ship.USSSanJacintoCVL30) || (bigshipgeneric instanceof Ship.USSBelleauWoodCVL24) || (bigshipgeneric instanceof Ship.USSPrincetonCVL23)))
		{
			if (bStandardDeckCVL)
			{
				dCatapultOffsetX = 6D;
				dCatapultOffsetY = -52D;
				flag2 = true;
			}
			else
			{
				dCatapultOffsetX = 5D;
				dCatapultOffsetY = -28D;
				flag2 = true;
			}
			if (!bCatapultAllowAI || !bCatapultAI_CVL)
				bCatapultAI = false;
			else
				bCatapultAI = true;
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

	public boolean getCatapultAI()
	{
		return bCatapultAI;
	}
	// ----------------------------------------

	// TODO: |ZUTI| Variables and Methods
	// ---------------------------------------------------------------------------------
	public ZutiAirfieldPoint zutiCurrentZAP = null;
	// ---------------------------------------------------------------------------------

    private static int debugLevel = Integer.MIN_VALUE;
    private static final int DEBUG_NONE = 0;
    private static final int DEBUG_NORMAL = 1;
    private static final int DEBUG_DETAILED = 2;
    private static final int DEBUG_DEFAULT = DEBUG_NONE;
    
    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_GEAR", DEBUG_DEFAULT);
        return debugLevel;
    }
    
    public static void printDebugMessage(int minLogLevel, String theMessage) {
        if (curDebugLevel() < minLogLevel) return;
        System.out.println(theMessage);
    }
    
 // TODO: +++ TD AI code backport from 4.13 +++
    public void setSteeringAngle(float f)
    {
        steerAngle = f;
    }
 // TODO: --- TD AI code backport from 4.13 ---

}
