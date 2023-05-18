/*Modified Aircraft class for the SAS Engine Mod*/
package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.CellAirPlane;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.BmpUtils;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.FObj;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.TimeSkip;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetWing;
import com.maddox.il2.objects.ActorCrater;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Wreck;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.il2.objects.weapons.BallisticProjectile;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.Fuze_EL_AZ;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.il2.objects.weapons.MGunAircraftGeneric;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.il2.objects.weapons.PylonRO_WfrGr21;
import com.maddox.il2.objects.weapons.PylonRO_WfrGr21Dual;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.RocketGunWfrGr21;
import com.maddox.il2.objects.weapons.Torpedo;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.KryptoInputFilter;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.MsgEndAction;
import com.maddox.rts.MsgEndActionListener;
import com.maddox.rts.NetChannel;
import com.maddox.rts.ObjIO;
import com.maddox.rts.ObjState;
import com.maddox.rts.Property;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;

public abstract class Aircraft extends NetAircraft implements MsgCollisionListener, MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener, MsgEndActionListener, Predator {
	// TODO: Default Parameters
	// --------------------------------------------------------
	public long tmSearchlighted;
	public static final float MINI_HIT = 5.000001E-007F;
	public static final float defaultUnitHit = 0.01F;
	public static final float powerPerMM = 1700F;
	public static final int HIT_COLLISION = 0;
	public static final int HIT_EXPLOSION = 1;
	public static final int HIT_SHOT = 2;
	protected static float ypr[] = { 0.0F, 0.0F, 0.0F };
	protected static float xyz[] = { 0.0F, 0.0F, 0.0F };
	public static final int _AILERON_L = 0;
	public static final int _AILERON_R = 1;
	public static final int _FUSELAGE = 2;
	public static final int _ENGINE_1 = 3;
	public static final int _ENGINE_2 = 4;
	public static final int _ENGINE_3 = 5;
	public static final int _ENGINE_4 = 6;
	public static final int _GEAR_C = 7;
	public static final int _FLAP_R = 8;
	public static final int _GEAR_L = 9;
	public static final int _GEAR_R = 10;
	public static final int _VER_STAB_1 = 11;
	public static final int _VER_STAB_2 = 12;
	public static final int _NOSE = 13;
	public static final int _OIL = 14;
	public static final int _RUDDER_1 = 15;
	public static final int _RUDDER_2 = 16;
	public static final int _HOR_STAB_L = 17;
	public static final int _HOR_STAB_R = 18;
	public static final int _TAIL_1 = 19;
	public static final int _TAIL_2 = 20;
	public static final int _TANK_1 = 21;
	public static final int _TANK_2 = 22;
	public static final int _TANK_3 = 23;
	public static final int _TANK_4 = 24;
	public static final int _TURRET_1 = 25;
	public static final int _TURRET_2 = 26;
	public static final int _TURRET_3 = 27;
	public static final int _TURRET_4 = 28;
	public static final int _TURRET_5 = 29;
	public static final int _TURRET_6 = 30;
	public static final int _ELEVATOR_L = 31;
	public static final int _ELEVATOR_R = 32;
	public static final int _WING_ROOT_L = 33;
	public static final int _WING_MIDDLE_L = 34;
	public static final int _WING_END_L = 35;
	public static final int _WING_ROOT_R = 36;
	public static final int _WING_MIDDLE_R = 37;
	public static final int _WING_END_R = 38;
	public static final int _FLAP_01 = 39;
	public static final int _FLAP_02 = 40;
	public static final int _FLAP_03 = 41;
	public static final int _FLAP_04 = 42;
	public static final int _NULLPART = 43;
	public static final int _NOMOREPARTS = 44;
	private static final String partNames[] = { "AroneL", "AroneR", "CF", "Engine1", "Engine2", "Engine3", "Engine4", "GearC2", "FlapR", "GearL2", "GearR2", "Keel1", "Keel2", "Nose", "Oil", "Rudder1", "Rudder2", "StabL", "StabR", "Tail1", "Tail2",
			"Tank1", "Tank2", "Tank3", "Tank4", "Turret1B", "Turret2B", "Turret3B", "Turret4B", "Turret5B", "Turret6B", "VatorL", "VatorR", "WingLIn", "WingLMid", "WingLOut", "WingRIn", "WingRMid", "WingROut", "Flap01", "Flap02", "Flap03", "Flap04",
			"NullPart", "EXPIRED" };
	private static final String partNamesForAll[] = { "AroneL", "AroneR", "CF", "GearL2", "GearR2", "Keel1", "Oil", "Rudder1", "StabL", "StabR", "Tail1", "VatorL", "VatorR", "WingLIn", "WingLMid", "WingLOut", "WingRIn", "WingRMid", "WingROut" };
	public Loc spawnLocSingleCoop;
	public String spawnActorName;
	public boolean stationarySpawnLocSet;
	public static final int END_EXPLODE = 2;
	public static final int END_FM_DESTROY = 3;
	public static final int END_DISAPPEAR = 4;
	private long timePostEndAction;
	public boolean buried;
	protected static final float EpsCoarse_ = 0.03F;
	protected static final float EpsSmooth_ = 0.003F;
	protected static final float EpsVerySmooth_ = 0.0005F;
	private float GearL_;
	private float GearR_;
	private float GearC_;
	private float Rudder_;
	private float Elevator_;
	private float Aileron_;
	private float Flap_;
	private float BayDoor_;
	private float AirBrake_;
	private float Steering_;
	public float wingfold_;
	public float cockpitDoor_;
	public float arrestor_;
	private LightPointWorld lLight[];
	private Hook lLightHook[] = { null, null, null, null };
	private static Loc lLightLoc1 = new Loc();
	private static Point3d lLightP1 = new Point3d();
	private static Point3d lLightP2 = new Point3d();
	private static Point3d lLightPL = new Point3d();
	private String _loadingCountry;
	private String typedName;
	private static String _skinMat[] = { "Gloss1D0o", "Gloss1D1o", "Gloss1D2o", "Gloss2D0o", "Gloss2D1o", "Gloss2D2o", "Gloss1D0p", "Gloss1D1p", "Gloss1D2p", "Gloss2D0p", "Gloss2D1p", "Gloss2D2p", "Gloss1D0q", "Gloss1D1q", "Gloss1D2q", "Gloss2D0q",
			"Gloss2D1q", "Gloss2D2q", "Matt1D0o", "Matt1D1o", "Matt1D2o", "Matt2D0o", "Matt2D1o", "Matt2D2o", "Matt1D0p", "Matt1D1p", "Matt1D2p", "Matt2D0p", "Matt2D1p", "Matt2D2p", "Matt1D0q", "Matt1D1q", "Matt1D2q", "Matt2D0q", "Matt2D1q", "Matt2D2q" };
	private static final String _curSkin[] = { "skin1o.tga", "skin1p.tga", "skin1q.tga" };
	private static HashMapExt meshCache = new HashMapExt();
	private static HashMapExt airCache = new HashMapExt();
	protected static Loc tmpLocCell = new Loc();
	private boolean wfrGr21dropped;
	private static final Loc engineBurnLightLoc = new Loc(0.0D, 0.0D, 1.5D, 0.0F, 0.0F, 0.0F);
	protected static Vector3d v1 = new Vector3d();
	private static Vector3d Vd = new Vector3d();
	protected static Point3d Pd = new Point3d();
	protected static Point3d tmpP1 = new Point3d();
	protected static Point3d tmpP2 = new Point3d();
	public static Loc tmpLoc1 = new Loc();
	protected static Loc tmpLoc2 = new Loc();
	protected static Loc tmpLoc3 = new Loc();
	protected static Loc tmpLocExp = new Loc();
	public static Orient tmpOr = new Orient();
	private static int tmpBonesHit;
	private static boolean bWasAlive = true;
	public static boolean showTaxingWay = false;
	public static List playerTaxingWay = null;
	public static float taxHlpX = 1.0F;
	private Aircraft bombScoreOwner;
	public float headingBug;
	public int idleTimeOnCarrier;
	public boolean bSpotter;
	private static final Class planesWithZBReceiver[];
	// --------------------------------------------------------

	// TODO: New/edited parameters
	// --------------------------------------------------------
	private float VarWing_;
	private float Refuel_ = 0.0F;
	private float CatLaunchBar_ = 0.0F;
	// Expanded to allow for up to 10 props
	protected float[] propPos = { 0.0F, 21.6F, 45.9F, 66.9F, 45.0F, 9.2F, 63.0F, 85.0F, 105.0F, 33.0F };
	protected int[] oldProp = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	protected static final String[][] Props = { { "Prop1_D0", "PropRot1_D0", "Prop1_D1" }, { "Prop2_D0", "PropRot2_D0", "Prop2_D1" }, { "Prop3_D0", "PropRot3_D0", "Prop3_D1" }, { "Prop4_D0", "PropRot4_D0", "Prop4_D1" },
			{ "Prop5_D0", "PropRot5_D0", "Prop5_D1" }, { "Prop6_D0", "PropRot6_D0", "Prop6_D1" }, { "Prop7_D0", "PropRot7_D0", "Prop7_D1" }, { "Prop8_D0", "PropRot8_D0", "Prop8_D1" },
			{ "Prop9_D0", "PropRot9_D0", "Prop9_D1" }, { "Prop10_D0", "PropRot10_D0", "Prop10_D1" } };
	// Adds addition aircraft to ZBReceiver list
	static {
		planesWithZBReceiver = (new Class[] { com.maddox.il2.objects.air.F4U.class, com.maddox.il2.objects.air.F4F.class, com.maddox.il2.objects.air.TBF.class, com.maddox.il2.objects.air.SBD.class, com.maddox.il2.objects.air.PBYX.class,
				com.maddox.il2.objects.air.F6F.class, com.maddox.il2.objects.air.F2A2.class, com.maddox.il2.objects.air.SEAFIRE3.class, com.maddox.il2.objects.air.SEAFIRE3F.class, com.maddox.il2.objects.air.Fulmar.class,
				com.maddox.il2.objects.air.Swordfish.class, com.maddox.il2.objects.air.MOSQUITO.class, com.maddox.il2.objects.air.B_25.class, com.maddox.il2.objects.air.A_20.class, com.maddox.il2.objects.air.B_17.class,
				com.maddox.il2.objects.air.B_24.class, com.maddox.il2.objects.air.B_29.class, com.maddox.il2.objects.air.BEAU.class, com.maddox.il2.objects.air.P_80.class, com.maddox.il2.objects.air.P_39.class, com.maddox.il2.objects.air.P_51.class,
				com.maddox.il2.objects.air.P_47.class, com.maddox.il2.objects.air.P_40.class, com.maddox.il2.objects.air.P_38.class, com.maddox.il2.objects.air.P_36.class, com.maddox.il2.objects.air.A_20.class, com.maddox.il2.objects.air.C_47.class,
				com.maddox.il2.objects.air.P_80.class, com.maddox.il2.objects.air.TypeZBReceiver.class });
	}

	static class CacheItem {

		HierMesh mesh;
		boolean bExistTextures;
		int loaded;
		long time;

		CacheItem() {
		}
	}

	public static class _WeaponSlot {

		public int trigger;
		public Class clazz;
		public int bullets;

		public _WeaponSlot(int i, String s, int j) throws Exception {
			trigger = i;
			clazz = ObjIO.classForName("weapons." + s);
			bullets = j;
		}
	}

	private static class MsgExplosionPostVarSet {

		Actor THIS;
		String chunkName;
		Point3d p;
		Actor initiator;
		float power;
		float radius;

		private MsgExplosionPostVarSet() {
			p = new Point3d();
		}

	}

	static class EndActionParam {

		Actor initiator;
		Eff3DActor smoke;

		public EndActionParam(Actor actor, Eff3DActor eff3dactor) {
			initiator = actor;
			smoke = eff3dactor;
		}
	}

	public static String[] partNames() {
		return partNames;
	}

	public int part(String s) {
		if (s == null) return 43;
		int i = 0;
		while (i < 44) {
			if (s.startsWith(partNames[i])) return i;
			i++;
		}
		return 43;
	}

	public boolean cut(String s) {
		FM.dryFriction = 1.0F;
		debugprintln("" + s + " goes off..");
		if (World.Rnd().nextFloat() < bailProbabilityOnCut(s)) {
			debugprintln("BAILING OUT - " + s + " gone, can't keep on..");
			hitDaSilk();
		}
		if (!isChunkAnyDamageVisible(s)) {
			debugprintln("" + s + " is already cut off - operation rejected..");
			return false;
		}
		int ai[] = hideSubTrees(s + "_D");
		if (ai == null) return false;
		for (int i = 0; i < ai.length; i++) {
			Wreckage wreckage = new Wreckage(this, ai[i]);
			for (int l = 0; l < 4; l++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[l + 0])) FM.AS.changeTankEffectBase(l, wreckage);

			for (int i1 = 0; i1 < FM.EI.getNum(); i1++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[i1 + 4])) {
					FM.AS.changeEngineEffectBase(i1, wreckage);
					FM.AS.changeSootEffectBase(i1, wreckage);
				}

