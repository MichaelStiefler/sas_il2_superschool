package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Mk78 extends Fuze {
    static {
        Class localClass = Fuze_Mk78.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 2.0F);
        Property.set(localClass, "minDelay", 25F);
        Property.set(localClass, "maxDelay", 30F);
    }
}
