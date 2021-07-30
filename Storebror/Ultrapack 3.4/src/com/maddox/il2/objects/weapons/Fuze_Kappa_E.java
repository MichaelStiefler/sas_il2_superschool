package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Kappa_E extends Fuze {
    static {
        Class localClass = Fuze_Kappa_E.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 33F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F, 3F });
    }
}
