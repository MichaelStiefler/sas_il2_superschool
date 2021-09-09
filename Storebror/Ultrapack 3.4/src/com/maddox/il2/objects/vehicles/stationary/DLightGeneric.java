package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RandomVector;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMeshDraw;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public abstract class DLightGeneric extends ActorHMesh implements ActorAlign {
    class MyDraw extends ActorMeshDraw {

        public void lightUpdate(Loc loc, boolean flag) {
            if (flag) {
                RandomVector.getTimed(Time.currentReal() * 24L, DLightGeneric.tmpv, DLightGeneric.this.hashCode);
                DLightGeneric.this.light.light.setEmit(1.0F, 22F);
            }
            super.lightUpdate(loc, flag);
        }

        MyDraw() {
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
            DLightGeneric dlightgeneric = null;
            try {
                DLightGeneric.constr_arg2 = actorspawnarg;
                dlightgeneric = (DLightGeneric) this.cls.newInstance();
                DLightGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                DLightGeneric.constr_arg2 = null;
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create DLightGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return dlightgeneric;
        }

        public Class cls;

        public SPAWN(Class class1) {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", DLightGeneric.mesh_name);
            this.cls = class1;
            Spawn.add(this.cls, this);
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public boolean isStaticPos() {
        return true;
    }

    protected DLightGeneric() {
        this(DLightGeneric.constr_arg2);
    }

    private DLightGeneric(ActorSpawnArg actorspawnarg) {
        super(DLightGeneric.mesh_name);
        actorspawnarg.setStationary(this);
        this.setArmy(0);
        this.collide(false);
        this.drawing(true);
        this.draw = new MyDraw();
        this.light = new LightPointActor(new LightPointWorld(), new Point3d(0.0D, 0.0D, 1.5D));
        this.light.light.setColor(new Color3f(1.0F, 0.95F, 0.66F));
        this.light.light.setEmit(1.0F, 22F);
        this.draw.lightMap().put("light", this.light);
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.heightAboveLandSurface = 0.0F;
        this.Align();
        this.hashCode = DLightGeneric.rnd.nextInt(0, 4095);
    }

    private void Align() {
        this.pos.getAbs(DLightGeneric.p);
        DLightGeneric.p.z = Engine.land().HQ(((Tuple3d) (DLightGeneric.p)).x, ((Tuple3d) (DLightGeneric.p)).y) + this.heightAboveLandSurface;
        DLightGeneric.o.setYPR(this.pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        this.pos.setAbs(DLightGeneric.p, DLightGeneric.o);
    }

    public void align() {
        this.Align();
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

    private static String        mesh_name   = "3do/buildings/furniture/DLight/mono.sim";
    private float                heightAboveLandSurface;
    private LightPointActor      light;
    private int                  hashCode;
    private static RangeRandom   rnd         = new RangeRandom();
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d       p           = new Point3d();
    private static Orient        o           = new Orient();
    private static Vector3d      tmpv        = new Vector3d();
}
