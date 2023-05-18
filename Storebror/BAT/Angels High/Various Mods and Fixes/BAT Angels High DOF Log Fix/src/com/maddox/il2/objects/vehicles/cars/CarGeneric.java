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
import com.maddox.il2.objects.ObjectsLogLevel;
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

public abstract class CarGeneric extends ActorHMesh
    implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Coward, Prey, Obstacle, UnitInterface
{
    public static class SPAWN
        implements UnitSpawn
    {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1)
        {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if(f2 == -9865.345F || f2 < f || f2 > f1)
            {
                if(f2 == -9865.345F)
                    System.out.println("Car: Parameter [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Car: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
                throw new RuntimeException("Can't set property");
            } else
            {
                return f2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1)
        {
            String s2 = sectfile.get(s, s1);
            if(s2 == null || s2.length() <= 0)
            {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    System.out.print("Car: Parameter [" + s + "]:<" + s1 + "> ");
                    System.out.println(s2 != null ? "is empty" : "not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Car \"" + s + "\" is not (correctly) declared in technics.ini file!");
                }
                return null;
                // ---
            } else
            {
                return s2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1, String s2)
        {
            String s3 = sectfile.get(s, s1);
            if(s3 == null || s3.length() <= 0)
                return s2;
            else
                return s3;
        }

        private static CarProperties LoadCarProperties(SectFile sectfile, String s, Class class1)
        {
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            String checkMesh = getS(sectfile, s, "MeshSummer");
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (checkMesh == null || checkMesh.length() == 0)) return null;
            // TODO: ---
            CarProperties carproperties = new CarProperties();
            String s1 = getS(sectfile, s, "PanzerType", null);
            if(s1 == null)
                s1 = "Tank";
            carproperties.fnShotPanzer = TableFunctions.GetFunc2(s1 + "ShotPanzer");
            carproperties.fnExplodePanzer = TableFunctions.GetFunc2(s1 + "ExplodePanzer");
            carproperties.PANZER_TNT_TYPE = getF(sectfile, s, "PanzerSubtype", 0.0F, 100F);
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//          carproperties.meshSummer = getS(sectfile, s, "MeshSummer");
            carproperties.meshSummer = checkMesh;
            // TODO: ---
            carproperties.meshDesert = getS(sectfile, s, "MeshDesert", carproperties.meshSummer);
            carproperties.meshWinter = getS(sectfile, s, "MeshWinter", carproperties.meshSummer);
            carproperties.meshSummer1 = getS(sectfile, s, "MeshSummerDamage", null);
            carproperties.meshDesert1 = getS(sectfile, s, "MeshDesertDamage", carproperties.meshSummer1);
            carproperties.meshWinter1 = getS(sectfile, s, "MeshWinterDamage", carproperties.meshSummer1);
            float f = (carproperties.meshSummer1 != null ? 0 : 1) + (carproperties.meshDesert1 != null ? 0 : 1) + (carproperties.meshWinter1 != null ? 0 : 1);
            if(f != 0 && f != 3)
            {
                System.out.println("Car: Uncomplete set of damage meshes for '" + s + "'");
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                    throw new RuntimeException("Can't register car object");
                return null;
                // ---
            }
            carproperties.PANZER_BODY_FRONT = getF(sectfile, s, "PanzerBodyFront", 0.001F, 9.999F);
            if(sectfile.get(s, "PanzerBodyBack", -9865.345F) == -9865.345F)
            {
                carproperties.PANZER_BODY_BACK = carproperties.PANZER_BODY_FRONT;
                carproperties.PANZER_BODY_SIDE = carproperties.PANZER_BODY_FRONT;
                carproperties.PANZER_BODY_TOP = carproperties.PANZER_BODY_FRONT;
            } else
            {
                carproperties.PANZER_BODY_BACK = getF(sectfile, s, "PanzerBodyBack", 0.001F, 9.999F);
                carproperties.PANZER_BODY_SIDE = getF(sectfile, s, "PanzerBodySide", 0.001F, 9.999F);
                carproperties.PANZER_BODY_TOP = getF(sectfile, s, "PanzerBodyTop", 0.001F, 9.999F);
            }
            carproperties.explodeName = getS(sectfile, s, "Explode", "Car");
            f = Math.min(Math.min(carproperties.PANZER_BODY_BACK, carproperties.PANZER_BODY_TOP), Math.min(carproperties.PANZER_BODY_SIDE, carproperties.PANZER_BODY_FRONT));
            carproperties.HITBY_MASK = f <= 0.015F ? -1 : -2;
            carproperties.SPEED_AVERAGE = CarGeneric.KmHourToMSec(getF(sectfile, s, "SpeedAverage", 1.0F, 100F));
            carproperties.SPEED_MAX = CarGeneric.KmHourToMSec(getF(sectfile, s, "SpeedMax", 1.0F, 100F));
            carproperties.SPEED_BACK = CarGeneric.KmHourToMSec(getF(sectfile, s, "SpeedBack", 0.5F, 100F));
            carproperties.ROT_SPEED_MAX = getF(sectfile, s, "RotSpeedMax", 0.1F, 800F);
            carproperties.ROT_INVIS_ANG = getF(sectfile, s, "RotInvisAng", 0.0F, 360F);
            carproperties.BEST_SPACE = getF(sectfile, s, "BestSpace", 0.1F, 100F);
            carproperties.AFTER_COLLISION_DIST = getF(sectfile, s, "AfterCollisionDist", 0.1F, 80F);
            carproperties.COMMAND_INTERVAL = getF(sectfile, s, "CommandInterval", 0.5F, 100F);
            carproperties.STAY_INTERVAL = getF(sectfile, s, "StayInterval", 0.1F, 200F);
            carproperties.soundMove = getS(sectfile, s, "SoundMove");
            if("none".equals(carproperties.soundMove))
                carproperties.soundMove = null;
            Property.set(class1, "meshName", carproperties.meshSummer);
            Property.set(class1, "speed", carproperties.SPEED_AVERAGE);
            if(sectfile.get(s, "Soldiers", -9865.345F) == -9865.345F)
                carproperties.NUM_HUMANS = 0;
            else
                carproperties.NUM_HUMANS = (int)getF(sectfile, s, "Soldiers", 1.0F, 2.0F);
            return carproperties;
        }

        public Class unitClass()
        {
            return cls;
        }

        public Actor unitSpawn(int i, int j, Actor actor)
        {
            proper.codeName = i;
            switch(World.cur().camouflage)
            {
            case 1: // '\001'
                proper.MESH0_NAME = proper.meshWinter;
                proper.MESH1_NAME = proper.meshWinter1;
                break;

            case 2: // '\002'
                proper.MESH0_NAME = proper.meshDesert;
                proper.MESH1_NAME = proper.meshDesert1;
                break;

            default:
                proper.MESH0_NAME = proper.meshSummer;
                proper.MESH1_NAME = proper.meshSummer1;
                break;
            }
            CarGeneric cargeneric = null;
            try
            {
                CarGeneric.constr_arg1 = proper;
                CarGeneric.constr_arg2 = actor;
                cargeneric = (CarGeneric)cls.newInstance();
                CarGeneric.constr_arg1 = null;
                CarGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
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

        public Class cls;
        public CarProperties proper;

        public SPAWN(Class class1)
        {
            try
            {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if(i < j)
                    i = j;
                String s1 = s.substring(i + 1);
                proper = LoadCarProperties(Statics.getTechnicsFile(), s1, class1);
                
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL && proper == null) return;
                // TODO: ---
                
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in car spawn: " + class1.getName());
            }
            cls = class1;
            Spawn.add(cls, this);
        }
    }

    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(dying != 0)
                return false;
            if(timeHumanLaunch > 0L && Time.current() >= timeHumanLaunch)
            {
                ChiefGround chiefground = (ChiefGround)actor.getOwner();
                if(chiefground.getCodeOfBridgeSegment((UnitInterface)actor) < 0)
                {
                    LaunchHumans();
                    timeHumanLaunch = Time.current() + (long)(int)CarGeneric.Rnd(12000F, 17000F);
                } else
                {
                    timeHumanLaunch = Time.current() + (long)(int)CarGeneric.Rnd(3000F, 5000F);
                }
                timeHumanLaunch = -timeHumanLaunch;
            }
            boolean flag = mov.moveCurTime < 0L && mov.rotatCurTime < 0L;
            if(isNetMirror() && flag)
            {
                mov.switchToStay(30F);
                flag = false;
            }
            if(flag)
            {
                ChiefGround chiefground1 = (ChiefGround)getOwner();
                float f1 = -1F;
                UnitMove unitmove;
                if(collisionStage == 0)
                    unitmove = chiefground1.AskMoveCommand(actor, null, obs);
                else
                if(collisionStage == 1)
                {
                    obs.collision(collidee, chiefground1, udata);
                    collidee = null;
                    float f2 = CarGeneric.Rnd(-70F, 70F);
                    Vector2d vector2d = CarGeneric.Rotate(collisVector, f2);
                    vector2d.scale((double)prop.AFTER_COLLISION_DIST * CarGeneric.Rnd(0.87D, 1.75D));
                    CarGeneric.p.set(vector2d.x, vector2d.y, -1D);
                    unitmove = chiefground1.AskMoveCommand(actor, CarGeneric.p, obs);
                    collisionStage = 2;
                    f1 = prop.SPEED_BACK;
                } else
                {
                    float f3 = CarGeneric.Rnd(0.0F, 359.99F);
                    Vector2d vector2d1 = CarGeneric.Rotate(collisVector, f3);
                    vector2d1.scale((double)prop.AFTER_COLLISION_DIST * CarGeneric.Rnd(0.20000000000000001D, 0.59999999999999998D));
                    CarGeneric.p.set(vector2d1.x, vector2d1.y, 1.0D);
                    unitmove = chiefground1.AskMoveCommand(actor, CarGeneric.p, obs);
                    collisionStage = 0;
                }
                mov.set(unitmove, actor, prop.SPEED_MAX, f1, prop.ROT_SPEED_MAX, prop.ROT_INVIS_ANG);
                if(isNetMaster())
                    send_MoveCommand(mov, f1);
            }
            if(mov.dstPos == null)
            {
                mov.moveCurTime--;
                if(engineSFX != null && engineSTimer > 0 && --engineSTimer == 0)
                    engineSFX.stop();
                return true;
            }
            if(engineSFX != null)
                if(engineSTimer == 0)
                {
                    engineSFX.play();
                    engineSTimer = (int)CarGeneric.SecsToTicks(CarGeneric.Rnd(10F, 12F));
                } else
                if(engineSTimer < ticksIn8secs)
                    engineSTimer = (int)CarGeneric.SecsToTicks(CarGeneric.Rnd(10F, 12F));
            pos.getAbs(CarGeneric.o);
            boolean flag1 = false;
            if(mov.rotatCurTime > 0L)
            {
                mov.rotatCurTime--;
                float f = 1.0F - (float)mov.rotatCurTime / (float)mov.rotatTotTime;
                CarGeneric.o.setYaw(mov.angles.getDeg(f));
                flag1 = true;
                if(mov.rotatCurTime <= 0L)
                {
                    mov.rotatCurTime = -1L;
                    mov.rotatingInPlace = false;
                }
            }
            if(!mov.rotatingInPlace && mov.moveCurTime > 0L)
            {
                mov.moveCurTime--;
                double d = 1.0D - (double)mov.moveCurTime / (double)mov.moveTotTime;
                CarGeneric.p.x = mov.srcPos.x * (1.0D - d) + mov.dstPos.x * d;
                CarGeneric.p.y = mov.srcPos.y * (1.0D - d) + mov.dstPos.y * d;
                if(mov.normal.z < 0.0F)
                {
                    CarGeneric.p.z = Engine.land().HQ(CarGeneric.p.x, CarGeneric.p.y) + (double)HeightAboveLandSurface();
                    Engine.land().N(CarGeneric.p.x, CarGeneric.p.y, CarGeneric.n);
                } else
                {
                    CarGeneric.p.z = mov.srcPos.z * (1.0D - d) + mov.dstPos.z * d;
                }
                flag1 = false;
                pos.setAbs(CarGeneric.p);
                if(mov.moveCurTime <= 0L)
                    mov.moveCurTime = -1L;
            }
            if(mov.normal.z < 0.0F)
            {
                if(flag1)
                    Engine.land().N(mov.srcPos.x, mov.srcPos.y, CarGeneric.n);
                CarGeneric.o.orient(CarGeneric.n);
            } else
            {
                CarGeneric.o.orient(mov.normal);
            }
            pos.setAbs(CarGeneric.o);
            return true;
        }

