package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunAO8 extends BombGun {

    static {
        Class class1 = BombGunAO8.class;
        Property.set(class1, "bulletClass", (Object) BombAO8.class);
        Property.set(class1, "bullets", 8);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
