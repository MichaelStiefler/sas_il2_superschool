package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
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
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class NGSGeneric extends ActorHMesh
    implements com.maddox.il2.objects.ActorAlign
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(NGSGeneric.this.engineSFX != null)
                if(NGSGeneric.this.engineSTimer >= 0)
                {
                    if(--NGSGeneric.this.engineSTimer <= 0)
                    {
                        NGSGeneric.this.engineSTimer = (int)NGSGeneric.SecsToTicks(NGSGeneric.Rnd(1.0F, 1.0F));
                        if(!NGSGeneric.this.danger())
                            NGSGeneric.this.engineSTimer = -NGSGeneric.this.engineSTimer;
                    }
                } else
                if(++NGSGeneric.this.engineSTimer >= 0)
                {
                    NGSGeneric.this.engineSTimer = -(int)NGSGeneric.SecsToTicks(NGSGeneric.Rnd(1.0F, 1.0F));
                    if(NGSGeneric.this.danger())
                        NGSGeneric.this.engineSTimer = -NGSGeneric.this.engineSTimer;
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

        public Master(Actor actor1)
        {
            super(actor1);
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

        public Mirror(Actor actor1, NetChannel netchannel, int i)
        {
            super(actor1, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    public static class SPAWN
        implements ActorSpawn
    {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            NGSGeneric ngsgeneric = null;
            try
            {
                NGSGeneric.constr_arg2 = actorspawnarg;
                ngsgeneric = (NGSGeneric)this.cls.newInstance();
                NGSGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                NGSGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create NGSGeneric object [class:" + this.cls.getName() + "]");
                return null;
            }
            return ngsgeneric;
        }

        public Class cls;

        public SPAWN(Class class1)
        {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", NGSGeneric.mesh_name);
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

    protected NGSGeneric()
    {
        this(constr_arg2);
    }

    private NGSGeneric(ActorSpawnArg actorspawnarg)
    {
        super(mesh_name);
        this.counter = 0;
        this.hadtarget = false;
        this.actor = null;
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
            this.engineSTimer = -(int)NGSGeneric.SecsToTicks(NGSGeneric.Rnd(5F, 5F));
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
        if(!this.hadtarget)
        {
            this.actor = Selector.getTarget();
            if(this.actor != null && !(this.actor instanceof Aircraft))
            {
                this.hadtarget = true;
                this.counter = 0;
            }
        }
        if(this.hadtarget)
        {
            if(this.counter >= 50)
            {
                String s = "weapon.bomb_std";
                Point3d point3d = new Point3d();
                this.actor.pos.getAbs(point3d);
                java.util.Random random = new Random();
                int i = random.nextInt(200);
                int j = i - 100;
                point3d.x += j;
                i = random.nextInt(200);
                j = i - 100;
                point3d.y += j;
                Explosions.generate(this.actor, point3d, 7F, 0, 30F, !Mission.isNet());
                MsgExplosion.send(this.actor, s, point3d, this.getOwner(), 0.0F, 7F, 0, 30F);
            }
            if(this.counter >= 43 && this.counter < 45)
                HUD.logCenter("                                                                             Splash!");
            if(this.counter > 21 && this.counter < 25)
                HUD.logCenter("                                                                             Rounds Complete.");
            if(this.counter > 15 && this.counter < 25)
                HUD.logCenter("                                                                             Firing.");
            if(this.counter > 5 && this.counter < 10)
                HUD.logCenter("                                                                             Target Received.");
            this.counter++;
        }
        if(this.counter > 70)
        {
            this.hadtarget = false;
            this.counter = 0;
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
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
    private int counter;
    private boolean hadtarget;
    Actor actor;





}
