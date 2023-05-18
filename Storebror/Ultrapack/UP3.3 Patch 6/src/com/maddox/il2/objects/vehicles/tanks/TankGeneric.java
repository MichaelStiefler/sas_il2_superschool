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
import com.maddox.il2.ai.Front.Marker;
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
import com.maddox.il2.engine.GunGeneric;
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

public abstract class TankGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, UnitInterface, HunterInterface {
    private TankProperties prop                         = null;
    private int            codeName;
    private float          heightAboveLandSurface;
    private float          heightAboveLandSurface2;
    public UnitData        udata                        = new UnitData();
    private Moving         mov                          = new Moving();
    protected Eff3DActor   dust;
    protected SoundFX      engineSFX                    = null;
    protected int          engineSTimer                 = 9999999;
    protected int          ticksIn8secs                 = (int) (8.0F / Time.tickConstLenFs());
    protected Gun          gun;
    private Aim            aime;
    private float          headYaw;
    private float          gunPitch;
    private int            collisionStage               = 0;
    static final int       COLLIS_NO_COLLISION          = 0;
    static final int       COLLIS_JUST_COLLIDED         = 1;
    static final int       COLLIS_MOVING_FROM_COLLISION = 2;
    private Vector2d       collisVector                 = new Vector2d();
    private Actor          collidee;
    private StaticObstacle obs                          = new StaticObstacle();

    // TODO: Edit by |ZUTI|: changed from private to public
    public int                    dying                         = 0;

    static final int              DYING_NONE                    = 0;
    static final int              DYING_SMOKE                   = 1;
    static final int              DYING_DEAD                    = 2;
    private long                  dyingDelay                    = 0L;
    private int                   codeOfUnderlyingBridgeSegment = -1;
    private static TankProperties constr_arg1                   = null;
    private static Actor          constr_arg2                   = null;
    private static Point3d        p                             = new Point3d();
    private static Point3d        p1                            = new Point3d();
    private static Orient         o                             = new Orient();
    private static Vector3f       n                             = new Vector3f();
    private static Vector3d       tmpv                          = new Vector3d();
    private NetMsgFiltered        outCommand                    = new NetMsgFiltered();

    public static class SPAWN implements UnitSpawn {
        public Class          cls;
        public TankProperties proper;

        private static float getF(SectFile sectfile, String string, String string_0_, float f, float f_1_) {
            float f_2_ = sectfile.get(string, string_0_, -9865.345F);
            if (f_2_ == -9865.345F || f_2_ < f || f_2_ > f_1_) {
                if (f_2_ == -9865.345F) System.out.println("Tank: Parameter [" + string + "]:<" + string_0_ + "> " + "not found");
                else System.out.println("Tank: Value of [" + string + "]:<" + string_0_ + "> (" + f_2_ + ")" + " is out of range (" + f + ";" + f_1_ + ")");
                throw new RuntimeException("Can't set property");
            }
            return f_2_;
        }

