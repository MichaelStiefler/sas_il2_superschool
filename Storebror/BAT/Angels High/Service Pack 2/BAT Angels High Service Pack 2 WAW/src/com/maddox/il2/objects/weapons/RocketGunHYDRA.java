package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHYDRA extends RocketGun {
    public void setRocketTimeLife(float f) {
        this.timeLife = -1F;
    }

    static {
        Class class1 = RocketGunHYDRA.class;
        Property.set(class1, "bulletClass", (Object) RocketHYDRA.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.rocketgun_82");
    }
}
