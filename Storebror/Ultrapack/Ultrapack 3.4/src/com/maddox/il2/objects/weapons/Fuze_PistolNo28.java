package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_PistolNo28 extends Fuze {

    static {
        Class class1 = Fuze_PistolNo28.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 75F);
        Property.set(class1, "fixedDelay", new float[] { 11F, 12F, 15F });
    }
}
