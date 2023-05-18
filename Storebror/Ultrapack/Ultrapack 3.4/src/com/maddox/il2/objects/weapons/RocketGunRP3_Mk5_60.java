package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunRP3_Mk5_60 extends RocketGun {

    public void setRocketTimeLife(float f) {
        super.timeLife = -1F;
    }

    static {
        Class class1 = RocketGunRP3_Mk5_60.class;
        Property.set(class1, "bulletClass", (Object) RocketRP3_Mk5_60.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
