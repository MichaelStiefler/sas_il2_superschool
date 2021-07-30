package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunRDS4T extends BombGun {
    static {
        Class class1 = BombGunRDS4T.class;
        Property.set(class1, "bulletClass", (Object) BombRDS4T.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
        Property.set(class1, "newEffect", 1);
    }
}
