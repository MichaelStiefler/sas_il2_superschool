/*410 class*/
package com.maddox.il2.objects.vehicles.cars;

import java.io.IOException;
import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.ai.ground.Coward;
import com.maddox.il2.ai.ground.Moving;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.StaticObstacle;
import com.maddox.il2.ai.ground.UnitData;
import com.maddox.il2.ai.ground.UnitInPackedForm;
import com.maddox.il2.ai.ground.UnitInterface;
import com.maddox.il2.ai.ground.UnitMove;
import com.maddox.il2.ai.ground.UnitSpawn;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Finger;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.util.TableFunction2;

public abstract class CarGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Coward, Prey, Obstacle, UnitInterface
{
	private CarProperties prop = null;
	private int codeName;
	private float heightAboveLandSurface;
	public UnitData udata = new UnitData();
	private Moving mov = new Moving();
	protected SoundFX engineSFX = null;
	protected int engineSTimer = 9999999;
	protected int ticksIn8secs = (int) (8.0F / Time.tickConstLenFs());
	private int collisionStage = 0;
	static final int COLLIS_NO_COLLISION = 0;
	static final int COLLIS_JUST_COLLIDED = 1;
	static final int COLLIS_MOVING_FROM_COLLISION = 2;
	private Vector2d collisVector = new Vector2d();
	private Actor collidee;
	private StaticObstacle obs = new StaticObstacle();
	private long timeHumanLaunch;
	private int dying = 0;
	static final int DYING_NONE = 0;
	static final int DYING_DEAD = 1;
	private int codeOfUnderlyingBridgeSegment = -1;
	private static CarProperties constr_arg1 = null;
	private static Actor constr_arg2 = null;
	private static Point3d p = new Point3d();
	private static Orient o = new Orient();
	private static Vector3f n = new Vector3f();
	private NetMsgFiltered outCommand = new NetMsgFiltered();

	public static class SPAWN implements UnitSpawn
	{
		public Class cls;
		public CarProperties proper;

		private static float getF(SectFile sectfile, String string, String string_0_, float f, float f_1_)
		{
			float f_2_ = sectfile.get(string, string_0_, -9865.345F);
			if (f_2_ == -9865.345F || f_2_ < f || f_2_ > f_1_)
			{
				if (f_2_ == -9865.345F)
					System.out.println("Car: Parameter [" + string + "]:<" + string_0_ + "> " + "not found");
				else
					System.out.println("Car: Value of [" + string + "]:<" + string_0_ + "> (" + f_2_ + ")" + " is out of range (" + f + ";" + f_1_ + ")");
				throw new RuntimeException("Can't set property");
			}
			return f_2_;
		}

