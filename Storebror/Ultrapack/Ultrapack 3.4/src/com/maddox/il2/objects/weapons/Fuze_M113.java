package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M113 extends Fuze {
    static {
        Class localClass = Fuze_M113.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 31F);
        Property.set(localClass, "minDelay", 4F);
        Property.set(localClass, "maxDelay", 15F);
    }
}
