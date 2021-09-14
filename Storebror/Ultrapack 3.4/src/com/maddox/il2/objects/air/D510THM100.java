package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class D510THM100 extends D510xyz {
    static {
        Class class1 = D510THM100.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D510THM100");
        Property.set(class1, "meshName", "3DO/Plane/D510/hier510TH.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/D510/hier510TH.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/D510TH.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitD510.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
