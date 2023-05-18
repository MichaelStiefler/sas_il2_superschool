package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSAB3m extends FlareBombGun {

    static {
        Class class1 = BombGunSAB3m.class;
        Property.set(class1, "bulletClass", (Object) BombSAB3m.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
        Property.set(class1, "verticalShift", 0.102F);
        Property.set(class1, "verticalShift_P_40M_NEW", 0.0F);
        Property.set(class1, "verticalShift_P_40E_NEW", 0.0F);
    }
}
