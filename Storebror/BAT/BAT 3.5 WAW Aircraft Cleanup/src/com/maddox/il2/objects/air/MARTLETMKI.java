package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MARTLETMKI extends F4F {

    public MARTLETMKI() {
    }

    static {
        Class class1 = MARTLETMKI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4F");
        Property.set(class1, "meshName", "3DO/Plane/MartletMkI/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/MartletMkI.fmd:MARTLET");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMARTLETMKI.class });
        Property.set(class1, "LOSElevation", 1.28265F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
