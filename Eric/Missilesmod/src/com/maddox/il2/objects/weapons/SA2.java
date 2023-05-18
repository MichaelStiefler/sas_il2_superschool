package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.rts.*;

import java.util.List;
import java.util.Random;

public class SA2 extends MissileSAM
{

    public boolean interpolateStep()
    {
        if(range == 0.0D)
        {
            Random random = new Random();
            int i = random.nextInt(100) + 10;
            range = i;
        }
        float f = Time.tickLenFs();
        float f1 = (float)getSpeed((Vector3d)null);
        f1 += (500F - f1) * 0.1F * f;
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
            if(victim == null || !Actor.isValid(victim))
            {
                victim = NearestTargets.getEnemy(9, -1, p, 35000D, getOwner().getArmy());
            } else
            {
            	if(victim != null){
                	List list1 = Engine.countermeasures();
                	int m = list1.size();
                	for (int t = 0; t < m; t++) {
                		Actor flarechaff = (Actor) list1.get(t);
                		if(flarechaff instanceof com.maddox.il2.objects.weapons.RocketFlare)
                			{
                				victim = flarechaff;
                			} 
                	}
                }
                victim.pos.getAbs(pT);
                pT.sub(p);
                or.transformInv(pT);
                if(p.distance(victim.pos.getAbsPoint()) < 5000D && (victim instanceof TypeFighter) && victim.pos.getAbsPoint().z > 1000D && victim != World.getPlayerAircraft())
                    doevade();
                if(p.distance(victim.pos.getAbsPoint()) < range || p.distance(victim.pos.getAbsPoint()) > lastdist)
                {
                    pos.setAbs(p, or);
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                    return false;
                }
                if(p.distance(victim.pos.getAbsPoint()) > range)
                {
                    if(pT.y > 1.10000000000000001D)
                        deltaAzimuth = -0.1F;
                    if(pT.y < -1.10000000000000001D)
                        deltaAzimuth = 0.1F;
                    if(pT.z < -1.10000000000000001D)
                        deltaTangage = -0.1F;
                    if(pT.z > 1.10000000000000001D)
                        deltaTangage = 0.1F;
                    or.increment(40F * f * deltaAzimuth, 40F * f * deltaTangage, 0.0F);
                    deltaAzimuth = deltaTangage = 0.0F;
                }
                lastdist = p.distance(victim.pos.getAbsPoint());
            }
        pos.setAbs(p, or);
        return false;
    }

    public boolean doevade()
    {
        ((Pilot)((Aircraft)victim).FM).setSpeedMode(9);
        ((Pilot)((Aircraft)victim).FM).CT.ElevatorControl = 0.1F;
        ((Pilot)((Aircraft)victim).FM).CT.AileronControl = 0.3F;
        return true;
    }

    public SA2()
    {
        lastdist = 35000D;
        evade = false;
        range = 0.0D;
        victim = null;
        fm = null;
        tStart = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        victim = null;
    }

    public SA2(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        lastdist = 20000D;
        evade = false;
        range = 0.0D;
        victim = null;
        fm = null;
        tStart = 0L;
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
        if(Actor.isValid(actor) && (actor instanceof Aircraft) && actor.isNetMirror())
        {
            destroy();
            return;
        } else
        {
            doStart(f);
            return;
        }
    }

    private void doStart(float f)
    {
        super.start(-1F, 0);
        fm = ((Aircraft)getOwner()).FM;
        tStart = Time.current();
        Eff3DActor.New(this, findHook("_SMOKE"), null, 1.0F, "3do/Effects/RocketSidewinder/NikeSmoke.eff", -1F);
        flame.drawing(false);
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
    private float deltaAzimuth;
    private float deltaTangage;
    private float deltaX;
    private Actor victim;
    private static AirGroup airgroup = null;
    private static Pilot pilot = null;
    private double lastdist;
    private boolean evade;
    private double range;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.SA2.class;
        Property.set(class1, "timeFire", 20F);
        Spawn.add(class1, new MissileSAM.SPAWN());
    }
}