package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunWPFO extends RocketGun
{

    public RocketGunWPFO()
    {
    }

    public void setRocketTimeLife(float f)
    {
        super.timeLife = -1F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunWPFO.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketWPFO.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_82");
    }
}