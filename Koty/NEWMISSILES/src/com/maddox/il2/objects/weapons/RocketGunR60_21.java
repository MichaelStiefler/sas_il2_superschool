// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 20.11.2018 9:06:55
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunR60M.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MissileGun, RocketGunWithDelay

public class RocketGunR60_21 extends MissileGun
    implements RocketGunWithDelay
{

    public RocketGunR60_21()
    {
    }

    static 
    {
        Class class1 = RocketGunR60_21.class;
        Property.set(class1, "bulletClass", (Object)MissileR60_21.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}