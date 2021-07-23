package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_generic_LongDelay extends Fuze {

    static {
        Class class1 = Fuze_generic_LongDelay.class;
        Property.set(class1, "type", 3);
        Property.set(class1, "airTravelToArm", 150F);
        Property.set(class1, "minDelay", 10F);
        Property.set(class1, "maxDelay", 60F);
    }
}
