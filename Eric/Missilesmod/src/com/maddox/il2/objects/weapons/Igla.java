package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;
import com.maddox.rts.*;
import java.util.List;
import java.util.Random;
import java.util.List;

public class Igla extends MissileSAM
{

    public boolean interpolateStep()
    {
        if(pos.getAbsPoint().z - World.land().HQ(pos.getAbsPoint().x, pos.getAbsPoint().y) > 100D)
            armed = true;
        if(range == 0.0D)
        {
            Random random = new Random();
            int i = random.nextInt(20) + 10;
            range = i;
        }
        float f = Time.tickLenFs();
        float f1 = (float)getSpeed((Vector3d)null);
        f1 += (430F - f1) * 0.1F * f;
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
            if(!locked)
            {
                victim = NearestTargets.getEnemy(9, -1, p, 4200D, getOwner().getArmy());
                locked = true;
            }
                if(victim instanceof Aircraft || victim instanceof MissileInterceptable)
                {
                victim.pos.getAbs(pT);
                pT.sub(p);
                or.transformInv(pT);
                float f2 = 0.6F;
                
                if((p.distance(victim.pos.getAbsPoint()) < range /*|| p.distance(victim.pos.getAbsPoint()) > lastdist)*/ && armed))
                {
                    pos.setAbs(p, or);
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                    return false;
                }
                
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
                if(victim != null)
                {
                    if(pT.y > 0.10000000000000001D)
                        deltaAzimuth = -f2;
                    if(pT.y < -0.10000000000000001D)
                        deltaAzimuth = f2;
                    if(pT.z < -0.10000000000000001D)
                        deltaTangage = -f2;
                    if(pT.z > 0.10000000000000001D)
                        deltaTangage = f2;
                    or.increment(50F * f * deltaAzimuth, 50F * f * deltaTangage, 0.0F);
                    deltaAzimuth = deltaTangage = 0.0F;
                }
                lastdist = p.distance(victim.pos.getAbsPoint());
            }
        pos.setAbs(p, or);
        return false;
    }

    public void doExplosion(Actor actor, String s)
    {
        pos.getTime(Time.current(), p);
        MsgExplosion.send(actor, s, p, getOwner(), 10F, 2.0F, 1, 10F);
        super.doExplosion(actor, s);
    }

    public void doExplosionAir()
    {
        pos.getTime(Time.current(), p);
        MsgExplosion.send(null, null, p, getOwner(), 10F, 2.0F, 1, 10F);
        super.doExplosionAir();
    }

    public Igla()
    {

        armed = false;
        locked = false;
        lastdist = 4200D;
        evade = false;
        range = 0.0D;
        clientGroup = null;
        victim = null;
        fm = null;
        tStart = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        victim = null;
    }

    public Igla(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        armed = false;
        locked = false;
        lastdist = 4200D;
        evade = false;
        range = 0.0D;
        clientGroup = null;
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
        Eff3DActor.New(this, findHook("_SMOKE"), null, 1.0F, "3DO/Effects/Rocket/rocketsmokewhite.eff", -1F);
        flame.drawing(false);
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
    }
    
	public boolean countermeasure;
    public boolean armed;
    public boolean locked;
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
//    private Actor flare;
    private static AirGroup airgroup = null;
    private static Pilot pilot = null;
    private double lastdist;
    private boolean evade;
    private double range;
    private AirGroup clientGroup;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Igla.class;
        Property.set(class1, "mesh", "3DO/Arms/2-75inch/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocketSAM");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "timeLife", 15F);
        Property.set(class1, "timeFire", 5F);
        Property.set(class1, "force", 20000F);
        Property.set(class1, "power", 150F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.72F);
        Property.set(class1, "massa", 9.15F);
        Property.set(class1, "massaEnd", 7.3F);
        Spawn.add(class1, new MissileSAM.SPAWN());
    }
}