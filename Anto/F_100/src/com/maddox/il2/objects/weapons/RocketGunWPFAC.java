package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunWPFAC extends RocketGun
{

    public RocketGunWPFAC()
    {
    }

    public void setRocketTimeLife(float f)
    {
        super.timeLife = -1F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunWPFAC.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketWPFAC.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_82");
    }
}