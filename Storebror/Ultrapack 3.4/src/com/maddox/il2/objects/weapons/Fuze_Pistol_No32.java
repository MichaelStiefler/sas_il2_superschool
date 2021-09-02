package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_No32 extends Fuze {

    static {
        Class localClass = Fuze_Pistol_No32.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 75F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F, 0.5F, 1.0F, 1.5F, 2.0F });
    }
}
