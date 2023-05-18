package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun260lbsFrag extends BombGun {
    static {
        Class class1 = BombGun260lbsFrag.class;
        Property.set(class1, "bulletClass", (Object) Bomb260lbsFrag.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
