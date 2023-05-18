package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
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
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class SARGeneric extends ActorHMesh
    implements com.maddox.il2.objects.ActorAlign
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(SARGeneric.this.engineSFX != null)
                if(SARGeneric.this.engineSTimer >= 0)
                {
                    if(--SARGeneric.this.engineSTimer <= 0)
                    {
                        SARGeneric.this.engineSTimer = (int)SARGeneric.SecsToTicks(SARGeneric.Rnd(15F, 15F));
                        if(!SARGeneric.this.danger())
                            SARGeneric.this.engineSTimer = -SARGeneric.this.engineSTimer;
                    }
                } else
                if(++SARGeneric.this.engineSTimer >= 0)
                {
                    SARGeneric.this.engineSTimer = -(int)SARGeneric.SecsToTicks(SARGeneric.Rnd(15F, 15F));
                    if(SARGeneric.this.danger())
                        SARGeneric.this.engineSTimer = -SARGeneric.this.engineSTimer;
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
            SARGeneric sargeneric = null;
            try
            {
                SARGeneric.constr_arg2 = actorspawnarg;
                sargeneric = (SARGeneric)this.cls.newInstance();
                SARGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                SARGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create SARGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return sargeneric;
        }

        public Class cls;

        public SPAWN(Class class1)
        {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", SARGeneric.mesh_name);
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

    protected SARGeneric()
    {
        this(constr_arg2);
    }

    private SARGeneric(ActorSpawnArg actorspawnarg)
    {
        super(mesh_name);
        this.pickup = false;
        this.popped = false;
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

    public void startMove()
    {
        if(!this.interpEnd("move"))
        {
            this.interpPut(new Move(), "move", Time.current(), null);
            this.engineSFX = this.newSound("objects.siren", false);
            this.engineSTimer = -(int)SARGeneric.SecsToTicks(SARGeneric.Rnd(30F, 30F));
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
        float f = World.getTimeofDay();
        String s = "Effects/Smokes/Yellowsmoke.eff";
        if(f >= 0.0F && f <= 5F || f >= 21F && f <= 24F)
            s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        int i = (int)(-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i = 360 + i;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            this.pos.getAbs(point3d);
            if((actor instanceof Aircraft) && actor.pos.getAbsPoint().distance(point3d) <= 1000D)
            {
                if(!this.popped)
                {
                    Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                    this.popped = true;
                }
                if((actor instanceof Aircraft) && actor.pos.getAbsPoint().distance(point3d) <= 300D && actor.getSpeed(vector3d) < 10D)
                {
                    HUD.logCenter("                                                                             We got him! Heading home!");
                    this.pickup = true;
                    this.destroy();
                }
            }
            if(!this.pickup)
            {
                boolean flag = false;
                Aircraft aircraft1 = World.getPlayerAircraft();
                if(aircraft1.pos.getAbsPoint().distance(point3d) < 3000D)
                    flag = true;
                this.pos.getAbs(point3d);
                double d2 = (Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (point3d)).x) / 10000D;
                double d3 = (Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (point3d)).y) / 10000D;
                double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                char c = (char)(int)(65D + Math.floor((d2 / 676D - Math.floor(d2 / 676D)) * 26D));
                char c1 = (char)(int)(65D + Math.floor((d2 / 26D - Math.floor(d2 / 26D)) * 26D));
                double d7 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (point3d)).x;
                double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (point3d)).y;
                new String();
                String s1;
                if(d4 > 260D)
                    s1 = "" + c + c1;
                else
                    s1 = "" + c1;
                double d9 = d7 - d;
                double d10 = d8 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d10, -d9);
                double d11 = Math.floor((int)f1) - 90D;
                if(d11 < 0.0D)
                    d11 = 360D + d11;
                int j = (int)(d11 - i);
                if(j < 0)
                    j = 360 + j;
                int k = (int)(Math.ceil((j + 15) / 30D) - 1.0D);
                if(k == 0)
                    k = 12;
                new String();
                double d12 = Math.ceil(d3);
                if(flag && aircraft1.pos.getAbsPoint().distance(point3d) < 2000D)
                    HUD.logCenter("                                                                             Man in the water! " + k + " o'clock!");
                else
                    HUD.logCenter("                                                                             SAR required at map grid " + s1 + "-" + d12);
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

    private static String mesh_name = "3do/humans/Paratroopers/Water/US_Dinghy/live.sim";
    private float heightAboveLandSurface;
    protected SoundFX engineSFX;
    protected int engineSTimer;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
    public boolean pickup;
    public boolean popped;
}
