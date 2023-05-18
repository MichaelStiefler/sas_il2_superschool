package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_generic_Delay extends Fuze {
    static {
        Class class1 = Fuze_generic_Delay.class;
        Property.set(class1, "type", 1);
        Property.set(class1, "airTravelToArm", 300F);
        Property.set(class1, "minDelay", 0.0F);
        Property.set(class1, "maxDelay", 10F);
    }
}
