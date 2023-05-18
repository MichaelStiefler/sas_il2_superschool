package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunFlareLO56_gn16 extends RocketGun {

    static {
        Class localClass = RocketGunFlareLO56_gn16.class;
        Property.set(localClass, "bulletClass", (Object) RocketFlareLO56_gn16.class);
        Property.set(localClass, "bullets", 1);
        Property.set(localClass, "shotFreq", 7F);
        Property.set(localClass, "sound", "weapon.Flare");
    }
}
