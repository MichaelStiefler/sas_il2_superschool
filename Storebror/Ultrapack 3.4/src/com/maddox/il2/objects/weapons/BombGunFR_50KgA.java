package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFR_50KgA extends BombGun {

    static {
        Class class1 = BombGunFR_50KgA.class;
        Property.set(class1, "bulletClass", (Object) BombFR_50KgA.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.8F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
