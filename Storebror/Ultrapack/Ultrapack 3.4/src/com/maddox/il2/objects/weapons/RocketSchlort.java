package com.maddox.il2.objects.weapons;

import java.io.IOException;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

public class RocketSchlort extends Rocket {

    private Eff3DActor         fl1                = null;
    private Eff3DActor         fl2                = null;
    private static Orient      or                 = new Orient();
    private static Point3d     p                  = new Point3d();
    private Orient             orVictimOffset     = null;
    private Point3f            pVictimOffset      = null;
    private Point3d            pT                 = null;
    private Vector3d           v                  = null;
    private Vector3d           victimSpeed        = null;
    private long               tStart             = 0L;
    private long               tDestroy           = 0L;
    private float              prevd              = 0.0F;
    private float              deltaAzimuth       = 0.0F;
    private float              deltaTangage       = 0.0F;
    private Actor              victim             = null;
    private float              oldDeltaAzimuth;
    private float              oldDeltaTangage;
    private static final float AIM9_TURN_MAX      = 12F; // °/s limit
    private static final float AIM9_TURN_DIFF_MAX = AIM9_TURN_MAX * 0.1F;

    private Loc                angleActorLoc      = new Loc();
    private Loc                distanceActorLoc   = new Loc();
    private Point3d            angleActorPos      = new Point3d();
    private Point3d            distanceActorPos   = new Point3d();
    private Point3d            distanceTargetPos  = new Point3d();
    private Point3d            angleTargetPos     = new Point3d();
    private Vector3d           angleTargRayDir    = new Vector3d();
    private Vector3d           angleNoseDir       = new Vector3d();

    private double distanceBetween(Actor actorFrom, Actor actorTo) {
        double distanceRetVal = 99999.999D;
        if (!Actor.isValid(actorFrom) || !Actor.isValid(actorTo)) return distanceRetVal;
        actorFrom.pos.getAbs(this.distanceActorLoc);
        this.distanceActorLoc.get(this.distanceActorPos);
        actorTo.pos.getAbs(this.distanceTargetPos);
        distanceRetVal = this.distanceActorPos.distance(this.distanceTargetPos);
        return distanceRetVal;
    }

