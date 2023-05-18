/* Gear class for the SAS Engine Mod*/

/*By PAL, from current Western Engine MOD*/
/*By PAL, implemented Diagonal extra force in Catapult*/
/*By western, landing wheel brake stronger for Heavy Jet on 25th/Jul./2018*/
/*By western, catapult Z force retouch on 12th/Feb./2019*/
/*By western, add historical wheel parameters on 29th/Sep./2019*/
/*By western, Helicopters or V/STOL planes don't use catapult on 07th/Oct./2019*/
/*By western, add historical wheel parameters and catapult cable hook msh-sim values on 26th/Feb./2020*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.effects.MiscEffects;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TypeBlastDeflector;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import java.io.PrintStream;
import java.util.Random;
import com.maddox.il2.objects.humans.LandAux;
import com.maddox.il2.objects.ActorSimpleMesh;
import java.util.AbstractCollection;

public class Gear {
	// TODO: Default Parameters
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
	private Eff3DActor clpEff[];
	public Eff3DActor clpGearEff[][] = { { null, null }, { null, null }, { null, null } };
	public Eff3DActor clpEngineEff[][];
	private String effectName;
	private boolean bTheBabysGonnaBlow;
	public boolean lgear;
	public boolean rgear;
	public boolean cgear;
	public boolean bIsHydroOperable;
	private boolean bIsOperable;
	public boolean bTailwheelLocked;
	public double gVelocity[] = { 0.0D, 0.0D, 0.0D };
	public float gWheelAngles[] = { 0.0F, 0.0F, 0.0F };
	public float gWheelSinking[] = { 0.0F, 0.0F, 0.0F };
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
	private double poiDrag;
	private double D;
	private double D0;
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
	private Vector3d tmpV1;
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
	private static Gear plateFilterGear = null;
	private static PlateFilter plateFilter = new PlateFilter();
	private static Point3d corn = new Point3d();
	private static Point3d corn1 = new Point3d();
	private static Loc L = new Loc();
	private static float plateBox[] = new float[6];
	public boolean bPlateExist;
	public boolean bPlateGround;
	public boolean bPlateConcrete;
	public boolean bPlateSand;
	private BornPlace currentBornPlace;
	private static String ZUTI_SKIS_AC_CLASSES[] = { "DXXI_SARJA3_EARLY", "DXXI_SARJA3_LATE", "DXXI_SARJA3_SARVANTO", "DXXI_SARJA4", "R_5_SKIS", "BLENHEIM1", "BLENHEIM4", "GLADIATOR1", "GLADIATOR1J8A", "GLADIATOR2", "I_15BIS_SKIS", "I_16TYPE5_SKIS",
			"I_16TYPE6_SKIS" };
	private boolean zutiHasPlaneSkisOnWinterCamo;
	Loc tempLoc;
	Point3f tempPoint;
	public float mgx;
	public float mgy;
	public float mgh;
	public float mgalpha;
	public float sgx;
	// --------------------------------------------------------

	// TODO: New parameters
	// --------------------------------------------------------
	private boolean bCatapultAllow;
	private boolean bCatapultBoost;
	private boolean bCatapultReferMissionYear;
	private float catapultPower;
	private float catapultPowerJets;
	private boolean bCatapultAI;
	private boolean bCatapultAllowAI;
	private boolean bCatapultSet;
	private boolean bAlreadySetCatapult;
	private boolean bStandardDeckCVL;
	private boolean bSteamCatapult;
	private int iCatapultAlreadySetNum;
	private boolean bHasBlastDeflector;
	private Eff3DActor catEff;
	private float carrierSpeedKMH;
	public ZutiAirfieldPoint zutiCurrentZAP;
	private boolean bUseOldCatapultPowerProcedure;
	private boolean bCatapultArmed;
	private double dCatapultForce;
	private int iCatapultNumber;
	private long lCatapultStartTime;
	private boolean bDebugCatapult;
	// --------------------------------------------------------
	private double[] dCatapultOffsetX = new double[4];
	private double[] dCatapultOffsetY = new double[4];
	private double[] dCatapultYaw = new double[4];
	private int iCatapults;

	public Loc catapults[][] = {
		{ new Loc(), new Loc() } ,
		{ new Loc(), new Loc() } ,
		{ new Loc(), new Loc() } ,
		{ new Loc(), new Loc() }
	};
	public boolean[] bCatapultHookExist = new boolean[4];

	public double[] fric_pub = new double[3];
	public double[] fricF_pub = new double[3];
	public double[] fricR_pub = new double[3];

	//By PAL, new for Shock Absorber
	public float shockAbsorber;

	//By western, tune wheels rotation speed using custom wheel radius bigger or smaller than stock 375mm.
	public double gWheelRadius[] = { 0.375D, 0.375D, 0.375D };
	private boolean bLoadedWheelRadius;

	//By western, max steering degree of nose gear
	public double gMaxNGearSteeringDegree = -1.0D;

	//By western, custom catapult gear X position and CatGear msh
	public double gCustomCatGearX = 0.0D;
	private String gCatGearMeshName = null;

	//By western, set Chock Offset and Special msh
	private double gChockLOffset[] = { -100.0D, -100.0D };
	private double gChockROffset[] = { -100.0D, -100.0D };
	private double gChockCOffset[] = { -100.0D, -100.0D };
	private String gChockLMeshName = null;
	private String gChockCMeshName = null;
	private String gChockCarrierLMeshName = null;
	private String gChockCarrierCMeshName = null;
	private long lLastCatPush = -1L;
	// --------------------------------------------------------

	private static class PlateFilter implements ActorFilter {

		public boolean isUse(Actor actor, double d1) {
			if (!(actor instanceof Plate)) return true;
			Mesh mesh = ((ActorMesh) actor).mesh();
			mesh.getBoundBox(Gear.plateBox);
			Gear.corn1.set(Gear.corn);
			Loc loc = actor.pos.getAbs();
			loc.transformInv(Gear.corn1);
			if ((double) (Gear.plateBox[0] - 2.5F) < Gear.corn1.x && Gear.corn1.x < (double) (Gear.plateBox[3] + 2.5F) && (double) (Gear.plateBox[1] - 2.5F) < Gear.corn1.y && Gear.corn1.y < (double) (Gear.plateBox[4] + 2.5F)) {
				Gear.plateFilterGear.bPlateExist = true;
				Gear.plateFilterGear.bPlateGround = ((Plate) actor).isGround();
				Gear.plateFilterGear.bPlateConcrete = ((Plate) actor).isConcrete();
				Gear.plateFilterGear.bPlateSand = ((Plate) actor).isSand();
			}
			return true;
		}

		private PlateFilter() {
		}

	}

	static class ClipFilter implements ActorFilter {

		public boolean isUse(Actor actor, double d1) {
			return actor instanceof BigshipGeneric;
		}

		ClipFilter() {
		}
	}

	public Gear() {
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
		effectName = "";
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
		tmpV1 = new Vector3d();
		fric = 0.0D;
		fricF = 0.0D;
		fricR = 0.0D;
		maxFric = 0.0D;
		screenHQ = 0.0D;
		canDoEffect = true;
		bPlateExist = false;
		bPlateGround = false;
		bPlateConcrete = false;
		bPlateSand = false;
		currentBornPlace = null;
		zutiHasPlaneSkisOnWinterCamo = false;
		tempLoc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		tempPoint = new Point3f();
		// TODO: +++ CTO Mod 4.12 +++
		for (int cati = 0; cati < 4; cati++) {
			dCatapultOffsetX[cati] = 0.0D;
			dCatapultOffsetY[cati] = 0.0D;
			dCatapultYaw[cati] = 0.0D;
		}
		iCatapults = 0;

		bCatapultAllow = true;
		bCatapultBoost = false;
		bCatapultAI = true;
		bCatapultAllowAI = true;
		bCatapultReferMissionYear = false;
		bCatapultSet = false;
		bAlreadySetCatapult = false;
		bStandardDeckCVL = false;
		bSteamCatapult = false;
		iCatapultAlreadySetNum = -1;
		bHasBlastDeflector = false;
		bUseOldCatapultPowerProcedure = true;

		if (Mission.cur().sectFile().get("Mods", "CatapultAllow", 1) == 0) {
			bCatapultAllow = false;
			bCatapultAllowAI = false;
		}
		if (Mission.cur().sectFile().get("Mods", "CatapultBoost", 1) == 1 && !Mission.isCoop()) bCatapultBoost = true;
		if (Config.cur.ini.get("Mods", "CatapultAllowAI", 1) == 0) bCatapultAllowAI = false;
		if (Mission.cur().sectFile().get("Mods", "CatapultAllowAI", 1) == 0) bCatapultAllowAI = false;
		if (Config.cur.ini.get("Mods", "CatapultReferMissionYear", 0) == 1) bCatapultReferMissionYear = true;
		if (Mission.cur().sectFile().get("Mods", "CatapultReferMissionYear", 0) == 1) bCatapultReferMissionYear = true;
		if (Config.cur.ini.get("Mods", "StandardDeckCVL", 0) == 1) bStandardDeckCVL = true;
		if (Mission.cur().sectFile().get("Mods", "StandardDeckCVL", 0) == 1) bStandardDeckCVL = true;
		if (Config.cur.ini.get("Mods", "OldCatapultPowerCode", 0) == 0) bUseOldCatapultPowerProcedure = false;
		if (Config.cur.ini.get("Mods", "DebugCatapult", 0) == 1) bDebugCatapult = true;
		bCatapultArmed = false;
		dCatapultForce = 0.0D;
		iCatapultNumber = 0;
		// TODO: --- CTO Mod 4.12 ---

		//By PAL, for Chocks:
		bShowChocks = (Config.cur.ini.get("Mods", "PALShowChocks", 0) == 1 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 2 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 3 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 11 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 12 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 13);
		bShowChocksLandAuxiliar = (Config.cur.ini.get("Mods", "PALShowChocks", 0) == 1 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 2 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 11 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 12);
		bShowChocksLandAuxiliarFIXsize = (Config.cur.ini.get("Mods", "PALShowChocks", 0) == 2 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 12);
		bUseChocksParking = (Config.cur.ini.get("Mods", "PALShowChocks", 0) == 11 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 12 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 13);
		//By PAL, for Catapult Cables, etc:
		bShowCatGear = Config.cur.ini.get("Mods", "PALShowCatGear", 0) == 1;

		bLoadedWheelRadius = false;
	}

	public boolean onGround() {
		return onGround;
	}

	public boolean nearGround() {
		return nearGround;
	}

	public boolean isHydroOperable() {
		return bIsHydroOperable;
	}

	public void setHydroOperable(boolean flag) {
		bIsHydroOperable = flag;
	}

	public boolean isOperable() {
		return bIsOperable;
	}

	public void setOperable(boolean flag) {
		bIsOperable = flag;
	}

	public float getSteeringAngle() {
		return steerAngle;
	}

	public void setSteeringAngle(float f) {
		steerAngle = f;
	}

	public boolean isUnderDeck() {
		return bUnderDeck;
	}

	public boolean getWheelsOnGround() {
		boolean flag = isGearColl;
		isGearColl = false;
		return flag;
	}

	public void set(HierMesh hiermesh) {
		HM = hiermesh;
		if (pnti == null) {
			int i;
			for (i = 0; i < 61 && HM.hookFind("_Clip" + s(i)) >= 0; i++)
				;
			pnti = new int[i + 3];
			pnti[0] = HM.hookFind("_ClipLGear");
			pnti[1] = HM.hookFind("_ClipRGear");
			pnti[2] = HM.hookFind("_ClipCGear");
			for (int j = 3; j < pnti.length; j++)
				pnti[j] = HM.hookFind("_Clip" + s(j - 3));

		}
		if (arrestorHook == null && hiermesh.hookFind("_ClipAGear") != -1) arrestorHook = new HookNamed(hiermesh, "_ClipAGear");
		int k = pnti[2];
		if (k > 0) {
			HM.hookMatrix(k, M4);
			if (M4.m03 > -1D) bFrontWheel = true;
		}
	}

//By PAL, check this
	public void computePlaneLandPose(FlightModel flightmodel) {
		FM = flightmodel;
		for (int i = 0; i < 3; i++)
			if (pnti[i] < 0) return;

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
		if (H == 0.0F || Pitch == 0.0F) {
			Pitch = -Geom.RAD2DEG((float) Math.atan2(d7, d6));
			if (d6 < 0.0D) Pitch += 180F;
			Line2f line2f = new Line2f();
			line2f.set(new Point2f((float) d1, (float) d2), new Point2f((float) d4, (float) d5));
			H = line2f.distance(new Point2f(0.0F, 0.0F));
			H -= (double) ((FM.M.massEmpty + FM.M.maxFuel + FM.M.maxNitro) * Atmosphere.g()) / 2700000D;
		}
		double d8 = Math.sqrt(d6 * d6 + d7 * d7);
		d1 = (d6 / d8) * d1 + (d7 / d8) * d2;
		if (d6 < 0.0D) d1 = -d1;
		mgx = (float) d1;
		mgy = (float) d3;
		sgx = (float) d4;
		mgh = (float) Math.sqrt(mgx * mgx + mgy * mgy);
		mgalpha = (float) Math.atan2(mgx, mgy);

		if (!bLoadedWheelRadius)
			loadWheelRadius((Aircraft) FM.actor);
	}

	public void set(Gear gear) {
		if (gear.pnti == null) return;
		pnti = new int[gear.pnti.length];
		if (gear.waterList != null) {
			waterList = new int[gear.waterList.length];
			for (int i = 0; i < waterList.length; i++)
				waterList[i] = gear.waterList[i];

		}
		for (int j = 0; j < pnti.length; j++)
			pnti[j] = gear.pnti[j];

		bIsSail = gear.bIsSail;
		sinkFactor = gear.sinkFactor;
		springsStiffness = gear.springsStiffness;
		//By PAL, new
		shockAbsorber = gear.shockAbsorber;
		H = gear.H;
		Pitch = gear.Pitch;
		bFrontWheel = gear.bFrontWheel;
	}

	public void ground(FlightModel flightmodel, boolean flag) {
		ground(flightmodel, flag, false);
	}

	public void ground(FlightModel flightmodel, boolean flag, boolean flag1) {
//		FM = flightmodel;
		bIsMaster = flag;
		onGround = flag1;
		flightmodel.Vrel.x = -flightmodel.Vwld.x;
		flightmodel.Vrel.y = -flightmodel.Vwld.y;
		flightmodel.Vrel.z = -flightmodel.Vwld.z;
		boolean bIsTypeFastJet = false;
		boolean bIsJets = false;
		if (flightmodel.actor instanceof TypeFastJet)
			bIsTypeFastJet = true;
		if (flightmodel.EI.engines[0].getType() == 2)
			bIsJets = true;
		for (int i = 0; i < 2; i++)
			if (fatigue[i] > 0) fatigue[i]--;

		Pn.set(flightmodel.Loc);
		Pn.z = Engine.cur.land.HQ(Pn.x, Pn.y);
		double d1 = Pn.z;
		screenHQ = d1;
		if (flightmodel.Loc.z - d1 > 50D && !bFlatTopGearCheck) {
			turnOffEffects();
			arrestorVSink = -50F;
			nearGround = false;
			return;
		}
		isWater = Engine.cur.land.isWater(Pn.x, Pn.y);
		if (isWater) roughness = 0.5D;
		setD0(Engine.cur.land.EQN(Pn.x, Pn.y, Normal));
		bUnderDeck = false;
		BigshipGeneric bigshipgeneric = null;
		if (bFlatTopGearCheck) {
			corn.set(flightmodel.Loc);
			corn1.set(flightmodel.Loc);
			corn1.z -= 20D;
			Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
			if (actor instanceof BigshipGeneric) {
				Pn.z = Pship.z + 0.5D;
				d1 = Pn.z;
				isWater = false;
				bUnderDeck = true;
				actor.getSpeed(Vship);
				flightmodel.Vrel.add(Vship);
				bigshipgeneric = (BigshipGeneric) actor;
				bigshipgeneric.addRockingSpeed(flightmodel.Vrel, Normal, flightmodel.Loc);
				if (flightmodel.AS.isMaster() && bigshipgeneric.getAirport() != null && flightmodel.CT.bHasArrestorControl) {
					if (!bigshipgeneric.isTowAircraft((Aircraft) flightmodel.actor) && flightmodel.Vrel.lengthSquared() > 10D && flightmodel.CT.getArrestor() > 0.1F) {
						bigshipgeneric.requestTowAircraft((Aircraft) flightmodel.actor);
						if (bigshipgeneric.isTowAircraft((Aircraft) flightmodel.actor)) {
							flightmodel.AS.setFlatTopString(bigshipgeneric, bigshipgeneric.towPortNum);
							if ((flightmodel instanceof RealFlightModel) && bIsMaster && ((RealFlightModel) flightmodel).isRealMode()) ((RealFlightModel) flightmodel).producedShakeLevel = 5F;
							((Aircraft) flightmodel.actor).sfxTow();
						}
					}
					if (bigshipgeneric.isTowAircraft((Aircraft) flightmodel.actor) && flightmodel.Vrel.lengthSquared() < 1.0D && World.Rnd().nextFloat() < 0.008F) {
						bigshipgeneric.requestDetowAircraft((Aircraft) flightmodel.actor);
						flightmodel.AS.setFlatTopString(bigshipgeneric, -1);
					}
				}
				if (bigshipgeneric.isTowAircraft((Aircraft) flightmodel.actor)) {
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
					if (v0.x > 0.0D) {
						if (bigshipgeneric.isTowAircraft((Aircraft) flightmodel.actor)) {
							bigshipgeneric.requestDetowAircraft((Aircraft) flightmodel.actor);
							flightmodel.AS.setFlatTopString(bigshipgeneric, -1);
						}
					} else {
						tmpV.set(flightmodel.Vrel);
						flightmodel.actor.pos.getAbsOrient().transformInv(tmpV);
						if (tmpV.x < 0.0D) {
							double d3 = v0.length();
							v0.normalize();
							arrestorVAngle = (float) Math.toDegrees(Math.asin(v0.z));
							// TODO: +++ CTO Mod 4.12 +++
///////////////////////////////////////////////////////////////////////////////////////////
// By PAL, arrestor cable Effect, multiplied!
///////////////////////////////////////////////////////////////////////////////////////////
							double dist = 10D;
							double mass = flightmodel.M.getFullMass() * 0.01F;
							double vmin = flightmodel.VminFLAPS * 1.1D;

							double d4 = mass * vmin * vmin / (2F * dist);
///////////////////////////////////////////////////////////////////////////////////////////
							//By PAL, original v0.scale(1000D * d3);
							v0.scale(d4 * d3);
							flightmodel.GF.add(v0);
							v0.scale(0.3D);
							v0.cross(l0.getPoint(), v0);
							flightmodel.GM.add(v0);
						}
					}
				} else {
					arrestorVAngle = 0.0F;
				}
			}
		}
		if (flightmodel.CT.bHasArrestorControl) {
			flightmodel.actor.pos.getAbs(Aircraft.tmpLoc1);
			Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			arrestorHook.computePos(flightmodel.actor, Aircraft.tmpLoc1, loc);
			arrestorVSink = (float) (Pn.z - loc.getPoint().z);
		}
		Fd.set(flightmodel.Vrel);
		Vnf.set(Normal);
		flightmodel.Or.transformInv(Normal);
		flightmodel.Or.transformInv(Fd);
		Fd.normalize();
		Pn.x = 0.0D;
		Pn.y = 0.0D;
		Pn.z -= flightmodel.Loc.z;
		flightmodel.Or.transformInv(Pn);
		D = -Normal.dot(Pn);
		if (!bIsMaster) D -= 0.015D;
		if (D > 50D) {
			nearGround = false;
			return;
		}
		nearGround = true;
		gWheelSinking[0] = gWheelSinking[1] = gWheelSinking[2] = 0.0F;
		for (int j = 0; j < pnti.length; j++) {
			int l = pnti[j];
			if (l <= 0) {
				Pnt[j].set(0.0F, 0.0F, 0.0F);
			} else {
				HM.hookMatrix(l, M4);
				Pnt[j].set((float) M4.m03, (float) M4.m13, (float) M4.m23);
			}
		}

//By PAL, forces section
		nP = 0;
		nOfGearsOnGr = 0;
		nOfPoiOnGr = 0;
		tmpV.set(0.0D, 1.0D, 0.0D);
		Forward.cross(tmpV, Normal);
		Forward.normalize();
		Right.cross(Normal, Forward);
		boolean flag2 = false;
		for (int i1 = 0; i1 < pnti.length; i1++) {
			clp[i1] = false;
			if (i1 <= 2) {
				bIsGear = true;
				if (i1 == 0 && (!lgear || flightmodel.CT.getGearL() < 0.01F) || i1 == 1 && (!rgear || flightmodel.CT.getGearR() < 0.01F) || i1 == 2 && !cgear) continue;
			} else {
				bIsGear = false;
			}
			PnT.set(Pnt[i1]);
			d = Normal.dot(PnT) + D;
			Fx.set(Fd);
			MassCoeff = 0.0004D * (double) flightmodel.M.getFullMass();
			if (MassCoeff > 1.0D) MassCoeff = 1.0D;
			if (d < 0.0D) {
				if (!testPropellorCollision(i1)) continue;
				if (isWater) {
					if (!testWaterCollision(i1)) continue;
				} else {
					if (!flag2 && !flightmodel.isPlayers() && flightmodel.isTick(4, 0) && flightmodel.Vrel.length() > 0.10D) {
						plateCheck(flightmodel);
						flag2 = true;
					}
					if (bIsGear ? !testGearCollision(i1) : !testNonGearCollision(i1)) continue;
				}
				clp[i1] = true;
				nP++;
			} else {
				if (d >= 0.10D || isWater || bIsGear || !testNonGearCollision(i1)) continue;
				clp[i1] = true;
				nP++;
			}
			PnT.x += flightmodel.Arms.GC_GEAR_SHIFT;
			Fx.cross(PnT, Tn);
			Fv.set(Fx);
			if (bIsSail && bInWaterList(i1)) {
				tmpV.scale(fricF * 0.5D, Forward);
				Tn.add(tmpV);
				tmpV.scale(fricR * 0.5D, Right);
				Tn.add(tmpV);
			}
			if (bIsMaster) {
				flightmodel.GF.add(Tn);
				flightmodel.GM.add(Fx);
			}
		}

		if (oldNOfGearsOnGr != nOfGearsOnGr || oldNOfPoiOnGr != nOfPoiOnGr) gearsChanged = true;
		else gearsChanged = false;
		oldNOfGearsOnGr = nOfGearsOnGr;
		oldNOfPoiOnGr = nOfPoiOnGr;
		onGround = nP > 0;
		if (Config.isUSE_RENDER()) drawEffects();
		if (bIsMaster) {
			flightmodel.canChangeBrakeShoe = false;
			BigshipGeneric bigshipgeneric1 = bigshipgeneric;
			if (bigshipgeneric1 != null) flightmodel.brakeShoeLastCarrier = bigshipgeneric1;
			else
			// TODO: +++ CTO Mod 4.12 +++
			// if((Mission.isCoop() || Mission.isDogfight()) && !Mission.isServer() && Actor.isAlive(flightmodel.brakeShoeLastCarrier) && Time.current() < 60000L)
			if (Mission.isCoop() && !Mission.isServer() && Actor.isAlive(flightmodel.brakeShoeLastCarrier) && Time.current() < 60000L)
			// TODO: --- CTO Mod 4.12 ---
				bigshipgeneric1 = (BigshipGeneric) flightmodel.brakeShoeLastCarrier;
			// By western, Helicopter or V/STOL planes don't use catapult
			if ((flightmodel.actor instanceof TypeHelicopter) || flightmodel.CT.bUseVarWingAsNozzleRot) {
				bCatapultAllow = false;
				bCatapultAllowAI = false;
			}
			if (bigshipgeneric1 != null) {
				if (flightmodel.brakeShoe) {
					if (!isAnyDamaged()) {
						L.set(flightmodel.brakeShoeLoc);
						L.add(flightmodel.brakeShoeLastCarrier.pos.getAbs());
						flightmodel.Loc.set(L.getPoint());
						flightmodel.Or.set(L.getOrient());
					//By PAL, Carrier MOD
					//By PAL, Section to Set the Catapult from .ini, etc. (Western + PAL)
						if (!bAlreadySetCatapult) {
							bCatapultSet = setCatapultOffset(bigshipgeneric1, new SectFile("com/maddox/il2/objects/Catapults.ini"));
							bAlreadySetCatapult = true;
						}
						if (bCatapultAllow && !bCatapultArmed && bCatapultSet) {
							bigshipgeneric1.getAirport().pos.getCurrent();
							Point3d point3d = L.getPoint();
							CellAirField cellairfield = bigshipgeneric1.getCellTO();
							for (int k1 = 0; k1 < iCatapults; k1++) {
								if ((dCatapultOffsetX[k1] != 0.0D && dCatapultOffsetY[k1] != 0.0D)
								|| bCatapultHookExist[k1]) {
									double d5 = 0.0D;
									double d6 = 0.0D;
									if (bCatapultHookExist[k1]) {
										d5 = catapults[k1][0].getY();
										d6 = catapults[k1][0].getX();
									} else {
										d5 = -cellairfield.leftUpperCorner().x - dCatapultOffsetX[k1];
										d6 = cellairfield.leftUpperCorner().y - dCatapultOffsetY[k1];
									}
									Loc loc1 = new Loc(d6, d5, 0.0D, 0.0F, 0.0F, 0.0F);
									loc1.add(flightmodel.brakeShoeLastCarrier.pos.getAbs());
									Point3d point3d1 = loc1.getPoint();
									double d7 = Math.abs((point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y));
									if (d7 <= 100D) {
										L.set(d6, d5, flightmodel.brakeShoeLoc.getZ(), flightmodel.brakeShoeLoc.getAzimut(), flightmodel.brakeShoeLoc.getTangage(), flightmodel.brakeShoeLoc.getKren());
										L.add(flightmodel.brakeShoeLastCarrier.pos.getAbs());
										flightmodel.Loc.set(L.getPoint());
										flightmodel.Or.setYPR(flightmodel.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw[k1], Pitch, flightmodel.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
										flightmodel.actor.pos.setAbs(flightmodel.Loc, flightmodel.Or);
										//By PAL, no more EI at all...
										setCatapult(cellairfield, k1);
										flightmodel.brakeShoeLoc.set(flightmodel.actor.pos.getAbs());
										flightmodel.brakeShoeLoc.sub(flightmodel.brakeShoeLastCarrier.pos.getAbs());
										iCatapultAlreadySetNum = k1;
										if (bHasBlastDeflector) {
											((TypeBlastDeflector)bigshipgeneric1).setBlastDeflector(k1, 1);
										}
										break;
									}
								}
							}
						}
					//By PAL, End Carrier MOD
						flightmodel.brakeShoeLastCarrier.getSpeed(flightmodel.Vwld);
						flightmodel.Vrel.set(0.0D, 0.0D, 0.0D);
						for (int j1 = 0; j1 < 3; j1++)
							gVelocity[j1] = 0.0D;

						onGround = true;
						flightmodel.canChangeBrakeShoe = true;
					} else {
						flightmodel.brakeShoe = false;
					}
					//By PAL, to later report how long has been working the Catapult
					if (bCatapultArmed && flightmodel.canChangeBrakeShoe && flightmodel.brakeShoe)
						lCatapultStartTime = System.currentTimeMillis();
				} else {
				//By PAL, this block is else if (flightmodel.brakeShoe)
					if (nOfGearsOnGr == 3 && nP == 3 && flightmodel.Vrel.lengthSquared() < 1.0D) {
						flightmodel.brakeShoeLoc.set(flightmodel.actor.pos.getCurrent());
						flightmodel.brakeShoeLoc.sub(flightmodel.brakeShoeLastCarrier.pos.getCurrent());
						flightmodel.canChangeBrakeShoe = true;
					}
					//By PAL, Carrier MOD
					bAlreadySetCatapult = false;
					if (bHasBlastDeflector && bCatapultArmed) {
						((TypeBlastDeflector) bigshipgeneric1).setBlastDeflector(getCatapultNumber(), 0);
					}
					//By PAL, Catapult pull not in EI, and as a Direct Force
					if (bCatapultArmed && lLastCatPush < Time.current()) {
						//By PAL, this sets orientation of the plane (aligned with catapult)
						flightmodel.Or.setYPR(flightmodel.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw[getCatapultNumber()],
									flightmodel.Or.getPitch(),//flightmodel.brakeShoeLastCarrier.pos.getAbsOrient().getPitch() + Pitch + 2.0F, //flightmodel.Or.getPitch() < 5.0F ? flightmodel.Or.getPitch(): 5.0F,
										flightmodel.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());

						//By PAL, if Catapult is Set, Chocks is not set, then give impulse to it

						 //TODO: +++ CTO Mod 4.12 +++
						//By PAL, to avoid excessive Catapult Stress!!!!!!!!!!!!!
						float enginefc = 0F;
						for (int en = 0; en < flightmodel.EI.getNum(); en++)
							enginefc += flightmodel.EI.engines[en].getEngineForce().x;
						if (flightmodel.getLoadDiff() < flightmodel.getLimitLoad() * 1.5F)
							flightmodel.producedAF.x += (dCatapultForce - enginefc * 0.98F) * 3.3F;
//							flightmodel.producedAF.x += dCatapultForce;
						//By PAL, Diagonal Force towards ground (Catapult gear)
						//By western, add limitter to avoid breaking gears or ordnances
						flightmodel.producedAF.z -= Math.min(0.1F * dCatapultForce, 2500D);
						if (bDebugCatapult)
							System.out.println("*** Catapult pushing... power x=" + (dCatapultForce - enginefc * 0.98F) + " , z=" + Math.min(0.1F * dCatapultForce, 2500D) + " (" + (0.1F * dCatapultForce) + ") , producedAF.z=" + flightmodel.producedAF.z + " , producedAF.x=" + flightmodel.producedAF.x);
						lLastCatPush = Time.current();
					}
					//By PAL, End of Carrier MOD
				}
				//By PAL, show Chocks and Catapult gear, different if in carrier or in land.
				showChocks(true, bCatapultArmed);
				showCatapult(true, bCatapultArmed);
			} else if (nOfGearsOnGr == 3 && nP == 3 && flightmodel.Vrel.lengthSquared() < 1.5D) {
				//By PAL, if not BigShipGeneric
				flightmodel.brakeShoeLoc.set(flightmodel.actor.pos.getCurrent());
				flightmodel.Vrel.set(0.0D, 0.0D, 0.0D);
				flightmodel.canChangeBrakeShoe = true;
				onGround = true;
				if (flightmodel.brakeShoe) {
					flightmodel.GF.set(0.0D, 0.0D, 0.0D);
					flightmodel.GM.set(0.0D, 0.0D, 0.0D);
					flightmodel.Vwld.set(0.0D, 0.0D, 0.0D);
				}
			}
			//By PAL, show Chocks and Catapult gear, different if in carrier or in land.
			showChocks(false, false);
			if ((flightmodel.actor instanceof TypeSeaPlane) || (flightmodel.actor instanceof HE_LERCHE3)) flightmodel.canChangeBrakeShoe = false;
			//By PAL, to indicate if Catapult is not armed anymore (when it left Carrier Deck)
			float Vfly;
			if (bIsTypeFastJet || bIsJets) {
				if (flightmodel.Vmin < 56F) Vfly = 69.5F;  // 250km/h static when Vmin < 200km/h
				else if (flightmodel.Vmin < 61.1F) Vfly = flightmodel.Vmin + 13.9F;  // Vmin + 50km/h when Vmin < 220km/h
				else Vfly = flightmodel.Vmin * 1.25F;
			}
			else {
				if (flightmodel.Vmin < 36.1F) Vfly = 44.4F;  // 160km/h static when Vmin < 130km/h
				else if (flightmodel.Vmin < 52.8F) Vfly = flightmodel.Vmin + 10.0F;  // Vmin + 36km/h when Vmin < 190km/h
				else Vfly = flightmodel.Vmin * 1.2F;
			}
			if ((!bUnderDeck || flightmodel.getSpeed() - carrierSpeedKMH / 3.6F > Vfly || flightmodel.getSpeedKMH() - carrierSpeedKMH > 330F) && bCatapultArmed)
				disarmCatapult();
		}
		if (!bIsMaster) return;
		if (onGround && !isWater) processingCollisionEffect();
		double d2 = Engine.cur.land.HQ_ForestHeightHere(flightmodel.Loc.x, flightmodel.Loc.y);
		if (d2 > 0.0D && flightmodel.Loc.z <= d1 + d2 && ((Aircraft) flightmodel.actor).isEnablePostEndAction(0.0D)) ((Aircraft) flightmodel.actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//By PAL, moved to the Gear class
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO: +++ CTO Mod 4.12 +++
	public void setCatapult(CellAirField cellairfield, int i) {
		float mult = 10.0F; //By PAL, default multiplier
		boolean bDoneAutoCalculate = false;
		boolean bForceAutoCalculate = false;
		boolean bIsTypeFastJet = false;
		boolean bIsJets = false;
		if (FM.actor instanceof TypeFastJet)
			bIsTypeFastJet = true;
		if (FM.EI.engines[0].getType() == 2)
			bIsJets = true;
		//By PAL, Western, Boost ; reverting old code by request on 15th/Jan./2017
		if (bCatapultBoost)
		{
//			//By PAL, this didn't work. Attempt to Close Cockpit of Jets it they are open, case 128 from HotKeys:
//			//if(!((RealFlightModel)FM).isRealMode())
////				if(FM.CT.bHasCockpitDoorControl)
////				{
////					if(bIsTypeFastJet || bIsJets)
////					{
////						if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.99F)
////						{
////							FM.AS.setCockpitDoor((Aircraft)FM.actor, 0);
////							HUD.log("CockpitDoorCLS");
////						}
////					}
////				}

			if (bCatapultReferMissionYear){
				if (bIsTypeFastJet || bIsJets) {
					mult = catapultPowerJets;
					if (Mission.curYear() > 1952) {
						int l1 = (int)Math.ceil((((FM.M.getFullMass() - FM.M.massEmpty) / 1000F) * catapultPowerJets) / 10F);
						mult = catapultPowerJets + (float)l1;
					}
				} else {
					mult = catapultPower;
					if (Mission.curYear() > 1952) {
						int i2 = (int)Math.ceil(((FM.M.getFullMass() - FM.M.massEmpty) / 1000F) * 2.0F);
						mult = catapultPower + (float)i2;
					}
				}
			} else {
				if (bIsTypeFastJet || bIsJets) {
					int l1 = (int)Math.ceil((((FM.M.getFullMass() - FM.M.massEmpty) / 1000F) * catapultPowerJets) / 10F);
					mult = catapultPowerJets + (float)l1;
				} else {
					int i2 = (int)Math.ceil(((FM.M.getFullMass() - FM.M.massEmpty) / 1000F) * 2.0F);
					mult = catapultPower + (float)i2;
				}
			}
		}

		//By PAL and western , Catapult Force Based on FullMass and MassEmpty, VminFLAPS, Vmin
		float dist = 0.0F;
		if (bCatapultHookExist[i]) {
			dist = (float)Math.sqrt((catapults[i][1].getY() - catapults[i][0].getY()) * (catapults[i][1].getY() - catapults[i][0].getY()) +
									 (catapults[i][1].getX() - catapults[i][0].getX()) * (catapults[i][1].getX() - catapults[i][0].getX()));
			if (catapultPower == 0.0F) bForceAutoCalculate = true;
		}
		else
			dist = 46F;
		float massX = FM.M.getFullMass();  // Math.min(FM.M.getFullMass(), 20000F); //By PAL and western, empiric factor
		massX *= Aircraft.cvt(Math.max(FM.CT.limitRatioAITakeoffElevatorPlus, FM.CT.trimElevator), 0.0F, 1.0F, 0.20F, 0.28F);
//		massX *= Aircraft.cvt(Math.max(FM.CT.limitRatioAITakeoffElevatorPlus, FM.CT.trimElevator), 0.0F, 1.0F, 0.25F, 0.40F);
//		if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
//			massX *= 0.35F;
//	   } else {
//			massX *= 0.16F;
//		}
		// target velocity m/s on catapult end (for empty loadout and fuel)
		float vTarget = FM.Vmin * 0.7F + FM.VminFLAPS * 0.3F;
		if (bIsTypeFastJet || bIsJets) {
			// When Vmin < 201km/h, vTarget = 240km/h static for all Jets
			if (vTarget < 56F) vTarget = 66.7F;
			// with heavy loadout, the aircraft needs higher take-off speed
			vTarget += (FM.M.getFullMass() - FM.M.massEmpty) * 0.0020F;
		}
		else
			vTarget += (FM.M.getFullMass() - FM.M.massEmpty) * 0.0013F;
		if (vTarget > 85F) vTarget = 85F;   // Limitter for 306km/h on the catapult's end
		carrierSpeedKMH = FM.getSpeedKMH();
		if (bDebugCatapult) {
			System.out.println("*** Catapult vTarget=" + vTarget + " m/s (" + (int)(vTarget * 3.6F) + " km/h) in auto calculation.");
			System.out.println("*** Carrier running speed is " + (int)(carrierSpeedKMH / 3.6F) + "m/s (" + (int)carrierSpeedKMH + "km/h) --- saved into carrierSpeedKMH.");
		}

		double dForceAuto = massX * vTarget * vTarget / dist;
		double dForceTrad = Math.min(FM.M.getFullMass(), 10000F) * mult;
		dForceTrad *= Aircraft.cvt(Math.max(FM.CT.limitRatioAITakeoffElevatorPlus, FM.CT.trimElevator), 0.0F, 1.0F, 0.42F, 0.77F);
//		if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
//			dForceTrad *= 0.75F;
//		} else {
//			dForceTrad *= 0.34F;
//		}
		if (bForceAutoCalculate || !bUseOldCatapultPowerProcedure) {
			dCatapultForce = dForceAuto;
			bDoneAutoCalculate = true;
		} else {
			dCatapultForce = dForceTrad;
			bDoneAutoCalculate = false;
		}

		iCatapultNumber = i;
		bCatapultArmed = true;
		//By PAL, Log Arming Catapult Parameters
		if (bDebugCatapult) {
			System.out.println("*** Catapult No. " + iCatapultNumber + " armed with " + dCatapultForce + " Force! calculated by " + (bDoneAutoCalculate? "Auto": "Traditional") + ", Distance: " + dist);
			System.out.println("*** Catapult power compare: Auto=" + dForceAuto + ", Traditional=" + dForceTrad );
		}
	}

	public void disarmCatapult() {
		bCatapultArmed = false;
		//By PAL, Log Finish of Catapult Effect
		if (bDebugCatapult)
			System.out.println("*** Catapult No. " + iCatapultNumber + " disarmed. Worked for " + (System.currentTimeMillis() - lCatapultStartTime) + "ms, Final V = " + FM.getSpeedKMH() + " km/h");
		//By PAL, important to send a last refresh to Kill Catapult Object
		showCatapult(true, false);
	}

	public boolean isCatapultArmed() {
		return bCatapultArmed;
	}

	public int getCatapultNumber() {
		return iCatapultNumber;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean testNonGearCollision(int i) {
		if (FM.isTick(4, 0) && i >= 7 && FM.Vrel.length() > 1.0D) {
			tempLoc.set(Pnt[i].x, Pnt[i].y, Pnt[i].z + 0.05F, 0.0F, 0.0F, 0.0F);
			if (bPlateConcrete) {
				MiscEffects.bellyLandingConcrete(FM, tempLoc, i, pnti[i]);
				if (World.Rnd().nextFloat() > 0.99F) {
					for (int j = 0; j < FM.AS.astateTankStates.length; j++)
						if (FM.AS.astateTankStates[j] == 1 || FM.AS.astateTankStates[j] == 2) {
							Hook hook = FM.actor.findHook("_Tank" + (j + 1) + "Leak");
							Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
							Loc loc1 = new Loc();
							loc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
							hook.computePos(FM.actor, loc, loc1);
							double d3 = tempLoc.getPoint().distance(loc1.getPoint());
							if (d3 < 2D) FM.AS.hitTank(Engine.actorLand(), j, 6);
						}

				}
			} else if (bPlateSand) {
				MiscEffects.bellyLandingSand(FM, tempLoc, i, pnti[i]);
			} else {
				int k = World.cur().camouflage;
				if (k == 1) MiscEffects.bellyLandingSnow(FM, tempLoc, i, pnti[i]);
				else if (k == 2) MiscEffects.bellyLandingSand(FM, tempLoc, i, pnti[i]);
				else MiscEffects.bellyLandingDirt(FM, tempLoc, i, pnti[i]);
			}
		}
		nOfPoiOnGr++;
		Vs.set(FM.Vrel);
		Vs.scale(-1D);
		FM.Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		if (NormalVPrj > 0.0D) {
			NormalVPrj -= 3D;
			if (NormalVPrj < 0.0D) NormalVPrj = 0.0D;
		}
		double d1 = 1.0D;
		double d2 = d - 0.06D;
		double d4 = d + 0.04D;
		if (d2 > 0.0D) d2 = 0.0D;
		if (d2 < -2D) d2 = -2D;
		if (d4 > 0.0D) d4 = 0.0D;
		if (d4 < -0.25D) d4 = -0.25D;
		d1 = Math.max(-120000D * d2, -360000D * d4);
		d1 *= MassCoeff;
		Tn.scale(d1, Normal);
		fric = -40000D * NormalVPrj;
		if (fric > 100000D) fric = 100000D;
		if (fric < -100000D) fric = -100000D;
		tmpV.scale(fric, Normal);
		Tn.add(tmpV);
		double d5 = 1.0D - (0.5D * (double) Math.abs(Pnt[i].y)) / (double) FM.Arms.WING_END;
		fricF = -8000D * ForwardVPrj;
		fricR = -50000D * RightVPrj;
		fric = Math.sqrt(fricF * fricF + fricR * fricR);
		if (fric > 20000D * d5) {
			fric = (20000D * d5) / fric;
			fricF *= fric;
			fricR *= fric;
		}
		tmpV.scale(fricF * 0.57D, Forward);
		Tn.add(tmpV);
		tmpV.scale(fricR * 0.57D, Right);
		Tn.add(tmpV);
		if (i > 6 && bIsMaster) {
			World.cur();
			if (FM.actor == World.getPlayerAircraft() && FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) < 2D && !bTheBabysGonnaBlow) {
				for (int l = 0; l < FM.CT.Weapons.length; l++) {
					if (FM.CT.Weapons[l] == null || FM.CT.Weapons[l].length <= 0) continue;
					for (int i1 = 0; i1 < FM.CT.Weapons[l].length; i1++)
						if (((FM.CT.Weapons[l][i1] instanceof BombGun) || (FM.CT.Weapons[l][i1] instanceof RocketGun) || (FM.CT.Weapons[l][i1] instanceof RocketBombGun)) && FM.CT.Weapons[l][i1].haveBullets() && FM.getSpeed() > 38F
								&& FM.CT.Weapons[l][i1].getHookName().startsWith("_External")) bTheBabysGonnaBlow = true;

				}

				if (bTheBabysGonnaBlow && (!FM.isPlayers() || World.cur().diffCur.Vulnerability) && ((Aircraft) FM.actor).isEnablePostEndAction(0.0D)) {
					((Aircraft) FM.actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
					if (FM.isPlayers()) HUD.log("FailedBombsDetonate");
				}
			}
		}
		if (bIsMaster && NormalVPrj < 0.0D) {
			double d6 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.00023668639053254438D + NormalVPrj * NormalVPrj * 0.020408163265306121D;
			if (d6 > 1.0D) landHit(i, (float) d6);
		}
		return true;
	}

	// TODO: This method has been extensively edited for the differential braking and steering mod. Please follow comments for changes
	private boolean testGearCollision(int i) {
		if (FM.CT.getGearR() < 0.01F && FM.CT.getGearL() < 0.01F && FM.CT.getGearC() < 0.01F) return false;
		double d1 = 1.0D;
		gWheelSinking[i] = (float) (-d);
		Vs.set(FM.Vrel);
		Vs.scale(-1D);
		FM.Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		if (NormalVPrj > 0.0D) NormalVPrj = 0.0D;
		double d2 = (FM.Vrel.x * FM.Vrel.x + FM.Vrel.y * FM.Vrel.y) - 2D;
		if (d2 < 0.0D) d2 = 0.0D;
		double d3 = 0.01D * d2;
		if (d3 < 0.0D) d3 = 0.0D;
		if (d3 > 4.5D) d3 = 4.5D;
		double d4 = 0.4D * Math.max(roughness * roughness, roughness);
		if (d3 > d4) d3 = d4;
		if (roughness > d3) roughness = d3;
		if (roughness < 0.2D) roughness = 0.2D;
		if (i < 2) {
			d += (double) World.Rnd().nextFloat(-2F, 1.0F) * 0.04D * d3 * MassCoeff;
			d1 = Math.max(-9500D * (d - 0.1D), -950000D * d);
			d1 *= springsStiffness;
		} else {
			d += (double) (World.Rnd().nextFloat(-2F, 1.0F) * 0.04F) * d3 * MassCoeff;
			d1 = Math.max(-9500D * (d - 0.1D), -950000D * d);
			if (Pnt[i].x > 0.0F && Fd.dot(Normal) >= 0.0D) d1 *= 0.45D;
			else d1 *= tailStiffness;
		}
	/////////////////////////////////////////////////////////////////////////////////////
	//  By PAL, Shock Absorber Effect (Vs.z typically between -1..-3 when landing
	/////////////////////////////////////////////////////////////////////////////////////
		d1 -= 40000D * NormalVPrj;
		if (Vs.z > 0.0F) {
			float abs = gWheelSinking[i] * shockAbsorber;
			if (abs > 1.0F) abs = 1.0F;
			Tn.scale(d1 * (1.0F - abs), Normal);
			//System.out.println("Vs=" + Vs.z + ", abs=" + abs);
		}
		else Tn.scale(d1, Normal);
		double d5 = 0.0001D * d1;
		// By western, wheel brakes can work from higher running speed for Fast Jets
		if (FM.actor instanceof TypeFastJet) {
			if (d5 > 5.0D)
				d5 = 5.0D;  // TODO: Limiter to avoid heavy planes bouncing on the ground
		} else {
			if (d5 > 3.3D)
				d5 = 3.3D;  // TODO: Limiter to avoid heavy planes bouncing on the ground
		}
		double d6 = FM.CT.getBrake();
		double d7 = FM.CT.getRudder();
	//By PAL, Differential Brakes
		double db1 = FM.CT.getBrakeRight();
		double db2 = FM.CT.getBrakeLeft();
		double da = db1 - db2; // diff alone
		double db = Math.max(d6, Math.max(db1, db2)); // applied pedal brakes
//By PAL, only for debug
//		if(db > 0.1F)
//		System.out.println("Brakes: " + d6 + ", db1: " + db1 + ", db2: " + db2 + ", Rudder: " + d7);
		// combined with common

		//By PAL, switching which wheel
		switch (i) {
		case 0: // '\0' // MAIN WHEELS ( LEFT & RIGHT )
		case 1: // '\001'
			double d8 = 1.2D;
			if (i == 0) d8 = -1.2D;
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
					if (d7 > 0.1D) db += d8 * db * (d7 - 0.1D); // common * rudder - brake
					// & pedal action
					// combined
					if (d7 < -0.1D) db += d8 * db * (d7 + 0.1D);
					break;
				case 2: // diff combined - common * rudder plus left/right
					// brakes difference
					if (d7 > 0.1D) db += d8 * db * (d7 - 0.1D);
					if (d7 < -0.1D) db += d8 * db * (d7 + 0.1D);
					if (da > 0.1D) db += d8 * 4D * (da - 0.1D);
					if (da < -0.1D) db += d8 * 4D * (da + 0.1D);
					break;
				case 3: // diff - toe brakes (By PAL, if not real independent control I will use Rudder)
					//By PAL, if Pedal Brakes enough strong and no Diff, use only Rudder Control (not Real Diff)
					if (db > 0.25F && Math.abs(da) < 0.1F) {
						if (d7 > 0.1D)
							db += d8 * db * (d7 - 0.1D);
						if (d7 < -0.1D)
							db += d8 * db * (d7 + 0.1D);
					} else {
						if (da > 0.1D)
							db += d8 * 2D * (da - 0.1D); // left/right brakes
						// difference alone
						if (da < -0.1D)
							db += d8 * 2D * (da + 0.1D);
						// if(d6 > 0.1D)
						// db += d8 * 2D * (d6 - 0.1D); // common added (overriding)
						// - in reality there should be no common!
					}
					break;
				case 4: // common * rudder - no pedals - Spitfire & Yak style
					if (d7 > 0.1D) db += d8 * d6 * (d7 - 0.1D);
					if (d7 < -0.1D) db += d8 * d6 * (d7 + 0.1D);
					break;
				}
				if (db > 1.0D) db = 1.0D;
				if (db < 0.0D) db = 0.0D;
				// PAS--
			}
			double d9 = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj);
			if (d9 < 0.01D) d9 = 0.01D;
			double d10 = 1.0D / d9;
			double d11 = ForwardVPrj * d10;
			if (d11 < 0.0D) d11 *= -1D;
			double d12 = RightVPrj * d10;
			if (d12 < 0.0D) d12 *= -1D;
			double d13 = 5D;
			if (((Tuple3d) (PnT)).y * RightVPrj > 0.0D) {
				if (((Tuple3d) (PnT)).y > 0.0D) d13 += 7D * RightVPrj;
				else d13 -= 7D * RightVPrj;
				if (d13 > 20D) d13 = 20D;
			}
			// By western, increase brake power for heavier aircrafts
			double d14 = (double) Aircraft.cvt(FM.M.massEmpty, 50000F, 80000F, 15000F, 20000F);
			if (d9 < 3D) {
				double d15 = -0.333334D * (d9 - 3D);
				// By western, increase brake power for heavier aircrafts
				d14 += (double) Aircraft.cvt(FM.M.massEmpty, 40000F, 60000F, 3000F, 4000F) * d15 * d15;
			}
			// By western, increase brake power for heavier aircrafts
			fricR = -d13 * (double) Aircraft.cvt(FM.M.massEmpty, 50000F, 80000F, 100000F, 130000F) * RightVPrj * d5;
			maxFric = d14 * d5 * d12;
			if (fricR > maxFric) fricR = maxFric;
			if (fricR < -maxFric) fricR = -maxFric;
			fricF = -d13 * 600D * ForwardVPrj * d5;
			maxFric = d13 * Math.max(200D * (1.0D - 0.04D * d9), 5D) * d5 * d11;
			if (fricF > maxFric) fricF = maxFric;
			if (fricF < -maxFric) fricF = -maxFric;
			double d16 = 0.03D;
			if (((Tuple3f) (Pnt[2])).x > 0.0F) d16 = 0.06D;
			double d17 = Math.abs(ForwardVPrj);
			if (d17 < 1.0D) d16 += 3D * (1.0D - d17);
			// TODO: PAS++
			d16 *= 0.03D * db;
			// PAS--
			// By western, increase brake power for heavier aircrafts
			fricF += (double) Aircraft.cvt(FM.M.massEmpty, 50000F, 80000F, -300000F, -400000F) * d16 * ForwardVPrj * d5;
			maxFric = d14 * d5 * d11;
			if (fricF > maxFric) fricF = maxFric;
			if (fricF < -maxFric) fricF = -maxFric;
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
				double d18 = ForwardVPrj * ForwardVPrj * 8.0E-005D + RightVPrj * RightVPrj * 0.0068D;
				if (FM.CT.bHasArrestorControl) d18 += NormalVPrj * NormalVPrj * 0.025D;
				else d18 += NormalVPrj * NormalVPrj * 0.07D;
				if (d18 > 1.0D) {
					fatigue[i] += 10;
					double d21 = 38000D + (double) FM.M.massEmpty * 6D;
					double d23 = (((Tuple3d) (Tn)).x * ((Tuple3d) (Tn)).x * 0.15D + ((Tuple3d) (Tn)).y * ((Tuple3d) (Tn)).y * 0.15D + ((Tuple3d) (Tn)).z * ((Tuple3d) (Tn)).z * 0.08D) / (d21 * d21);
					if (fatigue[i] > 100 || d23 > 1.0D) {
						landHit(i, (float) d18);
						Aircraft aircraft1 = (Aircraft) FM.actor;
						if (i == 0) aircraft1.msgCollision(aircraft1, "GearL2_D0", "GearL2_D0");
						if (i == 1) aircraft1.msgCollision(aircraft1, "GearR2_D0", "GearR2_D0");
					}
				}
			}
			break;

		case 2: // '\002' // FRONT or REAR, AUXILIARY WHEEL
			nOfGearsOnGr++;
			if (bTailwheelLocked && steerAngle > -5F && steerAngle < 5F) {
				gVelocity[i] = ForwardVPrj;
				steerAngle = 0.0F;
				fric = -400D * ForwardVPrj;
				maxFric = 400D;
				if (fric > maxFric) fric = maxFric;
				if (fric < -maxFric) fric = -maxFric;
				tmpV.scale(fric, Forward);
				Tn.add(tmpV);
				fric = -10000D * RightVPrj;
				maxFric = 40000D;
				if (fric > maxFric) fric = maxFric;
				if (fric < -maxFric) fric = -maxFric;
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
					// {if (FM.actor != World.getPlayerAircraft()) System.out.println(">>> Front wheel active steering");
					// msg1 = false;}
					boolean bTaxing = FM.Vwld.length() < 8.33F;  // (now taxing, speed < 30km/h)
					gVelocity[i] = ForwardVPrj;
					if (bTaxing)
						tmpV.set(1.0D, -1.04D * (double) FM.CT.getRudder(), 0.0D); // taxing
					else
						tmpV.set(1.0D, -0.5D * (double) FM.CT.getRudder(), 0.0D); // stock
					// -
					// rudder
					// control
					steerAngleFork.setDeg(steerAngle, (float) Math.toDegrees(Math.atan2(tmpV.y, tmpV.x)));
					steerAngle = steerAngleFork.getDeg(0.115F);
					nwRight.cross(Normal, tmpV);
					nwRight.normalize();
					nwForward.cross(nwRight, Normal);
					ForwardVPrj = nwForward.dot(Vs);
					RightVPrj = nwRight.dot(Vs);
					double d19 = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj);
					if (d19 < 0.01D) d19 = 0.01D;
					maxFric = bTaxing ? (double) Aircraft.cvt(FM.M.getFullMass(), 10000F, 20000F, 4400F, 6000F) : 4000F;
					fricF = -100D * ForwardVPrj;
					if (bTaxing)
						fricR = (double) Aircraft.cvt(FM.M.getFullMass(), 10000F, 20000F, -550F, -2500F) * RightVPrj;  // taxing
					else
						fricR = -500D * RightVPrj;  // stock
					if (fricR > maxFric) fricR = maxFric;
					if (fricR < -maxFric) fricR = -maxFric;
					if (bTaxing)
						fricF -= Math.abs(fricR / (double) Aircraft.cvt(FM.M.getFullMass(), 10000F, 20000F, 8.0F, 2.8F));   // braking effect in taxing turn
					if (fricF > maxFric) fricF = maxFric;
					if (fricF < -maxFric) fricF = -maxFric;
					maxFric = 1.0D - 0.02D * d19;
					if (maxFric < 0.1D) maxFric = 0.1D;
					maxFric = (bTaxing ? 7500D : 5000D) * maxFric;
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
					if (Vs.lengthSquared() > 0.04D) {
						steerAngleFork.setDeg(steerAngle, (float) Math.toDegrees(Math.atan2(Vs.y, Vs.x)));
					//By PAL, make it speed dependent?
//						float f = 0.115F * (float)gVelocity[i];
//						if(f > 1F) f = 1F;
//						steerAngle = steerAngleFork.getDeg(f);
						steerAngle = steerAngleFork.getDeg(0.115F);
					//By PAL, doesn't work OK:
//						TODO: This edit sets limits on degree of rotation for passively steering front wheels
//						if((steerAngleFork.getDeg(0.001F) > -1F) && (steerAngleFork.getDeg(0.001F) < 1F))
//							steerAngle = steerAngleFork.getDeg(0.001F);
//						else if(steerAngleFork.getDeg(0.001F) < -1F)
//							steerAngle = -1F;
//						else if(steerAngleFork.getDeg(0.001F) > 1F)
//							steerAngle = 1F;
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
				if (Vs.lengthSquared() > 0.04D) {
					steerAngleFork.setDeg(steerAngle, (float) Math.toDegrees(Math.atan2(((Tuple3d) (Vs)).y, ((Tuple3d) (Vs)).x)));
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
			if (!bIsMaster || NormalVPrj >= 0.0D) break;
			double d20 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.0001D;
			if (FM.CT.bHasArrestorControl) d20 += NormalVPrj * NormalVPrj * 0.004D;
			else d20 += NormalVPrj * NormalVPrj * 0.02D;
			if (d20 > 1.0D) {
				landHit(i, (float) d20);
				Aircraft aircraft = (Aircraft) FM.actor;
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
		if (!bPlateConcrete && FM.Vrel.length() > 0.1D && World.cur().camouflage == 1) {
			tempLoc.set(Pnt[i].x, Pnt[i].y, Pnt[i].z + 0.05F, 0.0F, 0.0F, 0.0F);
			MiscEffects.gearMarksSnow(FM, tempLoc, i, pnti[i]);
		}
		if (i >= 0 && i < 3) {
			fric_pub[i] = fric;
			fricF_pub[i] = fricF;
			fricR_pub[i] = fricR;
		}
		return true;
	}

	private boolean testWaterCollision(int i) {
		Vs.set(FM.Vrel);
		Vs.scale(-1D);
		FM.Or.transformInv(Vs);
		tmpV.set(Pnt[i]);
		tmpV.cross(FM.getW(), tmpV);
		Vs.add(tmpV);
		ForwardVPrj = Forward.dot(Vs);
		NormalVPrj = Normal.dot(Vs);
		RightVPrj = Right.dot(Vs);
		double d1 = ForwardVPrj;
		if (d1 < 0.0D) d1 = 0.0D;
		if ((!bIsSail || !bInWaterList(i)) && d < -2D) d = -2D;
		double d2 = -(1.0D + 0.3D * d1) * (double) sinkFactor * d * Math.abs(d) * (1.0D + 0.3D * Math.sin((double) ((long) (1 + i % 3) * Time.current()) * 0.001D));
		//double d3 = 0.0001D * d2;
		if (bIsSail && bInWaterList(i)) {
			if (NormalVPrj > 0.0D) NormalVPrj = 0.0D;
			Tn.scale(d2, Normal);
			fric = -1000D * NormalVPrj;
			if (fric > 4000D) fric = 4000D;
			if (fric < -4000D) fric = -4000D;
			tmpV.scale(fric, Normal);
			Tn.add(tmpV);
			fricF = -40D * ForwardVPrj;
			fricR = -300D * RightVPrj;
			fric = Math.sqrt(fricF * fricF + fricR * fricR);
			if (fric > 50000D) {
				fric = 50000D / fric;
				fricF *= fric;
				fricR *= fric;
			}
			tmpV.scale(fricF * 0.5D, Forward);
			Tn.add(tmpV);
			tmpV.scale(fricR * 0.5D, Right);
			Tn.add(tmpV);
		} else {
			Tn.scale(d2, Normal);
			fric = -1000D * NormalVPrj;
			if (fric > 4000D) fric = 4000D;
			if (fric < -4000D) fric = -4000D;
			tmpV.scale(fric, Normal);
			Tn.add(tmpV);
			fricF = -500D * ForwardVPrj;
			fricR = -800D * RightVPrj;
			fric = Math.sqrt(fricF * fricF + fricR * fricR);
			if (fric > 50000D) {
				fric = 50000D / fric;
				fricF *= fric;
				fricR *= fric;
			}
			tmpV.scale(fricF, Forward);
			Tn.add(tmpV);
			tmpV.scale(fricR, Right);
			Tn.add(tmpV);
			if (sinkFactor > 1.0F && !bIsSail) {
				sinkFactor -= 0.4F * Time.tickLenFs();
				if (sinkFactor < 1.0F) sinkFactor = 1.0F;
			}
		}
		if (bIsMaster && NormalVPrj < 0.0D) {
			double d4 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.0004D + NormalVPrj * NormalVPrj * 0.0011111111111111111D;
			if (d4 > 1.0D) landHit(i, (float) d4);
		}
		return true;
	}

	private boolean testPropellorCollision(int i) {
		if (bIsMaster && i >= 3 && i <= 6) {
			if (FM.actor == World.getPlayerAircraft() && !World.cur().diffCur.Realistic_Landings) return false;
			FM.setCapableOfTaxiing(false);
			switch (FM.Scheme) {
			default:
				break;

			case 1: // '\001'
				((Aircraft) FM.actor).hitProp(0, 0, Engine.actorLand());
				break;

			case 2: // '\002'
			case 3: // '\003'
				if (i < 5) ((Aircraft) FM.actor).hitProp(0, 0, Engine.actorLand());
				else ((Aircraft) FM.actor).hitProp(1, 0, Engine.actorLand());
				break;

			case 4: // '\004'
			case 5: // '\005'
			case 8:            // By western, add 8x engines case
			case 10:           // By western, add 10x engines case
				((Aircraft) FM.actor).hitProp(i - 3, 0, Engine.actorLand());
				break;

			case 6: // '\006'
				switch (i) {
				case 3: // '\003'
					((Aircraft) FM.actor).hitProp(0, 0, Engine.actorLand());
					break;

				case 4: // '\004'
				case 5: // '\005'
					((Aircraft) FM.actor).hitProp(1, 0, Engine.actorLand());
					break;

				case 6: // '\006'
					((Aircraft) FM.actor).hitProp(2, 0, Engine.actorLand());
					break;
				}
				break;
			}
			return false;
		} else {
			return true;
		}
	}

	private void landHit(int i, double d1) {
		if (FM.Vrel.length() < 13D || pnti[i] < 0) return;
		if (FM.actor == World.getPlayerAircraft() && !World.cur().diffCur.Realistic_Landings) return;
		ActorHMesh actorhmesh = (ActorHMesh) FM.actor;
		if (!Actor.isValid(actorhmesh)) return;
		Mesh mesh = actorhmesh.mesh();
		long l = Time.tick();
		String s1 = actorhmesh.findHook(mesh.hookName(pnti[i])).chunkName();
		if (s1.compareTo("CF_D0") == 0) {
			if (d1 > 2D) MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
		} else if (s1.compareTo("Tail1_D0") == 0) {
			if (d1 > 1.3D) MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
		} else if ((FM.actor instanceof Scheme1) && s1.compareTo("Engine1_D0") == 0) {
			MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
			if (d1 > 5D) MsgCollision.post(l, actorhmesh, Engine.actorLand(), "CF_D0", "Body");
		} else {
			MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
		}
	}

	public void hitLeftGear() {
		lgear = false;
		FM.brakeShoe = false;
	}

	public void hitRightGear() {
		rgear = false;
		FM.brakeShoe = false;
	}

	public void hitCentreGear() {
		cgear = false;
		FM.brakeShoe = false;
	}

	public boolean isAnyDamaged() {
		return !lgear || !rgear || !cgear;
	}

	private void drawEffects() {
		boolean flag = FM.isTick(16, 0);
		for (int i = 0; i < 3; i++) {
			if (bIsSail && flag && isWater && clp[i] && FM.getSpeedKMH() > 10F) {
				if (clpGearEff[i][0] == null) {
					clpGearEff[i][0] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WakeSmaller.eff", -1F);
					clpGearEff[i][1] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WaveSmaller.eff", -1F);
				}
				continue;
			}
			if (flag && clpGearEff[i][0] != null) {
				Eff3DActor.finish(clpGearEff[i][0]);
				Eff3DActor.finish(clpGearEff[i][1]);
				clpGearEff[i][0] = null;
				clpGearEff[i][1] = null;
			}
		}

		for (int j = 0; j < pnti.length; j++) {
			if (clp[j] && FM.Vrel.length() > 16.667D && !isUnderDeck()) {
				if (clpEff[j] != null) continue;
				if (isWater) effectName = "EFFECTS/Smokes/SmokeAirSplat.eff";
				else if (World.cur().camouflage == 1) effectName = "EFFECTS/Smokes/SmokeAirTouchW.eff";
				else effectName = "EFFECTS/Smokes/SmokeAirTouch.eff";
				clpEff[j] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[j]), new Orient(0.0F, 90F, 0.0F)), 1.0F, effectName, -1F);
				continue;
			}
			if (flag && clpEff[j] != null) {
				Eff3DActor.finish(clpEff[j]);
				clpEff[j] = null;
			}
		}

		//  By PAL, CatapultSteam introduced originally by PAL, Multileveled by Western
		//  By western, steam density is controlled by temperature and weather for artistic screenshot or movie
		if (bCatapultArmed && isUnderDeck() && !FM.brakeShoe) {
			if (FM.brakeShoeLastCarrier != null && catEff == null && bSteamCatapult) {
				FM.actor.pos.getAbs(Aircraft.tmpLoc1);
				FM.brakeShoeLoc.get(Pn);
				Aircraft.tmpLoc1.transform(Pn, PnT);
				float f = Atmosphere.temperature((float) FM.Loc.z) - 273.16F;
				float f1 = f - World.wind().curGust() - (float) (Mission.curCloudsType() * 3);
				if (f1 > 20F) effectName = "3DO/Effects/Ships/CatapultSteam.eff";
				else if (f1 > 10F) effectName = "3DO/Effects/Ships/CatapultSteam2.eff";
				else if (f1 > 0.0F) effectName = "3DO/Effects/Ships/CatapultSteam3.eff";
				else if (f1 > -10F) effectName = "3DO/Effects/Ships/CatapultSteam4.eff";
				else effectName = "3DO/Effects/Ships/CatapultSteam5.eff";
				catEff = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(1.0D, 0.0D, -H -0.25D), new Orient(0.0F, 0.0F, -70F)), 1.0F, effectName, -1F); //By PAL, 25 cm lower
				//catEff = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(-2D, 0.0D, -1.5D), new Orient(0.0F, 0.0F, -70F)), 1.0F, effectName, -1F);
			}
		} else if (flag && catEff != null) {
			Eff3DActor.finish(catEff);
			catEff = null;
		}
		// TODO: --- CTO Mod 4.12 ---
		if (FM.EI.getNum() > 0) {
			for (int k = 0; k < FM.EI.getNum(); k++) {
				FM.actor.pos.getAbs(Aircraft.tmpLoc1);
				Pn.set(FM.EI.engines[k].getPropPos());
				Aircraft.tmpLoc1.transform(Pn, PnT);
				float f = (float) (PnT.z - Engine.cur.land.HQ(PnT.x, PnT.y));
				if (f < 16.2F && FM.EI.engines[k].getThrustOutput() > 0.5F) {
					Pn.x -= f * Aircraft.cvt(FM.Or.getTangage(), -30F, 30F, 8F, 2.0F);
					Aircraft.tmpLoc1.transform(Pn, PnT);
					PnT.z = Engine.cur.land.HQ(PnT.x, PnT.y);
					if (clpEngineEff[k][0] == null) {
						Aircraft.tmpLoc1.transformInv(PnT);
						if (isWater) {
							clpEngineEff[k][0] = Eff3DActor.New(FM.actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust2.eff", -1F);
							clpEngineEff[k][1] = Eff3DActor.New(new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/WhiteEngineWaveTSPD.eff", -1F);
						} else {
							clpEngineEff[k][0] = Eff3DActor.New(FM.actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", -1F);
						}
						continue;
					}
					if (isWater) {
						if (clpEngineEff[k][1] == null) {
							Eff3DActor.finish(clpEngineEff[k][0]);
							clpEngineEff[k][0] = null;
							continue;
						}
					} else if (clpEngineEff[k][1] != null) {
						Eff3DActor.finish(clpEngineEff[k][0]);
						clpEngineEff[k][0] = null;
						Eff3DActor.finish(clpEngineEff[k][1]);
						clpEngineEff[k][1] = null;
						continue;
					}
					Aircraft.tmpOr.set(FM.Or.getAzimut() + 180F, 0.0F, 0.0F);
					clpEngineEff[k][0].pos.setAbs(PnT);
					clpEngineEff[k][0].pos.setAbs(Aircraft.tmpOr);
					clpEngineEff[k][0].pos.resetAsBase();
					if (clpEngineEff[k][1] != null) {
						PnT.z = 0.0D;
						clpEngineEff[k][1].pos.setAbs(PnT);
					}
					continue;
				}
				if (clpEngineEff[k][0] != null) {
					Eff3DActor.finish(clpEngineEff[k][0]);
					clpEngineEff[k][0] = null;
				}
				if (clpEngineEff[k][1] != null) {
					Eff3DActor.finish(clpEngineEff[k][1]);
					clpEngineEff[k][1] = null;
				}
			}

		}
	}

	private void turnOffEffects() {
		if (FM.isTick(69, 0)) {
			for (int i = 0; i < pnti.length; i++)
				if (clpEff[i] != null) {
					Eff3DActor.finish(clpEff[i]);
					clpEff[i] = null;
				}

			for (int j = 0; j < FM.EI.getNum(); j++) {
				if (clpEngineEff[j][0] != null) {
					Eff3DActor.finish(clpEngineEff[j][0]);
					clpEngineEff[j][0] = null;
				}
				if (clpEngineEff[j][1] != null) {
					Eff3DActor.finish(clpEngineEff[j][1]);
					clpEngineEff[j][1] = null;
				}
			}

		  //By PAL and Western, Catapult Effect
			// TODO: +++ CTO Mod 4.12 +++
			if (catEff != null) {
				Eff3DActor.finish(catEff);
				catEff = null;
			}
			// TODO: --- CTO Mod 4.12 ---
		}
	}

	private void processingCollisionEffect() {
		if (!canDoEffect) return;
		Vnorm = FM.Vwld.dot(Normal);
		if (FM.actor == World.getPlayerAircraft() && World.cur().diffCur.Realistic_Landings && Vnorm < -20D && World.Rnd().nextFloat() < 0.02F) {
			canDoEffect = false;
			int i = 20 + (int) (30F * World.Rnd().nextFloat());
			if (FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][0].countBullets() != 0) i = 0;
			if (((Aircraft) FM.actor).isEnablePostEndAction(i)) {
				Eff3DActor eff3dactor = null;
				if (i > 0) {
					Eff3DActor.New(FM.actor, null, new Loc(new Point3d(0.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/FireGND.eff", i);
					eff3dactor = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(0.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/BlackHeavyGND.eff", i + 10);
					((NetAircraft) FM.actor).sfxSmokeState(0, 0, true);
				}
				((Aircraft) FM.actor).postEndAction(i, Engine.actorLand(), 2, eff3dactor);
			}
		}
	}

	public void load(SectFile sectfile) {
		bIsSail = sectfile.get("Aircraft", "Seaplane", 0) != 0;
		sinkFactor = sectfile.get("Gear", "SinkFactor", 1.0F);
		springsStiffness = sectfile.get("Gear", "SpringsStiffness", 1.0F);
		tailStiffness = sectfile.get("Gear", "TailStiffness", 0.6F);
		//By PAL
		shockAbsorber = sectfile.get("Gear", "ShockAbsorber", 0.0F, 0.0F, 1.0F); //By PAL, Default, Min / Max
		//System.out.println("***Gear, ShockAbsorber= " + shockAbsorber + ", sStiffness= " + springsStiffness + ", tStiffness=" + tailStiffness + ", SinkFactor= " + sinkFactor);
		if (sectfile.get("Gear", "FromIni", 0) == 1) {
			H = sectfile.get("Gear", "H", 2.0F);
			Pitch = sectfile.get("Gear", "Pitch", 10F);
		} else {
			H = Pitch = 0.0F;
		}
		String s1 = sectfile.get("Gear", "WaterClipList", "-");
		if (!s1.startsWith("-")) {
			waterList = new int[3 + s1.length() / 2];
			waterList[0] = 0;
			waterList[1] = 1;
			waterList[2] = 2;
			for (int i = 0; i < waterList.length - 3; i++) {
				waterList[3 + i] = 10 * (s1.charAt(i + i) - 48) + 1 * (s1.charAt(i + i + 1) - 48);
				waterList[3 + i] += 3;
			}

		}

		// By western: set each wheels radius values
		double dtemp = (double) sectfile.get("Gear", "WheelDiameterLR", -1.0F);
		if (dtemp > 0D) {
			gWheelRadius[0] = gWheelRadius[1] = dtemp * 0.5D;
			bLoadedWheelRadius = true;
		}
		dtemp = (double) sectfile.get("Gear", "WheelDiameterC", -1.0F);
		if (dtemp > 0D) {
			gWheelRadius[2] = dtemp * 0.5D;
			bLoadedWheelRadius = true;
		}

		// By western: set max nose gear steering degree for realistic ground turn
		gMaxNGearSteeringDegree = (double) sectfile.get("Gear", "NoseGearMaxSteer", -1.0F);

		// By western: custom catapult gear X position and CatGear msh
		gCustomCatGearX = (double) sectfile.get("Gear", "CatGearOffsetX", 0.0F);
		gCatGearMeshName = getS(sectfile, "Gear", "CatGearMesh", null);

		// By western: set Chock Offset and Special msh
		gChockLOffset[0] = (double) sectfile.get("Gear", "ChockLOffsetX", -100.0F);
		gChockLOffset[1] = (double) sectfile.get("Gear", "ChockLOffsetY", -100.0F);
		gChockROffset[0] = (double) sectfile.get("Gear", "ChockROffsetX", -100.0F);
		gChockROffset[1] = (double) sectfile.get("Gear", "ChockROffsetY", -100.0F);
		gChockCOffset[0] = (double) sectfile.get("Gear", "ChockCOffsetX", -100.0F);
		gChockCOffset[1] = (double) sectfile.get("Gear", "ChockCOffsetY", -100.0F);
		gChockLMeshName = getS(sectfile, "Gear", "ChockLMesh", null);
		gChockCMeshName = getS(sectfile, "Gear", "ChockCMesh", null);
		gChockCarrierLMeshName = getS(sectfile, "Gear", "ChockCarrierLMesh", null);
		gChockCarrierCMeshName = getS(sectfile, "Gear", "ChockCarrierCMesh", null);

	}

	public float getLandingState() {
		if (!onGround) return 0.0F;
		float f = 0.4F + ((float) roughness - 0.2F) * 0.5F;
		if (f > 1.0F) f = 1.0F;
		return f;
	}

	public void plateCheck(FlightModel flightmodel) {
		Actor actor = flightmodel.actor;
		if (bUnderDeck) return;
		if (!Actor.isValid(actor)) {
			return;
		} else {
			float f = 200F;
			actor.pos.getAbs(corn);
			plateFilterGear = this;
			Engine.drawEnv().getFiltered((AbstractCollection) null, corn.x - (double) f, corn.y - (double) f, corn.x + (double) f, corn.y + (double) f, 1, plateFilter);
			return;
		}
	}

	public double plateFriction(FlightModel flightmodel) {
		Actor actor = flightmodel.actor;
		if (bUnderDeck) return 0.0D;
		if (!Actor.isValid(actor)) return 0.2D;
		if (!World.cur().diffCur.Realistic_Landings) return 0.2D;
		float f = 200F;
		actor.pos.getAbs(corn);
		bPlateExist = false;
		bPlateGround = false;
		bPlateConcrete = false;
		bPlateSand = false;
		plateFilterGear = this;
		Engine.drawEnv().getFiltered((AbstractCollection) null, corn.x - (double) f, corn.y - (double) f, corn.x + (double) f, corn.y + (double) f, 1, plateFilter);
		if (bPlateExist) return bPlateGround ? 0.8D : 0.0D;
		int i = Engine.cur.land.HQ_RoadTypeHere(flightmodel.Loc.x, flightmodel.Loc.y);
		switch (i) {
		case 1: // '\001'
			return 0.8D;

		case 2: // '\002'
			return 0.0D;

		case 3: // '\003'
			return 5D;
		}
		if (currentBornPlace != null) {
			double d1 = currentBornPlace.getBornPlaceFriction(flightmodel.Loc.x, flightmodel.Loc.y);
			if (d1 != -1D) if (zutiHasPlaneSkisOnWinterCamo && d1 > 2.4D) return 2.4D;
			else return d1;
		}
		currentBornPlace = BornPlace.getCurrentBornPlace(flightmodel.Loc.x, flightmodel.Loc.y);
		return !zutiHasPlaneSkisOnWinterCamo ? 3.8D : 2.4D;
	}

	private String s(int i) {
		return i < 10 ? "0" + i : "" + i;
	}

	private boolean bInWaterList(int i) {
		if (waterList != null) {
			for (int j = 0; j < waterList.length; j++)
				if (waterList[j] == i) return true;

		}
		return false;
	}

	public void zutiCheckPlaneForSkisAndWinterCamo(String s1) {
		for (int i = 0; i < ZUTI_SKIS_AC_CLASSES.length; i++)
			if (s1.endsWith(ZUTI_SKIS_AC_CLASSES[i])) {
				if ("WINTER".equals(Engine.land().config.camouflage)) zutiHasPlaneSkisOnWinterCamo = true;
				else zutiHasPlaneSkisOnWinterCamo = false;
				return;
			}

		zutiHasPlaneSkisOnWinterCamo = false;
	}

	// TODO: +++ CTO Mod 4.12 +++
	// TODO: This method controls carrier catapult offest and positioning. To be edited in future versions so this info is read from an external .ini file
	public boolean setCatapultOffset(BigshipGeneric bigshipgeneric, SectFile theSectFile) {
		boolean flag2 = false;
		bCatapultAI = false;

		String s = bigshipgeneric.getClass().getName();
		int i = s.lastIndexOf('.');
		int j = s.lastIndexOf('$');
		if (i < j) i = j;
		String strSection = s.substring(i + 1);
		if (!flag2) {
			if (!theSectFile.sectionExist(strSection)) strSection = "Default";
			else {
				if (!bStandardDeckCVL && theSectFile.exist(strSection, "dCatapultOffsetXAlt")) {
					dCatapultOffsetX[0] = theSectFile.get(strSection, "dCatapultOffsetXAlt", 0.0F);
					dCatapultOffsetY[0] = theSectFile.get(strSection, "dCatapultOffsetYAlt", 0.0F);
					dCatapultYaw[0] = theSectFile.get(strSection, "dCatapultYawAlt", 0.0F);
					if (iCatapults < 1 && !(dCatapultOffsetX[0] == 0.0D && dCatapultOffsetY[0] == 0.0D)) iCatapults = 1;
				} else {
					dCatapultOffsetX[0] = theSectFile.get(strSection, "dCatapultOffsetX", 0.0F);
					dCatapultOffsetY[0] = theSectFile.get(strSection, "dCatapultOffsetY", 0.0F);
					dCatapultYaw[0] = theSectFile.get(strSection, "dCatapultYaw", 0.0F);
					if (iCatapults < 1 && !(dCatapultOffsetX[0] == 0.0D && dCatapultOffsetY[0] == 0.0D)) iCatapults = 1;
				}
				if (iCatapults > 0) {
					for (int o = 1; o < 4 && theSectFile.exist(strSection, "dCatapultOffsetX" + Integer.toString(o + 1)); o++) {
						dCatapultOffsetX[o] = theSectFile.get(strSection, ("dCatapultOffsetX" + Integer.toString(o + 1)), 0.0F);
						dCatapultOffsetY[o] = theSectFile.get(strSection, ("dCatapultOffsetY" + Integer.toString(o + 1)), 0.0F);
						dCatapultYaw[o] = theSectFile.get(strSection, ("dCatapultYaw" + Integer.toString(o + 1)), 0.0F);
						iCatapults = o + 1;
					}
				}

				catapultPower = theSectFile.get(strSection, "catapultPower", 0.0F);
				catapultPowerJets = theSectFile.get(strSection, "catapultPowerJets", -1.0F);
				if (catapultPowerJets == -1.0F) catapultPowerJets = catapultPower;
				if (theSectFile.get(strSection, "bSteamCatapult", 0) == 1) bSteamCatapult = true;
				if (iCatapults > 0) {
					flag2 = true;
					bCatapultAI = bCatapultAllowAI;
				}
			}
		}

		bHasBlastDeflector = (bigshipgeneric instanceof TypeBlastDeflector);

		HierMesh carrierhiermesh = bigshipgeneric.hierMesh();

		int k = 0;
		for (; k < 4; k++) {
			if (findHook(carrierhiermesh, "_SCat" + (k + 1) + "_start", catapults[k][0])) {
				if (findHook(carrierhiermesh, "_SCat" + (k + 1) + "_end", catapults[k][1])) {
					dCatapultYaw[k] = Math.toDegrees(Math.atan((double) (catapults[k][1].getY() - catapults[k][0].getY()) / (double) (catapults[k][1].getX() - catapults[k][0].getX())));
					bSteamCatapult = true;
					bCatapultHookExist[k] = true;
				} else {
					System.out.println("Gear - 1935 : Hooks mismatching _SCat" + (k + 1) + "_start and _SCat" + k + 1 + "_end");
					break;
				}
			}
			else if (findHook(carrierhiermesh, "_Cat" + (k + 1) + "_start", catapults[k][0])) {
				if (findHook(carrierhiermesh, "_Cat" + (k + 1) + "_end", catapults[k][1])) {
					dCatapultYaw[k] = Math.toDegrees(Math.atan((double) (catapults[k][1].getY() - catapults[k][0].getY()) / (double) (catapults[k][1].getX() - catapults[k][0].getX())));
					bSteamCatapult = false;
					bCatapultHookExist[k] = true;
				} else {
					System.out.println("Gear - 1945 : Hooks mismatching _Cat" + (k + 1) + "_start and _Cat" + k + "_end");
					break;
				}
			}
			else break;
		}

		if (k > 0) {
			iCatapults = k;
			flag2 = true;
			bCatapultAI = bCatapultAllowAI;
		}

		return flag2;
	}

	private boolean findHook(HierMesh hiermesh, String s, Loc loc1) {
		int i = hiermesh.hookFind(s);
		if (i == -1) {
			return false;
		} else {
			Point3d pp = new Point3d();
			Orient po = new Orient();
			Matrix4d pm1 = new Matrix4d();
			double ptmp[] = new double[3];
			hiermesh.hookMatrix(i, pm1);
			pm1.getEulers(ptmp);
			po.setYPR(Geom.RAD2DEG((float)ptmp[0]), 360F - Geom.RAD2DEG((float)ptmp[1]), 360F - Geom.RAD2DEG((float)ptmp[2]));
			pp.set(pm1.m03, pm1.m13, pm1.m23);
			loc1.set(pp, po);
			return true;
		}
	}

	public double getCatapultOffsetX(int i) {
		return dCatapultOffsetX[i];
	}

	public double getCatapultOffsetY(int i) {
		return dCatapultOffsetY[i];
	}

	public double getCatapultOffsetX() {
		return getCatapultOffsetX(0);
	}

	public double getCatapultOffsetY() {
		return getCatapultOffsetY(0);
	}

	public double getCatapultOffsetX2() {
		return getCatapultOffsetX(1);
	}

	public double getCatapultOffsetY2() {
		return getCatapultOffsetY(1);
	}

	public boolean getCatapultAI() {
		return bCatapultAI;
	}

	public int getCatapultAlreadySetNum() {
		return iCatapultAlreadySetNum;
	}

	public double getD0() {
		return D0;
	}

	//By western, "setter" is needed.
	public void setD0(double d0) {
		D0 = d0;
	}

	static {
		Pnt = new Point3f[64];
		for (int i = 0; i < Pnt.length; i++)
			Pnt[i] = new Point3f();

	}

// --------------------------------------------------------
///////////////////////////////////////////////////////////////////////////
// By PAL, for Chocks MOD
///////////////////////////////////////////////////////////////////////////
	private boolean bChocks = false;
	private boolean bCatHook = false;
	static private boolean bShowChocks = false;
	static private boolean bShowChocksLandAuxiliar = false;
	static public boolean bShowChocksLandAuxiliarFIXsize = false;
	static public boolean bUseChocksParking = false;
	static private boolean bShowCatGear = false;
	private ActorSimpleMesh ChL = null;
	private ActorSimpleMesh ChR = null;
	private ActorSimpleMesh ChC = null;
	private ActorSimpleMesh CatH = null;
	private Loc locL = new Loc();
	private Loc locL0 = new Loc();
	private Loc locR = new Loc();
	private Loc locR0 = new Loc();
	private Loc locC = new Loc();
	private Loc locCatH = new Loc();
	private LandAux laL = null;
	private LandAux laR = null;
	private HookNamed hookl = null;
	private HookNamed hookr = null;
	private HookNamed hookc = null;
// --------------------------------------------------------


	//By PAL, for Chocks, show them
	private void showCatapult(boolean isCarrier, boolean isCatapult) {
		//By PAL, if not activated, don't do anything.
		if (!bShowCatGear) return;

		if (!isCarrier) return;

		if (isCatapult == bCatHook) {
			if (bCatHook && CatH != null) {
				Loc locGearC = new Loc();
				Loc locTemp = new Loc();
				Orientation orTemp = new Orientation();
				locTemp.set(FM.Loc.x, FM.Loc.y, FM.Loc.z,
						FM.Or.getAzimut(), FM.Or.getTangage(), FM.Or.getKren());
				corn.set(FM.Loc);
				corn1.set(FM.Loc);
				corn1.z -= 20D;
				Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
				//By PAL, why this is 0.5D over the collision line?
				if (FM.CT.bHasCatLaunchBarControl) {
					hookc = (HookNamed)FM.actor.findHook("_ClipCGear");
					hookc.computePos(FM.actor, locTemp, locGearC);
					locGearC.get(corn);
					locGearC.get(corn1);
					corn1.z -= 20D;
					actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
				}
				Pship.z += 0.5D;
				Orient ori = new Orient();
				ori = FM.brakeShoeLastCarrier.pos.getAbsOrient();
				ori.setYaw(ori.getYaw() + (float)dCatapultYaw[getCatapultNumber()]);
				locCatH.set(Pship, ori);
				CatH.pos.setAbs(locCatH);
			}
			return;
		}
		bCatHook = isCatapult;
		//By PAL, now I check status of Catapult
		if (!bCatHook) {
			//By PAL, this will destroy Catapult Hook
			if (CatH != null) {
				CatH.destroy();
				CatH = null;
				//By western, move Catapult launch bar
				if (FM.CT.bHasCatLaunchBarControl) {
					FM.CT.CatLaunchBarControl = 0.0F;
				}
				//System.out.println("Catapult Released...");
			}
		} else {
			//By PAL, this will destroy Catapult Hook
			if (CatH != null) {
				CatH.destroy();
				CatH = null;
			}

			ActorHMesh actorhmesh = (ActorHMesh)FM.actor;
			HierMesh hiermesh = actorhmesh.hierMesh();

			//By PAL, new in Test, pose Hooks
			for (int i = 0; i < 3; i++)
				if (pnti[i] < 0) return;
			//By PAL, Left Wheel
			HM.hookMatrix(pnti[0], M4);
			double PosLx = M4.m03;
			double PosLy = M4.m13;
			double PosLz = M4.m23;
			//By PAL, Right Wheel
			HM.hookMatrix(pnti[1], M4);
			double PosRx = M4.m03;
			double PosRy = M4.m13;
			double PosRz = M4.m23;
			//By PAL, Third Wheel
			HM.hookMatrix(pnti[2], M4);
			double PosCatHx = M4.m03;
			double PosCatHy = M4.m13;
			double PosCatHz = M4.m23;

			//By PAL, ***********create Catapult Hook only in Carrier***********
			try {
				//By PAL, I'm working based on the Wheel Hook now.
				hookc = (HookNamed)FM.actor.findHook("_ClipCGear");
				if (hookc == null)
					System.out.println("_ClipCGear not found! on " + this);
				else {
					//By PAL, the wheel could be: hiermesh.chunkFind("GearL4_D0") / "GearL3_D0" / "GearL2_D0" / "GearL1_D0"
					if (bDebugCatapult)
						System.out.println("Chunk Central Landing Gear: '" + hookc.chunkName() + "' on " + this);
					//By PAL, set the one where the Hook is
					hiermesh.setCurChunk(hookc.chunkNum());
					hiermesh.getChunkLocObj(locCatH);
					locCatH.set(bFrontWheel ? PosCatHx : (PosLx + PosRx) / 2,
								//By PAL, H of CG in 45degrees in front
								H * 1.5F, //PosCatHy,
								bFrontWheel ? PosCatHz + gWheelSinking[2] : (PosLz + gWheelSinking[0] + PosRz + gWheelSinking[1]) / 2,
								0F, -Pitch, 0F);
					locCatH.add(FM.actor.pos.getCurrent());
					//By PAL, load Cable Hook for Catapult
					//By western, move Catapult launch bar and draw shuttle
					Loc locGearC = new Loc();
					Loc locTemp = new Loc();
					Orientation orTemp = new Orientation();
					locTemp.set(FM.Loc.x, FM.Loc.y, FM.Loc.z,
							FM.Or.getAzimut(), FM.Or.getTangage(), FM.Or.getKren());
					if (FM.CT.bHasCatLaunchBarControl) {
						hookc.computePos(FM.actor, locTemp, locGearC);
						locGearC.get(corn);
						locGearC.get(corn1);
						corn1.z -= 20D;
						Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
						Pship.z += 0.5D;
						Orient ori = new Orient();
						ori = FM.brakeShoeLastCarrier.pos.getAbsOrient();
						ori.setYaw(ori.getYaw() + (float)dCatapultYaw[getCatapultNumber()]);
						locCatH.set(Pship, ori);
						FM.CT.CatLaunchBarControl = 1.0F;
						CatH = new ActorSimpleMesh("3DO/Arms/CatHook/Shuttle.sim");
						CatH.pos.setAbs(locCatH);
					} else {
						corn.set(FM.Loc);
						corn1.set(FM.Loc);
						corn1.z -= 20D;
						Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
						Pship.z += 0.5D;
						Orient ori = new Orient();
						ori = FM.brakeShoeLastCarrier.pos.getAbsOrient();
						ori.setYaw(ori.getYaw() + (float)dCatapultYaw[getCatapultNumber()]);
						locCatH.set(Pship, ori);
						if (gCatGearMeshName != null)
							CatH = new ActorSimpleMesh(gCatGearMeshName);
						else
							CatH = new ActorSimpleMesh("3DO/Arms/CatHook/CableHook.sim");
						CatH.pos.setAbs(locCatH);
//						CatH.mesh().setScaleXYZ(1F, H / 1.4F, H / 1.4F); //1.5F);
					}
				}
			}
			catch (Exception e){}
		}
	}

	private void showChocks(boolean isCarrier, boolean isCatapult) {
		//By PAL, if not activated, don't do anything.
		if (!bShowChocks) return;

		//By western, if SeaPlane variants, don't do anything.
		if (FM.actor instanceof TypeSeaPlane) return;

		//By PAL, with Catapult it shouldn't show any Chock
		if (isCatapult) {
			//By PAL and western, this will destroy Chock
			if (ChL != null) {
				ChL.destroy();
				ChL = null;
			}
			if (ChR != null) {
				ChR.destroy();
				ChR = null;
			}
			if (ChC != null) {
				ChC.destroy();
				ChC = null;
			}
			return;
		}

		//By PAL, if brakeShoe doesn't change, don't do anything.
		if (FM.brakeShoe == bChocks)
			return;
		bChocks = FM.brakeShoe;
		if (!bChocks) {
			//By PAL, this will destroy Chock and start Land Auxiliar (if it was not a carrier)
			if (ChL != null) {
				if (!isCarrier && bShowChocksLandAuxiliar) {
					ChL.pos.getAbs(locL); //By PAL, recupero la posición actual del Chock L.
					Orient otemp = new Orient();
					otemp.set(locL.getOrient()); //FM.actor.pos.getAbs()
					Orient opar = new Orient();
					opar.set(otemp.getAzimut() + 180F, 0F, 0F);
					opar.wrap360();
					otemp.increment(90F, 0F, 0F); //By PAL, perpendicular
					//By PAL, for single engines, some degrees in advance
					if (FM.EI.engines.length == 1)
						otemp.increment(30F, 0F, 0F);
					otemp.wrap360();
					locL.set(otemp);
					laL = new LandAux(FM.actor, FM.actor.getArmy(), locL, opar);
				}
				ChL.destroy();
				ChL = null;
			}

			//By PAL, this will destroy Chock and start Land Auxiliar (if it was not a carrier)
			if (ChR != null) {
				if (!isCarrier && bShowChocksLandAuxiliar) {
					ChR.pos.getAbs(locR); //By PAL, recupero la posición actual del Chock R.
					Orient otemp = new Orient();
					otemp.set(locR.getOrient()); //FM.actor.pos.getAbs()
					Orient opar = new Orient();
					opar.set(otemp.getAzimut(), 0F, 0F);
					opar.wrap360();
					//By PAL, perpendicular
					otemp.increment(90F, 0F, 0F);
					//By PAL, for single engines, some degrees in advance
					if (FM.EI.engines.length == 1)
						otemp.increment(-30F, 0F, 0F);
					otemp.wrap360();
					locR.set(otemp);
					laR = new LandAux(FM.actor, FM.actor.getArmy(), locR, opar);
				}
				ChR.destroy();
				ChR = null;
			}
			//By western, this will destroy Chock , but no Land Auxiliar for center gear's Chock
			if (ChC != null) {
				ChC.destroy();
				ChC = null;
			}
		} else {
		//By PAL, if chocks are set in front of the Catapult, I don't have to show them!
			ActorHMesh actorhmesh = (ActorHMesh)FM.actor;
			if (!Actor.isValid(actorhmesh)) {
				System.out.println("ERROR: Gear 2264 - Not valid Actor Mesh for Chocks");
				return;
			}
			HierMesh hiermesh = actorhmesh.hierMesh();

			//By PAL, new in Test, pose Hooks
			for (int i = 0; i < 3; i++)
				if (pnti[i] < 0) return;
			//By PAL, Left Wheel
			HM.hookMatrix(pnti[0], M4);
			Point3d pLgr = new Point3d(M4.m03, M4.m13, M4.m23);
			//By PAL, Right Wheel
			HM.hookMatrix(pnti[1], M4);
			Point3d pRgr = new Point3d(M4.m03, M4.m13, M4.m23);
			//By PAL and western, Center Wheel
			HM.hookMatrix(pnti[2], M4);
			Point3d pCgr = new Point3d(M4.m03, M4.m13, M4.m23);

			//By PAL, ***********create Chocks for Left Wheel***********
			try {
				//By PAL, if the Land Auxiliar still exists, destroy it
				if (laL != null) {
					laL.destroy();
					laL = null;
				}
				//By PAL, I'm working based on the Wheel Hook now.
				hookl = (HookNamed)FM.actor.findHook("_ClipLGear");
				if (hookl == null)
					System.out.println("ERROR: _ClipLGear not found! on " + this + " .... cannot set Chock 3d msh!");
				else {
					Orient tempO = new Orient(0F, Pitch, 0F);
					Point3d tempP = new Point3d(0D, 0D, gWheelSinking[0]);
					tempO.transform(tempP);
					pLgr.add(tempP);
					locL.set(pLgr);
					locL.set(new Orient(180F, Pitch, 0F));
					if (gChockLOffset[0] != -100.0D) {
						tempP.set(gChockLOffset[0], 0D, -gChockLOffset[0] * Math.tan(Math.toRadians(Pitch)));
						locL.add(tempP);
					}
					else if (Pitch > 0.8D && Pitch < 310D) {
						tempP.set(-Pitch * 0.006D, 0D, Pitch * 0.006D * Math.tan(Math.toRadians(Pitch)));
						locL.add(tempP);
					}
					if (gChockLOffset[1] != -100.0D) {
						tempP.set(0D, gChockLOffset[1], 0D);
						locL.add(tempP);
					}
					locL.add(FM.actor.pos.getCurrent());
					if (!Engine.cur.land.isWater(locL.getX(), locL.getY()) || bUnderDeck)
					{
						ChL = getChockMesh();
						ChL.pos.setBase(FM.actor, hookl, false);
						ChL.pos.setAbs(locL);
						ChL.pos.changeHookToRel();
						ChL.pos.resetAsBase();
					}
				}
			}
			catch (Exception e){ }

			//By PAL, **********create Right Wheel Chocks**********
			try {
				//By PAL, if the Land Auxiliar still exists, destroy it
				if (laR != null) {
					laR.destroy();
					laR = null;
				}
				//By PAL, I'm working based on the Wheel Hook now.
				hookr = (HookNamed)FM.actor.findHook("_ClipRGear");
				if (hookr == null)
					System.out.println("ERROR: _ClipRGear not found! on " + this + " .... cannot set Chock 3d msh!");
				else {
					Orient tempO = new Orient(0F, Pitch, 0F);
					Point3d tempP = new Point3d(0D, 0D, gWheelSinking[1]);
					tempO.transform(tempP);
					pRgr.add(tempP);
					locR.set(pRgr);
					locR.set(new Orient(0F, -Pitch, 0F));
					if (gChockROffset[0] != -100.0D) {
						tempP.set(gChockROffset[0], 0D, -gChockROffset[0] * Math.tan(Math.toRadians(Pitch)));
						locR.add(tempP);
					}
					else if (gChockLOffset[0] != -100.0D) {
						tempP.set(gChockLOffset[0], 0D, -gChockLOffset[0] * Math.tan(Math.toRadians(Pitch)));
						locR.add(tempP);
					}
					else if (Pitch > 0.8D && Pitch < 310D) {
						tempP.set(-Pitch * 0.006D, 0D, Pitch * 0.006D * Math.tan(Math.toRadians(Pitch)));
						locR.add(tempP);
					}
					if (gChockROffset[1] != -100.0D) {
						tempP.set(0D, gChockROffset[1], 0D);
						locR.add(tempP);
					}
					else if (gChockLOffset[1] != -100.0D) {
						tempP.set(0D, -gChockLOffset[1], 0D);
						locR.add(tempP);
					}
					locR.add(FM.actor.pos.getCurrent());
					if (!Engine.cur.land.isWater(locR.getX(), locR.getY()) || bUnderDeck)
					{
						ChR = getChockMesh();
						ChR.pos.setBase(FM.actor, hookr, false);
						ChR.pos.setAbs(locR);
						ChR.pos.changeHookToRel();
						ChR.pos.resetAsBase();
					}
				}
			}
			catch (Exception e){ }

			//By PAL and western, **********create Center Wheel Chocks when MeshName is defined**********
			if (gChockCMeshName != null) {
				try {
					//By PAL, I'm working based on the Wheel Hook now.
					hookc = (HookNamed)FM.actor.findHook("_ClipCGear");
					if (hookc == null)
						System.out.println("ERROR: _ClipCGear not found! on " + this + " .... cannot set Chock 3d msh!");
					else {
						Orient tempO = new Orient(0F, Pitch, 0F);
						Point3d tempP = new Point3d(0D, 0D, gWheelSinking[2]);
						tempO.transform(tempP);
						pCgr.add(tempP);
						locC.set(pCgr);
						locC.set(new Orient(0F, -Pitch, 0F));
						if (gChockCOffset[0] != -100.0D) {
							tempP.set(gChockCOffset[0], 0D, -gChockCOffset[0] * Math.tan(Math.toRadians(Pitch)));
							locC.add(tempP);
						}
						else if (Pitch > 0.8D && Pitch < 310D) {
							tempP.set(-Pitch * 0.006D, 0D, Pitch * 0.006D * Math.tan(Math.toRadians(Pitch)));
							locC.add(tempP);
						}
						if (gChockCOffset[1] != -100.0D) {
							tempP.set(0D, gChockCOffset[1], 0D);
							locC.add(tempP);
						}
						locC.add(FM.actor.pos.getCurrent());
						if (!Engine.cur.land.isWater(locC.getX(), locC.getY()) || bUnderDeck)
						{
							ChC = new ActorSimpleMesh(gChockCMeshName);
							ChC.pos.setBase(FM.actor, hookc, false);
							ChC.pos.setAbs(locC);
							ChC.pos.changeHookToRel();
							ChC.pos.resetAsBase();
						}
					}
				}
				catch (Exception e){}
			}
		}
	}

	private ActorSimpleMesh getChockMesh() {
		ActorSimpleMesh asm = null;
		if (bUnderDeck) {
			if (gChockCarrierLMeshName != null) {
				asm = new ActorSimpleMesh(gChockCarrierLMeshName);
				return asm;
			}
			String s = ((BigshipGeneric) FM.brakeShoeLastCarrier).getClass().getName();
			int i = s.lastIndexOf('.');
			int j = s.lastIndexOf('$');
			if (i < j) i = j;
			String sShipName = s.substring(i + 1);
			if (sShipName.startsWith("USS")) {
				if (gWheelRadius[0] > 0.40D)
					asm = new ActorSimpleMesh("3DO/Arms/ChocksUSN/monoL.sim");
				else
					asm = new ActorSimpleMesh("3DO/Arms/ChocksUSN/mono.sim");
			} else {
				if (gWheelRadius[0] > 0.50D)
					asm = new ActorSimpleMesh("3DO/Arms/ChocksNavy/monoL.sim");
				else
					asm = new ActorSimpleMesh("3DO/Arms/ChocksNavy/mono.sim");
			}
		} else {
			if (gChockLMeshName != null) {
				asm = new ActorSimpleMesh(gChockLMeshName);
				return asm;
			}
			float fyS = Property.floatValue(FM.actor.getClass(), "yearService", 0.0F);
			String soC = Property.stringValue(FM.actor.getClass(), "originCountry", null);
			if (soC != null && soC.equals(PaintScheme.countryUSA) && fyS > 1950.0F)
				asm = new ActorSimpleMesh("3DO/Arms/ChocksUSJet/mono.sim");
			else if (soC != null && soC.equals(PaintScheme.countryRussia) && fyS > 1950.0F)
				asm = new ActorSimpleMesh("3DO/Arms/ChocksRusJet1950/mono.sim");
			else {
				if (gWheelRadius[0] > 0.50D)
					asm = new ActorSimpleMesh("3DO/Arms/Chocks/monoL.sim");
				else
					asm = new ActorSimpleMesh("3DO/Arms/Chocks/mono.sim");
			}
		}
		return asm;
	}

	private void loadWheelRadius(Aircraft aircraft) {
		if (aircraft instanceof A_20) {
			gWheelRadius[0] = gWheelRadius[1] = 0.565D;  // main Tire Diameter 44.5"
			gWheelRadius[2] = 0.325D;					// aux Tire Diameter 25.61"
			gChockLOffset[0] = -0.20D;
		}
		else if (aircraft instanceof B_17) {
			gWheelRadius[0] = gWheelRadius[1] = 0.718D;  // main Tire Diameter 56.56"
			gWheelRadius[2] = 0.325D;					// aux Tire Diameter 25.61"
			gChockLOffset[0] = -0.28D;
		}
		else if (aircraft instanceof B_24) {
			gWheelRadius[0] = gWheelRadius[1] = 0.718D;  // main Tire Diameter 56.56"
			gWheelRadius[2] = 0.459D;					// aux Tire Diameter 36.15"
		}
		else if (aircraft instanceof B_25) {
			gWheelRadius[0] = gWheelRadius[1] = 0.468D;  // main Tire Diameter 36.86"
			gWheelRadius[2] = 0.468D;					// aux Tire Diameter 36.86"
		}
		else if (aircraft instanceof B_29X) {
			gWheelRadius[0] = gWheelRadius[1] = 0.718D;  // main Tire Diameter 56.56"
			gWheelRadius[2] = 0.459D;					// aux Tire Diameter 36.15"
			gChockLOffset[0] = -0.17D;
		}
		else if (aircraft instanceof C_47) {
			gWheelRadius[0] = gWheelRadius[1] = 0.567D;  // main Tire Diameter 44.71"
			gWheelRadius[2] = 0.278D;					// aux Tire Diameter 21.86"
		}
		else if (aircraft instanceof P_38) {
			gWheelRadius[0] = gWheelRadius[1] = 0.459D;  // main Tire Diameter 36.15"
			gWheelRadius[2] = 0.349D;					// aux Tire Diameter 27.5"
			gChockLOffset[0] = 0.04D;
			gChockLOffset[1] = 0.01D;
		}
		else if ((aircraft instanceof P_40) || (aircraft instanceof P_40SUKAISVOLOCH)) {
			gWheelRadius[0] = gWheelRadius[1] = 0.349D;  // main Tire Diameter 27.5"
			gWheelRadius[2] = 0.156D;					// aux Tire Diameter 12.29"
			gChockLOffset[0] = -0.02D;
			gChockLOffset[1] = 0.09D;
		}
		else if (aircraft instanceof P_47) {
			gWheelRadius[0] = gWheelRadius[1] = 0.421D;  // main Tire Diameter 33.20"
			gWheelRadius[2] = 0.183D;					// aux Tire Diameter 14.48"
			gChockLOffset[0] = -0.20D;
		}
		else if (aircraft instanceof P_51) {
			gWheelRadius[0] = gWheelRadius[1] = 0.349D;  // main Tire Diameter 27.5"
			gWheelRadius[2] = 0.156D;					// aux Tire Diameter 12.35"
			gChockLOffset[0] = -0.17D;
		}
		else if (aircraft instanceof P_80) {
			gWheelRadius[0] = gWheelRadius[1] = 0.323D;  // main Tire Diameter 25.4"
			gWheelRadius[2] = 0.280D;					// aux Tire Diameter 22.0"
		}
		else if (aircraft instanceof F2A) {
			gWheelRadius[0] = gWheelRadius[1] = 0.375D;  // main Tire Diameter 29.5"
			gWheelRadius[2] = 0.148D;					// aux Tire Diameter 11.65"
			gChockLOffset[0] = -0.10D;
		}
		else if (aircraft instanceof F4F) {
			gWheelRadius[0] = gWheelRadius[1] = 0.377D;  // main Tire Diameter 29.66"
			gWheelRadius[2] = 0.152D;					// aux Tire Diameter 12"
		}
		else if (aircraft instanceof F6F) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_F6F.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.389D;  // main Tire Diameter 30.69"
			gWheelRadius[2] = 0.156D;					// aux Tire Diameter 12.35"
			gChockLOffset[0] = -0.05D;
			gChockLOffset[1] = 0.16D;
		}
		else if (aircraft instanceof F4U) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_F4U.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.389D;  // main Tire Diameter 30.69"
			gWheelRadius[2] = 0.156D;					// aux Tire Diameter 12.35"
			gChockLOffset[0] = -0.08D;
			gChockLOffset[1] = -0.03D;
		}
		else if (aircraft instanceof SBD) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_SBD.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.371D;  // main Tire Diameter 29.2"
			gWheelRadius[2] = 0.156D;					// aux Tire Diameter 12.35"
			gChockLOffset[0] = -0.13D;
		}
		else if (aircraft instanceof TBD) {
			gWheelRadius[0] = gWheelRadius[1] = 0.398D;  // main Tire Diameter 31.3"
			gWheelRadius[2] = 0.092D;					// aux Tire Diameter 7.2"
			gChockLOffset[0] = -0.11D;
			gChockLOffset[1] = 0.04D;
			gChockROffset[1] = -0.35D;
		}
		else if (aircraft instanceof TBF) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_TBF.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.438D;  // main Tire Diameter 34.5"
			gWheelRadius[2] = 0.147D;					// aux Tire Diameter 11.56"
			gChockLOffset[0] = -0.16D;
			gChockLOffset[1] = 0.03D;
		}
		else if (aircraft instanceof BF_109) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Bf109.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.323D;  // main Tire Diameter 25.4"
			gWheelRadius[2] = 0.156D;					// aux Tire Diameter 12.35"
			gChockLOffset[0] = -0.12D;
		}
		else if (aircraft instanceof FW_190) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Fw190A.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.336D;  // main Tire Diameter 26.5"
			gWheelRadius[2] = 0.181D;					// aux Tire Diameter 14.25"
			gChockLOffset[0] = -0.13F;
		}
		else if (aircraft instanceof JU_87) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_F4U.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.412D;  // main Tire Diameter 32.44"
			gWheelRadius[2] = 0.153D;					// aux Tire Diameter 12.05"
		}
		else if ((aircraft instanceof JU_88) || (aircraft instanceof JU_88NEW)) {
			gWheelRadius[0] = gWheelRadius[1] = 0.570D;  // main Tire Diameter 44.94"
			gWheelRadius[2] = 0.260D;					// aux Tire Diameter 20.48"
		}
		else if (aircraft instanceof BEAU) {
			gWheelRadius[0] = gWheelRadius[1] = 0.531D;  // main Tire Diameter 41.8"
			gWheelRadius[2] = 0.232D;					// aux Tire Diameter 18.25"
		}
		else if (aircraft instanceof Fulmar) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Fulmar.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.404D;  // main Tire Diameter 31.5"
			gWheelRadius[2] = 0.134D;					// aux Tire Diameter 10.55"
		}
		else if (aircraft instanceof GLADIATOR) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Gladiator.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.404D;  // main Tire Diameter 31.5"
			gWheelRadius[2] = 0.161D;					// aux Tire Diameter 12.67"
		}
		else if (aircraft instanceof Hurricane) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Hurricane.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.323D;  // main Tire Diameter 25.4"
			gWheelRadius[2] = 0.149D;					// aux Tire Diameter 10.75"
		}
		else if (aircraft instanceof MOSQUITO) {
			gWheelRadius[0] = gWheelRadius[1] = 0.546D;  // main Tire Diameter 43"
			gWheelRadius[2] = 0.239D;					// aux Tire Diameter 18.85"
		}
		else if (aircraft instanceof SPITFIRE) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Seafire.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.308D;  // main Tire Diameter 28.2"
			gWheelRadius[2] = 0.128D;					// aux Tire Diameter 10.1"
		}
		else if (aircraft instanceof Swordfish) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Swordfish.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.36D;  // main Tire Diameter 28.35"
			gWheelRadius[2] = 0.136D;					// aux Tire Diameter 10.7"
		}
		else if (aircraft instanceof TEMPEST) {
			gWheelRadius[0] = gWheelRadius[1] = 0.381D;  // main Tire Diameter 30.0"
			gWheelRadius[2] = 0.191D;					// aux Tire Diameter 15.0"
		}
		else if (aircraft instanceof Wellington) {
			gWheelRadius[0] = gWheelRadius[1] = 0.648D;  // main Tire Diameter 51"
			gWheelRadius[2] = 0.238D;					// aux Tire Diameter 18.75"
		}
		else if (aircraft instanceof CR_42X) {
			gWheelRadius[0] = gWheelRadius[1] = 0.310D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.128D;					// aux Tire Diameter measured by 3d shape
		}
		else if (aircraft instanceof G50) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_SBD.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.315D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.140D;					// aux Tire Diameter measured by 3d shape
		}
		else if (aircraft instanceof G_55xyz) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_G55.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.286D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.150D;					// aux Tire Diameter measured by 3d shape
		}
		else if ((aircraft instanceof MC_200xyz) || (aircraft instanceof MC_202xyz)) {
			gWheelRadius[0] = gWheelRadius[1] = 0.310D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.145D;					// aux Tire Diameter measured by 3d shape
		}
		else if (aircraft instanceof RE_2000xyz) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Re2002.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.336D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.287D;					// aux Tire Diameter measured by 3d shape
			gChockLOffset[0] = 0.13D;
			gChockLOffset[1] = 0.20D;
		}
		else if (aircraft instanceof RE_2002xyz) {
			if (gCatGearMeshName == null)
				gCatGearMeshName = "3DO/Arms/CatHook/CableHook_Re2002.sim";
			gWheelRadius[0] = gWheelRadius[1] = 0.336D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.166D;					// aux Tire Diameter measured by 3d shape
			gChockLOffset[0] = 0.13D;
			gChockLOffset[1] = 0.20D;
		}
		else if (aircraft instanceof CantZ1007bis) {
			gWheelRadius[0] = gWheelRadius[1] = 0.572D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.23D;					// aux Tire Diameter measured by 3d shape
		}
		else if (aircraft instanceof SM79i) {
			gWheelRadius[0] = gWheelRadius[1] = 0.580D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.286D;					// aux Tire Diameter measured by 3d shape
			gChockLOffset[0] = 0.22D;
		}
		else if (aircraft instanceof LetovS_328) {
			gWheelRadius[0] = gWheelRadius[1] = 0.503D;  // main Tire Diameter measured by 3d shape
				// NO aux Tire for S-328
		}
		else if (aircraft instanceof AVIA_B534) {
			gWheelRadius[0] = gWheelRadius[1] = 0.422D;  // main Tire Diameter measured by 3d shape
			gWheelRadius[2] = 0.11D;					// aux Tire Diameter measured by 3d shape
		}

/*  checked list as default values OK
        D.XXI
        M.S.406/ 410/ MM
        B-239
        P.11c
        I.A.R.80/ 81
        IK-3
*/
		bLoadedWheelRadius = true;
		return;
	}

	private static String getS(SectFile sectfile, String s, String s1, String s2) {
		String s3 = sectfile.get(s, s1);
		if (s3 == null || s3.length() <= 0)
			return s2;
		else
			return new String(s3);
	}
}