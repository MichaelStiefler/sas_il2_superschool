package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_MU extends Fuze {
    static {
        Class localClass = Fuze_MU.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 5F);
        Property.set(localClass, "minDelay", 2.0F);
        Property.set(localClass, "maxDelay", 12F);
    }
}
