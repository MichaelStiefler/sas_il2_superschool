package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_generic_LowLevel extends Fuze {

    static {
        Class class1 = Fuze_generic_LowLevel.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 100F);
        Property.set(class1, "minDelay", 3F);
        Property.set(class1, "maxDelay", 15F);
    }
}