    private float angleActorBetween(Actor actorFrom, Actor actorTo) {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        actorFrom.pos.getAbs(this.angleActorLoc);
        this.angleActorLoc.get(this.angleActorPos);
        actorTo.pos.getAbs(this.angleTargetPos);
        this.angleTargRayDir.sub(this.angleTargetPos, this.angleActorPos);
        angleDoubleTemp = this.angleTargRayDir.length();
        this.angleTargRayDir.scale(1.0D / angleDoubleTemp);
        this.angleNoseDir.set(1.0D, 0.0D, 0.0D);
        this.angleActorLoc.transform(this.angleNoseDir);
        angleDoubleTemp = this.angleNoseDir.dot(this.angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (Time.current() < this.tStart + 1000L) return;
        super.msgCollision(actor, s, s1);
    }

    public boolean interpolateStep() {
        float maxSpeed = 320F;
        if (this.victim != null) if (!Actor.isValid(this.victim) || !(this.victim instanceof Aircraft)) {
            this.victim = null;
            this.tDestroy = Time.current() + 10000L + TrueRandom.nextLong(10000L);
        } else maxSpeed = (float) this.victim.getSpeed((Vector3d) null) * 1.7F;
        float f = Time.tickLenFs();
        float f1 = (float) this.getSpeed((Vector3d) null);
        if (Time.current() > this.tStart + 1000L) this.collide(true);
        f1 += (maxSpeed - f1) * 0.1F * f;
        this.pos.getAbs(p, or);
        this.v.set(1.0D, 0.0D, 0.0D);
        or.transform(this.v);
        this.v.scale(f1);
        this.setSpeed(this.v);
        p.x += this.v.x * f;
        p.y += this.v.y * f;
        p.z += this.v.z * f;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(p, or);
            return false;
        }

        if (this.victim != null) {
            if (this.victim instanceof Aircraft) this.pT = this.victim.pos.getAbsPoint();
            else this.victim.pos.getAbs(this.pT, this.orVictimOffset);
            this.orVictimOffset = this.victim.pos.getAbsOrient();

            // Calculate future victim position

            this.victim.getSpeed(this.victimSpeed); // target movement vector
            double victimDistance = this.distanceBetween(this, this.victim); // distance missile -> target
            double theVictimSpeed = this.victimSpeed.length(); // target speed
            double speedRel = 320D / theVictimSpeed; // relation missile speed / target speed
            double gamma = this.angleActorBetween(this.victim, this); // angle offset missile vector -> target vector
            double alpha = Geom.RAD2DEG((float) Math.asin(Math.sin(Geom.DEG2RAD((float) gamma)) / speedRel));
            double beta = 180.0D - gamma - alpha; // angle offset missile vector -> target interception path vector
            double victimAdvance = victimDistance * Math.sin(Geom.DEG2RAD((float) alpha)) / Math.sin(Geom.DEG2RAD((float) beta)); // track made good by target until impact
            victimAdvance -= 5.0D; // impact point 10m aft of engine (track exhaust).
            double timeToTarget = victimAdvance / theVictimSpeed; // time until calculated impact

            this.victimSpeed.scale(timeToTarget);
            this.pT.add(this.victimSpeed);
            this.v.set(this.pVictimOffset); // take victim Engine Offset into account
            or.transform(this.v); // take victim Engine Offset into account
            this.pT.add(this.v); // take victim Engine Offset into account

            this.pT.sub(p); // relative Position to target
            or.transformInv(this.pT); // set coordinate system according to A/C POV

            // Calculate angle to target.
            // This is required in order to respect the IR Seeker FOV.
            double angleAzimuth = Math.toDegrees(Math.atan(this.pT.y / this.pT.x));
            double angleTangage = Math.toDegrees(Math.atan(this.pT.z / this.pT.x));
            float turnStepMax = AIM9_TURN_MAX * f; // turn limit, results in 12G accelleration at Mach 1.7
            float turnDiffMax = AIM9_TURN_DIFF_MAX * f; // turn rate change limit, smoothen the turns.

            if (this.pT.x > -10D) { // don't track if target has been passed
                this.deltaAzimuth = (float) -angleAzimuth;
                this.deltaTangage = (float) angleTangage;
            } else {
                this.deltaAzimuth = (float) angleAzimuth;
                this.deltaTangage = (float) -angleTangage;
            }
            if (this.deltaAzimuth > turnStepMax) this.deltaAzimuth = turnStepMax; // limit turn
            if (this.deltaAzimuth < -turnStepMax) this.deltaAzimuth = -turnStepMax; // limit turn
            if (this.deltaTangage > turnStepMax) this.deltaTangage = turnStepMax; // limit turn
            if (this.deltaTangage < -turnStepMax) this.deltaTangage = -turnStepMax; // limit turn
            if (Math.abs(this.oldDeltaAzimuth - this.deltaAzimuth) > turnDiffMax) if (this.oldDeltaAzimuth < this.deltaAzimuth) this.deltaAzimuth = this.oldDeltaAzimuth + turnDiffMax;
            else this.deltaAzimuth = this.oldDeltaAzimuth - turnDiffMax;
            if (Math.abs(this.oldDeltaTangage - this.deltaTangage) > turnDiffMax) if (this.oldDeltaTangage < this.deltaTangage) this.deltaTangage = this.oldDeltaTangage + turnDiffMax;
            else this.deltaTangage = this.oldDeltaTangage - turnDiffMax;
            this.oldDeltaAzimuth = this.deltaAzimuth;
            this.oldDeltaTangage = this.deltaTangage;
            or.increment(this.deltaAzimuth, this.deltaTangage, 0.0F);
            or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        } else or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        this.deltaAzimuth = this.deltaTangage = 0.0F;
        this.pos.setAbs(p, or);
        if (Time.current() > this.tStart + 500L) if (Actor.isValid(this.victim)) {
            float f2 = (float) p.distance(this.victim.pos.getAbsPoint());
            if (this.victim instanceof Aircraft && f2 > this.prevd && this.prevd != 1000F) if (f2 < 10F) {
                this.doExplosionAir();
                this.postDestroy();
                this.collide(false);
                this.drawing(false);
                return false;
            }
            this.prevd = f2;
        } else this.prevd = 1000F;
        if (this.tDestroy != 0L && Time.current() > this.tDestroy) {
            this.doExplosionAir();
            this.postDestroy();
            this.collide(false);
            this.drawing(false);
            return false;
        } else return false;
    }

