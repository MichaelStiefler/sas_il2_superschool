package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFr500kg_Mle1930_MonoBloc extends BombGun {
    static {
        Class class1 = BombGunFr500kg_Mle1930_MonoBloc.class;
        Property.set(class1, "bulletClass", (Object) BombFr500kg_Mle1930_MonoBloc.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
