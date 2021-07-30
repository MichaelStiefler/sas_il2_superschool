package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M144 extends Fuze {
    static {
        Class localClass = Fuze_M144.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 396F);
        Property.set(localClass, "minDelay", 1.6F);
        Property.set(localClass, "maxDelay", 30.6F);
    }
}
