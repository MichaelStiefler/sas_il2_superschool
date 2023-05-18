/*4.10.1 class*/
package com.maddox.il2.objects.vehicles.tanks;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2d;
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
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.Moving;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.StaticObstacle;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.UnitData;
import com.maddox.il2.ai.ground.UnitInPackedForm;
import com.maddox.il2.ai.ground.UnitInterface;
import com.maddox.il2.ai.ground.UnitMove;
import com.maddox.il2.ai.ground.UnitSpawn;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Finger;
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
import com.maddox.sound.SoundFX;
import com.maddox.util.TableFunction2;
import com.maddox.il2.ai.Front.Marker;

public abstract class TankGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, UnitInterface,
		HunterInterface
{
	private TankProperties			prop							= null;
	private int						codeName;
	private float					heightAboveLandSurface;
	private float					heightAboveLandSurface2;
	public UnitData					udata							= new UnitData();
	private Moving					mov								= new Moving();
	protected Eff3DActor			dust;
	protected SoundFX				engineSFX						= null;
	protected int					engineSTimer					= 9999999;
	protected int					ticksIn8secs					= (int) (8.0F / Time.tickConstLenFs());
	protected Gun					gun;
	private Aim						aime;
	private float					headYaw;
	private float					gunPitch;
	private int						collisionStage					= 0;
	static final int				COLLIS_NO_COLLISION				= 0;
	static final int				COLLIS_JUST_COLLIDED			= 1;
	static final int				COLLIS_MOVING_FROM_COLLISION	= 2;
	private Vector2d				collisVector					= new Vector2d();
	private Actor					collidee;
	private StaticObstacle			obs								= new StaticObstacle();

	//TODO: Edit by |ZUTI|: changed from private to public
	public int						dying							= 0;

	static final int				DYING_NONE						= 0;
	static final int				DYING_SMOKE						= 1;
	static final int				DYING_DEAD						= 2;
	private long					dyingDelay						= 0L;
	private int						codeOfUnderlyingBridgeSegment	= -1;
	private static TankProperties	constr_arg1						= null;
	private static Actor			constr_arg2						= null;
	private static Point3d			p								= new Point3d();
	private static Point3d			p1								= new Point3d();
	private static Orient			o								= new Orient();
	private static Vector3f			n								= new Vector3f();
	private static Vector3d			tmpv							= new Vector3d();
	private NetMsgFiltered			outCommand						= new NetMsgFiltered();

	public static class SPAWN implements UnitSpawn
	{
		public Class			cls;
		public TankProperties	proper;

		private static float getF(SectFile sectfile, String string, String string_0_, float f, float f_1_)
		{
			float f_2_ = sectfile.get(string, string_0_, -9865.345F);
			if (f_2_ == -9865.345F || f_2_ < f || f_2_ > f_1_)
			{
				if (f_2_ == -9865.345F)
					System.out.println("Tank: Parameter [" + string + "]:<" + string_0_ + "> " + "not found");
				else
					System.out.println("Tank: Value of [" + string + "]:<" + string_0_ + "> (" + f_2_ + ")" + " is out of range (" + f + ";" + f_1_ + ")");
				throw new RuntimeException("Can't set property");
			}
			return f_2_;
		}

		private static String getS(SectFile sectfile, String string, String string_3_)
		{
			String string_4_ = sectfile.get(string, string_3_);
			if (string_4_ == null || string_4_.length() <= 0)
			{
				System.out.print("Tank: Parameter [" + string + "]:<" + string_3_ + "> ");
				System.out.println(string_4_ == null ? "not found" : "is empty");
				throw new RuntimeException("Can't set property");
			}
			return string_4_;
		}

		private static String getS(SectFile sectfile, String string, String string_5_, String string_6_)
		{
			String string_7_ = sectfile.get(string, string_5_);
			if (string_7_ == null || string_7_.length() <= 0)
				return string_6_;
			return string_7_;
		}

		private static TankProperties LoadTankProperties(SectFile sectfile, String string, Class var_class)
		{
			TankProperties tankproperties = new TankProperties();
			String string_8_ = getS(sectfile, string, "PanzerType", null);
			if (string_8_ == null)
				string_8_ = "Tank";
			tankproperties.fnShotPanzer = TableFunctions.GetFunc2(string_8_ + "ShotPanzer");
			tankproperties.fnExplodePanzer = TableFunctions.GetFunc2(string_8_ + "ExplodePanzer");
			tankproperties.PANZER_TNT_TYPE = getF(sectfile, string, "PanzerSubtype", 0.0F, 100.0F);
			tankproperties.meshSummer = getS(sectfile, string, "MeshSummer");
			tankproperties.meshDesert = getS(sectfile, string, "MeshDesert", tankproperties.meshSummer);
			tankproperties.meshWinter = getS(sectfile, string, "MeshWinter", tankproperties.meshSummer);
			tankproperties.meshSummer1 = getS(sectfile, string, "MeshSummerDamage", null);
			tankproperties.meshDesert1 = getS(sectfile, string, "MeshDesertDamage", tankproperties.meshSummer1);
			tankproperties.meshWinter1 = getS(sectfile, string, "MeshWinterDamage", tankproperties.meshSummer1);
			int i = ((tankproperties.meshSummer1 == null ? 1 : 0) + (tankproperties.meshDesert1 == null ? 1 : 0) + (tankproperties.meshWinter1 == null ? 1 : 0));
			if (i != 0 && i != 3)
			{
				System.out.println("Tank: Uncomplete set of damage meshes for '" + string + "'");
				throw new RuntimeException("Can't register tank object");
			}
			tankproperties.explodeName = getS(sectfile, string, "Explode", "Tank");
			tankproperties.PANZER_BODY_FRONT = getF(sectfile, string, "PanzerBodyFront", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerBodyBack", -9865.345F) == -9865.345F)
			{
				tankproperties.PANZER_BODY_BACK = tankproperties.PANZER_BODY_FRONT;
				tankproperties.PANZER_BODY_SIDE = tankproperties.PANZER_BODY_FRONT;
				tankproperties.PANZER_BODY_TOP = tankproperties.PANZER_BODY_FRONT;
			}
			else
			{
				tankproperties.PANZER_BODY_BACK = getF(sectfile, string, "PanzerBodyBack", 0.0010F, 9.999F);
				tankproperties.PANZER_BODY_SIDE = getF(sectfile, string, "PanzerBodySide", 0.0010F, 9.999F);
				tankproperties.PANZER_BODY_TOP = getF(sectfile, string, "PanzerBodyTop", 0.0010F, 9.999F);
			}
			if (sectfile.get(string, "PanzerHead", -9865.345F) == -9865.345F)
				tankproperties.PANZER_HEAD = tankproperties.PANZER_BODY_FRONT;
			else
				tankproperties.PANZER_HEAD = getF(sectfile, string, "PanzerHead", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerHeadTop", -9865.345F) == -9865.345F)
				tankproperties.PANZER_HEAD_TOP = tankproperties.PANZER_BODY_TOP;
			else
				tankproperties.PANZER_HEAD_TOP = getF(sectfile, string, "PanzerHeadTop", 0.0010F, 9.999F);
			float f = Math.min(Math.min(tankproperties.PANZER_BODY_BACK, tankproperties.PANZER_BODY_TOP), Math.min(tankproperties.PANZER_BODY_SIDE, tankproperties.PANZER_HEAD_TOP));
			tankproperties.HITBY_MASK = f > 0.015F ? -2 : -1;
			String string_9_ = ("com.maddox.il2.objects.weapons." + getS(sectfile, string, "Gun"));
			try
			{
				tankproperties.gunClass = Class.forName(string_9_);
			}
			catch (Exception exception)
			{
				System.out.println("Tank: Can't find gun class '" + string_9_ + "'");
				throw new RuntimeException("Can't register tank object");
			}
			tankproperties.WEAPONS_MASK = Gun.getProperties(tankproperties.gunClass).weaponType;
			if (tankproperties.WEAPONS_MASK == 0)
			{
				System.out.println("Tank: Undefined weapon type in gun class '" + string_9_ + "'");
				throw new RuntimeException("Can't register tank object");
			}
			tankproperties.MAX_SHELLS = (int) getF(sectfile, string, "NumShells", -1.0F, 30000.0F);
			tankproperties.ATTACK_MAX_DISTANCE = getF(sectfile, string, "AttackMaxDistance", 6.0F, 12000.0F);
			float f_10_ = getF(sectfile, string, "HeadYawHalfRange", 0.0F, 180.0F);
			tankproperties.HEAD_YAW_RANGE.set(-f_10_, f_10_);
			tankproperties.GUN_MIN_PITCH = getF(sectfile, string, "GunMinPitch", -15.0F, 85.0F);
			tankproperties.GUN_STD_PITCH = getF(sectfile, string, "GunStdPitch", -15.0F, 89.9F);
			tankproperties.GUN_MAX_PITCH = getF(sectfile, string, "GunMaxPitch", 0.0F, 89.9F);
			tankproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, string, "HeadMaxYawSpeed", 0.1F, 999.0F);
			tankproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, string, "GunMaxPitchSpeed", 0.1F, 999.0F);
			tankproperties.DELAY_AFTER_SHOOT = getF(sectfile, string, "DelayAfterShoot", 0.0F, 999.0F);
			tankproperties.CHAINFIRE_TIME = getF(sectfile, string, "ChainfireTime", 0.0F, 600.0F);
			tankproperties.ATTACK_FAST_TARGETS = true;
			float f_11_ = sectfile.get(string, "FireFastTargets", -9865.345F);
			if (f_11_ != -9865.345F)
				tankproperties.ATTACK_FAST_TARGETS = f_11_ > 0.5F;
			float f_12_ = sectfile.get(string, "FastTargetsAngleError", -9865.345F);
			if (f_12_ <= 0.0F)
				f_12_ = 0.0F;
			else if (f_12_ >= 45.0F)
				f_12_ = 45.0F;
			tankproperties.FAST_TARGETS_ANGLE_ERROR = f_12_;
			tankproperties.STAY_WHEN_FIRE = getF(sectfile, string, "StayWhenFire", 0.0F, 1.0F) > 0.0F;
			tankproperties.SPEED_AVERAGE = KmHourToMSec(getF(sectfile, string, "SpeedAverage", 1.0F, 100.0F));
			tankproperties.SPEED_MAX = KmHourToMSec(getF(sectfile, string, "SpeedMax", 1.0F, 100.0F));
			tankproperties.SPEED_BACK = KmHourToMSec(getF(sectfile, string, "SpeedBack", 0.5F, 100.0F));
			tankproperties.ROT_SPEED_MAX = getF(sectfile, string, "RotSpeedMax", 0.1F, 800.0F);
			tankproperties.ROT_INVIS_ANG = getF(sectfile, string, "RotInvisAng", 0.0F, 360.0F);
			tankproperties.BEST_SPACE = getF(sectfile, string, "BestSpace", 0.1F, 100.0F);
			tankproperties.AFTER_COLLISION_DIST = getF(sectfile, string, "AfterCollisionDist", 0.1F, 80.0F);
			tankproperties.COMMAND_INTERVAL = getF(sectfile, string, "CommandInterval", 0.5F, 100.0F);
			tankproperties.STAY_INTERVAL = getF(sectfile, string, "StayInterval", 0.1F, 200.0F);
			tankproperties.soundMove = getS(sectfile, string, "SoundMove");
			Property.set(var_class, "iconName", "icons/" + getS(sectfile, string, "Icon") + ".mat");
			Property.set(var_class, "meshName", tankproperties.meshSummer);
			Property.set(var_class, "speed", tankproperties.SPEED_AVERAGE);
			return tankproperties;
		}

		public SPAWN(Class var_class)
		{
			try
			{
				String string = var_class.getName();
				int i = string.lastIndexOf('.');
				int i_13_ = string.lastIndexOf('$');
				if (i < i_13_)
					i = i_13_;
				String string_14_ = string.substring(i + 1);
				proper = LoadTankProperties(Statics.getTechnicsFile(), string_14_, var_class);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("Problem in tank spawn: " + var_class.getName());
			}
			cls = var_class;
			Spawn.add(cls, this);
		}

		public Class unitClass()
		{
			return cls;
		}

		public Actor unitSpawn(int i, int i_15_, Actor actor)
		{
			proper.codeName = i;
			switch (World.cur().camouflage)
			{
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
			TankGeneric tankgeneric;
			try
			{
				TankGeneric.constr_arg1 = proper;
				TankGeneric.constr_arg2 = actor;
				tankgeneric = (TankGeneric) cls.newInstance();
				TankGeneric.constr_arg1 = null;
				TankGeneric.constr_arg2 = null;
			}
			catch (Exception exception)
			{
				TankGeneric.constr_arg1 = null;
				TankGeneric.constr_arg2 = null;
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("SPAWN: Can't create Tank object [class:" + cls.getName() + "]");
				return null;
			}
			return tankgeneric;
		}
	}

	class Move extends Interpolate
	{
		public boolean tick()
		{
			if (dying != 0)
			{
				//TODO: Added by |ZUTI|: Tank is dead, remove it's front marker, if any
				zutiRefreshFrontMarker(true);

				TankGeneric.this.neverDust();
				if (dying == 2)
					return false;
				if (access$1110(TankGeneric.this) <= 0L)
				{
					TankGeneric.this.ShowExplode();
					TankGeneric.this.MakeCrush();
					return false;
				}
				if (mov.rotatCurTime > 0L)
				{
					mov.rotatCurTime--;
					float f = 1.0F - ((float) mov.rotatCurTime / (float) mov.rotatTotTime);
					pos.getAbs(TankGeneric.o);
					TankGeneric.o.setYaw(mov.angles.getDeg(f));
					if (mov.normal.z < 0.0F)
					{
						Engine.land().N(mov.srcPos.x, mov.srcPos.y, TankGeneric.n);
						TankGeneric.o.orient(TankGeneric.n);
					}
					else
						TankGeneric.o.orient(mov.normal);
					pos.setAbs(TankGeneric.o);
				}
				return true;
			}
			boolean bool = mov.moveCurTime < 0L && mov.rotatCurTime < 0L;
			if (TankGeneric.this.isNetMirror() && bool)
			{
				mov.switchToStay(30.0F);
				bool = false;
			}
			if (bool)
			{
				ChiefGround chiefground = (ChiefGround) TankGeneric.this.getOwner();
				float f = -1.0F;
				UnitMove unitmove;
				if (collisionStage == 0)
				{
					if (prop.meshName2 != null)
					{
						TankGeneric.p.x = Rnd(-0.3, 0.3);
						TankGeneric.p.y = Rnd(-0.3, 0.3);
						TankGeneric.p.z = 1.0;
						unitmove = chiefground.AskMoveCommand(actor, TankGeneric.p, obs);
					}
					else
						unitmove = chiefground.AskMoveCommand(actor, null, obs);
				}
				else if (collisionStage == 1)
				{
					obs.collision(collidee, chiefground, udata);
					collidee = null;
					float f_16_ = Rnd(-70.0F, 70.0F);
					Vector2d vector2d = Rotate(collisVector, f_16_);
					vector2d.scale((double) prop.AFTER_COLLISION_DIST * Rnd(0.87, 1.75));
					TankGeneric.p.set(vector2d.x, vector2d.y, -1.0);
					unitmove = chiefground.AskMoveCommand(actor, TankGeneric.p, obs);
					collisionStage = 2;
					f = prop.SPEED_BACK;
				}
				else
				{
					float f_17_ = Rnd(0.0F, 359.99F);
					Vector2d vector2d = Rotate(collisVector, f_17_);
					vector2d.scale((double) prop.AFTER_COLLISION_DIST * Rnd(0.2, 0.6));
					TankGeneric.p.set(vector2d.x, vector2d.y, 1.0);
					unitmove = chiefground.AskMoveCommand(actor, TankGeneric.p, obs);
					collisionStage = 0;
				}
				mov.set(unitmove, actor, prop.SPEED_MAX, f, prop.ROT_SPEED_MAX, prop.ROT_INVIS_ANG);
				if (StayWhenFire())
				{
					if (Head360())
					{
						if (aime.isInFiringMode())
							mov.switchToStay(1.3F);
					}
					else if (aime.isInAimingMode())
						mov.switchToStay(1.3F);
				}
				if (TankGeneric.this.isNetMaster())
					TankGeneric.this.send_MoveCommand(mov, f);
			}
			aime.tick_();
			if (dust != null)
				dust._setIntesity(mov.movingForward ? 1.0F : 0.0F);
			if (mov.dstPos == null)
			{
				mov.moveCurTime--;
				if (engineSFX != null && engineSTimer > 0 && --engineSTimer == 0)
					engineSFX.stop();

				//TODO: Added by |ZUTI|: Tank moved, refresh it's front marker position, if it has any
				zutiRefreshFrontMarker(false);

				return true;
			}
			if (engineSFX != null)
			{
				if (engineSTimer == 0)
				{
					engineSFX.play();
					engineSTimer = (int) SecsToTicks(Rnd(10.0F, 12.0F));
				}
				else if (engineSTimer < ticksIn8secs)
					engineSTimer = (int) SecsToTicks(Rnd(10.0F, 12.0F));
			}
			pos.getAbs(TankGeneric.o);
			boolean bool_18_ = false;
			if (mov.rotatCurTime > 0L)
			{
				mov.rotatCurTime--;
				float f = (1.0F - (float) mov.rotatCurTime / (float) mov.rotatTotTime);
				TankGeneric.o.setYaw(mov.angles.getDeg(f));
				bool_18_ = true;
				if (mov.rotatCurTime <= 0L)
				{
					mov.rotatCurTime = -1L;
					mov.rotatingInPlace = false;
				}
			}
			if (!mov.rotatingInPlace && mov.moveCurTime > 0L)
			{
				mov.moveCurTime--;
				double d = (1.0 - (double) mov.moveCurTime / (double) mov.moveTotTime);
				TankGeneric.p.x = mov.srcPos.x * (1.0 - d) + mov.dstPos.x * d;
				TankGeneric.p.y = mov.srcPos.y * (1.0 - d) + mov.dstPos.y * d;
				if (mov.normal.z < 0.0F)
				{
					TankGeneric.p.z = (Engine.land().HQ(TankGeneric.p.x, TankGeneric.p.y) + (double) HeightAboveLandSurface());
					Engine.land().N(TankGeneric.p.x, TankGeneric.p.y, TankGeneric.n);
				}
				else
					TankGeneric.p.z = mov.srcPos.z * (1.0 - d) + mov.dstPos.z * d;
				bool_18_ = false;
				pos.setAbs(TankGeneric.p);
				if (mov.moveCurTime <= 0L)
					mov.moveCurTime = -1L;
			}
			if (mov.normal.z < 0.0F)
			{
				if (bool_18_)
					Engine.land().N(mov.srcPos.x, mov.srcPos.y, TankGeneric.n);
				TankGeneric.o.orient(TankGeneric.n);
			}
			else
				TankGeneric.o.orient(mov.normal);
			pos.setAbs(TankGeneric.o);

			//TODO: Added by |ZUTI|: Tank moved, refresh it's front marker position, if it has any
			zutiRefreshFrontMarker(false);

			return true;
		}
	}

	class Mirror extends ActorNet
	{
		NetMsgFiltered	out	= new NetMsgFiltered();

		private boolean handleGuaranted(NetMsgInput netmsginput) throws IOException
		{
			byte i = netmsginput.readByte();
			if (isMirrored())
			{
				int i_19_ = 0;
				if (i == 68 || i == 65)
					i_19_ = 1;
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, i_19_);
				post(netmsgguaranted);
			}
			Point3d point3d = readPackedPos(netmsginput);
			Orient orient = readPackedOri(netmsginput);
			float f = readPackedAng(netmsginput);
			float f_20_ = readPackedAng(netmsginput);
			TankGeneric.this.setPosition(point3d, orient, f, f_20_);
			mov.switchToStay(20.0F);
			switch (i)
			{
				case 73:
				case 105:
					//TODO: added by |ZUTI|: Tank died, remove it's front marker position, if it has any
					zutiRefreshFrontMarker(true);

					if (i == 105)
						TankGeneric.this.DieMirror(null, false);
				break;
				case 65:
				{
					com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
					Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
					TankGeneric.this.doAbsoluteDeath(actor);
					break;
				}
				case 67:
					TankGeneric.this.doCollisionDeath();
				break;
				case 68:
					if (dying == 0)
					{
						//TODO: Added by |ZUTI|: Tank died, remove it's front marker position, if it has any
						zutiRefreshFrontMarker(true);

						com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
						Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
						TankGeneric.this.DieMirror(actor, true);
					}
				break;
				default:
					System.out.println("TankGeneric: Unknown G message (" + i + ")");
					return false;
			}
			return true;
		}

		private boolean handleNonguaranted(NetMsgInput netmsginput) throws IOException
		{
			byte i = netmsginput.readByte();
			switch (i)
			{
				case 68:
					out.unLockAndSet(netmsginput, 1);
					out.setIncludeTime(false);
					postRealTo(Message.currentRealTime(), masterChannel(), out);
				break;
				case 84:
				{
					if (isMirrored())
					{
						out.unLockAndSet(netmsginput, 1);
						out.setIncludeTime(false);
						postReal(Message.currentRealTime(), out);
					}
					com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
					Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
					int i_21_ = netmsginput.readUnsignedByte();
					TankGeneric.this.Track_Mirror(actor, i_21_);
					break;
				}
				case 70:
				{
					if (isMirrored())
					{
						out.unLockAndSet(netmsginput, 1);
						out.setIncludeTime(true);
						postReal(Message.currentRealTime(), out);
					}
					com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
					Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
					float f = netmsginput.readFloat();
					float f_22_ = (0.0010F * (float) (Message.currentGameTime() - Time.current()) + f);
					int i_23_ = netmsginput.readUnsignedByte();
					TankGeneric.this.Fire_Mirror(actor, i_23_, f_22_);
					break;
				}
				case 83:
					if (isMirrored())
					{
						out.unLockAndSet(netmsginput, 0);
						out.setIncludeTime(false);
						postReal(Message.currentRealTime(), out);
					}
					mov.switchToStay(10.0F);
				break;
				case 77:
				case 109:
				{
					boolean bool = i == 77;
					if (isMirrored())
					{
						out.unLockAndSet(netmsginput, 0);
						out.setIncludeTime(true);
						postReal(Message.currentRealTime(), out);
					}
					Point3d point3d = readPackedPos(netmsginput);
					Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
					vector3f.z = readPackedNormal(netmsginput);
					if (vector3f.z >= 0.0F)
					{
						vector3f.x = readPackedNormal(netmsginput);
						vector3f.y = readPackedNormal(netmsginput);
						float f = vector3f.length();
						if (f > 0.0010F)
							vector3f.scale(1.0F / f);
						else
							vector3f.set(0.0F, 0.0F, 1.0F);
					}
					int i_24_ = netmsginput.readUnsignedShort();
					float f = 0.0010F * (float) (Message.currentGameTime() - Time.current() + (long) i_24_);
					if (f <= 0.0F)
						f = 0.1F;
					UnitMove unitmove = new UnitMove(0.0F, point3d, f, vector3f, -1.0F);
					if (dying == 0)
						mov.set(unitmove, actor(), 2.0F * prop.SPEED_MAX, bool ? 2.0F * prop.SPEED_BACK : -1.0F, 1.3F * prop.ROT_SPEED_MAX, 1.1F * prop.ROT_INVIS_ANG);
					break;
				}
				default:
					System.out.println("TankGeneric: Unknown NG message");
					return false;
			}
			return true;
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
				return handleGuaranted(netmsginput);
			return handleNonguaranted(netmsginput);
		}

		public Mirror(Actor actor, NetChannel netchannel, int i)
		{
			super(actor, netchannel, i);
		}
	}

	class Master extends ActorNet
	{
		public Master(Actor actor)
		{
			super(actor);
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
				return false;
			byte i = netmsginput.readByte();
			switch (i)
			{
				case 68:
					if (dying == 0)
					{
						com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
						Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
						TankGeneric.this.Die(actor, true);
					}
				break;
				case 67:
					collisionDeath();
				break;
				default:
					System.out.println("TankGeneric: Unknown M message (" + i + ")");
					return false;
			}
			return true;
		}
	}

	protected static class TankProperties implements Cloneable
	{
		public int				codeName					= 0;
		public String			meshName					= "3do/tanks/NameNotSpecified.him";
		public String			meshName2					= null;
		public String			meshSummer					= null;
		public String			meshDesert					= null;
		public String			meshWinter					= null;
		public String			meshSummer1					= null;
		public String			meshDesert1					= null;
		public String			meshWinter1					= null;
		public Class			gunClass					= null;
		public String			soundMove					= "models.Tank";
		public TableFunction2	fnShotPanzer				= null;
		public TableFunction2	fnExplodePanzer				= null;
		public float			PANZER_BODY_FRONT			= 0.0010F;
		public float			PANZER_BODY_BACK			= 0.0010F;
		public float			PANZER_BODY_SIDE			= 0.0010F;
		public float			PANZER_BODY_TOP				= 0.0010F;
		public float			PANZER_HEAD					= 0.0010F;
		public float			PANZER_HEAD_TOP				= 0.0010F;
		public float			PANZER_TNT_TYPE				= 1.0F;
		public int				WEAPONS_MASK				= 4;
		public int				HITBY_MASK					= -2;
		public String			explodeName					= null;
		public int				MAX_SHELLS					= 1;
		public float			ATTACK_MAX_DISTANCE			= 1.0F;
		public float			GUN_MIN_PITCH				= -30.0F;
		public float			GUN_STD_PITCH				= -29.5F;
		public float			GUN_MAX_PITCH				= -29.0F;
		public AnglesRange		HEAD_YAW_RANGE				= new AnglesRange(-1.0F, 1.0F);
		public float			HEAD_MAX_YAW_SPEED			= 3600.0F;
		public float			GUN_MAX_PITCH_SPEED			= 300.0F;
		public float			DELAY_AFTER_SHOOT			= 0.5F;
		public float			CHAINFIRE_TIME				= 0.0F;
		public boolean			ATTACK_FAST_TARGETS			= true;
		public float			FAST_TARGETS_ANGLE_ERROR	= 0.0F;
		public boolean			STAY_WHEN_FIRE				= true;
		public float			SPEED_AVERAGE				= KmHourToMSec(1.0F);
		public float			SPEED_MAX					= KmHourToMSec(2.0F);
		public float			SPEED_BACK					= KmHourToMSec(1.0F);
		public float			ROT_SPEED_MAX				= 3.6F;
		public float			ROT_INVIS_ANG				= 360.0F;
		public float			BEST_SPACE					= 2.0F;
		public float			AFTER_COLLISION_DIST		= 0.1F;
		public float			COMMAND_INTERVAL			= 20.0F;
		public float			STAY_INTERVAL				= 30.0F;

		public Object clone()
		{
			Object object;
			try
			{
				object = super.clone();
			}
			catch (Exception exception)
			{
				return null;
			}
			return object;
		}
	}

	public int getCRC(int i)
	{
		i = super.getCRC(i);
		i = Finger.incInt(i, (double) heightAboveLandSurface);
		i = Finger.incInt(i, (double) headYaw);
		i = Finger.incInt(i, (double) gunPitch);
		i = Finger.incInt(i, collisionStage);
		i = Finger.incInt(i, dying);
		i = Finger.incInt(i, codeOfUnderlyingBridgeSegment);
		if (mov != null)
		{
			i = Finger.incInt(i, mov.rotatingInPlace);
			i = Finger.incInt(i, mov.srcPos.x);
			i = Finger.incInt(i, mov.srcPos.y);
			i = Finger.incInt(i, mov.srcPos.z);
			if (mov.dstPos != null)
			{
				i = Finger.incInt(i, mov.dstPos.x);
				i = Finger.incInt(i, mov.dstPos.y);
				i = Finger.incInt(i, mov.dstPos.z);
			}
		}
		if (aime != null)
		{
			i = Finger.incInt(i, aime.timeTot);
			i = Finger.incInt(i, aime.timeCur);
		}
		return i;
	}

	public static final double Rnd(double d, double d_25_)
	{
		return World.Rnd().nextDouble(d, d_25_);
	}

	public static final float Rnd(float f, float f_26_)
	{
		return World.Rnd().nextFloat(f, f_26_);
	}

	private boolean RndB(float f)
	{
		return World.Rnd().nextFloat(0.0F, 1.0F) < f;
	}

	public static final float KmHourToMSec(float f)
	{
		return f / 3.6F;
	}

	private static final float TicksToSecs(long l)
	{
		if (l < 0L)
			l = 0L;
		return (float) l * Time.tickLenFs();
	}

	private static final long SecsToTicks(float f)
	{
		long l = (long) (0.5 + (double) (f / Time.tickLenFs()));
		return l < 1L ? 1L : l;
	}

	public static final Vector2d Rotate(Vector2d vector2d, float f)
	{
		float f_27_ = Geom.sinDeg(f);
		float f_28_ = Geom.cosDeg(f);
		return new Vector2d(((double) f_28_ * vector2d.x - (double) f_27_ * vector2d.y), ((double) f_27_ * vector2d.x + (double) f_28_ * vector2d.y));
	}

	protected final boolean Head360()
	{
		return prop.HEAD_YAW_RANGE.fullcircle();
	}

	protected final boolean StayWhenFire()
	{
		return prop.STAY_WHEN_FIRE;
	}

	public void msgCollisionRequest(Actor actor, boolean[] bools)
	{
		if (actor instanceof BridgeSegment)
			bools[0] = false;
	}

	public void msgCollision(Actor actor, String string, String string_29_)
	{
		if (dying == 0 && !(actor instanceof Soldier) && !isNetMirror() && collisionStage == 0 && !aime.isInFiringMode())
		{
			mov.switchToAsk();
			collisionStage = 1;
			collidee = actor;
			Point3d point3d = pos.getAbsPoint();
			Point3d point3d_30_ = actor.pos.getAbsPoint();
			collisVector.set(point3d.x - point3d_30_.x, point3d.y - point3d_30_.y);
			if (collisVector.length() >= 1.0E-6)
				collisVector.normalize();
			else
			{
				float f = Rnd(0.0F, 359.99F);
				collisVector.set((double) Geom.sinDeg(f), (double) Geom.cosDeg(f));
			}
			((ChiefGround) getOwner()).CollisionOccured(this, actor);
		}
	}

	public void msgShot(Shot shot)
	{
		shot.bodyMaterial = 2;
		if (dying == 0 && (!isNetMirror() || !shot.isMirage()) && !(shot.power <= 0.0F))
		{
			if (shot.powerType == 1)
			{
				if (!RndB(0.07692308F))
					Die(shot.initiator, false);
			}
			else
			{
				float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK,
						prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP);
				f *= Rnd(0.93F, 1.07F);
				float f_31_ = prop.fnShotPanzer.Value(shot.power, f);
				if (f_31_ < 1000.0F && (f_31_ <= 1.0F || RndB(1.0F / f_31_)))
					Die(shot.initiator, false);
			}
		}
	}

	public static boolean splintersKill(Explosion explosion, TableFunction2 tablefunction2, float f, float f_32_, ActorMesh actormesh, float f_33_, float f_34_, float f_35_, float f_36_, float f_37_,
			float f_38_, float f_39_, float f_40_)
	{
		if (explosion.power <= 0.0F)
			return false;
		actormesh.pos.getAbs(p);
		float[] fs = new float[2];
		explosion.computeSplintersHit(p, actormesh.mesh().collisionR(), f_33_, fs);
		fs[0] *= f * 0.85F + (1.0F - f) * 1.15F;
		int i = (int) fs[0];
		if (i <= 0)
			return false;
		Vector3d vector3d = new Vector3d(p.x - explosion.p.x, p.y - explosion.p.y, p.z - explosion.p.z);
		double d = vector3d.length();
		if (d < 0.0010000000474974513)
			vector3d.set(1.0, 0.0, 0.0);
		else
			vector3d.scale(1.0 / d);
		float f_41_ = Shot.panzerThickness(actormesh.pos.getAbsOrient(), vector3d, false, f_35_, f_36_, f_37_, f_38_, f_39_, f_40_);
		float f_42_ = Shot.panzerThickness(actormesh.pos.getAbsOrient(), vector3d, true, f_35_, f_36_, f_37_, f_38_, f_39_, f_40_);
		int i_43_ = (int) ((float) i * f_34_ + 0.5F);
		int i_44_ = i - i_43_;
		if (i_44_ < 0)
			i_44_ = 0;
		if (explosion != null)
		{
			/* empty */
		}
		float f_45_ = 0.015F * fs[1] * fs[1] * 0.5F;
		float f_46_ = tablefunction2.Value(f_45_, f_41_);
		float f_47_ = tablefunction2.Value(f_45_, f_42_);
		if (i_44_ > 0 && f_46_ <= 1.0F || i_43_ > 0 && f_47_ <= 1.0F)
			return true;
		float f_48_ = 0.0F;
		if (f_46_ < 1000.0F)
		{
			float f_49_ = 1.0F / f_46_;
			while (i_44_-- > 0)
				f_48_ += (1.0F - f_48_) * f_49_;
		}
		if (f_47_ < 1000.0F)
		{
			float f_50_ = 1.0F / f_47_;
			while (i_43_-- > 0)
				f_48_ += (1.0F - f_48_) * f_50_;
		}
		return f_48_ > 0.0010F && f_48_ >= f_32_;
	}

	public void msgExplosion(Explosion explosion)
	{
		if (dying == 0 && (!isNetMirror() || !explosion.isMirage()) && !(explosion.power <= 0.0F))
		{
			int i = explosion.powerType;
			if (explosion != null)
			{
				/* empty */
			}
			if (i == 1)
			{
				if (splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK,
						prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP))
					Die(explosion.initiator, false);
			}
			else
			{
				int i_51_ = explosion.powerType;
				if (explosion != null)
				{
					/* empty */
				}
				if (i_51_ == 2 && explosion.chunkName != null)
					Die(explosion.initiator, false);
				else
				{
					float f;
					if (explosion.chunkName != null)
						f = 0.5F * explosion.power;
					else
						f = explosion.receivedTNTpower(this);
					f *= Rnd(0.95F, 1.05F);
					float f_52_ = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
					if (f_52_ < 1000.0F && (f_52_ <= 1.0F || RndB(1.0F / f_52_)))
						Die(explosion.initiator, true);
				}
			}
		}
	}

	private void neverDust()
	{
		if (dust != null)
		{
			dust._finish();
			dust = null;
		}
	}

	private void RunSmoke(float f, float f_53_)
	{
		boolean bool = RndB(f);
		String string = bool ? "SmokeHead" : "SmokeEngine";
		Explosions.runByName("_TankSmoke_", this, string, "", Rnd(f_53_, f_53_ * 1.6F));
	}

	private void ShowExplode()
	{
		Explosions.runByName(prop.explodeName, this, "", "", -1.0F);
	}

	private void MakeCrush()
	{
		dying = 2;
		Point3d point3d = pos.getAbsPoint();
		long l = (long) (point3d.x % 2.1 * 221.0 + point3d.y % 3.1 * 211.0 * 211.0);
		RangeRandom rangerandom = new RangeRandom(l);
		float[] fs = new float[3];
		float[] fs_54_ = new float[3];
		fs[0] = fs[1] = fs[2] = 0.0F;
		fs_54_[0] = fs_54_[1] = fs_54_[2] = 0.0F;
		fs_54_[0] = headYaw + rangerandom.nextFloat(-45.0F, 45.0F);
		fs_54_[1] = rangerandom.nextFloat(-3.0F, 0.0F);
		fs_54_[2] = rangerandom.nextFloat(-9.0F, 9.0F);
		fs[2] = -rangerandom.nextFloat(0.0F, 0.1F);
		hierMesh().chunkSetLocate("Head", fs, fs_54_);
		fs[0] = fs[1] = fs[2] = 0.0F;
		fs_54_[0] = fs_54_[1] = fs_54_[2] = 0.0F;
		fs_54_[0] = -(prop.GUN_MIN_PITCH - rangerandom.nextFloat(6.0F, 10.0F));
		fs_54_[1] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(14.0F, 25.0F));
		hierMesh().chunkSetLocate("Gun", fs, fs_54_);
		fs[0] = fs[1] = fs[2] = 0.0F;
		fs_54_[0] = fs_54_[1] = fs_54_[2] = 0.0F;
		fs_54_[1] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(1.0F, 5.0F));
		fs_54_[2] = ((rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(7.0F, 13.0F));
		hierMesh().chunkSetLocate("Body", fs, fs_54_);
		engineSFX = null;
		engineSTimer = 99999999;
		breakSounds();
		neverDust();
		if (prop.meshName2 == null)
		{
			mesh().makeAllMaterialsDarker(0.22F, 0.35F);
			heightAboveLandSurface2 = heightAboveLandSurface;
			point3d.z -= (double) rangerandom.nextFloat(0.3F, 0.6F);
		}
		else
		{
			setMesh(prop.meshName2);
			heightAboveLandSurface2 = 0.0F;
			int i = mesh().hookFind("Ground_Level");
			if (i != -1)
			{
				Matrix4d matrix4d = new Matrix4d();
				mesh().hookMatrix(i, matrix4d);
				heightAboveLandSurface2 = (float) -matrix4d.m23;
			}
			point3d.z += (double) (heightAboveLandSurface2 - heightAboveLandSurface);
		}
		pos.setAbs(point3d);
		pos.reset();
	}

	private void Die(Actor actor, boolean bool)
	{
		if (isNetMirror())
			send_DeathRequest(actor);
		else
		{
			if (aime != null)
			{
				aime.forgetAll();
				aime = null;
			}
			if (gun != null)
			{
				ObjState.destroy(gun);
				gun = null;
			}
			collisionStage = 1;
			int i = ((ChiefGround) getOwner()).getCodeOfBridgeSegment(this);
			if (i >= 0)
			{
				if (BridgeSegment.isEncodedSegmentDamaged(i))
				{
					absoluteDeath(actor);
					return;
				}
				LongBridge.AddTraveller(i, this);
				codeOfUnderlyingBridgeSegment = i;
			}
			((ChiefGround) getOwner()).Detach(this, actor);
			World.onActorDied(this, actor);
			if (isNet() || prop.meshName2 == null)
				bool = true;
			if (!bool)
				bool = RndB(0.35F);
			if (bool)
			{
				ShowExplode();
				RunSmoke(0.3F, 15.0F);
				if (isNetMaster())
				{
					send_DeathCommand(actor);
					Point3d point3d = simplifyPos(pos.getAbsPoint());
					Orient orient = simplifyOri(pos.getAbsOrient());
					float f = simplifyAng(headYaw);
					float f_55_ = simplifyAng(gunPitch);
					setPosition(point3d, orient, f, f_55_);
				}
				MakeCrush();
			}
			else
			{
				dying = 1;
				dyingDelay = SecsToTicks(Rnd(6.0F, 12.0F));
				mov.switchToRotate(this, (RndB(0.5F) ? 1.0F : -1.0F) * Rnd(70.0F, 170.0F), prop.ROT_SPEED_MAX);
				RunSmoke(0.2F, 17.0F);
			}
		}
	}

	private void DieMirror(Actor actor, boolean bool)
	{
		if (!isNetMirror())
			System.out.println("Internal error in TankGeneric: DieMirror");
		if (aime != null)
		{
			aime.forgetAll();
			aime = null;
		}
		if (gun != null)
		{
			ObjState.destroy(gun);
			gun = null;
		}
		collisionStage = 1;
		((ChiefGround) getOwner()).Detach(this, actor);
		World.onActorDied(this, actor);
		if (bool)
		{
			ShowExplode();
			RunSmoke(0.3F, 15.0F);
		}
		MakeCrush();
	}

	public void destroy()
	{
		if (dust != null && !dust.isDestroyed())
			dust._finish();
		dust = null;
		engineSFX = null;
		engineSTimer = 99999999;
		breakSounds();
		if (codeOfUnderlyingBridgeSegment >= 0)
			LongBridge.DelTraveller(codeOfUnderlyingBridgeSegment, this);
		if (aime != null)
		{
			aime.forgetAll();
			aime = null;
		}
		if (gun != null)
		{
			ObjState.destroy(gun);
			gun = null;
		}
		super.destroy();
	}

	private void setPosition(Point3d point3d, Orient orient, float f, float f_56_)
	{
		headYaw = f;
		gunPitch = f_56_;
		hierMesh().chunkSetAngles("Head", headYaw, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Gun", -gunPitch, 0.0F, 0.0F);
		pos.setAbs(point3d, orient);
		pos.reset();
	}

	public Object getSwitchListener(Message message)
	{
		return this;
	}

	protected TankGeneric()
	{
		this(constr_arg1, constr_arg2);
	}

	public void setMesh(String string)
	{
		super.setMesh(string);
		if (!Config.cur.b3dgunners)
			mesh().materialReplaceToNull("Pilot1");
	}

	private TankGeneric(TankProperties tankproperties, Actor actor)
	{
		super(tankproperties.meshName);
		prop = tankproperties;
		collide(true);
		drawing(true);
		setOwner(actor);
		codeName = tankproperties.codeName;
		setName(actor.name() + codeName);
		setArmy(actor.getArmy());
		if (mesh().hookFind("SmokeHead") < 0)
			System.out.println("Tank " + this.getClass().getName() + ": hook SmokeHead not found");
		if (mesh().hookFind("SmokeEngine") < 0)
			System.out.println("Tank " + this.getClass().getName() + ": hook SmokeEngine not found");
		if (mesh().hookFind("Ground_Level") < 0)
			System.out.println("Tank " + this.getClass().getName() + ": hook Ground_Level not found");
		heightAboveLandSurface = 0.0F;
		int i = mesh().hookFind("Ground_Level");
		if (i != -1)
		{
			Matrix4d matrix4d = new Matrix4d();
			mesh().hookMatrix(i, matrix4d);
			heightAboveLandSurface = (float) -matrix4d.m23;
		}
		HookNamed hooknamed;
		HookNamed hooknamed_58_;
		try
		{
			hooknamed = new HookNamed(this, "DustL");
			hooknamed_58_ = new HookNamed(this, "DustR");
		}
		catch (Exception exception)
		{
			hooknamed = hooknamed_58_ = null;
		}
		if (hooknamed == null || hooknamed_58_ == null)
		{
			dust = null;
		}
		else
		{
			Loc loc = new Loc();
			Loc loc_59_ = new Loc();
			Loc loc_60_ = new Loc();
			hooknamed.computePos(this, loc, loc_59_);
			hooknamed_58_.computePos(this, loc, loc_60_);
			Loc loc_61_ = new Loc();
			loc_61_.interpolate(loc_59_, loc_60_, 0.5F);
			dust = Eff3DActor.New(this, null, loc_61_, 1.0F, "Effects/Smokes/TankDust.eff", -1.0F);
			if (dust != null)
				dust._setIntesity(0.0F);
		}
		if (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 101)
		{
			i = Mission.cur().getUnitNetIdRemote(this);
			NetChannel netchannel = Mission.cur().getNetMasterChannel();
			if (netchannel == null)
				net = new Master(this);
			else if (i != 0)
				net = new Mirror(this, netchannel, i);
		}
		gun = null;
		try
		{
			gun = (Gun) prop.gunClass.newInstance();
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			System.out.println("Tank: Can't create gun '" + prop.gunClass.getName() + "'");
		}
		gun.set(this, "Gun");
		gun.loadBullets(isNetMirror() ? -1 : prop.MAX_SHELLS);
		headYaw = 0.0F;
		gunPitch = prop.GUN_STD_PITCH;
		hierMesh().chunkSetAngles("Head", headYaw, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Gun", -gunPitch, 0.0F, 0.0F);
		aime = new Aim(this, isNetMirror());
	}

	private void send_DeathRequest(Actor actor)
	{
		if (isNetMirror() && !(net.masterChannel() instanceof NetChannelInStream))
		{
			try
			{
				NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
				netmsgfiltered.writeByte(68);
				netmsgfiltered.writeNetObj(actor == null ? (ActorNet) null : actor.net);
				netmsgfiltered.setIncludeTime(false);
				net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void send_CollisionDeathRequest()
	{
		if (isNetMirror() && !(net.masterChannel() instanceof NetChannelInStream))
		{
			try
			{
				NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
				netmsgfiltered.writeByte(67);
				netmsgfiltered.setIncludeTime(false);
				net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void send_FireCommand(Actor actor, int i, float f)
	{
		if (isNetMaster() && net.isMirrored() && (Actor.isValid(actor) && actor.isNet()))
		{
			i &= 0xff;
			if (f < 0.0F)
			{
				try
				{
					outCommand.unLockAndClear();
					outCommand.writeByte(84);
					outCommand.writeNetObj(actor.net);
					outCommand.writeByte(i);
					outCommand.setIncludeTime(false);
					net.post(Time.current(), outCommand);
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
					exception.printStackTrace();
				}
			}
			else
			{
				try
				{
					outCommand.unLockAndClear();
					outCommand.writeByte(70);
					outCommand.writeFloat(f);
					outCommand.writeNetObj(actor.net);
					outCommand.writeByte(i);
					outCommand.setIncludeTime(true);
					net.post(Time.current(), outCommand);
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
					exception.printStackTrace();
				}
			}
		}
	}

	private void send_AnByteAndPoseCommand(boolean bool, Actor actor, int i)
	{
		if (isNetMaster() && net.isMirrored())
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try
			{
				netmsgguaranted.writeByte(i);
				sendPose(netmsgguaranted);
				if (bool)
					netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void send_DeathCommand(Actor actor)
	{
		send_AnByteAndPoseCommand(true, actor, 68);
	}

	private void send_AbsoluteDeathCommand(Actor actor)
	{
		send_AnByteAndPoseCommand(true, actor, 65);
	}

	private void send_CollisionDeathCommand()
	{
		send_AnByteAndPoseCommand(false, null, 67);
	}

	private void send_MoveCommand(Moving moving, float f)
	{
		if (isNetMaster() && net.isMirrored() && (moving.moveCurTime >= 0L || moving.rotatCurTime >= 0L))
		{
			try
			{
				outCommand.unLockAndClear();
				if (moving.dstPos == null || moving.moveTotTime <= 0L || moving.normal == null)
				{
					outCommand.writeByte(83);
					outCommand.setIncludeTime(false);
					net.post(Time.current(), outCommand);
				}
				else
				{
					if (f > 0.0F)
						outCommand.writeByte(77);
					else
						outCommand.writeByte(109);
					outCommand.write(packPos(moving.dstPos));
					outCommand.writeByte(packNormal(moving.normal.z));
					if (moving.normal.z >= 0.0F)
					{
						outCommand.writeByte(packNormal(moving.normal.x));
						outCommand.writeByte(packNormal(moving.normal.y));
					}
					int i = (int) ((long) Time.tickLen() * moving.moveTotTime);
					if (i >= 65536)
						i = 65535;
					outCommand.writeShort(i);
					outCommand.setIncludeTime(true);
					net.post(Time.current(), outCommand);
				}
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	static int packAng(float f)
	{
		return 0xff & (int) (f * 256.0F / 360.0F);
	}

	static int packNormal(float f)
	{
		f++;
		f *= 0.5F;
		f *= 254.0F;
		int i = (int) (f + 0.5F);
		if (i < 0)
			i = 0;
		if (i > 254)
			i = 254;
		return i - 127;
	}

	static byte[] packPos(Point3d point3d)
	{
		byte[] is = new byte[8];
		int i = (int) (point3d.x * 20.0 + 0.5);
		int i_62_ = (int) (point3d.y * 20.0 + 0.5);
		int i_63_ = (int) (point3d.z * 10.0 + 0.5);
		is[0] = (byte) (i >> 0 & 0xff);
		is[1] = (byte) (i >> 8 & 0xff);
		is[2] = (byte) (i >> 16 & 0xff);
		is[3] = (byte) (i_62_ >> 0 & 0xff);
		is[4] = (byte) (i_62_ >> 8 & 0xff);
		is[5] = (byte) (i_62_ >> 16 & 0xff);
		is[6] = (byte) (i_63_ >> 0 & 0xff);
		is[7] = (byte) (i_63_ >> 8 & 0xff);
		return is;
	}

	static byte[] packOri(Orient orient)
	{
		byte[] is = new byte[3];
		int i = (int) (orient.getYaw() * 256.0F / 360.0F);
		int i_64_ = (int) (orient.getPitch() * 256.0F / 360.0F);
		int i_65_ = (int) (orient.getRoll() * 256.0F / 360.0F);
		is[0] = (byte) (i & 0xff);
		is[1] = (byte) (i_64_ & 0xff);
		is[2] = (byte) (i_65_ & 0xff);
		return is;
	}

	static float unpackAng(int i)
	{
		return (float) i * 360.0F / 256.0F;
	}

	static float unpackNormal(int i)
	{
		return (float) i / 127.0F;
	}

	static Point3d unpackPos(byte[] is)
	{
		int i = (((is[2] & 0xff) << 16) + ((is[1] & 0xff) << 8) + ((is[0] & 0xff) << 0));
		int i_66_ = (((is[5] & 0xff) << 16) + ((is[4] & 0xff) << 8) + ((is[3] & 0xff) << 0));
		int i_67_ = ((is[7] & 0xff) << 8) + ((is[6] & 0xff) << 0);
		return new Point3d((double) i * 0.05, (double) i_66_ * 0.05, (double) i_67_ * 0.1);
	}

	static Orient unpackOri(byte[] is)
	{
		int i = is[0] & 0xff;
		int i_68_ = is[1] & 0xff;
		int i_69_ = is[2] & 0xff;
		Orient orient = new Orient();
		orient.setYPR((float) i * 360.0F / 256.0F, (float) i_68_ * 360.0F / 256.0F, (float) i_69_ * 360.0F / 256.0F);
		return orient;
	}

	static float simplifyAng(float f)
	{
		return unpackAng(packAng(f));
	}

	static Point3d simplifyPos(Point3d point3d)
	{
		return unpackPos(packPos(point3d));
	}

	static Orient simplifyOri(Orient orient)
	{
		return unpackOri(packOri(orient));
	}

	static float readPackedAng(NetMsgInput netmsginput) throws IOException
	{
		return unpackAng(netmsginput.readByte());
	}

	static float readPackedNormal(NetMsgInput netmsginput) throws IOException
	{
		return unpackNormal(netmsginput.readByte());
	}

	static Point3d readPackedPos(NetMsgInput netmsginput) throws IOException
	{
		byte[] is = new byte[8];
		netmsginput.read(is);
		return unpackPos(is);
	}

	static Orient readPackedOri(NetMsgInput netmsginput) throws IOException
	{
		byte[] is = new byte[3];
		netmsginput.read(is);
		return unpackOri(is);
	}

	private void sendPose(NetMsgGuaranted netmsgguaranted) throws IOException
	{
		netmsgguaranted.write(packPos(pos.getAbsPoint()));
		netmsgguaranted.write(packOri(pos.getAbsOrient()));
		netmsgguaranted.writeByte(packAng(headYaw));
		netmsgguaranted.writeByte(packAng(gunPitch));
	}

	public void netFirstUpdate(NetChannel netchannel) throws IOException
	{
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		int i = dying == 0 ? 73 : 105;
		netmsgguaranted.writeByte(i);
		sendPose(netmsgguaranted);
		net.postTo(netchannel, netmsgguaranted);
	}

	public void startMove()
	{
		if (!interpEnd("move"))
		{
			mov = new Moving();
			if (aime != null)
			{
				aime.forgetAll();
				aime = null;
			}
			aime = new Aim(this, isNetMirror());
			collisionStage = 0;
			interpPut(new Move(), "move", Time.current(), null);
			engineSFX = newSound(prop.soundMove, true);
			engineSTimer = (int) SecsToTicks(Rnd(5.0F, 7.0F));
		}
	}

	public void forceReaskMove()
	{
		if (!isNetMirror() && collisionStage == 0 && dying == 0 && (mov != null && mov.normal != null))
			mov.switchToAsk();
	}

	public UnitData GetUnitData()
	{
		return udata;
	}

	public float HeightAboveLandSurface()
	{
		return heightAboveLandSurface;
	}

	public float SpeedAverage()
	{
		return prop.SPEED_AVERAGE;
	}

	public float BestSpace()
	{
		return prop.BEST_SPACE;
	}

	public float CommandInterval()
	{
		return prop.COMMAND_INTERVAL;
	}

	public float StayInterval()
	{
		return prop.STAY_INTERVAL;
	}

	public UnitInPackedForm Pack()
	{
		int i = Finger.Int(this.getClass().getName());
		int i_70_ = 0;
		return new UnitInPackedForm(codeName, i, i_70_, SpeedAverage(), BestSpace(), WeaponsMask(), HitbyMask());
	}

	public int WeaponsMask()
	{
		return prop.WEAPONS_MASK;
	}

	public int HitbyMask()
	{
		return prop.HITBY_MASK;
	}

	public int chooseBulletType(BulletProperties[] bulletpropertieses)
	{
		if (dying != 0)
			return -1;
		if (bulletpropertieses.length == 1)
			return 0;
		if (bulletpropertieses.length <= 0)
			return -1;
		if (this instanceof TgtTank)
		{
			if (bulletpropertieses[0].cumulativePower > 0.0F)
				return 0;
			if (bulletpropertieses[1].cumulativePower > 0.0F)
				return 1;
			if (bulletpropertieses[0].power <= 0.0F)
				return 0;
			if (bulletpropertieses[1].power <= 0.0F)
				return 1;
		}
		else
		{
			if (bulletpropertieses[0].power <= 0.0F)
				return 0;
			if (bulletpropertieses[1].power <= 0.0F)
				return 1;
			if (bulletpropertieses[0].cumulativePower > 0.0F)
				return 0;
			if (bulletpropertieses[1].cumulativePower > 0.0F)
				return 1;
		}
		if (bulletpropertieses[0].powerType == 1)
			return 0;
		if (bulletpropertieses[1].powerType == 1)
			return 1;
		if (bulletpropertieses[0].powerType == 0)
			return 1;
		return 0;
	}

	public int chooseShotpoint(BulletProperties bulletproperties)
	{
		if (dying != 0)
			return -1;
		return 0;
	}

	public boolean getShotpointOffset(int i, Point3d point3d)
	{
		if (dying != 0)
			return false;
		if (i != 0)
			return false;
		if (point3d != null)
			point3d.set(0.0, 0.0, 0.0);
		return true;
	}

	public float AttackMaxDistance()
	{
		return prop.ATTACK_MAX_DISTANCE;
	}

	public void absoluteDeath(Actor actor)
	{
		if (!isNetMirror())
		{
			if (isNetMaster())
				send_AbsoluteDeathCommand(actor);
			doAbsoluteDeath(actor);
		}
	}

	private void doAbsoluteDeath(Actor actor)
	{
		ChiefGround chiefground = (ChiefGround) getOwner();
		if (chiefground != null)
			chiefground.Detach(this, actor);
		if (!getDiedFlag())
			World.onActorDied(this, actor);
		Explosions.Tank_ExplodeCollapse(pos.getAbsPoint());
		destroy();
	}

	public boolean unmovableInFuture()
	{
		return dying != 0;
	}

	public void collisionDeath()
	{
		if (isNetMirror())
			send_CollisionDeathRequest();
		else
		{
			if (isNetMaster())
				send_CollisionDeathCommand();
			doCollisionDeath();
		}
	}

	private void doCollisionDeath()
	{
		ChiefGround chiefground = (ChiefGround) getOwner();
		boolean bool = (chiefground == null && codeOfUnderlyingBridgeSegment >= 0 || (chiefground != null && chiefground.getCodeOfBridgeSegment(this) >= 0));
		if (chiefground != null)
			chiefground.Detach(this, null);
		if (bool)
			Explosions.Tank_ExplodeCollapse(pos.getAbsPoint());
		else
			Explosions.Tank_ExplodeCollapse(pos.getAbsPoint());
		destroy();
	}

	public float futurePosition(float f, Point3d point3d)
	{
		pos.getAbs(point3d);
		if (f <= 0.0F)
			return 0.0F;
		if (mov.moveCurTime < 0L && mov.rotatCurTime < 0L)
			return 0.0F;
		float f_71_ = TicksToSecs(mov.moveCurTime);
		if (mov.dstPos == null)
		{
			if (f_71_ >= f)
				return f;
			return f_71_;
		}
		float f_72_ = 0.0F;
		if (mov.rotatingInPlace)
		{
			f_72_ = TicksToSecs(mov.rotatCurTime);
			if (f_72_ >= f)
				return f;
		}
		if (f_71_ <= 0.0F)
			return f_72_;
		if (f_72_ + f_71_ <= f)
		{
			point3d.set(mov.dstPos);
			return f_72_ + f_71_;
		}
		Point3d point3d_73_ = new Point3d();
		point3d_73_.set(mov.dstPos);
		double d = (double) ((f - f_72_) / f_71_);
		p.x = point3d.x * (1.0 - d) + point3d_73_.x * d;
		p.y = point3d.y * (1.0 - d) + point3d_73_.y * d;
		if (mov.normal.z < 0.0F)
			p.z = (Engine.land().HQ(p.x, p.y) + (double) HeightAboveLandSurface());
		else
			p.z = point3d.z * (1.0 - d) + point3d_73_.z * d;
		point3d.set(p);
		return f;
	}

	public float getReloadingTime(Aim aim)
	{
		if (isNetMirror())
			return 1.0F;
		if (gun.haveBullets())
			return prop.DELAY_AFTER_SHOOT;
		gun.loadBullets(prop.MAX_SHELLS);
		return 120.0F;
	}

	public float chainFireTime(Aim aim)
	{
		return (prop.CHAINFIRE_TIME <= 0.0F ? 0.0F : prop.CHAINFIRE_TIME * Rnd(0.75F, 1.25F));
	}

	public float probabKeepSameEnemy(Actor actor)
	{
		return Head360() ? 0.8F : 0.0F;
	}

	public float minTimeRelaxAfterFight()
	{
		return Head360() ? 0.0F : 10.0F;
	}

	public void gunStartParking(Aim aim)
	{
		aim.setRotationForParking(headYaw, gunPitch, 0.0F, prop.GUN_STD_PITCH, prop.HEAD_YAW_RANGE, prop.HEAD_MAX_YAW_SPEED, prop.GUN_MAX_PITCH_SPEED);
	}

	public void gunInMove(boolean bool, Aim aim)
	{
		float f = aim.t();
		if (Head360() || bool || !aim.bodyRotation)
		{
			headYaw = aim.anglesYaw.getDeg(f);
			gunPitch = aim.anglesPitch.getDeg(f);
			hierMesh().chunkSetAngles("Head", headYaw, 0.0F, 0.0F);
			hierMesh().chunkSetAngles("Gun", -gunPitch, 0.0F, 0.0F);
			pos.inValidate(false);
            return;
		}
        float f1 = aim.anglesYaw.getDeg(f);
        pos.getAbs(o);
        o.setYaw(f1);
        if (mov != null && mov.normal != null) {
            if (mov.normal.z < 0.0F) {
                com.maddox.il2.engine.Engine.land().N(mov.srcPos.x, mov.srcPos.y, n);
                o.orient(n);
            } else {
                o.orient(mov.normal);
            }
        }
        pos.setAbs(o);
        gunPitch = aim.anglesPitch.getDeg(f);
        hierMesh().chunkSetAngles("Gun", -gunPitch, 0.0F, 0.0F);
        pos.inValidate(false);
	}

	public Actor findEnemy(Aim aim)
	{
		if (isNetMirror())
			return null;
		Actor actor = null;
		ChiefGround chiefground = (ChiefGround) getOwner();
		if (chiefground != null)
		{
			if (chiefground.getCodeOfBridgeSegment(this) >= 0)
				return null;
			actor = chiefground.GetNearestEnemy(pos.getAbsPoint(), (double) AttackMaxDistance(), WeaponsMask(), (prop.ATTACK_FAST_TARGETS ? -1.0F : KmHourToMSec(100.0F)));
		}
		if (actor == null)
			return null;
		BulletProperties bulletproperties = null;
		if (gun.prop != null)
		{
			int i = ((Prey) actor).chooseBulletType(gun.prop.bullet);
			if (i < 0)
				return null;
			bulletproperties = gun.prop.bullet[i];
		}
		int i = ((Prey) actor).chooseShotpoint(bulletproperties);
		if (i < 0)
			return null;
		aim.shotpoint_idx = i;
		return actor;
	}

	public boolean enterToFireMode(int i, Actor actor, float f, Aim aim)
	{
		if (i == 1 || !Head360() && aim.bodyRotation)
		{
			if (collisionStage != 0)
				return false;
			if (StayWhenFire())
				mov.switchToStay(1.5F);
		}
		if (!isNetMirror())
			send_FireCommand(actor, aim.shotpoint_idx, i == 0 ? -1.0F : f);
		return true;
	}

	private void Track_Mirror(Actor actor, int i)
	{
		if (dying == 0 && actor != null && aime != null)
			aime.passive_StartFiring(0, actor, i, 0.0F);
	}

	private void Fire_Mirror(Actor actor, int i, float f)
	{
		if (dying == 0 && actor != null && aime != null)
		{
			if (f <= 0.2F)
				f = 0.2F;
			if (f >= 15.0F)
				f = 15.0F;
			aime.passive_StartFiring(1, actor, i, f);
		}
	}

	public int targetGun(Aim aim, Actor actor, float f, boolean bool)
	{
		if (!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
			return 0;
		if (gun instanceof CannonMidrangeGeneric)
		{
			int i = ((Prey) actor).chooseBulletType(gun.prop.bullet);
			if (i < 0)
				return 0;
			((CannonMidrangeGeneric) gun).setBulletType(i);
		}
		boolean bool_75_ = ((Prey) actor).getShotpointOffset(aim.shotpoint_idx, p1);
		if (!bool_75_)
			return 0;
		float f_76_ = f * Rnd(0.8F, 1.2F);
		if (!Aimer.Aim((BulletAimer) gun, actor, this, f_76_, p1, null))
			return 0;
		Point3d point3d = new Point3d();
		Aimer.GetPredictedTargetPosition(point3d);
		Point3d point3d_77_ = Aimer.GetHunterFirePoint();
		float f_78_ = 0.19F;
		double d = point3d.distance(point3d_77_);
		double d_79_ = point3d.z;
		point3d.sub(point3d_77_);
		point3d.scale(Rnd(0.95, 1.05));
		point3d.add(point3d_77_);
		if (f_76_ > 0.0010F)
		{
			Point3d point3d_80_ = new Point3d();
			actor.pos.getAbs(point3d_80_);
			tmpv.sub(point3d, point3d_80_);
			double d_81_ = tmpv.length();
			if (d_81_ > 0.0010)
			{
				float f_82_ = (float) d_81_ / f_76_;
				if (f_82_ > 200.0F)
					f_82_ = 200.0F;
				float f_83_ = f_82_ * 0.02F;
				point3d_80_.sub(point3d_77_);
				double d_84_ = (point3d_80_.x * point3d_80_.x + point3d_80_.y * point3d_80_.y + point3d_80_.z * point3d_80_.z);
				if (d_84_ > 0.01)
				{
					float f_85_ = (float) tmpv.dot(point3d_80_);
					f_85_ /= (float) (d_81_ * Math.sqrt(d_84_));
					f_85_ = (float) Math.sqrt((double) (1.0F - f_85_ * f_85_));
					f_83_ *= 0.4F + 0.6F * f_85_;
				}
				int i = Mission.curCloudsType();
				if (i >= 3)
				{
					float f_86_ = i >= 5 ? 250.0F : 500.0F;
					float f_87_ = (float) (d / (double) f_86_);
					if (f_87_ > 1.0F)
					{
						if (f_87_ > 10.0F)
							return 0;
						f_87_ = (f_87_ - 1.0F) / 9.0F * 2.0F + 1.0F;
						f_83_ *= f_87_;
					}
				}
				if (i >= 3 && d_79_ > (double) Mission.curCloudsHeight())
					f_83_ *= 1.25F;
				f_78_ += f_83_;
			}
		}
		if (World.Sun().ToSun.z < -0.15F)
		{
			float f_88_ = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
			if (f_88_ >= 1.0F)
				f_88_ = 1.0F;
			if (actor instanceof Aircraft && Time.current() - ((Aircraft) actor).tmSearchlighted < 1000L)
				f_88_ = 0.0F;
			f_78_ += 12.0F * f_88_;
		}
		float f_89_ = (float) actor.getSpeed(null);
		float f_90_ = 83.333336F;
		f_89_ = f_89_ >= f_90_ ? 1.0F : f_89_ / f_90_;
		f_78_ += f_89_ * prop.FAST_TARGETS_ANGLE_ERROR;
		Vector3d vector3d = new Vector3d();
		if (!((BulletAimer) gun).FireDirection(point3d_77_, point3d, vector3d))
			return 0;
		float f_91_;
		float f_92_;
		float f_93_;
		if (bool)
		{
			f_91_ = 99999.0F;
			f_92_ = 99999.0F;
			f_93_ = 99999.0F;
		}
		else
		{
			f_91_ = prop.HEAD_MAX_YAW_SPEED;
			f_92_ = prop.GUN_MAX_PITCH_SPEED;
			f_93_ = prop.ROT_SPEED_MAX;
		}
		Orient orient = pos.getAbs().getOrient();
		f_90_ = 0.0F;
		if (!Head360())
			f_90_ = orient.getYaw();
		int i = aim.setRotationForTargeting(this, orient, point3d_77_, headYaw, gunPitch, vector3d, f_78_, f_76_, prop.HEAD_YAW_RANGE, prop.GUN_MIN_PITCH, prop.GUN_MAX_PITCH, f_91_, f_92_, f_93_);
		if (!Head360() && i != 0 && aim.bodyRotation)
			aim.anglesYaw.rotateDeg(f_90_);
		return i;
	}

	public void singleShot(Aim aim)
	{
		if (StayWhenFire())
			mov.switchToStay(1.5F);
		gun.shots(1);
	}

	public void startFire(Aim aim)
	{
		if (StayWhenFire())
			mov.switchToStay(1.5F);
		gun.shots(-1);
	}

	public void continueFire(Aim aim)
	{
		if (StayWhenFire())
			mov.switchToStay(1.5F);
	}

	public void stopFire(Aim aim)
	{
		if (StayWhenFire())
			mov.switchToStay(1.5F);
		gun.shots(0);
	}

	static long access$1110(TankGeneric tankgeneric)
	{
		return tankgeneric.dyingDelay--;
	}

	//TODO: |ZUTI| methods and variables
	//------------------------------------------------------------------
	private ArrayList	zutiFrontMarkers	= null;
	private Point3d		zutiPosition		= null;

	private void zutiRefreshFrontMarker(boolean died)
	{
		//System.out.println("Refreshing tank front marker: " + Time.current());

		if (zutiFrontMarkers == null)
			return;

		Marker marker = (Marker) zutiFrontMarkers.get(0);

		if (marker == null)
			return;

		if (dying == 0)
		{
			zutiPosition = this.pos.getAbsPoint();
			marker.x = zutiPosition.x;
			marker.y = zutiPosition.y;

			com.maddox.il2.ai.Front.setMarkersChanged();
		}
		else
		{
			ZutiSupportMethods.removeFrontMarker(marker, 1);
			zutiFrontMarkers.clear();
			zutiFrontMarkers = null;
		}

		if (!zutiHasFrontMarkerAssigned())
			return;

		if (!died && (Time.current() - ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK) < ZutiSupportMethods.BASE_CAPTURING_INTERVAL)
			return;

		//Check if new front position has overrun some home bases
		ZutiSupportMethods.checkIfAnyBornPlaceWasOverrun();

		//Set current time as last refresh time
		ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK = Time.current();
	}

	//Called from: Mission, Front
	public boolean zutiHasFrontMarkerAssigned()
	{
		if (zutiFrontMarkers == null || zutiFrontMarkers.size() == 0)
			return false;
		
		return true;
	}

	public void zutiResetFrontMarkers()
	{
		if( zutiFrontMarkers != null )
			zutiFrontMarkers.clear();
	}
	
	//Called from: Mission, Front
	public void zutiAddFrontMarker(Marker marker)
	{
		if (zutiFrontMarkers == null)
			zutiFrontMarkers = new ArrayList(1);

		zutiFrontMarkers.add(marker);
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}