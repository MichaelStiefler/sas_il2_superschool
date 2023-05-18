package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109D1L extends BF_109_Early {

    static {
        Class class1 = BF_109D1L.class;
        BF_109_Early.init(class1);
        Property.set(class1, "meshName", "3DO/Plane/BF_109D1L/hier.him");
        Property.set(class1, "FlightModel", "FlightModels/Bf-109D-2SAS.fmd");
    }
}