        Move()
        {
        }
    }

    class Mirror extends ActorNet
    {

        private boolean handleGuaranted(NetMsgInput netmsginput)
            throws IOException
        {
            byte byte0 = netmsginput.readByte();
            if(isMirrored())
            {
                int i = 0;
                if(byte0 == 68 || byte0 == 65)
                    i = 1;
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, i);
                post(netmsgguaranted);
            }
            Point3d point3d = CarGeneric.readPackedPos(netmsginput);
            Orient orient = CarGeneric.readPackedOri(netmsginput);
            setPosition(point3d, orient);
            mov.switchToStay(20F);
            switch(byte0)
            {
            case 73: // 'I'
            case 105: // 'i'
                if(dying != 0)
                    System.out.println("Car is dead at init stage");
                if(byte0 == 105)
                    DieMirror(null, false);
                break;

            case 67: // 'C'
                doCollisionDeath();
                break;

            case 65: // 'A'
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                doAbsoluteDeath(actor);
                break;

            case 68: // 'D'
                if(dying == 0)
                {
                    com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                    Actor actor1 = netobj1 != null ? ((ActorNet)netobj1).actor() : null;
                    DieMirror(actor1, true);
                }
                break;

            default:
                System.out.println("CarGeneric: Unknown G message (" + byte0 + ")");
                return false;
            }
            return true;
        }

        private boolean handleNonguaranted(NetMsgInput netmsginput)
            throws IOException
        {
            byte byte0 = netmsginput.readByte();
            switch(byte0)
            {
            case 68: // 'D'
                out.unLockAndSet(netmsginput, 1);
                out.setIncludeTime(false);
                postRealTo(Message.currentRealTime(), masterChannel(), out);
                break;

            case 83: // 'S'
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 0);
                    out.setIncludeTime(false);
                    postReal(Message.currentRealTime(), out);
                }
                mov.switchToStay(10F);
                break;

            case 77: // 'M'
            case 109: // 'm'
                boolean flag = byte0 == 77;
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 0);
                    out.setIncludeTime(true);
                    postReal(Message.currentRealTime(), out);
                }
                Point3d point3d = CarGeneric.readPackedPos(netmsginput);
                Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
                vector3f.z = CarGeneric.readPackedNormal(netmsginput);
                if(vector3f.z >= 0.0F)
                {
                    vector3f.x = CarGeneric.readPackedNormal(netmsginput);
                    vector3f.y = CarGeneric.readPackedNormal(netmsginput);
                    float f = vector3f.length();
                    if(f > 0.001F)
                        vector3f.scale(1.0F / f);
                    else
                        vector3f.set(0.0F, 0.0F, 1.0F);
                }
                int i = netmsginput.readUnsignedShort();
                float f1 = 0.001F * (float)((Message.currentGameTime() - Time.current()) + (long)i);
                if(f1 <= 0.0F)
                    f1 = 0.1F;
                UnitMove unitmove = new UnitMove(0.0F, point3d, f1, vector3f, -1F);
                if(dying == 0)
                    mov.set(unitmove, actor(), 2.0F * prop.SPEED_MAX, flag ? 2.0F * prop.SPEED_BACK : -1F, 1.3F * prop.ROT_SPEED_MAX, 1.1F * prop.ROT_INVIS_ANG);
                break;

            default:
                System.out.println("CarGeneric: Unknown NG message");
                return false;
            }
            return true;
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return handleGuaranted(netmsginput);
            else
                return handleNonguaranted(netmsginput);
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return false;
            byte byte0 = netmsginput.readByte();
            switch(byte0)
            {
            case 68: // 'D'
                if(dying == 0)
                {
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                    Die(actor);
                }
                break;

            case 67: // 'C'
                collisionDeath();
                break;

            default:
                System.out.println("CarGeneric: Unknown M message (" + byte0 + ")");
                return false;
            }
            return true;
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    protected static class CarProperties
        implements Cloneable
    {

        public Object clone() {
            try {
                return super.clone();
            } catch (Exception e) {
            }
            return null;
        }

        public int codeName;
        public String MESH0_NAME;
        public String MESH1_NAME;
        public String meshSummer;
        public String meshDesert;
        public String meshWinter;
        public String meshSummer1;
        public String meshDesert1;
        public String meshWinter1;
        public String soundMove;
        public TableFunction2 fnShotPanzer;
        public TableFunction2 fnExplodePanzer;
        public float PANZER_BODY_FRONT;
        public float PANZER_BODY_BACK;
        public float PANZER_BODY_SIDE;
        public float PANZER_BODY_TOP;
        public float PANZER_TNT_TYPE;
        public String explodeName;
        public int HITBY_MASK;
        public float SPEED_AVERAGE;
        public float SPEED_MAX;
        public float SPEED_BACK;
        public float ROT_SPEED_MAX;
        public float ROT_INVIS_ANG;
        public float BEST_SPACE;
        public float AFTER_COLLISION_DIST;
        public float COMMAND_INTERVAL;
        public float STAY_INTERVAL;
        public int NUM_HUMANS;

        protected CarProperties()
        {
            codeName = 0;
            MESH0_NAME = "3do/cars/None.him";
            MESH1_NAME = null;
            meshSummer = null;
            meshDesert = null;
            meshWinter = null;
            meshSummer1 = null;
            meshDesert1 = null;
            meshWinter1 = null;
            soundMove = "models.Car";
            fnShotPanzer = null;
            fnExplodePanzer = null;
            PANZER_BODY_FRONT = 0.001F;
            PANZER_BODY_BACK = 0.001F;
            PANZER_BODY_SIDE = 0.001F;
            PANZER_BODY_TOP = 0.001F;
            PANZER_TNT_TYPE = 1.0F;
            explodeName = null;
            HITBY_MASK = -1;
            SPEED_AVERAGE = CarGeneric.KmHourToMSec(1.0F);
            SPEED_MAX = CarGeneric.KmHourToMSec(2.0F);
            SPEED_BACK = CarGeneric.KmHourToMSec(1.0F);
            ROT_SPEED_MAX = 3.6F;
            ROT_INVIS_ANG = 360F;
            BEST_SPACE = 2.0F;
            AFTER_COLLISION_DIST = 0.1F;
            COMMAND_INTERVAL = 20F;
            STAY_INTERVAL = 30F;
            NUM_HUMANS = 0;
        }
    }


    public static final double Rnd(double d, double d1)
    {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
        return World.Rnd().nextFloat(f, f1);
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
        if(l < 0L)
            l = 0L;
        return (float)l * Time.tickLenFs();
    }

    private static final long SecsToTicks(float f)
    {
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    public static final Vector2d Rotate(Vector2d vector2d, float f)
    {
        float f1 = Geom.sinDeg(f);
        float f2 = Geom.cosDeg(f);
        return new Vector2d((double)f2 * vector2d.x - (double)f1 * vector2d.y, (double)f1 * vector2d.x + (double)f2 * vector2d.y);
    }

    public void SetTimerToLaunchHumans()
    {
        if(timeHumanLaunch > 0L)
            return;
        if(timeHumanLaunch == 0L || -timeHumanLaunch <= Time.current())
            timeHumanLaunch = Time.current() + (long)(int)Rnd(500F, 1500F);
    }

    public void LaunchHumans()
    {
        if(prop.NUM_HUMANS == 0)
            return;
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        loc.set(0.0D, 0.0D, 0.0D, 170F - Rnd(0.0F, 130F), Rnd(-5F, 2.0F), 0.0F);
        loc1.set(0.0D, 0.0D, 0.0D, 190F + Rnd(0.0F, 130F), Rnd(-5F, 2.0F), 0.0F);
        Loc loc2 = pos.getAbs();
        loc.add(loc2);
        loc1.add(loc2);
        if(prop.NUM_HUMANS == 1)
        {
            if(RndB(0.5F))
                loc = loc1;
            new Soldier(this, getArmy(), loc);
        } else
        {
            new Soldier(this, getArmy(), loc);
            new Soldier(this, getArmy(), loc1);
        }
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if(actor instanceof BridgeSegment)
            aflag[0] = false;
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(dying != 0)
            return;
        if(actor instanceof Soldier)
            return;
        if(isNetMirror())
            return;
        if(collisionStage != 0)
            return;
        mov.switchToAsk();
        collisionStage = 1;
        collidee = actor;
        Point3d point3d = pos.getAbsPoint();
        Point3d point3d1 = actor.pos.getAbsPoint();
        collisVector.set(point3d.x - point3d1.x, point3d.y - point3d1.y);
        if(collisVector.length() >= 9.9999999999999995E-007D)
        {
            collisVector.normalize();
        } else
        {
            float f = Rnd(0.0F, 359.99F);
            collisVector.set(Geom.sinDeg(f), Geom.cosDeg(f));
        }
        ((ChiefGround)getOwner()).CollisionOccured(this, actor);
    }

    public void msgShot(Shot shot)
    {
        shot.bodyMaterial = 2;
        if(dying != 0)
            return;
        if(isNetMirror() && shot.isMirage())
        {
            SetTimerToLaunchHumans();
            return;
        }
        if(shot.power <= 0.0F)
        {
            SetTimerToLaunchHumans();
            return;
        }
        if(shot.powerType == 1)
            if(RndB(0.05F))
            {
                SetTimerToLaunchHumans();
                return;
            } else
            {
                Die(shot.initiator);
                return;
            }
        float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, false, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_TOP);
        f *= Rnd(0.93F, 1.07F);
        float f1 = prop.fnShotPanzer.Value(shot.power, f);
        if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
            Die(shot.initiator);
        else
            SetTimerToLaunchHumans();
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying != 0)
            return;
        if(isNetMirror() && explosion.isMirage())
        {
            SetTimerToLaunchHumans();
            return;
        }
        if(explosion.power <= 0.0F)
        {
            SetTimerToLaunchHumans();
            return;
        }
        if(explosion.powerType == 1)
        {
            if(TankGeneric.splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.0F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_TOP))
                Die(explosion.initiator);
            else
                SetTimerToLaunchHumans();
            return;
        }
        if(explosion.powerType == 2 && explosion.chunkName != null)
        {
            Die(explosion.initiator);
            return;
        }
        float f;
        if(explosion.chunkName != null)
            f = 0.5F * explosion.power;
        else
            f = explosion.receivedTNTpower(this);
        f *= Rnd(0.95F, 1.05F);
        float f1 = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
        if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
            Die(explosion.initiator);
        else
            SetTimerToLaunchHumans();
    }

    public void scare()
    {
        SetTimerToLaunchHumans();
    }

    private void ShowExplode(float f)
    {
        if(f > 0.0F)
            f = Rnd(f, f * 1.6F);
        Explosions.runByName(prop.explodeName, this, "Smoke", "", f);
    }

    private void ActivateMesh()
    {
        boolean flag = dying == 1;
        if(!flag)
            setMesh(prop.MESH0_NAME);
        else
        if(prop.MESH1_NAME == null)
        {
            setMesh(prop.MESH0_NAME);
            mesh().makeAllMaterialsDarker(0.22F, 0.35F);
        } else
        {
            setMesh(prop.MESH1_NAME);
        }
        int i = mesh().hookFind("Ground_Level");
        float f = heightAboveLandSurface;
        if(i != -1)
        {
            Matrix4d matrix4d = new Matrix4d();
            mesh().hookMatrix(i, matrix4d);
            heightAboveLandSurface = (float)(-matrix4d.m23);
        } else
        {
            float af[] = new float[6];
            mesh().getBoundBox(af);
            heightAboveLandSurface = -af[2];
        }
        if(flag)
        {
            Point3d point3d = pos.getAbsPoint();
            point3d.z += heightAboveLandSurface - f;
            pos.setAbs(point3d);
            pos.reset();
        }
    }

    private void MakeCrush()
    {
        engineSFX = null;
        engineSTimer = 0x5f5e0ff;
        breakSounds();
        dying = 1;
        ActivateMesh();
    }

    private void Die(Actor actor)
    {
        if(isNetMirror())
        {
            send_DeathRequest(actor);
            return;
        }
        collisionStage = 1;
        int i = ((ChiefGround)getOwner()).getCodeOfBridgeSegment(this);
        if(i >= 0)
        {
            if(BridgeSegment.isEncodedSegmentDamaged(i))
            {
                absoluteDeath(actor);
                return;
            }
            LongBridge.AddTraveller(i, this);
            codeOfUnderlyingBridgeSegment = i;
        }
        ((ChiefGround)getOwner()).Detach(this, actor);
        World.onActorDied(this, actor);
        ShowExplode(16F);
        if(isNetMaster())
        {
            send_DeathCommand(actor);
            Point3d point3d = simplifyPos(pos.getAbsPoint());
            Orient orient = simplifyOri(pos.getAbsOrient());
            setPosition(point3d, orient);
        }
        MakeCrush();
    }

    private void DieMirror(Actor actor, boolean flag)
    {
        if(!isNetMirror())
            System.out.println("Internal error in CarGeneric: DieMirror");
        collisionStage = 1;
        ((ChiefGround)getOwner()).Detach(this, actor);
        World.onActorDied(this, actor);
        if(flag)
            ShowExplode(16F);
        MakeCrush();
    }

    public void destroy()
    {
        engineSFX = null;
        engineSTimer = 0x5f5e0ff;
        breakSounds();
        if(codeOfUnderlyingBridgeSegment >= 0)
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

    public void setMesh(String s)
    {
        super.setMesh(s);
        if(Config.cur.b3dgunners)
        {
            return;
        } else
        {
            mesh().materialReplaceToNull("Pilot1");
            return;
        }
    }

    public CarGeneric(CarProperties carproperties, Actor actor)
    {
        super(carproperties.MESH0_NAME);
        prop = null;
        udata = new UnitData();
        mov = new Moving();
        engineSFX = null;
        engineSTimer = 0x98967f;
        ticksIn8secs = (int)(8F / Time.tickConstLenFs());
        collisionStage = 0;
        collisVector = new Vector2d();
        obs = new StaticObstacle();
        dying = 0;
        codeOfUnderlyingBridgeSegment = -1;
        outCommand = new NetMsgFiltered();
        prop = carproperties;
        timeHumanLaunch = -(Time.current() + (long)(int)Rnd(2000F, 8000F));
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
        if(netchannel == null)
            net = new Master(this);
        else
        if(i != 0)
            net = new Mirror(this, netchannel, i);
    }

    private void send_DeathRequest(Actor actor)
    {
        if(!isNetMirror())
            return;
        if(net.masterChannel() instanceof NetChannelInStream)
            return;
        try
        {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(68);
            netmsgfiltered.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            netmsgfiltered.setIncludeTime(false);
            net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_CollisionDeathRequest()
    {
        if(!isNetMirror())
            return;
        if(net.masterChannel() instanceof NetChannelInStream)
            return;
        try
        {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.unLockAndClear();
            netmsgfiltered.writeByte(67);
            netmsgfiltered.setIncludeTime(false);
            net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_AnByteAndPoseCommand(boolean flag, Actor actor, int i)
    {
        if(!isNetMaster() || !net.isMirrored())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(i);
            sendPose(netmsgguaranted);
            if(flag)
                netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
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
        if(!isNetMaster() || !net.isMirrored())
            return;
        if(moving.moveCurTime < 0L && moving.rotatCurTime < 0L)
            return;
        try
        {
            outCommand.unLockAndClear();
            if(moving.dstPos == null || moving.moveTotTime <= 0L || moving.normal == null)
            {
                outCommand.writeByte(83);
                outCommand.setIncludeTime(false);
                net.post(Time.current(), outCommand);
            } else
            {
                if(f > 0.0F)
                    outCommand.writeByte(77);
                else
                    outCommand.writeByte(109);
                outCommand.write(packPos(moving.dstPos));
                outCommand.writeByte(packNormal(moving.normal.z));
                if(moving.normal.z >= 0.0F)
                {
                    outCommand.writeByte(packNormal(moving.normal.x));
                    outCommand.writeByte(packNormal(moving.normal.y));
                }
                int i = (int)((long)Time.tickLen() * moving.moveTotTime);
                if(i >= 0x10000)
                    i = 65535;
                outCommand.writeShort(i);
                outCommand.setIncludeTime(true);
                net.post(Time.current(), outCommand);
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    static int packNormal(float f)
    {
        f++;
        f *= 0.5F;
        f *= 254F;
        int i = (int)(f + 0.5F);
        if(i < 0)
            i = 0;
        if(i > 254)
            i = 254;
        return i - 127;
    }

    static byte[] packPos(Point3d point3d)
    {
        byte abyte0[] = new byte[8];
        int i = (int)(point3d.x * 20D + 0.5D);
        int j = (int)(point3d.y * 20D + 0.5D);
        int k = (int)(point3d.z * 10D + 0.5D);
        abyte0[0] = (byte)(i >> 0 & 0xff);
        abyte0[1] = (byte)(i >> 8 & 0xff);
        abyte0[2] = (byte)(i >> 16 & 0xff);
        abyte0[3] = (byte)(j >> 0 & 0xff);
        abyte0[4] = (byte)(j >> 8 & 0xff);
        abyte0[5] = (byte)(j >> 16 & 0xff);
        abyte0[6] = (byte)(k >> 0 & 0xff);
        abyte0[7] = (byte)(k >> 8 & 0xff);
        return abyte0;
    }

    static byte[] packOri(Orient orient)
    {
        byte abyte0[] = new byte[3];
        int i = (int)((orient.getYaw() * 256F) / 360F);
        int j = (int)((orient.getPitch() * 256F) / 360F);
        int k = (int)((orient.getRoll() * 256F) / 360F);
        abyte0[0] = (byte)(i & 0xff);
        abyte0[1] = (byte)(j & 0xff);
        abyte0[2] = (byte)(k & 0xff);
        return abyte0;
    }

    static float unpackNormal(int i)
    {
        return (float)i / 127F;
    }

    static Point3d unpackPos(byte abyte0[])
    {
        int i = ((abyte0[2] & 0xff) << 16) + ((abyte0[1] & 0xff) << 8) + ((abyte0[0] & 0xff) << 0);
        int j = ((abyte0[5] & 0xff) << 16) + ((abyte0[4] & 0xff) << 8) + ((abyte0[3] & 0xff) << 0);
        int k = ((abyte0[7] & 0xff) << 8) + ((abyte0[6] & 0xff) << 0);
        return new Point3d((double)i * 0.050000000000000003D, (double)j * 0.050000000000000003D, (double)k * 0.10000000000000001D);
    }

    static Orient unpackOri(byte abyte0[])
    {
        int i = abyte0[0] & 0xff;
        int j = abyte0[1] & 0xff;
        int k = abyte0[2] & 0xff;
        Orient orient = new Orient();
        orient.setYPR(((float)i * 360F) / 256F, ((float)j * 360F) / 256F, ((float)k * 360F) / 256F);
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

    static float readPackedNormal(NetMsgInput netmsginput)
        throws IOException
    {
        return unpackNormal(netmsginput.readByte());
    }

    static Point3d readPackedPos(NetMsgInput netmsginput)
        throws IOException
    {
        byte abyte0[] = new byte[8];
        netmsginput.read(abyte0);
        return unpackPos(abyte0);
    }

    static Orient readPackedOri(NetMsgInput netmsginput)
        throws IOException
    {
        byte abyte0[] = new byte[3];
        netmsginput.read(abyte0);
        return unpackOri(abyte0);
    }

    private void sendPose(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.write(packPos(pos.getAbsPoint()));
        netmsgguaranted.write(packOri(pos.getAbsOrient()));
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        byte byte0 = ((byte)(dying != 0 ? 105 : 73));
        netmsgguaranted.writeByte(byte0);
        sendPose(netmsgguaranted);
        net.postTo(netchannel, netmsgguaranted);
    }

    public void startMove()
    {
        if(!interpEnd("move"))
        {
            mov = new Moving();
            collisionStage = 0;
            interpPut(new Move(), "move", Time.current(), null);
            engineSFX = newSound(prop.soundMove, true);
            engineSTimer = (int)SecsToTicks(Rnd(5F, 7F));
        }
    }

    public void forceReaskMove()
    {
        if(isNetMirror())
            return;
        if(collisionStage != 0)
            return;
        if(dying != 0)
            return;
        if(mov == null || mov.normal == null)
        {
            return;
        } else
        {
            mov.switchToAsk();
            return;
        }
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
        int i = Finger.Int(getClass().getName());
        int j = 0;
        return new UnitInPackedForm(codeName, i, j, SpeedAverage(), BestSpace(), 0, HitbyMask());
    }

    public int HitbyMask()
    {
        return prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties abulletproperties[])
    {
        if(dying != 0)
            return -1;
        if(abulletproperties.length == 1)
            return 0;
        if(abulletproperties.length <= 0)
            return -1;
        if(abulletproperties[0].power <= 0.0F)
            return 0;
        if(abulletproperties[1].power <= 0.0F)
            return 1;
        if(abulletproperties[0].cumulativePower > 0.0F)
            return 0;
        if(abulletproperties[1].cumulativePower > 0.0F)
            return 1;
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        return abulletproperties[0].powerType != 2 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        return dying == 0 ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        if(dying != 0)
            return false;
        if(i != 0)
            return false;
        if(point3d != null)
            point3d.set(0.0D, 0.0D, 0.0D);
        return true;
    }

    public void absoluteDeath(Actor actor)
    {
        if(isNetMirror())
            return;
        if(isNetMaster())
            send_AbsoluteDeathCommand(actor);
        doAbsoluteDeath(actor);
    }

    private void doAbsoluteDeath(Actor actor)
    {
        ChiefGround chiefground = (ChiefGround)getOwner();
        if(chiefground != null)
            chiefground.Detach(this, actor);
        if(!getDiedFlag())
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
        if(isNetMirror())
        {
            send_CollisionDeathRequest();
            return;
        }
        if(isNetMaster())
            send_CollisionDeathCommand();
        doCollisionDeath();
    }

    private void doCollisionDeath()
    {
        ChiefGround chiefground = (ChiefGround)getOwner();
        boolean flag = chiefground == null && codeOfUnderlyingBridgeSegment >= 0 || chiefground != null && chiefground.getCodeOfBridgeSegment(this) >= 0;
        if(chiefground != null)
            chiefground.Detach(this, null);
        if(flag)
            Explosions.Car_ExplodeCollapse(pos.getAbsPoint());
        else
            Explosions.Car_ExplodeCollapse(pos.getAbsPoint());
        destroy();
    }

    public float futurePosition(float f, Point3d point3d)
    {
        pos.getAbs(point3d);
        if(f <= 0.0F)
            return 0.0F;
        if(mov.moveCurTime < 0L && mov.rotatCurTime < 0L)
            return 0.0F;
        float f1 = TicksToSecs(mov.moveCurTime);
        if(mov.dstPos == null)
            if(f1 >= f)
                return f;
            else
                return f1;
        float f2 = 0.0F;
        if(mov.rotatingInPlace)
        {
            f2 = TicksToSecs(mov.rotatCurTime);
            if(f2 >= f)
                return f;
        }
        if(f1 <= 0.0F)
            return f2;
        if(f2 + f1 <= f)
        {
            point3d.set(mov.dstPos);
            return f2 + f1;
        }
        Point3d point3d1 = new Point3d();
        point3d1.set(mov.dstPos);
        double d = (f - f2) / f1;
        p.x = point3d.x * (1.0D - d) + point3d1.x * d;
        p.y = point3d.y * (1.0D - d) + point3d1.y * d;
        if(mov.normal.z < 0.0F)
            p.z = Engine.land().HQ(p.x, p.y) + (double)HeightAboveLandSurface();
        else
            p.z = point3d.z * (1.0D - d) + point3d1.z * d;
        point3d.set(p);
        return f;
    }

    private CarProperties prop;
    private int codeName;
    private float heightAboveLandSurface;
    public UnitData udata;
    private Moving mov;
    protected SoundFX engineSFX;
    protected int engineSTimer;
    protected int ticksIn8secs;
    private int collisionStage;
    static final int COLLIS_NO_COLLISION = 0;
    static final int COLLIS_JUST_COLLIDED = 1;
    static final int COLLIS_MOVING_FROM_COLLISION = 2;
    private Vector2d collisVector;
    private Actor collidee;
    private StaticObstacle obs;
    private long timeHumanLaunch;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_DEAD = 1;
    private int codeOfUnderlyingBridgeSegment;
    private static CarProperties constr_arg1 = null;
    private static Actor constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
    private NetMsgFiltered outCommand;
























}
