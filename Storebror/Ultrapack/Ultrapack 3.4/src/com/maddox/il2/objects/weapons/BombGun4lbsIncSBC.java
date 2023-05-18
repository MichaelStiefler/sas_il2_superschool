package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun4lbsIncSBC extends BombGun {

    static {
        Class class1 = BombGun4lbsIncSBC.class;
        Property.set(class1, "bulletClass", (Object) Bomb4lbsIncSBC.class);
        Property.set(class1, "bullets", 6);
        Property.set(class1, "shotFreq", 8F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
