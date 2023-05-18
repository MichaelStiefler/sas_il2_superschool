// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 8/31/2013 9:32:29 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CannonRocketSimpleS5MS.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            CannonRocketSimple, RocketSimpleS5MS

public class CannonRocketSimpleS5MS extends CannonRocketSimple
{

    public CannonRocketSimpleS5MS()
    {
    }

    protected void Specify(GunProperties gunproperties)
    {
        gunproperties.sound = "weapon.rocketgun_132";
        gunproperties.shotFreq = 9.33F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 2000F;
        BulletProperties bulletproperties = gunproperties.bullet[0];
        bulletproperties.speed = 550F;
    }

    public void launch(Point3d point3d, Orient orient, float f, Actor actor)
    {
        RocketSimpleS5MS rocketsimpleS5MS= new RocketSimpleS5MS(point3d, orient, actor);
        rocketsimpleS5MS.start(f);
    }
}