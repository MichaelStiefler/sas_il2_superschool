package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_FlareMk8 extends Fuze {
    static {
        Class localClass = Fuze_FlareMk8.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 90F, 120F });
    }
}
