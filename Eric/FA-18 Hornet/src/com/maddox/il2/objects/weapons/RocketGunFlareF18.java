// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/6/2012 6:58:19 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunFlareNEW.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunFlareF18 extends RocketGun
{

    public RocketGunFlareF18()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunFlareF18.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.RocketFlareF18.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3.5F);
        Property.set(class1, "sound", "weapon.Flare");
        Property.set(class1, "cassette", 1);
    }
}