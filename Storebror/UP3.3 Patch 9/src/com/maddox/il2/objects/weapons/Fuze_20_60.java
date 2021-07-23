package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_20_60 extends Fuze {
    static {
        Class localClass = Fuze_20_60.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 5F);
        Property.set(localClass, "fixedDelay", new float[] { 1200F, 1500F, 1800F, 2100F, 2400F, 2700F, 3000F, 3300F, 3600F });
    }
}
