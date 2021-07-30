package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_USnukeLowAlt extends Fuze {

    static {
        Class class1 = Fuze_USnukeLowAlt.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 31F);
        Property.set(class1, "minDelay", 15F);
        Property.set(class1, "maxDelay", 150F);
    }
}
