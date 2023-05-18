// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/24/2012 2:39:28 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAGM84.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketBombGun

public class RocketGunAGM84B extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunAGM84B()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM84B.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileAGM84B.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.00F);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}