		private static String getS(SectFile sectfile, String string, String string_3_)
		{
			String string_4_ = sectfile.get(string, string_3_);
			if (string_4_ == null || string_4_.length() <= 0)
			{
				System.out.print("Car: Parameter [" + string + "]:<" + string_3_ + "> ");
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

		private static CarProperties LoadCarProperties(SectFile sectfile, String string, Class var_class)
		{
			CarProperties carproperties = new CarProperties();
			String string_8_ = getS(sectfile, string, "PanzerType", null);
			if (string_8_ == null)
				string_8_ = "Tank";
			carproperties.fnShotPanzer = TableFunctions.GetFunc2(string_8_ + "ShotPanzer");
			carproperties.fnExplodePanzer = TableFunctions.GetFunc2(string_8_ + "ExplodePanzer");
			carproperties.PANZER_TNT_TYPE = getF(sectfile, string, "PanzerSubtype", 0.0F, 100.0F);
			carproperties.meshSummer = getS(sectfile, string, "MeshSummer");
			carproperties.meshDesert = getS(sectfile, string, "MeshDesert", carproperties.meshSummer);
			carproperties.meshWinter = getS(sectfile, string, "MeshWinter", carproperties.meshSummer);
			carproperties.meshSummer1 = getS(sectfile, string, "MeshSummerDamage", null);
			carproperties.meshDesert1 = getS(sectfile, string, "MeshDesertDamage", carproperties.meshSummer1);
			carproperties.meshWinter1 = getS(sectfile, string, "MeshWinterDamage", carproperties.meshSummer1);
			int i = ((carproperties.meshSummer1 == null ? 1 : 0) + (carproperties.meshDesert1 == null ? 1 : 0) + (carproperties.meshWinter1 == null ? 1 : 0));
			if (i != 0 && i != 3)
			{
				System.out.println("Car: Uncomplete set of damage meshes for '" + string + "'");
				throw new RuntimeException("Can't register car object");
			}
			carproperties.PANZER_BODY_FRONT = getF(sectfile, string, "PanzerBodyFront", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerBodyBack", -9865.345F) == -9865.345F)
			{
				carproperties.PANZER_BODY_BACK = carproperties.PANZER_BODY_FRONT;
				carproperties.PANZER_BODY_SIDE = carproperties.PANZER_BODY_FRONT;
				carproperties.PANZER_BODY_TOP = carproperties.PANZER_BODY_FRONT;
			}
			else
			{
				carproperties.PANZER_BODY_BACK = getF(sectfile, string, "PanzerBodyBack", 0.0010F, 9.999F);
				carproperties.PANZER_BODY_SIDE = getF(sectfile, string, "PanzerBodySide", 0.0010F, 9.999F);
				carproperties.PANZER_BODY_TOP = getF(sectfile, string, "PanzerBodyTop", 0.0010F, 9.999F);
			}
			carproperties.explodeName = getS(sectfile, string, "Explode", "Car");
			float f = Math.min(Math.min(carproperties.PANZER_BODY_BACK, carproperties.PANZER_BODY_TOP), Math.min(carproperties.PANZER_BODY_SIDE, carproperties.PANZER_BODY_FRONT));
			carproperties.HITBY_MASK = f > 0.015F ? -2 : -1;
			carproperties.SPEED_AVERAGE = KmHourToMSec(getF(sectfile, string, "SpeedAverage", 1.0F, 100.0F));
			carproperties.SPEED_MAX = KmHourToMSec(getF(sectfile, string, "SpeedMax", 1.0F, 100.0F));
			carproperties.SPEED_BACK = KmHourToMSec(getF(sectfile, string, "SpeedBack", 0.5F, 100.0F));
			carproperties.ROT_SPEED_MAX = getF(sectfile, string, "RotSpeedMax", 0.1F, 800.0F);
			carproperties.ROT_INVIS_ANG = getF(sectfile, string, "RotInvisAng", 0.0F, 360.0F);
			carproperties.BEST_SPACE = getF(sectfile, string, "BestSpace", 0.1F, 100.0F);
			carproperties.AFTER_COLLISION_DIST = getF(sectfile, string, "AfterCollisionDist", 0.1F, 80.0F);
			carproperties.COMMAND_INTERVAL = getF(sectfile, string, "CommandInterval", 0.5F, 100.0F);
			carproperties.STAY_INTERVAL = getF(sectfile, string, "StayInterval", 0.1F, 200.0F);
			carproperties.soundMove = getS(sectfile, string, "SoundMove");
			if ("none".equals(carproperties.soundMove))
				carproperties.soundMove = null;
			Property.set(var_class, "meshName", carproperties.meshSummer);
			Property.set(var_class, "speed", carproperties.SPEED_AVERAGE);
			if (sectfile.get(string, "Soldiers", -9865.345F) == -9865.345F)
				carproperties.NUM_HUMANS = 0;
			else
				carproperties.NUM_HUMANS = (int) getF(sectfile, string, "Soldiers", 1.0F, 2.0F);
			return carproperties;
		}

		public SPAWN(Class var_class)
		{
			try
			{
				String string = var_class.getName();
				int i = string.lastIndexOf('.');
				int i_9_ = string.lastIndexOf('$');
				if (i < i_9_)
					i = i_9_;
				String string_10_ = string.substring(i + 1);
				proper = LoadCarProperties(Statics.getTechnicsFile(), string_10_, var_class);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("Problem in car spawn: " + var_class.getName());
			}
			cls = var_class;
			Spawn.add(cls, this);
		}

		public Class unitClass()
		{
			return cls;
		}

		public Actor unitSpawn(int i, int i_11_, Actor actor)
		{
			proper.codeName = i;
			switch (World.cur().camouflage)
			{
				case 1:
					proper.MESH0_NAME = proper.meshWinter;
					proper.MESH1_NAME = proper.meshWinter1;
				break;
				case 2:
					proper.MESH0_NAME = proper.meshDesert;
					proper.MESH1_NAME = proper.meshDesert1;
				break;
				default:
					proper.MESH0_NAME = proper.meshSummer;
					proper.MESH1_NAME = proper.meshSummer1;
			}
			CarGeneric cargeneric;
			try
			{
				CarGeneric.constr_arg1 = proper;
				CarGeneric.constr_arg2 = actor;
				cargeneric = (CarGeneric) cls.newInstance();
				CarGeneric.constr_arg1 = null;
				CarGeneric.constr_arg2 = null;
			}
			catch (Exception exception)
			{
				CarGeneric.constr_arg1 = null;
				CarGeneric.constr_arg2 = null;
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("SPAWN: Can't create Car object [class:" + cls.getName() + "]");
				return null;
			}
			return cargeneric;
		}
	}

	class Move extends Interpolate
	{
		public boolean tick()
		{
			if (dying != 0)
				return false;
			if (timeHumanLaunch > 0L && Time.current() >= timeHumanLaunch)
			{
				ChiefGround chiefground = (ChiefGround) actor.getOwner();
				if (chiefground.getCodeOfBridgeSegment((UnitInterface) actor) < 0)
				{
					LaunchHumans();
					timeHumanLaunch = Time.current() + (long) (int) Rnd(12000.0F, 17000.0F);
				}
				else
					timeHumanLaunch = Time.current() + (long) (int) Rnd(3000.0F, 5000.0F);
				timeHumanLaunch = -timeHumanLaunch;
			}
			boolean bool = mov.moveCurTime < 0L && mov.rotatCurTime < 0L;
			if (CarGeneric.this.isNetMirror() && bool)
			{
				mov.switchToStay(30.0F);
				bool = false;
			}
			if (bool)
			{
				ChiefGround chiefground = (ChiefGround) CarGeneric.this.getOwner();
				float f = -1.0F;
				UnitMove unitmove;
				if (collisionStage == 0)
					unitmove = chiefground.AskMoveCommand(actor, null, obs);
				else if (collisionStage == 1)
				{
					obs.collision(collidee, chiefground, udata);
					collidee = null;
					float f_12_ = Rnd(-70.0F, 70.0F);
					Vector2d vector2d = Rotate(collisVector, f_12_);
					vector2d.scale((double) prop.AFTER_COLLISION_DIST * Rnd(0.87, 1.75));
					CarGeneric.p.set(vector2d.x, vector2d.y, -1.0);
					unitmove = chiefground.AskMoveCommand(actor, CarGeneric.p, obs);
					collisionStage = 2;
					f = prop.SPEED_BACK;
				}
				else
				{
					float f_13_ = Rnd(0.0F, 359.99F);
					Vector2d vector2d = Rotate(collisVector, f_13_);
					vector2d.scale((double) prop.AFTER_COLLISION_DIST * Rnd(0.2, 0.6));
					CarGeneric.p.set(vector2d.x, vector2d.y, 1.0);
					unitmove = chiefground.AskMoveCommand(actor, CarGeneric.p, obs);
					collisionStage = 0;
				}
				mov.set(unitmove, actor, prop.SPEED_MAX, f, prop.ROT_SPEED_MAX, prop.ROT_INVIS_ANG);
				if (CarGeneric.this.isNetMaster())
					CarGeneric.this.send_MoveCommand(mov, f);
			}
			if (mov.dstPos == null)
			{
				mov.moveCurTime--;
				if (engineSFX != null && engineSTimer > 0 && --engineSTimer == 0)
					engineSFX.stop();
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
			pos.getAbs(CarGeneric.o);
			boolean bool_14_ = false;
			if (mov.rotatCurTime > 0L)
			{
				mov.rotatCurTime--;
				float f = (1.0F - (float) mov.rotatCurTime / (float) mov.rotatTotTime);
				CarGeneric.o.setYaw(mov.angles.getDeg(f));
				bool_14_ = true;
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
				CarGeneric.p.x = mov.srcPos.x * (1.0 - d) + mov.dstPos.x * d;
				CarGeneric.p.y = mov.srcPos.y * (1.0 - d) + mov.dstPos.y * d;
				if (mov.normal.z < 0.0F)
				{
					CarGeneric.p.z = (Engine.land().HQ(CarGeneric.p.x, CarGeneric.p.y) + (double) HeightAboveLandSurface());
					Engine.land().N(CarGeneric.p.x, CarGeneric.p.y, CarGeneric.n);
				}
				else
					CarGeneric.p.z = mov.srcPos.z * (1.0 - d) + mov.dstPos.z * d;
				bool_14_ = false;
				pos.setAbs(CarGeneric.p);
				if (mov.moveCurTime <= 0L)
					mov.moveCurTime = -1L;
			}
			if (mov.normal.z < 0.0F)
			{
				if (bool_14_)
					Engine.land().N(mov.srcPos.x, mov.srcPos.y, CarGeneric.n);
				CarGeneric.o.orient(CarGeneric.n);
			}
			else
				CarGeneric.o.orient(mov.normal);
			pos.setAbs(CarGeneric.o);
			return true;
		}
	}

	class Mirror extends ActorNet
	{
		NetMsgFiltered out = new NetMsgFiltered();

		private boolean handleGuaranted(NetMsgInput netmsginput) throws IOException
		{
			byte i = netmsginput.readByte();
			if (isMirrored())
			{
				int i_15_ = 0;
				if (i == 68 || i == 65)
					i_15_ = 1;
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, i_15_);
				post(netmsgguaranted);
			}
			Point3d point3d = readPackedPos(netmsginput);
			Orient orient = readPackedOri(netmsginput);
			CarGeneric.this.setPosition(point3d, orient);
			mov.switchToStay(20.0F);
			switch (i)
			{
				case 73:
				case 105:
					if (dying != 0)
						System.out.println("Car is dead at init stage");
					if (i == 105)
						CarGeneric.this.DieMirror(null, false);
				break;
				case 67:
					CarGeneric.this.doCollisionDeath();
				break;
				case 65:
				{
					com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
					Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
					CarGeneric.this.doAbsoluteDeath(actor);
					break;
				}
				case 68:
					if (dying == 0)
					{
						com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
						Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
						CarGeneric.this.DieMirror(actor, true);
					}
				break;
				default:
					System.out.println("CarGeneric: Unknown G message (" + i + ")");
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
					int i_16_ = netmsginput.readUnsignedShort();
					float f = 0.0010F * (float) (Message.currentGameTime() - Time.current() + (long) i_16_);
					if (f <= 0.0F)
						f = 0.1F;
					UnitMove unitmove = new UnitMove(0.0F, point3d, f, vector3f, -1.0F);
					if (dying == 0)
						mov.set(unitmove, actor(), 2.0F * prop.SPEED_MAX, bool ? 2.0F * prop.SPEED_BACK : -1.0F, 1.3F * prop.ROT_SPEED_MAX, 1.1F * prop.ROT_INVIS_ANG);
					break;
				}
				default:
					System.out.println("CarGeneric: Unknown NG message");
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
						CarGeneric.this.Die(actor);
					}
				break;
				case 67:
					collisionDeath();
				break;
				default:
					System.out.println("CarGeneric: Unknown M message (" + i + ")");
					return false;
			}
			return true;
		}
	}

