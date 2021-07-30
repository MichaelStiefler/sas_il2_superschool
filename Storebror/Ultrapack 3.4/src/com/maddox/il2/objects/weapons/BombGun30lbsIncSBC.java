package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun30lbsIncSBC extends BombGun {

    static {
        Class class1 = BombGun30lbsIncSBC.class;
        Property.set(class1, "bulletClass", (Object) Bomb30lbsIncSBC.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 4F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
