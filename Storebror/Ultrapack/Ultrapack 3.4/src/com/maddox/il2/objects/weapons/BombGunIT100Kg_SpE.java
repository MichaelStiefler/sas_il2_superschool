package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunIT100Kg_SpE extends BombGun {
    static {
        Class class1 = BombGunIT100Kg_SpE.class;
        Property.set(class1, "bulletClass", (Object) BombIT100Kg_SpE.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
