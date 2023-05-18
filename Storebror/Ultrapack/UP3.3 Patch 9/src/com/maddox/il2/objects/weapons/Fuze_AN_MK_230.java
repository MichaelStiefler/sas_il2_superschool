package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_MK_230 extends Fuze_Hydrostatic {
    static {
        Class localClass = Fuze_AN_MK_230.class;
        Property.set(localClass, "type", 9);
        Property.set(localClass, "airTravelToArm", 120F);
        Property.set(localClass, "fixedDelay", new float[] { 1.34F, 1.89F, 2.31F, 2.67F, 3.27F });
    }
}
