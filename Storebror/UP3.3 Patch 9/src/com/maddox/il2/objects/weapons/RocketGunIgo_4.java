package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunIgo_4 extends RocketBombGun {

    public void setRocketTimeLife(float f) {
        this.timeLife = 30F;
    }

    static {
        Class class1 = RocketGunIgo_4.class;
        Property.set(class1, "bulletClass", (Object) RocketIgo_4.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
