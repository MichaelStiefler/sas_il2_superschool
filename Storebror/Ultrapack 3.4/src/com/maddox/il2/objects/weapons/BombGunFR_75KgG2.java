package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFR_75KgG2 extends BombGun {
    static {
        Class class1 = BombGunFR_75KgG2.class;
        Property.set(class1, "bulletClass", (Object) BombFR_75KgG2.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.8F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
