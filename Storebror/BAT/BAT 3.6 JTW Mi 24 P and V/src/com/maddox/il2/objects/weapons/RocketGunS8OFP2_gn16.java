package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunS8OFP2_gn16 extends RocketGun {

    public RocketGunS8OFP2_gn16() {
        this.setSpreadRnd(0);
    }

    public void setConvDistance(float f, float f1) {
        super.setConvDistance(f + 1000F, f1 + 2.0F);
    }

    public void setRocketTimeLife(float f) {
        this.timeLife = -1F;
    }

    static {
        Class class1 = RocketGunS8OFP2_gn16.class;
        Property.set(class1, "bulletClass", (Object) RocketS8OFP2_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.33F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
