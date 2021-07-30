package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
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
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class SN2Generic extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (SN2Generic.this.engineSFX != null) if (SN2Generic.this.engineSTimer >= 0) {
                if (--SN2Generic.this.engineSTimer <= 0) {
                    SN2Generic.this.engineSTimer = (int) SN2Generic.SecsToTicks(SN2Generic.Rnd(5F, 5F));
                    if (!SN2Generic.this.danger()) SN2Generic.this.engineSTimer = -SN2Generic.this.engineSTimer;
                }
            } else if (++SN2Generic.this.engineSTimer >= 0) {
                SN2Generic.this.engineSTimer = -(int) SN2Generic.SecsToTicks(SN2Generic.Rnd(5F, 5F));
                if (SN2Generic.this.danger()) SN2Generic.this.engineSTimer = -SN2Generic.this.engineSTimer;
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
            SN2Generic sn2generic = null;
            try {
                SN2Generic.constr_arg2 = actorspawnarg;
                sn2generic = (SN2Generic) this.cls.newInstance();
                SN2Generic.constr_arg2 = null;
            } catch (Exception exception) {
                SN2Generic.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create SN2Generic object [class:" + this.cls.getName() + "]");
                return null;
            }
            return sn2generic;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", SN2Generic.mesh_name);
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

    protected SN2Generic() {
        this(constr_arg2);
    }

    private SN2Generic(ActorSpawnArg actorspawnarg) {
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
            this.engineSTimer = -(int) SN2Generic.SecsToTicks(SN2Generic.Rnd(5F, 5F));
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
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) -(aircraft.pos.getAbsOrient().getYaw() - 90D);
        if (i < 0) i = 360 + i;
        int j = (int) -(aircraft.pos.getAbsOrient().getPitch() - 90D);
        if (j < 0) j = 360 + j;
        boolean flag1 = false;
        float f = 100F;
        do {
            java.util.List list = Engine.targets();
            int k = list.size();
            for (int l = 0; l < k; l++) {
                Actor actor = (Actor) list.get(l);
                if (actor instanceof Aircraft && actor != World.getPlayerAircraft() && actor.getArmy() != this.myArmy) {
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d6 = d3 - d;
                    double d7 = d4 - d1;
                    double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    float f1 = 57.32484F * (float) Math.atan2(d7, -d6);
                    int i1 = (int) (Math.floor((int) f1) - 90D);
                    if (i1 < 0) i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d8 * d8);
                    int k1 = (int) (Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
                    float f2 = 57.32484F * (float) Math.atan2(k1, d11);
                    int l1 = (int) (Math.floor((int) f2) - 90D);
                    if (l1 < 0) l1 = 360 + l1;
                    int i2 = l1 - j;
                    if (k1 <= f && k1 >= 400D && i2 >= 220 && i2 <= 320 && Math.sqrt(j1 * j1) <= 60D && actor.getSpeed(vector3d) > 20D) {
                        String s = "level with us";
                        if (d2 - d5 - 200D >= 0.0D) s = "below us";
                        if (d2 - d5 + 200D < 0.0D) s = "above us";
                        int j2 = k1;
                        String s1 = "m";
                        if (j2 >= 1000) {
                            j2 = (int) Math.floor(j2 / 1000);
                            s1 = "km";
                        }
                        HUD.logCenter("                                          RO: Target bearing " + i1 + "\260" + ", range " + j2 + s1 + ", " + s);
                        flag1 = true;
                    }
                }
                f += 100F;
            }

        } while (!flag1 && f <= 4000F);
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