	protected static class CarProperties implements Cloneable
	{
		public int codeName = 0;
		public String MESH0_NAME = "3do/cars/None.him";
		public String MESH1_NAME = null;
		public String meshSummer = null;
		public String meshDesert = null;
		public String meshWinter = null;
		public String meshSummer1 = null;
		public String meshDesert1 = null;
		public String meshWinter1 = null;
		public String soundMove = "models.Car";
		public TableFunction2 fnShotPanzer = null;
		public TableFunction2 fnExplodePanzer = null;
		public float PANZER_BODY_FRONT = 0.0010F;
		public float PANZER_BODY_BACK = 0.0010F;
		public float PANZER_BODY_SIDE = 0.0010F;
		public float PANZER_BODY_TOP = 0.0010F;
		public float PANZER_TNT_TYPE = 1.0F;
		public String explodeName = null;
		public int HITBY_MASK = -1;
		public float SPEED_AVERAGE = KmHourToMSec(1.0F);
		public float SPEED_MAX = KmHourToMSec(2.0F);
		public float SPEED_BACK = KmHourToMSec(1.0F);
		public float ROT_SPEED_MAX = 3.6F;
		public float ROT_INVIS_ANG = 360.0F;
		public float BEST_SPACE = 2.0F;
		public float AFTER_COLLISION_DIST = 0.1F;
		public float COMMAND_INTERVAL = 20.0F;
		public float STAY_INTERVAL = 30.0F;
		public int NUM_HUMANS = 0;

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

