// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 02.10.2020 3:11:27
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunS8KOM_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunS8KOM_gn16 extends RocketGun
{

    public RocketGunS8KOM_gn16()
    {
    }



    public void setRocketTimeLife(float f)
    {
        timeLife = -1F;
    }



    static 
    {
        Class class1 = RocketGunS8KOM_gn16.class;
        Property.set(class1, "bulletClass", (Object)RocketS8KOM_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.33F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}