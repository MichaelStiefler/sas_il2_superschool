package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M102A2 extends Fuze {
    static {
        Class localClass = Fuze_AN_M102A2.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 135F);
        Property.set(localClass, "minDelay", 0.0F);
        Property.set(localClass, "maxDelay", 0.24F);
    }
}
