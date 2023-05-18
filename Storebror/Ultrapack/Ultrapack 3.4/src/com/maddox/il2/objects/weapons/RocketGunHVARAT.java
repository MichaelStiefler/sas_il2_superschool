package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHVARAT extends RocketGun {

    public void setRocketTimeLife(float paramFloat) {
        this.timeLife = -1F;
    }

    static {
        Class class1 = RocketGunHVARAT.class;
        Property.set(class1, "bulletClass", (Object) RocketHVARAT.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
