package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun500lbsE1 extends BombGun {

    static {
        Class class1 = BombGun500lbsE1.class;
        Property.set(class1, "bulletClass", (Object) Bomb500lbsE.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
