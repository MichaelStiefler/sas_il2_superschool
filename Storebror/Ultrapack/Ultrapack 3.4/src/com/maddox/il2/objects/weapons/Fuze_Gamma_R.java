package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Gamma_R extends Fuze {
    static {
        Class localClass = Fuze_Gamma_R.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 5F);
        Property.set(localClass, "fixedDelay", new float[] { 12F });
    }
}
