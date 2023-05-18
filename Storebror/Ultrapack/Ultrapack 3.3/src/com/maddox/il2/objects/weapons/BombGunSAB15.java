package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSAB15 extends FlareBombGun {

    static {
        Class class1 = BombGunSAB15.class;
        Property.set(class1, "bulletClass", (Object) BombSAB15.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
        Property.set(class1, "verticalShift", 0.072F);
    }
}
