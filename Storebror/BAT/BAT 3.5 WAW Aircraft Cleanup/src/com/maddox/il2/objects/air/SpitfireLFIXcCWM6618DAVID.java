package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SpitfireLFIXcCWM6618DAVID extends SPITFIRE9 {

    public SpitfireLFIXcCWM6618DAVID() {
    }

    static {
        Class var_class = SpitfireLFIXcCWM6618DAVID.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "Spit");
        Property.set(var_class, "meshName", "3DO/Plane/SpitfireMkIXcD(Multi1)/hier_cp.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(var_class, "meshName_gb", "3DO/Plane/SpitfireMkIXcD(GB)/hier_cp.him");
        Property.set(var_class, "PaintScheme_gb", new PaintSchemeFMPar04());
        Property.set(var_class, "yearService", 1943F);
        Property.set(var_class, "yearExpired", 1946.5F);
        Property.set(var_class, "FlightModel", "FlightModels/Spitfire-LF-IXc-M66-18-CW.fmd:SPIT9_FM");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitSpit8.class });
        Property.set(var_class, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 9, 3, 3, 9, 3 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev08", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01", "_ExternalBomb01" });
    }
}
