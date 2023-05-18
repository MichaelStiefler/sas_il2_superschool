package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M166E1 extends Fuze_Proximity {
    static {
        Class localClass = Fuze_AN_M166E1.class;
        Property.set(localClass, "type", 10);
        Property.set(localClass, "airTravelToArm", 610F);
        Property.set(localClass, "fixedDelay", new float[] { 12F, 13.5F, 15F, 18F });
    }
}
