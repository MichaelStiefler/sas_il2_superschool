package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.War;
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
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class RCGCIGeneric extends ActorHMesh implements com.maddox.il2.objects.ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (RCGCIGeneric.this.engineSFX != null) if (RCGCIGeneric.this.engineSTimer >= 0) {
                if (--RCGCIGeneric.this.engineSTimer <= 0) {
                    RCGCIGeneric.this.engineSTimer = (int) RCGCIGeneric.SecsToTicks(RCGCIGeneric.Rnd(30F, 30F));
                    if (!RCGCIGeneric.this.danger()) RCGCIGeneric.this.engineSTimer = -RCGCIGeneric.this.engineSTimer;
                }
            } else if (++RCGCIGeneric.this.engineSTimer >= 0) {
                RCGCIGeneric.this.engineSTimer = -(int) RCGCIGeneric.SecsToTicks(RCGCIGeneric.Rnd(30F, 30F));
                if (RCGCIGeneric.this.danger()) RCGCIGeneric.this.engineSTimer = -RCGCIGeneric.this.engineSTimer;
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
            RCGCIGeneric rcgcigeneric = null;
            try {
                RCGCIGeneric.constr_arg2 = actorspawnarg;
                rcgcigeneric = (RCGCIGeneric) this.cls.newInstance();
                RCGCIGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                RCGCIGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create RCGCIGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return rcgcigeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", RCGCIGeneric.mesh_name);
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

    protected RCGCIGeneric() {
        this(constr_arg2);
    }

    private RCGCIGeneric(ActorSpawnArg actorspawnarg) {
        super(mesh_name);
        this.mainforcefound = false;
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
            this.engineSTimer = -(int) RCGCIGeneric.SecsToTicks(RCGCIGeneric.Rnd(60F, 60F));
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

    public boolean danger() {
        if (!Actor.isValid(World.getPlayerAircraft())) return true;
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.GetNearestEnemyAircraft(World.getPlayerAircraft(), 500000F, 9);
        if (!Actor.isValid(aircraft)) return true;
        for (int i = 0; !(aircraft instanceof TypeBomber) && i < 99;) {
            aircraft = War.GetNearestEnemyAircraft(World.getPlayerAircraft(), 500000F, 9);
            if (++i == 98) this.destroy();
        }

        int j = 0;
        if (this.mainforcefound) while (j < 99 && ((Maneuver) aircraft.FM).Skill != 2) {
            aircraft = War.GetNearestEnemyAircraft(World.getPlayerAircraft(), 500000F, 9);
            if (++j == 98) this.destroy();
        }
        for (int k = 0; k < 99 && ((Maneuver) aircraft.FM).Skill == 3 && !((Maneuver) aircraft.FM).hasBombs();) {
            aircraft = War.GetNearestEnemyAircraft(World.getPlayerAircraft(), 500000F, 9);
            if (++k == 98) this.destroy();
        }

        if (((Maneuver) aircraft.FM).Skill >= 2 && aircraft instanceof TypeBomber && aircraft.pos.getAbsPoint().distance(point3d) < 200000D && aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().z >= 150D) {
            this.pos.getAbs(point3d);
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
            double d2 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
            char c = (char) (int) (65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char) (int) (65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
            new String();
            String s;
            if (d > 260D) s = "" + c + c1;
            else s = "" + c1;
            new String();
            double d5 = Math.ceil(d2);
            int l = (int) -(aircraft.pos.getAbsOrient().getYaw() - 90D);
            if (l < 0) l = 360 + l;
            if (this.mainforcefound) HUD.logCenter("                                          Confirmed Mainforce at " + s + "-" + d5 + ", heading " + l + "\260");
            else if (!this.mainforcefound) HUD.logCenter("                                          Incoming Raid at " + s + "-" + d5 + ", heading " + l + "\260");
            if (!this.mainforcefound && !((Maneuver) aircraft.FM).hasBombs() && ((Maneuver) aircraft.FM).Skill == 2) this.mainforcefound = true;
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
    private boolean              mainforcefound;
}
