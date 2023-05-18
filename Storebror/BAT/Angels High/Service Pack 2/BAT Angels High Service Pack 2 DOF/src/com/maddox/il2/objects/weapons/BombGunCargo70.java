package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunCargo70 extends BombGun {

    static {
        Class class1 = BombGunCargo70.class;
        Property.set(class1, "bulletClass", (Object) BombCargo70.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.75F);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
