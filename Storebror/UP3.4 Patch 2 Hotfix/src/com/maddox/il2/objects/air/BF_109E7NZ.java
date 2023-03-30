package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109E7NZ extends BF_109Ex {

    static {
        Class class1 = BF_109E7NZ.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109E-7NZ/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109E-7NZ.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Ex.class });
        Property.set(class1, "LOSElevation", 0.74985F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02" });
    }
}
