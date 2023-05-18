package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4U1C extends F4U {

    public F4U1C() {
    }

    static {
        Class class1 = F4U1C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4U");
        Property.set(class1, "meshName", "3DO/Plane/F4U-1C(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F4U-1C(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_rz", "3DO/Plane/F4U-1C(RZ)/hier.him");
        Property.set(class1, "PaintScheme_rz", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F4U-1C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF4U1D.class });
        Property.set(class1, "LOSElevation", 1.0585F);
        weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02",
                "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
