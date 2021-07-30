package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun20lbsSBC extends BombGun {

    static {
        Class class1 = BombGun20lbsSBC.class;
        Property.set(class1, "bulletClass", (Object) Bomb20lbsSBC.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 4F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
