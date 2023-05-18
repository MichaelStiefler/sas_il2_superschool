package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_PistolNo54 extends Fuze {

    static {
        Class class1 = Fuze_PistolNo54.class;
        Property.set(class1, "type", 1);
        Property.set(class1, "airTravelToArm", 200F);
        Property.set(class1, "fixedDelay", new float[] { 1.0F, 2.5F });
    }
}
