
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class RocketGunAIM54A_gn16 extends MissileGun
    implements RocketGunWithDelay
{

    public RocketGunAIM54A_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAIM54A_gn16.class;
        Property.set(class1, "bulletClass", (Object) MissileAIM54A_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}