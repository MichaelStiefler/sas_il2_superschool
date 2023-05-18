package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HurricaneMkIIa extends Hurricane implements TypeFighter {

    public HurricaneMkIIa() {
    }

    static {
        Class class1 = HurricaneMkIIa.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "HurriMkIIa");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIa(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_gb", "3DO/Plane/HurricaneMkIIa(Multi1)/hierRAF.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIa.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRII.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[8]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08" });
    }
}
