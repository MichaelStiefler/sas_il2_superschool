package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunRRAB3_250 extends BombGun {
    static {
        Class class1 = BombGunRRAB3_250.class;
        Property.set(class1, "bulletClass", (Object) BombRRAB3_250.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
        Property.set(class1, "verticalShift", -0.08F);
    }
}
