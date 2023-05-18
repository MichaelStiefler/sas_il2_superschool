package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketX4homing extends RocketX4 {

    public boolean interpolateStep() {
        float f = Time.tickLenFs();
        float f1 = (float) this.getSpeed((Vector3d) null);
        f1 += (320F - f1) * 0.1F * f;
        this.pos.getAbs(RocketX4homing.p, RocketX4homing.or);
        RocketX4homing.v.set(1.0D, 0.0D, 0.0D);
        RocketX4homing.or.transform(RocketX4homing.v);
        RocketX4homing.v.scale(f1);
        this.setSpeed(RocketX4homing.v);
        RocketX4homing.p.x += RocketX4homing.v.x * f;
        RocketX4homing.p.y += RocketX4homing.v.y * f;
        RocketX4homing.p.z += RocketX4homing.v.z * f;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(RocketX4homing.p, RocketX4homing.or);
            return false;
        }
        if (Time.current() > (this.tStart + 350L)) {
            if (!Actor.isValid(this.victim)) {
                if ((this.victim = NearestTargets.getEnemy(9, -1, RocketX4homing.p, 5000D, this.getOwner().getArmy())) == null) {
                    this.victim = NearestTargets.getEnemy(8, -1, RocketX4homing.p, 10000D, this.getOwner().getArmy());
                }
            } else {
                this.victim.pos.getAbs(RocketX4homing.pT);
                RocketX4homing.pT.sub(RocketX4homing.p);
                RocketX4homing.or.transformInv(RocketX4homing.pT);
                if (RocketX4homing.pT.x > 0.0D) {
                    RocketX4homing.pT.y += (RocketX4homing.v.y * RocketX4homing.pT.x) / 666.666D;
                    RocketX4homing.pT.z += (RocketX4homing.v.z * RocketX4homing.pT.x) / 666.666D;
                    if (RocketX4homing.pT.y > 0.1D) {
                        if (this.lastAdjSide <=0 || this.prevAdjSide + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjSide = -1;
                            this.prevAdjSide = Time.current();
                            this.deltaAzimuth = -1F;
                        }
                    }
                    if (RocketX4homing.pT.y < -0.1D) {
                        if (this.lastAdjSide >=0 || this.prevAdjSide + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjSide = 1;
                            this.prevAdjSide = Time.current();
                            this.deltaAzimuth = 1F;
                       }
                    }
                    if (RocketX4homing.pT.z < -0.1D) {
                        if (this.lastAdjAttitude <=0 || this.prevAdjAttitude + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjAttitude = -1;
                            this.prevAdjAttitude = Time.current();
                            this.deltaTangage = -1F;
                        }
                    }
                    if (RocketX4homing.pT.z > 0.1D) {
                        if (this.lastAdjAttitude >=0 || this.prevAdjAttitude + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjAttitude = 1;
                            this.prevAdjAttitude = Time.current();
                            this.deltaTangage = 1F;
                        }
                    }
                    RocketX4homing.or.increment(50F * f * this.deltaAzimuth, 50F * f * this.deltaTangage, 0.0F);
                    this.deltaAzimuth = this.deltaTangage = 0.0F;
                } else if (RocketX4homing.p.distance(this.victim.pos.getAbsPoint()) > 30D) {
                    this.victim = null;
                } else {
                    if (this.victim instanceof Aircraft) {
                        Point3d proximityFuseDetonationPoint = Fuze_Proximity.checkBlowProximityFuze(this, this.victim, RocketX4homing.p, this.lastPos, 30D);
                        if (proximityFuseDetonationPoint != null) {
                            this.pos.setAbs(proximityFuseDetonationPoint, RocketX4homing.or);
                            this.doExplosionAir();
                            this.postDestroy();
                            this.collide(false);
                            this.drawing(false);
                        }
                    }
//                    this.pos.setAbs(RocketX4homing.p, RocketX4homing.or);
//                    this.doExplosionAir();
//                    this.postDestroy();
//                    this.collide(false);
//                    this.drawing(false);
//                    return false;
                }
            }
            this.lastPos.set(RocketX4homing.p);
        }
        this.pos.setAbs(RocketX4homing.p, RocketX4homing.or);
        if (!Actor.isValid(this.getOwner()) || !(this.getOwner() instanceof Aircraft)) {
            this.doExplosionAir();
            this.postDestroy();
            this.collide(false);
            this.drawing(false);
        }
        return false;
    }

    public RocketX4homing() {
        this.victim = null;
        this.fm = null;
        this.tStart = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.victim = null;
    }

    public RocketX4homing(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.victim = null;
        this.fm = null;
        this.tStart = 0L;
        this.net = new RocketX4.Mirror(this, netchannel, i);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, (Hook) null, true);
        this.doStart(-1F);
        RocketX4homing.v.set(1.0D, 0.0D, 0.0D);
        orient.transform(RocketX4homing.v);
        RocketX4homing.v.scale(f);
        this.setSpeed(RocketX4homing.v);
        this.collide(false);
    }

    public void start(float f, int i) {
        Actor actor = this.pos.base();
        if (Actor.isValid(actor) && (actor instanceof Aircraft)) {
            if (actor.isNetMirror()) {
                this.destroy();
                return;
            }
            this.net = new RocketX4.Master(this);
        }
        this.doStart(f);
    }

    private void doStart(float f) {
        super.start(-1F, 0);
        this.fm = ((Aircraft) this.getOwner()).FM;
        this.tStart = Time.current();
        if (Config.isUSE_RENDER()) {
            this.flame.drawing(true);
        }
        this.pos.getAbs(RocketX4homing.p, RocketX4homing.or);
        RocketX4homing.or.setYPR(RocketX4homing.or.getYaw(), RocketX4homing.or.getPitch(), 0.0F);
        this.pos.setAbs(RocketX4homing.p, RocketX4homing.or);
        if (this.isNet() && this.isNetMirror()) {
            return;
        }
        if ((this.getOwner() == World.getPlayerAircraft()) && ((RealFlightModel) this.fm).isRealMode()) {
            this.victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
        }
        this.lastPos.set(RocketX4homing.p);
    }

    protected void doExplosion(Actor actor, String s) {
        this.pos.getTime(Time.current(), RocketX4homing.p);
        MsgExplosion.send(actor, s, RocketX4homing.p, this.getOwner(), 45F, 1.0F, 1, 400F);
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir() {
        this.pos.getTime(Time.current(), RocketX4homing.p);
        MsgExplosion.send((Actor) null, (String) null, RocketX4homing.p, this.getOwner(), 45F, 1.0F, 1, 400F);
        super.doExplosionAir();
    }

    private FlightModel     fm;
    private static Orient   or = new Orient();
    private static Point3d  p  = new Point3d();
    private static Point3d  pT = new Point3d();
    private static Vector3d v  = new Vector3d();
    private long            tStart;
    private float           deltaAzimuth;
    private float           deltaTangage;
    private Actor           victim;
    private Point3d         lastPos = new Point3d();
    private int             lastAdjSide = 0;
    private int             lastAdjAttitude = 0;
    private long            prevAdjSide = 0;
    private long            prevAdjAttitude = 0;
    private static final long MIN_DIRECTION_CHANGE_TIMEOUT = 1000L;

    static {
        Class class1 = RocketX4homing.class;
        Property.set(class1, "timeFire", 2.5F);
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 1.0F);
        Spawn.add(class1, new RocketX4.SPAWN());
    }
}
