// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 12/10/2015 03:55:34 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAGM65.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAGM65 extends RocketGun
{

    public RocketGunAGM65()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM65.class;
        Property.set(class1, "bulletClass", com.maddox.il2.objects.weapons.AGM65.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
