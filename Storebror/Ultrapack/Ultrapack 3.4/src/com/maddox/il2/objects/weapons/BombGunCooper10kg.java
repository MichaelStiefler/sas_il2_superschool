package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunCooper10kg extends BombGun {
    static {
        Class class1 = BombGunCooper10kg.class;
        Property.set(class1, "bulletClass", (Object) BombCooper10kg.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
