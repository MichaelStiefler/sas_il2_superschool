package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunHC4000 extends BombGun {
    static {
        Class class1 = BombGunHC4000.class;
        Property.set(class1, "bulletClass", (Object) BombHC4000.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
