package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M908 extends Fuze {
    static {
        Class localClass = Fuze_M908.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 125F);
        Property.set(localClass, "minDelay", 4F);
        Property.set(localClass, "maxDelay", 92F);
    }
}
