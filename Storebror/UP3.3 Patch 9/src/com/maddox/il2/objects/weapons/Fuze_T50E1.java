package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_T50E1 extends Fuze_Proximity {
    static {
        Class localClass = Fuze_T50E1.class;
        Property.set(localClass, "type", 10);
        Property.set(localClass, "airTravelToArm", 530F);
        Property.set(localClass, "fixedDelay", new float[] { 3F, 4.5F, 6F, 7.5F, 9F, 10.5F, 12F });
    }
}
