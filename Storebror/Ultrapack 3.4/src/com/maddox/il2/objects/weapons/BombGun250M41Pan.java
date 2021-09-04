package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun250M41Pan extends BombGun {
    static {
        Class class1 = BombGun250M41Pan.class;
        Property.set(class1, "bulletClass", (Object) Bomb250M41Pan.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
