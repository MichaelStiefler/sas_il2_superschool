package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSpezzonierI extends BombGun {
    static {
        Class class1 = BombGunSpezzonierI.class;
        Property.set(class1, "bulletClass", (Object) Bomblet2KgI.class);
        Property.set(class1, "bullets", 54);
        Property.set(class1, "shotFreq", 33F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
