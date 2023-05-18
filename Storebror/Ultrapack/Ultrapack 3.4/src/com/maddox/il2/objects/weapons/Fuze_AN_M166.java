package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M166 extends Fuze_Proximity {
    static {
        Class localClass = Fuze_AN_M166.class;
        Property.set(localClass, "type", 10);
        Property.set(localClass, "airTravelToArm", 1110F);
        Property.set(localClass, "fixedDelay", new float[] { 12F, 13.5F, 15F, 18F });
    }
}
