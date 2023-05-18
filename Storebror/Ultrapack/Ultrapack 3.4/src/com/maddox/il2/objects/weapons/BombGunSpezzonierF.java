package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSpezzonierF extends BombGun {
    static {
        Class class1 = BombGunSpezzonierF.class;
        Property.set(class1, "bulletClass", (Object) Bomblet2KgF.class);
        Property.set(class1, "bullets", 22);
        Property.set(class1, "shotFreq", 20F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
