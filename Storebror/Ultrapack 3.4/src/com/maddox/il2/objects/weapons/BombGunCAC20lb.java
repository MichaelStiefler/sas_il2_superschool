package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunCAC20lb extends BombGun {

    public BombGunCAC20lb() {
    }

    static {
        Class class1 = BombGunCAC20lb.class;
        Property.set(class1, "bulletClass", (Object) BombCAC20lb.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
