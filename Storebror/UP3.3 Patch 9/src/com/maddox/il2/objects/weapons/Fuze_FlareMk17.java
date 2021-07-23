package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_FlareMk17 extends Fuze {
    static {
        Class localClass = Fuze_FlareMk17.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 60F, 330F });
    }
}
