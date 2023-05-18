package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHVAR5BEAU25AP extends RocketGun {
    public void setRocketTimeLife(float f) {
        super.timeLife = -1F;
    }

    static {
        Class class1 = RocketGunHVAR5BEAU25AP.class;
        Property.set(class1, "bulletClass", (Object) RocketHVAR5BEAU25AP.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
