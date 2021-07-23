package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_SSQ extends Fuze {
    static {
        Class localClass = Fuze_SSQ.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F });
    }
}
