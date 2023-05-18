package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_No37 extends Fuze {

    static {
        Class localClass = Fuze_Pistol_No37.class;
        Property.set(localClass, "type", 3);
        Property.set(localClass, "airTravelToArm", 2.5F);
        Property.set(localClass, "fixedDelay", new float[] { 21600F, 43200F, 129600F, 259200F, 518400F });
    }
}
