package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun20lbsFmkIIPara extends BombGun {
    static {
        Class class1 = BombGun20lbsFmkIIPara.class;
        Property.set(class1, "bulletClass", (Object) Bomb20lbsFmkIIPara.class);
        Property.set(class1, "bullets", 4);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
