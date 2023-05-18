package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
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
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class GNRGeneric extends ActorHMesh
    implements com.maddox.il2.objects.ActorAlign
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(GNRGeneric.this.engineSFX != null)
                if(GNRGeneric.this.engineSTimer >= 0)
                {
                    if(--GNRGeneric.this.engineSTimer <= 0)
                    {
                        GNRGeneric.this.engineSTimer = (int)GNRGeneric.SecsToTicks(GNRGeneric.Rnd(30F, 30F));
                        if(!GNRGeneric.this.danger())
                            GNRGeneric.this.engineSTimer = -GNRGeneric.this.engineSTimer;
                    }
                } else
                if(++GNRGeneric.this.engineSTimer >= 0)
                {
                    GNRGeneric.this.engineSTimer = -(int)GNRGeneric.SecsToTicks(GNRGeneric.Rnd(30F, 30F));
                    if(GNRGeneric.this.danger())
                        GNRGeneric.this.engineSTimer = -GNRGeneric.this.engineSTimer;
                }
            return true;
        }

        Move()
        {
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return true;
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    public static class SPAWN
        implements ActorSpawn
    {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            GNRGeneric gnrgeneric = null;
            try
            {
                GNRGeneric.constr_arg2 = actorspawnarg;
                gnrgeneric = (GNRGeneric)this.cls.newInstance();
                GNRGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                GNRGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create GNRGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return gnrgeneric;
        }

        public Class cls;

        public SPAWN(Class class1)
        {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", GNRGeneric.mesh_name);
            this.cls = class1;
            Spawn.add(this.cls, this);
        }
    }


    public static final double Rnd(double d, double d1)
    {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
        return World.Rnd().nextFloat(f, f1);
    }

    private static final long SecsToTicks(float f)
    {
        long l = (long)(0.5D + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    public void destroy()
    {
        if(this.isDestroyed())
        {
            return;
        } else
        {
            this.engineSFX = null;
            this.engineSTimer = 0xfa0a1f01;
            this.breakSounds();
            super.destroy();
            return;
        }
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    protected GNRGeneric()
    {
        this(constr_arg2);
    }

    private GNRGeneric(ActorSpawnArg actorspawnarg)
    {
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

    public void startMove()
    {
        if(!this.interpEnd("move"))
        {
            this.interpPut(new Move(), "move", Time.current(), null);
            this.engineSFX = this.newSound("objects.siren", false);
            this.engineSTimer = -(int)GNRGeneric.SecsToTicks(GNRGeneric.Rnd(5F, 5F));
        }
    }

    private void Align()
    {
        this.pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) + this.heightAboveLandSurface;
        o.setYPR(this.pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        this.pos.setAbs(p, o);
    }

    public void align()
    {
        this.Align();
    }

    private boolean danger()
    {
        if (!Actor.isValid(World.getPlayerAircraft())) return true;
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int)(-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i = 360 + i;
        Pilot pilot = (Pilot)aircraft.FM;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if((pilot.Wingman != null || pilot.crew > 1) && (actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != this.myArmy && actor.getSpeed(vector3d) > 20D)
            {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                double d8 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                String s = "";
                if(d2 - d5 - 500D >= 0.0D)
                    s = " low";
                if((d2 - d5) + 500D < 0.0D)
                    s = " high";
                new String();
                double d9 = d3 - d;
                double d10 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d10, -d9);
                int j = (int)(Math.floor((int)f) - 90D);
                if(j < 0)
                    j = 360 + j;
                int k = j - i;
                if(k < 0)
                    k = 360 + k;
                int l = (int)(Math.ceil((k + 15) / 30D) - 1.0D);
                if(l < 1)
                    l = 12;
                double d11 = d - d3;
                double d12 = d1 - d4;
                double d13 = Math.ceil(Math.sqrt(d12 * d12 + d11 * d11) / 10D) * 10D;
                String s1 = "Aircraft ";
                if(actor instanceof TypeFighter)
                    s1 = "Fighters ";
                if(actor instanceof TypeBomber)
                    s1 = "Bombers ";
                float f1 = World.getTimeofDay();
                boolean flag = false;
                if(f1 >= 0.0F && f1 <= 5F || f1 >= 21F && f1 <= 24F)
                    flag = true;
                java.util.Random random = new Random();
                int i1 = random.nextInt(100);
                if(!flag && d13 <= 6000D && d13 >= 500D && Math.sqrt(d8 * d8) <= 2000D)
                    HUD.logCenter("                                          " + s1 + "at " + l + " o'clock" + s + "!");
                if(flag && i1 <= 50 && d13 <= 1000D && d13 >= 100D && Math.sqrt(d8 * d8) <= 500D)
                    HUD.logCenter("                                          " + s1 + "at " + l + " o'clock" + s + "!");
            }
        }

        return true;
    }

    public void createNetObject(NetChannel netchannel, int i)
    {
        if(netchannel == null)
            this.net = new Master(this);
        else
            this.net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
    }

    private static String mesh_name = "3do/primitive/siren/mono.sim";
    private float heightAboveLandSurface;
    protected SoundFX engineSFX;
    protected int engineSTimer;
    private int myArmy;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
}
