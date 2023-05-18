// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:19:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunFlare.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunFlare extends RocketGun
{

    public RocketGunFlare()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunFlare.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.RocketFlare.class);
        Property.set(class1, "bullets", 3);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.Flare");
        Property.set(class1, "cassette", 1);
    }
}