package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTIGreen extends BombGun {
    static {
        Class class1 = BombGunTIGreen.class;
        Property.set(class1, "bulletClass", (Object) BombTIGreen.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
