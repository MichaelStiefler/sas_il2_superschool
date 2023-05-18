package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M130 extends Fuze {
    static {
        Class localClass = Fuze_M130.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 30F);
        Property.set(localClass, "minDelay", 600F);
        Property.set(localClass, "maxDelay", 1800F);
    }
}
