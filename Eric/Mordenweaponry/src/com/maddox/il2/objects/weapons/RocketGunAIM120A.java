// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/5/2012 9:17:59 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAIM7M.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MissileGun

public class RocketGunAIM120A extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunAIM120A()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAIM120A.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileAIM120A.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}