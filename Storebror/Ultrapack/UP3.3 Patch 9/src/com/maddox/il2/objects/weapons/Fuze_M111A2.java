package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M111A2 extends Fuze {
    static {
        Class localClass = Fuze_M111A2.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 396F);
        Property.set(localClass, "minDelay", 5F);
        Property.set(localClass, "maxDelay", 92F);
    }
}
