package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class VisualLandingCalc
{
    VisualLandingCalc()
    {
    }

    public float getPapiGlidePath(Actor actor, Aircraft aircraft1)
    {
        double d = actor.pos.getAbsPoint().z + 3D;
        double d1 = Math.abs(aircraft1.pos.getAbsPoint().x - actor.pos.getAbsPoint().x);
        double d2 = Math.abs(aircraft1.pos.getAbsPoint().y - actor.pos.getAbsPoint().y);
        float f1 = 10F;
        double d3 = Math.sqrt(d1 * d1 + d2 * d2) - (double)f1;
        double d4 = aircraft1.pos.getAbsPoint().z - d;
        if(d3 < d4)
            return 90F;
        double d5 = Math.asin(d4 / d3);
        double d6 = glideScopeInRads - d5;
        float f2 = (float)Math.toDegrees(d6);
        return f2;
    }
  
    public float getPapiAzimuthBP(Actor actor, Aircraft aircraft1)
    {
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        aircraft1.pos.getAbs(p2);
        actor.pos.getAbs(p1);
        p1.sub(p2);
        double d = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
        double d1 = (double) aircraft1.FM.getAltitude() - actor.pos.getAbsPoint().z;
        float f = getConeOfSilence(d, d1);
        if (d > 35000D) {
            return 0F;
        }
        float f1 = f;
        f1 *= Aircraft.cvt((float) d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float) Math.atan2(p1.x, p1.y);
        f2 = actor.pos.getAbsOrient().getYaw() + f2;
        for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F)
            ;
        for (; f2 >= 360F; f2 -= 360F)
            ;
        float f3 = f2 - 90F;
        boolean flag = true;
        boolean flag1 = false;
        if (f3 > 90F) {
            f3 = -(f3 - 180F);
            flag = false;
        }
        float f4 = 0.0F;
        if (!flag) f4 = f3 * -18F;
        else f4 = f3 * 18F;

        return f3;
    }
  
    public float getPapiAzimuthPB(Actor actor, Aircraft aircraft1)
    {
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        aircraft1.pos.getAbs(p2);
        actor.pos.getAbs(p1);
        p1.sub(p2);
        double d = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
        double d1 = (double) aircraft1.FM.getAltitude() - actor.pos.getAbsPoint().z;
        float f = getConeOfSilence(d, d1);
        if (d > 35000D) {
            return 0F;
        }
        float f1 = f;
        f1 *= Aircraft.cvt((float) d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float) Math.atan2(p1.x, p1.y);
        f2 = actor.pos.getAbsOrient().getYaw() + f2;
        for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F)
            ;
        for (; f2 >= 360F; f2 -= 360F)
            ;
        float f3 = f2 - 90F;
        boolean flag = true;
        boolean flag1 = false;
        if (f3 > 90F) {
            f3 = -(f3 - 180F);
            flag = false;
        }
        float f4 = 0.0F;
        if (!flag) f4 = f3 * -18F;
        else f4 = f3 * 18F;

        return f4 * f;
    }
  
    private float getConeOfSilence(double d, double d1) {
        float f = 57.32484F * (float) Math.atan2(d, d1);
        return Aircraft.cvt(f, 20F, 40F, 0.0F, 1.0F);
    }

    public Aircraft getNearestAircraftFront(Actor actor)
    {
        List list = Engine.targets();
        int i = list.size();
        double oldnearestdist = 12001D;
        int oldnearestindex = -1;

        for (int j = 0; j < i; j++)
        {
            Actor target = (Actor) list.get(j);
            if (!(target instanceof Aircraft)) continue;
            Aircraft aircraft1 = (Aircraft) target;
            Point3d point3d1 = aircraft1.pos.getAbsPoint();
            Point3d point3d2 = actor.pos.getAbsPoint();
            double d1 = Math.sqrt((point3d2.x - point3d1.x) * (point3d2.x - point3d1.x) + (point3d2.y - point3d1.y) * (point3d2.y - point3d1.y));
            if (d1 >= 12000D) continue;
            if (d1 >= oldnearestdist) continue;
            if (getPapiGlidePath(actor, aircraft1) == 90F) continue;
            if (Math.abs(getPapiAzimuthBP(actor, aircraft1)) > 20F) continue;
            if (Math.abs(getPapiAzimuthPB(actor, aircraft1)) > 90F) continue;
            if (aircraft1.FM.Gears.onGround() || aircraft1.FM.isCrashedOnGround()) continue;

            oldnearestdist = d1;
            oldnearestindex = j;
        }

        if (oldnearestindex == -1)  return null;
        else return (Aircraft) list.get(oldnearestindex);
    }


    private static final double glideScopeInRads = Math.toRadians(3D);

}
