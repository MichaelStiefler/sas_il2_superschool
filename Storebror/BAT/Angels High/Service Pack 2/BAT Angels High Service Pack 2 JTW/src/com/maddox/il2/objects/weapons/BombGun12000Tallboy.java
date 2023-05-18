package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun12000Tallboy extends BombGun {
    static {
        Class class1 = BombGun12000Tallboy.class;
        Property.set(class1, "bulletClass", (Object) Bomb12000Tallboy.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
