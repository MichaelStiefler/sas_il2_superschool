package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunNull extends BombGun {

    public BombGunNull() {
    }

    public void setBombDelay(float f) {
    }

    public void shots(int i) {
        this.bullets(0);
    }

    static {
        Class class1 = BombGunNull.class;
        Property.set(class1, "bulletClass", (Object) BombNull.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.bombgun_phball");
    }
}
