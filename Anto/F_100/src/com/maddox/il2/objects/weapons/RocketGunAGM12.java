package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM12 extends RocketGun
{

    public RocketGunAGM12()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM12.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketAGM12.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}