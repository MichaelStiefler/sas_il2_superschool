package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Culot_No7_I2 extends Fuze {
    static {
        Class localClass = Fuze_Culot_No7_I2.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 10F);
        Property.set(localClass, "fixedDelay", new float[] { 0.7F });
    }
}
