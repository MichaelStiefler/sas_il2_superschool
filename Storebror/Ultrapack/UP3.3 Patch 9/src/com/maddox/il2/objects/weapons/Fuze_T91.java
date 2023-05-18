package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_T91 extends Fuze_Proximity {
    static {
        Class localClass = Fuze_T91.class;
        Property.set(localClass, "type", 10);
        Property.set(localClass, "airTravelToArm", 230F);
        Property.set(localClass, "fixedDelay", new float[] { 9F, 10.5F, 12F, 13.5F, 15F, 18F });
    }
}