	public static final double Rnd(double d, double d_17_)
	{
		return World.Rnd().nextDouble(d, d_17_);
	}

	public static final float Rnd(float f, float f_18_)
	{
		return World.Rnd().nextFloat(f, f_18_);
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
		float f_19_ = Geom.sinDeg(f);
		float f_20_ = Geom.cosDeg(f);
		return new Vector2d(((double) f_20_ * vector2d.x - (double) f_19_ * vector2d.y), ((double) f_19_ * vector2d.x + (double) f_20_ * vector2d.y));
	}

	public void SetTimerToLaunchHumans()
	{
		if (timeHumanLaunch <= 0L)
		{
			if (timeHumanLaunch == 0L || -timeHumanLaunch <= Time.current())
				timeHumanLaunch = Time.current() + (long) (int) Rnd(500.0F, 1500.0F);
		}
	}

	public void LaunchHumans()
	{
		if (prop.NUM_HUMANS != 0)
		{
			Loc loc = new Loc();
			Loc loc_21_ = new Loc();
			loc.set(0.0, 0.0, 0.0, 170.0F - Rnd(0.0F, 130.0F), Rnd(-5.0F, 2.0F), 0.0F);
			loc_21_.set(0.0, 0.0, 0.0, 190.0F + Rnd(0.0F, 130.0F), Rnd(-5.0F, 2.0F), 0.0F);
			Loc loc_22_ = pos.getAbs();
			loc.add(loc_22_);
			loc_21_.add(loc_22_);
			if (prop.NUM_HUMANS == 1)
			{
				if (RndB(0.5F))
					loc = loc_21_;
				new Soldier(this, getArmy(), loc);
			}
			else
			{
				new Soldier(this, getArmy(), loc);
				new Soldier(this, getArmy(), loc_21_);
			}
		}
	}

