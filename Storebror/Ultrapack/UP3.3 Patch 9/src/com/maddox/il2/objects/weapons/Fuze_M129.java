package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_M129 extends Fuze {
    static {
        Class localClass = Fuze_M129.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 15F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F });
    }
}
