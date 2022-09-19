package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketX4R extends RocketX4 {

    public boolean interpolateStep() {
        float f = Time.tickLenFs();
        float f1 = (float) this.getSpeed((Vector3d) null);
        f1 += (320F - f1) * 0.1F * f;
        this.pos.getAbs(RocketX4R.p, RocketX4R.or);
        RocketX4R.v.set(1.0D, 0.0D, 0.0D);
        RocketX4R.or.transform(RocketX4R.v);
        RocketX4R.v.scale(f1);
        this.setSpeed(RocketX4R.v);
        RocketX4R.p.x += RocketX4R.v.x * f;
        RocketX4R.p.y += RocketX4R.v.y * f;
        RocketX4R.p.z += RocketX4R.v.z * f;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(RocketX4R.p, RocketX4R.or);
            return false;
        }
        if (Actor.isValid(this.getOwner())) {
            if (((this.getOwner() != World.getPlayerAircraft()) || !((RealFlightModel) this.fm).isRealMode()) && (this.fm instanceof Pilot)) {
                Pilot pilot = (Pilot) this.fm;
                if (pilot.target != null) {
                    this.victim = pilot.target.actor;
                } else {
                    this.victim = null;
                }
            } else if ((this.victim = Main3D.cur3D().getViewPadlockEnemy()) == null) {
                this.victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
            }
            if (this.victim != null) {
                this.victim.pos.getAbs(RocketX4R.pT);
                RocketX4R.pT.sub(RocketX4R.p);
                RocketX4R.or.transformInv(RocketX4R.pT);
                if (RocketX4R.pT.x > -10D) {
                    if (RocketX4R.pT.y > 0.1D) {
                        if (this.lastAdjSide <=0 || this.prevAdjSide + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjSide = -1;
                            this.prevAdjSide = Time.current();
                            this.deltaAzimuth = -1F;
                        }
                    }
                    if (RocketX4R.pT.y < -0.1D) {
                        if (this.lastAdjSide >=0 || this.prevAdjSide + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjSide = 1;
                            this.prevAdjSide = Time.current();
                            this.deltaAzimuth = 1F;
                       }
                    }
                    if (RocketX4R.pT.z < -0.1D) {
                        if (this.lastAdjAttitude <=0 || this.prevAdjAttitude + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjAttitude = -1;
                            this.prevAdjAttitude = Time.current();
                            this.deltaTangage = -1F;
                        }
                    }
                    if (RocketX4R.pT.z > 0.1D) {
                        if (this.lastAdjAttitude >=0 || this.prevAdjAttitude + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                            this.lastAdjAttitude = 1;
                            this.prevAdjAttitude = Time.current();
                            this.deltaTangage = 1F;
                        }
                    }
                    RocketX4R.or.increment(50F * f * this.deltaAzimuth, 50F * f * this.deltaTangage, 0.0F);
                    this.deltaAzimuth = this.deltaTangage = 0.0F;
                }
            }
        }
        this.pos.setAbs(RocketX4R.p, RocketX4R.or);
        if (Time.current() > (this.tStart + 500L)) {
            if (Actor.isValid(this.victim)) {
                float f2 = (float) RocketX4R.p.distance(this.victim.pos.getAbsPoint());

                if ((this.victim instanceof Aircraft) && (this.prevd != 1000F)) {
                    Point3d proximityFuseDetonationPoint = Fuze_Proximity.checkBlowProximityFuze(this, this.victim, RocketX4R.p, this.lastPos, 30D);
                    if (proximityFuseDetonationPoint != null) {
                        this.pos.setAbs(proximityFuseDetonationPoint, RocketX4R.or);
                        this.doExplosionAir();
                        this.postDestroy();
                        this.collide(false);
                        this.drawing(false);
                    }
                }
//                if((victim instanceof Aircraft) && f2 > prevd && prevd != 1000F && f2 < 30F)
//                {
//                    doExplosionAir();
//                    postDestroy();
//                    collide(false);
//                    drawing(false);
//                }
                this.prevd = f2;
            } else {
                this.prevd = 1000F;
            }
            this.lastPos.set(RocketX4R.p);
        }
        if (!Actor.isValid(this.getOwner()) || !(this.getOwner() instanceof Aircraft)) {
            this.doExplosionAir();
            this.postDestroy();
            this.collide(false);
            this.drawing(false);
        }
        return false;
    }

    public RocketX4R() {
        this.victim = null;
        this.fm = null;
        this.tStart = 0L;
        this.prevd = 1000F;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.victim = null;
    }

    public RocketX4R(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.victim = null;
        this.fm = null;
        this.tStart = 0L;
        this.prevd = 1000F;
        this.net = new RocketX4.Mirror(this, netchannel, i);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, (Hook) null, true);
        this.doStart(-1F);
        RocketX4R.v.set(1.0D, 0.0D, 0.0D);
        orient.transform(RocketX4R.v);
        RocketX4R.v.scale(f);
        this.setSpeed(RocketX4R.v);
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
        this.pos.getAbs(RocketX4R.p, RocketX4R.or);
        RocketX4R.or.setYPR(RocketX4R.or.getYaw(), RocketX4R.or.getPitch(), 0.0F);
        this.pos.setAbs(RocketX4R.p, RocketX4R.or);
        this.lastPos.set(RocketX4R.p);
    }

    private FlightModel     fm;
    private static Orient   or      = new Orient();
    private static Point3d  p       = new Point3d();
    private static Point3d  pT      = new Point3d();
    private static Vector3d v       = new Vector3d();
    private long            tStart;
    private float           prevd;
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
        Class class1 = RocketX4R.class;
        Spawn.add(class1, new RocketX4.SPAWN());
    }
}
