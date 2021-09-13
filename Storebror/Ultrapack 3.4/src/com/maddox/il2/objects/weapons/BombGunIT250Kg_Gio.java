package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunIT250Kg_Gio extends BombGun {
    static {
        Class class1 = BombGunIT250Kg_Gio.class;
        Property.set(class1, "bulletClass", (Object) BombIT250Kg_Gio.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.9F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
