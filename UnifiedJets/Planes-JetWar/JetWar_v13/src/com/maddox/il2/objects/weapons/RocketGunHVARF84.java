package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHVARF84 extends RocketGun
{

    public RocketGunHVARF84()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = -1F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunHVARF84.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketHVARF84.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}