// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/5/2013 5:19:34 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunR13M.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MissileGun, RocketGunWithDelay

public class RocketGunR13MS extends MissileGun
    implements RocketGunWithDelay
{

    public RocketGunR13MS()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunR13MS.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileR13MS.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}