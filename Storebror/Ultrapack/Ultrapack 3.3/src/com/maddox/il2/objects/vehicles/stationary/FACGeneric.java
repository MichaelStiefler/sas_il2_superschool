package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class FACGeneric extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (FACGeneric.this.engineSFX != null) if (FACGeneric.this.engineSTimer >= 0) {
                if (--FACGeneric.this.engineSTimer <= 0) {
                    FACGeneric.this.engineSTimer = (int) FACGeneric.SecsToTicks(FACGeneric.Rnd(60F, 60F));
                    if (!FACGeneric.this.danger()) FACGeneric.this.engineSTimer = -FACGeneric.this.engineSTimer;
                }
            } else if (++FACGeneric.this.engineSTimer >= 0) {
                FACGeneric.this.engineSTimer = -(int) FACGeneric.SecsToTicks(FACGeneric.Rnd(60F, 60F));
                if (FACGeneric.this.danger()) FACGeneric.this.engineSTimer = -FACGeneric.this.engineSTimer;
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
            FACGeneric facgeneric = null;
            try {
                FACGeneric.constr_arg2 = actorspawnarg;
                facgeneric = (FACGeneric) this.cls.newInstance();
                FACGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                FACGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create FACGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return facgeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", FACGeneric.mesh_name);
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

    protected FACGeneric() {
        this(constr_arg2);
    }

    private FACGeneric(ActorSpawnArg actorspawnarg) {
        super(mesh_name);
        this.flag2 = false;
        this.spread = 0;
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
            this.engineSTimer = -(int) FACGeneric.SecsToTicks(FACGeneric.Rnd(30F, 30F));
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
        boolean flag1 = false;
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (actor == World.getPlayerAircraft() && actor.pos.getAbsPoint().distance(point3d) < 10000D) flag = true;
        }

        for (java.util.Map.Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
            Actor actor1 = (Actor) entry1.getValue();
            if (Actor.isAlive(actor1) && (actor1 instanceof TankGeneric || actor1 instanceof ArtilleryGeneric || actor1 instanceof CarGeneric) && actor1.getArmy() != this.myArmy && actor1.pos.getAbsPoint().distance(point3d) < 500D) {
                this.pos.getAbs(point3d);
                double d = (Main3D.cur3D().land2D.worldOfsX() + point3d.x) / 10000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsY() + point3d.y) / 10000D;
                double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d3 = actor1.pos.getAbsPoint().distance(point3d);
                double d4 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                double d5 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().z;
                char c = (char) (int) (65D + Math.floor((d / 676D - Math.floor(d / 676D)) * 26D));
                char c1 = (char) (int) (65D + Math.floor((d / 26D - Math.floor(d / 26D)) * 26D));
                new String();
                String s;
                if (d2 > 260D) s = "" + c + c1;
                else s = "" + c1;
                double d7 = d4 - d;
                double d8 = d5 - d1;
                float f = 57.32484F * (float) Math.atan2(d8, d7);
                int i = (int) f;
                i = (i + 180) % 360;
                new String();
                String s1 = "West";
                if (i <= 315D && i >= 225D) s1 = "North";
                if (i <= 135D && i >= 45D) s1 = "South";
                if (i <= 44D && i >= 316D) s1 = "West";
                if (i <= 224D && i >= 136D) s1 = "East";
                new String();
                String s2 = "units";
                if (actor1 instanceof TankGeneric) s2 = "armor";
                if (actor1 instanceof ArtilleryGeneric) s2 = "guns";
                if (actor1 instanceof CarGeneric) s2 = "vehicles";
                new String();
                int j = (int) (Math.ceil(d3 / 10D) * 10D);
                double d9 = Math.ceil(d1);
                float f1 = World.getTimeofDay();
                boolean flag3 = false;
                if (f1 >= 0.0F && f1 <= 5F || f1 >= 21F && f1 <= 24F) flag3 = true;
                if (flag) {
                    if (!this.flag2) {
                        if (!flag3) {
                            HUD.logCenter("                                                                             Popping Smoke!");
                            Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/Redsmoke.eff", -1F);
                        } else {
                            HUD.logCenter("                                                                             Popping Flare!");
                            Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", -1F);
                        }
                        this.flag2 = true;
                    } else {
                        HUD.logCenter("                                                                             Enemy " + s2 + " " + j + " yards " + s1 + " of my mark");
                        if (!flag1) {
                            java.util.Random random = new Random();
                            int k = random.nextInt(40);
                            this.spread = k - 20;
                            int l = (int) ((d4 - d) * 10000D);
                            int i1 = (int) ((d5 - d1) * 10000D);
                            Eff3DActor.New(this, null, new Loc(l + this.spread, i1 + this.spread, d6, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/SmokeWhite.eff", 60F);
                            flag1 = true;
                        }
                    }
                } else HUD.logCenter("                                                                             Request CAS at map grid " + s + "-" + d9);
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
    private boolean              flag2;
    private int                  spread;
}
