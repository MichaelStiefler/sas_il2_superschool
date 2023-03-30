/* 4.10.1 class */
package com.maddox.il2.objects.vehicles.artillery;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Front.Marker;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
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
import com.maddox.il2.ai.ground.TgtInfantry;
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
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
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
    private ArtilleryProperties prop                = null;
    private boolean             nearAirfield        = false;
    private boolean             dontShoot           = false;
    private long                time_lastCheckShoot = 0L;
    private static final int    DIST_TO_AIRFIELD    = 2000;
    private float               heightAboveLandSurface;
    private float               heightAboveLandSurface2;
    protected Gun               gun;
    private Aim                 aime;
    private float               headYaw;
    private float               gunPitch;
    private long                startDelay;

    // TODO: Edit by |ZUTI|: changed from private to public
    public int                         dying            = 0;

    static final int                   DYING_NONE       = 0;
    static final int                   DYING_DEAD       = 1;
    private short                      deathSeed;
    private long                       respawnDelay     = 0L;
    private long                       hideTmr          = 0L;
    private static long                delay_hide_ticks = 0L;
    public float                       RADIUS_HIDE      = 0.0F;
    public static float                new_RADIUS_HIDE  = 0.0F;
    private static ArtilleryProperties constr_arg1      = null;
    private static ActorSpawnArg       constr_arg2      = null;
    private static Point3d             p                = new Point3d();
    private static Point3d             p1               = new Point3d();
    private static Orient              o                = new Orient();
    private static Vector3f            n                = new Vector3f();
    private static Vector3d            tmpv             = new Vector3d();
    private NetMsgFiltered             outCommand       = new NetMsgFiltered();

    public static class SPAWN implements ActorSpawn {
        public Class               cls;
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
            int i = (artilleryproperties.meshSummer1 == null ? 1 : 0) + (artilleryproperties.meshDesert1 == null ? 1 : 0) + (artilleryproperties.meshWinter1 == null ? 1 : 0);
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
            String string_9_ = "com.maddox.il2.objects.weapons." + getS(sectfile, string, "Gun");
            try {
                artilleryproperties.gunClass = Class.forName(string_9_);
            } catch (Exception exception) {
                System.out.println("Artillery: Can't find gun class '" + string_9_ + "'");
                throw new RuntimeException("Can't register artillery object");
            }
            artilleryproperties.WEAPONS_MASK = GunGeneric.getProperties(artilleryproperties.gunClass).weaponType;
            if (artilleryproperties.WEAPONS_MASK == 0) {
                System.out.println("Artillery: Undefined weapon type in gun class '" + string_9_ + "'");
                throw new RuntimeException("Can't register artillery object");
            }
            artilleryproperties.ATTACK_FAST_TARGETS = true;
            float f_10_ = sectfile.get(string, "FireFastTargets", -9865.345F);
            if (f_10_ != -9865.345F) artilleryproperties.ATTACK_FAST_TARGETS = f_10_ > 0.5F;
            else if (string_8_.equals("Tank")) artilleryproperties.ATTACK_FAST_TARGETS = false;
            artilleryproperties.ATTACK_MAX_DISTANCE = getF(sectfile, string, "AttackMaxDistance", 6.0F, 12000.0F);
            artilleryproperties.ATTACK_MAX_RADIUS = getF(sectfile, string, "AttackMaxRadius", 6.0F, 12000.0F);
            artilleryproperties.ATTACK_MAX_HEIGHT = getF(sectfile, string, "AttackMaxHeight", 6.0F, 12000.0F);
            float f_11_ = getF(sectfile, string, "HeadYawHalfRange", 0.0F, 180.0F);
            artilleryproperties.HEAD_YAW_RANGE.set(-f_11_, f_11_);
            artilleryproperties.GUN_MIN_PITCH = getF(sectfile, string, "GunMinPitch", -15.0F, 85.0F);
            artilleryproperties.GUN_STD_PITCH = getF(sectfile, string, "GunStdPitch", -15.0F, 89.9F);
            artilleryproperties.GUN_MAX_PITCH = getF(sectfile, string, "GunMaxPitch", 0.0F, 89.9F);
            artilleryproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, string, "HeadMaxYawSpeed", 0.1F, 999.0F);
            artilleryproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, string, "GunMaxPitchSpeed", 0.1F, 999.0F);
            artilleryproperties.DELAY_AFTER_SHOOT = getF(sectfile, string, "DelayAfterShoot", 0.0F, 999.0F);
            artilleryproperties.CHAINFIRE_TIME = getF(sectfile, string, "ChainfireTime", 0.0F, 600.0F);
            float f_12_ = sectfile.get(string, "FastTargetsAngleError", -9865.345F);
            if (f_12_ <= 0.0F) f_12_ = 0.0F;
            else if (f_12_ >= 45.0F) f_12_ = 45.0F;
            artilleryproperties.FAST_TARGETS_ANGLE_ERROR = f_12_;
            Property.set(var_class, "iconName", "icons/" + getS(sectfile, string, "Icon") + ".mat");
            Property.set(var_class, "meshName", artilleryproperties.meshSummer);
            return artilleryproperties;
        }

        public SPAWN(Class var_class) {
            try {
                String string = var_class.getName();
                int i = string.lastIndexOf('.');
                int i_13_ = string.lastIndexOf('$');
                if (i < i_13_) i = i_13_;
                String string_14_ = string.substring(i + 1);
                this.proper = LoadArtilleryProperties(Statics.getTechnicsFile(), string_14_, var_class);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + var_class.getName());
            }
            this.cls = var_class;
            Spawn.add(this.cls, this);
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            switch (World.cur().camouflage) {
                case 1:
                    this.proper.meshName = this.proper.meshWinter;
                    this.proper.meshName2 = this.proper.meshWinter1;
                    break;
                case 2:
                    this.proper.meshName = this.proper.meshDesert;
                    this.proper.meshName2 = this.proper.meshDesert1;
                    break;
                default:
                    this.proper.meshName = this.proper.meshSummer;
                    this.proper.meshName2 = this.proper.meshSummer1;
            }
            ArtilleryGeneric artillerygeneric;
            try {
                ArtilleryGeneric.constr_arg1 = this.proper;
                ArtilleryGeneric.constr_arg2 = actorspawnarg;
                artillerygeneric = (ArtilleryGeneric) this.cls.newInstance();
                ArtilleryGeneric.constr_arg1 = null;
                ArtilleryGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                ArtilleryGeneric.constr_arg1 = null;
                ArtilleryGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Artillery object [class:" + this.cls.getName() + "]");
                return null;
            }
            return artillerygeneric;
        }
    }

    class Mirror extends ActorNet {
        NetMsgFiltered out = new NetMsgFiltered();

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) switch (netmsginput.readByte()) {
                case 73: {
                    if (this.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        this.post(netmsgguaranted);
                    }
                    short i = netmsginput.readShort();
                    float f = netmsginput.available() > 3?netmsginput.readFloat():0F;
                    float f_15_ = netmsginput.available() > 3?netmsginput.readFloat():0F;
                    if (i <= 0) {
                        if (ArtilleryGeneric.this.dying != 1) {
                            ArtilleryGeneric.this.aime.forgetAiming();
                            ArtilleryGeneric.this.setGunAngles(f, f_15_);
                        }
                    } else if (ArtilleryGeneric.this.dying != 1) {
                        // TODO: Added by |ZUTI|: AAA vehicle died, remove it's front marker position, if it has any
                        ArtilleryGeneric.this.zutiRefreshFrontMarker(true);

                        ArtilleryGeneric.this.setGunAngles(f, f_15_);
                        ArtilleryGeneric.this.Die(null, i, false);
                    }
                    return true;
                }
                case 82:
                    if (this.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        this.post(netmsgguaranted);
                    }
                    ArtilleryGeneric.this.dying = 0;
                    ArtilleryGeneric.this.setDiedFlag(false);
                    ArtilleryGeneric.this.aime.forgetAiming();
                    ArtilleryGeneric.this.setMesh(ArtilleryGeneric.this.prop.meshName);
                    ArtilleryGeneric.this.setDefaultLivePose();
                    return true;
                case 68: {
                    if (this.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
                        this.post(netmsgguaranted);
                    }
                    short i = netmsginput.readShort();
                    float f = netmsginput.available() > 3?netmsginput.readFloat():0F;
                    float f_16_ = netmsginput.available() > 3?netmsginput.readFloat():0F;
                    if (i > 0 && ArtilleryGeneric.this.dying != 1) {
                        // TODO: Added by |ZUTI|: AAA vehicle died, remove it's front marker position, if it has any
                        ArtilleryGeneric.this.zutiRefreshFrontMarker(true);

                        ArtilleryGeneric.this.setGunAngles(f, f_16_);
                        com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                        Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                        ArtilleryGeneric.this.Die(actor, i, true);
                    }
                    return true;
                }
                default:
                    return false;
            }
            switch (netmsginput.readByte()) {
                case 84: {
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    int i = netmsginput.readUnsignedByte();
                    ArtilleryGeneric.this.Track_Mirror(actor, i);
                    break;
                }
                case 70: {
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    float f = netmsginput.readFloat();
                    float f_17_ = 0.0010F * (Message.currentGameTime() - Time.current()) + f;
                    int i = netmsginput.readUnsignedByte();
                    ArtilleryGeneric.this.Fire_Mirror(actor, i, f_17_);
                    break;
                }
                case 68:
                    this.out.unLockAndSet(netmsginput, 1);
                    this.out.setIncludeTime(false);
                    this.postRealTo(Message.currentRealTime(), this.masterChannel(), this.out);
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
            if (ArtilleryGeneric.this.dying == 1) return true;
            com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
            Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
            ArtilleryGeneric.this.Die(actor, (short) 0, true);
            return true;
        }
    }

    class Move extends Interpolate {
        public boolean tick() {
            if (ArtilleryGeneric.this.dying == 1) {
                // TODO: Added by |ZUTI|: AAA vehicle is dead, remove it's front marker, if any
                ArtilleryGeneric.this.zutiRefreshFrontMarker(true);

                if (access$110(ArtilleryGeneric.this) <= 0L) {
                    if (!Mission.isDeathmatch()) {
                        if (ArtilleryGeneric.this.aime != null) {
                            ArtilleryGeneric.this.aime.forgetAll();
                            ArtilleryGeneric.this.aime = null;
                        }
                        if (ArtilleryGeneric.this.gun != null) {
                            ObjState.destroy(ArtilleryGeneric.this.gun);
                            ArtilleryGeneric.this.gun = null;
                        }
                        return false;
                    }
                    if (!ArtilleryGeneric.this.isNetMaster()) {
                        ArtilleryGeneric.this.respawnDelay = 10000L;
                        return true;
                    }
                    ArtilleryGeneric.this.dying = 0;
                    ArtilleryGeneric.this.hideTmr = 0L;
                    if (!ArtilleryGeneric.this.isNetMirror() && ArtilleryGeneric.this.RADIUS_HIDE > 0.0F) ArtilleryGeneric.this.hideTmr = -1L;
                    ArtilleryGeneric.this.setDiedFlag(false);
                    ArtilleryGeneric.this.aime.forgetAiming();
                    ArtilleryGeneric.this.setMesh(ArtilleryGeneric.this.prop.meshName);
                    ArtilleryGeneric.this.setDefaultLivePose();
                    ArtilleryGeneric.this.send_RespawnCommand();
                    ArtilleryGeneric.this.dontShoot = false;
                    ArtilleryGeneric.this.time_lastCheckShoot = Time.current() - 12000L;
                    return true;
                }
                return true;
            }
            ArtilleryGeneric.this.aime.tick_();
            if (ArtilleryGeneric.this.RADIUS_HIDE > 0.0F && ArtilleryGeneric.this.hideTmr >= 0L && !ArtilleryGeneric.this.isNetMirror()) if (ArtilleryGeneric.this.aime.getEnemy() != null) ArtilleryGeneric.this.hideTmr = 0L;
            else if (access$304(ArtilleryGeneric.this) > ArtilleryGeneric.delay_hide_ticks) ArtilleryGeneric.this.hideTmr = -1L;
            // TODO: Added by |ZUTI|: AAA vehicle moved, refresh it's front marker position, if it has any
            ArtilleryGeneric.this.zutiRefreshFrontMarker(false);

            return true;
        }
    }

    public static class ArtilleryProperties {
        public String         meshName                 = null;
        public String         meshName2                = null;
        public String         meshSummer               = null;
        public String         meshDesert               = null;
        public String         meshWinter               = null;
        public String         meshSummer1              = null;
        public String         meshDesert1              = null;
        public String         meshWinter1              = null;
        public Class          gunClass                 = null;
        public TableFunction2 fnShotPanzer             = null;
        public TableFunction2 fnExplodePanzer          = null;
        public float          PANZER_BODY_FRONT        = 0.0010F;
        public float          PANZER_BODY_BACK         = 0.0010F;
        public float          PANZER_BODY_SIDE         = 0.0010F;
        public float          PANZER_BODY_TOP          = 0.0010F;
        public float          PANZER_HEAD              = 0.0010F;
        public float          PANZER_HEAD_TOP          = 0.0010F;
        public float          PANZER_TNT_TYPE          = 1.0F;
        public String         explodeName              = null;
        public int            WEAPONS_MASK             = 4;
        public int            HITBY_MASK               = -2;
        public boolean        ATTACK_FAST_TARGETS      = true;
        public float          ATTACK_MAX_DISTANCE      = 1.0F;
        public float          ATTACK_MAX_RADIUS        = 1.0F;
        public float          ATTACK_MAX_HEIGHT        = 1.0F;
        public AnglesRange    HEAD_YAW_RANGE           = new AnglesRange(-1.0F, 1.0F);
        public float          GUN_MIN_PITCH            = -20.0F;
        public float          GUN_STD_PITCH            = -18.0F;
        public float          GUN_MAX_PITCH            = -15.0F;
        public float          HEAD_MAX_YAW_SPEED       = 720.0F;
        public float          GUN_MAX_PITCH_SPEED      = 60.0F;
        public float          DELAY_AFTER_SHOOT        = 1.0F;
        public float          CHAINFIRE_TIME           = 0.0F;
        public float          FAST_TARGETS_ANGLE_ERROR = 0.0F;
    }

    public static final double Rnd(double d, double d_18_) {
        return World.Rnd().nextDouble(d, d_18_);
    }

    public static final float Rnd(float f, float f_19_) {
        return World.Rnd().nextFloat(f, f_19_);
    }

    private boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5 + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    private boolean friendPlanesAreNear(Aircraft aircraft) {
        this.time_lastCheckShoot = Time.current() - (long) Rnd(0.0F, 1200.0F);
        this.dontShoot = false;
        Point3d point3d = aircraft.pos.getAbsPoint();
        double d = 1.6E7;
        if (!(aircraft.FM instanceof Maneuver)) return false;
        AirGroup airgroup = ((Maneuver) aircraft.FM).Group;
        if (airgroup == null) return false;
        int i = AirGroupList.length(airgroup.enemies[0]);
        for (int i_20_ = 0; i_20_ < i; i_20_++) {
            AirGroup airgroup_21_ = AirGroupList.getGroup(airgroup.enemies[0], i_20_);
            if (airgroup_21_.nOfAirc > 0) {
                double d_22_ = airgroup_21_.Pos.x - point3d.x;
                double d_23_ = airgroup_21_.Pos.y - point3d.y;
                double d_24_ = airgroup_21_.Pos.z - point3d.z;
                if (d_22_ * d_22_ + d_23_ * d_23_ + d_24_ * d_24_ <= d) {
                    d_24_ = point3d.z - Engine.land().HQ(point3d.x, point3d.y);
                    if (d_24_ > 50.0) {
                        this.dontShoot = true;
                        break;
                    }
                }
            }
        }
        return this.dontShoot;
    }

    protected final boolean Head360() {
        return this.prop.HEAD_YAW_RANGE.fullcircle();
    }

    public void msgCollisionRequest(Actor actor, boolean[] bools) {
        if (actor instanceof ActorMesh && ((ActorMesh) actor).isStaticPos()) bools[0] = false;
    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if (this.dying == 0 && !(shot.power <= 0.0F) && (!this.isNetMirror() || !shot.isMirage())) if (shot.powerType == 1) {
            if (!this.RndB(0.15F)) this.Die(shot.initiator, (short) 0, true);
        } else {
            float f = Shot.panzerThickness(this.pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), this.prop.PANZER_BODY_FRONT, this.prop.PANZER_BODY_SIDE, this.prop.PANZER_BODY_BACK, this.prop.PANZER_BODY_TOP, this.prop.PANZER_HEAD,
                    this.prop.PANZER_HEAD_TOP);
            f *= Rnd(0.93F, 1.07F);
            float f_25_ = this.prop.fnShotPanzer.Value(shot.power, f);
            if (f_25_ < 1000.0F && (f_25_ <= 1.0F || this.RndB(1.0F / f_25_))) this.Die(shot.initiator, (short) 0, true);
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (this.dying == 0 && (!this.isNetMirror() || !explosion.isMirage()) && !(explosion.power <= 0.0F)) {
            int i = explosion.powerType;
            if (explosion != null) {
                /* empty */
            }
            if (i == 1) {
                if (TankGeneric.splintersKill(explosion, this.prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, this.prop.PANZER_BODY_FRONT, this.prop.PANZER_BODY_SIDE, this.prop.PANZER_BODY_BACK, this.prop.PANZER_BODY_TOP,
                        this.prop.PANZER_HEAD, this.prop.PANZER_HEAD_TOP))
                    this.Die(explosion.initiator, (short) 0, true);
            } else {
                int i_26_ = explosion.powerType;
                if (explosion != null) {
                    /* empty */
                }
                if (i_26_ == 2 && explosion.chunkName != null) this.Die(explosion.initiator, (short) 0, true);
                else {
                    float f;
                    if (explosion.chunkName != null) f = 0.5F * explosion.power;
                    else f = explosion.receivedTNTpower(this);
                    f *= Rnd(0.95F, 1.05F);
                    float f_27_ = this.prop.fnExplodePanzer.Value(f, this.prop.PANZER_TNT_TYPE);
                    if (f_27_ < 1000.0F && (f_27_ <= 1.0F || this.RndB(1.0F / f_27_))) this.Die(explosion.initiator, (short) 0, true);
                }
            }
        }
    }

    private void ShowExplode(float f) {
        if (f > 0.0F) f = Rnd(f, f * 1.6F);
        Explosions.runByName(this.prop.explodeName, this, "SmokeHead", "", f);
    }

    private float[] computeDeathPose(short i) {
        RangeRandom rangerandom = new RangeRandom(i);
        float[] fs = new float[10];
        fs[0] = this.headYaw + rangerandom.nextFloat(-15.0F, 15.0F);
        fs[1] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 9.0F);
        fs[2] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 9.0F);
        fs[3] = -this.gunPitch + rangerandom.nextFloat(-15.0F, 15.0F);
        fs[4] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(2.0F, 5.0F);
        fs[5] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(5.0F, 9.0F);
        fs[6] = 0.0F;
        fs[7] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(4.0F, 8.0F);
        fs[8] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(7.0F, 12.0F);
        fs[9] = -rangerandom.nextFloat(0.0F, 0.25F);
        return fs;
    }

    private void Die(Actor actor, short i, boolean bool) {
        if (this.dying == 0) {
            if (i <= 0) {
                if (this.isNetMirror()) {
                    this.send_DeathRequest(actor);
                    return;
                }
                i = (short) (int) Rnd(1.0F, 30000.0F);
            }
            this.deathSeed = i;
            this.dying = 1;
            World.onActorDied(this, actor);
            if (this.aime != null) this.aime.forgetAiming();
            float[] fs = this.computeDeathPose(i);
            this.hierMesh().chunkSetAngles("Head", fs[0], fs[1], fs[2]);
            this.hierMesh().chunkSetAngles("Gun", fs[3], fs[4], fs[5]);
            this.hierMesh().chunkSetAngles("Body", fs[6], fs[7], fs[8]);
            if (this.prop.meshName2 == null) {
                this.mesh().makeAllMaterialsDarker(0.22F, 0.35F);
                this.heightAboveLandSurface2 = this.heightAboveLandSurface;
                this.heightAboveLandSurface = this.heightAboveLandSurface2 + fs[9];
            } else {
                this.setMesh(this.prop.meshName2);
                this.heightAboveLandSurface2 = 0.0F;
                int i_28_ = this.mesh().hookFind("Ground_Level");
                if (i_28_ != -1) {
                    Matrix4d matrix4d = new Matrix4d();
                    this.mesh().hookMatrix(i_28_, matrix4d);
                    this.heightAboveLandSurface2 = (float) -matrix4d.m23;
                }
                this.heightAboveLandSurface = this.heightAboveLandSurface2;
            }
            this.Align();
            // TODO: +++ TgtInfantry backport by SAS~Storebror +++
//          if (bool) this.ShowExplode(15.0F);
          if (bool && !(this instanceof TgtInfantry)) ShowExplode(15F);
          // TODO: --- TgtInfantry backport by SAS~Storebror ---
            if (bool) this.send_DeathCommand(actor);
        }
    }

    private void setGunAngles(float f, float f_29_) {
        this.headYaw = f;
        this.gunPitch = f_29_;
        this.hierMesh().chunkSetAngles("Head", this.headYaw, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Gun", -this.gunPitch, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        this.pos.inValidate(false);
    }

    public void destroy() {
        if (!this.isDestroyed()) {
            if (this.aime != null) {
                this.aime.forgetAll();
                this.aime = null;
            }
            if (this.gun != null) {
                ObjState.destroy(this.gun);
                this.gun = null;
            }
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
        this.heightAboveLandSurface = 0.0F;
        int i = this.hierMesh().hookFind("Ground_Level");
        if (i != -1) {
            Matrix4d matrix4d = new Matrix4d();
            this.hierMesh().hookMatrix(i, matrix4d);
            this.heightAboveLandSurface = (float) -matrix4d.m23;
        }
        this.setGunAngles(0.0F, this.prop.GUN_STD_PITCH);
        this.Align();
    }

    protected ArtilleryGeneric() {
        this(constr_arg1, constr_arg2);
    }

    public void setMesh(String string) {
        super.setMesh(string);
        if (!Config.cur.b3dgunners) this.mesh().materialReplaceToNull("Pilot1");
    }

    private ArtilleryGeneric(ArtilleryProperties artilleryproperties, ActorSpawnArg actorspawnarg) {
        super(artilleryproperties.meshName);
        this.prop = artilleryproperties;
        delay_hide_ticks = SecsToTicks(240.0F);
        actorspawnarg.setStationary(this);
        this.collide(true);
        this.drawing(true);
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.startDelay = 0L;
        if (actorspawnarg.timeLenExist) {
            this.startDelay = (long) (actorspawnarg.timeLen * 60.0F * 1000.0F + 0.5F);
            if (this.startDelay < 0L) this.startDelay = 0L;
        }
        this.RADIUS_HIDE = new_RADIUS_HIDE;
        this.hideTmr = 0L;
        this.gun = null;
        try {
            this.gun = (Gun) this.prop.gunClass.newInstance();
            this.gun.set(this, "Gun");
            this.gun.loadBullets(-1);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println("Artillery: Can't create gun '" + this.prop.gunClass.getName() + "'");
        }
        this.headYaw = 0.0F;
        this.gunPitch = 0.0F;
        if (!this.isNetMirror() && this.RADIUS_HIDE > 0.0F) this.hideTmr = -1L;
        this.setDefaultLivePose();
        this.startMove();
        Point3d point3d = this.pos.getAbsPoint();
        Airport airport = Airport.nearest(point3d, -1, 7);
        if (airport != null) {
            float f = (float) airport.pos.getAbsPoint().distance(point3d);
            this.nearAirfield = f <= DIST_TO_AIRFIELD;
        } else this.nearAirfield = false;
        this.dontShoot = false;
        this.time_lastCheckShoot = Time.current() - (long) Rnd(0.0F, 12000.0F);
    }

    private void Align() {
        this.pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) + this.heightAboveLandSurface;
        o.setYPR(this.pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        this.pos.setAbs(p, o);
    }

    public void align() {
        this.Align();
    }

    public void startMove() {
        if (!this.interpEnd("move")) {
            if (this.aime != null) {
                this.aime.forgetAll();
                this.aime = null;
            }
            this.aime = new Aim(this, this.isNetMirror());
            this.interpPut(new Move(), "move", Time.current(), null);
        }
    }

    public int WeaponsMask() {
        return this.prop.WEAPONS_MASK;
    }

    public int HitbyMask() {
        return this.prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties[] bulletpropertieses) {
        if (this.dying != 0) return -1;
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
        if (this.dying != 0) return -1;
        return 0;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.dying != 0) return false;
        if (i != 0) return false;
        if (point3d != null) point3d.set(0.0, 0.0, 0.0);
        return true;
    }

    public float AttackMaxDistance() {
        return this.prop.ATTACK_MAX_DISTANCE;
    }

    public float AttackMaxRadius() {
        return this.prop.ATTACK_MAX_RADIUS;
    }

    public float AttackMaxHeight() {
        return this.prop.ATTACK_MAX_HEIGHT;
    }

    public boolean unmovableInFuture() {
        return true;
    }

    public void collisionDeath() {
        if (!this.isNet()) {
            this.ShowExplode(-1.0F);
            this.destroy();
        }
    }

    public float futurePosition(float f, Point3d point3d) {
        this.pos.getAbs(point3d);

        // TODO: Added by |ZUTI|
        // ---------------------------------------------------------------------
        if (f <= 0.0) System.out.println("ArtilleryGeneric - reached final destination!");
        // ---------------------------------------------------------------------

        return f <= 0.0F ? 0.0F : f;
    }

    private void send_DeathCommand(Actor actor) {
        if (this.isNetMaster()) {
            if (Mission.isDeathmatch()) {
                float f = Mission.respawnTime("Artillery");
                this.respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
            } else this.respawnDelay = 0L;
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try {
                netmsgguaranted.writeByte(68);
                netmsgguaranted.writeShort(this.deathSeed);
                netmsgguaranted.writeFloat(this.headYaw);
                netmsgguaranted.writeFloat(this.gunPitch);
                netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
                this.net.post(netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void send_RespawnCommand() {
        if (this.isNetMaster() && Mission.isDeathmatch()) {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try {
                netmsgguaranted.writeByte(82);
                this.net.post(netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void send_FireCommand(Actor actor, int i, float f) {
        if (this.isNetMaster() && this.net.isMirrored() && Actor.isValid(actor) && actor.isNet()) {
            i &= 0xff;
            if (f < 0.0F) try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(84);
                this.outCommand.writeNetObj(actor.net);
                this.outCommand.writeByte(i);
                this.outCommand.setIncludeTime(false);
                this.net.post(Time.current(), this.outCommand);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
            else try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(70);
                this.outCommand.writeFloat(f);
                this.outCommand.writeNetObj(actor.net);
                this.outCommand.writeByte(i);
                this.outCommand.setIncludeTime(true);
                this.net.post(Time.current(), this.outCommand);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void send_DeathRequest(Actor actor) {
        if (this.isNetMirror() && !(this.net.masterChannel() instanceof NetChannelInStream)) try {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(68);
            netmsgfiltered.writeNetObj(actor == null ? (ActorNet) null : actor.net);
            netmsgfiltered.setIncludeTime(false);
            this.net.postTo(Time.current(), this.net.masterChannel(), netmsgfiltered);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) this.net = new Master(this);
        else this.net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        if (this.dying == 0) netmsgguaranted.writeShort(0);
        else netmsgguaranted.writeShort(this.deathSeed);
        netmsgguaranted.writeFloat(this.headYaw);
        netmsgguaranted.writeFloat(this.gunPitch);
        this.net.postTo(netchannel, netmsgguaranted);
    }

    public float getReloadingTime(Aim aim) {
        return this.prop.DELAY_AFTER_SHOOT;
    }

    public float chainFireTime(Aim aim) {
        return this.prop.CHAINFIRE_TIME <= 0.0F ? 0.0F : this.prop.CHAINFIRE_TIME * Rnd(0.75F, 1.25F);
    }

    public float probabKeepSameEnemy(Actor actor) {
        if (this.nearAirfield || this.isNetMirror() || actor == null || !(actor instanceof Aircraft) || Math.abs(this.time_lastCheckShoot - Time.current()) < 12000L || (float) actor.getSpeed(null) < 10.0F) return 0.75F;
        if (this.friendPlanesAreNear((Aircraft) actor)) return 0.0F;
        return 0.75F;
    }

    public float minTimeRelaxAfterFight() {
        return 0.0F;
    }

    public void gunStartParking(Aim aim) {
        aim.setRotationForParking(this.headYaw, this.gunPitch, 0.0F, this.prop.GUN_STD_PITCH, this.prop.HEAD_YAW_RANGE, this.prop.HEAD_MAX_YAW_SPEED, this.prop.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean bool, Aim aim) {
        float f = aim.t();
        float f_30_ = aim.anglesYaw.getDeg(f);
        float f_31_ = aim.anglesPitch.getDeg(f);
        this.setGunAngles(f_30_, f_31_);
    }

    public static final float KmHourToMSec(float f) {
        return f * 0.27778F;
    }

    public Actor findEnemy(Aim aim) {
        if (this.isNetMirror()) return null;
        if (Time.current() < this.startDelay) return null;
        if (this.prop.ATTACK_FAST_TARGETS) NearestEnemies.set(this.WeaponsMask());
        else NearestEnemies.set(this.WeaponsMask(), -9999.9F, KmHourToMSec(100.0F));
        Actor actor = NearestEnemies.getAFoundEnemy(this.pos.getAbsPoint(), this.AttackMaxRadius(), this.getArmy());
        if (actor == null) return null;
        if (!(actor instanceof Prey)) {
            System.out.println("arti: nearest enemies: non-Prey");
            return null;
        }
        if (!this.nearAirfield && !this.isNetMirror() && actor instanceof Aircraft && !((float) actor.getSpeed(null) < 10.0F)) if (Math.abs(this.time_lastCheckShoot - Time.current()) < 12000L) {
            if (this.dontShoot) return null;
        } else if (this.friendPlanesAreNear((Aircraft) actor)) return null;
        BulletProperties bulletproperties = null;
        if (this.gun.prop != null) {
            int i = ((Prey) actor).chooseBulletType(this.gun.prop.bullet);
            if (i < 0) return null;
            bulletproperties = this.gun.prop.bullet[i];
        }
        int i = ((Prey) actor).chooseShotpoint(bulletproperties);
        if (i < 0) return null;
        aim.shotpoint_idx = i;

        // TODO: ++ Anti Sniper Artillery Mod ++
        double d = this.distance(actor);
        d /= this.AttackMaxDistance();
        aim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
        if (actor instanceof Aircraft) d *= 0.25D;
        aim.scaleAimingError((float) (500D * d));
        // TODO: -- Anti Sniper Artillery Mod --

        return actor;
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim) {
        if (i == 1 && this.hideTmr < 0L) {
            float f_32_ = (float) actor.pos.getAbsPoint().distanceSquared(this.pos.getAbsPoint());
            if (f_32_ > this.RADIUS_HIDE * this.RADIUS_HIDE) return false;
            this.hideTmr = 0L;
        }
        if (!this.isNetMirror()) this.send_FireCommand(actor, aim.shotpoint_idx, i == 0 ? -1.0F : f);
        return true;
    }

    private void Track_Mirror(Actor actor, int i) {
        if (this.dying != 1 && actor != null && this.aime != null) this.aime.passive_StartFiring(0, actor, i, 0.0F);
    }

    private void Fire_Mirror(Actor actor, int i, float f) {
        if (this.dying != 1 && actor != null && this.aime != null) {
            if (f <= 0.2F) f = 0.2F;
            if (f >= 15.0F) f = 15.0F;
            this.aime.passive_StartFiring(1, actor, i, f);
        }
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean bool) {
        if (!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0) return 0;
        if (this.gun instanceof CannonMidrangeGeneric) {
            int i = ((Prey) actor).chooseBulletType(this.gun.prop.bullet);
            if (i < 0) return 0;
            ((CannonMidrangeGeneric) this.gun).setBulletType(i);
        }
        boolean bool_33_ = ((Prey) actor).getShotpointOffset(aim.shotpoint_idx, p1);
        if (!bool_33_) return 0;
        float f_34_ = f * Rnd(0.8F, 1.2F);
        if (!Aimer.Aim((BulletAimer) this.gun, actor, this, f_34_, p1, null)) return 0;
        Point3d point3d = new Point3d();
        Aimer.GetPredictedTargetPosition(point3d);

        // TODO: ++ Anti Sniper Artillery Mod ++
        point3d.add(aim.getAimingError());
        // TODO: -- Anti Sniper Artillery Mod --

        Point3d point3d_35_ = Aimer.GetHunterFirePoint();
        float f_36_ = 0.19F;
        double d = point3d.distance(point3d_35_);
        double d_37_ = point3d.z;
        point3d.sub(point3d_35_);
        point3d.scale(Rnd(0.96, 1.04));
        point3d.add(point3d_35_);
        if (f_34_ > 0.0010F) {
            Point3d point3d_38_ = new Point3d();
            actor.pos.getAbs(point3d_38_);

            // TODO: ++ Anti Sniper Artillery Mod ++
            point3d_38_.add(aim.getAimingError());
            // TODO: -- Anti Sniper Artillery Mod --

            tmpv.sub(point3d, point3d_38_);
            double d_39_ = tmpv.length();
            if (d_39_ > 0.0010) {
                float f_40_ = (float) d_39_ / f_34_;
                if (f_40_ > 200.0F) f_40_ = 200.0F;
                float f_41_ = f_40_ * 0.015F;
                point3d_38_.sub(point3d_35_);
                double d_42_ = point3d_38_.x * point3d_38_.x + point3d_38_.y * point3d_38_.y + point3d_38_.z * point3d_38_.z;
                if (d_42_ > 0.01) {
                    float f_43_ = (float) tmpv.dot(point3d_38_);
                    f_43_ /= (float) (d_39_ * Math.sqrt(d_42_));
                    f_43_ = (float) Math.sqrt(1.0F - f_43_ * f_43_);
                    f_41_ *= 0.4F + 0.6F * f_43_;
                }
                f_41_ *= 1.1F;
                int i = Mission.curCloudsType();
                if (i > 2) {
                    float f_44_ = i > 4 ? 300.0F : 500.0F;
                    float f_45_ = (float) (d / f_44_);
                    if (f_45_ > 1.0F) {
                        if (f_45_ > 10.0F) return 0;
                        f_45_ = (f_45_ - 1.0F) / 9.0F * 2.0F + 1.0F;
                        f_41_ *= f_45_;
                    }
                }
                if (i >= 3 && d_37_ > Mission.curCloudsHeight()) f_41_ *= 1.25F;
                f_36_ += f_41_;
            }
        }

        // TODO: ++ Anti Sniper Artillery Mod ++
        if (actor instanceof Aircraft) {
            aim.scaleAimingError(0.73F);
            f_36_ = (float) (f_36_ * 0.8D);
        } else if (aim.getAimingError().length() > 0.011D) aim.scaleAimingError(0.993F);
        // TODO: -- Anti Sniper Artillery Mod --

        if (World.Sun().ToSun.z < -0.15F) {
            float f_46_ = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if (f_46_ >= 1.0F) f_46_ = 1.0F;
            if (actor instanceof Aircraft && Time.current() - ((Aircraft) actor).tmSearchlighted < 1000L) f_46_ = 0.0F;
            f_36_ += 12.0F * f_46_;
        }
        float f_47_ = (float) actor.getSpeed(null) - 10.0F;
        if (f_47_ > 0.0F) {
            float f_48_ = 83.333336F;
            f_47_ = f_47_ >= f_48_ ? 1.0F : f_47_ / f_48_;
            f_36_ += f_47_ * this.prop.FAST_TARGETS_ANGLE_ERROR;
        }
        Vector3d vector3d = new Vector3d();
        if (!((BulletAimer) this.gun).FireDirection(point3d_35_, point3d, vector3d)) return 0;
        float f_49_;
        float f_50_;
        if (bool) {
            f_49_ = 99999.0F;
            f_50_ = 99999.0F;
        } else {
            f_49_ = this.prop.HEAD_MAX_YAW_SPEED;
            f_50_ = this.prop.GUN_MAX_PITCH_SPEED;
        }
        int i = aim.setRotationForTargeting(this, this.pos.getAbs().getOrient(), point3d_35_, this.headYaw, this.gunPitch, vector3d, f_36_, f_34_, this.prop.HEAD_YAW_RANGE, this.prop.GUN_MIN_PITCH, this.prop.GUN_MAX_PITCH, f_49_, f_50_, 0.0F);
        return i;
    }

    public void singleShot(Aim aim) {
        this.gun.shots(1);
    }

    public void startFire(Aim aim) {
        this.gun.shots(-1);
    }

    public void continueFire(Aim aim) {
        /* empty */
    }

    public void stopFire(Aim aim) {
        this.gun.shots(0);
    }

    /* synthetic */static long access$110(ArtilleryGeneric artillerygeneric) {
        return artillerygeneric.respawnDelay--;
    }

    /* synthetic */static long access$304(ArtilleryGeneric artillerygeneric) {
        return ++artillerygeneric.hideTmr;
    }

    // TODO: |ZUTI| methods and variables
    // ----------------------------------------------------------------------------------
    private ArrayList zutiFrontMarkers = null;
    private Point3d   zutiPosition     = null;

    private void zutiRefreshFrontMarker(boolean died) {
        // System.out.println("Refreshing tank front marker: " + Time.current());

        if (this.zutiFrontMarkers == null) return;

        Marker marker = (Marker) this.zutiFrontMarkers.get(0);

        if (marker == null) return;

        if (this.dying == 0) {
            this.zutiPosition = this.pos.getAbsPoint();
            marker.x = this.zutiPosition.x;
            marker.y = this.zutiPosition.y;

            com.maddox.il2.ai.Front.setMarkersChanged();
        } else {
            ZutiSupportMethods.removeFrontMarker(marker, 2);
            this.zutiFrontMarkers.clear();
            this.zutiFrontMarkers = null;
        }

        if (!this.zutiHasFrontMarkerAssigned()) return;

        if (!died && Time.current() - ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK < ZutiSupportMethods.BASE_CAPTURING_INTERVAL) return;

        // Check if new front position has overrun some home bases
        ZutiSupportMethods.checkIfAnyBornPlaceWasOverrun();

        // Set current time as last refresh time
        ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK = Time.current();
    }

    // Called from: Mission, Front
    public boolean zutiHasFrontMarkerAssigned() {
        if (this.zutiFrontMarkers == null || this.zutiFrontMarkers.size() == 0) return false;

        return true;
    }

    // Called from: Mission, Front
    public void zutiAddFrontMarker(Marker marker) {
        if (this.zutiFrontMarkers == null) this.zutiFrontMarkers = new ArrayList(1);

        this.zutiFrontMarkers.add(marker);
    }

    public void zutiResetFrontMarkers() {
        if (this.zutiFrontMarkers != null) this.zutiFrontMarkers.clear();
    }
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
