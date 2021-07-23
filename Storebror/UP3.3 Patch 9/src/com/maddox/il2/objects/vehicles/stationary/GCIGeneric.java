package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.War;
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
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class GCIGeneric extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (GCIGeneric.this.engineSFX != null) if (GCIGeneric.this.engineSTimer >= 0) {
                if (--GCIGeneric.this.engineSTimer <= 0) {
                    GCIGeneric.this.engineSTimer = (int) GCIGeneric.SecsToTicks(GCIGeneric.Rnd(30F, 50F));
                    if (!GCIGeneric.this.danger()) GCIGeneric.this.engineSTimer = -GCIGeneric.this.engineSTimer;
                }
            } else if (++GCIGeneric.this.engineSTimer >= 0) {
                GCIGeneric.this.engineSTimer = -(int) GCIGeneric.SecsToTicks(GCIGeneric.Rnd(30F, 50F));
                if (GCIGeneric.this.danger()) GCIGeneric.this.engineSTimer = -GCIGeneric.this.engineSTimer;
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
            GCIGeneric gcigeneric = null;
            try {
                GCIGeneric.constr_arg2 = actorspawnarg;
                gcigeneric = (GCIGeneric) this.cls.newInstance();
                GCIGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                GCIGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create GCIGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return gcigeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", GCIGeneric.mesh_name);
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

    protected GCIGeneric() {
        this(constr_arg2);
    }

    private GCIGeneric(ActorSpawnArg actorspawnarg) {
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
            this.engineSTimer = -(int) GCIGeneric.SecsToTicks(GCIGeneric.Rnd(30F, 30F));
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
        Vector3d vector3d = new Vector3d();
        boolean flag = false;
        float f = 1000F;
        Aircraft aircraft = War.GetNearestEnemyAircraft(World.getPlayerAircraft(), 1000F, 9);
        // TODO: Added by SAS~Storebror to avoid null dereference
        if (aircraft == null) return true;
        if (!Actor.isValid(aircraft)) return true;
        for (int i = 0; aircraft == null && i < 100; i++) {
            aircraft = War.GetNearestEnemyAircraft(World.getPlayerAircraft(), 1000F + f, 9);
            f += 1000F;
        }

        if (aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().distance(point3d) < 100000D && aircraft.pos.getAbsPoint().z >= 150D && aircraft != World.getPlayerAircraft() && aircraft.getArmy() != this.myArmy) {
            this.pos.getAbs(point3d);
            Aircraft aircraft1 = World.getPlayerAircraft();
            if (aircraft1.pos.getAbsPoint().distance(point3d) < 100000D && aircraft1.pos.getAbsPoint().z >= 150D) flag = true;
            double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 10000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 10000D;
            double d2 = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 1000D;
            double d3 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 1000D;
            double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d5 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
            double d6 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
            double d7 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D;
            double d8 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D;
            char c = (char) (int) (65D + Math.floor((d5 / 676D - Math.floor(d5 / 676D)) * 26D));
            char c1 = (char) (int) (65D + Math.floor((d5 / 26D - Math.floor(d5 / 26D)) * 26D));
            new String();
            String s;
            if (d4 > 260D) s = "" + c + c1;
            else s = "" + c1;
            new String();
            int j = (int) (Math.floor(aircraft.pos.getAbsPoint().z * 0.1D) * 10D);
            new String();
            double d9 = Math.ceil(d6);
            double d10 = d5 - d;
            double d11 = d6 - d1;
            float f1 = 57.32484F * (float) Math.atan2(d11, -d10);
            double d12 = Math.floor((int) f1) - 90D;
            if (d12 < 0.0D) d12 = 360D + d12;
            int l = (int) d12;
            double d13 = d2 - d7;
            double d14 = d3 - d8;
            int i1 = (int) -(aircraft.pos.getAbsOrient().getYaw() - 90D);
            if (i1 < 0) i1 = 360 + i1;
            int j1 = (int) Math.ceil(Math.sqrt(d14 * d14 + d13 * d13));
            if (flag) {
                if (j1 > 4) HUD.logCenter("                                          Target bearing " + l + "\260" + ", range " + j1 + "km, height " + j + "m, heading " + i1 + "\260");
                else HUD.logCenter(" ");
            } else HUD.logCenter("                                                                             Target at " + s + "-" + d9 + ", height " + j + "m, heading " + i1 + "\260");
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
