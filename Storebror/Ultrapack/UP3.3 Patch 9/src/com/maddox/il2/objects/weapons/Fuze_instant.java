package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_instant extends Fuze {

    static {
        Class class1 = Fuze_instant.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "airTravelToArm", 0.0F);
        Property.set(class1, "armingTime", 0);
        Property.set(class1, "fixedDelay", new float[] { 0.0F });
    }
}
