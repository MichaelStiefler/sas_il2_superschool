package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M160 extends Fuze {
    static {
        Class localClass = Fuze_M160.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 609F);
        Property.set(localClass, "minDelay", 0.0F);
        Property.set(localClass, "maxDelay", 0.24F);
    }
}
