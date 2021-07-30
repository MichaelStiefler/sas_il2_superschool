package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LAGG_3SERIES29 extends LAGG_3 implements TypeTNBFighter {

    public LAGG_3SERIES29() {
    }

    static {
        Class class1 = LAGG_3SERIES29.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "LaGG");
        Property.set(class1, "meshName", "3DO/Plane/LaGG-3series29/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/LaGG-3series29.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLAGG_3SERIES4.class });
        Property.set(class1, "LOSElevation", 0.69445F);
        weaponTriggersRegister(class1, new int[] { 0, 1, 3, 3, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalRock01", "_ExternalDev02", "_ExternalRock02", "_ExternalDev03", "_ExternalRock03", "_ExternalDev04",
                "_ExternalRock04", "_ExternalDev05", "_ExternalRock05", "_ExternalDev06", "_ExternalRock06", "_ExternalDev07", "_ExternalRock07", "_ExternalDev08", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
