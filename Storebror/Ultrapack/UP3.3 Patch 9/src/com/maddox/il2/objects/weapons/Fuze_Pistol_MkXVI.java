package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_MkXVI extends Fuze_Hydrostatic {
    static {
        Class localClass = Fuze_Pistol_MkXVI.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 1.19F, 1.31F });
    }
}
