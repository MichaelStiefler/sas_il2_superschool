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
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class CWGeneric extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (CWGeneric.this.engineSFX != null) if (CWGeneric.this.engineSTimer >= 0) {
                if (--CWGeneric.this.engineSTimer <= 0) {
                    CWGeneric.this.engineSTimer = (int) CWGeneric.SecsToTicks(CWGeneric.Rnd(30F, 30F));
                    if (!CWGeneric.this.danger()) CWGeneric.this.engineSTimer = -CWGeneric.this.engineSTimer;
                }
            } else if (++CWGeneric.this.engineSTimer >= 0) {
                CWGeneric.this.engineSTimer = -(int) CWGeneric.SecsToTicks(CWGeneric.Rnd(30F, 30F));
                if (CWGeneric.this.danger()) CWGeneric.this.engineSTimer = -CWGeneric.this.engineSTimer;
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
            CWGeneric cwgeneric = null;
            try {
                CWGeneric.constr_arg2 = actorspawnarg;
                cwgeneric = (CWGeneric) this.cls.newInstance();
                CWGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                CWGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create CWGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return cwgeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", CWGeneric.mesh_name);
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

    protected CWGeneric() {
        this(constr_arg2);
    }

    private CWGeneric(ActorSpawnArg actorspawnarg) {
        super(mesh_name);
        this.engineSFX = null;
        this.engineSTimer = 0x98967f;
        actorspawnarg.setStationary(this);
        this.myArmy = this.getArmy();
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
            this.engineSTimer = -(int) CWGeneric.SecsToTicks(CWGeneric.Rnd(30F, 30F));
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
        boolean flag = false;
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (actor == World.getPlayerAircraft() && actor.pos.getAbsPoint().distance(point3d) < 10000D) flag = true;
        }

        Aircraft aircraft = World.getPlayerAircraft();
        double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
        double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
        for (java.util.Map.Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
            Actor actor1 = (Actor) entry1.getValue();
            if (Actor.isAlive(actor1) && (actor1 instanceof BigshipGeneric || actor1 instanceof ShipGeneric) && actor1.getArmy() != this.myArmy && actor1.pos.getAbsPoint().distance(point3d) < 15000D) {
                this.pos.getAbs(point3d);
                double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
                char c = (char) (int) (65D + Math.floor((d3 / 676D - Math.floor(d3 / 676D)) * 26D));
                char c1 = (char) (int) (65D + Math.floor((d3 / 26D - Math.floor(d3 / 26D)) * 26D));
                new String();
                String s;
                if (d2 > 260D) s = "" + c + c1;
                else s = "" + c1;
                new String();
                new String();
                double d7 = Math.ceil(d4);
                double d8 = d3 - d;
                double d9 = d4 - d1;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                double d10 = Math.floor((int) f) - 90D;
                if (d10 < 0.0D) d10 = 360D + d10;
                if (!flag) HUD.logCenter("                                                                     Enemy ship spotted at map grid " + s + "-" + d7);
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
    private int                  myArmy;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d       p           = new Point3d();
    private static Orient        o           = new Orient();
    private static Vector3f      n           = new Vector3f();
}
