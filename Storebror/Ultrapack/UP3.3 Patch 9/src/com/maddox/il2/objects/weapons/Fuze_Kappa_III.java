package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Kappa_III extends Fuze {
    static {
        Class localClass = Fuze_Kappa_III.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 45F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F });
    }
}
