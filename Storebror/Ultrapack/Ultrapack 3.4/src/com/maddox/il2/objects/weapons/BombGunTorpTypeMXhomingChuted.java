package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTorpTypeMXhomingChuted extends TorpedoGun {

    static {
        Class class1 = BombGunTorpTypeMXhomingChuted.class;
        Property.set(class1, "bulletClass", (Object) BombTorpTypeMXhomingChuted.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.1F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
    }
}
