package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_KEMA extends Fuze {
    static {
        Class localClass = Fuze_KEMA.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 3F });
    }
}
