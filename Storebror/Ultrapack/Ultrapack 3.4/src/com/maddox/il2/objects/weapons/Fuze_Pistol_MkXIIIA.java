package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_MkXIIIA extends Fuze_Hydrostatic {
    static {
        Class localClass = Fuze_Pistol_MkXIIIA.class;
        Property.set(localClass, "type", 0);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 1.89F, 2.67F, 3.27F });
    }
}
