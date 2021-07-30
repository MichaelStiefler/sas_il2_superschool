package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M106 extends Fuze {
    static {
        Class localClass = Fuze_M106.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "minDelay", 4F);
        Property.set(localClass, "maxDelay", 5F);
    }
}