	public void msgCollisionRequest(Actor actor, boolean[] bools)
	{
		if (actor instanceof BridgeSegment)
			bools[0] = false;
	}

	public void msgCollision(Actor actor, String string, String string_23_)
	{
		if (dying == 0 && !(actor instanceof Soldier) && !isNetMirror() && collisionStage == 0)
		{
			mov.switchToAsk();
			collisionStage = 1;
			collidee = actor;
			Point3d point3d = pos.getAbsPoint();
			Point3d point3d_24_ = actor.pos.getAbsPoint();
			collisVector.set(point3d.x - point3d_24_.x, point3d.y - point3d_24_.y);
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
		if (dying == 0)
		{
			if (isNetMirror() && shot.isMirage())
				SetTimerToLaunchHumans();
			else if (shot.power <= 0.0F)
				SetTimerToLaunchHumans();
			else if (shot.powerType == 1)
			{
				if (RndB(0.05F))
					SetTimerToLaunchHumans();
				else
					Die(shot.initiator);
			}
			else
			{
				float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, false, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_TOP);
				f *= Rnd(0.93F, 1.07F);
				float f_25_ = prop.fnShotPanzer.Value(shot.power, f);
				if (f_25_ < 1000.0F && (f_25_ <= 1.0F || RndB(1.0F / f_25_)))
					Die(shot.initiator);
				else
					SetTimerToLaunchHumans();
			}
		}
	}

