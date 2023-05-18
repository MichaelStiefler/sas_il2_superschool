package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;

public abstract class CandCGeneric extends ActorHMesh implements ActorAlign {
    class Move extends Interpolate {

        public boolean tick() {
            if (CandCGeneric.this.engineSFX != null) {
                if (CandCGeneric.this.engineSTimer >= 0) {
                    if (--CandCGeneric.this.engineSTimer <= 0) {
                        CandCGeneric.this.engineSTimer = (int) CandCGeneric.SecsToTicks(CandCGeneric.Rnd(CandCGeneric.this.Timer1, CandCGeneric.this.Timer2));
                        if (!CandCGeneric.this.danger()) {
                            CandCGeneric.this.engineSTimer = -CandCGeneric.this.engineSTimer;
                        }
                    }
                } else if (++CandCGeneric.this.engineSTimer >= 0) {
                    CandCGeneric.this.engineSTimer = -(int) CandCGeneric.SecsToTicks(CandCGeneric.Rnd(CandCGeneric.this.Timer1, CandCGeneric.this.Timer2));
                    if (CandCGeneric.this.danger()) {
                        CandCGeneric.this.engineSTimer = -CandCGeneric.this.engineSTimer;
                    }
                }
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
            CandCGeneric candcgeneric = null;
            try {
                CandCGeneric.constr_arg2 = actorspawnarg;
                candcgeneric = (CandCGeneric) this.cls.newInstance();
                CandCGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                CandCGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create CandCGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return candcgeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", CandCGeneric.mesh_name);
            this.cls = class1;
            com.maddox.rts.Spawn.add(this.cls, this);
        }
    }

    public void setTimer(int i) {
//        Random random = new Random(); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        this.Timer1 = (float) (TrueRandom.nextInt(i) * 0.1D); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        this.Timer2 = (float) (TrueRandom.nextInt(i) * 0.1D); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
    }

    public void resetTimer(float f) {
        this.Timer1 = f;
        this.Timer2 = f;
    }

    public static double Rnd(double d, double d1) {
        return TrueRandom.nextDouble(d, d1);
    }

    public static float Rnd(float f, float f1) {
        return TrueRandom.nextFloat(f, f1);
    }

    public boolean RndB(float f) {
        return TrueRandom.nextFloat(0.0F, 1.0F) < f;
    }

    public static long SecsToTicks(float f) {
        long l = (long) (0.5D + (f / Time.tickLenFs()));
        return l < 1L ? 1L : l;
    }

    public void destroy() {
        if (this.isDestroyed()) {
            return;
        } else {
            this.engineSFX = null;
            this.engineSTimer = 0xfa0a1f01; // funny value :)
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

    public CandCGeneric() {
        this(constr_arg2);
    }

    public CandCGeneric(ActorSpawnArg actorspawnarg) {
        mesh_name = "3do/primitive/siren/mono.sim";
        this.engineSFX = null;
        this.engineSTimer = 9999999;
        this.outCommand = new NetMsgFiltered();
        actorspawnarg.setStationary(this);
        this.myArmy = this.getArmy();
        this.setArmy(this.getArmy());
        this.collide(false);
        this.drawing(true);
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.heightAboveLandSurface = 0.0F;
        this.Align();
        this.startMove();
        this.Timer1 = 30F;
        this.Timer2 = 50F;
        this.delay = 3F;
    }

    public void startMove() {
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
            this.engineSFX = this.newSound("objects.siren", false);
            this.engineSTimer = -(int) SecsToTicks(Rnd(this.delay, this.delay));
        }
    }

    public void Align() {
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
        return false;
    }

    public void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) {
            this.net = new Master(this);
        } else {
            this.net = new Mirror(this, netchannel, i);
        }
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
    }

    public static String        mesh_name;
    public float                heightAboveLandSurface;
    public SoundFX              engineSFX;
    public int                  engineSTimer;
    public int                  myArmy;
    public static ActorSpawnArg constr_arg2 = null;
    public static Point3d       p           = new Point3d();
    public static Orient        o           = new Orient();
    public static Vector3f      n           = new Vector3f();
    public static Vector3d      tmpv        = new Vector3d();
    public NetMsgFiltered       outCommand;
    public float                Timer1;
    public float                Timer2;
    public float                delay;

}
