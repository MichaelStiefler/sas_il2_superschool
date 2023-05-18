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
import com.maddox.il2.objects.ObjectsLogLevel;
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

public abstract class ArtilleryGeneric extends ActorHMesh
    implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, ActorAlign, HunterInterface
{
    public static class ArtilleryProperties
    {

        public String meshName;
        public String meshName2;
        public String meshSummer;
        public String meshDesert;
        public String meshWinter;
        public String meshSummer1;
        public String meshDesert1;
        public String meshWinter1;
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
        public GunProps gunProperties[];
        boolean bHideGunners;

        public ArtilleryProperties()
        {
            meshName = null;
            meshName2 = null;
            meshSummer = null;
            meshDesert = null;
            meshWinter = null;
            meshSummer1 = null;
            meshDesert1 = null;
            meshWinter1 = null;
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
            bHideGunners = false;
        }
    }

    public static class GunProps
    {

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

        public GunProps()
        {
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

    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(dying == 1)
                if(respawnDelay-- <= 0L)
                {
                    if(!Mission.isDeathmatch())
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
                                ObjState.destroy(arms[i].gun);
                                arms[i].gun = null;
                                arms[i] = null;
                            }
                        }

                        arms = null;
                        return false;
                    }
                    if(!isNetMaster())
                    {
                        respawnDelay = 10000L;
                        return true;
                    }
                    dying = 0;
                    hideTmr = 0L;
                    if(!isNetMirror() && RADIUS_HIDE > 0.0F)
                        hideTmr = -1L;
                    setDiedFlag(false);
                    for(int j = 0; j < arms.length; j++)
                        arms[j].aim.forgetAiming();

