package com.maddox.il2.objects.vehicles.artillery;

import java.io.IOException;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.ObjState;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.TableFunction2;

public abstract class ArtilleryGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, ActorAlign, HunterInterface {
	private ArtilleryProperties prop = null;
	private boolean nearAirfield = false;
	private boolean dontShoot = false;
	private long time_lastCheckShoot = 0L;
	protected static final int DELAY_CHECK_SHOOT = 12000;
	protected static final int DIST_TO_FRIEND_PLANES = 4000;
	protected static final int DIST_TO_AIRFIELD = 2000;
	private float heightAboveLandSurface;
	private float heightAboveLandSurface2;
	private FireDevice[] arms;
	private long startDelay;
	public int dying = 0;
	static final int DYING_NONE = 0;
	static final int DYING_DEAD = 1;
	private short deathSeed;
	private long respawnDelay = 0L;
	private long hideTmr = 0L;
	private static long delay_hide_ticks = 0L;
	public float RADIUS_HIDE = 0.0F;
	public int Skill = 0;
	public float skillErrCoef = 1.0F;
	public static float new_RADIUS_HIDE = 0.0F;
	public static int new_SKILL = 1;
	public static boolean new_SPOTTER = false;
	private static ArtilleryProperties constr_arg1 = null;
	private static ActorSpawnArg constr_arg2 = null;
	private static Point3d p = new Point3d();
	private static Point3d p1 = new Point3d();
	private static Orient o = new Orient();
	private static Vector3f n = new Vector3f();
	private static Vector3d tmpv = new Vector3d();
	Aircraft spotterAc;
	public boolean isSpotterAcGuided;
	static final float[] err = { 1.17F, 1.37F, 1.67F, 2.13F };
	private NetMsgFiltered outCommand = new NetMsgFiltered();
	float spotterCorrection = 500.0F;
	// TODO: New parameters
	private long tmAAAScared;
	private long tmHL;
	private float syaw;
	boolean bHideGunners = false;

	public static class SPAWN implements ActorSpawn {
		public Class cls;
		public ArtilleryProperties proper;

		private static float getF(SectFile sectfile, String string, String string_0_, float f, float f_1_) {
			float f_2_ = sectfile.get(string, string_0_, -9865.345F);
			if (f_2_ == -9865.345F || f_2_ < f || f_2_ > f_1_) {
				if (f_2_ == -9865.345F) System.out.println("Artillery: Parameter [" + string + "]:<" + string_0_ + "> " + "not found");
				else System.out.println("Artillery: Value of [" + string + "]:<" + string_0_ + "> (" + f_2_ + ")" + " is out of range (" + f + ";" + f_1_ + ")");
				throw new RuntimeException("Can't set property");
			}
			return f_2_;
		}

		private static String getS(SectFile sectfile, String string, String string_3_) {
			String string_4_ = sectfile.get(string, string_3_);
			if (string_4_ == null || string_4_.length() <= 0) {
				System.out.print("Artillery: Parameter [" + string + "]:<" + string_3_ + "> ");
				System.out.println(string_4_ == null ? "not found" : "is empty");
				throw new RuntimeException("Can't set property");
			}
			return string_4_;
		}

		private static String getS(SectFile sectfile, String string, String string_5_, String string_6_) {
			String string_7_ = sectfile.get(string, string_5_);
			if (string_7_ == null || string_7_.length() <= 0) return string_6_;
			return string_7_;
		}

