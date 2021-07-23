package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunChaff_gn16 extends RocketGun {

    static {
        Class class1 = RocketGunChaff_gn16.class;
        Property.set(class1, "bulletClass", (Object) RocketChaff_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 7F);
        Property.set(class1, "sound", "weapon.Flare");
        Property.set(class1, "cassette", 1);
    }
}
