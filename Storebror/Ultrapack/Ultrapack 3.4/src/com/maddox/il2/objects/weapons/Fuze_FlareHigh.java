package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_FlareHigh extends Fuze {
    static {
        Class localClass = Fuze_FlareHigh.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "minDelay", 5F);
        Property.set(localClass, "maxDelay", 27F);
    }
}
