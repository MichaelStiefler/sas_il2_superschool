package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunIT50Kg_T_Late extends BombGun {
    static {
        Class class1 = BombGunIT50Kg_T_Late.class;
        Property.set(class1, "bulletClass", (Object) BombIT50Kg_T_Late.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
