package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M105 extends Fuze {
    static {
        Class localClass = Fuze_M105.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 120F);
        Property.set(localClass, "minDelay", 0.0F);
        Property.set(localClass, "maxDelay", 0.1F);
    }
}
