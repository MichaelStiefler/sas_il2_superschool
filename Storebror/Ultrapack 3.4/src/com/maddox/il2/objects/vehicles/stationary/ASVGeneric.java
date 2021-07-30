package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.Ship;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class ASVGeneric extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (ASVGeneric.this.engineSFX != null) if (ASVGeneric.this.engineSTimer >= 0) {
                if (--ASVGeneric.this.engineSTimer <= 0) {
                    ASVGeneric.this.engineSTimer = (int) ASVGeneric.SecsToTicks(ASVGeneric.Rnd(15F, 15F));
                    if (!ASVGeneric.this.danger()) ASVGeneric.this.engineSTimer = -ASVGeneric.this.engineSTimer;
                }
            } else if (++ASVGeneric.this.engineSTimer >= 0) {
                ASVGeneric.this.engineSTimer = -(int) ASVGeneric.SecsToTicks(ASVGeneric.Rnd(15F, 15F));
                if (ASVGeneric.this.danger()) ASVGeneric.this.engineSTimer = -ASVGeneric.this.engineSTimer;
            }
            return true;
        }

        Move() {
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return true;
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    public static class SPAWN implements ActorSpawn {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            ASVGeneric asvgeneric = null;
            try {
                ASVGeneric.constr_arg2 = actorspawnarg;
                asvgeneric = (ASVGeneric) this.cls.newInstance();
                ASVGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                ASVGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create ASVGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return asvgeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", ASVGeneric.mesh_name);
            this.cls = class1;
            Spawn.add(this.cls, this);
        }
    }

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5D + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    public void destroy() {
        if (this.isDestroyed()) return;
        else {
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

    protected ASVGeneric() {
        this(constr_arg2);
    }

    private ASVGeneric(ActorSpawnArg actorspawnarg) {
        super(mesh_name);
        this.engineSFX = null;
        this.engineSTimer = 0x98967f;
        actorspawnarg.setStationary(this);
        this.collide(false);
        this.drawing(true);
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.heightAboveLandSurface = 0.0F;
        this.Align();
        this.startMove();
    }

    public void startMove() {
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
            this.engineSFX = this.newSound("objects.siren", false);
            this.engineSTimer = -(int) ASVGeneric.SecsToTicks(ASVGeneric.Rnd(5F, 5F));
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

    private boolean danger() {
        if (!Actor.isValid(World.getPlayerAircraft())) return true;
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) -(aircraft.pos.getAbsOrient().getYaw() - 90D);
        if (i < 0) i = 360 + i;
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (actor instanceof BigshipGeneric || actor instanceof ShipGeneric && Actor.isAlive(actor)) {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                new String();
                new String();
                new String();
                double d6 = d3 - d;
                double d7 = d4 - d1;
                float f = 57.32484F * (float) Math.atan2(d7, -d6);
                int j = (int) (Math.floor((int) f) - 90D);
                if (j < 0) j = 360 + j;
                int k = j - i;
                double d8 = d - d3;
                double d9 = d1 - d4;
                String s = "Ship";
                byte byte0 = 30;
                if (actor instanceof BigshipGeneric) s = "Warship";
                if (actor instanceof Ship.SubTypeVIIC_Sub || actor instanceof Ship.USSGatoSS212_Sub || actor instanceof Ship.USSGreenlingSS213_Sub) {
                    s = "Periscope";
                    byte0 = 5;
                }
                if (actor instanceof Ship.SubTypeVIIC_Srf || actor instanceof Ship.USSGatoSS212_Srf || actor instanceof Ship.USSGreenlingSS213_Srf) {
                    s = "Surfaced sub";
                    byte0 = 15;
                }
                int l = (int) (Math.ceil(Math.sqrt(d9 * d9 + d8 * d8)) / 1000D);
                if (l <= byte0 && d2 <= 3000D && Math.sqrt(k * k) <= 60D) HUD.logCenter("                                              Contact: " + s + ", bearing " + j + "\260" + ", range " + l + "km");
            }
        }

        return true;
    }

    public void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) this.net = new Master(this);
        else this.net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
    }

    private static String        mesh_name   = "3do/primitive/siren/mono.sim";
    private float                heightAboveLandSurface;
    protected SoundFX            engineSFX;
    protected int                engineSTimer;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d       p           = new Point3d();
    private static Orient        o           = new Orient();
    private static Vector3f      n           = new Vector3f();
}
