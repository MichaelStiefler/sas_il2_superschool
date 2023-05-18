package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPITFIRE9E extends SPITFIRE9 {

    public SPITFIRE9E() {
    }

    static {
        Class class1 = SPITFIRE9E.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireMkIXe(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/SpitfireMkIXe(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Spitfire-LF-IXe-M66-18.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpit9C.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 3, 3, 9, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev08", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01", "_ExternalBomb01" });
    }
}
