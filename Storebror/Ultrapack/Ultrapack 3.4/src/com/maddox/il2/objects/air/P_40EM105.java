package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_40EM105 extends P_40 {

    public P_40EM105() {
    }

    static {
        Class class1 = P_40EM105.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-40");
        Property.set(class1, "meshName", "3DO/Plane/P-40E-M-105(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-40E-M-105.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_40E.class });
        Property.set(class1, "LOSElevation", 1.06965F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03",
                "_ExternalRock04", "_ExternalBomb01" });
    }
}