    public RocketSchlort() {
        this.tStart = 0L;
        this.prevd = 1000F;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.pT = new Point3d();
        this.v = new Vector3d();
        this.victimSpeed = new Vector3d();
        this.oldDeltaAzimuth = 0.0F;
        this.oldDeltaTangage = 0.0F;
        this.orVictimOffset = new Orient();
        this.pVictimOffset = new Point3f();
    }

    public RocketSchlort(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.tStart = 0L;
        this.prevd = 1000F;
        this.pT = new Point3d();
        this.v = new Vector3d();
        this.victimSpeed = new Vector3d();
        this.orVictimOffset = new Orient();
        this.pVictimOffset = new Point3f();
        this.oldDeltaAzimuth = 0.0F;
        this.oldDeltaTangage = 0.0F;
        this.net = new Mirror(this, netchannel, i);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, null, true);
        this.doStart(-1F);
        this.v.set(1.0D, 0.0D, 0.0D);
        orient.transform(this.v);
        this.v.scale(f);
        this.setSpeed(this.v);
        this.collide(false);
    }

    public void start(float f) {
        Actor actor = this.pos.base();
        try {
            if (Actor.isValid(actor) && actor instanceof Aircraft) {
                if (actor.isNetMirror()) {
                    this.destroy();
                    return;
                }
                this.net = new Master(this);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            this.destroy();
        }
        this.collide(false);
        this.doStart(f);
    }

    private void doStart(float f) {
        super.start(-1F);
        this.tStart = Time.current();
        if (Config.isUSE_RENDER()) {
            this.flame.drawing(true);
            ((ActorSimpleMesh) this.flame).mesh().setScale(0.1F);
        }
        if (this.isNet() && this.isNetMirror()) return;
        float offsetDist = 5.0F;
        double rnd1 = TrueRandom.nextDouble(Math.PI);
        double rnd2 = TrueRandom.nextDouble(Math.PI * 2D);
        this.pVictimOffset.set(offsetDist * (float) Math.sin(rnd1) * (float) Math.cos(rnd2), offsetDist * (float) Math.sin(rnd1) * (float) Math.sin(rnd2), offsetDist * (float) Math.cos(rnd1));
    }

    public void setVictim(Actor victim) {
        this.victim = victim;
    }

    public void destroy() {
        if (Config.isUSE_RENDER()) {
            Eff3DActor.finish(this.fl1);
            Eff3DActor.finish(this.fl2);
        }
        this.tStart = 0L;
        this.prevd = 1000F;
        super.destroy();
    }

    protected void doExplosion(Actor actor, String s) {
        this.pos.getTime(Time.current(), p);
        MsgExplosion.send(actor, s, p, this.getOwner(), 0F, 0.0001F, 1, 1F);
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir() {
        this.pos.getTime(Time.current(), p);
        MsgExplosion.send(null, null, p, this.getOwner(), 5F, 0.01F, 1, 10F);
        super.doExplosionAir();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(this.getOwner().net);
        Point3d point3d = this.pos.getAbsPoint();
        netmsgspawn.writeFloat((float) point3d.x);
        netmsgspawn.writeFloat((float) point3d.y);
        netmsgspawn.writeFloat((float) point3d.z);
        Orient orient = this.pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        float f = (float) this.getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    static {
        Class class1 = RocketSchlort.class;
        Property.set(class1, "mesh", "3DO/Arms/Schlort/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Schlort/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Schlort/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Schlort/rocketsmokewhite.eff");
        Property.set(class1, "smokeStart", "3DO/Effects/Schlort/rocketsmokewhitestart.eff");
        Property.set(class1, "smokeTile", "3DO/Effects/Schlort/rocketsmokewhitetile.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "sound", "weapon.schlong");
        Property.set(class1, "radius", 1F);
        Property.set(class1, "timeLife", 1000000F);
        Property.set(class1, "timeFire", 1000F);
        Property.set(class1, "force", 1000F);
        Property.set(class1, "power", 0.0000001F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 1.049F);
        Property.set(class1, "massa", 60F);
        Property.set(class1, "massaEnd", 45F);
        Spawn.add(class1, new SPAWN());
    }

    static class SPAWN implements NetSpawn {

        public void netSpawn(int i, NetMsgInput netmsginput) {
            NetObj netobj;
            netobj = netmsginput.readNetObj();
            if (netobj == null) return;
            try {
                Actor actor = (Actor) netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
                float f = netmsginput.readFloat();
                new RocketSchlort(actor, netmsginput.channel(), i, point3d, orient, f);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
            return;
        }

        SPAWN() {
        }
    }

    class Mirror extends ActorNet {

        public void msgNetNewChannel(NetChannel netchannel) {
            if (!Actor.isValid(this.actor())) return;
            if (netchannel.isMirrored(this)) return;
            try {
                if (netchannel.userState == 0) {
                    NetMsgSpawn netmsgspawn = this.actor().netReplicate(netchannel);
                    if (netmsgspawn != null) {
                        this.postTo(netchannel, netmsgspawn);
                        this.actor().netFirstUpdate(netchannel);
                    }
                }
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
            return;
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) return false;
            if (this.isMirrored()) {
                this.out.unLockAndSet(netmsginput, 0);
                this.postReal(Message.currentTime(true), this.out);
            }
            RocketSchlort.p.x = netmsginput.readFloat();
            RocketSchlort.p.y = netmsginput.readFloat();
            RocketSchlort.p.z = netmsginput.readFloat();
            int i = netmsginput.readShort();
            int j = netmsginput.readShort();
            float f = -(i * 180F / 32000F);
            float f1 = j * 90F / 32000F;
            RocketSchlort.or.set(f, f1, 0.0F);
            RocketSchlort.this.pos.setAbs(RocketSchlort.p, RocketSchlort.or);
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet implements NetUpdate {

        public void msgNetNewChannel(NetChannel netchannel) {
            if (!Actor.isValid(this.actor())) return;
            if (netchannel.isMirrored(this)) return;
            try {
                if (netchannel.userState == 0) {
                    NetMsgSpawn netmsgspawn = this.actor().netReplicate(netchannel);
                    if (netmsgspawn != null) {
                        this.postTo(netchannel, netmsgspawn);
                        this.actor().netFirstUpdate(netchannel);
                    }
                }
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
            return;
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return false;
        }

        public void netUpdate() {
            try {
                this.out.unLockAndClear();
                RocketSchlort.this.pos.getAbs(RocketSchlort.p, RocketSchlort.or);
                this.out.writeFloat((float) RocketSchlort.p.x);
                this.out.writeFloat((float) RocketSchlort.p.y);
                this.out.writeFloat((float) RocketSchlort.p.z);
                RocketSchlort.or.wrap();
                int i = (int) (RocketSchlort.or.getYaw() * 32000F / 180F);
                int j = (int) (RocketSchlort.or.tangage() * 32000F / 90F);
                this.out.writeShort(i);
                this.out.writeShort(j);
                this.post(Time.current(), this.out);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;

        public Master(Actor actor) {
            super(actor);
            this.out = new NetMsgFiltered();
        }
    }
}
