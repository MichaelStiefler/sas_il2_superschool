package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_fragBundle extends Fuze {
    static {
        Class class1 = Fuze_fragBundle.class;
        Property.set(class1, "type", 1);
        Property.set(class1, "airTravelToArm", 50F);
        Property.set(class1, "fixedDelay", new float[] { 0.2F, 0.5F, 1.0F, 1.5F, 2.0F, 3F, 5F });
    }
}
