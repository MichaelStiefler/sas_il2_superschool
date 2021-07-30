package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSC50C extends BombGun {

    static {
        Class class1 = BombGunSC50C.class;
        Property.set(class1, "bulletClass", (Object) BombSC50C.class);
        Property.set(class1, "bullets", 10);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
