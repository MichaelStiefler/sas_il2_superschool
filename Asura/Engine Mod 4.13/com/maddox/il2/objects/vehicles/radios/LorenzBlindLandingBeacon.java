// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   LorenzBlindLandingBeacon.java

package com.maddox.il2.objects.vehicles.radios;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;

// Referenced classes of package com.maddox.il2.objects.vehicles.radios:
//            BeaconGeneric, BlindLandingData

public class LorenzBlindLandingBeacon extends BeaconGeneric
{

    public LorenzBlindLandingBeacon()
    {
        p1 = new Point3d();
        p2 = new Point3d();
        tempPoint1 = new Point3d();
        tempPoint2 = new Point3d();
        emittingPoint = new Point3d();
        outerMarkerDist = 3000F;
        innerMarkerDist = 300F;
        inner2MarkerDist = 185F;
        LorenzBlindLandingBeacon _tmp = this;
        innerMarkerDist = constr_arg1.innerMarkerDist;
        LorenzBlindLandingBeacon _tmp1 = this;
        outerMarkerDist = constr_arg1.outerMarkerDist;
    }

    public void rideBeam(Aircraft aircraft, BlindLandingData blindlandingdata)
    {
        aircraft.pos.getAbs(p2);
        pos.getAbs(p1);
        p1.sub(p2);
        double d = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
        double d1 = (double)aircraft.FM.getAltitude() - pos.getAbsPoint().z;
        float f = getConeOfSilence(d, d1);
        if(d > 35000D)
        {
            blindlandingdata.addSignal(0.0F, 0.0F, 35000F, false, 0.0F, 0.0F, 0.0F, 0.0F, false);
            return;
        }
        float f1 = f;
        if(Landscape.rayHitHQ(emittingPoint, aircraft.FM.Loc, tempPoint1))
        {
            Landscape.rayHitHQ(aircraft.FM.Loc, emittingPoint, tempPoint2);
            tempPoint1.sub(tempPoint2);
            double d2 = Math.sqrt(tempPoint1.x * tempPoint1.x + tempPoint1.y * tempPoint1.y);
            f1 = cvt((float)d2, 0.0F, 5000F, 1.0F, 0.1F);
        }
        f1 *= cvt((float)d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float)Math.atan2(p1.x, p1.y);
        f2 = pos.getAbsOrient().getYaw() + f2;
        for(f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F);
        for(; f2 >= 360F; f2 -= 360F);
        float f3 = f2 - 90F;
        boolean flag = true;
        boolean flag1 = false;
        if(f3 > 90F)
        {
            f3 = -(f3 - 180F);
            flag = false;
        }
        float f4 = 0.0F;
        if(!flag)
            f4 = f3 * -18F;
        else
            f4 = f3 * 18F;
        blindlandingdata.addSignal(f3, f4 * f, (float)d, flag, f1, outerMarkerDist, innerMarkerDist, inner2MarkerDist, flag1);
    }

    private float getConeOfSilence(double d, double d1)
    {
        float f = 57.32484F * (float)Math.atan2(d, d1);
        return cvt(f, 20F, 40F, 0.0F, 1.0F);
    }

    public void missionStarting()
    {
        super.missionStarting();
        emittingPoint.x = pos.getAbsPoint().x;
        emittingPoint.y = pos.getAbsPoint().y;
        emittingPoint.z = pos.getAbsPoint().z + 20D;
    }

    public void showGuideArrows()
    {
        hierMesh().chunkVisible("GuideArrows", true);
    }

    public void align()
    {
        super.align();
        emittingPoint.x = pos.getAbsPoint().x;
        emittingPoint.y = pos.getAbsPoint().y;
        emittingPoint.z = pos.getAbsPoint().z + 20D;
    }

    Point3d p1;
    Point3d p2;
    private Point3d tempPoint1;
    private Point3d tempPoint2;
    private Point3d emittingPoint;
    public float outerMarkerDist;
    public float innerMarkerDist;
    public float inner2MarkerDist;
}