        private static String getS(SectFile sectfile, String string, String string_3_) {
            String string_4_ = sectfile.get(string, string_3_);
            if (string_4_ == null || string_4_.length() <= 0) {
                System.out.print("Tank: Parameter [" + string + "]:<" + string_3_ + "> ");
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

        private static TankProperties LoadTankProperties(SectFile sectfile, String string, Class var_class) {
            TankProperties tankproperties = new TankProperties();
            String string_8_ = getS(sectfile, string, "PanzerType", null);
            if (string_8_ == null) string_8_ = "Tank";
            tankproperties.fnShotPanzer = TableFunctions.GetFunc2(string_8_ + "ShotPanzer");
            tankproperties.fnExplodePanzer = TableFunctions.GetFunc2(string_8_ + "ExplodePanzer");
            tankproperties.PANZER_TNT_TYPE = getF(sectfile, string, "PanzerSubtype", 0.0F, 100.0F);
            tankproperties.meshSummer = getS(sectfile, string, "MeshSummer");
            tankproperties.meshDesert = getS(sectfile, string, "MeshDesert", tankproperties.meshSummer);
            tankproperties.meshWinter = getS(sectfile, string, "MeshWinter", tankproperties.meshSummer);
            tankproperties.meshSummer1 = getS(sectfile, string, "MeshSummerDamage", null);
            tankproperties.meshDesert1 = getS(sectfile, string, "MeshDesertDamage", tankproperties.meshSummer1);
            tankproperties.meshWinter1 = getS(sectfile, string, "MeshWinterDamage", tankproperties.meshSummer1);
            int i = (tankproperties.meshSummer1 == null ? 1 : 0) + (tankproperties.meshDesert1 == null ? 1 : 0) + (tankproperties.meshWinter1 == null ? 1 : 0);
            if (i != 0 && i != 3) {
                System.out.println("Tank: Uncomplete set of damage meshes for '" + string + "'");
                throw new RuntimeException("Can't register tank object");
            }
            tankproperties.explodeName = getS(sectfile, string, "Explode", "Tank");
            tankproperties.PANZER_BODY_FRONT = getF(sectfile, string, "PanzerBodyFront", 0.0010F, 9.999F);
            if (sectfile.get(string, "PanzerBodyBack", -9865.345F) == -9865.345F) {
                tankproperties.PANZER_BODY_BACK = tankproperties.PANZER_BODY_FRONT;
                tankproperties.PANZER_BODY_SIDE = tankproperties.PANZER_BODY_FRONT;
                tankproperties.PANZER_BODY_TOP = tankproperties.PANZER_BODY_FRONT;
            } else {
                tankproperties.PANZER_BODY_BACK = getF(sectfile, string, "PanzerBodyBack", 0.0010F, 9.999F);
                tankproperties.PANZER_BODY_SIDE = getF(sectfile, string, "PanzerBodySide", 0.0010F, 9.999F);
                tankproperties.PANZER_BODY_TOP = getF(sectfile, string, "PanzerBodyTop", 0.0010F, 9.999F);
            }
            if (sectfile.get(string, "PanzerHead", -9865.345F) == -9865.345F) tankproperties.PANZER_HEAD = tankproperties.PANZER_BODY_FRONT;
            else tankproperties.PANZER_HEAD = getF(sectfile, string, "PanzerHead", 0.0010F, 9.999F);
            if (sectfile.get(string, "PanzerHeadTop", -9865.345F) == -9865.345F) tankproperties.PANZER_HEAD_TOP = tankproperties.PANZER_BODY_TOP;
            else tankproperties.PANZER_HEAD_TOP = getF(sectfile, string, "PanzerHeadTop", 0.0010F, 9.999F);
            float f = Math.min(Math.min(tankproperties.PANZER_BODY_BACK, tankproperties.PANZER_BODY_TOP), Math.min(tankproperties.PANZER_BODY_SIDE, tankproperties.PANZER_HEAD_TOP));
            tankproperties.HITBY_MASK = f > 0.015F ? -2 : -1;
            String string_9_ = "com.maddox.il2.objects.weapons." + getS(sectfile, string, "Gun");
            try {
                tankproperties.gunClass = Class.forName(string_9_);
            } catch (Exception exception) {
                System.out.println("Tank: Can't find gun class '" + string_9_ + "'");
                throw new RuntimeException("Can't register tank object");
            }
            tankproperties.WEAPONS_MASK = GunGeneric.getProperties(tankproperties.gunClass).weaponType;
            if (tankproperties.WEAPONS_MASK == 0) {
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
            if (f_11_ != -9865.345F) tankproperties.ATTACK_FAST_TARGETS = f_11_ > 0.5F;
            float f_12_ = sectfile.get(string, "FastTargetsAngleError", -9865.345F);
            if (f_12_ <= 0.0F) f_12_ = 0.0F;
            else if (f_12_ >= 45.0F) f_12_ = 45.0F;
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

        public SPAWN(Class var_class) {
            try {
                String string = var_class.getName();
                int i = string.lastIndexOf('.');
                int i_13_ = string.lastIndexOf('$');
                if (i < i_13_) i = i_13_;
                String string_14_ = string.substring(i + 1);
                this.proper = LoadTankProperties(Statics.getTechnicsFile(), string_14_, var_class);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in tank spawn: " + var_class.getName());
            }
            this.cls = var_class;
            Spawn.add(this.cls, this);
        }

        public Class unitClass() {
            return this.cls;
        }

        public Actor unitSpawn(int i, int i_15_, Actor actor) {
            this.proper.codeName = i;
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
            TankGeneric tankgeneric;
            try {
                TankGeneric.constr_arg1 = this.proper;
                TankGeneric.constr_arg2 = actor;
                tankgeneric = (TankGeneric) this.cls.newInstance();
                TankGeneric.constr_arg1 = null;
                TankGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                TankGeneric.constr_arg1 = null;
                TankGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Tank object [class:" + this.cls.getName() + "]");
                return null;
            }
            return tankgeneric;
        }
    }

    class Move extends Interpolate {
        public boolean tick() {
            if (TankGeneric.this.dying != 0) {
                // TODO: Added by |ZUTI|: Tank is dead, remove it's front marker, if any
                TankGeneric.this.zutiRefreshFrontMarker(true);

                TankGeneric.this.neverDust();
                if (TankGeneric.this.dying == 2) return false;
                if (access$1110(TankGeneric.this) <= 0L) {
                    TankGeneric.this.ShowExplode();
                    TankGeneric.this.MakeCrush();
                    return false;
                }
                if (TankGeneric.this.mov.rotatCurTime > 0L) {
                    TankGeneric.this.mov.rotatCurTime--;
                    float f = 1.0F - (float) TankGeneric.this.mov.rotatCurTime / (float) TankGeneric.this.mov.rotatTotTime;
                    TankGeneric.this.pos.getAbs(TankGeneric.o);
                    TankGeneric.o.setYaw(TankGeneric.this.mov.angles.getDeg(f));
                    if (TankGeneric.this.mov.normal.z < 0.0F) {
                        Engine.land().N(TankGeneric.this.mov.srcPos.x, TankGeneric.this.mov.srcPos.y, TankGeneric.n);
                        TankGeneric.o.orient(TankGeneric.n);
                    } else TankGeneric.o.orient(TankGeneric.this.mov.normal);
                    TankGeneric.this.pos.setAbs(TankGeneric.o);
                }
                return true;
            }
            boolean bool = TankGeneric.this.mov.moveCurTime < 0L && TankGeneric.this.mov.rotatCurTime < 0L;
            if (TankGeneric.this.isNetMirror() && bool) {
                TankGeneric.this.mov.switchToStay(30.0F);
                bool = false;
            }
            if (bool) {
                ChiefGround chiefground = (ChiefGround) TankGeneric.this.getOwner();
                float f = -1.0F;
                UnitMove unitmove;
                if (TankGeneric.this.collisionStage == 0) {
                    if (TankGeneric.this.prop.meshName2 != null) {
                        TankGeneric.p.x = Rnd(-0.3, 0.3);
                        TankGeneric.p.y = Rnd(-0.3, 0.3);
                        TankGeneric.p.z = 1.0;
                        unitmove = chiefground.AskMoveCommand(this.actor, TankGeneric.p, TankGeneric.this.obs);
                    } else unitmove = chiefground.AskMoveCommand(this.actor, null, TankGeneric.this.obs);
                } else if (TankGeneric.this.collisionStage == 1) {
                    TankGeneric.this.obs.collision(TankGeneric.this.collidee, chiefground, TankGeneric.this.udata);
                    TankGeneric.this.collidee = null;
                    float f_16_ = Rnd(-70.0F, 70.0F);
                    Vector2d vector2d = Rotate(TankGeneric.this.collisVector, f_16_);
                    vector2d.scale(TankGeneric.this.prop.AFTER_COLLISION_DIST * Rnd(0.87, 1.75));
                    TankGeneric.p.set(vector2d.x, vector2d.y, -1.0);
                    unitmove = chiefground.AskMoveCommand(this.actor, TankGeneric.p, TankGeneric.this.obs);
                    TankGeneric.this.collisionStage = 2;
                    f = TankGeneric.this.prop.SPEED_BACK;
                } else {
                    float f_17_ = Rnd(0.0F, 359.99F);
                    Vector2d vector2d = Rotate(TankGeneric.this.collisVector, f_17_);
                    vector2d.scale(TankGeneric.this.prop.AFTER_COLLISION_DIST * Rnd(0.2, 0.6));
                    TankGeneric.p.set(vector2d.x, vector2d.y, 1.0);
                    unitmove = chiefground.AskMoveCommand(this.actor, TankGeneric.p, TankGeneric.this.obs);
                    TankGeneric.this.collisionStage = 0;
                }
                TankGeneric.this.mov.set(unitmove, this.actor, TankGeneric.this.prop.SPEED_MAX, f, TankGeneric.this.prop.ROT_SPEED_MAX, TankGeneric.this.prop.ROT_INVIS_ANG);
                if (TankGeneric.this.StayWhenFire()) if (TankGeneric.this.Head360()) {
                    if (TankGeneric.this.aime.isInFiringMode()) TankGeneric.this.mov.switchToStay(1.3F);
                } else if (TankGeneric.this.aime.isInAimingMode()) TankGeneric.this.mov.switchToStay(1.3F);
                if (TankGeneric.this.isNetMaster()) TankGeneric.this.send_MoveCommand(TankGeneric.this.mov, f);
            }
            TankGeneric.this.aime.tick_();
            if (TankGeneric.this.dust != null) TankGeneric.this.dust._setIntesity(TankGeneric.this.mov.movingForward ? 1.0F : 0.0F);
            if (TankGeneric.this.mov.dstPos == null) {
                TankGeneric.this.mov.moveCurTime--;
                if (TankGeneric.this.engineSFX != null && TankGeneric.this.engineSTimer > 0 && --TankGeneric.this.engineSTimer == 0) TankGeneric.this.engineSFX.stop();

                // TODO: Added by |ZUTI|: Tank moved, refresh it's front marker position, if it has any
                TankGeneric.this.zutiRefreshFrontMarker(false);

                return true;
            }
            if (TankGeneric.this.engineSFX != null) if (TankGeneric.this.engineSTimer == 0) {
                TankGeneric.this.engineSFX.play();
                TankGeneric.this.engineSTimer = (int) SecsToTicks(Rnd(10.0F, 12.0F));
            } else if (TankGeneric.this.engineSTimer < TankGeneric.this.ticksIn8secs) TankGeneric.this.engineSTimer = (int) SecsToTicks(Rnd(10.0F, 12.0F));
            TankGeneric.this.pos.getAbs(TankGeneric.o);
            boolean bool_18_ = false;
            if (TankGeneric.this.mov.rotatCurTime > 0L) {
                TankGeneric.this.mov.rotatCurTime--;
                float f = 1.0F - (float) TankGeneric.this.mov.rotatCurTime / (float) TankGeneric.this.mov.rotatTotTime;
                TankGeneric.o.setYaw(TankGeneric.this.mov.angles.getDeg(f));
                bool_18_ = true;
                if (TankGeneric.this.mov.rotatCurTime <= 0L) {
                    TankGeneric.this.mov.rotatCurTime = -1L;
                    TankGeneric.this.mov.rotatingInPlace = false;
                }
            }
            if (!TankGeneric.this.mov.rotatingInPlace && TankGeneric.this.mov.moveCurTime > 0L) {
                TankGeneric.this.mov.moveCurTime--;
                double d = 1.0 - (double) TankGeneric.this.mov.moveCurTime / (double) TankGeneric.this.mov.moveTotTime;
                TankGeneric.p.x = TankGeneric.this.mov.srcPos.x * (1.0 - d) + TankGeneric.this.mov.dstPos.x * d;
                TankGeneric.p.y = TankGeneric.this.mov.srcPos.y * (1.0 - d) + TankGeneric.this.mov.dstPos.y * d;
                if (TankGeneric.this.mov.normal.z < 0.0F) {
                    TankGeneric.p.z = Engine.land().HQ(TankGeneric.p.x, TankGeneric.p.y) + TankGeneric.this.HeightAboveLandSurface();
                    Engine.land().N(TankGeneric.p.x, TankGeneric.p.y, TankGeneric.n);
                } else TankGeneric.p.z = TankGeneric.this.mov.srcPos.z * (1.0 - d) + TankGeneric.this.mov.dstPos.z * d;
                bool_18_ = false;
                TankGeneric.this.pos.setAbs(TankGeneric.p);
                if (TankGeneric.this.mov.moveCurTime <= 0L) TankGeneric.this.mov.moveCurTime = -1L;
            }
            if (TankGeneric.this.mov.normal.z < 0.0F) {
                if (bool_18_) Engine.land().N(TankGeneric.this.mov.srcPos.x, TankGeneric.this.mov.srcPos.y, TankGeneric.n);
                TankGeneric.o.orient(TankGeneric.n);
            } else TankGeneric.o.orient(TankGeneric.this.mov.normal);
            TankGeneric.this.pos.setAbs(TankGeneric.o);

            // TODO: Added by |ZUTI|: Tank moved, refresh it's front marker position, if it has any
            TankGeneric.this.zutiRefreshFrontMarker(false);

            return true;
        }
    }

    class Mirror extends ActorNet {
        NetMsgFiltered out = new NetMsgFiltered();

        private boolean handleGuaranted(NetMsgInput netmsginput) throws IOException {
            byte i = netmsginput.readByte();
            if (this.isMirrored()) {
                int i_19_ = 0;
                if (i == 68 || i == 65) i_19_ = 1;
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, i_19_);
                this.post(netmsgguaranted);
            }
            Point3d point3d = readPackedPos(netmsginput);
            Orient orient = readPackedOri(netmsginput);
            float f = readPackedAng(netmsginput);
            float f_20_ = readPackedAng(netmsginput);
            TankGeneric.this.setPosition(point3d, orient, f, f_20_);
            TankGeneric.this.mov.switchToStay(20.0F);
            switch (i) {
                case 73:
                case 105:
                    // TODO: added by |ZUTI|: Tank died, remove it's front marker position, if it has any
                    TankGeneric.this.zutiRefreshFrontMarker(true);

                    if (i == 105) TankGeneric.this.DieMirror(null, false);
                    break;
                case 65: {
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    TankGeneric.this.doAbsoluteDeath(actor);
                    break;
                }
                case 67:
                    TankGeneric.this.doCollisionDeath();
                    break;
                case 68:
                    if (TankGeneric.this.dying == 0) {
                        // TODO: Added by |ZUTI|: Tank died, remove it's front marker position, if it has any
                        TankGeneric.this.zutiRefreshFrontMarker(true);

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

        private boolean handleNonguaranted(NetMsgInput netmsginput) throws IOException {
            byte i = netmsginput.readByte();
            switch (i) {
                case 68:
                    this.out.unLockAndSet(netmsginput, 1);
                    this.out.setIncludeTime(false);
                    this.postRealTo(Message.currentRealTime(), this.masterChannel(), this.out);
                    break;
                case 84: {
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    int i_21_ = netmsginput.readUnsignedByte();
                    TankGeneric.this.Track_Mirror(actor, i_21_);
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
                    float f_22_ = 0.0010F * (Message.currentGameTime() - Time.current()) + f;
                    int i_23_ = netmsginput.readUnsignedByte();
                    TankGeneric.this.Fire_Mirror(actor, i_23_, f_22_);
                    break;
                }
                case 83:
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 0);
                        this.out.setIncludeTime(false);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    TankGeneric.this.mov.switchToStay(10.0F);
                    break;
                case 77:
                case 109: {
                    boolean bool = i == 77;
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 0);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    Point3d point3d = readPackedPos(netmsginput);
                    Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
                    vector3f.z = readPackedNormal(netmsginput);
                    if (vector3f.z >= 0.0F) {
                        vector3f.x = readPackedNormal(netmsginput);
                        vector3f.y = readPackedNormal(netmsginput);
                        float f = vector3f.length();
                        if (f > 0.0010F) vector3f.scale(1.0F / f);
                        else vector3f.set(0.0F, 0.0F, 1.0F);
                    }
                    int i_24_ = netmsginput.readUnsignedShort();
                    float f = 0.0010F * (Message.currentGameTime() - Time.current() + i_24_);
                    if (f <= 0.0F) f = 0.1F;
                    UnitMove unitmove = new UnitMove(0.0F, point3d, f, vector3f, -1.0F);
                    if (TankGeneric.this.dying == 0) TankGeneric.this.mov.set(unitmove, this.actor(), 2.0F * TankGeneric.this.prop.SPEED_MAX, bool ? 2.0F * TankGeneric.this.prop.SPEED_BACK : -1.0F, 1.3F * TankGeneric.this.prop.ROT_SPEED_MAX,
                            1.1F * TankGeneric.this.prop.ROT_INVIS_ANG);
                    break;
                }
                default:
                    System.out.println("TankGeneric: Unknown NG message");
                    return false;
            }
            return true;
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) return this.handleGuaranted(netmsginput);
            return this.handleNonguaranted(netmsginput);
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
            if (netmsginput.isGuaranted()) return false;
            byte i = netmsginput.readByte();
            switch (i) {
                case 68:
                    if (TankGeneric.this.dying == 0) {
                        com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                        Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                        TankGeneric.this.Die(actor, true);
                    }
                    break;
                case 67:
                    TankGeneric.this.collisionDeath();
                    break;
                default:
                    System.out.println("TankGeneric: Unknown M message (" + i + ")");
                    return false;
            }
            return true;
        }
    }

    protected static class TankProperties implements Cloneable {
        public int            codeName                 = 0;
        public String         meshName                 = "3do/tanks/NameNotSpecified.him";
        public String         meshName2                = null;
        public String         meshSummer               = null;
        public String         meshDesert               = null;
        public String         meshWinter               = null;
        public String         meshSummer1              = null;
        public String         meshDesert1              = null;
        public String         meshWinter1              = null;
        public Class          gunClass                 = null;
        public String         soundMove                = "models.Tank";
        public TableFunction2 fnShotPanzer             = null;
        public TableFunction2 fnExplodePanzer          = null;
        public float          PANZER_BODY_FRONT        = 0.0010F;
        public float          PANZER_BODY_BACK         = 0.0010F;
        public float          PANZER_BODY_SIDE         = 0.0010F;
        public float          PANZER_BODY_TOP          = 0.0010F;
        public float          PANZER_HEAD              = 0.0010F;
        public float          PANZER_HEAD_TOP          = 0.0010F;
        public float          PANZER_TNT_TYPE          = 1.0F;
        public int            WEAPONS_MASK             = 4;
        public int            HITBY_MASK               = -2;
        public String         explodeName              = null;
        public int            MAX_SHELLS               = 1;
        public float          ATTACK_MAX_DISTANCE      = 1.0F;
        public float          GUN_MIN_PITCH            = -30.0F;
        public float          GUN_STD_PITCH            = -29.5F;
        public float          GUN_MAX_PITCH            = -29.0F;
        public AnglesRange    HEAD_YAW_RANGE           = new AnglesRange(-1.0F, 1.0F);
        public float          HEAD_MAX_YAW_SPEED       = 3600.0F;
        public float          GUN_MAX_PITCH_SPEED      = 300.0F;
        public float          DELAY_AFTER_SHOOT        = 0.5F;
        public float          CHAINFIRE_TIME           = 0.0F;
        public boolean        ATTACK_FAST_TARGETS      = true;
        public float          FAST_TARGETS_ANGLE_ERROR = 0.0F;
        public boolean        STAY_WHEN_FIRE           = true;
        public float          SPEED_AVERAGE            = KmHourToMSec(1.0F);
        public float          SPEED_MAX                = KmHourToMSec(2.0F);
        public float          SPEED_BACK               = KmHourToMSec(1.0F);
        public float          ROT_SPEED_MAX            = 3.6F;
        public float          ROT_INVIS_ANG            = 360.0F;
        public float          BEST_SPACE               = 2.0F;
        public float          AFTER_COLLISION_DIST     = 0.1F;
        public float          COMMAND_INTERVAL         = 20.0F;
        public float          STAY_INTERVAL            = 30.0F;

        public Object clone() {
            Object object;
            try {
                object = super.clone();
            } catch (Exception exception) {
                // TODO: Fixed by SAS~Storebror: clone() is never allowed to return null value!
                // return null;
                return new TankProperties();
            }
            return object;
        }
    }

    public int getCRC(int i) {
        i = super.getCRC(i);
        i = Finger.incInt(i, this.heightAboveLandSurface);
        i = Finger.incInt(i, this.headYaw);
        i = Finger.incInt(i, this.gunPitch);
        i = Finger.incInt(i, this.collisionStage);
        i = Finger.incInt(i, this.dying);
        i = Finger.incInt(i, this.codeOfUnderlyingBridgeSegment);
        if (this.mov != null) {
            i = Finger.incInt(i, this.mov.rotatingInPlace);
            i = Finger.incInt(i, this.mov.srcPos.x);
            i = Finger.incInt(i, this.mov.srcPos.y);
            i = Finger.incInt(i, this.mov.srcPos.z);
            if (this.mov.dstPos != null) {
                i = Finger.incInt(i, this.mov.dstPos.x);
                i = Finger.incInt(i, this.mov.dstPos.y);
                i = Finger.incInt(i, this.mov.dstPos.z);
            }
        }
        if (this.aime != null) {
            i = Finger.incInt(i, this.aime.timeTot);
            i = Finger.incInt(i, this.aime.timeCur);
        }
        return i;
    }

    public static final double Rnd(double d, double d_25_) {
        return World.Rnd().nextDouble(d, d_25_);
    }

    public static final float Rnd(float f, float f_26_) {
        return World.Rnd().nextFloat(f, f_26_);
    }

    private boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public static final float KmHourToMSec(float f) {
        return f / 3.6F;
    }

    private static final float TicksToSecs(long l) {
        if (l < 0L) l = 0L;
        return l * Time.tickLenFs();
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5 + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    public static final Vector2d Rotate(Vector2d vector2d, float f) {
        float f_27_ = Geom.sinDeg(f);
        float f_28_ = Geom.cosDeg(f);
        return new Vector2d(f_28_ * vector2d.x - f_27_ * vector2d.y, f_27_ * vector2d.x + f_28_ * vector2d.y);
    }

    protected final boolean Head360() {
        return this.prop.HEAD_YAW_RANGE.fullcircle();
    }

    protected final boolean StayWhenFire() {
        return this.prop.STAY_WHEN_FIRE;
    }

    public void msgCollisionRequest(Actor actor, boolean[] bools) {
        if (actor instanceof BridgeSegment) bools[0] = false;
    }

    public void msgCollision(Actor actor, String string, String string_29_) {
        if (this.dying == 0 && !(actor instanceof Soldier) && !this.isNetMirror() && this.collisionStage == 0 && !this.aime.isInFiringMode()) {
            this.mov.switchToAsk();
            this.collisionStage = 1;
            this.collidee = actor;
            Point3d point3d = this.pos.getAbsPoint();
            Point3d point3d_30_ = actor.pos.getAbsPoint();
            this.collisVector.set(point3d.x - point3d_30_.x, point3d.y - point3d_30_.y);
            if (this.collisVector.length() >= 1.0E-6) this.collisVector.normalize();
            else {
                float f = Rnd(0.0F, 359.99F);
                this.collisVector.set(Geom.sinDeg(f), Geom.cosDeg(f));
            }
            ((ChiefGround) this.getOwner()).CollisionOccured(this, actor);
        }
    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if (this.dying == 0 && (!this.isNetMirror() || !shot.isMirage()) && !(shot.power <= 0.0F)) if (shot.powerType == 1) {
            if (!this.RndB(0.07692308F)) this.Die(shot.initiator, false);
        } else {
            float f = Shot.panzerThickness(this.pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), this.prop.PANZER_BODY_FRONT, this.prop.PANZER_BODY_SIDE, this.prop.PANZER_BODY_BACK, this.prop.PANZER_BODY_TOP, this.prop.PANZER_HEAD,
                    this.prop.PANZER_HEAD_TOP);
            f *= Rnd(0.93F, 1.07F);
            float f_31_ = this.prop.fnShotPanzer.Value(shot.power, f);
            if (f_31_ < 1000.0F && (f_31_ <= 1.0F || this.RndB(1.0F / f_31_))) this.Die(shot.initiator, false);
        }
    }

    public static boolean splintersKill(Explosion explosion, TableFunction2 tablefunction2, float f, float f_32_, ActorMesh actormesh, float f_33_, float f_34_, float f_35_, float f_36_, float f_37_, float f_38_, float f_39_, float f_40_) {
        if (explosion.power <= 0.0F) return false;
        actormesh.pos.getAbs(p);
        float[] fs = new float[2];
        explosion.computeSplintersHit(p, actormesh.mesh().collisionR(), f_33_, fs);
        fs[0] *= f * 0.85F + (1.0F - f) * 1.15F;
        int i = (int) fs[0];
        if (i <= 0) return false;
        Vector3d vector3d = new Vector3d(p.x - explosion.p.x, p.y - explosion.p.y, p.z - explosion.p.z);
        double d = vector3d.length();
        if (d < 0.0010000000474974513) vector3d.set(1.0, 0.0, 0.0);
        else vector3d.scale(1.0 / d);
        float f_41_ = Shot.panzerThickness(actormesh.pos.getAbsOrient(), vector3d, false, f_35_, f_36_, f_37_, f_38_, f_39_, f_40_);
        float f_42_ = Shot.panzerThickness(actormesh.pos.getAbsOrient(), vector3d, true, f_35_, f_36_, f_37_, f_38_, f_39_, f_40_);
        int i_43_ = (int) (i * f_34_ + 0.5F);
        int i_44_ = i - i_43_;
        if (i_44_ < 0) i_44_ = 0;
        if (explosion != null) {
            /* empty */
        }
        float f_45_ = 0.015F * fs[1] * fs[1] * 0.5F;
        float f_46_ = tablefunction2.Value(f_45_, f_41_);
        float f_47_ = tablefunction2.Value(f_45_, f_42_);
        if (i_44_ > 0 && f_46_ <= 1.0F || i_43_ > 0 && f_47_ <= 1.0F) return true;
        float f_48_ = 0.0F;
        if (f_46_ < 1000.0F) {
            float f_49_ = 1.0F / f_46_;
            while (i_44_-- > 0)
                f_48_ += (1.0F - f_48_) * f_49_;
        }
        if (f_47_ < 1000.0F) {
            float f_50_ = 1.0F / f_47_;
            while (i_43_-- > 0)
                f_48_ += (1.0F - f_48_) * f_50_;
        }
        return f_48_ > 0.0010F && f_48_ >= f_32_;
    }

    public void msgExplosion(Explosion explosion) {
        if (this.dying == 0 && (!this.isNetMirror() || !explosion.isMirage()) && !(explosion.power <= 0.0F)) {
            int i = explosion.powerType;
            if (explosion != null) {
                /* empty */
            }
            if (i == 1) {
                if (splintersKill(explosion, this.prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, this.prop.PANZER_BODY_FRONT, this.prop.PANZER_BODY_SIDE, this.prop.PANZER_BODY_BACK, this.prop.PANZER_BODY_TOP,
                        this.prop.PANZER_HEAD, this.prop.PANZER_HEAD_TOP))
                    this.Die(explosion.initiator, false);
            } else {
                int i_51_ = explosion.powerType;
                if (explosion != null) {
                    /* empty */
                }
                if (i_51_ == 2 && explosion.chunkName != null) this.Die(explosion.initiator, false);
                else {
                    float f;
                    if (explosion.chunkName != null) f = 0.5F * explosion.power;
                    else f = explosion.receivedTNTpower(this);
                    f *= Rnd(0.95F, 1.05F);
                    float f_52_ = this.prop.fnExplodePanzer.Value(f, this.prop.PANZER_TNT_TYPE);
                    if (f_52_ < 1000.0F && (f_52_ <= 1.0F || this.RndB(1.0F / f_52_))) this.Die(explosion.initiator, true);
                }
            }
        }
    }

    private void neverDust() {
        if (this.dust != null) {
            this.dust._finish();
            this.dust = null;
        }
    }

    private void RunSmoke(float f, float f_53_) {
        boolean bool = this.RndB(f);
        String string = bool ? "SmokeHead" : "SmokeEngine";
        Explosions.runByName("_TankSmoke_", this, string, "", Rnd(f_53_, f_53_ * 1.6F));
    }

    private void ShowExplode() {
        Explosions.runByName(this.prop.explodeName, this, "", "", -1.0F);
    }

    private void MakeCrush() {
        this.dying = 2;
        Point3d point3d = this.pos.getAbsPoint();
        long l = (long) (point3d.x % 2.1 * 221.0 + point3d.y % 3.1 * 211.0 * 211.0);
        RangeRandom rangerandom = new RangeRandom(l);
        float[] fs = new float[3];
        float[] fs_54_ = new float[3];
        fs[0] = fs[1] = fs[2] = 0.0F;
        fs_54_[0] = fs_54_[1] = fs_54_[2] = 0.0F;
        fs_54_[0] = this.headYaw + rangerandom.nextFloat(-45.0F, 45.0F);
        fs_54_[1] = rangerandom.nextFloat(-3.0F, 0.0F);
        fs_54_[2] = rangerandom.nextFloat(-9.0F, 9.0F);
        fs[2] = -rangerandom.nextFloat(0.0F, 0.1F);
        this.hierMesh().chunkSetLocate("Head", fs, fs_54_);
        fs[0] = fs[1] = fs[2] = 0.0F;
        fs_54_[0] = fs_54_[1] = fs_54_[2] = 0.0F;
        fs_54_[0] = -(this.prop.GUN_MIN_PITCH - rangerandom.nextFloat(6.0F, 10.0F));
        fs_54_[1] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(14.0F, 25.0F);
        this.hierMesh().chunkSetLocate("Gun", fs, fs_54_);
        fs[0] = fs[1] = fs[2] = 0.0F;
        fs_54_[0] = fs_54_[1] = fs_54_[2] = 0.0F;
        fs_54_[1] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(1.0F, 5.0F);
        fs_54_[2] = (rangerandom.nextBoolean() ? 1.0F : -1.0F) * rangerandom.nextFloat(7.0F, 13.0F);
        this.hierMesh().chunkSetLocate("Body", fs, fs_54_);
        this.engineSFX = null;
        this.engineSTimer = 99999999;
        this.breakSounds();
        this.neverDust();
        if (this.prop.meshName2 == null) {
            this.mesh().makeAllMaterialsDarker(0.22F, 0.35F);
            this.heightAboveLandSurface2 = this.heightAboveLandSurface;
            point3d.z -= rangerandom.nextFloat(0.3F, 0.6F);
        } else {
            this.setMesh(this.prop.meshName2);
            this.heightAboveLandSurface2 = 0.0F;
            int i = this.mesh().hookFind("Ground_Level");
            if (i != -1) {
                Matrix4d matrix4d = new Matrix4d();
                this.mesh().hookMatrix(i, matrix4d);
                this.heightAboveLandSurface2 = (float) -matrix4d.m23;
            }
            point3d.z += this.heightAboveLandSurface2 - this.heightAboveLandSurface;
        }
        this.pos.setAbs(point3d);
        this.pos.reset();
    }

    private void Die(Actor actor, boolean bool) {
        if (this.isNetMirror()) this.send_DeathRequest(actor);
        else {
            if (this.aime != null) {
                this.aime.forgetAll();
                this.aime = null;
            }
            if (this.gun != null) {
                ObjState.destroy(this.gun);
                this.gun = null;
            }
            this.collisionStage = 1;
            int i = ((ChiefGround) this.getOwner()).getCodeOfBridgeSegment(this);
            if (i >= 0) {
                if (BridgeSegment.isEncodedSegmentDamaged(i)) {
                    this.absoluteDeath(actor);
                    return;
                }
                LongBridge.AddTraveller(i, this);
                this.codeOfUnderlyingBridgeSegment = i;
            }
            ((ChiefGround) this.getOwner()).Detach(this, actor);
            World.onActorDied(this, actor);
            if (this.isNet() || this.prop.meshName2 == null) bool = true;
            if (!bool) bool = this.RndB(0.35F);
            if (bool) {
                this.ShowExplode();
                this.RunSmoke(0.3F, 15.0F);
                if (this.isNetMaster()) {
                    this.send_DeathCommand(actor);
                    Point3d point3d = simplifyPos(this.pos.getAbsPoint());
                    Orient orient = simplifyOri(this.pos.getAbsOrient());
                    float f = simplifyAng(this.headYaw);
                    float f_55_ = simplifyAng(this.gunPitch);
                    this.setPosition(point3d, orient, f, f_55_);
                }
                this.MakeCrush();
            } else {
                this.dying = 1;
                this.dyingDelay = SecsToTicks(Rnd(6.0F, 12.0F));
                this.mov.switchToRotate(this, (this.RndB(0.5F) ? 1.0F : -1.0F) * Rnd(70.0F, 170.0F), this.prop.ROT_SPEED_MAX);
                this.RunSmoke(0.2F, 17.0F);
            }
        }
    }

    private void DieMirror(Actor actor, boolean bool) {
        if (!this.isNetMirror()) System.out.println("Internal error in TankGeneric: DieMirror");
        if (this.aime != null) {
            this.aime.forgetAll();
            this.aime = null;
        }
        if (this.gun != null) {
            ObjState.destroy(this.gun);
            this.gun = null;
        }
        this.collisionStage = 1;
        ((ChiefGround) this.getOwner()).Detach(this, actor);
        World.onActorDied(this, actor);
        if (bool) {
            this.ShowExplode();
            this.RunSmoke(0.3F, 15.0F);
        }
        this.MakeCrush();
    }

    public void destroy() {
        if (this.dust != null && !this.dust.isDestroyed()) this.dust._finish();
        this.dust = null;
        this.engineSFX = null;
        this.engineSTimer = 99999999;
        this.breakSounds();
        if (this.codeOfUnderlyingBridgeSegment >= 0) LongBridge.DelTraveller(this.codeOfUnderlyingBridgeSegment, this);
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

    private void setPosition(Point3d point3d, Orient orient, float f, float f_56_) {
        this.headYaw = f;
        this.gunPitch = f_56_;
        this.hierMesh().chunkSetAngles("Head", this.headYaw, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Gun", -this.gunPitch, 0.0F, 0.0F);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    protected TankGeneric() {
        this(constr_arg1, constr_arg2);
    }

    public void setMesh(String string) {
        super.setMesh(string);
        if (!Config.cur.b3dgunners) this.mesh().materialReplaceToNull("Pilot1");
    }

    private TankGeneric(TankProperties tankproperties, Actor actor) {
        super(tankproperties.meshName);
        this.prop = tankproperties;
        this.collide(true);
        this.drawing(true);
        this.setOwner(actor);
        this.codeName = tankproperties.codeName;
        this.setName(actor.name() + this.codeName);
        this.setArmy(actor.getArmy());
        if (this.mesh().hookFind("SmokeHead") < 0) System.out.println("Tank " + this.getClass().getName() + ": hook SmokeHead not found");
        if (this.mesh().hookFind("SmokeEngine") < 0) System.out.println("Tank " + this.getClass().getName() + ": hook SmokeEngine not found");
        if (this.mesh().hookFind("Ground_Level") < 0) System.out.println("Tank " + this.getClass().getName() + ": hook Ground_Level not found");
        this.heightAboveLandSurface = 0.0F;
        int i = this.mesh().hookFind("Ground_Level");
        if (i != -1) {
            Matrix4d matrix4d = new Matrix4d();
            this.mesh().hookMatrix(i, matrix4d);
            this.heightAboveLandSurface = (float) -matrix4d.m23;
        }
        HookNamed hooknamed;
        HookNamed hooknamed_58_;
        try {
            hooknamed = new HookNamed(this, "DustL");
            hooknamed_58_ = new HookNamed(this, "DustR");
        } catch (Exception exception) {
            hooknamed = hooknamed_58_ = null;
        }
        if (hooknamed == null || hooknamed_58_ == null) this.dust = null;
        else {
            Loc loc = new Loc();
            Loc loc_59_ = new Loc();
            Loc loc_60_ = new Loc();
            hooknamed.computePos(this, loc, loc_59_);
            hooknamed_58_.computePos(this, loc, loc_60_);
            Loc loc_61_ = new Loc();
            loc_61_.interpolate(loc_59_, loc_60_, 0.5F);
            this.dust = Eff3DActor.New(this, null, loc_61_, 1.0F, "Effects/Smokes/TankDust.eff", -1.0F);
            if (this.dust != null) this.dust._setIntesity(0.0F);
        }
        if (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 101) {
            i = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if (netchannel == null) this.net = new Master(this);
            else if (i != 0) this.net = new Mirror(this, netchannel, i);
        }
        this.gun = null;
        try {
            this.gun = (Gun) this.prop.gunClass.newInstance();
            this.gun.set(this, "Gun");
            this.gun.loadBullets(this.isNetMirror() ? -1 : this.prop.MAX_SHELLS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println("Tank: Can't create gun '" + this.prop.gunClass.getName() + "'");
        }
        this.headYaw = 0.0F;
        this.gunPitch = this.prop.GUN_STD_PITCH;
        this.hierMesh().chunkSetAngles("Head", this.headYaw, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Gun", -this.gunPitch, 0.0F, 0.0F);
        this.aime = new Aim(this, this.isNetMirror());
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

    private void send_CollisionDeathRequest() {
        if (this.isNetMirror() && !(this.net.masterChannel() instanceof NetChannelInStream)) try {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(67);
            netmsgfiltered.setIncludeTime(false);
            this.net.postTo(Time.current(), this.net.masterChannel(), netmsgfiltered);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
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

    private void send_AnByteAndPoseCommand(boolean bool, Actor actor, int i) {
        if (this.isNetMaster() && this.net.isMirrored()) {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try {
                netmsgguaranted.writeByte(i);
                this.sendPose(netmsgguaranted);
                if (bool) netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
                this.net.post(netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void send_DeathCommand(Actor actor) {
        this.send_AnByteAndPoseCommand(true, actor, 68);
    }

    private void send_AbsoluteDeathCommand(Actor actor) {
        this.send_AnByteAndPoseCommand(true, actor, 65);
    }

    private void send_CollisionDeathCommand() {
        this.send_AnByteAndPoseCommand(false, null, 67);
    }

    private void send_MoveCommand(Moving moving, float f) {
        if (this.isNetMaster() && this.net.isMirrored() && (moving.moveCurTime >= 0L || moving.rotatCurTime >= 0L)) try {
            this.outCommand.unLockAndClear();
            if (moving.dstPos == null || moving.moveTotTime <= 0L || moving.normal == null) {
                this.outCommand.writeByte(83);
                this.outCommand.setIncludeTime(false);
                this.net.post(Time.current(), this.outCommand);
            } else {
                if (f > 0.0F) this.outCommand.writeByte(77);
                else this.outCommand.writeByte(109);
                this.outCommand.write(packPos(moving.dstPos));
                this.outCommand.writeByte(packNormal(moving.normal.z));
                if (moving.normal.z >= 0.0F) {
                    this.outCommand.writeByte(packNormal(moving.normal.x));
                    this.outCommand.writeByte(packNormal(moving.normal.y));
                }
                int i = (int) (Time.tickLen() * moving.moveTotTime);
                if (i >= 65536) i = 65535;
                this.outCommand.writeShort(i);
                this.outCommand.setIncludeTime(true);
                this.net.post(Time.current(), this.outCommand);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    static int packAng(float f) {
        return 0xff & (int) (f * 256.0F / 360.0F);
    }

    static int packNormal(float f) {
        f++;
        f *= 0.5F;
        f *= 254.0F;
        int i = (int) (f + 0.5F);
        if (i < 0) i = 0;
        if (i > 254) i = 254;
        return i - 127;
    }

    static byte[] packPos(Point3d point3d) {
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

    static byte[] packOri(Orient orient) {
        byte[] is = new byte[3];
        int i = (int) (orient.getYaw() * 256.0F / 360.0F);
        int i_64_ = (int) (orient.getPitch() * 256.0F / 360.0F);
        int i_65_ = (int) (orient.getRoll() * 256.0F / 360.0F);
        is[0] = (byte) (i & 0xff);
        is[1] = (byte) (i_64_ & 0xff);
        is[2] = (byte) (i_65_ & 0xff);
        return is;
    }

    static float unpackAng(int i) {
        return i * 360.0F / 256.0F;
    }

    static float unpackNormal(int i) {
        return i / 127.0F;
    }

    static Point3d unpackPos(byte[] is) {
        int i = ((is[2] & 0xff) << 16) + ((is[1] & 0xff) << 8) + ((is[0] & 0xff) << 0);
        int i_66_ = ((is[5] & 0xff) << 16) + ((is[4] & 0xff) << 8) + ((is[3] & 0xff) << 0);
        int i_67_ = ((is[7] & 0xff) << 8) + ((is[6] & 0xff) << 0);
        return new Point3d(i * 0.05, i_66_ * 0.05, i_67_ * 0.1);
    }

    static Orient unpackOri(byte[] is) {
        int i = is[0] & 0xff;
        int i_68_ = is[1] & 0xff;
        int i_69_ = is[2] & 0xff;
        Orient orient = new Orient();
        orient.setYPR(i * 360.0F / 256.0F, i_68_ * 360.0F / 256.0F, i_69_ * 360.0F / 256.0F);
        return orient;
    }

    static float simplifyAng(float f) {
        return unpackAng(packAng(f));
    }

    static Point3d simplifyPos(Point3d point3d) {
        return unpackPos(packPos(point3d));
    }

    static Orient simplifyOri(Orient orient) {
        return unpackOri(packOri(orient));
    }

    static float readPackedAng(NetMsgInput netmsginput) throws IOException {
        return unpackAng(netmsginput.readByte());
    }

    static float readPackedNormal(NetMsgInput netmsginput) throws IOException {
        return unpackNormal(netmsginput.readByte());
    }

    static Point3d readPackedPos(NetMsgInput netmsginput) throws IOException {
        byte[] is = new byte[8];
        netmsginput.read(is);
        return unpackPos(is);
    }

    static Orient readPackedOri(NetMsgInput netmsginput) throws IOException {
        byte[] is = new byte[3];
        netmsginput.read(is);
        return unpackOri(is);
    }

    private void sendPose(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.write(packPos(this.pos.getAbsPoint()));
        netmsgguaranted.write(packOri(this.pos.getAbsOrient()));
        netmsgguaranted.writeByte(packAng(this.headYaw));
        netmsgguaranted.writeByte(packAng(this.gunPitch));
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        int i = this.dying == 0 ? 73 : 105;
        netmsgguaranted.writeByte(i);
        this.sendPose(netmsgguaranted);
        this.net.postTo(netchannel, netmsgguaranted);
    }

    public void startMove() {
        if (!this.interpEnd("move")) {
            this.mov = new Moving();
            if (this.aime != null) {
                this.aime.forgetAll();
                this.aime = null;
            }
            this.aime = new Aim(this, this.isNetMirror());
            this.collisionStage = 0;
            this.interpPut(new Move(), "move", Time.current(), null);
            this.engineSFX = this.newSound(this.prop.soundMove, true);
            this.engineSTimer = (int) SecsToTicks(Rnd(5.0F, 7.0F));
        }
    }

    public void forceReaskMove() {
        if (!this.isNetMirror() && this.collisionStage == 0 && this.dying == 0 && this.mov != null && this.mov.normal != null) this.mov.switchToAsk();
    }

    public UnitData GetUnitData() {
        return this.udata;
    }

    public float HeightAboveLandSurface() {
        return this.heightAboveLandSurface;
    }

    public float SpeedAverage() {
        return this.prop.SPEED_AVERAGE;
    }

    public float BestSpace() {
        return this.prop.BEST_SPACE;
    }

    public float CommandInterval() {
        return this.prop.COMMAND_INTERVAL;
    }

    public float StayInterval() {
        return this.prop.STAY_INTERVAL;
    }

    public UnitInPackedForm Pack() {
        int i = Finger.Int(this.getClass().getName());
        int i_70_ = 0;
        return new UnitInPackedForm(this.codeName, i, i_70_, this.SpeedAverage(), this.BestSpace(), this.WeaponsMask(), this.HitbyMask());
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

    public void absoluteDeath(Actor actor) {
        if (!this.isNetMirror()) {
            if (this.isNetMaster()) this.send_AbsoluteDeathCommand(actor);
            this.doAbsoluteDeath(actor);
        }
    }

    private void doAbsoluteDeath(Actor actor) {
        ChiefGround chiefground = (ChiefGround) this.getOwner();
        if (chiefground != null) chiefground.Detach(this, actor);
        if (!this.getDiedFlag()) World.onActorDied(this, actor);
        Explosions.Tank_ExplodeCollapse(this.pos.getAbsPoint());
        this.destroy();
    }

    public boolean unmovableInFuture() {
        return this.dying != 0;
    }

    public void collisionDeath() {
        if (this.isNetMirror()) this.send_CollisionDeathRequest();
        else {
            if (this.isNetMaster()) this.send_CollisionDeathCommand();
            this.doCollisionDeath();
        }
    }

    private void doCollisionDeath() {
        ChiefGround chiefground = (ChiefGround) this.getOwner();
        boolean bool = chiefground == null && this.codeOfUnderlyingBridgeSegment >= 0 || chiefground != null && chiefground.getCodeOfBridgeSegment(this) >= 0;
        if (chiefground != null) chiefground.Detach(this, null);
        if (bool) Explosions.Tank_ExplodeCollapse(this.pos.getAbsPoint());
        else Explosions.Tank_ExplodeCollapse(this.pos.getAbsPoint());
        this.destroy();
    }

    public float futurePosition(float f, Point3d point3d) {
        this.pos.getAbs(point3d);
        if (f <= 0.0F) return 0.0F;
        if (this.mov.moveCurTime < 0L && this.mov.rotatCurTime < 0L) return 0.0F;
        float f_71_ = TicksToSecs(this.mov.moveCurTime);
        if (this.mov.dstPos == null) {
            if (f_71_ >= f) return f;
            return f_71_;
        }
        float f_72_ = 0.0F;
        if (this.mov.rotatingInPlace) {
            f_72_ = TicksToSecs(this.mov.rotatCurTime);
            if (f_72_ >= f) return f;
        }
        if (f_71_ <= 0.0F) return f_72_;
        if (f_72_ + f_71_ <= f) {
            point3d.set(this.mov.dstPos);
            return f_72_ + f_71_;
        }
        Point3d point3d_73_ = new Point3d();
        point3d_73_.set(this.mov.dstPos);
        double d = (f - f_72_) / f_71_;
        p.x = point3d.x * (1.0 - d) + point3d_73_.x * d;
        p.y = point3d.y * (1.0 - d) + point3d_73_.y * d;
        if (this.mov.normal.z < 0.0F) p.z = Engine.land().HQ(p.x, p.y) + this.HeightAboveLandSurface();
        else p.z = point3d.z * (1.0 - d) + point3d_73_.z * d;
        point3d.set(p);
        return f;
    }

    public float getReloadingTime(Aim aim) {
        if (this.isNetMirror()) return 1.0F;
        if (this.gun.haveBullets()) return this.prop.DELAY_AFTER_SHOOT;
        this.gun.loadBullets(this.prop.MAX_SHELLS);
        return 120.0F;
    }

    public float chainFireTime(Aim aim) {
        return this.prop.CHAINFIRE_TIME <= 0.0F ? 0.0F : this.prop.CHAINFIRE_TIME * Rnd(0.75F, 1.25F);
    }

    public float probabKeepSameEnemy(Actor actor) {
        return this.Head360() ? 0.8F : 0.0F;
    }

    public float minTimeRelaxAfterFight() {
        return this.Head360() ? 0.0F : 10.0F;
    }

    public void gunStartParking(Aim aim) {
        aim.setRotationForParking(this.headYaw, this.gunPitch, 0.0F, this.prop.GUN_STD_PITCH, this.prop.HEAD_YAW_RANGE, this.prop.HEAD_MAX_YAW_SPEED, this.prop.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean bool, Aim aim) {
        float f = aim.t();
        if (this.Head360() || bool || !aim.bodyRotation) {
            this.headYaw = aim.anglesYaw.getDeg(f);
            this.gunPitch = aim.anglesPitch.getDeg(f);
            this.hierMesh().chunkSetAngles("Head", this.headYaw, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Gun", -this.gunPitch, 0.0F, 0.0F);
            this.pos.inValidate(false);
            return;
        }
        float f1 = aim.anglesYaw.getDeg(f);
        this.pos.getAbs(o);
        o.setYaw(f1);
        if (this.mov != null && this.mov.normal != null) if (this.mov.normal.z < 0.0F) {
            com.maddox.il2.engine.Engine.land().N(this.mov.srcPos.x, this.mov.srcPos.y, n);
            o.orient(n);
        } else o.orient(this.mov.normal);
        this.pos.setAbs(o);
        this.gunPitch = aim.anglesPitch.getDeg(f);
        this.hierMesh().chunkSetAngles("Gun", -this.gunPitch, 0.0F, 0.0F);
        this.pos.inValidate(false);
    }

    public Actor findEnemy(Aim aim) {
        if (this.isNetMirror()) return null;
        Actor actor = null;
        ChiefGround chiefground = (ChiefGround) this.getOwner();
        if (chiefground != null) {
            if (chiefground.getCodeOfBridgeSegment(this) >= 0) return null;
            actor = chiefground.GetNearestEnemy(this.pos.getAbsPoint(), this.AttackMaxDistance(), this.WeaponsMask(), this.prop.ATTACK_FAST_TARGETS ? -1.0F : KmHourToMSec(100.0F));
        }
        if (actor == null) return null;
        BulletProperties bulletproperties = null;
        if (this.gun.prop != null) {
            int i = ((Prey) actor).chooseBulletType(this.gun.prop.bullet);
            if (i < 0) return null;
            bulletproperties = this.gun.prop.bullet[i];
        }
        int i = ((Prey) actor).chooseShotpoint(bulletproperties);
        if (i < 0) return null;
        aim.shotpoint_idx = i;
        return actor;
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim) {
        if (i == 1 || !this.Head360() && aim.bodyRotation) {
            if (this.collisionStage != 0) return false;
            if (this.StayWhenFire()) this.mov.switchToStay(1.5F);
        }
        if (!this.isNetMirror()) this.send_FireCommand(actor, aim.shotpoint_idx, i == 0 ? -1.0F : f);
        return true;
    }

    private void Track_Mirror(Actor actor, int i) {
        if (this.dying == 0 && actor != null && this.aime != null) this.aime.passive_StartFiring(0, actor, i, 0.0F);
    }

    private void Fire_Mirror(Actor actor, int i, float f) {
        if (this.dying == 0 && actor != null && this.aime != null) {
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
        boolean bool_75_ = ((Prey) actor).getShotpointOffset(aim.shotpoint_idx, p1);
        if (!bool_75_) return 0;
        float f_76_ = f * Rnd(0.8F, 1.2F);
        if (!Aimer.Aim((BulletAimer) this.gun, actor, this, f_76_, p1, null)) return 0;
        Point3d point3d = new Point3d();
        Aimer.GetPredictedTargetPosition(point3d);
        Point3d point3d_77_ = Aimer.GetHunterFirePoint();
        float f_78_ = 0.19F;
        double d = point3d.distance(point3d_77_);
        double d_79_ = point3d.z;
        point3d.sub(point3d_77_);
        point3d.scale(Rnd(0.95, 1.05));
        point3d.add(point3d_77_);
        if (f_76_ > 0.0010F) {
            Point3d point3d_80_ = new Point3d();
            actor.pos.getAbs(point3d_80_);
            tmpv.sub(point3d, point3d_80_);
            double d_81_ = tmpv.length();
            if (d_81_ > 0.0010) {
                float f_82_ = (float) d_81_ / f_76_;
                if (f_82_ > 200.0F) f_82_ = 200.0F;
                float f_83_ = f_82_ * 0.02F;
                point3d_80_.sub(point3d_77_);
                double d_84_ = point3d_80_.x * point3d_80_.x + point3d_80_.y * point3d_80_.y + point3d_80_.z * point3d_80_.z;
                if (d_84_ > 0.01) {
                    float f_85_ = (float) tmpv.dot(point3d_80_);
                    f_85_ /= (float) (d_81_ * Math.sqrt(d_84_));
                    f_85_ = (float) Math.sqrt(1.0F - f_85_ * f_85_);
                    f_83_ *= 0.4F + 0.6F * f_85_;
                }
                int i = Mission.curCloudsType();
                if (i >= 3) {
                    float f_86_ = i >= 5 ? 250.0F : 500.0F;
                    float f_87_ = (float) (d / f_86_);
                    if (f_87_ > 1.0F) {
                        if (f_87_ > 10.0F) return 0;
                        f_87_ = (f_87_ - 1.0F) / 9.0F * 2.0F + 1.0F;
                        f_83_ *= f_87_;
                    }
                }
                if (i >= 3 && d_79_ > Mission.curCloudsHeight()) f_83_ *= 1.25F;
                f_78_ += f_83_;
            }
        }
        if (World.Sun().ToSun.z < -0.15F) {
            float f_88_ = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if (f_88_ >= 1.0F) f_88_ = 1.0F;
            if (actor instanceof Aircraft && Time.current() - ((Aircraft) actor).tmSearchlighted < 1000L) f_88_ = 0.0F;
            f_78_ += 12.0F * f_88_;
        }
        float f_89_ = (float) actor.getSpeed(null);
        float f_90_ = 83.333336F;
        f_89_ = f_89_ >= f_90_ ? 1.0F : f_89_ / f_90_;
        f_78_ += f_89_ * this.prop.FAST_TARGETS_ANGLE_ERROR;
        Vector3d vector3d = new Vector3d();
        if (!((BulletAimer) this.gun).FireDirection(point3d_77_, point3d, vector3d)) return 0;
        float f_91_;
        float f_92_;
        float f_93_;
        if (bool) {
            f_91_ = 99999.0F;
            f_92_ = 99999.0F;
            f_93_ = 99999.0F;
        } else {
            f_91_ = this.prop.HEAD_MAX_YAW_SPEED;
            f_92_ = this.prop.GUN_MAX_PITCH_SPEED;
            f_93_ = this.prop.ROT_SPEED_MAX;
        }
        Orient orient = this.pos.getAbs().getOrient();
        f_90_ = 0.0F;
        if (!this.Head360()) f_90_ = orient.getYaw();
        int i = aim.setRotationForTargeting(this, orient, point3d_77_, this.headYaw, this.gunPitch, vector3d, f_78_, f_76_, this.prop.HEAD_YAW_RANGE, this.prop.GUN_MIN_PITCH, this.prop.GUN_MAX_PITCH, f_91_, f_92_, f_93_);
        if (!this.Head360() && i != 0 && aim.bodyRotation) aim.anglesYaw.rotateDeg(f_90_);
        return i;
    }

    public void singleShot(Aim aim) {
        if (this.StayWhenFire()) this.mov.switchToStay(1.5F);
        this.gun.shots(1);
    }

    public void startFire(Aim aim) {
        if (this.StayWhenFire()) this.mov.switchToStay(1.5F);
        this.gun.shots(-1);
    }

    public void continueFire(Aim aim) {
        if (this.StayWhenFire()) this.mov.switchToStay(1.5F);
    }

    public void stopFire(Aim aim) {
        if (this.StayWhenFire()) this.mov.switchToStay(1.5F);
        this.gun.shots(0);
    }

    static long access$1110(TankGeneric tankgeneric) {
        return tankgeneric.dyingDelay--;
    }

    // TODO: |ZUTI| methods and variables
    // ------------------------------------------------------------------
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
            ZutiSupportMethods.removeFrontMarker(marker, 1);
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

    public void zutiResetFrontMarkers() {
        if (this.zutiFrontMarkers != null) this.zutiFrontMarkers.clear();
    }

    // Called from: Mission, Front
    public void zutiAddFrontMarker(Marker marker) {
        if (this.zutiFrontMarkers == null) this.zutiFrontMarkers = new ArrayList(1);

        this.zutiFrontMarkers.add(marker);
    }
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
