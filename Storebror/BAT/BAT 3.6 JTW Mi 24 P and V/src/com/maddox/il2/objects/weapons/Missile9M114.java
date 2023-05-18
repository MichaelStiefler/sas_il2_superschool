package com.maddox.il2.objects.weapons;

import java.util.List;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Mi24X;
import com.maddox.il2.objects.air.TypeLaserSpotter;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class Missile9M114 extends Missile {
    static class SPAWN extends Missile.SPAWN {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new Missile9M114(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN() {
        }
    }

    public boolean interpolateStep() {
        float f1 = Time.tickLenFs();
        float f2 = (float) this.getSpeed(null);
        f2 += (320F - f2) * 70.1F * f1;
        this.pos.getAbs(Missile9M114.p, Missile9M114.or);
        Missile9M114.v.set(1.0D, 0.0D, 0.0D);
        Missile9M114.or.transform(Missile9M114.v);
        Missile9M114.v.scale(f2);
        this.setSpeed(Missile9M114.v);
        Missile9M114.p.x += Missile9M114.v.x * f1;
        Missile9M114.p.y += Missile9M114.v.y * f1;
        Missile9M114.p.z += Missile9M114.v.z * f1;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(Missile9M114.p, Missile9M114.or);
            return false;
        }
        if (Actor.isValid(this.getOwner())) {
            if (((this.getOwner() != World.getPlayerAircraft()) || !((RealFlightModel) this.fm).isRealMode()) && (this.fm instanceof Pilot)) {
                Pilot localPilot = (Pilot) this.fm;
                if (((Maneuver) (localPilot)).target_ground != null) {
                    this.victim = ((Maneuver) (localPilot)).target_ground;
                } else if (((Maneuver) (localPilot)).target != null) {
                    this.victim = ((Interpolate) (((Maneuver) (localPilot)).target)).actor;
                } else if (this.getOwner() instanceof Mi24X) {
                    this.victim = ((Mi24X) this.getOwner()).victim;
                } else {
                    this.victim = null;
                }
            } else {
                this.checklaser();
                if (!this.laseron) {
                    if (this.victim.isDestroyed()) {
                        this.victim = null;
                    } else {
                        this.victim.pos.getAbs(Missile9M114.pT);
                        Missile9M114.pT.sub(Missile9M114.p);
                        Missile9M114.or.transformInv(Missile9M114.pT);
                        if ((((Tuple3d) (Missile9M114.pT)).y > (((Tuple3d) (Missile9M114.pT)).x / 4D)) || (Missile9M114.pT.y < (-Missile9M114.pT.x / 4D)) || (Missile9M114.pT.z > (Missile9M114.pT.x / 4D)) || (Missile9M114.pT.z < (-Missile9M114.pT.x / 4D))) {
                            this.victim = null;
                        }
                    }
                }
                if ((Time.current() > (this.tStart + 50000L)) && (this.getOwner() instanceof TypeX4Carrier) && (((TypeX4Carrier) ((Interpolate) (this.fm)).actor).typeX4CgetdeltaAzimuth() != ((TypeX4Carrier) ((Interpolate) (this.fm)).actor).typeX4CgetdeltaTangage())) {
                    ((TypeX4Carrier) ((Interpolate) (this.fm)).actor).typeX4CResetControls();
                    this.doExplosionAir();
                    this.postDestroy();
                    this.collide(false);
                    this.drawing(false);
                }
            }
            if (this.laseron) {
                Missile9M114.pT.sub(Missile9M114.p);
                Missile9M114.or.transformInv(Missile9M114.pT);
                float f3 = 0.1F;
                if (Missile9M114.p.distance(Missile9M114.pT) > 0.0D) {
                    if (Missile9M114.pT.y > 0.1D) {
                        this.deltaAzimuth = -f3;
                    }
                    if (Missile9M114.pT.y < -0.1D) {
                        this.deltaAzimuth = f3;
                    }
                    if (Missile9M114.pT.z < -0.1D) {
                        this.deltaTangage = -f3;
                    }
                    if (Missile9M114.pT.z > 0.1D) {
                        this.deltaTangage = f3;
                    }
                    Missile9M114.or.increment(20F * f3 * this.deltaAzimuth, 20F * f3 * this.deltaTangage, 10550F * f1);
                    this.deltaAzimuth = this.deltaTangage = 0.0F;
                }
            } else {
                Missile9M114.or.increment(0.0F, 0.0F, 10550F * f1);
            }
        }
        this.pos.setAbs(Missile9M114.p, Missile9M114.or);
        return false;
    }

    private void checklaser() {
        this.laseron = false;
        List localList = Engine.targets();
        int i = localList.size();
        for (int j = 0; j < i; j++) {
            Actor localActor = (Actor) localList.get(j);
            if ((localActor instanceof TypeLaserSpotter) && (localActor.pos.getAbsPoint().distance(this.pos.getAbsPoint()) < 20000D) && (localActor == World.getPlayerAircraft())) {
                Point3d localPoint3d = new Point3d();
                localPoint3d = TypeLaserSpotter.spot;
                this.pos.getAbsPoint().distance(localPoint3d);
                Missile9M114.pT.set(localPoint3d);
                this.laseron = true;
            }
        }

    }

    public Missile9M114(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
        this.victim = null;
        this.fm = null;
        this.tStart = 0L;
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, null, true);
        this.doStart(-1F);
        Missile9M114.v.set(1.0D, 0.0D, 0.0D);
        orient.transform(Missile9M114.v);
        Missile9M114.v.scale(f);
        this.setSpeed(Missile9M114.v);
        this.collide(false);
    }

    public Missile9M114() {
        this.victim = null;
        this.fm = null;
        this.tStart = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.victim = null;
    }

    public void start(float f, int i) {
        Actor actor = this.pos.base();
        if (Actor.isValid(actor) && (actor instanceof Aircraft) && actor.isNetMirror()) {
            this.destroy();
            return;
        } else {
            this.doStart(f);
            return;
        }
    }

    private void doStart(float f) {
        this.start(-1F, 0);
        if (this.getOwner() instanceof Aircraft) {
            this.fm = ((Aircraft) this.getOwner()).FM;
        }
        this.tStart = Time.current();
        this.pos.getAbs(Missile9M114.p, Missile9M114.or);
        Missile9M114.or.setYPR(Missile9M114.or.getYaw(), Missile9M114.or.getPitch(), 0.0F);
        this.pos.setAbs(Missile9M114.p, Missile9M114.or);
    }

    private boolean         laseron;
    private FlightModel     fm;
    private static Orient   or = new Orient();
    private static Point3d  p  = new Point3d();
    private static Point3d  pT = new Point3d();
    private static Vector3d v  = new Vector3d();
    private long            tStart;
    private float           deltaAzimuth;
    private float           deltaTangage;
    private Actor           victim;

    static {
        Class class1 = Missile9M114.class;
        Property.set(class1, "mesh", "3DO/Arms/5inchZuni/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 20F);
        Property.set(class1, "timeFire", 5F);
        Property.set(class1, "force", 3530.394F);
        Property.set(class1, "forceT1", 2.0F);
        Property.set(class1, "forceP1", 7453.054F);
        Property.set(class1, "forceT2", 3F);
        Property.set(class1, "forceP2", 3530.394F);
        Property.set(class1, "dragCoefficient", 0.4F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 2.4F);
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "kalibr", 0.13F);
        Property.set(class1, "massa", 31.4F);
        Property.set(class1, "massaEnd", 19.2F);
        Property.set(class1, "stepMode", 1);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 0);
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 0);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", -200L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 272);
        Property.set(class1, "shotFreq", 5.01F);
        Property.set(class1, "groundTrackFactor", 1000F);
        Property.set(class1, "flareLockTime", 50000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 30F);
        Property.set(class1, "maxLockGForce", 2.0F);
        Property.set(class1, "maxFOVfrom", 30F);
        Property.set(class1, "maxFOVto", 360F);
        Property.set(class1, "PkMaxFOVfrom", 35F);
        Property.set(class1, "PkMaxFOVto", 3.402823E+038F);
        Property.set(class1, "PkDistMin", 300F);
        Property.set(class1, "PkDistOpt", 1500F);
        Property.set(class1, "PkDistMax", 5000F);
        Property.set(class1, "leadPercent", 0.0F);
        Property.set(class1, "maxGForce", 12F);
        Property.set(class1, "stepsForFullTurn", 17);
        Property.set(class1, "fxLock", (String) null);
        Property.set(class1, "fxNoLock", (String) null);
        Property.set(class1, "smplLock", (String) null);
        Property.set(class1, "smplNoLock", (String) null);
        Property.set(class1, "friendlyName", "9M114");
        Spawn.add(class1, new SPAWN());
    }
}
