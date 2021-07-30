package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class PylonP47Nextra extends PylonRO_82_1 { // Extend "PylonRO_82_1" for
    // no drag / no weight
    // penalty

    static {
        Property.set(PylonP47Nextra.class, "mesh", "3DO/Arms/RackP47N/mono.sim");
    }
}
