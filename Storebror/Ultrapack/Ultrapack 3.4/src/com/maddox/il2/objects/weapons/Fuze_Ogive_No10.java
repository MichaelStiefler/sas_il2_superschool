package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Ogive_No10 extends Fuze {
    static {
        Class localClass = Fuze_Ogive_No10.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 3F);
        Property.set(localClass, "fixedDelay", new float[] { 0.35F });
    }
}