		private static ArtilleryProperties LoadArtilleryProperties(SectFile sectfile, String string, Class var_class) {
			ArtilleryProperties artilleryproperties = new ArtilleryProperties();
			String string_8_ = getS(sectfile, string, "PanzerType", null);
			if (string_8_ == null) string_8_ = "Tank";
			artilleryproperties.fnShotPanzer = TableFunctions.GetFunc2(string_8_ + "ShotPanzer");
			artilleryproperties.fnExplodePanzer = TableFunctions.GetFunc2(string_8_ + "ExplodePanzer");
			artilleryproperties.PANZER_TNT_TYPE = getF(sectfile, string, "PanzerSubtype", 0.0F, 100.0F);
			artilleryproperties.meshSummer = getS(sectfile, string, "MeshSummer");
			artilleryproperties.meshDesert = getS(sectfile, string, "MeshDesert", artilleryproperties.meshSummer);
			artilleryproperties.meshWinter = getS(sectfile, string, "MeshWinter", artilleryproperties.meshSummer);
			artilleryproperties.meshSummer1 = getS(sectfile, string, "MeshSummerDamage", null);
			artilleryproperties.meshDesert1 = getS(sectfile, string, "MeshDesertDamage", artilleryproperties.meshSummer1);
			artilleryproperties.meshWinter1 = getS(sectfile, string, "MeshWinterDamage", artilleryproperties.meshSummer1);
			int i = ((artilleryproperties.meshSummer1 == null ? 1 : 0) + (artilleryproperties.meshDesert1 == null ? 1 : 0) + (artilleryproperties.meshWinter1 == null ? 1 : 0));
			if (i != 0 && i != 3) {
				System.out.println("Artillery: Uncomplete set of damage meshes for '" + string + "'");
				throw new RuntimeException("Can't register artillery object");
			}
			artilleryproperties.PANZER_BODY_FRONT = getF(sectfile, string, "PanzerBodyFront", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerBodyBack", -9865.345F) == -9865.345F) {
				artilleryproperties.PANZER_BODY_BACK = artilleryproperties.PANZER_BODY_FRONT;
				artilleryproperties.PANZER_BODY_SIDE = artilleryproperties.PANZER_BODY_FRONT;
				artilleryproperties.PANZER_BODY_TOP = artilleryproperties.PANZER_BODY_FRONT;
			} else {
				artilleryproperties.PANZER_BODY_BACK = getF(sectfile, string, "PanzerBodyBack", 0.0010F, 9.999F);
				artilleryproperties.PANZER_BODY_SIDE = getF(sectfile, string, "PanzerBodySide", 0.0010F, 9.999F);
				artilleryproperties.PANZER_BODY_TOP = getF(sectfile, string, "PanzerBodyTop", 0.0010F, 9.999F);
			}
			if (sectfile.get(string, "PanzerHead", -9865.345F) == -9865.345F) artilleryproperties.PANZER_HEAD = artilleryproperties.PANZER_BODY_FRONT;
			else artilleryproperties.PANZER_HEAD = getF(sectfile, string, "PanzerHead", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerHeadTop", -9865.345F) == -9865.345F) artilleryproperties.PANZER_HEAD_TOP = artilleryproperties.PANZER_BODY_TOP;
			else artilleryproperties.PANZER_HEAD_TOP = getF(sectfile, string, "PanzerHeadTop", 0.0010F, 9.999F);
			float f = Math.min(Math.min(artilleryproperties.PANZER_BODY_BACK, artilleryproperties.PANZER_BODY_TOP), Math.min(artilleryproperties.PANZER_BODY_SIDE, artilleryproperties.PANZER_HEAD_TOP));
			artilleryproperties.HITBY_MASK = f > 0.015F ? -2 : -1;
			artilleryproperties.explodeName = getS(sectfile, string, "Explode", "Artillery");
			int i_9_ = 0;
			for (;;) {
				if (i_9_ != 0) {
					String string_10_ = sectfile.get(string, "Gun_" + i_9_);
					if (string_10_ == null || string_10_.length() <= 0) break;
				}
				i_9_++;
			}
			artilleryproperties.gunProperties = new GunProps[i_9_];
			for (int i_11_ = 0; i_11_ < i_9_; i_11_++) {
				artilleryproperties.gunProperties[i_11_] = new GunProps();
				String string_12_ = "Gun";
				String string_13_ = "";
				if (i_11_ != 0) {
					string_13_ = "_" + i_11_;
					string_12_ += string_13_;
				}
				String string_14_ = ("com.maddox.il2.objects.weapons." + getS(sectfile, string, string_12_));
				try {
					artilleryproperties.gunProperties[i_11_].gunClass = Class.forName(string_14_);
				} catch (Exception exception) {
					System.out.println("Artillery: Can't find gun class '" + string_14_ + "'");
					throw new RuntimeException("Can't register artillery object");
				}
				artilleryproperties.gunProperties[i_11_].WEAPONS_MASK = (Gun.getProperties(artilleryproperties.gunProperties[i_11_].gunClass).weaponType);
				if (artilleryproperties.gunProperties[i_11_].WEAPONS_MASK == 0) {
					System.out.println("Artillery: Undefined weapon type in gun class '" + string_14_ + "'");
					throw new RuntimeException("Can't register artillery object");
				}
				artilleryproperties.gunProperties[i_11_].ATTACK_FAST_TARGETS = true;
				float f_15_ = sectfile.get(string, "FireFastTargets" + string_13_, -9865.345F);
				if (f_15_ != -9865.345F) artilleryproperties.gunProperties[i_11_].ATTACK_FAST_TARGETS = f_15_ > 0.5F;
				else if (string_8_.equals("Tank")) artilleryproperties.gunProperties[i_11_].ATTACK_FAST_TARGETS = false;
				artilleryproperties.gunProperties[i_11_].ATTACK_MAX_DISTANCE = getF(sectfile, string, "AttackMaxDistance" + string_13_, 6.0F, 12000.0F);
				artilleryproperties.gunProperties[i_11_].ATTACK_MAX_RADIUS = getF(sectfile, string, "AttackMaxRadius" + string_13_, 6.0F, 12000.0F);
				artilleryproperties.gunProperties[i_11_].ATTACK_MAX_HEIGHT = getF(sectfile, string, "AttackMaxHeight" + string_13_, 6.0F, 12000.0F);
				int i_16_ = sectfile.sectionIndex(string);
				if (sectfile.varExist(i_16_, "HeadYawHalfRange" + string_13_)) {
					float f_17_ = getF(sectfile, string, "HeadYawHalfRange" + string_13_, 0.0F, 180.0F);
					artilleryproperties.gunProperties[i_11_].HEAD_YAW_RANGE = new AnglesRange(-1.0F, 1.0F);
					artilleryproperties.gunProperties[i_11_].HEAD_YAW_RANGE.set(-f_17_, f_17_);
					artilleryproperties.gunProperties[i_11_].HEAD_STD_YAW = 0.0F;
				} else {
					float f_18_ = getF(sectfile, string, "HeadMinYaw" + string_13_, -180.0F, 180.0F);
					float f_19_ = getF(sectfile, string, "HeadStdYaw" + string_13_, -180.0F, 180.0F);
					float f_20_ = getF(sectfile, string, "HeadMaxYaw" + string_13_, -180.0F, 180.0F);
					artilleryproperties.gunProperties[i_11_].HEAD_YAW_RANGE = new AnglesRange(-1.0F, 1.0F);
					artilleryproperties.gunProperties[i_11_].HEAD_YAW_RANGE.set(f_18_, f_20_);
					artilleryproperties.gunProperties[i_11_].HEAD_STD_YAW = f_19_;
				}
				artilleryproperties.gunProperties[i_11_].GUN_MIN_PITCH = getF(sectfile, string, "GunMinPitch" + string_13_, -15.0F, 85.0F);
				artilleryproperties.gunProperties[i_11_].GUN_STD_PITCH = getF(sectfile, string, "GunStdPitch" + string_13_, -15.0F, 89.9F);
				artilleryproperties.gunProperties[i_11_].GUN_MAX_PITCH = getF(sectfile, string, "GunMaxPitch" + string_13_, 0.0F, 89.9F);
				artilleryproperties.gunProperties[i_11_].HEAD_MAX_YAW_SPEED = getF(sectfile, string, "HeadMaxYawSpeed" + string_13_, 0.1F, 999.0F);
				artilleryproperties.gunProperties[i_11_].GUN_MAX_PITCH_SPEED = getF(sectfile, string, "GunMaxPitchSpeed" + string_13_, 0.1F, 999.0F);
				artilleryproperties.gunProperties[i_11_].DELAY_AFTER_SHOOT = getF(sectfile, string, "DelayAfterShoot" + string_13_, 0.0F, 999.0F);
				artilleryproperties.gunProperties[i_11_].CHAINFIRE_TIME = getF(sectfile, string, "ChainfireTime" + string_13_, 0.0F, 600.0F);
				float f_21_ = sectfile.get(string, "FastTargetsAngleError", -9865.345F);
				if (f_21_ <= 0.0F) f_21_ = 0.0F;
				else if (f_21_ >= 45.0F) f_21_ = 45.0F;
				artilleryproperties.gunProperties[i_11_].FAST_TARGETS_ANGLE_ERROR = f_21_;
			}
			Property.set(var_class, "iconName", "icons/" + getS(sectfile, string, "Icon") + ".mat");
			Property.set(var_class, "meshName", artilleryproperties.meshSummer);
			return artilleryproperties;
		}

		public SPAWN(Class var_class) {
			try {
				String string = var_class.getName();
				int i = string.lastIndexOf('.');
				int i_22_ = string.lastIndexOf('$');
				if (i < i_22_) i = i_22_;
				String string_23_ = string.substring(i + 1);
				proper = LoadArtilleryProperties(Statics.getTechnicsFile(), string_23_, var_class);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("Problem in spawn: " + var_class.getName());
			}
			cls = var_class;
			Spawn.add(cls, this);
		}

		public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
			switch (World.cur().camouflage) {
			case 1:
				proper.meshName = proper.meshWinter;
				proper.meshName2 = proper.meshWinter1;
				break;
			case 2:
				proper.meshName = proper.meshDesert;
				proper.meshName2 = proper.meshDesert1;
				break;
			default:
				proper.meshName = proper.meshSummer;
				proper.meshName2 = proper.meshSummer1;
			}
			ArtilleryGeneric artillerygeneric;
			try {
				ArtilleryGeneric.constr_arg1 = proper;
				ArtilleryGeneric.constr_arg2 = actorspawnarg;
				artillerygeneric = (ArtilleryGeneric) cls.newInstance();
				ArtilleryGeneric.constr_arg1 = null;
				ArtilleryGeneric.constr_arg2 = null;
			} catch (Exception exception) {
				ArtilleryGeneric.constr_arg1 = null;
				ArtilleryGeneric.constr_arg2 = null;
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("SPAWN: Can't create Artillery object [class:" + cls.getName() + "]");
				return null;
			}
			return artillerygeneric;
		}
	}

