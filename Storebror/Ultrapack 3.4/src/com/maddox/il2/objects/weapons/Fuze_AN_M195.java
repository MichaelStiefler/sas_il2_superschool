package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M195 extends Fuze {
    static {
        Class localClass = Fuze_AN_M195.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 170F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F, 0.01F, 0.025F, 0.1F, 0.24F });
    }
}
