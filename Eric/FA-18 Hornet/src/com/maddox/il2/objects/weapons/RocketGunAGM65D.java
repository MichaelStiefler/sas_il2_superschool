// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/25/2011 2:59:46 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAGM12.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAGM65D extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunAGM65D()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }


    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM65D.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileAGM65D.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 4.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}