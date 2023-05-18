package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFrEclairante_Mle1933 extends BombGun {
    static {
        Class class1 = BombGunFrEclairante_Mle1933.class;
        Property.set(class1, "bulletClass", (Object) BombFrEclairante_Mle1933.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
