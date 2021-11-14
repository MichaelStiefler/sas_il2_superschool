package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketX4R extends RocketX4
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
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
            {
                Pilot pilot = (Pilot)fm;
                if(pilot.target != null)
                    victim = pilot.target.actor;
                else
                    victim = null;
            } else
            if((victim = Main3D.cur3D().getViewPadlockEnemy()) == null)
                victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
            if(victim != null)
            {
                victim.pos.getAbs(pT);
                pT.sub(p);
                or.transformInv(pT);
                if(pT.x > -10D)
                {
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
                }
            }
        }
        pos.setAbs(p, or);
        if(Time.current() > tStart + 500L)
            if(Actor.isValid(victim))
            {
                float f2 = (float)p.distance(victim.pos.getAbsPoint());
                if((victim instanceof Aircraft) && f2 > prevd && prevd != 1000F && f2 < 30F)
                {
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                }
                prevd = f2;
            } else
            {
                prevd = 1000F;
            }
        return false;
    }

    public RocketX4R()
    {
        victim = null;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        victim = null;
    }

    public RocketX4R(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        victim = null;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
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
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
    }

    private FlightModel fm;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private long tStart;
    private float prevd;
    private float deltaAzimuth;
    private float deltaTangage;
    private Actor victim;

    static 
    {
        Class class1 = RocketX4R.class;
        Spawn.add(class1, new RocketX4.SPAWN());
    }
}