	public void msgExplosion(Explosion explosion)
	{
		if (dying == 0)
		{
			if (isNetMirror() && explosion.isMirage())
				SetTimerToLaunchHumans();
			else if (explosion.power <= 0.0F)
				SetTimerToLaunchHumans();
			else
			{
				int i = explosion.powerType;
				if (explosion != null)
				{
					/* empty */
				}
				if (i == 1)
				{
					if (TankGeneric.splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.0F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_BODY_FRONT,
							prop.PANZER_BODY_TOP))
						Die(explosion.initiator);
					else
						SetTimerToLaunchHumans();
				}
				else
				{
					int i_26_ = explosion.powerType;
					if (explosion != null)
					{
						/* empty */
					}
					if (i_26_ == 2 && explosion.chunkName != null)
						Die(explosion.initiator);
					else
					{
						float f;
						if (explosion.chunkName != null)
							f = 0.5F * explosion.power;
						else
							f = explosion.receivedTNTpower(this);
						f *= Rnd(0.95F, 1.05F);
						float f_27_ = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
						if (f_27_ < 1000.0F && (f_27_ <= 1.0F || RndB(1.0F / f_27_)))
							Die(explosion.initiator);
						else
							SetTimerToLaunchHumans();
					}
				}
			}
		}
	}

	public void scare()
	{
		SetTimerToLaunchHumans();
	}

	private void ShowExplode(float f)
	{
		if (f > 0.0F)
			f = Rnd(f, f * 1.6F);
		Explosions.runByName(prop.explodeName, this, "Smoke", "", f);
	}

	private void ActivateMesh()
	{
		boolean bool = dying == 1;
		if (!bool)
			setMesh(prop.MESH0_NAME);
		else if (prop.MESH1_NAME == null)
		{
			setMesh(prop.MESH0_NAME);
			mesh().makeAllMaterialsDarker(0.22F, 0.35F);
		}
		else
			setMesh(prop.MESH1_NAME);
		int i = mesh().hookFind("Ground_Level");
		float f = heightAboveLandSurface;
		if (i != -1)
		{
			Matrix4d matrix4d = new Matrix4d();
			mesh().hookMatrix(i, matrix4d);
			heightAboveLandSurface = (float) -matrix4d.m23;
		}
		else
		{
			float[] fs = new float[6];
			mesh().getBoundBox(fs);
			heightAboveLandSurface = -fs[2];
		}
		if (bool)
		{
			Point3d point3d = pos.getAbsPoint();
			point3d.z += (double) (heightAboveLandSurface - f);
			pos.setAbs(point3d);
			pos.reset();
		}
	}

