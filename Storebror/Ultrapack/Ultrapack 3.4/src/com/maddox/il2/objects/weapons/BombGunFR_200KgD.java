package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFR_200KgD extends BombGun {
    static {
        Class class1 = BombGunFR_200KgD.class;
        Property.set(class1, "bulletClass", (Object) BombFR_200KgD.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.5F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
