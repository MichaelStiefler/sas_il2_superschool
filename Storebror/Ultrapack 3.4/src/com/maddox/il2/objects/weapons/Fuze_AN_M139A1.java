package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M139A1 extends Fuze {
    static {
        Class localClass = Fuze_AN_M139A1.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 155F);
        Property.set(localClass, "fixedDelay", new float[] { 0.01F });
    }
}
