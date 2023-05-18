package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunSNEB100 extends RocketGun {

    public RocketGunSNEB100() {
    }

    public void setRocketTimeLife(float f) {
        this.timeLife = -1F;
    }

    static {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunSNEB100.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketSNEB100.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }

}
