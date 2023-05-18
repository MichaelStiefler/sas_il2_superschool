// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 25.01.2013 13:05:32
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunFlare.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunChaff extends RocketGun
{

    public RocketGunChaff()
    {
    }



    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunChaff.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.RocketFlare.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3.5F);
        Property.set(class1, "sound", "weapon.Flare");
        Property.set(class1, "cassette", 1);
    }
}