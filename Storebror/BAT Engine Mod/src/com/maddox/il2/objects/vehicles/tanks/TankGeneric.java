/*Modified TankGeneric class for the SAS Engine Mod*/
/*By western, Add radar search and missile interceptable flags about firing enemies on 24th/Apr./2018 - 22th/Jun./2018*/
package com.maddox.il2.objects.vehicles.tanks;

import java.io.IOException;

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
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeRadarWarningReceiver;
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
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.util.TableFunction2;

public abstract class TankGeneric extends ActorHMesh
    implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, UnitInterface, HunterInterface
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
                    System.out.println("Tank: Parameter [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Tank: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
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
                    System.out.print("Tank: Parameter [" + s + "]:<" + s1 + "> ");
                    System.out.println(s2 != null ? "is empty" : "not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Tank \"" + s + "\" is not (correctly) declared in technics.ini file!");
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

        private static TankProperties LoadTankProperties(SectFile sectfile, String s, Class class1)
        {
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            String checkMesh = getS(sectfile, s, "MeshSummer");
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (checkMesh == null || checkMesh.length() == 0)) return null;
            // TODO: ---
            TankProperties tankproperties = new TankProperties();
            String s1 = getS(sectfile, s, "PanzerType", null);
            if(s1 == null)
                s1 = "Tank";
            tankproperties.fnShotPanzer = TableFunctions.GetFunc2(s1 + "ShotPanzer");
            tankproperties.fnExplodePanzer = TableFunctions.GetFunc2(s1 + "ExplodePanzer");
            tankproperties.PANZER_TNT_TYPE = getF(sectfile, s, "PanzerSubtype", 0.0F, 100F);
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//          tankproperties.meshSummer = getS(sectfile, s, "MeshSummer");
            tankproperties.meshSummer = checkMesh;
            // TODO: ---
            tankproperties.meshDesert = getS(sectfile, s, "MeshDesert", tankproperties.meshSummer);
            tankproperties.meshWinter = getS(sectfile, s, "MeshWinter", tankproperties.meshSummer);
            tankproperties.meshSummer1 = getS(sectfile, s, "MeshSummerDamage", null);
            tankproperties.meshDesert1 = getS(sectfile, s, "MeshDesertDamage", tankproperties.meshSummer1);
            tankproperties.meshWinter1 = getS(sectfile, s, "MeshWinterDamage", tankproperties.meshSummer1);
            float f = (tankproperties.meshSummer1 != null ? 0 : 1) + (tankproperties.meshDesert1 != null ? 0 : 1) + (tankproperties.meshWinter1 != null ? 0 : 1);
            if(f != 0 && f != 3)
            {
                System.out.println("Tank: Uncomplete set of damage meshes for '" + s + "'");
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                    throw new RuntimeException("Can't register tank object");
                return null;
                // ---
            }
            tankproperties.explodeName = getS(sectfile, s, "Explode", "Tank");
            tankproperties.PANZER_BODY_FRONT = getF(sectfile, s, "PanzerBodyFront", 0.001F, 9.999F);
            if(sectfile.get(s, "PanzerBodyBack", -9865.345F) == -9865.345F)
            {
                tankproperties.PANZER_BODY_BACK = tankproperties.PANZER_BODY_FRONT;
                tankproperties.PANZER_BODY_SIDE = tankproperties.PANZER_BODY_FRONT;
                tankproperties.PANZER_BODY_TOP = tankproperties.PANZER_BODY_FRONT;
            } else
            {
                tankproperties.PANZER_BODY_BACK = getF(sectfile, s, "PanzerBodyBack", 0.001F, 9.999F);
                tankproperties.PANZER_BODY_SIDE = getF(sectfile, s, "PanzerBodySide", 0.001F, 9.999F);
                tankproperties.PANZER_BODY_TOP = getF(sectfile, s, "PanzerBodyTop", 0.001F, 9.999F);
            }
            if(sectfile.get(s, "PanzerHead", -9865.345F) == -9865.345F)
                tankproperties.PANZER_HEAD = tankproperties.PANZER_BODY_FRONT;
            else
                tankproperties.PANZER_HEAD = getF(sectfile, s, "PanzerHead", 0.001F, 9.999F);
            if(sectfile.get(s, "PanzerHeadTop", -9865.345F) == -9865.345F)
                tankproperties.PANZER_HEAD_TOP = tankproperties.PANZER_BODY_TOP;
            else
                tankproperties.PANZER_HEAD_TOP = getF(sectfile, s, "PanzerHeadTop", 0.001F, 9.999F);
            f = Math.min(Math.min(tankproperties.PANZER_BODY_BACK, tankproperties.PANZER_BODY_TOP), Math.min(tankproperties.PANZER_BODY_SIDE, tankproperties.PANZER_HEAD_TOP));
            tankproperties.HITBY_MASK = f <= 0.015F ? -1 : -2;
            int i = 0;
            do
            {
                if(i != 0)
                {
                    String s2 = sectfile.get(s, "Gun_" + i);
                    if(s2 == null || s2.length() <= 0)
                        break;
                }
                i++;
            } while(true);
            tankproperties.gunProperties = new GunProps[i];
            for(int j = 0; j < i; j++)
            {
                tankproperties.gunProperties[j] = new GunProps();
                String s3 = "Gun";
                String s4 = "";
                if(j != 0)
                {
                    s4 = "_" + j;
                    s3 = s3 + s4;
                }
                String s5 = "com.maddox.il2.objects.weapons." + getS(sectfile, s, s3);
                try
                {
                    tankproperties.gunProperties[j].gunClass = Class.forName(s5);
                }
                catch(Exception exception)
                {
                    System.out.println("Tank: Can't find gun class '" + s5 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                        throw new RuntimeException("Can't register tank object");
                    return null;
                    // ---
                }
                tankproperties.gunProperties[j].WEAPONS_MASK = Gun.getProperties(tankproperties.gunProperties[j].gunClass).weaponType;
                if(tankproperties.gunProperties[j].WEAPONS_MASK == 0)
                {
                    System.out.println("Tank: Undefined weapon type in gun class '" + s5 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                        throw new RuntimeException("Can't register tank object");
                    return null;
                    // ---
                }
                tankproperties.gunProperties[j].MAX_SHELLS = (int)getF(sectfile, s, "NumShells" + s4, -1F, 30000F);
                tankproperties.gunProperties[j].ATTACK_FAST_TARGETS = false;
                float f1 = sectfile.get(s, "FireFastTargets", -9865.345F);
                if(f1 != -9865.345F)
                    tankproperties.gunProperties[j].ATTACK_FAST_TARGETS = f1 > 0.5F;
                // +++ By western, expanded for flags Intercept missiles and Radar use
                tankproperties.gunProperties[j].ATTACK_MISSILES = false;
                float f7 = sectfile.get(s, "InterceptMissiles", -9865.345F);
                if(f7 != -9865.345F)
                    tankproperties.gunProperties[j].ATTACK_MISSILES = f7 > 0.5F;
                tankproperties.gunProperties[j].USE_RADAR_SEARCH = false;
                float f8 = sectfile.get(s, "RadarSearch", -9865.345F);
                if(f8 != -9865.345F)
                    tankproperties.gunProperties[j].USE_RADAR_SEARCH = f8 > 0.5F;
                if(tankproperties.gunProperties[j].USE_RADAR_SEARCH)
                {
                    tankproperties.gunProperties[j].SOUND_PW_RADAR_SEARCH = getS(sectfile, s, "SoundRadarSearchPulseWave");
                    tankproperties.gunProperties[j].SOUND_PW_RADAR_LOCK = getS(sectfile, s, "SoundRadarLockPulseWave");
                }
                // ---
                tankproperties.gunProperties[j].ATTACK_MAX_DISTANCE = getF(sectfile, s, "AttackMaxDistance" + s4, 6F, 12000F);
                tankproperties.gunProperties[j].ATTACK_MAX_RADIUS = getF(sectfile, s, "AttackMaxRadius" + s4, 6F, 12000F);
                tankproperties.gunProperties[j].ATTACK_MAX_HEIGHT = getF(sectfile, s, "AttackMaxHeight" + s4, 6F, 12000F);
                int k = sectfile.sectionIndex(s);
                if(sectfile.varExist(k, "HeadYawHalfRange" + s4))
                {
                    float f2 = getF(sectfile, s, "HeadYawHalfRange" + s4, 0.0F, 180F);
                    tankproperties.gunProperties[j].HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
                    tankproperties.gunProperties[j].HEAD_YAW_RANGE.set(-f2, f2);
                    tankproperties.gunProperties[j].HEAD_STD_YAW = 0.0F;
                } else
                {
                    float f3 = getF(sectfile, s, "HeadMinYaw" + s4, -180F, 180F);
                    float f5 = getF(sectfile, s, "HeadStdYaw" + s4, -180F, 180F);
                    float f6 = getF(sectfile, s, "HeadMaxYaw" + s4, -180F, 180F);
                    tankproperties.gunProperties[j].HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
                    tankproperties.gunProperties[j].HEAD_YAW_RANGE.set(f3, f6);
                    tankproperties.gunProperties[j].HEAD_STD_YAW = f5;
                }
                tankproperties.gunProperties[j].GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch" + s4, -15F, 85F);
                tankproperties.gunProperties[j].GUN_STD_PITCH = getF(sectfile, s, "GunStdPitch" + s4, -15F, 89.9F);
                tankproperties.gunProperties[j].GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch" + s4, 0.0F, 89.9F);
                tankproperties.gunProperties[j].HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed" + s4, 0.1F, 999F);
                tankproperties.gunProperties[j].GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed" + s4, 0.1F, 999F);
                tankproperties.gunProperties[j].DELAY_AFTER_SHOOT = getF(sectfile, s, "DelayAfterShoot" + s4, 0.0F, 999F);
                tankproperties.gunProperties[j].CHAINFIRE_TIME = getF(sectfile, s, "ChainfireTime" + s4, 0.0F, 600F);
                tankproperties.gunProperties[j].STAY_WHEN_FIRE = sectfile.get(s, "FireFastTargets" + s4, 1) == 1;
                float f4 = sectfile.get(s, "FastTargetsAngleError" + s4, -9865.345F);
                if(f4 <= 0.0F)
                    f4 = 0.0F;
                else
                if(f4 >= 45F)
                    f4 = 45F;
                tankproperties.gunProperties[j].FAST_TARGETS_ANGLE_ERROR = f4;
            }

            tankproperties.SPEED_AVERAGE = TankGeneric.KmHourToMSec(getF(sectfile, s, "SpeedAverage", 1.0F, 100F));
            tankproperties.SPEED_MAX = TankGeneric.KmHourToMSec(getF(sectfile, s, "SpeedMax", 1.0F, 100F));
            tankproperties.SPEED_BACK = TankGeneric.KmHourToMSec(getF(sectfile, s, "SpeedBack", 0.5F, 100F));
            tankproperties.ROT_SPEED_MAX = getF(sectfile, s, "RotSpeedMax", 0.1F, 800F);
            tankproperties.ROT_INVIS_ANG = getF(sectfile, s, "RotInvisAng", 0.0F, 360F);
            tankproperties.BEST_SPACE = getF(sectfile, s, "BestSpace", 0.1F, 100F);
            tankproperties.AFTER_COLLISION_DIST = getF(sectfile, s, "AfterCollisionDist", 0.1F, 80F);
            tankproperties.COMMAND_INTERVAL = getF(sectfile, s, "CommandInterval", 0.5F, 100F);
            tankproperties.STAY_INTERVAL = getF(sectfile, s, "StayInterval", 0.1F, 200F);
            tankproperties.soundMove = getS(sectfile, s, "SoundMove");
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", tankproperties.meshSummer);
            Property.set(class1, "speed", tankproperties.SPEED_AVERAGE);
            return tankproperties;
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
                proper.meshName = proper.meshWinter;
                proper.meshName2 = proper.meshWinter1;
                break;

            case 2: // '\002'
                proper.meshName = proper.meshDesert;
                proper.meshName2 = proper.meshDesert1;
                break;

            default:
                proper.meshName = proper.meshSummer;
                proper.meshName2 = proper.meshSummer1;
                break;
            }
            TankGeneric tankgeneric = null;
            try
            {
                TankGeneric.constr_arg1 = proper;
                TankGeneric.constr_arg2 = actor;
                tankgeneric = (TankGeneric)cls.newInstance();
                TankGeneric.constr_arg1 = null;
                TankGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
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

        public Class cls;
        public TankProperties proper;

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
                proper = LoadTankProperties(Statics.getTechnicsFile(), s1, class1);

                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL && proper == null) return;
                // TODO: ---

            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in tank spawn: " + class1.getName());
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
            {
                neverDust();
                if(dying == 2)
                    return false;
                if(dyingDelay-- <= 0L)
                {
                    ShowExplode();
                    MakeCrush();
                    return false;
                }
                if(mov.rotatCurTime > 0L)
                {
                    mov.rotatCurTime--;
                    float f = 1.0F - (float)mov.rotatCurTime / (float)mov.rotatTotTime;
                    pos.getAbs(TankGeneric.o);
                    TankGeneric.o.setYaw(mov.angles.getDeg(f));
                    if(mov.normal.z < 0.0F)
                    {
                        Engine.land().N(mov.srcPos.x, mov.srcPos.y, TankGeneric.n);
                        TankGeneric.o.orient(TankGeneric.n);
                    } else
                    {
                        TankGeneric.o.orient(mov.normal);
                    }
                    pos.setAbs(TankGeneric.o);
                }
                return true;
            }
            boolean flag = mov.moveCurTime < 0L && mov.rotatCurTime < 0L;
            if(isNetMirror() && flag)
            {
                mov.switchToStay(30F);
                flag = false;
            }
            if(flag)
            {
                ChiefGround chiefground = (ChiefGround)getOwner();
                float f2 = -1F;
                UnitMove unitmove;
                if(collisionStage == 0)
                {
                    if(prop.meshName2 != null)
                    {
                        TankGeneric.p.x = TankGeneric.Rnd(-0.29999999999999999D, 0.29999999999999999D);
                        TankGeneric.p.y = TankGeneric.Rnd(-0.29999999999999999D, 0.29999999999999999D);
                        TankGeneric.p.z = 1.0D;
                        unitmove = chiefground.AskMoveCommand(actor, TankGeneric.p, obs);
                    } else
                    {
                        unitmove = chiefground.AskMoveCommand(actor, null, obs);
                    }
                } else
                if(collisionStage == 1)
                {
                    obs.collision(collidee, chiefground, udata);
                    collidee = null;
                    float f3 = TankGeneric.Rnd(-70F, 70F);
                    Vector2d vector2d = TankGeneric.Rotate(collisVector, f3);
                    vector2d.scale((double)prop.AFTER_COLLISION_DIST * TankGeneric.Rnd(0.87D, 1.75D));
                    TankGeneric.p.set(vector2d.x, vector2d.y, -1D);
                    unitmove = chiefground.AskMoveCommand(actor, TankGeneric.p, obs);
                    collisionStage = 2;
                    f2 = prop.SPEED_BACK;
                } else
                {
                    float f4 = TankGeneric.Rnd(0.0F, 359.99F);
                    Vector2d vector2d1 = TankGeneric.Rotate(collisVector, f4);
                    vector2d1.scale((double)prop.AFTER_COLLISION_DIST * TankGeneric.Rnd(0.20000000000000001D, 0.59999999999999998D));
                    TankGeneric.p.set(vector2d1.x, vector2d1.y, 1.0D);
                    unitmove = chiefground.AskMoveCommand(actor, TankGeneric.p, obs);
                    collisionStage = 0;
                }
                mov.set(unitmove, actor, prop.SPEED_MAX, f2, prop.ROT_SPEED_MAX, prop.ROT_INVIS_ANG);
                for(int j = 0; j < arms.length; j++)
                {
                    if(!StayWhenFire(arms[j].aim))
                        continue;
                    if(Head360(prop.gunProperties[j]))
                    {
                        if(!arms[j].aim.isInFiringMode())
                            continue;
                        mov.switchToStay(1.3F);
                        break;
                    }
                    if(!arms[j].aim.isInAimingMode())
                        continue;
                    mov.switchToStay(1.3F);
                    break;
                }

                if(isNetMaster())
                    send_MoveCommand(mov, f2);
            }
            for(int i = 0; i < arms.length; i++)
                arms[i].aim.tick_();

            if(dust != null)
                dust._setIntesity(mov.movingForward ? 1.0F : 0.0F);
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
                    engineSTimer = (int)TankGeneric.SecsToTicks(TankGeneric.Rnd(10F, 12F));
                } else
                if(engineSTimer < ticksIn8secs)
                    engineSTimer = (int)TankGeneric.SecsToTicks(TankGeneric.Rnd(10F, 12F));
            pos.getAbs(TankGeneric.o);
            boolean flag1 = false;
            if(mov.rotatCurTime > 0L)
            {
                mov.rotatCurTime--;
                float f1 = 1.0F - (float)mov.rotatCurTime / (float)mov.rotatTotTime;
                TankGeneric.o.setYaw(mov.angles.getDeg(f1));
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
                TankGeneric.p.x = mov.srcPos.x * (1.0D - d) + mov.dstPos.x * d;
                TankGeneric.p.y = mov.srcPos.y * (1.0D - d) + mov.dstPos.y * d;
                if(mov.normal.z < 0.0F)
                {
                    TankGeneric.p.z = Engine.land().HQ(TankGeneric.p.x, TankGeneric.p.y) + (double)HeightAboveLandSurface();
                    Engine.land().N(TankGeneric.p.x, TankGeneric.p.y, TankGeneric.n);
                } else
                {
                    TankGeneric.p.z = mov.srcPos.z * (1.0D - d) + mov.dstPos.z * d;
                }
                flag1 = false;
                pos.setAbs(TankGeneric.p);
                if(mov.moveCurTime <= 0L)
                    mov.moveCurTime = -1L;
            }
            if(mov.normal.z < 0.0F)
            {
                if(flag1)
                    Engine.land().N(mov.srcPos.x, mov.srcPos.y, TankGeneric.n);
                TankGeneric.o.orient(TankGeneric.n);
            } else
            {
                TankGeneric.o.orient(mov.normal);
            }
            pos.setAbs(TankGeneric.o);
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
            int i = 0;
            if(byte0 == 68 || byte0 == 65)
                i = 1;
            if(isMirrored())
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, i);
                post(netmsgguaranted);
            }
            Point3d point3d = TankGeneric.readPackedPos(netmsginput);
            Orient orient = TankGeneric.readPackedOri(netmsginput);
            float af[] = new float[arms.length];
            float af1[] = new float[arms.length];
            af[0] = TankGeneric.readPackedAng(netmsginput);
            af1[0] = TankGeneric.readPackedAng(netmsginput);
            com.maddox.rts.NetObj netobj = null;
            if(i == 1)
                netobj = netmsginput.readNetObj();
            if(netmsginput.available() > 0)
            {
                for(int j = 1; j < arms.length; j++)
                {
                    af[j] = netmsginput.readFloat();
                    af1[j] = netmsginput.readFloat();
                }

            }
            setPosition(point3d, orient, af, af1);
            mov.switchToStay(20F);
            switch(byte0)
            {
            case 73: // 'I'
            case 105: // 'i'
                if(dying != 0)
                    System.out.println("Tank is dead at init stage");
                if(byte0 == 105)
                    DieMirror(null, false);
                break;

            case 65: // 'A'
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                doAbsoluteDeath(actor);
                break;

            case 67: // 'C'
                doCollisionDeath();
                break;

            case 68: // 'D'
                if(dying == 0)
                {
                    Actor actor1 = netobj != null ? ((ActorNet)netobj).actor() : null;
                    DieMirror(actor1, true);
                }
                break;

            default:
                System.out.println("TankGeneric: Unknown G message (" + byte0 + ")");
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

            case 84: // 'T'
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 1);
                    out.setIncludeTime(false);
                    postReal(Message.currentRealTime(), out);
                }
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                int i = netmsginput.readUnsignedByte();
                int j = 0;
                if(netmsginput.available() > 0)
                    j = netmsginput.readInt();
                Track_Mirror(j, actor, i);
                break;

            case 70: // 'F'
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 1);
                    out.setIncludeTime(true);
                    postReal(Message.currentRealTime(), out);
                }
                com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                Actor actor1 = netobj1 != null ? ((ActorNet)netobj1).actor() : null;
                float f = netmsginput.readFloat();
                float f1 = 0.001F * (float)(Message.currentGameTime() - Time.current()) + f;
                int l = netmsginput.readUnsignedByte();
                int i1 = 0;
                if(netmsginput.available() > 0)
                    i1 = netmsginput.readInt();
                Fire_Mirror(i1, actor1, l, f1);
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
                Point3d point3d = TankGeneric.readPackedPos(netmsginput);
                Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
                vector3f.z = TankGeneric.readPackedNormal(netmsginput);
                if(vector3f.z >= 0.0F)
                {
                    vector3f.x = TankGeneric.readPackedNormal(netmsginput);
                    vector3f.y = TankGeneric.readPackedNormal(netmsginput);
                    float f2 = vector3f.length();
                    if(f2 > 0.001F)
                        vector3f.scale(1.0F / f2);
                    else
                        vector3f.set(0.0F, 0.0F, 1.0F);
                }
                int k = netmsginput.readUnsignedShort();
                float f3 = 0.001F * (float)((Message.currentGameTime() - Time.current()) + (long)k);
                if(f3 <= 0.0F)
                    f3 = 0.1F;
                UnitMove unitmove = new UnitMove(0.0F, point3d, f3, vector3f, -1F);
                if(dying == 0)
                    mov.set(unitmove, actor(), 2.0F * prop.SPEED_MAX, flag ? 2.0F * prop.SPEED_BACK : -1F, 1.3F * prop.ROT_SPEED_MAX, 1.1F * prop.ROT_INVIS_ANG);
                break;

            default:
                System.out.println("TankGeneric: Unknown NG message");
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
                    Die(actor, true);
                }
                break;

            case 67: // 'C'
                collisionDeath();
                break;

            default:
                System.out.println("TankGeneric: Unknown M message (" + byte0 + ")");
                return false;
            }
            return true;
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    private class FireDevice
    {

        private Gun gun;
        private Aim aim;
        private float headYaw;
        private float gunPitch;
        private Point3d fireOffset;
        private Orient fireOrient;













        public FireDevice()
        {
        }
    }

    public static class GunProps
    {

        public int WEAPONS_MASK;
        public float ATTACK_MAX_DISTANCE;
        public float ATTACK_MAX_RADIUS;
        public float ATTACK_MAX_HEIGHT;
        public boolean ATTACK_FAST_TARGETS;
        // +++ By western, expanded for flags Intercept missiles and Radar use
        public boolean ATTACK_MISSILES;
        public boolean USE_RADAR_SEARCH;
        public String SOUND_PW_RADAR_SEARCH;
        public String SOUND_PW_RADAR_LOCK;
        // ---
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
        public boolean STAY_WHEN_FIRE;
        public int MAX_SHELLS;

        public GunProps()
        {
            MAX_SHELLS = 1;
        }
    }

    protected static class TankProperties
        implements Cloneable
    {

        public Object clone()
        {
            try
            {
              return super.clone();
            }
            catch (Exception e) {}
            return null;
        }

        public int codeName;
        public String meshName;
        public String meshName2;
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
        public float PANZER_HEAD;
        public float PANZER_HEAD_TOP;
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
        public GunProps gunProperties[];

        protected TankProperties()
        {
            codeName = 0;
            meshName = "3do/tanks/NameNotSpecified.him";
            meshName2 = null;
            meshSummer = null;
            meshDesert = null;
            meshWinter = null;
            meshSummer1 = null;
            meshDesert1 = null;
            meshWinter1 = null;
            soundMove = "models.Tank";
            fnShotPanzer = null;
            fnExplodePanzer = null;
            PANZER_BODY_FRONT = 0.001F;
            PANZER_BODY_BACK = 0.001F;
            PANZER_BODY_SIDE = 0.001F;
            PANZER_BODY_TOP = 0.001F;
            PANZER_HEAD = 0.001F;
            PANZER_HEAD_TOP = 0.001F;
            PANZER_TNT_TYPE = 1.0F;
            explodeName = null;
            HITBY_MASK = -2;
            SPEED_AVERAGE = TankGeneric.KmHourToMSec(1.0F);
            SPEED_MAX = TankGeneric.KmHourToMSec(2.0F);
            SPEED_BACK = TankGeneric.KmHourToMSec(1.0F);
            ROT_SPEED_MAX = 3.6F;
            ROT_INVIS_ANG = 360F;
            BEST_SPACE = 2.0F;
            AFTER_COLLISION_DIST = 0.1F;
            COMMAND_INTERVAL = 20F;
            STAY_INTERVAL = 30F;
        }
    }


    public int getCRC(int i)
    {
        i = super.getCRC(i);
        i = Finger.incInt(i, heightAboveLandSurface);
        i = Finger.incInt(i, collisionStage);
        i = Finger.incInt(i, dying);
        i = Finger.incInt(i, codeOfUnderlyingBridgeSegment);
        if(mov != null)
        {
            i = Finger.incInt(i, mov.rotatingInPlace);
            i = Finger.incInt(i, mov.srcPos.x);
            i = Finger.incInt(i, mov.srcPos.y);
            i = Finger.incInt(i, mov.srcPos.z);
            if(mov.dstPos != null)
            {
                i = Finger.incInt(i, mov.dstPos.x);
                i = Finger.incInt(i, mov.dstPos.y);
                i = Finger.incInt(i, mov.dstPos.z);
            }
        }
        for(int j = 0; j < arms.length; j++)
        {
            i = Finger.incInt(i, arms[j].headYaw);
            i = Finger.incInt(i, arms[j].gunPitch);
            if(arms[j].aim != null)
            {
                i = Finger.incInt(i, arms[j].aim.timeTot);
                i = Finger.incInt(i, arms[j].aim.timeCur);
            }
        }

        return i;
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

    protected final boolean Head360(GunProps gunprops)
    {
        return gunprops.HEAD_YAW_RANGE.fullcircle();
    }

    protected final boolean Head360()
    {
        return prop.gunProperties[0].HEAD_YAW_RANGE.fullcircle();
    }

    protected final boolean StayWhenFire(Aim aim)
    {
        for(int i = 0; i < arms.length; i++)
            if(arms[i].aim.equals(aim))
                return prop.gunProperties[i].STAY_WHEN_FIRE;

        return false;
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
        for(int i = 0; i < arms.length; i++)
            if(arms[i].aim.isInFiringMode())
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
            return;
        if(shot.power <= 0.0F)
            return;
        if(shot.powerType == 1)
            if(RndB(0.07692308F))
            {
                return;
            } else
            {
                Die(shot.initiator, false);
                return;
            }
        float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP);
        f *= Rnd(0.93F, 1.07F);
        float f1 = prop.fnShotPanzer.Value(shot.power, f);
        if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
            Die(shot.initiator, false);
    }

    public static boolean splintersKill(Explosion explosion, TableFunction2 tablefunction2, float f, float f1, ActorMesh actormesh, float f2, float f3, float f4,
            float f5, float f6, float f7, float f8, float f9)
    {
        if(explosion.power <= 0.0F)
            return false;
        actormesh.pos.getAbs(p);
        float af[] = new float[2];
        explosion.computeSplintersHit(p, actormesh.mesh().collisionR(), f2, af);
        af[0] *= f * 0.85F + (1.0F - f) * 1.15F;
        int i = (int)af[0];
        if(i <= 0)
            return false;
        Vector3d vector3d = new Vector3d(p.x - explosion.p.x, p.y - explosion.p.y, p.z - explosion.p.z);
        double d = vector3d.length();
        if(d < 0.0010000000474974513D)
            vector3d.set(1.0D, 0.0D, 0.0D);
        else
            vector3d.scale(1.0D / d);
        float f10 = Shot.panzerThickness(actormesh.pos.getAbsOrient(), vector3d, false, f4, f5, f6, f7, f8, f9);
        float f11 = Shot.panzerThickness(actormesh.pos.getAbsOrient(), vector3d, true, f4, f5, f6, f7, f8, f9);
        int j = (int)((float)i * f3 + 0.5F);
        int k = i - j;
        if(k < 0)
            k = 0;
        float f12 = 0.015F * af[1] * af[1] * 0.5F;
        float f13 = tablefunction2.Value(f12, f10);
        float f14 = tablefunction2.Value(f12, f11);
        if(k > 0 && f13 <= 1.0F || j > 0 && f14 <= 1.0F)
            return true;
        float f15 = 0.0F;
        if(f13 < 1000F)
        {
            float f16 = 1.0F / f13;
            while(k-- > 0)
                f15 += (1.0F - f15) * f16;
        }
        if(f14 < 1000F)
        {
            float f17 = 1.0F / f14;
            while(j-- > 0)
                f15 += (1.0F - f15) * f17;
        }
        return f15 > 0.001F && f15 >= f1;
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying != 0)
            return;
        if(isNetMirror() && explosion.isMirage())
            return;
        if(explosion.power <= 0.0F)
            return;
        if(explosion.powerType == 1)
        {
            if(splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP))
                Die(explosion.initiator, false);
            return;
        }
        if(explosion.powerType == 2 && explosion.chunkName != null)
        {
            Die(explosion.initiator, false);
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
            Die(explosion.initiator, true);
    }

    private void neverDust()
    {
        if(dust != null)
        {
            dust._finish();
            dust = null;
        }
    }

    private void RunSmoke(float f, float f1)
    {
        boolean flag = RndB(f);
        String s = flag ? "SmokeHead" : "SmokeEngine";
        Explosions.runByName("_TankSmoke_", this, s, "", Rnd(f1, f1 * 1.6F));
    }

    private void ShowExplode()
    {
        Explosions.runByName(prop.explodeName, this, "", "", -1F);
    }

    private void MakeCrush()
    {
        dying = 2;
        Point3d point3d = pos.getAbsPoint();
        long l = (long)((point3d.x % 2.1000000000000001D) * 221D + (point3d.y % 3.1000000000000001D) * 211D * 211D);
        RangeRandom rangerandom = new RangeRandom(l);
        float af[] = new float[3];
        float af1[] = new float[3];
        for(int i = 0; i < arms.length; i++)
        {
            af[0] = af[1] = af[2] = 0.0F;
            af1[0] = af1[1] = af1[2] = 0.0F;
            af1[0] = arms[i].headYaw + rangerandom.nextFloat(-45F, 45F);
            af1[1] = rangerandom.nextFloat(-3F, 0.0F);
            af1[2] = rangerandom.nextFloat(-9F, 9F);
            af[2] = -rangerandom.nextFloat(0.0F, 0.1F);
            String s = "";
            if(i != 0)
                s = "_" + i;
            hierMesh().chunkSetLocate("Head" + s, af, af1);
            af[0] = af[1] = af[2] = 0.0F;
            af1[0] = af1[1] = af1[2] = 0.0F;
            af1[0] = -(prop.gunProperties[i].GUN_MIN_PITCH - rangerandom.nextFloat(6F, 10F));
            af1[1] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(14F, 25F);
            hierMesh().chunkSetLocate("Gun" + s, af, af1);
        }

        af[0] = af[1] = af[2] = 0.0F;
        af1[0] = af1[1] = af1[2] = 0.0F;
        af1[1] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(1.0F, 5F);
        af1[2] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(7F, 13F);
        hierMesh().chunkSetLocate("Body", af, af1);
        engineSFX = null;
        engineSTimer = 0x5f5e0ff;
        breakSounds();
        neverDust();
        if(prop.meshName2 == null)
        {
            mesh().makeAllMaterialsDarker(0.22F, 0.35F);
            heightAboveLandSurface2 = heightAboveLandSurface;
            point3d.z -= rangerandom.nextFloat(0.3F, 0.6F);
        } else
        {
            setMesh(prop.meshName2);
            heightAboveLandSurface2 = 0.0F;
            int j = mesh().hookFind("Ground_Level");
            if(j != -1)
            {
                Matrix4d matrix4d = new Matrix4d();
                mesh().hookMatrix(j, matrix4d);
                heightAboveLandSurface2 = (float)(-matrix4d.m23);
            }
            point3d.z += heightAboveLandSurface2 - heightAboveLandSurface;
        }
        pos.setAbs(point3d);
        pos.reset();
    }

    private void Die(Actor actor, boolean flag)
    {
        if(isNetMirror())
        {
            send_DeathRequest(actor);
            return;
        }
        for(int i = 0; i < arms.length; i++)
        {
            if(arms[i].aim != null)
            {
                arms[i].aim.forgetAiming();
                arms[i].aim = null;
            }
            if(arms[i].gun != null)
            {
                destroy(arms[i].gun);
                arms[i].gun = null;
            }
        }

        collisionStage = 1;
        int j = ((ChiefGround)getOwner()).getCodeOfBridgeSegment(this);
        if(j >= 0)
        {
            if(BridgeSegment.isEncodedSegmentDamaged(j))
            {
                absoluteDeath(actor);
                return;
            }
            LongBridge.AddTraveller(j, this);
            codeOfUnderlyingBridgeSegment = j;
        }
        ((ChiefGround)getOwner()).Detach(this, actor);
        World.onActorDied(this, actor);
        if(isNet() || prop.meshName2 == null)
            flag = true;
        if(!flag)
            flag = RndB(0.35F);
        if(flag)
        {
            ShowExplode();
            RunSmoke(0.3F, 15F);
            if(isNetMaster())
            {
                send_DeathCommand(actor);
                Point3d point3d = simplifyPos(pos.getAbsPoint());
                Orient orient = simplifyOri(pos.getAbsOrient());
                float af[] = new float[arms.length];
                float af1[] = new float[arms.length];
                for(int k = 0; k < arms.length; k++)
                {
                    af[k] = simplifyAng(arms[k].headYaw);
                    af1[k] = simplifyAng(arms[k].gunPitch);
                }

                setPosition(point3d, orient, af, af1);
            }
            MakeCrush();
        } else
        {
            dying = 1;
            dyingDelay = SecsToTicks(Rnd(6F, 12F));
            mov.switchToRotate(this, (RndB(0.5F) ? 1.0F : -1F) * Rnd(70F, 170F), prop.ROT_SPEED_MAX);
            RunSmoke(0.2F, 17F);
        }
    }

    private void DieMirror(Actor actor, boolean flag)
    {
        if(!isNetMirror())
            System.out.println("Internal error in TankGeneric: DieMirror");
        for(int i = 0; i < arms.length; i++)
        {
            if(arms[i].aim != null)
            {
                arms[i].aim.forgetAll();
                arms[i].aim = null;
            }
            if(arms[i].gun != null)
            {
                destroy(arms[i].gun);
                arms[i].gun = null;
            }
        }

        collisionStage = 1;
        ((ChiefGround)getOwner()).Detach(this, actor);
        World.onActorDied(this, actor);
        if(flag)
        {
            ShowExplode();
            RunSmoke(0.3F, 15F);
        }
        MakeCrush();
    }

    public void destroy()
    {
        if(dust != null && !dust.isDestroyed())
            dust._finish();
        dust = null;
        engineSFX = null;
        engineSTimer = 0x5f5e0ff;
        breakSounds();
        if(codeOfUnderlyingBridgeSegment >= 0)
            LongBridge.DelTraveller(codeOfUnderlyingBridgeSegment, this);
        if(arms != null)
        {
            for(int i = 0; i < arms.length; i++)
            {
                if(arms[i].aim != null)
                {
                    arms[i].aim.forgetAll();
                    arms[i].aim = null;
                }
                if(arms[i].gun != null)
                {
                    destroy(((com.maddox.rts.Destroy) (arms[i].gun)));
                    arms[i].gun = null;
                }
            }

        }
        super.destroy();
    }

    private void setPosition(Point3d point3d, Orient orient, float af[], float af1[])
    {
        for(int i = 0; i < arms.length; i++)
        {
            arms[i].headYaw = af[i];
            arms[i].gunPitch = af1[i];
            String s = "";
            if(i != 0)
                s = "_" + i;
            hierMesh().chunkSetAngles("Head" + s, arms[i].headYaw, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Gun" + s, -arms[i].gunPitch, 0.0F, 0.0F);
        }

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

    private TankGeneric(TankProperties tankproperties, Actor actor)
    {
        super(tankproperties.meshName);
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
        dyingDelay = 0L;
        codeOfUnderlyingBridgeSegment = -1;
        outCommand = new NetMsgFiltered();
        prop = tankproperties;
        collide(true);
        drawing(true);
        setOwner(actor);
        codeName = tankproperties.codeName;
        setName(actor.name() + codeName);
        setArmy(actor.getArmy());
        if(mesh().hookFind("SmokeHead") < 0)
            System.out.println("Tank " + getClass().getName() + ": hook SmokeHead not found");
        if(mesh().hookFind("SmokeEngine") < 0)
            System.out.println("Tank " + getClass().getName() + ": hook SmokeEngine not found");
        if(mesh().hookFind("Ground_Level") < 0)
            System.out.println("Tank " + getClass().getName() + ": hook Ground_Level not found");
        heightAboveLandSurface = 0.0F;
        int i = mesh().hookFind("Ground_Level");
        if(i != -1)
        {
            Matrix4d matrix4d = new Matrix4d();
            mesh().hookMatrix(i, matrix4d);
            heightAboveLandSurface = (float)(-matrix4d.m23);
        }
        HookNamed hooknamed = null;
        HookNamed hooknamed1 = null;
        try
        {
            hooknamed = new HookNamed(this, "DustL");
            hooknamed1 = new HookNamed(this, "DustR");
        }
        catch(Exception exception1)
        {
            hooknamed = hooknamed1 = null;
        }
        if(hooknamed == null || hooknamed1 == null)
        {
            hooknamed = hooknamed1 = null;
            dust = null;
        } else
        {
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            Loc loc3 = new Loc();
            hooknamed.computePos(this, loc, loc1);
            hooknamed1.computePos(this, loc, loc3);
            Loc loc4 = new Loc();
            loc4.interpolate(loc1, loc3, 0.5F);
            dust = Eff3DActor.New(this, null, loc4, 1.0F, "Effects/Smokes/TankDust.eff", -1F);
            if(dust != null)
                dust._setIntesity(0.0F);
        }
        if(!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 101)
        {
            int j = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if(netchannel == null)
                net = new Master(this);
            else
            if(j != 0)
                net = new Mirror(this, netchannel, j);
        }
        actorsAimed = new Actor[prop.gunProperties.length];
        arms = new FireDevice[prop.gunProperties.length];
        for(int k = 0; k < arms.length; k++)
        {
            arms[k] = new FireDevice();
            arms[k].gun = null;
            try
            {
                arms[k].gun = (Gun)prop.gunProperties[k].gunClass.newInstance();
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Tank: Can't create gun '" + prop.gunProperties[k].gunClass.getName() + "'");
            }
            String s = "";
            if(k != 0)
                s = "_" + k;
            arms[k].gun.set(this, "Gun" + s);
            arms[k].gun.loadBullets(isNetMirror() ? -1 : prop.gunProperties[k].MAX_SHELLS);
            arms[k].headYaw = prop.gunProperties[k].HEAD_STD_YAW;
            arms[k].gunPitch = prop.gunProperties[k].GUN_STD_PITCH;
            hierMesh().chunkSetAngles("Head" + s, arms[k].headYaw, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Gun" + s, -arms[k].gunPitch, 0.0F, 0.0F);
            arms[k].headYaw = prop.gunProperties[k].HEAD_STD_YAW;
            arms[k].gunPitch = prop.gunProperties[k].GUN_STD_PITCH;
            int l = hierMesh().chunkFind("Head" + s);
            mesh().setCurChunk(l);
            Loc loc2 = new Loc();
            hierMesh().getChunkLocObj(loc2);
            Point3d point3d = new Point3d();
            loc2.get(point3d);
            arms[k].fireOffset = new Point3d();
            arms[k].fireOffset.set(point3d);
            Orient orient = new Orient();
            loc2.get(orient);
            arms[k].fireOrient = new Orient();
            arms[k].fireOrient.set(orient);
        }

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

    private void send_FireCommand(Actor actor, int i, float f, int j)
    {
        if(!isNetMaster() || !net.isMirrored())
            return;
        if(!Actor.isValid(actor) || !actor.isNet())
            return;
        i &= 0xff;
        if(f < 0.0F)
            try
            {
                outCommand.unLockAndClear();
                outCommand.writeByte(84);
                outCommand.writeNetObj(actor.net);
                outCommand.writeByte(i);
                outCommand.writeInt(j);
                outCommand.setIncludeTime(false);
                net.post(Time.current(), outCommand);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        else
            try
            {
                outCommand.unLockAndClear();
                outCommand.writeByte(70);
                outCommand.writeFloat(f);
                outCommand.writeNetObj(actor.net);
                outCommand.writeByte(i);
                outCommand.writeInt(j);
                outCommand.setIncludeTime(true);
                net.post(Time.current(), outCommand);
            }
            catch(Exception exception1)
            {
                System.out.println(exception1.getMessage());
                exception1.printStackTrace();
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
            sendPose(netmsgguaranted, 0);
            if(flag)
                netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            sendPose(netmsgguaranted, 1);
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

    static int packAng(float f)
    {
        return 0xff & (int)((f * 256F) / 360F);
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

    static float unpackAng(int i)
    {
        return ((float)i * 360F) / 256F;
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

    static float readPackedAng(NetMsgInput netmsginput)
        throws IOException
    {
        return unpackAng(netmsginput.readByte());
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

    private void sendPose(NetMsgGuaranted netmsgguaranted, int i)
        throws IOException
    {
        if(i == 0)
        {
            netmsgguaranted.write(packPos(pos.getAbsPoint()));
            netmsgguaranted.write(packOri(pos.getAbsOrient()));
            netmsgguaranted.writeByte(packAng(arms[0].headYaw));
            netmsgguaranted.writeByte(packAng(arms[0].gunPitch));
        } else
        {
            for(int j = 1; j < arms.length; j++)
            {
                netmsgguaranted.writeByte(packAng(arms[j].headYaw));
                netmsgguaranted.writeByte(packAng(arms[j].gunPitch));
            }

        }
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        byte byte0 = ((byte)(dying != 0 ? 105 : 73));
        netmsgguaranted.writeByte(byte0);
        sendPose(netmsgguaranted, 0);
        sendPose(netmsgguaranted, 1);
        net.postTo(netchannel, netmsgguaranted);
    }

    public void startMove()
    {
        if(!interpEnd("move"))
        {
            mov = new Moving();
            for(int i = 0; i < arms.length; i++)
            {
                if(arms[i].aim != null)
                {
                    arms[i].aim.forgetAll();
                    arms[i].aim = null;
                }
                arms[i].aim = new Aim(this, isNetMirror());
            }

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
        return new UnitInPackedForm(codeName, i, j, SpeedAverage(), BestSpace(), WeaponsMask(), HitbyMask());
    }

    public int WeaponsMask()
    {
        int i = 0;
        for(int j = 0; j < arms.length; j++)
            i |= prop.gunProperties[j].WEAPONS_MASK;

        return i;
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
        if(this instanceof TgtTank)
        {
            if(abulletproperties[0].cumulativePower > 0.0F)
                return 0;
            if(abulletproperties[1].cumulativePower > 0.0F)
                return 1;
            if(abulletproperties[0].power <= 0.0F)
                return 0;
            if(abulletproperties[1].power <= 0.0F)
                return 1;
        } else
        {
            if(abulletproperties[0].power <= 0.0F)
                return 0;
            if(abulletproperties[1].power <= 0.0F)
                return 1;
            if(abulletproperties[0].cumulativePower > 0.0F)
                return 0;
            if(abulletproperties[1].cumulativePower > 0.0F)
                return 1;
        }
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        return abulletproperties[0].powerType != 0 ? 0 : 1;
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

    public float AttackMaxDistance()
    {
        float f = 0.0F;
        for(int i = 0; i < arms.length; i++)
            if(prop.gunProperties[i].ATTACK_MAX_DISTANCE > f)
                f = prop.gunProperties[i].ATTACK_MAX_DISTANCE;

        return f;
    }

    public float AttackMaxDistance(Aim aim)
    {
        for(int i = 0; i < arms.length; i++)
            if(arms[i].aim.equals(aim))
                return prop.gunProperties[i].ATTACK_MAX_DISTANCE;

        return 0.0F;
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
        Explosions.Tank_ExplodeCollapse(pos.getAbsPoint());
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
            Explosions.Tank_ExplodeCollapse(pos.getAbsPoint());
        else
            Explosions.Tank_ExplodeCollapse(pos.getAbsPoint());
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

    public void gunStartParking(Aim aim)
    {
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                arms[i].aim.setRotationForParking(arms[i].headYaw, arms[i].gunPitch, prop.gunProperties[i].HEAD_STD_YAW, prop.gunProperties[i].GUN_STD_PITCH, prop.gunProperties[i].HEAD_YAW_RANGE, prop.gunProperties[i].HEAD_MAX_YAW_SPEED, prop.gunProperties[i].GUN_MAX_PITCH_SPEED);
                break;
            }
            i++;
        } while(true);
    }

    public float getReloadingTime(Aim aim)
    {
        if(isNetMirror())
            return 1.0F;
        for(int i = 0; i < arms.length; i++)
            if(aim.equals(arms[i].aim))
                if(arms[i].gun.haveBullets())
                {
                    return prop.gunProperties[i].DELAY_AFTER_SHOOT;
                } else
                {
                    arms[i].gun.loadBullets(prop.gunProperties[i].MAX_SHELLS);
                    return 120F;
                }

        return prop.gunProperties[0].DELAY_AFTER_SHOOT;
    }

    public float chainFireTime(Aim aim)
    {
        for(int i = 0; i < arms.length; i++)
            if(aim.equals(arms[i].aim))
                return prop.gunProperties[i].CHAINFIRE_TIME > 0.0F ? prop.gunProperties[i].CHAINFIRE_TIME * Rnd(0.75F, 1.25F) : 0.0F;

        return prop.gunProperties[0].CHAINFIRE_TIME > 0.0F ? prop.gunProperties[0].CHAINFIRE_TIME * Rnd(0.75F, 1.25F) : 0.0F;
    }

    public float probabKeepSameEnemy(Actor actor)
    {
        return Head360() ? 0.8F : 0.0F;
    }

    public float minTimeRelaxAfterFight()
    {
        return Head360() ? 0.0F : 10F;
    }

    public void gunInMove(boolean flag, Aim aim)
    {
        int i = 0;
        for (int j = 0; j < this.arms.length; j++) {
            if(j >= arms.length)
                break;
            if(aim.equals(arms[j].aim))
            {
                i = j;
                break;
            }
        }
        float f = arms[i].aim.t();
        String s = "";
        if(i != 0)
            s = "_" + i;
        if(Head360(prop.gunProperties[i]) || flag || !aim.bodyRotation)
        {
            arms[i].headYaw = arms[i].aim.anglesYaw.getDeg(f);
            arms[i].gunPitch = arms[i].aim.anglesPitch.getDeg(f);
            hierMesh().chunkSetAngles("Head" + s, arms[i].headYaw, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Gun" + s, -arms[i].gunPitch, 0.0F, 0.0F);
            pos.inValidate(false);
            return;
        }
        float f1 = aim.anglesYaw.getDeg(f);
        pos.getAbs(o);
        o.setYaw(f1);
        if(mov != null && mov.normal != null)
            if(mov.normal.z < 0.0F)
            {
                Engine.land().N(mov.srcPos.x, mov.srcPos.y, n);
                o.orient(n);
            } else
            {
                o.orient(mov.normal);
            }
        pos.setAbs(o);
        arms[i].gunPitch = aim.anglesPitch.getDeg(f);
        hierMesh().chunkSetAngles("Gun" + s, -arms[i].gunPitch, 0.0F, 0.0F);
        pos.inValidate(false);
    }

    public Actor findEnemy(Aim aim)
    {
        if(isNetMirror())
            return null;
        int i = 0;
        int j = 0;
        do
        {
            if(j >= arms.length)
                break;
            if(aim.equals(arms[j].aim))
            {
                i = j;
                break;
            }
            j++;
        } while(true);
        Actor actor = null;
        Object obj = (ChiefGround)getOwner();
        if(obj != null)
        {
            if(((ChiefGround) (obj)).getCodeOfBridgeSegment(this) >= 0)
                return null;
            // By western, expanded for flags Intercept missiles and Radar use
            actor = ((ChiefGround) (obj)).GetNearestEnemy(pos.getAbsPoint(), AttackMaxDistance(aim), WeaponsMask(), prop.gunProperties[i].ATTACK_FAST_TARGETS ? -1F : KmHourToMSec(100F), prop.gunProperties[i].ATTACK_MISSILES, prop.gunProperties[i].USE_RADAR_SEARCH, prop.gunProperties[i].SOUND_PW_RADAR_SEARCH, (Actor)this);

            if( bLogDetail ) System.out.println("TankGeneric(" + actorString(this) + ") - findEnemy(aim) - GetNearestEnemy()=" + actorString(actor));
        }
        if(actor == null)
            return null;
        obj = null;
        if(arms[i].gun.prop != null)
        {
            int k = ((Prey)actor).chooseBulletType(arms[i].gun.prop.bullet);
            if( bLogDetail ) System.out.println("TankGeneric(" + actorString(this) + ") - findEnemy(aim) - chooseBulletType k=" + k);
            if(k < 0)
                return null;
            obj = arms[i].gun.prop.bullet[k];
        }
        int l = ((Prey)actor).chooseShotpoint(((BulletProperties) (obj)));
        if( bLogDetail ) System.out.println("TankGeneric(" + actorString(this) + ") - findEnemy(aim) - chooseShotpoint l=" + l);
        if(l < 0)
        {
            return null;
        } else
        {
            arms[i].aim.shotpoint_idx = l;
            double d = distance(actor);
            d /= AttackMaxDistance(aim);
            aim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
            aim.scaleAimingError((float)(211.25999450683594D * d));

            if( bLogDetail ) System.out.println("TankGeneric(" + actorString(this) + ") - findEnemy(aim) - return actor=" + actorString(actor));

            // By western, Notice to the RadarWarningReceiver plane when Radar is used
            if(prop.gunProperties[i].USE_RADAR_SEARCH && (actor instanceof TypeRadarWarningReceiver))
            {
                ((TypeRadarWarningReceiver)actor).myRadarLockYou((Actor)this, prop.gunProperties[i].SOUND_PW_RADAR_LOCK);
                actorsAimed[i] = actor;
            }
            else
                actorsAimed[i] = null;

            return actor;
        }
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim)
    {
        int j = 0;
        int k = 0;
        do
        {
            if(k >= arms.length)
                break;
            if(aim.equals(arms[k].aim))
            {
                j = k;
                break;
            }
            k++;
        } while(true);
        if(i == 1 || !Head360(prop.gunProperties[j]) && aim.bodyRotation)
        {
            if(collisionStage != 0)
                return false;
            if(StayWhenFire(aim))
                mov.switchToStay(1.5F);
        }
        if(!isNetMirror())
            send_FireCommand(actor, arms[j].aim.shotpoint_idx, i != 0 ? f : -1F, j);
        return true;
    }

    private void Track_Mirror(int i, Actor actor, int j)
    {
        if(dying != 0)
            return;
        if(actor == null)
            return;
        if(arms[i].aim == null)
        {
            return;
        } else
        {
            arms[i].aim.passive_StartFiring(0, actor, j, 0.0F);
            return;
        }
    }

    private void Fire_Mirror(int i, Actor actor, int j, float f)
    {
        if(dying != 0)
            return;
        if(actor == null)
            return;
        if(arms[i].aim == null)
            return;
        if(f <= 0.2F)
            f = 0.2F;
        if(f >= 15F)
            f = 15F;
        arms[i].aim.passive_StartFiring(1, actor, j, f);
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean flag)
    {
        if(!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
            return 0;
        int i = 0;
        for (int j = 0; j < this.arms.length; j++) {
            if(j >= arms.length)
                break;
            if(aim.equals(arms[j].aim))
            {
                i = j;
                break;
            }
        }
        if(arms[i].gun instanceof CannonMidrangeGeneric)
        {
            int k = ((Prey)actor).chooseBulletType(arms[i].gun.prop.bullet);
            if(k < 0)
                return 0;
            ((CannonMidrangeGeneric)arms[i].gun).setBulletType(k);
        }
        boolean bool = ((Prey)actor).getShotpointOffset(arms[i].aim.shotpoint_idx, p1);
        if(!bool)
            return 0;
        float f1 = f * Rnd(0.8F, 1.2F);
        if(!Aimer.aim((BulletAimer)arms[i].gun, actor, this, f1, p1, arms[i].fireOffset))
            return 0;
        Point3d point3d = new Point3d();
        Aimer.getPredictedTargetPosition(point3d);
        point3d.add(aim.getAimingError());
        Point3d point3d1 = Aimer.getHunterFirePoint();
        float f2 = 0.19F;
        double d = point3d.distance(point3d1);
        double d1 = point3d.z;
        point3d.sub(point3d1);
        point3d.scale(Rnd(0.94999999999999996D, 1.05D));
        point3d.add(point3d1);
        if(f1 > 0.001F)
        {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            point3d2.add(aim.getAimingError());
            tmpv.sub(point3d, point3d2);
            double d2 = tmpv.length();
            if(d2 > 0.001D)
            {
                float f8 = (float)d2 / f1;
                if(f8 > 200F)
                    f8 = 200F;
                float f9 = f8 * 0.02F;
                point3d2.sub(point3d1);
                double d3 = point3d2.x * point3d2.x + point3d2.y * point3d2.y + point3d2.z * point3d2.z;
                if(d3 > 0.01D)
                {
                    float f10 = (float)tmpv.dot(point3d2);
                    f10 /= (float)(d2 * Math.sqrt(d3));
                    f10 = (float)Math.sqrt(1.0F - f10 * f10);
                    f9 *= 0.4F + 0.6F * f10;
                }
                int k = Mission.curCloudsType();
                if(k >= 3)
                {
                    float f11 = k < 5 ? 500F : 250F;
                    float f12 = (float)(d / (double)f11);
                    if(f12 > 1.0F)
                    {
                        if(f12 > 10F)
                            return 0;
                        f12 = ((f12 - 1.0F) / 9F) * 2.0F + 1.0F;
                        f9 *= f12;
                    }
                }
                if(k >= 3 && d1 > (double)Mission.curCloudsHeight())
                    f9 *= 1.25F;
                f2 += f9;
            }
        }
        if(aim.getAimingError().length() > 1.1100000143051147D)
            aim.scaleAimingError(0.973F);
        if(World.Sun().ToSun.z < -0.15F)
        {
            float f5 = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if(f5 >= 1.0F)
                f5 = 1.0F;
            if((actor instanceof Aircraft) && Time.current() - ((Aircraft)actor).tmSearchlighted < 1000L)
                f5 = 0.0F;
            f2 += 12F * f5;
        }
        float f6 = (float)actor.getSpeed(null);
        float f7 = 83.33334F;
        f6 = f6 < f7 ? f6 / f7 : 1.0F;
        f2 += f6 * prop.gunProperties[i].FAST_TARGETS_ANGLE_ERROR;
        Vector3d vector3d = new Vector3d();
        if(!((BulletAimer)arms[i].gun).FireDirection(point3d1, point3d, vector3d))
            return 0;
        float f3;
        float f4;
        float f5;
        if(flag)
        {
            f3 = 99999F;
            f5 = 99999F;
            f4 = 99999F;
        } else
        {
            f3 = prop.gunProperties[i].HEAD_MAX_YAW_SPEED;
            f5 = prop.gunProperties[i].GUN_MAX_PITCH_SPEED;
            f4 = prop.ROT_SPEED_MAX;
        }
        if(i != 0)
            f4 = -1F;
        Orient orient = pos.getAbs().getOrient();
        f7 = 0.0F;
        if(i == 0 && !Head360())
            f7 = orient.getYaw();
        Orient orient1 = new Orient();
        orient1.add(arms[i].fireOrient, orient);
        int j = aim.setRotationForTargeting(this, orient1, point3d1, arms[i].headYaw, arms[i].gunPitch, vector3d, f2, f1, prop.gunProperties[i].HEAD_YAW_RANGE, prop.gunProperties[i].GUN_MIN_PITCH, prop.gunProperties[i].GUN_MAX_PITCH, f3, f5, f4);
        if(i == 0 && !Head360() && j != 0 && aim.bodyRotation)
            aim.anglesYaw.rotateDeg(f7);
        return j;
    }

    public void singleShot(Aim aim)
    {
        if(StayWhenFire(aim))
            mov.switchToStay(1.5F);
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                arms[i].gun.shots(1);
                if(actorsAimed[i] != null && Actor.isValid(actorsAimed[i]) && (actorsAimed[i] instanceof TypeRadarWarningReceiver) && prop.gunProperties[i].USE_RADAR_SEARCH)
                    ((TypeRadarWarningReceiver)actorsAimed[i]).myRadarLockYou((Actor)this, prop.gunProperties[i].SOUND_PW_RADAR_LOCK);
                break;
            }
            i++;
        } while(true);
    }

    public void startFire(Aim aim)
    {
        if(StayWhenFire(aim))
            mov.switchToStay(1.5F);
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                arms[i].gun.shots(-1);
                if(actorsAimed[i] != null && Actor.isValid(actorsAimed[i]) && (actorsAimed[i] instanceof TypeRadarWarningReceiver) && prop.gunProperties[i].USE_RADAR_SEARCH)
                    ((TypeRadarWarningReceiver)actorsAimed[i]).myRadarLockYou((Actor)this, prop.gunProperties[i].SOUND_PW_RADAR_LOCK);
                break;
            }
            i++;
        } while(true);
    }

    public void continueFire(Aim aim)
    {
        if(StayWhenFire(aim))
            mov.switchToStay(1.5F);
    }

    public void stopFire(Aim aim)
    {
        if(StayWhenFire(aim))
            mov.switchToStay(1.5F);
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                arms[i].gun.shots(0);
                break;
            }
            i++;
        } while(true);
    }

    public boolean getHasRadar()
    {
        boolean bHasRadar = false;

        for(int i = 0; i < arms.length; i++)
            if(prop.gunProperties[i].USE_RADAR_SEARCH)
                bHasRadar = true;

        return bHasRadar;
    }

    private static String actorString(Actor actor) {
        if(!Actor.isValid(actor)) return "(InvalidActor)";
        String s;
        try {
            s = actor.getClass().getName();
        } catch(Exception e) {
            System.out.println("Missile - actorString(): Cannot resolve class name of " + actor);
            return "(NoClassnameActor)";
        }
        int i = s.lastIndexOf('.');
        String strSection = s.substring(i + 1);
        strSection =  strSection + '@' + Integer.toHexString(actor.hashCode());
        return strSection;
    }

    /*private*/ static float Thicknesses[] = null;
    /*private*/ static float Energies[] = null;
    /*private*/ static float NumShots_Thickness_Energy[][] = (float[][])null;
    private TankProperties prop;
    private int codeName;
    private float heightAboveLandSurface;
    private float heightAboveLandSurface2;
    public UnitData udata;
    private Moving mov;
    protected Eff3DActor dust;
    protected SoundFX engineSFX;
    protected int engineSTimer;
    protected int ticksIn8secs;
    private Actor actorsAimed[];
    private FireDevice arms[];
    private int collisionStage;
    static final int COLLIS_NO_COLLISION = 0;
    static final int COLLIS_JUST_COLLIDED = 1;
    static final int COLLIS_MOVING_FROM_COLLISION = 2;
    private Vector2d collisVector;
    private Actor collidee;
    private StaticObstacle obs;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_SMOKE = 1;
    static final int DYING_DEAD = 2;
    private long dyingDelay;
    private int codeOfUnderlyingBridgeSegment;
    private static TankProperties constr_arg1 = null;
    private static Actor constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Point3d p1 = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
    private static Vector3d tmpv = new Vector3d();
    private NetMsgFiltered outCommand;

    private static boolean bLogDetail = false;
}
