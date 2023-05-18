package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M134 extends Fuze {
    static {
        Class localClass = Fuze_M134.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 20F);
        Property.set(localClass, "fixedDelay", new float[] { 960F });
    }
}
