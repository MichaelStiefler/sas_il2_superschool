package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HE_112B2 extends HE_112 {
    static {
        Class class1 = HE_112B2.class;
        initStatic(class1);
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3do/plane/He-112B1/hier.him");
        Property.set(class1, "FlightModel", "FlightModels/He-112B-2.fmd");
    }
}
