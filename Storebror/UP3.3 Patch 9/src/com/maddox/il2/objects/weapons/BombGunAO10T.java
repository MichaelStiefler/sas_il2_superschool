package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunAO10T extends BombGun {

    static {
        Class class1 = BombGunAO10T.class;
        Property.set(class1, "bulletClass", (Object) BombAO10T.class);
        Property.set(class1, "bullets", 4);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
