package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFR_40KgF extends BombGun {
    static {
        Class class1 = BombGunFR_40KgF.class;
        Property.set(class1, "bulletClass", (Object) BombFR_40KgF.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
