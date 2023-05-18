// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 21.08.2019 15:16:54
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GunMissileV755.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.vehicles.stationary.StationarySAM;
import com.maddox.rts.NetChannel;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            CannonAntiAirGeneric, MissileV755, Missile

public class GunMissileV755 extends CannonAntiAirGeneric
{

    public GunMissileV755()
    {
        point3d = new Point3d();
        orient = new Orient();
    }

    protected float Specify(GunProperties paramGunProperties)
    {
        BulletProperties localBulletProperties = paramGunProperties.bullet[0];
        localBulletProperties.speed = 150F;
        return 56F;
    }

    public void doStartBullet(double paramDouble)
    {
        super.pos.getAbs(tmpP);
        super.pos.getAbs(tmpO);
        netchannel = null;
        victim = ((com.maddox.il2.objects.vehicles.stationary.StationarySAM.S_75M)getOwner()).getMissileTarget();
        MissileV755 missile = new MissileV755(getOwner(), netchannel, 1, tmpP, tmpO, 100F, victim);
        missile.start(0.0F, 0);
    }

    public NetChannel netchannel;
    Point3d point3d;
    Orient orient;
    private static Point3d tmpP = new Point3d();
    private static Orient tmpO = new Orient();
    private Actor victim;

}