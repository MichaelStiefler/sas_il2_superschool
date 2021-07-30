package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_24B extends P_24 {

    static {
        Class class1 = P_24B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P.24");
        Property.set(class1, "meshName", "3DO/Plane/P-24B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/P-24B (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_24.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        weaponTriggersRegister(class1, new int[] { 0, 1, 1, 0, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
