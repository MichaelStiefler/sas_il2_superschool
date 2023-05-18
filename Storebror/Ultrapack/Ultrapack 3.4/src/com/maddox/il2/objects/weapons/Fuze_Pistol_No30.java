package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_No30 extends Fuze {

    static {
        Class localClass = Fuze_Pistol_No30.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 5F);
        Property.set(localClass, "fixedDelay", new float[] { 0.01F, 1.0F });
    }
}
