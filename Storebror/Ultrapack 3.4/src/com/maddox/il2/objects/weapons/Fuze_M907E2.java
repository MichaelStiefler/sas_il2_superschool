package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M907E2 extends Fuze {
    static {
        Class localClass = Fuze_M907E2.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 115F);
        Property.set(localClass, "minDelay", 3F);
        Property.set(localClass, "maxDelay", 92F);
    }
}
