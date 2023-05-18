package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHYDRA extends RocketGun
{

    public RocketGunHYDRA()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = -1F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunHYDRA.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketHYDRA.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.rocketgun_82");
    }
}