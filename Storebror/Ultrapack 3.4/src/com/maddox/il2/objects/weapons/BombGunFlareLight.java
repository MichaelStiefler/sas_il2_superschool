package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFlareLight extends BombGun {
    static {
        Class class1 = BombGunFlareLight.class;
        Property.set(class1, "bulletClass", (Object) BombMk24FlareLight.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.3F);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
