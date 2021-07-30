package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunSchlong500 extends RocketGun {

    public void setRocketTimeLife(float f) {
        this.timeLife = -1.0F;
    }

    static {
        Class class1 = RocketGunSchlong500.class;
        Property.set(class1, "bulletClass", (Object) RocketSchlong500.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