	class Mirror extends ActorNet {
		NetMsgFiltered out = new NetMsgFiltered();

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			if (netmsginput.isGuaranted()) {
				switch (netmsginput.readByte()) {
				case 73: {
					if (isMirrored()) {
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
						post(netmsgguaranted);
					}
					short i = netmsginput.readShort();
					float f = netmsginput.readFloat();
					float f_24_ = netmsginput.readFloat();
					if (i <= 0) {
						if (dying != 1) {
							arms[0].aim.forgetAiming();
							ArtilleryGeneric.this.setGunAngles(0, f, f_24_);
						}
					} else if (dying != 1) {
						ArtilleryGeneric.this.setGunAngles(0, f, f_24_);
						ArtilleryGeneric.this.Die(null, i, false);
					}
					if (netmsginput.available() > 0) {
						for (int i_25_ = 1; i_25_ < arms.length; i_25_++) {
							f = netmsginput.readFloat();
							f_24_ = netmsginput.readFloat();
							if (i <= 0) {
								if (dying != 1) {
									arms[i_25_].aim.forgetAiming();
									ArtilleryGeneric.this.setGunAngles(i_25_, f, f_24_);
								}
							} else if (dying != 1) ArtilleryGeneric.this.setGunAngles(i_25_, f, f_24_);
						}
					}
					return true;
				}
				case 82:
					if (isMirrored()) {
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
						post(netmsgguaranted);
					}
					dying = 0;
					ArtilleryGeneric.this.setDiedFlag(false);
					for (int i = 0; i < arms.length; i++)
						arms[i].aim.forgetAiming();
					setMesh(prop.meshName);
					ArtilleryGeneric.this.setDefaultLivePose();
					return true;
				case 68: {
					if (isMirrored()) {
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
						post(netmsgguaranted);
					}
					short i = netmsginput.readShort();
					float f = netmsginput.readFloat();
					float f_26_ = netmsginput.readFloat();
					int i_27_ = 0;
					ArtilleryGeneric.this.setGunAngles(i_27_, f, f_26_);
					if (i > 0 && dying != 1) {
						com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
						Actor actor = (netobj == null ? null : ((ActorNet) netobj).actor());
						while (netmsginput.available() > 0) {
							i_27_++;
							f = netmsginput.readFloat();
							f_26_ = netmsginput.readFloat();
							ArtilleryGeneric.this.setGunAngles(i_27_, f, f_26_);
						}
						ArtilleryGeneric.this.Die(actor, i, true);
					}
					return true;
				}
				default:
					return false;
				}
			}
			switch (netmsginput.readByte()) {
			case 84: {
				if (isMirrored()) {
					out.unLockAndSet(netmsginput, 1);
					out.setIncludeTime(false);
					postReal(Message.currentRealTime(), out);
				}
				com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
				Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
				int i = netmsginput.readUnsignedByte();
				int i_28_ = 0;
				if (netmsginput.available() > 0) i_28_ = netmsginput.readInt();
				ArtilleryGeneric.this.Track_Mirror(i_28_, actor, i);
				break;
			}
			case 70: {
				if (isMirrored()) {
					out.unLockAndSet(netmsginput, 1);
					out.setIncludeTime(true);
					postReal(Message.currentRealTime(), out);
				}
				com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
				Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
				float f = netmsginput.readFloat();
				float f_29_ = (0.0010F * (float) (Message.currentGameTime() - Time.current()) + f);
				int i = netmsginput.readUnsignedByte();
				int i_30_ = 0;
				if (netmsginput.available() > 0) i_30_ = netmsginput.readInt();
				ArtilleryGeneric.this.Fire_Mirror(i_30_, actor, i, f_29_);
				break;
			}
			case 68:
				out.unLockAndSet(netmsginput, 1);
				out.setIncludeTime(false);
				postRealTo(Message.currentRealTime(), masterChannel(), out);
				return true;
			}
			return true;
		}