	private void MakeCrush()
	{
		engineSFX = null;
		engineSTimer = 99999999;
		breakSounds();
		dying = 1;
		ActivateMesh();
	}

	private void Die(Actor actor)
	{
		if (isNetMirror())
			send_DeathRequest(actor);
		else
		{
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
			ShowExplode(16.0F);
			if (isNetMaster())
			{
				send_DeathCommand(actor);
				Point3d point3d = simplifyPos(pos.getAbsPoint());
				Orient orient = simplifyOri(pos.getAbsOrient());
				setPosition(point3d, orient);
			}
			MakeCrush();
		}
	}

	private void DieMirror(Actor actor, boolean bool)
	{
		if (!isNetMirror())
			System.out.println("Internal error in CarGeneric: DieMirror");
		collisionStage = 1;
		((ChiefGround) getOwner()).Detach(this, actor);
		World.onActorDied(this, actor);
		if (bool)
			ShowExplode(16.0F);
		MakeCrush();
	}

	public void destroy()
	{
		engineSFX = null;
		engineSTimer = 99999999;
		breakSounds();
		if (codeOfUnderlyingBridgeSegment >= 0)
			LongBridge.DelTraveller(codeOfUnderlyingBridgeSegment, this);
		super.destroy();
	}

	private void setPosition(Point3d point3d, Orient orient)
	{
		pos.setAbs(point3d, orient);
		pos.reset();
	}

	public Object getSwitchListener(Message message)
	{
		return this;
	}

	protected CarGeneric()
	{
		this(constr_arg1, constr_arg2);
	}

	public void setMesh(String string)
	{
		super.setMesh(string);
		if (!Config.cur.b3dgunners)
			mesh().materialReplaceToNull("Pilot1");
	}

	public CarGeneric(CarProperties carproperties, Actor actor)
	{
		super(carproperties.MESH0_NAME);
		prop = carproperties;
		timeHumanLaunch = -(Time.current() + (long) (int) Rnd(2000.0F, 8000.0F));
		collide(true);
		drawing(true);
		setOwner(actor);
		codeName = carproperties.codeName;
		setName(actor.name() + codeName);
		setArmy(actor.getArmy());
		new HookNamed(this, "Smoke");
		ActivateMesh();
		int i = Mission.cur().getUnitNetIdRemote(this);
		NetChannel netchannel = Mission.cur().getNetMasterChannel();
		if (netchannel == null)
			net = new Master(this);
		else if (i != 0)
			net = new Mirror(this, netchannel, i);

		//TODO: Added by |ZUTI|:
		//----------------------------------------------
		if( actor instanceof ChiefGround )
		{
			((ChiefGround)actor).zutiUnitsList.add(this);
		}
		//----------------------------------------------
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
				netmsgfiltered.unLockAndClear();
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
		int i_28_ = (int) (point3d.y * 20.0 + 0.5);
		int i_29_ = (int) (point3d.z * 10.0 + 0.5);
		is[0] = (byte) (i >> 0 & 0xff);
		is[1] = (byte) (i >> 8 & 0xff);
		is[2] = (byte) (i >> 16 & 0xff);
		is[3] = (byte) (i_28_ >> 0 & 0xff);
		is[4] = (byte) (i_28_ >> 8 & 0xff);
		is[5] = (byte) (i_28_ >> 16 & 0xff);
		is[6] = (byte) (i_29_ >> 0 & 0xff);
		is[7] = (byte) (i_29_ >> 8 & 0xff);
		return is;
	}

	static byte[] packOri(Orient orient)
	{
		byte[] is = new byte[3];
		int i = (int) (orient.getYaw() * 256.0F / 360.0F);
		int i_30_ = (int) (orient.getPitch() * 256.0F / 360.0F);
		int i_31_ = (int) (orient.getRoll() * 256.0F / 360.0F);
		is[0] = (byte) (i & 0xff);
		is[1] = (byte) (i_30_ & 0xff);
		is[2] = (byte) (i_31_ & 0xff);
		return is;
	}

