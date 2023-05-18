package com.maddox.il2.objects.weapons;

import java.io.IOException;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
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

public class RocketX4 extends Rocket {
    static class SPAWN implements NetSpawn {

        public void netSpawn(int i, NetMsgInput netmsginput) {
            try {
                NetObj netobj = netmsginput.readNetObj();
                if (netobj == null) {
                    return;
                }
                Actor actor = (Actor) netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
                float f = netmsginput.readFloat();
                new RocketX4(actor, netmsginput.channel(), i, point3d, orient, f);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        SPAWN() {
        }
    }

    class Mirror extends ActorNet {

        public void msgNetNewChannel(NetChannel netchannel) {
            if (!Actor.isValid(this.actor())) {
                return;
            }
            try {
                if (netchannel.isMirrored(this)) {
                    return;
                }
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
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                return false;
            }
            if (this.isMirrored()) {
                this.out.unLockAndSet(netmsginput, 0);
                this.postReal(Message.currentTime(true), this.out);
            }
            RocketX4.p.x = netmsginput.readFloat();
            RocketX4.p.y = netmsginput.readFloat();
            RocketX4.p.z = netmsginput.readFloat();
            int i = netmsginput.readShort();
            int j = netmsginput.readShort();
            float f = -((i * 180F) / 32000F);
            float f1 = (j * 90F) / 32000F;
            RocketX4.or.set(f, f1, 0.0F);
            RocketX4.this.pos.setAbs(RocketX4.p, RocketX4.or);
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
            if (!Actor.isValid(this.actor())) {
                return;
            }
            try {
                if (netchannel.isMirrored(this)) {
                    return;
                }
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
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return false;
        }

        public void netUpdate() {
            try {
                this.out.unLockAndClear();
                RocketX4.this.pos.getAbs(RocketX4.p, RocketX4.or);
                this.out.writeFloat((float) RocketX4.p.x);
                this.out.writeFloat((float) RocketX4.p.y);
                this.out.writeFloat((float) RocketX4.p.z);
                RocketX4.or.wrap();
                int i = (int) ((RocketX4.or.getYaw() * 32000F) / 180F);
                int j = (int) ((RocketX4.or.tangage() * 32000F) / 90F);
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

    public boolean interpolateStep() {
        float f = Time.tickLenFs();
        float f1 = (float) this.getSpeed(null);
        f1 += (320F - f1) * 0.1F * f;
        this.pos.getAbs(RocketX4.p, RocketX4.or);
        RocketX4.v.set(1.0D, 0.0D, 0.0D);
        RocketX4.or.transform(RocketX4.v);
        RocketX4.v.scale(f1);
        this.setSpeed(RocketX4.v);
        RocketX4.p.x += RocketX4.v.x * f;
        RocketX4.p.y += RocketX4.v.y * f;
        RocketX4.p.z += RocketX4.v.z * f;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(RocketX4.p, RocketX4.or);
            return false;
        }
        if (Actor.isValid(this.getOwner())) {
            if (((this.getOwner() != World.getPlayerAircraft()) || !((RealFlightModel) this.fm).isRealMode()) && (this.fm instanceof Pilot)) {
                Pilot pilot = (Pilot) this.fm;
                if (pilot.target != null) {
                    pilot.target.Loc.get(RocketX4.pT);
                    RocketX4.pT.sub(RocketX4.p);
                    RocketX4.or.transformInv(RocketX4.pT);
                    if (RocketX4.pT.x > -10D) {
                        double d = Aircraft.cvt(this.fm.Skill, 0.0F, 3F, 15F, 0.0F);
                        if (RocketX4.pT.y > d) {
                            if (this.lastAdjSide <=0 || this.prevAdjSide + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                                this.lastAdjSide = -1;
                                this.prevAdjSide = Time.current();
                                ((TypeX4Carrier) this.fm.actor).typeX4CAdjSideMinus();
                            }
                        }
                        if (RocketX4.pT.y < -d) {
                            if (this.lastAdjSide >=0 || this.prevAdjSide + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                                this.lastAdjSide = 1;
                                this.prevAdjSide = Time.current();
                                ((TypeX4Carrier) this.fm.actor).typeX4CAdjSidePlus();
                            }
                        }
                        if (RocketX4.pT.z < -d) {
                            if (this.lastAdjAttitude <=0 || this.prevAdjAttitude + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                                this.lastAdjAttitude = -1;
                                this.prevAdjAttitude = Time.current();
                                ((TypeX4Carrier) this.fm.actor).typeX4CAdjAttitudeMinus();
                            }
                        }
                        if (RocketX4.pT.z > d) {
                            if (this.lastAdjAttitude >=0 || this.prevAdjAttitude + MIN_DIRECTION_CHANGE_TIMEOUT < Time.current()) {
                                this.lastAdjAttitude = 1;
                                this.prevAdjAttitude = Time.current();
                                ((TypeX4Carrier) this.fm.actor).typeX4CAdjAttitudePlus();
                            }
                        }
                    }
                }
            }
            RocketX4.or.increment(50F * f * ((TypeX4Carrier) this.fm.actor).typeX4CgetdeltaAzimuth(), 50F * f * ((TypeX4Carrier) this.fm.actor).typeX4CgetdeltaTangage(), 0.0F);
            RocketX4.or.setYPR(RocketX4.or.getYaw(), RocketX4.or.getPitch(), 0.0F);
            ((TypeX4Carrier) this.fm.actor).typeX4CResetControls();
        }
        this.pos.setAbs(RocketX4.p, RocketX4.or);
        if (Time.current() > (this.tStart + 500L)) {
            Actor actor = NearestTargets.getEnemy(0, -1, RocketX4.p, 800D, 0);
            if (Actor.isValid(actor)) {
                float f2 = (float) RocketX4.p.distance(actor.pos.getAbsPoint());
                if (actor instanceof Aircraft && this.prevd != 1000F) {
                    Point3d proximityFuseDetonationPoint = Fuze_Proximity.checkBlowProximityFuze(this, actor, RocketX4.p, this.lastPos, 40D);
                    if (proximityFuseDetonationPoint != null) {
                        this.pos.setAbs(proximityFuseDetonationPoint, RocketX4.or);
                        this.doExplosionAir();
                        this.postDestroy();
                        this.collide(false);
                        this.drawing(false);
                    }
                }
                
//                if ((actor instanceof Aircraft) && ((f2 < 20F) || ((f2 < 40F) && (f2 > this.prevd) && (this.prevd != 1000F)))) {
//                    this.doExplosionAir();
//                    this.postDestroy();
//                    this.collide(false);
//                    this.drawing(false);
//                }
                this.prevd = f2;
            } else {
                this.prevd = 1000F;
            }
            this.lastPos.set(RocketX4.p);
        }
        if (!Actor.isValid(this.getOwner()) || !(this.getOwner() instanceof Aircraft)) {
            this.doExplosionAir();
            this.postDestroy();
            this.collide(false);
            this.drawing(false);
        }
        return false;
    }
   
    public RocketX4() {
        this.fm = null;
        this.tStart = 0L;
        this.prevd = 1000F;
    }

    public RocketX4(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.fm = null;
        this.tStart = 0L;
        this.prevd = 1000F;
        this.net = new Mirror(this, netchannel, i);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, null, true);
        this.doStart(-1F);
        RocketX4.v.set(1.0D, 0.0D, 0.0D);
        orient.transform(RocketX4.v);
        RocketX4.v.scale(f);
        this.setSpeed(RocketX4.v);
        this.collide(false);
    }

    public void start(float f, int i) {
        Actor actor = this.pos.base();
        if (Actor.isValid(actor) && (actor instanceof Aircraft)) {
            if (actor.isNetMirror()) {
                this.destroy();
                return;
            }
            this.net = new Master(this);
        }
        this.doStart(f);
    }

    private void doStart(float f) {
        super.start(-1F, 0);
        this.fm = ((Aircraft) this.getOwner()).FM;
        this.tStart = Time.current();
        if (Config.isUSE_RENDER()) {
            this.fl1 = Eff3DActor.New(this, this.findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            this.fl2 = Eff3DActor.New(this, this.findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
            this.flame.drawing(false);
        }
        this.pos.getAbs(RocketX4.p, RocketX4.or);
        RocketX4.or.setYPR(RocketX4.or.getYaw(), RocketX4.or.getPitch(), 0.0F);
        this.pos.setAbs(RocketX4.p, RocketX4.or);
        this.lastPos.set(RocketX4.p);
    }

    public void destroy() {
        if (this.isNet() && this.isNetMirror()) {
            this.doExplosionAir();
        }
        if (Config.isUSE_RENDER()) {
            Eff3DActor.finish(this.fl1);
            Eff3DActor.finish(this.fl2);
        }
        super.destroy();
    }

    protected void doExplosion(Actor actor, String s) {
        this.pos.getTime(Time.current(), RocketX4.p);
        MsgExplosion.send(actor, s, RocketX4.p, this.getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir() {
        this.pos.getTime(Time.current(), RocketX4.p);
        MsgExplosion.send(null, null, RocketX4.p, this.getOwner(), 45F, 2.0F, 1, 550F);
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

    private FlightModel     fm;
    private Eff3DActor      fl1;
    private Eff3DActor      fl2;
    private static Orient   or = new Orient();
    private static Point3d  p  = new Point3d();
    private static Point3d  pT = new Point3d();
    private static Vector3d v  = new Vector3d();
    private long            tStart;
    private float           prevd;
    private Point3d         lastPos = new Point3d();
    private int             lastAdjSide = 0;
    private int             lastAdjAttitude = 0;
    private long            prevAdjSide = 0;
    private long            prevAdjAttitude = 0;
    private static final long MIN_DIRECTION_CHANGE_TIMEOUT = 1000L;

    static {
        Class class1 = RocketX4.class;
        Property.set(class1, "mesh", "3do/arms/X-4/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "timeLife", 30F);
        Property.set(class1, "timeFire", 33F);
        Property.set(class1, "force", 15712F);
        Property.set(class1, "power", 2.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.22F);
        Property.set(class1, "massa", 60F);
        Property.set(class1, "massaEnd", 45F);
        Spawn.add(class1, new SPAWN());
    }

}
