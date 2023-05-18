package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_43 extends P_43xyz {
    static {
        Class class1 = P_43.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Lancer");
        Property.set(class1, "meshName", "3DO/Plane/P-43A(multi1)/P43hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/P-43.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_43.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03" });
    }
}
