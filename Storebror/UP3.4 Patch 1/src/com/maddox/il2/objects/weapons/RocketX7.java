package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;

public class RocketX7 extends RocketX4
{

    public boolean interpolateStep()
    {
        float f = Time.tickLenFs();
        float f1 = (float)getSpeed(null);
        f1 += (320F - f1) * 0.1F * f;
        super.pos.getAbs(p, or);
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(f1);
        setSpeed(v);
        p.x += ((Tuple3d) (v)).x * (double)f;
        p.y += ((Tuple3d) (v)).y * (double)f;
        p.z += ((Tuple3d) (v)).z * (double)f;
        if(isNet() && isNetMirror())
        {
            super.pos.setAbs(p, or);
            return false;
        }
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
            {
                Pilot pilot = (Pilot)fm;
                if(((Maneuver) (pilot)).target_ground != null || ((Maneuver) (pilot)).target != null)
                {
                    if(((Maneuver) (pilot)).target_ground != null)
                        ((Maneuver) (pilot)).target_ground.pos.getAbs(pT);
                    else
                        ((FlightModelMain) (((Maneuver) (pilot)).target)).Loc.get(pT);
                    pT.sub(p);
                    or.transformInv(pT);
                    if(((Tuple3d) (pT)).x > -200D - 33.33331D * (double)(fm.Skill * fm.Skill))
                    {
                        double d = Aircraft.cvt(fm.Skill, 0.0F, 3F, 15F, 0.0F);
                        if(((Tuple3d) (pT)).y > d)
                            ((TypeX4Carrier)fm.actor).typeX4CAdjSideMinus();
                        if(((Tuple3d) (pT)).y < -d)
                            ((TypeX4Carrier)fm.actor).typeX4CAdjSidePlus();
                        if(((Tuple3d) (pT)).z < -d)
                            ((TypeX4Carrier)fm.actor).typeX4CAdjAttitudeMinus();
                        if(((Tuple3d) (pT)).z > d)
                            ((TypeX4Carrier)fm.actor).typeX4CAdjAttitudePlus();
                    }
                }
            }
            or.increment(50F * f * ((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth(), 50F * f * ((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage(), 0.0F);
            or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
            ((TypeX4Carrier)fm.actor).typeX4CResetControls();
        }
        super.pos.setAbs(p, or);
        return false;
    }

    public RocketX7()
    {
        fm = null;
    }

    public RocketX7(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        fm = null;
        super.net = new RocketX4.Mirror(this, netchannel, i);
        super.pos.setAbs(point3d, orient);
        super.pos.reset();
        super.pos.setBase(actor, null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f, int i)
    {
        Actor actor = super.pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            super.net = new RocketX4.Master(this);
        }
        doStart(f);
    }

    private void doStart(float f)
    {
        super.start(-1F, 0);
        fm = ((SndAircraft) ((Aircraft)getOwner())).FM;
        if(Config.isUSE_RENDER())
        {
            Eff3DActor.New(this, findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            Eff3DActor.New(this, findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
            super.flame.drawing(false);
        }
        super.pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        super.pos.setAbs(p, or);
    }

    private FlightModel fm;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();

    static 
    {
        Class class1 = RocketX7.class;
        Property.set(class1, "mesh", "3do/arms/X-7/mono.sim");
        Property.set(class1, "timeLife", 15F);
        Property.set(class1, "timeFire", 16.5F);
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 90F);
        Property.set(class1, "massaEnd", 67.5F);
        Spawn.add(class1, new RocketX4.SPAWN());
    }
}