	static float unpackNormal(int i)
	{
		return (float) i / 127.0F;
	}

	static Point3d unpackPos(byte[] is)
	{
		int i = (((is[2] & 0xff) << 16) + ((is[1] & 0xff) << 8) + ((is[0] & 0xff) << 0));
		int i_32_ = (((is[5] & 0xff) << 16) + ((is[4] & 0xff) << 8) + ((is[3] & 0xff) << 0));
		int i_33_ = ((is[7] & 0xff) << 8) + ((is[6] & 0xff) << 0);
		return new Point3d((double) i * 0.05, (double) i_32_ * 0.05, (double) i_33_ * 0.1);
	}

	static Orient unpackOri(byte[] is)
	{
		int i = is[0] & 0xff;
		int i_34_ = is[1] & 0xff;
		int i_35_ = is[2] & 0xff;
		Orient orient = new Orient();
		orient.setYPR((float) i * 360.0F / 256.0F, (float) i_34_ * 360.0F / 256.0F, (float) i_35_ * 360.0F / 256.0F);
		return orient;
	}

	static Point3d simplifyPos(Point3d point3d)
	{
		return unpackPos(packPos(point3d));
	}

	static Orient simplifyOri(Orient orient)
	{
		return unpackOri(packOri(orient));
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
		int i_36_ = 0;
		return new UnitInPackedForm(codeName, i, i_36_, SpeedAverage(), BestSpace(), 0, HitbyMask());
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
		if (bulletpropertieses[0].power <= 0.0F)
			return 0;
		if (bulletpropertieses[1].power <= 0.0F)
			return 1;
		if (bulletpropertieses[0].cumulativePower > 0.0F)
			return 0;
		if (bulletpropertieses[1].cumulativePower > 0.0F)
			return 1;
		if (bulletpropertieses[0].powerType == 1)
			return 0;
		if (bulletpropertieses[1].powerType == 1)
			return 1;
		if (bulletpropertieses[0].powerType == 2)
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
		Explosions.Car_ExplodeCollapse(pos.getAbsPoint());
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
			Explosions.Car_ExplodeCollapse(pos.getAbsPoint());
		else
			Explosions.Car_ExplodeCollapse(pos.getAbsPoint());
		destroy();
	}

	public float futurePosition(float f, Point3d point3d)
	{
		pos.getAbs(point3d);
		if (f <= 0.0F)
			return 0.0F;
		if (mov.moveCurTime < 0L && mov.rotatCurTime < 0L)
			return 0.0F;
		float f_37_ = TicksToSecs(mov.moveCurTime);
		if (mov.dstPos == null)
		{
			if (f_37_ >= f)
				return f;
			return f_37_;
		}
		float f_38_ = 0.0F;
		if (mov.rotatingInPlace)
		{
			f_38_ = TicksToSecs(mov.rotatCurTime);
			if (f_38_ >= f)
				return f;
		}
		if (f_37_ <= 0.0F)
			return f_38_;
		if (f_38_ + f_37_ <= f)
		{
			point3d.set(mov.dstPos);
			return f_38_ + f_37_;
		}
		Point3d point3d_39_ = new Point3d();
		point3d_39_.set(mov.dstPos);
		double d = (double) ((f - f_38_) / f_37_);
		p.x = point3d.x * (1.0 - d) + point3d_39_.x * d;
		p.y = point3d.y * (1.0 - d) + point3d_39_.y * d;
		if (mov.normal.z < 0.0F)
			p.z = (Engine.land().HQ(p.x, p.y) + (double) HeightAboveLandSurface());
		else
			p.z = point3d.z * (1.0 - d) + point3d_39_.z * d;
		point3d.set(p);
		return f;
	}
}