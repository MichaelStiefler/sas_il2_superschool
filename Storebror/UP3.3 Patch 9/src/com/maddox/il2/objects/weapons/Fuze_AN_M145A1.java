package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M145A1 extends Fuze {
    static {
        Class localClass = Fuze_AN_M145A1.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 350F);
        Property.set(localClass, "minDelay", 5F);
        Property.set(localClass, "maxDelay", 92F);
    }
}
