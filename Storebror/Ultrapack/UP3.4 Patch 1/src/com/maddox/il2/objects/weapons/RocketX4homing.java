package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketX4homing extends RocketX4
{

    public boolean interpolateStep()
    {
        float f = Time.tickLenFs();
        float f1 = (float)getSpeed((Vector3d)null);
        f1 += (320F - f1) * 0.1F * f;
        pos.getAbs(p, or);
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(f1);
        setSpeed(v);
        p.x += v.x * (double)f;
        p.y += v.y * (double)f;
        p.z += v.z * (double)f;
        if(isNet() && isNetMirror())
        {
            pos.setAbs(p, or);
            return false;
        }
        if(Time.current() > tStart + 350L)
            if(!Actor.isValid(victim))
            {
                if((victim = NearestTargets.getEnemy(9, -1, p, 5000D, getOwner().getArmy())) == null)
                    victim = NearestTargets.getEnemy(8, -1, p, 10000D, getOwner().getArmy());
            } else
            {
                victim.pos.getAbs(pT);
                pT.sub(p);
                or.transformInv(pT);
                if(pT.x > 0.0D)
                {
                    pT.y += (v.y * pT.x) / 666.666D;
                    pT.z += (v.z * pT.x) / 666.666D;
                    if(pT.y > 0.1D)
                        deltaAzimuth = -1F;
                    if(pT.y < -0.1D)
                        deltaAzimuth = 1.0F;
                    if(pT.z < -0.1D)
                        deltaTangage = -1F;
                    if(pT.z > 0.1D)
                        deltaTangage = 1.0F;
                    or.increment(50F * f * deltaAzimuth, 50F * f * deltaTangage, 0.0F);
                    deltaAzimuth = deltaTangage = 0.0F;
                } else
                if(p.distance(victim.pos.getAbsPoint()) > 30D)
                {
                    victim = null;
                } else
                {
                    pos.setAbs(p, or);
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                    return false;
                }
            }
        pos.setAbs(p, or);
        return false;
    }

    public RocketX4homing()
    {
        victim = null;
        fm = null;
        tStart = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        victim = null;
    }

    public RocketX4homing(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        victim = null;
        fm = null;
        tStart = 0L;
        net = new RocketX4.Mirror(this, netchannel, i);
        pos.setAbs(point3d, orient);
        pos.reset();
        pos.setBase(actor, (Hook)null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f, int i)
    {
        Actor actor = pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            net = new RocketX4.Master(this);
        }
        doStart(f);
    }

    private void doStart(float f)
    {
        super.start(-1F, 0);
        fm = ((Aircraft)getOwner()).FM;
        tStart = Time.current();
        if(Config.isUSE_RENDER())
            flame.drawing(true);
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
        if(isNet() && isNetMirror())
            return;
        if(getOwner() == World.getPlayerAircraft() && ((RealFlightModel)fm).isRealMode())
            victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
    }

    protected void doExplosion(Actor actor, String s)
    {
        pos.getTime(Time.current(), p);
        MsgExplosion.send(actor, s, p, getOwner(), 45F, 1.0F, 1, 400F);
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir()
    {
        pos.getTime(Time.current(), p);
        MsgExplosion.send((Actor)null, (String)null, p, getOwner(), 45F, 1.0F, 1, 400F);
        super.doExplosionAir();
    }

    private FlightModel fm;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private long tStart;
    private float deltaAzimuth;
    private float deltaTangage;
    private Actor victim;

    static 
    {
        Class class1 = RocketX4homing.class;
        Property.set(class1, "timeFire", 2.5F);
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 1.0F);
        Spawn.add(class1, new RocketX4.SPAWN());
    }
}
