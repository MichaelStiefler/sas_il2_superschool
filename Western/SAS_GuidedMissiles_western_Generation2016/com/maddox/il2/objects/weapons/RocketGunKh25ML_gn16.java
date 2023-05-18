// Last Modified by: western0221 2018-12-30
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunKh25ML_gn16 extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunKh25ML_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunKh25ML_gn16.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileKh25ML_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
