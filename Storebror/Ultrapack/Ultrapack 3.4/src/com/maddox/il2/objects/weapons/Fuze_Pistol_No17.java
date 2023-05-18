package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_No17 extends Fuze {

    static {
        Class localClass = Fuze_Pistol_No17.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 2.5F);
        Property.set(localClass, "fixedDelay", new float[] { 1800F, 3600F, 7200F, 10800F, 21600F, 43200F, 64800F, 86400F, 129600F });
    }
}
