package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFrNRuptureRC438kg_Mle1938 extends BombGun {
    static {
        Class class1 = BombGunFrNRuptureRC438kg_Mle1938.class;
        Property.set(class1, "bulletClass", (Object) BombFrNRuptureRC438kg_Mle1938.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
