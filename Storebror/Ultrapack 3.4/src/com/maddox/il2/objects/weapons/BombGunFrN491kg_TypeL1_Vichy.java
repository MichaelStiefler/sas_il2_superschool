package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFrN491kg_TypeL1_Vichy extends BombGun {
    static {
        Class class1 = BombGunFrN491kg_TypeL1_Vichy.class;
        Property.set(class1, "bulletClass", (Object) BombFrN491kg_TypeL1_Vichy.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
