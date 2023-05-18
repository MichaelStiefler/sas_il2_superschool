// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 02.01.2020 13:07:53
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunFlareLO56_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunFlare_gn16 extends RocketGun
{

    public RocketGunFlare_gn16()
    {
    }

    static 
    {
        Class localClass = RocketGunFlare_gn16.class;
        Property.set(localClass, "bulletClass", (Object)RocketFlareLO56_gn16.class);
        Property.set(localClass, "bullets", 1);
        Property.set(localClass, "shotFreq", 7F);
        Property.set(localClass, "sound", "weapon.Flare");
    }
}