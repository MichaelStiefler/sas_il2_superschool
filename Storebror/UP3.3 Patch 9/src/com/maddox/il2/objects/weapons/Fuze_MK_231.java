package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_MK_231 extends Fuze_Hydrostatic {
    static {
        Class localClass = Fuze_MK_231.class;
        Property.set(localClass, "type", 9);
        Property.set(localClass, "airTravelToArm", 335F);
        Property.set(localClass, "fixedDelay", new float[] { 1.34F });
    }
}
