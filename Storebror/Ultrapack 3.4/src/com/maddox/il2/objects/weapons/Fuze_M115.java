package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M115 extends Fuze {
    static {
        Class localClass = Fuze_M115.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 160F);
        Property.set(localClass, "minDelay", 4F);
        Property.set(localClass, "maxDelay", 15F);
    }
}
