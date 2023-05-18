// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/4/2012 7:22:23 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombMk82LGB.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.TypeLaserSpotter;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, Ballistics

public class BombMk82LGB extends Bomb
{

    public BombMk82LGB()
    {
        victim = null;
        lastdist = 20000D;
        evade = false;
        range = 0.0D;
        popped = false;
        laseron = false;
    }

    public void start()
    {
        super.start();
        drawing(true);
        t1 = Time.current() + 1000L;
    }

    public void interpolateTick()
    {
    	if(!popped && Time.current() > t1)
        {
            setMesh("3DO/arms/Mk82LGBD/mono.sim");
            popped = true;
        }
    	if(Time.current() > t1 + 1000L)
        {
            float f = Time.tickLenFs();
            float f1 = (float)getSpeed((Vector3d)null);
            f1 += (200F - f1) * 0.1F * f;
            pos.getAbs(p, or);
            v.set(1.0D, 0.0D, 0.0D);
            or.transform(v);
            v.scale(f1);
            setSpeed(v);
            p.x += v.x * (double)f;
            p.y += v.y * (double)f;
            p.z += v.z * (double)f - 0.01D;
            if(isNet() && isNetMirror())
                pos.setAbs(p, or);
            if(Time.current() > tStart + 350L)
            {
                checklaser();
                if(!laseron)
                {
                    curTm += Time.tickLenFs();
                    Ballistics.updateBomb(this, M, S, J, DistFromCMtoStab);
                    updateSound();
                } else
                {
                    pT.sub(p);
                    or.transformInv(pT);
                    float f2 = 0.05F;
                    if(p.distance(pT) > 0.0D)
                    {
                        if(pT.y > 0.10000000000000001D)
                            deltaAzimuth = -f2;
                        if(pT.y < -0.10000000000000001D)
                            deltaAzimuth = f2;
                        if(pT.z < -0.10000000000000001D)
                            deltaTangage = -f2;
                        if(pT.z > 0.10000000000000001D)
                            deltaTangage = f2;
                        or.increment(70F * f2 * deltaAzimuth, 70F * f2 * deltaTangage, 0.0F);
                        deltaAzimuth = deltaTangage = 0.0F;
                    }
                }
                pos.setAbs(p, or);
            }
        } else
        {
            curTm += Time.tickLenFs();
            Ballistics.updateBomb(this, M, S, J, DistFromCMtoStab);
            updateSound();
        }
    }

    private void checklaser()
    {
        laseron = false;
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if((actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(pos.getAbsPoint()) < 20000D && actor == World.getPlayerAircraft())
            {
                Point3d point3d = new Point3d();
                point3d = TypeLaserSpotter.spot;
                if(pos.getAbsPoint().distance(point3d) >= 15000D);
                pT.set(point3d);
                laseron = true;
            }
        }

    }

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
    private long t1;
    private boolean popped;
    private boolean laseron;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk82LGB.class;
        Property.set(class1, "mesh", "3DO/arms/Mk82LGB/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 64F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.32F);
        Property.set(class1, "massa", 250F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}