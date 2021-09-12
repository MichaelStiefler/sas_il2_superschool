package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunIT160Kg_AS extends BombGun {
    static {
        Class class1 = BombGunIT160Kg_AS.class;
        Property.set(class1, "bulletClass", (Object) BombIT160Kg_AS.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
