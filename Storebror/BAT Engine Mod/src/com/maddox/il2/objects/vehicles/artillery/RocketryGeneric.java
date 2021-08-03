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
import com.maddox.il2.game.Main;
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
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class RocketryGeneric extends ActorMesh implements MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey {
    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            char c = (char) netmsginput.readByte();
            if (netmsginput.isGuaranted()) {
                switch (c) {
                    case 97:
                    case 98:
                    case 101:
                    case 102:
                    case 108:
                    case 114:
                    case 120:
                        int i = netmsginput.readUnsignedByte();
                        int j = netmsginput.readUnsignedShort();
                        NetObj netobj1 = netmsginput.readNetObj();
                        Actor actor1 = netobj1 == null ? null : ((ActorNet) netobj1).actor();
                        RocketryGeneric.this.handleRocketCommand_Both(c, i, j, actor1, true);
                        return true;

                    case 99:
                    case 100:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 115:
                    case 116:
                    case 117:
                    case 118:
                    case 119:
                    default:
                        return false;
                }
            }
            if (c != '-') {
                return false;
            } else {
                float f = netmsginput.readUnsignedShort() / 65000F;
                NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                RocketryGeneric.this.handleDamageRamp_Both(f, actor, true);
                return true;
            }
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    public static class MeshesNames {

        public void setNull() {
            this.ramp = null;
            this.ramp_d = null;
            this.wagon = null;
            this.rocket = null;
        }

        public String ramp;
        public String ramp_d;
        public String wagon;
        public String rocket;

        public MeshesNames() {
        }
    }

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            char c = (char) netmsginput.readByte();
            if (netmsginput.isGuaranted()) {
                switch (c) {
                    case 97:
                    case 98:
                    case 101:
                    case 102:
                    case 108:
                    case 114:
                    case 120:
                        if (!this.isMaster()) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
                            this.postTo(this.masterChannel(), netmsgguaranted);
                        }
                        return true;

                    case 65:
                    case 66:
                    case 68:
                    case 69:
                    case 70:
                    case 76:
                    case 82:
                    case 88:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 1);
                            this.post(netmsgguaranted1);
                        }
                        int i = netmsginput.readUnsignedByte();
                        int i1 = netmsginput.readUnsignedShort();
                        NetObj netobj = netmsginput.readNetObj();
                        Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                        if (c != 'D') {
                            RocketryGeneric.this.handleRocketCommand_Both(c, i, i1, actor, false);
                        } else {
                            RocketryGeneric.this.handleDamageRamp_Both(1.0F, actor, false);
                        }
                        return true;

                    case 80:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted2);
                        }
                        int j = netmsginput.readUnsignedShort();
                        int j1 = netmsginput.readUnsignedByte();
                        int l1 = netmsginput.readUnsignedShort();
                        RocketryGeneric.this.handlePrepareLaunchCommand_Mirror(j, j1, l1);
                        return true;

                    case 83:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted3 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted3);
                        }
                        int k = netmsginput.readUnsignedByte();
                        RocketryGeneric.this.handleRespawnCommand_Mirror(k);
                        return true;

                    case 73:
                        boolean flag = netmsginput.readByte() != 0;
                        int k1 = netmsginput.readUnsignedByte();
                        int i2 = netmsginput.readUnsignedByte();
                        if (i2 > 10) {
                            i2 = 10;
                        }
                        RocketInGame arocketingame[] = new RocketInGame[i2];
                        for (int j2 = 0; j2 < i2; j2++) {
                            arocketingame[j2] = new RocketInGame();
                            arocketingame[j2].idR = netmsginput.readUnsignedByte();
                            arocketingame[j2].timeAfterStartS = netmsginput.readFloat();
                            arocketingame[j2].randseed = netmsginput.readUnsignedShort();
                        }

                        RocketryGeneric.this.handleInitCommand_Mirror(flag, k1, arocketingame);
                        return true;

                    case 67:
                    case 71:
                    case 72:
                    case 74:
                    case 75:
                    case 77:
                    case 78:
                    case 79:
                    case 81:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 99:
                    case 100:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 115:
                    case 116:
                    case 117:
                    case 118:
                    case 119:
                    default:
                        return false;
                }
            }
            switch (c) {
                case 45:
                    if (!NetMissionTrack.isPlaying()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postRealTo(Message.currentRealTime(), RocketryGeneric.this.net.masterChannel(), this.out);
                    }
                    return true;

                case 89:
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 0);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    long l = (long) (netmsginput.readFloat() * 1000D);
                    long l2 = Message.currentGameTime() + l;
                    int k2 = netmsginput.readUnsignedByte();
                    RocketryGeneric.this.handleSyncLaunchCommand_Mirror(l2, k2);
                    return true;
            }
            return false;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    class Move extends Interpolate {

        public boolean tick() {
            if (Time.current() < RocketryGeneric.this.actionTimeMS) {
                return true;
            }
            if (RocketryGeneric.this.damage >= 1.0F) {
                if (!Mission.isDeathmatch()) {
                    return false;
                }
                if (!RocketryGeneric.this.isNetMaster()) {
                    RocketryGeneric.this.actionTimeMS = Time.current() + 99999L;
                    return true;
                } else {
                    RocketryGeneric.this.damage = 0.0F;
                    RocketryGeneric.this.actionTimeMS = Time.current() + ((1000L * RocketryGeneric.this.launchIntervalS) / 2L);
                    RocketryGeneric.this.setMesh(RocketryGeneric.this.meshNames.ramp);
                    RocketryGeneric.this.setDiedFlag(false);
                    RocketryGeneric.this.sendRespawn_Master();
                    return true;
                }
            }
            if (RocketryGeneric.this.isNetMaster()) {
                RocketryGeneric.this.prepareLaunch_Master(RocketryGeneric.this.launchIntervalS / 2);
            } else {
                RocketryGeneric.this.actionTimeMS = Time.current() + 0x1869fL;
            }
            return true;
        }

        Move() {
        }
    }

    public static class RocketInGame {

        public int   idR;
        public float timeAfterStartS;
        public int   randseed;

        public RocketInGame() {
        }
    }

    public static class RocketryProperties {

        public String             name;
        public MeshesNames        summerNames;
        public MeshesNames        desertNames;
        public MeshesNames        winterNames;
        public String             soundName;
        public boolean            air;
        public float              MASS_FULL;
        public float              MASS_TNT;
        public float              EXPLOSION_RADIUS;
        public float              TAKEOFF_SPEED;
        public float              MAX_SPEED;
        public float              SPEEDUP_TIME;
        public float              FLY_HEIGHT;
        public float              HIT_ANGLE;
        public float              MAX_ERR_HEIGHT;
        public float              MAX_ERR_HIT_DISTANCE;
        public StrengthProperties stre;
        public float              DMG_WARHEAD_MM;
        public float              DMG_WARHEAD_PROB;
        public float              DMG_FUEL_MM;
        public float              DMG_FUEL_PROB;
        public float              DMG_ENGINE_MM;
        public float              DMG_ENGINE_PROB;
        public float              DMG_WING_MM;
        public float              DMG_WING_PROB;
        public float              DMG_WARHEAD_TNT;
        public float              DMG_WING_TNT;

        public RocketryProperties() {
            this.name = null;
            this.summerNames = new MeshesNames();
            this.desertNames = new MeshesNames();
            this.winterNames = new MeshesNames();
            this.soundName = null;
            this.air = false;
            this.MASS_FULL = 200F;
            this.MASS_TNT = 100F;
            this.EXPLOSION_RADIUS = 500F;
            this.TAKEOFF_SPEED = 1.0F;
            this.MAX_SPEED = 1.0F;
            this.SPEEDUP_TIME = 1.0F;
            this.FLY_HEIGHT = 1.0F;
            this.HIT_ANGLE = 30F;
            this.MAX_ERR_HEIGHT = 0.0F;
            this.MAX_ERR_HIT_DISTANCE = 0.0F;
            this.stre = new StrengthProperties();
            this.DMG_WARHEAD_MM = 0.0F;
            this.DMG_WARHEAD_PROB = 0.0F;
            this.DMG_FUEL_MM = 0.0F;
            this.DMG_FUEL_PROB = 0.0F;
            this.DMG_ENGINE_MM = 0.0F;
            this.DMG_ENGINE_PROB = 0.0F;
            this.DMG_WING_MM = 0.0F;
            this.DMG_WING_PROB = 0.0F;
            this.DMG_WARHEAD_TNT = 0.0F;
            this.DMG_WING_TNT = 0.0F;
        }
    }

    public static class TrajSeg {

        public double   t0;
        public double   t;
        public Point3d  pos0;
        public Vector3d v0;
        public Vector3d a;

        public TrajSeg() {
            this.pos0 = new Point3d();
            this.v0 = new Vector3d();
            this.a = new Vector3d();
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

    public static final float RndF(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    public static final int RndI(int i, int j) {
        return World.Rnd().nextInt(i, j);
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5D + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.Corpse() || !Actor.isValid(actor) || ((actor.net != null) && actor.net.isMirror())) {
            return;
        }
        if (actor instanceof Wreckage) {
            return;
        }
        if (!(actor instanceof Aircraft)) {
            return;
        }
        if (actor.getSpeed(null) < 28D) {
            return;
        }
        if ((s1 != null) && (s1.startsWith("Wing") || s1.startsWith("Stab") || s1.startsWith("Arone") || s1.startsWith("Vator") || s1.startsWith("Keel") || s1.startsWith("Rudder") || s1.startsWith("Pilot"))) {
            return;
        }
        if (this.isNetMirror()) {
            this.sendRampDamage_Mirror(1.0F, actor);
        } else {
            this.handleDamageRamp_Both(1.0F, actor, true);
        }
    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if (this.Corpse() || (shot.power <= 0.0F) || (this.isNetMirror() && shot.isMirage())) {
            return;
        }
        float f = shot.power;
        if (shot.powerType == 1) {
            f = shot.power / 2.4E-007F;
        }
        f *= RocketryGeneric.RndF(1.0F, 1.1F);
        if (f < this.prop.stre.SHOT_MIN_ENERGY) {
            return;
        }
        float f1 = f / this.prop.stre.SHOT_MAX_ENERGY;
        if (this.isNetMirror()) {
            this.sendRampDamage_Mirror(f1, shot.initiator);
        } else {
            this.handleDamageRamp_Both(f1, shot.initiator, true);
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (this.Corpse() || (this.isNetMirror() && explosion.isMirage())) {
            return;
        }
        float f = explosion.power;
        if ((explosion.powerType == 2) && (explosion.chunkName != null)) {
            f *= 4F;
        }
        float f1 = -1F;
        if (explosion.chunkName != null) {
            float f2 = f;
            f2 *= RocketryGeneric.RndF(1.0F, 1.1F);
            if (f2 >= this.prop.stre.EXPLHIT_MIN_TNT) {
                f1 = f2 / this.prop.stre.EXPLHIT_MAX_TNT;
            }
        }
        if (f1 < 0.0F) {
            float f3 = explosion.receivedTNT_1meter(this);
            f3 *= RocketryGeneric.RndF(1.0F, 1.1F);
            if (f3 >= this.prop.stre.EXPLNEAR_MIN_TNT) {
                f1 = f3 / this.prop.stre.EXPLNEAR_MAX_TNT;
            }
        }
        if (f1 > 0.0F) {
            if (this.isNetMirror()) {
                this.sendRampDamage_Mirror(f1, explosion.initiator);
            } else {
                this.handleDamageRamp_Both(f1, explosion.initiator, true);
            }
        }
    }

    private static void getHookOffset(Mesh mesh, String s, boolean flag, Point3d point3d) {
        int i = mesh.hookFind(s);
        if (i == -1) {
            if (flag) {
                System.out.println("Rocketry: Hook [" + s + "] not found");
                throw new RuntimeException("Can't work");
            }
            point3d.set(0.0D, 0.0D, 0.0D);
        }
        Matrix4d matrix4d = new Matrix4d();
        mesh.hookMatrix(i, matrix4d);
        point3d.set(matrix4d.m03, matrix4d.m13, matrix4d.m23);
    }

    TrajSeg[] _computeFallTrajectory_(int i, Point3d point3d, Vector3d vector3d) {
        TrajSeg atrajseg[] = new TrajSeg[1];
        for (int j = 0; j < atrajseg.length; j++) {
            atrajseg[j] = new TrajSeg();
        }

        atrajseg[0].pos0.set(point3d);
        atrajseg[0].v0.set(vector3d);
        atrajseg[0].a.set(0.0D, 0.0D, -3D);
        atrajseg[0].t0 = 0.0D;
        atrajseg[0].t = 500D;
        return atrajseg;
    }

    TrajSeg[] _computeWagonTrajectory_(int i) {
        RocketryGeneric.rndSeed.setSeed(i);
        Vector2d vector2d = new Vector2d();
        vector2d.set(this.endPos_wagon.x - this.begPos_wagon.x, this.endPos_wagon.y - this.begPos_wagon.y);
        vector2d.normalize();
        TrajSeg atrajseg[] = new TrajSeg[2];
        for (int j = 0; j < atrajseg.length; j++) {
            atrajseg[j] = new TrajSeg();
        }

        Vector3d vector3d = new Vector3d();
        float f = this.prop.TAKEOFF_SPEED;
        vector3d.sub(this.endPos_wagon, this.begPos_wagon);
        double d = vector3d.length();
        vector3d.scale(1.0D / d);
        atrajseg[0].v0.set(0.0D, 0.0D, 0.0D);
        atrajseg[0].pos0.set(this.begPos_wagon);
        atrajseg[0].t = (2D * d) / (0.0D + f);
        atrajseg[0].a.set(vector3d);
        atrajseg[0].a.scale(f / atrajseg[0].t);
        f = this.prop.TAKEOFF_SPEED * RocketryGeneric.rndSeed.nextFloat(0.85F, 0.97F);
        double d1 = vector3d.z * d;
        double d2 = Math.sqrt((d * d) - (d1 * d1));
        atrajseg[1].v0.set(d2, 0.0D, d1);
        atrajseg[1].v0.normalize();
        atrajseg[1].v0.scale(f);
        atrajseg[1].pos0.set(0.0D, 0.0D, this.endPos_wagon.z);
        atrajseg[1].t = 30D + (2D * (atrajseg[1].v0.z / 9.8D));
        atrajseg[1].a.set(0.0D, 0.0D, -9.8D);
        for (int k = 0; k <= 1; k++) {
            if (atrajseg[k].t <= 0.0D) {
                return null;
            }
        }

        atrajseg[0].t0 = 0.0D;
        for (int l = 1; l <= 1; l++) {
            atrajseg[l].t0 = atrajseg[l - 1].t0 + atrajseg[l - 1].t;
        }

        for (int i1 = 1; i1 <= 1; i1++) {
            atrajseg[i1].pos0.set(vector2d.x * atrajseg[i1].pos0.x, vector2d.y * atrajseg[i1].pos0.x, atrajseg[i1].pos0.z);
            atrajseg[i1].pos0.add(this.endPos_wagon.x, this.endPos_wagon.y, 0.0D);
            atrajseg[i1].v0.set(vector2d.x * atrajseg[i1].v0.x, vector2d.y * atrajseg[i1].v0.x, atrajseg[i1].v0.z);
            atrajseg[i1].a.set(vector2d.x * atrajseg[i1].a.x, vector2d.y * atrajseg[i1].a.x, atrajseg[i1].a.z);
        }

        return atrajseg;
    }

    private TrajSeg[] _computeAirTrajectory_(int i) {
        RocketryGeneric.rndSeed.setSeed(i);
        Point3d point3d = new Point3d();
        point3d.set(this.targetPos);
        double d = RocketryGeneric.rndSeed.nextFloat(0.0F, 359.99F);
        float f = RocketryGeneric.rndSeed.nextFloat(0.0F, this.prop.MAX_ERR_HIT_DISTANCE);
        point3d.add(Geom.cosDeg((float) d) * f, Geom.sinDeg((float) d) * f, 0.0D);
        d = this.prop.FLY_HEIGHT + RocketryGeneric.rndSeed.nextFloat(-this.prop.MAX_ERR_HEIGHT, this.prop.MAX_ERR_HEIGHT);
        Point3d point3d1 = new Point3d();
        point3d1.set(super.pos.getAbsPoint());
        point3d1.z = d;
        Object obj = new Point2d(point3d.x, point3d.y);
        Point2d point2d = new Point2d(point3d1.x, point3d1.y);
        double d1 = ((Point2d) obj).distance(point2d);
        obj = new Vector2d();
        ((Vector2d) obj).set(point3d.x - point3d1.x, point3d.y - point3d1.y);
        ((Vector2d) obj).normalize();
        float f1 = this.prop.MAX_SPEED;
        TrajSeg atrajseg[] = new TrajSeg[3];
        for (int j = 0; j < atrajseg.length; j++) {
            atrajseg[j] = new TrajSeg();
        }

        atrajseg[0].v0.set(f1, 0.0D, 0.0D);
        atrajseg[1].v0.set(f1, 0.0D, 0.0D);
        atrajseg[2].v0.set(f1 * Geom.cosDeg(this.prop.HIT_ANGLE), 0.0D, -f1 * Geom.sinDeg(this.prop.HIT_ANGLE));
        atrajseg[0].pos0.set(0.0D, 0.0D, d);
        atrajseg[2].pos0.set(d1, 0.0D, point3d.z);
        atrajseg[1].t = (2D * (atrajseg[2].pos0.z - d)) / (atrajseg[1].v0.z + atrajseg[2].v0.z);
        atrajseg[1].pos0.set(atrajseg[2].pos0.x - (0.5D * (atrajseg[1].v0.x + atrajseg[2].v0.x) * atrajseg[1].t), 0.0D, d);
        atrajseg[2].t = 100D;
        atrajseg[0].t = (2D * (atrajseg[1].pos0.x - atrajseg[0].pos0.x)) / (atrajseg[0].v0.x + atrajseg[1].v0.x);
        for (int k = 0; k <= 2; k++) {
            if (atrajseg[k].t <= 0.0D) {
                return null;
            }
        }

        atrajseg[0].a.set(0.0D, 0.0D, 0.0D);
        for (int l = 1; l <= 1; l++) {
            atrajseg[l].a.sub(atrajseg[l + 1].v0, atrajseg[l].v0);
            atrajseg[l].a.scale(1.0D / atrajseg[l].t);
        }

        atrajseg[2].a.set(0.0D, 0.0D, 0.0D);
        atrajseg[0].t0 = 0.0D;
        for (int i1 = 1; i1 <= 2; i1++) {
            atrajseg[i1].t0 = atrajseg[i1 - 1].t0 + atrajseg[i1 - 1].t;
        }

        for (int j1 = 0; j1 <= 2; j1++) {
            atrajseg[j1].pos0.set(((Vector2d) obj).x * atrajseg[j1].pos0.x, ((Vector2d) obj).y * atrajseg[j1].pos0.x, atrajseg[j1].pos0.z);
            atrajseg[j1].pos0.add(point3d1.x, point3d1.y, 0.0D);
            atrajseg[j1].v0.set(((Vector2d) obj).x * atrajseg[j1].v0.x, ((Vector2d) obj).y * atrajseg[j1].v0.x, atrajseg[j1].v0.z);
            atrajseg[j1].a.set(((Vector2d) obj).x * atrajseg[j1].a.x, ((Vector2d) obj).y * atrajseg[j1].a.x, atrajseg[j1].a.z);
        }

        return atrajseg;
    }

    private TrajSeg[] _computeRampTrajectory_(int i, boolean flag) {
        RocketryGeneric.rndSeed.setSeed(i);
        Point3d point3d = new Point3d();
        point3d.set(this.targetPos);
        double d = RocketryGeneric.rndSeed.nextFloat(0.0F, 359.99F);
        float f = RocketryGeneric.rndSeed.nextFloat(0.0F, this.prop.MAX_ERR_HIT_DISTANCE);
        point3d.add(Geom.cosDeg((float) d) * f, Geom.sinDeg((float) d) * f, 0.0D);
        Object obj = new Point2d(point3d.x, point3d.y);
        Point2d point2d = new Point2d(this.endPos_rocket.x, this.endPos_rocket.y);
        d = ((Point2d) obj).distance(point2d);
        obj = new Vector2d();
        ((Vector2d) obj).set(point3d.x - this.endPos_rocket.x, point3d.y - this.endPos_rocket.y);
        ((Vector2d) obj).normalize();
        float f1 = flag ? 0.5F : 1.0F;
        double d1 = (f1 * this.prop.FLY_HEIGHT) + (f1 * RocketryGeneric.rndSeed.nextFloat(-this.prop.MAX_ERR_HEIGHT, this.prop.MAX_ERR_HEIGHT));
        float f2 = flag ? this.prop.TAKEOFF_SPEED + 1.0F : this.prop.MAX_SPEED;
        TrajSeg atrajseg[] = new TrajSeg[6];
        for (int j = 0; j < atrajseg.length; j++) {
            atrajseg[j] = new TrajSeg();
        }

        Vector3d vector3d = new Vector3d();
        vector3d.sub(this.endPos_rocket, this.begPos_rocket);
        double d2 = vector3d.length();
        vector3d.scale(1.0D / d2);
        atrajseg[0].v0.set(0.0D, 0.0D, 0.0D);
        atrajseg[0].pos0.set(this.begPos_rocket);
        atrajseg[0].t = (2D * d2) / (0.0D + this.prop.TAKEOFF_SPEED);
        atrajseg[0].a.set(vector3d);
        atrajseg[0].a.scale(this.prop.TAKEOFF_SPEED / atrajseg[0].t);
        double d3 = vector3d.z * d2;
        double d4 = Math.sqrt((d2 * d2) - (d3 * d3));
        atrajseg[1].v0.set(d4, 0.0D, d3);
        atrajseg[1].v0.normalize();
        atrajseg[1].v0.scale(this.prop.TAKEOFF_SPEED);
        atrajseg[2].v0.set(this.prop.TAKEOFF_SPEED, 0.0D, 0.0D);
        atrajseg[3].v0.set(f2, 0.0D, 0.0D);
        atrajseg[4].v0.set(f2, 0.0D, 0.0D);
        atrajseg[5].v0.set(f2 * Geom.cosDeg(this.prop.HIT_ANGLE), 0.0D, -f2 * Geom.sinDeg(this.prop.HIT_ANGLE));
        atrajseg[1].pos0.set(0.0D, 0.0D, this.endPos_rocket.z);
        atrajseg[1].t = (2D * (d1 - atrajseg[1].pos0.z)) / (atrajseg[1].v0.z + 0.0D);
        atrajseg[2].pos0.set(atrajseg[1].pos0.x + (0.5D * (atrajseg[1].v0.x + atrajseg[2].v0.x) * atrajseg[1].t), 0.0D, d1);
        atrajseg[2].t = flag ? 0.5D : this.prop.SPEEDUP_TIME;
        atrajseg[3].pos0.set(atrajseg[2].pos0.x + (0.5D * (atrajseg[2].v0.x + atrajseg[3].v0.x) * atrajseg[2].t), 0.0D, d1);
        atrajseg[5].pos0.set(d, 0.0D, point3d.z);
        atrajseg[4].t = (2D * (atrajseg[5].pos0.z - d1)) / (atrajseg[4].v0.z + atrajseg[5].v0.z);
        atrajseg[4].pos0.set(atrajseg[5].pos0.x - (0.5D * (atrajseg[4].v0.x + atrajseg[5].v0.x) * atrajseg[4].t), 0.0D, d1);
        atrajseg[5].t = 100D;
        atrajseg[3].t = ((2D * atrajseg[4].pos0.x) - atrajseg[3].pos0.x) / (atrajseg[3].v0.x + atrajseg[4].v0.x);
        for (int k = 0; k <= 5; k++) {
            if (atrajseg[k].t <= 0.0D) {
                return null;
            }
        }

        for (int l = 1; l <= 4; l++) {
            atrajseg[l].a.sub(atrajseg[l + 1].v0, atrajseg[l].v0);
            atrajseg[l].a.scale(1.0D / atrajseg[l].t);
        }

        atrajseg[5].a.set(0.0D, 0.0D, 0.0D);
        atrajseg[0].t0 = 0.0D;
        for (int i1 = 1; i1 <= 5; i1++) {
            atrajseg[i1].t0 = atrajseg[i1 - 1].t0 + atrajseg[i1 - 1].t;
        }

        for (int j1 = 1; j1 <= 5; j1++) {
            atrajseg[j1].pos0.set(((Vector2d) obj).x * atrajseg[j1].pos0.x, ((Vector2d) obj).y * atrajseg[j1].pos0.x, atrajseg[j1].pos0.z);
            atrajseg[j1].pos0.add(this.endPos_rocket.x, this.endPos_rocket.y, 0.0D);
            atrajseg[j1].v0.set(((Vector2d) obj).x * atrajseg[j1].v0.x, ((Vector2d) obj).y * atrajseg[j1].v0.x, atrajseg[j1].v0.z);
            atrajseg[j1].a.set(((Vector2d) obj).x * atrajseg[j1].a.x, ((Vector2d) obj).y * atrajseg[j1].a.x, atrajseg[j1].a.z);
        }

        return atrajseg;
    }

    private TrajSeg[] computeNormalTrajectory(int i) {
        TrajSeg atrajseg[] = null;
        if (this.prop.air) {
            atrajseg = this._computeAirTrajectory_(i);
        } else {
            atrajseg = this._computeRampTrajectory_(i, false);
            if (atrajseg == null) {
                atrajseg = this._computeRampTrajectory_(i, true);
            }
        }
        return atrajseg;
    }

    private RocketryGeneric(RocketryProperties rocketryproperties, MeshesNames meshesnames, String s, int i, NetChannel netchannel, int j, double d, double d1, float f, Point2d point2d, float f1, float f2, int k) {
        super(meshesnames.ramp);
        this.prop = null;
        this.meshNames = null;
        this.targetPos = null;
        this.begPos_wagon = new Point3d();
        this.endPos_wagon = new Point3d();
        this.begPos_rocket = new Point3d();
        this.endPos_rocket = new Point3d();
        this.launchIntervalS = 0;
        this.rs = new ArrayList();
        this.damage = 0.0F;
        this.actionTimeMS = 0L;
        this.prop = rocketryproperties;
        this.meshNames = meshesnames;
        this.countRockets = k;
        if ((this.countRockets == 0) || (point2d == null)) {
            this.countRockets = 0;
            point2d = null;
            this.targetPos = null;
        } else {
            this.targetPos = new Point3d();
            this.targetPos.set(point2d.x, point2d.y, Engine.land().HQ(point2d.x, point2d.y));
        }
        this.setName(s);
        this.setArmy(i);
        Point3d point3d = new Point3d();
        if (this.prop.air) {
            point3d.set(d, d1, this.prop.FLY_HEIGHT);
        } else {
            point3d.set(d, d1, Engine.land().HQ(d, d1));
            Point3d point3d1 = new Point3d();
            RocketryGeneric.getHookOffset(this.mesh(), "Ground_Level", false, point3d1);
            point3d.z -= point3d1.z;
        }
        Orient orient = new Orient();
        if (this.targetPos == null) {
            orient.set(f, 0.0F, 0.0F);
        } else {
            Vector3d vector3d = new Vector3d();
            vector3d.sub(this.targetPos, point3d);
            vector3d.z = 0.0D;
            orient.setAT0(vector3d);
        }
        super.pos.setAbs(point3d, orient);
        super.pos.reset();
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
            RocketryGeneric.getHookOffset(this.mesh(), "_begWagon", true, this.begPos_wagon);
            super.pos.getAbs().transform(this.begPos_wagon);
            RocketryGeneric.getHookOffset(this.mesh(), "_endWagon", true, this.endPos_wagon);
            super.pos.getAbs().transform(this.endPos_wagon);
            RocketryGeneric.getHookOffset(this.mesh(), "_begRocket", true, this.begPos_rocket);
            super.pos.getAbs().transform(this.begPos_rocket);
            RocketryGeneric.getHookOffset(this.mesh(), "_endRocket", true, this.endPos_rocket);
            super.pos.getAbs().transform(this.endPos_rocket);
        }
        int l = (int) ((f1 * 60F) + 0.5F);
        if (l < 0) {
            l = 0;
        }
        if (l > 14400) {
            l = 14400;
        }
        this.timeout = l;
        this.launchIntervalS = (int) ((f2 * 60F) + 0.5F);
        if (this.launchIntervalS < 180) {
            this.launchIntervalS = 180;
        }
        if (this.launchIntervalS > 14400) {
            this.launchIntervalS = 14400;
        }
        this.damage = 0.0F;
        this.actionTimeMS = 0x7fffffffffffffffL;
        this.createNetObject(netchannel, j);
        if (!World.cur().triggersGuard.listTriggerChiefSol.contains(s)) {
            if (this.targetPos != null) {
                if (this.isNetMaster()) {
                    long l1 = (1000L * this.timeout) - ((1000L * this.launchIntervalS) / 2L);
                    if (l1 <= 0L) {
                        this.prepareLaunch_Master(this.timeout);
                    } else {
                        this.actionTimeMS = Time.current() + l1;
                    }
                } else {
                    this.actionTimeMS = 0x7fffffffffffffffL;
                }
            }
            if (!this.interpEnd("move")) {
                this.interpPut(new Move(), "move", Time.current(), null);
            }
        }
    }

    public void startMove() {
        if (this.targetPos != null) {
            if (this.isNetMaster()) {
                long l1 = (1000L * this.timeout) - ((1000L * this.launchIntervalS) / 2L);
                if (l1 <= 0L) {
                    this.prepareLaunch_Master(this.timeout);
                } else {
                    this.actionTimeMS = Time.current() + l1;
                }
            } else {
                this.actionTimeMS = 0x7fffffffffffffffL;
            }
        }
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
        }
    }

    public int HitbyMask() {
        return -2;
    }

    public int chooseBulletType(BulletProperties abulletproperties[]) {
        if (this.Corpse()) {
            return -1;
        }
        if (abulletproperties.length == 1) {
            return 0;
        }
        if (abulletproperties.length <= 0) {
            return -1;
        }
        if (abulletproperties[0].power <= 0.0F) {
            return 0;
        }
        if (abulletproperties[1].power <= 0.0F) {
            return 1;
        }
        if (abulletproperties[0].cumulativePower > 0.0F) {
            return 0;
        }
        if (abulletproperties[1].cumulativePower > 0.0F) {
            return 1;
        }
        if (abulletproperties[0].powerType == 1) {
            return 0;
        }
        if (abulletproperties[1].powerType == 1) {
            return 1;
        } else {
            return abulletproperties[0].powerType == 0 ? 1 : 0;
        }
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        return this.Corpse() ? -1 : 0;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.Corpse() || (i != 0)) {
            return false;
        }
        if (point3d != null) {
            point3d.set(0.0D, 0.0D, 0.0D);
        }
        return true;
    }

    private RocketryRocket findMyRocket(int i) {
        for (int j = 0; j < this.rs.size(); j++) {
            if (((RocketryRocket) this.rs.get(j)).idR == i) {
                return (RocketryRocket) this.rs.get(j);
            }
        }

        return null;
    }

    void forgetRocket(RocketryRocket rocketryrocket) {
        for (int i = 0; i < this.rs.size(); i++) {
            if ((RocketryRocket) this.rs.get(i) == rocketryrocket) {
                this.rs.remove(i);
                return;
            }
        }

    }

    private void killWrongRockets(int i) {
        for (int j = 0; j < this.rs.size(); j++) {
            int k = ((RocketryRocket) this.rs.get(j)).idR;
            byte byte0 = (byte) (k - i);
            if ((byte0 >= 0) && (byte0 <= 20)) {
                ((RocketryRocket) this.rs.get(j)).silentDeath();
                this.rs.remove(j);
                j--;
            }
        }

    }

    private void sendRampDamage_Mirror(float f, Actor actor) {
        if (!this.isNetMirror() || (super.net.masterChannel() instanceof NetChannelInStream)) {
            return;
        }
        int i = (int) (f * 65000F);
        if (i <= 0) {
            return;
        }
        if (i > 65000) {
            i = 65000;
        }
        try {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(45);
            netmsgfiltered.writeShort(i);
            netmsgfiltered.writeNetObj(actor == null ? null : ((NetObj) (actor.net)));
            netmsgfiltered.setIncludeTime(false);
            super.net.postTo(Time.current(), super.net.masterChannel(), netmsgfiltered);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void sendRespawn_Master() {
        if (!super.net.isMirrored()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(83);
            netmsgguaranted.writeByte(this.nextFreeIdR);
            super.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void prepareLaunch_Master(int i) {
        if (this.countRockets <= 0) {
            this.countRockets = 0;
            this.actionTimeMS = 0x7fffffffffffffffL;
            return;
        }
        long l = Time.current();
        int j = RocketryGeneric.RndI(0, 65535);
        int k = this.nextFreeIdR;
        this.nextFreeIdR = (this.nextFreeIdR + 1) & 0xff;
        long l1 = l + (1000L * i);
        this.actionTimeMS = l1 + ((1000L * this.launchIntervalS) / 2L);
        this.killWrongRockets(k);
        TrajSeg atrajseg[] = this.computeNormalTrajectory(j);
        if (atrajseg == null) {
            return;
        }
        RocketryRocket rocketryrocket = new RocketryRocket(this, this.meshNames.rocket, k, j, l1, l, atrajseg);
        if (rocketryrocket.isDamaged()) {
            rocketryrocket.silentDeath();
            return;
        }
        ZutiPadObject zutipadobject = new ZutiPadObject(rocketryrocket, Main.cur().mission.zutiRadar_RefreshInterval > 0);
        zutipadobject.type = 3;
        GUI.pad.zutiPadObjects.put(new Integer(zutipadobject.hashCode()), zutipadobject);
        this.rs.add(rocketryrocket);
        if (this.countRockets < 1000) {
            this.countRockets--;
        }
        if (!super.net.isMirrored()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(80);
            netmsgguaranted.writeShort(i);
            netmsgguaranted.writeByte(k);
            netmsgguaranted.writeShort(j);
            super.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void handleInitCommand_Mirror(boolean flag, int i, RocketInGame arocketingame[]) {
        for (int j = 0; j < this.rs.size(); j++) {
            ((RocketryRocket) this.rs.get(j)).silentDeath();
        }

        this.rs.clear();
        this.damage = flag ? 0.0F : 1.0F;
        this.setMesh(flag ? this.meshNames.ramp : this.meshNames.ramp_d);
        if (this.targetPos == null) {
            return;
        }
        long l = Time.current();
        for (int k = 0; k < arocketingame.length; k++) {
            TrajSeg atrajseg[] = this.computeNormalTrajectory(arocketingame[k].randseed);
            if (atrajseg != null) {
                RocketryRocket rocketryrocket = new RocketryRocket(this, this.meshNames.rocket, arocketingame[k].idR, arocketingame[k].randseed, l - (long) (1000D * arocketingame[k].timeAfterStartS), l, atrajseg);
                if (rocketryrocket.isDamaged()) {
                    rocketryrocket.silentDeath();
                } else {
                    if (GUI.pad != null) {
                        ZutiPadObject zutipadobject = new ZutiPadObject(rocketryrocket, Main.cur().mission.zutiRadar_RefreshInterval > 0);
                        zutipadobject.type = 3;
                        GUI.pad.zutiPadObjects.put(new Integer(zutipadobject.hashCode()), zutipadobject);
                    }
                    this.rs.add(rocketryrocket);
                }
            }
        }

    }

    private void handleRespawnCommand_Mirror(int i) {
        this.killWrongRockets(i);
        this.actionTimeMS = Time.current() + 0x1869fL;
        if (this.damage >= 1.0F) {
            this.damage = 0.0F;
            this.setMesh(this.meshNames.ramp);
            this.setDiedFlag(false);
        } else {
            this.damage = 0.0F;
        }
    }

    private void handlePrepareLaunchCommand_Mirror(int i, int j, int k) {
        this.killWrongRockets(j);
        if (this.targetPos == null) {
            return;
        }
        long l = Time.current();
        TrajSeg atrajseg[] = this.computeNormalTrajectory(k);
        if (atrajseg == null) {
            return;
        }
        RocketryRocket rocketryrocket = new RocketryRocket(this, this.meshNames.rocket, j, k, l + i * 1000, l, atrajseg);
        if (rocketryrocket.isDamaged()) {
            rocketryrocket.silentDeath();
        } else {
            this.rs.add(rocketryrocket);
        }
    }

    private void handleSyncLaunchCommand_Mirror(long l, int i) {
        this.killWrongRockets((i + 1) & 0xff);
        if (this.targetPos == null) {
            return;
        }
        RocketryRocket rocketryrocket = this.findMyRocket(i);
        if (rocketryrocket != null) {
            rocketryrocket.changeLaunchTimeIfCan(l);
        }
    }

    public void sendRocketStateChange(RocketryRocket rocketryrocket, char c, Actor actor) {
        boolean flag = this.isNetMaster();
        int i = RocketryGeneric.RndI(0, 65535);
        if (flag) {
            this.handleRocketCommand_Both(c, rocketryrocket.idR, i, actor, true);
            return;
        }
        if (!this.isNetMirror() || (super.net.masterChannel() instanceof NetChannelInStream)) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(Character.toLowerCase(c));
            netmsgguaranted.writeByte(rocketryrocket.idR);
            netmsgguaranted.writeShort(i);
            netmsgguaranted.writeNetObj(actor == null ? null : ((NetObj) (actor.net)));
            super.net.postTo(super.net.masterChannel(), netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void handleRocketCommand_Both(char c, int i, int j, Actor actor, boolean flag) {
        if (this.targetPos == null) {
            return;
        }
        RocketryRocket rocketryrocket = this.findMyRocket(i);
        if (rocketryrocket == null) {
            return;
        }
        c = rocketryrocket.handleCommand(c, j, actor);
        if ((c == 0) || !flag || !super.net.isMirrored()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(Character.toUpperCase(c));
            netmsgguaranted.writeByte(i);
            netmsgguaranted.writeShort(j);
            netmsgguaranted.writeNetObj(actor == null ? null : ((NetObj) (actor.net)));
            super.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void handleDamageRamp_Both(float f, Actor actor, boolean flag) {
        if (this.prop.air || (f <= 0.0F) || (this.damage >= 1.0F)) {
            return;
        }
        this.damage += f;
        if (this.damage >= 1.0F) {
            this.damage = 1.0F;
        } else {
            return;
        }
        World.onActorDied(this, actor);
        this.setMesh(this.meshNames.ramp_d);
        this.actionTimeMS = Time.current();
        if (Mission.isDeathmatch()) {
            this.actionTimeMS += RocketryGeneric.SecsToTicks(Mission.respawnTime("Artillery") * RocketryGeneric.RndF(1.0F, 1.2F));
        }
        for (int i = 0; i < this.rs.size(); i++) {
            RocketryRocket rocketryrocket = (RocketryRocket) this.rs.get(i);
            int j = rocketryrocket.idR;
            if (rocketryrocket.isOnRamp()) {
                this.handleRocketCommand_Both('X', j, RocketryGeneric.RndI(0, 65535), actor, flag);
                i = 0;
            }
        }

        Explosions.ExplodeBridge(this.begPos_wagon, this.endPos_wagon, 1.2F);
        if (!flag || !super.net.isMirrored()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeByte(this.nextFreeIdR);
            netmsgguaranted.writeShort(RocketryGeneric.RndI(0, 65535));
            netmsgguaranted.writeNetObj(actor == null ? null : ((NetObj) (actor.net)));
            super.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) {
            super.net = new Master(this);
        } else {
            super.net = new Mirror(this, netchannel, i);
        }
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        netmsgguaranted.writeByte(this.damage >= 1.0F ? 0 : 1);
        netmsgguaranted.writeByte(this.nextFreeIdR);
        int i = 0;
        for (int j = 0; j < this.rs.size(); j++) {
            RocketryRocket rocketryrocket = (RocketryRocket) this.rs.get(j);
            if (!rocketryrocket.isDamaged()) {
                i++;
            }
        }

        if (i > 10) {
            i = 10;
        }
        netmsgguaranted.writeByte(i);
        for (int k = this.rs.size() - 1; k >= 0; k--) {
            RocketryRocket rocketryrocket1 = (RocketryRocket) this.rs.get(k);
            if (rocketryrocket1.isDamaged()) {
                continue;
            }
            netmsgguaranted.writeByte(rocketryrocket1.idR);
            float f = (float) ((Time.current() - rocketryrocket1.timeOfStartMS) * 0.001D);
            netmsgguaranted.writeFloat(f);
            netmsgguaranted.writeShort(rocketryrocket1.randseed);
            if (--i <= 0) {
                break;
            }
        }

        super.net.postTo(netchannel, netmsgguaranted);
    }

    public static RocketryGeneric New(String s, String s1, NetChannel netchannel, int i, int j, double d, double d1, float f, float f1, int k, float f2, Point2d point2d) {
        RocketryProperties rocketryproperties = (RocketryProperties) RocketryGeneric.rocketryMap.get(s1);
        if (rocketryproperties == null) {
            System.out.println("***** Rocketry: Unknown type [" + s1 + "]");
            return null;
        }
        MeshesNames meshesnames = null;
        switch (World.cur().camouflage) {
            case 1:
                meshesnames = rocketryproperties.winterNames;
                break;

            case 2:
                meshesnames = rocketryproperties.desertNames;
                break;

            default:
                meshesnames = rocketryproperties.summerNames;
                break;
        }
        return new RocketryGeneric(rocketryproperties, meshesnames, s, j, netchannel, i, d, d1, f, point2d, f1, f2, k);
    }

    public static final float KmHourToMSec(float f) {
        return f / 3.6F;
    }

    private static float getF(SectFile sectfile, String s, String s1, float f, float f1) {
        float f2 = sectfile.get(s, s1, -9865.345F);
        if ((f2 == -9865.345F) || (f2 < f) || (f2 > f1)) {
            if (f2 == -9865.345F) {
                System.out.println("Rocketry: Parameter [" + s + "]:<" + s1 + "> " + "not found");
            } else {
                System.out.println("Rocketry: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
            }
            throw new RuntimeException("Can't set property");
        } else {
            return f2;
        }
    }

    private static String getS(SectFile sectfile, String s, String s1) {
        String s2 = sectfile.get(s, s1);
        if ((s2 == null) || (s2.length() <= 0)) {
            System.out.print("Rocketry: Parameter [" + s + "]:<" + s1 + "> ");
            System.out.println(s2 == null ? "not found" : "is empty");
            throw new RuntimeException("Can't set property");
        } else {
            return s2;
        }
    }

    private static String getS(SectFile sectfile, String s, String s1, String s2) {
        String s3 = sectfile.get(s, s1);
        if ((s3 == null) || (s3.length() <= 0)) {
            return s2;
        } else {
            return s3;
        }
    }

    public static void PreLoad(String s) {
        Property.set(RocketryGeneric.class, "iconName", "icons/objV1.mat");
        RocketryGeneric.rocketryMap = new HashMap();
        SectFile sectfile = new SectFile(s, 0);
        int i = sectfile.sections();
        for (int j = 0; j < i; j++) {
            RocketryProperties rocketryproperties = new RocketryProperties();
            rocketryproperties.name = sectfile.sectionName(j);
            rocketryproperties.summerNames.setNull();
            rocketryproperties.desertNames.setNull();
            rocketryproperties.winterNames.setNull();
            rocketryproperties.summerNames.ramp = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshSummer_ramp", "");
            if (rocketryproperties.summerNames.ramp == "") {
                rocketryproperties.air = true;
                rocketryproperties.summerNames.ramp = "3do/primitive/coord/mono.sim";
                rocketryproperties.summerNames.ramp_d = rocketryproperties.summerNames.ramp;
                rocketryproperties.desertNames = rocketryproperties.summerNames;
                rocketryproperties.winterNames = rocketryproperties.summerNames;
            } else {
                rocketryproperties.air = false;
                rocketryproperties.desertNames.ramp = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshDesert_ramp", rocketryproperties.summerNames.ramp);
                rocketryproperties.winterNames.ramp = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshWinter_ramp", rocketryproperties.summerNames.ramp);
                rocketryproperties.summerNames.ramp_d = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshSummerDamage_ramp");
                rocketryproperties.desertNames.ramp_d = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshDesertDamage_ramp", rocketryproperties.summerNames.ramp_d);
                rocketryproperties.winterNames.ramp_d = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshWinterDamage_ramp", rocketryproperties.summerNames.ramp_d);
                rocketryproperties.summerNames.wagon = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshSummer_wagon");
                rocketryproperties.desertNames.wagon = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshDesert_wagon", rocketryproperties.summerNames.wagon);
                rocketryproperties.winterNames.wagon = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshWinter_wagon", rocketryproperties.summerNames.wagon);
            }
            rocketryproperties.summerNames.rocket = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshSummer_rocket");
            rocketryproperties.desertNames.rocket = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshDesert_rocket", rocketryproperties.summerNames.rocket);
            rocketryproperties.winterNames.rocket = RocketryGeneric.getS(sectfile, rocketryproperties.name, "MeshWinter_rocket", rocketryproperties.summerNames.rocket);
            rocketryproperties.soundName = RocketryGeneric.getS(sectfile, rocketryproperties.name, "SoundMove");
            rocketryproperties.MASS_FULL = RocketryGeneric.getF(sectfile, rocketryproperties.name, "MassFull", 0.5F, 100000F);
            rocketryproperties.MASS_TNT = RocketryGeneric.getF(sectfile, rocketryproperties.name, "MassTNT", 0.0F, 1E+007F);
            rocketryproperties.EXPLOSION_RADIUS = RocketryGeneric.getF(sectfile, rocketryproperties.name, "ExplosionRadius", 0.01F, 100000F);
            if (!rocketryproperties.air) {
                rocketryproperties.TAKEOFF_SPEED = RocketryGeneric.getF(sectfile, rocketryproperties.name, "TakeoffSpeed", 1.0F, 3000F);
                rocketryproperties.TAKEOFF_SPEED = RocketryGeneric.KmHourToMSec(rocketryproperties.TAKEOFF_SPEED);
            }
            rocketryproperties.MAX_SPEED = RocketryGeneric.getF(sectfile, rocketryproperties.name, "MaxSpeed", rocketryproperties.TAKEOFF_SPEED, 3000F);
            rocketryproperties.MAX_SPEED = RocketryGeneric.KmHourToMSec(rocketryproperties.MAX_SPEED);
            if (!rocketryproperties.air) {
                rocketryproperties.SPEEDUP_TIME = RocketryGeneric.getF(sectfile, rocketryproperties.name, "SpeedupTime", 1.0F, 10000F);
            }
            rocketryproperties.FLY_HEIGHT = RocketryGeneric.getF(sectfile, rocketryproperties.name, "FlyHeight", 100F, 15000F);
            rocketryproperties.HIT_ANGLE = RocketryGeneric.getF(sectfile, rocketryproperties.name, "HitAngle", 5F, 89F);
            rocketryproperties.MAX_ERR_HEIGHT = RocketryGeneric.getF(sectfile, rocketryproperties.name, "MaxErrHeight", 0.0F, 2000F);
            rocketryproperties.MAX_ERR_HIT_DISTANCE = RocketryGeneric.getF(sectfile, rocketryproperties.name, "MaxErrHitDistance", 0.0F, 10000F);
            if (!rocketryproperties.air && !rocketryproperties.stre.read("Rocketry", sectfile, null, rocketryproperties.name)) {
                throw new RuntimeException("Can't register Rocketry data");
            }
            rocketryproperties.DMG_WARHEAD_MM = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgWarhead_mm", 0.0F, 2000F);
            rocketryproperties.DMG_WARHEAD_PROB = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgWarhead_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_FUEL_MM = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgFuel_mm", 0.0F, 2000F);
            rocketryproperties.DMG_FUEL_PROB = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgFuel_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_ENGINE_MM = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgEngine_mm", 0.0F, 2000F);
            rocketryproperties.DMG_ENGINE_PROB = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgEngine_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_WING_MM = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgWing_mm", 0.0F, 2000F);
            rocketryproperties.DMG_WING_PROB = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgWing_prob", 0.0F, 1.0F);
            rocketryproperties.DMG_WARHEAD_TNT = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgWarhead_tnt", 0.0F, 1000000F);
            rocketryproperties.DMG_WING_TNT = RocketryGeneric.getF(sectfile, rocketryproperties.name, "DmgWing_tnt", 0.0F, 1000000F);
            RocketryGeneric.rocketryMap.put(rocketryproperties.name, rocketryproperties);
        }

    }

    private static HashMap     rocketryMap = new HashMap();
    RocketryProperties         prop;
    MeshesNames                meshNames;
    private Point3d            targetPos;
    private Point3d            begPos_wagon;
    private Point3d            endPos_wagon;
    private Point3d            begPos_rocket;
    private Point3d            endPos_rocket;
    private int                launchIntervalS;
    private ArrayList          rs;
    private int                nextFreeIdR;
    private float              damage;
    private long               actionTimeMS;
    private int                countRockets;
    private static RangeRandom rndSeed     = new RangeRandom();
    private int                timeout;
}
