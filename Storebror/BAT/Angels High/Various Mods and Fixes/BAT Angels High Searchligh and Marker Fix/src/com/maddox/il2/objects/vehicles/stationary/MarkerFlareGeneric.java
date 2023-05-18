package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.weapons.RocketFlareBall;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;

public abstract class MarkerFlareGeneric extends ActorHMesh implements ActorAlign, MsgExplosionListener, MsgShotListener {
    public static class SPAWN implements ActorSpawn {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            MarkerFlareGeneric markerflaregeneric = null;
            try {
                MarkerFlareGeneric.constr_arg2 = actorspawnarg;
                markerflaregeneric = (MarkerFlareGeneric) this.cls.newInstance();
                MarkerFlareGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                MarkerFlareGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create MarkerFlareGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return markerflaregeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", MarkerFlareGeneric.mesh_name);
            this.cls = class1;
            com.maddox.rts.Spawn.add(this.cls, this);
        }
    }

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor1, NetChannel netchannel1, int i) {
            super(actor1, netchannel1, i);
            this.out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return true;
        }

        public Master(Actor actor1) {
            super(actor1);
        }
    }

    class Move extends Interpolate {

        public boolean tick() {
            if (MarkerFlareGeneric.this.engineSFX != null) {
                if (MarkerFlareGeneric.this.engineSTimer >= 0) {
                    if (--MarkerFlareGeneric.this.engineSTimer <= 0) {
                        float f = (float) (Math.random() + 1.0D);
                        int i = TrueRandom.nextInt(5);
                        MarkerFlareGeneric.this.engineSTimer = (int) MarkerFlareGeneric.SecsToTicks(MarkerFlareGeneric.Rnd(10 + i + f, 10 + i + f));
                        if (!MarkerFlareGeneric.this.danger()) {
                            MarkerFlareGeneric.this.engineSTimer = -MarkerFlareGeneric.this.engineSTimer;
                        }
                    }
                } else if (++MarkerFlareGeneric.this.engineSTimer >= 0) {
                    MarkerFlareGeneric.this.engineSTimer = -(int) MarkerFlareGeneric.SecsToTicks(MarkerFlareGeneric.Rnd(1.0F, 1.0F));
                    if (MarkerFlareGeneric.this.danger()) {
                        MarkerFlareGeneric.this.engineSTimer = -MarkerFlareGeneric.this.engineSTimer;
                    }
                }
            }
            return true;
        }

        Move() {
        }
    }

    public static final double Rnd(double d, double d1) {
        return TrueRandom.nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return TrueRandom.nextFloat(f, f1);
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5F + (f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    public void destroy() {
        if (this.isDestroyed()) {
            return;
        } else {
            this.engineSFX = null;
            this.engineSTimer = 0xfa0a1f01;
            this.breakSounds();
            super.destroy();
            return;
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public boolean isStaticPos() {
        return true;
    }

    protected MarkerFlareGeneric() {
        this(constr_arg2);
    }

    private MarkerFlareGeneric(ActorSpawnArg actorspawnarg) {
        super(mesh_name);
        this.nextFlareTime = Time.current() + 10000L;
        this.actor = null;
        this.engineSFX = null;
        this.engineSTimer = 9999999;
        actorspawnarg.setStationary(this);
        this.myArmy = this.getArmy();
        this.collide(false);
        this.drawing(true);
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.heightAboveLandSurface = 0.0F;
        this.Align();
        this.startMove();
        this.nearestBornPlace = BornPlace.getCurrentBornPlace(this.pos.getAbsPoint().x, this.pos.getAbsPoint().y);
        if (this.nearestBornPlace == null) {
            System.out.println("No Bornplace close to this Marker Flare!");
        } else {
            System.out.println("Marker Bornplace x=" + this.nearestBornPlace.place.x + ", y=" + this.nearestBornPlace.place.y + ", r=" + this.nearestBornPlace.r);
        }
    }

    public void startMove() {
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
            this.engineSFX = this.newSound("objects.siren", false);
            this.engineSTimer = -(int) SecsToTicks(Rnd(5F, 5F));
        }
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

    private static float angleBetween(Actor angleActor, Point3d targetPoint) {
        float f = 180.10001F;
        double d1 = 0.0D;
        Loc localLoc = new Loc();
        Point3d localPoint3d1 = new Point3d();
        Vector3d localVector3d1 = new Vector3d();
        Vector3d localVector3d2 = new Vector3d();
        angleActor.pos.getAbs(localLoc);
        localLoc.get(localPoint3d1);
        localVector3d1.sub(targetPoint, localPoint3d1);
        d1 = localVector3d1.length();
        localVector3d1.scale(1.0D / d1);
        localVector3d2.set(1.0D, 0.0D, 0.0D);
        localLoc.transform(localVector3d2);
        d1 = localVector3d2.dot(localVector3d1);
        f = Geom.RAD2DEG((float) Math.acos(d1));
        return f;
    }

    private boolean danger() {
        List targetList = Engine.targets();
        boolean incomingEnemy = false;
        boolean incomingFriendly = false;
        boolean waitingForTakeoff = false;

        for (int targetListIndex = 0; targetListIndex < targetList.size(); targetListIndex++) {
            if (incomingEnemy && incomingFriendly && waitingForTakeoff) {
                break;
            }
            Actor target = (Actor) targetList.get(targetListIndex);
            if (target instanceof BridgeSegment) {
                continue; // potential target is a Bridge Segment, we're searching for all kind of actors but this explicitely does not include Bridges!
            }
            int targetArmy = target.getArmy();
            if (targetArmy == 0) {
                continue; // neutrals don't count!
            }

            double targetDistance = this.pos.getAbsPoint().distance(target.pos.getAbsPoint());
            if (targetDistance > World.MaxVisualDistance) {
                continue; // Too far away to be seen!
            }

            if (this.nearestBornPlace != null) {
                this.point3d.set(this.nearestBornPlace.place);
            } else {
                this.point3d.set(this.pos.getAbsPoint());
            }
            if (!incomingEnemy && (targetArmy != this.myArmy)) { // This is an enemy actor...
//                System.out.println("Target " + target.getClass().getName() + " (Hash: " + target.hashCode() + ") distance=" + targetDistance + ", max visual distance=" + World.MaxVisualDistance + ", Angle=" + angleBetween(target, point3d) + " is Enemy");
                if (angleBetween(target, this.point3d) < 100F) {
//                    System.out.println("is incoming");
                    incomingEnemy = true; // Enemy is (potentially) approaching this Marker or it's Homebase!
                }
            } else { // A friendly fellow...
                if ((target instanceof Aircraft) && (!waitingForTakeoff || !incomingFriendly)) { // We are interested in friendly Aircraft only.
//                    System.out.print("Target " + target.getClass().getName() + " (Hash: " + target.hashCode() + ") distance=" + targetDistance + ", max visual distance=" + World.MaxVisualDistance + ", Angle=" + angleBetween(target, point3d) + " is friendly Aircraft");
                    Aircraft friendlyAircraft = (Aircraft) target;
                    if (!waitingForTakeoff && friendlyAircraft.FM.Gears.onGround()) {
//                        System.out.print(", Gears are on Ground");
                        if (friendlyAircraft.FM.getSpeedKMH() < 50F) {
//                            System.out.print(", Speed is less than 50 km/h");
                            if (this.nearestBornPlace != null) {
                                if (BornPlace.isLandedOnHomeBase(friendlyAircraft.FM, this.nearestBornPlace)) {
//                                    System.out.print(", is on Homebase");
                                    waitingForTakeoff = true;
                                }
                            } else {
                                if (this.point3d.distance(friendlyAircraft.pos.getAbsPoint()) < 2000D) { // No Homebase, but close to Marker
//                                    System.out.print(", is close to Marker");
                                    waitingForTakeoff = true;
                                }
                            }
                        }
                    } else if (!incomingFriendly) {
//                        System.out.print(", Gears are not on Ground");
                        float friendlyAircraftDistance = (float) this.point3d.distance(friendlyAircraft.pos.getAbsPoint());
                        float minDistance = 1000F;
                        if (this.nearestBornPlace != null) {
                            minDistance = this.nearestBornPlace.r;
                        }
                        if (friendlyAircraftDistance > minDistance) {
//                            System.out.print(", out of Homebase/Marker radius");
                            if (angleBetween(friendlyAircraft, this.point3d) < 100F) {
//                                System.out.print(", approaching");
                                float friendlyAltitude = (float) (friendlyAircraft.pos.getAbsPoint().z - World.land().HQ(friendlyAircraft.pos.getAbsPoint().x, friendlyAircraft.pos.getAbsPoint().y));
                                if ((friendlyAircraft.FM.getSpeedKMH() > 50F) && (friendlyAltitude > 10F)) {
//                                    System.out.print(", fast and high enough");
                                    incomingFriendly = true; // Friendly Aircraft is (potentially) approaching this Marker or it's Homebase!
                                }
                            }
                        }
                    }
//                    System.out.println();
                }
            }

        }

        this.pos.getAbs(this.point3d, this.orient);
        this.orient.setYPR(this.orient.getYaw(), this.orient.getPitch() + TrueRandom.nextFloat(87F, 90F), this.orient.getRoll() + TrueRandom.nextFloat(-180F, 180F));
        if (Time.current() > this.nextFlareTime) {
            RocketFlareBall rocketflareball = null;
            if (incomingEnemy) {
                rocketflareball = new RocketFlareBall(this, this.point3d, this.orient, RocketFlareBall.FLARE_COLOR_RED);
                this.lastFlareType = RocketFlareBall.FLARE_COLOR_RED;
            }
            if ((rocketflareball == null) && incomingFriendly && (!waitingForTakeoff || (this.lastFlareType != RocketFlareBall.FLARE_COLOR_WHITE))) {
                rocketflareball = new RocketFlareBall(this, this.point3d, this.orient, RocketFlareBall.FLARE_COLOR_WHITE);
                this.lastFlareType = RocketFlareBall.FLARE_COLOR_WHITE;
            }
            if ((rocketflareball == null) && waitingForTakeoff && (!incomingFriendly || (this.lastFlareType != RocketFlareBall.FLARE_COLOR_GREEN))) {
                rocketflareball = new RocketFlareBall(this, this.point3d, this.orient, RocketFlareBall.FLARE_COLOR_GREEN);
                this.lastFlareType = RocketFlareBall.FLARE_COLOR_GREEN;
            }
            if (rocketflareball != null) {
                rocketflareball.start(30F, 0);
                long nextFlareTimeout = TrueRandom.nextLong(45000L, 75000L); // FIXME!
                //long nextFlareTimeout = TrueRandom.nextLong(5000L, 10000L);
                if (incomingFriendly && waitingForTakeoff) {
                    nextFlareTimeout /= 2L;
                }
                this.nextFlareTime = Time.current() + nextFlareTimeout;
            }
        }
        return true;
    }

    public void createNetObject(NetChannel netchannel1, int i) {
        if (netchannel1 == null) {
            this.net = new Master(this);
        } else {
            this.net = new Mirror(this, netchannel1, i);
        }
    }

    public void netFirstUpdate(NetChannel netchannel1) throws IOException {
    }

    public abstract void msgExplosion(Explosion explosion);

    public abstract void msgShot(Shot shot);

    private static String        mesh_name        = "3do/primitive/siren/mono.sim";
    private float                heightAboveLandSurface;
    protected SoundFX            engineSFX;
    protected int                engineSTimer;
    private int                  myArmy;
    private static ActorSpawnArg constr_arg2      = null;
    private static Point3d       p                = new Point3d();
    private static Orient        o                = new Orient();
    private static Vector3f      n                = new Vector3f();
    public NetChannel            netchannel;
    private long                 nextFlareTime;
    Actor                        actor;
    private Point3d              point3d          = new Point3d();
    private Orient               orient           = new Orient();
    private BornPlace            nearestBornPlace = null;
    private int                  lastFlareType    = RocketFlareBall.FLARE_COLOR_WHITE;
}
