// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/4/2012 6:52:29 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitF18FLIR.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, F_18

public class CockpitF18CFLIR extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((F_18S)aircraft()).FLIR = true;
            CmdEnv.top().exec("fov 33.3");
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        super.doFocusLeave();
        ((F_18S)aircraft()).FLIR = false;
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        super.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        super.mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
        orient1.set(orient);
        laser(orient);
    }

    public void laser(Orient orient)
    {
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_18S)aircraft()).pos.getAbs(point3d, orient2);
        float f = orient.getYaw() - orient2.getYaw() + 90F;
        if(f > 360F)
            f -= 360F;
        else
        if(f < 0.0F)
            f += 360F;
        float f1 = f;
        if(f > 90F && f <= 180F)
            f = (float)Math.sqrt(Math.pow(f - 180F, 2D));
        else
        if(f > 180F && f <= 270F)
            f -= 180F;
        else
        if(f > 270F && f <= 360F)
            f = (float)Math.sqrt(Math.pow(f - 360F, 2D));
        float f2 = orient.getPitch() + orient2.getPitch() - 270F;
        if(f2 > 360F)
            f2 -= 360F;
        else
        if(f2 < 0.0F)
            f2 += 360F;
        f2 *= 0.01745329F;
        f *= 0.01745329F;
        double d = Math.tan(f2) * point3d.z;
        dY = Math.sin(f) * d;
        dX = Math.cos(f) * d;
        if(f1 > 0.0F && f1 <= 90F)
            spot1.set(point3d.x + dY, point3d.y + dX, 0.0D);
        else
        if(f1 > 90F && f1 <= 180F)
            spot1.set(point3d.x + dY, point3d.y - dX, 0.0D);
        else
        if(f1 > 180F && f1 <= 270F)
            spot1.set(point3d.x - dY, point3d.y - dX, 0.0D);
        else
            spot1.set(point3d.x - dY, point3d.y + dX, 0.0D);
        if(!((F_18S)aircraft()).hold)
        {
            F_18S F_18S = (F_18S)aircraft();
            F_18S.spot.set(spot1);
        }
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = Math.abs(f);
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
        } else
        {
            if(bNeedSetUp)
            {
                prevTime = Time.current() - 1L;
                bNeedSetUp = false;
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f3 = f - prevA0;
            float f4 = 0.001F * (float)(Time.current() - prevTime);
            float f5 = Math.abs(f3 / f4);
            if(f5 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f4;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f4;
            prevTime = Time.current();
            if(f1 > 0.0F)
                f1 = 0.0F;
            if(f1 < -95F)
                f1 = -95F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitF18CFLIR()
    {
        super("3DO/Cockpit/F18FLIR/F18FLIR.him", "he111_gunner");
        dY = 0.0D;
        dX = 0.0D;
        point3d1 = new Point3d();
        orient1 = new Orient();
        spot1 = new Point3d();
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
    }

    
    private double dY;
    private double dX;
    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    public Point3d point3d1;
    public Orient orient1;
    public Point3d spot1;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitF18CFLIR.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitF18CFLIR.class, "astatePilotIndx", 0);
    }
}