package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunM26Inc extends BombGun {

    static {
        Class class1 = BombGunM26Inc.class;
        Property.set(class1, "bulletClass", (Object) BombM26Inc.class);
        Property.set(class1, "bullets", 32);
        Property.set(class1, "shotFreq", 3);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
