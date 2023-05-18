/*LaserDesignatorGeneric class for the SAS Engine Mod*/
/*By western, new built on 25th/Jul./2020 */
package com.maddox.il2.objects.vehicles.artillery;

import java.io.IOException;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Aimer;
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
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeLaserDesignator;
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

public abstract class LaserDesignatorGeneric extends ActorHMesh
  implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, ActorAlign, HunterInterface, TypeLaserDesignator {
	private LaserDesigProperties prop = null;
	private long time_lastCheckShoot = 0L;
	protected static final int DELAY_CHECK_SHOOT = 12000;
	private float heightAboveLandSurface;
	private float heightAboveLandSurface2;
//	private Actor[] actorsAimed;
	private FireDevice laserarm;
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
	private static LaserDesigProperties constr_arg1 = null;
	private static ActorSpawnArg constr_arg2 = null;
	private static Point3d p = new Point3d();
	private static Point3d p1 = new Point3d();
	private static Orient o = new Orient();
	private static Vector3f n = new Vector3f();
	private static Vector3d tmpv = new Vector3d();
	public Aircraft spotterAc;
	public boolean isSpotterAcGuided;
	static final float[] err = { 1.17F, 1.37F, 1.67F, 2.13F };
	private NetMsgFiltered outCommand = new NetMsgFiltered();
	private float spotterCorrection = 500.0F;
	private long tmAAAScared;
	private long tmHL;
	private float syaw;
	private boolean bHideGunners = false;
	private boolean bLaserOn = false;
	private Point3d laserSpotPos = new Point3d();
	public static boolean bDrawLaserSpot = false;
	public static boolean bImmortalObject = false;

	public static class SPAWN implements ActorSpawn {
		public Class cls;
		public LaserDesigProperties proper;

		private static float getF(SectFile sectfile, String string, String string_0_, float f, float f_1_) {
			float f_2_ = sectfile.get(string, string_0_, -9865.345F);
			if (f_2_ == -9865.345F || f_2_ < f || f_2_ > f_1_) {
				if (f_2_ == -9865.345F) System.out.println("LaserDesignator: Parameter [" + string + "]:<" + string_0_ + "> " + "not found");
				else System.out.println("LaserDesignator: Value of [" + string + "]:<" + string_0_ + "> (" + f_2_ + ")" + " is out of range (" + f + ";" + f_1_ + ")");
				throw new RuntimeException("Can't set property");
			}
			return f_2_;
		}

		private static String getS(SectFile sectfile, String s, String s1)
		{
			String s2 = sectfile.get(s, s1);
			if (s2 == null || s2.length() <= 0)
			{
				// TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
				if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
					System.out.print("LaserDesignator: Parameter [" + s + "]:<" + s1 + "> ");
					System.out.println(s2 != null ? "is empty" : "not found");
					throw new RuntimeException("Can't set property");
				} else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
					System.out.println("LaserDesignator \"" + s + "\" is not (correctly) declared in technics.ini file!");
				}
				return null;
				// ---
			} else
			{
				return s2;
			}
		}

		private static String getS(SectFile sectfile, String string, String string_5_, String string_6_) {
			String string_7_ = sectfile.get(string, string_5_);
			if (string_7_ == null || string_7_.length() <= 0) return string_6_;
			return string_7_;
		}

		private static LaserDesigProperties LoadLaserDesigProperties(SectFile sectfile, String string, Class var_class) {
			// TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
			String checkMesh = getS(sectfile, string, "MeshSummer");
			if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (checkMesh == null || checkMesh.length() == 0)) return null;
			// TODO: ---
			LaserDesigProperties laserproperties = new LaserDesigProperties();
			String string_8_ = getS(sectfile, string, "PanzerType", null);
			if (string_8_ == null) string_8_ = "Artillery";
			laserproperties.fnShotPanzer = TableFunctions.GetFunc2(string_8_ + "ShotPanzer");
			laserproperties.fnExplodePanzer = TableFunctions.GetFunc2(string_8_ + "ExplodePanzer");
			laserproperties.PANZER_TNT_TYPE = getF(sectfile, string, "PanzerSubtype", 0.0F, 100.0F);
			// TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//			laserproperties.meshSummer = getS(sectfile, string, "MeshSummer");
			laserproperties.meshSummer = checkMesh;
			// TODO: ---
			laserproperties.meshDesert = getS(sectfile, string, "MeshDesert", laserproperties.meshSummer);
			laserproperties.meshWinter = getS(sectfile, string, "MeshWinter", laserproperties.meshSummer);
			laserproperties.meshSummer1 = getS(sectfile, string, "MeshSummerDamage", null);
			laserproperties.meshDesert1 = getS(sectfile, string, "MeshDesertDamage", laserproperties.meshSummer1);
			laserproperties.meshWinter1 = getS(sectfile, string, "MeshWinterDamage", laserproperties.meshSummer1);
			int i = ((laserproperties.meshSummer1 == null ? 1 : 0) + (laserproperties.meshDesert1 == null ? 1 : 0) + (laserproperties.meshWinter1 == null ? 1 : 0));
			if (i != 0 && i != 3) {
				System.out.println("LaserDesignator: Uncomplete set of damage meshes for '" + string + "'");
				// TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
				if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
					throw new RuntimeException("Can't register laser designator object");
				return null;
				// ---
			}
			laserproperties.PANZER_BODY_FRONT = getF(sectfile, string, "PanzerBodyFront", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerBodyBack", -9865.345F) == -9865.345F) {
				laserproperties.PANZER_BODY_BACK = laserproperties.PANZER_BODY_FRONT;
				laserproperties.PANZER_BODY_SIDE = laserproperties.PANZER_BODY_FRONT;
				laserproperties.PANZER_BODY_TOP = laserproperties.PANZER_BODY_FRONT;
			} else {
				laserproperties.PANZER_BODY_BACK = getF(sectfile, string, "PanzerBodyBack", 0.0010F, 9.999F);
				laserproperties.PANZER_BODY_SIDE = getF(sectfile, string, "PanzerBodySide", 0.0010F, 9.999F);
				laserproperties.PANZER_BODY_TOP = getF(sectfile, string, "PanzerBodyTop", 0.0010F, 9.999F);
			}
			if (sectfile.get(string, "PanzerHead", -9865.345F) == -9865.345F) laserproperties.PANZER_HEAD = laserproperties.PANZER_BODY_FRONT;
			else laserproperties.PANZER_HEAD = getF(sectfile, string, "PanzerHead", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerHeadTop", -9865.345F) == -9865.345F) laserproperties.PANZER_HEAD_TOP = laserproperties.PANZER_BODY_TOP;
			else laserproperties.PANZER_HEAD_TOP = getF(sectfile, string, "PanzerHeadTop", 0.0010F, 9.999F);
			float f = Math.min(Math.min(laserproperties.PANZER_BODY_BACK, laserproperties.PANZER_BODY_TOP), Math.min(laserproperties.PANZER_BODY_SIDE, laserproperties.PANZER_HEAD_TOP));
			laserproperties.HITBY_MASK = f > 0.015F ? -2 : -1;
			laserproperties.explodeName = getS(sectfile, string, "Explode", "Artillery");
			String string_14_ = "com.maddox.il2.objects.weapons.MachineGunBoforsUS40";
			try {
				laserproperties.gunClass = Class.forName(string_14_);
			} catch (Exception exception) {
				System.out.println("LaserDesignator: Can't find gun class '" + string_14_ + "'");
				// TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
				if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
					throw new RuntimeException("Can't register laser designator object");
				return null;
				// ---
			}
			laserproperties.WEAPONS_MASK = 4;  // copied from objects.weapons. CannonMidrangeGeneric.class
			laserproperties.ATTACK_FAST_TARGETS = false;
			f = sectfile.get(string, "FireFastTargets", -9865.345F);
			if (f != -9865.345F) laserproperties.ATTACK_FAST_TARGETS = f > 0.5F;
			laserproperties.ATTACK_MIN_DISTANCE = getF(sectfile, string, "AttackMinDistance", 50.0F, 10000.0F);
			laserproperties.ATTACK_MAX_DISTANCE = getF(sectfile, string, "AttackMaxDistance", 60.0F, 60000.0F);
			laserproperties.ATTACK_MAX_RADIUS = getF(sectfile, string, "AttackMaxRadius", 60.0F, 60000.0F);
			laserproperties.ATTACK_MAX_HEIGHT = getF(sectfile, string, "AttackMaxHeight", 6.0F, 18000.0F);
			if (laserproperties.ATTACK_MIN_DISTANCE > laserproperties.ATTACK_MAX_DISTANCE) laserproperties.ATTACK_MIN_DISTANCE = laserproperties.ATTACK_MAX_DISTANCE - 10.0F;
			int i_16_ = sectfile.sectionIndex(string);
			if (sectfile.varExist(i_16_, "HeadYawHalfRange")) {
				float f_17_ = getF(sectfile, string, "HeadYawHalfRange", 0.0F, 180.0F);
				laserproperties.HEAD_YAW_RANGE = new AnglesRange(-1.0F, 1.0F);
				laserproperties.HEAD_YAW_RANGE.set(-f_17_, f_17_);
				laserproperties.HEAD_STD_YAW = 0.0F;
			} else {
				float f_18_ = getF(sectfile, string, "HeadMinYaw", -180.0F, 180.0F);
				float f_19_ = getF(sectfile, string, "HeadStdYaw", -180.0F, 180.0F);
				float f_20_ = getF(sectfile, string, "HeadMaxYaw", -180.0F, 180.0F);
				laserproperties.HEAD_YAW_RANGE = new AnglesRange(-1.0F, 1.0F);
				laserproperties.HEAD_YAW_RANGE.set(f_18_, f_20_);
				laserproperties.HEAD_STD_YAW = f_19_;
			}
			laserproperties.GUN_MIN_PITCH = getF(sectfile, string, "GunMinPitch", -15.0F, 85.0F);
			laserproperties.GUN_STD_PITCH = getF(sectfile, string, "GunStdPitch", -15.0F, 89.9F);
			laserproperties.GUN_MAX_PITCH = getF(sectfile, string, "GunMaxPitch", 0.0F, 89.9F);
			laserproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, string, "HeadMaxYawSpeed", 0.1F, 999.0F);
			laserproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, string, "GunMaxPitchSpeed", 0.1F, 999.0F);
			laserproperties.DELAY_AFTER_SHOOT = getF(sectfile, string, "DelayAfterShoot", 0.0F, 999.0F);
			laserproperties.CHAINFIRE_TIME = getF(sectfile, string, "ChainfireTime", 0.0F, 600.0F);
			float f_21_ = sectfile.get(string, "FastTargetsAngleError", -9865.345F);
			if (f_21_ <= 0.0F) f_21_ = 0.0F;
			else if (f_21_ >= 45.0F) f_21_ = 45.0F;
			laserproperties.FAST_TARGETS_ANGLE_ERROR = f_21_;
			Property.set(var_class, "iconName", "icons/" + getS(sectfile, string, "Icon") + ".mat");
			Property.set(var_class, "meshName", laserproperties.meshSummer);
			if (Config.cur.ini.get("Mods", "ShowGroundLaserDesignatorSpot", 0) == 1)
				bDrawLaserSpot = true;
			if (Config.cur.ini.get("Mods", "ImmortalGroundLaserDesignator", 0) == 1) {
				bImmortalObject = true;
				laserproperties.PANZER_BODY_FRONT = 9.999F;
				laserproperties.PANZER_BODY_BACK = 9.999F;
				laserproperties.PANZER_BODY_SIDE = 9.999F;
				laserproperties.PANZER_BODY_TOP = 9.999F;
			}
			return laserproperties;
		}

		public SPAWN(Class var_class) {
			try {
				String string = var_class.getName();
				int i = string.lastIndexOf('.');
				int i_22_ = string.lastIndexOf('$');
				if (i < i_22_) i = i_22_;
				String string_23_ = string.substring(i + 1);
				proper = LoadLaserDesigProperties(Statics.getTechnicsFile(), string_23_, var_class);

				// TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
				if (ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL && proper == null) return;
				// TODO: ---

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
			LaserDesignatorGeneric laserdesiggeneric;
			try {
				LaserDesignatorGeneric.constr_arg1 = proper;
				LaserDesignatorGeneric.constr_arg2 = actorspawnarg;
				laserdesiggeneric = (LaserDesignatorGeneric) cls.newInstance();
				LaserDesignatorGeneric.constr_arg1 = null;
				LaserDesignatorGeneric.constr_arg2 = null;
			} catch (Exception exception) {
				LaserDesignatorGeneric.constr_arg1 = null;
				LaserDesignatorGeneric.constr_arg2 = null;
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("SPAWN: Can't create LaserDesignator object [class:" + cls.getName() + "]");
				return null;
			}
			return laserdesiggeneric;
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
							laserarm.aim.forgetAiming();
							LaserDesignatorGeneric.this.setGunAngles(f, f_24_);
						}
					} else if (dying != 1) {
						LaserDesignatorGeneric.this.setGunAngles(f, f_24_);
						LaserDesignatorGeneric.this.Die(null, i, false);
					}
					if (netmsginput.available() > 0) {
						f = netmsginput.readFloat();
						f_24_ = netmsginput.readFloat();
						if (i <= 0) {
							if (dying != 1) {
								laserarm.aim.forgetAiming();
								LaserDesignatorGeneric.this.setGunAngles(f, f_24_);
							}
						} else if (dying != 1) LaserDesignatorGeneric.this.setGunAngles(f, f_24_);
					}
					return true;
				}
				case 82:
					if (isMirrored()) {
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
						post(netmsgguaranted);
					}
					dying = 0;
					LaserDesignatorGeneric.this.setDiedFlag(false);
					laserarm.aim.forgetAiming();
					setMesh(prop.meshName);
					LaserDesignatorGeneric.this.setDefaultLivePose();
					return true;
				case 68: {
					if (isMirrored()) {
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
						post(netmsgguaranted);
					}
					short i = netmsginput.readShort();
					float f = netmsginput.readFloat();
					float f_26_ = netmsginput.readFloat();
					LaserDesignatorGeneric.this.setGunAngles(f, f_26_);
					if (i > 0 && dying != 1) {
						com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
						Actor actor = (netobj == null ? null : ((ActorNet) netobj).actor());
						LaserDesignatorGeneric.this.Die(actor, i, true);
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
				LaserDesignatorGeneric.this.Track_Mirror(i_28_, actor, i);
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
				LaserDesignatorGeneric.this.Fire_Mirror(i_30_, actor, i, f_29_);
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
			LaserDesignatorGeneric.this.Die(actor, (short) 0, true);
			return true;
		}
	}

	class Move extends Interpolate {
		public boolean tick() {
			if (dying == 1) {
				if (respawnDelay-- <= 0L) {
					if (!Mission.isDeathmatch()) {
						if (laserarm.aim != null) {
							laserarm.aim.forgetAll();
							laserarm.aim = null;
						}
						if (laserarm.gun != null) {
							ObjState.destroy(laserarm.gun);
							laserarm.gun = null;
						}
						laserarm = null;
						return false;
					}
					if (!LaserDesignatorGeneric.this.isNetMaster()) {
						respawnDelay = 10000L;
						return true;
					}
					dying = 0;
					hideTmr = 0L;
					if (!LaserDesignatorGeneric.this.isNetMirror() && RADIUS_HIDE > 0.0F) hideTmr = -1L;
					LaserDesignatorGeneric.this.setDiedFlag(false);
					laserarm.aim.forgetAiming();
					setMesh(prop.meshName);
					LaserDesignatorGeneric.this.setDefaultLivePose();
					LaserDesignatorGeneric.this.send_RespawnCommand();
					time_lastCheckShoot = Time.current() - 12000L;
					return true;
				}
				return true;
			}
			laserarm.aim.tick_();
			if (RADIUS_HIDE > 0.0F && hideTmr >= 0L && !LaserDesignatorGeneric.this.isNetMirror()) {
				if (laserarm.aim.getEnemy() != null) hideTmr = 0L;
				else if (++hideTmr > LaserDesignatorGeneric.delay_hide_ticks) hideTmr = -1L;
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

	public static class LaserDesigProperties {
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
		public int WEAPONS_MASK = 0;
		public float ATTACK_MIN_DISTANCE;
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

	protected final boolean Head360(LaserDesigProperties gunprops) {
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

	private float[] computeDeathPose(short i) {
		RangeRandom rangerandom = new RangeRandom((long) i);
		float[] fs = new float[10];
		fs[0] = laserarm.headYaw + rangerandom.nextFloat(-15.0F, 15.0F);
		fs[1] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 9.0F));
		fs[2] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 9.0F));
		fs[3] = -laserarm.gunPitch + rangerandom.nextFloat(-15.0F, 15.0F);
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
			if (laserarm.aim != null) laserarm.aim.forgetAiming();
			float f = 0.0F;
			float[] fs = computeDeathPose(i);
//			String string = "";
			hierMesh().chunkSetAngles("Head", fs[0], fs[1], fs[2]);
			hierMesh().chunkSetAngles("Gun", fs[3], fs[4], fs[5]);
			hierMesh().chunkSetAngles("Body", fs[6], fs[7], fs[8]);
			f = fs[9];
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

	private void setGunAngles(float f, float f_44_) {
		laserarm.headYaw = f;
		laserarm.gunPitch = f_44_;
		hierMesh().chunkSetAngles("Head", laserarm.headYaw, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Gun", -laserarm.gunPitch, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
		pos.inValidate(false);
	}

	public void destroy() {
		if (!isDestroyed()) {
			bLaserOn = false;
			if (laserarm.aim != null) {
				laserarm.aim.forgetAll();
				laserarm.aim = null;
			}
			if (laserarm.gun != null) {
				destroy(laserarm.gun);
				laserarm.gun = null;
			}
			laserarm = null;
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
		setGunAngles(prop.HEAD_STD_YAW, prop.GUN_STD_PITCH);
		Align();
	}

	protected LaserDesignatorGeneric() {
		this(constr_arg1, constr_arg2);
	}

	public void setMesh(String string) {
		super.setMesh(string);
		if (!Config.cur.b3dgunners || false != bHideGunners) this.mesh().materialReplaceToNull("Pilot1");
	}

	private LaserDesignatorGeneric(LaserDesigProperties laserproperties, ActorSpawnArg actorspawnarg) {
		super(laserproperties.meshName);
		prop = laserproperties;
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
		laserarm = new FireDevice();
		laserarm.gun = null;
		try {
			laserarm.gun = (Gun) prop.gunClass.newInstance();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			System.out.println("LaserDesignator: Can't create gun '" + prop.gunClass.getName() + "'");
		}
		laserarm.gun.set(this, "Gun");
		laserarm.gun.loadBullets(-1);
		laserarm.headYaw = prop.HEAD_STD_YAW;
		laserarm.gunPitch = prop.GUN_STD_PITCH;
		int i_46_ = hierMesh().chunkFind("Head");
		hierMesh().setCurChunk(i_46_);
		Loc loc = new Loc();
		hierMesh().getChunkLocObj(loc);
		laserarm.fireOffset = new Point3d();
		loc.get(laserarm.fireOffset);
		laserarm.fireOrient = new Orient();
		loc.get(laserarm.fireOrient);
		laserarm.gun.qualityModifier = err[Skill - 1];
		if (isSpotterAcGuided) laserarm.gun.spotterModifier = laserarm.gun.prop.aimMaxDist;
		if (!isNetMirror() && RADIUS_HIDE > 0.0F) hideTmr = -1L;
		setDefaultLivePose();
		startMove();
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
			if (laserarm.aim != null) {
				laserarm.aim.forgetAll();
				laserarm.aim = null;
			}
			laserarm.aim = new Aim(this, isNetMirror());
			interpPut(new Move(), "move", Time.current(), null);
		}
	}

	public int WeaponsMask() {
		int i = 0;
		i |= prop.WEAPONS_MASK;
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
		if (prop.ATTACK_MAX_DISTANCE > f) f = prop.ATTACK_MAX_DISTANCE;
		return f;
	}

	public float AttackMaxDistance(Aim aim) {
		if (laserarm.aim.equals(aim)) return prop.ATTACK_MAX_DISTANCE;
		return -1.0F;
	}

	public float AttackMaxRadius(Aim aim) {
		if (laserarm.aim.equals(aim)) return prop.ATTACK_MAX_RADIUS;
		return 0.0F;
	}

	public float AttackMaxHeight(Aim aim) {
		if (laserarm.aim.equals(aim)) return prop.ATTACK_MAX_HEIGHT;
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
				netmsgguaranted.writeFloat(laserarm.headYaw);
				netmsgguaranted.writeFloat(laserarm.gunPitch);
				netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
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
		netmsgguaranted.writeFloat(laserarm.headYaw);
		netmsgguaranted.writeFloat(laserarm.gunPitch);
		net.postTo(netchannel, netmsgguaranted);
	}

	public float getReloadingTime(Aim aim) {
		return prop.DELAY_AFTER_SHOOT;
	}

	public float chainFireTime(Aim aim) {
		return (prop.CHAINFIRE_TIME <= 0.0F ? 0.0F : prop.CHAINFIRE_TIME * Rnd(0.9F, 1.03F));
	}

	public float probabKeepSameEnemy(Actor actor) {
		if (isNetMirror() || actor == null || !(actor instanceof Aircraft) || Math.abs(time_lastCheckShoot - Time.current()) < 12000L || (float) actor.getSpeed(null) < 10.0F) {
			if (isSpotterAcGuided) return 0.98F;
			return 0.825F;
		}
		return 0.75F;
	}

	public float minTimeRelaxAfterFight() {
		return 0.0F;
	}

	public void gunStartParking(Aim aim) {
		if (aim.equals(laserarm.aim)) {
			laserarm.aim.setRotationForParking(laserarm.headYaw, laserarm.gunPitch, prop.HEAD_STD_YAW, prop.GUN_STD_PITCH, prop.HEAD_YAW_RANGE, prop.HEAD_MAX_YAW_SPEED,
					prop.GUN_MAX_PITCH_SPEED);
		}
	}

	public void gunInMove(boolean bool, Aim aim) {
		if (aim.equals(laserarm.aim)) {
			float f = laserarm.aim.t();
			float f_49_ = laserarm.aim.anglesYaw.getDeg(f);
			float f_50_ = laserarm.aim.anglesPitch.getDeg(f);
			setGunAngles(f_49_, f_50_);
			aim.getEnemy().pos.getAbs(laserSpotPos);
			if (bLaserOn && bDrawLaserSpot) showLaserSpot(laserSpotPos);
			if ( bLogDetail && bLaserOn) System.out.println("LaserDesignatorGeneric(" + actorString(this) + ") - gunInMove(bool, aim) - bLaserOn, laserSpotPos=" + laserSpotPos);
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
		NearestEnemies.set(prop.WEAPONS_MASK, -9999.9F, KmHourToMSec(100.0F));
		if (!isSpotterAcGuided) actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), (double) AttackMaxRadius(aim), getArmy(), (Actor) this);
		if ( bLogDetail ) System.out.println("LaserDesignatorGeneric(" + actorString(this) + ") - findEnemy(aim) - GetNearestEnemy()=" + actorString(actor));
		if (actor == null) return null;
		if (!(actor instanceof Prey)) {
			System.out.println("ldg: nearest enemies: non-Prey");
			return null;
		}
		BulletProperties bulletproperties = null;
		if (laserarm.gun.prop != null) {
			int i_52_ = ((Prey) actor).chooseBulletType(laserarm.gun.prop.bullet);
			if ( bLogDetail ) System.out.println("LaserDesignatorGeneric(" + actorString(this) + ") - findEnemy(aim) - chooseBulletType i_52_=" + i_52_);
			if (i_52_ < 0) return null;
			bulletproperties = laserarm.gun.prop.bullet[i_52_];
		}
		int i_53_ = ((Prey) actor).chooseShotpoint(bulletproperties);
		if ( bLogDetail ) System.out.println("LaserDesignatorGeneric(" + actorString(this) + ") - findEnemy(aim) - chooseShotpoint i_53_=" + i_53_);
		if (i_53_ < 0) return null;
		laserarm.aim.shotpoint_idx = i_53_;
		double d = distance(actor);
		d /= (double) AttackMaxDistance(aim);
		aim.setAimingError(World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F), 0.0F);
		if (actor instanceof Aircraft) d *= 0.25;
		aim.scaleAimingError((float) ((double) spotterCorrection * d));

		if ( bLogDetail ) System.out.println("LaserDesignatorGeneric(" + actorString(this) + ") - findEnemy(aim) - return actor=" + actorString(actor));

		return actor;
	}

	public boolean enterToFireMode(int i, Actor actor, float f, Aim aim) {
		if (i == 1 && hideTmr < 0L) {
			float f_54_ = (float) actor.pos.getAbsPoint().distanceSquared(pos.getAbsPoint());
			if (f_54_ > RADIUS_HIDE * RADIUS_HIDE) return false;
			hideTmr = 0L;
		}
		if (!isNetMirror()) send_FireCommand(actor, laserarm.aim.shotpoint_idx, i == 0 ? -1.0F : f, 0);
		return true;
	}

	private void Track_Mirror(int i, Actor actor, int i_57_) {
		if (dying != 1 && actor != null && laserarm.aim != null) laserarm.aim.passive_StartFiring(0, actor, i_57_, 0.0F);
	}

	private void Fire_Mirror(int i, Actor actor, int i_58_, float f) {
		if (dying != 1 && actor != null && laserarm.aim != null) {
			if (f <= 0.2F) f = 0.2F;
			if (f >= 15.0F) f = 15.0F;
			laserarm.aim.passive_StartFiring(1, actor, i_58_, f);
		}
	}

	void hideGunners(boolean bool) {
		bHideGunners = bool;
		setMesh(prop.meshName);
	}

	public int targetGun(Aim aim, Actor actor, float f, boolean bool) {
		if (!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0) return 0;
		if (isSpotterAcGuided && spotterAc != null && !spotterAc.bSpotter) {
			laserarm.gun.spotterModifier = 0.0F;
			return 0;
		}
		if (laserarm.gun instanceof CannonMidrangeGeneric) {
			int i_60_ = ((Prey) actor).chooseBulletType(laserarm.gun.prop.bullet);
			if (i_60_ < 0) return 0;
			((CannonMidrangeGeneric) laserarm.gun).setBulletType(i_60_);
		}
		boolean bool_61_ = ((Prey) actor).getShotpointOffset(laserarm.aim.shotpoint_idx, p1);
		if (!bool_61_) return 0;
		float f_62_ = f * Rnd(0.8F, 1.2F);
		if (!Aimer.aim((BulletAimer) laserarm.gun, actor, this, f_62_, p1, laserarm.fireOffset)) return 0;
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
		float f3 = laserarm.gunPitch;
		float f4 = -actor.pos.getAbsOrient().getTangage();
		if (d < 800D && f4 - f3 > -5F && f4 - f3 < 5F && (actor instanceof Aircraft)) {
			RangeRandom rangerandom = World.Rnd();
			double d2 = 7D * (230D / d) * (230D / d);
			if (rangerandom.nextDouble(0.01D, 10D) <= d2) {
				float f9 = actor.pos.getAbsOrient().azimut();
				float f12 = laserarm.headYaw;
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
		float f_77_ = (float) actor.getSpeed(null) - 10.0F;
		if (f_77_ > 0.0F) {
			float f_78_ = 83.333336F;
			f_77_ = f_77_ >= f_78_ ? 1.0F : f_77_ / f_78_;
			f_64_ += f_77_ * prop.FAST_TARGETS_ANGLE_ERROR;
		}
		Vector3d vector3d = new Vector3d();
		if (!((BulletAimer) laserarm.gun).FireDirection(point3d1, point3d, vector3d)) return 0;
		float f_79_;
		float f_80_;
		if (bool) {
			f_79_ = 99999.0F;
			f_80_ = 99999.0F;
		} else {
			f_79_ = prop.HEAD_MAX_YAW_SPEED;
			f_80_ = prop.GUN_MAX_PITCH_SPEED;
		}
		Orient orient = new Orient();
		orient.add(laserarm.fireOrient, pos.getAbs().getOrient());
		int i_81_ = (laserarm.aim.setRotationForTargeting(this, orient, point3d1, laserarm.headYaw, laserarm.gunPitch, vector3d, f_64_, f_62_, prop.HEAD_YAW_RANGE, prop.GUN_MIN_PITCH, prop.GUN_MAX_PITCH,
				f_79_, f_80_, 0.0F));
		return i_81_;
	}

	public void singleShot(Aim aim) {
	}

	public void startFire(Aim aim) {
		if (aim.equals(laserarm.aim)) {
			bLaserOn = true;
			aim.getEnemy().pos.getAbs(laserSpotPos);
			if (bDrawLaserSpot) showLaserSpot(laserSpotPos);
			if ( bLogDetail ) System.out.println("LaserDesignatorGeneric(" + actorString(this) + ") - startFire(aim) - bLaserOn, laserSpotPos=" + laserSpotPos);
		}
	}

	public void continueFire(Aim aim) {
	}

	public void stopFire(Aim aim) {
		if (aim.equals(laserarm.aim)) {
			bLaserOn = false;
		}
	}

	public Point3d getLaserSpot() {
		return laserSpotPos;
	}

	public boolean setLaserSpot(Point3d p3d) {
		laserSpotPos = p3d;
		return true;
	}

	public boolean getLaserOn() {
		return bLaserOn;
	}

	public boolean setLaserOn(boolean flag) {
		return bLaserOn = flag;
	}

	public boolean getLaserArmEngaged() {
		return false;
	}

	public boolean setLaserArmEngaged(boolean flag) {
		return false;
	}

	private void showLaserSpot(Point3d point3d)
	{
		point3d.z = World.land().HQ(point3d.x, point3d.y);
		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
		eff3dactor.postDestroy(Time.current() + 1500L);
	}

	private static String actorString(Actor actor) {
		if (!Actor.isValid(actor)) return "(InvalidActor)";
		String s;
		try {
			s = actor.getClass().getName();
		} catch(Exception e) {
			System.out.println("LaserDesignatorGeneric - actorString(): Cannot resolve class name of " + actor);
			return "(NoClassnameActor)";
		}
		int i = s.lastIndexOf('.');
		String strSection = s.substring(i + 1);
		strSection = strSection + '@' + Integer.toHexString(actor.hashCode());
		return strSection;
	}

	private static boolean bLogDetail = false;
}
