package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_35A extends P_35A123 {
    static {
        Class class1 = P_35A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P_35");
        Property.set(class1, "meshName", "3DO/Plane/P-35A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-35Anew.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_35_A.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
