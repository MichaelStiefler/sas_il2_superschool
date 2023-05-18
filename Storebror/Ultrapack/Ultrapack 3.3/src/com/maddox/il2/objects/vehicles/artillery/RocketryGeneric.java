/* 4.10.1 class */
package com.maddox.il2.objects.vehicles.artillery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.StrengthProperties;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiPadObject;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class RocketryGeneric extends ActorMesh implements MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey {
    private static HashMap     rocketryMap     = new HashMap();
    RocketryProperties         prop            = null;
    MeshesNames                meshNames       = null;
    private Point3d            targetPos       = null;
    private Point3d            begPos_wagon    = new Point3d();
    private Point3d            endPos_wagon    = new Point3d();
    private Point3d            begPos_rocket   = new Point3d();
    private Point3d            endPos_rocket   = new Point3d();
    private int                launchIntervalS = 0;
    private ArrayList          rs              = new ArrayList();
    private int                nextFreeIdR;
    private float              damage          = 0.0F;
    private long               actionTimeMS    = 0L;
    private int                countRockets;
    private static RangeRandom rndSeed         = new RangeRandom();

    class Mirror extends ActorNet {
        NetMsgFiltered out = new NetMsgFiltered();

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            char c = (char) netmsginput.readByte();
            if (netmsginput.isGuaranted()) switch (c) {
                case 'a':
                case 'b':
                case 'e':
                case 'f':
                case 'l':
                case 'r':
                case 'x':
                    if (!this.isMaster()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
                        this.postTo(this.masterChannel(), netmsgguaranted);
                    }
                    return true;
                case 'A':
                case 'B':
                case 'D':
                case 'E':
                case 'F':
                case 'L':
                case 'R':
                case 'X': {
                    if (this.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
                        this.post(netmsgguaranted);
                    }
                    int i = netmsginput.readUnsignedByte();
                    int i_0_ = netmsginput.readUnsignedShort();
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    if (c != 'D') RocketryGeneric.this.handleRocketCommand_Both(c, i, i_0_, actor, false);
                    else RocketryGeneric.this.handleDamageRamp_Both(1.0F, actor, false);
                    return true;
                }
                case 'P': {
                    if (this.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        this.post(netmsgguaranted);
                    }
                    int i = netmsginput.readUnsignedShort();
                    int i_1_ = netmsginput.readUnsignedByte();
                    int i_2_ = netmsginput.readUnsignedShort();
                    RocketryGeneric.this.handlePrepareLaunchCommand_Mirror(i, i_1_, i_2_);
                    return true;
                }
                case 'S': {
                    if (this.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        this.post(netmsgguaranted);
                    }
                    int i = netmsginput.readUnsignedByte();
                    RocketryGeneric.this.handleRespawnCommand_Mirror(i);
                    return true;
                }
                case 'I': {
                    boolean bool = netmsginput.readByte() != 0;
                    int i = netmsginput.readUnsignedByte();
                    int i_3_ = netmsginput.readUnsignedByte();
                    if (i_3_ > 10) i_3_ = 10;
                    RocketInGame[] rocketingames = new RocketInGame[i_3_];
                    for (int i_4_ = 0; i_4_ < i_3_; i_4_++) {
                        rocketingames[i_4_] = new RocketInGame();
                        rocketingames[i_4_].idR = netmsginput.readUnsignedByte();
                        rocketingames[i_4_].timeAfterStartS = netmsginput.readFloat();
                        rocketingames[i_4_].randseed = netmsginput.readUnsignedShort();
                    }
                    RocketryGeneric.this.handleInitCommand_Mirror(bool, i, rocketingames);
                    return true;
                }
                default:
                    return false;
            }
            switch (c) {
                case '-':
                    if (!NetMissionTrack.isPlaying()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postRealTo(Message.currentRealTime(), RocketryGeneric.this.net.masterChannel(), this.out);
                    }
                    return true;
                case 'Y': {
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 0);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    long l = (long) (netmsginput.readFloat() * 1000.0);
                    long l_5_ = Message.currentGameTime() + l;
                    int i = netmsginput.readUnsignedByte();
                    RocketryGeneric.this.handleSyncLaunchCommand_Mirror(l_5_, i);
                    return true;
                }
                default:
                    return false;
            }
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
            char c = (char) netmsginput.readByte();
            if (netmsginput.isGuaranted()) switch (c) {
                case 'a':
                case 'b':
                case 'e':
                case 'f':
                case 'l':
                case 'r':
                case 'x': {
                    int i = netmsginput.readUnsignedByte();
                    int i_6_ = netmsginput.readUnsignedShort();
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    RocketryGeneric.this.handleRocketCommand_Both(c, i, i_6_, actor, true);
                    return true;
                }
                default:
                    return false;
            }
            if (c != '-') return false;
            float f = netmsginput.readUnsignedShort() / 65000.0F;
            com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
            Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
            RocketryGeneric.this.handleDamageRamp_Both(f, actor, true);
            return true;
        }
    }

    class Move extends Interpolate {
        public boolean tick() {
            if (Time.current() < RocketryGeneric.this.actionTimeMS) return true;
            if (RocketryGeneric.this.damage >= 1.0F) {
                if (!Mission.isDeathmatch()) return false;
                if (!RocketryGeneric.this.isNetMaster()) {
                    RocketryGeneric.this.actionTimeMS = Time.current() + 99999L;
                    return true;
                }
                RocketryGeneric.this.damage = 0.0F;
                RocketryGeneric.this.actionTimeMS = Time.current() + 1000L * RocketryGeneric.this.launchIntervalS / 2L;
                RocketryGeneric.this.setMesh(RocketryGeneric.this.meshNames.ramp);
                RocketryGeneric.this.setDiedFlag(false);
                RocketryGeneric.this.sendRespawn_Master();
                return true;
            }
            if (RocketryGeneric.this.isNetMaster()) RocketryGeneric.this.prepareLaunch_Master(RocketryGeneric.this.launchIntervalS / 2);
            else RocketryGeneric.this.actionTimeMS = Time.current() + 99999L;
            return true;
        }
    }

    public static class RocketInGame {
        public int   idR;
        public float timeAfterStartS;
        public int   randseed;
    }

    public static class TrajSeg {
        public double   t0;
        public double   t;
        public Point3d  pos0 = new Point3d();
        public Vector3d v0   = new Vector3d();
        public Vector3d a    = new Vector3d();
    }

    public static class RocketryProperties {
        public String             name                 = null;
        public MeshesNames        summerNames          = new MeshesNames();
        public MeshesNames        desertNames          = new MeshesNames();
        public MeshesNames        winterNames          = new MeshesNames();
        public String             soundName            = null;
        public boolean            air                  = false;
        public float              MASS_FULL            = 200.0F;
        public float              MASS_TNT             = 100.0F;
        public float              EXPLOSION_RADIUS     = 500.0F;
        public float              TAKEOFF_SPEED        = 1.0F;
        public float              MAX_SPEED            = 1.0F;
        public float              SPEEDUP_TIME         = 1.0F;
        public float              FLY_HEIGHT           = 1.0F;
        public float              HIT_ANGLE            = 30.0F;
        public float              MAX_ERR_HEIGHT       = 0.0F;
        public float              MAX_ERR_HIT_DISTANCE = 0.0F;
        public StrengthProperties stre                 = new StrengthProperties();
        public float              DMG_WARHEAD_MM       = 0.0F;
        public float              DMG_WARHEAD_PROB     = 0.0F;
        public float              DMG_FUEL_MM          = 0.0F;
        public float              DMG_FUEL_PROB        = 0.0F;
        public float              DMG_ENGINE_MM        = 0.0F;
        public float              DMG_ENGINE_PROB      = 0.0F;
        public float              DMG_WING_MM          = 0.0F;
        public float              DMG_WING_PROB        = 0.0F;
        public float              DMG_WARHEAD_TNT      = 0.0F;
        public float              DMG_WING_TNT         = 0.0F;
    }

    public static class MeshesNames {
        public String ramp;
        public String ramp_d;
        public String wagon;
        public String rocket;

        public void setNull() {
            this.ramp = null;
            this.ramp_d = null;
            this.wagon = null;
            this.rocket = null;
        }
    }

    private final boolean Corpse() {
        return this.damage >= 1.0F;
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public boolean isStaticPos() {
        return true;
    }

    public static final float RndF(float f, float f_7_) {
        return World.Rnd().nextFloat(f, f_7_);
    }

    public static final int RndI(int i, int i_8_) {
        return World.Rnd().nextInt(i, i_8_);
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5 + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    public void msgCollision(Actor actor, String string, String string_9_) {
        if (!this.Corpse() && Actor.isValid(actor) && (actor.net == null || !actor.net.isMirror()) && !(actor instanceof Wreckage) && actor instanceof Aircraft && !(actor.getSpeed(null) < 28.0) && (string_9_ == null
                || !string_9_.startsWith("Wing") && !string_9_.startsWith("Stab") && !string_9_.startsWith("Arone") && !string_9_.startsWith("Vator") && !string_9_.startsWith("Keel") && !string_9_.startsWith("Rudder") && !string_9_.startsWith("Pilot")))
            if (this.isNetMirror()) this.sendRampDamage_Mirror(1.0F, actor);
            else this.handleDamageRamp_Both(1.0F, actor, true);
    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if (!this.Corpse() && !(shot.power <= 0.0F) && (!this.isNetMirror() || !shot.isMirage())) {
            float f = shot.power;
            if (shot.powerType == 1) f = shot.power / 2.4E-7F;
            f *= RndF(1.0F, 1.1F);
            if (!(f < this.prop.stre.SHOT_MIN_ENERGY)) {
                float f_10_ = f / this.prop.stre.SHOT_MAX_ENERGY;
                if (this.isNetMirror()) this.sendRampDamage_Mirror(f_10_, shot.initiator);
                else this.handleDamageRamp_Both(f_10_, shot.initiator, true);
            }
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (!this.Corpse() && (!this.isNetMirror() || !explosion.isMirage())) {
            float f = explosion.power;
            int i = explosion.powerType;
            if (explosion != null) {
                /* empty */
            }
            if (i == 2 && explosion.chunkName != null) f *= 4.0F;
            float f_11_ = -1.0F;
            if (explosion.chunkName != null) {
                float f_12_ = f;
                f_12_ *= RndF(1.0F, 1.1F);
                if (f_12_ >= this.prop.stre.EXPLHIT_MIN_TNT) f_11_ = f_12_ / this.prop.stre.EXPLHIT_MAX_TNT;
            }
            if (f_11_ < 0.0F) {
                float f_13_ = explosion.receivedTNT_1meter(this);
                f_13_ *= RndF(1.0F, 1.1F);
                if (f_13_ >= this.prop.stre.EXPLNEAR_MIN_TNT) f_11_ = f_13_ / this.prop.stre.EXPLNEAR_MAX_TNT;
            }
            if (f_11_ > 0.0F) if (this.isNetMirror()) this.sendRampDamage_Mirror(f_11_, explosion.initiator);
            else this.handleDamageRamp_Both(f_11_, explosion.initiator, true);
        }
    }

    private static void getHookOffset(Mesh mesh, String string, boolean bool, Point3d point3d) {
        int i = mesh.hookFind(string);
        if (i == -1) {
            if (bool) {
                System.out.println("Rocketry: Hook [" + string + "] not found");
                throw new RuntimeException("Can't work");
            }
            point3d.set(0.0, 0.0, 0.0);
        }
        Matrix4d matrix4d = new Matrix4d();
        mesh.hookMatrix(i, matrix4d);
        point3d.set(matrix4d.m03, matrix4d.m13, matrix4d.m23);
    }

    TrajSeg[] _computeFallTrajectory_(int i, Point3d point3d, Vector3d vector3d) {
        TrajSeg[] trajsegs = new TrajSeg[1];
        for (int i_14_ = 0; i_14_ < trajsegs.length; i_14_++)
            trajsegs[i_14_] = new TrajSeg();
        trajsegs[0].pos0.set(point3d);
        trajsegs[0].v0.set(vector3d);
        trajsegs[0].a.set(0.0, 0.0, -3.0);
        trajsegs[0].t0 = 0.0;
        trajsegs[0].t = 500.0;
        return trajsegs;
    }

    TrajSeg[] _computeWagonTrajectory_(int i) {
        rndSeed.setSeed(i);
        Vector2d vector2d = new Vector2d();
        vector2d.set(this.endPos_wagon.x - this.begPos_wagon.x, this.endPos_wagon.y - this.begPos_wagon.y);
        vector2d.normalize();
        TrajSeg[] trajsegs = new TrajSeg[2];
        for (int i_15_ = 0; i_15_ < trajsegs.length; i_15_++)
            trajsegs[i_15_] = new TrajSeg();
        Vector3d vector3d = new Vector3d();
        float f = this.prop.TAKEOFF_SPEED;
        vector3d.sub(this.endPos_wagon, this.begPos_wagon);
        double d = vector3d.length();
        vector3d.scale(1.0 / d);
        trajsegs[0].v0.set(0.0, 0.0, 0.0);
        trajsegs[0].pos0.set(this.begPos_wagon);
        trajsegs[0].t = 2.0 * d / (0.0 + f);
        trajsegs[0].a.set(vector3d);
        trajsegs[0].a.scale(f / trajsegs[0].t);
        f = this.prop.TAKEOFF_SPEED * rndSeed.nextFloat(0.85F, 0.97F);
        double d_16_ = vector3d.z * d;
        double d_17_ = Math.sqrt(d * d - d_16_ * d_16_);
        trajsegs[1].v0.set(d_17_, 0.0, d_16_);
        trajsegs[1].v0.normalize();
        trajsegs[1].v0.scale(f);
        trajsegs[1].pos0.set(0.0, 0.0, this.endPos_wagon.z);
        trajsegs[1].t = 30.0 + 2.0 * (trajsegs[1].v0.z / 9.8);
        trajsegs[1].a.set(0.0, 0.0, -9.8);
        for (int i_18_ = 0; i_18_ <= 1; i_18_++)
            if (trajsegs[i_18_].t <= 0.0) return null;
        trajsegs[0].t0 = 0.0;
        for (int i_19_ = 1; i_19_ <= 1; i_19_++)
            trajsegs[i_19_].t0 = trajsegs[i_19_ - 1].t0 + trajsegs[i_19_ - 1].t;
        for (int i_20_ = 1; i_20_ <= 1; i_20_++) {
            trajsegs[i_20_].pos0.set(vector2d.x * trajsegs[i_20_].pos0.x, vector2d.y * trajsegs[i_20_].pos0.x, trajsegs[i_20_].pos0.z);
            trajsegs[i_20_].pos0.add(this.endPos_wagon.x, this.endPos_wagon.y, 0.0);
            trajsegs[i_20_].v0.set(vector2d.x * trajsegs[i_20_].v0.x, vector2d.y * trajsegs[i_20_].v0.x, trajsegs[i_20_].v0.z);
            trajsegs[i_20_].a.set(vector2d.x * trajsegs[i_20_].a.x, vector2d.y * trajsegs[i_20_].a.x, trajsegs[i_20_].a.z);
        }
        return trajsegs;
    }

    private TrajSeg[] _computeAirTrajectory_(int i) {
        rndSeed.setSeed(i);
        Point3d point3d = new Point3d();
        point3d.set(this.targetPos);
        float f = rndSeed.nextFloat(0.0F, 359.99F);
        float f_21_ = rndSeed.nextFloat(0.0F, this.prop.MAX_ERR_HIT_DISTANCE);
        point3d.add(Geom.cosDeg(f) * f_21_, Geom.sinDeg(f) * f_21_, 0.0);
        double d = this.prop.FLY_HEIGHT + rndSeed.nextFloat(-this.prop.MAX_ERR_HEIGHT, this.prop.MAX_ERR_HEIGHT);
        Point3d point3d_22_ = new Point3d();
        point3d_22_.set(this.pos.getAbsPoint());
        point3d_22_.z = d;
        Point2d point2d = new Point2d(point3d.x, point3d.y);
        Point2d point2d_23_ = new Point2d(point3d_22_.x, point3d_22_.y);
        double d_24_ = point2d.distance(point2d_23_);
        Vector2d vector2d = new Vector2d();
        vector2d.set(point3d.x - point3d_22_.x, point3d.y - point3d_22_.y);
        vector2d.normalize();
        float f_25_ = this.prop.MAX_SPEED;
        TrajSeg[] trajsegs = new TrajSeg[3];
        for (int i_26_ = 0; i_26_ < trajsegs.length; i_26_++)
            trajsegs[i_26_] = new TrajSeg();
        trajsegs[0].v0.set(f_25_, 0.0, 0.0);
        trajsegs[1].v0.set(f_25_, 0.0, 0.0);
        trajsegs[2].v0.set(f_25_ * Geom.cosDeg(this.prop.HIT_ANGLE), 0.0, -f_25_ * Geom.sinDeg(this.prop.HIT_ANGLE));
        trajsegs[0].pos0.set(0.0, 0.0, d);
        trajsegs[2].pos0.set(d_24_, 0.0, point3d.z);
        trajsegs[1].t = 2.0 * (trajsegs[2].pos0.z - d) / (trajsegs[1].v0.z + trajsegs[2].v0.z);
        trajsegs[1].pos0.set(trajsegs[2].pos0.x - 0.5 * (trajsegs[1].v0.x + trajsegs[2].v0.x) * trajsegs[1].t, 0.0, d);
        trajsegs[2].t = 100.0;
        trajsegs[0].t = 2.0 * (trajsegs[1].pos0.x - trajsegs[0].pos0.x) / (trajsegs[0].v0.x + trajsegs[1].v0.x);
        for (int i_27_ = 0; i_27_ <= 2; i_27_++)
            if (trajsegs[i_27_].t <= 0.0) return null;
        trajsegs[0].a.set(0.0, 0.0, 0.0);
        for (int i_28_ = 1; i_28_ <= 1; i_28_++) {
            trajsegs[i_28_].a.sub(trajsegs[i_28_ + 1].v0, trajsegs[i_28_].v0);
            trajsegs[i_28_].a.scale(1.0 / trajsegs[i_28_].t);
        }
        trajsegs[2].a.set(0.0, 0.0, 0.0);
        trajsegs[0].t0 = 0.0;
        for (int i_29_ = 1; i_29_ <= 2; i_29_++)
            trajsegs[i_29_].t0 = trajsegs[i_29_ - 1].t0 + trajsegs[i_29_ - 1].t;
        for (int i_30_ = 0; i_30_ <= 2; i_30_++) {
            trajsegs[i_30_].pos0.set(vector2d.x * trajsegs[i_30_].pos0.x, vector2d.y * trajsegs[i_30_].pos0.x, trajsegs[i_30_].pos0.z);
            trajsegs[i_30_].pos0.add(point3d_22_.x, point3d_22_.y, 0.0);
            trajsegs[i_30_].v0.set(vector2d.x * trajsegs[i_30_].v0.x, vector2d.y * trajsegs[i_30_].v0.x, trajsegs[i_30_].v0.z);
            trajsegs[i_30_].a.set(vector2d.x * trajsegs[i_30_].a.x, vector2d.y * trajsegs[i_30_].a.x, trajsegs[i_30_].a.z);
        }
        return trajsegs;
    }

    private TrajSeg[] _computeRampTrajectory_(int i, boolean bool) {
        rndSeed.setSeed(i);
        Point3d point3d = new Point3d();
        point3d.set(this.targetPos);
        float f = rndSeed.nextFloat(0.0F, 359.99F);
        float f_31_ = rndSeed.nextFloat(0.0F, this.prop.MAX_ERR_HIT_DISTANCE);
        point3d.add(Geom.cosDeg(f) * f_31_, Geom.sinDeg(f) * f_31_, 0.0);
        Point2d point2d = new Point2d(point3d.x, point3d.y);
        Point2d point2d_32_ = new Point2d(this.endPos_rocket.x, this.endPos_rocket.y);
        double d = point2d.distance(point2d_32_);
        Vector2d vector2d = new Vector2d();
        vector2d.set(point3d.x - this.endPos_rocket.x, point3d.y - this.endPos_rocket.y);
        vector2d.normalize();
        float f_33_ = bool ? 0.5F : 1.0F;
        double d_34_ = f_33_ * this.prop.FLY_HEIGHT + f_33_ * rndSeed.nextFloat(-this.prop.MAX_ERR_HEIGHT, this.prop.MAX_ERR_HEIGHT);
        float f_35_ = bool ? this.prop.TAKEOFF_SPEED + 1.0F : this.prop.MAX_SPEED;
        TrajSeg[] trajsegs = new TrajSeg[6];
        for (int i_36_ = 0; i_36_ < trajsegs.length; i_36_++)
            trajsegs[i_36_] = new TrajSeg();
        Vector3d vector3d = new Vector3d();
        vector3d.sub(this.endPos_rocket, this.begPos_rocket);
        double d_37_ = vector3d.length();
        vector3d.scale(1.0 / d_37_);
        trajsegs[0].v0.set(0.0, 0.0, 0.0);
        trajsegs[0].pos0.set(this.begPos_rocket);
        trajsegs[0].t = 2.0 * d_37_ / (0.0 + this.prop.TAKEOFF_SPEED);
        trajsegs[0].a.set(vector3d);
        trajsegs[0].a.scale(this.prop.TAKEOFF_SPEED / trajsegs[0].t);
        double d_38_ = vector3d.z * d_37_;
        double d_39_ = Math.sqrt(d_37_ * d_37_ - d_38_ * d_38_);
        trajsegs[1].v0.set(d_39_, 0.0, d_38_);
        trajsegs[1].v0.normalize();
        trajsegs[1].v0.scale(this.prop.TAKEOFF_SPEED);
        trajsegs[2].v0.set(this.prop.TAKEOFF_SPEED, 0.0, 0.0);
        trajsegs[3].v0.set(f_35_, 0.0, 0.0);
        trajsegs[4].v0.set(f_35_, 0.0, 0.0);
        trajsegs[5].v0.set(f_35_ * Geom.cosDeg(this.prop.HIT_ANGLE), 0.0, -f_35_ * Geom.sinDeg(this.prop.HIT_ANGLE));
        trajsegs[1].pos0.set(0.0, 0.0, this.endPos_rocket.z);
        trajsegs[1].t = 2.0 * (d_34_ - trajsegs[1].pos0.z) / (trajsegs[1].v0.z + 0.0);
        trajsegs[2].pos0.set(trajsegs[1].pos0.x + 0.5 * (trajsegs[1].v0.x + trajsegs[2].v0.x) * trajsegs[1].t, 0.0, d_34_);
        trajsegs[2].t = bool ? 0.5 : (double) this.prop.SPEEDUP_TIME;
        trajsegs[3].pos0.set(trajsegs[2].pos0.x + 0.5 * (trajsegs[2].v0.x + trajsegs[3].v0.x) * trajsegs[2].t, 0.0, d_34_);
        trajsegs[5].pos0.set(d, 0.0, point3d.z);
        trajsegs[4].t = 2.0 * (trajsegs[5].pos0.z - d_34_) / (trajsegs[4].v0.z + trajsegs[5].v0.z);
        trajsegs[4].pos0.set(trajsegs[5].pos0.x - 0.5 * (trajsegs[4].v0.x + trajsegs[5].v0.x) * trajsegs[4].t, 0.0, d_34_);
        trajsegs[5].t = 100.0;
        trajsegs[3].t = 2.0 * (trajsegs[4].pos0.x - trajsegs[3].pos0.x) / (trajsegs[3].v0.x + trajsegs[4].v0.x);
        for (int i_40_ = 0; i_40_ <= 5; i_40_++)
            if (trajsegs[i_40_].t <= 0.0) return null;
        for (int i_41_ = 1; i_41_ <= 4; i_41_++) {
            trajsegs[i_41_].a.sub(trajsegs[i_41_ + 1].v0, trajsegs[i_41_].v0);
            trajsegs[i_41_].a.scale(1.0 / trajsegs[i_41_].t);
        }
        trajsegs[5].a.set(0.0, 0.0, 0.0);
        trajsegs[0].t0 = 0.0;
        for (int i_42_ = 1; i_42_ <= 5; i_42_++)
            trajsegs[i_42_].t0 = trajsegs[i_42_ - 1].t0 + trajsegs[i_42_ - 1].t;
        for (int i_43_ = 1; i_43_ <= 5; i_43_++) {
            trajsegs[i_43_].pos0.set(vector2d.x * trajsegs[i_43_].pos0.x, vector2d.y * trajsegs[i_43_].pos0.x, trajsegs[i_43_].pos0.z);
            trajsegs[i_43_].pos0.add(this.endPos_rocket.x, this.endPos_rocket.y, 0.0);
            trajsegs[i_43_].v0.set(vector2d.x * trajsegs[i_43_].v0.x, vector2d.y * trajsegs[i_43_].v0.x, trajsegs[i_43_].v0.z);
            trajsegs[i_43_].a.set(vector2d.x * trajsegs[i_43_].a.x, vector2d.y * trajsegs[i_43_].a.x, trajsegs[i_43_].a.z);
        }
        return trajsegs;
    }

    private TrajSeg[] computeNormalTrajectory(int i) {
        TrajSeg[] trajsegs;
        if (this.prop.air) trajsegs = this._computeAirTrajectory_(i);
        else {
            trajsegs = this._computeRampTrajectory_(i, false);
            if (trajsegs == null) trajsegs = this._computeRampTrajectory_(i, true);
        }
        return trajsegs;
    }

    private RocketryGeneric(RocketryProperties rocketryproperties, MeshesNames meshesnames, String string, int i, NetChannel netchannel, int i_44_, double d, double d_45_, float f, Point2d point2d, float f_46_, float f_47_, int i_48_) {
        super(meshesnames.ramp);
        this.prop = rocketryproperties;
        this.meshNames = meshesnames;
        this.countRockets = i_48_;
        if (this.countRockets == 0 || point2d == null) {
            this.countRockets = 0;
            this.targetPos = null;
        } else {
            this.targetPos = new Point3d();
            this.targetPos.set(point2d.x, point2d.y, Engine.land().HQ(point2d.x, point2d.y));
        }
        this.setName(string);
        this.setArmy(i);
        Point3d point3d = new Point3d();
        if (this.prop.air) point3d.set(d, d_45_, this.prop.FLY_HEIGHT);
        else {
            point3d.set(d, d_45_, Engine.land().HQ(d, d_45_));
            Point3d point3d_49_ = new Point3d();
            getHookOffset(this.mesh(), "Ground_Level", false, point3d_49_);
            point3d.z -= point3d_49_.z;
        }
        Orient orient = new Orient();
        if (this.targetPos == null) orient.set(f, 0.0F, 0.0F);
        else {
            Vector3d vector3d = new Vector3d();
            vector3d.sub(this.targetPos, point3d);
            vector3d.z = 0.0;
            orient.setAT0(vector3d);
        }
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        if (this.prop.air) {
            this.collide(false);
            this.drawing(false);
        } else {
            this.collide(true);
            this.drawing(true);
        }
        if (this.prop.air) {
            this.begPos_wagon = null;
            this.endPos_wagon = null;
            this.begPos_rocket = null;
            this.endPos_rocket = null;
        } else {
            getHookOffset(this.mesh(), "_begWagon", true, this.begPos_wagon);
            this.pos.getAbs().transform(this.begPos_wagon);
            getHookOffset(this.mesh(), "_endWagon", true, this.endPos_wagon);
            this.pos.getAbs().transform(this.endPos_wagon);
            getHookOffset(this.mesh(), "_begRocket", true, this.begPos_rocket);
            this.pos.getAbs().transform(this.begPos_rocket);
            getHookOffset(this.mesh(), "_endRocket", true, this.endPos_rocket);
            this.pos.getAbs().transform(this.endPos_rocket);
        }
        int i_50_ = (int) (f_46_ * 60.0F + 0.5F);
        if (i_50_ < 0) i_50_ = 0;
        if (i_50_ > 14400) i_50_ = 14400;
        this.launchIntervalS = (int) (f_47_ * 60.0F + 0.5F);
        if (this.launchIntervalS < 180) this.launchIntervalS = 180;
        if (this.launchIntervalS > 14400) this.launchIntervalS = 14400;
        this.damage = 0.0F;
        this.actionTimeMS = 9223372036854775807L;
        this.createNetObject(netchannel, i_44_);
        if (this.targetPos != null) if (this.isNetMaster()) {
            long l = 1000L * i_50_ - 1000L * this.launchIntervalS / 2L;
            if (l <= 0L) this.prepareLaunch_Master(i_50_);
            else this.actionTimeMS = Time.current() + l;
        } else this.actionTimeMS = 9223372036854775807L;
        if (!this.interpEnd("move")) this.interpPut(new Move(), "move", Time.current(), null);
    }

    public int HitbyMask() {
        return -2;
    }

    public int chooseBulletType(BulletProperties[] bulletpropertieses) {
        if (this.Corpse()) return -1;
        if (bulletpropertieses.length == 1) return 0;
        if (bulletpropertieses.length <= 0) return -1;
        if (bulletpropertieses[0].power <= 0.0F) return 0;
        if (bulletpropertieses[1].power <= 0.0F) return 1;
        if (bulletpropertieses[0].cumulativePower > 0.0F) return 0;
        if (bulletpropertieses[1].cumulativePower > 0.0F) return 1;
        if (bulletpropertieses[0].powerType == 1) return 0;
        if (bulletpropertieses[1].powerType == 1) return 1;
        if (bulletpropertieses[0].powerType == 0) return 1;
        return 0;
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        if (this.Corpse()) return -1;
        return 0;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.Corpse()) return false;
        if (i != 0) return false;
        if (point3d != null) point3d.set(0.0, 0.0, 0.0);
        return true;
    }

    private RocketryRocket findMyRocket(int i) {
        for (int i_51_ = 0; i_51_ < this.rs.size(); i_51_++)
            if (((RocketryRocket) this.rs.get(i_51_)).idR == i) return (RocketryRocket) this.rs.get(i_51_);
        return null;
    }

    void forgetRocket(RocketryRocket rocketryrocket) {
        for (int i = 0; i < this.rs.size(); i++)
            if ((RocketryRocket) this.rs.get(i) == rocketryrocket) {
                this.rs.remove(i);
                break;
            }
    }

    private void killWrongRockets(int i) {
        for (int i_52_ = 0; i_52_ < this.rs.size(); i_52_++) {
            int i_53_ = ((RocketryRocket) this.rs.get(i_52_)).idR;
            byte i_54_ = (byte) (i_53_ - i);
            if (i_54_ >= 0 && i_54_ <= 20) {
                ((RocketryRocket) this.rs.get(i_52_)).silentDeath();
                this.rs.remove(i_52_);
                i_52_--;
            }
        }
    }

    private void sendRampDamage_Mirror(float f, Actor actor) {
        if (this.isNetMirror() && !(this.net.masterChannel() instanceof NetChannelInStream)) {
            int i = (int) (f * 65000.0F);
            if (i > 0) {
                if (i > 65000) i = 65000;
                try {
                    NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
                    netmsgfiltered.writeByte(45);
                    netmsgfiltered.writeShort(i);
                    netmsgfiltered.writeNetObj(actor == null ? (ActorNet) null : actor.net);
                    netmsgfiltered.setIncludeTime(false);
                    this.net.postTo(Time.current(), this.net.masterChannel(), netmsgfiltered);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
    }

    private void sendRespawn_Master() {
        if (this.net.isMirrored()) {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try {
                netmsgguaranted.writeByte(83);
                netmsgguaranted.writeByte(this.nextFreeIdR);
                this.net.post(netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void prepareLaunch_Master(int i) {
        if (this.countRockets <= 0) {
            this.countRockets = 0;
            this.actionTimeMS = 9223372036854775807L;
        } else {
            long l = Time.current();
            int i_55_ = RndI(0, 65535);
            int i_56_ = this.nextFreeIdR;
            this.nextFreeIdR = this.nextFreeIdR + 1 & 0xff;
            long l_57_ = l + 1000L * i;
            this.actionTimeMS = l_57_ + 1000L * this.launchIntervalS / 2L;
            this.killWrongRockets(i_56_);
            TrajSeg[] trajsegs = this.computeNormalTrajectory(i_55_);
            if (trajsegs != null) {
                RocketryRocket rocketryrocket = new RocketryRocket(this, this.meshNames.rocket, i_56_, i_55_, l_57_, l, trajsegs);
                if (rocketryrocket.isDamaged()) rocketryrocket.silentDeath();
                else {
                    // TODO: Edited by |ZUTI|: this is called if we are master/server
                    // ------------------------------------------------------------------------
                    if (Mission.MDS_VARIABLES().zutiIcons_ShowRockets) {
                        ZutiPadObject zo = new ZutiPadObject(rocketryrocket, Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0);
                        zo.type = 3;

                        GUI.pad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
                    }
                    // ------------------------------------------------------------------------

                    this.rs.add(rocketryrocket);
                    if (this.countRockets < 1000) this.countRockets--;
                    if (this.net.isMirrored()) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        try {
                            netmsgguaranted.writeByte(80);
                            netmsgguaranted.writeShort(i);
                            netmsgguaranted.writeByte(i_56_);
                            netmsgguaranted.writeShort(i_55_);
                            this.net.post(netmsgguaranted);
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void handleInitCommand_Mirror(boolean bool, int i, RocketInGame[] rocketingames) {
        for (int i_58_ = 0; i_58_ < this.rs.size(); i_58_++)
            ((RocketryRocket) this.rs.get(i_58_)).silentDeath();
        this.rs.clear();
        this.damage = bool ? 0.0F : 1.0F;
        this.setMesh(bool ? this.meshNames.ramp : this.meshNames.ramp_d);
        if (this.targetPos != null) {
            long l = Time.current();
            for (int i_59_ = 0; i_59_ < rocketingames.length; i_59_++) {
                TrajSeg[] trajsegs = this.computeNormalTrajectory(rocketingames[i_59_].randseed);
                if (trajsegs != null) {
                    RocketryRocket rocketryrocket = new RocketryRocket(this, this.meshNames.rocket, rocketingames[i_59_].idR, rocketingames[i_59_].randseed, l - (long) (1000.0 * rocketingames[i_59_].timeAfterStartS), l, trajsegs);

                    // TODO: Implement 4.10.1 Codechanges +++

                    if (rocketryrocket.isDamaged()) {
                        rocketryrocket.silentDeath();
                        continue;
                    } else {
                        // TODO: Edited by |ZUTI|: this is called if we are client
                        // ------------------------------------------------------------------------
                        if (GUI.pad != null && Mission.MDS_VARIABLES().zutiIcons_ShowRockets) {
                            ZutiPadObject zo = new ZutiPadObject(rocketryrocket, Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0);
                            zo.type = 3;

                            GUI.pad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
                        }
                        // ------------------------------------------------------------------------

                        this.rs.add(rocketryrocket);
                    }

//                    if (rocketryrocket.isDamaged())
//                        rocketryrocket.silentDeath();
//                    else {
//                        // TODO: Edited by |ZUTI|: this is called if we are client
//                        // ------------------------------------------------------------------------
//                        if (Mission.MDS_VARIABLES().zutiIcons_ShowRockets) {
//                            ZutiPadObject zo = new ZutiPadObject(rocketryrocket, Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0);
//                            zo.type = 3;
//
//                            GUI.pad.zutiPadObjects.put(new Integer(zo.hashCode()), zo);
//                        }
//                        // ------------------------------------------------------------------------
//
//                        rs.add(rocketryrocket);
//                    }
                    // TODO: Implement 4.10.1 Codechanges ---
                }
            }
        }
    }

    private void handleRespawnCommand_Mirror(int i) {
        this.killWrongRockets(i);
        this.actionTimeMS = Time.current() + 99999L;
        if (this.damage >= 1.0F) {
            this.damage = 0.0F;
            this.setMesh(this.meshNames.ramp);
            this.setDiedFlag(false);
        } else this.damage = 0.0F;
    }

    private void handlePrepareLaunchCommand_Mirror(int i, int i_60_, int i_61_) {
        this.killWrongRockets(i_60_);
        if (this.targetPos != null) {
            long l = Time.current();
            TrajSeg[] trajsegs = this.computeNormalTrajectory(i_61_);
            if (trajsegs != null) {
                RocketryRocket rocketryrocket = new RocketryRocket(this, this.meshNames.rocket, i_60_, i_61_, l + i * 1000, l, trajsegs);
                if (rocketryrocket.isDamaged()) rocketryrocket.silentDeath();
                else this.rs.add(rocketryrocket);
            }
        }
    }

    private void handleSyncLaunchCommand_Mirror(long l, int i) {
        this.killWrongRockets(i + 1 & 0xff);
        if (this.targetPos != null) {
            RocketryRocket rocketryrocket = this.findMyRocket(i);
            if (rocketryrocket != null) rocketryrocket.changeLaunchTimeIfCan(l);
        }
    }

    public void sendRocketStateChange(RocketryRocket rocketryrocket, char c, Actor actor) {
        boolean bool = this.isNetMaster();
        int i = RndI(0, 65535);
        if (bool) this.handleRocketCommand_Both(c, rocketryrocket.idR, i, actor, true);
        else if (this.isNetMirror() && !(this.net.masterChannel() instanceof NetChannelInStream)) {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            try {
                netmsgguaranted.writeByte(Character.toLowerCase(c));
                netmsgguaranted.writeByte(rocketryrocket.idR);
                netmsgguaranted.writeShort(i);
                netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
                this.net.postTo(this.net.masterChannel(), netmsgguaranted);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void handleRocketCommand_Both(char c, int i, int i_62_, Actor actor, boolean bool) {
        if (this.targetPos != null) {
            RocketryRocket rocketryrocket = this.findMyRocket(i);
            if (rocketryrocket != null) {
                c = rocketryrocket.handleCommand(c, i_62_, actor);
                if (c != 0 && bool && this.net.isMirrored()) {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    try {
                        netmsgguaranted.writeByte(Character.toUpperCase(c));
                        netmsgguaranted.writeByte(i);
                        netmsgguaranted.writeShort(i_62_);
                        netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
                        this.net.post(netmsgguaranted);
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleDamageRamp_Both(float f, Actor actor, boolean bool) {
        if (!this.prop.air && !(f <= 0.0F) && !(this.damage >= 1.0F)) {
            this.damage += f;
            if (this.damage >= 1.0F) this.damage = 1.0F;
            else return;
            World.onActorDied(this, actor);
            this.setMesh(this.meshNames.ramp_d);
            this.actionTimeMS = Time.current();
            if (Mission.isDeathmatch()) this.actionTimeMS += SecsToTicks(Mission.respawnTime("Artillery") * RndF(1.0F, 1.2F));
            for (int i = 0; i < this.rs.size(); i++) {
                RocketryRocket rocketryrocket = (RocketryRocket) this.rs.get(i);
                int i_63_ = rocketryrocket.idR;
                if (rocketryrocket.isOnRamp()) {
                    this.handleRocketCommand_Both('X', i_63_, RndI(0, 65535), actor, bool);
                    i = 0;
                }
            }
            Explosions.ExplodeBridge(this.begPos_wagon, this.endPos_wagon, 1.2F);
            if (bool && this.net.isMirrored()) {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                try {
                    netmsgguaranted.writeByte(68);
                    netmsgguaranted.writeByte(this.nextFreeIdR);
                    netmsgguaranted.writeShort(RndI(0, 65535));
                    netmsgguaranted.writeNetObj(actor == null ? (ActorNet) null : actor.net);
                    this.net.post(netmsgguaranted);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
    }

    private void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) this.net = new Master(this);
        else this.net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        netmsgguaranted.writeByte(this.damage >= 1.0F ? 0 : 1);
        netmsgguaranted.writeByte(this.nextFreeIdR);
        int i = 0;
        for (int i_64_ = 0; i_64_ < this.rs.size(); i_64_++) {
            RocketryRocket rocketryrocket = (RocketryRocket) this.rs.get(i_64_);
            if (!rocketryrocket.isDamaged()) i++;
        }
        if (i > 10) i = 10;
        netmsgguaranted.writeByte(i);
        for (int i_65_ = this.rs.size() - 1; i_65_ >= 0; i_65_--) {
            RocketryRocket rocketryrocket = (RocketryRocket) this.rs.get(i_65_);
            if (!rocketryrocket.isDamaged()) {
                netmsgguaranted.writeByte(rocketryrocket.idR);
                float f = (float) ((Time.current() - rocketryrocket.timeOfStartMS) * 0.0010);
                netmsgguaranted.writeFloat(f);
                netmsgguaranted.writeShort(rocketryrocket.randseed);
                if (--i <= 0) break;
            }
        }
        this.net.postTo(netchannel, netmsgguaranted);
    }

    public static RocketryGeneric New(String string, String string_66_, NetChannel netchannel, int i, int i_67_, double d, double d_68_, float f, float f_69_, int i_70_, float f_71_, Point2d point2d) {
        RocketryProperties rocketryproperties = (RocketryProperties) rocketryMap.get(string_66_);
        if (rocketryproperties == null) {
            System.out.println("***** Rocketry: Unknown type [" + string_66_ + "]");
            return null;
        }
        MeshesNames meshesnames;
        switch (World.cur().camouflage) {
            case 1:
                meshesnames = rocketryproperties.winterNames;
                break;
            case 2:
                meshesnames = rocketryproperties.desertNames;
                break;
            default:
                meshesnames = rocketryproperties.summerNames;
        }
        return new RocketryGeneric(rocketryproperties, meshesnames, string, i_67_, netchannel, i, d, d_68_, f, point2d, f_69_, f_71_, i_70_);
    }

    public static final float KmHourToMSec(float f) {
        return f / 3.6F;
    }

    private static float getF(SectFile sectfile, String string, String string_72_, float f, float f_73_) {
        float f_74_ = sectfile.get(string, string_72_, -9865.345F);
        if (f_74_ == -9865.345F || f_74_ < f || f_74_ > f_73_) {
            if (f_74_ == -9865.345F) System.out.println("Rocketry: Parameter [" + string + "]:<" + string_72_ + "> " + "not found");
            else System.out.println("Rocketry: Value of [" + string + "]:<" + string_72_ + "> (" + f_74_ + ")" + " is out of range (" + f + ";" + f_73_ + ")");
            throw new RuntimeException("Can't set property");
        }
        return f_74_;
    }

    private static String getS(SectFile sectfile, String string, String string_75_) {
        String string_76_ = sectfile.get(string, string_75_);
        if (string_76_ == null || string_76_.length() <= 0) {
            System.out.print("Rocketry: Parameter [" + string + "]:<" + string_75_ + "> ");
            System.out.println(string_76_ == null ? "not found" : "is empty");
            throw new RuntimeException("Can't set property");
        }
        return string_76_;
    }

    private static String getS(SectFile sectfile, String string, String string_77_, String string_78_) {
        String string_79_ = sectfile.get(string, string_77_);
        if (string_79_ == null || string_79_.length() <= 0) return string_78_;
        return string_79_;
    }

    public static void PreLoad(String string) {
        Property.set(RocketryGeneric.class, "iconName", "icons/objV1.mat");
        rocketryMap = new HashMap();
        SectFile sectfile = new SectFile(string, 0);
        int i = sectfile.sections();
        for (int i_80_ = 0; i_80_ < i; i_80_++) {
            RocketryProperties rocketryproperties = new RocketryProperties();
            rocketryproperties.name = sectfile.sectionName(i_80_);
            rocketryproperties.summerNames.setNull();
            rocketryproperties.desertNames.setNull();
            rocketryproperties.winterNames.setNull();
            rocketryproperties.summerNames.ramp = getS(sectfile, rocketryproperties.name, "MeshSummer_ramp", "");
            if (rocketryproperties.summerNames.ramp == "") {
                rocketryproperties.air = true;
                rocketryproperties.summerNames.ramp = "3do/primitive/coord/mono.sim";
                rocketryproperties.summerNames.ramp_d = rocketryproperties.summerNames.ramp;
                rocketryproperties.desertNames = rocketryproperties.summerNames;
                rocketryproperties.winterNames = rocketryproperties.summerNames;
            } else {
                rocketryproperties.air = false;
                rocketryproperties.desertNames.ramp = getS(sectfile, rocketryproperties.name, "MeshDesert_ramp", rocketryproperties.summerNames.ramp);
                rocketryproperties.winterNames.ramp = getS(sectfile, rocketryproperties.name, "MeshWinter_ramp", rocketryproperties.summerNames.ramp);
                rocketryproperties.summerNames.ramp_d = getS(sectfile, rocketryproperties.name, "MeshSummerDamage_ramp");
                rocketryproperties.desertNames.ramp_d = getS(sectfile, rocketryproperties.name, "MeshDesertDamage_ramp", rocketryproperties.summerNames.ramp_d);
                rocketryproperties.winterNames.ramp_d = getS(sectfile, rocketryproperties.name, "MeshWinterDamage_ramp", rocketryproperties.summerNames.ramp_d);
                rocketryproperties.summerNames.wagon = getS(sectfile, rocketryproperties.name, "MeshSummer_wagon");
                rocketryproperties.desertNames.wagon = getS(sectfile, rocketryproperties.name, "MeshDesert_wagon", rocketryproperties.summerNames.wagon);
                rocketryproperties.winterNames.wagon = getS(sectfile, rocketryproperties.name, "MeshWinter_wagon", rocketryproperties.summerNames.wagon);
            }
            rocketryproperties.summerNames.rocket = getS(sectfile, rocketryproperties.name, "MeshSummer_rocket");
            rocketryproperties.desertNames.rocket = getS(sectfile, rocketryproperties.name, "MeshDesert_rocket", rocketryproperties.summerNames.rocket);
            rocketryproperties.winterNames.rocket = getS(sectfile, rocketryproperties.name, "MeshWinter_rocket", rocketryproperties.summerNames.rocket);
            rocketryproperties.soundName = getS(sectfile, rocketryproperties.name, "SoundMove");
            rocketryproperties.MASS_FULL = getF(sectfile, rocketryproperties.name, "MassFull", 0.5F, 100000.0F);
            rocketryproperties.MASS_TNT = getF(sectfile, rocketryproperties.name, "MassTNT", 0.0F, 1.0E7F);
            rocketryproperties.EXPLOSION_RADIUS = getF(sectfile, rocketryproperties.name, "ExplosionRadius", 0.01F, 100000.0F);
            if (!rocketryproperties.air) {
                rocketryproperties.TAKEOFF_SPEED = getF(sectfile, rocketryproperties.name, "TakeoffSpeed", 1.0F, 3000.0F);
                rocketryproperties.TAKEOFF_SPEED = KmHourToMSec(rocketryproperties.TAKEOFF_SPEED);
            }
            rocketryproperties.MAX_SPEED = getF(sectfile, rocketryproperties.name, "MaxSpeed", rocketryproperties.TAKEOFF_SPEED, 3000.0F);
            rocketryproperties.MAX_SPEED = KmHourToMSec(rocketryproperties.MAX_SPEED);
            if (!rocketryproperties.air) rocketryproperties.SPEEDUP_TIME = getF(sectfile, rocketryproperties.name, "SpeedupTime", 1.0F, 10000.0F);
            rocketryproperties.FLY_HEIGHT = getF(sectfile, rocketryproperties.name, "FlyHeight", 100.0F, 15000.0F);
            rocketryproperties.HIT_ANGLE = getF(sectfile, rocketryproperties.name, "HitAngle", 5.0F, 89.0F);
            rocketryproperties.MAX_ERR_HEIGHT = getF(sectfile, rocketryproperties.name, "MaxErrHeight", 0.0F, 2000.0F);
            rocketryproperties.MAX_ERR_HIT_DISTANCE = getF(sectfile, rocketryproperties.name, "MaxErrHitDistance", 0.0F, 10000.0F);
            if (!rocketryproperties.air && !rocketryproperties.stre.read("Rocketry", sectfile, null, rocketryproperties.name)) throw new RuntimeException("Can't register Rocketry data");
            rocketryproperties.DMG_WARHEAD_MM = getF(sectfile, rocketryproperties.name, "DmgWarhead_mm", 0.0F, 2000.0F);
            rocketryproperties.DMG_WARHEAD_PROB = getF(sectfile, rocketryproperties.name, "DmgWarhead_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_FUEL_MM = getF(sectfile, rocketryproperties.name, "DmgFuel_mm", 0.0F, 2000.0F);
            rocketryproperties.DMG_FUEL_PROB = getF(sectfile, rocketryproperties.name, "DmgFuel_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_ENGINE_MM = getF(sectfile, rocketryproperties.name, "DmgEngine_mm", 0.0F, 2000.0F);
            rocketryproperties.DMG_ENGINE_PROB = getF(sectfile, rocketryproperties.name, "DmgEngine_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_WING_MM = getF(sectfile, rocketryproperties.name, "DmgWing_mm", 0.0F, 2000.0F);
            rocketryproperties.DMG_WING_PROB = getF(sectfile, rocketryproperties.name, "DmgWing_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_WARHEAD_TNT = getF(sectfile, rocketryproperties.name, "DmgWarhead_tnt", 0.0F, 1000000.0F);
            rocketryproperties.DMG_WING_TNT = getF(sectfile, rocketryproperties.name, "DmgWing_tnt", 0.0F, 1000000.0F);
            rocketryMap.put(rocketryproperties.name, rocketryproperties);
        }
    }

}
