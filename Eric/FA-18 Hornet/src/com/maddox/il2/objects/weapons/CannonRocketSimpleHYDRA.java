// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14.07.2012 13:22:05
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CannonRocketSimpleRS132.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            CannonRocketSimple, RocketSimpleRS132

public class CannonRocketSimpleHYDRA extends CannonRocketSimple
{

    public CannonRocketSimpleHYDRA()
    {
    }

    protected void Specify(GunProperties gunproperties)
    {
        gunproperties.sound = "weapon.rocketgun_132";
        gunproperties.shotFreq = 10.33F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 8000F;
        BulletProperties bulletproperties = gunproperties.bullet[0];
        bulletproperties.speed = 900F;
    }

    public void launch(Point3d point3d, Orient orient, float f, Actor actor)
    {
        RocketSimpleHYDRA rocketsimplehydra = new RocketSimpleHYDRA(point3d, orient, actor);
        rocketsimplehydra.start(f);
    }
}