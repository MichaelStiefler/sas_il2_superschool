package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_IT_Standard extends Fuze {

    static {
        Class class1 = Fuze_IT_Standard.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "airTravelToArm", 620F);
        Property.set(class1, "fixedDelay", new float[] { 0.0F, 0.1F, 0.3F });
    }
}
