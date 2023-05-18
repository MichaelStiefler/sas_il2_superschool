package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109E1 extends BF_109E3 {

    public BF_109E1() {
        this.bIsE3 = false;
    }

    static {
        Class class1 = BF_109E1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109E-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_de", "3DO/Plane/Bf-109E-1/hier.him");
        Property.set(class1, "PaintScheme_de", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109E-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109E3.class });
        Property.set(class1, "LOSElevation", 0.74985F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
