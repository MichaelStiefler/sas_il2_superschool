package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFrN150kg_TypeI2 extends BombGun {
    static {
        Class class1 = BombGunFrN150kg_TypeI2.class;
        Property.set(class1, "bulletClass", (Object) BombFrN150kg_TypeI2.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
