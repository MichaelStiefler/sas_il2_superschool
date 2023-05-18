package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunPuW12_5Sp extends BombGun {
    static {
        Class class1 = BombGunPuW12_5Sp.class;
        Property.set(class1, "bulletClass", (Object) BombPuW12_5Sp.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