			for (int j1 = 0; j1 < 6; j1++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[j1 + 12])) FM.AS.changeNavLightEffectBase(j1, wreckage);

			for (int k1 = 0; k1 < 4; k1++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[k1 + 18])) FM.AS.changeLandingLightEffectBase(k1, wreckage);

			for (int l1 = 0; l1 < FM.EI.getNum(); l1++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[l1 + 22])) FM.AS.changeOilEffectBase(l1, wreckage);

			if (hierMesh().chunkName().startsWith(s) && World.Rnd().nextInt(0, 99) < 50) Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE, 3F);
			Vd.set(FM.Vwld);
			wreckage.setSpeed(Vd);
		}

		ai = hierMesh().getSubTrees(s + "_D");
		for (int j = 0; j < ai.length; j++)
			detachGun(ai[j]);

		String s1 = s + "_CAP";
		if (hierMesh().chunkFindCheck(s1) >= 0) hierMesh().chunkVisible(s1, true);
		for (int k = 0; k < ai.length; k++) {
			for (int i2 = 3; i2 < FM.Gears.pnti.length; i2++)
				try {
					if (FM.Gears.pnti[i2] != -1 && ai[k] == hierMesh().chunkByHookNamed(FM.Gears.pnti[i2])) FM.Gears.pnti[i2] = -1;
				} catch (Exception exception) {
					System.out.println("FATAL ERROR: Gear pnti[] cut failed on tt[] = " + i2 + " - " + FM.Gears.pnti.length);
				}

		}

		hierMesh().setCurChunk(ai[0]);
		hierMesh().getChunkLocObj(tmpLoc1);
		sfxCrash(tmpLoc1.getPoint());
		return true;
	}

	public boolean cut_Subtrees(String s) {
		debugprintln("" + s + " goes off..");
		if (World.Rnd().nextFloat() < bailProbabilityOnCut(s)) {
			debugprintln("BAILING OUT - " + s + " gone, can't keep on..");
			hitDaSilk();
		}
		if (!isChunkAnyDamageVisible(s)) {
			debugprintln("" + s + " is already cut off - operation rejected..");
			return false;
		}
		int i = hierMesh().chunkFindCheck(s + "_D0");
		if (i >= 0) {
			int j;
			for (j = 0; j <= 9; j++) {
				int k = hierMesh().chunkFindCheck(s + "_D" + j);
				if (k < 0) continue;
				hierMesh().setCurChunk(k);
				if (hierMesh().isChunkVisible()) break;
			}

			if (j > 9) i = -1;
		}
		Object obj = null;
		if (i >= 0) {
			obj = Wreckage.makeWreck(this, i);
			((Actor) (obj)).setOwner(this, false, false, false);
		}
		int ai[] = hideSubTrees(s + "_D");
		if (ai == null) return false;
		for (int l = 0; l < ai.length; l++) {
			if (i < 0) obj = new Wreckage(this, ai[l]);
			else hierMesh().setCurChunk(ai[l]);
			for (int k1 = 0; k1 < 4; k1++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[k1 + 0])) FM.AS.changeTankEffectBase(k1, ((Actor) (obj)));

			for (int l1 = 0; l1 < 4; l1++)
				if (hierMesh().chunkName().startsWith(FM.AS.astateEffectChunks[l1 + 4])) {
					FM.AS.changeEngineEffectBase(l1, ((Actor) (obj)));
					FM.AS.changeSootEffectBase(l1, ((Actor) (obj)));
				}

			if (hierMesh().chunkName().startsWith(s) && World.Rnd().nextInt(0, 99) < 50) Eff3DActor.New(((Actor) (obj)), null, null, 1.0F, Wreckage.SMOKE, 3F);
			Vd.set(FM.Vwld);
			if (i < 0) ((Wreckage) obj).setSpeed(Vd);
			else ((Wreck) obj).setSpeed(Vd);
		}

		ai = hierMesh().getSubTrees(s + "_D");
		for (int i1 = 0; i1 < ai.length; i1++)
			detachGun(ai[i1]);

		String s1 = s + "_CAP";
		if (hierMesh().chunkFindCheck(s1) >= 0) hierMesh().chunkVisible(s1, true);
		for (int j1 = 0; j1 < ai.length; j1++) {
			for (int i2 = 3; i2 < FM.Gears.pnti.length; i2++)
				try {
					if (FM.Gears.pnti[i2] != -1 && ai[j1] == hierMesh().chunkByHookNamed(FM.Gears.pnti[i2])) FM.Gears.pnti[i2] = -1;
				} catch (Exception exception) {
					System.out.println("FATAL ERROR: Gear pnti[] cut failed on tt[] = " + i2 + " - " + FM.Gears.pnti.length);
				}

		}

		hierMesh().setCurChunk(ai[0]);
		hierMesh().getChunkLocObj(tmpLoc1);
		sfxCrash(tmpLoc1.getPoint());
		return true;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		FM.dryFriction = 1.0F;
		switch (i) {
		default:
			break;

		case 2: // '\002'
			if (isEnablePostEndAction(0.0D)) postEndAction(0.0D, actor, 2, null);
			return false;

		case 3: // '\003'
			if (FM.EI.engines.length > 0) {
				hitProp(0, j, actor);
				FM.EI.engines[0].setEngineStuck(actor);
			}
			break;

		case 4: // '\004'
			if (FM.EI.engines.length > 1) {
				hitProp(1, j, actor);
				FM.EI.engines[1].setEngineStuck(actor);
			}
			break;

		case 5: // '\005'
			if (FM.EI.engines.length > 2) {
				hitProp(2, j, actor);
				FM.EI.engines[2].setEngineStuck(actor);
			}
			break;

		case 6: // '\006'
			if (FM.EI.engines.length > 3) {
				hitProp(3, j, actor);
				FM.EI.engines[3].setEngineStuck(actor);
			}
			break;
		}
		return cut(partNames[i]);
	}

	protected int curDMGLevel(int i) {
		return curDMGLevel(partNames[i] + "_D0");
	}

	private int curDMGLevel(String s) {
		int i = s.length() - 1;
		if (i < 2) return 0;
		boolean flag = s.charAt(i - 2) == '_' && Character.toUpperCase(s.charAt(i - 1)) == 'D' && Character.isDigit(s.charAt(i));
		if (!flag) return 0;
		HierMesh hiermesh = hierMesh();
		String s1 = s.substring(0, i);
		int j = 0;
		do {
			if (j >= 10) break;
			String s2 = s1 + j;
			if (hiermesh.chunkFindCheck(s2) < 0) return 0;
			if (hiermesh.isChunkVisible(s2)) break;
			j++;
		} while (true);
		if (j == 10) return 0;
		else return j;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		int j = s.length() - 1;
		HierMesh hiermesh = hierMesh();
		String s3 = s;
		boolean flag = s.charAt(j - 2) == '_' && Character.toUpperCase(s.charAt(j - 1)) == 'D' && Character.isDigit(s.charAt(j));
		FM.dryFriction = 1.0F;
		String s4;
		if (flag) {
			int k = s.charAt(j) - 48;
			String s1 = s.substring(0, j);
			while (!hiermesh.isChunkVisible(s3)) {
				if (k < 9) k++;
				else return;
				s3 = s1 + k;
				if (hiermesh.chunkFindCheck(s3) < 0) return;
			}
			if (k < 9) {
				k++;
				s4 = s1 + k;
				if (hiermesh.chunkFindCheck(s4) < 0) s4 = null;
			} else {
				s4 = null;
			}
			s1 = s.substring(0, j - 2);
		} else {
			if (!hiermesh.isChunkVisible(s3)) return;
			s4 = null;
		}
		if (s4 == null) {
			if (!isNet() || isNetMaster()) nextCUTLevel(s, i, actor);
			return;
		} else {
			int l = part(s);
			FM.hit(l);
			hiermesh.chunkVisible(s3, false);
			hiermesh.chunkVisible(s4, true);
			return;
		}
	}

	protected void nextDMGLevels(int i, int j, String s, Actor actor) {
		if (i <= 0) return;
		if (i > 4) i = 4;
		if (this == World.getPlayerAircraft() && !World.cur().diffCur.Vulnerability) return;
		if (isNet()) {
			if (isNetPlayer() && !World.cur().diffCur.Vulnerability) return;
			if (!Actor.isValid(actor)) return;
			int k = part(s);
			if (!isNetMaster()) netPutHits(true, null, i, j, k, actor);
			netPutHits(false, null, i, j, k, actor);
			if (actor != this && FM.isPlayers() && (actor instanceof Aircraft) && ((Aircraft) actor).isNetPlayer() && j != 0 && i > 3) if (s.startsWith("Wing")) {
				if (!FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_blowwing", (Aircraft) actor, this);
				FM.setSentWingNote(true);
			} else if (s.startsWith("Tail") && !FM.isSentBuryNote()) Chat.sendLogRnd(3, "gore_blowtail", (Aircraft) actor, this);
		}
		while (i-- > 0)
			nextDMGLevel(s, j, actor);
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		FM.dryFriction = 1.0F;
		debugprintln("Detected NCL in " + s + "..");
		if (this == World.getPlayerAircraft() && !World.cur().diffCur.Vulnerability) return;
		int j = s.length() - 1;
		HierMesh hiermesh = hierMesh();
		String s3 = s;
		boolean flag = s.charAt(j - 2) == '_' && Character.toUpperCase(s.charAt(j - 1)) == 'D' && Character.isDigit(s.charAt(j));
		if (flag) {
		} else {
			if (!hiermesh.isChunkVisible(s3)) return;
		}
		int k = part(s);
		if (cutFM(k, i, actor)) {
			FM.cut(k, i, actor);
			netPutCut(k, i, actor);
			if (FM.isPlayers() && this != actor && (actor instanceof Aircraft) && ((Aircraft) actor).isNetPlayer() && i == 2 && !FM.isSentWingNote() && !FM.isSentBuryNote() && (k == 34 || k == 37 || k == 33 || k == 36)) {
				Chat.sendLogRnd(3, "gore_sawwing", (Aircraft) actor, this);
				FM.setSentWingNote(true);
			}
		}
	}

	public boolean isEnablePostEndAction(double d) {
		if (timePostEndAction < 0L) return true;
		long l = Time.current() + (long) (int) (d * 1000D);
		return l < timePostEndAction;
	}

	public void postEndAction(double d, Actor actor, int i, Eff3DActor eff3dactor) {
		if (!isEnablePostEndAction(d)) {
			return;
		} else {
			timePostEndAction = Time.current() + (long) (int) (d * 1000D);
			MsgEndAction.post(0, d, this, new EndActionParam(actor, eff3dactor), i);
			return;
		}
	}

	public void msgEndAction(Object obj, int i) {
		EndActionParam endactionparam = (EndActionParam) obj;
		if (isAlive()) {
			if (FM.isPlayers() && !FM.isSentBuryNote()) switch (i) {
			case 2: // '\002'
				if (Actor.isAlive(endactionparam.initiator) && (endactionparam.initiator instanceof Aircraft) && ((Aircraft) endactionparam.initiator).isNetPlayer() && FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) > 100D)
					Chat.sendLogRnd(1, "gore_blowup", (Aircraft) endactionparam.initiator, this);
				break;
			}
			int l;
			switch (i) {
			case 2: // '\002'
				netExplode();
				if (endactionparam.smoke != null) {
					Eff3DActor.finish(endactionparam.smoke);
					sfxSmokeState(0, 0, false);
				}
				doExplosion();
				for (int j = 0; j < FM.AS.astateEngineStates.length; j++)
					FM.AS.hitEngine(this, j, 1000);

				for (int k = 0; k < FM.AS.astateTankStates.length; k++)
					FM.AS.hitTank(this, k, 1000);

				float f = cvt(FM.M.fuel, 100F, 2000F, 5F, 40F);
				Actor actor1 = null;
				String s = null;
				if (FM.Gears.onGround() && FM.Vrel.lengthSquared() < 70D) {
					f = 0.0F;
				} else {
					Point3d point3d = new Point3d(FM.Loc);
					Point3d point3d1 = new Point3d(FM.Loc);
					Point3d point3d2 = new Point3d();
					FM.Vrel.set(FM.Vwld);
					FM.Vrel.normalize();
					FM.Vrel.scale(20D);
					point3d1.add(FM.Vrel);
					actor1 = Engine.collideEnv().getLine(point3d, point3d1, false, this, point3d2);
					if (Actor.isAlive(actor1) && (actor1 instanceof ActorHMesh)) {
						Mesh mesh = ((ActorMesh) actor1).mesh();
						Loc loc = actor1.pos.getAbs();
						float f3 = mesh.detectCollisionLine(loc, point3d, point3d1);
						if (f3 >= 0.0F) s = Mesh.collisionChunk(0);
						if ((actor1 instanceof BigshipGeneric) || (actor1 instanceof ShipGeneric)) {
							float f4 = 0.018F * (float) FM.Vwld.length();
							if (f4 > 1.0F) f4 = 1.0F;
							if (f4 < 0.1F) f4 = 0.1F;
							float f5 = FM.M.fuel;
							if (f5 > 300F) f5 = 300F;
							f = f4 * (50F + 0.7F * FM.CT.getWeaponMass() + 0.3F * f5);
						}
					}
				}
				float f1 = 0.5F * f;
				if (f1 < 10F) f1 = 10F;
				if (f1 > 300F) f1 = 300F;
				float f2 = 0.7F * f;
				if (f2 < 30F) f2 = 30F;
				if (f2 > 350F) f2 = 350F;
				MsgExplosion.send(actor1, s, FM.Loc, this, f, 0.9F * f, 0, f1);
				MsgExplosion.send(actor1, s, FM.Loc, this, 0.5F * f, 0.25F * f, 1, f2);
				// fall through

			case 3: // '\003'
				explode();
				// fall through

			default:
				l = 0;
				break;
			}
			for (; l < Math.min(FM.crew, 9); l++)
				if (!FM.AS.isPilotDead(l)) FM.AS.hitPilot(FM.actor, l, 100);

			setDamager(endactionparam.initiator, 4);
			Actor actor = getDamager();
			World.onActorDied(this, actor, getAssister(actor));
		}
		MsgDestroy.Post(Time.current(), this);
	}

	protected void doExplosion() {
		if (FM.Loc.z < Engine.cur.land.HQ_Air(FM.Loc.x, FM.Loc.y) + (double) (FM.Length / 2.0F)) {
			World.cur();
			if (World.land().isWater(FM.Loc.x, FM.Loc.y)) Explosions.AirDrop_Water(FM.Loc);
			else Explosions.AirDrop_Land(FM.Loc);
		} else {
			Explosions.ExplodeFuel(FM.Loc);
		}
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		boolean flag = Engine.collideEnv().isDoCollision() && World.getPlayerAircraft() != this;
		if (actor instanceof BigshipGeneric) {
			FM.Gears.bFlatTopGearCheck = true;
			if (flag && (Time.tickCounter() + hashCode() & 0xf) != 0) aflag[0] = false;
		} else if (flag && (Time.tickCounter() & 0xf) != 0 && (actor instanceof Aircraft) && FM.Gears.isUnderDeck()) aflag[0] = !((Aircraft) actor).FM.Gears.isUnderDeck();
		if (Engine.collideEnv().isDoCollision() && (actor instanceof Aircraft) && Mission.isCoop() && actor.isNetMirror() && (isMirrorUnderDeck() || FM.Gears.isUnderDeck() || Time.tickCounter() <= 2)) aflag[0] = false;
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if (isNet() && isNetMirror()) return;
		if (actor instanceof ActorCrater) {
			if (!s.startsWith("Gear")) return;
			if (netUser() != null && netUser() == ((ActorCrater) actor).netOwner) return;
		}
		if (this == World.getPlayerAircraft()) TimeSkip.airAction(1);
		FM.dryFriction = 1.0F;
		if (s.startsWith("Pilot")) if (this == World.getPlayerAircraft() && !World.cur().diffCur.Vulnerability) {
			return;
		} else {
			int i = s.charAt(5) - 49;
			killPilot(this, i);
			return;
		}
		if (s.startsWith("Head")) if (this == World.getPlayerAircraft() && !World.cur().diffCur.Vulnerability) {
			return;
		} else {
			int j = s.charAt(4) - 49;
			killPilot(this, j);
			return;
		}
		if (actor instanceof Wreckage) {
			if (s.startsWith("CF_")) return;
			if (actor.getOwner() == this) return;
			if (netUser() != null && netUser() == ((Wreckage) actor).netOwner) {
				return;
			} else {
				actor.collide(false);
				nextDMGLevels(3, 0, s, this);
				return;
			}
		}
		if (actor instanceof Paratrooper) {
			FM.getSpeed(v1);
			actor.getSpeed(Vd);
			Vd.x -= v1.x;
			Vd.y -= v1.y;
			Vd.z -= v1.z;
			if (Vd.length() > 30D) {
				setDamager(actor, 4);
				nextDMGLevels(4, 0, s, actor);
			}
			return;
		}
		if ((actor instanceof RocketryRocket) && s1.startsWith("Wing")) {
			RocketryRocket rocketryrocket = (RocketryRocket) actor;
			Loc loc = new Loc();
			Point3d point3d = new Point3d();
			Vector3d vector3d = new Vector3d();
			Vector3d vector3d1 = new Vector3d();
			pos.getAbs(loc);
			point3d.set(actor.pos.getAbsPoint());
			loc.transformInv(point3d);
			boolean flag = point3d.y > 0.0D;
			vector3d.set(0.0D, flag ? hierMesh().collisionR() : -hierMesh().collisionR(), 0.0D);
			loc.transform(vector3d);
			point3d.set(FM.Loc);
			point3d.add(vector3d);
			actor.pos.getAbs(loc);
			loc.transformInv(point3d);
			vector3d.set(FM.Vwld);
			actor.pos.speed(vector3d1);
			vector3d.sub(vector3d1);
			loc.transformInv(vector3d);
			vector3d.z += (flag ? 1.0D : -1D) * FM.getW().x * (double) hierMesh().collisionR();
			if (vector3d.x * vector3d.x + vector3d.y * vector3d.y < 4D) {
				if (point3d.y * vector3d.z > 0.0D) rocketryrocket.sendRocketStateChange('a', this);
				else rocketryrocket.sendRocketStateChange('b', this);
				return;
			}
			rocketryrocket.sendRocketStateChange(flag ? 'l' : 'r', this);
		}
		if (FM.turnOffCollisions && (s.startsWith("Wing") || s.startsWith("Arone") || s.startsWith("Keel") || s.startsWith("Rudder") || s.startsWith("Stab") || s.startsWith("Vator") || s.startsWith("Nose") || s.startsWith("Tail"))) return;
		if ((actor instanceof Aircraft) && Actor.isValid(actor) && getArmy() == actor.getArmy()) {
			double d = Engine.cur.land.HQ(FM.Loc.x, FM.Loc.y);
			Aircraft aircraft = (Aircraft) actor;
			if (FM.Loc.z - (double) (2.0F * FM.Gears.H) < d && aircraft.FM.Loc.z - (double) (2.0F * aircraft.FM.Gears.H) < d) setDamagerExclude(actor);
		}
		if (s != null && hierMesh().chunkFindCheck(s) != -1) {
			hierMesh().setCurChunk(s);
			hierMesh().getChunkLocObj(tmpLoc1);
			Vd.set(FM.Vwld);
			FM.Or.transformInv(Vd);
			Vd.normalize();
			Vd.negate();
			Vd.scale(2000F / FM.M.mass);
			Vd.cross(tmpLoc1.getPoint(), Vd);
			FM.getW().x += (float) Vd.x;
			FM.getW().y += (float) Vd.y;
			FM.getW().z += (float) Vd.z;
		}
		setDamager(actor, 4);
		nextDMGLevels(4, 0, s, actor);
	}

	private void splintersHit(Explosion explosion) {
		float af[] = new float[2];
		float f = mesh().collisionR();
		float f1 = 1.0F;
		pos.getTime(Time.current(), tmpLocExp);
		tmpLocExp.get(Pd);
		explosion.computeSplintersHit(Pd, f, 1.0F, af);
		Shot shot = new Shot();
		shot.chunkName = "CF_D0";
		shot.initiator = explosion.initiator;
		shot.tickOffset = Time.tickOffset();
		int i = (int) (af[0] * 2.0F + 0.5F);
		if (i <= 0) return;
		while (i > 192) {
			i = (int) ((float) i * 0.5F);
			f1 *= 2.0F;
		}
		for (int k = 0; k < i; k++) {
			tmpP1.set(explosion.p);
			tmpLocExp.get(tmpP2);
			double d = tmpP1.distance(tmpP2);
			tmpP2.add(World.Rnd().nextDouble(-f, f), World.Rnd().nextDouble(-f, f), World.Rnd().nextDouble(-f, f));
			if (d > (double) f) tmpP1.interpolate(tmpP1, tmpP2, 1.0D - (double) f / d);
			tmpP2.interpolate(tmpP1, tmpP2, 2D);
			int j = hierMesh().detectCollisionLineMulti(tmpLocExp, tmpP1, tmpP2);
			if (j <= 0) continue;
			shot.mass = 0.015F * World.Rnd().nextFloat(0.25F, 1.75F) * f1;
			if (World.Rnd().nextFloat() < 0.1F) {
				shot.mass = 0.015F * World.Rnd().nextFloat(0.1F, 10F) * f1;
			}
			float f2 = explosion.power * 10F;
			if (shot.mass > f2) shot.mass = f2;
			shot.p.interpolate(tmpP1, tmpP2, hierMesh().collisionDistMulti(0));
			if (World.Rnd().nextFloat() < 0.333333F) shot.powerType = 2;
			else if (World.Rnd().nextFloat() < 0.5F) shot.powerType = 3;
			else shot.powerType = 0;
			shot.v.x = (float) (tmpP2.x - tmpP1.x);
			shot.v.y = (float) (tmpP2.y - tmpP1.y);
			shot.v.z = (float) (tmpP2.z - tmpP1.z);
			shot.v.normalize();
			if (World.Rnd().nextFloat() < 0.02F) shot.v.scale(af[1] * World.Rnd().nextFloat(0.1F, 10F));
			else shot.v.scale(af[1] * World.Rnd().nextFloat(0.9F, 1.1F));
			msgShot(shot);
		}

	}

	public void msgExplosion(Explosion explosion) {
		if (this == World.getPlayerAircraft()) TimeSkip.airAction(3);
		setExplosion(explosion);
		FM.dryFriction = 1.0F;
		if (explosion.power <= 0.0F || explosion.chunkName != null && explosion.chunkName.equals(partNames[43])) {
			debugprintln("Splash hit from " + ((explosion.initiator instanceof Aircraft) ? ((Aircraft) explosion.initiator).typedName() : explosion.initiator.name()) + " in " + explosion.chunkName + " is Nill..");
			return;
		}
		if (explosion.powerType == 1) {
			splintersHit(explosion);
			return;
		}
		float f = explosion.power;
		float f1 = 0.0F;
		if (explosion.powerType == 0) {
			f *= 0.5F;
			f1 = f;
		}
		if (explosion.chunkName != null) {
			if (explosion.chunkName.startsWith("Wing") && explosion.chunkName.endsWith("_D3")) FM.setCapableOfACM(false);
			if (explosion.chunkName.startsWith("Wing") && explosion.power > 0.0025F) {
				if (explosion.chunkName.startsWith("WingL")) {
					debugprintln("Large Shockwave Hits the Left Wing - Wing Stalls.");
					FM.AS.setFMSFX(explosion.initiator, 2, (int) (explosion.power * 20000F));
				}
				if (explosion.chunkName.startsWith("WingR")) {
					debugprintln("Large Shockwave Hits the Right Wing - Wing Stalls.");
					FM.AS.setFMSFX(explosion.initiator, 3, (int) (explosion.power * 20000F));
				}
			}
		}
		float f2;
		if (explosion.chunkName == null) f2 = explosion.receivedTNT_1meter(this);
		else f2 = f;
		if (f2 <= 5.000001E-007F) return;
		debugprintln("Splash hit from " + ((explosion.initiator instanceof Aircraft) ? ((Aircraft) explosion.initiator).typedName() : explosion.initiator.name()) + " in " + explosion.chunkName + " for "
				+ (int) ((100F * f2) / (0.01F + 3F * FM.Sq.getToughness(part(explosion.chunkName)))) + " % ( " + f2 + " kg)..");
		if (explosion.chunkName == null) {
			f2 /= 0.01F;
		} else {
			if (explosion.chunkName.endsWith("_D0") && !explosion.chunkName.startsWith("Gear")) {
				if (f2 > 0.01F) f2 = 1.0F + (f2 - 0.01F) / FM.Sq.getToughness(part(explosion.chunkName));
				else f2 /= 0.01F;
			} else {
				f2 /= FM.Sq.getToughness(part(explosion.chunkName));
			}
			f2 += FM.Sq.eAbsorber[part(explosion.chunkName)];
		}
		if (f2 >= 1.0F) setDamager(explosion.initiator, (int) f2);
		if (explosion.chunkName != null) {
			if ((int) f2 > 0) {
				setDamager(explosion.initiator, 1);
				if (explosion.chunkName.startsWith("Pilot")) {
					killPilot(explosion.initiator, explosion.chunkName.charAt(5) - 49);
					return;
				}
				if (explosion.chunkName.startsWith("Head")) {
					killPilot(explosion.initiator, explosion.chunkName.charAt(4) - 49);
					return;
				}
			}
			nextDMGLevels((int) f2, 1, explosion.chunkName, explosion.initiator);
		} else {
			int i = 0;
			do {
				if (i >= partNamesForAll.length) break;
				int l = World.Rnd().nextInt(partNamesForAll.length);
				if (isChunkAnyDamageVisible(partNamesForAll[l])) {
					nextDMGLevels((int) f2, 1, partNamesForAll[l] + "_D0", explosion.initiator);
					break;
				}
				i++;
			} while (true);
		}
		if (explosion.chunkName != null) FM.Sq.eAbsorber[part(explosion.chunkName)] = f2 - (float) (int) f2;
		if (f2 > 8F) if (f2 / (float) partNamesForAll.length > 1.5F) {
			for (int j = 0; j < partNamesForAll.length; j++)
				if (isChunkAnyDamageVisible(partNamesForAll[j])) nextDMGLevels(3, 1, partNamesForAll[j] + "_D0", explosion.initiator);

		} else {
			int k = (int) f2 / 3 - 1;
			if (k > partNamesForAll.length * 2) k = partNamesForAll.length * 2;
			for (int i1 = 0; i1 < k; i1++) {
				int j1 = World.Rnd().nextInt(partNamesForAll.length);
				if (isChunkAnyDamageVisible(partNamesForAll[j1])) nextDMGLevels(3, 1, partNamesForAll[j1] + "_D0", explosion.initiator);
			}

		}
		if (bWasAlive && FM.isTakenMortalDamage() && (getDamager() instanceof Aircraft) && FM.actor.getArmy() != getDamager().getArmy() && World.Rnd().nextInt(0, 99) < 66) {
			if (!buried) Voice.speakNiceKill((Aircraft) getDamager());
			buried = true;
		}
		bWasAlive = true;
		if (f1 > 0.0F) {
			MsgExplosionPostVarSet msgexplosionpostvarset = new MsgExplosionPostVarSet();
			msgexplosionpostvarset.THIS = this;
			msgexplosionpostvarset.chunkName = explosion.chunkName;
			msgexplosionpostvarset.p.set(explosion.p);
			msgexplosionpostvarset.initiator = explosion.initiator;
			msgexplosionpostvarset.power = f1;
			msgexplosionpostvarset.radius = explosion.radius;
			new MsgAction(false, msgexplosionpostvarset) {

				public void doAction(Object obj) {
					MsgExplosionPostVarSet msgexplosionpostvarset1 = (MsgExplosionPostVarSet) obj;
					if (!Actor.isValid(msgexplosionpostvarset1.THIS)) {
						return;
					} else {
						MsgExplosion.send(msgexplosionpostvarset1.THIS, msgexplosionpostvarset1.chunkName, msgexplosionpostvarset1.p, msgexplosionpostvarset1.initiator, 48F * msgexplosionpostvarset1.power, msgexplosionpostvarset1.power, 1,
								Math.max(msgexplosionpostvarset1.radius, 30F));
						return;
					}
				}

			};
		}
	}

	protected void doRicochet(Shot shot) {
		v1.x *= World.Rnd().nextFloat(0.25F, 1.0F);
		v1.y *= World.Rnd().nextFloat(-1F, -0.25F);
		v1.z *= World.Rnd().nextFloat(-1F, -0.25F);
		v1.normalize();
		v1.scale(World.Rnd().nextFloat(10F, 600F));
		FM.Or.transform(v1);
		doRicochet(shot.p, v1);
		shot.power = 0.0F;
	}

	protected void doRicochetBack(Shot shot) {
		v1.x *= -1D;
		v1.y *= -1D;
		v1.z *= -1D;
		v1.scale(World.Rnd().nextFloat(0.25F, 1.0F));
		FM.Or.transform(v1);
		doRicochet(shot.p, v1);
	}

	protected void doRicochet(Point3d point3d, Vector3d vector3d) {
		BallisticProjectile ballisticprojectile = new BallisticProjectile(point3d, vector3d, 1.0F);
		Eff3DActor.New(ballisticprojectile, null, null, 4F, "3DO/Effects/Tracers/TrailRicochet.eff", 1.0F);
		pos.getAbs(tmpLoc1);
		tmpLoc1.transformInv(point3d);
		Eff3DActor.New(this, null, new Loc(point3d), 1.0F, "3DO/Effects/Fireworks/12mmRicochet.eff", 0.2F);
		Eff3DActor.New(this, null, new Loc(point3d), 0.5F, "3DO/Effects/Fireworks/20_Sparks.eff", -1F);
	}

	protected void setShot(Shot shot) {
		if ((this == World.getPlayerAircraft() || isNetPlayer()) && !World.cur().diffCur.Vulnerability) {
			shot.chunkName = partNames[43];
			shot.power = 0.0F;
			shot.mass = 0.0F;
		}
		if (bWasAlive) bWasAlive = !FM.isTakenMortalDamage();
		v1.sub(shot.v, FM.Vwld);
		double d = v1.length();
		shot.power = (float) ((double) shot.mass * d * d) * 0.5F;
		if (shot.powerType == 0) shot.power *= 0.666F;
		FM.Or.transformInv(v1);
		v1.normalize();
		tmpLoc1.set(shot.p);
		pos.getAbs(tmpLoc2);
		pos.getCurrent(tmpLoc3);
		tmpLoc3.interpolate(tmpLoc2, shot.tickOffset);
		tmpLoc1.sub(tmpLoc3);
		tmpLoc1.get(Pd);
		Vd.set(shot.v);
		Vd.normalize();
		Vd.scale(0.10000000149011612D);
		tmpP1.set(shot.p);
		tmpP1.sub(Vd);
		Vd.normalize();
		Vd.scale(48.900001525878906D);
		tmpP2.set(shot.p);
		tmpP2.add(Vd);
		tmpBonesHit = hierMesh().detectCollisionLineMulti(tmpLoc3, tmpP1, tmpP2);
		if (Config.isUSE_RENDER() && World.cur().isArcade()) {
			ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh("3DO/Arms/MatrixXX/mono.sim");
			actorsimplemesh.pos.setBase(this, null, false);
			tmpOr.setAT0(v1);
			actorsimplemesh.pos.setRel(Pd, tmpOr);
			float f = (float) Math.sqrt(Math.sqrt(shot.mass));
			actorsimplemesh.mesh().setScaleXYZ(0.75F * f, f, f);
			actorsimplemesh.drawing(true);
			actorsimplemesh.postDestroy(Time.current() + 30000L);
		}
	}

	protected void setExplosion(Explosion explosion) {
		if ((this == World.getPlayerAircraft() || isNetPlayer()) && !World.cur().diffCur.Vulnerability) explosion.chunkName = partNames[43];
		if (explosion.chunkName == null && !isChunkAnyDamageVisible("CF")) explosion.chunkName = partNames[43];
		if (bWasAlive) bWasAlive = !FM.isTakenMortalDamage();
	}

	protected void msgSndShot(float f, double d, double d1, double d2) {
		if (!Config.isUSE_RENDER()) return;
		_tmpPoint.set(d, d1, d2);
		sfxHit(f, _tmpPoint);
		if (isNet() && FM.isPlayers() && (FM instanceof RealFlightModel)) {
			FM.dryFriction = 1.0F;
			((RealFlightModel) FM).producedShakeLevel = 1.0F;
			float f1 = (2000F * f) / FM.M.mass;
			FM.getW().add(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
		}
	}

	public void msgShot(Shot shot) {
		if (this == World.getPlayerAircraft()) TimeSkip.airAction(2);
		setShot(shot);
		if (!isNet()) {
			FM.dryFriction = 1.0F;
			if (FM.isPlayers() && FM instanceof RealFlightModel) ((RealFlightModel) FM).producedShakeLevel = 1.0F;
			float f = 2000.0F * shot.mass / FM.M.mass;
			FM.getW().add((double) World.Rnd().nextFloat(-f, f), (double) World.Rnd().nextFloat(-f, f), (double) World.Rnd().nextFloat(-f, f));
		}
		if (shot.chunkName != null) {
			if (shot.chunkName == partNames[43]) {
				if (World.Rnd().nextFloat() < 0.25F) doRicochet(shot);
			} else {
				if (shot.chunkName.startsWith("Wing") && (shot.chunkName.endsWith("_D3") || shot.chunkName.endsWith("_D2") && FM.Skill >= 2)) FM.setCapableOfACM(false);
				if (FM instanceof Pilot && World.Rnd().nextInt(-1, 8) < FM.Skill) ((Pilot) FM).setAsDanger(shot.initiator);
				if (Config.isUSE_RENDER() && FM instanceof RealFlightModel) {
					_tmpPoint.set(pos.getAbsPoint());
					_tmpPoint.sub(shot.p);
					msgSndShot(shot.mass, _tmpPoint.x, _tmpPoint.y, _tmpPoint.z);
				}
				shot.bodyMaterial = 2;
				if (isNetPlayer()) sendMsgSndShot(shot);
				if (tmpBonesHit > 0) {
					debuggunnery("");
					debuggunnery("New Bullet: E = " + (int) shot.power + " [J], M = " + (int) (1000.0F * shot.mass) + " [g], Type = (" + sttp(shot.powerType) + ")");
					if (shot.powerType == 1) tmpBonesHit = Math.min(tmpBonesHit, 2);
					for (int i = 0; i < tmpBonesHit; i++) {
						hierMesh();
						String string = HierMesh.collisionNameMulti(i, 1);
						if (string.length() == 0) {
							hierMesh();
							string = HierMesh.collisionNameMulti(i, 0);
						}
						if (shot.power > 0.0F) {
							Point3d point3d = Pd;
							Point3d point3d_71_ = tmpP1;
							Point3d point3d_72_ = tmpP2;
							hierMesh();
							point3d.interpolate(point3d_71_, point3d_72_, HierMesh.collisionDistMulti(i));
							tmpLoc3.transformInv(Pd);
							debuggunnery("Hit Bone [" + string + "], E = " + (int) shot.power);
							hitBone(string, shot, Pd);
							if (!string.startsWith("xx")) {
								Aircraft aircraft_73_ = this;
								float f = 33.333F;
								float f_74_;
								if (i == tmpBonesHit - 1) f_74_ = 0.02F;
								else {
									hierMesh();
									float f_75_ = HierMesh.collisionDistMulti(i + 1);
									hierMesh();
									f_74_ = (f_75_ - HierMesh.collisionDistMulti(i));
								}
								aircraft_73_.getEnergyPastArmor(f * f_74_, shot);
								if (World.Rnd().nextFloat() < 0.05F) {
									shot.power = 0.0F;
									debuggunnery("Inner Ricochet");
								}
							}
							if (FM.CT.bHasGearControl && (string.toLowerCase().startsWith("xgear") || string.toLowerCase().startsWith("gear")) && getEnergyPastArmor(2.0F, shot) > 0.0F && shot.power > 2500.0F) {
								int i_76_ = -1;
								String string_77_ = "";
								if (string.toLowerCase().startsWith("x")) string_77_ = string.substring(5, 6).toUpperCase();
								else string_77_ = string.substring(4, 5).toUpperCase();
								int i_78_ = 0;
								int i_79_ = World.Rnd().nextInt(0, 150);
								if (FM.CT.getGear() > 0.99F) {
									if (i_79_ < 5) i_78_ = 8;
									else if (i_79_ < 10) i_78_ = 7;
									else if (i_79_ < 15) i_78_ = 1;
									else if (i_79_ < 20) i_78_ = 2;
									else if (i_79_ < 25) i_78_ = 3;
									else if (i_79_ < 30) i_78_ = 10;
								} else if (string.indexOf("1") != -1) {
									if (i_79_ < 5) i_78_ = 8;
									else if (i_79_ < 10) i_78_ = 7;
									else if (i_79_ < 15) i_78_ = 1;
									else if (i_79_ < 20) i_78_ = 2;
									else if (i_79_ < 25) i_78_ = 3;
									else if (i_79_ < 30) i_78_ = 10;
								} else if (i_79_ < 1) i_78_ = 6;
								else if (i_79_ < 2) i_78_ = 5;
								else if (i_79_ < 3) i_78_ = 4;
								else if (i_79_ < 12) i_78_ = 2;
								else if (i_79_ < 17) i_78_ = 9;
								else if (i_79_ < 27) i_78_ = 1;
								else if (i_79_ < 32) i_78_ = 10;
								else if (i_79_ < 35) i_78_ = 8;
								else if (i_79_ < 37) i_78_ = 7;
								else if (i_79_ < 40) i_78_ = 3;
								if (string_77_.equals("L")) i_76_ = 0;
								else if (string_77_.equals("R")) i_76_ = 1;
								else if (string_77_.equals("C")) i_76_ = 2;
								if ((i_76_ == 0 || i_76_ == 1) && i_79_ > 99 && chunkDamageVisible("WingLIn") > 1 && chunkDamageVisible("WingRIn") > 1) {
									FM.AS.hitGear(shot.initiator, 0, 4);
									FM.AS.hitGear(shot.initiator, 1, 4);
								} else if (i_78_ != 0) {
									if (shot.power > 40000.0F) i_76_ += 3;
									if (i_78_ == 8) {
										if (World.Rnd().nextFloat() < 0.5F) {
											FM.AS.hitGear(shot.initiator, 1, 8);
											FM.AS.hitGear(shot.initiator, 0, 8);
										} else FM.AS.hitGear(shot.initiator, i_76_, i_78_);
									} else FM.AS.hitGear(shot.initiator, i_76_, i_78_);
								}
							}
						}
					}
				}
				boolean bool = false;
				for (int i = 0; i < tmpBonesHit; i++) {
					hierMesh();
					if (HierMesh.collisionNameMulti(i, 1) != null) {
						hierMesh();
						String string = HierMesh.collisionNameMulti(i, 1);
						hierMesh();
						if (!string.equals(HierMesh.collisionNameMulti(i, 0))) continue;
					}
					bool = true;
				}
				if (bool) {
					debuggunnery("[+++ PROCESS OLD +++]");
					Shot shot_80_ = shot;
					hierMesh();
					shot_80_.chunkName = HierMesh.collisionNameMulti(0, 0);
					if (shot.chunkName.startsWith("WingLOut") && World.Rnd().nextInt(0, 99) < 20) shot.chunkName = "AroneL_D0";
					if (shot.chunkName.startsWith("WingROut") && World.Rnd().nextInt(0, 99) < 20) shot.chunkName = "AroneR_D0";
					if (shot.chunkName.startsWith("StabL") && World.Rnd().nextInt(0, 99) < 45) shot.chunkName = "VatorL_D0";
					if (shot.chunkName.startsWith("StabR") && World.Rnd().nextInt(0, 99) < 45) shot.chunkName = "VatorR_D0";
					if (shot.chunkName.startsWith("Keel1") && World.Rnd().nextInt(0, 99) < 33) shot.chunkName = "Rudder1_D0";
					if (shot.chunkName.startsWith("Keel2") && World.Rnd().nextInt(0, 99) < 33) shot.chunkName = "Rudder2_D0";
					float f = shot.powerToTNT();
					debugprintln("Bullet hit from " + (shot.initiator instanceof Aircraft ? ((Aircraft) shot.initiator).typedName() : shot.initiator.name()) + " in " + shot.chunkName + " for "
							+ (int) (100.0F * f / (0.01F + (3.0F * (FM.Sq.getToughness(part(shot.chunkName)))))) + " %..");
					shot.bodyMaterial = 2;
					if (FM instanceof Pilot && World.Rnd().nextInt(-1, 8) < FM.Skill) ((Pilot) FM).setAsDanger(shot.initiator);
					if (f <= 5.0000006E-7F) return;
					if (shot.chunkName.endsWith("_D0") && !shot.chunkName.startsWith("Gear")) {
						if (f > 0.01F) f = (1.0F + ((f - 0.01F) / FM.Sq.getToughness(part(shot.chunkName))));
						else f /= 0.01F;
					} else f /= FM.Sq.getToughness(part(shot.chunkName));
					f += FM.Sq.eAbsorber[part(shot.chunkName)];
					int i = (int) f;
					FM.Sq.eAbsorber[part(shot.chunkName)] = f - (float) i;
					if (i > 0) {
						setDamager(shot.initiator, i);
						if (shot.chunkName.startsWith("Pilot")) {
							killPilot(shot.initiator, shot.chunkName.charAt(5) - '1');
							return;
						}
						if (shot.chunkName.startsWith("Head")) {
							killPilot(shot.initiator, shot.chunkName.charAt(4) - '1');
							return;
						}
					}
					nextDMGLevels(i, 2, shot.chunkName, shot.initiator);
				}
				if (bWasAlive && FM.isTakenMortalDamage() && getDamager() instanceof Aircraft && FM.actor.getArmy() != getDamager().getArmy() && World.Rnd().nextInt(0, 99) < 66) {
					if (!buried) Voice.speakNiceKill((Aircraft) getDamager());
					buried = true;
				}
				bWasAlive = true;
			}
		}
	}

	private String sttp(int i) {
		switch (i) {
		case 2: // '\002'
			return "AP";

		case 3: // '\003'
			return "API/APIT";

		case 1: // '\001'
			return "CUMULATIVE";

		case 0: // '\0'
			return "HE";
		}
		return null;
	}

	public void hitGearPanels(int i, int j) {
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
	}

	protected void hitChunk(String s, Shot shot) {
		if (s.lastIndexOf("_") == -1) s = s + "_D" + chunkDamageVisible(s);
		float f = shot.powerToTNT();
		if (s.endsWith("_D0") && !s.startsWith("Gear")) {
			if (f > 0.01F) f = 1.0F + (f - 0.01F) / FM.Sq.getToughness(part(s));
			else f /= 0.01F;
		} else {
			f /= FM.Sq.getToughness(part(s));
		}
		f += FM.Sq.eAbsorber[part(s)];
		int i = (int) f;
		FM.Sq.eAbsorber[part(s)] = f - (float) i;
		if (i > 0) setDamager(shot.initiator, i);
		nextDMGLevels(i, 2, s, shot.initiator);
	}

	protected void hitFlesh(int i, Shot shot, int j) {
		int k = (int) (shot.power * 0.0035F * World.Rnd().nextFloat(0.5F, 1.5F));
		switch (j) {
		case 0: // '\0'
			if (World.Rnd().nextFloat() < 0.05F) return;
			if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
			k = (int) ((float) k * 30F);
			break;

		case 2: // '\002'
			k = (int) ((float) k / 3F);
			break;
		}
		debuggunnery("*** Pilot " + i + " hit for " + k + "% (" + (int) shot.power + " J)");
		FM.AS.hitPilot(shot.initiator, i, k);
		if (FM.AS.astatePilotStates[i] > 95 && j == 0) debuggunnery("*** Headshot!.");
	}

	protected float getEnergyPastArmor(float f, float f1, Shot shot) {
		shot.power -= (float) ((shot.powerType != 0 ? 1.0D : 2.0D) * ((double) f * 1700D * Math.cos(f1)));
		return shot.power;
	}

	protected float getEnergyPastArmor(float f, Shot shot) {
		shot.power -= (shot.powerType != 0 ? 1.0F : 2.0F) * (f * 1700F);
		return shot.power;
	}

	public static boolean isArmorPenetrated(float f, Shot shot) {
		return shot.power > (shot.powerType != 0 ? 1.0F : 2.0F) * (f * 1700F);
	}

	protected float getEnergyPastArmor(double d, float f, Shot shot) {
		shot.power -= (float) ((shot.powerType != 0 ? 1.0D : 2.0D) * (d * 1700D * Math.cos(f)));
		return shot.power;
	}

	protected float getEnergyPastArmor(double d, Shot shot) {
		shot.power -= (float) ((shot.powerType != 0 ? 1.0D : 2.0D) * (d * 1700D));
		return shot.power;
	}

	public static boolean isArmorPenetrated(double d, Shot shot) {
		return (double) shot.power > (shot.powerType != 0 ? 1.0D : 2.0D) * (d * 1700D);
	}

	protected void netHits(int i, int j, int k, Actor actor) {
		if (isNetMaster()) setDamager(actor, i);
		while (i-- > 0)
			nextDMGLevel(partNames[k] + "_D0", j, actor);
	}

	public int curDMGProp(int i) {
		String s = "Prop" + (i + 1) + "_D1";
		HierMesh hiermesh = hierMesh();
		if (hiermesh.chunkFindCheck(s) < 0) return 0;
		return !hiermesh.isChunkVisible(s) ? 0 : 1;
	}

	protected void addGun(BulletEmitter bulletemitter, int i) {
		if (this == World.getPlayerAircraft() && !World.cur().diffCur.Limited_Ammo) bulletemitter.loadBullets(-1);
		String s = bulletemitter.getHookName();
		if (s == null) return;
		BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
		int j;
		if (abulletemitter == null) j = 0;
		else j = abulletemitter.length;
		BulletEmitter abulletemitter1[] = new BulletEmitter[j + 1];
		int k;
		for (k = 0; k < j; k++)
			abulletemitter1[k] = abulletemitter[k];

		abulletemitter1[k] = bulletemitter;
		FM.CT.Weapons[i] = abulletemitter1;
		if (bulletemitter.isEnablePause()) bGunPodsExist = true;
	}

	public void detachGun(int i) {
		if (FM == null || FM.CT == null || FM.CT.Weapons == null) return;
		for (int j = 0; j < FM.CT.Weapons.length; j++) {
			BulletEmitter abulletemitter[] = FM.CT.Weapons[j];
			if (abulletemitter == null) continue;
			for (int k = 0; k < abulletemitter.length; k++)
				abulletemitter[k] = abulletemitter[k].detach(hierMesh(), i);

		}

	}

	public Gun getGunByHookName(String s) {
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
			if (abulletemitter == null) continue;
			for (int j = 0; j < abulletemitter.length; j++) {
				if (!(abulletemitter[j] instanceof Gun)) continue;
				Gun gun = (Gun) abulletemitter[j];
				if (s.equals(gun.getHookName())) return gun;
			}

		}

		return GunEmpty.get();
	}

	public BulletEmitter getBulletEmitterByHookName(String s) {
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
			if (abulletemitter == null) continue;
			for (int j = 0; j < abulletemitter.length; j++) {
				if (s.equals(abulletemitter[j].getHookName())) return abulletemitter[j];
			}
		}

		return GunEmpty.get();
	}

	public static void moveGear(HierMesh hiermesh, float f) {
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
		moveGear(f);
	}

	public void forceGear(float f) {
		moveGear(f, f, f);
	}

	public static void forceGear(Class class1, HierMesh hiermesh, float f) {
		try {
			Method method = class1.getMethod("moveGear", new Class[] { com.maddox.il2.engine.HierMesh.class, Float.TYPE, Float.TYPE, Float.TYPE });
			method.invoke(null, new Object[] { hiermesh, new Float(f), new Float(f), new Float(f) });
			method = class1.getMethod("moveGear", new Class[] { com.maddox.il2.engine.HierMesh.class, Float.TYPE });
			method.invoke(null, new Object[] { hiermesh, new Float(f) });
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static void forceParkingStyle(Class class1, HierMesh hiermesh) {
		try {
			Method method = class1.getMethod("forceParkingStyle", new Class[] { com.maddox.il2.engine.HierMesh.class });
			method.invoke(null, new Object[] { hiermesh });
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static void forceParkingStyle(HierMesh hiermesh) {
	}

	public void moveArrestorHook(float f) {
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
	}

	public void moveWingFold(float f) {
	}

	public void moveCockpitDoor(float f) {
	}

	protected void moveRudder(float f) {
	}

	protected void moveElevator(float f) {
	}

	protected void moveAileron(float f) {
	}

	protected void moveFlap(float f) {
	}

	// TODO:
	protected void moveVarWing(float f) {
	}

	protected void moveBayDoor(float f) {
	}

	protected void moveAirBrake(float f) {
	}

	// TODO:
	protected void moveRefuel(float f) {
	}

	protected void moveCatLaunchBar(float f) {
	}

	public void moveSteering(float f) {
	}

	public void moveWheelSink() {
	}

	public void rareAction(float f, boolean flag) {
	}

	public float getBayDoor() {
		return BayDoor_;
	}

	protected void moveFan(float f) {
		int i = 0;
		for (int j = 0; j < FM.EI.getNum(); j++) {
			if (oldProp[j] < 2) {
				i = Math.abs((int) (FM.EI.engines[j].getw() * 0.06F));
				if (i >= 1) i = 1;
				if (i != oldProp[j] && hierMesh().isChunkVisible(Props[j][oldProp[j]])) {
					hierMesh().chunkVisible(Props[j][oldProp[j]], false);
					oldProp[j] = i;
					hierMesh().chunkVisible(Props[j][i], true);
				}
			}
			if (i == 0) {
				propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[j].getw() * f) % 360F;
			} else {
				float f1 = 57.3F * FM.EI.engines[j].getw();
				f1 %= 2880F;
				f1 /= 2880F;
				if (f1 <= 0.5F) f1 *= 2.0F;
				else f1 = f1 * 2.0F - 2.0F;
				f1 *= 1200F;
				propPos[j] = (propPos[j] + f1 * f) % 360F;
			}
			hierMesh().chunkSetAngles(Props[j][0], 0.0F, -propPos[j], 0.0F);
		}

	}

	public void hitProp(int i, int j, Actor actor) {
		if (i > FM.EI.getNum() - 1 || oldProp[i] == 2) return;
		super.hitProp(i, j, actor);
		FM.cut(part("Engine" + (i + 1)), j, actor);
		if (isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) {
			hierMesh().chunkVisible(Props[i][0], false);
			hierMesh().chunkVisible(Props[i][1], false);
			hierMesh().chunkVisible(Props[i][2], true);
		}
		FM.EI.engines[i].setFricCoeffT(2.0F);
		// TODO: If the prop is hit, tells motor code to stop engine spinning immediately instead of windmilling
		FM.EI.engines[i].bPropHit = true;
		oldProp[i] = 2;
	}

	public void updateLLights() {
		pos.getRender(_tmpLoc);
		if (lLight == null) {
			if (_tmpLoc.getX() < 1.0D) return;
			lLight = (new LightPointWorld[] { null, null, null, null });
			for (int i = 0; i < 4; i++) {
				lLight[i] = new LightPointWorld();
				lLight[i].setColor(0.4941176F, 0.9098039F, 0.9607843F);
				lLight[i].setEmit(0.0F, 0.0F);
				try {
					lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
				} catch (Exception exception) {
				}
			}

			return;
		}
		for (int j = 0; j < 4; j++) {
			if (FM.AS.astateLandingLightEffects[j] != null) {
				lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				lLightHook[j].computePos(this, _tmpLoc, lLightLoc1);
				lLightLoc1.get(lLightP1);
				lLightLoc1.set(1000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				lLightHook[j].computePos(this, _tmpLoc, lLightLoc1);
				lLightLoc1.get(lLightP2);
				Engine.land();
				if (Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL)) {
					lLightPL.z++;
					lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
					lLight[j].setPos(lLightP2);
					float f = (float) lLightP1.distance(lLightPL);
					float f1 = f * 0.5F + 30F;
					float f2 = 0.5F - (0.5F * f) / 1000F;
					lLight[j].setEmit(f2, f1);
				} else {
					lLight[j].setEmit(0.0F, 0.0F);
				}
				continue;
			}
			if (lLight[j].getR() != 0.0F) lLight[j].setEmit(0.0F, 0.0F);
		}

	}

	public boolean isUnderWater() {
		Point3d point3d = pos.getAbsPoint();
		if (!Engine.land().isWater(point3d.x, point3d.y)) return false;
		else return point3d.z < 0.0D;
	}

	public void update(float f) {
		super.update(f);
		if (this == World.getPlayerAircraft()) {
			if (isUnderWater()) World.doPlayerUnderWater();
			EventLog.flyPlayer(pos.getAbsPoint());
			if (this instanceof TypeBomber) ((TypeBomber) this).typeBomberUpdate(f);
		}
		Controls controls = FM.CT;
		moveFan(f);
		if (controls.bHasGearControl) {
			float f6 = controls.getGearL();
			float f7 = controls.getGearR();
			float f8 = controls.getGearC();
			if (Math.abs(GearL_ - f6) > 0.003F) {
				if (!(this instanceof I_16)) if (Math.abs(f6 - controls.GearControl) <= 0.003F) sfxGear(false);
				else sfxGear(true);
				moveGear(GearL_ = f6, GearR_ = f7, GearC_ = f8);
			} else if (Math.abs(GearR_ - f7) > 0.003F) {
				if (!(this instanceof I_16)) if (Math.abs(f7 - controls.GearControl) <= 0.003F) sfxGear(false);
				else sfxGear(true);
				moveGear(GearL_ = f6, GearR_ = f7, GearC_ = f8);
			} else if (Math.abs(GearC_ - f8) > 0.003F) {
				if (!(this instanceof I_16)) if (Math.abs(f8 - controls.GearControl) <= 0.003F) sfxGear(false);
				else sfxGear(true);
				moveGear(GearL_ = f6, GearR_ = f7, GearC_ = f8);
			}
		}
		if (controls.bHasArrestorControl) {
			float f1 = controls.getArrestor();
			if (Math.abs(arrestor_ - f1) > 0.003F) moveArrestorHook(arrestor_ = f1);
		}
		if (controls.bHasWingControl) {
			float f2 = controls.getWing();
			if (Math.abs(wingfold_ - f2) > 0.0005F) moveWingFold(wingfold_ = f2);
		}
		if (controls.bHasCockpitDoorControl) {
			float f3 = controls.getCockpitDoor();
			if (Math.abs(cockpitDoor_ - f3) > 0.0005F) moveCockpitDoor(cockpitDoor_ = f3);
		}
		if (controls.bHasFlapsControl) {
			float f4 = controls.getFlap();
			if (Math.abs(Flap_ - f4) > 0.003F) {
				if (Math.abs(f4 - controls.FlapsControl) <= 0.003F) sfxFlaps(false);
				else sfxFlaps(true);
				moveFlap(Flap_ = f4);
			}
		}
		// TODO: Variable Wing Control
		if (controls.bHasVarWingControl) {
			float f_102x_ = controls.getVarWing();
			if (Math.abs(VarWing_ - f_102x_) > EpsSmooth_) {
				if (Math.abs(f_102x_ - controls.VarWingControl) <= EpsSmooth_) sfxFlaps(false);
				else sfxFlaps(true);
				moveVarWing(VarWing_ = f_102x_);
			}
		}
		float f5 = controls.getRudder();
		if (Math.abs(Rudder_ - f5) > 0.03F) moveRudder(Rudder_ = f5);
		f5 = controls.getElevator();
		if (Math.abs(Elevator_ - f5) > 0.03F) moveElevator(Elevator_ = f5);
		f5 = controls.getAileron();
		if (Math.abs(Aileron_ - f5) > 0.03F) moveAileron(Aileron_ = f5);
		f5 = controls.getBayDoor();
		if (Math.abs(BayDoor_ - f5) > 0.025F) {
			BayDoor_ += 0.025F * (f5 <= BayDoor_ ? -1F : 2.0F);
			moveBayDoor(BayDoor_);
		}
		f5 = controls.getAirBrake();
		if (Math.abs(AirBrake_ - f5) > 0.003F) {
			moveAirBrake(AirBrake_ = f5);
			if (Math.abs(AirBrake_ - 0.5F) >= 0.48F) sfxAirBrake();
		}
		// TODO: Added for refuelling equipment
		f5 = controls.getRefuel();
		if (Math.abs(Refuel_ - f5) > EpsSmooth_) {
			moveRefuel(Refuel_ = f5);
			if (Math.abs(Refuel_ - 0.5F) >= 0.48F) sfxAirBrake();
		}
		// TODO: Added for catapult launch bar on the nose gear
		f5 = controls.getCatLaunchBar();
		if (Math.abs(CatLaunchBar_ - f5) > EpsSmooth_) {
			moveCatLaunchBar(CatLaunchBar_ = f5);
		}
		f5 = FM.Gears.getSteeringAngle();
		if (Math.abs(Steering_ - f5) > 0.003F) moveSteering(Steering_ = f5);
		if (FM.Gears.nearGround()) moveWheelSink();
		if (Mission.isSingle()) {
			if (this == World.getPlayerAircraft()) {
				RealFlightModel realflightmodel = (RealFlightModel) World.getPlayerFM();
				if (realflightmodel.isRealMode() && isAircraftTaxing()) {
					Maneuver.updateCollisionMap(realflightmodel);
					taxHlpX -= 1.0F * f;
				} else {
					showTaxingWay = false;
				}
			}
		} else {
			if (Mission.isCoop() && this == World.getPlayerAircraft()) if (isAircraftTaxing()) taxHlpX -= 1.0F * f;
			else showTaxingWay = false;
			if (Mission.isServer() && isNetPlayer() && isAircraftTaxing()) Maneuver.updateCollisionMap(FM);
		}
	}

	public void setFM(int i, boolean flag) {
		setFM(Property.stringValue(getClass(), "FlightModel", null), i, flag);
	}

	public void setFM(String s, int i, boolean flag) {
		if (this instanceof JU_88MSTL) i = 1;
		switch (i) {
		case 0: // '\0'
		default:
			FM = new Pilot(s);
			break;

		case 1: // '\001'
			FM = new RealFlightModel(s);
			break;

		case 2: // '\002'
			FM = new FlightModel(s);
			FM.AP = new Autopilotage();
			// By western, add reserve AP
			FM.APreserve = new Autopilotage();
			break;
		}
		FM.actor = this;
		FM.AS.set(this, flag && !NetMissionTrack.isPlaying());
		FM.EI.setNotMirror(flag && !NetMissionTrack.isPlaying());
		SectFile sectfile = FlightModelMain.sectFile(s);
		byte byte0 = 0;
		String s1 = sectfile.get("SOUND", "FeedType", "PNEUMATIC");
		if (s1.compareToIgnoreCase("PNEUMATIC") == 0) byte0 = 0;
		else if (s1.compareToIgnoreCase("ELECTRIC") == 0) byte0 = 1;
		else if (s1.compareToIgnoreCase("HYDRAULIC") == 0) byte0 = 2;
		else System.out.println("ERROR: Invalid feed type" + s1);
		FM.set(hierMesh());
		forceGear(getClass(), hierMesh(), 1.0F);
		FM.Gears.computePlaneLandPose(FM);
		forceGear(getClass(), hierMesh(), 0.0F);
		FM.EI.set(this);
		initSound(sectfile);
		sfxInit(byte0);
		interpPut(FM, "FlightModel", Time.current(), null);
	}

	public void destroy() {
		if (isAlive() && Mission.isPlaying() && name().charAt(0) != ' ' && FM != null) {
			Front.checkAircraftCaptured(this);
			World.onActorDied(this, World.remover);
		}
		if (lLight != null) {
			for (int i = 0; i < 4; i++)
				ObjState.destroy(lLight[i]);

		}
		if (World.getPlayerAircraft() == this) deleteCockpits();
		Wing wing = getWing();
		if (Actor.isValid(wing) && (wing instanceof NetWing)) wing.destroy();
		detachGun(-1);
		super.destroy();
		_removeMesh();
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

	public Aircraft() {
		spawnLocSingleCoop = null;
		spawnActorName = null;
		stationarySpawnLocSet = false;
		timePostEndAction = -1L;
		buried = false;
		BayDoor_ = 0.0F;
		AirBrake_ = 0.0F;
		Steering_ = 0.0F;
		wingfold_ = 0.0F;
		cockpitDoor_ = 0.0F;
		arrestor_ = 0.0F;
		typedName = "UNKNOWN";
		wfrGr21dropped = false;
		bombScoreOwner = null;
		headingBug = 0.0F;
		idleTimeOnCarrier = 0;
		bSpotter = false;
		checkLoadingCountry();
		if (_loadingCountry == null) _setMesh(Property.stringValue(getClass(), "meshName", null));
		else _setMesh(Property.stringValue(getClass(), "meshName_" + _loadingCountry, null));
		collide(true);
		drawing(true);
		dreamFire(true);
	}

	private void checkLoadingCountry() {
		_loadingCountry = null;
		if (loadingCountry == null) return;
		Class class1 = getClass();
		if (Property.value(class1, "PaintScheme_" + loadingCountry) != null && Property.stringValue(class1, "meshName_" + loadingCountry, null) != null) _loadingCountry = loadingCountry;
	}

	public static String getPropertyMeshDemo(Class class1, String s) {
		String s1 = "meshNameDemo";
		String s2 = Property.stringValue(class1, s1, (String) null);
		if (s2 != null) return s2;
		else return getPropertyMesh(class1, s);
	}

	public static String getPropertyMesh(Class class1, String s) {
		String s1 = "meshName";
		String s2 = null;
		if (s != null) s2 = Property.stringValue(class1, s1 + "_" + s, null);
		if (s2 == null) s2 = Property.stringValue(class1, s1);
		return s2;
	}

	public static PaintScheme getPropertyPaintScheme(Class class1, String s) {
		String s1 = "PaintScheme";
		PaintScheme paintscheme = null;
		if (s != null) paintscheme = (PaintScheme) Property.value(class1, s1 + "_" + s, null);
		if (paintscheme == null) paintscheme = (PaintScheme) Property.value(class1, s1);
		return paintscheme;
	}

	public String typedName() {
		return typedName;
	}

	private void correctTypedName() {
		if (typedName != null && typedName.indexOf('_') >= 0) {
			StringBuffer stringbuffer = new StringBuffer();
			int i = typedName.length();
			for (int j = 0; j < i; j++) {
				char c = typedName.charAt(j);
				if (c != '_') stringbuffer.append(c);
			}

			typedName = stringbuffer.toString();
		}
	}

	public void preparePaintScheme() {
		PaintScheme paintscheme = getPropertyPaintScheme(getClass(), _loadingCountry);
		if (paintscheme != null) {
			paintscheme.prepare(this, bPaintShemeNumberOn);
			typedName = paintscheme.typedName(this);
			correctTypedName();
		}
	}

	public void preparePaintScheme(int i) {
		PaintScheme paintscheme = getPropertyPaintScheme(getClass(), _loadingCountry);
		if (paintscheme != null) {
			paintscheme.prepareNum(this, i, bPaintShemeNumberOn);
			typedName = paintscheme.typedNameNum(this, i);
			correctTypedName();
		}
	}

	public void prepareCamouflage() {
		String s = getPropertyMesh(getClass(), _loadingCountry);
		prepareMeshCamouflage(s, hierMesh(), getClass(), getRegiment());
	}

	public static void prepareMeshCamouflage(String s, HierMesh hiermesh, Class class1, Regiment regiment) {
		prepareMeshCamouflage(s, hiermesh, null, class1, regiment);
	}

	public static void prepareMeshCamouflage(String s, HierMesh hiermesh, String s1, Class class1, Regiment regiment) {
		prepareMeshCamouflage(s, hiermesh, s1, null, class1, regiment);
	}

	public static void prepareMeshCamouflage(String s, HierMesh hiermesh, String s1, Mat amat[], Class class1, Regiment regiment) {
		if (!Config.isUSE_RENDER()) return;
		String s2 = s.substring(0, s.lastIndexOf('/') + 1);
		String s3 = "";
		if (class1 != null) try {
			Method method = class1.getMethod("getSkinPrefix", new Class[] { java.lang.String.class, com.maddox.il2.ai.Regiment.class });
			s3 = (String) method.invoke(null, new Object[] { new String(s), regiment });
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		if (s1 == null) {
			String s5;
			switch (World.cur().camouflage) {
			case 0: // '\0'
				s5 = "summer";
				break;

			case 1: // '\001'
				s5 = "winter";
				break;

			case 2: // '\002'
				s5 = "desert";
				break;

			case 3: // '\003'
				s5 = "pacific";
				break;

			case 4: // '\004'
				s5 = "eto";
				break;

			case 5: // '\005'
				s5 = "mto";
				break;

			case 6: // '\006'
				s5 = "cbi";
				break;

			default:
				s5 = "summer";
				break;
			}
			String s4 = s3 + s5;
			if (!existSFSFile(s2 + s4 + "/skin1o.tga")) if (World.cur().camouflage == 5) {
				s4 = s3 + "desert";
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) s4 = s5;
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) s4 = "desert";
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) s4 = "summer";
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) return;
			} else {
				s4 = s3 + "summer";
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) s4 = s5;
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) s4 = "summer";
				if (!existSFSFile(s2 + s4 + "/skin1o.tga")) return;
			}
			s1 = s2 + s4;
		}
		String as[] = { s1 + "/skin1o.tga", s1 + "/skin1p.tga", s1 + "/skin1q.tga" };
		int ai[] = new int[4];
		for (int i = 0; i < _skinMat.length; i++) {
			int j = hiermesh.materialFind(_skinMat[i]);
			if (j < 0) continue;
			Mat mat = hiermesh.material(j);
			boolean flag = false;
			for (int k = 0; k < 4; k++) {
				ai[k] = -1;
				if (!mat.isValidLayer(k)) continue;
				mat.setLayer(k);
				String s7 = mat.get('\0');
				for (int l = 0; l < 3; l++) {
					if (s7.regionMatches(true, s7.length() - 10, _curSkin[l], 0, 10)) {
						ai[k] = l;
						flag = true;
						break;
					}
				}
			}

			if (!flag) continue;
			String s6 = s1 + "/" + _skinMat[i] + ".mat";
			Mat mat1;
			if (FObj.Exist(s6)) {
				mat1 = (Mat) FObj.Get(s6);
			} else {
				mat1 = (Mat) mat.Clone();
				mat1.Rename(s6);
				for (int i1 = 0; i1 < 4; i1++)
					if (ai[i1] >= 0) {
						mat1.setLayer(i1);
						mat1.set('\0', as[ai[i1]]);
					}

			}
			if (amat != null) {
				for (int j1 = 0; j1 < 4; j1++)
					if (ai[j1] >= 0) amat[ai[j1]] = mat1;

			}
			hiermesh.materialReplace(_skinMat[i], mat1);
		}

	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		return "";
	}

	public static boolean prepareMeshSkin(String s, HierMesh hiermesh, String s1, String s2, Regiment regiment) {
		String s3 = s;
		int i = s3.lastIndexOf('/');
		if (i >= 0) s3 = s3.substring(0, i + 1) + "summer";
		else s3 = s3 + "summer";
		try {
			File file = new File(HomePath.toFileSystemName(s2, 0));
			if (!file.isDirectory()) file.mkdir();
		} catch (Exception exception) {
			return false;
		}
		if (!BmpUtils.bmp8PalTo4TGA4(s1, s3, s2)) return false;
		if (s2 == null) {
			return false;
		} else {
			prepareMeshCamouflage(s, hiermesh, s2, null, regiment);
			return true;
		}
	}

	public static void prepareMeshPilot(HierMesh hiermesh, int i, String s, String s1) {
		prepareMeshPilot(hiermesh, i, s, s1, null);
	}

	public static void prepareMeshPilot(HierMesh hiermesh, int i, String s, String s1, Mat amat[]) {
		if (!Config.isUSE_RENDER()) return;
		String s2 = "Pilot" + (1 + i);
		int j = hiermesh.materialFind(s2);
		if (j < 0) return;
		Mat mat;
		if (FObj.Exist(s)) {
			mat = (Mat) FObj.Get(s);
		} else {
			Mat mat1 = hiermesh.material(j);
			mat = (Mat) mat1.Clone();
			mat.Rename(s);
			mat.setLayer(0);
			mat.set('\0', s1);
		}
		if (amat != null) amat[0] = mat;
		hiermesh.materialReplace(s2, mat);
	}

	public static void prepareMeshNoseart(HierMesh hiermesh, String s, String s1, String s2, String s3) {
		prepareMeshNoseart(hiermesh, s, s1, s2, s3, null);
	}

	public static void prepareMeshNoseart(HierMesh hiermesh, String s, String s1, String s2, String s3, Mat amat[]) {
		if (!Config.isUSE_RENDER()) return;
		String s4 = "Overlay9";
		int i = hiermesh.materialFind(s4);
		if (i < 0) return;
		Mat mat;
		if (FObj.Exist(s)) {
			mat = (Mat) FObj.Get(s);
		} else {
			Mat mat1 = hiermesh.material(i);
			mat = (Mat) mat1.Clone();
			mat.Rename(s);
			mat.setLayer(0);
			mat.set('\0', s2);
		}
		if (amat != null) amat[0] = mat;
		hiermesh.materialReplace(s4, mat);
		s4 = "OverlayA";
		i = hiermesh.materialFind(s4);
		if (i < 0) return;
		if (FObj.Exist(s1)) {
			mat = (Mat) FObj.Get(s1);
		} else {
			Mat mat2 = hiermesh.material(i);
			mat = (Mat) mat2.Clone();
			mat.Rename(s1);
			mat.setLayer(0);
			mat.set('\0', s3);
		}
		if (amat != null) amat[1] = mat;
		hiermesh.materialReplace(s4, mat);
	}

	private static boolean existSFSFile(String s) {
		try {
			SFSInputStream sfsinputstream = new SFSInputStream(s);
			sfsinputstream.close();
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public double getSpeed(Vector3d vector3d) {
		if (FM == null) {
			if (vector3d != null) vector3d.set(0.0D, 0.0D, 0.0D);
			return 0.0D;
		}
		if (vector3d != null) vector3d.set(FM.Vwld);
		return FM.Vwld.length();
	}

	public void setSpeed(Vector3d vector3d) {
		super.setSpeed(vector3d);
		FM.Vwld.set(vector3d);
	}

	public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
		FM.CT.setLanded();
		forceGear(getClass(), hierMesh(), FM.CT.getGear());
		if (point3d != null && orient != null) {
			pos.setAbs(point3d, orient);
			pos.reset();
		}
		if (vector3d != null) setSpeed(vector3d);
		// TODO: By western, forcing set Chocks when customized in conf.ini
		Vector3d vtemp = new Vector3d();
		FM.actor.pos.speed(vtemp);
		if (FM.Gears.bUseChocksParking && vtemp.length() == 0.0D && !(FM.actor instanceof TypeSeaPlane)) {
			FM.brakeShoe = true;
			FM.spawnedWithChocks = true;	// to avoid unremovable chocks trouble about IK-3 etc.
		}
	}

	public void load(SectFile sectfile, String s, int i, NetChannel netchannel, int j) throws Exception {
		if (this == World.getPlayerAircraft()) {
			setFM(1, true);
			World.setPlayerFM();
		} else if (netchannel != null) setFM(2, false);
		else setFM(0, true);
		String s1 = sectfile.get(s, "spawn" + i, "");
		if (!s1.equals("")) {
			spawnActorName = s1;
			int k = sectfile.sectionIndex("NStationary");
			if (k >= 0) {
				int l = sectfile.vars(k);
				int j1 = 0;
				do {
					if (j1 >= l) break;
					NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(k, j1));
					String s4 = numbertokenizer.next("");
					String s6 = numbertokenizer.next("");
					if (s4.equals(s1) && s6.startsWith("vehicles.planes.Plane")) {
						numbertokenizer.next(0);
						double d = numbertokenizer.next(0.0D);
						double d1 = numbertokenizer.next(0.0D);
						float f = numbertokenizer.next(0.0F);
						spawnLocSingleCoop = new Loc(d, d1, 0.0D, f, 0.0F, 0.0F);
						break;
					}
					j1++;
				} while (true);
			}
		}
		if (sectfile.exist(s, "Skill" + i)) FM.setSkill(sectfile.get(s, "Skill" + i, 1));
		else FM.setSkill(sectfile.get(s, "Skill", 1));
		FM.M.fuel = sectfile.get(s, "Fuel", 100F, 0.0F, 100F) * 0.01F * FM.M.maxFuel;
		if (sectfile.exist(s, "numberOn" + i)) bPaintShemeNumberOn = sectfile.get(s, "numberOn" + i, 1, 0, 1) == 1;
		FM.AS.bIsEnableToBailout = sectfile.get(s, "Parachute", 1, 0, 1) == 1;
		if (Mission.isServer()) createNetObject(null, 0);
		else if (netchannel != null) createNetObject(netchannel, j);
		if (net != null) {
			((NetAircraft.AircraftNet) net).netName = name();
			((NetAircraft.AircraftNet) net).netUser = null;
		}
		String s2 = s + "_weapons";
		int i1 = sectfile.sectionIndex(s2);
		if (i1 >= 0) {
			int k1 = sectfile.vars(i1);
			for (int l1 = 0; l1 < k1; l1++) {
				NumberTokenizer numbertokenizer1 = new NumberTokenizer(sectfile.line(i1, l1));
				int j2 = numbertokenizer1.next(9, 0, 19);
				String s3 = numbertokenizer1.next();
				String s5 = numbertokenizer1.next();
				Class class1 = ObjIO.classForName("weapons." + s5);
				Object obj = class1.newInstance();
				if (obj instanceof BulletEmitter) {
					BulletEmitter bulletemitter = (BulletEmitter) obj;
					bulletemitter.set(this, s3, dumpName(s3));
					int i2 = numbertokenizer1.next(-12345);
					if (i2 == -12345) bulletemitter.loadBullets();
					else bulletemitter._loadBullets(i2);
					addGun(bulletemitter, j2);
				}
			}

		} else {
			thisWeaponsName = sectfile.get(s, "weapons", (String) null);
			if (thisWeaponsName != null) weaponsLoad(this, thisWeaponsName);
		}
		if (this == World.getPlayerAircraft()) createCockpits();
		onAircraftLoaded();
		FM.Gears.zutiCheckPlaneForSkisAndWinterCamo(getClass().toString());
	}

	private static String dumpName(String s) {
		int i;
		for (i = s.length() - 1; i >= 0 && Character.isDigit(s.charAt(i)); i--)
			;
		i++;
		return s.substring(0, i) + "Dump" + s.substring(i);
	}

	public boolean turretAngles(int i, float af[]) {
		for (int j = 0; j < 2; j++) {
			af[j] = (af[j] + 3600F) % 360F;
			if (af[j] > 180F) af[j] -= 360F;
		}

		af[2] = 0.0F;
		return true;
	}

	public int WeaponsMask() {
		return -1;
	}

	public int HitbyMask() {
		try {
			return FM.Vwld.length() >= 2D ? -25 : -1;
		} catch (NullPointerException nullpointerexception) {
			return 0;
		}
	}

	public int chooseBulletType(BulletProperties abulletproperties[]) {
		if (FM.isTakenMortalDamage()) return -1;
		if (abulletproperties.length == 1) return 0;
		if (abulletproperties.length <= 0) return -1;
		if (abulletproperties[0].power <= 0.0F) return 1;
		if (abulletproperties[1].power <= 0.0F) return 0;
		if (abulletproperties[0].powerType == 1) return 0;
		if (abulletproperties[1].powerType == 1) return 1;
		if (abulletproperties[0].powerType == 0) return 0;
		if (abulletproperties[1].powerType == 0) return 1;
		return abulletproperties[0].powerType != 2 ? 0 : 1;
	}

	public int chooseShotpoint(BulletProperties bulletproperties) {
		return !FM.isTakenMortalDamage() ? 0 : -1;
	}

	public boolean getShotpointOffset(int i, Point3d point3d) {
		if (FM.isTakenMortalDamage()) return false;
		if (i != 0) return false;
		if (point3d != null) point3d.set(0.0D, 0.0D, 0.0D);
		return true;
	}

	public float AttackMaxDistance() {
		return 1500F;
	}

	private static int[] getSwTbl(int i) {
		if (i < 0) i = -i;
		int j = i % 16 + 11;
		int k = i % Finger.kTable.length;
		if (j < 0) j = -j % 16;
		if (j < 10) j = 10;
		if (k < 0) k = -k % Finger.kTable.length;
		int ai[] = new int[j];
		for (int l = 0; l < j; l++)
			ai[l] = Finger.kTable[(k + l) % Finger.kTable.length];

		return ai;
	}

	public static void weapons(Class class1) {
		try {
//			ArrayList arraylist = weaponsListProperty(class1);
//			HashMapInt hashmapint = weaponsMapProperty(class1);

			// By western: ignoring cod/ weapon loadout flag, added in Engine mod 2.8.12w
			boolean bIgnoreCodWeapon = (Property.intValue(class1, "IgnoreCodWeapon", 0) == 1);
			if (bIgnoreCodWeapon) return;
			int i = Finger.Int("ce" + class1.getName() + "vd");

			// FIXME: By SAS~Storebror: Avoid duplicate Loadout Lists
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new KryptoInputFilter(new SFSInputStream(Finger.LongFN(0L, "cod/" + Finger.incInt(i, "adt"))), getSwTbl(i))));
			ArrayList arraylist = new ArrayList();
			Property.set(class1, "weaponsList", arraylist);
			HashMapInt hashmapint = new HashMapInt();
			Property.set(class1, "weaponsMap", hashmapint);

			do {
				String s = bufferedreader.readLine();
				if (s == null) break;
				StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
				int j = stringtokenizer.countTokens() - 1;
				String s1 = stringtokenizer.nextToken();
				_WeaponSlot a_lweaponslot[] = new _WeaponSlot[j];
				for (int k = 0; k < j; k++) {
					String s2 = stringtokenizer.nextToken();
					if (s2 != null && s2.length() > 3) {
						NumberTokenizer numbertokenizer = new NumberTokenizer(s2);
						a_lweaponslot[k] = new _WeaponSlot(numbertokenizer.next(0), numbertokenizer.next(null), numbertokenizer.next(-12345));
					}
				}

				arraylist.add(s1);
				hashmapint.put(Finger.Int(s1), a_lweaponslot);
			} while (true);
			bufferedreader.close();

		// FIXME: By SAS~Storebror: Don't hide potential errors inside cod files
		} catch (FileNotFoundException fnfe) {
		} catch (Exception exception) {
			if (ObjectsLogLevel.getObjectsLogLevel() > ObjectsLogLevel.OBJECTS_LOGLEVEL_NONE)
				System.out.println("Some weapon class is missing. Loadout list is unfinished. Aircraft cod/ register error in " + class1.getName());
			if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
				exception.printStackTrace();
		}
	}

	public long finger(long l) {
		Class class1 = getClass();
		l = FlightModelMain.finger(l, Property.stringValue(class1, "FlightModel", null));
		l = Finger.incLong(l, Property.stringValue(class1, "meshName", null));
		Object obj = Property.value(class1, "cockpitClass", null);
		if (obj != null) if (obj instanceof Class) {
			l = Finger.incLong(l, ((Class) obj).getName());
		} else {
			Class aclass[] = (Class[]) (Class[]) obj;
			for (int j = 0; j < aclass.length; j++)
				l = Finger.incLong(l, aclass[j].getName());

		}
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
			if (abulletemitter == null) continue;
			for (int k = 0; k < abulletemitter.length; k++) {
				BulletEmitter bulletemitter = abulletemitter[k];
				l = Finger.incLong(l, Property.intValue(bulletemitter, "_count", 0));
				if (bulletemitter instanceof Gun) {
					GunProperties gunproperties = ((Gun) bulletemitter).prop;
					l = Finger.incLong(l, gunproperties.shotFreq);
					l = Finger.incLong(l, gunproperties.shotFreqDeviation);
					l = Finger.incLong(l, gunproperties.maxDeltaAngle);
					l = Finger.incLong(l, gunproperties.bullets);
					BulletProperties abulletproperties[] = gunproperties.bullet;
					if (abulletproperties == null) continue;
					for (int i1 = 0; i1 < abulletproperties.length; i1++) {
						l = Finger.incLong(l, abulletproperties[i1].massa);
						l = Finger.incLong(l, abulletproperties[i1].kalibr);
						l = Finger.incLong(l, abulletproperties[i1].speed);
						l = Finger.incLong(l, abulletproperties[i1].cumulativePower);
						l = Finger.incLong(l, abulletproperties[i1].power);
						l = Finger.incLong(l, abulletproperties[i1].powerType);
						l = Finger.incLong(l, abulletproperties[i1].powerRadius);
						l = Finger.incLong(l, abulletproperties[i1].timeLife);
					}

					continue;
				}
				if (bulletemitter instanceof RocketGun) {
					RocketGun rocketgun = (RocketGun) bulletemitter;
					Class class2 = (Class) Property.value(rocketgun.getClass(), "bulletClass", null);
					l = Finger.incLong(l, Property.intValue(rocketgun.getClass(), "bullets", 1));
					l = Finger.incLong(l, Property.floatValue(rocketgun.getClass(), "shotFreq", 0.5F));
					if (class2 != null) {
						l = Finger.incLong(l, Property.floatValue(class2, "radius", 1.0F));
						l = Finger.incLong(l, Property.floatValue(class2, "timeLife", 1.0F));
						l = Finger.incLong(l, Property.floatValue(class2, "timeFire", 1.0F));
						l = Finger.incLong(l, Property.floatValue(class2, "force", 1.0F));
						l = Finger.incLong(l, Property.floatValue(class2, "power", 1.0F));
						l = Finger.incLong(l, Property.intValue(class2, "powerType", 1));
						l = Finger.incLong(l, Property.floatValue(class2, "kalibr", 1.0F));
						l = Finger.incLong(l, Property.floatValue(class2, "massa", 1.0F));
						l = Finger.incLong(l, Property.floatValue(class2, "massaEnd", 1.0F));
					}
					continue;
				}
				if (!(bulletemitter instanceof BombGun)) continue;
				BombGun bombgun = (BombGun) bulletemitter;
				Class class3 = (Class) Property.value(bombgun.getClass(), "bulletClass", null);
				l = Finger.incLong(l, Property.intValue(bombgun.getClass(), "bullets", 1));
				l = Finger.incLong(l, Property.floatValue(bombgun.getClass(), "shotFreq", 0.5F));
				if (class3 != null) {
					l = Finger.incLong(l, Property.floatValue(class3, "radius", 1.0F));
					l = Finger.incLong(l, Property.floatValue(class3, "power", 1.0F));
					l = Finger.incLong(l, Property.intValue(class3, "powerType", 1));
					l = Finger.incLong(l, Property.floatValue(class3, "kalibr", 1.0F));
					l = Finger.incLong(l, Property.floatValue(class3, "massa", 1.0F));
				}
			}

		}

		return l;
	}

	protected static void weaponTriggersRegister(Class class1, int ai[]) {
		Property.set(class1, "weaponTriggers", ai);
	}

	public static int[] getWeaponTriggersRegistered(Class class1) {
		return (int[]) (int[]) Property.value(class1, "weaponTriggers", null);
	}

	protected static void weaponHooksRegister(Class class1, String as[]) {
		if (as.length != getWeaponTriggersRegistered(class1).length) {
			throw new RuntimeException("Sizeof 'weaponHooks' != sizeof 'weaponTriggers'");
		} else {
			Property.set(class1, "weaponHooks", as);
			return;
		}
	}

	public static String[] getWeaponHooksRegistered(Class class1) {
		return (String[]) (String[]) Property.value(class1, "weaponHooks", null);
	}

	protected static void weaponsRegister(Class class1, String s, String as[]) {
	}

	protected static void weaponsUnRegister(Class class1, String s) {
		ArrayList arraylist = weaponsListProperty(class1);
		HashMapInt hashmapint = weaponsMapProperty(class1);
		int i = arraylist.indexOf(s);
		if (i < 0) {
			return;
		} else {
			arraylist.remove(i);
			hashmapint.remove(Finger.Int(s));
			return;
		}
	}

	public static String[] getWeaponsRegistered(Class class1) {
		ArrayList arraylist = weaponsListProperty(class1);
		String as[] = new String[arraylist.size()];
		for (int i = 0; i < as.length; i++)
			as[i] = (String) arraylist.get(i);

		return as;
	}

	public static _WeaponSlot[] getWeaponSlotsRegistered(Class class1, String s) {
		HashMapInt hashmapint = weaponsMapProperty(class1);
		return (_WeaponSlot[]) (_WeaponSlot[]) hashmapint.get(Finger.Int(s));
	}

	public static boolean weaponsExist(Class class1, String s) {
		Object obj = Property.value(class1, "weaponsMap", null);
		if (obj == null) {
			return false;
		} else {
			HashMapInt hashmapint = (HashMapInt) obj;
			int i = Finger.Int(s);
			boolean flag = isWeaponDateOk(class1, s);
			return hashmapint.containsKey(i) && flag;
		}
	}

	public static boolean isWeaponDateOk(Class class1, String s) {
		HashMapInt hashmapint = weaponsMapProperty(class1);
		int i = Finger.Int(s);
		if (!hashmapint.containsKey(i)) return true;
		int j = Mission.getMissionDate(false);
		if (j == 0) return true;
		String s1 = "";
		try {
			s1 = class1.toString().substring(class1.toString().lastIndexOf(".") + 1, class1.toString().length());
		} catch (Exception exception) {
			return true;
		}
		String as[] = getWeaponHooksRegistered(class1);
		_WeaponSlot a_lweaponslot[] = (_WeaponSlot[]) (_WeaponSlot[]) hashmapint.get(i);
		for (int k = 0; k < as.length; k++) {
			if (a_lweaponslot[k] == null) continue;
			int l = Property.intValue(a_lweaponslot[k].clazz, "dateOfUse_" + s1, 0);
			if (l == 0) l = Property.intValue(a_lweaponslot[k].clazz, "dateOfUse", 0);
			if (l != 0 && j < l) return false;
		}

		return true;
	}

	protected void weaponsLoad(String s) throws Exception {
		weaponsLoad(this, s);
	}

	protected void weaponsLoad(Aircraft aircraft, String s) throws Exception {
		Class class1 = aircraft.getClass();
		HashMapInt hashmapint = weaponsMapProperty(class1);
		int i = Finger.Int(s);
		if (!hashmapint.containsKey(i)) {
			throw new RuntimeException("Weapon set '" + s + "' not registered in " + ObjIO.classGetName(class1));
		} else {
			weaponsLoad(aircraft, i, hashmapint);
			return;
		}
	}

	protected void weaponsLoad(Aircraft aircraft, int i) throws Exception {
		HashMapInt hashmapint = weaponsMapProperty(aircraft.getClass());
		if (!hashmapint.containsKey(i)) {
			throw new RuntimeException("Weapon set '" + i + "' not registered in " + ObjIO.classGetName(aircraft.getClass()));
		} else {
			weaponsLoad(aircraft, i, hashmapint);
			return;
		}
	}

	protected void weaponsLoad(Aircraft aircraft, int i, HashMapInt hashmapint) throws Exception {
		if (World.getPlayerAircraft() == aircraft) {
			Fuze_EL_AZ.reset();
			AircraftLH.clearInfo();
			Torpedo.clearInfo();
		}
		String as[] = getWeaponHooksRegistered(aircraft.getClass());
		_WeaponSlot a_lweaponslot[] = (_WeaponSlot[]) (_WeaponSlot[]) hashmapint.get(i);
		for (int j = 0; j < as.length; j++) {
			if (a_lweaponslot[j] == null) continue;
			if (aircraft.mesh().hookFind(as[j]) != -1) {
				BulletEmitter bulletemitter = (BulletEmitter) a_lweaponslot[j].clazz.newInstance();
				bulletemitter.set(aircraft, as[j], dumpName(as[j]));
				if (aircraft.isNet() && aircraft.isNetMirror()) {
					if (!World.cur().diffCur.Limited_Ammo) bulletemitter.loadBullets(-1);
					else if (a_lweaponslot[j].trigger == 2 || a_lweaponslot[j].trigger == 3 || a_lweaponslot[j].trigger >= 10) {
						if (a_lweaponslot[j].bullets == -12345) bulletemitter.loadBullets();
						else bulletemitter._loadBullets(a_lweaponslot[j].bullets);
					} else {
						bulletemitter.loadBullets(-1);
					}
				} else if (a_lweaponslot[j].bullets == -12345) bulletemitter.loadBullets();
				else bulletemitter.loadBullets(a_lweaponslot[j].bullets);
				aircraft.addGun(bulletemitter, a_lweaponslot[j].trigger);
				Property.set(bulletemitter, "_count", a_lweaponslot[j].bullets);
				switch (a_lweaponslot[j].trigger) {
				case 0: // '\0'
					if (bulletemitter instanceof MGunAircraftGeneric)
						if (World.getPlayerAircraft() == aircraft) ((MGunAircraftGeneric) bulletemitter).setConvDistance(World.cur().userCoverMashineGun, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F));
						else if (aircraft.isNet() && aircraft.isNetPlayer()) ((MGunAircraftGeneric) bulletemitter).setConvDistance(400F, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F));
						else ((MGunAircraftGeneric) bulletemitter).setConvDistance(FM.convAI + FM.convAI * 0.25F, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F));
					break;

				case 1: // '\001'
					if (bulletemitter instanceof MGunAircraftGeneric)
						if (World.getPlayerAircraft() == aircraft) ((MGunAircraftGeneric) bulletemitter).setConvDistance(World.cur().userCoverCannon, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F));
						else if (aircraft.isNet() && aircraft.isNetPlayer()) ((MGunAircraftGeneric) bulletemitter).setConvDistance(400F, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F));
						else ((MGunAircraftGeneric) bulletemitter).setConvDistance(FM.convAI, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F));
					break;

				// By western: Engine mod expanded rocket triggers 4,5,6 are treated as same as stock trigger 2
				case 2: // '\002'
				case 4:
				case 5:
				case 6:
					if (bulletemitter instanceof RocketGun) if (World.getPlayerAircraft() == aircraft) {
						((RocketGun) bulletemitter).setRocketTimeLife(World.cur().userRocketDelay);
						((RocketGun) bulletemitter).setConvDistance(World.cur().userCoverRocket, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F) - 2.81F);
					} else if (aircraft.isNet() && aircraft.isNetPlayer()) ((RocketGun) bulletemitter).setConvDistance(400F, Property.floatValue(aircraft.getClass(), "LOSElevation", 0.75F) - 2.81F);
					else if (aircraft instanceof TypeFighter) ((RocketGun) bulletemitter).setConvDistance(400F, -1.8F);
					else if (((RocketGun) bulletemitter).bulletMassa() > 10F) {
						if (aircraft instanceof IL_2) ((RocketGun) bulletemitter).setConvDistance(400F, -2F);
						else ((RocketGun) bulletemitter).setConvDistance(400F, -1.65F);
					} else if (aircraft instanceof IL_2) ((RocketGun) bulletemitter).setConvDistance(400F, -2.1F);
					else ((RocketGun) bulletemitter).setConvDistance(400F, -1.9F);
					break;

				case 3: // '\003'
					if ((bulletemitter instanceof BombGun) && World.getPlayerAircraft() == aircraft) {
						((BombGun) bulletemitter).setBombDelay(World.cur().userBombDelay);
						((BombGun) bulletemitter).selectFuzeAutomatically(true);
						if (bulletemitter instanceof TorpedoGun) {
							Class class1 = (Class) Property.value(bulletemitter.getClass(), "bulletClass", null);
							Torpedo.setInfo(class1);
						}
					}
					break;
				}
			} else {
				System.err.println("Hook '" + as[j] + "' NOT found in mesh of " + aircraft.getClass());
			}
		}

		aircraft.FM.AS.setArmingSeeds();
	}

	public void missionStarting() {
		super.missionStarting();
		// +++ Select Bomb mod
		if (FM.CT.bHasBombSelect) FM.CT.registerBombs();
		// --- Select Bomb mod
		if (this == World.getPlayerAircraft()) return;
		boolean flag = false;
		if (Mission.isSingle() || Mission.isCoop()) flag = isPlayersWing(this);
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
			if (abulletemitter == null) continue;
			for (int j = 0; j < abulletemitter.length; j++) {
				BulletEmitter bulletemitter = abulletemitter[j];
				if (!(bulletemitter instanceof BombGun)) continue;
				BombGun bombgun = (BombGun) bulletemitter;
				if (flag) {
					bombgun.setBombDelay(World.cur().userBombDelay);
					bombgun.selectFuzeAutomatically(false);
					if (Mission.isCoop()) {
						UserCfg usercfg = World.cur().userCfg;
						int k = usercfg.fuzeType;
						float f = usercfg.bombDelay;
						FM.AS.replicateFuzeStatesToNet(k, 1, f);
					}
				} else {
					bombgun.addGenericFuze();
				}
			}

		}

	}

	private static boolean isPlayersWing(Aircraft aircraft) {
		try {
			Wing wing = aircraft.getWing();
			if (wing == null) return false;
			for (int i = 0; i < wing.airc.length; i++) {
				Aircraft aircraft2 = wing.airc[i];
				if (aircraft2 == World.getPlayerAircraft()) return true;
				if (aircraft2.isNetPlayer() || aircraft2.isNetMaster()) return false;
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}

	private static ArrayList weaponsListProperty(Class class1) {
		Object obj = Property.value(class1, "weaponsList", null);
		if (obj != null) {
			return (ArrayList) obj;
		} else {
			ArrayList arraylist = new ArrayList();
			Property.set(class1, "weaponsList", arraylist);
			return (ArrayList) arraylist;
		}
	}

	// TODO: Edited by |ZUTI|: changed from private to public - called from ZutiTimer_ChangeLoadout
	public static HashMapInt weaponsMapProperty(Class class1) {
		Object obj = Property.value(class1, "weaponsMap", null);
		if (obj != null) {
			return (HashMapInt) obj;
		} else {
			HashMapInt hashmapint = new HashMapInt();
			Property.set(class1, "weaponsMap", hashmapint);
			return (HashMapInt) hashmapint;
		}
	}

	// TODO: Edited to include RocketBombGun
	public void hideWingWeapons(boolean bool) {
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			BulletEmitter[] bulletemitters = FM.CT.Weapons[i];
			if (bulletemitters != null) {
				for (int i_164_ = 0; i_164_ < bulletemitters.length; i_164_++) {
					if (bulletemitters[i_164_] instanceof BombGun) ((BombGun) bulletemitters[i_164_]).hide(bool);
					else if (bulletemitters[i_164_] instanceof RocketGun) ((RocketGun) bulletemitters[i_164_]).hide(bool);
					else if (bulletemitters[i_164_] instanceof RocketBombGun) ((RocketBombGun) bulletemitters[i_164_]).hide(bool);
					else if (bulletemitters[i_164_] instanceof Pylon) ((Pylon) bulletemitters[i_164_]).drawing(!bool);
				}
			}
		}
	}

	public void createCockpits() {
		if (!Config.isUSE_RENDER()) return;
		deleteCockpits();
		Object obj = Property.value(getClass(), "cockpitClass");
		if (obj == null) return;
		Cockpit._newAircraft = this;
		if (obj instanceof Class) {
			Class class1 = (Class) obj;
			try {
				Main3D.cur3D().cockpits = new Cockpit[1];
				Main3D.cur3D().cockpits[0] = (Cockpit) class1.newInstance();
				Main3D.cur3D().cockpitCur = Main3D.cur3D().cockpits[0];
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		} else {
			Class aclass[] = (Class[]) (Class[]) obj;
			try {
				Main3D.cur3D().cockpits = new Cockpit[aclass.length];
				for (int i = 0; i < aclass.length; i++)
					Main3D.cur3D().cockpits[i] = (Cockpit) aclass[i].newInstance();

				Main3D.cur3D().cockpitCur = Main3D.cur3D().cockpits[0];
			} catch (Exception exception1) {
				System.out.println(exception1.getMessage());
				exception1.printStackTrace();
			}
		}
		Cockpit._newAircraft = null;
	}

	protected void deleteCockpits() {
		if (!Config.isUSE_RENDER()) return;
		Cockpit acockpit[] = Main3D.cur3D().cockpits;
		if (acockpit == null) return;
		for (int i = 0; i < acockpit.length; i++) {
			acockpit[i].destroy();
			acockpit[i] = null;
		}

		Main3D.cur3D().cockpits = null;
		Main3D.cur3D().cockpitCur = null;
	}

	private void explode() {
		if (FM.Wingman != null) FM.Wingman.Leader = FM.Leader;
		if (FM.Leader != null) FM.Leader.Wingman = FM.Wingman;
		FM.Wingman = null;
		FM.Leader = null;
		HierMesh hiermesh = hierMesh();
		int l = -1;
		float f = 30F;
		for (int i = 9; i >= 0 && (l = hiermesh.chunkFindCheck("CF_D" + i)) < 0; i--)
			;
		int ai1[] = hideSubTrees("");
		if (ai1 == null) return;
		int ai[] = ai1;
		ai1 = new int[ai.length + 1];
		int j;
		for (j = 0; j < ai.length; j++)
			ai1[j] = ai[j];

		ai1[j] = l;
		for (int k = 0; k < ai1.length; k++) {
			Wreckage wreckage = new Wreckage(this, ai1[k]);
			if (World.Rnd().nextInt(0, 99) < 30) {
				float f1 = 2.5F + World.Rnd().nextFloat(0.0F, 5F);
				Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.FIRE, f1);
				if (World.Rnd().nextInt(0, 99) < 50) Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE_EXPLODE, f1 + 1.0F);
			}
			getSpeed(Vd);
			Vd.x += (double) f * (World.Rnd().nextDouble(0.0D, 1.0D) - 0.5D);
			Vd.y += (double) f * (World.Rnd().nextDouble(0.0D, 1.0D) - 0.5D);
			Vd.z += (double) f * (World.Rnd().nextDouble(0.0D, 1.0D) - 0.5D);
			wreckage.setSpeed(Vd);
		}

	}

	public int aircNumber() {
		Wing wing = (Wing) getOwner();
		if (wing == null) return -1;
		else return wing.aircReady();
	}

	public int aircIndex() {
		Wing wing = (Wing) getOwner();
		if (wing == null) return -1;
		else return wing.aircIndex(this);
	}

	public boolean isInPlayerWing() {
		if (!Actor.isValid(World.getPlayerAircraft())) return false;
		else return getWing() == World.getPlayerAircraft().getWing();
	}

	public boolean isInPlayerSquadron() {
		if (!Actor.isValid(World.getPlayerAircraft())) return false;
		else return getSquadron() == World.getPlayerAircraft().getSquadron();
	}

	public boolean isInPlayerRegiment() {
		return getRegiment() == World.getPlayerRegiment();
	}

	public boolean isChunkAnyDamageVisible(String s) {
		if (s.lastIndexOf("_") == -1) s = s + "_D";
		for (int i = 0; i < 4; i++)
			if (hierMesh().chunkFindCheck(s + i) != -1 && hierMesh().isChunkVisible(s + i)) return true;

		return false;
	}

	protected int chunkDamageVisible(String s) {
		if (s.lastIndexOf("_") == -1) s = s + "_D";
		for (int i = 0; i < 4; i++)
			if (hierMesh().chunkFindCheck(s + i) != -1 && hierMesh().isChunkVisible(s + i)) return i;

		return 0;
	}

	public Wing getWing() {
		return (Wing) getOwner();
	}

	public Squadron getSquadron() {
		Wing wing = getWing();
		if (wing == null) return null;
		else return wing.squadron();
	}

	public Regiment getRegiment() {
		Wing wing = getWing();
		if (wing == null) return null;
		else return wing.regiment();
	}

	public void hitDaSilk() {
		FM.AS.hitDaSilk();
		FM.setReadyToDie(true);
		if (FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) > 20D) Voice.speakBailOut(this);
	}

	protected void killPilot(Actor actor, int i) {
		FM.AS.hitPilot(actor, i, 100);
	}

	public void doWoundPilot(int i, float f) {
	}

	public void doMurderPilot(int i) {
	}

	public void doRemoveBodyFromPlane(int i) {
		doRemoveBodyChunkFromPlane("Pilot" + i);
		doRemoveBodyChunkFromPlane("Head" + i);
		doRemoveBodyChunkFromPlane("HMask" + i);
		doRemoveBodyChunkFromPlane("Pilot" + i + "a");
		doRemoveBodyChunkFromPlane("Head" + i + "a");
		doRemoveBodyChunkFromPlane("Pilot" + i + "FAK");
		doRemoveBodyChunkFromPlane("Head" + i + "FAK");
		doRemoveBodyChunkFromPlane("Pilot" + i + "FAL");
		doRemoveBodyChunkFromPlane("Head" + i + "FAL");
	}

	protected void doRemoveBodyChunkFromPlane(String s) {
		if (hierMesh().chunkFindCheck(s + "_D0") != -1) hierMesh().chunkVisible(s + "_D0", false);
		if (hierMesh().chunkFindCheck(s + "_D1") != -1) hierMesh().chunkVisible(s + "_D1", false);
	}

	public void doSetSootState(int i, int j) {
		for (int k = 0; k < 2; k++) {
			if (FM.AS.astateSootEffects[i][k] != null) Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
			FM.AS.astateSootEffects[i][k] = null;
		}

		switch (j) {
		case 1: // '\001'
			FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
			FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
			break;

		case 3: // '\003'
			FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
			// fall through

		case 2: // '\002'
			FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
			break;

		case 5: // '\005'
			FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 3F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
			// fall through

		case 4: // '\004'
			FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
			break;
		}
	}

	public void onAircraftLoaded() {
		if (FM instanceof Maneuver) {
			Maneuver maneuver = (Maneuver) FM;
			maneuver.takeIntoAccount[0] = 1.0F;
			maneuver.takeIntoAccount[1] = 1.0F;
			maneuver.takeIntoAccount[2] = 0.7F;
			if (this instanceof TypeFighter) {
				if (aircIndex() % 2 == 0) {
					maneuver.takeIntoAccount[3] = 0.0F;
					maneuver.takeIntoAccount[4] = 1.0F;
				} else {
					maneuver.takeIntoAccount[2] = 0.1F;
					maneuver.takeIntoAccount[3] = 1.0F;
					maneuver.takeIntoAccount[4] = 0.0F;
				}
				maneuver.takeIntoAccount[5] = 0.3F;
				maneuver.takeIntoAccount[6] = 0.3F;
				maneuver.takeIntoAccount[7] = 0.1F;
			} else if (this instanceof TypeStormovik) {
				if (aircIndex() != 0) maneuver.takeIntoAccount[2] = 0.5F;
				maneuver.takeIntoAccount[3] = 0.4F;
				maneuver.takeIntoAccount[4] = 0.2F;
				maneuver.takeIntoAccount[5] = 0.1F;
				maneuver.takeIntoAccount[6] = 0.1F;
				maneuver.takeIntoAccount[7] = 0.6F;
			} else {
				if (aircIndex() != 0) maneuver.takeIntoAccount[2] = 0.4F;
				maneuver.takeIntoAccount[3] = 0.2F;
				maneuver.takeIntoAccount[4] = 0.0F;
				maneuver.takeIntoAccount[5] = 0.0F;
				maneuver.takeIntoAccount[6] = 0.0F;
				maneuver.takeIntoAccount[7] = 1.0F;
			}
			for (int i = 0; i < 8; i++)
				maneuver.AccountCoeff[i] = 0.0F;

		}

		// +++ Import from 4.13.2m
		FM.CT.setBombModeDefaults();
		FM.CT.setWeaponFireModeDefaults();
		// --- Import from 4.13.2m
	}

	public static float cvt(float f, float f1, float f2, float f3, float f4) {
		f = Math.min(Math.max(f, f1), f2);
		return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
	}

	protected void debugprintln(String s) {
		if (World.cur().isDebugFM()) System.out.println("<" + name() + "> (" + typedName() + ") " + s);
	}

	public static void debugprintln(Actor actor, String s) {
		if (World.cur().isDebugFM()) {
			if (Actor.isValid(actor)) {
				System.out.print("<" + actor.name() + ">");
				if (actor instanceof Aircraft) System.out.print(" (" + ((Aircraft) actor).typedName() + ")");
			} else {
				System.out.print("<INVALIDACTOR>");
			}
			System.out.println(" " + s);
		}
	}

	public void debuggunnery(String s) {
		if (World.cur().isDebugFM()) System.out.println("<" + name() + "> (" + typedName() + ") *** BULLET *** : " + s);
	}

	protected float bailProbabilityOnCut(String s) {
		if (s.startsWith("Nose")) return 0.5F;
		if (s.startsWith("Wing")) return 0.99F;
		if (s.startsWith("Aroone")) return 0.05F;
		if (s.startsWith("Tail")) return 0.99F;
		if (s.startsWith("StabL") && !isChunkAnyDamageVisible("VatorR")) return 0.99F;
		if (s.startsWith("StabR") && !isChunkAnyDamageVisible("VatorL")) return 0.99F;
		if (s.startsWith("Stab")) return 0.33F;
		if (s.startsWith("VatorL") && !isChunkAnyDamageVisible("VatorR")) return 0.99F;
		if (s.startsWith("VatorR") && !isChunkAnyDamageVisible("VatorL")) return 0.99F;
		if (s.startsWith("Vator")) return 0.01F;
		if (s.startsWith("Keel")) return 0.5F;
		if (s.startsWith("Rudder")) return 0.05F;
		return !s.startsWith("Engine") ? -0F : 0.12F;
	}

	private void _setMesh(String s) {
		setMesh(s);
		CacheItem cacheitem = (CacheItem) meshCache.get(s);
		if (cacheitem == null) {
			cacheitem = new CacheItem();
			cacheitem.mesh = new HierMesh(s);
			prepareMeshCamouflage(s, cacheitem.mesh, null, null);
			cacheitem.bExistTextures = true;
			cacheitem.loaded = 1;
			meshCache.put(s, cacheitem);
		} else {
			cacheitem.loaded++;
			if (!cacheitem.bExistTextures) {
				cacheitem.mesh.destroy();
				cacheitem.mesh = new HierMesh(s);
				prepareMeshCamouflage(s, cacheitem.mesh, null, null);
				cacheitem.bExistTextures = true;
			}
		}
		airCache.put(this, cacheitem);
		checkMeshCache();
	}

	private void _removeMesh() {
		CacheItem cacheitem = (CacheItem) airCache.get(this);
		if (cacheitem == null) return;
		airCache.remove(this);
		cacheitem.loaded--;
		if (cacheitem.loaded == 0) cacheitem.time = Time.real();
		checkMeshCache();
	}

	public static void checkMeshCache() {
		if (!Config.isUSE_RENDER()) return;
		long l = Time.real();
		for (java.util.Map.Entry entry = meshCache.nextEntry(null); entry != null; entry = meshCache.nextEntry(entry)) {
			CacheItem cacheitem = (CacheItem) entry.getValue();
			if (cacheitem.loaded != 0 || !cacheitem.bExistTextures || l - cacheitem.time <= 0x2bf20L) continue;
			HierMesh hiermesh = cacheitem.mesh;
			int i = hiermesh.materials();
			Mat mat = Mat.New("3do/textures/clear.mat");
			for (int j = 0; j < i; j++)
				hiermesh.materialReplace(j, mat);

			cacheitem.bExistTextures = false;
		}

	}

	public static void resetGameClear() {
		meshCache.clear();
		airCache.clear();
	}

	public void setCockpitState(int i) {
		if (FM.isPlayers() && World.cur().diffCur.Vulnerability && Actor.isValid(Main3D.cur3D().cockpitCur)) Main3D.cur3D().cockpitCur.doReflectCockitState();
	}

	protected void resetYPRmodifier() {
		ypr[0] = ypr[1] = ypr[2] = xyz[0] = xyz[1] = xyz[2] = 0.0F;
	}

	public CellAirPlane getCellAirPlane() {
		CellAirPlane cellairplane = (CellAirPlane) Property.value(this, "CellAirPlane", (Object) null);
		if (cellairplane != null) return cellairplane;
		cellairplane = (CellAirPlane) Property.value(getClass(), "CellObject", (Object) null);
		if (cellairplane == null) {
			tmpLocCell.set(0.0D, 0.0D, FM.Gears.H, 0.0F, FM.Gears.Pitch, 0.0F);
			cellairplane = new CellAirPlane(new com.maddox.il2.ai.air.CellObject[1][1], hierMesh(), tmpLocCell, 1.0D);
			cellairplane.blurSiluet8x();
			cellairplane.clampCells();
			Property.set(getClass(), "CellObject", cellairplane);
		}
		cellairplane = (CellAirPlane) cellairplane.getClone();
		Property.set(this, "CellObject", cellairplane);
		return cellairplane;
	}

	public static CellAirPlane getCellAirPlane(Class class1) {
		CellAirPlane cellairplane = null;
		tmpLocCell.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		HierMesh hiermesh = new HierMesh(getPropertyMesh(class1, null));
		cellairplane = new CellAirPlane(new com.maddox.il2.ai.air.CellObject[1][1], hiermesh, tmpLocCell, 1.0D);
		cellairplane.blurSiluet8x();
		cellairplane.clampCells();
		return cellairplane;
	}

	public boolean dropExternalStores(boolean flag) {
		boolean flag1 = false;
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
			if (abulletemitter == null) continue;
			for (int j = 0; j < abulletemitter.length; j++) {
				BulletEmitter bulletemitter = abulletemitter[j];
				if (!(bulletemitter instanceof BombGun) || (bulletemitter instanceof FuelTankGun)) continue;
				((BombGun) bulletemitter).jettisonBomb();
				if (bulletemitter.countBullets() == 0) continue;
				if (bulletemitter.countBullets() == -1) bulletemitter.loadBullets(1);
				flag1 = true;
				bulletemitter.shots(99);
				if (bulletemitter.getHookName().startsWith("_BombSpawn")) FM.CT.BayDoorControl = 1.0F;
			}

		}

		if (!flag1) return dropWfrGr21();
		else return flag1;
	}

	private boolean dropWfrGr21() {
		if (!wfrGr21dropped) {
			Object aobj[] = pos.getBaseAttached();
			if (aobj != null) {
				for (int i = 0; i < aobj.length; i++) {
					if (aobj[i] instanceof PylonRO_WfrGr21) {
						PylonRO_WfrGr21 pylonro_wfrgr21 = (PylonRO_WfrGr21) aobj[i];
						pylonro_wfrgr21.drawing(false);
						pylonro_wfrgr21.visibilityAsBase(false);
						wfrGr21dropped = true;
						if (World.getPlayerAircraft() == this) World.cur().scoreCounter.playerDroppedExternalStores(2);
					}
					if (!(aobj[i] instanceof PylonRO_WfrGr21Dual)) continue;
					PylonRO_WfrGr21Dual pylonro_wfrgr21dual = (PylonRO_WfrGr21Dual) aobj[i];
					pylonro_wfrgr21dual.drawing(false);
					pylonro_wfrgr21dual.visibilityAsBase(false);
					wfrGr21dropped = true;
					if (World.getPlayerAircraft() == this) World.cur().scoreCounter.playerDroppedExternalStores(4);
				}

			}
			if (wfrGr21dropped) {
				for (int j = 0; j < FM.CT.Weapons.length; j++) {
					BulletEmitter abulletemitter[] = FM.CT.Weapons[j];
					if (abulletemitter == null) continue;
					for (int k = 0; k < abulletemitter.length; k++) {
						Object obj = abulletemitter[k];
						if (obj instanceof RocketGunWfrGr21) {
							RocketGunWfrGr21 rocketgunwfrgr21 = (RocketGunWfrGr21) obj;
							FM.CT.Weapons[j][k] = GunEmpty.get();
							rocketgunwfrgr21.setHookToRel(true);
							rocketgunwfrgr21.shots(0);
							rocketgunwfrgr21.hide(true);
							obj = GunEmpty.get();
							rocketgunwfrgr21.doDestroy();
						}
						if ((obj instanceof PylonRO_WfrGr21) || (obj instanceof PylonRO_WfrGr21Dual)) {
							((Pylon) obj).destroy();
							FM.CT.Weapons[j][k] = GunEmpty.get();
							obj = GunEmpty.get();
						}
					}

				}

				sfxHit(1.0F, new Point3d(0.0D, 0.0D, -1D));
				Vector3d vector3d = new Vector3d();
				vector3d.set(FM.Vwld);
				vector3d.z = vector3d.z - 6D;
				Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("WfrGr21L"));
				vector3d.x += Math.random() + 0.5D;
				vector3d.y += Math.random() + 0.5D;
				vector3d.z += Math.random() + 0.5D;
				wreckage.setSpeed(vector3d);
				wreckage.collide(true);
				Vector3d vector3d1 = new Vector3d();
				vector3d1.set(FM.Vwld);
				vector3d1.z = vector3d1.z - 7D;
				Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("WfrGr21R"));
				vector3d1.x += Math.random() + 0.5D;
				vector3d1.y += Math.random() + 0.5D;
				vector3d1.z += Math.random() + 0.5D;
				wreckage1.setSpeed(vector3d1);
				wreckage1.collide(true);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void blisterRemoved(int i) {
	}

	public static boolean hasPlaneZBReceiver(Aircraft aircraft) {
		for (int i = 0; i < planesWithZBReceiver.length; i++) {
			if (!planesWithZBReceiver[i].isInstance(aircraft)) continue;
			String s = aircraft.getRegiment().country();
			if (s.equals(PaintScheme.countryBritain) || s.equals(PaintScheme.countryUSA) || s.equals(PaintScheme.countryNewZealand)) return true;
		}

		return false;
	}

	public boolean isAircraftTaxing() {
		return FM.Gears.isUnderDeck() || FM.Gears.getWheelsOnGround() || FM.Gears.onGround();
	}

	public static boolean isPlayerTaxing() {
		Aircraft aircraft = World.getPlayerAircraft();
		return aircraft.isDestroyed() || aircraft.FM.Gears.isUnderDeck() || aircraft.FM.Gears.getWheelsOnGround() || aircraft.FM.Gears.onGround();
	}

	public void setHumanControlledTurretAngels(Turret turret, float af[], HierMesh hiermesh, ActorHMesh actorhmesh) {
		af[0] = af[1] = af[2] = 0.0F;
		af[1] = turret.tu[0];
		hiermesh.setCurChunk(turret.indexA);
		actorhmesh.hierMesh().chunkSetAngles(af);
		af[1] = turret.tu[1];
		hiermesh.setCurChunk(turret.indexB);
		actorhmesh.hierMesh().chunkSetAngles(af);
	}

	public void initPlayerTaxingWay() {
		showTaxingWay = false;
		Aircraft aircraft = World.getPlayerAircraft();
		playerTaxingWay = new ArrayList();
		if (aircraft.FM == null || aircraft.FM.AP == null) return;
		int i = 0;
		do {
			if (i >= aircraft.FM.AP.way.size()) break;
			WayPoint waypoint = aircraft.FM.AP.way.look_at_point(i);
			if ((waypoint == null || waypoint.waypointType != 4) && waypoint.waypointType != 5) break;
			World.cur();
			World.land();
			double d = Landscape.HQ_Air(waypoint.x(), waypoint.y()) + 0.1F;
			Point3d point3d = new Point3d(waypoint.x(), waypoint.y(), d);
			playerTaxingWay.add(point3d);
			i++;
		} while (true);
	}

	public void setBombScoreOwner(Aircraft aircraft) {
		bombScoreOwner = aircraft;
	}

	public Aircraft getBombScoreOwner() {
		return bombScoreOwner;
	}

	public Point3d getTankBurnLightPoint(int i, Hook hook) {
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		if (hook.chunkName().toLowerCase().startsWith("cf")) {
			hook.computePos(this, loc, loc1);
			Point3d point3d = loc1.getPoint();
			if (point3d.z > 0.0D) loc = new Loc(0.0D, 0.0D, 0.5D, 0.0F, 0.0F, 0.0F);
			else loc = new Loc(0.0D, 0.0D, -0.75D, 0.0F, 0.0F, 0.0F);
			loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		} else {
			loc = new Loc(-1D, 0.0D, 0.5D, 0.0F, 0.0F, 0.0F);
		}
		hook.computePos(this, loc, loc1);
		Point3d point3d1 = loc1.getPoint();
		return point3d1;
	}

	public Loc getEngineBurnLightLoc(int i) {
		return engineBurnLightLoc;
	}

	public float getWheelWidth(int i) {
		float f = cvt(FM.Wingspan, 10F, 40F, 0.12F, 0.42F);
		if (i == 2) f = f * 0.6F;
		return f;
	}

	public boolean needsOpenBombBay() {
		return true;
	}

	public boolean canOpenBombBay() {
		return true;
	}

	public float getEyeLevelCorrection() {
		return 0.0F;
	}

		// +++ Import from 4.13.2m
	public boolean hasIntervalometer() {
		if (!World.cur().diffCur.Limited_Ammo)
			return false;
		else
			return FM.actor instanceof TypeBomber;
	}

	public int[] getBombTrainDelayArray() {
		return Controls.bombTrainDelays;
	}

	public int getBombTrainMaxAmount() {
		return FM.CT.getWeaponCount(3);
	}
		// --- Import from 4.13.2m
}
