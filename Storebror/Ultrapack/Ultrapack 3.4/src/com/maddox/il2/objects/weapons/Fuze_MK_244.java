package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_MK_244 extends Fuze {
    static {
        Class localClass = Fuze_MK_244.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 135F);
        Property.set(localClass, "fixedDelay", new float[] { 4F });
    }
}
