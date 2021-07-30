package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
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

public abstract class OBOEGeneric extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (OBOEGeneric.this.engineSFX != null) if (OBOEGeneric.this.engineSTimer >= 0) {
                if (--OBOEGeneric.this.engineSTimer <= 0) {
                    OBOEGeneric.this.engineSTimer = (int) OBOEGeneric.SecsToTicks(OBOEGeneric.Rnd(1.0F, 1.0F));
                    if (!OBOEGeneric.this.danger()) OBOEGeneric.this.engineSTimer = -OBOEGeneric.this.engineSTimer;
                }
            } else if (++OBOEGeneric.this.engineSTimer >= 0) {
                OBOEGeneric.this.engineSTimer = -(int) OBOEGeneric.SecsToTicks(OBOEGeneric.Rnd(1.0F, 1.0F));
                if (OBOEGeneric.this.danger()) OBOEGeneric.this.engineSTimer = -OBOEGeneric.this.engineSTimer;
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
            OBOEGeneric oboegeneric = null;
            try {
                OBOEGeneric.constr_arg2 = actorspawnarg;
                oboegeneric = (OBOEGeneric) this.cls.newInstance();
                OBOEGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                OBOEGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create OBOEGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return oboegeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", OBOEGeneric.mesh_name);
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

    protected OBOEGeneric() {
        this(constr_arg2);
    }

    private OBOEGeneric(ActorSpawnArg actorspawnarg) {
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
            this.engineSTimer = -(int) OBOEGeneric.SecsToTicks(OBOEGeneric.Rnd(5F, 5F));
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
        Aircraft aircraft = World.getPlayerAircraft();
        boolean flag = ((Maneuver) aircraft.FM).hasBombs();
        if (aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().z >= 500D && flag) {
            this.pos.getAbs(point3d);
            double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
            double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d6 = aircraft.getSpeed(vector3d);
            double d7 = ((Maneuver) aircraft.FM).getAltitude();
            double d8 = d - d3;
            double d9 = d1 - d4;
            float f = 57.32484F * (float) Math.atan2(d9, -d8);
            double d10 = Math.floor((int) f) - 90D;
            if (d10 < 0.0D) d10 = 360D + d10;
            double d11 = -aircraft.pos.getAbsOrient().getYaw() + 90D;
            if (d11 < 0.0D) d11 += 360D;
            double d12 = d3 - d;
            double d13 = d4 - d1;
            double d14 = Math.sqrt(d13 * d13 + d12 * d12);
            double d15 = d7 - World.land().HQ(point3d.x, point3d.y);
            if (d15 < 0.0D) d15 = 0.0D;
            double d16 = d10 - d11;
            String s = "<<<";
            if (d16 > 0.0D) s = ">>>";
            double d17 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
            int i = (int) ((d14 - d17) / d6 / 60D);
            String s1 = "|" + i + "|";
            if (i < 1) {
                i = (int) ((d14 - d17) / d6);
                s1 = "[" + i + "]";
            }
            if (d16 <= 1.0D || d16 >= -1D) HUD.logCenter("                                                                             " + s1);
            if (d16 >= 1.0D || d16 <= -1D) HUD.logCenter("                                                                             " + s);
            if ((d16 <= 1.0D || d16 >= -1D) && (d14 <= d17 || i == 0)) HUD.logCenter("                                                                             Drop!");
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
