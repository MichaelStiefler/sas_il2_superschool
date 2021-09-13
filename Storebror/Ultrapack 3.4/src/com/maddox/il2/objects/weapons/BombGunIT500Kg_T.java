package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunIT500Kg_T extends BombGun {
    static {
        Class class1 = BombGunIT500Kg_T.class;
        Property.set(class1, "bulletClass", (Object) BombIT500Kg_T.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