		public Mirror(Actor actor, NetChannel netchannel, int i) {
			super(actor, netchannel, i);
		}
	}

	class Master extends ActorNet {
		public Master(Actor actor) {
			super(actor);
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			if (netmsginput.isGuaranted()) return true;
			if (netmsginput.readByte() != 68) return false;
			if (dying == 1) return true;
			com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
			Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
			ArtilleryGeneric.this.Die(actor, (short) 0, true);
			return true;
		}
	}

	class Move extends Interpolate {
		public boolean tick() {
			if (dying == 1) {
//				if (access$610(ArtilleryGeneric.this) <= 0L) {
				if (respawnDelay-- <= 0L) {
					if (!Mission.isDeathmatch()) {
						for (int i = 0; i < arms.length; i++) {
							if (arms[i].aim != null) {
								arms[i].aim.forgetAll();
								arms[i].aim = null;
							}
							if (arms[i].gun != null) {
								ObjState.destroy(arms[i].gun);
								arms[i].gun = null;
								arms[i] = null;
							}
						}
						arms = null;
						return false;
					}
					if (!ArtilleryGeneric.this.isNetMaster()) {
						respawnDelay = 10000L;
						return true;
					}
					dying = 0;
					hideTmr = 0L;
					if (!ArtilleryGeneric.this.isNetMirror() && RADIUS_HIDE > 0.0F) hideTmr = -1L;
					ArtilleryGeneric.this.setDiedFlag(false);
					for (int i = 0; i < arms.length; i++)
						arms[i].aim.forgetAiming();
					setMesh(prop.meshName);
					ArtilleryGeneric.this.setDefaultLivePose();
					ArtilleryGeneric.this.send_RespawnCommand();
					dontShoot = false;
					time_lastCheckShoot = Time.current() - 12000L;
					return true;
				}
				return true;
			}
			for (int i = 0; i < arms.length; i++)
				arms[i].aim.tick_();
			if (RADIUS_HIDE > 0.0F && hideTmr >= 0L && !ArtilleryGeneric.this.isNetMirror()) {
				for (int i = 0; i < arms.length; i++) {
					if (arms[i].aim.getEnemy() != null) hideTmr = 0L;
//					else if (access$804(ArtilleryGeneric.this) > ArtilleryGeneric.delay_hide_ticks) hideTmr = -1L;
					else if (++hideTmr > ArtilleryGeneric.delay_hide_ticks) hideTmr = -1L;
				}
			}
			return true;
		}
	}

	private class FireDevice {
		private Gun gun;
		private Aim aim;
		private float headYaw;
		private float gunPitch;
		private Point3d fireOffset;
		private Orient fireOrient;

		public FireDevice() {
		}
	}

	public static class GunProps {
		public int WEAPONS_MASK;
		public float ATTACK_MAX_DISTANCE;
		public float ATTACK_MAX_RADIUS;
		public float ATTACK_MAX_HEIGHT;
		public boolean ATTACK_FAST_TARGETS;
		public float FAST_TARGETS_ANGLE_ERROR;
		public AnglesRange HEAD_YAW_RANGE;
		public float HEAD_STD_YAW;
		public float GUN_MIN_PITCH;
		public float GUN_STD_PITCH;
		public float GUN_MAX_PITCH;
		public float HEAD_MAX_YAW_SPEED;
		public float GUN_MAX_PITCH_SPEED;
		public float DELAY_AFTER_SHOOT;
		public float CHAINFIRE_TIME;
		public Class gunClass;
	}

	public static class ArtilleryProperties {
		public String meshName = null;
		public String meshName2 = null;
		public String meshSummer = null;
		public String meshDesert = null;
		public String meshWinter = null;
		public String meshSummer1 = null;
		public String meshDesert1 = null;
		public String meshWinter1 = null;
		public TableFunction2 fnShotPanzer = null;
		public TableFunction2 fnExplodePanzer = null;
		public float PANZER_BODY_FRONT = 0.0010F;
		public float PANZER_BODY_BACK = 0.0010F;
		public float PANZER_BODY_SIDE = 0.0010F;
		public float PANZER_BODY_TOP = 0.0010F;
		public float PANZER_HEAD = 0.0010F;
		public float PANZER_HEAD_TOP = 0.0010F;
		public float PANZER_TNT_TYPE = 1.0F;
		public String explodeName = null;
		public int HITBY_MASK = -2;
		public GunProps[] gunProperties;
		boolean bHideGunners = false;

	}

	public static final double Rnd(double d, double d_31_) {
		return World.Rnd().nextDouble(d, d_31_);
	}

	public static final float Rnd(float f, float f_32_) {
		return World.Rnd().nextFloat(f, f_32_);
	}

	private boolean RndB(float f) {
		return World.Rnd().nextFloat(0.0F, 1.0F) < f;
	}

	private static final long SecsToTicks(float f) {
		long l = (long) (0.5 + (double) (f / Time.tickLenFs()));
		return l < 1L ? 1L : l;
	}

	private boolean friendPlanesAreNear(Aircraft aircraft) {
		time_lastCheckShoot = Time.current() - (long) Rnd(0.0F, 1200.0F);
		dontShoot = false;
		Point3d point3d = aircraft.pos.getAbsPoint();
		double d = 1.6E7;
		if (!(aircraft.FM instanceof Maneuver)) return false;
		AirGroup airgroup = ((Maneuver) aircraft.FM).Group;
		if (airgroup == null) return false;
		int i = AirGroupList.length(airgroup.enemies[0]);
		for (int i_33_ = 0; i_33_ < i; i_33_++) {
			AirGroup airgroup_34_ = AirGroupList.getGroup(airgroup.enemies[0], i_33_);
			if (airgroup_34_.nOfAirc > 0) {
				double d_35_ = airgroup_34_.Pos.x - point3d.x;
				double d_36_ = airgroup_34_.Pos.y - point3d.y;
				double d_37_ = airgroup_34_.Pos.z - point3d.z;
				if (d_35_ * d_35_ + d_36_ * d_36_ + d_37_ * d_37_ <= d) {
					d_37_ = point3d.z - Engine.land().HQ(point3d.x, point3d.y);
					if (d_37_ > 50.0) {
						dontShoot = true;
						break;
					}
				}
			}
		}
		return dontShoot;
	}

	protected final boolean Head360(GunProps gunprops) {
		return gunprops.HEAD_YAW_RANGE.fullcircle();
	}

	public void msgCollisionRequest(Actor actor, boolean[] bools) {
		if (actor instanceof ActorMesh && ((ActorMesh) actor).isStaticPos()) bools[0] = false;
	}

	public void msgShot(Shot shot) {
		shot.bodyMaterial = 2;
		if (dying == 0 && !(shot.power <= 0.0F) && (!isNetMirror() || !shot.isMirage())) {
			if (shot.powerType == 1) {
				if (!RndB(0.15F)) Die(shot.initiator, (short) 0, true);
			} else {
				float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP);
				f *= Rnd(0.93F, 1.07F);
				float f_38_ = prop.fnShotPanzer.Value(shot.power, f);
				if (f_38_ < 1000.0F && (f_38_ <= 1.0F || RndB(1.0F / f_38_))) Die(shot.initiator, (short) 0, true);
			}
		}
	}

	public void msgExplosion(Explosion explosion) {
		if (dying == 0 && (!isNetMirror() || !explosion.isMirage()) && !(explosion.power <= 0.0F)) {
			if (explosion.powerType == 1) {
				if (TankGeneric.splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD,
						prop.PANZER_HEAD_TOP)) Die(explosion.initiator, (short) 0, true);
			} else if (explosion.powerType == 2 && explosion.chunkName != null) Die(explosion.initiator, (short) 0, true);
			else {
				float f;
				if (explosion.chunkName != null) f = 0.5F * explosion.power;
				else f = explosion.receivedTNTpower(this);
				f *= Rnd(0.95F, 1.05F);
				float f_39_ = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
				if (f_39_ < 1000.0F && (f_39_ <= 1.0F || RndB(1.0F / f_39_))) Die(explosion.initiator, (short) 0, true);
			}
		}
	}

	private void ShowExplode(float f) {
		if (f > 0.0F) f = Rnd(f, f * 1.6F);
		Explosions.runByName(prop.explodeName, this, "SmokeHead", "", f);
	}

	private float[] computeDeathPose(short i, int i_40_) {
		RangeRandom rangerandom = new RangeRandom((long) i);
		float[] fs = new float[10];
		fs[0] = arms[i_40_].headYaw + rangerandom.nextFloat(-15.0F, 15.0F);
		fs[1] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 9.0F));
		fs[2] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 9.0F));
		fs[3] = -arms[i_40_].gunPitch + rangerandom.nextFloat(-15.0F, 15.0F);
		fs[4] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(2.0F, 5.0F));
		fs[5] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(5.0F, 9.0F));
		fs[6] = 0.0F;
		fs[7] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 8.0F));
		fs[8] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(7.0F, 12.0F));
		fs[9] = -rangerandom.nextFloat(0.0F, 0.25F);
		return fs;
	}

	private void Die(Actor actor, short i, boolean bool) {
		if (dying == 0) {
			if (i <= 0) {
				if (isNetMirror()) {
					send_DeathRequest(actor);
					return;
				}
				i = (short) (int) Rnd(1.0F, 30000.0F);
			}
			deathSeed = i;
			dying = 1;
			World.onActorDied(this, actor);
			for (int i_41_ = 0; i_41_ < arms.length; i_41_++) {
				if (arms[i_41_].aim != null) arms[i_41_].aim.forgetAiming();
			}
			float f = 0.0F;
			for (int i_42_ = 0; i_42_ < arms.length; i_42_++) {
				float[] fs = computeDeathPose(i, i_42_);
				String string = "";
				if (i_42_ != 0) string = "_" + i_42_;
				hierMesh().chunkSetAngles("Head" + string, fs[0], fs[1], fs[2]);
				hierMesh().chunkSetAngles("Gun" + string, fs[3], fs[4], fs[5]);
				hierMesh().chunkSetAngles("Body", fs[6], fs[7], fs[8]);
				f = fs[9];
			}
			if (prop.meshName2 == null) {
				mesh().makeAllMaterialsDarker(0.22F, 0.35F);
				heightAboveLandSurface2 = heightAboveLandSurface;
				heightAboveLandSurface = heightAboveLandSurface2 + f;
			} else {
				setMesh(prop.meshName2);
				heightAboveLandSurface2 = 0.0F;
				int i_43_ = mesh().hookFind("Ground_Level");
				if (i_43_ != -1) {
					Matrix4d matrix4d = new Matrix4d();
					mesh().hookMatrix(i_43_, matrix4d);
					heightAboveLandSurface2 = (float) -matrix4d.m23;
				}
				heightAboveLandSurface = heightAboveLandSurface2;
			}
			Align();
			if (bool) ShowExplode(15.0F);
			if (bool) send_DeathCommand(actor);
		}
	}

	private void setGunAngles(int i, float f, float f_44_) {
		arms[i].headYaw = f;
		arms[i].gunPitch = f_44_;
		String string = "";
		if (i != 0) string = "_" + i;
		hierMesh().chunkSetAngles("Head" + string, arms[i].headYaw, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Gun" + string, -arms[i].gunPitch, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
		pos.inValidate(false);
	}

	public void destroy() {
		if (!isDestroyed()) {
			if (arms != null) {
				for (int i = 0; i < arms.length; i++) {
					if (arms[i].aim != null) {
						arms[i].aim.forgetAll();
						arms[i].aim = null;
					}
					if (arms[i].gun != null) {
						destroy(arms[i].gun);
						arms[i].gun = null;
					}
					arms[i] = null;
				}
			}
			arms = null;
			super.destroy();
		}
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

	public boolean isStaticPos() {
		return true;
	}

	private void setDefaultLivePose() {
		heightAboveLandSurface = 0.0F;
		int i = hierMesh().hookFind("Ground_Level");
		if (i != -1) {
			Matrix4d matrix4d = new Matrix4d();
			hierMesh().hookMatrix(i, matrix4d);
			heightAboveLandSurface = (float) -matrix4d.m23;
		}
		for (int i_45_ = 0; i_45_ < arms.length; i_45_++)
			setGunAngles(i_45_, prop.gunProperties[i_45_].HEAD_STD_YAW, prop.gunProperties[i_45_].GUN_STD_PITCH);
		Align();
	}

	protected ArtilleryGeneric() {
		this(constr_arg1, constr_arg2);
	}

	public void setMesh(String string) {
		super.setMesh(string);
		if (!Config.cur.b3dgunners || false != bHideGunners) this.mesh().materialReplaceToNull("Pilot1");
	}

	private ArtilleryGeneric(ArtilleryProperties artilleryproperties, ActorSpawnArg actorspawnarg) {
		super(artilleryproperties.meshName);
		prop = artilleryproperties;
		delay_hide_ticks = SecsToTicks(240.0F);
		actorspawnarg.setStationary(this);
		collide(true);
		drawing(true);
		createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
		startDelay = 0L;
		if (actorspawnarg.timeLenExist) {
			startDelay = (long) (actorspawnarg.timeLen * 60.0F * 1000.0F + 0.5F);
			if (startDelay < 0L) startDelay = 0L;
		}
		RADIUS_HIDE = new_RADIUS_HIDE;
		isSpotterAcGuided = new_SPOTTER;
		Skill = 4 - new_SKILL;
		skillErrCoef = (float) (Skill * Skill) * 0.211F;
		spotterCorrection = 111.25F * skillErrCoef;
		hideTmr = 0L;
		arms = new FireDevice[prop.gunProperties.length];
		for (int i = 0; i < arms.length; i++) {
			arms[i] = new FireDevice();
			arms[i].gun = null;
			try {
				arms[i].gun = (Gun) prop.gunProperties[i].gunClass.newInstance();
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("Artillery: Can't create gun '" + prop.gunProperties[i].gunClass.getName() + "'");
			}
			String string = "";
			if (i != 0) string = "_" + i;
			arms[i].gun.set(this, "Gun" + string);
			arms[i].gun.loadBullets(-1);
			arms[i].headYaw = prop.gunProperties[i].HEAD_STD_YAW;
			arms[i].gunPitch = prop.gunProperties[i].GUN_STD_PITCH;
			int i_46_ = hierMesh().chunkFind("Head" + string);
			hierMesh().setCurChunk(i_46_);
			Loc loc = new Loc();
			hierMesh().getChunkLocObj(loc);
			arms[i].fireOffset = new Point3d();
			loc.get(arms[i].fireOffset);
			arms[i].fireOrient = new Orient();
			loc.get(arms[i].fireOrient);
			arms[i].gun.qualityModifier = err[Skill - 1];
			if (isSpotterAcGuided) arms[i].gun.spotterModifier = arms[i].gun.prop.aimMaxDist;
		}
		if (!isNetMirror() && RADIUS_HIDE > 0.0F) hideTmr = -1L;
		setDefaultLivePose();
		startMove();
		Point3d point3d = pos.getAbsPoint();
		Airport airport = Airport.nearest(point3d, -1, 7);
		if (airport != null) {
			float f = (float) airport.pos.getAbsPoint().distance(point3d);
			nearAirfield = f <= 2000.0F;
		} else nearAirfield = false;
		dontShoot = false;
		time_lastCheckShoot = Time.current() - (long) Rnd(0.0F, 12000.0F);
	}

	private void Align() {
		pos.getAbs(p);
		p.z = Engine.land().HQ(p.x, p.y) + (double) heightAboveLandSurface;
		o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
		Engine.land().N(p.x, p.y, n);
		o.orient(n);
		pos.setAbs(p, o);
		syaw = o.getYaw();
	}

	public void align() {
		Align();
	}

	public void startMove() {
		if (!interpEnd("move")) {
			for (int i = 0; i < arms.length; i++) {
				if (arms[i].aim != null) {
					arms[i].aim.forgetAll();
					arms[i].aim = null;
				}
				arms[i].aim = new Aim(this, isNetMirror());
			}
			interpPut(new Move(), "move", Time.current(), null);
		}
	}

	public int WeaponsMask() {
		int i = 0;
		for (int i_47_ = 0; i_47_ < arms.length; i_47_++)
			i |= prop.gunProperties[i_47_].WEAPONS_MASK;
		return i;
	}

	public int HitbyMask() {
		return prop.HITBY_MASK;
	}

	public int chooseBulletType(BulletProperties[] bulletpropertieses) {
		if (dying != 0) return -1;
		if (bulletpropertieses.length == 1) return 0;
		if (bulletpropertieses.length <= 0) return -1;
		if (this instanceof TgtTank) {
			if (bulletpropertieses[0].cumulativePower > 0.0F) return 0;
			if (bulletpropertieses[1].cumulativePower > 0.0F) return 1;
			if (bulletpropertieses[0].power <= 0.0F) return 0;
			if (bulletpropertieses[1].power <= 0.0F) return 1;
		} else {
			if (bulletpropertieses[0].power <= 0.0F) return 0;
			if (bulletpropertieses[1].power <= 0.0F) return 1;
			if (bulletpropertieses[0].cumulativePower > 0.0F) return 0;
			if (bulletpropertieses[1].cumulativePower > 0.0F) return 1;
		}
		if (bulletpropertieses[0].powerType == 1) return 0;
		if (bulletpropertieses[1].powerType == 1) return 1;
		if (bulletpropertieses[0].powerType == 0) return 1;
		return 0;
	}

	public int chooseShotpoint(BulletProperties bulletproperties) {
		if (dying != 0) return -1;
		return 0;
	}

	public boolean getShotpointOffset(int i, Point3d point3d) {
		if (dying != 0) return false;
		if (i != 0) return false;
		if (point3d != null) point3d.set(0.0, 0.0, 0.0);
		return true;
	}

	public float AttackMaxDistance() {
		float f = 0.0F;
		for (int i = 0; i < arms.length; i++) {
			if (prop.gunProperties[i].ATTACK_MAX_DISTANCE > f) f = prop.gunProperties[i].ATTACK_MAX_DISTANCE;
		}
		return f;
	}

	public float AttackMaxDistance(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (arms[i].aim.equals(aim)) return prop.gunProperties[i].ATTACK_MAX_DISTANCE;
		}
		return -1.0F;
	}

	public float AttackMaxRadius(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (arms[i].aim.equals(aim)) return prop.gunProperties[i].ATTACK_MAX_RADIUS;
		}
		return 0.0F;
	}

	public float AttackMaxHeight(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (arms[i].aim.equals(aim)) return prop.gunProperties[i].ATTACK_MAX_HEIGHT;
		}
		return 0.0F;
	}

	public boolean unmovableInFuture() {
		return true;
	}

	public void collisionDeath() {
		if (!isNet()) {
			ShowExplode(-1.0F);
			destroy();
		}
	}

	public float futurePosition(float f, Point3d point3d) {
		pos.getAbs(point3d);
		return f <= 0.0F ? 0.0F : f;
	}

	private void send_DeathCommand(Actor actor) {
		if (isNetMaster()) {
			if (Mission.isDeathmatch()) {
				float f = Mission.respawnTime("Artillery");
				respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
			} else respawnDelay = 0L;
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try {
				netmsgguaranted.writeByte(68);
				netmsgguaranted.writeShort(deathSeed);
				netmsgguaranted.writeFloat(arms[0].headYaw);
				netmsgguaranted.writeFloat(arms[0].gunPitch);
				netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
				for (int i = 1; i < arms.length; i++) {
					netmsgguaranted.writeFloat(arms[i].headYaw);
					netmsgguaranted.writeFloat(arms[i].gunPitch);
				}
				net.post(netmsgguaranted);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void send_RespawnCommand() {
		if (isNetMaster() && Mission.isDeathmatch()) {
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try {
				netmsgguaranted.writeByte(82);
				net.post(netmsgguaranted);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void send_FireCommand(Actor actor, int i, float f, int i_48_) {
		if (isNetMaster() && net.isMirrored() && (Actor.isValid(actor) && actor.isNet())) {
			i &= 0xff;
			if (f < 0.0F) {
				try {
					outCommand.unLockAndClear();
					outCommand.writeByte(84);
					outCommand.writeNetObj(actor.net);
					outCommand.writeByte(i);
					outCommand.writeInt(i_48_);
					outCommand.setIncludeTime(false);
					net.post(Time.current(), outCommand);
				} catch (Exception exception) {
					System.out.println(exception.getMessage());
					exception.printStackTrace();
				}
			} else {
				try {
					outCommand.unLockAndClear();
					outCommand.writeByte(70);
					outCommand.writeFloat(f);
					outCommand.writeNetObj(actor.net);
					outCommand.writeByte(i);
					outCommand.writeInt(i_48_);
					outCommand.setIncludeTime(true);
					net.post(Time.current(), outCommand);
				} catch (Exception exception) {
					System.out.println(exception.getMessage());
					exception.printStackTrace();
				}
			}
		}
	}

	private void send_DeathRequest(Actor actor) {
		if (isNetMirror() && !(net.masterChannel() instanceof NetChannelInStream)) {
			try {
				NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
				netmsgfiltered.writeByte(68);
				netmsgfiltered.writeNetObj(actor == null ? (ActorNet) null : actor.net);
				netmsgfiltered.setIncludeTime(false);
				net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	public void createNetObject(NetChannel netchannel, int i) {
		if (netchannel == null) net = new Master(this);
		else net = new Mirror(this, netchannel, i);
	}

	public void netFirstUpdate(NetChannel netchannel) throws IOException {
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(73);
		if (dying == 0) netmsgguaranted.writeShort(0);
		else netmsgguaranted.writeShort(deathSeed);
		for (int i = 0; i < arms.length; i++) {
			netmsgguaranted.writeFloat(arms[i].headYaw);
			netmsgguaranted.writeFloat(arms[i].gunPitch);
		}
		net.postTo(netchannel, netmsgguaranted);
	}

	public float getReloadingTime(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) return prop.gunProperties[i].DELAY_AFTER_SHOOT;
		}
		return prop.gunProperties[0].DELAY_AFTER_SHOOT;
	}

	public float chainFireTime(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) return (prop.gunProperties[i].CHAINFIRE_TIME <= 0.0F ? 0.0F : prop.gunProperties[i].CHAINFIRE_TIME * Rnd(0.75F, 1.25F));
		}
		return (prop.gunProperties[0].CHAINFIRE_TIME <= 0.0F ? 0.0F : prop.gunProperties[0].CHAINFIRE_TIME * Rnd(0.75F, 1.25F));
	}

	public float probabKeepSameEnemy(Actor actor) {
		if (nearAirfield || isNetMirror() || actor == null || !(actor instanceof Aircraft) || Math.abs(time_lastCheckShoot - Time.current()) < 12000L || (float) actor.getSpeed(null) < 10.0F) {
			if (isSpotterAcGuided) return 0.98F;
			return 0.825F;
		}
		if (friendPlanesAreNear((Aircraft) actor)) return 0.0F;
		return 0.75F;
	}

	public float minTimeRelaxAfterFight() {
		return 0.0F;
	}

	public void gunStartParking(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) {
				arms[i].aim.setRotationForParking(arms[i].headYaw, arms[i].gunPitch, prop.gunProperties[i].HEAD_STD_YAW, prop.gunProperties[i].GUN_STD_PITCH, prop.gunProperties[i].HEAD_YAW_RANGE, prop.gunProperties[i].HEAD_MAX_YAW_SPEED,
						prop.gunProperties[i].GUN_MAX_PITCH_SPEED);
				break;
			}
		}
	}

	public void gunInMove(boolean bool, Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) {
				float f = arms[i].aim.t();
				float f_49_ = arms[i].aim.anglesYaw.getDeg(f);
				float f_50_ = arms[i].aim.anglesPitch.getDeg(f);
				setGunAngles(i, f_49_, f_50_);
				break;
			}
		}
	}

	public static final float KmHourToMSec(float f) {
		return f * 0.27778F;
	}

	Actor getFireCommandFromSpotter(Aircraft aircraft) {
		Actor actor = ((Maneuver) aircraft.FM).target_ground;
		return actor;
	}

	public Actor findEnemy(Aim aim) {
		if (isNetMirror()) return null;
		if (Time.current() < startDelay) return null;
		int i = 0;
		for (int i_51_ = 0; i_51_ < arms.length; i_51_++) {
			if (aim.equals(arms[i_51_].aim)) {
				i = i_51_;
				break;
			}
		}
		Actor actor = null;
		if (isSpotterAcGuided && spotterAc == null) {
			spotterAc = War.getNearestSpotter(this, AttackMaxRadius(aim) * 1.5F);
			if (spotterAc != null) actor = getFireCommandFromSpotter(spotterAc);
			else return null;
		} else if (spotterAc != null) {
			if (spotterAc.bSpotter) actor = getFireCommandFromSpotter(spotterAc);
			else {
				spotterAc = null;
				isSpotterAcGuided = false;
				return null;
			}
		}
		if (prop.gunProperties[i].ATTACK_FAST_TARGETS) NearestEnemies.set(prop.gunProperties[i].WEAPONS_MASK);
		else NearestEnemies.set(prop.gunProperties[i].WEAPONS_MASK, -9999.9F, KmHourToMSec(100.0F));
		if (!isSpotterAcGuided) actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), (double) AttackMaxRadius(aim), getArmy());
		if (actor == null) return null;
		if (!(actor instanceof Prey)) {
			System.out.println("arti: nearest enemies: non-Prey");
			return null;
		}
		if (!nearAirfield && !isNetMirror() && actor instanceof Aircraft && !((float) actor.getSpeed(null) < 10.0F)) {
			if (Math.abs(time_lastCheckShoot - Time.current()) < 12000L) {
				if (dontShoot) return null;
			} else if (friendPlanesAreNear((Aircraft) actor)) return null;
		}
		BulletProperties bulletproperties = null;
		if (arms[i].gun.prop != null) {
			int i_52_ = ((Prey) actor).chooseBulletType(arms[i].gun.prop.bullet);
			if (i_52_ < 0) return null;
			bulletproperties = arms[i].gun.prop.bullet[i_52_];
		}
		int i_53_ = ((Prey) actor).chooseShotpoint(bulletproperties);
		if (i_53_ < 0) return null;
		arms[i].aim.shotpoint_idx = i_53_;
		double d = distance(actor);
		d /= (double) AttackMaxDistance(aim);
		aim.setAimingError(World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F), 0.0F);
		if (actor instanceof Aircraft) d *= 0.25;
		aim.scaleAimingError((float) ((double) spotterCorrection * d));
		return actor;
	}

	public boolean enterToFireMode(int i, Actor actor, float f, Aim aim) {
		if (i == 1 && hideTmr < 0L) {
			float f_54_ = (float) actor.pos.getAbsPoint().distanceSquared(pos.getAbsPoint());
			if (f_54_ > RADIUS_HIDE * RADIUS_HIDE) return false;
			hideTmr = 0L;
		}
		int i_55_ = 0;
		for (int i_56_ = 0; i_56_ < arms.length; i_56_++) {
			if (aim.equals(arms[i_56_].aim)) {
				i_55_ = i_56_;
				break;
			}
		}
		if (!isNetMirror()) send_FireCommand(actor, arms[i_55_].aim.shotpoint_idx, i == 0 ? -1.0F : f, i_55_);
		return true;
	}

	private void Track_Mirror(int i, Actor actor, int i_57_) {
		if (dying != 1 && actor != null && arms[i].aim != null) arms[i].aim.passive_StartFiring(0, actor, i_57_, 0.0F);
	}

	private void Fire_Mirror(int i, Actor actor, int i_58_, float f) {
		if (dying != 1 && actor != null && arms[i].aim != null) {
			if (f <= 0.2F) f = 0.2F;
			if (f >= 15.0F) f = 15.0F;
			arms[i].aim.passive_StartFiring(1, actor, i_58_, f);
		}
	}

	void hideGunners(boolean bool) {
		bHideGunners = bool;
		setMesh(prop.meshName);
	}

	public int targetGun(Aim aim, Actor actor, float f, boolean bool) {
		if (!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0) return 0;
		if (isSpotterAcGuided && spotterAc != null && !spotterAc.bSpotter) {
			for (int i = 0; i < arms.length; i++)
				arms[i].gun.spotterModifier = 0.0F;
			return 0;
		}
		int i = 0;
		for (int i_59_ = 0; i_59_ < arms.length; i_59_++) {
			if (aim.equals(arms[i_59_].aim)) {
				i = i_59_;
				break;
			}
		}
		if (arms[i].gun instanceof CannonMidrangeGeneric) {
			int i_60_ = ((Prey) actor).chooseBulletType(arms[i].gun.prop.bullet);
			if (i_60_ < 0) return 0;
			((CannonMidrangeGeneric) arms[i].gun).setBulletType(i_60_);
		}
		boolean bool_61_ = ((Prey) actor).getShotpointOffset(arms[i].aim.shotpoint_idx, p1);
		if (!bool_61_) return 0;
		float f_62_ = f * Rnd(0.8F, 1.2F);
		if (!Aimer.aim((BulletAimer) arms[i].gun, actor, this, f_62_, p1, arms[i].fireOffset)) return 0;
		Point3d point3d = new Point3d();
		Aimer.getPredictedTargetPosition(point3d);
		point3d.add(aim.getAimingError());
		Point3d point3d1 = Aimer.getHunterFirePoint();
		float f_64_ = 0.01F * skillErrCoef;
		double d = point3d.distance(point3d1);
		double d_65_ = point3d.z;
		if (spotterAc == null) {
			spotterCorrection = 500F;
		} else {
			if (spotterCorrection > 3F) {
				point3d.x += World.Rnd().nextDouble(-spotterCorrection * skillErrCoef, spotterCorrection * skillErrCoef);
				point3d.y += World.Rnd().nextDouble(-spotterCorrection * skillErrCoef, spotterCorrection * skillErrCoef);
				spotterCorrection -= 3F;
			}
			point3d.sub(point3d1);
			point3d.scale(Rnd(1.0F - 0.015F * skillErrCoef, 1.0F + 0.015F * skillErrCoef));
			point3d.add(point3d1);
		}
		// TODO: ZP's gunner scare code
		float f3 = arms[i].gunPitch;
		float f4 = -actor.pos.getAbsOrient().getTangage();
		if (d < 800D && f4 - f3 > -5F && f4 - f3 < 5F) {
			RangeRandom rangerandom = World.Rnd();
			double d2 = 7D * (230D / d) * (230D / d);
			if (rangerandom.nextDouble(0.01D, 10D) <= d2) {
				float f9 = actor.pos.getAbsOrient().azimut();
				float f12 = arms[i].headYaw;
				float f14 = f12 * -1F;
				float f15 = (f9 - f14) + syaw;
				if (f15 > -185F && f15 < -175F || f15 < 185F && f15 > 175F) {
					tmAAAScared = Time.current();
					if (tmAAAScared != 0L && tmAAAScared - tmHL > 0x186a0L) {
						tmHL = Time.current();
						Loc loc = new Loc();
						Loc loc1 = new Loc();
						Loc loc2 = new Loc();
						loc.set(0.0D, 0.0D, 0.0D, 170F - Rnd(0.0F, 130F), Rnd(-5F, 2.0F), 0.0F);
						loc1.set(0.0D, 0.0D, 0.0D, 190F + Rnd(0.0F, 130F), Rnd(-5F, 2.0F), 0.0F);
						loc2.set(0.0D, 0.0D, 0.0D, 190F + Rnd(0.0F, 130F), Rnd(-5F, 2.0F), 0.0F);
						Loc loc3 = super.pos.getAbs();
						loc.add(loc3);
						loc1.add(loc3);
						loc2.add(loc3);
						new Soldier(this, getArmy(), loc);
						new Soldier(this, getArmy(), loc1);
						new Soldier(this, getArmy(), loc2);
					}
				}
			}
		}
		if (tmAAAScared != 0L && Time.current() < tmAAAScared + 0x186a0L) return 0;

		// TODO
		if (f_62_ > 0.0010F) {
			Point3d point3d_66_ = new Point3d();
			actor.pos.getAbs(point3d_66_);
			point3d_66_.add(aim.getAimingError());
			tmpv.sub(point3d, point3d_66_);
			double d_67_ = tmpv.length();
			if (d_67_ > 0.0010) {
				float f_68_ = (float) d_67_ / f_62_;
				if (f_68_ > 200.0F) f_68_ = 200.0F;
				float f_69_ = f_68_ * 0.015F;
				point3d_66_.sub(point3d1);
				double d_70_ = (point3d_66_.x * point3d_66_.x + point3d_66_.y * point3d_66_.y + point3d_66_.z * point3d_66_.z);
				if (d_70_ > 0.01) {
					float f_71_ = (float) tmpv.dot(point3d_66_);
					f_71_ /= (float) (d_67_ * Math.sqrt(d_70_));
					f_71_ = (float) Math.sqrt((double) (1.0F - f_71_ * f_71_));
					f_69_ *= 0.4F + 0.6F * f_71_;
				}
				f_69_ *= 1.1F;
				int i_73_ = Mission.curCloudsType();
				if (i_73_ > 2) {
					float f_74_ = i_73_ > 4 ? 300.0F : 500.0F;
					float f_75_ = (float) (d / (double) f_74_);
					if (f_75_ > 1.0F) {
						if (f_75_ > 10.0F) return 0;
						f_75_ = (f_75_ - 1.0F) / 9.0F * 2.0F + 1.0F;
						f_69_ *= f_75_;
					}
				}
				if (i_73_ >= 3 && d_65_ > (double) Mission.curCloudsHeight()) f_69_ *= 1.25F;
				f_64_ += f_69_;
			}
		}

		if (actor instanceof Aircraft) {
			aim.scaleAimingError(0.73F);
			f_64_ *= 0.3 + (double) (skillErrCoef * 0.5F);
		} else if (aim.getAimingError().length() > 0.011 * (double) skillErrCoef) {
			if (spotterAc == null) aim.scaleAimingError(0.993F);
			else aim.scaleAimingError(0.973F);
		}
		if (World.Sun().ToSun.z < -0.15F) {
			float f_76_ = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
			if (f_76_ >= 1.0F) f_76_ = 1.0F;
			if (actor instanceof Aircraft && Time.current() - ((Aircraft) actor).tmSearchlighted < 1000L) f_76_ = 0.0F;
			f_64_ += 12.0F * f_76_;
		}
		float f_77_ = (float) actor.getSpeed(null) - 10.0F;
		if (f_77_ > 0.0F) {
			float f_78_ = 83.333336F;
			f_77_ = f_77_ >= f_78_ ? 1.0F : f_77_ / f_78_;
			f_64_ += f_77_ * prop.gunProperties[i].FAST_TARGETS_ANGLE_ERROR;
		}
		Vector3d vector3d = new Vector3d();
		if (!((BulletAimer) arms[i].gun).FireDirection(point3d1, point3d, vector3d)) return 0;
		float f_79_;
		float f_80_;
		if (bool) {
			f_79_ = 99999.0F;
			f_80_ = 99999.0F;
		} else {
			f_79_ = prop.gunProperties[i].HEAD_MAX_YAW_SPEED;
			f_80_ = prop.gunProperties[i].GUN_MAX_PITCH_SPEED;
		}
		Orient orient = new Orient();
		orient.add(arms[i].fireOrient, pos.getAbs().getOrient());
		int i_81_ = (arms[i].aim.setRotationForTargeting(this, orient, point3d1, arms[i].headYaw, arms[i].gunPitch, vector3d, f_64_, f_62_, prop.gunProperties[i].HEAD_YAW_RANGE, prop.gunProperties[i].GUN_MIN_PITCH, prop.gunProperties[i].GUN_MAX_PITCH,
				f_79_, f_80_, 0.0F));
		return i_81_;
	}

	public void singleShot(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) {
				arms[i].gun.shots(1);
				break;
			}
		}
	}

	public void startFire(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) {
				arms[i].gun.shots(-1);
				break;
			}
		}
	}

	public void continueFire(Aim aim) {
	}

	public void stopFire(Aim aim) {
		for (int i = 0; i < arms.length; i++) {
			if (aim.equals(arms[i].aim)) {
				arms[i].gun.shots(0);
				break;
			}
		}
	}

//	/* synthetic */static long access$610(ArtilleryGeneric artillerygeneric) {
//		return artillerygeneric.respawnDelay--;
//	}
//
//	/* synthetic */static long access$804(ArtilleryGeneric artillerygeneric) {
//		return ++artillerygeneric.hideTmr;
//	}
}
