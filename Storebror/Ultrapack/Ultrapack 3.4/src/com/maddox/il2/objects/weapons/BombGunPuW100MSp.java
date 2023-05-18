package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunPuW100MSp extends BombGun {
    static {
        Class class1 = BombGunPuW100MSp.class;
        Property.set(class1, "bulletClass", (Object) BombPuW100MSp.class);
        Property.set(class1, "bullets", 2);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 0);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
