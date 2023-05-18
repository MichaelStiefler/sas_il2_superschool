package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM123A_gn16 extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunAGM123A_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM123A_gn16.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileAGM123A_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
