
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class RocketGunFlare_gn16 extends RocketGun
{

    public RocketGunFlare_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunFlare_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketFlare_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 7F);
        Property.set(class1, "sound", "weapon.Flare");
        Property.set(class1, "cassette", 1);
    }
}
