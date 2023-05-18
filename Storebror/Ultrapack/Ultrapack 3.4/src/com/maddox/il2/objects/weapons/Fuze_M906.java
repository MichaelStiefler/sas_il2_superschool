package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M906 extends Fuze {
    static {
        Class localClass = Fuze_M906.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 20F);
        Property.set(localClass, "fixedDelay", new float[] { 5F, 12.5F });
    }
}
