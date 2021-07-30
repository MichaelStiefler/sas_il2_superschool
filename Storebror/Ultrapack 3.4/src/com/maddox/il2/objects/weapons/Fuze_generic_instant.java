package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_generic_instant extends Fuze {

    static {
        Class class1 = Fuze_generic_instant.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "airTravelToArm", 250F);
        Property.set(class1, "fixedDelay", new float[] { 0.0F, 0.1F, 0.5F });
    }
}
