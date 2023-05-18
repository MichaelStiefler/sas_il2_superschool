package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_PistolNo44 extends Fuze {

    static {
        Class class1 = Fuze_PistolNo44.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "airTravelToArm", 600F);
        Property.set(class1, "fixedDelay", new float[] { 0.0F, 0.025F, 0.12F });
    }
}
