package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun250lbsE1 extends BombGun {

    static {
        Class class1 = BombGun250lbsE1.class;
        Property.set(class1, "bulletClass", (Object) Bomb250lbsE.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
