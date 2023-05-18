package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M120A1 extends Fuze {
    static {
        Class localClass = Fuze_AN_M120A1.class;
        Property.set(localClass, "type", 2);
        Property.set(localClass, "airTravelToArm", 30F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F });
    }
}
