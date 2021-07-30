package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M190 extends Fuze {
    static {
        Class localClass = Fuze_M190.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 250F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F, 0.01F, 0.025F, 0.1F, 0.24F });
    }
}
