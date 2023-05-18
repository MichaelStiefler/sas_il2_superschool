package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Culot_No8 extends Fuze {
    static {
        Class localClass = Fuze_Culot_No8.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 3F);
        Property.set(localClass, "minDelay", 0.1F);
        Property.set(localClass, "maxDelay", 0.7F);
    }
}
