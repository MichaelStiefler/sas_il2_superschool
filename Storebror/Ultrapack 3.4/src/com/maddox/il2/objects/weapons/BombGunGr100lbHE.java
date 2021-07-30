package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunGr100lbHE extends BombGun {
    static {
        Class class1 = BombGunGr100lbHE.class;
        Property.set(class1, "bulletClass", (Object) BombGr100lbHE.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
