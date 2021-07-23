package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunCargoB extends BombGun {

    static {
        Class class1 = BombGunCargoB.class;
        Property.set(class1, "bulletClass", (Object) BombCargoB.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.75F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
