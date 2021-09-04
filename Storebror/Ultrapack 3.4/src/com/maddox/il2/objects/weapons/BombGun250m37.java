package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun250m37 extends BombGun {
    static {
        Class class1 = BombGun250m37.class;
        Property.set(class1, "bulletClass", (Object) Bomb250m37.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
