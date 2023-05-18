package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFr140kg_Mle1925_mod1932 extends BombGun {
    static {
        Class class1 = BombGunFr140kg_Mle1925_mod1932.class;
        Property.set(class1, "bulletClass", (Object) BombFr140kg_Mle1925_mod1932.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
