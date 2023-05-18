package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_MK_243 extends Fuze {
    static {
        Class localClass = Fuze_MK_243.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 135F);
        Property.set(localClass, "fixedDelay", new float[] { 4F });
    }
}
