package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M135 extends Fuze {
    static {
        Class localClass = Fuze_M135.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 396F);
        Property.set(localClass, "minDelay", 5F);
        Property.set(localClass, "maxDelay", 92F);
    }
}
