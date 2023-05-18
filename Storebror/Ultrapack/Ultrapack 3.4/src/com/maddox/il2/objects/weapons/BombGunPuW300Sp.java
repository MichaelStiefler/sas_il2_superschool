package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunPuW300Sp extends BombGun {
    static {
        Class class1 = BombGunPuW300Sp.class;
        Property.set(class1, "bulletClass", (Object) BombPuW300Sp.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
