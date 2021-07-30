package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun110Napalm extends BombGun {

    public BombGun110Napalm() {
    }

    static {
        Class class1 = BombGun110Napalm.class;
        Property.set(class1, "bulletClass", (Object) Bomb110Napalm.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
