// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10.02.2013 0:49:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunS5.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunS8KOM extends RocketGun
{

    public RocketGunS8KOM()
    {
    	setSpreadRnd(3);
    }

    public void setConvDistance(float f, float f1)
    {
        super.setConvDistance(f + 1000F, f1 + 2.81F);
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunS8KOM.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.RocketS8KOM.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.33F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}