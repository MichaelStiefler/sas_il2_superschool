// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 05.05.2019 23:18:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunS8OFP2_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunS8OFP2_gn16 extends RocketGun
{

    public RocketGunS8OFP2_gn16()
    {
        setSpreadRnd(0);
    }


    static 
    {
        Class class1 = RocketGunS8OFP2_gn16.class;
        Property.set(class1, "bulletClass", (Object)RocketS8OFP2_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 8F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}