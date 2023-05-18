package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSC70C extends BombGun {

    static {
        Class class1 = BombGunSC70C.class;
        Property.set(class1, "bulletClass", (Object) BombSC70.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