                    setMesh(prop.meshName);
                    setDefaultLivePose();
                    send_RespawnCommand();
                    dontShoot = false;
                    time_lastCheckShoot = Time.current() - 12000L;
                    return true;
                } else
                {
                    return true;
                }
            for(int k = 0; k < arms.length; k++)
                arms[k].aim.tick_();

            if(RADIUS_HIDE > 0.0F && hideTmr >= 0L && !isNetMirror())
            {
                for(int l = 0; l < arms.length; l++)
                {
                    if(arms[l].aim.getEnemy() != null)
                    {
                        hideTmr = 0L;
                        continue;
                    }
                    if(++hideTmr > ArtilleryGeneric.delay_hide_ticks)
                        hideTmr = -1L;
                }

            }
            return true;
        }

        Move()
        {
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
            if(dying == 1)
            {
                return true;
            } else
            {
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                Die(actor, (short)0, true);
                return true;
            }
        }

        public Master(Actor actor)
        {
            super(actor);
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
                case 73: // 'I'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted);
                    }
                    short word0 = netmsginput.readShort();
                    float f = netmsginput.readFloat();
                    float f4 = netmsginput.readFloat();
                    if(word0 <= 0)
                    {
                        if(dying != 1)
                        {
                            arms[0].aim.forgetAiming();
                            setGunAngles(0, f, f4);
                        }
                    } else
                    if(dying != 1)
                    {
                        setGunAngles(0, f, f4);
                        Die(null, word0, false);
                    }
                    if(netmsginput.available() > 0)
                    {
                        for(int k = 1; k < arms.length; k++)
                        {
                            float f1 = netmsginput.readFloat();
                            float f5 = netmsginput.readFloat();
                            if(word0 <= 0)
                            {
                                if(dying != 1)
                                {
                                    arms[k].aim.forgetAiming();
                                    setGunAngles(k, f1, f5);
                                }
                                continue;
                            }
                            if(dying != 1)
                                setGunAngles(k, f1, f5);
                        }

                    }
                    return true;

                case 82: // 'R'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted1);
                    }
                    dying = 0;
                    setDiedFlag(false);
                    for(int i = 0; i < arms.length; i++)
                        arms[i].aim.forgetAiming();

                    setMesh(prop.meshName);
                    setDefaultLivePose();
                    return true;

                case 68: // 'D'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                        post(netmsgguaranted2);
                    }
                    short word1 = netmsginput.readShort();
                    float f2 = netmsginput.readFloat();
                    float f6 = netmsginput.readFloat();
                    int l = 0;
                    setGunAngles(l, f2, f6);
                    if(word1 > 0 && dying != 1)
                    {
                        com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                        Actor actor2 = netobj2 != null ? ((ActorNet)netobj2).actor() : null;
                        float f3;
                        float f7;
                        for(; netmsginput.available() > 0; setGunAngles(l, f3, f7))
                        {
                            l++;
                            f3 = netmsginput.readFloat();
                            f7 = netmsginput.readFloat();
                        }

                        Die(actor2, word1, true);
                    }
                    return true;
                }
                return false;
            }
            switch(netmsginput.readByte())
            {
            default:
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
                int j = netmsginput.readUnsignedByte();
                int i1 = 0;
                if(netmsginput.available() > 0)
                    i1 = netmsginput.readInt();
                Track_Mirror(i1, actor, j);
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
                float f8 = netmsginput.readFloat();
                float f9 = 0.001F * (float)(Message.currentGameTime() - Time.current()) + f8;
                int j1 = netmsginput.readUnsignedByte();
                int k1 = 0;
                if(netmsginput.available() > 0)
                    k1 = netmsginput.readInt();
                Fire_Mirror(k1, actor1, j1, f9);
                break;

            case 68: // 'D'
                out.unLockAndSet(netmsginput, 1);
                out.setIncludeTime(false);
                postRealTo(Message.currentRealTime(), masterChannel(), out);
                return true;
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

    public static class SPAWN
        implements ActorSpawn
    {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1)
        {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if(f2 == -9865.345F || f2 < f || f2 > f1)
            {
                if(f2 == -9865.345F)
                    System.out.println("Artillery: Parameter [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Artillery: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
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
                    System.out.print("Artillery: Parameter [" + s + "]:<" + s1 + "> ");
                    System.out.println(s2 != null ? "is empty" : "not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Artillery \"" + s + "\" is not (correctly) declared in technics.ini file!");
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

        private static ArtilleryProperties LoadArtilleryProperties(SectFile sectfile, String s, Class class1)
        {
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            String checkMesh = getS(sectfile, s, "MeshSummer");
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (checkMesh == null || checkMesh.length() == 0)) return null;
            // TODO: ---
            ArtilleryProperties artilleryproperties = new ArtilleryProperties();
            String s1 = getS(sectfile, s, "PanzerType", null);
            if(s1 == null)
                s1 = "Tank";
            artilleryproperties.fnShotPanzer = TableFunctions.GetFunc2(s1 + "ShotPanzer");
            artilleryproperties.fnExplodePanzer = TableFunctions.GetFunc2(s1 + "ExplodePanzer");
            artilleryproperties.PANZER_TNT_TYPE = getF(sectfile, s, "PanzerSubtype", 0.0F, 100F);
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//          artilleryproperties.meshSummer = getS(sectfile, s, "MeshSummer");
            artilleryproperties.meshSummer = checkMesh;
            // TODO: ---
            artilleryproperties.meshDesert = getS(sectfile, s, "MeshDesert", artilleryproperties.meshSummer);
            artilleryproperties.meshWinter = getS(sectfile, s, "MeshWinter", artilleryproperties.meshSummer);
            artilleryproperties.meshSummer1 = getS(sectfile, s, "MeshSummerDamage", null);
            artilleryproperties.meshDesert1 = getS(sectfile, s, "MeshDesertDamage", artilleryproperties.meshSummer1);
            artilleryproperties.meshWinter1 = getS(sectfile, s, "MeshWinterDamage", artilleryproperties.meshSummer1);
            int i = (artilleryproperties.meshSummer1 != null ? 0 : 1) + (artilleryproperties.meshDesert1 != null ? 0 : 1) + (artilleryproperties.meshWinter1 != null ? 0 : 1);
            if(i != 0 && i != 3)
            {
                System.out.println("Artillery: Uncomplete set of damage meshes for '" + s + "'");
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                  throw new RuntimeException("Can't register artillery object");
                return null;
                // ---
            }
            artilleryproperties.PANZER_BODY_FRONT = getF(sectfile, s, "PanzerBodyFront", 0.001F, 9.999F);
            if(sectfile.get(s, "PanzerBodyBack", -9865.345F) == -9865.345F)
            {
                artilleryproperties.PANZER_BODY_BACK = artilleryproperties.PANZER_BODY_FRONT;
                artilleryproperties.PANZER_BODY_SIDE = artilleryproperties.PANZER_BODY_FRONT;
                artilleryproperties.PANZER_BODY_TOP = artilleryproperties.PANZER_BODY_FRONT;
            } else
            {
                artilleryproperties.PANZER_BODY_BACK = getF(sectfile, s, "PanzerBodyBack", 0.001F, 9.999F);
                artilleryproperties.PANZER_BODY_SIDE = getF(sectfile, s, "PanzerBodySide", 0.001F, 9.999F);
                artilleryproperties.PANZER_BODY_TOP = getF(sectfile, s, "PanzerBodyTop", 0.001F, 9.999F);
            }
            if(sectfile.get(s, "PanzerHead", -9865.345F) == -9865.345F)
                artilleryproperties.PANZER_HEAD = artilleryproperties.PANZER_BODY_FRONT;
            else
                artilleryproperties.PANZER_HEAD = getF(sectfile, s, "PanzerHead", 0.001F, 9.999F);
            if(sectfile.get(s, "PanzerHeadTop", -9865.345F) == -9865.345F)
                artilleryproperties.PANZER_HEAD_TOP = artilleryproperties.PANZER_BODY_TOP;
            else
                artilleryproperties.PANZER_HEAD_TOP = getF(sectfile, s, "PanzerHeadTop", 0.001F, 9.999F);
            float f = Math.min(Math.min(artilleryproperties.PANZER_BODY_BACK, artilleryproperties.PANZER_BODY_TOP), Math.min(artilleryproperties.PANZER_BODY_SIDE, artilleryproperties.PANZER_HEAD_TOP));
            artilleryproperties.HITBY_MASK = f <= 0.015F ? -1 : -2;
            artilleryproperties.explodeName = getS(sectfile, s, "Explode", "Artillery");
            int j = 0;
            do
            {
                if(j != 0)
                {
                    String s2 = sectfile.get(s, "Gun_" + j);
                    if(s2 == null || s2.length() <= 0)
                        break;
                }
                j++;
            } while(true);
            artilleryproperties.gunProperties = new GunProps[j];
            for(int k = 0; k < j; k++)
            {
                artilleryproperties.gunProperties[k] = new GunProps();
                String s3 = "Gun";
                String s4 = "";
                if(k != 0)
                {
                    s4 = "_" + k;
                    s3 = s3 + s4;
                }
                String s5 = "com.maddox.il2.objects.weapons." + getS(sectfile, s, s3);
                try
                {
                    artilleryproperties.gunProperties[k].gunClass = Class.forName(s5);
                }
                catch(Exception exception)
                {
                    System.out.println("Artillery: Can't find gun class '" + s5 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register artillery object");
                    return null;
                    // ---
                }
                artilleryproperties.gunProperties[k].WEAPONS_MASK = Gun.getProperties(artilleryproperties.gunProperties[k].gunClass).weaponType;
                if(artilleryproperties.gunProperties[k].WEAPONS_MASK == 0)
                {
                    System.out.println("Artillery: Undefined weapon type in gun class '" + s5 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                      throw new RuntimeException("Can't register artillery object");
                    return null;
                    // ---
                }
                artilleryproperties.gunProperties[k].ATTACK_FAST_TARGETS = true;
                float f1 = sectfile.get(s, "FireFastTargets" + s4, -9865.345F);
                if(f1 != -9865.345F)
                    artilleryproperties.gunProperties[k].ATTACK_FAST_TARGETS = f1 > 0.5F;
                else
                if(s1.equals("Tank"))
                    artilleryproperties.gunProperties[k].ATTACK_FAST_TARGETS = false;
                artilleryproperties.gunProperties[k].ATTACK_MAX_DISTANCE = getF(sectfile, s, "AttackMaxDistance" + s4, 6F, 60000F);
                artilleryproperties.gunProperties[k].ATTACK_MAX_RADIUS = getF(sectfile, s, "AttackMaxRadius" + s4, 6F, 60000F);
                artilleryproperties.gunProperties[k].ATTACK_MAX_HEIGHT = getF(sectfile, s, "AttackMaxHeight" + s4, 6F, 18000F);
                int l = sectfile.sectionIndex(s);
                if(sectfile.varExist(l, "HeadYawHalfRange" + s4))
                {
                    float f2 = getF(sectfile, s, "HeadYawHalfRange" + s4, 0.0F, 180F);
                    artilleryproperties.gunProperties[k].HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
                    artilleryproperties.gunProperties[k].HEAD_YAW_RANGE.set(-f2, f2);
                    artilleryproperties.gunProperties[k].HEAD_STD_YAW = 0.0F;
                } else
                {
                    float f3 = getF(sectfile, s, "HeadMinYaw" + s4, -180F, 180F);
                    float f5 = getF(sectfile, s, "HeadStdYaw" + s4, -180F, 180F);
                    float f6 = getF(sectfile, s, "HeadMaxYaw" + s4, -180F, 180F);
                    artilleryproperties.gunProperties[k].HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
                    artilleryproperties.gunProperties[k].HEAD_YAW_RANGE.set(f3, f6);
                    artilleryproperties.gunProperties[k].HEAD_STD_YAW = f5;
                }
                artilleryproperties.gunProperties[k].GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch" + s4, -15F, 85F);
                artilleryproperties.gunProperties[k].GUN_STD_PITCH = getF(sectfile, s, "GunStdPitch" + s4, -15F, 89.9F);
                artilleryproperties.gunProperties[k].GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch" + s4, 0.0F, 89.9F);
                artilleryproperties.gunProperties[k].HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed" + s4, 0.1F, 999F);
                artilleryproperties.gunProperties[k].GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed" + s4, 0.1F, 999F);
                artilleryproperties.gunProperties[k].DELAY_AFTER_SHOOT = getF(sectfile, s, "DelayAfterShoot" + s4, 0.0F, 999F);
                artilleryproperties.gunProperties[k].CHAINFIRE_TIME = getF(sectfile, s, "ChainfireTime" + s4, 0.0F, 600F);
                float f4 = sectfile.get(s, "FastTargetsAngleError", -9865.345F);
                if(f4 <= 0.0F)
                    f4 = 0.0F;
                else
                if(f4 >= 45F)
                    f4 = 45F;
                artilleryproperties.gunProperties[k].FAST_TARGETS_ANGLE_ERROR = f4;
            }

            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", artilleryproperties.meshSummer);
            return artilleryproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
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
            ArtilleryGeneric artillerygeneric;
            try
            {
                ArtilleryGeneric.constr_arg1 = proper;
                ArtilleryGeneric.constr_arg2 = actorspawnarg;
                artillerygeneric = (ArtilleryGeneric)cls.newInstance();
                ArtilleryGeneric.constr_arg1 = null;
                ArtilleryGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                ArtilleryGeneric.constr_arg1 = null;
                ArtilleryGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Artillery object [class:" + cls.getName() + "]");
                return null;
            }
            return artillerygeneric;
        }

        public Class cls;
        public ArtilleryProperties proper;

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
                proper = LoadArtilleryProperties(Statics.getTechnicsFile(), s1, class1);
                
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

    private static final long SecsToTicks(float f)
    {
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    private boolean friendPlanesAreNear(Aircraft aircraft)
    {
        time_lastCheckShoot = Time.current() - (long)Rnd(0.0F, 1200F);
        dontShoot = false;
        Point3d point3d = aircraft.pos.getAbsPoint();
        double d = 16000000D;
        if(!(aircraft.FM instanceof Maneuver))
            return false;
        AirGroup airgroup = ((Maneuver)aircraft.FM).Group;
        if(airgroup == null)
            return false;
        int i = AirGroupList.length(airgroup.enemies[0]);
        for(int j = 0; j < i; j++)
        {
            AirGroup airgroup1 = AirGroupList.getGroup(airgroup.enemies[0], j);
            if(airgroup1.nOfAirc <= 0)
                continue;
            double d1 = airgroup1.Pos.x - point3d.x;
            double d2 = airgroup1.Pos.y - point3d.y;
            double d3 = airgroup1.Pos.z - point3d.z;
            if(d1 * d1 + d2 * d2 + d3 * d3 > d)
                continue;
            d3 = point3d.z - Engine.land().HQ(point3d.x, point3d.y);
            if(d3 <= 50D)
                continue;
            dontShoot = true;
            break;
        }

        return dontShoot;
    }

    protected final boolean Head360(GunProps gunprops)
    {
        return gunprops.HEAD_YAW_RANGE.fullcircle();
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if((actor instanceof ActorMesh) && ((ActorMesh)actor).isStaticPos())
            aflag[0] = false;
    }

    public void msgShot(Shot shot)
    {
        shot.bodyMaterial = 2;
        if(dying == 0 && shot.power > 0.0F && (!isNetMirror() || !shot.isMirage()))
            if(shot.powerType == 1)
            {
                if(!RndB(0.15F))
                    Die(shot.initiator, (short)0, true);
            } else
            {
                float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP);
                f *= Rnd(0.93F, 1.07F);
                float f1 = prop.fnShotPanzer.Value(shot.power, f);
                if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
                    Die(shot.initiator, (short)0, true);
            }
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying == 0 && (!isNetMirror() || !explosion.isMirage()) && explosion.power > 0.0F)
            if(explosion.powerType == 1)
            {
                if(TankGeneric.splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP))
                    Die(explosion.initiator, (short)0, true);
            } else
            if(explosion.powerType == 2 && explosion.chunkName != null)
            {
                Die(explosion.initiator, (short)0, true);
            } else
            {
                float f;
                if(explosion.chunkName != null)
                    f = 0.5F * explosion.power;
                else
                    f = explosion.receivedTNTpower(this);
                f *= Rnd(0.95F, 1.05F);
                float f1 = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
                if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
                    Die(explosion.initiator, (short)0, true);
            }
    }

    private void ShowExplode(float f)
    {
        if(f > 0.0F)
            f = Rnd(f, f * 1.6F);
        Explosions.runByName(prop.explodeName, this, "SmokeHead", "", f);
    }

    private float[] computeDeathPose(short word0, int i)
    {
        RangeRandom rangerandom = new RangeRandom(word0);
        float af[] = new float[10];
        af[0] = arms[i].headYaw + rangerandom.nextFloat(-15F, 15F);
        af[1] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(4F, 9F);
        af[2] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(4F, 9F);
        af[3] = -arms[i].gunPitch + rangerandom.nextFloat(-15F, 15F);
        af[4] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(2.0F, 5F);
        af[5] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(5F, 9F);
        af[6] = 0.0F;
        af[7] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(4F, 8F);
        af[8] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(7F, 12F);
        af[9] = -rangerandom.nextFloat(0.0F, 0.25F);
        return af;
    }

    private void Die(Actor actor, short word0, boolean flag)
    {
        if(dying == 0)
        {
            if(word0 <= 0)
            {
                if(isNetMirror())
                {
                    send_DeathRequest(actor);
                    return;
                }
                word0 = (short)(int)Rnd(1.0F, 30000F);
            }
            deathSeed = word0;
            dying = 1;
            World.onActorDied(this, actor);
            for(int i = 0; i < arms.length; i++)
                if(arms[i].aim != null)
                    arms[i].aim.forgetAiming();

            float f = 0.0F;
            for(int j = 0; j < arms.length; j++)
            {
                float af[] = computeDeathPose(word0, j);
                String s = "";
                if(j != 0)
                    s = "_" + j;
                hierMesh().chunkSetAngles("Head" + s, af[0], af[1], af[2]);
                hierMesh().chunkSetAngles("Gun" + s, af[3], af[4], af[5]);
                hierMesh().chunkSetAngles("Body", af[6], af[7], af[8]);
                f = af[9];
            }

            if(prop.meshName2 == null)
            {
                mesh().makeAllMaterialsDarker(0.22F, 0.35F);
                heightAboveLandSurface2 = heightAboveLandSurface;
                heightAboveLandSurface = heightAboveLandSurface2 + f;
            } else
            {
                setMesh(prop.meshName2);
                heightAboveLandSurface2 = 0.0F;
                int k = mesh().hookFind("Ground_Level");
                if(k != -1)
                {
                    Matrix4d matrix4d = new Matrix4d();
                    mesh().hookMatrix(k, matrix4d);
                    heightAboveLandSurface2 = (float)(-matrix4d.m23);
                }
                heightAboveLandSurface = heightAboveLandSurface2;
            }
            Align();
            if(flag)
                ShowExplode(15F);
            if(flag)
                send_DeathCommand(actor);
        }
    }

    private void setGunAngles(int i, float f, float f1)
    {
        arms[i].headYaw = f;
        arms[i].gunPitch = f1;
        String s = "";
        if(i != 0)
            s = "_" + i;
        hierMesh().chunkSetAngles("Head" + s, arms[i].headYaw, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Gun" + s, -arms[i].gunPitch, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        pos.inValidate(false);
    }

    public void destroy()
    {
        if(!isDestroyed())
        {
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
                    arms[i] = null;
                }

            }
            arms = null;
            super.destroy();
        }
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    private void setDefaultLivePose()
    {
        heightAboveLandSurface = 0.0F;
        int i = hierMesh().hookFind("Ground_Level");
        if(i != -1)
        {
            Matrix4d matrix4d = new Matrix4d();
            hierMesh().hookMatrix(i, matrix4d);
            heightAboveLandSurface = (float)(-matrix4d.m23);
        }
        for(int j = 0; j < arms.length; j++)
            setGunAngles(j, prop.gunProperties[j].HEAD_STD_YAW, prop.gunProperties[j].GUN_STD_PITCH);

        Align();
    }

    protected ArtilleryGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    public void setMesh(String s)
    {
        super.setMesh(s);
        if(!Config.cur.b3dgunners || bHideGunners)
            mesh().materialReplaceToNull("Pilot1");
    }

    private ArtilleryGeneric(ArtilleryProperties artilleryproperties, ActorSpawnArg actorspawnarg)
    {
        super(artilleryproperties.meshName);
        prop = null;
        nearAirfield = false;
        dontShoot = false;
        time_lastCheckShoot = 0L;
        dying = 0;
        respawnDelay = 0L;
        hideTmr = 0L;
        RADIUS_HIDE = 0.0F;
        Skill = 0;
        skillErrCoef = 1.0F;
        outCommand = new NetMsgFiltered();
        spotterCorrection = 500F;
        bHideGunners = false;
        prop = artilleryproperties;
        delay_hide_ticks = SecsToTicks(240F);
        actorspawnarg.setStationary(this);
        collide(true);
        drawing(true);
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        startDelay = 0L;
        if(actorspawnarg.timeLenExist)
        {
            startDelay = (long)(actorspawnarg.timeLen * 60F * 1000F + 0.5F);
            if(startDelay < 0L)
                startDelay = 0L;
        }
        RADIUS_HIDE = new_RADIUS_HIDE;
        isSpotterAcGuided = new_SPOTTER;
        Skill = 4 - new_SKILL;
        skillErrCoef = (float)(Skill * Skill) * 0.211F;
        spotterCorrection = 111.25F * skillErrCoef;
        hideTmr = 0L;
        arms = new FireDevice[prop.gunProperties.length];
        for(int i = 0; i < arms.length; i++)
        {
            arms[i] = new FireDevice();
            arms[i].gun = null;
            try
            {
                arms[i].gun = (Gun)prop.gunProperties[i].gunClass.newInstance();
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Artillery: Can't create gun '" + prop.gunProperties[i].gunClass.getName() + "'");
            }
            String s = "";
            if(i != 0)
                s = "_" + i;
            arms[i].gun.set(this, "Gun" + s);
            arms[i].gun.loadBullets(-1);
            arms[i].headYaw = prop.gunProperties[i].HEAD_STD_YAW;
            arms[i].gunPitch = prop.gunProperties[i].GUN_STD_PITCH;
            int j = hierMesh().chunkFind("Head" + s);
            hierMesh().setCurChunk(j);
            Loc loc = new Loc();
            hierMesh().getChunkLocObj(loc);
            arms[i].fireOffset = new Point3d();
            loc.get(arms[i].fireOffset);
            arms[i].fireOrient = new Orient();
            loc.get(arms[i].fireOrient);
            arms[i].gun.qualityModifier = err[Skill - 1];
            if(isSpotterAcGuided)
                arms[i].gun.spotterModifier = arms[i].gun.prop.aimMaxDist;
        }

        if(!isNetMirror() && RADIUS_HIDE > 0.0F)
            hideTmr = -1L;
        setDefaultLivePose();
        startMove();
        Point3d point3d = pos.getAbsPoint();
        Airport airport = Airport.nearest(point3d, -1, 7);
        if(airport != null)
        {
            float f = (float)airport.pos.getAbsPoint().distance(point3d);
            nearAirfield = f <= 2000F;
        } else
        {
            nearAirfield = false;
        }
        dontShoot = false;
        time_lastCheckShoot = Time.current() - (long)Rnd(0.0F, 12000F);
    }

    private void Align()
    {
        pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) + (double)heightAboveLandSurface;
        o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        pos.setAbs(p, o);
        syaw = o.getYaw();
    }

    public void align()
    {
        Align();
    }

    public void startMove()
    {
        if(!interpEnd("move"))
        {
            for(int i = 0; i < arms.length; i++)
            {
                if(arms[i].aim != null)
                {
                    arms[i].aim.forgetAll();
                    arms[i].aim = null;
                }
                arms[i].aim = new Aim(this, isNetMirror());
            }

            interpPut(new Move(), "move", Time.current(), null);
        }
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

        return -1F;
    }

    public float AttackMaxRadius(Aim aim)
    {
        for(int i = 0; i < arms.length; i++)
            if(arms[i].aim.equals(aim))
                return prop.gunProperties[i].ATTACK_MAX_RADIUS;

        return 0.0F;
    }

    public float AttackMaxHeight(Aim aim)
    {
        for(int i = 0; i < arms.length; i++)
            if(arms[i].aim.equals(aim))
                return prop.gunProperties[i].ATTACK_MAX_HEIGHT;

        return 0.0F;
    }

    public boolean unmovableInFuture()
    {
        return true;
    }

    public void collisionDeath()
    {
        if(!isNet())
        {
            ShowExplode(-1F);
            destroy();
        }
    }

    public float futurePosition(float f, Point3d point3d)
    {
        pos.getAbs(point3d);
        return f > 0.0F ? f : 0.0F;
    }

    private void send_DeathCommand(Actor actor)
    {
        if(isNetMaster())
        {
            if(Mission.isDeathmatch())
            {
                float f = Mission.respawnTime("Artillery");
                respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
            } else
            {
                respawnDelay = 0L;
            }
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try
            {
                netmsgguaranted.writeByte(68);
                netmsgguaranted.writeShort(deathSeed);
                netmsgguaranted.writeFloat(arms[0].headYaw);
                netmsgguaranted.writeFloat(arms[0].gunPitch);
                netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : ((com.maddox.rts.NetObj) ((ActorNet)null)));
                for(int i = 1; i < arms.length; i++)
                {
                    netmsgguaranted.writeFloat(arms[i].headYaw);
                    netmsgguaranted.writeFloat(arms[i].gunPitch);
                }

                net.post(netmsgguaranted);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void send_RespawnCommand()
    {
        if(isNetMaster() && Mission.isDeathmatch())
        {
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
    }

    private void send_FireCommand(Actor actor, int i, float f, int j)
    {
        if(isNetMaster() && net.isMirrored() && Actor.isValid(actor) && actor.isNet())
        {
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
    }

    private void send_DeathRequest(Actor actor)
    {
        if(isNetMirror() && !(net.masterChannel() instanceof NetChannelInStream))
            try
            {
                NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
                netmsgfiltered.writeByte(68);
                netmsgfiltered.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : ((com.maddox.rts.NetObj) ((ActorNet)null)));
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
            netmsgguaranted.writeShort(0);
        else
            netmsgguaranted.writeShort(deathSeed);
        for(int i = 0; i < arms.length; i++)
        {
            netmsgguaranted.writeFloat(arms[i].headYaw);
            netmsgguaranted.writeFloat(arms[i].gunPitch);
        }

        net.postTo(netchannel, netmsgguaranted);
    }

    public float getReloadingTime(Aim aim)
    {
        for(int i = 0; i < arms.length; i++)
            if(aim.equals(arms[i].aim))
                return prop.gunProperties[i].DELAY_AFTER_SHOOT;

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
        if(nearAirfield || isNetMirror() || actor == null || !(actor instanceof Aircraft) || Math.abs(time_lastCheckShoot - Time.current()) < 12000L || (float)actor.getSpeed(null) < 10F)
            return !isSpotterAcGuided ? 0.825F : 0.98F;
        return !friendPlanesAreNear((Aircraft)actor) ? 0.75F : 0.0F;
    }

    public float minTimeRelaxAfterFight()
    {
        return 0.0F;
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

    public void gunInMove(boolean flag, Aim aim)
    {
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                float f = arms[i].aim.t();
                float f1 = arms[i].aim.anglesYaw.getDeg(f);
                float f2 = arms[i].aim.anglesPitch.getDeg(f);
                setGunAngles(i, f1, f2);
                break;
            }
            i++;
        } while(true);
    }

    public static final float KmHourToMSec(float f)
    {
        return f * 0.27778F;
    }

    Actor getFireCommandFromSpotter(Aircraft aircraft)
    {
        Actor actor = ((Maneuver)aircraft.FM).target_ground;
        return actor;
    }

    public Actor findEnemy(Aim aim)
    {
        if(isNetMirror())
            return null;
        if(Time.current() < startDelay)
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
        if(isSpotterAcGuided && spotterAc == null)
        {
            spotterAc = War.getNearestSpotter(this, AttackMaxRadius(aim) * 1.5F);
            if(spotterAc != null)
                actor = getFireCommandFromSpotter(spotterAc);
            else
                return null;
        } else
        if(spotterAc != null)
            if(spotterAc.bSpotter)
            {
                actor = getFireCommandFromSpotter(spotterAc);
            } else
            {
                spotterAc = null;
                isSpotterAcGuided = false;
                return null;
            }
        if(prop.gunProperties[i].ATTACK_FAST_TARGETS)
            NearestEnemies.set(prop.gunProperties[i].WEAPONS_MASK);
        else
            NearestEnemies.set(prop.gunProperties[i].WEAPONS_MASK, -9999.9F, KmHourToMSec(100F));
        if(!isSpotterAcGuided)
            actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), AttackMaxRadius(aim), getArmy());
        if(actor == null)
            return null;
        if(!(actor instanceof Prey))
        {
            System.out.println("arti: nearest enemies: non-Prey");
            return null;
        }
        if(!nearAirfield && !isNetMirror() && (actor instanceof Aircraft) && (float)actor.getSpeed(null) >= 10F)
            if(Math.abs(time_lastCheckShoot - Time.current()) < 12000L)
            {
                if(dontShoot)
                    return null;
            } else
            if(friendPlanesAreNear((Aircraft)actor))
                return null;
        BulletProperties bulletproperties = null;
        if(arms[i].gun.prop != null)
        {
            int k = ((Prey)actor).chooseBulletType(arms[i].gun.prop.bullet);
            if(k < 0)
                return null;
            bulletproperties = arms[i].gun.prop.bullet[k];
        }
        int l = ((Prey)actor).chooseShotpoint(bulletproperties);
        if(l < 0)
            return null;
        arms[i].aim.shotpoint_idx = l;
        double d = distance(actor);
        d /= AttackMaxDistance(aim);
        aim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
        if(actor instanceof Aircraft)
            d *= 0.25D;
        aim.scaleAimingError((float)((double)spotterCorrection * d));
        return actor;
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim)
    {
        if(i == 1 && hideTmr < 0L)
        {
            float f1 = (float)actor.pos.getAbsPoint().distanceSquared(pos.getAbsPoint());
            if(f1 > RADIUS_HIDE * RADIUS_HIDE)
                return false;
            hideTmr = 0L;
        }
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
        if(!isNetMirror())
            send_FireCommand(actor, arms[j].aim.shotpoint_idx, i != 0 ? f : -1F, j);
        return true;
    }

    private void Track_Mirror(int i, Actor actor, int j)
    {
        if(dying != 1 && actor != null && arms[i].aim != null)
            arms[i].aim.passive_StartFiring(0, actor, j, 0.0F);
    }

    private void Fire_Mirror(int i, Actor actor, int j, float f)
    {
        if(dying != 1 && actor != null && arms[i].aim != null)
        {
            if(f <= 0.2F)
                f = 0.2F;
            if(f >= 15F)
                f = 15F;
            arms[i].aim.passive_StartFiring(1, actor, j, f);
        }
    }

    void hideGunners(boolean flag)
    {
        bHideGunners = flag;
        setMesh(prop.meshName);
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean flag)
    {
        if(!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
            return 0;
        if(isSpotterAcGuided && spotterAc != null && !spotterAc.bSpotter)
        {
            for(int i = 0; i < arms.length; i++)
                arms[i].gun.spotterModifier = 0.0F;

            return 0;
        }
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
        if(arms[j].gun instanceof CannonMidrangeGeneric)
        {
            int l = ((Prey)actor).chooseBulletType(arms[j].gun.prop.bullet);
            if(l < 0)
                return 0;
            ((CannonMidrangeGeneric)arms[j].gun).setBulletType(l);
        }
        boolean flag1 = ((Prey)actor).getShotpointOffset(arms[j].aim.shotpoint_idx, p1);
        if(!flag1)
            return 0;
        float f1 = f * Rnd(0.8F, 1.2F);
        if(!Aimer.aim((BulletAimer)arms[j].gun, actor, this, f1, p1, arms[j].fireOffset))
            return 0;
        Point3d point3d = new Point3d();
        Aimer.getPredictedTargetPosition(point3d);
        point3d.add(aim.getAimingError());
        Point3d point3d1 = Aimer.getHunterFirePoint();
        float f2 = 0.01F * skillErrCoef;
        double d = point3d.distance(point3d1);
        double d1 = point3d.z;
        if(spotterAc == null)
        {
            spotterCorrection = 500F;
        } else
        {
            if(spotterCorrection > 3F)
            {
                point3d.x += World.Rnd().nextDouble(-spotterCorrection * skillErrCoef, spotterCorrection * skillErrCoef);
                point3d.y += World.Rnd().nextDouble(-spotterCorrection * skillErrCoef, spotterCorrection * skillErrCoef);
                spotterCorrection -= 3F;
            }
            point3d.sub(point3d1);
            point3d.scale(Rnd(1.0F - 0.015F * skillErrCoef, 1.0F + 0.015F * skillErrCoef));
            point3d.add(point3d1);
        }
        float f3 = arms[j].gunPitch;
        float f4 = -actor.pos.getAbsOrient().getTangage();
        if(d < 800D && f4 - f3 > -5F && f4 - f3 < 5F && (actor instanceof Aircraft))
        {
            RangeRandom rangerandom = World.Rnd();
            double d2 = 7D * (230D / d) * (230D / d);
            if(rangerandom.nextDouble(0.01D, 10D) <= d2)
            {
                float f9 = actor.pos.getAbsOrient().azimut();
                float f12 = arms[j].headYaw;
                float f14 = f12 * -1F;
                float f15 = (f9 - f14) + syaw;
                if(f15 > -185F && f15 < -175F || f15 < 185F && f15 > 175F)
                {
                    tmAAAScared = Time.current();
                    if(tmAAAScared != 0L && tmAAAScared - tmHL > 0x186a0L)
                    {
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
        if(tmAAAScared != 0L && Time.current() < tmAAAScared + 0x186a0L)
            return 0;
        if(f1 > 0.001F)
        {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            point3d2.add(aim.getAimingError());
            tmpv.sub(point3d, point3d2);
            double d3 = tmpv.length();
            if(d3 > 0.001D)
            {
                float f10 = (float)d3 / f1;
                if(f10 > 200F)
                    f10 = 200F;
                float f13 = f10 * 0.015F;
                point3d2.sub(point3d1);
                double d4 = point3d2.x * point3d2.x + point3d2.y * point3d2.y + point3d2.z * point3d2.z;
                if(d4 > 0.01D)
                {
                    float f16 = (float)tmpv.dot(point3d2);
                    f16 /= (float)(d3 * Math.sqrt(d4));
                    f16 = (float)Math.sqrt(1.0F - f16 * f16);
                    f13 *= 0.4F + 0.6F * f16;
                }
                f13 *= 1.1F;
                int j1 = Mission.curCloudsType();
                if(j1 > 2)
                {
                    float f17 = j1 <= 4 ? 500F : 300F;
                    float f18 = (float)(d / (double)f17);
                    if(f18 > 1.0F)
                    {
                        if(f18 > 10F)
                            return 0;
                        f18 = ((f18 - 1.0F) / 9F) * 2.0F + 1.0F;
                        f13 *= f18;
                    }
                }
                if(j1 >= 3 && d1 > (double)Mission.curCloudsHeight())
                    f13 *= 1.25F;
                f2 += f13;
            }
        }
        if(actor instanceof Aircraft)
        {
            aim.scaleAimingError(0.73F);
            f2 = (float)((double)f2 * (0.29999999999999999D + (double)(skillErrCoef * 0.5F)));
        } else
        if(aim.getAimingError().length() > 0.010999999999999999D * (double)skillErrCoef)
            if(spotterAc == null)
                aim.scaleAimingError(0.993F);
            else
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
        float f6 = (float)actor.getSpeed(null) - 10F;
        if(f6 > 0.0F)
        {
            float f7 = 83.33334F;
            f6 = f6 < f7 ? f6 / f7 : 1.0F;
            f2 += f6 * prop.gunProperties[j].FAST_TARGETS_ANGLE_ERROR;
        }
        Vector3d vector3d = new Vector3d();
        if(!((BulletAimer)arms[j].gun).FireDirection(point3d1, point3d, vector3d))
            return 0;
        float f8;
        float f11;
        if(flag)
        {
            f8 = 99999F;
            f11 = 99999F;
        } else
        {
            f8 = prop.gunProperties[j].HEAD_MAX_YAW_SPEED;
            f11 = prop.gunProperties[j].GUN_MAX_PITCH_SPEED;
        }
        Orient orient = new Orient();
        orient.add(arms[j].fireOrient, pos.getAbs().getOrient());
        int i1 = arms[j].aim.setRotationForTargeting(this, orient, point3d1, arms[j].headYaw, arms[j].gunPitch, vector3d, f2, f1, prop.gunProperties[j].HEAD_YAW_RANGE, prop.gunProperties[j].GUN_MIN_PITCH, prop.gunProperties[j].GUN_MAX_PITCH, f8, f11, 0.0F);
        return i1;
    }

    public void singleShot(Aim aim)
    {
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                arms[i].gun.shots(1);
                break;
            }
            i++;
        } while(true);
    }

    public void startFire(Aim aim)
    {
        int i = 0;
        do
        {
            if(i >= arms.length)
                break;
            if(aim.equals(arms[i].aim))
            {
                arms[i].gun.shots(-1);
                break;
            }
            i++;
        } while(true);
    }

    public void continueFire(Aim aim)
    {
    }

    public void stopFire(Aim aim)
    {
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

    private ArtilleryProperties prop;
    private boolean nearAirfield;
    private boolean dontShoot;
    private long time_lastCheckShoot;
    protected static final int DELAY_CHECK_SHOOT = 12000;
    protected static final int DIST_TO_FRIEND_PLANES = 4000;
    protected static final int DIST_TO_AIRFIELD = 2000;
    private float heightAboveLandSurface;
    private float heightAboveLandSurface2;
    private FireDevice arms[];
    private long startDelay;
    public int dying;
    static final int DYING_NONE = 0;
    static final int DYING_DEAD = 1;
    private short deathSeed;
    private long respawnDelay;
    private long hideTmr;
    private static long delay_hide_ticks = 0L;
    public float RADIUS_HIDE;
    public int Skill;
    public float skillErrCoef;
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
    static final float err[] = {
        1.17F, 1.37F, 1.67F, 2.13F
    };
    private NetMsgFiltered outCommand;
    float spotterCorrection;
    private long tmAAAScared;
    private long tmHL;
    private float syaw;
    boolean bHideGunners;




















}
