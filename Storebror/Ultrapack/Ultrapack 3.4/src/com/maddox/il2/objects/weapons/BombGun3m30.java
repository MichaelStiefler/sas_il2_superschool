package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun3m30 extends BombGun {
    static {
        Class class1 = BombGun3m30.class;
        Property.set(class1, "bulletClass", (Object) Bomb3m30.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
