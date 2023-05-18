package com.maddox.il2.objects.ships;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.StrengthProperties;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.VisibilityLong;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
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

public class ShipGeneric extends ActorHMesh
    implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, ActorAlign, HunterInterface, VisibilityLong
{
    public static class SPAWN
        implements ActorSpawn
    {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1)
        {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if(f2 == -9865.345F || f2 < f || f2 > f1)
            {
                if(f2 == -9865.345F)
                    System.out.println("Ship: Value of [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Ship: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
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
                    System.out.print("Ship: Value of [" + s + "]:<" + s1 + "> not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Ship \"" + s + "\" is not (correctly) declared in ships.ini file!");
                }
                return null;
                // ---
            } else
            {
                return new String(s2);
            }
        }

        /*private*/ static String getS(SectFile sectfile, String s, String s1, String s2)
        {
            String s3 = sectfile.get(s, s1);
            if(s3 == null || s3.length() <= 0)
                return s2;
            else
                return new String(s3);
        }

        private static ShipProperties LoadShipProperties(SectFile sectfile, String s, Class class1)
        {
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            String checkMesh = getS(sectfile, s, "Mesh");
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (checkMesh == null || checkMesh.length() == 0)) return null;
            // TODO: ---
            ShipProperties shipproperties = new ShipProperties();
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//            shipproperties.meshName = getS(sectfile, s, "Mesh");
            shipproperties.meshName = checkMesh;
            // TODO: ---
            shipproperties.soundName = getS(sectfile, s, "SoundMove");
            if(shipproperties.soundName.equalsIgnoreCase("none"))
                shipproperties.soundName = null;
            if(!shipproperties.stre.read("Ship", sectfile, null, s))
                throw new RuntimeException("Can't register Ship object");
            int i;
            for(i = 0; sectfile.sectionIndex(s + ":Gun" + i) >= 0; i++);
            shipproperties.guns = new ShipGunProperties[i];
            shipproperties.WEAPONS_MASK = 0;
            shipproperties.ATTACK_MAX_DISTANCE = 1.0F;
            for(int j = 0; j < i; j++)
            {
                shipproperties.guns[j] = new ShipGunProperties();
                ShipGunProperties shipgunproperties = shipproperties.guns[j];
                String s1 = s + ":Gun" + j;
                String s2 = "com.maddox.il2.objects.weapons." + getS(sectfile, s1, "Gun");
                try
                {
                    shipgunproperties.gunClass = Class.forName(s2);
                }
                catch(Exception exception)
                {
                    System.out.println("Ship: Can't find gun class '" + s2 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register Ship object");
                    return null;
                    // ---
                }
                shipgunproperties.WEAPONS_MASK = Gun.getProperties(shipgunproperties.gunClass).weaponType;
                if(shipgunproperties.WEAPONS_MASK == 0)
                {
                    System.out.println("Ship: Undefined weapon type in gun class '" + s2 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register Ship object");
                    return null;
                    // ---
                }
                shipproperties.WEAPONS_MASK |= shipgunproperties.WEAPONS_MASK;
                shipgunproperties.ATTACK_MAX_DISTANCE = getF(sectfile, s1, "AttackMaxDistance", 6F, 50000F);
                shipgunproperties.ATTACK_MAX_RADIUS = getF(sectfile, s1, "AttackMaxRadius", 6F, 50000F);
                shipgunproperties.ATTACK_MAX_HEIGHT = getF(sectfile, s1, "AttackMaxHeight", 6F, 15000F);
                if(shipgunproperties.ATTACK_MAX_DISTANCE > shipproperties.ATTACK_MAX_DISTANCE)
                    shipproperties.ATTACK_MAX_DISTANCE = shipgunproperties.ATTACK_MAX_DISTANCE;
                shipgunproperties.TRACKING_ONLY = false;
                if(sectfile.exist(s1, "TrackingOnly"))
                    shipgunproperties.TRACKING_ONLY = true;
                shipgunproperties.ATTACK_FAST_TARGETS = 1;
                if(sectfile.exist(s1, "FireFastTargets"))
                {
                    float f = getF(sectfile, s1, "FireFastTargets", 0.0F, 2.0F);
                    shipgunproperties.ATTACK_FAST_TARGETS = (int)(f + 0.5F);
                    if(shipgunproperties.ATTACK_FAST_TARGETS > 2)
                        shipgunproperties.ATTACK_FAST_TARGETS = 2;
                }
                shipgunproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
                if(sectfile.exist(s1, "FastTargetsAngleError"))
                {
                    float f1 = getF(sectfile, s1, "FastTargetsAngleError", 0.0F, 45F);
                    shipgunproperties.FAST_TARGETS_ANGLE_ERROR = f1;
                }
                float f2 = getF(sectfile, s1, "HeadMinYaw", -360F, 360F);
                float f3 = getF(sectfile, s1, "HeadStdYaw", -360F, 360F);
                float f4 = getF(sectfile, s1, "HeadMaxYaw", -360F, 360F);
                if(f2 > f4)
                {
                    System.out.println("Ship: Wrong yaw angles in gun #" + j + " of '" + s + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register Ship object");
                    return null;
                    // ---
                }
                if(f3 < f2 || f3 > f4)
                    System.out.println("Ship: Wrong std yaw angle in gun #" + j + " of '" + s + "'");
                shipgunproperties.HEAD_YAW_RANGE.set(f2, f4);
                shipgunproperties.HEAD_STD_YAW = f3;
                shipgunproperties.GUN_MIN_PITCH = getF(sectfile, s1, "GunMinPitch", -15F, 85F);
                shipgunproperties.GUN_STD_PITCH = getF(sectfile, s1, "GunStdPitch", -14F, 89.9F);
                shipgunproperties.GUN_MAX_PITCH = getF(sectfile, s1, "GunMaxPitch", 0.0F, 89.9F);
                shipgunproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s1, "HeadMaxYawSpeed", 0.1F, 999F);
                shipgunproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s1, "GunMaxPitchSpeed", 0.1F, 999F);
                shipgunproperties.DELAY_AFTER_SHOOT = getF(sectfile, s1, "DelayAfterShoot", 0.0F, 999F);
                shipgunproperties.CHAINFIRE_TIME = getF(sectfile, s1, "ChainfireTime", 0.0F, 600F);
            }

            shipproperties.nGuns = i;
            shipproperties.SLIDER_DIST = getF(sectfile, s, "SliderDistance", 5F, 1000F);
            shipproperties.SPEED = ShipGeneric.KmHourToMSec(getF(sectfile, s, "Speed", 0.5F, 200F));
            shipproperties.DELAY_RESPAWN_MIN = 15F;
            shipproperties.DELAY_RESPAWN_MAX = 30F;
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", shipproperties.meshName);
            Property.set(class1, "speed", shipproperties.SPEED);
            return shipproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            ShipGeneric shipgeneric = null;
            try
            {
                ShipGeneric.constr_arg1 = proper;
                ShipGeneric.constr_arg2 = actorspawnarg;
                shipgeneric = (ShipGeneric)cls.newInstance();
                ShipGeneric.constr_arg1 = null;
                ShipGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                ShipGeneric.constr_arg1 = null;
                ShipGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Ship object [class:" + cls.getName() + "]");
                return null;
            }
            return shipgeneric;
        }

