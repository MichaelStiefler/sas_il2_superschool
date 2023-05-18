// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/28/2012 10:19:37 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAGM88.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketBombGun

public class RocketGunAGM_154B extends RocketBombGun
{

    public RocketGunAGM_154B()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM_154B.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.AGM_154B.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}