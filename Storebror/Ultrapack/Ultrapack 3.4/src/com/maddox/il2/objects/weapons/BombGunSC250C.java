package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSC250C extends BombGun {

    static {
        Class class1 = BombGunSC250C.class;
        Property.set(class1, "bulletClass", (Object) BombSC250.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