        public Class cls;
        public ShipProperties proper;

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
                proper = LoadShipProperties(Statics.getTechnicsFile(), s1, class1);
                
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL && proper == null) return;
                // TODO: ---
                
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + class1.getName());
            }
            cls = class1;
            Spawn.add(cls, this);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
            {
                switch(netmsginput.readByte())
                {
                case 73:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted);
                    }
                    timeOfDeath = netmsginput.readLong();
                    if(timeOfDeath < 0L)
                    {
                        if(dying == 0)
                        {
                            life = 1.0F;
                            setDefaultLivePose();
                            forgetAllAiming();
                        }
                    } else
                    if(dying == 0)
                        Die(null, timeOfDeath, false);
                    return true;

                case 82:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted1);
                    }
                    life = 1.0F;
                    dying = 0;
                    setDiedFlag(false);
                    setMesh(prop.meshName);
                    setDefaultLivePose();
                    forgetAllAiming();
                    bodyDepth = 0.0F;
                    bodyPitch = bodyRoll = 0.0F;
                    setPosition();
                    pos.reset();
                    return true;

                case 68:
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                        post(netmsgguaranted2);
                    }
                    timeOfDeath = netmsginput.readLong();
                    if(timeOfDeath >= 0L && dying == 0)
                    {
                        com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                        Actor actor2 = netobj2 != null ? ((ActorNet)netobj2).actor() : null;
                        Die(actor2, timeOfDeath, true);
                    }
                    return true;
                }
                return false;
            }
            switch(netmsginput.readByte())
            {
            default:
                break;

            case 84:
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 1);
                    out.setIncludeTime(false);
                    postReal(Message.currentRealTime(), out);
                }
                byte byte0 = netmsginput.readByte();
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                int i = netmsginput.readUnsignedByte();
                Track_Mirror(byte0, actor, i);
                break;

            case 70:
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 1);
                    out.setIncludeTime(true);
                    postReal(Message.currentRealTime(), out);
                }
                byte byte1 = netmsginput.readByte();
                com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                Actor actor1 = netobj1 != null ? ((ActorNet)netobj1).actor() : null;
                float f = netmsginput.readFloat();
                float f1 = 0.001F * (float)(Message.currentGameTime() - Time.current()) + f;
                int j = netmsginput.readUnsignedByte();
                Fire_Mirror(byte1, actor1, j, f1);
                break;

            case 68:
                out.unLockAndSet(netmsginput, 1);
                out.setIncludeTime(false);
                postRealTo(Message.currentRealTime(), masterChannel(), out);
                break;
            }
            return true;
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
                return true;
            if(netmsginput.readByte() != 68)
                return false;
            if(dying != 0)
            {
                return true;
            } else
            {
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                Die(actor, -1L, true);
                return true;
            }
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(dying == 0)
            {
                if(path != null)
                {
                    bodyDepth = 0.0F;
                    bodyPitch = bodyRoll = 0.0F;
                    setMovablePosition(NetServerParams.getServerTime());
                }
                if(wakeupTmr == 0L)
                {
                    for(int i = 0; i < prop.nGuns; i++)
                        arms[i].aime.tick_();

                } else
                if(wakeupTmr > 0L)
                    wakeupTmr--;
                else
                if(++wakeupTmr == 0L)
                    if(isAnyEnemyNear())
                        wakeupTmr = ShipGeneric.SecsToTicks(ShipGeneric.Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
                    else
                        wakeupTmr = -ShipGeneric.SecsToTicks(ShipGeneric.Rnd(4F, 7F));
                return true;
            }
            if(dying == 2)
            {
                if(path != null || !Mission.isDeathmatch())
                {
                    eraseGuns();
                    return false;
                }
                if(respawnDelay-- > 0L)
                    return true;
                if(!isNetMaster())
                {
                    respawnDelay = 10000L;
                    return true;
                } else
                {
                    life = 1.0F;
                    dying = 0;
                    wakeupTmr = 0L;
                    setDiedFlag(false);
                    forgetAllAiming();
                    setDefaultLivePose();
                    bodyDepth = 0.0F;
                    bodyPitch = bodyRoll = 0.0F;
                    setPosition();
                    pos.reset();
                    send_RespawnCommand();
                    return true;
                }
            }
            long l = NetServerParams.getServerTime() - timeOfDeath;
            if(l <= 0L)
                l = 0L;
            if(l >= timeForRotation)
            {
                bodyPitch = drownBodyPitch;
                bodyRoll = drownBodyRoll;
            } else
            {
                bodyPitch = drownBodyPitch * ((float)l / (float)timeForRotation);
                bodyRoll = drownBodyRoll * ((float)l / (float)timeForRotation);
            }
            bodyDepth = sinkingDepthSpeed * (float)l * 0.001F * 5F;
            if(bodyDepth >= 5F)
            {
                float f = Math.abs(Geom.sinDeg(bodyPitch) * collisionR());
                f += bodyDepth;
                if(f + bodyDepth >= seaDepth)
                    dying = 2;
                if(bodyDepth > mesh().visibilityR())
                    dying = 2;
            }
            if(path != null)
                setMovablePosition(timeOfDeath);
            else
                setPosition();
            return true;
        }

        Move()
        {
        }
    }

    static class HookNamedZ0 extends HookNamed
    {

        public void computePos(Actor actor, Loc loc, Loc loc1)
        {
            super.computePos(actor, loc, loc1);
            loc1.getPoint().z = 0.25D;
        }

        public HookNamedZ0(ActorMesh actormesh, String s)
        {
            super(actormesh, s);
        }

        public HookNamedZ0(Mesh mesh, String s)
        {
            super(mesh, s);
        }
    }

    private class Segment
    {

        public Point3d posIn;
        public Point3d posOut;
        public float length;
        public long timeIn;
        public long timeOut;
        public float speedIn;
        public float speedOut;

        private Segment()
        {
        }

    }

    public class FiringDevice
    {

        private int id;
        private Gun gun;
        private Aim aime;
        private float headYaw;
        private float gunPitch;











        public FiringDevice()
        {
        }
    }

    public static class ShipProperties
    {

        public String meshName;
        public String soundName;
        public StrengthProperties stre;
        public int WEAPONS_MASK;
        public int HITBY_MASK;
        public float ATTACK_MAX_DISTANCE;
        public float SLIDER_DIST;
        public float SPEED;
        public float DELAY_RESPAWN_MIN;
        public float DELAY_RESPAWN_MAX;
        public ShipGunProperties guns[];
        public int nGuns;

        public ShipProperties()
        {
            meshName = null;
            soundName = null;
            stre = new StrengthProperties();
            WEAPONS_MASK = 4;
            HITBY_MASK = -2;
            ATTACK_MAX_DISTANCE = 1.0F;
            SLIDER_DIST = 1.0F;
            SPEED = 1.0F;
            DELAY_RESPAWN_MIN = 15F;
            DELAY_RESPAWN_MAX = 30F;
            guns = null;
        }
    }

    public static class ShipGunProperties
    {

        public Class gunClass;
        public int WEAPONS_MASK;
        public boolean TRACKING_ONLY;
        public float ATTACK_MAX_DISTANCE;
        public float ATTACK_MAX_RADIUS;
        public float ATTACK_MAX_HEIGHT;
        public int ATTACK_FAST_TARGETS;
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
        public Point3d fireOffset;
        public Orient fireOrient;

        public ShipGunProperties()
        {
            gunClass = null;
            WEAPONS_MASK = 4;
            TRACKING_ONLY = false;
            ATTACK_MAX_DISTANCE = 1.0F;
            ATTACK_MAX_RADIUS = 1.0F;
            ATTACK_MAX_HEIGHT = 1.0F;
            ATTACK_FAST_TARGETS = 1;
            FAST_TARGETS_ANGLE_ERROR = 0.0F;
            HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            HEAD_STD_YAW = 0.0F;
            GUN_MIN_PITCH = -20F;
            GUN_STD_PITCH = -18F;
            GUN_MAX_PITCH = -15F;
            HEAD_MAX_YAW_SPEED = 720F;
            GUN_MAX_PITCH_SPEED = 60F;
            DELAY_AFTER_SHOOT = 1.0F;
            CHAINFIRE_TIME = 0.0F;
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

    /*private*/ boolean RndB(float f)
    {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public static final float KmHourToMSec(float f)
    {
        return f / 3.6F;
    }

    private static final long SecsToTicks(float f)
    {
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    protected final boolean Head360(FiringDevice firingdevice)
    {
        return prop.guns[firingdevice.id].HEAD_YAW_RANGE.fullcircle();
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if(actor instanceof BridgeSegment)
        {
            if(dying != 0)
                aflag[0] = false;
            return;
        }
        if(path == null && (actor instanceof ActorMesh) && ((ActorMesh)actor).isStaticPos())
        {
            aflag[0] = false;
            return;
        } else
        {
            return;
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(dying != 0)
            return;
        if(isNetMirror())
            return;
        if(actor instanceof WeakBody)
            return;
        if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof BridgeSegment))
            Die(null, -1L, true);
    }

    public void msgShot(Shot shot)
    {
        shot.bodyMaterial = 2;
        if(dying != 0)
            return;
        if(shot.power <= 0.0F)
            return;
        if(isNetMirror() && shot.isMirage())
            return;
        if(wakeupTmr < 0L)
            wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
        float f;
        float f1;
        if(shot.powerType == 1)
        {
            f = prop.stre.EXPLHIT_MAX_TNT;
            f1 = prop.stre.EXPLHIT_MAX_TNT;
        } else
        {
            f = prop.stre.SHOT_MIN_ENERGY;
            f1 = prop.stre.SHOT_MAX_ENERGY;
        }
        float f2 = shot.power * Rnd(1.0F, 1.1F);
        if(f2 < f)
            return;
        float f3 = f2 / f1;
        life -= f3;
        if(life > 0.0F)
        {
            return;
        } else
        {
            Die(shot.initiator, -1L, true);
            return;
        }
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying != 0)
            return;
        if(isNetMirror() && explosion.isMirage())
            return;
        if(wakeupTmr < 0L)
            wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
        float f = explosion.power;
//        Explosion _tmp = explosion;
        if(explosion.powerType == 2 && explosion.chunkName != null)
            f *= 0.45F;
        float f1;
        if(explosion.chunkName != null)
        {
            float f2 = f;
            f2 *= Rnd(0.5F, 1.6F);
            if(f2 < prop.stre.EXPLHIT_MIN_TNT)
                return;
            f1 = (f2 - prop.stre.EXPLHIT_MIN_TNT) / prop.stre.EXPLHIT_MAX_TNT;
        } else
        {
            float f3 = (float)pos.getAbsPoint().distance(p) - collisionR();
            float f4 = explosion.receivedTNT_1meterWater(f3, -(float)explosion.p.z);
            f4 *= Rnd(0.5F, 1.6F);
            if(f4 < prop.stre.EXPLNEAR_MIN_TNT)
                return;
            f1 = (f4 - prop.stre.EXPLHIT_MIN_TNT) / prop.stre.EXPLNEAR_MAX_TNT;
        }
        life -= f1;
        if(life > 0.0F)
        {
            return;
        } else
        {
            Die(explosion.initiator, -1L, true);
            return;
        }
    }

    private float computeSeaDepth(Point3d point3d)
    {
        for(float f = 5F; f <= 355F; f += 10F)
        {
            for(float f1 = 0.0F; f1 < 360F; f1 += 30F)
            {
                float f2 = f * Geom.cosDeg(f1);
                float f3 = f * Geom.sinDeg(f1);
                f2 += (float)point3d.x;
                f3 += (float)point3d.y;
                if(!World.land().isWater(f2, f3))
                    return 50F * (f / 355F);
            }

        }

        return 1000F;
    }

    private void computeSinkingParams(long l)
    {
        RangeRandom rangerandom = new RangeRandom(l % 11073L);
        timeForRotation = 40000L + (long)(rangerandom.nextFloat() * 0.0F);
        drownBodyPitch = 50F - rangerandom.nextFloat() * 20F;
        if(rangerandom.nextFloat() < 0.5F)
            drownBodyPitch = -drownBodyPitch;
        drownBodyRoll = 30F - rangerandom.nextFloat() * 60F;
        sinkingDepthSpeed = 0.55F + rangerandom.nextFloat() * 0.0F;
        seaDepth = computeSeaDepth(pos.getAbsPoint());
        seaDepth *= 1.0F + rangerandom.nextFloat() * 0.2F;
    }

    private void showExplode()
    {
        if(mesh() instanceof HierMesh)
            Explosions.Antiaircraft_Explode(pos.getAbsPoint());
    }

    private void Die(Actor actor, long l, boolean flag)
    {
        if(dying != 0)
            return;
        if(l < 0L)
        {
            if(isNetMirror())
            {
                send_DeathRequest(actor);
                return;
            }
            l = NetServerParams.getServerTime();
        }
        life = 0.0F;
        dying = 1;
        World.onActorDied(this, actor);
        forgetAllAiming();
        SetEffectsIntens(-1F);
        if(path != null)
        {
            bodyDepth = 0.0F;
            bodyPitch = bodyRoll = 0.0F;
            setMovablePosition(l);
        } else
        {
            bodyDepth = 0.0F;
            bodyPitch = bodyRoll = 0.0F;
            setPosition();
        }
        pos.reset();
        computeSinkingParams(l);
        if(Mission.isDeathmatch())
        {
            timeOfDeath = Time.current();
            if(!flag)
                timeOfDeath = 0L;
        } else
        {
            timeOfDeath = l;
        }
        if(flag)
            showExplode();
        if(flag)
            send_DeathCommand(actor);
    }

    public void destroy()
    {
        if(isDestroyed())
        {
            return;
        } else
        {
            eraseGuns();
            super.destroy();
            return;
        }
    }

    private boolean isAnyEnemyNear()
    {
        NearestEnemies.set(WeaponsMask());
        Actor actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), 2000D, getArmy());
        return actor != null;
    }

    private final FiringDevice GetFiringDevice(Aim aim)
    {
        for(int i = 0; i < prop.nGuns; i++)
            if(arms[i] != null && arms[i].aime == aim)
                return arms[i];

        System.out.println("Internal error 1: Can't find ship gun.");
        return null;
    }

    private final ShipGunProperties GetGunProperties(Aim aim)
    {
        for(int i = 0; i < prop.nGuns; i++)
            if(arms[i].aime == aim)
                return prop.guns[arms[i].id];

        System.out.println("Internal error 2: Can't find ship gun.");
        return null;
    }

    private void setGunAngles(FiringDevice firingdevice, float f, float f1)
    {
        firingdevice.headYaw = f;
        firingdevice.gunPitch = f1;
        hierMesh().chunkSetAngles("Head" + firingdevice.id, firingdevice.headYaw, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Gun" + firingdevice.id, -firingdevice.gunPitch, 0.0F, 0.0F);
    }

    private void eraseGuns()
    {
        if(arms != null)
        {
            for(int i = 0; i < prop.nGuns; i++)
            {
                if(arms[i] == null)
                    continue;
                if(arms[i].aime != null)
                {
                    arms[i].aime.forgetAll();
                    arms[i].aime = null;
                }
                if(arms[i].gun != null)
                {
                    destroy(arms[i].gun);
                    arms[i].gun = null;
                }
                arms[i] = null;
            }

            arms = null;
        }
    }

    private void forgetAllAiming()
    {
        if(arms != null)
        {
            for(int i = 0; i < prop.nGuns; i++)
                if(arms[i] != null && arms[i].aime != null)
                    arms[i].aime.forgetAiming();

        }
    }

    private void CreateGuns()
    {
        arms = new FiringDevice[prop.nGuns];
        for(int i = 0; i < prop.nGuns; i++)
        {
            arms[i] = new FiringDevice();
            arms[i].id = i;
            arms[i].gun = null;
            try
            {
                arms[i].gun = (Gun)prop.guns[i].gunClass.newInstance();
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Ship: Can't create gun '" + prop.guns[i].gunClass.getName() + "'");
            }
            arms[i].gun.set(this, "ShellStart" + i);
            arms[i].gun.loadBullets(-1);
            Loc loc = new Loc();
            hierMesh().setCurChunk("Head" + i);
            hierMesh().getChunkLocObj(loc);
            prop.guns[i].fireOffset = new Point3d();
            loc.get(prop.guns[i].fireOffset);
            prop.guns[i].fireOrient = new Orient();
            loc.get(prop.guns[i].fireOrient);
            if(prop.guns[i].TRACKING_ONLY)
                arms[i].aime = new Aim(this, isNetMirror(), SLOWFIRE_K * prop.guns[i].DELAY_AFTER_SHOOT, true);
            else
                arms[i].aime = new Aim(this, isNetMirror(), SLOWFIRE_K * prop.guns[i].DELAY_AFTER_SHOOT);
        }

    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    private void setDefaultLivePose()
    {
        int i = mesh().hookFind("Ground_Level");
        if(i != -1)
        {
            Matrix4d matrix4d = new Matrix4d();
            hierMesh().hookMatrix(i, matrix4d);
        }
        if(mesh() instanceof HierMesh)
            hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        for(int j = 0; j < prop.nGuns; j++)
            setGunAngles(arms[j], prop.guns[j].HEAD_STD_YAW, prop.guns[j].GUN_STD_PITCH);

        bodyDepth = 0.0F;
        align();
    }

    protected ShipGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private ShipGeneric(ShipProperties shipproperties, ActorSpawnArg actorspawnarg)
    {
        super(shipproperties.meshName);
        prop = null;
        arms = null;
        cachedSeg = 0;
        life = 1.0F;
        dying = 0;
        respawnDelay = 0L;
        wakeupTmr = 0L;
        DELAY_WAKEUP = 0.0F;
        SKILL_IDX = 2;
        SLOWFIRE_K = 1.0F;
        pipe = null;
        nose = null;
        tail = null;
        outCommand = new NetMsgFiltered();
        prop = shipproperties;
        actorspawnarg.setStationary(this);
        path = null;
        collide(true);
        drawing(true);
        bodyDepth = 0.0F;
        bodyPitch = bodyRoll = 0.0F;
        bodyYaw = actorspawnarg.orient.getYaw();
        setPosition();
        pos.reset();
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        SKILL_IDX = Chief.new_SKILL_IDX;
        SLOWFIRE_K = Chief.new_SLOWFIRE_K;
        DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
        wakeupTmr = 0L;
        CreateGuns();
        setDefaultLivePose();
        if(!isNetMirror() && prop.nGuns > 0 && DELAY_WAKEUP > 0.0F)
            wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
        if(!interpEnd("move"))
            interpPut(new Move(), "move", Time.current(), null);
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

    public ShipGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
    {
        prop = null;
        arms = null;
        cachedSeg = 0;
        life = 1.0F;
        dying = 0;
        respawnDelay = 0L;
        wakeupTmr = 0L;
        DELAY_WAKEUP = 0.0F;
        SKILL_IDX = 2;
        SLOWFIRE_K = 1.0F;
        pipe = null;
        nose = null;
        tail = null;
        outCommand = new NetMsgFiltered();
        try
        {
            int j = sectfile.sectionIndex(s1);
            String s3 = sectfile.var(j, 0);
            Object obj = Spawn.get(s3);
            if(obj == null)
                throw new ActorException("Ship: Unknown class of ship (" + s3 + ")");
            prop = ((SPAWN)obj).proper;
            try
            {
                setMesh(prop.meshName);
            }
            catch(RuntimeException runtimeexception)
            {
                super.destroy();
                throw runtimeexception;
            }
            if(prop.soundName != null)
                newSound(prop.soundName, true);
            setName(s);
            setArmy(i);
            LoadPath(sectfile1, s2);
            cachedSeg = 0;
            bodyDepth = 0.0F;
            bodyPitch = bodyRoll = 0.0F;
            setMovablePosition(NetServerParams.getServerTime());
            pos.reset();
            collide(true);
            drawing(true);
            pipe = null;
            if(mesh().hookFind("Vapor") >= 0)
            {
                HookNamed hooknamed = new HookNamed(this, "Vapor");
                pipe = Eff3DActor.New(this, hooknamed, null, 1.0F, "Effects/Smokes/SmokePipeShip.eff", -1F);
            }
            wake[2] = wake[1] = wake[0] = null;
            tail = null;
            nose = null;
            boolean flag = prop.SLIDER_DIST / 2.5F < 90F;
            if(mesh().hookFind("_Prop") >= 0)
            {
                HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Prop");
                tail = Eff3DActor.New(this, hooknamedz0, null, 1.0F, flag ? "3DO/Effects/Tracers/ShipTrail/PropWakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/PropWake.eff", -1F);
            }
            if(mesh().hookFind("_Centre") >= 0)
            {
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                HookNamed hooknamed1 = new HookNamed(this, "_Left");
                hooknamed1.computePos(this, new Loc(), loc);
                HookNamed hooknamed2 = new HookNamed(this, "_Right");
                hooknamed2.computePos(this, new Loc(), loc1);
                float f = (float)loc.getPoint().distance(loc1.getPoint());
                HookNamedZ0 hooknamedz0_1 = new HookNamedZ0(this, "_Centre");
                if(mesh().hookFind("_Prop") >= 0)
                {
                    HookNamedZ0 hooknamedz0_3 = new HookNamedZ0(this, "_Prop");
                    Loc loc2 = new Loc();
                    hooknamedz0_1.computePos(this, new Loc(), loc2);
                    Loc loc3 = new Loc();
                    hooknamedz0_3.computePos(this, new Loc(), loc3);
                    float f1 = (float)loc2.getPoint().distance(loc3.getPoint());
                    wake[0] = Eff3DActor.New(this, hooknamedz0_3, new Loc((double)(-f1) * 0.33000000000000002D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/Wake.eff", -1F);
                    wake[1] = Eff3DActor.New(this, hooknamedz0_1, new Loc((double)f1 * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                    wake[2] = Eff3DActor.New(this, hooknamedz0_1, new Loc((double)(-f1) * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                } else
                {
                    wake[0] = Eff3DActor.New(this, hooknamedz0_1, new Loc((double)(-f) * 0.29999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, (double)prop.SLIDER_DIST / 2.5D >= 50D ? "3DO/Effects/Tracers/ShipTrail/Wake.eff" : "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff", -1F);
                }
            }
            if(mesh().hookFind("_Nose") >= 0)
            {
                HookNamedZ0 hooknamedz0_2 = new HookNamedZ0(this, "_Nose");
                nose = Eff3DActor.New(this, hooknamedz0_2, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), 1.0F, "3DO/Effects/Tracers/ShipTrail/SideWave.eff", -1F);
            }
            SetEffectsIntens(0.0F);
            int k = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if(netchannel == null)
                net = new Master(this);
            else
            if(k != 0)
                net = new Mirror(this, netchannel, k);
            SKILL_IDX = Chief.new_SKILL_IDX;
            SLOWFIRE_K = Chief.new_SLOWFIRE_K;
            DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
            wakeupTmr = 0L;
            CreateGuns();
            setDefaultLivePose();
            if(!isNetMirror() && prop.nGuns > 0 && DELAY_WAKEUP > 0.0F)
                wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
            if(!interpEnd("move"))
                interpPut(new Move(), "move", Time.current(), null);
        }
        catch(Exception exception)
        {
            System.out.println("Ship creation failure:");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            throw new ActorException();
        }
    }

    private void SetEffectsIntens(float f)
    {
        if(dying != 0)
            f = -1F;
        if(pipe != null)
            if(f >= 0.0F)
            {
                pipe._setIntesity(f);
            } else
            {
                pipe._finish();
                pipe = null;
            }
        for(int i = 0; i < 3; i++)
        {
            if(wake[i] == null)
                continue;
            if(f >= 0.0F)
            {
                wake[i]._setIntesity(f);
            } else
            {
                wake[i]._finish();
                wake[i] = null;
            }
        }

        if(nose != null)
            if(f >= 0.0F)
            {
                nose._setIntesity(f);
            } else
            {
                nose._finish();
                nose = null;
            }
        if(tail != null)
            if(f >= 0.0F)
            {
                tail._setIntesity(f);
            } else
            {
                tail._finish();
                tail = null;
            }
    }

    private void LoadPath(SectFile sectfile, String s)
    {
        int i = sectfile.sectionIndex(s);
        if(i < 0)
            throw new ActorException("Ship path: Section [" + s + "] not found");
        int j = sectfile.vars(i);
        if(j < 1)
            throw new ActorException("Ship path must contain at least 2 nodes");
        path = new ArrayList();
        for(int k = 0; k < j; k++)
        {
            StringTokenizer stringtokenizer = new StringTokenizer(sectfile.line(i, k));
            float f1 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            float f2 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            
            // TODO: By SAS~Storebror - ATTENTION: f4 is unused, but the Token index still must be pushed forward!
//            float f4 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            stringtokenizer.nextToken();
            // ---
            
            double d = 0.0D;
            float f7 = 0.0F;
            if(stringtokenizer.hasMoreTokens())
            {
                d = Double.valueOf(stringtokenizer.nextToken()).doubleValue();
                if(stringtokenizer.hasMoreTokens())
                {
                    Double.valueOf(stringtokenizer.nextToken()).doubleValue();
                    if(stringtokenizer.hasMoreTokens())
                        f7 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
                }
            }
            if(k >= j - 1)
                d = 1.0D;
            Segment segment7 = new Segment();
            segment7.posIn = new Point3d(f1, f2, 0.0D);
            if(Math.abs(d) < 0.10000000000000001D)
                segment7.timeIn = 0L;
            else
                segment7.timeIn = (long)(d * 60D * 1000D + (d <= 0.0D ? -0.5D : 0.5D));
            if(f7 <= 0.0F && (k == 0 || k == j - 1 || segment7.timeIn == 0L))
                f7 = prop.SPEED;
            segment7.speedIn = f7;
            path.add(segment7);
        }

        for(int l = 0; l < path.size() - 1; l++)
        {
            Segment segment = (Segment)path.get(l);
            Segment segment1 = (Segment)path.get(l + 1);
            if(segment.timeIn > 0L && segment1.timeIn > 0L)
            {
                Segment segment2 = new Segment();
                segment2.posIn = new Point3d(segment.posIn);
                segment2.posIn.add(segment1.posIn);
                segment2.posIn.scale(0.5D);
                segment2.timeIn = 0L;
                segment2.speedIn = (segment.speedIn + segment1.speedIn) * 0.5F;
                path.add(l + 1, segment2);
            }
        }

        int i1 = 0;
        float f = ((Segment)path.get(i1)).length;
        int j1;
        for(; i1 < path.size() - 1; i1 = j1)
        {
            j1 = i1 + 1;
            do
            {
                Segment segment3 = (Segment)path.get(j1);
                if((double)segment3.speedIn > 0.0D)
                    break;
                f += segment3.length;
                j1++;
            } while(true);
            if(j1 - i1 <= 1)
                continue;
            float f3 = ((Segment)path.get(i1)).length;
            float f5 = ((Segment)path.get(i1)).speedIn;
            float f6 = ((Segment)path.get(j1)).speedIn;
            for(int i2 = i1 + 1; i2 < j1; i2++)
            {
                Segment segment6 = (Segment)path.get(i2);
                float f9 = f3 / f;
                segment6.speedIn = f5 * (1.0F - f9) + f6 * f9;
                f += segment6.length;
            }

        }

        long l1 = 0L;
        for(int k1 = 0; k1 < path.size() - 1; k1++)
        {
            Segment segment4 = (Segment)path.get(k1);
            Segment segment5 = (Segment)path.get(k1 + 1);
            if(k1 == 0)
                l1 = segment4.timeIn;
            segment4.posOut = new Point3d(segment5.posIn);
            segment5.posIn = segment4.posOut;
            segment4.length = (float)segment4.posIn.distance(segment5.posIn);
            float f8 = segment4.speedIn;
            float f10 = segment5.speedIn;
            float f11 = (f8 + f10) * 0.5F;
            if(segment4.timeIn > 0L)
                if(segment4.timeIn > l1)
                    segment4.timeIn -= l1;
                else
                    segment4.timeIn = 0L;
            if(segment4.timeIn == 0L && segment5.timeIn > 0L)
            {
                int j2 = (int)(((2.0F * segment4.length) / f8) * 1000F + 0.5F);
                j2 = (int)((long)j2 + l1);
                if(segment5.timeIn > (long)j2)
                    segment5.timeIn -= j2;
                else
                    segment5.timeIn = 0L;
            }
            if(segment4.timeIn > 0L)
            {
                segment4.speedIn = 0.0F;
                segment4.speedOut = f10;
                float f12 = ((2.0F * segment4.length) / f10) * 1000F + 0.5F;
                segment4.timeIn = l1 + segment4.timeIn;
                segment4.timeOut = segment4.timeIn + (long)(int)f12;
                l1 = segment4.timeOut;
                continue;
            }
            if(segment5.timeIn > 0L)
            {
                segment4.speedIn = f8;
                segment4.speedOut = 0.0F;
                float f13 = ((2.0F * segment4.length) / f8) * 1000F + 0.5F;
                segment4.timeIn = l1 + segment4.timeIn;
                segment4.timeOut = segment4.timeIn + (long)(int)f13;
                l1 = segment4.timeOut + segment5.timeIn;
            } else
            {
                segment4.speedIn = f8;
                segment4.speedOut = f10;
                float f14 = (segment4.length / f11) * 1000F + 0.5F;
                segment4.timeIn = l1;
                segment4.timeOut = segment4.timeIn + (long)(int)f14;
                l1 = segment4.timeOut;
            }
        }

        path.remove(path.size() - 1);
    }

    public void align()
    {
        pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) - (double)bodyDepth;
        pos.setAbs(p);
    }

    private void setMovablePosition(long l)
    {
        if(cachedSeg < 0)
            cachedSeg = 0;
        else
        if(cachedSeg >= path.size())
            cachedSeg = path.size() - 1;
        Segment segment = (Segment)path.get(cachedSeg);
        if(segment.timeIn <= l && l <= segment.timeOut)
        {
            SetEffectsIntens(1.0F);
            setMovablePosition((float)(l - segment.timeIn) / (float)(segment.timeOut - segment.timeIn));
            return;
        }
        if(l > segment.timeOut)
        {
            while(cachedSeg + 1 < path.size()) 
            {
                Segment segment1 = (Segment)path.get(++cachedSeg);
                if(l <= segment1.timeIn)
                {
                    SetEffectsIntens(0.0F);
                    setMovablePosition(0.0F);
                    return;
                }
                if(l <= segment1.timeOut)
                {
                    SetEffectsIntens(1.0F);
                    setMovablePosition((float)(l - segment1.timeIn) / (float)(segment1.timeOut - segment1.timeIn));
                    return;
                }
            }
            SetEffectsIntens(-1F);
            setMovablePosition(1.0F);
            return;
        }
        while(cachedSeg > 0) 
        {
            Segment segment2 = (Segment)path.get(--cachedSeg);
            if(l >= segment2.timeOut)
            {
                SetEffectsIntens(0.0F);
                setMovablePosition(1.0F);
                return;
            }
            if(l >= segment2.timeIn)
            {
                SetEffectsIntens(1.0F);
                setMovablePosition((float)(l - segment2.timeIn) / (float)(segment2.timeOut - segment2.timeIn));
                return;
            }
        }
        SetEffectsIntens(0.0F);
        setMovablePosition(0.0F);
    }

    private void setMovablePosition(float f)
    {
        Segment segment = (Segment)path.get(cachedSeg);
        float f1 = (float)(segment.timeOut - segment.timeIn) * 0.001F;
        float f2 = segment.speedIn;
        float f3 = segment.speedOut;
        float f4 = (f3 - f2) / f1;
        f *= f1;
        float f5 = f2 * f + f4 * f * f * 0.5F;
        int i = cachedSeg;
        float f6 = prop.SLIDER_DIST - (segment.length - f5);
        if(f6 <= 0.0F)
            p1.interpolate(segment.posIn, segment.posOut, (f5 + prop.SLIDER_DIST) / segment.length);
        else
            do
            {
                if(i + 1 >= path.size())
                {
                    p1.interpolate(segment.posIn, segment.posOut, 1.0F + f6 / segment.length);
                    break;
                }
                segment = (Segment)path.get(++i);
                if(f6 <= segment.length)
                {
                    p1.interpolate(segment.posIn, segment.posOut, f6 / segment.length);
                    break;
                }
                f6 -= segment.length;
            } while(true);
        i = cachedSeg;
        segment = (Segment)path.get(i);
        f6 = prop.SLIDER_DIST - f5;
        if(f6 <= 0.0F)
            p2.interpolate(segment.posIn, segment.posOut, (f5 - prop.SLIDER_DIST) / segment.length);
        else
            do
            {
                if(i <= 0)
                {
                    p2.interpolate(segment.posIn, segment.posOut, 0.0F - f6 / segment.length);
                    break;
                }
                segment = (Segment)path.get(--i);
                if(f6 <= segment.length)
                {
                    p2.interpolate(segment.posIn, segment.posOut, 1.0F - f6 / segment.length);
                    break;
                }
                f6 -= segment.length;
            } while(true);
        p.interpolate(p1, p2, 0.5F);
        tmpv.sub(p1, p2);
        if(tmpv.lengthSquared() < 0.0010000000474974513D)
        {
            Segment segment1 = (Segment)path.get(cachedSeg);
            tmpv.sub(segment1.posOut, segment1.posIn);
        }
        float f7 = (float)(Math.atan2(tmpv.y, tmpv.x) * 57.295779513082323D);
        setPosition(p, f7);
    }

    private void setPosition(Point3d point3d, float f)
    {
        bodyYaw = f;
        o.setYPR(bodyYaw, bodyPitch, bodyRoll);
        point3d.z = -bodyDepth;
        pos.setAbs(point3d, o);
    }

    private void setPosition()
    {
        o.setYPR(bodyYaw, bodyPitch, bodyRoll);
        pos.setAbs(o);
        align();
    }

    public int WeaponsMask()
    {
        return prop.WEAPONS_MASK;
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
        if(abulletproperties[0].powerType == 0)
            return 0;
        if(abulletproperties[1].powerType == 0)
            return 1;
        return abulletproperties[0].powerType != 1 ? 0 : 1;
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
        return prop.ATTACK_MAX_DISTANCE;
    }

    public float AttackMaxDistance(Aim aim)
    {
        return GetGunProperties(aim).ATTACK_MAX_DISTANCE;
    }

    private void send_DeathCommand(Actor actor)
    {
        if(!isNetMaster())
            return;
        if(Mission.isDeathmatch())
        {
            float f = Mission.respawnTime("Ship");
            respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
        } else
        {
            respawnDelay = 0L;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeLong(timeOfDeath);
            netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_RespawnCommand()
    {
        if(!isNetMaster() || !Mission.isDeathmatch())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(82);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_FireCommand(int i, Actor actor, int j, float f)
    {
        if(!isNetMaster())
            return;
        if(!net.isMirrored())
            return;
        if(!Actor.isValid(actor) || !actor.isNet())
            return;
        j &= 0xff;
        if(f < 0.0F)
            try
            {
                outCommand.unLockAndClear();
                outCommand.writeByte(84);
                outCommand.writeByte(i);
                outCommand.writeNetObj(actor.net);
                outCommand.writeByte(j);
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
                outCommand.writeByte(i);
                outCommand.writeFloat(f);
                outCommand.writeNetObj(actor.net);
                outCommand.writeByte(j);
                outCommand.setIncludeTime(true);
                net.post(Time.current(), outCommand);
            }
            catch(Exception exception1)
            {
                System.out.println(exception1.getMessage());
                exception1.printStackTrace();
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

    public void createNetObject(NetChannel netchannel, int i)
    {
        if(netchannel == null)
            net = new Master(this);
        else
            net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        if(dying == 0)
            netmsgguaranted.writeLong(-1L);
        else
            netmsgguaranted.writeLong(timeOfDeath);
        net.postTo(netchannel, netmsgguaranted);
    }

    public float getReloadingTime(Aim aim)
    {
        return SLOWFIRE_K * GetGunProperties(aim).DELAY_AFTER_SHOOT;
    }

    public float chainFireTime(Aim aim)
    {
        float f = GetGunProperties(aim).CHAINFIRE_TIME;
        return f > 0.0F ? f * Rnd(0.75F, 1.25F) : 0.0F;
    }

    public float probabKeepSameEnemy(Actor actor)
    {
        return 0.75F;
    }

    public float minTimeRelaxAfterFight()
    {
        return 0.1F;
    }

    public void gunStartParking(Aim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        ShipGunProperties shipgunproperties = prop.guns[firingdevice.id];
        aim.setRotationForParking(firingdevice.headYaw, firingdevice.gunPitch, shipgunproperties.HEAD_STD_YAW, shipgunproperties.GUN_STD_PITCH, shipgunproperties.HEAD_YAW_RANGE, shipgunproperties.HEAD_MAX_YAW_SPEED, shipgunproperties.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean flag, Aim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        float f = aim.t();
        float f1 = aim.anglesYaw.getDeg(f);
        float f2 = aim.anglesPitch.getDeg(f);
        setGunAngles(firingdevice, f1, f2);
        pos.inValidate(false);
    }

    public Actor findEnemy(Aim aim)
    {
        if(isNetMirror())
            return null;
        ShipGunProperties shipgunproperties = GetGunProperties(aim);
        Actor actor = null;
        switch(shipgunproperties.ATTACK_FAST_TARGETS)
        {
        case 0:
            NearestEnemies.set(shipgunproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100F));
            break;

        case 1:
            NearestEnemies.set(shipgunproperties.WEAPONS_MASK);
            break;

        default:
            NearestEnemies.set(shipgunproperties.WEAPONS_MASK, KmHourToMSec(100F), 9999.9F);
            break;
        }
        actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), shipgunproperties.ATTACK_MAX_RADIUS, getArmy(), (float)shipgunproperties.fireOffset.z);
        if(actor == null)
            return null;
        if(!(actor instanceof Prey))
        {
            System.out.println("ship: nearest enemies: non-Prey");
            return null;
        }
        FiringDevice firingdevice = GetFiringDevice(aim);
        BulletProperties bulletproperties = null;
        if(firingdevice.gun.prop != null)
        {
            int i = ((Prey)actor).chooseBulletType(firingdevice.gun.prop.bullet);
            if(i < 0)
                return null;
            bulletproperties = firingdevice.gun.prop.bullet[i];
        }
        int j = ((Prey)actor).chooseShotpoint(bulletproperties);
        if(j < 0)
            return null;
        aim.shotpoint_idx = j;
        double d = distance(actor);
        d /= AttackMaxDistance(aim);
        aim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
        aim.scaleAimingError(23.47F * (float)(4 - SKILL_IDX) * (float)(4 - SKILL_IDX));
        if(actor instanceof Aircraft)
            d *= 0.25D;
        aim.scaleAimingError((float)d);
        return actor;
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim)
    {
        if(!isNetMirror())
        {
            FiringDevice firingdevice = GetFiringDevice(aim);
            send_FireCommand(firingdevice.id, actor, aim.shotpoint_idx, i != 0 ? f : -1F);
        }
        return true;
    }

    private void Track_Mirror(int i, Actor actor, int j)
    {
        if(actor == null)
            return;
        if(arms == null || arms[i].aime == null)
        {
            return;
        } else
        {
            arms[i].aime.passive_StartFiring(0, actor, j, 0.0F);
            return;
        }
    }

    private void Fire_Mirror(int i, Actor actor, int j, float f)
    {
        if(actor == null)
            return;
        if(arms == null || arms[i].aime == null)
            return;
        if(f <= 0.2F)
            f = 0.2F;
        if(f >= 15F)
            f = 15F;
        arms[i].aime.passive_StartFiring(1, actor, j, f);
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean flag)
    {
        if(!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
            return 0;
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(firingdevice.gun instanceof CannonMidrangeGeneric)
        {
            int i = ((Prey)actor).chooseBulletType(firingdevice.gun.prop.bullet);
            if(i < 0)
                return 0;
            ((CannonMidrangeGeneric)firingdevice.gun).setBulletType(i);
        }
        boolean flag1 = ((Prey)actor).getShotpointOffset(aim.shotpoint_idx, p1);
        if(!flag1)
            return 0;
        ShipGunProperties shipgunproperties = prop.guns[firingdevice.id];
        float f1 = f * Rnd(0.8F, 1.2F);
        if(!Aimer.aim((BulletAimer)firingdevice.gun, actor, this, f1, p1, shipgunproperties.fireOffset))
            return 0;
        Point3d point3d = new Point3d();
        Aimer.getPredictedTargetPosition(point3d);
        point3d.add(aim.getAimingError());
        Point3d point3d1 = Aimer.getHunterFirePoint();
        float f2 = 0.05F;
        double d = point3d.distance(point3d1);
        float fd1 = (float)point3d.z;
        if(f1 > 0.001F)
        {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            point3d2.add(aim.getAimingError());
            tmpv.sub(point3d, point3d2);
            double d2 = tmpv.length();
            if(d2 > 0.001D)
            {
                float f7 = (float)d2 / f1;
                if(f7 > 200F)
                    f7 = 200F;
                float f8 = f7 * 0.01F;
                point3d2.sub(point3d1);
                double d3 = point3d2.x * point3d2.x + point3d2.y * point3d2.y + point3d2.z * point3d2.z;
                if(d3 > 0.01D)
                {
                    float f9 = (float)tmpv.dot(point3d2);
                    f9 /= (float)(d2 * Math.sqrt(d3));
                    f9 = (float)Math.sqrt(1.0F - f9 * f9);
                    f8 *= 0.4F + 0.6F * f9;
                }
                f8 *= 1.3F;
                f8 *= Aim.AngleErrorKoefForSkill[SKILL_IDX];
                int k = Mission.curCloudsType();
                if(k > 2)
                {
                    float f10 = k <= 4 ? 800F : 400F;
                    float f11 = (float)(d / (double)f10);
                    if(f11 > 1.0F)
                    {
                        if(f11 > 10F)
                            return 0;
                        f11 = (f11 - 1.0F) / 9F;
                        f8 *= f11 + 1.0F;
                    }
                }
                if(k >= 3 && fd1 > Mission.curCloudsHeight())
                    f8 *= 1.25F;
                f2 += f8;
            }
        }
        if(actor instanceof Aircraft)
        {
            aim.scaleAimingError(0.73F);
            f2 = (float)((double)f2 * (0.5D + (double)(Aim.AngleErrorKoefForSkill[SKILL_IDX] * 0.75F)));
        } else
        if(aim.getAimingError().length() > 1.1100000143051147D)
            aim.scaleAimingError(0.973F);
        if(World.Sun().ToSun.z < -0.15F)
        {
            float f4 = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if(f4 >= 1.0F)
                f4 = 1.0F;
            if((actor instanceof Aircraft) && Time.current() - ((Aircraft)actor).tmSearchlighted < 1000L)
                f4 = 0.0F;
            f2 += 10F * f4;
        }
        float f5 = (float)actor.getSpeed(null) - 10F;
        if(f5 > 0.0F)
        {
            float f6 = 83.33334F;
            f5 = f5 < f6 ? f5 / f6 : 1.0F;
            f2 += f5 * shipgunproperties.FAST_TARGETS_ANGLE_ERROR;
        }
        Vector3d vector3d = new Vector3d();
        if(!((BulletAimer)firingdevice.gun).FireDirection(point3d1, point3d, vector3d))
            return 0;
        float f3;
        if(flag)
        {
            f3 = 99999F;
            fd1 = 99999F;
        } else
        {
            f3 = shipgunproperties.HEAD_MAX_YAW_SPEED;
            fd1 = shipgunproperties.GUN_MAX_PITCH_SPEED;
        }
        o.add(shipgunproperties.fireOrient, pos.getAbs().getOrient());
        int j = aim.setRotationForTargeting(this, o, point3d1, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f2, f1, shipgunproperties.HEAD_YAW_RANGE, shipgunproperties.GUN_MIN_PITCH, shipgunproperties.GUN_MAX_PITCH, f3, fd1, 0.0F);
        return j;
    }

    public void singleShot(Aim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(!prop.guns[firingdevice.id].TRACKING_ONLY)
            firingdevice.gun.shots(1);
    }

    public void startFire(Aim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(!prop.guns[firingdevice.id].TRACKING_ONLY)
            firingdevice.gun.shots(-1);
    }

    public void continueFire(Aim aim)
    {
    }

    public void stopFire(Aim aim)
    {
        FiringDevice firingdevice = GetFiringDevice(aim);
        if(!prop.guns[firingdevice.id].TRACKING_ONLY)
            firingdevice.gun.shots(0);
    }

    public boolean isVisibilityLong()
    {
        return true;
    }

    public int zutiGetDying()
    {
        return dying;
    }

    public boolean zutiIsStatic()
    {
        return path == null || path.size() <= 0;
    }

    private ShipProperties prop;
    private FiringDevice arms[];
    private ArrayList path;
    private int cachedSeg;
    private float bodyDepth;
    private float bodyYaw;
    private float bodyPitch;
    private float bodyRoll;
    private float seaDepth;
    private long timeOfDeath;
    private long timeForRotation;
    private float drownBodyPitch;
    private float drownBodyRoll;
    private float sinkingDepthSpeed;
    private float life;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_SINK = 1;
    static final int DYING_DEAD = 2;
    private long respawnDelay;
    private long wakeupTmr;
    public float DELAY_WAKEUP;
    public int SKILL_IDX;
    public float SLOWFIRE_K;
    private Eff3DActor pipe;
    private Eff3DActor wake[] = {
        null, null, null
    };
    private Eff3DActor nose;
    private Eff3DActor tail;
    private static ShipProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Point3d p1 = new Point3d();
    private static Point3d p2 = new Point3d();
    private static Orient o = new Orient();
//    private static Vector3f n = new Vector3f();
    private static Vector3d tmpv = new Vector3d();
    private NetMsgFiltered outCommand;
}
