package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.rts.*;

public class RocketX7homing extends RocketX4
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
            if(!isValid(victim))
            {
                if((victim = NearestTargets.getEnemy(0, -1, p, 5000D, 0)) != null)
                {
                    if(victim instanceof BridgeSegment)
                        victim = null;
                    else
                    if(getOwner().getArmy() == victim.getArmy())
                        if(victim instanceof BigshipGeneric)
                            victim = null;
                        else
                        if(victim instanceof TgtTank)
                            victim = null;
                    if(victim != null)
                    {
                        victim.pos.getAbs(pT);
                        pT.sub(p);
                        or.transformInv(pT);
                        if(pT.y > pT.x / 4D || pT.y < -pT.x / 4D || pT.z > pT.x / 4D || pT.z < -pT.x / 4D)
                            victim = null;
                    }
                }
                or.increment(0.0F, 0.0F, 720F * f);
            } else
            {
                victim.pos.getAbs(pT);
                pT.sub(p);
                or.transformInv(pT);
                if(pT.x > 0.0D)
                {
                    if(pT.y > 0.1D)
                        deltaAzimuth = -1F;
                    if(pT.y < -0.1D)
                        deltaAzimuth = 1.0F;
                    if(pT.z < -0.1D)
                        deltaTangage = -1F;
                    if(pT.z > 0.1D)
                        deltaTangage = 1.0F;
                    or.increment(50F * f * deltaAzimuth, 50F * f * deltaTangage, 720F * f);
                    deltaAzimuth = deltaTangage = 0.0F;
                } else
                {
                    victim = null;
                }
            }
        pos.setAbs(p, or);
        return false;
    }

    public RocketX7homing()
    {
        victim = null;
        fm = null;
        tStart = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        victim = null;
    }

    public RocketX7homing(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
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

    public void start(float f)
    {
        Actor actor = pos.base();
        if(isValid(actor) && (actor instanceof Aircraft))
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
        super.start(-1F);
        fm = ((Aircraft)getOwner()).FM;
        tStart = Time.current();
        if(Config.isUSE_RENDER())
            flame.drawing(true);
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
        if(isNet() && isNetMirror())
            return;
        if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
        {
            Pilot pilot = (Pilot)fm;
            if(pilot.target_ground != null)
                victim = pilot.target_ground;
            else
            if(pilot.target.actor != null)
                victim = pilot.target.actor;
        } else
        if((victim = Main3D.cur3D().getViewPadlockEnemy()) == null && (victim = Selector.look(true, true, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], -1, -1, World.getPlayerAircraft(), false)) != null)
            if(victim instanceof BridgeSegment)
                victim = null;
            else
            if(victim instanceof Soldier)
                victim = null;
            else
            if(getOwner().getArmy() == victim.getArmy())
                if(victim instanceof Aircraft)
                    victim = null;
                else
                if(victim instanceof TgtTank)
                    victim = null;
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
        Class class1 = RocketX7homing.class;
        Property.set(class1, "timeLife", 300F);
        Property.set(class1, "timeFire", 2.5F);
        Property.set(class1, "massa", 90F);
        Property.set(class1, "massaEnd", 67.5F);
        Spawn.add(class1, new RocketX4.SPAWN());
    }
}
