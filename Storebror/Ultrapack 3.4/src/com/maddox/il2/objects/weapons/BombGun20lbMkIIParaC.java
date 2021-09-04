package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun20lbMkIIParaC extends BombGun {
    static {
        Class class1 = BombGun20lbMkIIParaC.class;
        Property.set(class1, "bulletClass", (Object) Bomb20lbsFmkIIPara.class);
        Property.set(class1, "bullets", 4);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
