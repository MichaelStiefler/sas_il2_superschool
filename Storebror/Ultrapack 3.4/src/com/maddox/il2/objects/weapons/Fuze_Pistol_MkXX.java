package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_MkXX extends Fuze_Hydrostatic {
    static {
        Class localClass = Fuze_Pistol_MkXX.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 1.0F, 1.13F });
    }
}
