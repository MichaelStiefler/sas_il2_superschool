package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunIT500Kg_Gio extends BombGun {
    static {
        Class class1 = BombGunIT500Kg_Gio.class;
        Property.set(class1, "bulletClass", (Object) BombIT500Kg_Gio.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.9F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
