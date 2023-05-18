package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAS30 extends RocketGun
{

    public RocketGunAS30()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 120F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAS30.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketAS30.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}