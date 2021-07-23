package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_PistolNo34 extends Fuze {

    static {
        Class class1 = Fuze_PistolNo34.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 0.0F);
        Property.set(class1, "fixedDelay", new float[] { 2.5F, 12F, 15F });
    }
}
