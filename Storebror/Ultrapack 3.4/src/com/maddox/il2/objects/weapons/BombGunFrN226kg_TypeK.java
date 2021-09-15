package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFrN226kg_TypeK extends BombGun {
    static {
        Class class1 = BombGunFrN226kg_TypeK.class;
        Property.set(class1, "bulletClass", (Object) BombFrN226kg_TypeK.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
